package com.proit.vista.compras.proveedores;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.proit.modelo.compras.CuentaBancaria;
import com.proit.modelo.compras.Proveedor;
import com.proit.servicios.compras.ProveedorService;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.dataproviders.ProveedoresDataProvider;

@AuthorizeInstantiation({"Administrador","Solo Lectura","Desarrollador"})
public class ProveedoresPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(ProveedoresPage.class.getName());
	
	private static final int RESULTADOS_POR_PAGINA = 10;
	
	private ProveedorService proveedorService;
	
	private TextField<String> razonSocialTextField;
	private TextField<String> cuitCuilTextField;
	
	private AjaxPagingNavigator navigator;
	
	public ProveedoresPage(final PageParameters parameters) {
		super(parameters);
				
		proveedorService = new ProveedorService();
		
		WebMarkupContainer listadoContainer = new WebMarkupContainer("listadoContainer");
		listadoContainer.setOutputMarkupPlaceholderTag(true);
		
		WebMarkupContainer msjFiltrar = new WebMarkupContainer("msjFiltrar");
		msjFiltrar.setOutputMarkupPlaceholderTag(true);

		final WebMarkupContainer container = new WebMarkupContainer("dataContainer");
		container.setOutputMarkupPlaceholderTag(true);
		listadoContainer.add(container);
		
		crearProveedorLink();
		
		addFilters(listadoContainer, msjFiltrar);
		
		DataView<Proveedor> dataView = addProveedorList(container);
		addNavigator(listadoContainer, dataView);
				
		listadoContainer.setVisible(false); //Por defecto no lo muestro
		add(listadoContainer);
		add(msjFiltrar);
		
		mostrarAlertaEnPantallaSiCorresponde(parameters);
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
	}
	
	private void crearProveedorLink() {
		Link<Proveedor> crearProveedorLink = new Link<Proveedor>("crearProveedorLink", Model.of(new Proveedor())) {			
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage(new RegistrarProveedorPage());
			}
		};
		crearProveedorLink.setEnabled(!isUsuarioLogueadoRolSoloLectura());
		add(crearProveedorLink);
	}

	private void addFilters(final WebMarkupContainer container, final WebMarkupContainer msjFiltrar) {
		razonSocialTextField = new TextField<String>("filtroRazonSocial", Model.of(""));
		cuitCuilTextField = new TextField<String>("filtroCuitCuil", Model.of(""));
		
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
		
		Form<?> form = new Form<Void>("form");
		add(form);
		form.add(razonSocialTextField);
		form.add(cuitCuilTextField);
		form.add(filterButton);
		
		add(new FeedbackPanel("feedback"));
	}
	
	private DataView<Proveedor> addProveedorList(WebMarkupContainer container) {
		IDataProvider<Proveedor> proveedoresDataProvider = getProveedoresProvider();
		
		DataView<Proveedor> dataView = new DataView<Proveedor>("listaProveedores", proveedoresDataProvider, RESULTADOS_POR_PAGINA) {
			private static final long serialVersionUID = 1L;
			private static final String nombreModal = "eliminarModal";
			private int idIncremental = 1; // este atributo es utilizado para obtener un id unico para el componente modal de bootstrap.
			
			@Override
			protected void populateItem(Item<Proveedor> item) {
				CuentaBancaria cuentaBancaria = item.getModelObject().getCuentaBancaria();
				final Boolean tieneCobrosAlternativos = item.getModelObject().tieneCobrosAlternativos();
				item.add(new Label("razonSocial", item.getModelObject().getRazonSocial()));
				String cuitCuil = item.getModelObject().getCuitCuil() != null ? item.getModelObject().getCuitCuil() : "---";
				item.add(new Label("cuitCuil", cuitCuil));
				String cuenta = cuentaBancaria!=null ? cuentaBancaria.getCbu() : "---";
				item.add(new Label("cuenta", cuenta));
				item.add(new Label("modosPago",item.getModelObject().getModosPagoString()));
				Label cobrosAltLabel = new Label("cobrosAlternativos"){
					private static final long serialVersionUID = 1L;
					@Override
				    protected void onComponentTag(final ComponentTag tag){
						super.onComponentTag(tag);
				        if (tieneCobrosAlternativos) {
					        tag.put("class", "glyphicon glyphicon-ok");
				        } else {
				        	tag.put("class", "glyphicon glyphicon-minus");
				        }
					}
				};
				item.add(cobrosAltLabel);
								
				Link<Proveedor> editLink = new Link<Proveedor>("editar", item.getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						Proveedor proveedorSeleccionado = (Proveedor) getModelObject();
						setResponsePage(new RegistrarProveedorPage(new PageParameters(),new Integer(proveedorSeleccionado.getId())));
					}
				};
				editLink.setEnabled(!isUsuarioLogueadoRolSoloLectura());
				item.add(editLink);
				
				Button botonIntentarEliminar = new Button("botonIntentarEliminar");
				botonIntentarEliminar.add(new AttributeModifier("data-target", "#" + nombreModal+idIncremental));
				botonIntentarEliminar.setEnabled(!isUsuarioLogueadoRolSoloLectura());
				item.add(botonIntentarEliminar);
				
				WebMarkupContainer webMarkupContainer = new WebMarkupContainer("eliminarModal");
				webMarkupContainer.add(new Label("proveedorAEliminar", item.getModelObject().getRazonSocial()));
				webMarkupContainer.add(new Link<Proveedor>("eliminar", item.getModel()) {
					
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						Proveedor proveedorSeleccionado = (Proveedor) getModelObject();
						try {							
							String whereIsUsed = proveedorService.delete(proveedorSeleccionado);
							String textoPorPantalla = "El proveedor " + proveedorSeleccionado.getRazonSocial() + " ha sido eliminado.";
							if ( !whereIsUsed.isEmpty() ) {
								textoPorPantalla = "El proveedor " + proveedorSeleccionado.getRazonSocial() + " no fue eliminado. El mismo esta siendo usado en las siguientes entidades: " + whereIsUsed;
							}
							crearAlerta(textoPorPantalla, !whereIsUsed.isEmpty(), false);
							
						} catch(Exception e) {
							log.error(e.getMessage(), e);
							String textoPorPantalla = "El proveedor " + proveedorSeleccionado.getRazonSocial() + " no pudo ser eliminado correctamente. Por favor, vuelva a intentarlo.";
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
	
	private IDataProvider<Proveedor> getProveedoresProvider() {
		return new ProveedoresDataProvider(razonSocialTextField.getModel(), cuitCuilTextField.getModel());
	}
	
	private void addNavigator(WebMarkupContainer listadoContainer, DataView<Proveedor> dataView) {
		navigator = new AjaxPagingNavigator("paginator", dataView);
		listadoContainer.add(navigator);
	}
	
}
