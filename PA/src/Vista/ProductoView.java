package Vista;

import Controlador.ProductoController;
import Modelo.Inventario;

import javax.swing.*;
import java.awt.*;

public class ProductoView extends JFrame {
    private JTable tablaProductos;
    private ProductoController productoController;

    public ProductoView() {
        setTitle("Gestión de Productos");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Inicializar el controlador
        Inventario inventario = new Inventario("productos.csv", "productos.json");
        productoController = new ProductoController(inventario);

        // Crear la tabla de productos
        String[] columnas = {"Código", "Nombre", "Precio", "Stock"};
        Object[][] datos = obtenerDatosProductos(); // Obtener datos desde el controlador
        tablaProductos = new JTable(datos, columnas);
        JScrollPane scrollPane = new JScrollPane(tablaProductos);

        // Agregar la tabla a la ventana
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private Object[][] obtenerDatosProductos() {
        String productos = productoController.listarProductos();
        String[] lineas = productos.split("\n");
        Object[][] datos = new Object[lineas.length][4];

        for (int i = 0; i < lineas.length; i++) {
            String[] partes = lineas[i].split(" - ");
            datos[i][0] = partes[0]; // Código
            datos[i][1] = partes[1]; // Nombre
            datos[i][2] = partes[2]; // Precio
            datos[i][3] = partes[3]; // Stock
        }

        return datos;
    }
}