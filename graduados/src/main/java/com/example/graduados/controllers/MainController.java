package com.example.graduados.controllers;

import com.example.graduados.models.admin;
import com.example.graduados.models.Graduado;
import com.example.graduados.repository.AdminRepository;
import com.example.graduados.repository.GraduadoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private GraduadoRepository graduadoRepository;

    @GetMapping("/")
    public String index() {
        return "index"; // Renderiza templates/index.html
    }

    @GetMapping("/inicio")
    public String inicio(HttpSession session, Model model) {
        // Recuperar el objeto Graduado de la sesión
        Graduado graduado = (Graduado) session.getAttribute("user");
    
        if (graduado == null) {
            // Si no hay sesión, redirigir al index
            return "redirect:/";
        }
    
        // Pasar los datos del graduado al modelo
        model.addAttribute("nombre", graduado.getNombre());
        model.addAttribute("carrera", graduado.getCarrera());
        model.addAttribute("grupo", graduado.getGrupo());
        model.addAttribute("op_Titulacion", graduado.getOpTitulacion());
    
        return "inicio"; // Renderizar la página de inicio
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin"; // Renderiza templates/admin.html
    }

    @GetMapping("/actualizar_datos")
    public String actualizar_datos() {
        return "actualizar_datos"; // Renderiza templates/actualizar_datos.html
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
}