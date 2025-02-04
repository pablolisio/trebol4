package com.proit.vista.compras.solicitudes.conversion;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
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

import com.googlecode.wicket.jquery.ui.form.autocomplete.AutoCompleteTextField;
import com.googlecode.wicket.jquery.ui.form.datepicker.AjaxDatePicker;
import com.proit.modelo.ModoPago;
import com.proit.modelo.Usuario;
import com.proit.modelo.compras.EstadoSolicitudPago;
import com.proit.modelo.compras.OrdenPago;
import com.proit.modelo.compras.Pago;
import com.proit.modelo.compras.PlanCuenta;
import com.proit.modelo.compras.SolicitudPago;
import com.proit.servicios.EventoService;
import com.proit.servicios.compras.OrdenPagoService;
import com.proit.servicios.compras.PlanCuentaService;
import com.proit.servicios.compras.SolicitudPagoService;
import com.proit.utils.Constantes;
import com.proit.utils.ExcelGeneratorOP;
import com.proit.utils.GeneralValidator;
import com.proit.utils.Utils;
import com.proit.vista.compras.solicitudes.SolicitudesPagoPage;
import com.proit.wicket.components.CustomTextFieldDouble;
import com.proit.wicket.components.EventoSearchAutoCompleteTextField;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class ConvertirAOPPage3SPySF extends ConvertirAOPPage3{
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(ConvertirAOPPage3SPySF.class.getName());
	
	private AutoCompleteTextField<String> eventoSearchAutoComplete;
	
	public ConvertirAOPPage3SPySF(SolicitudPago solicitudPago){
		super(solicitudPago);
		
		setearDefaultModel();
		
		armarCuadroObservaciones(solicitudPago);
		
		crearForm();
	}

	private void armarCuadroObservaciones(SolicitudPago solicitudPago) {
		String observaciones = solicitudPago.getObservaciones();
		WebMarkupContainer observacionesContainer = new WebMarkupContainer("observacionesContainer");
		Label observacionesLbl = new Label("observaciones", "Observaciones: " + observaciones);
		observacionesLbl.setVisible(observaciones!=null && !observaciones.isEmpty());
		Label importeLbl = new Label("importeSolicitud", "$ " + Utils.round2Decimals(importeFinal, locale) );		
		observacionesContainer.add(importeLbl);
		observacionesContainer.add(observacionesLbl);
		add(observacionesContainer);
	}
	
	private void crearForm() {
		FeedbackPanel feedback = new FeedbackPanel("feedback");
		
		//Importe Final a Pagar
		Label proveedorLbl = new Label("proveedor", "<Sin Proveedor>");
		crearImporteFinalInput();
		
		//Formas de Pago
		WebMarkupContainer seccionEfectivo = new WebMarkupContainer("seccionEfectivo");
		WebMarkupContainer seccionCheques = new WebMarkupContainer("seccionCheques");
		WebMarkupContainer seccionTransferenciaSinProv = new WebMarkupContainer("seccionTransferenciaSinProv");
		
		final CustomTextFieldDouble importeEfectivoInput = crearImporteEfectivoInput();
		
		final CustomTextFieldDouble importeTransferenciaSinProvInput = crearImporteTransferenciaSinProvInput();
		
		IModel<String> cuitCuilModel = new PropertyModel<String>(this, "cuitCuil");
		final TextField<String> cuitCuilTextField = new TextField<String>("cuitCuil", cuitCuilModel);
		
		IModel<String> cbuModel = new PropertyModel<String>(this, "cuentaBancaria.cbu");
		final TextField<String> cbuTextField = new TextField<String>("cbu", cbuModel);
		
		//Detalles
		final AjaxDatePicker ajaxDatePicker = crearDatePicker();
		AjaxLink<String> hoyLink = crearHoyLink(ajaxDatePicker);
		
		eventoSearchAutoComplete = new EventoSearchAutoCompleteTextField("eventoSearchAutoComplete", Model.of(((OrdenPago)getDefaultModelObject()).getEvento().getNombreConCliente()), true);
		eventoSearchAutoComplete.setRequired(true);
		
		DropDownChoice<PlanCuenta> planCuentaDropDownChoice = crearPlanCuentaDropDownChoice();
		
		IModel<String> conceptoModel = new PropertyModel<String>(getDefaultModel(), "concepto");
		TextField<String> conceptoTextField = new TextField<String>("concepto", conceptoModel);
		
		IModel<String> observacionesModel = new PropertyModel<String>(getDefaultModel(), "observaciones");
		TextArea<String> observacionesTextArea = new TextArea<String>("observaciones", observacionesModel);
		
		DropDownChoice<Usuario> solicitanteDropDownChoice = crearSolicitanteDropDownChoice();
		
		Form<?> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onValidate() {
				super.onValidate();
				boolean validacionOK = true;
				
				OrdenPagoService ordenPagoService = new OrdenPagoService();
				OrdenPago ordenPago = (OrdenPago)ConvertirAOPPage3SPySF.this.getDefaultModelObject();
				
				if (importeFinalInput==null || importeFinalInput.getConvertedInput()==null || importeFinalInput.getConvertedInput() <= 0) {
					validacionOK = informarError("El Importe Final debe ser mayor a 0.");
				}

				List<Pago> listadoPagos = obtenerListadoPagosPreparado(ordenPago);				
				double totalPagos = ordenPagoService.calculateTotalPagos(listadoPagos);

				//Se valida que el total de pagos debe ser igual al importe total
				if ( ! Utils.round2Decimals(totalPagos, locale).equals(Utils.round2Decimals(importeFinal, locale)) ){
					String errorString = "El Total de Pagos debe ser igual al Importe Final indicado. "
							+ "Importe Total: $"+ Utils.round2Decimals(importeFinal, locale) + ". "
							+ "Total Pagos: $" + Utils.round2Decimals(totalPagos, locale) + ".";
					validacionOK = informarError(errorString);
				}

				if ( ! ordenPagoService.cantidadChequesPermitida(listadoPagos) ) {
					validacionOK = informarError("Se pueden cargar hasta " + Constantes.MAX_CANTIDAD_CHEQUES_POR_ORDEN_PERMITIDA + " cheques.");
				}
				if ( ! ordenPagoService.todoChequeTieneBanco(listadoPagos) ) {
					validacionOK = informarError("Todo cheque debe contener Banco.");
				}
				if ( ! ordenPagoService.todoChequeTieneNro(listadoPagos) ) {
					validacionOK = informarError("Todo cheque debe contener Nro.");
				}
				if ( ! ordenPagoService.todoChequeTieneFecha(listadoPagos) ) {
					validacionOK = informarError("Todo cheque debe contener Fecha.");
				}
				//El importe de los cheques no hace falta validarlos porque ya vienen validados/corregidos
				
				GeneralValidator generalValidator = new GeneralValidator();
				if (importeTransferenciaSinProv>0 && cuitCuilTextField.getConvertedInput()==null){
					validacionOK = informarError("Falta ingresar CUIT/CUIL.");
				} else if ( importeTransferenciaSinProv>0 && ! generalValidator.cuitCuilEsValido(cuitCuilTextField.getConvertedInput()) ) {
					validacionOK = informarError("CUIT/CUIL ingresado no es válido.");
				}
				if (importeTransferenciaSinProv>0 && cbuTextField.getConvertedInput()==null){
					validacionOK = informarError("Falta ingresar CBU.");
				} else if (importeTransferenciaSinProv>0 && ! generalValidator.cbuEsValido(cbuTextField.getConvertedInput())) {
					validacionOK = informarError("CBU ingresado no es válido.");
				}
								
				EventoService eventoService = new EventoService();
				String nombreEvento = Utils.getEventFromFullName(eventoSearchAutoComplete.getModelObject());
				String razonSocialCliente = Utils.getClientFromFullName(eventoSearchAutoComplete.getModelObject());
				if (nombreEvento == null || !eventoService.existsByClienteAndNombreEvento(razonSocialCliente, nombreEvento) ) {
					validacionOK = informarError("Debe ingresar un evento valido. Utilice la funcion autocompletado.");
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
				OrdenPagoService ordenPagoService = new OrdenPagoService();
				OrdenPago ordenPago = (OrdenPago)ConvertirAOPPage3SPySF.this.getDefaultModelObject();
				ordenPago.setListadoPagos(obtenerListadoPagosPreparado(ordenPago));
				
				Calendar fecha = Calendar.getInstance();
				fecha.setTime(ajaxDatePicker.getModelObject());
				ordenPago.setFecha(fecha);
				
				EventoService eventoService = new EventoService();
				String nombreEvento = Utils.getEventFromFullName(eventoSearchAutoComplete.getModelObject());
				String razonSocialCliente = Utils.getClientFromFullName(eventoSearchAutoComplete.getModelObject());
				if (nombreEvento!=null) {
					ordenPago.setEvento(eventoService.getByClienteAndNombreEvento(razonSocialCliente, nombreEvento));
				}
				
				boolean error = false;
				ExcelGeneratorOP excelGenerator = new ExcelGeneratorOP(getRuntimeConfigurationType());
				String nroOP = null;
				try {
					if (importeTransferenciaSinProv<=0) {
						cuentaBancaria = null;
					}
					nroOP = ordenPagoService.createOrUpdateOPSPySF(ordenPago, cuentaBancaria);
					
					SolicitudPagoService solicitudPagoService = new SolicitudPagoService();
					solicitudPago.setEstadoSolicitudPago(EstadoSolicitudPago.CUMPLIDA);
					solicitudPagoService.createOrUpdate(solicitudPago);
					
					error = excelGenerator.generarExcel(ordenPago, "<Sin Proveedor>", null, null, locale);
				} catch(Exception e) {
					log.error(e.getMessage(), e);
					error = true;
				}
				
			String textoPorPantalla = (error?"La orden de pago no ha podido ser registrada correctamente. Por favor, vuelva a intentarlo.":"Una nueva orden de pago ha sido registrada con N° "+nroOP+".");
				String resultado = (error?"ERROR":"OK");
				
				String fileName = excelGenerator.getFileName(ordenPago);
				PageParameters pageParameters = new PageParameters();
				pageParameters.add("Resultado", resultado);
				pageParameters.add("TextoPantalla", textoPorPantalla);
				pageParameters.add("FileName", fileName);	//Mando por parametro el FileName para que se pueda descargar
				setResponsePage(SolicitudesPagoPage.class, pageParameters);
			}

		};
		
		final WebMarkupContainer chequesContainer = new WebMarkupContainer("chequesContainer");
		chequesContainer.setOutputMarkupPlaceholderTag(true);

		seccionCheques.add(new AjaxFallbackLink<Pago>("agregarCheque") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				OrdenPago ordenPago = (OrdenPago)ConvertirAOPPage3SPySF.this.getDefaultModelObject();
				Pago pago = new Pago();
				pago.setModoPago(ModoPago.CHEQUE);
				pago.setOrdenPago(ordenPago);
				((OrdenPago)ConvertirAOPPage3SPySF.this.getDefaultModelObject()).getListadoPagos().add(pago);
				target.add(chequesContainer);
			}
		});
		
		this.add(form);
		form.add(feedback.setOutputMarkupId(true));
		
		form.add(proveedorLbl);
		form.add(importeFinalInput);
		
		seccionEfectivo.add(importeEfectivoInput);
		addChequesList(chequesContainer);
		seccionCheques.add(chequesContainer);
		seccionTransferenciaSinProv.add(importeTransferenciaSinProvInput);
		seccionTransferenciaSinProv.add(cuitCuilTextField);
		seccionTransferenciaSinProv.add(cbuTextField);
		
		form.add(seccionEfectivo);
		form.add(seccionCheques);
		form.add(seccionTransferenciaSinProv);
		
		form.add(ajaxDatePicker);
		form.add(hoyLink);
		form.add(eventoSearchAutoComplete);
		form.add(planCuentaDropDownChoice);
		form.add(conceptoTextField);
		form.add(observacionesTextArea);
		form.add(solicitanteDropDownChoice);
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