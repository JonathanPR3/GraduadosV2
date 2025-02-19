package com.example.graduados.services;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.example.graduados.models.Graduado;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class PdfService {

    public void generarInvitaciones(Graduado graduado, int numAcompanantes) {
        Document document = new Document();

        try {
            // Crear el archivo PDF
            PdfWriter.getInstance(document, new FileOutputStream("invitaciones.pdf"));
            document.open();

            // Invitación del graduado
            document.add(new Paragraph("Invitación para el Graduado"));
            document.add(new Paragraph("Nombre: " + graduado.getNombre()));
            document.add(new Paragraph("Carrera: " + graduado.getCarrera()));
            document.add(new Paragraph("Grupo: " + graduado.getGrupo()));
            document.add(new Paragraph("Opción de Titulación: " + graduado.getOpTitulacion()));
            document.add(new Paragraph("----------------------------------------"));

            // Salto de página para la siguiente invitación
            document.newPage();

            // Invitaciones de los acompañantes
            for (int i = 1; i <= numAcompanantes; i++) {
                document.add(new Paragraph("Invitación para Acompañante " + i));
                document.add(new Paragraph("Este es un acompañante del graduado: " + graduado.getNombre()));
                document.add(new Paragraph("----------------------------------------"));

                // Salto de página para la siguiente invitación (excepto la última)
                if (i < numAcompanantes) {
                    document.newPage();
                }
            }

            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}