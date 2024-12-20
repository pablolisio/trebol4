package com.proit.vista.users;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import com.proit.modelo.Rol;
import com.proit.modelo.Usuario;
import com.proit.servicios.RolService;
import com.proit.servicios.UsuarioService;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.vista.base.FacturarOnLinePage;
import com.proit.wicket.components.ListMultipleChoiceTransfer;
//import javax.mail.MessagingException;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class RegistrarUsuarioPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(RegistrarUsuarioPage.class.getName());
	
	private UsuarioService usuarioService;
	
	private ListMultipleChoiceTransfer listMultipleChoiceTransfer;
	
	public RegistrarUsuarioPage() {
		this(new PageParameters(),null);
	}
	
	public RegistrarUsuarioPage(final PageParameters parameters, Integer userId) {
		super(parameters);
		usuarioService = new UsuarioService();
		
		setearDefaultModel(userId);
		
		boolean esEditar = verificarSiEsEditar(userId);
		
		crearForm(esEditar);
		add(new FeedbackPanel("feedback"));
		
		facturarOnLineMenu.setearMenuActivo(false, false, true, false);
		
		if ( getUsuarioLogueado() == null ){
			 facturarOnLineMenu.setVisible(false);
		}
		
		mostrarAlertaEnPantallaSiCorresponde(parameters);
	}

	private void setearDefaultModel(Integer userId) {
		Usuario usuario = new Usuario();
		if (userId != null) {
			usuario = (Usuario) usuarioService.get(userId);
		}
		this.setDefaultModel(Model.of(usuario));
	}

	private void crearForm(final boolean esEditar) {
		IModel<String> nombreModel = new PropertyModel<String>(getDefaultModel(), "nombreORazonSocial");		
		TextField<String> nombreTextField = new TextField<String>("nombre", nombreModel);
		nombreTextField.setRequired(true);
		nombreTextField.add(StringValidator.maximumLength(255));
		
		IModel<String> apellidoModel = new PropertyModel<String>(getDefaultModel(), "apellido");
		TextField<String> apellidoTextField = new TextField<String>("apellido", apellidoModel);
		apellidoTextField.setRequired(true);
		apellidoTextField.add(StringValidator.maximumLength(255));
		
		IModel<String> emailModel = new PropertyModel<String>(getDefaultModel(), "email");
		final TextField<String> emailTextField = new TextField<String>("email", emailModel);
		emailTextField.add(EmailAddressValidator.getInstance());
		emailTextField.setRequired(true);
		emailTextField.add(StringValidator.maximumLength(255));
		
		IModel<String> claveModel = new PropertyModel<String>(getDefaultModel(), "clave");
		PasswordTextField claveTextField = new PasswordTextField("clave", claveModel);
		claveTextField.add(StringValidator.maximumLength(255));
		
		IModel<String> telefonoModel = new PropertyModel<String>(getDefaultModel(), "telefono");
		TextField<String> telefonoTextField = new TextField<String>("telefono", telefonoModel);
		telefonoTextField.add(StringValidator.maximumLength(255));
		
		WebMarkupContainer rolContainer = new WebMarkupContainer("rolContainer");		
		crearListMultipleChoice(esEditar);
	    rolContainer.add(listMultipleChoiceTransfer);
		
		if (esEditar){
			emailTextField.setEnabled(false);
			claveTextField.setResetPassword(false);	//no se resetea el password
			claveTextField.setVisible(false);
		}
		
		Link<WebPage> cancelarLink = new Link<WebPage>("cancelarLink") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage(FacturarOnLinePage.class);
			}
		};
				
		Form<?> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onValidate() {
				super.onValidate();
				Usuario usuario = (Usuario)RegistrarUsuarioPage.this.getDefaultModelObject();
				String email = emailTextField.getConvertedInput();
				
				if (usuarioService.existsByEmail(email, usuario.getId())) {
					this.error("Ya existe un Usuario con el email \"" + email + "\". Utilice dicho email para recuperar la clave en caso de ser necesario.");
					onError();
					return;
				}
				
				if (listMultipleChoiceTransfer.getDestinations().getChoices().isEmpty()) {
					this.error("Al menos debe seleccionar un rol para el usuario.");
					onError();
					return;
				}
			}
			
			@Override
			protected void onSubmit() {
				Usuario usuario = (Usuario)RegistrarUsuarioPage.this.getDefaultModelObject();
				
				usuario.setListadoRoles(getSelectedRoles());
				
				String textoPorPantalla = "El usuario '" + usuario.getEmail() + "' ha sido " + (esEditar ? "editado." : "creado.");
				String resultado = "OK";
				
				if ( ! esEditar) {
					String clave = usuario.getClave();
					usuario.setClave(DigestUtils.md5Hex(clave));
					
					//Genero Token Activacion y lo envio por email junto con la clave
					usuarioService.generarYAgregarTokenActivacion(usuario);
					
					//Descomentar esto para que se envie el email para activacion de usuario.
					/*try {
						usuarioService.sendEmailWithTokenAndPass(usuario.getEmail(), clave, usuario.getActivacion());
					} catch (MessagingException e) {
						log.error(e.getMessage(), e);
						textoPorPantalla = "El usuario '" + usuario.getEmail() + "' no pudo ser creado correctamente. La casilla de email ingresada debe ser existente.";
						resultado = "ERROR";
					}*/
					
				}
				
				try {
					//el usuario sera modificado siempre que sea una edicion, y sera creado solamente si no se lanzo antes un error (sino nunca lo va a poder activar al usuario web)
					if (resultado.equals("OK")) {
						usuarioService.createOrUpdate(usuario);
					}					
				} catch(Exception e) {
					log.error(e.getMessage(), e);
					textoPorPantalla = "El usuario '" + usuario.getEmail() + "' no pudo ser " + (esEditar ? "editado " : "creado ") + "correctamente. Por favor, vuelva a intentarlo.";
					resultado = "ERROR";
				}
								
				PageParameters pageParameters = new PageParameters();
				pageParameters.add("Resultado", resultado);
				pageParameters.add("TextoPantalla", textoPorPantalla);
				setResponsePage(UsuariosPage.class, pageParameters);
			}

			private List<Rol> getSelectedRoles() {
				@SuppressWarnings("unchecked")
				List<String> finalChoices = (List<String>) listMultipleChoiceTransfer.getDestinations().getChoices();
				List<Rol> selectedRoles = new ArrayList<Rol>();
				for (String choice : finalChoices) {
					for (Rol rol : Rol.AVAILABLE_ROLES) {
						if (rol.getNombreRol().equals(choice)) {
							selectedRoles.add(rol);
						}
					}
				}
				return selectedRoles;
			}
		};
		
		add(form);
		form.add(nombreTextField);
		form.add(apellidoTextField);
		form.add(claveTextField);
		form.add(emailTextField);
		form.add(telefonoTextField);
		form.add(rolContainer);
		form.add(cancelarLink);
	}

	private void crearListMultipleChoice(final boolean esEditar) {
		List<String> actualRoles = new ArrayList<String>();
		if (esEditar) {
			Usuario usuario = (Usuario)RegistrarUsuarioPage.this.getDefaultModelObject();
			for (Rol rol : usuario.getListadoRoles()) {
				actualRoles.add(rol.getNombreRol());
			}
		}		
		listMultipleChoiceTransfer = new ListMultipleChoiceTransfer("listaRolesMultipleChoice", actualRoles){
			private static final long serialVersionUID = 1L;
			@Override
			public List<String> getOriginalChoices() {
				RolService rolService = new RolService();
				List<Rol> listaRoles = rolService.getRoles(isUsuarioLogueadoRolDesarrollador());
				List<String> listaRolesStr = new ArrayList<String>();
				for (Rol rol : listaRoles) {
					listaRolesStr.add(rol.getNombreRol());
				}
				return listaRolesStr;
			}
	    };
	}

