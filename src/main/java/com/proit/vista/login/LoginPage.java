package com.proit.vista.login;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import com.proit.servicios.UsuarioService;
import com.proit.vista.base.FacturarOnLineBasePage;

public class LoginPage extends FacturarOnLineBasePage {
	
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(LoginPage.class.getName());
	
	private UsuarioService usuarioService;
	
	public LoginPage(final PageParameters parameters) {
		super(parameters);
		usuarioService = new UsuarioService();
		
		FacturarOnLineSignInPanel signInPanel = new FacturarOnLineSignInPanel("signInPanel");
		add(signInPanel);
		
		this.get("facturarOnLineMenu").setVisible(false);
			
		agregarOlvidoClave();
		
		mostrarAlertaEnPantallaSiCorresponde(parameters);
	}

	private void agregarOlvidoClave() {
		final TextField<String> emailTextField = new TextField<String>("email", Model.of(""));
		emailTextField.add(EmailAddressValidator.getInstance());
		
		Form<?> form = new Form<Void>("resetPasswordForm") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit() {				
				try{
					String email = emailTextField.getModelObject();
					String result = usuarioService.resetPassword(email, getRuntimeConfigurationType());
					if (result != null) {
						info("Su nueva clave ha sido enviada a su casilla de Email: " + email + ". Por favor, verifiquela.");
					} else {
						error("El email ingresado no fue encontrado.");
					}
				} catch (MessagingException e) {
					log.error(e.getMessage(), e);
					error("Se ha producido un error al intentar enviarle un email a su casilla. Por favor, vuelva a intentarlo.");
				}
			}
			
		};
		add(form);
		form.add(emailTextField);
	}

}
