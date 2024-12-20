package com.proit.vista.eventos;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
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

import com.proit.modelo.Evento;
import com.proit.modelo.Usuario;
import com.proit.servicios.EventoService;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.dataproviders.EventosDataProvider;

@AuthorizeInstantiation({"Desarrollador", "Solicitante Facturas Ventas"})
public class EventosPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(EventosPage.class.getName());
	
	private static final int RESULTADOS_POR_PAGINA = 10;
		
	private EventoService eventoService;
	private TextField<String> eventoTextField;
	
	private IModel<Boolean> soloAbiertosModel;
	private IModel<Boolean> soloUsadosPorUsuarioLogueadoModel;
	private IModel<Usuario> usuarioLogueadoModel;
	
	private AjaxPagingNavigator navigator;
	
	public EventosPage(final PageParameters parameters) {
		super(parameters);
				
		eventoService = new EventoService();
		
		WebMarkupContainer listadoContainer = new WebMarkupContainer("listadoContainer");
		listadoContainer.setOutputMarkupPlaceholderTag(true);
		
		WebMarkupContainer msjFiltrar = new WebMarkupContainer("msjFiltrar");
		msjFiltrar.setOutputMarkupPlaceholderTag(true);
		
		final WebMarkupContainer container = new WebMarkupContainer("dataContainer");
		container.setOutputMarkupPlaceholderTag(true);
		listadoContainer.add(container);
		
		crearEventoLink();
		
		addFilters(listadoContainer, msjFiltrar);
		
		DataView<Evento> dataView = addEventosList(container);
		addNavigator(listadoContainer, dataView);
				
		listadoContainer.setVisible(false); //Por defecto no lo muestro
		add(listadoContainer);
		add(msjFiltrar);
				
		mostrarAlertaEnPantallaSiCorresponde(parameters);
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
	}
	
	private void addFilters(final WebMarkupContainer container, final WebMarkupContainer msjFiltrar) {		
		eventoTextField = new TextField<String>("filtroEvento", Model.of(""));
		eventoTextField.setOutputMarkupId(true);
		
		final AjaxButton filterButton = new AjaxButton("buscar") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit();
				container.setVisible(true);//Una vez utilizado el filtro lo empiezo a mostrar siempre el listado
				msjFiltrar.setVisible(false);//Una vez utilizado el filtro no lo muestro mas al msj
				target.add(container);
				target.add(msjFiltrar);
				target.add(navigator);
			}
		};
		
		AjaxLink<String> todosLosEventosLink = crearTodosEventosLink(container, "todosEventos");
		
		soloAbiertosModel = Model.of(true);
		CheckBox checkSoloAbiertos = crearFiltroCheckBox(container, soloAbiertosModel, "soloAbiertos");
		
		usuarioLogueadoModel = Model.of(getUsuarioLogueado()); 	//por defecto traigo los eventos del usuario logueado
		soloUsadosPorUsuarioLogueadoModel = Model.of(true); 	//por defecto traigo los eventos del usuario logueado
		CheckBox checkSoloUsadosPorUsuarioLogueado = crearFiltroCheckBox(container, soloUsadosPorUsuarioLogueadoModel, "soloUsadosPorUsuarioLogueado");
		
		Form<?> form = new Form<Void>("form");
		add(form);
		form.add(eventoTextField);
		form.add(todosLosEventosLink);
		form.add(filterButton);
		form.add(checkSoloAbiertos);
		form.add(checkSoloUsadosPorUsuarioLogueado);
	}

	private void crearEventoLink() {
		Link<Evento> crearEventoLink = new Link<Evento>("crearEventoLink", Model.of(new Evento())) {			
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage(new RegistrarEventoPage());
			}
		};
		//crearEventoLink.setEnabled(!isUsuarioLogueadoRolSoloLectura());
		add(crearEventoLink);
	}

	private DataView<Evento> addEventosList(WebMarkupContainer container) {
		IDataProvider<Evento> eventosDataProvider = getEventosDataProvider();
		
		DataView<Evento> dataView = new DataView<Evento>("listaEventos", eventosDataProvider, RESULTADOS_POR_PAGINA) {
			private static final long serialVersionUID = 1L;
			private static final String nombreModal = "eliminarModal";
			private int idIncremental = 1;
			
			@Override
			protected void populateItem(Item<Evento> item) {
				Locale locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
				String razonSocialCliente = item.getModelObject().getCliente()!=null?item.getModelObject().getCliente().getRazonSocial():"-";
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				String fecha = item.getModelObject().getFecha()!=null?dateFormat.format(item.getModelObject().getFecha().getTime()):"-";
				item.add(new Label("cliente", razonSocialCliente));
				item.add(new Label("nombre", item.getModelObject().getNombre()));
				item.add(new Label("fecha", fecha));
				item.add(new Label("totalEvento", Utils.round2Decimals(item.getModelObject().getTotalEvento(), locale)));
				item.add(new Label("costoTotal", Utils.round2Decimals(item.getModelObject().getCostoTotal(), locale)));
//				item.add(new Label("totalEventoConIVA", Utils.round2Decimals(item.getModelObject().getTotalEventoConIVA(), locale)));
//				item.add(new Label("costoTotalConIVA", Utils.round2Decimals(item.getModelObject().getCostoTotalConIVA(), locale)));
				
				Link<Evento> cerrarAbrirLink = new Link<Evento>("cerrarAbrirLink", item.getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						Evento eventoSeleccionado = (Evento) getModelObject();
						
						String textoPorPantalla = "El evento '" + eventoSeleccionado.getNombreConCliente() + "' se " + (eventoSeleccionado.isCerrado()?"abrió":"cerró") + " correctamente.";
						String resultado = "OK";
						
						boolean operacionOK = eventoService.cerrarAbrirEvento(eventoSeleccionado);
						if (!operacionOK) {
							textoPorPantalla = "El evento '" + eventoSeleccionado.getNombreConCliente() + "' no puede ser cerrado ya que las facturas ventas o los pagos no coinciden con los datos ingresados en el evento.";
							resultado = "ERROR";
						}
						
						PageParameters pageParameters = new PageParameters();
						pageParameters.add("Resultado", resultado);
						pageParameters.add("TextoPantalla", textoPorPantalla);
						mostrarAlertaEnPantallaSiCorresponde(pageParameters);						
						
					}
				};
				cerrarAbrirLink.setEnabled(isUsuarioLogueadoRolDesarrollador() || isUsuarioLogueadoRolAdministrador());
				item.add(cerrarAbrirLink);
				
				final Boolean cerrado = item.getModelObject().isCerrado();
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
				cerrarAbrirLink.add(cerradoLabel);
				
				final Boolean costoFinal = item.getModelObject().isCostoFinal();
				Label costoFinalLabel = new Label("costoFinal"){
					private static final long serialVersionUID = 1L;
					@Override
				    protected void onComponentTag(final ComponentTag tag){
						super.onComponentTag(tag);
				        if (costoFinal) {
					        tag.put("class", "glyphicon glyphicon-ok text-success");
				        } else {
				        	tag.put("class", "glyphicon glyphicon-minus text-danger");
				        }
					}
				};
				item.add(costoFinalLabel);
				
				Link<Evento> editLink = new Link<Evento>("editar", item.getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						Evento eventoSeleccionado = (Evento) getModelObject();
						setResponsePage(new RegistrarEventoPage(new Integer(eventoSeleccionado.getId())));
					}
				};
				//editLink.setEnabled(!isUsuarioLogueadoRolSoloLectura());
				editLink.setVisible(!cerrado);
				item.add(editLink);
				
				Button botonIntentarEliminar = new Button("botonIntentarEliminar");
				botonIntentarEliminar.add(new AttributeModifier("data-target", "#" + nombreModal+idIncremental));
				//deleteButton.setEnabled(!isUsuarioLogueadoRolSoloLectura());
				botonIntentarEliminar.setVisible(!cerrado);
				item.add(botonIntentarEliminar);
				
				WebMarkupContainer webMarkupContainer = new WebMarkupContainer("eliminarModal");
				webMarkupContainer.add(new Label("eventoAEliminar", item.getModelObject().getNombre()));
				webMarkupContainer.add(new Link<Evento>("eliminar", item.getModel()) {
					
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						Evento eventoSeleccionado = (Evento) getModelObject();
						try {
							String whereIsUsed = eventoService.delete(eventoSeleccionado);
							String textoPorPantalla = "El evento " + eventoSeleccionado.getNombreConCliente() + " ha sido eliminado.";
							if ( !whereIsUsed.isEmpty() ) {
								textoPorPantalla = "El evento " + eventoSeleccionado.getNombreConCliente() + " no fue eliminado. El mismo esta siendo usado en las siguientes entidades: " + whereIsUsed;
							}
							crearAlerta(textoPorPantalla, !whereIsUsed.isEmpty(), false);
							
						} catch(Exception e) {
							log.error(e.getMessage(), e);
							String textoPorPantalla = "El evento " + eventoSeleccionado.getNombreConCliente()+ " no pudo ser eliminado correctamente. Por favor, vuelva a intentarlo.";
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
	
	private IDataProvider<Evento> getEventosDataProvider() {
		return new EventosDataProvider(eventoTextField.getModel(), usuarioLogueadoModel, soloAbiertosModel);
	}
	
	private AjaxLink<String> crearTodosEventosLink(final WebMarkupContainer container, String nombreLink) {
		AjaxLink<String> link = new AjaxLink<String>(nombreLink) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				eventoTextField.setModelObject(null);
				target.add(eventoTextField);
				target.add(container);
			}
		};
		return link;
	}
	
	private CheckBox crearFiltroCheckBox(final WebMarkupContainer container, IModel<Boolean> model, String checkBoxName) {
		CheckBox checkbox = new CheckBox(checkBoxName, model);
		checkbox.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;

			protected void onUpdate(AjaxRequestTarget target) {
				Usuario usuarioLogueado = null;
				if (soloUsadosPorUsuarioLogueadoModel.getObject()) {
					usuarioLogueado = getUsuarioLogueado();
				}
				usuarioLogueadoModel.setObject(usuarioLogueado);
				target.add(container);
            }
        });
		return checkbox;
	}
	
	private void addNavigator(WebMarkupContainer listadoContainer, DataView<Evento> dataView) {
		navigator = new AjaxPagingNavigator("paginator", dataView);
		listadoContainer.add(navigator);
	}
	
}