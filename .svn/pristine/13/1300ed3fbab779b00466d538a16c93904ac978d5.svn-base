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
 * Esta clase representa un Detalle de una Factura Venta.
 */
@Entity
@Table(name="detalle_factura_venta")
public class DetalleFacturaVenta extends EntidadGenerica implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(nullable = false)
	private int cantidad;
	
	@Column(nullable = false)
	private String descripcion;
	
	@Column(columnDefinition="numeric", nullable = false)
	private double importe;
	
	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="factura_venta_fk"), name="factura_venta_id", nullable = false)
	private FacturaVenta facturaVenta;

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}
	
	public FacturaVenta getFacturaVenta() {
		return facturaVenta;
	}

	public void setFacturaVenta(FacturaVenta facturaVenta) {
		this.facturaVenta = facturaVenta;
	}
	
	public double calculateTotalDetalle() {
		return importe * cantidad;
	}

	@Override
	public String toString(){
		String texto = "DetalleFacturaVenta id: "+ id + ", ";
		texto += "cantidad: " + cantidad+ ", ";
		texto += "descripcion: " + descripcion+ ", ";
		texto += "importe: " + importe;
		return texto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DetalleFacturaVenta){
			DetalleFacturaVenta other = (DetalleFacturaVenta) obj;
			return this.getCantidad() == other.getCantidad()
				&& ((this.getDescripcion() == null && other.getDescripcion() == null) || (this.getDescripcion() != null && this.getDescripcion().equals(other.getDescripcion())))
				&& this.isBorrado()==other.isBorrado();
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode(){
	    return (int) getCantidad() *
	    		getDescripcion().hashCode();
	}

}
