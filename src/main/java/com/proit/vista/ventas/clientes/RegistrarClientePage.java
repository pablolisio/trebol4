package com.proit.vista.ventas.clientes;

import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.StringValidator;

import com.proit.modelo.ventas.Cliente;
import com.proit.servicios.ventas.ClienteService;
import com.proit.utils.GeneralValidator;
import com.proit.vista.base.FacturarOnLineBasePage;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class RegistrarClientePage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(RegistrarClientePage.class.getName());
	
	private ClienteService clienteService;
		
	public RegistrarClientePage() {
		this(new PageParameters(),null);
	}
	
	public RegistrarClientePage(final PageParameters parameters, Integer clienteId) {
		super(parameters);
		clienteService = new ClienteService();
		
		setearDefaultModel(clienteId);
		
		boolean esEditar = verificarSiEsEditar(clienteId);
		
		crearForm(esEditar);
		add(new FeedbackPanel("feedback"));
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
		
		mostrarAlertaEnPantallaSiCorresponde(parameters);
	}

	private void setearDefaultModel(Integer clienteId) {
		Cliente cliente;
		if (clienteId != null) {
			cliente = (Cliente) clienteService.get(clienteId);
		} else {
			cliente = new Cliente();
			cliente.setModoSinFactura(true);
		}
		this.setDefaultModel(Model.of(cliente));
	}

	private void crearForm(final boolean esEditar) {
		IModel<String> razonSocialModel = new PropertyModel<String>(getDefaultModel(), "razonSocial");		
		final TextField<String> razonSocialTextField = new TextField<String>("razonSocial", razonSocialModel);
		razonSocialTextField.setRequired(true);
		razonSocialTextField.add(StringValidator.maximumLength(255));
		
		IModel<String> cuitCuilModel = new PropertyModel<String>(getDefaultModel(), "cuitCuil");
		final TextField<String> cuitCuilTextField = new TextField<String>("cuitCuil", cuitCuilModel);
		cuitCuilTextField.setRequired(true);
		cuitCuilTextField.add(StringValidator.maximumLength(13));
		
		IModel<String> domicilioModel = new PropertyModel<String>(getDefaultModel(), "domicilio");
		final TextField<String> domicilioTextField = new TextField<String>("domicilio", domicilioModel);
		domicilioTextField.add(StringValidator.maximumLength(255));
		
		IModel<String> telefono1Model = new PropertyModel<String>(getDefaultModel(), "telefono1");
		final TextField<String> telefono1TextField = new TextField<String>("telefono1", telefono1Model);
		
		IModel<String> telefono2Model = new PropertyModel<String>(getDefaultModel(), "telefono2");
		final TextField<String> telefono2TextField = new TextField<String>("telefono2", telefono2Model);
		
		IModel<String> contactoModel = new PropertyModel<String>(getDefaultModel(), "contacto");
		final TextField<String> contactoTextField = new TextField<String>("contacto", contactoModel);
		
		IModel<String> mailsModel = new PropertyModel<String>(getDefaultModel(), "mails");
		final TextField<String> mailsTextField = new TextField<String>("mails", mailsModel);
		
		IModel<String> referenciasModel = new PropertyModel<String>(getDefaultModel(), "referencias");
		final TextField<String> referenciasTextField = new TextField<String>("referencias", referenciasModel);
		
		IModel<String> observacionesModel = new PropertyModel<String>(getDefaultModel(), "observaciones");
		TextArea<String> observacionesTextArea = new TextArea<String>("observaciones", observacionesModel);
		
		IModel<Boolean> modelModoSinFactura = new PropertyModel<Boolean>(getDefaultModel(), "modoSinFactura");
		CheckBox checkBoxModoSinFactura = new CheckBox("modoSinFactura", modelModoSinFactura);
		IModel<Boolean> modelModoEfectivo = new PropertyModel<Boolean>(getDefaultModel(), "modoEfectivo");
		CheckBox checkBoxModoEfectivo = new CheckBox("modoEfectivo", modelModoEfectivo);
		IModel<Boolean> modelModoCheque = new PropertyModel<Boolean>(getDefaultModel(), "modoCheque");
		CheckBox checkBoxModoCheque = new CheckBox("modoCheque", modelModoCheque);
		IModel<Boolean> modelModoTransferencia = new PropertyModel<Boolean>(getDefaultModel(), "modoTransferencia");
		CheckBox checkBoxModoTransferencia = new CheckBox("modoTransferencia", modelModoTransferencia);
		//checkBoxModoSinFactura.setModelObject(true);
		checkBoxModoSinFactura.setEnabled(false);
		checkBoxModoSinFactura.setVisible(false); //Me pidio Angel que no lo quiere ver para no confundirse
		
		Link<WebPage> cancelarLink = new Link<WebPage>("cancelarLink") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage(ClientesPage.class);
			}
		};
				
		Form<?> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit() {
				submitForm(esEditar);
			}

		};
		Button guardarButton = new Button("guardar");      
        
        //When false (default is true), all validation and form updating is bypassed and the onSubmit method of that button is called directly, and the onSubmit method of the parent form is not called
        //guardarYVerCobrosAlternativosButton.setDefaultFormProcessing(false);
        
		add(form);
		form.add(razonSocialTextField);
		form.add(cuitCuilTextField);
		form.add(checkBoxModoSinFactura);
		form.add(checkBoxModoEfectivo);
		form.add(checkBoxModoCheque);
		form.add(checkBoxModoTransferencia);
		form.add(domicilioTextField);
		form.add(telefono1TextField);
		form.add(telefono2TextField);
		form.add(contactoTextField);
		form.add(mailsTextField);
		form.add(referenciasTextField);
		form.add(observacionesTextArea);
		form.add(cancelarLink);
		form.add(guardarButton);
	}
	
	private boolean validacionOK(boolean esEditar){
		boolean validacionOK = true;
		Cliente cliente = (Cliente)RegistrarClientePage.this.getDefaultModelObject();
		
		String cuitCuil = cliente.getCuitCuil();		
		String razonSocial = cliente.getRazonSocial();
		
		GeneralValidator generalValidator = new GeneralValidator();
		
		if (!esEditar && cuitCuil!=null && clienteService.existsByCuitCuil(cuitCuil)) {
			error("Ya existe un Cliente con el CUIT/CUIL \"" + cuitCuil + "\".");			
			validacionOK = false;
		}
		if (!esEditar && clienteService.existsByRazonSocial(razonSocial)) {
			error("Ya existe un Cliente con la razón social \"" + razonSocial + "\".");
			validacionOK = false;
		}
		
		if ( cuitCuil!=null &&  ! generalValidator.cuitCuilEsValido(cuitCuil) ) {
			error("CUIT/CUIL ingresado no es válido."); 
			validacionOK = false;
		}
		
		//Si no selecciona al menos un Modo de Cobro
		if ( !cliente.isModoEfectivo() && !cliente.isModoCheque() && !cliente.isModoTransferencia() ) {
			error("Debe seleccionar al menos un Modo de Cobro.");
			validacionOK = false;
		}
		
		return validacionOK;
	}
	
	private void submitForm(boolean esEditar) {
		if ( ! validacionOK(esEditar) ) {
			return;
		}
		Cliente cliente = (Cliente)RegistrarClientePage.this.getDefaultModelObject();
		String textoPorPantalla = "El cliente '" + cliente.getRazonSocial() + "' ha sido " + (esEditar ? "editado." : "creado.");
		String resultado = "OK";
		
		try {
			clienteService.createOrUpdate(cliente);
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			textoPorPantalla = "El cliente '" + cliente.getRazonSocial() + "' no pudo ser " + (esEditar ? "editado " : "creado ") + "correctamente. Por favor, vuelva a intentarlo.";
			resultado = "ERROR";
		}
		
		PageParameters pageParameters = new PageParameters();
		pageParameters.add("Resultado", resultado);
		pageParameters.add("TextoPantalla", textoPorPantalla);
		setResponsePage(ClientesPage.class, pageParameters);
	}


}
