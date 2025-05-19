package Persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:sqlserver://localhost:14330;"
            + "databaseName=POS;encrypt=true;trustServerCertificate=true";

    private static String usuario;
    private static String contrasena;

    // Cargamos el driver JDBC al inicializar la clase
    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC no encontrado: " + e.getMessage());
        }
    }

    /**
     * Debe llamarse desde LoginController antes de obtener la conexión.
     */
    public static void establecerCredenciales(String usuarioIngresado, String contrasenaIngresada) {
        usuario = usuarioIngresado;
        contrasena = contrasenaIngresada;
    }

    /**
     * Obtiene la conexión a la base de datos usando las credenciales establecidas.
     */
    public static Connection obtenerConexion() throws SQLException {
        if (usuario == null || contrasena == null) {
            throw new SQLException("❌ Usuario o contraseña no establecidos. ¿Llamaste a establecerCredenciales()?");
        }

        try {
            return DriverManager.getConnection(URL, usuario, contrasena);
        } catch (SQLException e) {
            System.err.println("Error al conectar como '" + usuario + "': " + e.getMessage());
            throw e;
        }
    }

    /**
     * Método alias para facilitar la integración en DAO: getConnection().
     */
    public static Connection getConnection() throws SQLException {
        return obtenerConexion();
    }

    /**
     * Método de prueba rápida: imprime en consola si la conexión funciona.
     */
    public static void probarConexion() {
        try (Connection conn = obtenerConexion()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Conexión exitosa a la BD como '" + usuario + "'.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al probar conexión: " + e.getMessage());
        }
    }
}
