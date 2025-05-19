package Controlador;

import Persistencia.ReporteDAO;
import Modelo.MetodoPago;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap;
import javax.swing.*;

/**
 * Controlador para disparar consultas avanzadas de ReporteDAO
 * y mostrarlas al usuario en ventanas emergentes.
 */
public class ReporteAvanzadoController {
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /** Muestra los productos más vendidos. */
    public void productosMasVendidos() {
        try {
            List<Map.Entry<String,Integer>> datos = ReporteDAO.productosMasVendidos();
            StringBuilder sb = new StringBuilder("Producto – Unidades vendidas\n\n");
            for (var e : datos) {
                sb.append(String.format("%-30s %5d\n", e.getKey(), e.getValue()));
            }
            JTextArea area = new JTextArea(sb.toString());
            area.setEditable(false);
            JOptionPane.showMessageDialog(null, new JScrollPane(area),
                "Productos más vendidos", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            showError(ex);
        }
    }

    /** Muestra las ventas realizadas por cada usuario. */
    public void ventasPorUsuario() {
        try {
            List<Map.Entry<String,Integer>> datos = ReporteDAO.ventasPorUsuario();
            StringBuilder sb = new StringBuilder("Usuario – Ventas realizadas\n\n");
            for (var e : datos) {
                sb.append(String.format("%-30s %5d\n", e.getKey(), e.getValue()));
            }
            JOptionPane.showMessageDialog(null, new JScrollPane(new JTextArea(sb.toString())),
                "Ventas por usuario", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            showError(ex);
        }
    }

    /** Muestra la suma de ventas agrupadas por método de pago. */
    public void ventasPorMetodoPago() {
        try {
            List<Map.Entry<MetodoPago,Double>> datos = ReporteDAO.ventasPorMetodoPago();
            StringBuilder sb = new StringBuilder("Método de pago – Total recaudado\n\n");
            for (var e : datos) {
                sb.append(String.format("%-20s $%8.2f\n",
                    e.getKey().name(), e.getValue()));
            }
            JOptionPane.showMessageDialog(null, new JScrollPane(new JTextArea(sb.toString())),
                "Ventas por método de pago", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            showError(ex);
        }
    }

    /** Pide al usuario un rango de fechas y muestra estadísticas (COUNT, SUM, AVG, MIN, MAX). */
    public void statsVentas() {
        try {
            String desdeStr = JOptionPane.showInputDialog(
                null, "Fecha DESDE (yyyy-MM-dd):", "2025-01-01");
            if (desdeStr == null) return;
            String hastaStr = JOptionPane.showInputDialog(
                null, "Fecha HASTA (yyyy-MM-dd):", "2025-12-31");
            if (hastaStr == null) return;

            Date desde = sdf.parse(desdeStr);
            Date hasta = sdf.parse(hastaStr);
            Map<String,Double> m = ReporteDAO.statsVentas(desde, hasta);

            String msg = String.format(
                "Ventas entre %s y %s:\n\n" +
                "Total de registros: %.0f\n" +
                "Suma total:       $%.2f\n" +
                "Promedio:         $%.2f\n" +
                "Mínima:           $%.2f\n" +
                "Máxima:           $%.2f\n",
                desdeStr, hastaStr,
                m.getOrDefault("count", 0.0),
                m.getOrDefault("sum",   0.0),
                m.getOrDefault("avg",   0.0),
                m.getOrDefault("min",   0.0),
                m.getOrDefault("max",   0.0)
            );

            JOptionPane.showMessageDialog(null, msg,
                "Estadísticas de Ventas", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(null,
            "Error al ejecutar reporte:\n" + ex.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }
}
