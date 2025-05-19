package Vista;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;

/**
 * Panel para generar reportes de ventas en distintos formatos.
 */
public class ReportePanel extends JPanel {
    private JDateChooser dateDesde;
    private JDateChooser dateHasta;
    private JComboBox<String> comboTipoReporte;
    private JButton btnGenerarPDF;
    private JButton btnGenerarJSON;

    public ReportePanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Panel de selección de parámetros
        JPanel params = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        params.add(new JLabel("Fecha Desde:"), gbc);
        dateDesde = new JDateChooser();
        gbc.gridx = 1;
        params.add(dateDesde, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        params.add(new JLabel("Fecha Hasta:"), gbc);
        dateHasta = new JDateChooser();
        gbc.gridx = 1;
        params.add(dateHasta, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        params.add(new JLabel("Tipo de Reporte:"), gbc);
        comboTipoReporte = new JComboBox<>(new String[]{
            "Productos más vendidos",
            "Ventas por vendedor",
            "Ventas por método de pago"
        });
        gbc.gridx = 1;
        params.add(comboTipoReporte, gbc);

        // Botones de exportación
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnGenerarPDF = new JButton("Exportar a PDF");
        btnGenerarJSON = new JButton("Exportar a JSON");
        botones.add(btnGenerarPDF);
        botones.add(btnGenerarJSON);

        // Montar en el BorderLayout
        add(params, BorderLayout.NORTH);
        add(botones, BorderLayout.SOUTH);
    }

    public JDateChooser getDateDesde() {
        return dateDesde;
    }

    public JDateChooser getDateHasta() {
        return dateHasta;
    }

    public JComboBox<String> getComboTipoReporte() {
        return comboTipoReporte;
    }

    public JButton getBtnGenerarPDF() {
        return btnGenerarPDF;
    }

    public JButton getBtnGenerarJSON() {
        return btnGenerarJSON;
    }
}