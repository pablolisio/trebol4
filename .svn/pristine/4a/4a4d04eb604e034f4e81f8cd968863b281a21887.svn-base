package com.proit.modelo;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 
 * Esta clase representa la carga de horas que me lleva desarrollar el sistema.
 */
@Entity
@Table(name="carga_hs")
public class CargaHoras extends EntidadGenerica implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private String detalle;
	
	@Column(columnDefinition="timestamp without time zone", nullable = false)
	private Calendar fecha;
	
	@Column(columnDefinition="numeric", nullable = false)
	private double monto;

	
	public String getDetalle() {
		return detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	public Calendar getFecha() {
		return fecha;
	}

	public void setFecha(Calendar fecha) {
		this.fecha = fecha;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}
	
}
