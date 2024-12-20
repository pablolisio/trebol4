package com.proit.vista.bancos;

import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.StringValidator;

import com.proit.modelo.Banco;
import com.proit.servicios.BancoService;
import com.proit.vista.base.FacturarOnLineBasePage;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class RegistrarBancoPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(RegistrarBancoPage.class.getName());
	
	private BancoService bancoService;
	
	public RegistrarBancoPage() {
		this(null);
	}
	
	public RegistrarBancoPage(Integer bancoId) {
		bancoService = new BancoService();
		
		setearDefaultModel(bancoId);
		
		boolean esEditar = verificarSiEsEditar(bancoId);
		
		crearForm(esEditar);
		add(new FeedbackPanel("feedback"));
		
		facturarOnLineMenu.setearMenuActivo(false, false, true, false);
	}

	private void setearDefaultModel(Integer bancoId) {
		Banco banco = new Banco();
		if (bancoId != null) {
			banco = (Banco) bancoService.get(bancoId);
		}
		this.setDefaultModel(Model.of(banco));
	}

	private void crearForm(final boolean esEditar) {		
		IModel<String> nombreModel = new PropertyModel<String>(getDefaultModel(), "nombre");
		final TextField<String> nombreTextField = new TextField<String>("nombre", nombreModel);
		nombreTextField.add(StringValidator.maximumLength(255));
		
		Form<?> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onValidate() {
				super.onValidate();
				Banco banco = (Banco)RegistrarBancoPage.this.getDefaultModelObject();

				String nombreBanco = nombreTextField.getConvertedInput();
				if (nombreBanco==null || nombreBanco.isEmpty()){
					error("Debe ingresar el nombre del Banco."); 
					onError();
					return;
				}
						
				if (bancoService.existsByName(nombreBanco, banco.getId())) {
					error("Ya existe un Banco con el nombre \"" + nombreBanco + "\"."); 
					onError();
					return;
				}
			}
			@Override
			protected void onSubmit() {
				Banco banco = (Banco)RegistrarBancoPage.this.getDefaultModelObject();
				
				String textoPorPantalla = "El banco " + banco.getNombre() + " ha sido " + (esEditar ? "editado." : "creado.");
				String resultado = "OK";
				
				try {
					bancoService.createOrUpdate(banco);
				} catch(Exception e) {
					log.error(e.getMessage(), e);
					textoPorPantalla = "El banco " + banco.getNombre() + " no pudo ser " + (esEditar ? "editado " : "creado ") + "correctamente. Por favor, vuelva a intentarlo.";
					resultado = "ERROR";
				}
				
				PageParameters pageParameters = new PageParameters();
				pageParameters.add("Resultado", resultado);
				pageParameters.add("TextoPantalla", textoPorPantalla);
				setResponsePage(BancosPage.class, pageParameters);
			}

		};
		
		add(form);
		form.add(nombreTextField);
	}

}
