package com.proit.vista.users;

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

import com.proit.modelo.Rol;
import com.proit.modelo.Usuario;
import com.proit.servicios.UsuarioService;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.dataproviders.UsuariosDataProvider;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class UsuariosPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(UsuariosPage.class.getName());
	
	private static final int RESULTADOS_POR_PAGINA = 10;
	
	private UsuarioService usuarioService;
	
	private TextField<String> nombreTextField;
	private TextField<String> apellidoTextField;
	private TextField<String> emailTextField;
	
	private AjaxPagingNavigator navigator;
	
	public UsuariosPage(final PageParameters parameters) {
		super(parameters);
				
		usuarioService = new UsuarioService();

		final WebMarkupContainer container = new WebMarkupContainer("dataContainer");
		container.setOutputMarkupPlaceholderTag(true);
		add(container);
		
		addFilters(container);
		
		DataView<Usuario> dataView = addUserList(container);
		addNavigator(container, dataView);
				
		mostrarAlertaEnPantallaSiCorresponde(parameters);
		
		facturarOnLineMenu.setearMenuActivo(false, false, true, false);
	}

	private void addFilters(final WebMarkupContainer container) {
		nombreTextField = new TextField<String>("filtroNombre", Model.of(""));
		apellidoTextField = new TextField<String>("filtroApellido", Model.of(""));
		emailTextField = new TextField<String>("filtroEmail", Model.of(""));
		
		final AjaxButton filterButton = new AjaxButton("buscar") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit();
				target.add(container);
				target.add(navigator);
			}
		};
		
		Form<?> form = new Form<Void>("form");
		add(form);
		form.add(nombreTextField);
		form.add(apellidoTextField);
		form.add(emailTextField);
		form.add(filterButton);
		
		add(new FeedbackPanel("feedback"));
	}
	
	private DataView<Usuario> addUserList(WebMarkupContainer container) {
		IDataProvider<Usuario> usuariosDataProvider = getUsuariosProvider();
		
		DataView<Usuario> dataView = new DataView<Usuario>("listaUsuarios", usuariosDataProvider, RESULTADOS_POR_PAGINA) {
			private static final long serialVersionUID = 1L;
			private static final String nombreModal = "eliminarModal";
			private int idIncremental = 1; // este atributo es utilizado para obtener un id unico para el componente modal de bootstrap.
			
			@Override
			protected void populateItem(Item<Usuario> item) {
				item.add(new Label("nombre", item.getModelObject().getNombreORazonSocial()));
				item.add(new Label("apellido", item.getModelObject().getApellido()));
				item.add(new Label("email", item.getModelObject().getEmail()));
				String roles = "";
				for (Rol rol : item.getModelObject().getListadoRoles()){
					roles += roles.isEmpty()? "" : ", ";
					roles += rol.getNombreRol();
				}
				item.add(new Label("roles", roles));
				
				Link<Usuario> editLink = new Link<Usuario>("editar", item.getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						Usuario usuarioSeleccionado = (Usuario) getModelObject();
						setResponsePage(new RegistrarUsuarioPage(new PageParameters(),new Integer(usuarioSeleccionado.getId())));
					}
				};
				editLink.setEnabled(!item.getModelObject().isRolDesarrollador());
				item.add(editLink);
				
				Button botonIntentarEliminar = new Button("botonIntentarEliminar");
				botonIntentarEliminar.add(new AttributeModifier("data-target", "#" + nombreModal+idIncremental));
				item.add(botonIntentarEliminar);
				
				boolean deleteLinkVisible = !item.getModelObject().isRolDesarrollador()
						&& ! (isUsuarioLogueadoRolAdministrador() && item.getModelObject().equals(getUsuarioLogueado()));
				botonIntentarEliminar.setEnabled(deleteLinkVisible);
				
				WebMarkupContainer webMarkupContainer = new WebMarkupContainer("eliminarModal");
				webMarkupContainer.add(new Label("usuarioAEliminar", item.getModelObject().getNombreORazonSocial()+ " " + item.getModelObject().getApellido()));
				webMarkupContainer.add(new Link<Usuario>("eliminar", item.getModel()) {
					
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						Usuario usuarioSeleccionado = (Usuario) getModelObject();
						try {
							usuarioService.delete(usuarioSeleccionado.getId());
							String textoPorPantalla = "El usuario "+usuarioSeleccionado.getNombreORazonSocial() + " " + usuarioSeleccionado.getApellido() + " ha sido eliminado.";
							crearAlerta(textoPorPantalla, false, false);
						} catch(Exception e) {
							log.error(e.getMessage(), e);
							String textoPorPantalla = "El usuario " + usuarioSeleccionado.getNombreORazonSocial() + " " + usuarioSeleccionado.getApellido() + " no pudo ser eliminado correctamente. Por favor, vuelva a intentarlo.";
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
	
	private IDataProvider<Usuario> getUsuariosProvider() {
		return new UsuariosDataProvider(nombreTextField.getModel(), apellidoTextField.getModel(), emailTextField.getModel(), isUsuarioLogueadoRolDesarrollador());
	}
	
	private void addNavigator(final WebMarkupContainer dataContainer, DataView<Usuario> dataView) {
		navigator = new AjaxPagingNavigator("paginator", dataView);
		add(navigator);
	}
	
}
