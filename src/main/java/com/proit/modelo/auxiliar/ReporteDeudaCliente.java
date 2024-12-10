package com.proit.modelo.auxiliar;

import java.io.Serializable;
import java.math.BigDecimal;

public class ReporteDeudaCliente implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String cliente;
	private BigDecimal subtotalAnioAnterior;
	private BigDecimal subtotalAnioActual;
	private BigDecimal subtotalPendiente;
	private BigDecimal total;
	
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public BigDecimal getSubtotalAnioAnterior() {
		return subtotalAnioAnterior;
	}
	public void setSubtotalAnioAnterior(BigDecimal subtotalAnioAnterior) {
		this.subtotalAnioAnterior = subtotalAnioAnterior;
	}
	public BigDecimal getSubtotalAnioActual() {
		return subtotalAnioActual;
	}
	public void setSubtotalAnioActual(BigDecimal subtotalAnioActual) {
		this.subtotalAnioActual = subtotalAnioActual;
	}
	public BigDecimal getSubtotalPendiente() {
		return subtotalPendiente;
	}
	public void setSubtotalPendiente(BigDecimal subtotalPendiente) {
		this.subtotalPendiente = subtotalPendiente;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
			
}
