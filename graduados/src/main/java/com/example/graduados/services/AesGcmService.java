package com.example.graduados.services;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class AesGcmService {
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128;
    private static final int IV_LENGTH_BYTE = 12;

    public String encrypt(String plaintext, String base64Key) throws Exception {
        byte[] key = Base64.getDecoder().decode(base64Key);
        byte[] iv = new byte[IV_LENGTH_BYTE];
        new SecureRandom().nextBytes(iv);
        
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
        
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);
        byte[] cipherText = cipher.doFinal(plaintext.getBytes());
        
        byte[] encryptedData = ByteBuffer.allocate(iv.length + cipherText.length)
                .put(iv)
                .put(cipherText)
                .array();
        
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    public String decrypt(String encryptedData, String base64Key) throws Exception {
        byte[] key = Base64.getDecoder().decode(base64Key);
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        
        ByteBuffer bb = ByteBuffer.wrap(decodedData);
        byte[] iv = new byte[IV_LENGTH_BYTE];
        bb.get(iv);
        
        byte[] cipherText = new byte[bb.remaining()];
        bb.get(cipherText);
        
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
        
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);
        byte[] plainText = cipher.doFinal(cipherText);
        
        return new String(plainText);
    }
}