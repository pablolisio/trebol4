package com.proit.modelo.compras;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.proit.modelo.EntidadGenerica;
import com.proit.modelo.Evento;
import com.proit.modelo.Usuario;
import com.proit.modelo.ventas.Cliente;

/**
 * 
 * Esta clase representa una Solicitud de Pago.
 */
@Entity
@Table(name="solicitud_pago")
public class SolicitudPago extends EntidadGenerica implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(columnDefinition="timestamp without time zone", nullable = false)
	private Calendar fecha;
	
	@Column(nullable = false)
	private String nro;
		
	/**
	 * si existe el proveedor, se llena este campo
	 */
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="proveedor_fk"))
	private Proveedor proveedor;
	
	/**
	 * si NO existe el proveedor, se llena este campo
	 */
	@Column(name= "razon_social")
	private String razonSocial;
	
	/**
	 * si NO existe el proveedor, se llena este campo
	 */
	@Column(name = "cuit_cuil")
	private String cuitCuil;
	
	/**
	 * si NO existe el proveedor, y si se necesita transferencia se llena este campo
	 */
	@Column()
	private String cbu;
	
	@Column(name="con_factura", nullable = false)
	private boolean conFactura;
		
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="evento_fk"), nullable=false)
	private Evento evento;
	
	@Column
	private String concepto;
	
	@Column
	private String observaciones;
	
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="cliente_fk"), nullable=false)
	private Cliente cliente;
	
	@Column(name = "nro_factura_venta")
	private String nroFacturaVenta;
		
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="usuario_solicitante_fk"), name = "usuario_solicitante_id")
	private Usuario usuarioSolicitante;
	
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="estado_solicitud_pago_fk"), name="estado_solicitud_pago_id", nullable = false)
	private EstadoSolicitudPago estadoSolicitudPago;
	
	@OneToMany(mappedBy="solicitudPago", fetch=FetchType.LAZY)
	@Cascade(value={CascadeType.ALL})
	private List<PagoSolicitudPago> listadoPagos;
	
	@OneToMany(mappedBy="solicitudPago", fetch=FetchType.LAZY)
	@Cascade(value={CascadeType.ALL})
	private List<FacturaSolicitudPago> listadoFacturas;
	
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

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getCuitCuil() {
		return cuitCuil;
	}

	public void setCuitCuil(String cuitCuil) {
		this.cuitCuil = cuitCuil;
	}

	public String getCbu() {
		return cbu;
	}

	public void setCbu(String cbu) {
		this.cbu = cbu;
	}

	public boolean isConFactura() {
		return conFactura;
	}

	public void setConFactura(boolean conFactura) {
		this.conFactura = conFactura;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getNroFacturaVenta() {
		return nroFacturaVenta;
	}

	public void setNroFacturaVenta(String nroFacturaVenta) {
		this.nroFacturaVenta = nroFacturaVenta;
	}

	public Usuario getUsuarioSolicitante() {
		return usuarioSolicitante;
	}

	public void setUsuarioSolicitante(Usuario usuarioSolicitante) {
		this.usuarioSolicitante = usuarioSolicitante;
	}

	public EstadoSolicitudPago getEstadoSolicitudPago() {
		return estadoSolicitudPago;
	}

	public void setEstadoSolicitudPago(EstadoSolicitudPago estadoSolicitudPago) {
		this.estadoSolicitudPago = estadoSolicitudPago;
	}

	public List<PagoSolicitudPago> getListadoPagos() {
		return listadoPagos;
	}

	public void setListadoPagos(List<PagoSolicitudPago> listadoPagos) {
		this.listadoPagos = listadoPagos;
	}

	public List<FacturaSolicitudPago> getListadoFacturas() {
		return listadoFacturas;
	}

	public void setListadoFacturas(List<FacturaSolicitudPago> listadoFacturas) {
		this.listadoFacturas = listadoFacturas;
	}

	public boolean isPendiente1(){
		return estadoSolicitudPago.equals(EstadoSolicitudPago.PENDIENTE_1);
	}
	public boolean isPendiente2(){
		return estadoSolicitudPago.equals(EstadoSolicitudPago.PENDIENTE_2);
	}
	public boolean isPendiente3(){
		return estadoSolicitudPago.equals(EstadoSolicitudPago.PENDIENTE_3);
	}
	public boolean isCumplida(){
		return estadoSolicitudPago.equals(EstadoSolicitudPago.CUMPLIDA);
	}
	public boolean isRechazada(){
		return estadoSolicitudPago.equals(EstadoSolicitudPago.RECHAZADA);
	}

	public boolean isSPySF() {
		boolean tieneProveedor = proveedor!=null || razonSocial!=null;
		return !isConFactura() && !tieneProveedor;
	}
	
	public boolean isCPySF() {
		boolean tieneProveedor = proveedor!=null || razonSocial!=null;
		return !isConFactura() &&  tieneProveedor;
	}
	
	public boolean hasNextFacturaSolicitudPagoNoConvertida() {
		for (FacturaSolicitudPago facturaSolicitudPago : getListadoFacturas()) {
			if (facturaSolicitudPago.getFacturaCompra()==null) {
				return true;
			}
		}
		return false;
	}
	
	public FacturaSolicitudPago getNextFacturaSolicitudPagoNoConvertida() {
		for (FacturaSolicitudPago facturaSolicitudPago : getListadoFacturas()) {
			if (facturaSolicitudPago.getFacturaCompra()==null) {
				return facturaSolicitudPago;
			}
		}
		return null;
	}
	
	public String getModosPagosElegidos(){
		String modos = "";
		boolean ef = false;
		boolean ch = false;
		boolean tr = false;
		boolean tar = false;		
		for (PagoSolicitudPago pago : listadoPagos){
			if ( !ef && pago.isEfectivo()) {
				modos = addModo(modos, "Efectivo");
				ef = true;
			}
			if ( !ch && pago.isCheque()) {
				modos = addModo(modos, "Cheque");
				ch = true;
			}
			if ( !tr && (pago.isTransferencia() || pago.isTransferencia3ro() || pago.isTransferenciaSinProv())) {
				modos = addModo(modos, "Transferencia");
				tr = true;
			}
			if ( !tar && pago.isTarjetaCredito()) {
				modos = addModo(modos, "Tarjeta Crédito");
				tar = true;
			}
		}
		return modos;
	}
	
	private String addModo(String modos, String modo){
		if (modos.isEmpty()){
			modos += modo;
		} else {
			modos += ", " + modo;
		}
		return modos;
	}
	
	@Override
	public String toString(){
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String texto = "SolicitudPago id: "+ id + ", ";
		texto += "fecha: " + dateFormat.format(fecha.getTime())+ ", ";
		texto += "nro: " + nro+ ", ";
		texto += "evento: " + evento.getNombreConCliente()+ ", ";
		texto += "concepto: " + concepto+ ", ";
		texto += "solicitante: " + usuarioSolicitante.getNombreCompleto()+ ", ";
		if (proveedor != null) {
			texto += "proveedor: " + proveedor.getRazonSocial()+ ", ";
		}
		if (razonSocial != null) {
			texto += "razonSocial: " + razonSocial+ ", ";
		}
		if (cuitCuil != null) {
			texto += "cuitCuil: " + cuitCuil+ ", ";
		}
		if (cbu != null) {
			texto += "cbu: " + cbu+ ", ";
		}
		texto += "listadoFacturas: {";
		if (listadoFacturas!=null){
			for (int i = 0; i < listadoFacturas.size(); i++) {
				texto += "["+listadoFacturas.get(i).toString()+"]";
			}
		} else {
			texto+="empty";
		}
		texto += "}";
		texto += "listadoPagos: {";
		if (listadoPagos!=null){
			for (int i = 0; i < listadoPagos.size(); i++) {
				texto += "["+listadoPagos.get(i).toString()+"]";
			}
		} else {
			texto+="empty";
		}
		texto += "}";
		return texto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SolicitudPago){
			SolicitudPago other = (SolicitudPago) obj;
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
