package com.example.graduados.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import java.awt.image.BufferedImage;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class QrService {

    private final AesGcmService aesGcmService;
    private static final int DEFAULT_SIZE = 300;

    @Autowired
    public QrService(AesGcmService aesGcmService) {
        this.aesGcmService = aesGcmService;
    }

    public BufferedImage generarQR(String texto, boolean cifrar, String aesKey) throws WriterException, SecurityException {
        validateParameters(texto, cifrar, aesKey);
        
        try {
            String contenidoFinal = cifrar ? 
                aesGcmService.encrypt(texto, aesKey) : 
                texto;

            return generateQRImage(contenidoFinal, DEFAULT_SIZE, DEFAULT_SIZE);
        } catch (Exception e) {
            throw new SecurityException("Error al cifrar contenido para QR", e);
        }
    }

    public BufferedImage generarQR(String texto) throws WriterException {
        return generateQRImage(texto, DEFAULT_SIZE, DEFAULT_SIZE);
    }

    private BufferedImage generateQRImage(String content, int width, int height) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    private void validateParameters(String texto, boolean cifrar, String aesKey) {
        if (texto == null || texto.isEmpty()) {
            throw new IllegalArgumentException("El texto no puede estar vacío");
        }
        if (cifrar && (aesKey == null || aesKey.isEmpty())) {
            throw new IllegalArgumentException("Se requiere clave AES para cifrado");
        }
    }
}