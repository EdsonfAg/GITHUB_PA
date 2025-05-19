// EstadisticasPanel.java
package Vista;

import Persistencia.VentaDAO;
import Modelo.Venta;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Panel para mostrar estadísticas de ventas.
 */
public class EstadisticasPanel extends JPanel {
    private JComboBox<String> comboTipo;
    private JButton btnGenerar;
    private JTextArea areaResultados;
    private List<Venta> ventas;

    public EstadisticasPanel() {
        setLayout(new BorderLayout(10, 10));

        // Cargar ventas
        ventas = VentaDAO.listarTodas();

        // Panel superior: selector y botón
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Selecciona tipo de estadística:"));
        comboTipo = new JComboBox<>(new String[]{
            "Productos más vendidos",
            "Ventas por usuario",
            "Ventas por día"
        });
        top.add(comboTipo);
        btnGenerar = new JButton("Generar Estadística");
        top.add(btnGenerar);
        add(top, BorderLayout.NORTH);

        // Área de resultados
        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        add(new JScrollPane(areaResultados), BorderLayout.CENTER);
    }

    public JComboBox<String> getComboTipo() {
        return comboTipo;
    }

    public JButton getBtnGenerar() {
        return btnGenerar;
    }

    public JTextArea getAreaResultados() {
        return areaResultados;
    }

    public List<Venta> getVentas() {
        return ventas;
    }
}