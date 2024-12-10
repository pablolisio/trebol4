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
 * Esta clase representa un Dato Adicional de una Solicitud de Factura Venta.
 */
@Entity
@Table(name="adicional_solicitud_factura_venta")
public class DatoAdicionalSolicitudFacturaVenta extends EntidadGenerica implements Serializable {

	private static final long serialVersionUID = 1L;
		
	@Column(nullable = false)
	private String descripcion;
	
	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="solicitud_factura_venta_fk"), name="solicitud_factura_venta_id", nullable = false)
	private SolicitudFacturaVenta solicitudFacturaVenta;

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public SolicitudFacturaVenta getSolicitudFacturaVenta() {
		return solicitudFacturaVenta;
	}

	public void setSolicitudFacturaVenta(SolicitudFacturaVenta solicitudFacturaVenta) {
		this.solicitudFacturaVenta = solicitudFacturaVenta;
	}
	
	@Override
	public String toString(){
		String texto = "DatoAdicionalSolicitudFacturaVenta id: "+ id + ", ";
		texto += "descripcion: " + descripcion;
		return texto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DatoAdicionalSolicitudFacturaVenta){
			DatoAdicionalSolicitudFacturaVenta other = (DatoAdicionalSolicitudFacturaVenta) obj;
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

	/**
	 * Se convierte a DatoAdicionalFacturaVenta. Utilizado para conversion desde Solicitud Fac Vta a Factura venta 
	 * @return
	 */
	public DatoAdicionalFacturaVenta convertirADatoAdicionalFacturaVenta(FacturaVenta facturaVenta){
		DatoAdicionalFacturaVenta datoAdicionalFacturaVenta = new DatoAdicionalFacturaVenta();
		datoAdicionalFacturaVenta.setBorrado(borrado);
		datoAdicionalFacturaVenta.setDescripcion(descripcion);
		datoAdicionalFacturaVenta.setFacturaVenta(facturaVenta);
		return datoAdicionalFacturaVenta;
	}
}
