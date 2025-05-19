package Modelo;

import java.util.List;

/**
 * Modelo de usuario para la aplicación POS.
 */
public class Usuario {
    private int id;
    private String nombre;
    private String username;
    private String passwordHash;
    private List<Rol> roles;

    /**
     * Constructor usado por UsuarioDAO al recuperar desde BD.
     */
    public Usuario(int id, String nombre, String username, String passwordHash) {
        this.id = id;
        this.nombre = nombre;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    /**
     * Constructor completo con roles (registro o actualizaciones).
     */
    public Usuario(int id, String nombre, String username, String passwordHash, List<Rol> roles) {
        this(id, nombre, username, passwordHash);
        this.roles = roles;
    }

    /**
     * Constructor para registrar un nuevo usuario (sin id aún).
     */
    public Usuario(String nombre, String username, String passwordHash, List<Rol> roles) {
        this(0, nombre, username, passwordHash, roles);
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public List<Rol> getRoles() {
        return roles;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }
}
