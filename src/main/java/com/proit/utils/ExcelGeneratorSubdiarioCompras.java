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
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.apache.wicket.RuntimeConfigurationType;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;

import com.proit.modelo.auxiliar.FacturaCompraExcel;
import com.proit.modelo.compras.FacturaCompra;

public class ExcelGeneratorSubdiarioCompras implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(ExcelGeneratorSubdiarioCompras.class.getName());
	
	private String excelPathGenerated;
	private String excelPathTemplate;
	
	public ExcelGeneratorSubdiarioCompras(RuntimeConfigurationType runtimeConfigurationType) {
		super();
		
		if (runtimeConfigurationType.equals(RuntimeConfigurationType.DEPLOYMENT)){
			excelPathGenerated = Constantes.EXCEL_PATH_GENERATED_PROD;
			excelPathTemplate = Constantes.EXCEL_PATH_SUBDIARIO_COMPRAS_TEMPLATE_PROD;
		} else {
			excelPathGenerated = Constantes.EXCEL_PATH_GENERATED_DEV;
			excelPathTemplate = Constantes.EXCEL_PATH_SUBDIARIO_COMPRAS_TEMPLATE_DEV;
		}
	}

	public boolean generarExcel(ArrayList<FacturaCompra> listaFacturas, Calendar año, Calendar mes) {
		ArrayList<FacturaCompraExcel> listaFacturasExcel = new ArrayList<FacturaCompraExcel>();
		for (FacturaCompra factura : listaFacturas) {
			FacturaCompraExcel facturaExcel = new FacturaCompraExcel();
			facturaExcel.convertFromFactura(factura);
			listaFacturasExcel.add(facturaExcel);
		}

		try {
			InputStream is = new FileInputStream(excelPathTemplate);			
			OutputStream os = new FileOutputStream(excelPathGenerated + getFileName());
            Context context = new Context();
            DateFormat añoDateFormat = new SimpleDateFormat("yyyy");
    		DateFormat mesDateFormat = new SimpleDateFormat("MM");
    		String fecha = añoDateFormat.format(año.getTime());
    		if (mes!=null) {
    			fecha = mesDateFormat.format(mes.getTime()) + "/" + fecha;
    		} else {
    			fecha = "Todo " + fecha;
    		}
            context.putVar("facturas", listaFacturasExcel);
            context.putVar("titulo", "Subdiario Compras - " + fecha);
            JxlsHelper.getInstance().processTemplate(is, os, context);
	    } catch (IOException e) {
	    	log.error(e.getMessage(), e);
			return true;
		}
		return false;
	}	

	public String getFileName() {
		return "SubdiarioCompras.xls";
	}

}
