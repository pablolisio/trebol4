package com.proit.modelo.compras;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.proit.modelo.EntidadGenerica;
import com.proit.modelo.TipoFactura;

/**
 * 
 * Esta clase representa una factura de compra.
 */
@Entity
@Table(name="factura_compra")
public class FacturaCompra extends EntidadGenerica implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(columnDefinition="timestamp without time zone", nullable = false)
	private Calendar fecha;
	
	/**
	 * Es el mes en que la factura será impositiva. 
	 * Este campo es utilizado a la hora de generar el subdiario
	 */
	@Column(name="mes_impositivo", columnDefinition="timestamp without time zone", nullable = false)
	private Calendar mesImpositivo;
	
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="proveedor_fk"), nullable = false)
	private Proveedor proveedor;

	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="tipo_factura_fk"), name="tipo_factura_id", nullable = false)
	private TipoFactura tipoFactura;
	
	@Column(nullable = false)
	private String nro;
	
	@Column(columnDefinition="numeric", nullable = false)
	private double subtotal;
	
	@Column(columnDefinition="numeric", nullable = false)
	private double iva;
	
	@Column(name="perc_iva", columnDefinition="numeric")
	private double percIva;
	
	@Column(name="perc_iibb", columnDefinition="numeric")
	private double percIibb;
	
	@Column(name="perc_gcias", columnDefinition="numeric")
	private double percGcias;
	
	@Column(name="perc_suss", columnDefinition="numeric")
	private double percSUSS;
	
	@Column(name="otras_perc", columnDefinition="numeric")
	private double otrasPerc;
	
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="estado_factura_fk"), name="estado_factura_compra_id", nullable = false)
	private EstadoFacturaCompra estadoFactura;
	
	@ManyToMany(cascade = {CascadeType.ALL})
	@JoinTable(name="factura_compra_orden_pago", 
				joinColumns={@JoinColumn(name="factura_compra_id")}, 
		        inverseJoinColumns={@JoinColumn(name="orden_pago_id")})
	private List<OrdenPago> listadoOrdenesPago;

	public Calendar getFecha() {
		return fecha;
	}

	public void setFecha(Calendar fecha) {
		this.fecha = fecha;
	}

	public Calendar getMesImpositivo() {
		return mesImpositivo;
	}

	public void setMesImpositivo(Calendar mesImpositivo) {
		this.mesImpositivo = mesImpositivo;
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	public TipoFactura getTipoFactura() {
		return tipoFactura;
	}

	public void setTipoFactura(TipoFactura tipoFactura) {
		this.tipoFactura = tipoFactura;
	}

	public String getNro() {
		return nro;
	}

	public void setNro(String nro) {
		this.nro = nro;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public double getIva() {
		return iva;
	}

	public void setIva(double iva) {
		this.iva = iva;
	}

	public double getPercIva() {
		return percIva;
	}

	public void setPercIva(double percIva) {
		this.percIva = percIva;
	}

	public double getPercIibb() {
		return percIibb;
	}

	public void setPercIibb(double percIibb) {
		this.percIibb = percIibb;
	}
	
	public double getPercGcias() {
		return percGcias;
	}

	public void setPercGcias(double percGcias) {
		this.percGcias = percGcias;
	}

	public double getPercSUSS() {
		return percSUSS;
	}

	public void setPercSUSS(double percSUSS) {
		this.percSUSS = percSUSS;
	}

	public double getOtrasPerc() {
		return otrasPerc;
	}

	public void setOtrasPerc(double otrasPerc) {
		this.otrasPerc = otrasPerc;
	}

	public EstadoFacturaCompra getEstadoFactura() {
		return estadoFactura;
	}

	public void setEstadoFactura(EstadoFacturaCompra estadoFactura) {
		this.estadoFactura = estadoFactura;
	}

	public List<OrdenPago> getListadoOrdenesPago() {
		return listadoOrdenesPago;
	}

	public void setListadoOrdenesPago(List<OrdenPago> listadoOrdenesPago) {
		this.listadoOrdenesPago = listadoOrdenesPago;
	}
	
	public double calculateTotal() {
		return subtotal + iva + percIva + percIibb + percGcias + percSUSS + otrasPerc;
	}
	
	/**
	 * Utilizado para saber desde la página de Ordenes de Pago si la factura ha sido seleccionada o no
	 * @return
	 */
	public boolean isSeleccionada(){
		return !borrado;
	}
	
	/**
	 * Utilizado para saber desde la página de Ordenes de Pago si la factura ha sido seleccionada o no
	 * @param definido
	 */
	public void setSeleccionada(boolean definido){
		setBorrado(!definido);
	}
	
	public boolean isTipoA(){
		return getTipoFactura().isTipoA();
	}
	
	public boolean isTipoB(){
		return getTipoFactura().isTipoB();
	}
	
	public boolean isTipoC(){
		return getTipoFactura().isTipoC();
	}
	
	public boolean isNotaCreditoA(){
		return getTipoFactura().isNotaCreditoA();
	}
	
	public boolean isNotaCreditoB(){
		return getTipoFactura().isNotaCreditoB();
	}
	
	public boolean isNotaCreditoC(){
		return getTipoFactura().isNotaCreditoC();
	}
	
	public boolean isNotaCredito(){
		return getTipoFactura().isNotaCredito();
	}
	
	public boolean isNotaDebitoA(){
		return getTipoFactura().isNotaDebitoA();
	}
	
	public boolean isNotaDebitoB(){
		return getTipoFactura().isNotaDebitoB();
	}
	
	public boolean isNotaDebitoC(){
		return getTipoFactura().isNotaDebitoC();
	}
	
	public boolean isNotaDebito(){
		return getTipoFactura().isNotaDebito();
	}

	@Override
	public String toString(){
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		DateFormat dateFormat2 = new SimpleDateFormat("MM/yyyy");
		String texto = "Factura Compra id: "+ id + ", ";
		texto += "fecha: " + dateFormat.format(fecha.getTime())+ ", ";
		texto += "mesImpositivo: " + dateFormat2.format(mesImpositivo.getTime())+ ", ";
		texto += "proveedor: " + proveedor.getRazonSocial() + ", ";
		texto += "tipoFactura: " + tipoFactura.getNombre() + ", ";
		texto += "nro: " + nro + ", ";
		texto += "subtotal: " + subtotal + ", ";
		texto += "iva: " + iva + ", ";
		texto += "percIva: " + percIva + ", ";
		texto += "percIibb: " + percIibb + ", ";
		texto += "percGcias: " + percGcias + ", ";
		texto += "percSUSS: " + percSUSS + ", ";
		texto += "otrasPerc: " + otrasPerc + ", ";
		texto += "estadoFactura: " + estadoFactura.getNombre() ;
		return texto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FacturaCompra){
			FacturaCompra other = (FacturaCompra) obj;
			return ((this.getFecha() == null && other.getFecha() == null) || (this.getFecha() != null && this.getFecha().equals(other.getFecha())))
				&&	((this.getProveedor() == null && other.getProveedor() == null) || (this.getProveedor() != null && this.getProveedor().equals(other.getProveedor())))
				&&	((this.getNro() == null && other.getNro() == null) || (this.getNro() != null && this.getNro().equals(other.getNro())))
				&& this.isBorrado()==other.isBorrado();
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode(){
	    return (int) getFecha().hashCode() *
	    		getProveedor().hashCode() *
	    		getNro().hashCode();
	}
}
