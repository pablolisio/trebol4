package com.proit.wicket.forms;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.proit.modelo.auxiliar.EmailAEnviar;
import com.proit.utils.EmailSender;
import com.proit.vista.login.LoginPage;

public class ContactenosForm  extends CaptchaForm<Void>{
	private static final long serialVersionUID = 1L;
	
	private IModel<EmailAEnviar> emailModel;
	
	private Logger logger;
	
	private RuntimeConfigurationType runtimeConfigurationType;
	
	public ContactenosForm(String id, RuntimeConfigurationType runtimeConfigurationType) {
		super(id);
		this.logger = LoggerFactory.getLogger(ContactenosForm.class);
		this.runtimeConfigurationType = runtimeConfigurationType;
		
		emailModel = new Model<EmailAEnviar>(new EmailAEnviar());
		
		IModel<String> paraModel = new PropertyModel<String>(emailModel, "para");
		TextField<String> paraTextField = new TextField<String>("para", paraModel);
		paraTextField.add(EmailAddressValidator.getInstance());
		paraTextField.setRequired(true);
		
		List<String> posiblesAsuntos = getPosiblesAsuntos();
		
		IModel<String> asuntoModel = new PropertyModel<String>(emailModel, "asunto");
		asuntoModel.setObject(posiblesAsuntos.get(0));
		DropDownChoice<String> asuntoDropDownChoice = new DropDownChoice<String>("asunto", asuntoModel, posiblesAsuntos);
		asuntoDropDownChoice.setRequired(true);
		
		IModel<String> cuerpoModel = new PropertyModel<String>(emailModel, "cuerpo");
		TextArea<String> cuerpoTextField = new TextArea<String>("cuerpo", cuerpoModel);
		cuerpoTextField.setRequired(true);

		add(paraTextField);
		add(asuntoDropDownChoice);
		add(cuerpoTextField);
	}

	private List<String> getPosiblesAsuntos() {
		List<String> posiblesAsuntos = new ArrayList<String>();
		posiblesAsuntos.add("Quiero contratar un plan");
		posiblesAsuntos.add("Quiero hacer una consulta");
		posiblesAsuntos.add("Otros");
		return posiblesAsuntos;
	}
	
	@Override
	protected void onValidate() {
		super.onValidate();
		verifyCaptcha();
	}
	
	@Override
	public void onSubmit() {
		EmailAEnviar emailAEnviar = emailModel.getObject();
		String cuerpo = "'" + emailAEnviar.getPara() + "' ha mandado el siguiente mensaje:\n" + emailAEnviar.getCuerpo();
		
		logger.info("De: " + emailAEnviar.getPara());
		logger.info("Asunto: " + emailAEnviar.getAsunto());
		logger.info("Mensaje: " + cuerpo);
		
		//Ademas de mostrar este msj, tal vez se podria mandar un mail avisandole que hemos recibido la consulta
		String textoPorPantalla = "¡Gracias por contarse con Trebol4 SRL! Estaremos contactandonos con usted a la brevedad.";
		String resultado = "OK";
		
		try {
			EmailSender.sendEmail("info@trebol4sistema.com.ar", emailAEnviar.getAsunto(), cuerpo, EmailSender.TO, runtimeConfigurationType);
		} catch (AddressException e) {
			textoPorPantalla = "El campo \"Para\" no tenía el formato correcto. Por favor, vuelva a intentarlo.";
			resultado = "ERROR";
		} catch (MessagingException e) {
			textoPorPantalla = "Se ha producido un error al intentar enviar el email. Por favor, vuelva a intentarlo.";
			resultado = "ERROR";
		}
		
		PageParameters pageParameters = new PageParameters();
		pageParameters.add("Resultado", resultado);
		pageParameters.add("TextoPantalla", textoPorPantalla);
		setResponsePage(LoginPage.class, pageParameters);
	}

}
