package Modelo;

import com.google.gson.Gson;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Representa una venta con items, impuestos y método de pago.
 */
public class Venta implements ImpuestoAplicable {
    private final String id;
    private Date fecha;
    private final Usuario usuarioVendedor;
    private MetodoPago metodoPago;
    private List<LineaVenta> items;

    /**
     * Constructor de conveniencia para una venta nueva. Genera id y fecha automática.
     */
    public Venta(Usuario usuarioVendedor) {
        this.id = UUID.randomUUID().toString();
        this.fecha = new Date();
        this.usuarioVendedor = usuarioVendedor;
        this.items = new ArrayList<>();
    }

    /**
     * Constructor para crear venta con ID personalizado y sin fecha explícita.
     */
    public Venta(String id, Usuario usuarioVendedor, MetodoPago metodoPago) {
        this.id = id;
        this.fecha = new Date();
        this.usuarioVendedor = usuarioVendedor;
        this.metodoPago = metodoPago;
        this.items = new ArrayList<>();
    }

    /**
     * Constructor completo, por ejemplo al cargar desde BD.
     */
    public Venta(String id, Date fecha, Usuario usuarioVendedor, MetodoPago metodoPago) {
        this.id = id;
        this.fecha = fecha;
        this.usuarioVendedor = usuarioVendedor;
        this.metodoPago = metodoPago;
        this.items = new ArrayList<>();
    }

    // Agrega o actualiza cantidad de un producto
    public void agregarProducto(Producto producto, int cantidad) {
        for (LineaVenta item : items) {
            if (item.getProducto().getCodigo().equals(producto.getCodigo())) {
                item.incrementarCantidad(cantidad);
                return;
            }
        }
        items.add(new LineaVenta(producto, cantidad));
    }

    // Elimina producto por código
    public void eliminarProducto(String codigoProducto) {
        items.removeIf(item -> item.getProducto().getCodigo().equals(codigoProducto));
    }

    public double calcularSubtotal() {
        return items.stream()
                .mapToDouble(item -> item.getProducto().getPrecio() * item.getCantidad())
                .sum();
    }

    @Override
    public double calcularIVA() {
        return calcularSubtotal() * 0.16;
    }

    @Override
    public double calcularIEPS() {
        return calcularSubtotal() * 0.08;
    }

    public double calcularTotal() {
        return calcularSubtotal() + calcularIVA() + calcularIEPS();
    }

    /**
     * Setter de método de pago.
     */
    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    // Getters básicos
    public String getId() {
        return id;
    }

    public Date getFecha() {
        return fecha;
    }

    public Usuario getUsuarioVendedor() {
        return usuarioVendedor;
    }

    public List<LineaVenta> getItems() {
        return items;
    }

    /**
     * Devuelve el ID de la venta como entero. Útil para BD.
     */
    public int getIdAsInt() {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("ID de venta no es un entero: " + id, e);
        }
    }

    /**
     * Exporta la venta a PDF.
     */
    public void exportarPDF(String ruta) throws Exception {
        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream(ruta));
        doc.open();
        doc.add(new Paragraph("Venta ID: " + id));
        doc.add(new Paragraph("Fecha: " + fecha));
        doc.add(new Paragraph("Vendedor: " + usuarioVendedor.getNombre()));
        doc.add(new Paragraph("Método de Pago: " + metodoPago));
        doc.add(new Paragraph("--------------------------------------------------"));
        for (LineaVenta item : items) {
            doc.add(new Paragraph(
                item.getProducto().getNombre() + " x" + item.getCantidad() +
                " = $" + (item.getProducto().getPrecio() * item.getCantidad())
            ));
        }
        doc.add(new Paragraph("Subtotal: $" + calcularSubtotal()));
        doc.add(new Paragraph("IVA: $" + calcularIVA()));
        doc.add(new Paragraph("IEPS: $" + calcularIEPS()));
        doc.add(new Paragraph("Total: $" + calcularTotal()));
        doc.close();
    }

    /**
     * Exporta la venta a JSON.
     */
    public void exportarJSON(String ruta) throws IOException {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(ruta)) {
            gson.toJson(this, writer);
        }
    }

    @Override
    public String toString() {
        return "Venta #" + id + " (" + fecha + ")";
    }
}
