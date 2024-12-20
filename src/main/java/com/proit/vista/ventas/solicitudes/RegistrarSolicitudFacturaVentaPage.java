package com.proit.vista.ventas.solicitudes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.StringValidator;

import com.googlecode.wicket.jquery.ui.form.autocomplete.AutoCompleteTextField;
import com.proit.modelo.TipoFactura;
import com.proit.modelo.ventas.Cliente;
import com.proit.modelo.ventas.DatoAdicionalSolicitudFacturaVenta;
import com.proit.modelo.ventas.DetalleSolicitudFacturaVenta;
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

@AuthorizeInstantiation({"Administrador","Desarrollador","Solicitante Facturas Ventas","Editor Solicitudes Factura"})
public class RegistrarSolicitudFacturaVentaPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(RegistrarSolicitudFacturaVentaPage.class.getName());

	private ClienteService clienteService;
	
	private AutoCompleteTextField<String> clienteSearchAutoComplete;
	private AutoCompleteTextField<String> cuitCuilClienteSearchAutoComplete;
	private AutoCompleteTextField<String> eventoSearchAutoComplete;
	
	private double importeFinal;		
	private CustomTextFieldDouble importeFinalInput;
	
	private TextField<String> facturaVentaAAnularTextField;
	
	public RegistrarSolicitudFacturaVentaPage() {
		this(false, null);
	}

	public RegistrarSolicitudFacturaVentaPage(boolean esVerDetalles, SolicitudFacturaVenta solicitud) {
		
		if (solicitud!=null){
			SolicitudFacturaVentaService solicitudFacturaVentaService = new SolicitudFacturaVentaService();
			solicitud = (SolicitudFacturaVenta) solicitudFacturaVentaService.get(solicitud.getId());
			this.setDefaultModel(Model.of(solicitud));
		} else {
			setearDefaultModel();
		}
		
		clienteService = new ClienteService();
		
		armarCuadroNroSolicitud(solicitud);
		
		crearForm(esVerDetalles);
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
	}

	private void armarCuadroNroSolicitud(SolicitudFacturaVenta solicitud) {
		WebMarkupContainer nroSolicitudContainer = new WebMarkupContainer("nroSolicitudContainer");
		String nro = solicitud!=null ? solicitud.getNro() : "";		
		Label nroSolicitudLbl = new Label("nroSolicitud", nro);
		nroSolicitudContainer.setVisible(solicitud!=null);
		nroSolicitudContainer.add(nroSolicitudLbl);
		add(nroSolicitudContainer);
	}

	private void setearDefaultModel() {
		SolicitudFacturaVenta solicitud = new SolicitudFacturaVenta();
		solicitud.setListadoDetalles(new ArrayList<DetalleSolicitudFacturaVenta>());
		solicitud.setListadoDatosAdicionales(new ArrayList<DatoAdicionalSolicitudFacturaVenta>());
		
		//Agrego un Detalle por defecto en el listado de Detalles
		DetalleSolicitudFacturaVenta detalle = new DetalleSolicitudFacturaVenta();
		detalle.setSolicitudFacturaVenta(solicitud);
		solicitud.getListadoDetalles().add(detalle);
		
		//Agrego un DatoAdicional por defecto en el listado de Datos Adicionales
		DatoAdicionalSolicitudFacturaVenta datoAdicional = new DatoAdicionalSolicitudFacturaVenta();
		datoAdicional.setSolicitudFacturaVenta(solicitud);
		solicitud.getListadoDatosAdicionales().add(datoAdicional);
				
		this.setDefaultModel(Model.of(solicitud));
	}

	private void crearForm(final boolean esVerDetalles) {
		FeedbackPanel feedback = new FeedbackPanel("feedback");
		
		// *** SECCION DETALLES Y DATOS ADICIONALES***
		//Importe Final
		importeFinalInput = new CustomTextFieldDouble("importeFinal", new PropertyModel<Double>(this, "importeFinal"));
		importeFinalInput.setOutputMarkupId(true);
		importeFinalInput.setEnabled(false);
		
		//Listado detalles
		final WebMarkupContainer detallesContainer = new WebMarkupContainer("detallesContainer");
		detallesContainer.setOutputMarkupPlaceholderTag(true);
		final AjaxFallbackLink<DetalleSolicitudFacturaVenta> agregarDetalle = new AjaxFallbackLink<DetalleSolicitudFacturaVenta>("agregarDetalle") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				SolicitudFacturaVenta solicitud = (SolicitudFacturaVenta)RegistrarSolicitudFacturaVentaPage.this.getDefaultModelObject();
				DetalleSolicitudFacturaVenta detalle = new DetalleSolicitudFacturaVenta();
				detalle.setSolicitudFacturaVenta(solicitud);
				((SolicitudFacturaVenta)RegistrarSolicitudFacturaVentaPage.this.getDefaultModelObject()).getListadoDetalles().add(detalle);
				target.add(detallesContainer);
				actualizarImporteFinal(target);
			}
		};		
		addDetallesList(detallesContainer, esVerDetalles);
				
		//Listado Datos Adicionales
		final WebMarkupContainer datosAdicionalesContainer = new WebMarkupContainer("datosAdicionalesContainer");
		datosAdicionalesContainer.setOutputMarkupPlaceholderTag(true);
		final AjaxFallbackLink<DatoAdicionalSolicitudFacturaVenta> agregarDatoAdicional = new AjaxFallbackLink<DatoAdicionalSolicitudFacturaVenta>("agregarDatoAdicional") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				SolicitudFacturaVenta solicitud = (SolicitudFacturaVenta)RegistrarSolicitudFacturaVentaPage.this.getDefaultModelObject();
				DatoAdicionalSolicitudFacturaVenta datoAdicional = new DatoAdicionalSolicitudFacturaVenta();
				datoAdicional.setSolicitudFacturaVenta(solicitud);
				((SolicitudFacturaVenta)RegistrarSolicitudFacturaVentaPage.this.getDefaultModelObject()).getListadoDatosAdicionales().add(datoAdicional);
				target.add(datosAdicionalesContainer);
			}
		};
		IModel<String> nroOrdenCompraModel = new PropertyModel<String>(getDefaultModel(), "nroOrdenCompra");
		TextField<String> nroOrdenCompraTextField = new TextField<String>("nroOrdenCompra", nroOrdenCompraModel);
		nroOrdenCompraTextField.add(StringValidator.maximumLength(255));
		
		IModel<String> nroRequisicionModel = new PropertyModel<String>(getDefaultModel(), "nroRequisicion");
		TextField<String> nroRequisicionTextField = new TextField<String>("nroRequisicion", nroRequisicionModel);
		nroRequisicionTextField.add(StringValidator.maximumLength(255));
		
		nroOrdenCompraTextField.add(createNewAjaxFormComponentUpdatingBehavior());
		nroRequisicionTextField.add(createNewAjaxFormComponentUpdatingBehavior());
		
		datosAdicionalesContainer.add(nroOrdenCompraTextField);
		datosAdicionalesContainer.add(nroRequisicionTextField);
		addDatosAdicionalesList(datosAdicionalesContainer, esVerDetalles);
		
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
		
		SolicitudFacturaVenta solicitud = (SolicitudFacturaVenta)getDefaultModelObject();
		
		// *** SECCION OTROS DATOS ***
		final WebMarkupContainer facturaAAnularContainer = new WebMarkupContainer("facturaAAnularContainer");
		Label nroFacturaVentaPrefixLbl2 = new Label("nroFacturaVentaPrefix2", Constantes.NRO_FACTURA_VENTA_PREFIX_00003);
		facturaVentaAAnularTextField = new TextField<String>("nroFacturaVentaAAnular", new Model<String>());
		setPrefix2Properly(nroFacturaVentaPrefixLbl2, solicitud.getTipoFactura());
		facturaAAnularContainer.add(nroFacturaVentaPrefixLbl2);
		facturaAAnularContainer.add(facturaVentaAAnularTextField);
		facturaAAnularContainer.setEnabled(false);
		facturaAAnularContainer.setOutputMarkupId(true);
		
		final DropDownChoice<TipoFactura> tipoFacturaAAnularDropDownChoice = crearTipoFacturaAAnularDropDownChoice(solicitud);
		tipoFacturaAAnularDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			
			protected void onUpdate(AjaxRequestTarget target) {
				TipoFactura tipoFacturaSeleccionado = ((TipoFactura)tipoFacturaAAnularDropDownChoice.getDefaultModelObject());
				setPrefix2Properly(nroFacturaVentaPrefixLbl2, tipoFacturaSeleccionado);
				target.add(facturaAAnularContainer);
            }
        });
		tipoFacturaAAnularDropDownChoice.setVisible(solicitud!=null && solicitud.getTipoFactura()!=null && solicitud.getTipoFactura().isNotaCreditoN());
		facturaAAnularContainer.add(tipoFacturaAAnularDropDownChoice);
				
		final DropDownChoice<TipoFactura> tipoFacturaDropDownChoice = crearTipoFacturaDropDownChoice();
		tipoFacturaDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			
			protected void onUpdate(AjaxRequestTarget target) {
				TipoFactura tipoFacturaSeleccionado = ((TipoFactura)tipoFacturaDropDownChoice.getDefaultModelObject());
				boolean enabled = tipoFacturaSeleccionado.isNotaCredito();
				facturaAAnularContainer.setEnabled(enabled);
				if (tipoFacturaSeleccionado.isTipoE() || tipoFacturaSeleccionado.isNotaCreditoE() || tipoFacturaSeleccionado.isNotaDebitoE()) {
					nroFacturaVentaPrefixLbl2.setDefaultModelObject(Constantes.NRO_FACTURA_VENTA_PREFIX_00004);
				} else if (tipoFacturaSeleccionado.isTipoN() || tipoFacturaSeleccionado.isNotaCreditoN()) {
					TipoFactura tipoFacturaAAnularSel = ((TipoFactura)tipoFacturaAAnularDropDownChoice.getDefaultModelObject());
					setPrefix2Properly(nroFacturaVentaPrefixLbl2, tipoFacturaAAnularSel);
				} else {
					nroFacturaVentaPrefixLbl2.setDefaultModelObject(Constantes.NRO_FACTURA_VENTA_PREFIX_00003);
				}
				tipoFacturaAAnularDropDownChoice.setVisible(tipoFacturaSeleccionado.isNotaCreditoN());
				target.add(facturaAAnularContainer);
            }
        });
				
		eventoSearchAutoComplete = new EventoSearchAutoCompleteTextField("eventoSearchAutoComplete", new Model<String>(), true);
		eventoSearchAutoComplete.setRequired(true);
		
		IModel<Boolean> permitirCualquierEventoModel = new PropertyModel<Boolean>(getDefaultModel(), "permitirCualquierEvento");
		final CheckBox permitirCualquierEventoCheckbox = new CheckBox("permitirCualquierEvento", permitirCualquierEventoModel);
		permitirCualquierEventoCheckbox.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;

			protected void onUpdate(AjaxRequestTarget target) {
				boolean permitirCualquierEvento = (Boolean) permitirCualquierEventoCheckbox.getDefaultModelObject();
				((EventoSearchAutoCompleteTextField)eventoSearchAutoComplete).setPermitirCualquierEvento(permitirCualquierEvento);
            }
        });
						
		IModel<String> observacionesModel = new PropertyModel<String>(getDefaultModel(), "observaciones");
		TextArea<String> observacionesTextArea = new TextArea<String>("observaciones", observacionesModel);
				
		WebMarkupContainer estadoContainer = new WebMarkupContainer("estadoContainer");
		Label estado = new Label("estado",new PropertyModel<Double>(getDefaultModel(), "estadoSolicitudFactura.nombre"));
		estadoContainer.add(estado);
		estadoContainer.setVisible(false);
		
		Form<?> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;

			private boolean validacionOK(List<DetalleSolicitudFacturaVenta> listaDetalles, List<DatoAdicionalSolicitudFacturaVenta> listaDatosAdicionales) {
				super.onValidate();
				boolean validacionOK = true;
				SolicitudFacturaVentaService solicitudFacturaVentaService = new SolicitudFacturaVentaService();
				ClienteService clienteService = new ClienteService();
				GeneralValidator generalValidator = new GeneralValidator();
				
				String razonSocialCliente = clienteSearchAutoComplete.getModelObject();
								
				if (listaDetalles.isEmpty()) {
					validacionOK = informarError("Debe cargar al menos un detalle.");
				} else {
					if ( ! solicitudFacturaVentaService.todoDetalleTieneCantidadMayorACero(listaDetalles) ) {
						validacionOK = informarError("Toda cantidad de detalle debe ser mayor a Cero.");
					}
					if ( ! solicitudFacturaVentaService.todoDetalleTieneImporteDistintoACero(listaDetalles) ) {
						validacionOK = informarError("Todo importe de detalle debe ser distinto a Cero.");
					}
				}
								
				if (importeFinal <= 0) {
					validacionOK = informarError("El Subtotal debe ser mayor a 0.");
				}
				
				if ( razonSocialCliente == null || !clienteService.existsByRazonSocial(razonSocialCliente) ) {
					validacionOK = informarError("Debe ingresar un cliente valido. Utilice la funcion autocompletado.");
				}
				
				String nroFacturaVentaAAnular = (String) facturaVentaAAnularTextField.getDefaultModelObject();
				TipoFactura tipoFactura = ((TipoFactura)tipoFacturaDropDownChoice.getDefaultModelObject());
				if ( tipoFactura.isNotaCredito() && (nroFacturaVentaAAnular==null||nroFacturaVentaAAnular.isEmpty())){
					validacionOK = informarError("Debe indicar el Nro de Factura A Anular, ya que el Tipo de Factura seleccionado es Nota de Crédito.");
				} else if (tipoFactura.isNotaCredito() && nroFacturaVentaAAnular!=null && !nroFacturaVentaAAnular.isEmpty()) {
					TipoFacturaService tipoFacturaService = new TipoFacturaService();
					TipoFactura tipoFacturaVentaAAnular;
					if (solicitud.isNotaCreditoN()) {
						tipoFacturaVentaAAnular = ((TipoFactura)tipoFacturaAAnularDropDownChoice.getDefaultModelObject());
					} else {
						tipoFacturaVentaAAnular = tipoFacturaService.getEquivalenteParaNCoND(tipoFactura);
					}
					String nroFacturaVentaAAnularFinal = Utils.getNroFacturaVentaConPrefijo(tipoFacturaVentaAAnular, nroFacturaVentaAAnular);
					if (!tipoFacturaVentaAAnular.isTipoN() && !generalValidator.nroFacturaEsValido(nroFacturaVentaAAnularFinal)){
						validacionOK = informarError("El Nro de Factura de Venta A Anular debe contener el formato correcto (Ejemplo: 00003-00000135)");
					} else {
						FacturaVentaService facturaVentaService = new FacturaVentaService();
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
				
				boolean permitirCualquierEvento = ((SolicitudFacturaVenta)RegistrarSolicitudFacturaVentaPage.this.getDefaultModelObject()).isPermitirCualquierEvento();
				
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
				SolicitudFacturaVenta solicitud = (SolicitudFacturaVenta)RegistrarSolicitudFacturaVentaPage.this.getDefaultModelObject();
				List<DetalleSolicitudFacturaVenta> listaDetalles = getListaDetallesPreparada(solicitud.getListadoDetalles());
				List<DatoAdicionalSolicitudFacturaVenta> listaDatosAdicionales = getListaDatosAdicionalesPreparada(solicitud.getListadoDatosAdicionales());
				
				if ( ! validacionOK(listaDetalles, listaDatosAdicionales) ) {
					return;
				}
				
				SolicitudFacturaVentaService solicitudFacturaVentaService = new SolicitudFacturaVentaService();
				String razonSocialCliente = clienteSearchAutoComplete.getModelObject();
				String cuitCuilCliente = cuitCuilClienteSearchAutoComplete.getModelObject();
				Cliente cliente = clienteService.getByRazonSocialAndCuitCuil(razonSocialCliente, cuitCuilCliente);
				
				solicitud.setCliente(cliente);
				
				solicitud.setEstadoSolicitudFactura(EstadoSolicitudFactura.PENDIENTE);
				
				solicitud.setListadoDetalles(listaDetalles);
				solicitud.setListadoDatosAdicionales(listaDatosAdicionales);
								
				String nroFacturaVentaAAnular = (String) facturaVentaAAnularTextField.getDefaultModelObject();
				if (solicitud.getTipoFactura().isNotaCredito() && nroFacturaVentaAAnular!=null) {
					TipoFacturaService tipoFacturaService = new TipoFacturaService();
					TipoFactura tipoFacturaVentaAAnular;
					if (solicitud.isNotaCreditoN()) {
						tipoFacturaVentaAAnular = ((TipoFactura)tipoFacturaAAnularDropDownChoice.getDefaultModelObject());
					} else {
						tipoFacturaVentaAAnular = tipoFacturaService.getEquivalenteParaNCoND(solicitud.getTipoFactura());
					}
					nroFacturaVentaAAnular = Utils.getNroFacturaVentaConPrefijo(tipoFacturaVentaAAnular, nroFacturaVentaAAnular);
					FacturaVentaService facturaVentaService = new FacturaVentaService();
					FacturaVenta facturaVentaAAnular = facturaVentaService.getFacturaByTipoYNroFactura(tipoFacturaVentaAAnular, nroFacturaVentaAAnular);
					solicitud.setFacturaVentaAAnular(facturaVentaAAnular);
				}
								
				EventoService eventoService = new EventoService();
				String nombreEvento = Utils.getEventFromFullName(eventoSearchAutoComplete.getModelObject());
				String clienteEvento = Utils.getClientFromFullName(eventoSearchAutoComplete.getModelObject());
				if (nombreEvento!=null) {
					solicitud.setEvento(eventoService.getByClienteAndNombreEvento(clienteEvento, nombreEvento));
				}
				
				if(solicitud.getNro()==null){ //no piso estos datos en caso de editar
					solicitud.setFecha(Calendar.getInstance());
					solicitud.setUsuarioSolicitante(getUsuarioLogueado());
				}
								
				String textoPorPantalla;
				String resultado;
				
				try {
					if(solicitud.getNro()==null){ //no piso estos datos en caso de editar
						String nro = solicitudFacturaVentaService.getNextNroSolicitud();
						solicitud.setNro(nro); //es el ultimo paso						
						textoPorPantalla = "La Solicitud de Factura Venta " + solicitud.getNro() + " ha sido creada.";
					} else {
						textoPorPantalla = "La Solicitud de Factura Venta " + solicitud.getNro() + " ha sido modificada.";
					}
					solicitudFacturaVentaService.createOrUpdate(solicitud);
					resultado = "OK";
				} catch(Exception e) {
					log.error(e.getMessage(), e);
					textoPorPantalla = "La Solicitud de Factura Venta " + solicitud.getNro() + " no pudo ser guardada correctamente. Por favor, vuelva a intentarlo.";
					resultado = "ERROR";
				}
				
				PageParameters pageParameters = new PageParameters();
				pageParameters.add("Resultado", resultado);
				pageParameters.add("TextoPantalla", textoPorPantalla);
				
				if (isUsuarioLogueadoRolAdministrador()||isUsuarioLogueadoRolDesarrollador()||isUsuarioLogueadoRolEditorSolicitudesFactura()) {
					setResponsePage(SolicitudesFacturaVentaPage.class, pageParameters);
				} else {
					setResponsePage(MisSolicitudesFacturaVentaPage.class, pageParameters);
				}
				
			}

			/**
			 * Obtiene solo la lista de detalles que fueron completados desde la pagina. Lo demás se descarta.
			 */
			private List<DetalleSolicitudFacturaVenta> getListaDetallesPreparada(List<DetalleSolicitudFacturaVenta> listaDetalles) {
				List<DetalleSolicitudFacturaVenta> listaDetallesNueva = new ArrayList<DetalleSolicitudFacturaVenta>();
				for (DetalleSolicitudFacturaVenta detalle : listaDetalles) {
//					if (!detalle.isBorrado()) {
						if (detalle.getDescripcion()!=null && !detalle.getDescripcion().isEmpty()) {
							listaDetallesNueva.add(detalle);
						}
//					}
				}
				return listaDetallesNueva;
			}
			
			/**
			 * Obtiene solo la lista de datos adicionales que fueron completados desde la pagina. Lo demás se descarta.
			 */
			private List<DatoAdicionalSolicitudFacturaVenta> getListaDatosAdicionalesPreparada(List<DatoAdicionalSolicitudFacturaVenta> listaDatosAdicionales) {
				List<DatoAdicionalSolicitudFacturaVenta> listaDatosAdicionalesNueva = new ArrayList<DatoAdicionalSolicitudFacturaVenta>();
				for (DatoAdicionalSolicitudFacturaVenta datoAdicional : listaDatosAdicionales) {
//					if (!datoAdicional.isBorrado()) {
						if (datoAdicional.getDescripcion()!=null && !datoAdicional.getDescripcion().isEmpty()) {
							listaDatosAdicionalesNueva.add(datoAdicional);
						}
//					}
				}
				return listaDatosAdicionalesNueva;
			}

		};
		
		Button botonGuardar = new Button("guardar");
		
		agregarRechazarBtn(form, esVerDetalles);
		agregarEliminarBtn(form, esVerDetalles);
		
		Link<WebPage> volverLink = new Link<WebPage>("volverLink") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				if (isUsuarioLogueadoRolAdministrador()||isUsuarioLogueadoRolDesarrollador()||isUsuarioLogueadoRolEditorSolicitudesFactura()) {
					setResponsePage(SolicitudesFacturaVentaPage.class);
				} else {
					setResponsePage(MisSolicitudesFacturaVentaPage.class);
				}
			}
		};
		
		if (solicitud.getNro()!=null) { //es editar
			clienteSearchAutoComplete.setDefaultModelObject(solicitud.getCliente().getRazonSocial());
			cuitCuilClienteSearchAutoComplete.setDefaultModelObject(solicitud.getCliente().getCuitCuil());
			eventoSearchAutoComplete.setDefaultModelObject(solicitud.getEvento().getNombreConCliente());
			
			((EventoSearchAutoCompleteTextField)eventoSearchAutoComplete).setRazonSocialCliente(solicitud.getCliente().getRazonSocial());
			((EventoSearchAutoCompleteTextField)eventoSearchAutoComplete).setPermitirCualquierEvento(permitirCualquierEventoModel.getObject());
			
			FacturaVenta facturaAAnular = solicitud.getFacturaVentaAAnular();
			if (facturaAAnular!=null) {
				if (esVerDetalles) {
					facturaVentaAAnularTextField.setDefaultModelObject(facturaAAnular.getNro());
				} else {
					TipoFacturaService tipoFacturaService = new TipoFacturaService();
					TipoFactura tipoFacturaVentaAAnular;
					if (solicitud.isNotaCreditoN()) {
						tipoFacturaVentaAAnular = ((TipoFactura)tipoFacturaAAnularDropDownChoice.getDefaultModelObject());
					} else {
						tipoFacturaVentaAAnular = tipoFacturaService.getEquivalenteParaNCoND(solicitud.getTipoFactura());
					}					
					String nroSinPrefijo = Utils.getNroFacturaVentaSinPrefijo(tipoFacturaVentaAAnular, facturaAAnular.getNro());
					facturaVentaAAnularTextField.setDefaultModelObject(nroSinPrefijo);
					facturaAAnularContainer.setEnabled(solicitud.isNotaCreditoN());
				}
				
			}
			
			actualizarImporteFinal();
		}
		
		if (esVerDetalles) {
			clienteSearchAutoComplete.setEnabled(false);
			cuitCuilClienteSearchAutoComplete.setEnabled(false);			
			agregarDetalle.setVisible(false);
			agregarDatoAdicional.setVisible(false);			
			nroOrdenCompraTextField.setEnabled(false);
			nroRequisicionTextField.setEnabled(false);
			tipoFacturaDropDownChoice.setEnabled(false);
			eventoSearchAutoComplete.setEnabled(false);
			permitirCualquierEventoCheckbox.setEnabled(false);
			observacionesTextArea.setEnabled(false);
			nroFacturaVentaPrefixLbl2.setVisible(false);
			facturaVentaAAnularTextField.setEnabled(false);
			estadoContainer.setVisible(true);
			botonGuardar.setVisible(false);
		}
		
		this.add(form);
		form.add(feedback.setOutputMarkupId(true));
		
		form.add(clienteSearchAutoComplete);
		form.add(cuitCuilClienteSearchAutoComplete);

		form.add(importeFinalInput);
		
		form.add(detallesContainer);
		form.add(agregarDetalle);
		
		form.add(datosAdicionalesContainer);
		form.add(agregarDatoAdicional);
		
		form.add(eventoSearchAutoComplete);
		form.add(permitirCualquierEventoCheckbox);
		form.add(tipoFacturaDropDownChoice);
		form.add(observacionesTextArea);
		
		form.add(facturaAAnularContainer);
		form.add(estadoContainer);
		form.add(botonGuardar);
		form.add(volverLink);
	}
	
	private void agregarRechazarBtn(Form<?> form, boolean esVerDetalles) {
		final SolicitudFacturaVenta solicitudFactura = (SolicitudFacturaVenta)RegistrarSolicitudFacturaVentaPage.this.getDefaultModelObject();
		Button intentarRechazarBtn = new Button("botonIntentarRechazar");
		intentarRechazarBtn.setVisible( esVerDetalles && solicitudFactura.isPendiente()	&& (isUsuarioLogueadoRolAdministrador() || isUsuarioLogueadoRolDesarrollador()) );
		form.add(intentarRechazarBtn);
		
		Link<SolicitudFacturaVenta> rechazarBtn = new Link<SolicitudFacturaVenta>("rechazarBtn") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				SolicitudFacturaVentaService solicitudFacturaVentaService = new SolicitudFacturaVentaService();
				solicitudFactura.setEstadoSolicitudFactura(EstadoSolicitudFactura.RECHAZADA);
				solicitudFacturaVentaService.createOrUpdate(solicitudFactura);
				setResponsePage(new RegistrarSolicitudFacturaVentaPage(true, solicitudFactura));
			}
		};
		form.add(rechazarBtn);
		form.add(new Label("solicitudARechazar", solicitudFactura.getNro()));
	}
	
	private void agregarEliminarBtn(Form<?> form, boolean esVerDetalles) {
		final SolicitudFacturaVenta solicitudFactura = (SolicitudFacturaVenta)RegistrarSolicitudFacturaVentaPage.this.getDefaultModelObject();
		Button intentarEliminarBtn = new Button("botonIntentarEliminar");
		intentarEliminarBtn.setVisible( esVerDetalles && solicitudFactura.isPendiente()
				 						&& (isUsuarioLogueadoRolSolicitanteFacturasVentas() || isUsuarioLogueadoRolDesarrollador()
				 						&& getUsuarioLogueado().equals(solicitudFactura.getUsuarioSolicitante())) );
		form.add(intentarEliminarBtn);
		
		Link<SolicitudFacturaVenta> eliminarBtn = new Link<SolicitudFacturaVenta>("eliminarBtn") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				String textoPorPantalla = "La Solicitud de Factura Venta " + solicitudFactura.getNro() + " ha sido eliminada.";
				String resultado = "OK";
				try {
					SolicitudFacturaVentaService solicitudFacturaVentaService = new SolicitudFacturaVentaService();
					boolean fueBorrada = solicitudFacturaVentaService.delete(solicitudFactura);
					if (!fueBorrada) {
						textoPorPantalla = "La Solicitud de Factura Venta " + solicitudFactura.getNro() + " no puede ser eliminada, porque su estado ya no es Pendiente.";
						resultado = "ERROR";
					}
				} catch(Exception e) {
					log.error(e.getMessage(), e);
					textoPorPantalla = "La Solicitud de Factura Venta " + solicitudFactura.getNro() + " no ha sido eliminada correctamente. Por favor, vuelva a intentarlo.";
					resultado = "ERROR";
				}
				PageParameters pageParameters = new PageParameters();
				pageParameters.add("Resultado", resultado);
				pageParameters.add("TextoPantalla", textoPorPantalla);
				if (isUsuarioLogueadoRolAdministrador()||isUsuarioLogueadoRolDesarrollador()||isUsuarioLogueadoRolEditorSolicitudesFactura()) {
					setResponsePage(SolicitudesFacturaVentaPage.class, pageParameters);
				} else {
					setResponsePage(MisSolicitudesFacturaVentaPage.class, pageParameters);
				}
			}
		};
		form.add(eliminarBtn);
		form.add(new Label("solicitudAEliminar", solicitudFactura.getNro()));
	}
	
	private void addDetallesList(final WebMarkupContainer detallesContainer, final boolean esVerDetalles) {
		IDataProvider<DetalleSolicitudFacturaVenta> detallesDataProvider = getDetallesProvider();
		
		DataView<DetalleSolicitudFacturaVenta> dataView = new DataView<DetalleSolicitudFacturaVenta>("listaDetalles", detallesDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(final Item<DetalleSolicitudFacturaVenta> item) {
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
						target.add(importeTextField);
		            }
		        });
				item.add(cantidadTextField);
				item.add(descripcionTextField);
				item.add(importeTextField);
				
				//Utilizo lo siguiente para que todos los campos ingresados en la grilla de la pagina no sean eliminados al intentar agregar o quitar una fila.
				cantidadTextField.add(createNewAjaxFormComponentUpdatingBehavior());
				descripcionTextField.add(createNewAjaxFormComponentUpdatingBehavior());
				importeTextField.add(createNewAjaxFormComponentUpdatingBehavior());
				
				AjaxFallbackLink<DetalleSolicitudFacturaVenta> eliminarLink = new AjaxFallbackLink<DetalleSolicitudFacturaVenta>("eliminar", item.getModel()) {
					private static final long serialVersionUID = 1L;
					
					@Override
					public void onClick(AjaxRequestTarget target) {
						DetalleSolicitudFacturaVenta detalleSeleccionado = (DetalleSolicitudFacturaVenta) getModelObject();
						detalleSeleccionado.setBorrado(true);
						target.add(detallesContainer);
						actualizarImporteFinal(target);
					}
				};
				item.add(eliminarLink);
				
				if (item.getModelObject().isBorrado()) { //para el caso de editar se debe poner invisible
					item.setVisible(false);
				}
				
				if (esVerDetalles) {
					cantidadTextField.setEnabled(false);
					descripcionTextField.setEnabled(false);
					importeTextField.setEnabled(false);
					eliminarLink.setVisible(false);
				}
			}

		};
		
		detallesContainer.add(dataView);
	}
	
	private void addDatosAdicionalesList(final WebMarkupContainer datosAdicionalesContainer, final boolean esVerDetalles) {
		IDataProvider<DatoAdicionalSolicitudFacturaVenta> datosAdicionalesDataProvider = getDatosAdicionalesProvider();
		
		DataView<DatoAdicionalSolicitudFacturaVenta> dataView = new DataView<DatoAdicionalSolicitudFacturaVenta>("listaDatosAdicionales", datosAdicionalesDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(final Item<DatoAdicionalSolicitudFacturaVenta> item) {
				IModel<String> descripcionModel = new PropertyModel<String>(item.getDefaultModel(), "descripcion");
				TextField<String> descripcionTextField = new TextField<String>("descripcion", descripcionModel);

				item.add(descripcionTextField);
				
				//Utilizo lo siguiente para que todos los campos ingresados en la grilla de la pagina no sean eliminados al intentar agregar o quitar una fila.
				descripcionTextField.add(createNewAjaxFormComponentUpdatingBehavior());
				
				AjaxFallbackLink<DatoAdicionalSolicitudFacturaVenta> eliminarLink = new AjaxFallbackLink<DatoAdicionalSolicitudFacturaVenta>("eliminar", item.getModel()) {					
					private static final long serialVersionUID = 1L;
					
					@Override
					public void onClick(AjaxRequestTarget target) {
						DatoAdicionalSolicitudFacturaVenta datoAdicionalSeleccionado = (DatoAdicionalSolicitudFacturaVenta) getModelObject();
						datoAdicionalSeleccionado.setBorrado(true);
						target.add(datosAdicionalesContainer);
						actualizarImporteFinal(target);
					}
				};
				
				item.add(eliminarLink);
				
				if (item.getModelObject().isBorrado()) { //para el caso de editar se debe poner invisible
					item.setVisible(false);
				}
				
				if (esVerDetalles) {
					descripcionTextField.setEnabled(false);
					eliminarLink.setVisible(false);
				}
			}

		};
		
		datosAdicionalesContainer.add(dataView);
	}
	
	private IDataProvider<DetalleSolicitudFacturaVenta> getDetallesProvider() {
		IDataProvider<DetalleSolicitudFacturaVenta> detallesProvider = new IDataProvider<DetalleSolicitudFacturaVenta>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void detach() {
			}

			@Override
			public Iterator<DetalleSolicitudFacturaVenta> iterator(long first, long count) {
				return ((SolicitudFacturaVenta)RegistrarSolicitudFacturaVentaPage.this.getDefaultModelObject()).getListadoDetalles().iterator();
			}

			@Override
			public long size() {
				return ((SolicitudFacturaVenta)RegistrarSolicitudFacturaVentaPage.this.getDefaultModelObject()).getListadoDetalles().size();
			}

			@Override
			public IModel<DetalleSolicitudFacturaVenta> model(DetalleSolicitudFacturaVenta detalle) {
				return new Model<DetalleSolicitudFacturaVenta>(detalle);
			}
        	
        };
		return detallesProvider;
	}
	
	private IDataProvider<DatoAdicionalSolicitudFacturaVenta> getDatosAdicionalesProvider() {
		IDataProvider<DatoAdicionalSolicitudFacturaVenta> datosAdicionalesDataProvider = new IDataProvider<DatoAdicionalSolicitudFacturaVenta>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void detach() {
			}

			@Override
			public Iterator<DatoAdicionalSolicitudFacturaVenta> iterator(long first, long count) {
				return ((SolicitudFacturaVenta)RegistrarSolicitudFacturaVentaPage.this.getDefaultModelObject()).getListadoDatosAdicionales().iterator();
			}

			@Override
			public long size() {
				return ((SolicitudFacturaVenta)RegistrarSolicitudFacturaVentaPage.this.getDefaultModelObject()).getListadoDatosAdicionales().size();
			}

			@Override
			public IModel<DatoAdicionalSolicitudFacturaVenta> model(DatoAdicionalSolicitudFacturaVenta datoAdicional) {
				return new Model<DatoAdicionalSolicitudFacturaVenta>(datoAdicional);
			}
        	
        };
		return datosAdicionalesDataProvider;
	}
		
	private DropDownChoice<TipoFactura> crearTipoFacturaDropDownChoice() {
		SolicitudFacturaVenta solicitud = (SolicitudFacturaVenta)RegistrarSolicitudFacturaVentaPage.this.getDefaultModelObject();
		TipoFacturaService tipoFacturaService = new TipoFacturaService();
		List<TipoFactura> tiposFactura = tipoFacturaService.getAllForVentas();
		
		IModel<TipoFactura> tipoModel = new PropertyModel<TipoFactura>(getDefaultModel(), "tipoFactura");
		if (solicitud.getNro()==null) {
			TipoFactura tipoSeleccionado = null;
			for (TipoFactura tipoActual : tiposFactura) {
				if (tipoActual.equals(TipoFactura.TIPO_A)) { //tipo de factura seleccionado por defecto
					tipoSeleccionado = tipoActual;
					break;
				}
			}
			tipoModel.setObject(tipoSeleccionado);
		}
		return new DropDownChoice<TipoFactura>("tipoFactura", tipoModel, tiposFactura, new ChoiceRenderer<TipoFactura>("nombre"));
	}
	
	private DropDownChoice<TipoFactura> crearTipoFacturaAAnularDropDownChoice(SolicitudFacturaVenta solicitud) {
		TipoFacturaService tipoFacturaService = new TipoFacturaService();
		List<TipoFactura> tiposFactura = tipoFacturaService.getAllForNotaCreditoN();
		
		TipoFactura tipoSeleccionado = null;
		IModel<TipoFactura> tipoModel = Model.of(tipoSeleccionado);
		if (solicitud.getNro()==null) { //no es editar y no es verDetalles
			for (TipoFactura tipoActual : tiposFactura) {
				if (tipoActual.equals(TipoFactura.TIPO_A)) { //tipo de factura seleccionado por defecto
					tipoSeleccionado = tipoActual;
					break;
				}
			}
		} else { //es editar o ver detalles
			FacturaVenta fcAAnular = solicitud.getFacturaVentaAAnular();
			if (fcAAnular!=null) {
				for (TipoFactura tipoActual : tiposFactura) {
					if (tipoActual.equals(fcAAnular.getTipoFactura())) { //tipo de factura seleccionado al crear la fact
						tipoSeleccionado = tipoActual;
						break;
					}
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
		SolicitudFacturaVenta solicitud = (SolicitudFacturaVenta)RegistrarSolicitudFacturaVentaPage.this.getDefaultModelObject();
		double importeFinal = 0;
		List<DetalleSolicitudFacturaVenta> listadoDetalles = solicitud.getListadoDetalles();
		List<DetalleSolicitudFacturaVenta> listadoDetallesSinBorrar = new ArrayList<DetalleSolicitudFacturaVenta>();
		for (DetalleSolicitudFacturaVenta detalle : listadoDetalles) {
			if (!detalle.isBorrado()){
				listadoDetallesSinBorrar.add(detalle);
			}
		}
		
		for (DetalleSolicitudFacturaVenta detalle : listadoDetallesSinBorrar) {
			importeFinal += detalle.calculateTotalDetalle();
		}
		importeFinalInput.setDefaultModelObject(importeFinal);
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
