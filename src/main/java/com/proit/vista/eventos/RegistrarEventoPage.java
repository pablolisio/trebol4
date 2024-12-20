package com.proit.vista.eventos;

import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
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
import com.proit.modelo.ventas.Cliente;
import com.proit.servicios.EventoService;
import com.proit.servicios.ventas.ClienteService;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.components.ClienteSearchAutoCompleteTextField;
import com.proit.wicket.components.CustomTextFieldDouble;

@AuthorizeInstantiation({"Desarrollador", "Solicitante Facturas Ventas"})
public class RegistrarEventoPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(RegistrarEventoPage.class.getName());
	
	private EventoService eventoService;
	
	private ClienteService clienteService;
	
	private AutoCompleteTextField<String> clienteSearchAutoComplete;
	
	public RegistrarEventoPage() {
		this(null);
	}
	
	public RegistrarEventoPage(Integer eventoId) {
		eventoService = new EventoService();
		clienteService = new ClienteService();
		
		setearDefaultModel(eventoId);
		
		boolean esEditar = verificarSiEsEditar(eventoId);
		
		crearForm(esEditar);
		add(new FeedbackPanel("feedback"));
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
	}

	private void setearDefaultModel(Integer eventoId) {
		Evento evento = new Evento();
		if (eventoId != null) {
			evento = (Evento) eventoService.get(eventoId);
		} else { //Al ser nuevo pongo valor por defecto
			evento.setCerrado(false);
		}
		this.setDefaultModel(Model.of(evento));
	}

	private void crearForm(final boolean esEditar) {	
		
		clienteSearchAutoComplete = new ClienteSearchAutoCompleteTextField("clienteSearchAutocomplete", new Model<String>());
		Evento ev = ((Evento)RegistrarEventoPage.this.getDefaultModelObject());
		if (esEditar && ev.getCliente() != null) {
			clienteSearchAutoComplete.setDefaultModelObject(((Evento)RegistrarEventoPage.this.getDefaultModelObject()).getCliente().getRazonSocial());
		}
		
		IModel<String> nombreModel = new PropertyModel<String>(getDefaultModel(), "nombre");
		final TextField<String> nombreTextField = new TextField<String>("nombre", nombreModel);
		nombreTextField.add(StringValidator.maximumLength(255));
		
		IModel<Date> fechaModel = new PropertyModel<Date>(getDefaultModel(), "fechaAsDate");
		AjaxDatePicker ajaxDatePicker = new AjaxDatePicker("datepicker", fechaModel, "dd/MM/yyyy", new Options());
		
		IModel<Double> totalEventoConIVAModel = new PropertyModel<Double>(getDefaultModel(), "totalEventoConIVA");
		final CustomTextFieldDouble totalEventoConIVATextField = new CustomTextFieldDouble("totalEventoConIVA", totalEventoConIVAModel);
		totalEventoConIVATextField.setOutputMarkupId(true);
		totalEventoConIVATextField.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(totalEventoConIVATextField);
            }
        });
		
		IModel<Double> costoTotalConIVAModel = new PropertyModel<Double>(getDefaultModel(), "costoTotalConIVA");
		final CustomTextFieldDouble costoTotalConIVATextField = new CustomTextFieldDouble("costoTotalConIVA", costoTotalConIVAModel);
		costoTotalConIVATextField.setOutputMarkupId(true);
		costoTotalConIVATextField.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(costoTotalConIVATextField);
            }
        });
		
		IModel<Double> totalEventoModel = new PropertyModel<Double>(getDefaultModel(), "totalEvento");
		final CustomTextFieldDouble totalEventoTextField = new CustomTextFieldDouble("totalEvento", totalEventoModel);
		totalEventoTextField.setOutputMarkupId(true);
		totalEventoTextField.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				double totalEvento = ((Evento)RegistrarEventoPage.this.getDefaultModelObject()).getTotalEvento();
				double totalEventoConIVA = Utils.round(totalEvento * 1.21 , 2);
				totalEventoConIVATextField.setDefaultModelObject(Utils.round(totalEventoConIVA, 2));
				target.add(totalEventoConIVATextField);				
				target.add(totalEventoTextField);
            }
        });
		
		IModel<Double> costoTotalModel = new PropertyModel<Double>(getDefaultModel(), "costoTotal");
		final CustomTextFieldDouble costoTotalTextField = new CustomTextFieldDouble("costoTotal", costoTotalModel);
		costoTotalTextField.setOutputMarkupId(true);
		costoTotalTextField.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				double costoTotal = ((Evento)RegistrarEventoPage.this.getDefaultModelObject()).getCostoTotal();
				double costoTotalConIVA = Utils.round(costoTotal * 1.21 , 2);
				costoTotalConIVATextField.setDefaultModelObject(Utils.round(costoTotalConIVA, 2));
				target.add(costoTotalConIVATextField);	
				target.add(costoTotalTextField);
            }
        });
		
		WebMarkupContainer recordatorio = new WebMarkupContainer("recordatorio");
		recordatorio.setVisible(esEditar);
		
		IModel<Boolean> modelCostoFinal = new PropertyModel<Boolean>(getDefaultModel(), "costoFinal");
		CheckBox checkBoxCostoFinal = new CheckBox("costoFinal", modelCostoFinal);		
		
		WebMarkupContainer soloAdministradoresContainer = new WebMarkupContainer("soloAdministradoresContainer");
		IModel<Boolean> modelSoloAdministradores = new PropertyModel<Boolean>(getDefaultModel(), "soloAdministradores");
		CheckBox checkBoxSoloAdministradores = new CheckBox("soloAdministradores", modelSoloAdministradores);
		soloAdministradoresContainer.add(checkBoxSoloAdministradores);
		soloAdministradoresContainer.setVisible(isUsuarioLogueadoRolAdministrador()||isUsuarioLogueadoRolDesarrollador());
		
		Label btnSubmitTextLbl = new Label("btnSubmitText", esEditar?"Guardar cambios":"Crear evento y solicitud de factura");
		
		Form<?> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;

			private boolean validacionOK() {
				super.onValidate();
				boolean validacionOK = true;
				
				String razonSocialCliente = clienteSearchAutoComplete.getModelObject();
				
				if ( razonSocialCliente == null || !clienteService.existsByRazonSocial(razonSocialCliente) ) {
					validacionOK = informarError("Debe ingresar un cliente valido. Utilice la funcion autocompletado.");
				}
				
				String nombreEvento = nombreTextField.getConvertedInput();
				if (nombreEvento==null || nombreEvento.isEmpty()){
					validacionOK = informarError("Debe ingresar el nombre del Evento.");
				} else if (nombreEvento.contains("(") || nombreEvento.contains(")")) {
					validacionOK = informarError("El nombre del evento no puede contener paréntesis.");
				}
				
				int idEventoActual = ((Evento)RegistrarEventoPage.this.getDefaultModelObject()).getId();
				if (razonSocialCliente!=null && nombreEvento!=null) {
					if (eventoService.existsByClienteAndNombreEvento(razonSocialCliente, nombreEvento, idEventoActual)) {
						validacionOK = informarError("Ya existe un Evento con el nombre \"" + nombreEvento + "\" para el cliente seleccionado.");
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
				
				Evento evento = (Evento)RegistrarEventoPage.this.getDefaultModelObject();
				
				String razonSocialCliente = clienteSearchAutoComplete.getModelObject();
				Cliente cliente = clienteService.getByRazonSocial(razonSocialCliente);
				evento.setCliente(cliente);
				
				if (!esEditar) {
					evento.setResponsable(getUsuarioLogueado());
				}
				
				String textoPorPantalla = "El evento " + evento.getNombreConCliente() + " ha sido " + (esEditar ? "editado." : "creado, y una nueva solicitud de factura venta ha sido creada.");
				String resultado = "OK";
				
				try {
					if (esEditar) {
						eventoService.createOrUpdateSoloEvento(evento);
					} else {
						eventoService.createOrUpdateEventoYSolicitudFacturaVenta(evento, cliente, getUsuarioLogueado());
					}
					
				} catch(Exception e) {
					log.error(e.getMessage(), e);
					textoPorPantalla = "El evento " + evento.getNombreConCliente() + " no pudo ser " + (esEditar ? "editado " : "creado ") + "correctamente. Por favor, vuelva a intentarlo.";
					resultado = "ERROR";
				}
				
				PageParameters pageParameters = new PageParameters();
				pageParameters.add("Resultado", resultado);
				pageParameters.add("TextoPantalla", textoPorPantalla);
				setResponsePage(EventosPage.class, pageParameters);
			}

		};
		
		add(form);
		form.add(btnSubmitTextLbl);
		form.add(clienteSearchAutoComplete);
		form.add(nombreTextField);
		form.add(ajaxDatePicker);
		form.add(totalEventoTextField);
		form.add(costoTotalTextField);
		form.add(totalEventoConIVATextField);
		form.add(costoTotalConIVATextField);
		form.add(recordatorio);
		form.add(checkBoxCostoFinal);
		form.add(soloAdministradoresContainer);
	}

}
