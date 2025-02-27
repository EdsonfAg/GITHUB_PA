package Modelo;

abstract class ItemVenta {
	// Clase base para Producto y Venta
	    protected String codigo;
	    protected String nombre;
	    protected double precio;

	    public ItemVenta(String codigo, String nombre, double precio) {
	        this.codigo = codigo;
	        this.nombre = nombre;
	        this.precio = precio;
	    }
	    
public String getCodigo() { 
	return codigo; 
	}

public String getNombre() {
	return nombre;
	}

public double getPrecio() {
	    	return precio; 
	    	}
	

}
