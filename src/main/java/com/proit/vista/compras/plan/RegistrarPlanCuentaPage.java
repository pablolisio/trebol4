package com.proit.vista.compras.plan;

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

import com.proit.modelo.compras.PlanCuenta;
import com.proit.servicios.compras.PlanCuentaService;
import com.proit.vista.compras.plan.PlanesCuentaPage;
import com.proit.vista.base.FacturarOnLineBasePage;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class RegistrarPlanCuentaPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(RegistrarPlanCuentaPage.class.getName());
	
	private PlanCuentaService planCuentaService;
	
	public RegistrarPlanCuentaPage() {
		this(null);
	}
	
	public RegistrarPlanCuentaPage(Integer planCuentaId) {
		planCuentaService = new PlanCuentaService();
		
		setearDefaultModel(planCuentaId);
		
		boolean esEditar = verificarSiEsEditar(planCuentaId);
		
		crearForm(esEditar);
		add(new FeedbackPanel("feedback"));
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
	}

	private void setearDefaultModel(Integer planCuentaId) {
		PlanCuenta planCuenta = new PlanCuenta();
		if (planCuentaId != null) {
			planCuenta = (PlanCuenta) planCuentaService.get(planCuentaId);
		}
		this.setDefaultModel(Model.of(planCuenta));
	}

	private void crearForm(final boolean esEditar) {		
		IModel<String> nombreModel = new PropertyModel<String>(getDefaultModel(), "nombre");
		final TextField<String> nombreTextField = new TextField<String>("nombre", nombreModel);
		nombreTextField.add(StringValidator.maximumLength(255));
		
		IModel<String> descripcionModel = new PropertyModel<String>(getDefaultModel(), "descripcion");
		TextField<String> descripcionTextField = new TextField<String>("descripcion", descripcionModel);
		descripcionTextField.add(StringValidator.maximumLength(255));
		
		Form<?> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onValidate() {
				super.onValidate();
				PlanCuenta planCuenta = (PlanCuenta)RegistrarPlanCuentaPage.this.getDefaultModelObject();
				
				String nombrePlanCuenta = nombreTextField.getConvertedInput();
				if (nombrePlanCuenta==null || nombrePlanCuenta.isEmpty()){
					error("Debe ingresar el nombre."); 
					onError();
					return;
				}
						
				if (planCuentaService.existsByName(nombrePlanCuenta, planCuenta.getId())) {
					error("Ya existe un Plan de Cuenta con el nombre \"" + nombrePlanCuenta + "\"."); 
					onError();
					return;
				}
			}
			@Override
			protected void onSubmit() {
				PlanCuenta planCuenta = (PlanCuenta)RegistrarPlanCuentaPage.this.getDefaultModelObject();
				
				String textoPorPantalla = "El plan de cuenta " + planCuenta.getNombre() + " ha sido " + (esEditar ? "editado." : "creado.");
				String resultado = "OK";
				
				try {
					planCuentaService.createOrUpdate(planCuenta);
				} catch(Exception e) {
					log.error(e.getMessage(), e);
					textoPorPantalla = "El plan de cuenta " + planCuenta.getNombre() + " no pudo ser " + (esEditar ? "editado " : "creado ") + "correctamente. Por favor, vuelva a intentarlo.";
					resultado = "ERROR";
				}
				
				PageParameters pageParameters = new PageParameters();
				pageParameters.add("Resultado", resultado);
				pageParameters.add("TextoPantalla", textoPorPantalla);
				setResponsePage(PlanesCuentaPage.class, pageParameters);
			}

		};
		
		add(form);
		form.add(nombreTextField);
		form.add(descripcionTextField);
	}

}
