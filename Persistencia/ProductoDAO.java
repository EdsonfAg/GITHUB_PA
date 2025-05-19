package Persistencia;

import Modelo.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    // Listar todos los productos desde SQL Server
    public static List<Producto> obtenerTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Producto";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Producto(
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getInt("stock")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener productos: " + e.getMessage());
        }

        return lista;
    }
 // Listar productos cuyo stock sea menor al umbral indicado
    public static List<Producto> listarPorStockMenor(int umbral) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Producto WHERE stock < ?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, umbral);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Producto(
                            rs.getString("codigo"),
                            rs.getString("nombre"),
                            rs.getDouble("precio"),
                            rs.getInt("stock")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar productos por stock menor: " + e.getMessage());
        }
        return lista;
    }


    // Insertar un nuevo producto
    public static boolean insertar(Producto p) {
        String sql = "INSERT INTO Producto (codigo, nombre, precio, stock) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getCodigo());
            stmt.setString(2, p.getNombre());
            stmt.setDouble(3, p.getPrecio());
            stmt.setInt(4, p.getStock());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar producto: " + e.getMessage());
            return false;
        }
    }

    // Actualizar stock
    public static boolean actualizarStock(String codigo, int nuevoStock) {
        String sql = "UPDATE Producto SET stock = ? WHERE codigo = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, nuevoStock);
            stmt.setString(2, codigo);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar stock: " + e.getMessage());
            return false;
        }
    }

    // Buscar por código
    public static Producto buscarPorCodigo(String codigo) {
        String sql = "SELECT * FROM Producto WHERE codigo = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Producto(
                            rs.getString("codigo"),
                            rs.getString("nombre"),
                            rs.getDouble("precio"),
                            rs.getInt("stock")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar producto por código: " + e.getMessage());
        }

        return null;
    }
    
    // Descontar stock tras venta
    public static void descontarStock(String codigo, int cantidad) throws SQLException {
        String sql = "UPDATE Producto SET stock = stock - ? WHERE codigo = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setString(2, codigo);
            ps.executeUpdate();
        }
    }
    
    public static List<Producto> buscarPorNombre(String patron) throws SQLException {
        String sql = 
          "SELECT * FROM Producto " +
          "WHERE nombre LIKE ? " +
          "ORDER BY stock ASC, precio DESC";

        try (Connection c = ConexionBD.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, "%" + patron + "%");
            try (ResultSet rs = ps.executeQuery()) {
                List<Producto> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(new Producto(
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getInt("stock")
                    ));
                }
                return lista;
            }
        }
    }

}
