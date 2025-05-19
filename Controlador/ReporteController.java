package Controlador;

import Modelo.Venta;
import Persistencia.VentaDAO;
import Utils.VentaFiltroHelper;
import Vista.ReportePanel;

import javax.swing.*;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import Modelo.MetodoPago;

/**
 * Controlador para generar reportes de ventas en distintos formatos.
 */
public class ReporteController {
    private final ReportePanel view;
    private final List<Venta> ventas;

    public ReporteController(ReportePanel view) {
        this.view = view;
        this.ventas = VentaDAO.listarTodas();
        attachListeners();
    }

    private void attachListeners() {
        view.getBtnGenerarPDF().addActionListener(e -> generarReporte("pdf"));
        view.getBtnGenerarJSON().addActionListener(e -> generarReporte("json"));
    }

    private void generarReporte(String formato) {
        Date desde = view.getDateDesde().getDate();
        Date hasta = view.getDateHasta().getDate();

        if (desde == null || hasta == null) {
            JOptionPane.showMessageDialog(view, "Selecciona ambas fechas.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Venta> filtradas = VentaFiltroHelper.filtrarPorRango(ventas, desde, hasta);
        if (filtradas.isEmpty()) {
            JOptionPane.showMessageDialog(view, "No hay ventas en ese rango.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String tipo = (String) view.getComboTipoReporte().getSelectedItem();
        String contenido;
        switch (tipo) {
            case "Productos más vendidos":
                contenido = generarProductosMasVendidos(filtradas);
                break;
            case "Ventas por vendedor":
                contenido = generarVentasPorVendedor(filtradas);
                break;
            case "Ventas por método de pago":
                contenido = generarVentasPorMetodoPago(filtradas);
                break;
            default:
                contenido = "";
        }

        exportar(formato, tipo, contenido);
    }

    private String generarProductosMasVendidos(List<Venta> ventas) {
        Map<String, Integer> productos = new HashMap<>();
        for (Venta v : ventas) {
            v.getItems().forEach(i -> {
                String nombre = i.getProducto().getNombre();
                productos.put(nombre, productos.getOrDefault(nombre, 0) + i.getCantidad());
            });
        }
        return productos.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .map(e -> e.getKey() + ": " + e.getValue() + " unidades")
            .collect(Collectors.joining("\n"));
    }

    private String generarVentasPorVendedor(List<Venta> ventas) {
        Map<String, Integer> vendedores = new HashMap<>();
        for (Venta v : ventas) {
            String nombre = v.getUsuarioVendedor().getNombre();
            vendedores.put(nombre, vendedores.getOrDefault(nombre, 0) + 1);
        }
        return vendedores.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .map(e -> e.getKey() + ": " + e.getValue() + " ventas")
            .collect(Collectors.joining("\n"));
    }

    private String generarVentasPorMetodoPago(List<Venta> ventas) {
        Map<MetodoPago, Double> pagos = new java.util.EnumMap<>(MetodoPago.class);
        for (Venta v : ventas) {
            pagos.put(v.getMetodoPago(), pagos.getOrDefault(v.getMetodoPago(), 0.0) + v.calcularTotal());
        }
        return pagos.entrySet().stream()
            .map(e -> e.getKey() + ": $" + String.format("%.2f", e.getValue()))
            .collect(Collectors.joining("\n"));
    }

    private void exportar(String formato, String tipo, String contenido) {
        String nombreArchivo = "reporte_" + tipo.replace(" ", "_") + "." + formato;
        try {
            if ("pdf".equalsIgnoreCase(formato)) {
                // 1) Generar un PDF con formato mejorado
                generarPDFMejorado(nombreArchivo, tipo, contenido);
            } else {
                // 2) Texto plano (txt, csv, etc.)
                try (BufferedWriter out = new BufferedWriter(new FileWriter(nombreArchivo))) {
                    out.write(contenido);
                }
            }
    
            // 3) Abrir automáticamente el archivo recién creado
            Desktop.getDesktop().open(new File(nombreArchivo));
    
            JOptionPane.showMessageDialog(
                view,
                "Reporte generado: " + nombreArchivo,
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                view,
                "Error al exportar: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    /**
     * Genera un PDF con formato mejorado según el tipo de reporte
     */
    private void generarPDFMejorado(String nombreArchivo, String tipo, String contenido) throws Exception {
        // Configuración del documento
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(nombreArchivo));
        
        // Añadir eventos para encabezados y pies de página
        PDFEventoEncabezado evento = new PDFEventoEncabezado(tipo);
        writer.setPageEvent(evento);
        
        document.open();
        
        // Añadir título
        Paragraph titulo = new Paragraph(tipo, getFuenteTitulo());
        titulo.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        document.add(titulo);
        
        // Añadir fecha del reporte
        Paragraph fechaReporte = new Paragraph(
            "Reporte generado el " + new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()),
            getFuenteNormal()
        );
        fechaReporte.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
        fechaReporte.setSpacingAfter(15);
        document.add(fechaReporte);
        
        // Añadir información de filtros
        Paragraph filtros = new Paragraph(
            "Rango de fechas: " + 
            new java.text.SimpleDateFormat("dd/MM/yyyy").format(view.getDateDesde().getDate()) + 
            " a " + 
            new java.text.SimpleDateFormat("dd/MM/yyyy").format(view.getDateHasta().getDate()),
            getFuenteNormal()
        );
        filtros.setSpacingAfter(20);
        document.add(filtros);
        
        // Añadir línea separadora
        document.add(crearLineaSeparadora());
        
        // Añadir contenido según el tipo de reporte
        switch (tipo) {
            case "Productos más vendidos":
                generarTablaProductos(document, contenido);
                break;
            case "Ventas por vendedor":
                generarTablaVendedores(document, contenido);
                break;
            case "Ventas por método de pago":
                generarTablaMetodosPago(document, contenido);
                break;
            default:
                document.add(new Paragraph(contenido, getFuenteNormal()));
        }
        
        // Añadir nota de pie
        Paragraph nota = new Paragraph("Este es un reporte generado automáticamente por el sistema.", getFuentePie());
        nota.setSpacingBefore(20);
        nota.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        document.add(nota);
        
        document.close();
    }
    
    /**
     * Genera una tabla para el reporte de productos más vendidos
     */
    private void generarTablaProductos(Document document, String contenido) throws Exception {
        // Crear tabla con 2 columnas
        com.itextpdf.text.pdf.PdfPTable tabla = new com.itextpdf.text.pdf.PdfPTable(2);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{7, 3});
        
        // Cabecera de tabla
        com.itextpdf.text.pdf.PdfPCell celdaProducto = new com.itextpdf.text.pdf.PdfPCell(new Phrase("Producto", getFuenteNegrita()));
        com.itextpdf.text.pdf.PdfPCell celdaCantidad = new com.itextpdf.text.pdf.PdfPCell(new Phrase("Cantidad", getFuenteNegrita()));
        
        celdaProducto.setBackgroundColor(new com.itextpdf.text.BaseColor(220, 220, 220));
        celdaCantidad.setBackgroundColor(new com.itextpdf.text.BaseColor(220, 220, 220));
        
        celdaProducto.setPadding(5);
        celdaCantidad.setPadding(5);
        
        tabla.addCell(celdaProducto);
        tabla.addCell(celdaCantidad);
        
        // Contenido de la tabla
        String[] lineas = contenido.split("\\r?\\n");
        int contador = 0;
        for (String linea : lineas) {
            String[] partes = linea.split(": ");
            if (partes.length == 2) {
                com.itextpdf.text.pdf.PdfPCell celda1 = new com.itextpdf.text.pdf.PdfPCell(new Phrase(partes[0], getFuenteNormal()));
                com.itextpdf.text.pdf.PdfPCell celda2 = new com.itextpdf.text.pdf.PdfPCell(new Phrase(partes[1], getFuenteNormal()));
                
                // Alternar colores de fondo
                if (contador % 2 == 1) {
                    celda1.setBackgroundColor(new com.itextpdf.text.BaseColor(240, 240, 240));
                    celda2.setBackgroundColor(new com.itextpdf.text.BaseColor(240, 240, 240));
                }
                
                celda1.setPadding(4);
                celda2.setPadding(4);
                
                tabla.addCell(celda1);
                tabla.addCell(celda2);
                contador++;
            }
        }
        
        document.add(tabla);
    }
    
    /**
     * Genera una tabla para el reporte de ventas por vendedor
     */
    private void generarTablaVendedores(Document document, String contenido) throws Exception {
        // Crear tabla con 2 columnas
        com.itextpdf.text.pdf.PdfPTable tabla = new com.itextpdf.text.pdf.PdfPTable(2);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{7, 3});
        
        // Cabecera de tabla
        com.itextpdf.text.pdf.PdfPCell celdaVendedor = new com.itextpdf.text.pdf.PdfPCell(new Phrase("Vendedor", getFuenteNegrita()));
        com.itextpdf.text.pdf.PdfPCell celdaVentas = new com.itextpdf.text.pdf.PdfPCell(new Phrase("Cantidad de Ventas", getFuenteNegrita()));
        
        celdaVendedor.setBackgroundColor(new com.itextpdf.text.BaseColor(220, 220, 220));
        celdaVentas.setBackgroundColor(new com.itextpdf.text.BaseColor(220, 220, 220));
        
        celdaVendedor.setPadding(5);
        celdaVentas.setPadding(5);
        
        tabla.addCell(celdaVendedor);
        tabla.addCell(celdaVentas);
        
        // Contenido de la tabla
        String[] lineas = contenido.split("\\r?\\n");
        int contador = 0;
        for (String linea : lineas) {
            String[] partes = linea.split(": ");
            if (partes.length == 2) {
                com.itextpdf.text.pdf.PdfPCell celda1 = new com.itextpdf.text.pdf.PdfPCell(new Phrase(partes[0], getFuenteNormal()));
                com.itextpdf.text.pdf.PdfPCell celda2 = new com.itextpdf.text.pdf.PdfPCell(new Phrase(partes[1], getFuenteNormal()));
                
                if (contador % 2 == 1) {
                    celda1.setBackgroundColor(new com.itextpdf.text.BaseColor(240, 240, 240));
                    celda2.setBackgroundColor(new com.itextpdf.text.BaseColor(240, 240, 240));
                }
                
                celda1.setPadding(4);
                celda2.setPadding(4);
                
                tabla.addCell(celda1);
                tabla.addCell(celda2);
                contador++;
            }
        }
        
        document.add(tabla);
    }
    
    /**
     * Genera una tabla para el reporte de ventas por método de pago
     */
    private void generarTablaMetodosPago(Document document, String contenido) throws Exception {
        // Crear tabla con 2 columnas
        com.itextpdf.text.pdf.PdfPTable tabla = new com.itextpdf.text.pdf.PdfPTable(2);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{6, 4});
        
        // Cabecera de tabla
        com.itextpdf.text.pdf.PdfPCell celdaMetodo = new com.itextpdf.text.pdf.PdfPCell(new Phrase("Método de Pago", getFuenteNegrita()));
        com.itextpdf.text.pdf.PdfPCell celdaMonto = new com.itextpdf.text.pdf.PdfPCell(new Phrase("Monto Total", getFuenteNegrita()));
        
        celdaMetodo.setBackgroundColor(new com.itextpdf.text.BaseColor(220, 220, 220));
        celdaMonto.setBackgroundColor(new com.itextpdf.text.BaseColor(220, 220, 220));
        
        celdaMetodo.setPadding(5);
        celdaMonto.setPadding(5);
        
        tabla.addCell(celdaMetodo);
        tabla.addCell(celdaMonto);
        
        // Contenido de la tabla
        String[] lineas = contenido.split("\\r?\\n");
        int contador = 0;
        for (String linea : lineas) {
            String[] partes = linea.split(": ");
            if (partes.length == 2) {
                com.itextpdf.text.pdf.PdfPCell celda1 = new com.itextpdf.text.pdf.PdfPCell(new Phrase(partes[0], getFuenteNormal()));
                com.itextpdf.text.pdf.PdfPCell celda2 = new com.itextpdf.text.pdf.PdfPCell(new Phrase(partes[1], getFuenteNormal()));
                
                if (contador % 2 == 1) {
                    celda1.setBackgroundColor(new com.itextpdf.text.BaseColor(240, 240, 240));
                    celda2.setBackgroundColor(new com.itextpdf.text.BaseColor(240, 240, 240));
                }
                
                celda1.setPadding(4);
                celda2.setPadding(4);
                celda2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
                
                tabla.addCell(celda1);
                tabla.addCell(celda2);
                contador++;
            }
        }
        
        document.add(tabla);
    }
    
    /**
     * Retorna una línea separadora para el documento PDF
     */
    private com.itextpdf.text.pdf.draw.LineSeparator crearLineaSeparadora() {
        com.itextpdf.text.pdf.draw.LineSeparator linea = new com.itextpdf.text.pdf.draw.LineSeparator(1, 100, null, com.itextpdf.text.Element.ALIGN_CENTER, -5);
        linea.setLineColor(new com.itextpdf.text.BaseColor(0, 123, 255));
        return linea;
    }
    
    /**
     * Retorna la fuente para títulos
     */
    private com.itextpdf.text.Font getFuenteTitulo() {
        return new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD, new com.itextpdf.text.BaseColor(0, 51, 102));
    }
    
    /**
     * Retorna la fuente para texto normal
     */
    private com.itextpdf.text.Font getFuenteNormal() {
        return new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.NORMAL);
    }
    
    /**
     * Retorna la fuente para texto en negrita
     */
    private com.itextpdf.text.Font getFuenteNegrita() {
        return new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 11, com.itextpdf.text.Font.BOLD, new com.itextpdf.text.BaseColor(0, 51, 102));
    }
    
    /**
     * Retorna la fuente para notas de pie
     */
    private com.itextpdf.text.Font getFuentePie() {
        return new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 8, com.itextpdf.text.Font.ITALIC, com.itextpdf.text.BaseColor.GRAY);
    }
}
