package com.example.graduados.services;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfContentByte;

public class FondoPdfPageEventHelper extends PdfPageEventHelper {
    private Image fondo;

    public FondoPdfPageEventHelper(Image fondo) {
        this.fondo = fondo;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            // Agregar el fondo en cada página
            PdfContentByte canvas = writer.getDirectContentUnder();
            fondo.setAbsolutePosition(0, 0);
            fondo.scaleToFit(document.getPageSize().getWidth(), document.getPageSize().getHeight());
            canvas.addImage(fondo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}