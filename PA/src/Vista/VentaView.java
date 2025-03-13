package Vista;
import javax.swing.*;
import java.awt.*;

public class VentaView extends JFrame {
    
	private static final long serialVersionUID = 1L;
	private JList<String> listaProductos;
    private JButton btnAgregarProducto, btnFinalizarVenta;

    public VentaView() {
        setTitle("Punto de Venta");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Lista de productos en la venta
        listaProductos = new JList<>();
        JScrollPane scrollPane = new JScrollPane(listaProductos);

        // Botones
        btnAgregarProducto = new JButton("Agregar Producto");
        btnFinalizarVenta = new JButton("Finalizar Venta");

        // Panel para botones
        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAgregarProducto);
        panelBotones.add(btnFinalizarVenta);

        // Agregar componentes a la ventana
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        setVisible(true);
    }
}