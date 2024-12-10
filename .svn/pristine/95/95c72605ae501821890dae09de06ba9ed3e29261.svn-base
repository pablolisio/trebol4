package com.proit.modelo.ventas;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.proit.modelo.EntidadGenerica;

/**
 * 
 * Esta clase representa un Dato Adicional de una Factura Venta.
 */
@Entity
@Table(name="adicional_factura_venta")
public class DatoAdicionalFacturaVenta extends EntidadGenerica implements Serializable {

	private static final long serialVersionUID = 1L;
		
	@Column(nullable = false)
	private String descripcion;
	
	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="factura_venta_fk"), name="factura_venta_id", nullable = false)
	private FacturaVenta facturaVenta;

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public FacturaVenta getFacturaVenta() {
		return facturaVenta;
	}

	public void setFacturaVenta(FacturaVenta facturaVenta) {
		this.facturaVenta = facturaVenta;
	}
	
	@Override
	public String toString(){
		String texto = "DatoAdicionalFacturaVenta id: "+ id + ", ";
		texto += "descripcion: " + descripcion;
		return texto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DatoAdicionalFacturaVenta){
			DatoAdicionalFacturaVenta other = (DatoAdicionalFacturaVenta) obj;
			return ((this.getDescripcion() == null && other.getDescripcion() == null) || (this.getDescripcion() != null && this.getDescripcion().equals(other.getDescripcion())))
				&& this.isBorrado()==other.isBorrado();
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode(){
	    return (int) getDescripcion().hashCode();
	}

}
