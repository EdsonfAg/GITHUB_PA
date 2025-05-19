package Persistencia;

import Modelo.Caja;
import Modelo.MovimientoCaja;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos para las operaciones de Caja y sus movimientos.
 */
public class CajaDAO {

    /**
     * Abre una nueva sesión de caja e inserta el registro en BD.
     */
    public static Caja abrirCaja(Caja caja) throws SQLException {
        String sql = "INSERT INTO Caja (idUsuario, montoInicial) VALUES (?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, caja.getUsuario().getId());
            ps.setDouble(2, caja.getMontoInicial());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    caja.setId(rs.getInt(1));
                }
            }
        }
        return caja;
    }

    /**
     * Cierra la caja actual actualizando fecha de cierre y monto final.
     */
    public static void cerrarCaja(int idCaja, double montoFinal) throws SQLException {
        String sql = "UPDATE Caja SET fechaCierre = GETDATE(), montoFinal = ? WHERE id = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, montoFinal);
            ps.setInt(2, idCaja);
            ps.executeUpdate();
        }
    }

    /**
     * Inserta un movimiento en la tabla MovimientoCaja.
     * Usa GETDATE() para la fecha de movimiento.
     */
    public static void registrarMovimiento(int cajaId,
                                           String tipo,
                                           String metodoPago,
                                           double monto,
                                           int idUsuario) throws SQLException {
        String sql = "INSERT INTO MovimientoCaja " +
                     "(idCaja, tipo, metodoPago, monto, fechaMovimiento, idUsuario) " +
                     "VALUES (?, ?, ?, ?, GETDATE(), ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cajaId);
            ps.setString(2, tipo);
            ps.setString(3, metodoPago);
            ps.setDouble(4, monto);
            ps.setInt(5, idUsuario);
            ps.executeUpdate();
        }
    }


    /**
     * Devuelve el historial de movimientos de una caja.
     */
    public static List<MovimientoCaja> listarMovimientos(int idCaja) throws SQLException {
        List<MovimientoCaja> lista = new ArrayList<>();
        String sql = "SELECT id, idCaja, fechaMovimiento, tipo, metodoPago, monto, idUsuario " +
                     "FROM MovimientoCaja WHERE idCaja = ? ORDER BY fechaMovimiento";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCaja);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new MovimientoCaja(
                        rs.getInt("id"),
                        rs.getInt("idCaja"),
                        rs.getTimestamp("fechaMovimiento"),
                        rs.getString("tipo"),
                        rs.getString("metodoPago"),
                        rs.getDouble("monto"),
                        rs.getInt("idUsuario")
                    ));
                }
            }
        }
        return lista;
    }
}
