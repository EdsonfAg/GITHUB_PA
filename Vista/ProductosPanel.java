package Vista;

import Modelo.Producto;
import Persistencia.ProductoDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel para mostrar y gestionar productos.
 */
public class ProductosPanel extends JPanel {
    private final JTable tblProductos;
    private final DefaultTableModel productosModel;
    private final JButton btnAgregar;

    public ProductosPanel() {
        setLayout(new BorderLayout(10, 10));

        // Título
        JLabel title = new JLabel("Lista de Productos");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        add(title, BorderLayout.NORTH);

        // Modelo y JTable
        String[] columnas = {"Código", "Nombre", "Precio", "Stock"};
        productosModel = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblProductos = new JTable(productosModel);
        add(new JScrollPane(tblProductos), BorderLayout.CENTER);

        // Botón Agregar Producto
        btnAgregar = new JButton("Agregar Producto");
        btnAgregar.addActionListener(e -> onAgregarProducto());
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(btnAgregar);
        add(south, BorderLayout.SOUTH);

        // Carga inicial de datos
        reloadProductos();
    }

    /**
     * Recarga el contenido de la tabla consultando la BD.
     */
    public void reloadProductos() {
        productosModel.setRowCount(0);
        List<Producto> lista = ProductoDAO.obtenerTodos();
        for (Producto p : lista) {
            productosModel.addRow(new Object[]{
                p.getCodigo(),
                p.getNombre(),
                String.format("%.2f", p.getPrecio()),
                p.getStock()
            });
        }
    }

    private void onAgregarProducto() {
        Window parent = SwingUtilities.getWindowAncestor(this);
        try {
            String codigo = JOptionPane.showInputDialog(parent, "Código:", "Nuevo Producto", JOptionPane.QUESTION_MESSAGE);
            if (codigo == null || codigo.isBlank()) return;

            String nombre = JOptionPane.showInputDialog(parent, "Nombre:", "Nuevo Producto", JOptionPane.QUESTION_MESSAGE);
            if (nombre == null || nombre.isBlank()) return;

            String precioStr = JOptionPane.showInputDialog(parent, "Precio:", "Nuevo Producto", JOptionPane.QUESTION_MESSAGE);
            if (precioStr == null) return;
            double precio = Double.parseDouble(precioStr);

            String stockStr = JOptionPane.showInputDialog(parent, "Stock:", "Nuevo Producto", JOptionPane.QUESTION_MESSAGE);
            if (stockStr == null) return;
            int stock = Integer.parseInt(stockStr);

            Producto p = new Producto(codigo, nombre, precio, stock);
            boolean ok = ProductoDAO.insertar(p);
            if (ok) {
                JOptionPane.showMessageDialog(parent, "Producto agregado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                reloadProductos();
            } else {
                JOptionPane.showMessageDialog(parent, "Error al agregar producto", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(parent, "Precio o stock inválido", "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Getters para controlador si fueran necesarios
    public JTable getTblProductos() {
        return tblProductos;
    }

    public DefaultTableModel getProductosModel() {
        return productosModel;
    }

    public JButton getBtnAgregar() {
        return btnAgregar;
    }
}
