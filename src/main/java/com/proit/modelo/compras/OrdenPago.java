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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.proit.modelo.EntidadGenerica;
import com.proit.modelo.Evento;
import com.proit.modelo.Usuario;
import com.proit.utils.Constantes;

/**
 * 
 * Esta clase representa una Orden de Pago.
 */
@Entity
@Table(name="orden_pago")
public class OrdenPago extends EntidadGenerica implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(columnDefinition="timestamp without time zone", nullable = false)
	private Calendar fecha;
	
	@Column(nullable = false)
	private String nro;
	
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="evento_fk"))
	private Evento evento;
	
	@Column
	private String concepto;
	
	@Column
	private String observaciones;
	
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="usuario_solicitante_fk"), name = "usuario_solicitante_id")
	private Usuario usuarioSolicitante;
	
	/**
	 * La orden de pago puede tener uno o varios pagos.
	 */
	@OneToMany(mappedBy="ordenPago", fetch=FetchType.LAZY)
	@Cascade(value={CascadeType.ALL})
	private List<Pago> listadoPagos;
	
	@ManyToMany(mappedBy="listadoOrdenesPago")
	private List<FacturaCompra> listadoFacturas;
	
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="solicitud_pago_fk"), name = "solicitud_pago_id")
	private SolicitudPago solicitudPago;
	
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="plan_cuenta_fk"), name = "plan_cuenta_id")
	private PlanCuenta planCuenta;

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

	public Usuario getUsuarioSolicitante() {
		return usuarioSolicitante;
	}

	public void setUsuarioSolicitante(Usuario usuarioSolicitante) {
		this.usuarioSolicitante = usuarioSolicitante;
	}

	public List<Pago> getListadoPagos() {
		return listadoPagos;
	}

	public void setListadoPagos(List<Pago> listadoPagos) {
		this.listadoPagos = listadoPagos;
	}

	public List<FacturaCompra> getListadoFacturas() {
		return listadoFacturas;
	}

	public void setListadoFacturas(List<FacturaCompra> listadoFacturas) {
		this.listadoFacturas = listadoFacturas;
	}
	
	public SolicitudPago getSolicitudPago() {
		return solicitudPago;
	}

	public void setSolicitudPago(SolicitudPago solicitudPago) {
		this.solicitudPago = solicitudPago;
	}
	
	public PlanCuenta getPlanCuenta() {
		return planCuenta;
	}

	public void setPlanCuenta(PlanCuenta planCuenta) {
		this.planCuenta = planCuenta;
	}

	public boolean isSPySF(){
		return listadoFacturas.isEmpty();
	}

	public boolean isCPySF(){
		return !isSPySF() && listadoFacturas.get(0).getNro().startsWith(Constantes.PREFIX_NRO_FACTURA_SF); //Ya se que tiene al menos una factura por la primer condicion
	}
	
	public String getModosPagosElegidos(){
		String modos = "";
		boolean ef = false;
		boolean ch = false;
		boolean tr = false;
		boolean tar = false;		
		for (Pago pago : listadoPagos){
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
		String texto = "OrdenPago id: "+ id + ", ";
		texto += "fecha: " + dateFormat.format(fecha.getTime())+ ", ";
		texto += "nro: " + nro+ ", ";
		texto += "evento: " + evento.getNombreConCliente()+ ", ";
		texto += "concepto: " + concepto+ ", ";
		texto += "solicitante: " + usuarioSolicitante.getNombreCompleto()+ ", ";
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
		if (solicitudPago!=null) {
			texto += "solicitudPago: " + solicitudPago.getNro()+ ", ";
		}
		if (planCuenta!=null) {
			texto += "planCuenta: " + planCuenta.getNombre()+ ", ";
		}
		return texto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof OrdenPago){
			OrdenPago other = (OrdenPago) obj;
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
