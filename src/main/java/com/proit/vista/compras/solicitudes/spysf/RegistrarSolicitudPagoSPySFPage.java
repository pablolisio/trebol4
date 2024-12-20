package com.proit.vista.compras.solicitudes.spysf;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.StringValidator;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.ui.form.autocomplete.AutoCompleteTextField;
import com.googlecode.wicket.jquery.ui.form.datepicker.AjaxDatePicker;
import com.proit.modelo.ModoPago;
import com.proit.modelo.compras.CuentaBancaria;
import com.proit.modelo.compras.EstadoSolicitudPago;
import com.proit.modelo.compras.FacturaSolicitudPago;
import com.proit.modelo.compras.Pago;
import com.proit.modelo.compras.PagoSolicitudPago;
import com.proit.modelo.compras.SolicitudPago;
import com.proit.servicios.EventoService;
import com.proit.servicios.compras.SolicitudPagoService;
import com.proit.servicios.ventas.ClienteService;
import com.proit.utils.Constantes;
import com.proit.utils.GeneralValidator;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.vista.compras.solicitudes.MisSolicitudesPagoPage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.ClienteSearchAutoCompleteTextField;
import com.proit.wicket.components.CustomTextFieldDouble;
import com.proit.wicket.components.EventoSearchAutoCompleteTextField;

