package com.proit.vista.compras.solicitudes.conversion;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.DateValidator;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.ui.form.autocomplete.AutoCompleteTextField;
import com.googlecode.wicket.jquery.ui.form.datepicker.AjaxDatePicker;
import com.proit.modelo.TipoFactura;
import com.proit.modelo.compras.EstadoFacturaCompra;
import com.proit.modelo.compras.EstadoSolicitudPago;
import com.proit.modelo.compras.FacturaCompra;
import com.proit.modelo.compras.FacturaSolicitudPago;
import com.proit.modelo.compras.SolicitudPago;
import com.proit.servicios.TipoFacturaService;
import com.proit.servicios.compras.FacturaCompraService;
import com.proit.servicios.compras.FacturaSolicitudPagoService;
import com.proit.servicios.compras.SolicitudPagoService;
import com.proit.utils.GeneralValidator;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.vista.compras.solicitudes.SolicitudesPagoPage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.CalendarChoiceRenderer;
import com.proit.wicket.components.CustomTextFieldDouble;
import com.proit.wicket.components.ProveedorSearchAutoCompleteTextField;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class ConvertirAOPPage2  extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(ConvertirAOPPage2.class.getName());
	
	private boolean guardarYContinuar = false;
	
	private SolicitudPago solicitudPago;
	private FacturaSolicitudPago facturaSolicitudPago;
	
	private FacturaCompraService facturaService;
	
	@SuppressWarnings("unused")
	private String total;
	
	private CustomTextFieldDouble subtotalInput;
	private CustomTextFieldDouble ivaInput;
	private CustomTextFieldDouble percIvaInput;
	private CustomTextFieldDouble percIibbInput;
	private CustomTextFieldDouble percGciasInput;
	private CustomTextFieldDouble percSUSSInput;
	private CustomTextFieldDouble otrasPercInput;
	private DropDownChoice<TipoFactura> tipoFacturaDropDownChoice;
	private Label totalLabel;
		
	public ConvertirAOPPage2(PageParameters parameters, SolicitudPago solicitudPago) {		
		facturaService = new FacturaCompraService();
		
		this.solicitudPago = solicitudPago;
		this.facturaSolicitudPago = solicitudPago.getNextFacturaSolicitudPagoNoConvertida();
		
		setearDefaultModel();
		
		recalcularTotal();
		
		crearForm();
		
		add(new FeedbackPanel("feedback"));
		
		mostrarAlertaEnPantallaSiCorresponde(parameters);
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
	}
	
	private void setearDefaultModel() {
		FacturaCompra facturaCompra = new FacturaCompra();
		facturaCompra.setEstadoFactura(EstadoFacturaCompra.PENDIENTE);
		facturaCompra.setProveedor(solicitudPago.getProveedor());
		facturaCompra.setFecha(facturaSolicitudPago.getFechaFacturaCompra());
		facturaCompra.setTipoFactura(facturaSolicitudPago.getTipoFacturaCompra());
		facturaCompra.setNro(facturaSolicitudPago.getNroFacturaCompra());		
		
		double total = facturaSolicitudPago.getTotal();
		double subtotal = Utils.getSubtotalFromTotal(total, facturaSolicitudPago.getTipoFacturaCompra());
		double iva = Utils.getIvaFromTotal(total, facturaSolicitudPago.getTipoFacturaCompra());
		
		facturaCompra.setSubtotal(subtotal);
		facturaCompra.setIva(iva);
		
		this.setDefaultModel(Model.of(facturaCompra));
	}
	
	private void crearForm() {
		final AjaxDatePicker ajaxDatePicker = crearDatePicker();
		
		AjaxLink<String> hoyLink = new AjaxLink<String>("hoy") {			
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				modificarFechaSeleccionada(ajaxDatePicker, target, true);
			}
		};
		
		DropDownChoice<Calendar> mesImpositivoDropDownChoice = crearMesImpositivoDropDownChoice();
		
		IModel<String> proveedorModel = new PropertyModel<String>(getDefaultModel(), "proveedor.razonSocial");
		final AutoCompleteTextField<String> proveedorSearchAutoComplete = new ProveedorSearchAutoCompleteTextField("proveedorSearchAutocomplete", proveedorModel);
		proveedorSearchAutoComplete.setEnabled(false);
		proveedorSearchAutoComplete.setRequired(true);
		
		tipoFacturaDropDownChoice = crearTipoFacturaDropDownChoice();

		tipoFacturaDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;

			protected void onUpdate(AjaxRequestTarget target) {
				actualizarImporteIVA(target);
            }
        });		
		
		IModel<String> nroModel = new PropertyModel<String>(getDefaultModel(), "nro");
		final TextField<String> nroTextField = new TextField<String>("nro", nroModel);
		nroTextField.setRequired(true);
		
		totalLabel = new Label("total",new PropertyModel<String>(this, "total"));
		totalLabel.setOutputMarkupId(true);
		
		final IModel<Double> subtotalModel = new PropertyModel<Double>(getDefaultModel(), "subtotal");
		subtotalInput = new CustomTextFieldDouble("subtotal", subtotalModel);
		subtotalInput.setOutputMarkupId(true);
		subtotalInput.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				actualizarImporteIVA(target);
            }
        });
		
		ivaInput = new CustomTextFieldDouble("iva", new PropertyModel<Double>(getDefaultModel(), "iva"));
		ivaInput.setOutputMarkupId(true);
		ivaInput.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				actualizarImportes(target);
            }
        });
		
		percIvaInput = new CustomTextFieldDouble("percIva", new PropertyModel<Double>(getDefaultModel(), "percIva"));
		percIvaInput.setOutputMarkupId(true);
		percIvaInput.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				actualizarImportes(target);
            }
        });
		
		percIibbInput = new CustomTextFieldDouble("percIibb", new PropertyModel<Double>(getDefaultModel(), "percIibb"));
		percIibbInput.setOutputMarkupId(true);
		percIibbInput.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				actualizarImportes(target);
            }
        });
		
		percGciasInput = new CustomTextFieldDouble("percGcias", new PropertyModel<Double>(getDefaultModel(), "percGcias"));
		percGciasInput.setOutputMarkupId(true);
		percGciasInput.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				actualizarImportes(target);
            }
        });
		
		percSUSSInput = new CustomTextFieldDouble("percSUSS", new PropertyModel<Double>(getDefaultModel(), "percSUSS"));
		percSUSSInput.setOutputMarkupId(true);
		percSUSSInput.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				actualizarImportes(target);
            }
        });
		
		otrasPercInput = new CustomTextFieldDouble("otrasPerc", new PropertyModel<Double>(getDefaultModel(), "otrasPerc"));
		otrasPercInput.setOutputMarkupId(true);
		otrasPercInput.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				actualizarImportes(target);
            }
        });
		
		WebMarkupContainer estadoContainer = new WebMarkupContainer("estadoContainer");
		Label estado = new Label("estado",new PropertyModel<Double>(getDefaultModel(), "estadoFactura.nombre"));
		estadoContainer.add(estado);
		estadoContainer.setVisible(false);
		
		Button botonGuardar = new Button("guardar");
		
		Button guardarYContinuarButton = new Button("guardarYContinuar"){
			private static final long serialVersionUID = 1L;
			public void onSubmit() {
            	guardarYContinuar = true;
            }
        };
        
        Link<WebPage> cancelarLink = new Link<WebPage>("cancelarLink") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage(SolicitudesPagoPage.class);
			}
		};
		
		Form<?> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onValidate() {
				super.onValidate();
				GeneralValidator generalValidator = new GeneralValidator();
				boolean validacionOK = true;
				double subtotal = subtotalModel.getObject();
				String razonSocial = proveedorSearchAutoComplete.getConvertedInput();
				if (razonSocial==null) {
					razonSocial = (String) proveedorSearchAutoComplete.getDefaultModelObject();
				}
				String nroFactura = nroTextField.getConvertedInput();
				TipoFactura tipoFactura = tipoFacturaDropDownChoice.getModelObject();
				
				if (nroFactura!=null && !generalValidator.nroFacturaEsValido(nroFactura)) {
					error("El Nro. de Factura no tiene el formato correcto (Ejemplo: 00002-00000135)");
					validacionOK = false;
				}
				
				if (subtotal <= 0) {
					error("Subtotal debe ser mayor a cero."); 
					validacionOK = false;
				}
				if ( facturaService.nroFacturaAlreadyExists(razonSocial, tipoFactura, nroFactura)) {
					error("El tipo y número de factura seleccionado ya existe para el proveedor seleccionado."); 
					validacionOK = false;
				}
				
				if ( ! validacionOK ) {
					onError();
					return;
				}
			}
			@Override
			protected void onSubmit() {
				FacturaCompra facturaCompra = (FacturaCompra)ConvertirAOPPage2.this.getDefaultModelObject();
				
				Calendar fecha = Calendar.getInstance();
				fecha.setTime(ajaxDatePicker.getModelObject());
				facturaCompra.setFecha(fecha);
				
				String textoPorPantalla = "La factura " + facturaCompra.getNro() + " ha sido creada.";
				String resultado = "OK";
				
				try {
					facturaService.createOrUpdate(facturaCompra);
					
					FacturaSolicitudPagoService facturaSolicitudPagoService = new FacturaSolicitudPagoService();
					facturaSolicitudPago.setFacturaCompra(facturaCompra);
					facturaSolicitudPago.setTipoFacturaCompra(null);
					facturaSolicitudPago.setNroFacturaCompra(null);
					facturaSolicitudPagoService.createOrUpdate(facturaSolicitudPago);
					
					
					
					//TODO aca habria que ir a actualizar todas las factura_solicitud_pago restantes cuya solicitud_pago tenga asociado el mismo proveedor_id (buscarlas simil a lo q se hace para proveedor: tipo_factura_compra_id y nro_factura_compra, y obvio mismo proveedor q ya lo dije antes). 
					//TODO tal vez tb por fecha y monto de fact?
					//TODO ademas hay que hacer para todas esas solicitud_pago lo q se hace aca abajo (cambiar de estado si solicitudPago.hasNextFacturaSolicitudPagoNoConvertida()) -> creo que hay que usar ese metodo tal cual esta
					
					
					
					
					
					if (!solicitudPago.hasNextFacturaSolicitudPagoNoConvertida()) {
						SolicitudPagoService solicitudPagoService = new SolicitudPagoService();
						solicitudPago.setEstadoSolicitudPago(EstadoSolicitudPago.PENDIENTE_3);
						solicitudPagoService.createOrUpdate(solicitudPago);
					}
					
				} catch(Exception e) {
					log.error(e.getMessage(), e);
					textoPorPantalla = "La factura " + facturaCompra.getNro() + " no pudo ser creada correctamente. Por favor, vuelva a intentarlo.";
					resultado = "ERROR";
				}
				
				PageParameters pageParameters = new PageParameters();
				pageParameters.add("Resultado", resultado);
				pageParameters.add("TextoPantalla", textoPorPantalla);
				
				if (!guardarYContinuar) {			
					setResponsePage(SolicitudesPagoPage.class, pageParameters);
				} else {
					if (!solicitudPago.hasNextFacturaSolicitudPagoNoConvertida()) {
						//setResponsePage(ConvertirAOPPage3.class, pageParameters);
						if (solicitudPago.isSPySF()) {
							setResponsePage(new ConvertirAOPPage3SPySF(solicitudPago));
						} else if (solicitudPago.isCPySF()) {
							setResponsePage(new ConvertirAOPPage3CPySF(solicitudPago));
						} else {
							setResponsePage(new ConvertirAOPPage3Normal(solicitudPago));
						}
					} else {
						setResponsePage(new ConvertirAOPPage2(pageParameters, solicitudPago));
					}
				}				
			}

		};
		
		add(form);
		form.add(ajaxDatePicker);
		form.add(hoyLink);
		form.add(mesImpositivoDropDownChoice);
		form.add(proveedorSearchAutoComplete);
		form.add(tipoFacturaDropDownChoice);
		form.add(nroTextField);
		form.add(subtotalInput);
		form.add(ivaInput);
		form.add(percIvaInput);
		form.add(percIibbInput);
		form.add(percGciasInput);
		form.add(percSUSSInput);
		form.add(otrasPercInput);
		form.add(totalLabel);
		form.add(estadoContainer);
		form.add(botonGuardar);
		form.add(guardarYContinuarButton);
		form.add(cancelarLink);
	}
	
	protected void actualizarImporteIVA(AjaxRequestTarget target) {
		double subtotal = ((FacturaCompra)ConvertirAOPPage2.this.getDefaultModelObject()).getSubtotal();
		TipoFactura tipoFactura = ((FacturaCompra)ConvertirAOPPage2.this.getDefaultModelObject()).getTipoFactura();
		double iva = Utils.calcularImporteIVA(subtotal, tipoFactura);
		ivaInput.setDefaultModelObject(Utils.round(iva, 2));
		actualizarImportes(target);		
	}
	
	private AjaxDatePicker crearDatePicker() {
		Options options = new Options();
		//options.set("minDate", 0); //Habilitar esto en caso de no permitir cargar reservass p
		Calendar fecha =  ((FacturaCompra)getDefaultModelObject()).getFecha();
		IModel<Date> fechaModel;
		if (fecha != null) {
			fechaModel = Model.of(fecha.getTime());
		} else {
			fechaModel  = new Model<Date>();
		}
		AjaxDatePicker ajaxDatePicker = new AjaxDatePicker("datepicker", fechaModel, "dd/MM/yyyy", options) ;
		Calendar minCalendar = Calendar.getInstance();
		minCalendar.set(Calendar.YEAR, 1980);
		minCalendar.set(Calendar.MONTH, minCalendar.getMinimum(Calendar.MONTH));
		minCalendar.set(Calendar.DAY_OF_MONTH, minCalendar.getMinimum(Calendar.DAY_OF_MONTH));
		minCalendar.set(Calendar.HOUR_OF_DAY, minCalendar.getMinimum(Calendar.HOUR_OF_DAY));
		minCalendar.set(Calendar.MINUTE, minCalendar.getMinimum(Calendar.MINUTE));
		minCalendar.set(Calendar.SECOND, minCalendar.getMinimum(Calendar.SECOND));
		minCalendar.set(Calendar.MILLISECOND, minCalendar.getMinimum(Calendar.MILLISECOND));
		Calendar maxCalendar = Utils.firstMillisecondOfDay(Calendar.getInstance());
		ajaxDatePicker.add(new DateValidator(minCalendar.getTime(), maxCalendar.getTime(), "dd/MM/yyyy"));
		ajaxDatePicker.setRequired(true);
		return ajaxDatePicker;
	}
	
	private void modificarFechaSeleccionada(AjaxDatePicker ajaxDatePicker, AjaxRequestTarget target, boolean clickEnHoyLink) {
		if (clickEnHoyLink) {
			ajaxDatePicker.setModelObject(Utils.firstMillisecondOfDay(Calendar.getInstance()).getTime());
		}
		target.add(ajaxDatePicker);
	}
	
	private DropDownChoice<Calendar> crearMesImpositivoDropDownChoice() {
		final IModel<Calendar> mesImpositivoModel = new PropertyModel<Calendar>(getDefaultModel(), "mesImpositivo");
		List<Calendar> listaMeses = Utils.getListaMeses(-2, 0);
		mesImpositivoModel.setObject(listaMeses.get(listaMeses.size()-1));
		return new DropDownChoice<Calendar>("mesImpositivo", mesImpositivoModel, listaMeses, new CalendarChoiceRenderer("MM/yyyy"));
	}
	
	private DropDownChoice<TipoFactura> crearTipoFacturaDropDownChoice() {
		TipoFacturaService tipoFacturaService = new TipoFacturaService();
		List<TipoFactura> tiposFactura = tipoFacturaService.getAllForCompras();
		
		IModel<TipoFactura> tipoModel = new PropertyModel<TipoFactura>(getDefaultModel(), "tipoFactura");
		return new DropDownChoice<TipoFactura>("tipo", tipoModel, tiposFactura, new ChoiceRenderer<TipoFactura>("nombre"));
	}
	
	private void recalcularTotal() {
		Double totalDouble = ((FacturaCompra)getDefaultModelObject()).getSubtotal()
							+ ((FacturaCompra)getDefaultModelObject()).getIva()
							+ ((FacturaCompra)getDefaultModelObject()).getPercIva()
							+ ((FacturaCompra)getDefaultModelObject()).getPercIibb()
							+ ((FacturaCompra)getDefaultModelObject()).getPercGcias()
							+ ((FacturaCompra)getDefaultModelObject()).getPercSUSS()
							+ ((FacturaCompra)getDefaultModelObject()).getOtrasPerc();
		total = Utils.round2Decimals(totalDouble, ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale());
	}
	
	private void actualizarImportes(AjaxRequestTarget target) {
		recalcularTotal();
		target.add(subtotalInput);
		target.add(ivaInput);
		target.add(percIvaInput);
		target.add(percIibbInput);
		target.add(percGciasInput);
		target.add(percSUSSInput);
		target.add(otrasPercInput);
		target.add(totalLabel);
	}

}
