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

import com.proit.modelo.auxiliar.ReporteDeudaCliente;

public class ExcelGeneratorDeudasPorCliente implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(ExcelGeneratorDeudasPorCliente.class.getName());
	
	private String excelPathGenerated;
	private String excelPathTemplate;
	
	public ExcelGeneratorDeudasPorCliente(RuntimeConfigurationType runtimeConfigurationType) {
		super();
		
		if (runtimeConfigurationType.equals(RuntimeConfigurationType.DEPLOYMENT)){
			excelPathGenerated = Constantes.EXCEL_PATH_GENERATED_PROD;
			excelPathTemplate = Constantes.EXCEL_PATH_DEUDAS_CLIENTE_TEMPLATE_PROD;
		} else {
			excelPathGenerated = Constantes.EXCEL_PATH_GENERATED_DEV;
			excelPathTemplate = Constantes.EXCEL_PATH_DEUDAS_CLIENTE_TEMPLATE_DEV;
		}
	}

	public boolean generarExcel(ArrayList<ReporteDeudaCliente> listaReporteDeudaCliente) {
		try {
			InputStream is = new FileInputStream(excelPathTemplate);			
			OutputStream os = new FileOutputStream(excelPathGenerated + getFileName());
            Context context = new Context();
            Calendar anioAnterior = Calendar.getInstance();
    		anioAnterior.add(Calendar.YEAR, -1);
    		Calendar anioActual = Calendar.getInstance();
    		DateFormat anioDateFormat = new SimpleDateFormat("yyyy");
    		context.putVar("anioAnterior", anioDateFormat.format(anioAnterior.getTime()));
    		context.putVar("anioActual", anioDateFormat.format(anioActual.getTime()));
    		
            context.putVar("listado", listaReporteDeudaCliente);
            context.putVar("titulo", "Deudas por cliente");
            JxlsHelper.getInstance().processTemplate(is, os, context);
	    } catch (IOException e) {
	    	log.error(e.getMessage(), e);
			return true;
		}
		return false;
	}	

	public String getFileName() {
		return "DeudasPorCliente.xls";
	}

}