@AuthorizeInstantiation({"Administrador","Desarrollador","Solicitante Pagos"})
public class RegistrarSolicitudPagoSPySFPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(RegistrarSolicitudPagoSPySFPage.class.getName());
	
	private AutoCompleteTextField<String> clienteSearchAutoComplete;
	private AutoCompleteTextField<String> eventoSearchAutoComplete;
	private double importeFinal;
	
	private double importeEfectivo;
	private double importeTransferenciaSinProv;
	private CuentaBancaria cuentaBancaria;
	private String cuitCuil;
	
	private CustomTextFieldDouble importeFinalInput;
	
	private Locale locale;

	public RegistrarSolicitudPagoSPySFPage() {		
		setearDefaultModel();
		
		locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
		
		cuentaBancaria = new CuentaBancaria();
			
		crearForm();
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
	}

	private void setearDefaultModel() {
		SolicitudPago solicitudPago = new SolicitudPago();
		solicitudPago.setListadoFacturas(new ArrayList<FacturaSolicitudPago>());
		solicitudPago.setListadoPagos(new ArrayList<PagoSolicitudPago>());
		
		//Agrego un cheque por defecto en el listado de Pagos
		PagoSolicitudPago pagoSolicitudPago = new PagoSolicitudPago();
		pagoSolicitudPago.setModoPago(ModoPago.CHEQUE);
		pagoSolicitudPago.setSolicitudPago(solicitudPago);
		solicitudPago.getListadoPagos().add(pagoSolicitudPago);
		
		this.setDefaultModel(Model.of(solicitudPago));
	}

	private void crearForm() {
		FeedbackPanel feedback = new FeedbackPanel("feedback");
		
		// *** SECCION IMPORTE A PAGAR ***
		//Importe Final
		importeFinalInput = new CustomTextFieldDouble("importeFinal", new PropertyModel<Double>(this, "importeFinal"));
		importeFinalInput.setRequired(true);
		importeFinalInput.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(importeFinalInput);
            }
        });
		
		// *** SECCION FORMAS DE PAGO ***
		final CustomTextFieldDouble importeEfectivoInput = new CustomTextFieldDouble("importeEfectivo", new PropertyModel<Double>(this, "importeEfectivo"));
		importeEfectivoInput.setRequired(true);
		importeEfectivoInput.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(importeEfectivoInput);
            }
        });
		
		final AjaxDatePicker datepickerEfectivo = new AjaxDatePicker("datepickerEfectivo", new Model<Date>(), "dd/MM/yyyy", new Options());
		
		//Listado Cheques
		final WebMarkupContainer chequesContainer = new WebMarkupContainer("chequesContainer");
		chequesContainer.setOutputMarkupPlaceholderTag(true);
		AjaxFallbackLink<Pago> agregarCheque = new AjaxFallbackLink<Pago>("agregarCheque") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				SolicitudPago solicitudPago = (SolicitudPago)RegistrarSolicitudPagoSPySFPage.this.getDefaultModelObject();
				PagoSolicitudPago pagoSolicitudPago = new PagoSolicitudPago();
				pagoSolicitudPago.setModoPago(ModoPago.CHEQUE);
				pagoSolicitudPago.setSolicitudPago(solicitudPago);
				((SolicitudPago)RegistrarSolicitudPagoSPySFPage.this.getDefaultModelObject()).getListadoPagos().add(pagoSolicitudPago);
				target.add(chequesContainer);
			}
		};		
		addChequesList(chequesContainer);
		
		//Transferencia Bancaria
		final CustomTextFieldDouble importeTransferenciaSinProvInput = new CustomTextFieldDouble("importeTransferenciaSinProv", new PropertyModel<Double>(this, "importeTransferenciaSinProv"));
		importeTransferenciaSinProvInput.setRequired(true);
		importeTransferenciaSinProvInput.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(importeTransferenciaSinProvInput);
			}
		});
		
		IModel<String> cuitCuilModel = new PropertyModel<String>(this, "cuitCuil");
		final TextField<String> cuitCuilTextField = new TextField<String>("cuitCuil", cuitCuilModel);
		cuitCuilTextField.add(StringValidator.maximumLength(13));
		
		IModel<String> cbuModel = new PropertyModel<String>(this, "cuentaBancaria.cbu");
		final TextField<String> cbuTextField = new TextField<String>("cbu", cbuModel);
		cbuTextField.add(StringValidator.maximumLength(22));
		
		final AjaxDatePicker datepickerTransferencia = new AjaxDatePicker("datepickerTransferencia", new Model<Date>(), "dd/MM/yyyy", new Options());
		
		// *** SECCION DETALLES ***
		clienteSearchAutoComplete = new ClienteSearchAutoCompleteTextField("clienteSearchAutocomplete", new Model<String>()){
			private static final long serialVersionUID = 1L;			
			@Override
			protected void onSelected(AjaxRequestTarget target){
				String razonSocial = (String) clienteSearchAutoComplete.getDefaultModelObject();
				((EventoSearchAutoCompleteTextField)eventoSearchAutoComplete).setRazonSocialCliente(razonSocial);
				clienteSearchAutoComplete.setEnabled(false);
				target.add(clienteSearchAutoComplete);
			}
			
		};
		clienteSearchAutoComplete.setRequired(true);
		
		eventoSearchAutoComplete = new EventoSearchAutoCompleteTextField("eventoSearchAutoComplete", new Model<String>(), true);
		eventoSearchAutoComplete.setRequired(true);
		
		IModel<String> conceptoModel = new PropertyModel<String>(getDefaultModel(), "concepto");
		TextField<String> conceptoTextField = new TextField<String>("concepto", conceptoModel);
		conceptoTextField.add(StringValidator.maximumLength(255));
		
		IModel<String> observacionesModel = new PropertyModel<String>(getDefaultModel(), "observaciones");
		TextArea<String> observacionesTextArea = new TextArea<String>("observaciones", observacionesModel);
		
		
