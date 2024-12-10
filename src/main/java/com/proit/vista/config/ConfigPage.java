package com.proit.vista.config;

import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.proit.modelo.Config;
import com.proit.servicios.ConfigService;
import com.proit.vista.base.FacturarOnLineBasePage;

@AuthorizeInstantiation("Desarrollador")
public class ConfigPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(ConfigPage.class.getName());
	
	private ConfigService configService;
	
	public ConfigPage(final PageParameters parameters) {
		super(parameters);
		configService = new ConfigService();
		
		this.setDefaultModel(Model.of(configService.getConfig()));
		
		crearForm();
		add(new FeedbackPanel("feedback"));
		
		mostrarAlertaEnPantallaSiCorresponde(parameters);
		
		facturarOnLineMenu.setearMenuActivo(false, false, true, false);
	}

	private void crearForm() {		
		IModel<Boolean> onlyDesarrolladorCanUseAppModel = new PropertyModel<Boolean>(getDefaultModel(), "onlyDesarrolladorCanUseApp");
		CheckBox checkBoxOnlyDesarrolladorCanUseApp = new CheckBox("onlyDesarrolladorCanUseApp", onlyDesarrolladorCanUseAppModel);
		
		Form<?> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onValidate() {
				super.onValidate();
			}
			@Override
			protected void onSubmit() {
				Config config = (Config)ConfigPage.this.getDefaultModelObject();
				
				String textoPorPantalla = "La configuración ha sido editada.";
				String resultado = "OK";
				
				try {
					configService.createOrUpdate(config);
				} catch(Exception e) {
					log.error(e.getMessage(), e);
					textoPorPantalla = "La configuración no pudo ser editada correctamente. Por favor, vuelva a intentarlo.";
					resultado = "ERROR";
				}
				
				PageParameters pageParameters = new PageParameters();
				pageParameters.add("Resultado", resultado);
				pageParameters.add("TextoPantalla", textoPorPantalla);
				setResponsePage(ConfigPage.class, pageParameters);
			}

		};
		
		add(form);
		form.add(checkBoxOnlyDesarrolladorCanUseApp);
	}

}
