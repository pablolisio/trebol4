package com.proit.vista.ventas.cobranzas;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.markup.ComponentTag;
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
import com.proit.modelo.ventas.Cliente;
import com.proit.modelo.ventas.Cobranza;
import com.proit.modelo.ventas.FacturaVenta;
import com.proit.modelo.ventas.FacturaVentaCobranza;
import com.proit.servicios.ventas.CobranzaService;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.ClienteSearchAutoCompleteTextField;
import com.proit.wicket.dataproviders.FacturaVentaCobranzaDataProvider;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class CobranzasPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(CobranzasPage.class.getName());
	
	private static final int RESULTADOS_POR_PAGINA = 10;
	
	private CobranzaService cobranzaService;
	
	private AutoCompleteTextField<String> clienteSearchAutoComplete;
	
	private AjaxDatePicker ajaxDatePicker;
	
	private AjaxPagingNavigator navigator;
	
	private IModel<Boolean> soloRetencionesSinValidarModel;
		
	public CobranzasPage(final PageParameters parameters) {
		super(parameters);
				
		cobranzaService = new CobranzaService();

		WebMarkupContainer listadoContainer = new WebMarkupContainer("listadoContainer");
		listadoContainer.setOutputMarkupPlaceholderTag(true);
		
		WebMarkupContainer msjFiltrar = new WebMarkupContainer("msjFiltrar");
		msjFiltrar.setOutputMarkupPlaceholderTag(true);
		
		final WebMarkupContainer dataContainer = new WebMarkupContainer("dataContainer");
		dataContainer.setOutputMarkupPlaceholderTag(true);
		listadoContainer.add(dataContainer);
		
		crearCobranzaLink();
		
		addFilters(listadoContainer, msjFiltrar);
		
		DataView<FacturaVentaCobranza> dataView = addCobranzaList(dataContainer);
		addNavigator(listadoContainer, dataView);
		
		listadoContainer.setVisible(false); //Por defecto no lo muestro
		add(listadoContainer);
		add(msjFiltrar);
		
		mostrarAlertaEnPantallaSiCorresponde(parameters);
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
	}
	
	private void crearCobranzaLink() {
		Link<Cobranza> crearCobranzaLink = new Link<Cobranza>("crearCobranzaLink", Model.of(new Cobranza())) {			
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage(new RegistrarCobranzaPage1());
			}
		};
		crearCobranzaLink.setEnabled(!isUsuarioLogueadoRolSoloLectura());
		add(crearCobranzaLink);
	}

	private void addFilters(final WebMarkupContainer container, final WebMarkupContainer msjFiltrar) {
		AutoCompleteTextField<String> clienteSearchAutoComplete = crearClienteSearchAutoCompleteTextField(container, msjFiltrar);
		add(clienteSearchAutoComplete);
		
		AjaxLink<String> todosLosClientesLink = crearTodosClientesLink(container, msjFiltrar, "todosClientes");
		add(todosLosClientesLink);
		
		crearDatePicker(container, msjFiltrar);
		add(ajaxDatePicker);
		
		AjaxLink<String> hoyLink = crearDiaLink(container, msjFiltrar, "hoy", Utils.firstMillisecondOfDay(Calendar.getInstance()).getTime());
		add(hoyLink);
		
		AjaxLink<String> todosLosDiasLink = crearDiaLink(container, msjFiltrar, "todasFechas", null);
		add(todosLosDiasLink);
		
		CheckBox soloRetencionesSinValidar = crearSoloRetencionesSinValidarCheckBox(container, msjFiltrar);
		add(soloRetencionesSinValidar);
	}

	private DataView<FacturaVentaCobranza> addCobranzaList(WebMarkupContainer container) {
		IDataProvider<FacturaVentaCobranza> facturaCobranzaDataProvider = getFacturaVentaCobranzaProvider();
		
		DataView<FacturaVentaCobranza> dataView = new DataView<FacturaVentaCobranza>("listaFacturaVentaCobranza", facturaCobranzaDataProvider, RESULTADOS_POR_PAGINA) {
			private static final long serialVersionUID = 1L;
			private static final String nombreModal = "eliminarModal";
			private int idIncremental = 1; // este atributo es utilizado para obtener un id unico para el componente modal de bootstrap.
			
			@Override
			protected void populateItem(Item<FacturaVentaCobranza> item) {
				DateFormat dateFormatFecha = new SimpleDateFormat("dd-MM-yyyy");
				Cobranza cobranza = item.getModelObject().getCobranza();
				String fecha = dateFormatFecha.format(cobranza.getFecha().getTime());
				
				Cliente cliente= cobranza.getListadoFacturas().get(0).getCliente(); //Agarro la primera, ya que tiene como minimo una factura
				String clienteStr = cliente.getRazonSocial();
				
				double totalRetenciones = cobranza.getPercIva() + cobranza.getPercIibb() + cobranza.getPercGcias() + cobranza.getPercSUSS() + cobranza.getOtrasPerc();
				double totalACobrarConRetenciones = cobranzaService.calculateTotalCobros(cobranza.getListadoCobros()) + totalRetenciones;
				
				item.add(new Label("nroRecibo", cobranza.getNroRecibo()!=null?cobranza.getNroRecibo():"<Sin Recibo>"));
				item.add(new Label("fecha", fecha));
				item.add(new Label("cliente", clienteStr));
				Label totalACobrarLbl = new Label("total", "$ " + Utils.round2Decimals(totalACobrarConRetenciones, ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale()) );
				item.add(totalACobrarLbl);
				
				Link<FacturaVentaCobranza> editarRetencionesLink = new Link<FacturaVentaCobranza>("editarRetenciones", item.getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						FacturaVentaCobranza facturaVentaCobranzaSeleccionado = (FacturaVentaCobranza) getModelObject();
						setResponsePage(new EditarRetencionesCobranzaPage(facturaVentaCobranzaSeleccionado));
					}
				};
				editarRetencionesLink.setVisible(!cobranza.isBorrado());
				item.add(editarRetencionesLink);
				
				final Boolean retencionesValidadas = cobranza.isRetencionesValidadas();
				Label retencionesValidadasLabel = new Label("retencionesValidadas"){
					private static final long serialVersionUID = 1L;
					@Override
				    protected void onComponentTag(final ComponentTag tag){
						super.onComponentTag(tag);
				        if (retencionesValidadas) {
					        tag.put("class", "glyphicon glyphicon-ok");
				        } else {
				        	tag.put("class", "glyphicon glyphicon-minus");
				        }
					}
				};
				editarRetencionesLink.add(retencionesValidadasLabel);
				
				Link<FacturaVentaCobranza> verDetallesLink = new Link<FacturaVentaCobranza>("verDetalles", item.getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						FacturaVentaCobranza facturaVentaCobranzaSeleccionado = (FacturaVentaCobranza) getModelObject();
						setResponsePage(new DetallesCobranzasPage(facturaVentaCobranzaSeleccionado));
					}
				};
				item.add(verDetallesLink);
				
				Button botonIntentarEliminar = new Button("botonIntentarEliminar");
				botonIntentarEliminar.add(new AttributeModifier("data-target", "#" + nombreModal+idIncremental));
				String textoEliminar = cobranza.isBorrado() ? " ya eliminada" : "";
				Label botonIntentarEliminarLabel = new Label("textoEliminar", textoEliminar);
				botonIntentarEliminar.add(botonIntentarEliminarLabel);
				botonIntentarEliminar.setEnabled( ! cobranza.isBorrado() && ! isUsuarioLogueadoRolSoloLectura() );
				item.add(botonIntentarEliminar);
				
				String facturasAsociadas = "";
				for (FacturaVenta factura : cobranza.getListadoFacturas()) {
					facturasAsociadas += facturasAsociadas.isEmpty() ? factura.getNro() : ", " +factura.getNro();
				}
				WebMarkupContainer webMarkupContainer = new WebMarkupContainer("eliminarModal");
				webMarkupContainer.add(new Label("cobranzaAEliminar", cobranza.getNroRecibo()));
				webMarkupContainer.add(new Label("facturasAsociadas", facturasAsociadas));
				webMarkupContainer.add(new Link<FacturaVentaCobranza>("eliminar", item.getModel()) {
					
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						FacturaVentaCobranza facturaVentaCobranzaSeleccionada = (FacturaVentaCobranza) getModelObject();
						String nroRec = facturaVentaCobranzaSeleccionada.getCobranza().getNroRecibo();
						nroRec = nroRec!=null?nroRec:"<Sin Recibo>";
						try {
							boolean fueBorrada = cobranzaService.delete(facturaVentaCobranzaSeleccionada);
							String textoPorPantalla = "La Cobranza "+ nroRec + " ha sido eliminada.";
							if ( ! fueBorrada ) {
								textoPorPantalla = "La Cobranza " + nroRec + " no fue eliminada. Se encontró otra Cobranza posterior asociada a la/s factura/s correspondiente/s.";
							}
							crearAlerta(textoPorPantalla, !fueBorrada , false);
						} catch(Exception e) {
							log.error(e.getMessage(), e);
							String textoPorPantalla = "La Cobranza " + nroRec + " no pudo ser eliminada correctamente. Por favor, vuelva a intentarlo.";
							crearAlerta(textoPorPantalla, true, false);
						}
					}
				});
				
				webMarkupContainer.setMarkupId(nombreModal+idIncremental);
				webMarkupContainer.setVisible(botonIntentarEliminar.isVisible()&&botonIntentarEliminar.isEnabled());
				idIncremental++;
				
				item.add(webMarkupContainer);
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
				actualizarContainer(container, msjFiltrar, target);
			}
			
		};
		return clienteSearchAutoComplete;
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
		AjaxLink<String> hoyLink = new AjaxLink<String>(nombreLink) {			
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				clienteSearchAutoComplete.setModelObject(null);
				target.add(clienteSearchAutoComplete);
				actualizarContainer(container, msjFiltrar, target);
			}
		};
		return hoyLink;
	}
	
	private CheckBox crearSoloRetencionesSinValidarCheckBox(final WebMarkupContainer container, final WebMarkupContainer msjFiltrar) {
		soloRetencionesSinValidarModel = Model.of(false);
		CheckBox checkSoloRetencionesSinValidar = new CheckBox("checkSoloRetencionesSinValidar", soloRetencionesSinValidarModel);
		checkSoloRetencionesSinValidar.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;

			protected void onUpdate(AjaxRequestTarget target) {
				actualizarContainer(container, msjFiltrar, target);
            }
        });
		return checkSoloRetencionesSinValidar;
	}
	
	private IDataProvider<FacturaVentaCobranza> getFacturaVentaCobranzaProvider() {
		return new FacturaVentaCobranzaDataProvider(clienteSearchAutoComplete.getModel(), ajaxDatePicker.getModel(), soloRetencionesSinValidarModel);
	}
	
	private void addNavigator(final WebMarkupContainer listadoContainer, DataView<FacturaVentaCobranza> dataView) {
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
