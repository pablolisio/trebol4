package com.proit.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.wicket.RuntimeConfigurationType;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;

import com.proit.modelo.auxiliar.FacturaVentaExcel;
import com.proit.modelo.ventas.FacturaVenta;

public class ExcelGeneratorListadoFacturasVenta implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(ExcelGeneratorListadoFacturasVenta.class.getName());
	
	private String excelPathGenerated;
	private String excelPathTemplate;
	
	public ExcelGeneratorListadoFacturasVenta(RuntimeConfigurationType runtimeConfigurationType) {
		super();
		
		if (runtimeConfigurationType.equals(RuntimeConfigurationType.DEPLOYMENT)){
			excelPathGenerated = Constantes.EXCEL_PATH_GENERATED_PROD;
			excelPathTemplate = Constantes.EXCEL_PATH_FACTURAS_VENTA_TEMPLATE_PROD;
		} else {
			excelPathGenerated = Constantes.EXCEL_PATH_GENERATED_DEV;
			excelPathTemplate = Constantes.EXCEL_PATH_FACTURAS_VENTA_TEMPLATE_DEV;
		}
	}
	
	public boolean generarExcel(ArrayList<FacturaVenta> listaFacturas, String excelName) {
		ArrayList<FacturaVentaExcel> listaFacturasExcel = new ArrayList<FacturaVentaExcel>();
		double totalSinIVA = 0;
		double totalConIVA = 0;
		for (FacturaVenta factura : listaFacturas) {
			FacturaVentaExcel facturaExcel = new FacturaVentaExcel();
			facturaExcel.convertFromFactura(factura);
			listaFacturasExcel.add(facturaExcel);
			if (!factura.isNotaCredito()) {
				totalSinIVA += factura.calculateSubtotal();
				totalConIVA += factura.calculateTotal();
			} else {
				totalSinIVA -= factura.calculateSubtotal();
				totalConIVA -= factura.calculateTotal();
			}
		}

		try {
			InputStream is = new FileInputStream(excelPathTemplate);			
			OutputStream os = new FileOutputStream(excelPathGenerated + excelName);
            Context context = new Context();
            context.putVar("facturas", listaFacturasExcel);
            context.putVar("totalSinIVA", Utils.round(totalSinIVA,2));
            context.putVar("totalConIVA", Utils.round(totalConIVA,2));
            JxlsHelper.getInstance().processTemplate(is, os, context);
	    } catch (IOException e) {
	    	log.error(e.getMessage(), e);
			return true;
		}
		return false;
	}
	
	public String getFileName() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String excelName = "ListadoFacturasVenta" + dateFormat.format(new Date()) + ".xls";
		return excelName;
	}

}
