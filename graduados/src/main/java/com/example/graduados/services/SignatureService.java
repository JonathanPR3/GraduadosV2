package com.example.graduados.services;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.stereotype.Service;

@Service
public class SignatureService {

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

    
}