//	private DropDownChoice<Rol> crearRolDropDownChoice(boolean esEditar) {
//		Rol rolUsuarioLogueado = getUsuarioLogueado().getRol();
//		RolService rolService = new RolService();
//		List<Rol> roles = rolService.getRoles(rolUsuarioLogueado);
//		
//		Rol rolSeleccionado = null;
//		if ( ! esEditar ) {
//			// Rol de Administrador seleccionado por defecto, al crear usuarios.
//			rolSeleccionado = Rol.ADMINISTRADOR;
//		} else {
//			rolSeleccionado = ((Usuario)RegistrarUsuarioPage.this.getDefaultModelObject()).getRol(); // Al editar, se elige el rol que ya tiene el usuario.
//		}
//		
//		IModel<Rol> rolModel = new PropertyModel<Rol>(getDefaultModel(), "rol");
//		rolModel.setObject(rolSeleccionado);
//		DropDownChoice<Rol> rolDropDownChoice = new DropDownChoice<Rol>("rol", rolModel, roles, new ChoiceRenderer<Rol>("nombreRol"));
//		rolDropDownChoice.setRequired(true);
//		
//		if (roles.size()==1) {
//			rolDropDownChoice.setEnabled(false);
//		}
//		return rolDropDownChoice;
//	}

}
