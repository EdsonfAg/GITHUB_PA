package Persistencia;

import Modelo.MetodoPago;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * DAO para consultas avanzadas de reportes.
 */
public class ReporteDAO {

    /**
     * Productos más vendidos (INNER JOIN + SUM + GROUP BY + ORDER BY).
     * @return Lista de <nombreProducto, totalVendido> ordenados desc.
     */
    public static List<Map.Entry<String,Integer>> productosMasVendidos() throws SQLException {
        String sql =
            "SELECT p.nombre AS nombre, SUM(lv.cantidad) AS total " +
            "FROM LineaVenta lv " +
            "JOIN Producto p ON lv.codigoProducto = p.codigo " +
            "GROUP BY p.nombre " +
            "ORDER BY total DESC";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Map.Entry<String,Integer>> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(
                    new AbstractMap.SimpleEntry<>(
                        rs.getString("nombre"),
                        rs.getInt("total")
                    )
                );
            }
            return lista;
        }
    }

    /**
     * Cantidad de ventas realizadas por cada usuario (COUNT + INNER JOIN + GROUP BY).
     * @return Lista de <nombreUsuario, ventasRealizadas> ordenados desc.
     */
    public static List<Map.Entry<String,Integer>> ventasPorUsuario() throws SQLException {
        String sql =
            "SELECT u.nombre AS nombre, COUNT(v.id) AS total " +
            "FROM Venta v " +
            "JOIN Usuario u ON v.idUsuario = u.id " +
            "GROUP BY u.nombre " +
            "ORDER BY total DESC";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Map.Entry<String,Integer>> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(
                    new AbstractMap.SimpleEntry<>(
                        rs.getString("nombre"),
                        rs.getInt("total")
                    )
                );
            }
            return lista;
        }
    }

    /**
     * Total de ventas por método de pago (GROUP BY).
     * @return Lista de <MetodoPago, sumaTotal>.
     */
    public static List<Map.Entry<MetodoPago,Double>> ventasPorMetodoPago() throws SQLException {
        String sql =
            "SELECT metodoPago AS metodo, SUM(total) AS suma " +
            "FROM Venta " +
            "GROUP BY metodoPago";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Map.Entry<MetodoPago,Double>> lista = new ArrayList<>();
            while (rs.next()) {
                MetodoPago m = MetodoPago.valueOf(rs.getString("metodo"));
                lista.add(
                    new AbstractMap.SimpleEntry<>(
                        m,
                        rs.getDouble("suma")
                    )
                );
            }
            return lista;
        }
    }

    /**
     * Estadísticas generales de ventas en un rango de fechas (COUNT, SUM, AVG, MIN, MAX).
     * @return Mapa con claves: count, sum, avg, min, max.
     */
    public static Map<String,Double> statsVentas(java.util.Date desde, java.util.Date hasta) throws SQLException {
        String sql =
            "SELECT " +
            " COUNT(*)    AS cnt, " +
            " SUM(total)  AS sumv, " +
            " AVG(total)  AS avgv, " +
            " MIN(total)  AS minv, " +
            " MAX(total)  AS maxv " +
            "FROM Venta " +
            "WHERE fecha BETWEEN ? AND ?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, new Timestamp(desde.getTime()));
            ps.setTimestamp(2, new Timestamp(hasta.getTime()));

            try (ResultSet rs = ps.executeQuery()) {
                Map<String,Double> m = new HashMap<>();
                if (rs.next()) {
                    m.put("count", rs.getDouble("cnt"));
                    m.put("sum",   rs.getDouble("sumv"));
                    m.put("avg",   rs.getDouble("avgv"));
                    m.put("min",   rs.getDouble("minv"));
                    m.put("max",   rs.getDouble("maxv"));
                }
                return m;
            }
        }
    }
}