package com.proit.modelo.auxiliar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.proit.modelo.compras.FacturaCompra;
import com.proit.modelo.compras.OrdenPago;
import com.proit.modelo.compras.Pago;
import com.proit.utils.NumberToLetterConverter;
import com.proit.utils.Utils;


public class OrdenPagoExcel {

	private String fecha;
	
	private String nro;
	
	private String evento;
	
	private String concepto;
	
	private String observaciones;
	
	private String solicitadoPor;
	
	private String proveedor;
	
	private double efectivo;
	
	private double tarjetaCredito;
	
	private String tercero;
	
	private ArrayList<ChequeExcel> listaCheques;
	
	private ArrayList<TransferenciaExcel> listaTransferencias; //Al primero de la lista lo uso para Transferencia y Transferencia 3ro
	
	private ArrayList<FacturaCompraExcel> listaFacturas;
	
	private String importeLetras;

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getNro() {
		return nro;
	}

	public void setNro(String nro) {
		this.nro = nro;
	}

	public String getEvento() {
		return evento;
	}

	public void setEvento(String evento) {
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

	public String getSolicitadoPor() {
		return solicitadoPor;
	}

	public void setSolicitadoPor(String solicitadoPor) {
		this.solicitadoPor = solicitadoPor;
	}	
	
	public String getProveedor() {
		return proveedor;
	}

	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}
	
	public double getEfectivo() {
		return efectivo;
	}

	public void setEfectivo(double efectivo) {
		this.efectivo = efectivo;
	}

	public String getTercero() {
		return tercero;
	}

	public void setTercero(String tercero) {
		this.tercero = tercero;
	}

	public double getTarjetaCredito() {
		return tarjetaCredito;
	}

	public void setTarjetaCredito(double tarjetaCredito) {
		this.tarjetaCredito = tarjetaCredito;
	}

	public ArrayList<ChequeExcel> getListaCheques() {
		return listaCheques;
	}

	public void setListaCheques(ArrayList<ChequeExcel> listaCheques) {
		this.listaCheques = listaCheques;
	}

	public ArrayList<TransferenciaExcel> getListaTransferencias() {
		return listaTransferencias;
	}

	public void setListaTransferencias(
			ArrayList<TransferenciaExcel> listaTransferencias) {
		this.listaTransferencias = listaTransferencias;
	}

	public ArrayList<FacturaCompraExcel> getListaFacturas() {
		return listaFacturas;
	}

	public void setListaFacturas(ArrayList<FacturaCompraExcel> listaFacturas) {
		this.listaFacturas = listaFacturas;
	}

	public String getImporteLetras() {
		return importeLetras;
	}

	public void setImporteLetras(String importeLetras) {
		this.importeLetras = importeLetras;
	}

	public void convertFromOrdenPago(OrdenPago ordenPago, String proveedor, String CBU, String alias, Locale locale) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String solicitadoPorStr = ordenPago.getUsuarioSolicitante().getNombreORazonSocial()==null || ordenPago.getUsuarioSolicitante().getNombreCompleto().isEmpty() ? "" : ordenPago.getUsuarioSolicitante().getNombreCompleto();
		String conceptoStr = ordenPago.getConcepto() ==null || ordenPago.getConcepto().isEmpty() ? "" : ordenPago.getConcepto();
		String observacionesStr = ordenPago.getObservaciones() ==null || ordenPago.getObservaciones().isEmpty() ? "" : ordenPago.getObservaciones();
		
		setNro(ordenPago.getNro());
		setFecha(dateFormat.format(ordenPago.getFecha().getTime()));
		setProveedor(proveedor);
		setSolicitadoPor(solicitadoPorStr);
		setEvento(ordenPago.getEvento().getNombreConCliente());
		setConcepto(conceptoStr);
		setObservaciones(observacionesStr);
		
		ArrayList<FacturaCompraExcel> listadoFacturas = new ArrayList<FacturaCompraExcel>();
		if (ordenPago.getListadoFacturas() != null) {
			for (FacturaCompra fact : ordenPago.getListadoFacturas() ) {
				FacturaCompraExcel facturaExcel = new FacturaCompraExcel();
				facturaExcel.convertFromFactura(fact);
				listadoFacturas.add(facturaExcel);
			}
		} else {
			FacturaCompraExcel facturaExcel = new FacturaCompraExcel();
			facturaExcel.setNro("<Sin Factura>");
			listadoFacturas.add(facturaExcel);
		}
		setListaFacturas(listadoFacturas);
		
		ArrayList<ChequeExcel> listadoCheques = new ArrayList<ChequeExcel>();
		ArrayList<TransferenciaExcel> listadoTransferencias = new ArrayList<TransferenciaExcel>();
		
		double totalPago = 0;
		for (Pago pago : ordenPago.getListadoPagos()){
			setTercero(""); //vacio por defecto
			if (pago.isEfectivo()) {
				setEfectivo( Utils.round(pago.getImporte(),2));
			} else if (pago.isTarjetaCredito()) {
				setTarjetaCredito( Utils.round(pago.getImporte(),2));
			} else if (pago.isTransferencia()) {
				TransferenciaExcel transferenciaExcel = new TransferenciaExcel();
				transferenciaExcel.convertFromPago(pago, CBU, alias, null);
				listadoTransferencias.add(transferenciaExcel);
			} else if (pago.isTransferencia3ro()) {
				TransferenciaExcel transferenciaExcel = new TransferenciaExcel();
				transferenciaExcel.convertFromPago(pago, pago.getCobroAlternativo().getCuentaBancaria().getCbu(), null, pago.getCobroAlternativo().getTitular());
				listadoTransferencias.add(transferenciaExcel);
//				setTercero( "(" +pago.getCobroAlternativo().getTitular() + ")"); //Ya no lo uso en el excel (visto con angel Julio 2016)
			}  else if (pago.isTransferenciaSinProv()) {
				TransferenciaExcel transferenciaExcel = new TransferenciaExcel();
				transferenciaExcel.convertFromPago(pago, pago.getCuentaBancaria().getCbu(), pago.getCuentaBancaria().getAlias(), null); //TODO aca llega cbu unicamente, no llega alias. Como no tiene en FE el alias para completar, llega como null
				listadoTransferencias.add(transferenciaExcel);
				setProveedor("<Sin Proveedor>");
			} else if (pago.isCheque()) {
				ChequeExcel chequeExcel = new ChequeExcel();
				chequeExcel.convertFromPago(pago);
				listadoCheques.add(chequeExcel);
			}
			
			totalPago += pago.getImporte();
		}
		
		setListaCheques(listadoCheques);
		setListaTransferencias(listadoTransferencias);
		
		if (totalPago<0) {
			setImporteLetras(NumberToLetterConverter.convertNumberToLetter(Utils.round(totalPago*(-1),2), locale));
		} else {
			setImporteLetras(NumberToLetterConverter.convertNumberToLetter(Utils.round(totalPago,2), locale));
		}
		
	}
}
