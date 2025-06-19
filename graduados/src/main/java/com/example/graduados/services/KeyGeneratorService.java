package com.example.graduados.services;

import com.example.graduados.models.Graduado;
import com.example.graduados.repository.GraduadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class KeyGeneratorService {

    private final String KEY_FOLDER = "secure_keys/";

    @Autowired
    private GraduadoRepository graduadoRepository;

    public KeyPair generateAndSaveKeys(int graduadoId) throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
        keyGen.initialize(new ECGenParameterSpec("secp256r1"));
        KeyPair keyPair = keyGen.generateKeyPair();

        // Guardar clave privada
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
        String privateKeyBase64 = Base64.getEncoder().encodeToString(privateKeyBytes);
        Files.createDirectories(Paths.get(KEY_FOLDER));
        Files.write(Paths.get(KEY_FOLDER + "privada_graduado_" + graduadoId + ".key"), privateKeyBase64.getBytes());

        // Guardar clave pública
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKeyBytes);
        Files.write(Paths.get(KEY_FOLDER + "publica_graduado_" + graduadoId + ".key"), publicKeyBase64.getBytes());

        // Guardar clave pública en base de datos
        Graduado graduado = graduadoRepository.findById(graduadoId)
            .orElseThrow(() -> new Exception("Graduado no encontrado con id " + graduadoId));
        graduado.setClavePublicaEcdsa(publicKeyBase64);
        graduadoRepository.save(graduado);

        return keyPair;
    }

    // KeyGeneratorService.java - Añadir este método
    public String generateAesKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[32]; // 256 bits
        secureRandom.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }


}
