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

import com.proit.modelo.auxiliar.OrdenPagoExcel2;
import com.proit.modelo.auxiliar.ReportePlanCuenta;
import com.proit.modelo.compras.FacturaCompraOrdenPago;

public class ExcelGeneratorPlanCuentasDetalle implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(ExcelGeneratorPlanCuentasDetalle.class.getName());
	
	private String excelPathGenerated;
	private String excelPathTemplate;
	private String fileName;
	
	public ExcelGeneratorPlanCuentasDetalle(RuntimeConfigurationType runtimeConfigurationType, String nombrePlanCuenta) {
		super();
		
		if (runtimeConfigurationType.equals(RuntimeConfigurationType.DEPLOYMENT)){
			excelPathGenerated = Constantes.EXCEL_PATH_GENERATED_PROD;
			excelPathTemplate = Constantes.EXCEL_PATH_DETALLE_PLAN_CUENTAS_TEMPLATE_PROD;
		} else {
			excelPathGenerated = Constantes.EXCEL_PATH_GENERATED_DEV;
			excelPathTemplate = Constantes.EXCEL_PATH_DETALLE_PLAN_CUENTAS_TEMPLATE_DEV;
		}
		
		fileName = nombrePlanCuenta.replaceAll("/", "") +" - Detalle.xls";
	}
	
	public boolean generarExcel(ArrayList<FacturaCompraOrdenPago> listaFacturasCompraOrdenPago, ReportePlanCuenta reportePlanCuenta, Calendar mes) {
		ArrayList<OrdenPagoExcel2> listaOPExcel = new ArrayList<OrdenPagoExcel2>();
		for (FacturaCompraOrdenPago facturaCompraOrdenPago : listaFacturasCompraOrdenPago) {
			OrdenPagoExcel2 ordenPagoExcel2 = new OrdenPagoExcel2();
			ordenPagoExcel2.convertFromOrdenPago(facturaCompraOrdenPago.getOrdenPago());
			listaOPExcel.add(ordenPagoExcel2);
		}

		try {			
			InputStream is = new FileInputStream(excelPathTemplate);			
			OutputStream os = new FileOutputStream(excelPathGenerated + fileName);
            Context context = new Context();
            DateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
            context.putVar("plan", reportePlanCuenta.getNombrePlanCuenta());
            context.putVar("mes", dateFormat.format(mes.getTime()));
            context.putVar("totalMensual", reportePlanCuenta.getTotal().doubleValue());
            context.putVar("ops", listaOPExcel);
            JxlsHelper.getInstance().processTemplate(is, os, context);
	    } catch (IOException e) {
	    	log.error(e.getMessage(), e);
			return true;
		}
		return false;
	}

	public String getFileName() {
		return fileName;
	}

}
