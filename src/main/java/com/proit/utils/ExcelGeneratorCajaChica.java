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

import com.proit.modelo.CajaChica;
import com.proit.modelo.auxiliar.CajaChicaExcel;

public class ExcelGeneratorCajaChica implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(ExcelGeneratorCajaChica.class.getName());
	
	private String excelPathGenerated;
	private String excelPathTemplate;
	
	public ExcelGeneratorCajaChica(RuntimeConfigurationType runtimeConfigurationType) {
		super();
		
		if (runtimeConfigurationType.equals(RuntimeConfigurationType.DEPLOYMENT)){
			excelPathGenerated = Constantes.EXCEL_PATH_GENERATED_PROD;
			excelPathTemplate = Constantes.EXCEL_PATH_CAJA_CHICA_TEMPLATE_PROD;
		} else {
			excelPathGenerated = Constantes.EXCEL_PATH_GENERATED_DEV;
			excelPathTemplate = Constantes.EXCEL_PATH_CAJA_CHICA_TEMPLATE_DEV;
		}
	}

	public boolean generarExcel(ArrayList<CajaChica> listaCajaChica, Calendar año, Calendar mes, double totalAnual) {
		ArrayList<CajaChicaExcel> listaCajaChicaExcel = new ArrayList<CajaChicaExcel>();
		for (CajaChica caja : listaCajaChica) {
			CajaChicaExcel cajaChicaExcel = new CajaChicaExcel();
			cajaChicaExcel.convertFromCajaChica(caja);
			listaCajaChicaExcel.add(cajaChicaExcel);
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
            context.putVar("listado", listaCajaChicaExcel);
            context.putVar("titulo", "Caja Chica - " + fecha);
            context.putVar("totalAnual", totalAnual);
            JxlsHelper.getInstance().processTemplate(is, os, context);
	    } catch (IOException e) {
	    	log.error(e.getMessage(), e);
			return true;
		}
		return false;
	}	

	public String getFileName() {
		return "CajaChica.xls";
	}

}