//		Label nroFacturaVentaPrefixLbl = new Label("nroFacturaVentaPrefix", Constantes.NRO_FACTURA_VENTA_PREFIX);
//		
//		final IModel<String> nroFacturaVentaModel = new PropertyModel<String>(getDefaultModel(), "nroFacturaVenta");
//		TextField<String> nroFacturaVentaTextField = new TextField<String>("nroFacturaVenta", nroFacturaVentaModel);
		
				
		Form<?> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;

			private boolean validacionOK(List<PagoSolicitudPago> listaPagos) {
				super.onValidate();
				boolean validacionOK = true;
				SolicitudPagoService solicitudPagoService = new SolicitudPagoService();
				ClienteService clienteService = new ClienteService();
				EventoService eventoService = new EventoService();
				GeneralValidator generalValidator = new GeneralValidator();
				
				String razonSocialCliente = clienteSearchAutoComplete.getModelObject();
				String nombreEvento = Utils.getEventFromFullName(eventoSearchAutoComplete.getModelObject()); 
				
				if (listaPagos.isEmpty()) {
					validacionOK = informarError("Debe cargar al menos una forma de pago.");
				} else {
					if (importeEfectivo>0 && datepickerEfectivo.getDefaultModelObject()==null) {
						validacionOK = informarError("Debe indicar una fecha para el importe en efectivo.");
					}
					if ( ! solicitudPagoService.cantidadChequesPermitida(listaPagos) ) {
						validacionOK = informarError("Se pueden cargar hasta " + Constantes.MAX_CANTIDAD_CHEQUES_POR_ORDEN_PERMITIDA + " cheques.");
					}					
					if ( ! solicitudPagoService.todoChequeTieneFecha(listaPagos) ) {
						validacionOK = informarError("Todo cheque debe contener Fecha.");
					}					
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
					if (importeTransferenciaSinProv>0 && datepickerTransferencia.getDefaultModelObject()==null) {
						validacionOK = informarError("Debe indicar una fecha para el importe de la transferencia.");
					}
				}
								
				if (importeFinal <= 0) {
					validacionOK = informarError("El Importe Final debe ser mayor a 0.");
				}
				
				double sumaTotalPagos = solicitudPagoService.calcularSumaTotalPagos(listaPagos);
				//El total de pagos debe ser igual al Importe Final a pagar ingresado
				if ( ! Utils.round2Decimals(sumaTotalPagos, locale).equals(Utils.round2Decimals(importeFinal, locale)) ){
					String errorString = "El Total de Pagos debe ser igual al Importe Final a pagar ingresado. "
							+ "Importe Total: $"+ Utils.round2Decimals(importeFinal, locale) + ". "
							+ "Total Pagos: $" + Utils.round2Decimals(sumaTotalPagos, locale) + ".";
					validacionOK = informarError(errorString);
				}
				
				if ( razonSocialCliente == null || !clienteService.existsByRazonSocial(razonSocialCliente) ) {
					validacionOK = informarError("Debe ingresar un cliente valido. Utilice la funcion autocompletado.");
				}
				
				if (nombreEvento == null || !eventoService.existsByClienteAndNombreEvento(razonSocialCliente, nombreEvento) ) {
					validacionOK = informarError("Debe ingresar un evento valido. Utilice la funcion autocompletado.");
				}
				
//				String nroFacturaVenta = nroFacturaVentaModel.getObject();
//				if (nroFacturaVenta!=null && !nroFacturaVenta.isEmpty()) {
//					String nroFacturaVentaFinal = Utils.getNroFacturaVentaConPrefijo(nroFacturaVenta);
//					if (!generalValidator.nroFacturaEsValido(nroFacturaVentaFinal)){
//						validacionOK = informarError("El Nro de Factura de Venta debe contener el formato correcto (Ejemplo: 00003-00000135)");
//					}
//				}
				
				return validacionOK;
			}

			private boolean informarError(String textoError) {
				error(textoError);
				return false;
			}
			
			@Override
			protected void onSubmit() {
				SolicitudPago solicitudPago = (SolicitudPago)RegistrarSolicitudPagoSPySFPage.this.getDefaultModelObject();
				List<PagoSolicitudPago> listaPagos = getListaPagosPreparada(solicitudPago.getListadoPagos(), solicitudPago);
				
				if ( ! validacionOK(listaPagos) ) {
					return;
				}
				
				SolicitudPagoService solicitudPagoService = new SolicitudPagoService();
				ClienteService clienteService = new ClienteService();
				EventoService eventoService = new EventoService();
				String cliente = clienteSearchAutoComplete.getModelObject();
				String nombreEvento = Utils.getEventFromFullName(eventoSearchAutoComplete.getModelObject()); 
//				String nroFacturaVentaFinal = nroFacturaVentaModel.getObject();
//				if (nroFacturaVentaFinal!=null && !nroFacturaVentaFinal.isEmpty()) {
//					nroFacturaVentaFinal = Utils.getNroFacturaVentaConPrefijo(nroFacturaVentaModel.getObject());
//				} else {
//					nroFacturaVentaFinal = "<Sin indicar>";
//				}
				
				solicitudPago.setConFactura(false);
				solicitudPago.setFecha(Calendar.getInstance());
				
				if (cliente!=null) {
					solicitudPago.setCliente(clienteService.getByRazonSocial(cliente));
				}
				if (nombreEvento!=null) {
					solicitudPago.setEvento(eventoService.getByClienteAndNombreEvento(cliente, nombreEvento));
				}
				solicitudPago.setUsuarioSolicitante(getUsuarioLogueado());
								
				solicitudPago.setEstadoSolicitudPago(EstadoSolicitudPago.PENDIENTE_3);//No existen ni PENDIENTE_1 ni PENDIENTE_2

				solicitudPago.setListadoPagos(listaPagos);
				
//				solicitudPago.setNroFacturaVenta(nroFacturaVentaFinal);
								
				String textoPorPantalla;
				String resultado;
				
				try {
					String nro = solicitudPagoService.createOrUpdateSolicitudSPySF(solicitudPago, (importeTransferenciaSinProv>0 ? cuentaBancaria : null) );
					textoPorPantalla = "La Solicitud de Pago " + nro + " ha sido creada.";
					resultado = "OK";					
				} catch(Exception e) {
					log.error(e.getMessage(), e);
					textoPorPantalla = "La Solicitud de Pago " + solicitudPago.getNro() + " no pudo ser creada correctamente. Por favor, vuelva a intentarlo.";
					resultado = "ERROR";
				}
				
				PageParameters pageParameters = new PageParameters();
				pageParameters.add("Resultado", resultado);
				pageParameters.add("TextoPantalla", textoPorPantalla);
				setResponsePage(MisSolicitudesPagoPage.class, pageParameters);
				
			}
			
			/**
			 * Obtiene solo la lista de formas de pago que fueron seleccionados desde la pagina. Lo demás se descarta.
			 */
			private List<PagoSolicitudPago> getListaPagosPreparada(List<PagoSolicitudPago> listaPagos, SolicitudPago solicitudPago) {
				List<PagoSolicitudPago> listaPagosNueva = new ArrayList<PagoSolicitudPago>();
				for (PagoSolicitudPago pago : listaPagos) {
					if (!pago.isBorrado()) {
						if (pago.getImporte()!=0) {
							listaPagosNueva.add(pago);
						}
					}
				}
				if (importeEfectivo>0) {
					Calendar fecha = Calendar.getInstance();
					PagoSolicitudPago pago = new PagoSolicitudPago();
					pago.setModoPago(ModoPago.EFECTIVO);
					pago.setImporte(importeEfectivo);
					pago.setSolicitudPago(solicitudPago);
					if (datepickerEfectivo.getModelObject()!=null) {
						fecha.setTime(datepickerEfectivo.getModelObject());
					}
					pago.setFecha(fecha);
					listaPagosNueva.add(pago);
				}
				if (importeTransferenciaSinProv>0) {
					Calendar fecha = Calendar.getInstance();
					PagoSolicitudPago pago = new PagoSolicitudPago();
					pago.setModoPago(ModoPago.TRANSFERENCIA_SIN_PROV);
					pago.setImporte(importeTransferenciaSinProv);
					pago.setCuentaBancaria(cuentaBancaria);
					pago.setCuitCuil(cuitCuil);
					pago.setSolicitudPago(solicitudPago);
					if (datepickerTransferencia.getModelObject()!=null) {
						fecha.setTime(datepickerTransferencia.getModelObject());
					}
					pago.setFecha(fecha);
					listaPagosNueva.add(pago);
				}
				return listaPagosNueva;
			}

		};
		
		this.add(form);
		form.add(feedback.setOutputMarkupId(true));
		
		form.add(importeFinalInput);
		
		form.add(importeEfectivoInput);
		form.add(datepickerEfectivo);
		form.add(chequesContainer);
		form.add(agregarCheque);
		form.add(importeTransferenciaSinProvInput);
		form.add(cuitCuilTextField);
		form.add(cbuTextField);
		form.add(datepickerTransferencia);
		
		form.add(clienteSearchAutoComplete);
		form.add(eventoSearchAutoComplete);
		form.add(conceptoTextField);
		form.add(observacionesTextArea);
		
