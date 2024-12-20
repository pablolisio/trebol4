package com.proit.modelo.ventas;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.proit.modelo.TipoFactura;
import com.proit.modelo.Usuario;
import com.proit.utils.Utils;

/**
 * 
 * Esta clase representa una Solicitud de Factura Venta.
 */
@Entity
@Table(name="solicitud_factura_venta")
public class SolicitudFacturaVenta extends EntidadGenerica implements Serializable {

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
	@JoinColumn(foreignKey=@ForeignKey(name="estado_solicitud_factura_venta_fk"), name="estado_solicitud_factura_venta_id", nullable = false)
	private EstadoSolicitudFactura estadoSolicitudFactura;
	
	@OneToMany(mappedBy="solicitudFacturaVenta", fetch=FetchType.LAZY)
	@Cascade(value={CascadeType.ALL})
	private List<DetalleSolicitudFacturaVenta> listadoDetalles;
		
	@OneToMany(mappedBy="solicitudFacturaVenta", fetch=FetchType.LAZY)
	@Cascade(value={CascadeType.ALL})
	private List<DatoAdicionalSolicitudFacturaVenta> listadoDatosAdicionales;
	
	/**
	 * Utilizado para permitir asignar eventos de cualquier cliente (no solo los eventos del cliente seleccionado)
	 */
	@Column(name="permitir_cualquier_evento", nullable = false)
	private boolean permitirCualquierEvento;
		
	public SolicitudFacturaVenta() {
		
	}
	
	public SolicitudFacturaVenta(Calendar fecha, Cliente cliente,
			Evento evento, TipoFactura tipoFactura, Usuario usuarioSolicitante,
			EstadoSolicitudFactura estadoSolicitudFactura) {
		super();
		this.fecha = fecha;
		this.cliente = cliente;
		this.evento = evento;
		this.tipoFactura = tipoFactura;
		this.usuarioSolicitante = usuarioSolicitante;
		this.estadoSolicitudFactura = estadoSolicitudFactura;
	}
	
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

	public EstadoSolicitudFactura getEstadoSolicitudFactura() {
		return estadoSolicitudFactura;
	}

	public void setEstadoSolicitudFactura(
			EstadoSolicitudFactura estadoSolicitudFactura) {
		this.estadoSolicitudFactura = estadoSolicitudFactura;
	}

	public List<DetalleSolicitudFacturaVenta> getListadoDetalles() {
		return listadoDetalles;
	}

	public void setListadoDetalles(
			List<DetalleSolicitudFacturaVenta> listadoDetalles) {
		this.listadoDetalles = listadoDetalles;
	}

	public List<DatoAdicionalSolicitudFacturaVenta> getListadoDatosAdicionales() {
		return listadoDatosAdicionales;
	}

	public void setListadoDatosAdicionales(
			List<DatoAdicionalSolicitudFacturaVenta> listadoDatosAdicionales) {
		this.listadoDatosAdicionales = listadoDatosAdicionales;
	}

	public boolean isPermitirCualquierEvento() {
		return permitirCualquierEvento;
	}

	public void setPermitirCualquierEvento(boolean permitirCualquierEvento) {
		this.permitirCualquierEvento = permitirCualquierEvento;
	}

	public double calculateSubtotal() {
		double subtotal = 0;
		for (DetalleSolicitudFacturaVenta detalle : listadoDetalles) {
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
	
	public boolean isTipoA(){//TODO falta done
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
	
	public boolean isNotaCreditoFCE(){
		return getTipoFactura().isNotaCreditoFCE();
	}
	
	public boolean isNotaCreditoN(){
		return getTipoFactura().isNotaCreditoN();
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
	
	public boolean isPendiente(){
		return estadoSolicitudFactura.equals(EstadoSolicitudFactura.PENDIENTE);
	}
	
	public boolean isCumplida(){
		return estadoSolicitudFactura.equals(EstadoSolicitudFactura.CUMPLIDA);
	}
	
	public boolean isRechazada(){
		return estadoSolicitudFactura.equals(EstadoSolicitudFactura.RECHAZADA);
	}
		
	@Override
	public String toString(){
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String texto = "SolicitudFacturaVenta id: "+ id + ", ";
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
		texto += "}";
		return texto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SolicitudFacturaVenta){
			SolicitudFacturaVenta other = (SolicitudFacturaVenta) obj;
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
	
	/**
	 * Se convierte a FacturaVenta. Utilizado para conversion desde Solicitud Fac Vta a Factura venta 
	 * @return
	 */
	public FacturaVenta convertirAFacturaVenta(){
		FacturaVenta facturaVenta = new FacturaVenta();
		facturaVenta.setCliente(cliente);
		facturaVenta.setEvento(evento);
		facturaVenta.setFacturaVentaAAnular(facturaVentaAAnular);
		facturaVenta.setNroOrdenCompra(nroOrdenCompra);
		facturaVenta.setNroRequisicion(nroRequisicion);
		facturaVenta.setObservaciones(observaciones);
		facturaVenta.setSolicitudFacturaVenta(this);
		facturaVenta.setTipoFactura(tipoFactura);
		facturaVenta.setUsuarioSolicitante(usuarioSolicitante);
		List<DetalleFacturaVenta> detallesFacVta = new ArrayList<DetalleFacturaVenta>();
		for (DetalleSolicitudFacturaVenta detalle : listadoDetalles) {
			detallesFacVta.add(detalle.convertirADetalleFacturaVenta(facturaVenta));
		}
		List<DatoAdicionalFacturaVenta> datosAdicFacVta = new ArrayList<DatoAdicionalFacturaVenta>();
		for (DatoAdicionalSolicitudFacturaVenta dato : listadoDatosAdicionales) {
			datosAdicFacVta.add(dato.convertirADatoAdicionalFacturaVenta(facturaVenta));
		}
		facturaVenta.setListadoDetalles(detallesFacVta);
		facturaVenta.setListadoDatosAdicionales(datosAdicFacVta);
		
		return facturaVenta;
	}
}
