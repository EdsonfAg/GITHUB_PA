package Vista;

import java.util.List;

import Modelo.Producto;
import Modelo.Venta;

public class Mostrar {

    public static String mostrarMenu(String[] opciones) {
        StringBuilder cadena = new StringBuilder();
        for (String opcion : opciones) {
            cadena.append(opcion).append("\n");
        }
        return cadena.toString();
    }

    public static String mostrarProducto(Producto producto) {
        String codigo = rellenarEspacios(producto.getCodigo(), 5);
        String nombre = rellenarEspacios(producto.getNombre(), 30);
        String precio = rellenarEspacios(String.format("%.2f", producto.getPrecio()), 10);
        String stock = rellenarEspacios(String.valueOf(producto.getStock()), 10);
        return codigo + nombre + precio + stock;
    }

    public static String mostrarListaProductos(List<Producto> productos) {
        StringBuilder salida = new StringBuilder();
        for (Producto producto : productos) {
            salida.append(mostrarProducto(producto)).append("\n");
        }
        return salida.toString();
    }

    public static String mostrarVenta(Venta venta) {
        StringBuilder salida = new StringBuilder();
        salida.append("Ticket No: ").append(venta.getIdVenta()).append("\n");
        for (Producto producto : venta.getProductosVendidos()) {
            salida.append(producto.getNombre()).append(" x ").append(producto.getStock())
                  .append(" - Total: ").append(String.format("%.2f", producto.getPrecio() * producto.getStock()))
                  .append("\n");
        }
        return salida.toString();
    }

    public static String mostrarListaVentas(List<Venta> ventas) {
        StringBuilder salida = new StringBuilder();
        for (Venta venta : ventas) {
            salida.append(mostrarVenta(venta)).append("\n------------------------\n");
        }
        return salida.toString();
    }

    private static String rellenarEspacios(String dato, int tamano) {
        return String.format("%1$-" + tamano + "s", dato);
    }
}
