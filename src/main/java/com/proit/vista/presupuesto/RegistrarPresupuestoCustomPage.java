package com.proit.vista.presupuesto;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.ui.form.datepicker.AjaxDatePicker;
import com.proit.modelo.Banco;
import com.proit.modelo.PresupuestoCustom;
import com.proit.servicios.BancoService;
import com.proit.servicios.PresupuestoCustomService;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.components.CustomTextFieldDouble;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class RegistrarPresupuestoCustomPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(RegistrarPresupuestoCustomPage.class.getName());
	
	private PresupuestoCustomService presupuestoCustomService;
	
	public RegistrarPresupuestoCustomPage() {
		this(null);
	}
	
	public RegistrarPresupuestoCustomPage(Integer presupuestoCustomId) {
		presupuestoCustomService = new PresupuestoCustomService();
		
		setearDefaultModel(presupuestoCustomId);
		
		boolean esEditar = verificarSiEsEditar(presupuestoCustomId);
		
		crearForm(esEditar);
		add(new FeedbackPanel("feedback"));
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
	}

	private void setearDefaultModel(Integer presupuestoCustomId) {
		PresupuestoCustom presupuestoCustom = new PresupuestoCustom();
		if (presupuestoCustomId != null) {
			presupuestoCustom = (PresupuestoCustom) presupuestoCustomService.get(presupuestoCustomId);
		} else { //Al ser nuevo pongo valor por defecto
			presupuestoCustom.setDebitado(false);
		}
		this.setDefaultModel(Model.of(presupuestoCustom));
	}

	private void crearForm(final boolean esEditar) {		
		IModel<String> detalleModel = new PropertyModel<String>(getDefaultModel(), "detalle");
		final TextField<String> detalleTextField = new TextField<String>("detalle", detalleModel);
		detalleTextField.setRequired(true);
		
		DropDownChoice<Banco> bancoDropDownChoice = crearBancoDropDownChoice(esEditar);
		bancoDropDownChoice.setRequired(true);
		
		IModel<Date> fechaModel = new PropertyModel<Date>(getDefaultModel(), "fechaAsDate");
		AjaxDatePicker ajaxDatePicker = new AjaxDatePicker("datepicker", fechaModel, "dd/MM/yyyy", new Options());
		ajaxDatePicker.setRequired(true);
		
		IModel<Double> importeModel = new PropertyModel<Double>(getDefaultModel(), "importe");
		final CustomTextFieldDouble importeTextField = new CustomTextFieldDouble("importe", importeModel);
		importeTextField.setOutputMarkupId(true);
		importeTextField.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(importeTextField);
            }
        });
		importeTextField.setRequired(true);
				
		Form<?> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;

			private boolean validacionOK() {
				super.onValidate();
				boolean validacionOK = true;
				
				PresupuestoCustom presupuestoCustom = (PresupuestoCustom)RegistrarPresupuestoCustomPage.this.getDefaultModelObject();
				String detalle = presupuestoCustom.getDetalle();
				if (detalle!=null) {
					if (presupuestoCustomService.existsByDetalle(detalle, presupuestoCustom.getId())) {
						validacionOK = informarError("Ya existe un Presupuesto con el detalle '" + detalle + "'.");
					}
				}
				
				return validacionOK;
			}
			
			private boolean informarError(String textoError) {
				error(textoError);
				return false;
			}
			
			@Override
			protected void onSubmit() {
				
				if ( ! validacionOK() ) {
					return;
				}
				
				PresupuestoCustom presupuestoCustom = (PresupuestoCustom)RegistrarPresupuestoCustomPage.this.getDefaultModelObject();
				
				
				String textoPorPantalla = "El presupuesto " + presupuestoCustom.getDetalle() + " ha sido " + (esEditar ? "editado." : "creado.");
				String resultado = "OK";
				
				try {
					
					presupuestoCustomService.createOrUpdate(presupuestoCustom);
					
				} catch(Exception e) {
					log.error(e.getMessage(), e);
					textoPorPantalla = "El presupuesto " + presupuestoCustom.getDetalle() + " no pudo ser " + (esEditar ? "editado " : "creado ") + "correctamente. Por favor, vuelva a intentarlo.";
					resultado = "ERROR";
				}
				
				PageParameters pageParameters = new PageParameters();
				pageParameters.add("Resultado", resultado);
				pageParameters.add("TextoPantalla", textoPorPantalla);
				setResponsePage(PresupuestosBancoPage.class, pageParameters);
			}

		};
		
		add(form);
		form.add(detalleTextField);
		form.add(ajaxDatePicker);
		form.add(bancoDropDownChoice);
		form.add(importeTextField);
	}
	
	private DropDownChoice<Banco> crearBancoDropDownChoice(boolean esEditar) {
		BancoService bancoService = new BancoService();
		List<Banco> bancos = bancoService.getBancos();
		
		Banco bancoSeleccionado = null;
		if ( esEditar ) {
			bancoSeleccionado = ((PresupuestoCustom)RegistrarPresupuestoCustomPage.this.getDefaultModelObject()).getBanco();
		} else {
			bancoSeleccionado = bancoService.getBancoActual(bancos);
		}
		
		IModel<Banco> bancoModel = new PropertyModel<Banco>(getDefaultModel(), "banco");
		bancoModel.setObject(bancoSeleccionado);
		DropDownChoice<Banco> bancoDropDownChoice = new DropDownChoice<Banco>("banco", bancoModel, bancos, new ChoiceRenderer<Banco>("nombre"));
		
		bancoDropDownChoice.setNullValid(true);
		return bancoDropDownChoice;
	}

}
