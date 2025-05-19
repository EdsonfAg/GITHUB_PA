package Modelo;

public enum Rol {
    ADMIN(1),
    CAJERO(2),
    SUPERVISOR(3);

    private final int id;
    
    Rol(int id) { 
    	this.id = id;
    	}
    public int getId() { 
    	return id;
    }
    

    /**
     * Devuelve el Rol correspondiente al ID de la BD.
     * Lanza IllegalArgumentException si no existe.
     */
    public static Rol fromId(int id) {
        for (Rol r : values()) {
            if (r.id == id) return r;
        }
        throw new IllegalArgumentException("Rol inválido para id=" + id);
    }
}
