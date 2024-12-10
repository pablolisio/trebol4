package com.proit.modelo.auxiliar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.proit.modelo.ventas.SolicitudFacturaVenta;
import com.proit.utils.Utils;

public class SolicitudFacturaVentaExcel {
	
	private String fecha;
	
	private String nro;
	
	private String solicitante;
		
	private String tipoFactura;
	
	private double totalSinIVA;
	
	
	public String getFecha() {
		return fecha;
	}


	public void setFecha(String fecha) {
		this.fecha = fecha;
	}


	public String getNro() {
		return nro;
	}


	public void setNro(String nro) {
		this.nro = nro;
	}


	public String getSolicitante() {
		return solicitante;
	}


	public void setSolicitante(String solicitante) {
		this.solicitante = solicitante;
	}


	public String getTipoFactura() {
		return tipoFactura;
	}


	public void setTipoFactura(String tipoFactura) {
		this.tipoFactura = tipoFactura;
	}


	public double getTotalSinIVA() {
		return totalSinIVA;
	}


	public void setTotalSinIVA(double totalSinIVA) {
		this.totalSinIVA = totalSinIVA;
	}


	public void convertFromSolicitudFactura(SolicitudFacturaVenta solicitud) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		setFecha(dateFormat.format(solicitud.getFecha().getTime()));
		setNro(solicitud.getNro());
		setSolicitante(solicitud.getUsuarioSolicitante().getNombreCompleto());
		setTipoFactura(solicitud.getTipoFactura().getNombreCorto());
		double subtotal = Utils.round(solicitud.calculateSubtotal(),2);
		if (solicitud.isNotaCredito()) {
			subtotal = subtotal * (-1);
		}
		setTotalSinIVA(subtotal);
	}

}
