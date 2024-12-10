package com.proit.modelo.auxiliar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.proit.modelo.ventas.FacturaVenta;
import com.proit.utils.Utils;

public class FacturaVentaExcel {
	
	private String fecha;
	
	private String cliente;
	
	private String evento;
	
	private String tipo;
	
	private String nro;
	
	private String estado;
	
	private double totalSinIVA;
	
	private double iva;
	
	private double total;

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getEvento() {
		return evento;
	}

	public void setEvento(String evento) {
		this.evento = evento;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNro() {
		return nro;
	}

	public void setNro(String nro) {
		this.nro = nro;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public double getTotalSinIVA() {
		return totalSinIVA;
	}

	public void setTotalSinIVA(double totalSinIVA) {
		this.totalSinIVA = totalSinIVA;
	}

	public double getIva() {
		return iva;
	}

	public void setIva(double iva) {
		this.iva = iva;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public void convertFromFactura(FacturaVenta factura) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		setFecha(dateFormat.format(factura.getFecha().getTime()));
		double subtotal = Utils.round(factura.calculateSubtotal(),2);
		double total = Utils.round(factura.calculateTotal(),2);
		double iva = Utils.round(total - subtotal,2);
		if (factura.isNotaCredito()) {
			subtotal = subtotal * (-1);
			iva = iva * (-1);
			total = total * (-1);
		}
		setTotalSinIVA(subtotal);
		setIva(iva);
		setTotal(total);
		setTipo(factura.getTipoFactura().getNombreCorto());
		setNro(factura.getNro());
		setCliente(factura.getCliente().getRazonSocial());
		setEvento(factura.getEvento().getNombre());
		setEstado(factura.getEstadoFacturaVenta().getNombre());
	}

}
