package com.proit.vista.reportes.ventas;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.proit.modelo.auxiliar.ReporteDeudaCliente;
import com.proit.servicios.ventas.ReportesVentasService;
import com.proit.utils.ExcelGeneratorDeudasPorCliente;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.AJAXDownload;

@AuthorizeInstantiation({"Administrador", "Desarrollador"})
public class DeudasPorClientePage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	private ReportesVentasService reportesVentasService;
	
	private Locale locale;
		
	public DeudasPorClientePage(final PageParameters parameters) {
		super(parameters);
		
		reportesVentasService = new ReportesVentasService();
		
		locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
		
		WebMarkupContainer container = new WebMarkupContainer("dataContainer");
		container.setOutputMarkupPlaceholderTag(true);
		add(container);
		
		Calendar anioAnterior = Calendar.getInstance();
		anioAnterior.add(Calendar.YEAR, -1);
		Calendar anioActual = Calendar.getInstance();
		DateFormat anioDateFormat = new SimpleDateFormat("yyyy");
		container.add(new Label("anioAnterior", anioDateFormat.format(anioAnterior.getTime())));
		container.add(new Label("anioActual", anioDateFormat.format(anioActual.getTime())));		
				
		addListado(container);
		
		agregarBotonExportarReporte();
		
		facturarOnLineMenu.setearMenuActivo(false, false, false, true);
		
		mostrarAlertaEnPantallaSiCorresponde(parameters);
	}
	
	private void agregarBotonExportarReporte() {
		final ExcelGeneratorDeudasPorCliente excelGenerator = new ExcelGeneratorDeudasPorCliente(getRuntimeConfigurationType());
		final AJAXDownload download = createAjaxDownload(excelGenerator.getFileName());
		AjaxLink<ReporteDeudaCliente> generarExcelLink = new AjaxLink<ReporteDeudaCliente>("generarExcel") {			
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				ArrayList<ReporteDeudaCliente> listado = (ArrayList<ReporteDeudaCliente>) buildListReporteDeudaCliente();
				excelGenerator.generarExcel(listado);
				download.initiate(target);
			}
		};
		add(generarExcelLink);
		add(download);
	}
		
	private void addListado(WebMarkupContainer container) { 
		IDataProvider<ReporteDeudaCliente> deudasPorClienteDataProvider = getDeudasPorClienteDataProvider();
		
		DataView<ReporteDeudaCliente> deudasPorClienteDataView = new DataView<ReporteDeudaCliente>("listaClientes", deudasPorClienteDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<ReporteDeudaCliente> item) {
				ReporteDeudaCliente reporteDeudaCliente = item.getModelObject();
				String cliente = reporteDeudaCliente.getCliente();
				double subtotalAnioAnterior = reporteDeudaCliente.getSubtotalAnioAnterior().doubleValue();
				double subtotalAnioActual = reporteDeudaCliente.getSubtotalAnioActual().doubleValue();
				double subtotalPendiente = reporteDeudaCliente.getSubtotalPendiente().doubleValue();
				double total = reporteDeudaCliente.getTotal().doubleValue();
				
				Label clienteLabel = new Label("cliente", cliente);
				Label subtotalAnioAnteriorLabel = new Label("subtotalAnioAnterior", "$" + Utils.round2Decimals(subtotalAnioAnterior, locale));
				Label subtotalAnioActualLabel = new Label("subtotalAnioActual", "$" + Utils.round2Decimals(subtotalAnioActual, locale));
				Label subtotalPendienteLabel = new Label("subtotalPendiente", "$" + Utils.round2Decimals(subtotalPendiente, locale));
				Label totalLabel = new Label("total", "$" + Utils.round2Decimals(total, locale));
				
				item.add(clienteLabel);
				item.add(subtotalAnioAnteriorLabel);
				item.add(subtotalAnioActualLabel);
				item.add(subtotalPendienteLabel);
				item.add(totalLabel);
			}
		};
		
		double sumaAnioAnterior = reportesVentasService.calculateSumDeudasPorCliente("subtotal_anio_anterior");
		double sumaAnioActual = reportesVentasService.calculateSumDeudasPorCliente("subtotal_anio_actual");
		double sumaPendiente = reportesVentasService.calculateSumDeudasPorCliente("subtotal_pendiente");
		double sumaTotales = sumaAnioAnterior + sumaAnioActual + sumaPendiente;
		
		Label sumaAnioAnteriorLabel = new Label("sumaAnioAnterior", "$" + Utils.round2Decimals(sumaAnioAnterior, locale));
		Label sumaAnioActualLabel = new Label("sumaAnioActual", "$" + Utils.round2Decimals(sumaAnioActual, locale));
		Label sumaPendienteLabel = new Label("sumaPendiente", "$" + Utils.round2Decimals(sumaPendiente, locale));
		Label sumaTotalesLabel = new Label("sumaTotales", "$" + Utils.round2Decimals(sumaTotales, locale));

		container.add(sumaAnioAnteriorLabel);
		container.add(sumaAnioActualLabel);
		container.add(sumaPendienteLabel);
		container.add(sumaTotalesLabel);
				
		container.add(deudasPorClienteDataView);
	}
	
	private IDataProvider<ReporteDeudaCliente> getDeudasPorClienteDataProvider() {
		IDataProvider<ReporteDeudaCliente> deudasPorClienteDataProvider = new IDataProvider<ReporteDeudaCliente>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void detach() {
			}

			@Override
			public Iterator<ReporteDeudaCliente> iterator(long first, long count) {
				List<ReporteDeudaCliente> listadoReporteDeudasCliente = buildListReporteDeudaCliente();
				return listadoReporteDeudasCliente.iterator();
			}

			@Override
			public long size() {
				return reportesVentasService.getDeudasPorClienteSize();
			}

			@Override
			public IModel<ReporteDeudaCliente> model(ReporteDeudaCliente reporteDeudaCliente) {
				return new Model<ReporteDeudaCliente>(reporteDeudaCliente);
			}
        	
        };
		return deudasPorClienteDataProvider;
	}
	
	private List<ReporteDeudaCliente> buildListReporteDeudaCliente() {
		List<Object[]> deudasPorCliente = reportesVentasService.getDeudasPorCliente();
		List<ReporteDeudaCliente> listadoReporteDeudasCliente = new ArrayList<ReporteDeudaCliente>();
		for (Object[] obj : deudasPorCliente) {
			ReporteDeudaCliente reporteDeudasCliente = new ReporteDeudaCliente();
			reporteDeudasCliente.setCliente((String) obj[0]);
			reporteDeudasCliente.setSubtotalAnioAnterior((BigDecimal) obj[1]);
			reporteDeudasCliente.setSubtotalAnioActual((BigDecimal) obj[2]);
			reporteDeudasCliente.setSubtotalPendiente((BigDecimal) obj[3]);
			reporteDeudasCliente.setTotal((BigDecimal) obj[4]);
			listadoReporteDeudasCliente.add(reporteDeudasCliente);
		}
		return listadoReporteDeudasCliente;
	}

}
