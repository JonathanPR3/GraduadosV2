package com.example.graduados.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.example.graduados.models.Graduado;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class PdfService {

    /**
     * @param graduado
     * @param numAcompanantes
     */
    public void generarInvitaciones(Graduado graduado, int numAcompanantes) {
        Document document = new Document();

        try {
            // Crear el archivo PDF
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("invitaciones.pdf"));

            // Cargar la imagen de fondo
            Image fondo = Image.getInstance("src/main/resources/static/img/fondoinvitacion.png");
            fondo.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());

            // Registrar el evento para agregar el fondo en cada página
            FondoPdfPageEventHelper eventHelper = new FondoPdfPageEventHelper(fondo);
            writer.setPageEvent(eventHelper);

            document.open();

            // Fuentes personalizadas
            Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Font.BOLD, BaseColor.BLACK); // Cambiado a negro
            Font contenidoFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK); // Cambiado a negro

            // Agregar espacio en blanco para mover el contenido más abajo
            for (int i = 0; i < 17; i++) {
                document.add(new Paragraph(" ")); // Agrega espacios en blanco
            }

            // Título de la invitación
            Paragraph titulo = new Paragraph("Invitación para el Graduado", tituloFont);
            titulo.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(titulo);

            Paragraph space= new Paragraph(" ");
            document.add(space);



            // Tabla con la información del graduado
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);

            table.addCell(new PdfPCell(new Paragraph("Nombre", contenidoFont)));
            table.addCell(new PdfPCell(new Paragraph(graduado.getNombre(), contenidoFont)));

            table.addCell(new PdfPCell(new Paragraph("Carrera", contenidoFont)));
            table.addCell(new PdfPCell(new Paragraph(graduado.getCarrera(), contenidoFont)));

            table.addCell(new PdfPCell(new Paragraph("Grupo", contenidoFont)));
            table.addCell(new PdfPCell(new Paragraph(graduado.getGrupo(), contenidoFont)));

            table.addCell(new PdfPCell(new Paragraph("Opción de Titulación", contenidoFont)));
            table.addCell(new PdfPCell(new Paragraph(graduado.getOpTitulacion(), contenidoFont)));

            document.add(table);

            // Salto de página para la siguiente invitación
            document.newPage();

            // Invitaciones de los acompañantes
            for (int i = 1; i <= numAcompanantes; i++) {
                // Agregar espacio en blanco para mover el contenido más abajo
                for (int j = 0; j < 17; j++) {
                    document.add(new Paragraph(" ")); // Agrega espacios en blanco
                }

                Paragraph tituloAcompanante = new Paragraph("Invitación para Acompañante " + i, tituloFont);
                tituloAcompanante.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(tituloAcompanante);
                document.add(space);


                Paragraph infoAcompanante = new Paragraph("Este es un acompañante del graduado: " + graduado.getNombre(), contenidoFont);
                document.add(infoAcompanante);

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