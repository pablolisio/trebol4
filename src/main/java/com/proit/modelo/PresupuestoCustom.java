package com.proit.modelo;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Esta clase representa un item custom de presupuesto.
 */
@Entity
@Table(name="presupuesto_custom")
public class PresupuestoCustom extends EntidadGenerica implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(nullable = false)
	private String detalle;
	
	@Column(columnDefinition="timestamp without time zone", nullable = false)
	private Calendar fecha;
	
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="banco_fk"), name="banco_id")
	private Banco banco;
	

	@Column(columnDefinition="numeric", nullable = false)
	private double importe;
	
	@Column(nullable = false)
	private boolean debitado;	
	
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

	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}

	public boolean isDebitado() {
		return debitado;
	}

	public void setDebitado(boolean debitado) {
		this.debitado = debitado;
	}

		//Estos dos metodos estan creados para ser usados las fechas como Date en vez de Calendar
		public Date getFechaAsDate() {
			if (fecha != null) {
				return fecha.getTime();
			}
			return null;
		}

		public void setFechaAsDate(Date fechaDate) {
			this.fecha = Calendar.getInstance();
			this.fecha.setTime(fechaDate);
		}

	

	@Override
	public String toString(){
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String texto = "Presupuesto Custom id: "+ id + ", ";
		texto += "fecha: " + dateFormat.format(fecha.getTime()) + ", ";
		texto += "detalle: " + detalle + ", ";
		texto += "banco: " + banco.getNombre() + ", ";
		texto += "importe: " + importe + ", ";
		texto += "debitado: " + debitado;
		return texto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CajaChica){
			PresupuestoCustom other = (PresupuestoCustom) obj;
			return ((this.getDetalle() == null && other.getDetalle() == null) || (this.getDetalle() != null && this.getDetalle().equals(other.getDetalle())))
				&& ((this.getFecha() == null && other.getFecha() == null) || (this.getFecha() != null && this.getFecha().equals(other.getFecha())))
				&& this.isBorrado()==other.isBorrado();
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode(){
	    return (int) getDetalle().hashCode() * 
	    		getFecha().hashCode();
	}

}
