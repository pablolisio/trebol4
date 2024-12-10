package com.proit.vista.ventas.solicitudes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.ui.form.autocomplete.AutoCompleteTextField;
import com.googlecode.wicket.jquery.ui.form.datepicker.AjaxDatePicker;
import com.proit.modelo.TipoFactura;
import com.proit.modelo.ventas.Cliente;
import com.proit.modelo.ventas.DatoAdicionalFacturaVenta;
import com.proit.modelo.ventas.DetalleFacturaVenta;
import com.proit.modelo.ventas.EstadoFacturaVenta;
import com.proit.modelo.ventas.EstadoSolicitudFactura;
import com.proit.modelo.ventas.FacturaVenta;
import com.proit.modelo.ventas.SolicitudFacturaVenta;
import com.proit.servicios.EventoService;
import com.proit.servicios.TipoFacturaService;
import com.proit.servicios.ventas.ClienteService;
import com.proit.servicios.ventas.FacturaVentaService;
import com.proit.servicios.ventas.SolicitudFacturaVentaService;
import com.proit.utils.Constantes;
import com.proit.utils.GeneralValidator;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.components.ClienteSearchAutoCompleteTextField;
import com.proit.wicket.components.CuitCuilClienteSearchAutoCompleteTextField;
import com.proit.wicket.components.CustomTextFieldDouble;
import com.proit.wicket.components.EventoSearchAutoCompleteTextField;

