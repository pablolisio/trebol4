package com.proit.modelo.ventas;

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
 * Esta clase representa la asociacion many-to-many entre facturas de venta y cobranzas. 
 */
@Entity
@Table(name="factura_venta_cobranza")
public class FacturaVentaCobranza implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(nullable = false)
	private boolean borrado;
	
	@Id
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="factura_venta_fk"), name="factura_venta_id", nullable = false)
	private FacturaVenta facturaVenta;
	
	@Id
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="cobranza_fk"), name="cobranza_id", nullable = false)
	private Cobranza cobranza;

	public boolean isBorrado() {
		return borrado;
	}

	public void setBorrado(boolean borrado) {
		this.borrado = borrado;
	}

	public FacturaVenta getFacturaVenta() {
		return facturaVenta;
	}

	public void setFacturaVenta(FacturaVenta facturaVenta) {
		this.facturaVenta = facturaVenta;
	}

	public Cobranza getCobranza() {
		return cobranza;
	}

	public void setCobranza(Cobranza cobranza) {
		this.cobranza = cobranza;
	}

	@Override
	public String toString(){
		String texto = "Factura-Cobranza: ";
		texto += "facturaVenta: " + getFacturaVenta()!=null ? getFacturaVenta().getNro() : "NULL" + ", ";
		texto += "cobranza: " + getCobranza().getNroRecibo()!=null ? getCobranza().getNroRecibo() : "<Sin Nro Recibo>";
		return texto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FacturaVentaCobranza){
			FacturaVentaCobranza other = (FacturaVentaCobranza) obj;
			return ((this.getFacturaVenta() == null && other.getFacturaVenta() == null) || (this.getFacturaVenta() != null && this.getFacturaVenta().equals(other.getFacturaVenta())))
					&&	((this.getCobranza() == null && other.getCobranza() == null) || (this.getCobranza() != null && this.getCobranza().equals(other.getCobranza())))
				&& this.isBorrado()==other.isBorrado();
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode(){
	    return (int) getFacturaVenta().hashCode() *
	    			getCobranza().hashCode() ;
	}
}
