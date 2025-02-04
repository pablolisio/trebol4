package com.proit.vista.compras.ordenes.normal;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.StringValidator;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.ui.form.autocomplete.AutoCompleteTextField;
import com.googlecode.wicket.jquery.ui.form.datepicker.AjaxDatePicker;
import com.proit.modelo.Evento;
import com.proit.modelo.Rol;
import com.proit.modelo.Usuario;
import com.proit.modelo.compras.OrdenPago;
import com.proit.modelo.compras.PlanCuenta;
import com.proit.servicios.EventoService;
import com.proit.servicios.UsuarioService;
import com.proit.servicios.compras.PlanCuentaService;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.EventoSearchAutoCompleteTextField;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class RegistrarOrdenPagoNormalPage3 extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	private AutoCompleteTextField<String> eventoSearchAutoComplete;
	
	private Boolean sinEvento;
	
	public RegistrarOrdenPagoNormalPage3() {
		setearDefaultModel();
		
		crearForm();
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
	}
	
	private void setearDefaultModel() {
		OrdenPago ordenPago = (OrdenPago) ((FacturarOnLineAuthenticatedWebSession) getSession()).getAttribute("op");
		//Seteo fecha actual por defecto
		if (ordenPago.getFecha()==null){
			ordenPago.setFecha(Utils.firstMillisecondOfDay(Calendar.getInstance()));
		}
		this.setDefaultModel(Model.of(ordenPago));
	}
	
	private void crearForm() {
		FeedbackPanel feedback = new FeedbackPanel("feedback");
		
		final AjaxDatePicker ajaxDatePicker = crearDatePicker();
		AjaxLink<String> hoyLink = new AjaxLink<String>("hoy") {			
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				ajaxDatePicker.setModelObject(Utils.firstMillisecondOfDay(Calendar.getInstance()).getTime());
				target.add(ajaxDatePicker);
			}
		};
				
		eventoSearchAutoComplete = new EventoSearchAutoCompleteTextField("eventoSearchAutoComplete", new Model<String>(), true);
		eventoSearchAutoComplete.setRequired(true);
		
		IModel<Boolean> sinEventoModel = new PropertyModel<Boolean>(this, "sinEvento");
		final CheckBox sinEventoCheckbox = new CheckBox("sinEvento", sinEventoModel);
		sinEventoCheckbox.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;

			protected void onUpdate(AjaxRequestTarget target) {
				boolean sinEvento = (Boolean) sinEventoCheckbox.getDefaultModelObject();
				((EventoSearchAutoCompleteTextField)eventoSearchAutoComplete).setEnabled(!sinEvento);
				eventoSearchAutoComplete.setRequired(!sinEvento);
				target.add(eventoSearchAutoComplete);
            }
        });
		
		IModel<String> conceptoModel = new PropertyModel<String>(getDefaultModel(), "concepto");
		TextField<String> conceptoTextField = new TextField<String>("concepto", conceptoModel);
		conceptoTextField.add(StringValidator.maximumLength(255));
		
		IModel<String> observacionesModel = new PropertyModel<String>(getDefaultModel(), "observaciones");
		TextArea<String> observacionesTextArea = new TextArea<String>("observaciones", observacionesModel);
		
		DropDownChoice<Usuario> solicitanteDropDownChoice = crearSolicitanteDropDownChoice();
		
		DropDownChoice<PlanCuenta> planCuentaDropDownChoice = crearPlanCuentaDropDownChoice();
		
		Form<?> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onValidate() {
				super.onValidate();
				boolean validacionOK = true;
				
				if (sinEvento!=null && !sinEvento) {
					EventoService eventoService = new EventoService();
					String nombreEvento = Utils.getEventFromFullName(eventoSearchAutoComplete.getModelObject());
					String razonSocialCliente = Utils.getClientFromFullName(eventoSearchAutoComplete.getModelObject());
					if (nombreEvento == null || !eventoService.existsByClienteAndNombreEvento(razonSocialCliente, nombreEvento) ) {
						validacionOK = informarError("Debe ingresar un evento valido. Utilice la funcion autocompletado.");
					}
				}
				
				if ( ! validacionOK ) {
					onError();
					return;
				}				
				
			}

			private boolean informarError(String textoError) {
				error(textoError);
				return false;
			}
			
			@Override
			protected void onSubmit() {
				OrdenPago ordenPago = (OrdenPago)RegistrarOrdenPagoNormalPage3.this.getDefaultModelObject();
				
				Calendar fecha = Calendar.getInstance();
				fecha.setTime(ajaxDatePicker.getModelObject());
				ordenPago.setFecha(fecha);
				
				if (sinEvento!=null && sinEvento) {
					ordenPago.setEvento(Evento.SIN_EVENTO);
				} else {
					EventoService eventoService = new EventoService();
					String nombreEvento = Utils.getEventFromFullName(eventoSearchAutoComplete.getModelObject());
					String razonSocialCliente = Utils.getClientFromFullName(eventoSearchAutoComplete.getModelObject());
					if (nombreEvento!=null) {
						ordenPago.setEvento(eventoService.getByClienteAndNombreEvento(razonSocialCliente, nombreEvento));
					}
				}
				
				setResponsePage(RegistrarOrdenPagoNormalPage4.class, new PageParameters());
			}

		};
		
		this.add(form);
		form.add(feedback.setOutputMarkupId(true));
		form.add(ajaxDatePicker);
		form.add(hoyLink);
		form.add(eventoSearchAutoComplete);
		form.add(sinEventoCheckbox);
		form.add(conceptoTextField);
		form.add(observacionesTextArea);
		form.add(solicitanteDropDownChoice);
		form.add(planCuentaDropDownChoice);
	}

	private AjaxDatePicker crearDatePicker() {
		Options options = new Options();
		
		//IModel<Date> fechaModel = new Model<Date>();
		Calendar calendar = ((OrdenPago)getDefaultModelObject()).getFecha();
		IModel<Date> fechaModel = Model.of(calendar.getTime());
		AjaxDatePicker ajaxDatePicker = new AjaxDatePicker("datepicker", fechaModel, "dd/MM/yyyy", options) ;
		ajaxDatePicker.setRequired(true);
		return ajaxDatePicker;
	}
	
	private DropDownChoice<Usuario> crearSolicitanteDropDownChoice() {
		UsuarioService usuarioService = new UsuarioService();
		List<Usuario> solicitantes = usuarioService.getUsuariosByRol(Rol.SOLICITANTE_PAGOS);
		
		IModel<Usuario> solicitanteModel = new PropertyModel<Usuario>(getDefaultModel(), "usuarioSolicitante");
		DropDownChoice<Usuario> solicitanteDropDownChoice = new DropDownChoice<Usuario>("solicitante", solicitanteModel, solicitantes, new ChoiceRenderer<Usuario>("nombreCompleto"));
		solicitanteDropDownChoice.setRequired(true);
		
		solicitanteDropDownChoice.setNullValid(true);
		return solicitanteDropDownChoice;
	}
	
	private DropDownChoice<PlanCuenta> crearPlanCuentaDropDownChoice() {
		PlanCuentaService planCuentaService = new PlanCuentaService();
		List<PlanCuenta> planesCuenta = planCuentaService.getPlanesCuenta();
		
		IModel<PlanCuenta> planCuentaModel = new PropertyModel<PlanCuenta>(getDefaultModel(), "planCuenta");
		DropDownChoice<PlanCuenta> planCuentaDropDownChoice = new DropDownChoice<PlanCuenta>("planCuenta", planCuentaModel, planesCuenta, new ChoiceRenderer<PlanCuenta>("nombre"));
		planCuentaDropDownChoice.setRequired(true);
		
		planCuentaDropDownChoice.setNullValid(true);
		return planCuentaDropDownChoice;
	}
	
}