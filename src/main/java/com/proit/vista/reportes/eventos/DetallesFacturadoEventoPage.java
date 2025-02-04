package com.proit.vista.reportes.eventos;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.Model;

import com.proit.modelo.Usuario;
import com.proit.modelo.auxiliar.ReporteTotalEvento;
import com.proit.modelo.ventas.FacturaVenta;
import com.proit.modelo.ventas.SolicitudFacturaVenta;
import com.proit.servicios.ventas.FacturaVentaService;
import com.proit.servicios.ventas.SolicitudFacturaVentaService;
import com.proit.utils.ExcelGeneratorEventoDetalleFacturas;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.AJAXDownload;
import com.proit.wicket.dataproviders.FacturasVentaDataProvider;
import com.proit.wicket.dataproviders.SolicitudesFacturaVentaDataProvider;

@AuthorizeInstantiation({"Administrador","Desarrollador", "Solicitante Pagos", "Solicitante Facturas Ventas","Editor Solicitudes Factura"})
public class DetallesFacturadoEventoPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	private FacturaVentaService facturaVentaService;
	private SolicitudFacturaVentaService solicitudFacturaVentaService;
	
	private Locale locale;
	
	public DetallesFacturadoEventoPage(ReporteTotalEvento reporteTotalEvento) {
		locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
		
		final WebMarkupContainer dataContainerFacturas = new WebMarkupContainer("dataContainerFacturas");
		dataContainerFacturas.setOutputMarkupPlaceholderTag(true);
		add(dataContainerFacturas);
		
		final WebMarkupContainer dataContainerSolicitudes = new WebMarkupContainer("dataContainerSolicitudes");
		dataContainerSolicitudes.setOutputMarkupPlaceholderTag(true);
		add(dataContainerSolicitudes);
		
		facturaVentaService = new FacturaVentaService();
		double totalFacturadoSinIVA = facturaVentaService.calculateSumSubtotalPorEvento(reporteTotalEvento.getIdEvento());
		double totalIVAFacturado = facturaVentaService.calculateSumIVAPorEvento(reporteTotalEvento.getIdEvento());
		double totalFacturadoConIVA = totalFacturadoSinIVA + totalIVAFacturado;
		
		solicitudFacturaVentaService = new SolicitudFacturaVentaService();
		double pendienteFacturacionSinIVA = solicitudFacturaVentaService.calculateSumSubtotal(reporteTotalEvento.getIdEvento(), null, true, false);
		
		armarCuadroEncabezado(reporteTotalEvento, totalFacturadoSinIVA, pendienteFacturacionSinIVA);
		
		agregarBotonExportarReporte(reporteTotalEvento, totalFacturadoSinIVA, pendienteFacturacionSinIVA);
		
		DataView<FacturaVenta> facturasDataView = addFacturaList(dataContainerFacturas, reporteTotalEvento.getIdEvento(), totalFacturadoSinIVA, totalIVAFacturado, totalFacturadoConIVA);
		dataContainerFacturas.add(facturasDataView);
		
		DataView<SolicitudFacturaVenta> solicitudesFacturasDataView = addSolicitudesFacturaList(dataContainerSolicitudes, reporteTotalEvento.getIdEvento(), pendienteFacturacionSinIVA);
		dataContainerSolicitudes.add(solicitudesFacturasDataView);
		
		facturarOnLineMenu.setearMenuActivo(false, false, false, true);		
	}
	
	private void agregarBotonExportarReporte(final ReporteTotalEvento reporteTotalEvento, final double totalFacturadoSinIVA, final double pendienteFacturacionSinIVA) {
		String nombreEvento = Utils.concatEventAndClient(reporteTotalEvento.getEvento(), reporteTotalEvento.getCliente());
		final ExcelGeneratorEventoDetalleFacturas excelGenerator = new ExcelGeneratorEventoDetalleFacturas(getRuntimeConfigurationType(), nombreEvento);
		final AJAXDownload download = createAjaxDownload(excelGenerator.getFileName());
		AjaxLink<FacturaVenta> generarExcelLink = new AjaxLink<FacturaVenta>("generarExcel") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {

				ArrayList<FacturaVenta> listaFacturas = (ArrayList<FacturaVenta>) facturaVentaService.getFacturas(null, reporteTotalEvento.getIdEvento(), null, false, null, null, null, false);
				ArrayList<SolicitudFacturaVenta> listaSolicitudes = (ArrayList<SolicitudFacturaVenta>) solicitudFacturaVentaService.getSolicitudesFactura(null, null, true, false, reporteTotalEvento.getIdEvento());
				excelGenerator.generarExcel(listaFacturas, listaSolicitudes, reporteTotalEvento, totalFacturadoSinIVA, pendienteFacturacionSinIVA);
				download.initiate(target);
			}
		};
		add(generarExcelLink);
		add(download);
	}
	
	private void armarCuadroEncabezado(ReporteTotalEvento reporteTotalEvento, double totalFacturadoSinIVA, double pendienteFacturacionSinIVA) {
		double totalEventoSinIVA = reporteTotalEvento.getTotalEvento().doubleValue();
		double facturadoConIVA = reporteTotalEvento.getTotalEventoConIVA().doubleValue();
		double totalPagadoConIVA = reporteTotalEvento.getPagadoTotal().doubleValue();
		double rentabilidad = facturadoConIVA - totalPagadoConIVA;
		double diferencia = totalEventoSinIVA - totalFacturadoSinIVA - pendienteFacturacionSinIVA;
		
		Label eventoLbl = new Label("evento", Utils.concatEventAndClient(reporteTotalEvento.getEvento(), reporteTotalEvento.getCliente()));
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Label fechaLbl = new Label("fecha", reporteTotalEvento.getFecha()!=null?dateFormat.format(reporteTotalEvento.getFecha().getTime()):"-");
		final Boolean cerrado = reporteTotalEvento.isCerrado();
		Label cerradoLbl = new Label("cerrado", cerrado?" Cerrado":" Abierto"){
			private static final long serialVersionUID = 1L;
			@Override
		    protected void onComponentTag(final ComponentTag tag){
				super.onComponentTag(tag);
		        if (cerrado) {
			        tag.put("class", "text-danger");
		        } else {
		        	tag.put("class", "text-success");
		        }
			}
		};
		Label totalEventoSinIVALabel = new Label("totalEventoSinIVA", "$" + Utils.round2Decimals(totalEventoSinIVA, locale));
		Label totalFacturadoSinIVALabel = new Label("totalFacturadoSinIVA", "$" + Utils.round2Decimals(totalFacturadoSinIVA, locale));
		Label pendienteFacturacionLabel = new Label("pendienteFacturacion", "$" + Utils.round2Decimals(pendienteFacturacionSinIVA, locale));
		Label diferenciaLabel = new Label("diferencia", "$" + Utils.round2Decimals(diferencia, locale));
		Label facturadoConIVALabel = new Label("facturadoConIVA", "$" + Utils.round2Decimals(facturadoConIVA, locale));
		Label totalPagadoConIVALabel = new Label("totalPagadoConIVA", "$" + Utils.round2Decimals(totalPagadoConIVA, locale));				
		Label rentabilidadLabel = new Label("rentabilidad", "$" + Utils.round2Decimals(rentabilidad, locale));
		
		add(eventoLbl);
		add(fechaLbl);
		add(cerradoLbl);
		add(totalEventoSinIVALabel);
		add(totalFacturadoSinIVALabel);
		add(pendienteFacturacionLabel);
		add(diferenciaLabel);
		add(facturadoConIVALabel);
		add(totalPagadoConIVALabel);
		add(rentabilidadLabel);
	}
	
	private DataView<FacturaVenta> addFacturaList(WebMarkupContainer container, int idEvento, double totalFacturadoSinIVA, double totalIVAFacturado, double totalFacturadoConIVA) {
		IDataProvider<FacturaVenta> facturaVentaDataProvider = getFacturaVentaDataProvider(idEvento);
		
		DataView<FacturaVenta> dataView = new DataView<FacturaVenta>("listaFacturas", facturaVentaDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<FacturaVenta> item) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				FacturaVenta factura = item.getModelObject();
				item.add(new Label("fecha", dateFormat.format(factura.getFecha().getTime())));
				item.add(new Label("nroFactura", factura.getNro()));
				item.add(new Label("tipoFactura", factura.getTipoFactura().getNombreCorto()));
				
				double totalSinIVA = factura.calculateSubtotal();
				double totalConIVA = factura.calculateTotal();
				double iva = totalConIVA - totalSinIVA;
				
				String totalSinIVAStr = factura.isNotaCredito() ? "$-" : "$";
				totalSinIVAStr += Utils.round2Decimals(totalSinIVA, locale );
				Label totalSinIVALbl = new Label("totalSinIVA", totalSinIVAStr);
				item.add(totalSinIVALbl);
				
				String ivaStr = factura.isNotaCredito() ? "$-" : "$";
				ivaStr += Utils.round2Decimals(iva, locale );
				Label ivaLbl = new Label("iva", ivaStr);
				item.add(ivaLbl);
				
				String totalConIVAStr = factura.isNotaCredito() ? "$-" : "$";
				totalConIVAStr += Utils.round2Decimals(totalConIVA, locale );
				Label totalConIVALbl = new Label("totalConIVA", totalConIVAStr);
				item.add(totalConIVALbl);
			}
		};
		
		Label sumaTotalSinIVALabel = new Label("sumaTotalSinIVA", "$" + Utils.round2Decimals(totalFacturadoSinIVA, locale));
		Label sumaTotalIVALabel = new Label("sumaTotalIVA", "$" + Utils.round2Decimals(totalIVAFacturado, locale));
		Label sumaTotalConIVALabel = new Label("sumaTotalConIVA", "$" + Utils.round2Decimals(totalFacturadoConIVA, locale));
		container.add(sumaTotalSinIVALabel);
		container.add(sumaTotalIVALabel);
		container.add(sumaTotalConIVALabel);
		container.add(dataView);
		return dataView;
	}
	
	private DataView<SolicitudFacturaVenta> addSolicitudesFacturaList(WebMarkupContainer container, int idEvento, double pendienteFacturacionSinIVA) {
		IDataProvider<SolicitudFacturaVenta> solicitudesFacturaVentaDataProvider = getSolicitudesFacturaVentaDataProvider(idEvento);
		
		DataView<SolicitudFacturaVenta> dataView = new DataView<SolicitudFacturaVenta>("listaSolicitudesFactura", solicitudesFacturaVentaDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<SolicitudFacturaVenta> item) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				SolicitudFacturaVenta solicitud = item.getModelObject();
				item.add(new Label("fecha", dateFormat.format(solicitud.getFecha().getTime())));
				item.add(new Label("nroSolicitud", solicitud.getNro()));
				item.add(new Label("solicitante", solicitud.getUsuarioSolicitante().getNombreCompleto()));
				item.add(new Label("tipoFactura", solicitud.getTipoFactura().getNombreCorto()));
				
				String totalSinIVA = solicitud.isNotaCredito() ? "$-" : "$";
				totalSinIVA += Utils.round2Decimals(solicitud.calculateSubtotal(), locale );
				Label totalSinIVALbl = new Label("totalSinIVA", totalSinIVA);
				item.add(totalSinIVALbl);
			}
		};
		
		Label sumaTotalSinIVALabel = new Label("sumaTotalSinIVA", "$" + Utils.round2Decimals(pendienteFacturacionSinIVA, locale));
		container.add(sumaTotalSinIVALabel);
		container.add(dataView);
		return dataView;
	}
	
	private IDataProvider<FacturaVenta> getFacturaVentaDataProvider(int idEvento) {
		String razonSocial = null;
		Date date = null;
		return new FacturasVentaDataProvider(Model.of(razonSocial), Model.of(idEvento), Model.of(date), Model.of(false), null, Model.of(false));
	}
	
	private IDataProvider<SolicitudFacturaVenta> getSolicitudesFacturaVentaDataProvider(int idEvento) {
		String evento = null;
		Usuario usuario = null;
		boolean miTrue = true;
		boolean miFalse = true;
		return new SolicitudesFacturaVentaDataProvider(Model.of(evento), Model.of(usuario), Model.of(miTrue), Model.of(miFalse), Model.of(idEvento));
	}
	
}