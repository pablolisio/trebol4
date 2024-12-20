package com.proit.vista.compras.solicitudes.cpysf;

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
import com.proit.modelo.compras.EstadoSolicitudPago;
import com.proit.modelo.compras.FacturaSolicitudPago;
import com.proit.modelo.compras.Pago;
import com.proit.modelo.compras.PagoSolicitudPago;
import com.proit.modelo.compras.Proveedor;
import com.proit.modelo.compras.SolicitudPago;
import com.proit.servicios.EventoService;
import com.proit.servicios.compras.ProveedorService;
import com.proit.servicios.compras.SolicitudPagoService;
import com.proit.servicios.ventas.ClienteService;
import com.proit.utils.Constantes;
import com.proit.utils.GeneralValidator;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.vista.compras.solicitudes.MisSolicitudesPagoPage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.ClienteSearchAutoCompleteTextField;
import com.proit.wicket.components.CuitCuilProveedorSearchAutoCompleteTextField;
import com.proit.wicket.components.CustomTextFieldDouble;
import com.proit.wicket.components.EventoSearchAutoCompleteTextField;
import com.proit.wicket.components.ProveedorSearchAutoCompleteTextField;

@AuthorizeInstantiation({"Administrador","Desarrollador","Solicitante Pagos"})
public class RegistrarSolicitudPagoCPySFPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(RegistrarSolicitudPagoCPySFPage.class.getName());

	private ProveedorService proveedorService;
	
	private AutoCompleteTextField<String> proveedorSearchAutoComplete;
	private AutoCompleteTextField<String> cuitCuilProveedorSearchAutoComplete;
	private AutoCompleteTextField<String> clienteSearchAutoComplete;
	private AutoCompleteTextField<String> eventoSearchAutoComplete;
	private double importeFinal;
	
	private double importeEfectivo;
	private double importeTarjeta;
	
	private CustomTextFieldDouble importeFinalInput;
	
	private Locale locale;

	public RegistrarSolicitudPagoCPySFPage() {		
		setearDefaultModel();
		
		proveedorService = new ProveedorService();
		
		locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
			
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
		
		//Agrego una transferencia por defecto en el listado de Pagos
		PagoSolicitudPago pagoSolicitudPago2 = new PagoSolicitudPago();
		pagoSolicitudPago2.setModoPago(ModoPago.TRANSFERENCIA);
		pagoSolicitudPago2.setSolicitudPago(solicitudPago);
		solicitudPago.getListadoPagos().add(pagoSolicitudPago2);
		
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
		
		final CustomTextFieldDouble importeTarjetaInput = new CustomTextFieldDouble("importeTarjeta", new PropertyModel<Double>(this, "importeTarjeta"));
		importeTarjetaInput.setRequired(true);
		importeTarjetaInput.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(importeTarjetaInput);
            }
        });
		
		final AjaxDatePicker datepickerTarjeta = new AjaxDatePicker("datepickerTarjeta", new Model<Date>(), "dd/MM/yyyy", new Options());
		
		//Listado Cheques
		final WebMarkupContainer chequesContainer = new WebMarkupContainer("chequesContainer");
		chequesContainer.setOutputMarkupPlaceholderTag(true);
		AjaxFallbackLink<Pago> agregarCheque = new AjaxFallbackLink<Pago>("agregarCheque") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				SolicitudPago solicitudPago = (SolicitudPago)RegistrarSolicitudPagoCPySFPage.this.getDefaultModelObject();
				PagoSolicitudPago pagoSolicitudPago = new PagoSolicitudPago();
				pagoSolicitudPago.setModoPago(ModoPago.CHEQUE);
				pagoSolicitudPago.setSolicitudPago(solicitudPago);
				((SolicitudPago)RegistrarSolicitudPagoCPySFPage.this.getDefaultModelObject()).getListadoPagos().add(pagoSolicitudPago);
				target.add(chequesContainer);
			}
		};
		
		//Listado Transferencias
		final WebMarkupContainer transferenciasContainer = new WebMarkupContainer("transferenciasContainer");
		transferenciasContainer.setOutputMarkupPlaceholderTag(true);		
		AjaxFallbackLink<Pago> agregarTransferencia = new AjaxFallbackLink<Pago>("agregarTransferencia") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				SolicitudPago solicitudPago = (SolicitudPago)RegistrarSolicitudPagoCPySFPage.this.getDefaultModelObject();
				PagoSolicitudPago pagoSolicitudPago = new PagoSolicitudPago();
				pagoSolicitudPago.setModoPago(ModoPago.TRANSFERENCIA);
				pagoSolicitudPago.setSolicitudPago(solicitudPago);
				((SolicitudPago)RegistrarSolicitudPagoCPySFPage.this.getDefaultModelObject()).getListadoPagos().add(pagoSolicitudPago);
				target.add(transferenciasContainer);
			}
		};
		
		addChequesList(chequesContainer);
		addTransferenciasList(transferenciasContainer);		
		
		// *** SECCION PROVEEDOR ***
		IModel<String> cbuModel = new PropertyModel<String>(getDefaultModel(), "cbu");
		final TextField<String> cbuTextField = new TextField<String>("cbu", cbuModel);
		cbuTextField.setOutputMarkupId(true);
		cbuTextField.add(StringValidator.maximumLength(22));
				
		proveedorSearchAutoComplete = new ProveedorSearchAutoCompleteTextField("proveedorSearchAutocomplete", new Model<String>()){
			private static final long serialVersionUID = 1L;			
			@Override
			protected void onSelected(AjaxRequestTarget target){
				String razonSocial = (String) proveedorSearchAutoComplete.getDefaultModelObject();
				cuitCuilProveedorSearchAutoComplete.setModelObject(proveedorService.getCuitCuilByRazonSocial(razonSocial));
				cbuTextField.setModelObject(proveedorService.getCBUByRazonSocial(razonSocial));
				proveedorSearchAutoComplete.setEnabled(false);
				cuitCuilProveedorSearchAutoComplete.setEnabled(false);
				cbuTextField.setEnabled(false);
				target.add(proveedorSearchAutoComplete);
				target.add(cuitCuilProveedorSearchAutoComplete);
				target.add(cbuTextField);
			}
			
		};
		proveedorSearchAutoComplete.setRequired(true);
		proveedorSearchAutoComplete.add(StringValidator.maximumLength(255));
		
		cuitCuilProveedorSearchAutoComplete = new CuitCuilProveedorSearchAutoCompleteTextField("cuitCuilProveedorSearchAutocomplete", new Model<String>()){
			private static final long serialVersionUID = 1L;			
			@Override
			protected void onSelected(AjaxRequestTarget target){
				String cuitCuil = (String) cuitCuilProveedorSearchAutoComplete.getDefaultModelObject();				
				proveedorSearchAutoComplete.setModelObject(proveedorService.getRazonSocialByCuitCuil(cuitCuil));
				cbuTextField.setModelObject(proveedorService.getCBUByCuitCuil(cuitCuil));
				proveedorSearchAutoComplete.setEnabled(false);
				cuitCuilProveedorSearchAutoComplete.setEnabled(false);
				cbuTextField.setEnabled(false);
				target.add(proveedorSearchAutoComplete);
				target.add(cuitCuilProveedorSearchAutoComplete);
				target.add(cbuTextField);
			}
			
		};
		cuitCuilProveedorSearchAutoComplete.setRequired(true);
		cuitCuilProveedorSearchAutoComplete.add(StringValidator.maximumLength(13));
		
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
				
				String razonSocialProveedor = proveedorSearchAutoComplete.getModelObject();
				String cuitCuilProveedor = cuitCuilProveedorSearchAutoComplete.getModelObject();
				String cbu = cbuTextField.getModelObject();
				String razonSocialCliente = clienteSearchAutoComplete.getModelObject();
				String nombreEvento = Utils.getEventFromFullName(eventoSearchAutoComplete.getModelObject());
				
				//Proveedor
				if ( ! proveedorService.existsByRazonSocialAndCuitCuil(razonSocialProveedor, cuitCuilProveedor) ) {					
					if (proveedorService.existsByRazonSocial(razonSocialProveedor) ) {
						validacionOK = informarError("La Razon Social ingresada ya existe en el sistema, pero el CUIT/CUIL ingresado no es el correcto.");
					}
					if (proveedorService.existsByCuitCuil(cuitCuilProveedor)) {
						validacionOK = informarError("El CUIT/CUIL ingresado ya existe en el sistema, pero la Razon Social ingresada no es la correcta.");
					}
					
					if ( cuitCuilProveedor!=null &&  ! generalValidator.cuitCuilEsValido(cuitCuilProveedor) ) {
						validacionOK = informarError("El CUIT/CUIL ingresado no es válido.");
					}
					
					if ( cbu!=null && ! cbu.isEmpty() && ! generalValidator.cbuEsValido(cbu) ) {
						validacionOK = informarError("El CBU ingresado no es válido.");
					}					
				}

				if (listaPagos.isEmpty()) {
					validacionOK = informarError("Debe cargar al menos una forma de pago.");
				} else {
					if (importeEfectivo>0 && datepickerEfectivo.getDefaultModelObject()==null) {
						validacionOK = informarError("Debe indicar una fecha para el importe en efectivo.");
					}
					if (importeTarjeta>0 && datepickerTarjeta.getDefaultModelObject()==null) {
						validacionOK = informarError("Debe indicar una fecha para el importe de la tarjeta.");
					}
					if ( ! solicitudPagoService.cantidadChequesPermitida(listaPagos) ) {
						validacionOK = informarError("Se pueden cargar hasta " + Constantes.MAX_CANTIDAD_CHEQUES_POR_ORDEN_PERMITIDA + " cheques.");
					}					
					if ( ! solicitudPagoService.todoChequeTieneFecha(listaPagos) ) {
						validacionOK = informarError("Todo cheque debe contener Fecha.");
					}					
					if ( ! solicitudPagoService.cantidadTransferenciasPermitida(listaPagos) ) {
						validacionOK = informarError("Se pueden cargar hasta " + Constantes.MAX_CANTIDAD_TRANSFERENCIAS_POR_ORDEN_PERMITIDA + " transferencias.");
					}					
					if (solicitudPagoService.calcularSumaImportesTransferencia(listaPagos)>0 && (cbu==null || cbu.isEmpty()) ) {
						validacionOK = informarError("Para poder realizar transferencias se necesita el CBU del proveedor.");
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
				SolicitudPago solicitudPago = (SolicitudPago)RegistrarSolicitudPagoCPySFPage.this.getDefaultModelObject();
				List<PagoSolicitudPago> listaPagos = getListaPagosPreparada(solicitudPago.getListadoPagos(), solicitudPago);
				
				if ( ! validacionOK(listaPagos) ) {
					return;
				}
				
				SolicitudPagoService solicitudPagoService = new SolicitudPagoService();
				ClienteService clienteService = new ClienteService();
				EventoService eventoService = new EventoService();
				String razonSocialProveedor = proveedorSearchAutoComplete.getModelObject();
				String cuitCuilProveedor = cuitCuilProveedorSearchAutoComplete.getModelObject();
				String cbu = cbuTextField.getModelObject();
				Proveedor proveedor = proveedorService.getByRazonSocialAndCuitCuil(razonSocialProveedor, cuitCuilProveedor);
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
				if (proveedor!=null) {
					solicitudPago.setProveedor(proveedor);
					solicitudPago.setCbu(null);//lo borro porque ya venia cargado de antes
				} else {
					solicitudPago.setRazonSocial(razonSocialProveedor);
					solicitudPago.setCuitCuil(cuitCuilProveedor);
					solicitudPago.setCbu(cbu);
				}
				if (cliente!=null) {
					solicitudPago.setCliente(clienteService.getByRazonSocial(cliente));
				}
				if (nombreEvento!=null) {
					solicitudPago.setEvento(eventoService.getByClienteAndNombreEvento(cliente, nombreEvento));
				}
				solicitudPago.setUsuarioSolicitante(getUsuarioLogueado());				
				
				EstadoSolicitudPago estadoSolicitudPago;
				if (proveedor==null) {
					estadoSolicitudPago = EstadoSolicitudPago.PENDIENTE_1;
				} else {													//No hay PENDIENTE_2 para cpysf
					estadoSolicitudPago = EstadoSolicitudPago.PENDIENTE_3;
				}				
				solicitudPago.setEstadoSolicitudPago(estadoSolicitudPago);

				solicitudPago.setListadoPagos(listaPagos);
				
//				solicitudPago.setNroFacturaVenta(nroFacturaVentaFinal);
								
				String textoPorPantalla;
				String resultado;
				
				try {
					String nro = solicitudPagoService.getNextNroSolicitud();
					solicitudPago.setNro(nro); //es el ultimo paso
					solicitudPagoService.createOrUpdate(solicitudPago);
					textoPorPantalla = "La Solicitud de Pago " + solicitudPago.getNro() + " ha sido creada.";
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
				if (importeTarjeta>0) {
					Calendar fecha = Calendar.getInstance();
					PagoSolicitudPago pago = new PagoSolicitudPago();
					pago.setModoPago(ModoPago.TARJETA_CRED);
					pago.setImporte(importeTarjeta);
					pago.setSolicitudPago(solicitudPago);
					if (datepickerTarjeta.getModelObject()!=null) {
						fecha.setTime(datepickerTarjeta.getModelObject());
					}
					pago.setFecha(fecha);
					listaPagosNueva.add(pago);
				}
				return listaPagosNueva;
			}

		};
		
		this.add(form);
		form.add(feedback.setOutputMarkupId(true));
		
		form.add(proveedorSearchAutoComplete);
		form.add(cuitCuilProveedorSearchAutoComplete);
		form.add(cbuTextField);
		
		form.add(importeFinalInput);
		
		form.add(importeEfectivoInput);
		form.add(datepickerEfectivo);
		form.add(importeTarjetaInput);
		form.add(datepickerTarjeta);
		form.add(chequesContainer);
		form.add(agregarCheque);
		form.add(transferenciasContainer);
		form.add(agregarTransferencia);
		
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
				AjaxDatePicker ajaxDatePicker = crearDatePicker(item, "fechaAsDate", "datepickerCheque");
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
	
	private void addTransferenciasList(final WebMarkupContainer transferenciasContainer) {
		IDataProvider<PagoSolicitudPago> pagosDataProvider = getTransferenciasProvider();
		
		DataView<PagoSolicitudPago> dataView = new DataView<PagoSolicitudPago>("listaTransferencias", pagosDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<PagoSolicitudPago> item) {
				AjaxDatePicker ajaxDatePicker = crearDatePicker(item, "fechaAsDate", "datepickerTransferencia");
				ajaxDatePicker.setOutputMarkupId(true);
				
				IModel<Double> importeModel = new PropertyModel<Double>(item.getModelObject(), "importe");
				final CustomTextFieldDouble importeTransferenciaTextField = new CustomTextFieldDouble("importeTransferencia", importeModel);
				importeTransferenciaTextField.setOutputMarkupId(true);
				importeTransferenciaTextField.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
					private static final long serialVersionUID = 1L;
					protected void onUpdate(AjaxRequestTarget target) {
						target.add(importeTransferenciaTextField);
		            }
		        });
				
				item.add(ajaxDatePicker);
				item.add(importeTransferenciaTextField);
				
				//Utilizo lo siguiente para que todos los campos ingresados en la grilla de la pagina no sean eliminados al intentar agregar o quitar una fila.
				ajaxDatePicker.add(createNewAjaxFormComponentUpdatingBehavior());
				importeTransferenciaTextField.add(createNewAjaxFormComponentUpdatingBehavior());
				
				item.add(new AjaxFallbackLink<PagoSolicitudPago>("eliminar", item.getModel()) {					
					private static final long serialVersionUID = 1L;
					
					@Override
					public void onClick(AjaxRequestTarget target) {
						PagoSolicitudPago pagoSeleccionado = (PagoSolicitudPago) getModelObject();
						pagoSeleccionado.setBorrado(true);
						target.add(transferenciasContainer);
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
		
		transferenciasContainer.add(dataView);
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
				List<PagoSolicitudPago> listaPagos = ((SolicitudPago)RegistrarSolicitudPagoCPySFPage.this.getDefaultModelObject()).getListadoPagos();
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
	
	private IDataProvider<PagoSolicitudPago> getTransferenciasProvider() {
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
				List<PagoSolicitudPago> listaPagos = ((SolicitudPago)RegistrarSolicitudPagoCPySFPage.this.getDefaultModelObject()).getListadoPagos();
				List<PagoSolicitudPago> returnList = new ArrayList<PagoSolicitudPago>();
				for (PagoSolicitudPago pago : listaPagos) {
					if (pago.isTransferencia()) {
						returnList.add(pago);
					}
				}
				return returnList;
			}
        	
        };
		return pagosDataProvider;
	}
	
	private AjaxDatePicker crearDatePicker(Item<PagoSolicitudPago> item, String nombreAtributo, String wicketID) {
		IModel<Date> fechaModel = new PropertyModel<Date>(item.getModelObject(), nombreAtributo);
		AjaxDatePicker ajaxDatePicker = new AjaxDatePicker(wicketID, fechaModel, "dd/MM/yyyy", new Options());
		return ajaxDatePicker;
	}
	
}
