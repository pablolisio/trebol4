package com.proit.modelo.ventas;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Transient;

import com.proit.modelo.EntidadGenerica;

/**
 * 
 * Esta clase representa un estado de la factura de venta.
 * Los mismos pueden ser: X COBRAR, COBRADO, COBRADO PARCIAL, ANULADO
 */
@Entity(name="estado_factura_venta")
public class EstadoFacturaVenta extends EntidadGenerica implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Transient
	public static final EstadoFacturaVenta X_COBRAR = new EstadoFacturaVenta(1, "X COBRAR", "Aun esta pendiente de cobro", false);
	@Transient
	public static final EstadoFacturaVenta COBRADO = new EstadoFacturaVenta(2, "COBRADO", "Ya cobrada", false);
	@Transient
	public static final EstadoFacturaVenta COBRADO_PARCIAL = new EstadoFacturaVenta(3, "COBRADO PARCIAL", "Ya cobrada (parcial)", false);
	@Transient
	public static final EstadoFacturaVenta ANULADO = new EstadoFacturaVenta(4, "ANULADO", "Anulada", false);
	
	private String nombre;
	
	private String descripcion;
	
	public EstadoFacturaVenta() {
	}
			
	public EstadoFacturaVenta(int id, String nombre, String descripcion, boolean borrado) {
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
		String texto = "Estado Factura Venta id: "+ id + ", ";
		texto += "nombre: " + nombre + ", ";
		texto += "descripcion: " + descripcion + ", ";
		return texto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EstadoFacturaVenta){
			EstadoFacturaVenta other = (EstadoFacturaVenta) obj;
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