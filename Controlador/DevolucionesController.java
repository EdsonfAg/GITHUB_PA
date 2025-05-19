package Controlador;

import Modelo.Devolucion;
import Modelo.LineaDevolucion;
import Modelo.Usuario;
import Modelo.Venta;
import Persistencia.DevolucionDAO;
import Vista.DevolucionesPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Controlador que enlaza devoluciones con su panel.
 */
public class DevolucionesController {
    private final DevolucionesPanel view;
    private final Usuario usuario;

    public DevolucionesController(Usuario usuario, DevolucionesPanel view) {
        this.usuario = usuario;
        this.view = view;
        view.getBtnProcesar().addActionListener(this::onProcesar);
        loadVentas();
    }

    private void loadVentas() {
        view.reloadVentas();
    }

    private void onProcesar(ActionEvent e) {
        // 1) Si hay una celda en edición, confirmar su valor
        JTable tbl = view.getTblDevoluciones();
        if (tbl.isEditing()) {
            tbl.getCellEditor().stopCellEditing();
        }

        // 2) Obtener datos de la vista
        Venta venta = view.getSelectedVenta();
        String motivo = view.getTxtMotivo().getText().trim();
        List<LineaDevolucion> items = view.getSelectedDevolucionLines();

        // 3) Validaciones por separado para mensajes más claros
        if (venta == null) {
            JOptionPane.showMessageDialog(view, "Por favor, selecciona una venta.");
            return;
        }
        if (motivo.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Por favor, ingresa el motivo de la devolución.");
            return;
        }
        if (items.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Debes indicar al menos un producto a devolver.");
            return;
        }

        // 4) Registrar la devolución
        Devolucion dev = new Devolucion(venta.getIdAsInt(), usuario, motivo, items);
        boolean success = DevolucionDAO.registrar(dev);

        // 5) Feedback y limpieza
        if (success) {
            JOptionPane.showMessageDialog(view, "¡Devolución registrada correctamente!");
            view.clearForm();
            // Opcional: recargar combo de ventas y tabla de líneas
            view.reloadVentas();
            view.reloadDevolucionLines();
        } else {
            JOptionPane.showMessageDialog(view, "Error al registrar la devolución.");
        }
    }

}