//		form.add(nroFacturaVentaPrefixLbl);
//		form.add(nroFacturaVentaTextField);
	}
	
	private void addChequesList(final WebMarkupContainer chequesContainer) {
		IDataProvider<PagoSolicitudPago> pagosDataProvider = getChequesProvider();
		
		DataView<PagoSolicitudPago> dataView = new DataView<PagoSolicitudPago>("listaCheques", pagosDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<PagoSolicitudPago> item) {				
				AjaxDatePicker ajaxDatePicker = crearChequeDatePicker(item);
				ajaxDatePicker.setOutputMarkupId(true);
				
				IModel<Double> importeModel = new PropertyModel<Double>(item.getModelObject(), "importe");
				final CustomTextFieldDouble importeChequeTextField = new CustomTextFieldDouble("importeCheque", importeModel);
				importeChequeTextField.setOutputMarkupId(true);
				importeChequeTextField.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
					private static final long serialVersionUID = 1L;
					protected void onUpdate(AjaxRequestTarget target) {
						target.add(importeChequeTextField);
		            }
		        });
				
				item.add(ajaxDatePicker);
				item.add(importeChequeTextField);
				
				//Utilizo lo siguiente para que todos los campos ingresados en la grilla de la pagina no sean eliminados al intentar agregar o quitar una fila.
				ajaxDatePicker.add(createNewAjaxFormComponentUpdatingBehavior());
				importeChequeTextField.add(createNewAjaxFormComponentUpdatingBehavior());
				
				item.add(new AjaxFallbackLink<PagoSolicitudPago>("eliminar", item.getModel()) {					
					private static final long serialVersionUID = 1L;
					
					@Override
					public void onClick(AjaxRequestTarget target) {
						PagoSolicitudPago pagoSeleccionado = (PagoSolicitudPago) getModelObject();
						pagoSeleccionado.setBorrado(true);
						target.add(chequesContainer);
					}
				});
				
				if (item.getModelObject().isBorrado()) { //para el caso de editar se debe poner invisible
					item.setVisible(false);
				}
			}

			/**
			 * Utilizado para que al eliminar o agregar una fila de la tabla los campos de las demas filas no se eliminen
			 * @return
			 */
			private AjaxFormComponentUpdatingBehavior createNewAjaxFormComponentUpdatingBehavior() {
				return new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
					private static final long serialVersionUID = 1L;
					protected void onUpdate(AjaxRequestTarget target) {
					}
				};
			}
		};
		
		chequesContainer.add(dataView);
	}
	
	private IDataProvider<PagoSolicitudPago> getChequesProvider() {
		IDataProvider<PagoSolicitudPago> pagosDataProvider = new IDataProvider<PagoSolicitudPago>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void detach() {
			}

			@Override
			public Iterator<PagoSolicitudPago> iterator(long first, long count) {
				return getListadoTodosPagos().iterator();
			}

			@Override
			public long size() {
				return getListadoTodosPagos().size();
			}

			@Override
			public IModel<PagoSolicitudPago> model(PagoSolicitudPago pagoSolicitudPago) {
				return new Model<PagoSolicitudPago>(pagoSolicitudPago);
			}
			
			private List<PagoSolicitudPago> getListadoTodosPagos() {
				List<PagoSolicitudPago> listaPagos = ((SolicitudPago)RegistrarSolicitudPagoSPySFPage.this.getDefaultModelObject()).getListadoPagos();
				List<PagoSolicitudPago> returnList = new ArrayList<PagoSolicitudPago>();
				for (PagoSolicitudPago pago : listaPagos) {
					if (pago.isCheque()) {
						returnList.add(pago);
					}
				}
				return returnList;
			}
        	
        };
		return pagosDataProvider;
	}

	private AjaxDatePicker crearChequeDatePicker(Item<PagoSolicitudPago> item) {
		IModel<Date> fechaModel = new PropertyModel<Date>(item.getModelObject(), "fechaAsDate");
		AjaxDatePicker ajaxDatePicker = new AjaxDatePicker("datepickerCheque", fechaModel, "dd/MM/yyyy", new Options());
		return ajaxDatePicker;
	}
		
}
