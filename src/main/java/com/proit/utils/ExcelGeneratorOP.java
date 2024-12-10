package com.proit.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.wicket.RuntimeConfigurationType;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;

import com.proit.modelo.auxiliar.ChequeExcel;
import com.proit.modelo.auxiliar.FacturaCompraExcel;
import com.proit.modelo.auxiliar.OrdenPagoExcel;
import com.proit.modelo.auxiliar.TransferenciaExcel;
import com.proit.modelo.compras.OrdenPago;

public class ExcelGeneratorOP implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(ExcelGeneratorOP.class.getName());
	
	private String excelPathGenerated;
	private String excelPathTemplate;	
	
	public ExcelGeneratorOP(RuntimeConfigurationType runtimeConfigurationType) {
		super();
		
		if (runtimeConfigurationType.equals(RuntimeConfigurationType.DEPLOYMENT)){
			excelPathGenerated = Constantes.EXCEL_PATH_GENERATED_PROD;
			excelPathTemplate = Constantes.EXCEL_PATH_OP_TEMPLATE_PROD;
		} else {
			excelPathGenerated = Constantes.EXCEL_PATH_GENERATED_DEV;
			excelPathTemplate = Constantes.EXCEL_PATH_OP_TEMPLATE_DEV;
		}
	}

	public boolean generarExcel(OrdenPago ordenPago, String proveedor, String CBU, String alias, Locale locale) {
		OrdenPagoExcel opExcel = new OrdenPagoExcel();
		opExcel.convertFromOrdenPago(ordenPago, proveedor, CBU, alias, locale);
		
		try {
			String excelName = getFileName(ordenPago);
			InputStream is = new FileInputStream(excelPathTemplate);			
			OutputStream os = new FileOutputStream(excelPathGenerated + excelName);
            Context context = new Context();
            context.putVar("op", opExcel);
            fillCheques(opExcel, context);
            fillTransferencias(opExcel, context);
            fillFacturas(opExcel, context);
            JxlsHelper.getInstance().processTemplate(is, os, context);
	    } catch (IOException e) {
	    	log.error(e.getMessage(), e);
			return true;
		}
		return false;
	}
	
	public String getFileName(OrdenPago ordenPago) {
		return "OP-" + ordenPago.getNro().replace("/", "_") + ".xlsx";
	}
	
	private void fillCheques(OrdenPagoExcel opExcel, Context context) {
		ArrayList<ChequeExcel> listadoCheques = opExcel.getListaCheques();
		
		for (int i = 0; i < Constantes.MAX_CANTIDAD_CHEQUES_POR_ORDEN_PERMITIDA; i++) {
			String varName = "cheque" + (i+1) ;
			if ( i < listadoCheques.size() ) {
				context.putVar(varName, listadoCheques.get(i));
			} else {
				ChequeExcel chequeExcel = new ChequeExcel();
				chequeExcel.setBanco("");
				chequeExcel.setFecha("");
				chequeExcel.setNro("");
				context.putVar(varName, chequeExcel);
			}
		}		
	}
	
	private void fillTransferencias(OrdenPagoExcel opExcel, Context context) {
		ArrayList<TransferenciaExcel> listadoTransferencias = opExcel.getListaTransferencias();
		
		for (int i = 0; i < Constantes.MAX_CANTIDAD_TRANSFERENCIAS_POR_ORDEN_PERMITIDA; i++) {
			String varName = "transf" + (i+1) ;
			if ( i < listadoTransferencias.size() ) {
				context.putVar(varName, listadoTransferencias.get(i));
			} else {
				TransferenciaExcel transferenciaExcel = new TransferenciaExcel();
				transferenciaExcel.setCBU("");
				transferenciaExcel.setFecha("");
				context.putVar(varName, transferenciaExcel);
			}
		}		
	}
	
	private void fillFacturas(OrdenPagoExcel opExcel, Context context) {
		ArrayList<FacturaCompraExcel> listadoFacturas = opExcel.getListaFacturas();
		for (int i = 0; i < Constantes.MAX_CANTIDAD_FACTURAS_POR_ORDEN_PERMITIDA; i++) {
			String varName = "factura" + (i+1) ;
			if ( i < listadoFacturas.size() ) {
				context.putVar(varName, listadoFacturas.get(i));
			} else {
				FacturaCompraExcel facturaExcel = new FacturaCompraExcel();
				facturaExcel.setEstado("");
				facturaExcel.setFecha("");
				facturaExcel.setNro("");
				facturaExcel.setProveedor("");
				context.putVar(varName, facturaExcel);
			}
		}		
	}

}
