package com.proit.vista.ventas.facturas;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.ui.form.autocomplete.AutoCompleteTextField;
import com.googlecode.wicket.jquery.ui.form.datepicker.AjaxDatePicker;
import com.proit.modelo.ventas.EstadoFacturaVenta;
import com.proit.modelo.ventas.FacturaVenta;
import com.proit.servicios.EventoService;
import com.proit.servicios.ventas.CobranzaService;
import com.proit.servicios.ventas.FacturaVentaService;
import com.proit.utils.ExcelGeneratorListadoFacturasVenta;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.AJAXDownload;
import com.proit.wicket.components.ClienteSearchAutoCompleteTextField;
import com.proit.wicket.components.EventoSearchAutoCompleteTextField;
import com.proit.wicket.dataproviders.FacturasVentaDataProvider;

@AuthorizeInstantiation({"Administrador","Solo Lectura","Desarrollador"})
public class FacturasVentasPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(FacturasVentasPage.class.getName());
	
	private static final int RESULTADOS_POR_PAGINA = 10;
	
	private FacturaVentaService facturaVentaService;
	
	private AutoCompleteTextField<String> clienteSearchAutoComplete;
	private AutoCompleteTextField<String> eventoSearchAutoComplete;
	private IModel<Integer> idEventoModel;
	
	private AjaxDatePicker ajaxDatePicker;
	
	private AjaxPagingNavigator navigator;
	
	private IModel<Boolean> soloFacturasXCobrarYCobroParcialModel;
		
	public FacturasVentasPage(final PageParameters parameters) {
		super(parameters);
		
		facturaVentaService = new FacturaVentaService();

		Integer idEvento = null;
		idEventoModel = Model.of(idEvento);
		
		WebMarkupContainer listadoContainer = new WebMarkupContainer("listadoContainer");
		listadoContainer.setOutputMarkupPlaceholderTag(true);
		
		WebMarkupContainer msjFiltrar = new WebMarkupContainer("msjFiltrar");
		msjFiltrar.setOutputMarkupPlaceholderTag(true);
		
		final WebMarkupContainer dataContainer = new WebMarkupContainer("dataContainer");
		dataContainer.setOutputMarkupPlaceholderTag(true);
		listadoContainer.add(dataContainer);
		
		crearFacturaLink();
		
		addFilters(listadoContainer, msjFiltrar);
		
		agregarBotonExportarReporte(listadoContainer);
		
		DataView<FacturaVenta> dataView = addFacturaList(dataContainer);
		addNavigator(listadoContainer, dataView);
		
		listadoContainer.setVisible(false); //Por defecto no lo muestro
		add(listadoContainer);
		add(msjFiltrar);
				
		mostrarAlertaEnPantallaSiCorresponde(parameters);
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
	}
	
	private void crearFacturaLink() {
		Link<FacturaVenta> crearFacturaLink = new Link<FacturaVenta>("crearFacturaLink", Model.of(new FacturaVenta())) {			
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage(new RegistrarFacturaVentaPage());
			}
		};
		crearFacturaLink.setEnabled(!isUsuarioLogueadoRolSoloLectura());
		add(crearFacturaLink);
	}

	private void agregarBotonExportarReporte(WebMarkupContainer listadoContainer) {
		final ExcelGeneratorListadoFacturasVenta excelGenerator = new ExcelGeneratorListadoFacturasVenta(getRuntimeConfigurationType());
		final String excelName = excelGenerator.getFileName();
		final AJAXDownload download = createAjaxDownload(excelName);
		AjaxLink<FacturaVenta> generarExcelLink = new AjaxLink<FacturaVenta>("generarExcel") {			
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				Calendar fecha = null;
				if (ajaxDatePicker.getModel().getObject() != null){
					fecha = Calendar.getInstance();
					fecha.setTime(ajaxDatePicker.getModel().getObject());
				}
				ArrayList<FacturaVenta> listaFacturas = (ArrayList<FacturaVenta>) facturaVentaService.getFacturas(clienteSearchAutoComplete.getModel().getObject(), idEventoModel.getObject(), fecha , soloFacturasXCobrarYCobroParcialModel.getObject(), null, null, null, false);
				excelGenerator.generarExcel(listaFacturas, excelName);
				download.initiate(target);
			}
		};
		listadoContainer.add(generarExcelLink);
		listadoContainer.add(download);
	}
	
	private void addFilters(final WebMarkupContainer container, final WebMarkupContainer msjFiltrar) {
		AutoCompleteTextField<String> clienteSearchAutoComplete = crearClienteSearchAutoCompleteTextField(container, msjFiltrar);
		add(clienteSearchAutoComplete);
		
		AjaxLink<String> todosLosClientesLink = crearTodosClientesLink(container, msjFiltrar, "todosClientes");
		add(todosLosClientesLink);
		
		AutoCompleteTextField<String> eventoSearchAutoComplete = crearEventoSearchAutoCompleteTextField(container, msjFiltrar);
		add(eventoSearchAutoComplete);
		
		AjaxLink<String> todosLosEventosLink = crearTodosEventosLink(container, msjFiltrar, "todosEventos");
		add(todosLosEventosLink);
		
		crearDatePicker(container, msjFiltrar);
		add(ajaxDatePicker);
		
		AjaxLink<String> hoyLink = crearDiaLink(container, msjFiltrar, "hoy", Utils.firstMillisecondOfDay(Calendar.getInstance()).getTime());
		add(hoyLink);
		
		AjaxLink<String> todosLosDiasLink = crearDiaLink(container, msjFiltrar, "todos", null);
		add(todosLosDiasLink);
		
		CheckBox soloFacturasXCobrarYCobroParcial = crearSoloFacturasXCobrarYCobroParcialCheckBox(container, msjFiltrar);
		add(soloFacturasXCobrarYCobroParcial);
	}

	private DataView<FacturaVenta> addFacturaList(WebMarkupContainer container) {
		IDataProvider<FacturaVenta> facturasDataProvider = getFacturasProvider();
		
		DataView<FacturaVenta> dataView = new DataView<FacturaVenta>("listaFacturas", facturasDataProvider, RESULTADOS_POR_PAGINA) {
			private static final long serialVersionUID = 1L;
			private static final String nombreModal = "anularModal";
			private int idIncremental = 1; // este atributo es utilizado para obtener un id unico para el componente modal de bootstrap.
			
			@Override
			protected void populateItem(Item<FacturaVenta> item) {
				CobranzaService cobranzaService = new CobranzaService();
				DateFormat dateFormatFecha = new SimpleDateFormat("dd-MM-yyyy");
				Locale locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
				String fecha = dateFormatFecha.format(item.getModelObject().getFecha().getTime());
				double totalNetoFactura = cobranzaService.calculateTotalNetoFactura(item.getModelObject());
				String faltaPagar = item.getModelObject().getEstadoFacturaVenta().equals(EstadoFacturaVenta.COBRADO_PARCIAL) ? 
						" (Faltante: $" + Utils.round2Decimals(totalNetoFactura, locale) + ")"		:		"";
				item.add(new Label("fecha", fecha));
				item.add(new Label("evento", Utils.concatEventAndClient(item.getModelObject().getEvento().getNombre(), item.getModelObject().getCliente())));
				item.add(new Label("nro", item.getModelObject().getNro()));
				item.add(new Label("estado", item.getModelObject().getEstadoFacturaVenta().getNombre() + faltaPagar));
				
				double subtotal = item.getModelObject().calculateSubtotal();
				double total = item.getModelObject().calculateTotal();
				double iva = total - subtotal;
				String subtotalStr = (item.getModelObject().isNotaCredito()?"-":"") + Utils.round2Decimals(subtotal, locale);
				item.add(new Label("subtotal", subtotalStr));
				String ivaStr = (item.getModelObject().isNotaCredito()?"-":"") + Utils.round2Decimals(iva, locale);
				item.add(new Label("iva", ivaStr));
				String totalStr = (item.getModelObject().isNotaCredito()?"-":"") + Utils.round2Decimals(total, locale);
				item.add(new Label("total", totalStr));
				
				Link<FacturaVenta> verDetallesLink = new Link<FacturaVenta>("verDetalles", item.getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						FacturaVenta facturaSeleccionada = (FacturaVenta) getModelObject();
						setResponsePage(new RegistrarFacturaVentaPage(true, facturaSeleccionada));
					}
				};
				item.add(verDetallesLink);
				
				Link<FacturaVenta> editLink = new Link<FacturaVenta>("editar", item.getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						FacturaVenta facturaSeleccionada = (FacturaVenta) getModelObject();
						setResponsePage(new RegistrarFacturaVentaPage(false, facturaSeleccionada));
					}
				};
				editLink.setEnabled(item.getModelObject().isXCobrar() && !item.getModelObject().isAsociadoConUnaNC());
				item.add(editLink);
				
				Button botonIntentarEliminar = new Button("botonIntentarEliminar");
				botonIntentarEliminar.add(new AttributeModifier("data-target", "#" + nombreModal+idIncremental));
				botonIntentarEliminar.setEnabled(item.getModelObject().isXCobrar() && !item.getModelObject().isAsociadoConUnaNC());
				item.add(botonIntentarEliminar);
				
				WebMarkupContainer webMarkupContainer = new WebMarkupContainer("eliminarModal");
				webMarkupContainer.add(new Label("facturaAEliminar", item.getModelObject().getNro()));
				webMarkupContainer.add(new Link<FacturaVenta>("eliminar", item.getModel()) {
					
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						FacturaVenta facturaSeleccionada = (FacturaVenta) getModelObject();
						try {
							String whereIsUsed = facturaVentaService.delete(facturaSeleccionada);
							String textoPorPantalla = "La Factura "+facturaSeleccionada.getNro() + " ha sido eliminada.";
							if ( !whereIsUsed.isEmpty() ) {
								textoPorPantalla = "La Factura " + facturaSeleccionada.getNro() + " no fue eliminada. " + whereIsUsed;
							}							
							crearAlerta(textoPorPantalla, !whereIsUsed.isEmpty() , false);
						} catch(Exception e) {
							log.error(e.getMessage(), e);
							String textoPorPantalla = "La Factura " + facturaSeleccionada.getNro() + " no pudo ser eliminada correctamente. Por favor, vuelva a intentarlo.";
							crearAlerta(textoPorPantalla, true, false);
						}
					}
				});
				
				webMarkupContainer.setMarkupId(nombreModal+idIncremental);
				webMarkupContainer.setVisible(botonIntentarEliminar.isVisible()&&botonIntentarEliminar.isEnabled());
				idIncremental++;
				
				item.add(webMarkupContainer);
				
