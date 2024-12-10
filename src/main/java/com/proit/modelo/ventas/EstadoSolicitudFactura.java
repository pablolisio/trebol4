package com.proit.modelo.ventas;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Transient;

import com.proit.modelo.EntidadGenerica;

/**
 * 
 * Esta clase representa un estado de la solicitud de factura venta.
 * Los mismos pueden ser: Pendiente, Cumplida, Rechazada
 */
@Entity(name="estado_solicitud_factura")
public class EstadoSolicitudFactura extends EntidadGenerica implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Transient
	public static final EstadoSolicitudFactura PENDIENTE = new EstadoSolicitudFactura(1, "Pendiente", "Pendiente", false);
	@Transient
	public static final EstadoSolicitudFactura CUMPLIDA = new EstadoSolicitudFactura(2, "Cumplida", "La factura venta fue creada a partir de la solicitud de factura venta", false);
	@Transient
	public static final EstadoSolicitudFactura RECHAZADA = new EstadoSolicitudFactura(3, "Rechazada", "Caso en que se rechaza la solicitud de factura venta", false);
	
	private String nombre;
	
	private String descripcion;
	
	public EstadoSolicitudFactura() {
	}
			
	public EstadoSolicitudFactura(int id, String nombre, String descripcion, boolean borrado) {
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
		String texto = "Estado Solicitud Factura id: "+ id + ", ";
		texto += "nombre: " + nombre + ", ";
		texto += "descripcion: " + descripcion + ", ";
		return texto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EstadoSolicitudFactura){
			EstadoSolicitudFactura other = (EstadoSolicitudFactura) obj;
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