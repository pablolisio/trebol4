package com.proit.vista.ventas.clientes;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
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

import com.proit.modelo.ventas.Cliente;
import com.proit.servicios.ventas.ClienteService;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.dataproviders.ClientesDataProvider;

@AuthorizeInstantiation({"Administrador","Solo Lectura","Desarrollador"})
public class ClientesPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(ClientesPage.class.getName());
	
	private static final int RESULTADOS_POR_PAGINA = 10;

	private ClienteService clienteService;
	
	private TextField<String> razonSocialTextField;
	private TextField<String> cuitCuilTextField;
	
	private AjaxPagingNavigator navigator;
	
	public ClientesPage(final PageParameters parameters) {
		super(parameters);
				
		clienteService = new ClienteService();
		
		WebMarkupContainer listadoContainer = new WebMarkupContainer("listadoContainer");
		listadoContainer.setOutputMarkupPlaceholderTag(true);
		
		WebMarkupContainer msjFiltrar = new WebMarkupContainer("msjFiltrar");
		msjFiltrar.setOutputMarkupPlaceholderTag(true);

		final WebMarkupContainer container = new WebMarkupContainer("dataContainer");
		container.setOutputMarkupPlaceholderTag(true);
		listadoContainer.add(container);
		
		crearClienteLink();
		
		addFilters(listadoContainer, msjFiltrar);
		
		DataView<Cliente> dataView = addClienteList(container);
		addNavigator(listadoContainer, dataView);
				
		listadoContainer.setVisible(false); //Por defecto no lo muestro
		add(listadoContainer);
		add(msjFiltrar);
		
		mostrarAlertaEnPantallaSiCorresponde(parameters);
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
	}
	
	private void crearClienteLink() {
		Link<Cliente> crearClienteLink = new Link<Cliente>("crearClienteLink", Model.of(new Cliente())) {			
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage(new RegistrarClientePage());
			}
		};
		crearClienteLink.setEnabled(!isUsuarioLogueadoRolSoloLectura());
		add(crearClienteLink);
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
	
	private DataView<Cliente> addClienteList(WebMarkupContainer container) {
		IDataProvider<Cliente> clientesDataProvider = getClientesProvider();
		
		DataView<Cliente> dataView = new DataView<Cliente>("listaClientes", clientesDataProvider, RESULTADOS_POR_PAGINA) {
			private static final long serialVersionUID = 1L;
			private static final String nombreModal = "eliminarModal";
			private int idIncremental = 1; // este atributo es utilizado para obtener un id unico para el componente modal de bootstrap.
			
			@Override
			protected void populateItem(Item<Cliente> item) {
				item.add(new Label("razonSocial", item.getModelObject().getRazonSocial()));
				String cuitCuil = item.getModelObject().getCuitCuil() != null ? item.getModelObject().getCuitCuil() : "---";
				item.add(new Label("cuitCuil", cuitCuil));
				item.add(new Label("modosPago",item.getModelObject().getModosCobroString()));
								
				Link<Cliente> editLink = new Link<Cliente>("editar", item.getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						Cliente clienteSeleccionado = (Cliente) getModelObject();
						setResponsePage(new RegistrarClientePage(new PageParameters(),new Integer(clienteSeleccionado.getId())));
					}
				};
				editLink.setEnabled(!isUsuarioLogueadoRolSoloLectura());
				item.add(editLink);
				
				Button botonIntentarEliminar = new Button("botonIntentarEliminar");
				botonIntentarEliminar.add(new AttributeModifier("data-target", "#" + nombreModal+idIncremental));
				botonIntentarEliminar.setEnabled(!isUsuarioLogueadoRolSoloLectura());
				item.add(botonIntentarEliminar);
				
				WebMarkupContainer webMarkupContainer = new WebMarkupContainer("eliminarModal");
				webMarkupContainer.add(new Label("clienteAEliminar", item.getModelObject().getRazonSocial()));
				webMarkupContainer.add(new Link<Cliente>("eliminar", item.getModel()) {
					
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						Cliente clienteSeleccionado = (Cliente) getModelObject();
						try {							
							String whereIsUsed = clienteService.delete(clienteSeleccionado);
							String textoPorPantalla = "El cliente " + clienteSeleccionado.getRazonSocial() + " ha sido eliminado.";
							if ( !whereIsUsed.isEmpty() ) {
								textoPorPantalla = "El cliente " + clienteSeleccionado.getRazonSocial() + " no fue eliminado. El mismo esta siendo usado en las siguientes entidades: " + whereIsUsed;
							}
							crearAlerta(textoPorPantalla, !whereIsUsed.isEmpty(), false);
							
						} catch(Exception e) {
							log.error(e.getMessage(), e);
							String textoPorPantalla = "El cliente " + clienteSeleccionado.getRazonSocial() + " no pudo ser eliminado correctamente. Por favor, vuelva a intentarlo.";
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
	
	private IDataProvider<Cliente> getClientesProvider() {
		return new ClientesDataProvider(razonSocialTextField.getModel(), cuitCuilTextField.getModel());
	}
	
	private void addNavigator(WebMarkupContainer listadoContainer, DataView<Cliente> dataView) {
		navigator = new AjaxPagingNavigator("paginator", dataView);
		listadoContainer.add(navigator);
	}
	
}
