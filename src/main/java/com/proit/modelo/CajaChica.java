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

import com.proit.utils.Utils;

/**
 * Esta clase representa la caja chica.
 * Los detalles serán cargados para un mes determinado. 
 */
@Entity
@Table(name="caja_chica")
public class CajaChica extends EntidadGenerica implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(columnDefinition="timestamp without time zone", nullable = false)
	private Calendar fecha;
	
	@Column(nullable = false)
	private String detalle;

	@Column(columnDefinition="numeric", nullable = false)
	private double monto;
	
	@Column(columnDefinition="date", nullable = false)
	private Calendar mes;
	
	@Column(name = "solicitado_por_otros" , nullable = false)
	private boolean solicitadoPorOtros;
	
	@Column(name = "solicitado_por_todos" , nullable = false)
	private boolean solicitadoPorTodos;
	
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="usuario_solicitante_fk"), name="usuario_solicitante_id")
	private Usuario usuarioSolicitante;

	public Calendar getFecha() {
		return fecha;
	}

	public void setFecha(Calendar fecha) {
		this.fecha = fecha;
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

	public String getDetalle() {
		return detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public Calendar getMes() {
		return mes;
	}

	public void setMes(Calendar mes) {
		mes = Utils.firstMillisecondOfDay((Calendar)mes.clone());
		mes.set(Calendar.DAY_OF_MONTH,1);
		this.mes = mes;
	}

	public boolean isSolicitadoPorOtros() {
		return solicitadoPorOtros;
	}

	public void setSolicitadoPorOtros(boolean solicitadoPorOtros) {
		this.solicitadoPorOtros = solicitadoPorOtros;
	}

	public boolean isSolicitadoPorTodos() {
		return solicitadoPorTodos;
	}

	public void setSolicitadoPorTodos(boolean solicitadoPorTodos) {
		this.solicitadoPorTodos = solicitadoPorTodos;
	}

	public Usuario getUsuarioSolicitante() {
		return usuarioSolicitante;
	}

	public void setUsuarioSolicitante(Usuario usuarioSolicitante) {
		this.usuarioSolicitante = usuarioSolicitante;
	}

	@Override
	public String toString(){
		DateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
		DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
		String texto = "Caja Chica id: "+ id + ", ";
		texto += "fecha: " + dateFormat2.format(fecha.getTime()) + ", ";
		texto += "detalle: " + detalle + ", ";
		texto += "monto: " + monto + ", ";
		texto += "mes: " + dateFormat.format(mes.getTime()) + ", ";
		if (solicitadoPorOtros) {
			texto += "solicitante: Otros";
		} else if (solicitadoPorTodos) {
			texto += "solicitante: Todos";
		} else if (usuarioSolicitante!=null) {
			texto += "solicitante: " + usuarioSolicitante.getNombreCompleto();
		}
		return texto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CajaChica){
			CajaChica other = (CajaChica) obj;
			return ((this.getDetalle() == null && other.getDetalle() == null) || (this.getDetalle() != null && this.getDetalle().equals(other.getDetalle())))
				&& ((this.getFecha() == null && other.getFecha() == null) || (this.getFecha() != null && this.getFecha().equals(other.getFecha())))
				&& this.getMes().equals(other.getMes())
				&& this.isBorrado()==other.isBorrado();
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode(){
	    return (int) getDetalle().hashCode() * 
	    		getFecha().hashCode() *
	    		getMes().hashCode();
	}

}
