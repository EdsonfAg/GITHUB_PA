package Utils;

import Modelo.Venta;
import Modelo.LineaVenta;
import Modelo.Usuario;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class EstadisticasHelper {

    public static String productosMasVendidos(List<Venta> ventas) {
        Map<String, Integer> conteo = new HashMap<>();

        for (Venta v : ventas) {
            for (LineaVenta item : v.getItems()) {
                String nombre = item.getProducto().getNombre();
                conteo.put(nombre, conteo.getOrDefault(nombre, 0) + item.getCantidad());
            }
        }

        return conteo.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .map(e -> e.getKey() + ": " + e.getValue() + " unidades")
                .collect(Collectors.joining("\n"));
    }

    public static String ventasPorUsuario(List<Venta> ventas) {
        Map<String, Integer> conteo = new HashMap<>();

        for (Venta v : ventas) {
            String nombre = v.getUsuarioVendedor().getNombre();
            conteo.put(nombre, conteo.getOrDefault(nombre, 0) + 1);
        }

        return conteo.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .map(e -> e.getKey() + ": " + e.getValue() + " ventas")
                .collect(Collectors.joining("\n"));
    }

    public static String ventasPorDia(List<Venta> ventas) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Integer> conteo = new HashMap<>();

        for (Venta v : ventas) {
            String fecha = sdf.format(v.getFecha());
            conteo.put(fecha, conteo.getOrDefault(fecha, 0) + 1);
        }

        return conteo.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> e.getKey() + ": " + e.getValue() + " ventas")
                .collect(Collectors.joining("\n"));
    }
}
