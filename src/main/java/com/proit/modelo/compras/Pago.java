package com.proit.modelo.compras;

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
 * Esta clase representa un pago. El mismo puede ser en Efectivo, Cheque o Transferencia.
 */
@Entity
@Table(name="pago")
public class Pago extends EntidadGenerica implements Serializable {

	private static final long serialVersionUID = 1L;

	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="modo_pago_fk"), name="modo_pago_id", nullable = false)
	private ModoPago modoPago;
	
	@Column(name="nro_cheque")
	private String nroCheque;
	
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="banco_fk"))
	private Banco banco;
	
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="cobro_alternativo_fk"), name="cobro_alternativo_id")
	private CobroAlternativo cobroAlternativo;
	
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="cuenta_bancaria_fk"), name="cuenta_bancaria_id")
	private CuentaBancaria cuentaBancaria;
	
	@Column(name = "cuit_cuil")
	private String cuitCuil;
	
	@Column(columnDefinition="numeric", nullable = false)
	private double importe;
	
	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="orden_pago_fk"), name="orden_pago_id", nullable = false)
	private OrdenPago ordenPago;
	
	/**
	 * Utilizada como fecha de pago.
	 * Ejemplo: Para cheque es la fecha de "cheque al". Para los demas tipos, es la fecha en que se tendria que realizar el pago
	 */
	@Column(columnDefinition="timestamp without time zone")
	private Calendar fecha;
	
	@Column(nullable=false)
	private boolean debitado;
	
	public ModoPago getModoPago() {
		return modoPago;
	}

	/**
	 * Ademas de setear el modo, tambien se setea el debitado por defecto
	 * @param modoPago
	 */
	public void setModoPago(ModoPago modoPago) {
		this.modoPago = modoPago;
		if (modoPago.equals(ModoPago.CHEQUE)||modoPago.equals(ModoPago.TRANSFERENCIA)||modoPago.equals(ModoPago.TRANSFERENCIA_SIN_PROV)||modoPago.equals(ModoPago.TRANSFERENCIA_3RO)) {
			setDebitado(false);
		} else {
			setDebitado(true);
		}
	}

	public String getNroCheque() {
		return nroCheque;
	}

	public void setNroCheque(String nroCheque) {
		this.nroCheque = nroCheque;
	}

	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public CobroAlternativo getCobroAlternativo() {
		return cobroAlternativo;
	}

	public void setCobroAlternativo(CobroAlternativo cobroAlternativo) {
		this.cobroAlternativo = cobroAlternativo;
	}

	public CuentaBancaria getCuentaBancaria() {
		return cuentaBancaria;
	}

	public void setCuentaBancaria(CuentaBancaria cuentaBancaria) {
		this.cuentaBancaria = cuentaBancaria;
	}

	public String getCuitCuil() {
		return cuitCuil;
	}

	public void setCuitCuil(String cuitCuil) {
		this.cuitCuil = cuitCuil;
	}

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}

	public OrdenPago getOrdenPago() {
		return ordenPago;
	}

	public void setOrdenPago(OrdenPago ordenPago) {
		this.ordenPago = ordenPago;
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
	
	//Metodos para saber que tipo de pago es
	public boolean isSF(){
		return modoPago.equals(ModoPago.SF);
	}
	
	public boolean isEfectivo(){
		return modoPago.equals(ModoPago.EFECTIVO);
	}

	public boolean isCheque(){
		return modoPago.equals(ModoPago.CHEQUE);
	}

	public boolean isDebitado() {
		return debitado;
	}

	public void setDebitado(boolean debitado) {
		this.debitado = debitado;
	}

	public boolean isTransferencia(){
		return modoPago.equals(ModoPago.TRANSFERENCIA);
	}

	public boolean isTransferencia3ro(){
		return modoPago.equals(ModoPago.TRANSFERENCIA_3RO);
	}
	
	public boolean isTransferenciaSinProv(){
		return modoPago.equals(ModoPago.TRANSFERENCIA_SIN_PROV);
	}
	
	public boolean isTarjetaCredito(){
		return modoPago.equals(ModoPago.TARJETA_CRED);
	}

	@Override
	public String toString(){
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String texto = "Pago id: "+ id + ", ";
		texto += "modoPago: " + modoPago.getNombre()+ ", ";
		texto += "nroCheque: " + nroCheque + ", ";
		if (banco!=null) {
			texto += "banco: " + banco.getNombre() + ", ";
		} else {
			texto += "banco: NULL, ";
		}
		if (fecha!=null) {
			texto += "fecha: " + dateFormat.format(fecha.getTime()) + ", ";
		} else {
			texto += "fecha: NULL, ";
		}
		if (cobroAlternativo!=null){
			texto += "cobroAlternativo: " + cobroAlternativo.getTitular() + ", ";
		} else {
			texto += "cobroAlternativo: NULL, ";
		}
		if (cuentaBancaria!=null){
			texto += "cuentaBancaria: {" + cuentaBancaria + "},";
		} else {
			texto += "cuentaBancaria: NULL, ";
		}
		texto += "cuitCuil: " + cuitCuil + ",";
		texto += "importe: " + importe + ",";
		texto += "debitado: " + debitado;
		return texto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Pago){
			Pago other = (Pago) obj;
			return ((this.getModoPago() == null && other.getModoPago() == null) || (this.getModoPago() != null && this.getModoPago().equals(other.getModoPago())))
				&&	((this.getNroCheque() == null && other.getNroCheque() == null) || (this.getNroCheque() != null && this.getNroCheque().equals(other.getNroCheque())))
				&&	((this.getCobroAlternativo() == null && other.getCobroAlternativo() == null) || (this.getCobroAlternativo() != null && this.getCobroAlternativo().equals(other.getCobroAlternativo())))
				&&	((this.getCuentaBancaria() == null && other.getCuentaBancaria() == null) || (this.getCuentaBancaria() != null && this.getCuentaBancaria().equals(other.getCuentaBancaria())))
				&&	((this.getOrdenPago() == null && other.getOrdenPago() == null) || (this.getOrdenPago() != null && this.getOrdenPago().getId() == (other.getOrdenPago().getId())))
				&& this.isBorrado()==other.isBorrado();
		} else {
			return false;
		}
	}

}
