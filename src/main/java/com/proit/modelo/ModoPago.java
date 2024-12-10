package com.proit.modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * 
 * Esta clase representa un modo de pago. 
 * Los mismos pueden ser: Sin Factura, Efectivo, Cheque, Transferencia
 */
@Entity(name="modo_pago")
public class ModoPago extends EntidadGenerica implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Transient
	public static final ModoPago SF = new ModoPago(1, "Sin Factura", "Para casos especiales donde no se trabaja con factura.", false);
	@Transient
	public static final ModoPago EFECTIVO = new ModoPago(2, "Efectivo", "", false);
	@Transient
	public static final ModoPago CHEQUE = new ModoPago(3, "Cheque", "", false);
	@Transient
	public static final ModoPago TRANSFERENCIA = new ModoPago(4, "Transferencia", "", false);
	@Transient
	public static final ModoPago TRANSFERENCIA_3RO = new ModoPago(5, "Transferencia a tercero", "", false);
	@Transient
	public static final ModoPago TRANSFERENCIA_SIN_PROV = new ModoPago(6, "Transferencia sin proveedor", "Usada en los casos de OP SinFact y SinProv.", false);
	@Transient
	public static final ModoPago TARJETA_CRED = new ModoPago(7, "Tarjeta Credito", "", false);
	
	private String nombre;
	
	private String descripcion;
	
	public ModoPago() {
	}
	
	public ModoPago(int id, String nombre, String descripcion, boolean borrado) {
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
		if (obj instanceof ModoPago){
			ModoPago other = (ModoPago) obj;
			return this.getNombre().equals(other.getNombre());
		} else {
			return false;
		}
	}
	
	
}