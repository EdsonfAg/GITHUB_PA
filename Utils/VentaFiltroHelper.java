package Utils;

import Modelo.Venta;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class VentaFiltroHelper {
    public static List<Venta> filtrarPorRango(List<Venta> ventas, Date desde, Date hasta) {
        return ventas.stream()
                .filter(v -> !v.getFecha().before(desde) && !v.getFecha().after(hasta))
                .collect(Collectors.toList());
    }
}
// Compare this snippet from src/Controlador/ReporteController.java: