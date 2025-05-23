package com.example.graduados.controllers;

import com.example.graduados.models.admin;
import com.example.graduados.models.Graduado;
import com.example.graduados.repository.AdminRepository;
import com.example.graduados.repository.GraduadoRepository;
import com.example.graduados.services.PdfService;
import com.example.graduados.services.KeyService;
import com.example.graduados.services.QrService;
import com.example.graduados.services.SignatureService;
import com.example.graduados.dto.InvitacionDTO;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.io.ByteArrayInputStream;
import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.awt.image.BufferedImage;



@Controller
public class MainController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private GraduadoRepository graduadoRepository;

    @Autowired
    private PdfService pdfService;
    
    @Autowired
    private QrService qrService;    
    
    @Autowired
    private KeyService keyService;   
    
    @Autowired
    private SignatureService signatureService;
    
    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        // Recuperar el graduado de la sesión (si existe)
        Graduado graduado = (Graduado) session.getAttribute("user");

        // Verificar si el usuario ya contestó el cuestionario
        boolean cuestionarioContestado = false;
        if (graduado != null) {
            cuestionarioContestado = graduado.isAsistencia() || graduado.getAcompanantes() > 0;
        }

        // Pasar la información a la vista
        model.addAttribute("cuestionarioContestado", cuestionarioContestado);
        return "index";
    }

    @GetMapping("/inicio")
    public String inicio(HttpSession session, Model model) {
        // Recuperar el objeto Graduado de la sesión
        Graduado graduado = (Graduado) session.getAttribute("user");

        if (graduado == null) {
            return "redirect:/"; // Redirigir al index si no hay sesión
        }

        // Pasar los datos del graduado al modelo
        model.addAttribute("nombre", graduado.getNombre());
        model.addAttribute("carrera", graduado.getCarrera());
        model.addAttribute("grupo", graduado.getGrupo());
        model.addAttribute("op_Titulacion", graduado.getOpTitulacion());

        // Verificar si el formulario ya fue contestado
        boolean contestado = graduado.isAsistencia() || graduado.getAcompanantes() > 0;
        model.addAttribute("contestado", contestado);

        return "inicio"; // Renderizar la página de inicio
    }

    @GetMapping("/admin")
    public String admin(HttpSession session, Model model) {
        // Recuperar el admin de la sesión
        admin admin = (admin) session.getAttribute("user");

        if (admin == null) {
            return "redirect:/"; // Redirigir al index si no hay sesión
        }

        // Pasar el nombre del admin a la vista
        model.addAttribute("nombreUsuario", admin.getNombre());

        // Obtener la lista de graduados para la tabla
        List<Graduado> graduados = graduadoRepository.findAll();
        model.addAttribute("graduados", graduados);

        return "admin"; // Renderizar la página de admin
    }

    @PostMapping("/actualizar-datos")
    public String actualizarDatos(
        @RequestParam("asistencia") boolean asistencia,
        @RequestParam("acompanantes") int acompanantes,
        HttpSession session
    ) {
        // Recuperar el graduado de la sesión
        Graduado graduado = (Graduado) session.getAttribute("user");
        if (graduado == null) {
            return "redirect:/"; // Redirigir al index si no hay sesión
        }

        // Actualizar los datos del graduado
        graduado.setAsistencia(asistencia);
        graduado.setAcompanantes(acompanantes);

        // Guardar los cambios en la base de datos
        graduadoRepository.save(graduado);

        return "redirect:/inicio"; // Redirigir a la página de inicio
    }

    @GetMapping("/registrar")
    public String registrar() {
        return "registrar"; // Renderiza templates/registrar.html
    }

    @PostMapping("/auth/login")
    public String login(@RequestParam("curp") String curp, Model model, HttpSession session) {
        // Buscar el CURP en la tabla de admins
        admin admin = adminRepository.findByCurp(curp);
        if (admin != null) {
            session.setAttribute("user", admin); // Guardar el admin en la sesión
            return "redirect:/admin"; // Redirigir a la página de admin
        }

        // Buscar el CURP en la tabla de graduados
        Graduado graduado = graduadoRepository.findByCurp(curp);
        if (graduado != null) {
            session.setAttribute("user", graduado); // Guardar el graduado en la sesión
            return "redirect:/inicio"; // Redirigir a la página de inicio
        }

        // Si el CURP no existe en ninguna tabla, mostrar un mensaje de error
        model.addAttribute("error", "CURP no encontrado");
        return "index"; // Volver a la página de inicio con el mensaje de error
    }


    @PostMapping("/cerrar-sesion")
        public String cerrarSesion(HttpSession session) {
            session.invalidate(); // Invalidar la sesión
            return "redirect:/"; // Redirigir al index
        }


        @GetMapping("/generar-pdf")
public ResponseEntity<InputStreamResource> generarPdf(HttpSession session) throws Exception {
    Graduado graduado = (Graduado) session.getAttribute("user");
    if (graduado == null) {
        return ResponseEntity.badRequest().build();
    }

    // Crear DTO con solo datos necesarios para firmar y QR
    InvitacionDTO dto = new InvitacionDTO(
        graduado.getId(),
        graduado.getCurp(),
        graduado.getNombre(),
        graduado.isAsistencia(),
        graduado.getOpTitulacion(),
        graduado.getAsiento(),
        graduado.getAcompanantes(),
        graduado.getCarrera(),
        graduado.getGrupo()
    );

    // Serializar DTO a JSON
    ObjectMapper mapper = new ObjectMapper();
    String datosJson = mapper.writeValueAsString(dto);

    // Cargar clave privada
    PrivateKey clavePrivada = keyService.cargarClavePrivada(graduado.getId());

    // Firmar datos JSON
    String firmaBase64 = signatureService.firmar(datosJson.getBytes(), clavePrivada);

    // Construir JSON final con datos y firma para QR
    ObjectNode qrJson = mapper.createObjectNode();
    qrJson.set("data", mapper.readTree(datosJson));
    qrJson.put("signature", firmaBase64);
    String qrContent = qrJson.toString();

    // Generar QR
    BufferedImage qrImage = qrService.generarQR(qrContent);

    // Generar PDF con QR
    byte[] pdfBytes = pdfService.generarInvitacionConQR(graduado, qrImage);

    // Preparar respuesta HTTP con PDF
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invitacion.pdf");

    return ResponseEntity.ok()
            .headers(headers)
            .contentLength(pdfBytes.length)
            .contentType(MediaType.APPLICATION_PDF)
            .body(new InputStreamResource(new ByteArrayInputStream(pdfBytes)));
}


        
}