package com.proit.modelo.auxiliar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.proit.modelo.compras.Pago;
import com.proit.utils.Utils;

public class ChequeExcel {
	
	private String banco;
	
	private String nro;
	
	private String fecha;
	
	private double importe;

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getNro() {
		return nro;
	}

	public void setNro(String nro) {
		this.nro = nro;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}
	
	public void convertFromPago(Pago pago) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		setBanco(pago.getBanco().getNombre());
		setFecha(dateFormat.format(pago.getFecha().getTime()));
		setImporte(Utils.round(pago.getImporte(),2));
		setNro(pago.getNroCheque());
		
	}

}
