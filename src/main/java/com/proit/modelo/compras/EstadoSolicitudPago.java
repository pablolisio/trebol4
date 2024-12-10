package com.proit.modelo.compras;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Transient;

import com.proit.modelo.EntidadGenerica;

/**
 * 
 * Esta clase representa un estado de la solicitud de pago.
 * Los mismos pueden ser: Pendiente 1 (Crear Proveedor), Pendiente 2 (Crear Factura/s), Pendiente 3 (Crear OP), Cumplida, Rechazada
 */
@Entity(name="estado_solicitud_pago")
public class EstadoSolicitudPago extends EntidadGenerica implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Transient
	public static final EstadoSolicitudPago PENDIENTE_1 = new EstadoSolicitudPago(1, "Pendiente (Crear Proveedor)", "Caso en que no existe el proveedor y hay que crearlo, luego crear las facturas, y por ultimo crear la OP.", false);
	@Transient
	public static final EstadoSolicitudPago PENDIENTE_2 = new EstadoSolicitudPago(2, "Pendiente (Crear Factura/s)", "Caso en que no existe/n la/s factura/s en el sistema. Luego hay que crear la OP tambien.", false);
	@Transient
	public static final EstadoSolicitudPago PENDIENTE_3 = new EstadoSolicitudPago(3, "Pendiente (Crear OP)", "Caso en el que el proveedor y las facturas ya estan cargadas en el sistema. Solo falta crear la OP.", false);
	@Transient
	public static final EstadoSolicitudPago CUMPLIDA = new EstadoSolicitudPago(4, "Cumplida", "La OP fue creada a partir de la solicitud de pago.", false);
	@Transient
	public static final EstadoSolicitudPago RECHAZADA = new EstadoSolicitudPago(5, "Rechazada", "Caso en que se rechaza la solicitud de pago.", false);
	
	private String nombre;
	
	private String descripcion;
	
	public EstadoSolicitudPago() {
	}
			
	public EstadoSolicitudPago(int id, String nombre, String descripcion, boolean borrado) {
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
		String texto = "Estado Solicitud Pago id: "+ id + ", ";
		texto += "nombre: " + nombre + ", ";
		texto += "descripcion: " + descripcion + ", ";
		return texto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EstadoSolicitudPago){
			EstadoSolicitudPago other = (EstadoSolicitudPago) obj;
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