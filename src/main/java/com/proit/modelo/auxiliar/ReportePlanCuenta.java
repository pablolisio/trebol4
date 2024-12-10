package com.proit.modelo.auxiliar;

import java.io.Serializable;

public class ReportePlanCuenta implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int idPlanCuenta;
	private String nombrePlanCuenta;
	private Double total;
	
	
	public int getIdPlanCuenta() {
		return idPlanCuenta;
	}
	public void setIdPlanCuenta(int idPlanCuenta) {
		this.idPlanCuenta = idPlanCuenta;
	}
	public String getNombrePlanCuenta() {
		return nombrePlanCuenta;
	}
	public void setNombrePlanCuenta(String nombrePlanCuenta) {
		this.nombrePlanCuenta = nombrePlanCuenta;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	
	
}
