package com.proit.modelo.auxiliar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.proit.modelo.compras.FacturaCompraOrdenPago;
import com.proit.modelo.compras.OrdenPago;
import com.proit.modelo.compras.Proveedor;
import com.proit.servicios.compras.OrdenPagoService;

public class ReporteOrdenesPagoExcel {
	
	private String fecha;
	
	private String nroOP;
	
	private String proveedor;
	
	private String evento;
	
	private String solicitante;
	
	private String modoPago;
	
	private double totalAPagar;

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getNroOP() {
		return nroOP;
	}

	public void setNroOP(String nroOP) {
		this.nroOP = nroOP;
	}

	public String getProveedor() {
		return proveedor;
	}

	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}

	public String getEvento() {
		return evento;
	}

	public void setEvento(String evento) {
		this.evento = evento;
	}

	public String getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(String solicitante) {
		this.solicitante = solicitante;
	}

	public String getModoPago() {
		return modoPago;
	}

	public void setModoPago(String modoPago) {
		this.modoPago = modoPago;
	}

	public double getTotalAPagar() {
		return totalAPagar;
	}

	public void setTotalAPagar(double totalAPagar) {
		this.totalAPagar = totalAPagar;
	}
	
	public void convertFromFacturaCompraOrdenPago(FacturaCompraOrdenPago facturaCompraOrdenPago, OrdenPagoService ordenPagoService) {
		DateFormat dateFormatFecha = new SimpleDateFormat("dd-MM-yyyy");
		OrdenPago ordenPago = facturaCompraOrdenPago.getOrdenPago();
		String fecha = dateFormatFecha.format(ordenPago.getFecha().getTime());
		String proveedorStr;
		//Si no tiene facturas, es porque la OP es Sin Proveedor y Sin Factura.
		if (ordenPago.getListadoFacturas().isEmpty()) {
			proveedorStr = "<Sin Proveedor>";
		} else { //Es OPNormal o OPConProveedorYSinFactura. Ambas tienen como minimo una factura
			Proveedor proveedor= ordenPago.getListadoFacturas().get(0).getProveedor(); //Agarro la primera
			proveedorStr = proveedor.getRazonSocialConCUIT();
		}
		
		setNroOP(ordenPago.getNro());
		setFecha(fecha);
		setProveedor(proveedorStr);
		setEvento(ordenPago.getEvento().getNombreConCliente());
		setSolicitante(ordenPago.getUsuarioSolicitante().getNombreCompleto());
		setModoPago(ordenPago.getModosPagosElegidos());
		setTotalAPagar(ordenPagoService.calculateTotalPagos(ordenPago.getListadoPagos()));
	}

}
