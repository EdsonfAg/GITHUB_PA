package Controlador;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

/**
 * Clase para manejar encabezados y pies de página en documentos PDF
 */
public class PDFEventoEncabezado extends PdfPageEventHelper {
    private String tituloReporte;
    private PdfTemplate plantillaNumPagina;
    private Image imagen;
    private Font fuenteEncabezado;
    private Font fuentePie;

    public PDFEventoEncabezado(String tituloReporte) {
        this.tituloReporte = tituloReporte;
        this.fuenteEncabezado = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.GRAY);
        this.fuentePie = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.GRAY);
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        plantillaNumPagina = writer.getDirectContent().createTemplate(30, 16);
        try {
            // Aquí se podría cargar un logo si lo tuviéramos
            // imagen = Image.getInstance("ruta/al/logo.png");
        } catch (Exception e) {
            // Manejo silencioso si no hay logo
        }
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        
        // Rectángulo de color en la parte superior
        cb.setColorFill(new BaseColor(0, 102, 204, 30));
        cb.rectangle(document.left(), document.top() + 10, document.right() - document.left(), 20);
        cb.fill();
        
        // Si hubiera un logo, lo añadiríamos aquí
        if (imagen != null) {
            try {
                imagen.scaleToFit(70, 70);
                imagen.setAbsolutePosition(document.left(), document.top() - 60);
                document.add(imagen);
            } catch (Exception e) {
                // Manejo silencioso
            }
        }
        
        // Título del sistema
        try {
            Phrase header = new Phrase("Sistema de Gestión de Ventas", fuenteEncabezado);
            ColumnText.showTextAligned(
                cb, Element.ALIGN_LEFT, 
                header, 
                document.left(), document.top() + 20, 0
            );
        } catch (Exception e) {
            // Manejo silencioso
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        
        // Línea al pie de página
        cb.setLineWidth(0.5f);
        cb.setColorStroke(BaseColor.LIGHT_GRAY);
        cb.moveTo(document.left(), document.bottom() - 15);
        cb.lineTo(document.right(), document.bottom() - 15);
        cb.stroke();
        
        // Información de pie de página
        Phrase footer = new Phrase("Página " + writer.getPageNumber(), fuentePie);
        ColumnText.showTextAligned(
            cb, Element.ALIGN_CENTER,
            footer,
            (document.left() + document.right()) / 2, document.bottom() - 25, 0
        );
        
        // Fecha en el pie de página
        Phrase fechaFrase = new Phrase(
            "Generado: " + new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date()), 
            fuentePie
        );
        ColumnText.showTextAligned(
            cb, Element.ALIGN_RIGHT,
            fechaFrase,
            document.right(), document.bottom() - 25, 0
        );
    }

    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
    }
}
