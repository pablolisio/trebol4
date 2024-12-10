package com.proit.vista.password;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.proit.servicios.UsuarioService;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.vista.base.FacturarOnLinePage;

@AuthorizeInstantiation({"Administrador","Solo Lectura","Desarrollador", "Editor Solicitudes Factura"})
public class CambiarClavePage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(CambiarClavePage.class.getName());
	
	private UsuarioService usuarioService;
	
	public CambiarClavePage(PageParameters parameters) {
		super(parameters);
		
		usuarioService = new UsuarioService();
		
		crearForm();
		add(new FeedbackPanel("feedback"));
		
		crearAlerta("", false, false);
		
		facturarOnLineMenu.setearMenuActivo(false, false, true, false);
	}
	
	private void crearForm() {
		final PasswordTextField claveActualTextField = new PasswordTextField("claveActual", Model.of(""));
		final PasswordTextField claveNuevaTextField = new PasswordTextField("claveNueva", Model.of(""));
		final PasswordTextField claveNuevaConfirmTextField = new PasswordTextField("claveNuevaConfirm", Model.of(""));
		Link<String> olvidoClaveLink = new Link<String>("olvidoClave") {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				try {
					usuarioService.resetPassword(getUsuarioLogueado().getEmail(), getRuntimeConfigurationType());
					crearAlerta("Su nueva clave ha sido enviada a su casilla de Email. Por favor, verifiquela.", false, false);
				} catch (MessagingException e) {
					log.error(e.getMessage(), e);
					crearAlerta("Se ha producido un error al intentar enviarle un email a su casilla. Por favor, vuelva a intentarlo.", true, false);
				}
				
			}
		};
		
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
			protected void onSubmit() {
				String claveActual = claveActualTextField.getModelObject();
				String claveNueva = claveNuevaTextField.getModelObject();
				String claveNuevaConfirm = claveNuevaConfirmTextField.getModelObject();
				
				boolean esClaveCorrecta = usuarioService.esClaveCorrecta(getUsuarioLogueado(), claveActual);
				
				if (!esClaveCorrecta) {
					error("La clave actual ingresada no es la correcta. Por favor, vuelva a intentarlo.");
				} else {
					if ( ! claveNueva.equals(claveNuevaConfirm) ) {
						error("Las claves nuevas ingresadas no coinciden. Por favor, vuelva a intentarlo.");
					} else if (claveNueva.equals(claveActual)){
						error("La clave nueva debe ser distinta a la clave actual.");
					} else {
						try {
							usuarioService.modificarClave(getUsuarioLogueado(), claveNueva, getRuntimeConfigurationType());
							crearAlerta("La clave ha sido modificada.", false, false);
						} catch (MessagingException e){
							log.error(e.getMessage(), e);
							crearAlerta("La clave ha sido modificada. Pero se ha producido un error al intentar enviarle un email a su casilla.", false, false);
						} catch(Exception e) {
							log.error(e.getMessage(), e);
							crearAlerta("La clave no pudo ser modificada correctamente. Por favor, vuelva a intentarlo.", true, false);
						}
					}
				}
			}
		};
		
		add(form);
		
		form.add(claveActualTextField);
		form.add(claveNuevaTextField);
		form.add(claveNuevaConfirmTextField);
		form.add(olvidoClaveLink);
		form.add(cancelarLink);
	}

}