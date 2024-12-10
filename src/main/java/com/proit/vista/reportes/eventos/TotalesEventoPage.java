package com.proit.vista.reportes.eventos;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.proit.modelo.auxiliar.ReporteTotalEvento;
import com.proit.modelo.compras.FacturaCompra;
import com.proit.servicios.EventoService;
import com.proit.utils.ExcelGeneratorTotalesPorEvento;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.AJAXDownload;

@AuthorizeInstantiation({"Administrador", "Desarrollador", "Solicitante Pagos", "Solicitante Facturas Ventas","Editor Solicitudes Factura"})
public class TotalesEventoPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	private EventoService eventoService;
	
	private Locale locale;
	
	private TextField<String> eventoTextField;
	
	private IModel<Boolean> verTodosModel;
	
	private Label sumaTotalEventoLabel;
	private Label sumaCostoTotalLabel;
	private Label sumaCostoTotalConIVALabel;
	private Label sumaPagadoTotalLabel;
	private Label sumaPendienteTotalLabel;
		
	public TotalesEventoPage(final PageParameters parameters) {
		super(parameters);
		
		eventoService = new EventoService();
		
		locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
		
		WebMarkupContainer container = new WebMarkupContainer("dataContainer");
		container.setOutputMarkupPlaceholderTag(true);
		add(container);
		
		addFilters(container);
		
		agregarBotonExportarReporte();
		
		addTotalesPorEvento(container);
		
		facturarOnLineMenu.setearMenuActivo(false, false, false, true);
		
		mostrarAlertaEnPantallaSiCorresponde(parameters);
	}
	
	private void addFilters(final WebMarkupContainer container) {
		eventoTextField = new TextField<String>("filtroEvento", Model.of(""));
		eventoTextField.setOutputMarkupId(true);
		
		final AjaxButton filterButton = new AjaxButton("buscar") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit();
				actualizarContainer(container, target);
			}
		};
		
		AjaxLink<String> todosLosEventosLink = crearTodosEventosLink(container, "todosEventos");
		
		verTodosModel = Model.of(false);
		CheckBox checkVerTodos = crearFiltroCheckBox(container, verTodosModel, "verTodos");
		
		Form<?> form = new Form<Void>("form");
		add(form);
		form.add(eventoTextField);
		form.add(todosLosEventosLink);
		form.add(checkVerTodos);
		form.add(filterButton);
	}
	
	private void agregarBotonExportarReporte() {
		final ExcelGeneratorTotalesPorEvento excelGenerator = new ExcelGeneratorTotalesPorEvento(getRuntimeConfigurationType());
		final String excelName = excelGenerator.getFileName();
		final AJAXDownload download = createAjaxDownload(excelName);
		AjaxLink<FacturaCompra> generarExcelLink = new AjaxLink<FacturaCompra>("generarExcel") {			
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				List<ReporteTotalEvento> listadoReporteTotalEvento = getListadoReporteTotalEvento();
				excelGenerator.generarExcel(listadoReporteTotalEvento, excelName);
				download.initiate(target);
			}
		};
		add(generarExcelLink);
		add(download);
	}
	
	private AjaxLink<String> crearTodosEventosLink(final WebMarkupContainer container, String nombreLink) {
		AjaxLink<String> link = new AjaxLink<String>(nombreLink) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				eventoTextField.setModelObject(null);
				target.add(eventoTextField);
				actualizarContainer(container, target);
			}
		};
		return link;
	}
	
	private CheckBox crearFiltroCheckBox(final WebMarkupContainer container, IModel<Boolean> model, String checkBoxName) {
		CheckBox checkbox = new CheckBox(checkBoxName, model);
		checkbox.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;

			protected void onUpdate(AjaxRequestTarget target) {
				actualizarContainer(container, target);
            }
        });
		return checkbox;
	}
	
	private void addTotalesPorEvento(WebMarkupContainer container) { 
		IDataProvider<ReporteTotalEvento> totalesEventosDataProvider = getTotalesEventosDataProvider();
		
		DataView<ReporteTotalEvento> totalesEventoDataView = new DataView<ReporteTotalEvento>("listaEventos", totalesEventosDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<ReporteTotalEvento> item) {
				ReporteTotalEvento reporteTotalEvento = item.getModelObject();
				String cliente = reporteTotalEvento.getCliente();
				String evento = reporteTotalEvento.getEvento();
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				String fecha = item.getModelObject().getFecha()!=null?dateFormat.format(item.getModelObject().getFecha().getTime()):"-";
				String responsable = item.getModelObject().getResponsableEvento();
				responsable = responsable!=null ? responsable.substring(0,responsable.length()-"@trebol4.com".length()) : "-"; //Uso solo el principio del email
				double totalEvento = reporteTotalEvento.getTotalEvento().doubleValue();
				double costoTotal = reporteTotalEvento.getCostoTotal().doubleValue();
				double costoTotalConIVA = reporteTotalEvento.getCostoTotalConIVA().doubleValue();
				double pagadoTotal = reporteTotalEvento.getPagadoTotal().doubleValue();
				double pendienteTotal = reporteTotalEvento.getPendienteTotal().doubleValue();
				final Boolean cumplido = reporteTotalEvento.isCumplido();
				final Boolean cerrado = reporteTotalEvento.isCerrado();
				
				Label clienteLabel = new Label("cliente", cliente);
				Label eventoLabel = new Label("evento", evento);
				Label fechaLabel = new Label("fecha", fecha);
				Label responsableLabel = new Label("responsable", responsable);
				Label totalEventoLabel = new Label("totalEvento", "$" + Utils.round2Decimals(totalEvento, locale));
				Label costoTotalLabel = new Label("costoTotal", "$" + Utils.round2Decimals(costoTotal, locale));
				Label costoTotalConIVALabel = new Label("costoTotalConIVA", "$" + Utils.round2Decimals(costoTotalConIVA, locale));
				Label pagadoTotalLabel = new Label("pagadoTotal", "$" + Utils.round2Decimals(pagadoTotal, locale));
				Label pendienteTotalLabel = new Label("pendienteTotal", "$" + Utils.round2Decimals(pendienteTotal, locale));
				Label cumplidoLabel = new Label("cumplido"){
					private static final long serialVersionUID = 1L;
					@Override
				    protected void onComponentTag(final ComponentTag tag){
						super.onComponentTag(tag);
				        if (cumplido) {
					        tag.put("class", "glyphicon glyphicon-ok text-default");
				        } else {
				        	tag.put("class", "glyphicon glyphicon-minus text-default");
				        }
					}
				};
				Label cerradoLabel = new Label("cerrado"){
					private static final long serialVersionUID = 1L;
					@Override
				    protected void onComponentTag(final ComponentTag tag){
						super.onComponentTag(tag);
				        if (cerrado) {
					        tag.put("class", "glyphicon glyphicon-ok text-success");
				        } else {
				        	tag.put("class", "glyphicon glyphicon-minus text-danger");
				        }
					}
				};
				
				Link<ReporteTotalEvento> verDetallesPagosLink = new Link<ReporteTotalEvento>("verDetallesPagos", item.getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						ReporteTotalEvento reporteTotalEvento = (ReporteTotalEvento) getModelObject();
						setResponsePage(new DetallesPagadoEventoPage(reporteTotalEvento));
					}
				};
				
				Link<ReporteTotalEvento> verDetallesFacturasLink = new Link<ReporteTotalEvento>("verDetallesFacturas", item.getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						ReporteTotalEvento reporteTotalEvento = (ReporteTotalEvento) getModelObject();
						setResponsePage(new DetallesFacturadoEventoPage(reporteTotalEvento));
					}
				};
				verDetallesFacturasLink.add(cumplidoLabel);
				
				item.add(clienteLabel);
				item.add(eventoLabel);
				item.add(fechaLabel);
				item.add(responsableLabel);
				item.add(totalEventoLabel);
				item.add(costoTotalLabel);
				item.add(costoTotalConIVALabel);
				item.add(pagadoTotalLabel);
				item.add(pendienteTotalLabel);				
				item.add(cerradoLabel);
				item.add(verDetallesPagosLink);
				item.add(verDetallesFacturasLink);
				
			}
		};
				
		sumaTotalEventoLabel = new Label("sumaTotalEvento");
		sumaCostoTotalLabel = new Label("sumaCostoTotal");
		sumaCostoTotalConIVALabel = new Label("sumaCostoTotalConIVA");
		sumaPagadoTotalLabel = new Label("sumaPagadoTotal");
		sumaPendienteTotalLabel = new Label("sumaPendienteTotal");
		
		recalculateTotales();
		
		sumaTotalEventoLabel.setOutputMarkupId(true);
		sumaCostoTotalLabel.setOutputMarkupId(true);
		sumaCostoTotalConIVALabel.setOutputMarkupId(true);
		sumaPagadoTotalLabel.setOutputMarkupId(true);
		sumaPendienteTotalLabel.setOutputMarkupId(true);

		container.add(sumaTotalEventoLabel);
		container.add(sumaCostoTotalLabel);
		container.add(sumaCostoTotalConIVALabel);
		container.add(sumaPagadoTotalLabel);
		container.add(sumaPendienteTotalLabel);
				
		container.add(totalesEventoDataView);
	}
	
	private IDataProvider<ReporteTotalEvento> getTotalesEventosDataProvider() {
		IDataProvider<ReporteTotalEvento> totalesEventosDataProvider = new IDataProvider<ReporteTotalEvento>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void detach() {
			}

			@Override
			public Iterator<ReporteTotalEvento> iterator(long first, long count) {
				List<ReporteTotalEvento> listadoReporteTotalEvento = getListadoReporteTotalEvento();
				return listadoReporteTotalEvento.iterator();
			}

			@Override
			public long size() {
				return eventoService.getTotalesPorEventoSize(eventoTextField.getModelObject(), verTodosModel.getObject(), isUsuarioLogueadoRolAdministrador()||isUsuarioLogueadoRolDesarrollador());
			}

			@Override
			public IModel<ReporteTotalEvento> model(ReporteTotalEvento reporteTotalEvento) {
				return new Model<ReporteTotalEvento>(reporteTotalEvento);
			}
        	
        };
		return totalesEventosDataProvider;
	}
	
	private List<ReporteTotalEvento> getListadoReporteTotalEvento() {
		List<Object[]> totalesPorEvento = eventoService.getTotalesPorEvento(eventoTextField.getModelObject(), verTodosModel.getObject(), isUsuarioLogueadoRolAdministrador()||isUsuarioLogueadoRolDesarrollador());
		List<ReporteTotalEvento> listadoReporteTotalEvento = new ArrayList<ReporteTotalEvento>();
		for (Object[] obj : totalesPorEvento) {
			ReporteTotalEvento reporteTotalEvento = new ReporteTotalEvento();
			Timestamp fechaTimestamp = ((Timestamp) obj[3]);
			Calendar fecha = null;
			if (fechaTimestamp!=null) {
				fecha = Calendar.getInstance();
				fecha.setTimeInMillis(fechaTimestamp.getTime());
			}					
			reporteTotalEvento.setIdEvento((Integer) obj[0]);
			reporteTotalEvento.setCliente((String) obj[1]);
			reporteTotalEvento.setEvento((String) obj[2]);
			reporteTotalEvento.setFecha(fecha);
			reporteTotalEvento.setCostoTotal((BigDecimal) obj[4]);
			reporteTotalEvento.setCostoTotalConIVA((BigDecimal) obj[5]);
			reporteTotalEvento.setPagadoTotal((BigDecimal) obj[6]);
			reporteTotalEvento.setPendienteTotal((BigDecimal) obj[7]);
			reporteTotalEvento.setTotalEvento((BigDecimal) obj[9]);
			reporteTotalEvento.setTotalEventoConIVA((BigDecimal) obj[10]);
			reporteTotalEvento.setCumplido((Boolean) obj[11]);
			reporteTotalEvento.setCerrado((Boolean) obj[12]);
			reporteTotalEvento.setCostoFinal((Boolean) obj[13]);
			reporteTotalEvento.setResponsableEvento((String) obj[16]);
			listadoReporteTotalEvento.add(reporteTotalEvento);
		}
		return listadoReporteTotalEvento;
	}
	
	private void recalculateTotales() {
		boolean isAdministrador = isUsuarioLogueadoRolAdministrador()||isUsuarioLogueadoRolDesarrollador();
		double sumaTotalEvento = eventoService.calculateSumTotalesPorEvento("total_evento", eventoTextField.getModelObject(), verTodosModel.getObject(), isAdministrador);
		double sumaCostoTotal = eventoService.calculateSumTotalesPorEvento("costo_total", eventoTextField.getModelObject(), verTodosModel.getObject(), isAdministrador);
		double sumaCostoTotalConIVA = eventoService.calculateSumTotalesPorEvento("costo_total_con_iva", eventoTextField.getModelObject(), verTodosModel.getObject(), isAdministrador);
		double sumaPagadoTotal = eventoService.calculateSumTotalesPorEvento("pagado_total", eventoTextField.getModelObject(), verTodosModel.getObject(), isAdministrador);
		double sumaPendienteTotal = eventoService.calculateSumTotalesPorEvento("pendiente_total", eventoTextField.getModelObject(), verTodosModel.getObject(), isAdministrador);
		
		sumaTotalEventoLabel.setDefaultModel(Model.of("$" + Utils.round2Decimals(sumaTotalEvento, locale)));
		sumaCostoTotalLabel.setDefaultModel(Model.of("$" + Utils.round2Decimals(sumaCostoTotal, locale)));
		sumaCostoTotalConIVALabel.setDefaultModel(Model.of("$" + Utils.round2Decimals(sumaCostoTotalConIVA, locale)));
		sumaPagadoTotalLabel.setDefaultModel(Model.of("$" + Utils.round2Decimals(sumaPagadoTotal, locale)));
		sumaPendienteTotalLabel.setDefaultModel(Model.of("$" + Utils.round2Decimals(sumaPendienteTotal, locale)));
	}
	
	private void actualizarContainer(final WebMarkupContainer container, AjaxRequestTarget target) {
		recalculateTotales();
		target.add(container);
	}

}
