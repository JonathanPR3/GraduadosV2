package com.example.graduados.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.example.graduados.models.Graduado;
import org.springframework.stereotype.Service;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;

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
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] generarInvitacionConQR(Graduado graduado, BufferedImage qrImage) throws Exception {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, baos);

            // Cargar la imagen de fondo
            Image fondo = Image.getInstance("src/main/resources/static/img/fondoinvitacion.png");
            fondo.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());

            // Registrar evento para el fondo
            FondoPdfPageEventHelper eventHelper = new FondoPdfPageEventHelper(fondo);
            writer.setPageEvent(eventHelper);

            document.open();

            // Fuentes personalizadas
            Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Font.BOLD, BaseColor.BLACK);
            Font contenidoFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

            // Espacios en blanco para separar contenido (opcional)
            for (int i = 0; i < 17; i++) {
                document.add(new Paragraph(" "));
            }

            // Título de la invitación
            Paragraph titulo = new Paragraph("Invitación para el Graduado", tituloFont);
            titulo.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(titulo);
            document.add(new Paragraph(" ")); // espacio

            // Tabla con información del graduado
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

            // Insertar imagen QR
            // Convertir BufferedImage a Image de iText
            ByteArrayOutputStream imgBaos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "PNG", imgBaos);
            imgBaos.flush();
            byte[] qrBytes = imgBaos.toByteArray();
            imgBaos.close();

            Image qrItextImage = Image.getInstance(qrBytes);
            qrItextImage.scaleToFit(250, 250);
            qrItextImage.setAbsolutePosition(
                (PageSize.A4.getWidth() - qrItextImage.getScaledWidth()) / 2,
                170 // Ajusta esto si quieres moverlo más arriba o abajo
            );
            document.add(qrItextImage);

            // Salto de página para acompañantes
            document.newPage();
            document.close();

            return baos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


}