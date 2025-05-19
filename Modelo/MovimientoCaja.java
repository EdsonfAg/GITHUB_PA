package Modelo;

import java.sql.Timestamp;

/**
 * Representa un registro de movimiento en la caja.
 */
public class MovimientoCaja {
    private int id;
    private int idCaja;
    private Timestamp fechaMovimiento;
    private String tipo;
    private String metodoPago;
    private double monto;
    private int idUsuario;

    /**
     * Constructor completo que refleja todas las columnas de la tabla MovimientoCaja.
     *
     * @param id                Identificador del movimiento (PK)
     * @param idCaja            FK a la caja asociada
     * @param fechaMovimiento   Timestamp del momento del movimiento
     * @param tipo              Tipo de movimiento (Venta, Apertura, Cierre...)
     * @param metodoPago        Método de pago (Efectivo, Tarjeta, QR...)
     * @param monto             Monto del movimiento
     * @param idUsuario         FK al usuario que realizó la operación
     */
    public MovimientoCaja(int id,
                          int idCaja,
                          Timestamp fechaMovimiento,
                          String tipo,
                          String metodoPago,
                          double monto,
                          int idUsuario) {
        this.id = id;
        this.idCaja = idCaja;
        this.fechaMovimiento = fechaMovimiento;
        this.tipo = tipo;
        this.metodoPago = metodoPago;
        this.monto = monto;
        this.idUsuario = idUsuario;
    }

    // Getters y setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdCaja() { return idCaja; }
    public void setIdCaja(int idCaja) { this.idCaja = idCaja; }

    public Timestamp getFechaMovimiento() { return fechaMovimiento; }
    public void setFechaMovimiento(Timestamp fechaMovimiento) { this.fechaMovimiento = fechaMovimiento; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
}
