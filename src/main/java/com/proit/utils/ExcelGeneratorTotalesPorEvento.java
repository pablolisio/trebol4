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
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.RuntimeConfigurationType;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;

import com.proit.modelo.auxiliar.ReporteTotalEvento;
import com.proit.modelo.auxiliar.ReporteTotalEventoExcel;

public class ExcelGeneratorTotalesPorEvento implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(ExcelGeneratorTotalesPorEvento.class.getName());
	
	private String excelPathGenerated;
	private String excelPathTemplate;
	
	public ExcelGeneratorTotalesPorEvento(RuntimeConfigurationType runtimeConfigurationType) {
		super();
		
		if (runtimeConfigurationType.equals(RuntimeConfigurationType.DEPLOYMENT)){
			excelPathGenerated = Constantes.EXCEL_PATH_GENERATED_PROD;
			excelPathTemplate = Constantes.EXCEL_PATH_TOTALES_EVENTO_TEMPLATE_PROD;
		} else {
			excelPathGenerated = Constantes.EXCEL_PATH_GENERATED_DEV;
			excelPathTemplate = Constantes.EXCEL_PATH_TOTALES_EVENTO_TEMPLATE_DEV;
		}
	}

	public boolean generarExcel(List<ReporteTotalEvento> listadoReporteTotalEvento, String excelName) {
		ArrayList<ReporteTotalEventoExcel> listaReporteTotalEventoExcel = new ArrayList<ReporteTotalEventoExcel>();
		for (ReporteTotalEvento reporteTotalEvento : listadoReporteTotalEvento) {
			ReporteTotalEventoExcel reporteTotalEventoExcel = new ReporteTotalEventoExcel();
			reporteTotalEventoExcel.convertFromReporteTotalEvento(reporteTotalEvento);
			listaReporteTotalEventoExcel.add(reporteTotalEventoExcel);
		}

		try {
			InputStream is = new FileInputStream(excelPathTemplate);			
			OutputStream os = new FileOutputStream(excelPathGenerated + excelName);
            Context context = new Context();
            context.putVar("eventos", listaReporteTotalEventoExcel);
            JxlsHelper.getInstance().processTemplate(is, os, context);
	    } catch (IOException e) {
	    	log.error(e.getMessage(), e);
			return true;
		}
		return false;
	}	

	public String getFileName() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String excelName = "TotalesPorEvento" + dateFormat.format(new Date()) + ".xls";
		return excelName;
	}	

}
