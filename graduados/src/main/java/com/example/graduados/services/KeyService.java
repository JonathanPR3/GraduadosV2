package com.example.graduados.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import com.example.graduados.services.KeyGeneratorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

    @Service
    public class KeyService {

        private final String KEYS_FOLDER = "secure_keys/"; // o ruta configurable

        @Autowired
        private KeyGeneratorService keyGeneratorService;

        public PrivateKey cargarClavePrivada(int graduadoId) throws Exception {
            String filePath = KEYS_FOLDER + "privada_graduado_" + graduadoId + ".key";
            Path path = Paths.get(filePath);

            if (!Files.exists(path)) {
                // Generar y guardar claves porque no existe archivo
                keyGeneratorService.generateAndSaveKeys(graduadoId);
            }

            // Leer archivo privado
            byte[] keyBytesBase64 = Files.readAllBytes(path);
            byte[] keyBytes = Base64.getDecoder().decode(new String(keyBytesBase64));

            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            return keyFactory.generatePrivate(keySpec);
        }
    }

