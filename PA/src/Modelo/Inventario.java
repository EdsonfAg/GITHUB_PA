package Modelo;

import java.util.ArrayList;
import java.util.List;

import Persistencia.PersistenciaDatos;

public class Inventario {
	private List<Producto> productos;
    private PersistenciaDatos<Producto> persistencia;

    public Inventario(String archivoCSV, String archivoJSON) {
        this.persistencia = new PersistenciaDatos<>(archivoCSV, archivoJSON);
        this.productos = persistencia.cargarJSON(new com.google.gson.reflect.TypeToken<List<Producto>>(){}.getType());
            if (this.productos==null) {
            	this.productos = new ArrayList<>();
            }
        if (productos.isEmpty()) {
            productos = new ArrayList<>();
        }
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
        persistencia.guardarJSON(productos);
    }

    public void eliminarProducto(String codigo) {
        productos.removeIf(p -> p.getCodigo().equals(codigo));
        persistencia.guardarJSON(productos);
    }

    public List<Producto> obtenerProductos() {
        return productos;
    }
}
