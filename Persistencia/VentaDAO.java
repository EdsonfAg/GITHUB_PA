package Persistencia;

import Modelo.LineaVenta;
import Modelo.MetodoPago;
import Modelo.Producto;
import Modelo.Usuario;
import Modelo.Venta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {
	public static boolean registrarVenta(Venta venta) {
	    String sqlVenta = """
	        INSERT INTO Venta (fecha, idUsuario, metodoPago, subtotal, iva, ieps, total)
	        VALUES (GETDATE(), ?, ?, ?, ?, ?, ?);
	    """;

	    String sqlLinea = """
	        INSERT INTO LineaVenta (idVenta, codigoProducto, cantidad, precioUnitario)
	        VALUES (?, ?, ?, ?);
	    """;

	    // Actualiza el stock en la misma transacción
	    String sqlStock = "UPDATE Producto SET stock = stock - ? WHERE codigo = ?";

	    try (Connection conn = ConexionBD.obtenerConexion()) {
	        conn.setAutoCommit(false);

	        // 1) Inserto la cabecera de venta
	        try (PreparedStatement stmtVenta = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
	            stmtVenta.setInt(1, venta.getUsuarioVendedor().getId());
	            stmtVenta.setString(2, venta.getMetodoPago().name());
	            stmtVenta.setDouble(3, venta.calcularSubtotal());
	            stmtVenta.setDouble(4, venta.calcularIVA());
	            stmtVenta.setDouble(5, venta.calcularIEPS());
	            stmtVenta.setDouble(6, venta.calcularTotal());
	            stmtVenta.executeUpdate();

	            try (ResultSet generatedKeys = stmtVenta.getGeneratedKeys()) {
	                if (!generatedKeys.next()) {
	                    conn.rollback();
	                    return false;
	                }
	                int idVenta = generatedKeys.getInt(1);

	                // 2) Inserto cada línea y descuesto stock
	                for (LineaVenta lv : venta.getItems()) {
	                    // 2a) Línea de venta
	                    try (PreparedStatement stmtLinea = conn.prepareStatement(sqlLinea)) {
	                        stmtLinea.setInt(1, idVenta);
	                        stmtLinea.setString(2, lv.getProducto().getCodigo());
	                        stmtLinea.setInt(3, lv.getCantidad());
	                        stmtLinea.setDouble(4, lv.getProducto().getPrecio());
	                        stmtLinea.executeUpdate();
	                    }
	                    // 2b) Descuento stock
	                    try (PreparedStatement stmtStock = conn.prepareStatement(sqlStock)) {
	                        stmtStock.setInt(1, lv.getCantidad());
	                        stmtStock.setString(2, lv.getProducto().getCodigo());
	                        stmtStock.executeUpdate();
	                    }
	                }

	                // 3) Commit de toda la transacción
	                conn.commit();
	                return true;
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("Error al registrar venta: " + e.getMessage());
	        // El cierre/rollback ocurrirá automáticamente al cerrar la conexión sin commit
	        return false;
	    }
	}

    // 🔍 Listar todas las ventas con sus líneas
    public static List<Venta> listarTodas() {
        List<Venta> lista = new ArrayList<>();
        String sqlVentas = "SELECT * FROM Venta";
        String sqlLineas = "SELECT * FROM LineaVenta WHERE idVenta = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmtVentas = conn.prepareStatement(sqlVentas);
             ResultSet rsVentas = stmtVentas.executeQuery()) {

            while (rsVentas.next()) {
                int idVenta = rsVentas.getInt("id");
                int idUsuario = rsVentas.getInt("idUsuario");
                // Usamos el constructor de Usuario de 4 parámetros (id y campos vacíos)
                Usuario usuario = new Usuario(idUsuario, "", "", null);
                MetodoPago metodo = MetodoPago.valueOf(rsVentas.getString("metodoPago"));

                Venta venta = new Venta(
                        String.valueOf(idVenta),
                        rsVentas.getDate("fecha"),
                        usuario,
                        metodo
                );

                try (PreparedStatement stmtLineas = conn.prepareStatement(sqlLineas)) {
                    stmtLineas.setInt(1, idVenta);
                    try (ResultSet rsLineas = stmtLineas.executeQuery()) {
                        while (rsLineas.next()) {
                            String codigo = rsLineas.getString("codigoProducto");
                            int cantidad = rsLineas.getInt("cantidad");

                            Producto producto = ProductoDAO.buscarPorCodigo(codigo);
                            if (producto != null) {
                                venta.agregarProducto(producto, cantidad);
                            }
                        }
                    }
                }

                lista.add(venta);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar ventas: " + e.getMessage());
        }

        return lista;
    }

    // 🔍 Buscar venta por ID
    public static Venta buscarPorId(int idVenta) {
        String sqlVenta = "SELECT * FROM Venta WHERE id = ?";
        String sqlLineas = "SELECT * FROM LineaVenta WHERE idVenta = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmtVenta = conn.prepareStatement(sqlVenta)) {

            stmtVenta.setInt(1, idVenta);
            try (ResultSet rsVenta = stmtVenta.executeQuery()) {
                if (rsVenta.next()) {
                    int idUsuario = rsVenta.getInt("idUsuario");
                    Usuario usuario = new Usuario(idUsuario, "", "", null);
                    MetodoPago metodo = MetodoPago.valueOf(rsVenta.getString("metodoPago"));

                    Venta venta = new Venta(
                            String.valueOf(idVenta),
                            rsVenta.getDate("fecha"),
                            usuario,
                            metodo
                    );

                    try (PreparedStatement stmtLineas = conn.prepareStatement(sqlLineas)) {
                        stmtLineas.setInt(1, idVenta);
                        try (ResultSet rsLineas = stmtLineas.executeQuery()) {
                            while (rsLineas.next()) {
                                String codigo = rsLineas.getString("codigoProducto");
                                int cantidad = rsLineas.getInt("cantidad");

                                Producto producto = ProductoDAO.buscarPorCodigo(codigo);
                                if (producto != null) {
                                    venta.agregarProducto(producto, cantidad);
                                }
                            }
                        }
                    }

                    return venta;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar venta por ID: " + e.getMessage());
        }

        return null;
    }

    // 🗑 Eliminar venta por ID
    public static boolean eliminarVenta(int idVenta) {
        String sqlLineas = "DELETE FROM LineaVenta WHERE idVenta = ?";
        String sqlVenta = "DELETE FROM Venta WHERE id = ?";

        try (Connection conn = ConexionBD.obtenerConexion()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtLineas = conn.prepareStatement(sqlLineas)) {
                stmtLineas.setInt(1, idVenta);
                stmtLineas.executeUpdate();
            }

            try (PreparedStatement stmtVenta = conn.prepareStatement(sqlVenta)) {
                stmtVenta.setInt(1, idVenta);
                stmtVenta.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al eliminar venta: " + e.getMessage());
            return false;
        }
    }
}
