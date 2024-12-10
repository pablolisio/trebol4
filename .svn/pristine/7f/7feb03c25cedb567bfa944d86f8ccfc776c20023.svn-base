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

import com.proit.modelo.auxiliar.FacturaCompraExcel;
import com.proit.modelo.compras.FacturaCompra;

public class ExcelGeneratorListadoFacturasCompra implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(ExcelGeneratorListadoFacturasCompra.class.getName());
	
	private String excelPathGenerated;
	private String excelPathTemplate;
	
	public ExcelGeneratorListadoFacturasCompra(RuntimeConfigurationType runtimeConfigurationType) {
		super();
		
		if (runtimeConfigurationType.equals(RuntimeConfigurationType.DEPLOYMENT)){
			excelPathGenerated = Constantes.EXCEL_PATH_GENERATED_PROD;
			excelPathTemplate = Constantes.EXCEL_PATH_FACTURAS_COMPRA_TEMPLATE_PROD;
		} else {
			excelPathGenerated = Constantes.EXCEL_PATH_GENERATED_DEV;
			excelPathTemplate = Constantes.EXCEL_PATH_FACTURAS_COMPRA_TEMPLATE_DEV;
		}
	}

	public boolean generarExcel(ArrayList<FacturaCompra> listaFacturas, String excelName, double totalDeuda, double totalPagado) {
		ArrayList<FacturaCompraExcel> listaFacturasExcel = new ArrayList<FacturaCompraExcel>();
		double total = 0;
		for (FacturaCompra factura : listaFacturas) {
			FacturaCompraExcel facturaExcel = new FacturaCompraExcel();
			facturaExcel.convertFromFactura(factura);
			listaFacturasExcel.add(facturaExcel);
			if (!factura.isNotaCredito()) {
				total += factura.calculateTotal();
			} else {
				total -= factura.calculateTotal();
			}
		}

		try {
			InputStream is = new FileInputStream(excelPathTemplate);			
			OutputStream os = new FileOutputStream(excelPathGenerated + excelName);
            Context context = new Context();
            context.putVar("facturas", listaFacturasExcel);
            context.putVar("total", Utils.round(total,2));
            context.putVar("pagado", Utils.round(totalPagado,2));
            context.putVar("deuda", Utils.round(totalDeuda,2));
            JxlsHelper.getInstance().processTemplate(is, os, context);
	    } catch (IOException e) {
	    	log.error(e.getMessage(), e);
			return true;
		}
		return false;
	}	

	public String getFileName() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String excelName = "ListadoFacturasCompra" + dateFormat.format(new Date()) + ".xls";
		return excelName;
	}	

}
