package Vista;

import Modelo.Producto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentasPanel extends JPanel {
    private JTable tblProductos;
    private JTable tblCarrito;
    private DefaultTableModel productosModel;
    private DefaultTableModel carritoModel;
    private JButton btnAgregar, btnQuitar, btnFinalizar;
    private JTextField txtCantidad;

    public VentasPanel() {
        setLayout(new BorderLayout(10,10));

        // Carga productos
        productosModel = new DefaultTableModel(new String[]{"Código","Nombre","Precio","Stock"}, 0);
        tblProductos = new JTable(productosModel);
        reloadProductos();

        // Carrito
        carritoModel = new DefaultTableModel(new String[]{"Código","Nombre","Cantidad","Subtotal"}, 0);
        tblCarrito = new JTable(carritoModel);

        // Controles
        txtCantidad = new JTextField(3);
        btnAgregar = new JButton("Agregar");
        btnQuitar = new JButton("Quitar");
        btnFinalizar = new JButton("Finalizar Venta");
        btnFinalizar.setEnabled(false);

        JPanel controls = new JPanel();
        controls.add(new JLabel("Cant:"));
        controls.add(txtCantidad);
        controls.add(btnAgregar);
        controls.add(btnQuitar);
        controls.add(btnFinalizar);

        // Layout
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            new JScrollPane(tblProductos), new JScrollPane(tblCarrito));
        split.setResizeWeight(0.5);

        add(split, BorderLayout.CENTER);
        add(controls, BorderLayout.SOUTH);
    }

    public void reloadProductos() {
        productosModel.setRowCount(0);
        List<Producto> list = Persistencia.ProductoDAO.obtenerTodos();
        for (Producto p: list) {
            productosModel.addRow(new Object[]{p.getCodigo(),p.getNombre(),p.getPrecio(),p.getStock()});
        }
    }

    // Getters para el controlador
    public JTable getTblProductos() { return tblProductos; }
    public JTable getTblCarrito() { return tblCarrito; }
    public DefaultTableModel getProductosModel() { return productosModel; }
    public DefaultTableModel getCarritoModel() { return carritoModel; }
    public JButton getBtnAgregar() { return btnAgregar; }
    public JButton getBtnQuitar() { return btnQuitar; }
    public JButton getBtnFinalizar() { return btnFinalizar; }
    public JTextField getTxtCantidad() { return txtCantidad; }
}