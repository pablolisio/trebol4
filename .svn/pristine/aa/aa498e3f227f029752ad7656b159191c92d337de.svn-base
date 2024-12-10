package com.proit.modelo.auxiliar;

import java.io.Serializable;
import java.math.BigDecimal;


public class ReporteTotalProveedor implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int proveedorId;
	private String razonSocial;
	private String cuitCuil;
	private BigDecimal totalDeuda;
	private BigDecimal totalPagado;
	private BigDecimal totalFacturas;
	
	public int getProveedorId() {
		return proveedorId;
	}
	public void setProveedorId(int proveedorId) {
		this.proveedorId = proveedorId;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public String getCuitCuil() {
		return cuitCuil;
	}
	public void setCuitCuil(String cuitCuil) {
		this.cuitCuil = cuitCuil;
	}
	public BigDecimal getTotalDeuda() {
		return totalDeuda;
	}
	public void setTotalDeuda(BigDecimal totalDeuda) {
		this.totalDeuda = totalDeuda;
	}
	public BigDecimal getTotalPagado() {
		return totalPagado;
	}
	public void setTotalPagado(BigDecimal totalPagado) {
		this.totalPagado = totalPagado;
	}
	public BigDecimal getTotalFacturas() {
		return totalFacturas;
	}
	public void setTotalFacturas(BigDecimal totalFacturas) {
		this.totalFacturas = totalFacturas;
	}
	
	
	public String getRazonSocialConCUIT() {
		return razonSocial + (cuitCuil!=null?" ("+cuitCuil + ")":"");
	}

}
