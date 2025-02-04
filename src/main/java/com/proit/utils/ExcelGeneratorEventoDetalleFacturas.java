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

import com.proit.modelo.auxiliar.FacturaVentaExcel;
import com.proit.modelo.auxiliar.ReporteTotalEvento;
import com.proit.modelo.auxiliar.SolicitudFacturaVentaExcel;
import com.proit.modelo.ventas.FacturaVenta;
import com.proit.modelo.ventas.SolicitudFacturaVenta;

public class ExcelGeneratorEventoDetalleFacturas implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(ExcelGeneratorEventoDetalleFacturas.class.getName());
	
	private String excelPathGenerated;
	private String excelPathTemplate;
	private String fileName;
	
	public ExcelGeneratorEventoDetalleFacturas(RuntimeConfigurationType runtimeConfigurationType, String nombreEvento) {
		super();
		
		if (runtimeConfigurationType.equals(RuntimeConfigurationType.DEPLOYMENT)){
			excelPathGenerated = Constantes.EXCEL_PATH_GENERATED_PROD;
			excelPathTemplate = Constantes.EXCEL_PATH_EVENTO_DETALLE_FACTURAS_TEMPLATE_PROD;
		} else {
			excelPathGenerated = Constantes.EXCEL_PATH_GENERATED_DEV;
			excelPathTemplate = Constantes.EXCEL_PATH_EVENTO_DETALLE_FACTURAS_TEMPLATE_DEV;
		}
		
		fileName = nombreEvento.replaceAll("/", "") +" - DetalleFacturas.xls";
	}
	
	public boolean generarExcel(ArrayList<FacturaVenta> listaFacturas,	ArrayList<SolicitudFacturaVenta> listaSolicitudes,
								ReporteTotalEvento reporteTotalEvento, double totalFacturadoSinIVA,	double pendienteFacturacionSinIVA) {
		ArrayList<FacturaVentaExcel> listaFacturasExcel = new ArrayList<FacturaVentaExcel>();
		ArrayList<SolicitudFacturaVentaExcel> listaSolicitudesExcel = new ArrayList<SolicitudFacturaVentaExcel>();
		for (FacturaVenta factura : listaFacturas) {
			FacturaVentaExcel facturaExcel = new FacturaVentaExcel();
			facturaExcel.convertFromFactura(factura);
			listaFacturasExcel.add(facturaExcel);
		}
		
		for (SolicitudFacturaVenta solicitud : listaSolicitudes) {
			SolicitudFacturaVentaExcel solicitudExcel = new SolicitudFacturaVentaExcel();
			solicitudExcel.convertFromSolicitudFactura(solicitud);
			listaSolicitudesExcel.add(solicitudExcel);
		}

		try {
			InputStream is = new FileInputStream(excelPathTemplate);			
			OutputStream os = new FileOutputStream(excelPathGenerated + fileName);
            Context context = new Context();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            double totalEventoSinIVA = reporteTotalEvento.getTotalEvento().doubleValue();
    		double facturadoConIVA = reporteTotalEvento.getTotalEventoConIVA().doubleValue();
    		double totalPagadoConIVA = reporteTotalEvento.getPagadoTotal().doubleValue();
    		double rentabilidad = facturadoConIVA - totalPagadoConIVA;
    		double diferencia = totalEventoSinIVA - totalFacturadoSinIVA - pendienteFacturacionSinIVA;
            context.putVar("evento", Utils.concatEventAndClient(reporteTotalEvento.getEvento(), reporteTotalEvento.getCliente()));
            context.putVar("fecha", reporteTotalEvento.getFecha()!=null?"Fecha: "+dateFormat.format(reporteTotalEvento.getFecha().getTime()):"Fecha: -");
            context.putVar("estado", reporteTotalEvento.isCerrado()?"Estado: Cerrado":"Estado: Abierto");
            context.putVar("totalEventoSinIVA", totalEventoSinIVA);
            context.putVar("totalFacturadoSinIVA", totalFacturadoSinIVA);
            context.putVar("pendienteFacturacionSinIVA", pendienteFacturacionSinIVA);
            context.putVar("diferencia", diferencia);
            context.putVar("facturadoConIVA", facturadoConIVA);
            context.putVar("totalPagadoConIVA", totalPagadoConIVA);
            context.putVar("rentabilidad", rentabilidad);
            context.putVar("facturas", listaFacturasExcel);
            context.putVar("solicitudes", listaSolicitudesExcel);
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
