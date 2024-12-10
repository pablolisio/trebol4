package com.proit.modelo.auxiliar;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

public class ItemListaPresupuestoBanco implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int id;
	
	private String razonSocialProveedor;
	
	private String banco;
	
	private String nroCheque;
	
	private Calendar fecha;
	
	private BigDecimal importe;
	
	private boolean debitado;
	
	private boolean custom;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRazonSocialProveedor() {
		return razonSocialProveedor;
	}

	public void setRazonSocialProveedor(String razonSocialProveedor) {
		this.razonSocialProveedor = razonSocialProveedor;
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getNroCheque() {
		return nroCheque;
	}

	public void setNroCheque(String nroCheque) {
		this.nroCheque = nroCheque;
	}

	public Calendar getFecha() {
		return fecha;
	}

	public void setFecha(Calendar fecha) {
		this.fecha = fecha;
	}

	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	public boolean isDebitado() {
		return debitado;
	}

	public void setDebitado(boolean debitado) {
		this.debitado = debitado;
	}

	public boolean isCustom() {
		return custom;
	}

	public void setCustom(boolean custom) {
		this.custom = custom;
	}

}
