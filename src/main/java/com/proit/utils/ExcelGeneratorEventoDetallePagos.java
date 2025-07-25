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

import org.apache.log4j.Logger;
import org.apache.wicket.RuntimeConfigurationType;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;

import com.proit.modelo.auxiliar.OrdenPagoExcel3;
import com.proit.modelo.auxiliar.ReporteTotalEvento;
import com.proit.modelo.compras.FacturaCompra;
import com.proit.modelo.compras.FacturaCompraOrdenPago;
import com.proit.modelo.compras.OrdenPago;

public class ExcelGeneratorEventoDetallePagos implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(ExcelGeneratorEventoDetallePagos.class.getName());
	
	private String excelPathGenerated;
	private String excelPathTemplate;
	private String fileName;
	
	public ExcelGeneratorEventoDetallePagos(RuntimeConfigurationType runtimeConfigurationType, String nombreEvento) {
		super();
		
		if (runtimeConfigurationType.equals(RuntimeConfigurationType.DEPLOYMENT)){
			excelPathGenerated = Constantes.EXCEL_PATH_GENERATED_PROD;
			excelPathTemplate = Constantes.EXCEL_PATH_EVENTO_DETALLE_PAGOS_TEMPLATE_PROD;
		} else {
			excelPathGenerated = Constantes.EXCEL_PATH_GENERATED_DEV;
			excelPathTemplate = Constantes.EXCEL_PATH_EVENTO_DETALLE_PAGOS_TEMPLATE_DEV;
		}
		
		fileName = nombreEvento.replaceAll("/", "") +" - DetallePagos.xls";
	}
	
	public boolean generarExcel(ArrayList<FacturaCompraOrdenPago> listaFacturasCompraOrdenPago, ReporteTotalEvento reporteTotalEvento) {
		ArrayList<OrdenPagoExcel3> listaOPExcel = new ArrayList<OrdenPagoExcel3>();
		for (FacturaCompraOrdenPago facturaCompraOrdenPago : listaFacturasCompraOrdenPago) {
			OrdenPago ordenPago = facturaCompraOrdenPago.getOrdenPago();
			if (ordenPago.isSPySF() || ordenPago.isCPySF()) {
				OrdenPagoExcel3 ordenPagoExcel3 = new OrdenPagoExcel3();
				ordenPagoExcel3.convertFromOrdenPagoSinFactura(ordenPago);
				listaOPExcel.add(ordenPagoExcel3);
			} else {
				boolean printOPInfo = true;
				boolean printTotalOP = true;
				for (FacturaCompra factura : ordenPago.getListadoFacturas()) {
					OrdenPagoExcel3 ordenPagoExcel3 = new OrdenPagoExcel3();
					ordenPagoExcel3.convertFromOrdenPagoConFactura(ordenPago, factura, printOPInfo, printTotalOP);
					//printOPInfo = false;
					printTotalOP = false;
					listaOPExcel.add(ordenPagoExcel3);
				}
				
			}
			
		}

		try {
			InputStream is = new FileInputStream(excelPathTemplate);			
			OutputStream os = new FileOutputStream(excelPathGenerated + fileName);
            Context context = new Context();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            double totalEvento = reporteTotalEvento.getTotalEvento().doubleValue();
    		double costoTotal = reporteTotalEvento.getCostoTotal().doubleValue();
    		double costoTotalConIVA = reporteTotalEvento.getCostoTotalConIVA().doubleValue();
    		double pagadoTotal = reporteTotalEvento.getPagadoTotal().doubleValue();
    		double pendienteTotal = reporteTotalEvento.getPendienteTotal().doubleValue();
            context.putVar("evento", Utils.concatEventAndClient(reporteTotalEvento.getEvento(), reporteTotalEvento.getCliente()));
            context.putVar("fecha", reporteTotalEvento.getFecha()!=null?"Fecha: "+dateFormat.format(reporteTotalEvento.getFecha().getTime()):"Fecha: -");
            context.putVar("costoFinal", reporteTotalEvento.isCostoFinal()?"Costo: final":"Costo: -");
            context.putVar("estado", reporteTotalEvento.isCerrado()?"Estado: Cerrado":"Estado: Abierto");
            context.putVar("totalEvento", totalEvento);
            context.putVar("costoTotal", costoTotal);
            context.putVar("costoTotalConIVA", costoTotalConIVA);
            context.putVar("pagadoTotal", pagadoTotal);
            context.putVar("pendienteTotal", pendienteTotal);
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
