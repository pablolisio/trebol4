package com.proit.modelo.ventas;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.proit.modelo.EntidadGenerica;
import com.proit.modelo.Evento;
import com.proit.modelo.TipoFactura;
import com.proit.modelo.Usuario;
import com.proit.utils.Utils;

/**
 * 
 * Esta clase representa una Factura Venta.
 */
@Entity
@Table(name="factura_venta")
public class FacturaVenta extends EntidadGenerica implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(columnDefinition="timestamp without time zone", nullable = false)
	private Calendar fecha;
	
	@Column(nullable = false)
	private String nro;
	
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="cliente_fk"), nullable=false)
	private Cliente cliente;
	
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="evento_fk"), nullable=false)
	private Evento evento;
	
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="tipo_factura_fk"), name="tipo_factura_id", nullable = false)
	private TipoFactura tipoFactura;
	
	@Column(name="nro_orden_compra")
	private String nroOrdenCompra;
	
	@Column(name="nro_requisicion")
	private String nroRequisicion;
	
	@Column
	private String observaciones;
	
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="factura_a_anular_fk"), name="factura_a_anular_id")
	private FacturaVenta facturaVentaAAnular;
	
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="usuario_solicitante_fk"), name = "usuario_solicitante_id", nullable = false)
	private Usuario usuarioSolicitante;
	
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="estado_factura_venta_fk"), name="estado_factura_venta_id", nullable = false)
	private EstadoFacturaVenta estadoFacturaVenta;
	
	@OneToMany(mappedBy="facturaVenta", fetch=FetchType.LAZY)
	@Cascade(value={CascadeType.ALL})
	private List<DetalleFacturaVenta> listadoDetalles;
		
	@OneToMany(mappedBy="facturaVenta", fetch=FetchType.LAZY)
	@Cascade(value={CascadeType.ALL})
	private List<DatoAdicionalFacturaVenta> listadoDatosAdicionales;
	
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="solicitud_factura_venta_fk"), name = "solicitud_factura_venta_id")
	private SolicitudFacturaVenta solicitudFacturaVenta;
	
	@ManyToMany(cascade = {javax.persistence.CascadeType.ALL})
	@JoinTable(name="factura_venta_cobranza", 
				joinColumns={@JoinColumn(name="factura_venta_id")}, 
		        inverseJoinColumns={@JoinColumn(name="cobranza_id")})
	private List<Cobranza> listadoCobranzas;
	
	@Transient
	private boolean asociadoConUnaNC;
	
	public Calendar getFecha() {
		return fecha;
	}

	public void setFecha(Calendar fecha) {
		this.fecha = fecha;
	}

	public String getNro() {
		return nro;
	}

	public void setNro(String nro) {
		this.nro = nro;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public TipoFactura getTipoFactura() {
		return tipoFactura;
	}

	public void setTipoFactura(TipoFactura tipoFactura) {
		this.tipoFactura = tipoFactura;
	}

	public String getNroOrdenCompra() {
		return nroOrdenCompra;
	}

	public void setNroOrdenCompra(String nroOrdenCompra) {
		this.nroOrdenCompra = nroOrdenCompra;
	}

	public String getNroRequisicion() {
		return nroRequisicion;
	}

	public void setNroRequisicion(String nroRequisicion) {
		this.nroRequisicion = nroRequisicion;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public FacturaVenta getFacturaVentaAAnular() {
		return facturaVentaAAnular;
	}

	public void setFacturaVentaAAnular(FacturaVenta facturaVentaAAnular) {
		this.facturaVentaAAnular = facturaVentaAAnular;
	}

	public Usuario getUsuarioSolicitante() {
		return usuarioSolicitante;
	}

	public void setUsuarioSolicitante(Usuario usuarioSolicitante) {
		this.usuarioSolicitante = usuarioSolicitante;
	}

	public EstadoFacturaVenta getEstadoFacturaVenta() {
		return estadoFacturaVenta;
	}

	public void setEstadoFacturaVenta(EstadoFacturaVenta estadoFacturaVenta) {
		this.estadoFacturaVenta = estadoFacturaVenta;
	}

	public List<DetalleFacturaVenta> getListadoDetalles() {
		return listadoDetalles;
	}

	public void setListadoDetalles(List<DetalleFacturaVenta> listadoDetalles) {
		this.listadoDetalles = listadoDetalles;
	}

	public List<DatoAdicionalFacturaVenta> getListadoDatosAdicionales() {
		return listadoDatosAdicionales;
	}

	public void setListadoDatosAdicionales(
			List<DatoAdicionalFacturaVenta> listadoDatosAdicionales) {
		this.listadoDatosAdicionales = listadoDatosAdicionales;
	}

	public SolicitudFacturaVenta getSolicitudFacturaVenta() {
		return solicitudFacturaVenta;
	}

	public void setSolicitudFacturaVenta(SolicitudFacturaVenta solicitudFacturaVenta) {
		this.solicitudFacturaVenta = solicitudFacturaVenta;
	}
	
	public List<Cobranza> getListadoCobranzas() {
		return listadoCobranzas;
	}

	public void setListadoCobranzas(List<Cobranza> listadoCobranzas) {
		this.listadoCobranzas = listadoCobranzas;
	}

	public double calculateSubtotal() {
		double subtotal = 0;
		for (DetalleFacturaVenta detalle : listadoDetalles) {
			if (!detalle.isBorrado()) {
				subtotal += detalle.calculateTotalDetalle();
			}
		}
		return subtotal;
	}
	
	public double calculateTotal() {
		double subtotal = calculateSubtotal();
		double iva = Utils.calcularImporteIVA(subtotal, tipoFactura);
		return subtotal + iva;
	}
	
	/**
	 * Utilizado para saber desde la página de Cobranzas si la factura ha sido seleccionada o no
	 * @return
	 */
	public boolean isSeleccionada(){
		return !borrado;
	}
	
	/**
	 * Utilizado para saber desde la página de Cobranzas si la factura ha sido seleccionada o no
	 * @param definido
	 */
	public void setSeleccionada(boolean definido){
		setBorrado(!definido);
	}
	
	public boolean isTipoA(){ //TODO falta done
		return getTipoFactura().isTipoA();
	}
	
	public boolean isTipoB(){//TODO falta done
		return getTipoFactura().isTipoB();
	}
	
	public boolean isTipoC(){//TODO falta done
		return getTipoFactura().isTipoC();
	}
	
	public boolean isTipoE(){//TODO falta done
		return getTipoFactura().isTipoE();
	}
	
	public boolean isTipoFCE(){
		return getTipoFactura().isTipoFCE();
	}
	
	public boolean isNotaCreditoA(){//TODO falta done
		return getTipoFactura().isNotaCreditoA();
	}
	
	public boolean isNotaCreditoB(){//TODO falta done
		return getTipoFactura().isNotaCreditoB();
	}
	
	public boolean isNotaCreditoC(){//TODO falta done
		return getTipoFactura().isNotaCreditoC();
	}
	
	public boolean isNotaCreditoE(){//TODO falta done
		return getTipoFactura().isNotaCreditoE();
	}
	
	public boolean isNotaCreditoN(){
		return getTipoFactura().isNotaCreditoN();
	}
	
	public boolean isNotaCreditoFCE(){
		return getTipoFactura().isNotaCreditoFCE();
	}
	
	public boolean isNotaCredito(){//TODO falta done
		return getTipoFactura().isNotaCredito();
	}
	
	public boolean isNotaDebitoA(){//TODO falta done
		return getTipoFactura().isNotaDebitoA();
	}
	
	public boolean isNotaDebitoB(){//TODO falta done
		return getTipoFactura().isNotaDebitoB();
	}
	
	public boolean isNotaDebitoC(){//TODO falta done
		return getTipoFactura().isNotaDebitoC();
	}
	
	public boolean isNotaDebitoE(){//TODO falta done
		return getTipoFactura().isNotaDebitoE();
	}
	
	public boolean isNotaDebitoFCE(){
		return getTipoFactura().isNotaDebitoFCE();
	}
	
	public boolean isNotaDebito(){//TODO falta done
		return getTipoFactura().isNotaDebito();
	}

	public boolean isXCobrar(){
		return estadoFacturaVenta.equals(EstadoFacturaVenta.X_COBRAR);
	}
	
	public boolean isCobrada(){
		return estadoFacturaVenta.equals(EstadoFacturaVenta.COBRADO);
	}
	
	public boolean isCobradaParcial(){
		return estadoFacturaVenta.equals(EstadoFacturaVenta.COBRADO_PARCIAL);
	}
	
	public boolean isAnulada(){ //TODO no se usa en ningun lado
		return estadoFacturaVenta.equals(EstadoFacturaVenta.ANULADO);
	}
		
	public boolean isAsociadoConUnaNC() {
		return asociadoConUnaNC;
	}

	public void setAsociadoConUnaNC(boolean asociadoConUnaNC) {
		this.asociadoConUnaNC = asociadoConUnaNC;
	}

	@Override
	public String toString(){
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String texto = "FacturaVenta id: "+ id + ", ";
		texto += "fecha: " + dateFormat.format(fecha.getTime())+ ", ";
		texto += "nro: " + nro+ ", ";
		texto += "cliente: " + cliente.getRazonSocial()+ ", ";
		texto += "evento: " + evento.getNombreConCliente()+ ", ";
		texto += "tipoFactura: " + tipoFactura.getNombre()+ ", ";
		texto += "solicitante: " + usuarioSolicitante.getNombreCompleto()+ ", ";
		texto += "listadoDetalles: {";
		if (listadoDetalles!=null){
			for (int i = 0; i < listadoDetalles.size(); i++) {
				texto += "["+listadoDetalles.get(i).toString()+"]";
			}
		} else {
			texto+="empty";
		}
		texto += "},";
		if (solicitudFacturaVenta!=null) {
			texto += "solicitudFacturaVenta: " + solicitudFacturaVenta.getNro()+ ", ";
		}
		return texto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FacturaVenta){
			FacturaVenta other = (FacturaVenta) obj;
			return ((this.getFecha() == null && other.getFecha() == null) || (this.getFecha() != null && this.getFecha().equals(other.getFecha())))
				&&	((this.getNro() == null && other.getNro() == null) || (this.getNro() != null && this.getNro().equals(other.getNro())))
				&& this.isBorrado()==other.isBorrado();
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode(){
	    return (int) getFecha().hashCode() *
	    		getNro().hashCode();
	}
	
}
