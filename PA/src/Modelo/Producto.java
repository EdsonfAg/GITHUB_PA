package Modelo;

class Producto extends ItemVenta implements ImpuestoAplicable {
    private int stock;

    public Producto(String codigo, String nombre, double precio, int stock) {
        super(codigo, nombre, precio); 
        this.stock = stock;
    }

    public int getStock() {
        return stock;
    }

    public void agregarStock(int cantidad) {
        this.stock += cantidad;
    }

    public boolean reducirStock(int cantidad) {
        if (this.stock >= cantidad) {
            this.stock -= cantidad;
            return true;
        } else {
            System.out.println("No hay suficiente stock para esta operación.");
            return false;
        }
    }

    @Override
    public double calcularIVA() {
        return this.getPrecio() * 0.16;
    }

    @Override
    public double calcularIEPS() {
        return this.getPrecio() * 0.08;
    }

    @Override
    public String toString() {
        return getCodigo() + "," + getNombre() + "," + getPrecio() + "," + stock;
    }
}