//				Button botonIntentarAnular = new Button("botonIntentarAnular");
//				botonIntentarAnular.add(new AttributeModifier("data-target", "#" + nombreModal+idIncremental));
//				botonIntentarAnular.setEnabled(!isUsuarioLogueadoRolSoloLectura());
//				item.add(botonIntentarAnular);
//				
//				WebMarkupContainer webMarkupContainer = new WebMarkupContainer("anularModal");
//				webMarkupContainer.add(new Label("facturaAAnular", item.getModelObject().getNro()));
//				webMarkupContainer.add(new Link<FacturaVenta>("anular", item.getModel()) {
//					
//					private static final long serialVersionUID = 1L;
//
//					@Override
//					public void onClick() {
//						FacturaVenta facturaSeleccionada = (FacturaVenta) getModelObject();
//						try {
//							boolean fueAnulada = facturaVentaService.anular(facturaSeleccionada);
//							String textoPorPantalla = "La Factura "+facturaSeleccionada.getNro() + " ha sido anulada.";
//							if ( ! fueAnulada ) {
//								textoPorPantalla = "La Factura " + facturaSeleccionada.getNro() + " no fue anulada. Solo se pueden anular facturas X Cobrar.";
//							}							
//							crearAlerta(textoPorPantalla, !fueAnulada , false);
//						} catch(Exception e) {
//							String textoPorPantalla = "La Factura " + facturaSeleccionada.getNro() + " no pudo ser anulada correctamente. Por favor, vuelva a intentarlo.";
//							crearAlerta(textoPorPantalla, true, false);
//						}
//					}
//				});
//				
//				webMarkupContainer.setMarkupId(nombreModal+idIncremental);
//				webMarkupContainer.setVisible(botonIntentarEliminar.isVisible()&&botonIntentarEliminar.isEnabled());
//				idIncremental++;
//				
//				item.add(webMarkupContainer);
			}
		};
		
		container.add(dataView);
		return dataView;
	}
	
	private AutoCompleteTextField<String> crearClienteSearchAutoCompleteTextField(final WebMarkupContainer container, final WebMarkupContainer msjFiltrar) {
		clienteSearchAutoComplete = new ClienteSearchAutoCompleteTextField("clienteSearchAutoComplete", new Model<String>()){
			private static final long serialVersionUID = 1L;			
			@Override
			protected void onSelected(AjaxRequestTarget target){
				String razonSocial = (String) clienteSearchAutoComplete.getDefaultModelObject();
				((EventoSearchAutoCompleteTextField)eventoSearchAutoComplete).setRazonSocialCliente(razonSocial);
				actualizarContainer(container, msjFiltrar, target);
			}
			
		};
		return clienteSearchAutoComplete;
	}
	
	private AutoCompleteTextField<String> crearEventoSearchAutoCompleteTextField(final WebMarkupContainer container, final WebMarkupContainer msjFiltrar) {
		eventoSearchAutoComplete = new EventoSearchAutoCompleteTextField("eventoSearchAutoComplete", new Model<String>(), false){
			private static final long serialVersionUID = 1L;			
			@Override
			protected void onSelected(AjaxRequestTarget target){
				EventoService eventoService = new EventoService();
				Integer idEvento = null;
				if (eventoSearchAutoComplete.getModel().getObject()!=null) {
					String nombreEvento = Utils.getEventFromFullName(eventoSearchAutoComplete.getModelObject());
					String razonSocialCliente = Utils.getClientFromFullName(eventoSearchAutoComplete.getModelObject());
					idEvento = eventoService.getByClienteAndNombreEvento(razonSocialCliente, nombreEvento).getId();
				}
				idEventoModel.setObject(idEvento);
				actualizarContainer(container, msjFiltrar, target);
			}
			
		};
		return eventoSearchAutoComplete;
	}
	
	private AjaxDatePicker crearDatePicker(final WebMarkupContainer container, final WebMarkupContainer msjFiltrar) {
		Options options = new Options();
		ajaxDatePicker = new AjaxDatePicker("datepicker", new Model<Date>(), "dd/MM/yyyy", options) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onValueChanged(IPartialPageRequestHandler handler) {
				actualizarContainer(container, msjFiltrar, handler);
			}
		};
		return ajaxDatePicker;
	}
	
	private AjaxLink<String> crearDiaLink(final WebMarkupContainer container, final WebMarkupContainer msjFiltrar, String nombreLink, final Date date) {
		AjaxLink<String> hoyLink = new AjaxLink<String>(nombreLink) {			
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				ajaxDatePicker.setModelObject(date);
				target.add(ajaxDatePicker);
				actualizarContainer(container, msjFiltrar, target);
			}
		};
		return hoyLink;
	}
	
	private AjaxLink<String> crearTodosClientesLink(final WebMarkupContainer container, final WebMarkupContainer msjFiltrar, String nombreLink) {
		AjaxLink<String> link = new AjaxLink<String>(nombreLink) {			
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				clienteSearchAutoComplete.setModelObject(null);
				((EventoSearchAutoCompleteTextField)eventoSearchAutoComplete).setRazonSocialCliente(null);
				target.add(clienteSearchAutoComplete);
				actualizarContainer(container, msjFiltrar, target);
			}
		};
		return link;
	}
	
	private AjaxLink<String> crearTodosEventosLink(final WebMarkupContainer container, final WebMarkupContainer msjFiltrar, String nombreLink) {
		AjaxLink<String> link = new AjaxLink<String>(nombreLink) {			
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				eventoSearchAutoComplete.setModelObject(null);
				idEventoModel.setObject(null);
				target.add(eventoSearchAutoComplete);
				actualizarContainer(container, msjFiltrar, target);
			}
		};
		return link;
	}
	
	private CheckBox crearSoloFacturasXCobrarYCobroParcialCheckBox(final WebMarkupContainer container, final WebMarkupContainer msjFiltrar) {
		soloFacturasXCobrarYCobroParcialModel = Model.of(false);
		CheckBox checkSoloFacturasXCobrarYCobroParcial = new CheckBox("checkSoloFacturasXCobrarYCobroParcial", soloFacturasXCobrarYCobroParcialModel);
		checkSoloFacturasXCobrarYCobroParcial.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;

			protected void onUpdate(AjaxRequestTarget target) {
				actualizarContainer(container, msjFiltrar, target);
            }
        });
		return checkSoloFacturasXCobrarYCobroParcial;
	}
	
	private IDataProvider<FacturaVenta> getFacturasProvider() {
		return new FacturasVentaDataProvider(clienteSearchAutoComplete.getModel(), idEventoModel, ajaxDatePicker.getModel(),soloFacturasXCobrarYCobroParcialModel, null, Model.of(false));
	}
	
	private void addNavigator(WebMarkupContainer listadoContainer, DataView<FacturaVenta> dataView) {
		navigator = new AjaxPagingNavigator("paginator", dataView);
		listadoContainer.add(navigator);
	}
	
	private void actualizarContainer(WebMarkupContainer container, WebMarkupContainer msjFiltrar, IPartialPageRequestHandler target) {
		container.setVisible(true);//Una vez utilizado el filtro lo empiezo a mostrar siempre el listado
		msjFiltrar.setVisible(false);//Una vez utilizado el filtro no lo muestro mas al msj
		target.add(container);
		target.add(msjFiltrar);
		target.add(navigator);
	}
}
