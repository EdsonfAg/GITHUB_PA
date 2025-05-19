package Vista;

import Modelo.LineaDevolucion;
import Modelo.LineaVenta;
import Modelo.Venta;
import Persistencia.VentaDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel para procesar devoluciones.
 */
public class DevolucionesPanel extends JPanel {
    private final JComboBox<Venta> cbVentas;
    private final JTable tblLineas;
    private final DefaultTableModel tblModel;
    private final JTextArea txtMotivo;
    private final JButton btnProcesar;
    private final JButton btnCancelar;

    // Formato para la fecha en el combo
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public DevolucionesPanel() {
        setLayout(new BorderLayout(10, 10));

        // --- Top: selector de venta ---
        cbVentas = new JComboBox<>();
        // Render personalizado para mostrar id y fecha
        cbVentas.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Venta) {
                    Venta v = (Venta) value;
                    setText(String.format("Venta #%d (%s)",
                            v.getIdAsInt(),
                            dateFormat.format(v.getFecha())
                    ));
                }
                return this;
            }
        });
        cbVentas.addActionListener(e -> reloadDevolucionLines());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Venta:"));
        top.add(cbVentas);
        add(top, BorderLayout.NORTH);

        // --- Center: tabla de líneas ---
        tblModel = new DefaultTableModel(new String[]{"Producto", "Vendidos", "A Devolver"}, 0) {
            @Override public boolean isCellEditable(int row, int col) {
                return col == 2; // solo la tercera columna editable
            }

            @Override public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1 || columnIndex == 2) {
                    return Integer.class;
                }
                return String.class;
            }
        };
        tblLineas = new JTable(tblModel);
        add(new JScrollPane(tblLineas), BorderLayout.CENTER);

        // --- South: motivo y botones ---
        txtMotivo = new JTextArea(3, 40);
        JPanel south = new JPanel(new BorderLayout(5,5));
        south.add(new JLabel("Motivo de la devolución:"), BorderLayout.NORTH);
        south.add(new JScrollPane(txtMotivo), BorderLayout.CENTER);

        btnProcesar = new JButton("Procesar");
        btnCancelar = new JButton("Cancelar");
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btns.add(btnCancelar);
        btns.add(btnProcesar);
        south.add(btns, BorderLayout.SOUTH);

        add(south, BorderLayout.SOUTH);

        // Listener para limpiar formulario
        btnCancelar.addActionListener(e -> clearForm());

        // Carga inicial de ventas y líneas
        reloadVentas();
    }

    /**
     * Carga todas las ventas en el combo y actualiza la tabla de líneas.
     */
    public void reloadVentas() {
        cbVentas.removeAllItems();
        List<Venta> ventas = VentaDAO.listarTodas();
        for (Venta v : ventas) {
            cbVentas.addItem(v);
        }
        if (!ventas.isEmpty()) {
            cbVentas.setSelectedIndex(0);
        }
        reloadDevolucionLines();
    }

    /**
     * Rellena la tabla con las líneas de la venta seleccionada.
     */
    public void reloadDevolucionLines() {
        tblModel.setRowCount(0);
        Venta v = getSelectedVenta();
        if (v == null) return;
        for (LineaVenta lv : v.getItems()) {
            tblModel.addRow(new Object[]{
                lv.getProducto().getNombre(),
                lv.getCantidad(),
                0              // inicia en 0 la columna "A Devolver"
            });
        }
    }

    /** Devuelve la venta actualmente seleccionada. */
    public Venta getSelectedVenta() {
        return (Venta) cbVentas.getSelectedItem();
    }

    /**
     * Lee las líneas donde el usuario indicó cantidad > 0.
     */
    public List<LineaDevolucion> getSelectedDevolucionLines() {
        List<LineaDevolucion> result = new ArrayList<>();
        Venta v = getSelectedVenta();
        if (v == null) return result;

        for (int i = 0; i < tblModel.getRowCount(); i++) {
            Object val = tblModel.getValueAt(i, 2);
            int devolver;
            if (val instanceof Integer) {
                devolver = (Integer) val;
            } else {
                try {
                    devolver = Integer.parseInt(val.toString().trim());
                } catch (NumberFormatException ex) {
                    continue;
                }
            }
            if (devolver > 0) {
                LineaVenta lv = v.getItems().get(i);
                result.add(new LineaDevolucion(v.getIdAsInt(), lv.getProducto(), devolver));
            }
        }
        return result;
    }

    /**
     * Limpia la tabla y el campo motivo.
     */
    public void clearForm() {
        txtMotivo.setText("");
        reloadDevolucionLines();
    }

    // Getters para el controlador
    public JTextArea getTxtMotivo()       { return txtMotivo; }
    public JButton getBtnProcesar()       { return btnProcesar; }
    public JTable getTblDevoluciones()    { return tblLineas; }
}
