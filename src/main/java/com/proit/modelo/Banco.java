package com.proit.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Esta clase representa una entidad bancaria 
 */
@Entity
public class Banco extends EntidadGenerica implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(nullable = false)
	private String nombre;
	
	@Column(nullable = false)
	private boolean actual;
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public boolean isActual() {
		return actual;
	}

	public void setActual(boolean actual) {
		this.actual = actual;
	}

	@Override
	public String toString(){
		String texto = "Banco id: "+ id + ", ";
		texto += "nombre: " + nombre + ", ";
		texto += "actual: " + actual;
		return texto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Banco){
			Banco other = (Banco) obj;
			return ((this.getNombre() == null && other.getNombre() == null) || (this.getNombre() != null && this.getNombre().equals(other.getNombre())))
				&& this.isBorrado()==other.isBorrado();
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode(){
	    return (int) getNombre().hashCode();
	}

}

