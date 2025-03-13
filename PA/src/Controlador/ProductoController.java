package Controlador;


	import Modelo.Producto;
	import Modelo.Inventario;

	public class ProductoController {
	    private Inventario inventario;

	    public ProductoController(Inventario inventario) {
	        this.inventario = inventario;
	    }

	    // Método para listar productos
	    public String listarProductos() {
	        StringBuilder productos = new StringBuilder();
	        for (Producto producto : inventario.obtenerProductos()) {
	            productos.append(producto.getCodigo()).append(" - ")
	                     .append(producto.getNombre()).append(" - ")
	                     .append(producto.getPrecio()).append(" - ")
	                     .append(producto.getStock()).append("\n");
	        }
	        return productos.toString();
	    }

	    // Método para agregar un producto
	    public boolean agregarProducto(String codigo, String nombre, double precio, int stock) {
	        Producto nuevoProducto = new Producto(codigo, nombre, precio, stock);
	        inventario.agregarProducto(nuevoProducto);
	        return true; // Podrías devolver false si hay algún error
	    }
	}
