package Modelo;

import java.util.Date;
import java.util.List;

/**
 * Representa una devolución de venta.
 */
public class Devolucion {
    private int id;
    private int idVenta;
    private Date fecha;
    private Usuario usuario;    // quien procesa la devolución
    private String motivo;
    private double total;
    private List<LineaDevolucion> items;

    /**
     * Constructor usado al recuperar desde la base de datos.
     */
    public Devolucion(int id, int idVenta, Date fecha, Usuario usuario, String motivo, double total, List<LineaDevolucion> items) {
        this.id = id;
        this.idVenta = idVenta;
        this.fecha = fecha;
        this.usuario = usuario;
        this.motivo = motivo;
        this.total = total;
        this.items = items;
    }

    /**
     * Constructor para crear una nueva devolución antes de persistirla (id se asigna en BD).
     */
    public Devolucion(int idVenta, Usuario usuario, String motivo, List<LineaDevolucion> items) {
        this(0, idVenta, new Date(), usuario, motivo, calcularTotal(items), items);
    }

    /**
     * Calcula el total sumando precioUnitario * cantidad de cada línea.
     */
    private static double calcularTotal(List<LineaDevolucion> items) {
        return items.stream()
                    .mapToDouble(l -> l.getPrecioUnitario() * l.getCantidad())
                    .sum();
    }

    // Getters y setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<LineaDevolucion> getItems() {
        return items;
    }

    public void setItems(List<LineaDevolucion> items) {
        this.items = items;
    }
}