@AuthorizeInstantiation({"Administrador","Desarrollador","Editor Solicitudes Factura"})
public class ConvertirAFacturaVentaPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(ConvertirAFacturaVentaPage.class.getName());

	private ClienteService clienteService;
	
	private AutoCompleteTextField<String> clienteSearchAutoComplete;
	private AutoCompleteTextField<String> cuitCuilClienteSearchAutoComplete;
	private AutoCompleteTextField<String> eventoSearchAutoComplete;
	
	private double importeFinal;		
	private CustomTextFieldDouble importeFinalInput;
	
	private TextField<String> facturaVentaAAnularTextField;
	
	private boolean permitirCualquierEvento;

	private DropDownChoice<TipoFactura> tipoFacturaAAnularDropDownChoice;
		
	public ConvertirAFacturaVentaPage(SolicitudFacturaVenta solicitud) {

		setearDefaultModel(solicitud);
		
		clienteService = new ClienteService();
		
		armarCuadroObservaciones(solicitud);
		
		crearForm();
		
		completarDatosFaltantes();
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
	}
	
	private void armarCuadroObservaciones(SolicitudFacturaVenta solicitud) {
		String observaciones = solicitud.getObservaciones();
		WebMarkupContainer observacionesContainer = new WebMarkupContainer("observacionesContainer");
		Label observacionesLbl = new Label("observaciones", "Observaciones: " + observaciones);
		observacionesContainer.setVisible(observaciones!=null && !observaciones.isEmpty());
		observacionesContainer.add(observacionesLbl);
		add(observacionesContainer);
	}

	private void completarDatosFaltantes() {
		FacturaVenta facturaVenta = (FacturaVenta)getDefaultModelObject();
		clienteSearchAutoComplete.setDefaultModelObject(facturaVenta.getCliente().getRazonSocial());
		cuitCuilClienteSearchAutoComplete.setDefaultModelObject(facturaVenta.getCliente().getCuitCuil());
		
		FacturaVenta facturaAAnular = facturaVenta.getFacturaVentaAAnular();
		if (facturaAAnular!=null) {
			TipoFacturaService tipoFacturaService = new TipoFacturaService();
			
			TipoFactura tipoFacturaVentaAAnular;
			if (facturaVenta.isNotaCreditoN()) {
				tipoFacturaVentaAAnular = ((TipoFactura)tipoFacturaAAnularDropDownChoice.getDefaultModelObject());
			} else {
				tipoFacturaVentaAAnular = tipoFacturaService.getEquivalenteParaNCoND(facturaVenta.getTipoFactura());
			}
			
			String nroSinPrefijo = Utils.getNroFacturaVentaSinPrefijo(tipoFacturaVentaAAnular, facturaAAnular.getNro());
			facturaVentaAAnularTextField.setDefaultModelObject(nroSinPrefijo);
		}
		
		((EventoSearchAutoCompleteTextField)eventoSearchAutoComplete).setRazonSocialCliente(facturaVenta.getCliente().getRazonSocial());
		((EventoSearchAutoCompleteTextField)eventoSearchAutoComplete).setPermitirCualquierEvento(permitirCualquierEvento);
		
		actualizarImporteFinal();		
	}

	private void setearDefaultModel(SolicitudFacturaVenta solicitudFacturaVenta) {
		SolicitudFacturaVentaService solicitudFacturaVentaService = new SolicitudFacturaVentaService();
		SolicitudFacturaVenta solicitud = (SolicitudFacturaVenta) solicitudFacturaVentaService.get(solicitudFacturaVenta.getId());
		FacturaVenta facturaVenta = solicitud.convertirAFacturaVenta();
		
		permitirCualquierEvento = solicitud.isPermitirCualquierEvento();
		
		//Como minimo la solicitud debe tener un detalle. Solo valido los datos adicionales
		if (facturaVenta.getListadoDatosAdicionales().isEmpty()) {
			facturaVenta.setListadoDatosAdicionales(new ArrayList<DatoAdicionalFacturaVenta>());

			//Agrego un DatoAdicionalFacturaVenta por defecto en el listado de Datos Adicionales
			DatoAdicionalFacturaVenta datoAdicionalFacturaVenta = new DatoAdicionalFacturaVenta();
			datoAdicionalFacturaVenta.setFacturaVenta(facturaVenta);
			facturaVenta.getListadoDatosAdicionales().add(datoAdicionalFacturaVenta);
		}
		
		facturaVenta.setFecha(Calendar.getInstance());
				
		this.setDefaultModel(Model.of(facturaVenta));
	}

	private void updateNroFacturaContainer(WebMarkupContainer nroFacturaContainer, Label nroFacturaVentaPrefixLbl, Label nroFacturaVentaPrefixLbl2, TipoFactura tipoFacturaSeleccionado) {
		nroFacturaContainer.setVisible(true);
		if (tipoFacturaSeleccionado.isTipoE() || tipoFacturaSeleccionado.isNotaCreditoE() || tipoFacturaSeleccionado.isNotaDebitoE()) {
			nroFacturaVentaPrefixLbl.setDefaultModelObject(Constantes.NRO_FACTURA_VENTA_PREFIX_00004);
			nroFacturaVentaPrefixLbl2.setDefaultModelObject(Constantes.NRO_FACTURA_VENTA_PREFIX_00004);
		} else if (tipoFacturaSeleccionado.isTipoN() || tipoFacturaSeleccionado.isNotaCreditoN()) {
			TipoFactura tipoFacturaAAnularSel = ((TipoFactura)tipoFacturaAAnularDropDownChoice.getDefaultModelObject());
			setPrefix2Properly(nroFacturaVentaPrefixLbl2, tipoFacturaAAnularSel);
			nroFacturaContainer.setVisible(false);
		} else {
			nroFacturaVentaPrefixLbl.setDefaultModelObject(Constantes.NRO_FACTURA_VENTA_PREFIX_00003);
			nroFacturaVentaPrefixLbl2.setDefaultModelObject(Constantes.NRO_FACTURA_VENTA_PREFIX_00003);
		}
		tipoFacturaAAnularDropDownChoice.setVisible(tipoFacturaSeleccionado.isNotaCreditoN());
	}
		
	private void crearForm() {
		FeedbackPanel feedback = new FeedbackPanel("feedback");
		
		// *** SECCION DETALLES Y DATOS ADICIONALES***
		//Importe Final
		importeFinalInput = new CustomTextFieldDouble("importeFinal", new PropertyModel<Double>(this, "importeFinal"));
		importeFinalInput.setOutputMarkupId(true);
		importeFinalInput.setEnabled(false);
		
		//Listado detalles
		final WebMarkupContainer detallesContainer = new WebMarkupContainer("detallesContainer");
		detallesContainer.setOutputMarkupPlaceholderTag(true);
		final AjaxFallbackLink<DetalleFacturaVenta> agregarDetalle = new AjaxFallbackLink<DetalleFacturaVenta>("agregarDetalle") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				FacturaVenta facturaVenta = (FacturaVenta)ConvertirAFacturaVentaPage.this.getDefaultModelObject();
				DetalleFacturaVenta detalleFacturaVenta = new DetalleFacturaVenta();
				detalleFacturaVenta.setFacturaVenta(facturaVenta);
				((FacturaVenta)ConvertirAFacturaVentaPage.this.getDefaultModelObject()).getListadoDetalles().add(detalleFacturaVenta);
				target.add(detallesContainer);
				actualizarImporteFinal(target);
			}
		};		
		addDetallesList(detallesContainer);
				
		//Listado Datos Adicionales
		final WebMarkupContainer datosAdicionalesContainer = new WebMarkupContainer("datosAdicionalesContainer");
		datosAdicionalesContainer.setOutputMarkupPlaceholderTag(true);
		final AjaxFallbackLink<DatoAdicionalFacturaVenta> agregarDatoAdicional = new AjaxFallbackLink<DatoAdicionalFacturaVenta>("agregarDatoAdicional") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				FacturaVenta facturaVenta = (FacturaVenta)ConvertirAFacturaVentaPage.this.getDefaultModelObject();
				DatoAdicionalFacturaVenta datoAdicionalFacturaVenta = new DatoAdicionalFacturaVenta();
				datoAdicionalFacturaVenta.setFacturaVenta(facturaVenta);
				((FacturaVenta)ConvertirAFacturaVentaPage.this.getDefaultModelObject()).getListadoDatosAdicionales().add(datoAdicionalFacturaVenta);
				target.add(datosAdicionalesContainer);
			}
		};
		IModel<String> nroOrdenCompraModel = new PropertyModel<String>(getDefaultModel(), "nroOrdenCompra");
		TextField<String> nroOrdenCompraTextField = new TextField<String>("nroOrdenCompra", nroOrdenCompraModel);
		
		IModel<String> nroRequisicionModel = new PropertyModel<String>(getDefaultModel(), "nroRequisicion");
		TextField<String> nroRequisicionTextField = new TextField<String>("nroRequisicion", nroRequisicionModel);
		
		nroOrdenCompraTextField.add(createNewAjaxFormComponentUpdatingBehavior());
		nroRequisicionTextField.add(createNewAjaxFormComponentUpdatingBehavior());
		
		datosAdicionalesContainer.add(nroOrdenCompraTextField);
		datosAdicionalesContainer.add(nroRequisicionTextField);
		addDatosAdicionalesList(datosAdicionalesContainer);
		
		// *** SECCION CLIENTE ***
		clienteSearchAutoComplete = new ClienteSearchAutoCompleteTextField("clienteSearchAutocomplete", new Model<String>()){
			private static final long serialVersionUID = 1L;			
			@Override
			protected void onSelected(AjaxRequestTarget target){
				String razonSocial = (String) clienteSearchAutoComplete.getDefaultModelObject();
				((EventoSearchAutoCompleteTextField)eventoSearchAutoComplete).setRazonSocialCliente(razonSocial);
				cuitCuilClienteSearchAutoComplete.setModelObject(clienteService.getCuitCuilByRazonSocial(razonSocial));
				clienteSearchAutoComplete.setEnabled(false);
				cuitCuilClienteSearchAutoComplete.setEnabled(false);
				target.add(clienteSearchAutoComplete);
				target.add(cuitCuilClienteSearchAutoComplete);
			}
			
		};
		clienteSearchAutoComplete.setRequired(true);
		
		cuitCuilClienteSearchAutoComplete = new CuitCuilClienteSearchAutoCompleteTextField("cuitCuilClienteSearchAutocomplete", new Model<String>()){
			private static final long serialVersionUID = 1L;			
			@Override
			protected void onSelected(AjaxRequestTarget target){
				String cuitCuil = (String) cuitCuilClienteSearchAutoComplete.getDefaultModelObject();
				String razonSocial = clienteService.getRazonSocialByCuitCuil(cuitCuil);
				((EventoSearchAutoCompleteTextField)eventoSearchAutoComplete).setRazonSocialCliente(razonSocial);
				clienteSearchAutoComplete.setModelObject(razonSocial);
				clienteSearchAutoComplete.setEnabled(false);
				cuitCuilClienteSearchAutoComplete.setEnabled(false);
				target.add(clienteSearchAutoComplete);
				target.add(cuitCuilClienteSearchAutoComplete);
			}
			
		};
		cuitCuilClienteSearchAutoComplete.setRequired(true);
		
		FacturaVenta factura = (FacturaVenta)getDefaultModelObject();
		
		// *** SECCION OTROS DATOS ***
		final WebMarkupContainer facturaAAnularContainer = new WebMarkupContainer("facturaAAnularContainer");
		Label nroFacturaVentaPrefixLbl2 = new Label("nroFacturaVentaPrefix2", Constantes.NRO_FACTURA_VENTA_PREFIX_00003);
		facturaVentaAAnularTextField = new TextField<String>("nroFacturaVentaAAnular", new Model<String>());
		setPrefix2Properly(nroFacturaVentaPrefixLbl2, factura.getTipoFactura());
		facturaAAnularContainer.add(nroFacturaVentaPrefixLbl2);
		facturaAAnularContainer.add(facturaVentaAAnularTextField);
		facturaAAnularContainer.setEnabled(((FacturaVenta)getDefaultModelObject()).getFacturaVentaAAnular()!=null);
		facturaAAnularContainer.setOutputMarkupId(true);
		
		WebMarkupContainer nroFacturaContainer = new WebMarkupContainer("nroFacturaContainer");
		Label nroFacturaVentaPrefixLbl = new Label("nroFacturaVentaPrefix", Constantes.NRO_FACTURA_VENTA_PREFIX_00003);
		setPrefix2Properly(nroFacturaVentaPrefixLbl, factura.getTipoFactura());
		final IModel<String> nroFacturaVentaModel = new PropertyModel<String>(getDefaultModel(), "nro");
		TextField<String> nroFacturaVentaTextField = new TextField<String>("nroFacturaVenta", nroFacturaVentaModel);
		nroFacturaVentaTextField.setRequired(true);
		nroFacturaContainer.add(nroFacturaVentaPrefixLbl);
		nroFacturaContainer.add(nroFacturaVentaTextField);
		nroFacturaContainer.setOutputMarkupPlaceholderTag(true);
		
		tipoFacturaAAnularDropDownChoice = crearTipoFacturaAAnularDropDownChoice(factura);
		tipoFacturaAAnularDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			
			protected void onUpdate(AjaxRequestTarget target) {
				TipoFactura tipoFacturaSeleccionado = ((TipoFactura)tipoFacturaAAnularDropDownChoice.getDefaultModelObject());
				setPrefix2Properly(nroFacturaVentaPrefixLbl2, tipoFacturaSeleccionado);
				target.add(facturaAAnularContainer);
            }
        });
		tipoFacturaAAnularDropDownChoice.setVisible(factura!=null && factura.getTipoFactura()!=null && factura.isNotaCreditoN());
		facturaAAnularContainer.add(tipoFacturaAAnularDropDownChoice);
		
				
		final DropDownChoice<TipoFactura> tipoFacturaDropDownChoice = crearTipoFacturaDropDownChoice();
		tipoFacturaDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			
			protected void onUpdate(AjaxRequestTarget target) {
				TipoFactura tipoFacturaSeleccionado = ((TipoFactura)tipoFacturaDropDownChoice.getDefaultModelObject());
				boolean enabled = tipoFacturaSeleccionado.isNotaCredito();
				facturaAAnularContainer.setEnabled(enabled);
				updateNroFacturaContainer(nroFacturaContainer, nroFacturaVentaPrefixLbl, nroFacturaVentaPrefixLbl2, tipoFacturaSeleccionado);
				target.add(facturaAAnularContainer);
				target.add(nroFacturaContainer);
			}
        });
		
		TipoFactura tipoFacturaSeleccionado = ((TipoFactura)tipoFacturaDropDownChoice.getDefaultModelObject());
		updateNroFacturaContainer(nroFacturaContainer, nroFacturaVentaPrefixLbl, nroFacturaVentaPrefixLbl2, tipoFacturaSeleccionado);//Para modificarlo dependiendo el tipo de factura elegido en la sol
		
		final AjaxDatePicker ajaxDatePicker = crearDatePicker();
		AjaxLink<String> hoyLink = new AjaxLink<String>("hoy") {			
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				ajaxDatePicker.setModelObject(Utils.firstMillisecondOfDay(Calendar.getInstance()).getTime());
				target.add(ajaxDatePicker);
			}
		};
		
		eventoSearchAutoComplete = new EventoSearchAutoCompleteTextField("eventoSearchAutoComplete", Model.of(((FacturaVenta)getDefaultModelObject()).getEvento().getNombreConCliente()), true);
		eventoSearchAutoComplete.setRequired(true);
		
		IModel<Boolean> permitirCualquierEventoModel = new PropertyModel<Boolean>(this, "permitirCualquierEvento");
		final CheckBox permitirCualquierEventoCheckbox = new CheckBox("permitirCualquierEvento", permitirCualquierEventoModel);
		permitirCualquierEventoCheckbox.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;

			protected void onUpdate(AjaxRequestTarget target) {
				boolean permitirCualquierEvento = (Boolean) permitirCualquierEventoCheckbox.getDefaultModelObject();
				((EventoSearchAutoCompleteTextField)eventoSearchAutoComplete).setPermitirCualquierEvento(permitirCualquierEvento);
            }
        });
				
		WebMarkupContainer estadoContainer = new WebMarkupContainer("estadoContainer");
		Label estado = new Label("estado",new PropertyModel<Double>(getDefaultModel(), "estadoFacturaVenta.nombre"));
		estadoContainer.add(estado);
		estadoContainer.setVisible(false);
		
		Button botonGuardar = new Button("guardar");
				
		Form<?> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;

			private boolean validacionOK(List<DetalleFacturaVenta> listaDetalles, List<DatoAdicionalFacturaVenta> listaDatosAdicionales) {
				super.onValidate();
				boolean validacionOK = true;
				FacturaVentaService facturaVentaService = new FacturaVentaService();
				ClienteService clienteService = new ClienteService();
				GeneralValidator generalValidator = new GeneralValidator();
				FacturaVenta facturaVenta = (FacturaVenta)ConvertirAFacturaVentaPage.this.getDefaultModelObject();
				
				String razonSocialCliente = clienteSearchAutoComplete.getModelObject();
								
				if (listaDetalles.isEmpty()) {
					validacionOK = informarError("Debe cargar al menos un detalle.");
				} else {
					if ( ! facturaVentaService.todoDetalleTieneCantidadMayorACero(listaDetalles) ) {
						validacionOK = informarError("Toda cantidad de detalle debe ser mayor a Cero.");
					}
					if ( ! facturaVentaService.todoDetalleTieneImporteDistintoACero(listaDetalles) ) {
						validacionOK = informarError("Todo importe de detalle debe ser distinto a Cero.");
					}
				}
								
				if (importeFinal <= 0) {
					validacionOK = informarError("El Subtotal debe ser mayor a 0.");
				}
				
				if ( razonSocialCliente == null || !clienteService.existsByRazonSocial(razonSocialCliente) ) {
					validacionOK = informarError("Debe ingresar un cliente valido. Utilice la funcion autocompletado.");
				}
				
				String nroFacturaVenta = nroFacturaVentaModel.getObject();
				TipoFactura tipoFactura = ((TipoFactura)tipoFacturaDropDownChoice.getDefaultModelObject());
				if (nroFacturaVenta!=null && !nroFacturaVenta.isEmpty()) { //Las facturas de tipo N tienen numero automatico incremental. nroFacturaVenta viene como null aca
					String nroFacturaVentaFinal = Utils.getNroFacturaVentaConPrefijo(tipoFactura, nroFacturaVenta);
					if (!generalValidator.nroFacturaEsValido(nroFacturaVentaFinal)){
						validacionOK = informarError("El Nro de Factura de Venta debe contener el formato correcto (Ejemplo: 00003-00000135)");
					} else {
						if ( facturaVentaService.nroFacturaAlreadyExists(tipoFactura, nroFacturaVentaFinal, facturaVenta.getId()) ) {
							validacionOK = informarError("El número de factura ingresado ya existe.");
						}
					}
				}
				
				String nroFacturaVentaAAnular = (String) facturaVentaAAnularTextField.getDefaultModelObject();
				if ( tipoFactura.isNotaCredito() && (nroFacturaVentaAAnular==null||nroFacturaVentaAAnular.isEmpty())){
					validacionOK = informarError("Debe indicar el Nro de Factura A Anular, ya que el Tipo de Factura seleccionado es Nota de Crédito.");
				} else if (tipoFactura.isNotaCredito() && nroFacturaVentaAAnular!=null && !nroFacturaVentaAAnular.isEmpty()) {
					TipoFacturaService tipoFacturaService = new TipoFacturaService();
					TipoFactura tipoFacturaVentaAAnular;
					if (facturaVenta.isNotaCreditoN()) {
						tipoFacturaVentaAAnular = ((TipoFactura)tipoFacturaAAnularDropDownChoice.getDefaultModelObject());
					} else {
						tipoFacturaVentaAAnular = tipoFacturaService.getEquivalenteParaNCoND(tipoFactura);
					}
					String nroFacturaVentaAAnularFinal = Utils.getNroFacturaVentaConPrefijo(tipoFacturaVentaAAnular, nroFacturaVentaAAnular);
					if (!tipoFacturaVentaAAnular.isTipoN() && !generalValidator.nroFacturaEsValido(nroFacturaVentaAAnularFinal)){
						validacionOK = informarError("El Nro de Factura de Venta A Anular debe contener el formato correcto (Ejemplo: 00003-00000135)");
					} else {
						FacturaVenta facturaVentaAAnular = facturaVentaService.getFacturaByTipoYNroFactura(tipoFacturaVentaAAnular, nroFacturaVentaAAnularFinal);
						if (facturaVentaAAnular==null) {
							validacionOK = informarError("El Nro de Factura de Venta A Anular ingresado no existe.");
						} else {
							if (!tipoFactura.isNotaCreditoN() && !facturaVentaAAnular.isXCobrar() && !facturaVentaAAnular.isCobradaParcial()) { //Antes solo x cobrar era posible, ahora tb puede ser cobrada parcial
								validacionOK = informarError("La Factura de Venta A Anular ingresada ya fue cobrada o fue anulada.");
							}/* else if (facturaVentaAAnular.calculateSubtotal()!=importeFinal) {
								Locale locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
								validacionOK = informarError("El importe (sin IVA) de la Factura a Anular ($"+Utils.round2Decimals(facturaVentaAAnular.calculateSubtotal(), locale)+") no es igual al importe indicado.");
							}*/
							//TODO agregar validacion de que la suma de todas las NC que afectan a una misma factura, no sean mayores al valor de la factura. esto en 3 lugares -> Al final no lo hice, ya fue, de ultima si la cargaron mal, que la eliminen y la carguen de nuevo
						}
						
					}
				}
				
				EventoService eventoService = new EventoService();
				String nombreEvento = Utils.getEventFromFullName(eventoSearchAutoComplete.getModelObject());
				String clienteEvento = Utils.getClientFromFullName(eventoSearchAutoComplete.getModelObject());
				 if ( (!permitirCualquierEvento && !eventoService.existsByClienteAndNombreEvento(razonSocialCliente, nombreEvento) ) 
						 || (permitirCualquierEvento && !eventoService.existsByClienteAndNombreEvento(clienteEvento, nombreEvento) ) ) {
					validacionOK = informarError("Debe ingresar un evento valido. Utilice la funcion autocompletado.");
				}
				
				return validacionOK;
			}

			private boolean informarError(String textoError) {
				error(textoError);
				return false;
			}
			
			@Override
			protected void onSubmit() {
				FacturaVenta facturaVenta = (FacturaVenta)ConvertirAFacturaVentaPage.this.getDefaultModelObject();
				List<DetalleFacturaVenta> listaDetalles = getListaDetallesPreparada(facturaVenta.getListadoDetalles());
				List<DatoAdicionalFacturaVenta> listaDatosAdicionales = getListaDatosAdicionalesPreparada(facturaVenta.getListadoDatosAdicionales());
				
				if ( ! validacionOK(listaDetalles, listaDatosAdicionales) ) {
					return;
				}
				
				FacturaVentaService facturaVentaService = new FacturaVentaService();
				String razonSocialCliente = clienteSearchAutoComplete.getModelObject();
				String cuitCuilCliente = cuitCuilClienteSearchAutoComplete.getModelObject();
				Cliente cliente = clienteService.getByRazonSocialAndCuitCuil(razonSocialCliente, cuitCuilCliente);
				
				Calendar fecha = Calendar.getInstance();
				fecha.setTime(ajaxDatePicker.getModelObject());
				facturaVenta.setFecha(fecha);
				
				facturaVenta.setCliente(cliente);
				facturaVenta.setUsuarioSolicitante(getUsuarioLogueado());
				facturaVenta.setEstadoFacturaVenta(EstadoFacturaVenta.X_COBRAR);
				
				facturaVenta.setListadoDetalles(listaDetalles);
				facturaVenta.setListadoDatosAdicionales(listaDatosAdicionales);
				
				String nroFacturaVentaAAnular = (String) facturaVentaAAnularTextField.getDefaultModelObject();
				if (facturaVenta.getTipoFactura().isNotaCredito() && nroFacturaVentaAAnular!=null) {
					TipoFacturaService tipoFacturaService = new TipoFacturaService();
					TipoFactura tipoFacturaVentaAAnular;
					if (facturaVenta.isNotaCreditoN()) {
						tipoFacturaVentaAAnular = ((TipoFactura)tipoFacturaAAnularDropDownChoice.getDefaultModelObject());
					} else {
						tipoFacturaVentaAAnular = tipoFacturaService.getEquivalenteParaNCoND(facturaVenta.getTipoFactura());
					}
					nroFacturaVentaAAnular = Utils.getNroFacturaVentaConPrefijo(tipoFacturaVentaAAnular, nroFacturaVentaAAnular);
					FacturaVenta facturaVentaAAnular = facturaVentaService.getFacturaByTipoYNroFactura(tipoFacturaVentaAAnular, nroFacturaVentaAAnular);
					facturaVenta.setFacturaVentaAAnular(facturaVentaAAnular);
					// --- Pedido por Bianca el 18-01-2017. No se tienen que poner como anulado. Se tienen que usar al crear la cobranza
					//facturaVentaAAnular.setEstadoFacturaVenta(EstadoFacturaVenta.ANULADO);
					//facturaVenta.setEstadoFacturaVenta(EstadoFacturaVenta.ANULADO);
					if (facturaVentaAAnular.isCobradaParcial()) {
						facturaVentaAAnular.setEstadoFacturaVenta(EstadoFacturaVenta.COBRADO_PARCIAL);
					} else {
						facturaVentaAAnular.setEstadoFacturaVenta(EstadoFacturaVenta.X_COBRAR);
					}
				}
				
				SolicitudFacturaVenta solicitudFacturaVenta = facturaVenta.getSolicitudFacturaVenta();
				SolicitudFacturaVentaService solicitudFacturaVentaService = new SolicitudFacturaVentaService();
				solicitudFacturaVenta.setEstadoSolicitudFactura(EstadoSolicitudFactura.CUMPLIDA);
				solicitudFacturaVentaService.createOrUpdate(solicitudFacturaVenta);
				
				EventoService eventoService = new EventoService();
				String nombreEvento = Utils.getEventFromFullName(eventoSearchAutoComplete.getModelObject());
				String clienteEvento = Utils.getClientFromFullName(eventoSearchAutoComplete.getModelObject());
				if (nombreEvento!=null) {
					facturaVenta.setEvento(eventoService.getByClienteAndNombreEvento(clienteEvento, nombreEvento));
				}
				
				String textoPorPantalla = "La Factura Venta " + (!facturaVenta.getTipoFactura().isTipoN()&&!facturaVenta.getTipoFactura().isNotaCreditoN()?facturaVenta.getNro():"") + " ha sido creada.";
				String resultado = "OK";
				
				try {
					facturaVentaService.createOrUpdateFacturaVenta(facturaVenta, nroFacturaVentaModel.getObject());
				} catch(Exception e) {
					log.error(e.getMessage(), e);
					textoPorPantalla = "La Factura Venta " + (!facturaVenta.getTipoFactura().isTipoN()&&!facturaVenta.getTipoFactura().isNotaCreditoN()?facturaVenta.getNro():"") + " no pudo ser creada correctamente. Por favor, vuelva a intentarlo.";
					resultado = "ERROR";
				}
				
				PageParameters pageParameters = new PageParameters();
				pageParameters.add("Resultado", resultado);
				pageParameters.add("TextoPantalla", textoPorPantalla);
				setResponsePage(SolicitudesFacturaVentaPage.class, pageParameters);
				
			}

			/**
			 * Obtiene solo la lista de detalles que fueron completados desde la pagina. Lo demás se descarta.
			 */
			private List<DetalleFacturaVenta> getListaDetallesPreparada(List<DetalleFacturaVenta> listaDetalles) {
				List<DetalleFacturaVenta> listaDetallesNueva = new ArrayList<DetalleFacturaVenta>();
				for (DetalleFacturaVenta detalle : listaDetalles) {
					if (!detalle.isBorrado()) {
						if (detalle.getDescripcion()!=null && !detalle.getDescripcion().isEmpty()) {
							listaDetallesNueva.add(detalle);
						}
					}
				}
				return listaDetallesNueva;
			}
			
			/**
			 * Obtiene solo la lista de datos adicionales que fueron completados desde la pagina. Lo demás se descarta.
			 */
			private List<DatoAdicionalFacturaVenta> getListaDatosAdicionalesPreparada(List<DatoAdicionalFacturaVenta> listaDatosAdicionales) {
				List<DatoAdicionalFacturaVenta> listaDatosAdicionalesNueva = new ArrayList<DatoAdicionalFacturaVenta>();
				for (DatoAdicionalFacturaVenta datoAdicional : listaDatosAdicionales) {
					if (!datoAdicional.isBorrado()) {
						if (datoAdicional.getDescripcion()!=null && !datoAdicional.getDescripcion().isEmpty()) {
							listaDatosAdicionalesNueva.add(datoAdicional);
						}
					}
				}
				return listaDatosAdicionalesNueva;
			}

		};
		
		this.add(form);
		form.add(feedback.setOutputMarkupId(true));
		
		form.add(clienteSearchAutoComplete);
		form.add(cuitCuilClienteSearchAutoComplete);

		form.add(importeFinalInput);
		
		form.add(detallesContainer);
		form.add(agregarDetalle);
		
		form.add(datosAdicionalesContainer);
		form.add(agregarDatoAdicional);
		
		form.add(nroFacturaContainer);
		form.add(ajaxDatePicker);
		form.add(hoyLink);
		form.add(eventoSearchAutoComplete);
		form.add(permitirCualquierEventoCheckbox);
		form.add(tipoFacturaDropDownChoice);
		
		form.add(facturaAAnularContainer);
		form.add(estadoContainer);
		form.add(botonGuardar);
	}
	
	private void addDetallesList(final WebMarkupContainer detallesContainer) {
		IDataProvider<DetalleFacturaVenta> detallesDataProvider = getDetallesProvider();
		
		DataView<DetalleFacturaVenta> dataView = new DataView<DetalleFacturaVenta>("listaDetalles", detallesDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(final Item<DetalleFacturaVenta> item) {
				IModel<Integer> cantidadModel = new PropertyModel<Integer>(item.getDefaultModel(), "cantidad");
				final TextField<Integer> cantidadTextField = new TextField<Integer>("cantidad", cantidadModel);
				cantidadTextField.setOutputMarkupId(true);
				cantidadTextField.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
					private static final long serialVersionUID = 1L;
					protected void onUpdate(AjaxRequestTarget target) {
						actualizarImporteFinal(target);
		            }
		        });
				
				IModel<String> descripcionModel = new PropertyModel<String>(item.getDefaultModel(), "descripcion");
				TextField<String> descripcionTextField = new TextField<String>("descripcion", descripcionModel);
				
				IModel<Double> importeModel = new PropertyModel<Double>(item.getModelObject(), "importe");
				final CustomTextFieldDouble importeTextField = new CustomTextFieldDouble("importe", importeModel);
				importeTextField.setOutputMarkupId(true);
				importeTextField.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
					private static final long serialVersionUID = 1L;
					protected void onUpdate(AjaxRequestTarget target) {
						actualizarImporteFinal(target);
		            }
		        });
				item.add(cantidadTextField);
				item.add(descripcionTextField);
				item.add(importeTextField);
				
				//Utilizo lo siguiente para que todos los campos ingresados en la grilla de la pagina no sean eliminados al intentar agregar o quitar una fila.
				cantidadTextField.add(createNewAjaxFormComponentUpdatingBehavior());
				descripcionTextField.add(createNewAjaxFormComponentUpdatingBehavior());
				importeTextField.add(createNewAjaxFormComponentUpdatingBehavior());
				
				AjaxFallbackLink<DetalleFacturaVenta> eliminarLink = new AjaxFallbackLink<DetalleFacturaVenta>("eliminar", item.getModel()) {
					private static final long serialVersionUID = 1L;
					
					@Override
					public void onClick(AjaxRequestTarget target) {
						DetalleFacturaVenta detalleSeleccionado = (DetalleFacturaVenta) getModelObject();
						detalleSeleccionado.setBorrado(true);
						target.add(detallesContainer);
						actualizarImporteFinal(target);
					}
				};
				item.add(eliminarLink);
				
				if (item.getModelObject().isBorrado()) { //para el caso de editar se debe poner invisible
					item.setVisible(false);
				}
				
			}

		};
		
		detallesContainer.add(dataView);
	}
	
	private void addDatosAdicionalesList(final WebMarkupContainer datosAdicionalesContainer) {
		IDataProvider<DatoAdicionalFacturaVenta> datosAdicionalesDataProvider = getDatosAdicionalesProvider();
		
		DataView<DatoAdicionalFacturaVenta> dataView = new DataView<DatoAdicionalFacturaVenta>("listaDatosAdicionales", datosAdicionalesDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(final Item<DatoAdicionalFacturaVenta> item) {
				IModel<String> descripcionModel = new PropertyModel<String>(item.getDefaultModel(), "descripcion");
				TextField<String> descripcionTextField = new TextField<String>("descripcion", descripcionModel);

				item.add(descripcionTextField);
				
				//Utilizo lo siguiente para que todos los campos ingresados en la grilla de la pagina no sean eliminados al intentar agregar o quitar una fila.
				descripcionTextField.add(createNewAjaxFormComponentUpdatingBehavior());
				
				AjaxFallbackLink<DatoAdicionalFacturaVenta> eliminarLink = new AjaxFallbackLink<DatoAdicionalFacturaVenta>("eliminar", item.getModel()) {					
					private static final long serialVersionUID = 1L;
					
					@Override
					public void onClick(AjaxRequestTarget target) {
						DatoAdicionalFacturaVenta datoAdicionalSeleccionado = (DatoAdicionalFacturaVenta) getModelObject();
						datoAdicionalSeleccionado.setBorrado(true);
						target.add(datosAdicionalesContainer);
						actualizarImporteFinal(target);
					}
				};
				
				item.add(eliminarLink);
				
				if (item.getModelObject().isBorrado()) { //para el caso de editar se debe poner invisible
					item.setVisible(false);
				}
				
			}

		};
		
		datosAdicionalesContainer.add(dataView);
	}
	
	private IDataProvider<DetalleFacturaVenta> getDetallesProvider() {
		IDataProvider<DetalleFacturaVenta> detallesProvider = new IDataProvider<DetalleFacturaVenta>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void detach() {
			}

			@Override
			public Iterator<DetalleFacturaVenta> iterator(long first, long count) {
				return ((FacturaVenta)ConvertirAFacturaVentaPage.this.getDefaultModelObject()).getListadoDetalles().iterator();
			}

			@Override
			public long size() {
				return ((FacturaVenta)ConvertirAFacturaVentaPage.this.getDefaultModelObject()).getListadoDetalles().size();
			}

			@Override
			public IModel<DetalleFacturaVenta> model(DetalleFacturaVenta detalle) {
				return new Model<DetalleFacturaVenta>(detalle);
			}
        	
        };
		return detallesProvider;
	}
	
	private IDataProvider<DatoAdicionalFacturaVenta> getDatosAdicionalesProvider() {
		IDataProvider<DatoAdicionalFacturaVenta> datosAdicionalesDataProvider = new IDataProvider<DatoAdicionalFacturaVenta>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void detach() {
			}

			@Override
			public Iterator<DatoAdicionalFacturaVenta> iterator(long first, long count) {
				return ((FacturaVenta)ConvertirAFacturaVentaPage.this.getDefaultModelObject()).getListadoDatosAdicionales().iterator();
			}

			@Override
			public long size() {
				return ((FacturaVenta)ConvertirAFacturaVentaPage.this.getDefaultModelObject()).getListadoDatosAdicionales().size();
			}

			@Override
			public IModel<DatoAdicionalFacturaVenta> model(DatoAdicionalFacturaVenta datoAdicionalFacturaVenta) {
				return new Model<DatoAdicionalFacturaVenta>(datoAdicionalFacturaVenta);
			}
        	
        };
		return datosAdicionalesDataProvider;
	}
		
	private DropDownChoice<TipoFactura> crearTipoFacturaDropDownChoice() {
		TipoFacturaService tipoFacturaService = new TipoFacturaService();
		List<TipoFactura> tiposFactura = tipoFacturaService.getAllForVentas();
		IModel<TipoFactura> tipoModel = new PropertyModel<TipoFactura>(getDefaultModel(), "tipoFactura");
		return new DropDownChoice<TipoFactura>("tipoFactura", tipoModel, tiposFactura, new ChoiceRenderer<TipoFactura>("nombre"));
	}
	
	private DropDownChoice<TipoFactura> crearTipoFacturaAAnularDropDownChoice(FacturaVenta factura) {
		TipoFacturaService tipoFacturaService = new TipoFacturaService();
		List<TipoFactura> tiposFactura = tipoFacturaService.getAllForNotaCreditoN();
		
		TipoFactura tipoSeleccionado = null;
		IModel<TipoFactura> tipoModel = Model.of(tipoSeleccionado);
		FacturaVenta fcAAnular = factura.getFacturaVentaAAnular();
		if (fcAAnular!=null) {
			for (TipoFactura tipoActual : tiposFactura) {
				if (tipoActual.equals(fcAAnular.getTipoFactura())) { //tipo de factura seleccionado al crear la fact
					tipoSeleccionado = tipoActual;
					break;
				}
			}
		}
		tipoModel.setObject(tipoSeleccionado);
		return new DropDownChoice<TipoFactura>("tipoFacturaAAnular", tipoModel, tiposFactura, new ChoiceRenderer<TipoFactura>("nombre"));
	}
		
	private void actualizarImporteFinal(AjaxRequestTarget target) {
		actualizarImporteFinal();
		target.add(importeFinalInput);
	}

	private void actualizarImporteFinal() {
		FacturaVenta facturaVenta = (FacturaVenta)ConvertirAFacturaVentaPage.this.getDefaultModelObject();
		double importeFinal = 0;
		List<DetalleFacturaVenta> listadoDetalles = facturaVenta.getListadoDetalles();
		List<DetalleFacturaVenta> listadoDetallesSinBorrar = new ArrayList<DetalleFacturaVenta>();
		for (DetalleFacturaVenta detalle : listadoDetalles) {
			if (!detalle.isBorrado()){
				listadoDetallesSinBorrar.add(detalle);
			}
		}
		
		for (DetalleFacturaVenta detalle : listadoDetallesSinBorrar) {
			importeFinal += detalle.calculateTotalDetalle();
		}
		importeFinalInput.setDefaultModelObject(importeFinal);
	}
	
	private AjaxDatePicker crearDatePicker() {
		Options options = new Options();
		
		//IModel<Date> fechaModel = new Model<Date>();
		Calendar calendar = ((FacturaVenta)getDefaultModelObject()).getFecha();
		IModel<Date> fechaModel = Model.of(calendar.getTime());
		AjaxDatePicker ajaxDatePicker = new AjaxDatePicker("datepicker", fechaModel, "dd/MM/yyyy", options) ;
		ajaxDatePicker.setRequired(true);
		return ajaxDatePicker;
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
	
	private void setPrefix2Properly(Label nroFacturaVentaPrefixLbl2, TipoFactura tipoFacturaSeleccionado) {
		if (tipoFacturaSeleccionado!=null) {
			String placeholder = "Usar ultimos 4 digitos";
			if (tipoFacturaSeleccionado.isTipoE() || tipoFacturaSeleccionado.isNotaCreditoE() || tipoFacturaSeleccionado.isNotaDebitoE()) {
				nroFacturaVentaPrefixLbl2.setDefaultModelObject(Constantes.NRO_FACTURA_VENTA_PREFIX_00004);
			} else if (tipoFacturaSeleccionado.isTipoN()) {
				placeholder = "Ingresar Nro completo";
				nroFacturaVentaPrefixLbl2.setDefaultModelObject(Constantes.NRO_FACTURA_VENTA_PREFIX_NONE);
			} else {
				nroFacturaVentaPrefixLbl2.setDefaultModelObject(Constantes.NRO_FACTURA_VENTA_PREFIX_00003);
			}
			facturaVentaAAnularTextField.add(new AttributeModifier("placeholder", placeholder));
		}		
	}
	
}
