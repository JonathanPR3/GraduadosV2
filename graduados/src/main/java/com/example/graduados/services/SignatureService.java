package com.example.graduados.services;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignatureService {

    @Autowired
    private AesGcmService aesGcmService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String firmar(byte[] datos, PrivateKey clavePrivada) throws Exception {
        Signature signature = Signature.getInstance("SHA256withECDSA");
        signature.initSign(clavePrivada);
        signature.update(datos);
        byte[] firmaBytes = signature.sign();
        return Base64.getEncoder().encodeToString(firmaBytes);
    }

    public boolean verificarFirma(byte[] datos, String firmaBase64, String clavePublicaBase64) throws Exception {
        byte[] firmaBytes = Base64.getDecoder().decode(firmaBase64);
        byte[] clavePublicaBytes = Base64.getDecoder().decode(clavePublicaBase64);

        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(clavePublicaBytes);
        PublicKey clavePublica = keyFactory.generatePublic(keySpec);

        Signature signature = Signature.getInstance("SHA256withECDSA");
        signature.initVerify(clavePublica);
        signature.update(datos);

        return signature.verify(firmaBytes);
    }

    public String generarDatosFirmadosYCifrados(String datos, PrivateKey clavePrivada, String aesKey) throws Exception {
        // 1. Firmar los datos originales
        String firma = firmar(datos.getBytes("UTF-8"), clavePrivada);
        
        // 2. Crear objeto con datos y firma
        DatosFirmados datosFirmados = new DatosFirmados(
            Base64.getEncoder().encodeToString(datos.getBytes("UTF-8")),
            firma
        );
        
        // 3. Convertir a JSON
        String jsonFirmado = objectMapper.writeValueAsString(datosFirmados);
        
        // 4. Cifrar el JSON firmado
        return aesGcmService.encrypt(jsonFirmado, aesKey);
    }

    public boolean validarDatosCifrados(String datosCifrados, String clavePublicaBase64, String aesKey) throws Exception {
        // 1. Descifrar los datos
        String jsonFirmado = aesGcmService.decrypt(datosCifrados, aesKey);
        
        // 2. Parsear JSON
        DatosFirmados datosFirmados = objectMapper.readValue(jsonFirmado, DatosFirmados.class);
        
        // 3. Decodificar datos originales
        byte[] datosBytes = Base64.getDecoder().decode(datosFirmados.getDatos());
        
        // 4. Verificar firma
        return verificarFirma(datosBytes, datosFirmados.getFirma(), clavePublicaBase64);
    }

    // Clase interna para representar los datos firmados
    private static class DatosFirmados {
        private String datos;
        private String firma;

        public DatosFirmados() {} // Constructor vacío para Jackson

        public DatosFirmados(String datos, String firma) {
            this.datos = datos;
            this.firma = firma;
        }

        // Getters y setters
        public String getDatos() { return datos; }
        public void setDatos(String datos) { this.datos = datos; }
        public String getFirma() { return firma; }
        public void setFirma(String firma) { this.firma = firma; }
    }
}