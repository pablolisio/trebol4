package com.proit.modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * 
 * Esta clase representa un Tipo de Cuenta, Ejemplo: Caja de Ahorro, Cuenta Corriente, etc.
 */
@Entity(name="tipo_cuenta")
public class TipoCuenta extends EntidadGenerica implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Transient
	public static final TipoCuenta CA = new TipoCuenta(1, "Caja de Ahorro", "", false);
	@Transient
	public static final TipoCuenta CC = new TipoCuenta(2, "Cuenta Corriente", "", false);
	
	private String nombre;
	
	private String descripcion;
	
	public TipoCuenta() {
	}
	
	public TipoCuenta(int id, String nombre, String descripcion, boolean borrado) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.borrado = borrado;
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
		String texto = "Tipo Cuenta id: "+ id + ", ";
		texto += "nombre: " + nombre + ", ";
		texto += "descripcion: " + descripcion + ", ";
		return texto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TipoCuenta){
			TipoCuenta other = (TipoCuenta) obj;
			return this.getNombre().equals(other.getNombre())
				&& ((this.getDescripcion() == null && other.getDescripcion() == null) || (this.getDescripcion() != null && this.getDescripcion().equals(other.getDescripcion())));
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode(){
	    return (int) getNombre().hashCode() *
	                (getDescripcion()==null?0:getDescripcion().hashCode());
	}
	
	
}