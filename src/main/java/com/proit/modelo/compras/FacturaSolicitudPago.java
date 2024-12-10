package com.proit.modelo.compras;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.proit.modelo.EntidadGenerica;
import com.proit.modelo.TipoFactura;

/**
 * 
 * Esta clase es una factura que se utiliza en la solicitud de pago. Nota: puede estar o no cargada en el sistema como factura-compra
 */
@Entity
@Table(name="factura_solicitud_pago")
public class FacturaSolicitudPago extends EntidadGenerica implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * si existe la factura compra, se llena este campo
	 */
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="factura_compra_fk"), name="factura_compra_id")
	private FacturaCompra facturaCompra;
	
	/**
	 * si NO existe la factura compra, se llena este campo
	 */
	@Column(name = "fecha_factura_compra", columnDefinition="timestamp without time zone")
	private Calendar fechaFacturaCompra;
	
	/**
	 * si NO existe la factura compra, se llena este campo
	 */
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="tipo_factura_fk"), name="tipo_factura_compra_id")
	private TipoFactura tipoFacturaCompra;
	
	/**
	 * si NO existe la factura compra, se llena este campo
	 */
	@Column(name = "nro_factura_compra")
	private String nroFacturaCompra;
	
	@Column(columnDefinition="numeric", nullable = false)
	private double total;
	
	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="solicitud_pago_fk"), name="solicitud_pago_id", nullable = false)
	private SolicitudPago solicitudPago;

	
	public FacturaCompra getFacturaCompra() {
		return facturaCompra;
	}

	public void setFacturaCompra(FacturaCompra facturaCompra) {
		this.facturaCompra = facturaCompra;
	}

	public Calendar getFechaFacturaCompra() {
		return fechaFacturaCompra;
	}

	public void setFechaFacturaCompra(Calendar fechaFacturaCompra) {
		this.fechaFacturaCompra = fechaFacturaCompra;
	}

	public TipoFactura getTipoFacturaCompra() {
		return tipoFacturaCompra;
	}

	public void setTipoFacturaCompra(TipoFactura tipoFacturaCompra) {
		this.tipoFacturaCompra = tipoFacturaCompra;
	}

	public String getNroFacturaCompra() {
		return nroFacturaCompra;
	}

	public void setNroFacturaCompra(String nroFacturaCompra) {
		this.nroFacturaCompra = nroFacturaCompra;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public SolicitudPago getSolicitudPago() {
		return solicitudPago;
	}

	public void setSolicitudPago(SolicitudPago solicitudPago) {
		this.solicitudPago = solicitudPago;
	}
	
	//Estos dos metodos estan creados para ser usados las fechas como Date en vez de Calendar
	public Date getFechaFacturaCompraAsDate() {
		if (fechaFacturaCompra != null) {
			return fechaFacturaCompra.getTime();
		}
		return null;
	}

	public void setFechaFacturaCompraAsDate(Date fechaDate) {
		this.fechaFacturaCompra = Calendar.getInstance();
		this.fechaFacturaCompra.setTime(fechaDate);
	}

	public boolean isTipoA(){
		TipoFactura tipoFactura = getFacturaCompra()!=null ? getFacturaCompra().getTipoFactura() : getTipoFacturaCompra();
		return tipoFactura.equals(TipoFactura.TIPO_A);
	}
	
	public boolean isTipoB(){
		TipoFactura tipoFactura = getFacturaCompra()!=null ? getFacturaCompra().getTipoFactura() : getTipoFacturaCompra();
		return tipoFactura.equals(TipoFactura.TIPO_B);
	}
	
	public boolean isTipoC(){
		TipoFactura tipoFactura = getFacturaCompra()!=null ? getFacturaCompra().getTipoFactura() : getTipoFacturaCompra();
		return tipoFactura.equals(TipoFactura.TIPO_C);
	}
	
	public boolean isNotaCreditoA(){
		TipoFactura tipoFactura = getFacturaCompra()!=null ? getFacturaCompra().getTipoFactura() : getTipoFacturaCompra();
		return tipoFactura.equals(TipoFactura.NOTA_CRED_A);
	}
	
	public boolean isNotaCreditoB(){
		TipoFactura tipoFactura = getFacturaCompra()!=null ? getFacturaCompra().getTipoFactura() : getTipoFacturaCompra();
		return tipoFactura.equals(TipoFactura.NOTA_CRED_B);
	}
	
	public boolean isNotaCreditoC(){
		TipoFactura tipoFactura = getFacturaCompra()!=null ? getFacturaCompra().getTipoFactura() : getTipoFacturaCompra();
		return tipoFactura.equals(TipoFactura.NOTA_CRED_C);
	}
	
	public boolean isNotaCredito(){
		return isNotaCreditoA() || isNotaCreditoB() || isNotaCreditoC();
	}
	
	public boolean isNotaDebitoA(){
		TipoFactura tipoFactura = getFacturaCompra()!=null ? getFacturaCompra().getTipoFactura() : getTipoFacturaCompra();
		return tipoFactura.equals(TipoFactura.NOTA_DEB_A);
	}
	
	public boolean isNotaDebitoB(){
		TipoFactura tipoFactura = getFacturaCompra()!=null ? getFacturaCompra().getTipoFactura() : getTipoFacturaCompra();
		return tipoFactura.equals(TipoFactura.NOTA_DEB_B);
	}
	
	public boolean isNotaDebitoC(){
		TipoFactura tipoFactura = getFacturaCompra()!=null ? getFacturaCompra().getTipoFactura() : getTipoFacturaCompra();
		return tipoFactura.equals(TipoFactura.NOTA_DEB_C);
	}
	
	public boolean isNotaDebito(){
		return isNotaDebitoA() || isNotaDebitoB() || isNotaDebitoC();
	}

	@Override
	public String toString(){
		String texto = "Factura Solicitud Pago id: "+ id + ", ";
		if (facturaCompra!=null) {
			texto += "facturaCompra: " + facturaCompra+ ", ";
		}if (tipoFacturaCompra!=null) {
			texto += "tipoFacturaCompra: " + tipoFacturaCompra+ ", ";
		}
		if (nroFacturaCompra!=null) {
			texto += "nroFacturaCompra: " + nroFacturaCompra+ ", ";
		}
		texto += "solicitudPago.getNro(): " + solicitudPago.getNro()+ ", ";
		return texto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FacturaSolicitudPago){
			FacturaSolicitudPago other = (FacturaSolicitudPago) obj;
			return ((this.getFacturaCompra() == null && other.getFacturaCompra() == null) || (this.getFacturaCompra() != null && this.getFacturaCompra().equals(other.getFacturaCompra())))
				&&	((this.getTipoFacturaCompra() == null && other.getTipoFacturaCompra() == null) || (this.getTipoFacturaCompra() != null && this.getTipoFacturaCompra().equals(other.getTipoFacturaCompra())))
				&&	((this.getNroFacturaCompra() == null && other.getNroFacturaCompra() == null) || (this.getNroFacturaCompra() != null && this.getNroFacturaCompra().equals(other.getNroFacturaCompra())))
				&& this.getSolicitudPago()==other.getSolicitudPago()
				&& this.isBorrado()==other.isBorrado();
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode(){
	    return (int) getFacturaCompra().hashCode() *
	    		getTipoFacturaCompra().hashCode() *
	    		getNroFacturaCompra().hashCode() *
	    		getSolicitudPago().hashCode();
	}
}
