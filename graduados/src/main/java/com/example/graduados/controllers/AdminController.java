package com.example.graduados.controllers;

import com.example.graduados.models.Graduado;
import com.example.graduados.models.admin; // Asegúrate de importar tu modelo admin
import com.example.graduados.repository.GraduadoRepository;
import com.example.graduados.repository.AdminRepository; // Necesitarás este repositorio
import com.example.graduados.services.SignatureService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private GraduadoRepository graduadoRepository;
    
    @Autowired
    private AdminRepository adminRepository; // Inyecta el repositorio de admin

    @Autowired
    private SignatureService signatureService;

    @PostMapping("/validar-firma")
    public ResponseEntity<Map<String, Object>> validarFirma(
            @RequestBody String qrJson,
            HttpSession session) {
        
        Map<String, Object> response = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        // 1. Verificar sesión de administrador
        admin admin = (admin) session.getAttribute("user");
        if (admin == null) {
            response.put("mensaje", "Acceso denegado. Debe iniciar sesión como administrador.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            // 2. Procesar la validación (tu lógica existente)
            JsonNode root = mapper.readTree(qrJson);
            JsonNode dataNode = root.get("data");
            String signatureBase64 = root.get("signature").asText();

            int idGraduado = dataNode.get("id").asInt();
            Graduado graduado = graduadoRepository.findById(idGraduado)
                .orElseThrow(() -> new Exception("Graduado no encontrado"));

            String clavePublicaBase64 = graduado.getClavePublicaEcdsa();
            if (clavePublicaBase64 == null) {
                response.put("valido", false);
                response.put("mensaje", "Clave pública no disponible.");
                return ResponseEntity.ok(response);
            }

            String datosJson = mapper.writeValueAsString(dataNode);
            boolean valido = signatureService.verificarFirma(
                datosJson.getBytes(), 
                signatureBase64, 
                clavePublicaBase64
            );

            response.put("valido", valido);

            if (valido) {
                if (!graduado.isAsistencia()) {
                    graduado.setAsistencia(true);
                    graduadoRepository.save(graduado);
                }
                response.put("mensaje", "Firma válida. Invitación auténtica y asistencia registrada.");
            } else {
                response.put("mensaje", "Firma inválida.");
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("valido", false);
            response.put("mensaje", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}