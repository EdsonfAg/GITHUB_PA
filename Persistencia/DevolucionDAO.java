package Persistencia;

import Modelo.Devolucion;
import Modelo.LineaDevolucion;
import Modelo.Producto;
import Modelo.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para manejar operaciones de Devolución y sus líneas.
 */
public class DevolucionDAO {

    /**
     * Registra una nueva devolución (encabezado y detalle) en la base de datos.
     */
    public static boolean registrar(Devolucion dev) {
        String sqlDev = "INSERT INTO Devolucion (idVenta, idUsuario, motivo, total) VALUES (?, ?, ?, ?)";
        String sqlLinea = "INSERT INTO LineaDevolucion (idDevolucion, codigoProducto, cantidad, precioUnitario) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection()) {
            conn.setAutoCommit(false);

            // Insertar encabezado y obtener id generado
            try (PreparedStatement psDev = conn.prepareStatement(sqlDev, Statement.RETURN_GENERATED_KEYS)) {
                psDev.setInt(1, dev.getIdVenta());
                psDev.setInt(2, dev.getUsuario().getId());
                psDev.setString(3, dev.getMotivo());
                psDev.setDouble(4, dev.getTotal());
                psDev.executeUpdate();

                try (ResultSet keys = psDev.getGeneratedKeys()) {
                    if (!keys.next()) {
                        conn.rollback();
                        return false;
                    }
                    int idDev = keys.getInt(1);
                    dev.setId(idDev);
                }
            }

            // Insertar líneas de devolución
            try (PreparedStatement psLinea = conn.prepareStatement(sqlLinea)) {
                for (LineaDevolucion item : dev.getItems()) {
                    psLinea.setInt(1, dev.getId());
                    psLinea.setString(2, item.getProducto().getCodigo());
                    psLinea.setInt(3, item.getCantidad());
                    psLinea.setDouble(4, item.getPrecioUnitario());
                    psLinea.addBatch();
                }
                psLinea.executeBatch();
            }

            conn.commit();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Lista todas las devoluciones con sus líneas.
     */
    public static List<Devolucion> listarTodas() {
        List<Devolucion> resultados = new ArrayList<>();
        String sqlDev = "SELECT * FROM Devolucion";
        String sqlLineas = "SELECT * FROM LineaDevolucion WHERE idDevolucion = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement psDev = conn.prepareStatement(sqlDev);
             ResultSet rsDev = psDev.executeQuery()) {

            while (rsDev.next()) {
                int id = rsDev.getInt("id");
                int idVenta = rsDev.getInt("idVenta");
                Date fecha = rsDev.getDate("fecha");
                int idUsuario = rsDev.getInt("idUsuario");
                String motivo = rsDev.getString("motivo");
                double total = rsDev.getDouble("total");

                // Construir objeto Usuario mínimo (solo id)
                Usuario usuario = new Usuario(idUsuario, "", "", null);

                // Cargar líneas de detalle
                List<LineaDevolucion> items = new ArrayList<>();
                try (PreparedStatement psLinea = conn.prepareStatement(sqlLineas)) {
                    psLinea.setInt(1, id);
                    try (ResultSet rsLinea = psLinea.executeQuery()) {
                        while (rsLinea.next()) {
                            int idLinea = rsLinea.getInt("id");
                            String codigo = rsLinea.getString("codigoProducto");
                            int cantidad = rsLinea.getInt("cantidad");
                            double precio = rsLinea.getDouble("precioUnitario");

                            Producto producto = ProductoDAO.buscarPorCodigo(codigo);
                            LineaDevolucion linea = new LineaDevolucion(idLinea, id, producto, cantidad, precio);
                            items.add(linea);
                        }
                    }
                }

                Devolucion dev = new Devolucion(id, idVenta, fecha, usuario, motivo, total, items);
                resultados.add(dev);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return resultados;
    }

    /**
     * Busca una devolución por su ID.
     */
    public static Devolucion buscarPorId(int idDev) {
        String sqlDev = "SELECT * FROM Devolucion WHERE id = ?";
        String sqlLineas = "SELECT * FROM LineaDevolucion WHERE idDevolucion = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement psDev = conn.prepareStatement(sqlDev)) {

            psDev.setInt(1, idDev);
            try (ResultSet rsDev = psDev.executeQuery()) {
                if (rsDev.next()) {
                    int id = rsDev.getInt("id");
                    int idVenta = rsDev.getInt("idVenta");
                    Date fecha = rsDev.getDate("fecha");
                    int idUsuario = rsDev.getInt("idUsuario");
                    String motivo = rsDev.getString("motivo");
                    double total = rsDev.getDouble("total");

                    Usuario usuario = new Usuario(idUsuario, "", "", null);

                    List<LineaDevolucion> items = new ArrayList<>();
                    try (PreparedStatement psLinea = conn.prepareStatement(sqlLineas)) {
                        psLinea.setInt(1, id);
                        try (ResultSet rsLinea = psLinea.executeQuery()) {
                            while (rsLinea.next()) {
                                int idLinea = rsLinea.getInt("id");
                                String codigo = rsLinea.getString("codigoProducto");
                                int cantidad = rsLinea.getInt("cantidad");
                                double precio = rsLinea.getDouble("precioUnitario");

                                Producto producto = ProductoDAO.buscarPorCodigo(codigo);
                                LineaDevolucion linea = new LineaDevolucion(idLinea, id, producto, cantidad, precio);
                                items.add(linea);
                            }
                        }
                    }

                    return new Devolucion(id, idVenta, fecha, usuario, motivo, total, items);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
