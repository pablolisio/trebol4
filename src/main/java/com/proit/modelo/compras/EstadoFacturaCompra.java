package com.proit.modelo.compras;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Transient;

import com.proit.modelo.EntidadGenerica;

/**
 * 
 * Esta clase representa un estado de la factura de compra.
 * Los mismos pueden ser: Pendiente de Pago, Cancelada, Pagada
 */
@Entity(name="estado_factura_compra")
public class EstadoFacturaCompra extends EntidadGenerica implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Transient
	public static final EstadoFacturaCompra PENDIENTE = new EstadoFacturaCompra(1, "Pendiente de Pago", "Pendiente de Pago", false);
	@Transient
	public static final EstadoFacturaCompra CANCELADA = new EstadoFacturaCompra(2, "Cancelada", "Cancelada", false);
	@Transient
	public static final EstadoFacturaCompra PAGADA_PARCIAL = new EstadoFacturaCompra(3, "Pagada Parcial", "Pagada Parcial", false);
	
	private String nombre;
	
	private String descripcion;
	
	public EstadoFacturaCompra() {
	}
			
	public EstadoFacturaCompra(int id, String nombre, String descripcion, boolean borrado) {
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
		String texto = "Estado Factura Compra id: "+ id + ", ";
		texto += "nombre: " + nombre + ", ";
		texto += "descripcion: " + descripcion + ", ";
		return texto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EstadoFacturaCompra){
			EstadoFacturaCompra other = (EstadoFacturaCompra) obj;
			return this.getNombre().equals(other.getNombre())
				&& ((this.getDescripcion() == null && other.getDescripcion() == null) || (this.getDescripcion() != null && this.getDescripcion().equals(other.getDescripcion())));
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode(){
	    return (int) getNombre().hashCode() *
	    		(getDescripcion()==null?0:getDescripcion().hashCode()) ;
	}
	
	
}