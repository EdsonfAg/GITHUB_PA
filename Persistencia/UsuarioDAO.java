package Persistencia;

import Modelo.Usuario;
import Modelo.Rol;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DAO para operaciones CRUD de usuario, incluyendo asignación de roles desde la UI.
 */
public class UsuarioDAO {

    /**
     * Busca un usuario completo (incluyendo rol) por su username.
     */
    public static Usuario buscarPorUsername(String username) {
        String sql =
            "SELECT id, nombre, username, passwordHash, rol " +
            "FROM dbo.Usuario " +
            "WHERE username = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("username"),
                        rs.getString("passwordHash"),
                        // El rol principal de la columna 'rol'
                        Collections.singletonList(Rol.valueOf(rs.getString("rol")))
                    );
                    // Cargar roles adicionales desde la tabla de unión, si existen
                    List<Rol> adicionales = new UsuarioDAO().getRolesByUsuario(u.getId());
                    if (!adicionales.isEmpty()) {
                        List<Rol> todos = new ArrayList<>(u.getRoles());
                        todos.addAll(adicionales);
                        u.setRoles(todos);
                    }
                    return u;
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error en UsuarioDAO.buscarPorUsername: " + ex.getMessage());
        }
        return null;
    }

    /**
     * Inserta un nuevo usuario y asigna el/los roles desde la UI.
     */
    public static boolean insertar(Usuario nuevo) throws SQLException {
        String sqlInsertUser =
            "INSERT INTO dbo.Usuario (nombre, username, passwordHash, rol) VALUES (?, ?, ?, ?)";
        String sqlInsertUserRole =
            "INSERT INTO dbo.UsuarioRol (idUsuario, idRol) VALUES (?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement psUser = conn.prepareStatement(sqlInsertUser, Statement.RETURN_GENERATED_KEYS)) {

            conn.setAutoCommit(false);
            // Campos base
            psUser.setString(1, nuevo.getNombre());
            psUser.setString(2, nuevo.getUsername());
            psUser.setString(3, nuevo.getPasswordHash());
            // Campo 'rol' de la tabla Usuario según el primer rol de la lista
            Rol rolPrincipal = nuevo.getRoles().get(0);
            psUser.setString(4, rolPrincipal.name());

            int affected = psUser.executeUpdate();
            if (affected == 0) {
                conn.rollback();
                return false;
            }

            // Obtener ID generado
            try (ResultSet genKeys = psUser.getGeneratedKeys()) {
                if (!genKeys.next()) {
                    conn.rollback();
                    return false;
                }
                nuevo.setId(genKeys.getInt(1));
            }

            // Insertar roles en tabla de unión, si hay más de uno
            try (PreparedStatement psUR = conn.prepareStatement(sqlInsertUserRole)) {
                for (Rol r : nuevo.getRoles()) {
                    psUR.setInt(1, nuevo.getId());
                    psUR.setInt(2, r.getId());
                    psUR.addBatch();
                }
                psUR.executeBatch();
            }

            conn.commit();
            return true;
        }
    }

    /**
     * Recupera roles desde la tabla de unión para un usuario dado.
     */
    public List<Rol> getRolesByUsuario(int idUsuario) throws SQLException {
        List<Rol> roles = new ArrayList<>();
        String sql =
            "SELECT r.nombreRol " +
            "FROM dbo.UsuarioRol ur " +
            "JOIN dbo.Rol r ON ur.idRol = r.idRol " +
            "WHERE ur.idUsuario = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    roles.add(Rol.valueOf(rs.getString("nombreRol")));
                }
            }
        }
        return roles;
    }
}
