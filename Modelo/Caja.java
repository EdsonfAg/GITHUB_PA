package Modelo;

import java.util.Date;
import java.util.List;

public class Caja {
    private int id;
    private Usuario usuario;
    private Date fechaApertura;
    private Date fechaCierre;
    private double montoInicial;
    private double montoFinal;
    private List<MovimientoCaja> movimientos;

    // Constructor BD
    public Caja(int id, Usuario usuario, Date fechaApertura, Date fechaCierre,
                double montoInicial, double montoFinal, List<MovimientoCaja> movimientos) {
        this.id = id;
        this.usuario = usuario;
        this.fechaApertura = fechaApertura;
        this.fechaCierre = fechaCierre;
        this.montoInicial = montoInicial;
        this.montoFinal = montoFinal;
        this.movimientos = movimientos;
    }

    // Constructor apertura
    public Caja(Usuario usuario, double montoInicial) {
        this(0, usuario, new Date(), null, montoInicial, 0.0, null);
    }

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the usuario
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the fechaApertura
	 */
	public Date getFechaApertura() {
		return fechaApertura;
	}

	/**
	 * @param fechaApertura the fechaApertura to set
	 */
	public void setFechaApertura(Date fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

	/**
	 * @return the fechaCierre
	 */
	public Date getFechaCierre() {
		return fechaCierre;
	}

	/**
	 * @param fechaCierre the fechaCierre to set
	 */
	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	/**
	 * @return the montoInicial
	 */
	public double getMontoInicial() {
		return montoInicial;
	}

	/**
	 * @param montoInicial the montoInicial to set
	 */
	public void setMontoInicial(double montoInicial) {
		this.montoInicial = montoInicial;
	}

	/**
	 * @return the montoFinal
	 */
	public double getMontoFinal() {
		return montoFinal;
	}

	/**
	 * @param montoFinal the montoFinal to set
	 */
	public void setMontoFinal(double montoFinal) {
		this.montoFinal = montoFinal;
	}

	/**
	 * @return the movimientos
	 */
	public List<MovimientoCaja> getMovimientos() {
		return movimientos;
	}

	/**
	 * @param movimientos the movimientos to set
	 */
	public void setMovimientos(List<MovimientoCaja> movimientos) {
		this.movimientos = movimientos;
	}

    // Getters y setters omitidos...
    

}

