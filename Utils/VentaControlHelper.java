package Utils;

import Modelo.Venta;
import Modelo.Usuario;
import Modelo.MetodoPago;
import javax.swing.*;

/**
 * Helper para crear nuevas instancias de Venta con ID único y lógica de método de pago.
 */
public class VentaControlHelper {

    /**
     * Crea una nueva venta pidiendo al usuario el método de pago y generando
     * un ID único basado en timestamp.
     *
     * @param usuarioVendedor Usuario que realiza la venta.
     * @return Objeto Venta recién creado.
     */
    public static Venta crearVentaNueva(Usuario usuarioVendedor) {
        // Generar un ID único usando timestamp
        String id = "V" + System.currentTimeMillis();

        // Pedir método de pago al usuario
        MetodoPago[] opciones = MetodoPago.values();
        MetodoPago metodo = (MetodoPago) JOptionPane.showInputDialog(
            null,
            "Selecciona método de pago:",
            "Método de Pago",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]
        );
        // Si el usuario cancela, usar primera opción por defecto
        if (metodo == null) {
            metodo = opciones[0];
        }

        // Crear y retornar la venta
        return new Venta(id, usuarioVendedor, metodo);
    }
}
