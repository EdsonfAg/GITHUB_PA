package Controlador;

import Modelo.Caja;
import Modelo.Usuario;
import Persistencia.CajaDAO;
import Vista.CajaPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Controlador que enlaza lógica de caja con su panel.
 */
public class CajaController {
    private final CajaPanel view;
    private final Usuario usuarioLogueado;
    private Caja caja;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Construye el controlador de caja con su vista y usuario autenticado.
     */
    public CajaController(CajaPanel view, Usuario usuarioLogueado) {
        this.view = view;
        this.usuarioLogueado = usuarioLogueado;
        view.getBtnAbrirCaja().addActionListener(this::onAbrir);
        view.getBtnCerrarCaja().addActionListener(this::onCerrar);
    }

    /**
     * Maneja la apertura de caja: persiste en BD y agrega movimiento a la tabla.
     */
    private void onAbrir(ActionEvent e) {
        double inicio;
        try {
            inicio = Double.parseDouble(view.getTxtMontoInicial().getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Monto inicial inválido.");
            return;
        }
        caja = new Caja(usuarioLogueado, inicio);
        try {
            CajaDAO.abrirCaja(caja);
            String ahora = LocalDateTime.now().format(dtf);
            view.agregarMovimiento(ahora, "Apertura", "Efectivo", inicio, usuarioLogueado.getUsername());
            view.getBtnAbrirCaja().setEnabled(false);
            view.getTxtMontoInicial().setEditable(false);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Error al abrir caja: " + ex.getMessage());
        }
    }

    /**
     * Maneja el cierre de caja: actualiza en BD y agrega movimiento de cierre.
     */
    private void onCerrar(ActionEvent e) {
        double finalCaja;
        try {
            finalCaja = Double.parseDouble(view.getTxtMontoFinal().getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Monto final inválido.");
            return;
        }
        try {
            CajaDAO.cerrarCaja(caja.getId(), finalCaja);
            String ahora = LocalDateTime.now().format(dtf);
            view.agregarMovimiento(ahora, "Cierre", "Efectivo", finalCaja, usuarioLogueado.getUsername());
            view.getBtnCerrarCaja().setEnabled(false);
            view.getTxtMontoFinal().setEditable(false);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Error al cerrar caja: " + ex.getMessage());
        }
    }

    /**
     * Registra un movimiento de venta en la caja: persiste y actualiza la tabla.
     * Asegura que la caja esté abierta.
     *
     * @param monto      Total de la venta
     * @param metodoPago Efectivo, Tarjeta, QR, etc.
     */
    public void registrarVentaEnCaja(double monto, String metodoPago) {
        // Verificar que la caja esté abierta
        if (caja == null) {
            JOptionPane.showMessageDialog(view,
                "Debe abrir la caja antes de registrar ventas.",
                "Caja no abierta", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Persistir en BD
        try {
            CajaDAO.registrarMovimiento(
                caja.getId(),
                "Venta",
                metodoPago,
                monto,
                usuarioLogueado.getId()
            );
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view,
                "Error al registrar venta en caja: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Actualizar UI
        String ahora = LocalDateTime.now().format(dtf);
        view.agregarMovimiento(ahora, "Venta", metodoPago, monto, usuarioLogueado.getUsername());
    }
}
