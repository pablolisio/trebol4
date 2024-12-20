package com.proit.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.apache.log4j.Logger;
import org.apache.wicket.RuntimeConfigurationType;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;


public class ExcelGeneratorPosicion implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(ExcelGeneratorPosicion.class.getName());
	
	private String excelPathGenerated;
	private String excelPathTemplate;	
	
	public ExcelGeneratorPosicion(RuntimeConfigurationType runtimeConfigurationType) {
		super();
		
		if (runtimeConfigurationType.equals(RuntimeConfigurationType.DEPLOYMENT)){
			excelPathGenerated = Constantes.EXCEL_PATH_GENERATED_PROD;
			excelPathTemplate = Constantes.EXCEL_PATH_POSICION_TEMPLATE_PROD;
		} else {
			excelPathGenerated = Constantes.EXCEL_PATH_GENERATED_DEV;
			excelPathTemplate = Constantes.EXCEL_PATH_POSICION_TEMPLATE_DEV;
		}
	}

	public boolean generarExcel(String fecha, double totalVentasXCobrarSinIVA,
			double pendientesFacturacionSinIVA, double saldoBancoCitibank,
			double saldoBancoSantander, double saldoBancoCiudad,
			double chequesCartera, double otrosAcreedor,
			double totalCostosAPagarEventosConIVA,
			double solicitudesPagoSinProcesarConIVA,
			double facturasProvPendientesPagoConIVA,
			double chequesEmitidosDiferidos, double tarjetasCredito,
			double sueldosAportesRetirosAdic, double totalCreditosTomados,
			double otrosGastosMensuales, double ivaAPagar, double otrosDeudor) {
		
		try {
			String excelName = getFileName();
			InputStream is = new FileInputStream(excelPathTemplate);			
			OutputStream os = new FileOutputStream(excelPathGenerated + excelName);
            Context context = new Context();
            context.putVar("fecha", fecha);
            context.putVar("totalVentasXCobrarSinIVA", totalVentasXCobrarSinIVA);
            context.putVar("pendientesFacturacionSinIVA", pendientesFacturacionSinIVA);
            context.putVar("saldoBancoCitibank", saldoBancoCitibank);
            context.putVar("saldoBancoSantander", saldoBancoSantander);
            context.putVar("saldoBancoCiudad", saldoBancoCiudad);
            context.putVar("chequesCartera", chequesCartera);
            context.putVar("otrosAcreedor", otrosAcreedor);
            context.putVar("totalCostosAPagarEventosConIVA", totalCostosAPagarEventosConIVA);
            context.putVar("solicitudesPagoSinProcesarConIVA", solicitudesPagoSinProcesarConIVA);
            context.putVar("facturasProvPendientesPagoConIVA", facturasProvPendientesPagoConIVA);
            context.putVar("chequesEmitidosDiferidos", chequesEmitidosDiferidos);
            context.putVar("tarjetasCredito", tarjetasCredito);
            context.putVar("sueldosAportesRetirosAdic", sueldosAportesRetirosAdic);
            context.putVar("totalCreditosTomados", totalCreditosTomados);
            context.putVar("otrosGastosMensuales", otrosGastosMensuales);
            context.putVar("ivaAPagar", ivaAPagar);
            context.putVar("otrosDeudor", otrosDeudor);
            JxlsHelper.getInstance().processTemplate(is, os, context);
	    } catch (IOException e) {
	    	log.error(e.getMessage(), e);
			return true;
		}
		return false;
	}
	
	public String getFileName() {
		return "Posicion.xlsx";
	}
	
}
