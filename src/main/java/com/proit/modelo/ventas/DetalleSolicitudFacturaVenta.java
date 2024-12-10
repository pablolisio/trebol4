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
 * Esta clase representa un Detalle de una Solicitud de Factura Venta.
 */
@Entity
@Table(name="detalle_solicitud_factura_venta")
public class DetalleSolicitudFacturaVenta extends EntidadGenerica implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(nullable = false)
	private int cantidad;
	
	@Column(nullable = false)
	private String descripcion;
	
	@Column(columnDefinition="numeric", nullable = false)
	private double importe;
	
	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="solicitud_factura_venta_fk"), name="solicitud_factura_venta_id", nullable = false)
	private SolicitudFacturaVenta solicitudFacturaVenta;

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

	public SolicitudFacturaVenta getSolicitudFacturaVenta() {
		return solicitudFacturaVenta;
	}

	public void setSolicitudFacturaVenta(SolicitudFacturaVenta solicitudFacturaVenta) {
		this.solicitudFacturaVenta = solicitudFacturaVenta;
	}
	
	public double calculateTotalDetalle() {
		return importe * cantidad;
	}
	
	@Override
	public String toString(){
		String texto = "DetalleSolicitudFacturaVenta id: "+ id + ", ";
		texto += "cantidad: " + cantidad+ ", ";
		texto += "descripcion: " + descripcion+ ", ";
		texto += "importe: " + importe;
		return texto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DetalleSolicitudFacturaVenta){
			DetalleSolicitudFacturaVenta other = (DetalleSolicitudFacturaVenta) obj;
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
	
	
	/**
	 * Se convierte a DetalleFacturaVenta. Utilizado para conversion desde Solicitud Fac Vta a Factura venta 
	 * @return
	 */
	public DetalleFacturaVenta convertirADetalleFacturaVenta(FacturaVenta facturaVenta){
		DetalleFacturaVenta detalleFacturaVenta = new DetalleFacturaVenta();
		detalleFacturaVenta.setBorrado(borrado);
		detalleFacturaVenta.setCantidad(cantidad);
		detalleFacturaVenta.setDescripcion(descripcion);
		detalleFacturaVenta.setFacturaVenta(facturaVenta);
		detalleFacturaVenta.setImporte(importe);		
		return detalleFacturaVenta;
	}

}
