package com.proit.modelo.compras;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * Esta clase representa la asociacion many-to-many entre facturas de compra y ordenes de pago. 
 */
@Entity
@Table(name="factura_compra_orden_pago")
public class FacturaCompraOrdenPago implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(nullable = false)
	private boolean borrado;
	
	@Id
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="factura_compra_fk"), name="factura_compra_id", nullable = false)
	private FacturaCompra facturaCompra;
	
	@Id
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="orden_pago_fk"), name="orden_pago_id", nullable = false)
	private OrdenPago ordenPago;


	public boolean isBorrado() {
		return borrado;
	}
	
	public void setBorrado(boolean borrado) {
		this.borrado = borrado;
	}

	public FacturaCompra getFacturaCompra() {
		return facturaCompra;
	}

	public void setFacturaCompra(FacturaCompra facturaCompra) {
		this.facturaCompra = facturaCompra;
	}

	public OrdenPago getOrdenPago() {
		return ordenPago;
	}

	public void setOrdenPago(OrdenPago ordenPago) {
		this.ordenPago = ordenPago;
	}

	@Override
	public String toString(){
		String texto = "Factura-OrdenPago: ";
		texto += "facturaCompra: " + getFacturaCompra()!=null ? getFacturaCompra().getNro() : "NULL" + ", ";
		texto += "ordenPago: " + getOrdenPago().getNro()+ ", ";
		return texto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FacturaCompraOrdenPago){
			FacturaCompraOrdenPago other = (FacturaCompraOrdenPago) obj;
			return ((this.getFacturaCompra() == null && other.getFacturaCompra() == null) || (this.getFacturaCompra() != null && this.getFacturaCompra().equals(other.getFacturaCompra())))
					&&	((this.getOrdenPago() == null && other.getOrdenPago() == null) || (this.getOrdenPago() != null && this.getOrdenPago().equals(other.getOrdenPago())))
				&& this.isBorrado()==other.isBorrado();
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode(){
	    return (int) getFacturaCompra().hashCode() *
	                getOrdenPago().hashCode() ;
	}
}
