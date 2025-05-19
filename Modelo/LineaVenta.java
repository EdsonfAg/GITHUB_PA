package Modelo;

public class LineaVenta {
 private Producto producto;
 private int cantidad;

 public LineaVenta(Producto producto, int cantidad) {
     this.producto = producto;
     this.cantidad = cantidad;
 }

 public Producto getProducto() {
     return producto;
 }

 public int getCantidad() {
     return cantidad;
 }

 public void incrementarCantidad(int extra) {
     this.cantidad += extra;
 }
}
