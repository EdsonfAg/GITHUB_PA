package Controlador;

import Modelo.MetodoPago;
import Modelo.Producto;
import Modelo.Usuario;
import Modelo.Venta;
import Persistencia.ProductoDAO;
import Persistencia.VentaDAO;
import Vista.ProductosPanel;
import Vista.VentasPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador que conecta un VentasPanel del MainView con la lógica de ventas y registra en Caja.
 */
public class VentaController {
    private final VentasPanel view;
    private Venta venta;
    private final Usuario usuario;
    private final CajaController cajaCtrl;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ProductosPanel productosView;
    /**
     * @param usuario    Usuario autenticado
     * @param view       Panel de ventas en la UI
     * @param cajaCtrl   Controlador de caja para registrar movimientos
     */
    public VentaController(Usuario usuario, VentasPanel view, CajaController cajaCtrl, ProductosPanel productosView) {
        this.usuario = usuario;
        this.view = view;
        this.cajaCtrl = cajaCtrl;
        this.venta = new Venta(usuario);  // inicia venta sin pedir método
        this.productosView = productosView;
        view.reloadProductos();
        view.getBtnFinalizar().setEnabled(false);
        attachListeners();
    }

	/**
	 * Carga el modelo de productos en la tabla y refresca el carrito.
	 */
	private void attachListeners() {
        view.getBtnAgregar().addActionListener(this::onAgregar);
        view.getBtnQuitar().addActionListener(this::onQuitar);
        view.getBtnFinalizar().addActionListener(this::onVender);
    }

    private void onAgregar(ActionEvent e) {
        JTable tbl = view.getTblProductos();
        int row = tbl.getSelectedRow();
        if (row < 0) return;
        String codigo = (String) view.getProductosModel().getValueAt(row, 0);
        Producto p = ProductoDAO.buscarPorCodigo(codigo);
        int cant;
        try {
            cant = Integer.parseInt(view.getTxtCantidad().getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Cantidad inválida.");
            return;
        }
        if (cant <= 0 || cant > p.getStock()) {
            JOptionPane.showMessageDialog(view, "Cantidad inválida o excede stock disponible.");
            return;
        }
        venta.agregarProducto(p, cant);
        refrescarCarrito();
        view.getBtnFinalizar().setEnabled(!venta.getItems().isEmpty());
    }

    private void onQuitar(ActionEvent e) {
        JTable tbl = view.getTblCarrito();
        int row = tbl.getSelectedRow();
        if (row < 0) return;
        String codigo = (String) view.getCarritoModel().getValueAt(row, 0);
        venta.eliminarProducto(codigo);
        refrescarCarrito();
        view.getBtnFinalizar().setEnabled(!venta.getItems().isEmpty());
    }

    /**
     * Finaliza la venta: pide método de pago, persiste en BD, registra movimiento en Caja, muestra ticket y actualiza UI.
     */
    private void onVender(ActionEvent e) {
        // 1) Selección de método de pago
        String[] opciones = {"EFECTIVO", "TARJETA", "QR"};
        String metodo = (String) JOptionPane.showInputDialog(
            view,
            "Selecciona método de pago:",
            "Método de Pago",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]
        );
        if (metodo == null) return;  // canceló

        // 2) Asigna método y calcula total
        venta.setMetodoPago(MetodoPago.valueOf(metodo));

        double total = venta.calcularTotal();

        // 3) Persistir venta en BD
        boolean ok = VentaDAO.registrarVenta(venta);
        if (!ok) {
            JOptionPane.showMessageDialog(view, "Error al registrar venta.");
            return;
        }

        // 4) Registrar movimiento en Caja
        cajaCtrl.registrarVentaEnCaja(total, metodo);

        // 5) Mostrar ticket y confirmar impresión
        mostrarTicket(venta, metodo);

        // 6) Alertas de stock bajo
        lanzarAlertasStock();

        // 7) Limpiar UI y reiniciar venta
        view.getCarritoModel().setRowCount(0);
        venta = new Venta(usuario);  // nueva instancia para la siguiente venta
        view.getBtnFinalizar().setEnabled(false);
        view.reloadProductos();          // refresca el stock en la pestaña Ventas
        productosView.reloadProductos(); // refresca el stock en la pestaña Productos
    }

    /**
     * Muestra un resumen de la venta y pregunta si imprimir.
     */
    private void mostrarTicket(Venta v, String metodo) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== TICKET DE VENTA ===\n");
        sb.append("Fecha: ").append(LocalDateTime.now().format(dtf)).append("\n");
        sb.append("Usuario: ").append(usuario.getUsername()).append("\n");
        sb.append("Método de pago: ").append(metodo).append("\n\n");
        sb.append("ITEMS:\n");
        v.getItems().forEach(item -> sb.append(
            item.getProducto().getNombre())
            .append(" x").append(item.getCantidad())
            .append("  $").append(item.getProducto().getPrecio() * item.getCantidad())
            .append("\n")
        );
        sb.append("\nTOTAL: $").append(String.format("%.2f", v.calcularTotal()));

        int imprimir = JOptionPane.showConfirmDialog(
            view,
            sb.toString(),
            "Resumen de Venta",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE
        );
        if (imprimir == JOptionPane.YES_OPTION) {
            // TODO: Lógica de impresión (PC/impresora)
        }
    }

    private void refrescarCarrito() {
        DefaultTableModel model = view.getCarritoModel();
        model.setRowCount(0);
        for (var lv : venta.getItems()) {
            model.addRow(new Object[]{
                lv.getProducto().getCodigo(),
                lv.getProducto().getNombre(),
                lv.getCantidad(),
                String.format("%.2f", lv.getProducto().getPrecio() * lv.getCantidad())
            });
        }
    }

    private void lanzarAlertasStock() {
        List<Producto> bajos = ProductoDAO.listarPorStockMenor(5);
        if (!bajos.isEmpty()) {
            String msg = "Stock bajo en:\n" + bajos.stream()
                .map(p -> p.getNombre() + " (" + p.getStock() + ")")
                .collect(Collectors.joining("\n"));
            JOptionPane.showMessageDialog(view,
                msg,
                "Alerta de Stock Bajo",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }
}