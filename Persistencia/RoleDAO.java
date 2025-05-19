package Persistencia;

import Modelo.Rol;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO {

    /** Recupera todos los roles definidos en la BD. */
    public List<Rol> getAllRoles() throws SQLException {
        List<Rol> roles = new ArrayList<>();
        String sql = "SELECT idRol, nombreRol FROM dbo.Rol";

        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id   = rs.getInt("idRol");
                // String name = rs.getString("nombreRol"); // ya no necesitamos el nombre
                roles.add(Rol.fromId(id));
            }
        }
        return roles;
    }

    /** Busca un rol por su nombre */
    public Rol getRoleByName(String nombreRol) throws SQLException {
        String sql = "SELECT idRol FROM dbo.Rol WHERE nombreRol = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombreRol);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Rol.fromId(rs.getInt("idRol"));
                }
                return null;
            }
        }
    }

    /** Inserta un rol nuevo en la BD */
    public void addRole(Rol rol) throws SQLException {
        String sql = "INSERT INTO dbo.Rol (nombreRol) VALUES (?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rol.name());
            ps.executeUpdate();
        }
    }
}
