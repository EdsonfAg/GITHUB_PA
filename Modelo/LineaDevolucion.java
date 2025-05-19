package Modelo;

/**
 * Representa una línea de detalle de una devolución.
 */
public class LineaDevolucion {
    private int id;
    private int idDevolucion;
    private Producto producto;
    private int cantidad;
    private double precioUnitario;

    /**
     * Constructor usado al recuperar desde la base de datos.
     */
    public LineaDevolucion(int id, int idDevolucion, Producto producto, int cantidad, double precioUnitario) {
        this.id = id;
        this.idDevolucion = idDevolucion;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    /**
     * Constructor para crear una nueva línea antes de persistirla
     * (id se asigna en BD, precioUnitario se toma del producto).
     */
    public LineaDevolucion(int idDevolucion, Producto producto, int cantidad) {
        this(0, idDevolucion, producto, cantidad, producto.getPrecio());
    }

    // Getters y setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdDevolucion() {
        return idDevolucion;
    }

    public void setIdDevolucion(int idDevolucion) {
        this.idDevolucion = idDevolucion;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    /** 
     * Calcula el subtotal de esta línea.
     */
    public double getSubtotal() {
        return this.precioUnitario * this.cantidad;
    }
}
