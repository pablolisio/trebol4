package com.proit.modelo.compras;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.proit.modelo.EntidadGenerica;

/**
 * 
 * Esta clase representa un Plan de Cuentas. 
 * Las OPs (los 3 tipos) van a tener asignado un plan de cuentas. 
 * Ejemplos son: honorarios, gastos de oficina, sueldos, insumos, fletes.
 */
@Entity(name="plan_cuenta")
public class PlanCuenta extends EntidadGenerica implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(nullable = false)
	private String nombre;
	
	private String descripcion;
	
	public PlanCuenta() {
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@Override
	public String toString(){
		String texto = "Plan Cuenta id: "+ id + ", ";
		texto += "nombre: " + nombre + ", ";
		texto += "descripcion: " + descripcion + ", ";
		return texto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PlanCuenta){
			PlanCuenta other = (PlanCuenta) obj;
			return this.getNombre().equals(other.getNombre());
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode(){
	    return (int) getNombre().hashCode();
	}
	
	
}