// EstadisticasController.java
package Controlador;

import Utils.EstadisticasHelper;
import Vista.EstadisticasPanel;

import java.awt.event.ActionEvent;
import javax.swing.*;

/**
 * Controlador que enlaza estadísticas con el panel respectivo.
 */
public class EstadisticasController {
    private final EstadisticasPanel view;

    public EstadisticasController(EstadisticasPanel view) {
        this.view = view;
        this.view.getBtnGenerar().addActionListener(this::onGenerar);
    }

    private void onGenerar(ActionEvent e) {
        var ventas = view.getVentas();  // carga interna de ventas
        String tipo = (String) view.getComboTipo().getSelectedItem();
        String resultado;
        switch (tipo) {
            case "Productos más vendidos": resultado = EstadisticasHelper.productosMasVendidos(ventas); break;
            case "Ventas por usuario":      resultado = EstadisticasHelper.ventasPorUsuario(ventas);      break;
            case "Ventas por día":         resultado = EstadisticasHelper.ventasPorDia(ventas);         break;
            default: resultado = "";
        }
        view.getAreaResultados().setText(resultado);
    }
}
