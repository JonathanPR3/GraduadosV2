package com.example.graduados.controllers;

import com.example.graduados.models.Graduado;
import com.example.graduados.repository.GraduadoRepository;
import com.example.graduados.services.SignatureService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private GraduadoRepository graduadoRepository;

    @Autowired
    private SignatureService signatureService;


    @PostMapping("/validar-firma")
    public Map<String, Object> validarFirma(@RequestBody String qrJson) throws Exception {
        Map<String, Object> response = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
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
                return response;
            }

            String datosJson = mapper.writeValueAsString(dataNode);

            boolean valido = signatureService.verificarFirma(datosJson.getBytes(), signatureBase64, clavePublicaBase64);

            response.put("valido", valido);

            if (valido) {
                // Actualizar asistencia y guardar en BD
                if (!graduado.isAsistencia()) {  // Solo actualizar si aún no estaba marcado
                    graduado.setAsistencia(true);
                    graduadoRepository.save(graduado);
                }
                response.put("mensaje", "Firma válida. Invitación auténtica y asistencia registrada.");
            } else {
                response.put("mensaje", "Firma inválida.");
            }

        } catch (Exception e) {
            response.put("valido", false);
            response.put("mensaje", "Error: " + e.getMessage());
        }

        return response;
    }

}
