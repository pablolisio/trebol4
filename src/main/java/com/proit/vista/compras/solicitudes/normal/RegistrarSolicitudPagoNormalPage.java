package com.proit.vista.compras.solicitudes.normal;

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
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
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
import com.proit.modelo.TipoFactura;
import com.proit.modelo.compras.EstadoSolicitudPago;
import com.proit.modelo.compras.FacturaCompra;
import com.proit.modelo.compras.FacturaSolicitudPago;
import com.proit.modelo.compras.Pago;
import com.proit.modelo.compras.PagoSolicitudPago;
import com.proit.modelo.compras.Proveedor;
import com.proit.modelo.compras.SolicitudPago;
import com.proit.servicios.EventoService;
import com.proit.servicios.TipoFacturaService;
import com.proit.servicios.compras.FacturaCompraService;
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
import com.proit.wicket.components.TipoYNroFacturaCompraSearchAutoCompleteTextField;
import com.proit.wicket.components.ProveedorSearchAutoCompleteTextField;

@AuthorizeInstantiation({"Administrador","Desarrollador","Solicitante Pagos"})
public class RegistrarSolicitudPagoNormalPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(RegistrarSolicitudPagoNormalPage.class.getName());

	private ProveedorService proveedorService;
	
	private AutoCompleteTextField<String> proveedorSearchAutoComplete;
	private AutoCompleteTextField<String> cuitCuilProveedorSearchAutoComplete;
	private AutoCompleteTextField<String> clienteSearchAutoComplete;
	private AutoCompleteTextField<String> eventoSearchAutoComplete;
	private double importeFinal;
	private double importeParcial;
	
	private double importeEfectivo;
	private double importeTarjeta;
	
	private CustomTextFieldDouble importeFinalInput;
	private CustomTextFieldDouble importeParcialInput;
	
	private Locale locale;

	public RegistrarSolicitudPagoNormalPage() {		
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
		
		//Agrego una facturaSolicitudPago por defecto en el listado de Facturas Solicitud Pago
		FacturaSolicitudPago facturaSolicitudPago = new FacturaSolicitudPago();
		facturaSolicitudPago.setSolicitudPago(solicitudPago);		
		solicitudPago.getListadoFacturas().add(facturaSolicitudPago);
		
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
		
		// *** SECCION FACTURAS ***
		//Importes Final y Parcial
		importeFinalInput = new CustomTextFieldDouble("importeFinal", new PropertyModel<Double>(this, "importeFinal"));
		importeFinalInput.setOutputMarkupId(true);
		importeFinalInput.setEnabled(false);
		
		importeParcialInput = new CustomTextFieldDouble("importeParcial", new PropertyModel<Double>(this, "importeParcial"));
		importeParcialInput.setOutputMarkupId(true);
		importeParcialInput.setEnabled(false);
		importeParcialInput.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(importeParcialInput);
            }
        });
		
		//Listado Facturas
		final WebMarkupContainer facturasSolicitudPagoContainer = new WebMarkupContainer("facturasSolicitudPagoContainer");
		facturasSolicitudPagoContainer.setOutputMarkupPlaceholderTag(true);
		AjaxFallbackLink<Pago> agregarFacturaSolicitudPago = new AjaxFallbackLink<Pago>("agregarFacturaSolicitudPago") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				SolicitudPago solicitudPago = (SolicitudPago)RegistrarSolicitudPagoNormalPage.this.getDefaultModelObject();
				FacturaSolicitudPago facturaSolicitudPago = new FacturaSolicitudPago();
				facturaSolicitudPago.setSolicitudPago(solicitudPago);
				((SolicitudPago)RegistrarSolicitudPagoNormalPage.this.getDefaultModelObject()).getListadoFacturas().add(facturaSolicitudPago);
				target.add(facturasSolicitudPagoContainer);
				actualizarImportesFinalyParcial(target);
			}
		};		
		addFacturasSolicitudPagoList(facturasSolicitudPagoContainer);
		
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
				SolicitudPago solicitudPago = (SolicitudPago)RegistrarSolicitudPagoNormalPage.this.getDefaultModelObject();
				PagoSolicitudPago pagoSolicitudPago = new PagoSolicitudPago();
				pagoSolicitudPago.setModoPago(ModoPago.CHEQUE);
				pagoSolicitudPago.setSolicitudPago(solicitudPago);
				((SolicitudPago)RegistrarSolicitudPagoNormalPage.this.getDefaultModelObject()).getListadoPagos().add(pagoSolicitudPago);
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
				SolicitudPago solicitudPago = (SolicitudPago)RegistrarSolicitudPagoNormalPage.this.getDefaultModelObject();
				PagoSolicitudPago pagoSolicitudPago = new PagoSolicitudPago();
				pagoSolicitudPago.setModoPago(ModoPago.TRANSFERENCIA);
				pagoSolicitudPago.setSolicitudPago(solicitudPago);
				((SolicitudPago)RegistrarSolicitudPagoNormalPage.this.getDefaultModelObject()).getListadoPagos().add(pagoSolicitudPago);
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
				
				limpiarYAdaptarListadoFacturas();				
				target.add(facturasSolicitudPagoContainer);
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
				
				limpiarYAdaptarListadoFacturas();				
				target.add(facturasSolicitudPagoContainer);
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

			private boolean validacionOK(List<FacturaSolicitudPago> listaFacturas, List<PagoSolicitudPago> listaPagos) {
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
				
				if (listaFacturas.isEmpty()) {
					validacionOK = informarError("Debe cargar al menos una factura.");
				} else {
					if (listaFacturas.size()>Constantes.MAX_CANTIDAD_FACTURAS_POR_ORDEN_PERMITIDA) {
						validacionOK = informarError("Debe cargar como máximo " + Constantes.MAX_CANTIDAD_FACTURAS_POR_ORDEN_PERMITIDA + " facturas.");
					}
					if ( ! solicitudPagoService.todaFacturaTieneFecha(listaFacturas) ) {
						validacionOK = informarError("Toda factura debe contener Fecha.");
					}
					if ( ! solicitudPagoService.todaFacturaTieneTipo(listaFacturas) ) {
						validacionOK = informarError("Toda factura debe contener Tipo.");
					}
					if ( ! solicitudPagoService.todaFacturaTieneNro(listaFacturas) ) {
						validacionOK = informarError("Toda factura debe contener Nro.");
					} else {
						if (! solicitudPagoService.todaFacturaTieneNroValido(listaFacturas)) {
							validacionOK = informarError("Los Nro. de Factura debe contener el formato correcto (Ejemplo: 00002-00000135)");
						}
					}
					if ( ! solicitudPagoService.todaFacturaYaEnSistemaEsPendiente(listaFacturas, razonSocialProveedor) ) {
						validacionOK = informarError("Se seleccionó una factura que ya está en el sistema como Cancelada.");
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
				
				if (importeParcial < 0) {
					validacionOK = informarError("El Importe Parcial debe ser mayor o igual a 0.");
				}
				
				if (Utils.round(importeParcial,2) >= Utils.round(importeFinal,2)) {
					validacionOK = informarError("El Importe Parcial debe ser menor al Importe Final.");
				}
				
				double sumaTotalPagos = solicitudPagoService.calcularSumaTotalPagos(listaPagos);
				//Si se eligio importe parcial, se valida que el total de pagos sea dicho importe parcial, sino debe ser igual al importe total
				if ( importeParcial>0 && ! Utils.round2Decimals(sumaTotalPagos, locale).equals(Utils.round2Decimals(importeParcial, locale)) ) {
					String errorString = "El Total de Pagos debe ser igual al Importe Parcial indicado. "
							+ "Importe Parcial: $"+ Utils.round2Decimals(importeParcial, locale) + ". "
							+ "Total Pagos: $" + Utils.round2Decimals(sumaTotalPagos, locale) + ".";
					validacionOK = informarError(errorString);
				} else if ( importeParcial==0 && ! Utils.round2Decimals(sumaTotalPagos, locale).equals(Utils.round2Decimals(importeFinal, locale)) ){
					String errorString = "El Total de Pagos debe ser igual al Importe Total de las facturas. "
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
				SolicitudPago solicitudPago = (SolicitudPago)RegistrarSolicitudPagoNormalPage.this.getDefaultModelObject();
				List<FacturaSolicitudPago> listaFacturas = getListaFacturasPreparada(solicitudPago.getListadoFacturas());
				List<PagoSolicitudPago> listaPagos = getListaPagosPreparada(solicitudPago.getListadoPagos(), solicitudPago);
				
				if ( ! validacionOK(listaFacturas, listaPagos) ) {
					return;
				}
				
				SolicitudPagoService solicitudPagoService = new SolicitudPagoService();
				FacturaCompraService facturaCompraService = new FacturaCompraService();
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
				
				solicitudPago.setConFactura(true);
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
				} else if (!solicitudPagoService.todaFacturaYaExisteCargadaEnSistema(solicitudPago.getListadoFacturas(), razonSocialProveedor)) {
					estadoSolicitudPago = EstadoSolicitudPago.PENDIENTE_2;
				} else {
					estadoSolicitudPago = EstadoSolicitudPago.PENDIENTE_3;
				}				
				solicitudPago.setEstadoSolicitudPago(estadoSolicitudPago);
				
				for (FacturaSolicitudPago facturaSolicitudPago : listaFacturas) {
					if (facturaCompraService.nroFacturaAlreadyExists(razonSocialProveedor, facturaSolicitudPago.getTipoFacturaCompra(), facturaSolicitudPago.getNroFacturaCompra())) {
						FacturaCompra facturaCompra = facturaCompraService.getFacturaByRazonSocialAndTipoAndNroFactura(razonSocialProveedor, facturaSolicitudPago.getTipoFacturaCompra(), facturaSolicitudPago.getNroFacturaCompra());
						facturaSolicitudPago.setFacturaCompra(facturaCompra);
						facturaSolicitudPago.setTipoFacturaCompra(null);
						facturaSolicitudPago.setNroFacturaCompra(null);
					}
					
				}

				solicitudPago.setListadoFacturas(listaFacturas);
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
			 * Obtiene solo la lista de facturas que fueron seleccionados desde la pagina. Lo demás se descarta.
			 */
			private List<FacturaSolicitudPago> getListaFacturasPreparada(List<FacturaSolicitudPago> listaFacturas) {
				List<FacturaSolicitudPago> listaFacturasNueva = new ArrayList<FacturaSolicitudPago>();
				for (FacturaSolicitudPago factura : listaFacturas) {
					if (!factura.isBorrado()) {
						if (factura.getTotal()!=0) {
							listaFacturasNueva.add(factura);
						}
					}
				}
				return listaFacturasNueva;
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
		
		form.add(facturasSolicitudPagoContainer);
		form.add(agregarFacturaSolicitudPago);
		form.add(importeFinalInput);
		form.add(importeParcialInput);
		
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
	
	private void addFacturasSolicitudPagoList(final WebMarkupContainer facturasSolicitudPagoContainer) {
		IDataProvider<FacturaSolicitudPago> facturasSolicitudPagoDataProvider = getFacturasSolicitudPagoProvider();
		
		DataView<FacturaSolicitudPago> dataView = new DataView<FacturaSolicitudPago>("listaFacturasSolicitudPago", facturasSolicitudPagoDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(final Item<FacturaSolicitudPago> item) {
				final AjaxDatePicker ajaxDatePicker = crearFacturaDatePicker(item);
				
				final DropDownChoice<TipoFactura> tipoFacturaDropDownChoice = crearTipoFacturaDropDownChoice(item);
				
				IModel<Double> totalModel = new PropertyModel<Double>(item.getModelObject(), "total");
				final CustomTextFieldDouble importeFacturaTextField = new CustomTextFieldDouble("importeFactura", totalModel);
				importeFacturaTextField.setOutputMarkupId(true);
				importeFacturaTextField.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
					private static final long serialVersionUID = 1L;
					protected void onUpdate(AjaxRequestTarget target) {
						target.add(importeFacturaTextField);						
						actualizarImportesFinalyParcial(target);
		            }
		        });
				
				tipoFacturaDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
					private static final long serialVersionUID = 1L;
					protected void onUpdate(AjaxRequestTarget target) {
						actualizarImportesFinalyParcial(target);
		            }
		        });
				
				final IModel<String> nroModel = new PropertyModel<String>(item.getModelObject(), "nroFacturaCompra");				
				AutoCompleteTextField<String> tipoYNroFacturaCompraSearchAutoCompleteTextField = new TipoYNroFacturaCompraSearchAutoCompleteTextField("nroFacturaCompraSearchAutoCompleteTextField", nroModel, proveedorSearchAutoComplete.getModelObject()) {
					private static final long serialVersionUID = 1L;			
					@Override
					protected void onSelected(AjaxRequestTarget target){
						//Al ser seleccionado desde el autocomplete, se sabe que el string tiene el formato "nroFactura (tipoFactura)"
						String tipoYNroFacturaString =  nroModel.getObject();
						String nroFactura = Utils.getNroFacturaFromTipoYNroFacturaString(tipoYNroFacturaString);
						TipoFactura tipoFactura = Utils.getTipoFacturaFromTipoYNroFacturaString(tipoYNroFacturaString);
						nroModel.setObject(nroFactura); //Le dejo seteado solo el nro sin el tipo
						FacturaCompraService facturaCompraService = new FacturaCompraService();
						FacturaCompra facturaCompra = facturaCompraService.getFacturaByRazonSocialAndTipoAndNroFactura(proveedorSearchAutoComplete.getModelObject(), tipoFactura, nroFactura);
						item.getModelObject().setFechaFacturaCompra(facturaCompra.getFecha());
						item.getModelObject().setTipoFacturaCompra(facturaCompra.getTipoFactura());
						item.getModelObject().setTotal(facturaCompra.calculateTotal());
						
						target.add(ajaxDatePicker);
						target.add(tipoFacturaDropDownChoice);
						target.add(importeFacturaTextField);
						target.add(this);
						actualizarImportesFinalyParcial(target);
					}
				};
				tipoYNroFacturaCompraSearchAutoCompleteTextField.add(StringValidator.maximumLength(255));
				
				item.add(ajaxDatePicker);
				item.add(tipoFacturaDropDownChoice);
				item.add(tipoYNroFacturaCompraSearchAutoCompleteTextField);
				item.add(importeFacturaTextField);
				
				//Utilizo lo siguiente para que todos los campos ingresados en la grilla de la pagina no sean eliminados al intentar agregar o quitar una fila.
				ajaxDatePicker.add(createNewAjaxFormComponentUpdatingBehavior());
				tipoFacturaDropDownChoice.add(createNewAjaxFormComponentUpdatingBehavior());
				tipoYNroFacturaCompraSearchAutoCompleteTextField.add(createNewAjaxFormComponentUpdatingBehavior());
				importeFacturaTextField.add(createNewAjaxFormComponentUpdatingBehavior());
				
				item.add(new AjaxFallbackLink<FacturaSolicitudPago>("eliminar", item.getModel()) {					
					private static final long serialVersionUID = 1L;
					
					@Override
					public void onClick(AjaxRequestTarget target) {
						FacturaSolicitudPago facturaSolicitudPagoSeleccionada = (FacturaSolicitudPago) getModelObject();
						facturaSolicitudPagoSeleccionada.setBorrado(true);
						target.add(facturasSolicitudPagoContainer);
						actualizarImportesFinalyParcial(target);
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
		
		facturasSolicitudPagoContainer.add(dataView);
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
	
	private IDataProvider<FacturaSolicitudPago> getFacturasSolicitudPagoProvider() {
		IDataProvider<FacturaSolicitudPago> facturasSolicitudPagoProvider = new IDataProvider<FacturaSolicitudPago>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void detach() {
			}

			@Override
			public Iterator<FacturaSolicitudPago> iterator(long first, long count) {
				return ((SolicitudPago)RegistrarSolicitudPagoNormalPage.this.getDefaultModelObject()).getListadoFacturas().iterator();
			}

			@Override
			public long size() {
				return ((SolicitudPago)RegistrarSolicitudPagoNormalPage.this.getDefaultModelObject()).getListadoFacturas().size();
			}

			@Override
			public IModel<FacturaSolicitudPago> model(FacturaSolicitudPago facturaSolicitudPago) {
				return new Model<FacturaSolicitudPago>(facturaSolicitudPago);
			}
        	
        };
		return facturasSolicitudPagoProvider;
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
				List<PagoSolicitudPago> listaPagos = ((SolicitudPago)RegistrarSolicitudPagoNormalPage.this.getDefaultModelObject()).getListadoPagos();
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
				List<PagoSolicitudPago> listaPagos = ((SolicitudPago)RegistrarSolicitudPagoNormalPage.this.getDefaultModelObject()).getListadoPagos();
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
	
	private AjaxDatePicker crearFacturaDatePicker(Item<FacturaSolicitudPago> item) {
		IModel<Date> fechaModel = new PropertyModel<Date>(item.getModelObject(), "fechaFacturaCompraAsDate");
		AjaxDatePicker ajaxDatePicker = new AjaxDatePicker("datepickerFactura", fechaModel, "dd/MM/yyyy", new Options());
		//ajaxDatePicker.setRequired(true);
		return ajaxDatePicker;
	}
	
	private DropDownChoice<TipoFactura> crearTipoFacturaDropDownChoice(Item<FacturaSolicitudPago> item) {
		TipoFacturaService tipoFacturaService = new TipoFacturaService();
		List<TipoFactura> tiposFactura = tipoFacturaService.getAllForCompras();
		
		IModel<TipoFactura> tipoModel = new PropertyModel<TipoFactura>(item.getModelObject(), "tipoFacturaCompra");
		
		DropDownChoice<TipoFactura> tipoFacturaDropDownChoice = new DropDownChoice<TipoFactura>("tipoFactura", tipoModel, tiposFactura, new ChoiceRenderer<TipoFactura>("nombre"));
		tipoFacturaDropDownChoice.setNullValid(true);
		return tipoFacturaDropDownChoice;
	}
	
	private AjaxDatePicker crearDatePicker(Item<PagoSolicitudPago> item, String nombreAtributo, String wicketID) {
		IModel<Date> fechaModel = new PropertyModel<Date>(item.getModelObject(), nombreAtributo);
		AjaxDatePicker ajaxDatePicker = new AjaxDatePicker(wicketID, fechaModel, "dd/MM/yyyy", new Options());
		return ajaxDatePicker;
	}
	
	/**
	 * Limpio el listado de Facturas y le cargo una factura vacia para que tome el proveedor en el SearchAutoComplete del Nro Factura
	 */
	private void limpiarYAdaptarListadoFacturas() {
		SolicitudPago solicitudPago = (SolicitudPago)RegistrarSolicitudPagoNormalPage.this.getDefaultModelObject();
		solicitudPago.setListadoFacturas(new ArrayList<FacturaSolicitudPago>());
		
		//Agrego una facturaSolicitudPago por defecto en el listado de Facturas Solicitud Pago
		FacturaSolicitudPago facturaSolicitudPago = new FacturaSolicitudPago();
		facturaSolicitudPago.setSolicitudPago(solicitudPago);		
		solicitudPago.getListadoFacturas().add(facturaSolicitudPago);
	}
	
	private void actualizarImportesFinalyParcial(AjaxRequestTarget target) {
		SolicitudPago solicitudPago = (SolicitudPago)RegistrarSolicitudPagoNormalPage.this.getDefaultModelObject();
		double importeFinal = 0;
		List<FacturaSolicitudPago> listadoFacturas = solicitudPago.getListadoFacturas();
		List<FacturaSolicitudPago> listadoFacturasSinBorrar = new ArrayList<FacturaSolicitudPago>();
		for (FacturaSolicitudPago facturaSolicitudPago : listadoFacturas) {
			if (!facturaSolicitudPago.isBorrado()){
				listadoFacturasSinBorrar.add(facturaSolicitudPago);
			}
		}
		
		for (FacturaSolicitudPago facturaSolicitudPago : listadoFacturasSinBorrar) {
			if ( facturaSolicitudPago.getTipoFacturaCompra() == null || ! facturaSolicitudPago.getTipoFacturaCompra().isNotaCredito() ) { 
				importeFinal += facturaSolicitudPago.getTotal();
			} else {
				importeFinal -= facturaSolicitudPago.getTotal();
			}
		}
		importeFinalInput.setDefaultModelObject(importeFinal);
		target.add(importeFinalInput);
		
		importeParcialInput.setEnabled(listadoFacturasSinBorrar.size()==1);
		if (listadoFacturasSinBorrar.size()==1) {
			importeParcialInput.setModelObject(0d);
		}
		target.add(importeParcialInput);
	}
}