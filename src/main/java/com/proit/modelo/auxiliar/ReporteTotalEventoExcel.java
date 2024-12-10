package com.proit.modelo.auxiliar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ReporteTotalEventoExcel {

	private String cliente;
	
	private String nombreEvento;
	
	private String fecha;
	
	private String responsable;
	
	private double ventaTotalSinIVA; 
	
	private double costoTotalSinIVA;
	
	private double costoTotalConIVA;
	
	private double pagado;
	
	private double saldoCostos;
	
	private String cumplido;
	
	private String cerrado;

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getNombreEvento() {
		return nombreEvento;
	}

	public void setNombreEvento(String evento) {
		this.nombreEvento = evento;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getResponsable() {
		return responsable;
	}

	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}

	public double getVentaTotalSinIVA() {
		return ventaTotalSinIVA;
	}

	public void setVentaTotalSinIVA(double ventaTotalSinIVA) {
		this.ventaTotalSinIVA = ventaTotalSinIVA;
	}

	public double getCostoTotalSinIVA() {
		return costoTotalSinIVA;
	}

	public void setCostoTotalSinIVA(double costoTotalSinIVA) {
		this.costoTotalSinIVA = costoTotalSinIVA;
	}

	public double getCostoTotalConIVA() {
		return costoTotalConIVA;
	}

	public void setCostoTotalConIVA(double costoTotalConIVA) {
		this.costoTotalConIVA = costoTotalConIVA;
	}

	public double getPagado() {
		return pagado;
	}

	public void setPagado(double pagado) {
		this.pagado = pagado;
	}

	public double getSaldoCostos() {
		return saldoCostos;
	}

	public void setSaldoCostos(double saldoCostos) {
		this.saldoCostos = saldoCostos;
	}

	public String getCumplido() {
		return cumplido;
	}

	public void setCumplido(String cumplido) {
		this.cumplido = cumplido;
	}

	public String getCerrado() {
		return cerrado;
	}

	public void setCerrado(String cerrado) {
		this.cerrado = cerrado;
	}
	
	public void convertFromReporteTotalEvento(ReporteTotalEvento reporteTotalEvento) {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String cli = reporteTotalEvento.getCliente()!=null?reporteTotalEvento.getCliente():"";
		String nomEv = reporteTotalEvento.getEvento()!=null?reporteTotalEvento.getEvento():"";
		String fecha = reporteTotalEvento.getFecha()!=null?dateFormat.format(reporteTotalEvento.getFecha().getTime()):"-";
		String resp = reporteTotalEvento.getResponsableEvento();
		resp = resp!=null ? resp.substring(0,resp.length()-"@trebol4.com".length()) : "-"; //Uso solo el principio del email
		String cumplido = reporteTotalEvento.isCumplido() ? "Si" : "No";
		String cerrado = reporteTotalEvento.isCerrado() ? "Si" : "No";
		
		setCliente(cli);
		setNombreEvento(nomEv);
		setFecha(fecha);
		setResponsable(resp);
		setVentaTotalSinIVA(reporteTotalEvento.getTotalEvento().doubleValue());
		setCostoTotalSinIVA(reporteTotalEvento.getCostoTotal().doubleValue());
		setCostoTotalConIVA(reporteTotalEvento.getCostoTotalConIVA().doubleValue());
		setPagado(reporteTotalEvento.getPagadoTotal().doubleValue());
		setSaldoCostos(reporteTotalEvento.getPendienteTotal().doubleValue());
		setCumplido(cumplido);
		setCerrado(cerrado);
	}
}
