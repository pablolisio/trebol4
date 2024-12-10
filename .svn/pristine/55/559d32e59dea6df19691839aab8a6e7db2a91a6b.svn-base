package com.proit.modelo.ventas;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.proit.modelo.Banco;
import com.proit.modelo.EntidadGenerica;
import com.proit.modelo.ModoPago;

/**
 * 
 * Esta clase representa un cobro. El mismo puede ser en Efectivo, Cheque o Transferencia.
 */
@Entity
@Table(name="cobro")
public class Cobro extends EntidadGenerica implements Serializable {

	private static final long serialVersionUID = 1L;

	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="modo_cobro_fk"), name="modo_cobro_id", nullable = false)
	private ModoPago modoCobro;
	
	@Column(name="banco_cheque")
	private String bancoCheque;
	
	@Column(name="nro_cheque")
	private String nroCheque;
		
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="banco_transferencia_fk"), name="banco_transferencia_id")
	private Banco bancoTransferencia;
			
	@Column(columnDefinition="numeric", nullable = false)
	private double importe;
	
	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="cobranza_fk"), name="cobranza_id", nullable = false)
	private Cobranza cobranza;	
	
	/**
	 * Utilizada como fecha de cobro.
	 * Ejemplo: Para cheque es la fecha de "cheque al". Para los demas tipos, es la fecha en que se tendria que realizar el cobro
	 */
	@Column(columnDefinition="timestamp without time zone")
	private Calendar fecha;
	
	public ModoPago getModoCobro() {
		return modoCobro;
	}

	public void setModoCobro(ModoPago modoCobro) {
		this.modoCobro = modoCobro;
	}

	public String getBancoCheque() {
		return bancoCheque;
	}

	public void setBancoCheque(String bancoCheque) {
		this.bancoCheque = bancoCheque;
	}

	public String getNroCheque() {
		return nroCheque;
	}

	public void setNroCheque(String nroCheque) {
		this.nroCheque = nroCheque;
	}

	public Banco getBancoTransferencia() {
		return bancoTransferencia;
	}

	public void setBancoTransferencia(Banco bancoTransferencia) {
		this.bancoTransferencia = bancoTransferencia;
	}

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}

	public Cobranza getCobranza() {
		return cobranza;
	}

	public void setCobranza(Cobranza cobranza) {
		this.cobranza = cobranza;
	}

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
	
	//Metodos para saber que tipo de cobro es
	public boolean isSF(){
		return modoCobro.equals(ModoPago.SF);
	}
	
	public boolean isEfectivo(){
		return modoCobro.equals(ModoPago.EFECTIVO);
	}

	public boolean isCheque(){
		return modoCobro.equals(ModoPago.CHEQUE);
	}

	public boolean isTransferencia(){
		return modoCobro.equals(ModoPago.TRANSFERENCIA);
	}


	@Override
	public String toString(){
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String texto = "Cobro id: "+ id + ", ";
		texto += "modoCobro: " + modoCobro.getNombre()+ ", ";
		texto += "nroCheque: " + nroCheque + ", ";
		texto += "bancoCheque: " + bancoCheque + ", ";
		if (fecha!=null) {
			texto += "fecha: " + dateFormat.format(fecha.getTime()) + ", ";
		} else {
			texto += "fecha: NULL, ";
		}
		if (bancoTransferencia!=null) {
			texto += "bancoTransferencia: " + bancoTransferencia.getNombre() + ", ";
		} else {
			texto += "bancoTransferencia: NULL, ";
		}
		texto += "importe: " + importe;
		return texto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Cobro){
			Cobro other = (Cobro) obj;
			return ((this.getModoCobro() == null && other.getModoCobro() == null) || (this.getModoCobro() != null && this.getModoCobro().equals(other.getModoCobro())))
				&&	((this.getNroCheque() == null && other.getNroCheque() == null) || (this.getNroCheque() != null && this.getNroCheque().equals(other.getNroCheque())))
				&&	((this.getCobranza() == null && other.getCobranza() == null) || (this.getCobranza() != null && this.getCobranza().getId() == (other.getCobranza().getId())))
				&& this.isBorrado()==other.isBorrado();
		} else {
			return false;
		}
	}

}
