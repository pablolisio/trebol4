package com.proit.modelo.auxiliar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.proit.modelo.compras.FacturaCompra;
import com.proit.modelo.compras.OrdenPago;
import com.proit.modelo.compras.Pago;
import com.proit.modelo.compras.Proveedor;
import com.proit.utils.Constantes;


public class OrdenPagoExcel2 {

	private String proveedor;
	
	private String nro;
	
	private String fecha;
	
	private String modoPago;
	
	private String concepto;
	
	private String facturas;
	
	private double total;	

	public String getProveedor() {
		return proveedor;
	}

	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}

	public String getNro() {
		return nro;
	}

	public void setNro(String nro) {
		this.nro = nro;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getModoPago() {
		return modoPago;
	}

	public void setModoPago(String modoPago) {
		this.modoPago = modoPago;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public String getFacturas() {
		return facturas;
	}

	public void setFacturas(String facturas) {
		this.facturas = facturas;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public void convertFromOrdenPago(OrdenPago ordenPago) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		setNro(ordenPago.getNro());
		setFecha(dateFormat.format(ordenPago.getFecha().getTime()));
		
		String proveedorStr;
		//Si no tiene facturas, es porque la OP es Sin Proveedor y Sin Factura.
		if (ordenPago.getListadoFacturas().isEmpty()) {
			proveedorStr = "<Sin Proveedor>";
		} else { //Es OPNormal o OPConProveedorYSinFactura. Ambas tienen como minimo una factura
			Proveedor proveedor= ordenPago.getListadoFacturas().get(0).getProveedor(); //Agarro la primera
			proveedorStr = proveedor.getRazonSocialConCUIT();
		}
		setProveedor(proveedorStr);
		
		setModoPago(ordenPago.getModosPagosElegidos());
		
		setConcepto(ordenPago.getConcepto());
		
		String facturasAsociadas = "";
		for (FacturaCompra factura : ordenPago.getListadoFacturas()) {
			if (factura.getNro().startsWith(Constantes.PREFIX_NRO_FACTURA_SF)){
				facturasAsociadas = "<Sin Factura>";
			} else {
				String textoFactura = factura.getTipoFactura().getNombreCorto() + factura.getNro();
				facturasAsociadas += facturasAsociadas.isEmpty() ? textoFactura : ", " +textoFactura;
			}
		}
		facturasAsociadas = facturasAsociadas.isEmpty()?"<Sin Factura>":facturasAsociadas;
		setFacturas(facturasAsociadas);
		
		double totalPago = 0;
		for (Pago pago : ordenPago.getListadoPagos()){			
			totalPago += pago.getImporte();
		}
		setTotal(totalPago);		
		
	}
}
