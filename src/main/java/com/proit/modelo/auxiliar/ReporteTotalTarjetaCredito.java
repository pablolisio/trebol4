package com.proit.modelo.auxiliar;

import java.io.Serializable;
import java.math.BigDecimal;

public class ReporteTotalTarjetaCredito implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String proveedor;
	private BigDecimal total;
	
	public String getProveedor() {
		return proveedor;
	}
	
	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}
	
	public BigDecimal getTotal() {
		return total;
	}
	
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
}
