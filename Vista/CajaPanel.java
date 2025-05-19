package Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CajaPanel extends JPanel {
    private final JTextField txtMontoInicial;
    private final JTextField txtMontoFinal;
    private final JButton btnAbrirCaja;
    private final JButton btnCerrarCaja;
    private final JTable tblMovimientos;
    private final DefaultTableModel modeloMovimientos;

    public CajaPanel() {
        setLayout(new BorderLayout(10, 10));

        // --- Panel superior: montos y botones ---
        JPanel pnlTop = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        pnlTop.add(new JLabel("Monto Inicial:"), gbc);
        gbc.gridx = 1;
        txtMontoInicial = new JTextField(10);
        pnlTop.add(txtMontoInicial, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        pnlTop.add(new JLabel("Monto Final:"), gbc);
        gbc.gridx = 1;
        txtMontoFinal = new JTextField(10);
        pnlTop.add(txtMontoFinal, gbc);

        gbc.gridx = 2; gbc.gridy = 0;
        btnAbrirCaja = new JButton("Abrir Caja");
        pnlTop.add(btnAbrirCaja, gbc);

        gbc.gridx = 2; gbc.gridy = 1;
        btnCerrarCaja = new JButton("Cerrar Caja");
        pnlTop.add(btnCerrarCaja, gbc);

        add(pnlTop, BorderLayout.NORTH);

        // --- Tabla de movimientos ---
        String[] columnas = {"Fecha", "Tipo", "Método Pago", "Monto", "Usuario"};
        modeloMovimientos = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblMovimientos = new JTable(modeloMovimientos);
        JScrollPane scroll = new JScrollPane(tblMovimientos);
        add(scroll, BorderLayout.CENTER);
    }

    /**
     * Agrega una fila a la tabla de movimientos.
     * @param fechaHora formateada
     * @param tipo Movimiento (Apertura, Venta, Retiro, Cierre...)
     * @param metodoPago Efectivo, Tarjeta, QR...
     * @param monto Monto del movimiento
     * @param usuario Usuario que realiza la operación
     */
    public void agregarMovimiento(String fechaHora, String tipo, String metodoPago, double monto, String usuario) {
        modeloMovimientos.addRow(new Object[]{fechaHora, tipo, metodoPago, monto, usuario});
    }

    // Getters para el controlador
    public JTextField getTxtMontoInicial() { return txtMontoInicial; }
    public JTextField getTxtMontoFinal()   { return txtMontoFinal;   }
    public JButton getBtnAbrirCaja()       { return btnAbrirCaja;    }
    public JButton getBtnCerrarCaja()      { return btnCerrarCaja;   }
}
