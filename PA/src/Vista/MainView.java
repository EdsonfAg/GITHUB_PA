package Vista;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainView extends JFrame {
    public MainView() {
        setTitle("Sistema de Gestión.");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear la barra de menú
        JMenuBar menuBar = new JMenuBar();

        // Menú de Productos
        JMenu menuProductos = new JMenu("Productos");
        JMenuItem itemListarProductos = new JMenuItem("Listar Productos");
        JMenuItem itemAgregarProducto = new JMenuItem("Agregar Producto");
        menuProductos.add(itemListarProductos);
        menuProductos.add(itemAgregarProducto);

        // Menú de Ventas
        JMenu menuVentas = new JMenu("Ventas");
        JMenuItem itemNuevaVenta = new JMenuItem("Nueva Venta");
        menuVentas.add(itemNuevaVenta);

        // Agregar menús a la barra de menú
        menuBar.add(menuProductos);
        menuBar.add(menuVentas);

        // Configurar la barra de menú en la ventana
        setJMenuBar(menuBar);

        // Listeners para los ítems del menú
        itemListarProductos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para listar productos
                System.out.println("Listar Productos");
            }
        });

        itemAgregarProducto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para agregar producto
                System.out.println("Agregar Producto");
            }
        });

        itemNuevaVenta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para nueva venta
                System.out.println("Nueva Venta");
            }
        });
        
        setVisible(true);
    }

    public static void main(String[] args) {
        new MainView();
    }
}