package com.proit.modelo.auxiliar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.proit.modelo.compras.FacturaCompra;
import com.proit.modelo.compras.OrdenPago;
import com.proit.modelo.compras.Pago;
import com.proit.modelo.compras.Proveedor;
import com.proit.utils.Constantes;


public class OrdenPagoExcel3 {

	private String proveedor;
	
	private String nro;
	
	private String fecha;
	
	private String modoPago;
	
	private String concepto;
	
	private String facturas;
	
	private String fcFecha;
	
	private double fcSubtotal;
	
	private double fcIVA;
	
	private double fcPercIVA;
	
	private double fcPercIIBB;
	
	private double fcPercGcias;
	
	private double fcPercSUSS;
	
	private double fcOtrasPerc;
	
	private double fcTotal;
	
	private double totalOP;
	
	private double subtotalEvento;
	

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
	

	public String getFcFecha() {
		return fcFecha;
	}

	public void setFcFecha(String fcFecha) {
		this.fcFecha = fcFecha;
	}

	public double getFcSubtotal() {
		return fcSubtotal;
	}

	public void setFcSubtotal(double fcSubtotal) {
		this.fcSubtotal = fcSubtotal;
	}

	public double getFcIVA() {
		return fcIVA;
	}

	public void setFcIVA(double fcIVA) {
		this.fcIVA = fcIVA;
	}

	public double getFcPercIVA() {
		return fcPercIVA;
	}

	public void setFcPercIVA(double fcPercIVA) {
		this.fcPercIVA = fcPercIVA;
	}

	public double getFcPercIIBB() {
		return fcPercIIBB;
	}

	public void setFcPercIIBB(double fcPercIIBB) {
		this.fcPercIIBB = fcPercIIBB;
	}

	public double getFcPercGcias() {
		return fcPercGcias;
	}

	public void setFcPercGcias(double fcPercGcias) {
		this.fcPercGcias = fcPercGcias;
	}

	public double getFcPercSUSS() {
		return fcPercSUSS;
	}

	public void setFcPercSUSS(double fcPercSUSS) {
		this.fcPercSUSS = fcPercSUSS;
	}

	public double getFcOtrasPerc() {
		return fcOtrasPerc;
	}

	public void setFcOtrasPerc(double fcOtrasPerc) {
		this.fcOtrasPerc = fcOtrasPerc;
	}

	public double getFcTotal() {
		return fcTotal;
	}

	public void setFcTotal(double fcTotal) {
		this.fcTotal = fcTotal;
	}

	public double getTotalOP() {
		return totalOP;
	}

	public void setTotalOP(double totalOP) {
		this.totalOP = totalOP;
	}

	public double getSubtotalEvento() {
		return subtotalEvento;
	}

	public void setSubtotalEvento(double subtotalEvento) {
		this.subtotalEvento = subtotalEvento;
	}

	public void convertFromOrdenPagoSinFactura(OrdenPago ordenPago) {
		printOPInfo(ordenPago);
		
		// -- Info Facturas --
		String facturasAsociadas = "<Sin Factura>";
		setFacturas(facturasAsociadas);
		setFcFecha("");
		
		double totalPagos = 0;
		for (Pago pago : ordenPago.getListadoPagos()){			
			totalPagos += pago.getImporte();
		}
		setSubtotalEvento(totalPagos);
	}
	
	public void convertFromOrdenPagoConFactura(OrdenPago ordenPago, FacturaCompra factura, boolean printOPInfo, boolean printTotalOP) {
		
		if (printOPInfo) {
			printOPInfo(ordenPago);
		} else {
			printOPInfoAsEmpty();
		}
		if (printTotalOP) {
			printTotalOP(ordenPago);
		}
		
		// -- Info Facturas --
		String facturasAsociadas = "";
		if (factura.getNro().startsWith(Constantes.PREFIX_NRO_FACTURA_SF)){
			facturasAsociadas = "<Sin Factura>";
			setFcFecha("");
		} else {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			facturasAsociadas = factura.getTipoFactura().getNombreCorto() + factura.getNro();
			setFcFecha(dateFormat.format(factura.getFecha().getTime()));
			int multiplier = factura.isNotaCredito() ? -1 : 1;
			double subtFact = factura.getSubtotal() * multiplier;
			double totFact = factura.calculateTotal() * multiplier;
			setFcSubtotal(subtFact);
			setFcIVA(factura.getIva() * multiplier);
			setFcPercIVA(factura.getPercIva() * multiplier);
			setFcPercIIBB(factura.getPercIibb() * multiplier);
			setFcPercGcias(factura.getPercGcias() * multiplier);
			setFcPercSUSS(factura.getPercSUSS() * multiplier);
			setFcOtrasPerc(factura.getOtrasPerc() * multiplier);
			setFcTotal(totFact);
			
			if (printTotalOP) {
				double subtotalEvento;
				if (ordenPago.getListadoFacturas().size() == 1) {
					double totalPagos = 0;
					for (Pago pago : ordenPago.getListadoPagos()){			
						totalPagos += pago.getImporte();
					}
					subtotalEvento = totalPagos * subtFact / totFact; //proporcional
				} else { // mas de una fact
					double subtotalFacturas = 0;
					for (FacturaCompra fc : ordenPago.getListadoFacturas()) {
						if ( ! fc.isNotaCredito() ) {
							subtotalFacturas += fc.getSubtotal();
						} else {
							subtotalFacturas -= fc.getSubtotal();
						}
					}
					subtotalEvento = subtotalFacturas;
				}
				setSubtotalEvento(subtotalEvento);
			}
		}
		facturasAsociadas = facturasAsociadas.isEmpty()?"<Sin Factura>":facturasAsociadas;
		setFacturas(facturasAsociadas);
		
	}

	private void printOPInfo(OrdenPago ordenPago) {
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
		
		setConcepto(ordenPago.getConcepto()!=null?ordenPago.getConcepto():"");
	}
	
	private void printOPInfoAsEmpty() {
		setNro("");
		setFecha("");
		setProveedor("");
		setModoPago("");
		setConcepto("");
	}
	
	private void printTotalOP(OrdenPago ordenPago) {
		double totalPagos = 0;
		for (Pago pago : ordenPago.getListadoPagos()){			
			totalPagos += pago.getImporte();
		}
		setTotalOP(totalPagos);
	}
}
