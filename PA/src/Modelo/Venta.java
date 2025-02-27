package Modelo;

import java.util.ArrayList;
import java.util.List;

class Venta {

	private String idVenta;
	private List<Producto> productosVendidos;

	public Venta(String idVenta) {
	        this.idVenta = idVenta;
	        this.productosVendidos = new ArrayList<>();
}

	
public String getIdVenta() {
		return idVenta;
	}


	public void setIdVenta(String idVenta) {
		this.idVenta = idVenta;
	}


	public List<Producto> getProductosVendidos() {
		return productosVendidos;
	}


	public void setProductosVendidos(List<Producto> productosVendidos) {
		this.productosVendidos = productosVendidos;
	}


public void agregarProducto(Producto producto, int cantidad) {
	        if (producto.reducirStock(cantidad)) {
	            productosVendidos.add(new Producto(producto.getCodigo(), producto.getNombre(), producto.getPrecio(), cantidad));
}
	        else {
	            System.out.println("No hay stock suficiente para " + producto.getNombre());
	        }
}
	    
public void mostrarTicket() {
System.out.println("Ticket No: " + idVenta);
productosVendidos.forEach(p -> System.out.println(p.getNombre() + " x " + p.getStock() + " - Total: " + (p.getPrecio() * p.getStock())));

}

}


