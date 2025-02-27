package Modelo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Principal {
    private static Inventario inventario;
    private static PersistenciaDatos<Venta> persistenciaVentas;
    private static List<Venta> ventas;

    public static void main(String[] args) throws IOException {
        inventario = new Inventario("productos.csv", "productos.json");
        persistenciaVentas = new PersistenciaDatos<>("ventas.csv", "ventas.json");
        ventas = persistenciaVentas.cargarJSON(new com.google.gson.reflect.TypeToken<List<Venta>>(){}.getType());

        if (ventas.isEmpty()) {
            ventas = new java.util.ArrayList<>();
        }

        menuPrincipal();
    }

    private static void menuPrincipal() throws IOException {
        String[] opciones = {"1.- Productos", "2.- Punto de Venta", "3.- Inventario", "4.- Ventas", "5.- Salir"};
        String opcion;
        do {
            opcion = leerEntrada("Menú Principal:\n" + Mostrar.mostrarMenu(opciones) + "\nElige una opción:");
            switch (opcion) {
                case "1":
                    menuProductos();
                    break;
                case "2":
                    menuPuntoVenta();
                    break;
                case "3":
                    menuInventario();
                    break;
                case "4":
                    mostrarVentas();
                    break;
                case "5":
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (!opcion.equals("5"));
    }

    private static void menuProductos() throws IOException {
        String[] opciones = {"1.- Listar productos", "2.- Agregar producto", "3.- Eliminar producto", "4.- Salir"};
        String opcion;
        do {
            opcion = leerEntrada("Menú de Productos:\n" + Mostrar.mostrarMenu(opciones));
            switch (opcion) {
                case "1":
                    System.out.println(Mostrar.mostrarListaProductos(inventario.obtenerProductos()));
                    break;
                case "2":
                    agregarProducto();
                    break;
                case "3":
                    eliminarProducto();
                    break;
                case "4":
                    System.out.println("Saliendo del menú de productos...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (!opcion.equals("4"));
    }

    private static void agregarProducto() throws IOException {
        String codigo = leerEntrada("Código del producto:");
        String nombre = leerEntrada("Nombre del producto:");
        double precio = Double.parseDouble(leerEntrada("Precio del producto:"));
        int stock = Integer.parseInt(leerEntrada("Stock inicial:"));

        Producto nuevoProducto = new Producto(codigo, nombre, precio, stock);
        inventario.agregarProducto(nuevoProducto);
        System.out.println("Producto agregado correctamente.");
    }

    private static void eliminarProducto() throws IOException {
        String codigo = leerEntrada("Código del producto a eliminar:");
        inventario.eliminarProducto(codigo);
        System.out.println("Producto eliminado correctamente.");
    }

    private static void menuInventario() throws IOException {
        String[] opciones = {"1.- Listar inventario", "2.- Agregar stock", "3.- Salir"};
        String opcion;
        do {
            opcion = leerEntrada("Menú de Inventario:\n" + Mostrar.mostrarMenu(opciones));
            switch (opcion) {
                case "1":
                    System.out.println(Mostrar.mostrarListaProductos(inventario.obtenerProductos()));
                    break;
                case "2":
                    agregarStock();
                    break;
                case "3":
                    System.out.println("Saliendo del menú de inventario...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (!opcion.equals("3"));
    }

    private static void agregarStock() throws IOException {
        String codigo = leerEntrada("Código del producto:");
        int cantidad = Integer.parseInt(leerEntrada("Cantidad a agregar:"));
        inventario.obtenerProductos().stream()
                .filter(p -> p.getCodigo().equals(codigo))
                .findFirst()
                .ifPresent(p -> p.agregarStock(cantidad));
        System.out.println("Stock actualizado.");
    }

    private static void menuPuntoVenta() throws IOException {
        String idVenta = "V" + (ventas.size() + 1);
        Venta venta = new Venta(idVenta);

        String opcion;
        do {
            System.out.println("Ticket actual:\n" + Mostrar.mostrarVenta(venta));
            String[] opciones = {"1.- Agregar producto", "2.- Finalizar venta", "3.- Cancelar venta"};
            opcion = leerEntrada("Menú de Venta:\n" + Mostrar.mostrarMenu(opciones));
            switch (opcion) {
                case "1":
                    agregarProductoAVenta(venta);
                    break;
                case "2":
                    finalizarVenta(venta);
                    break;
                case "3":
                    System.out.println("Venta cancelada.");
                    return;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (!opcion.equals("2") && !opcion.equals("3"));
    }

    private static void agregarProductoAVenta(Venta venta) throws IOException {
        String codigo = leerEntrada("Código del producto:");
        int cantidad = Integer.parseInt(leerEntrada("Cantidad:"));

        inventario.obtenerProductos().stream()
                .filter(p -> p.getCodigo().equals(codigo))
                .findFirst()
                .ifPresent(p -> venta.agregarProducto(p, cantidad));
    }

    private static void finalizarVenta(Venta venta) {
        ventas.add(venta);
        persistenciaVentas.guardarJSON(ventas);
        System.out.println("Venta finalizada:\n" + Mostrar.mostrarVenta(venta));
    }

    private static void mostrarVentas() {
        System.out.println(Mostrar.mostrarListaVentas(ventas));
    }

    private static String leerEntrada(String mensaje) throws IOException {
        System.out.println(mensaje);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        return br.readLine().trim();
    }
}
