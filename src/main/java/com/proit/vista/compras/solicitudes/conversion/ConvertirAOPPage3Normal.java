package com.proit.vista.compras.solicitudes.conversion;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
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

import com.googlecode.wicket.jquery.ui.form.autocomplete.AutoCompleteTextField;
import com.googlecode.wicket.jquery.ui.form.datepicker.AjaxDatePicker;
import com.proit.modelo.ModoPago;
import com.proit.modelo.Usuario;
import com.proit.modelo.compras.CobroAlternativo;
import com.proit.modelo.compras.EstadoFacturaCompra;
import com.proit.modelo.compras.EstadoSolicitudPago;
import com.proit.modelo.compras.FacturaCompra;
import com.proit.modelo.compras.FacturaSolicitudPago;
import com.proit.modelo.compras.OrdenPago;
import com.proit.modelo.compras.Pago;
import com.proit.modelo.compras.PagoSolicitudPago;
import com.proit.modelo.compras.PlanCuenta;
import com.proit.modelo.compras.Proveedor;
import com.proit.modelo.compras.SolicitudPago;
import com.proit.servicios.EventoService;
import com.proit.servicios.compras.FacturaCompraService;
import com.proit.servicios.compras.OrdenPagoService;
import com.proit.servicios.compras.PlanCuentaService;
import com.proit.servicios.compras.ProveedorService;
import com.proit.servicios.compras.SolicitudPagoService;
import com.proit.utils.Constantes;
import com.proit.utils.ExcelGeneratorOP;
import com.proit.utils.Utils;
import com.proit.vista.compras.solicitudes.SolicitudesPagoPage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.CustomTextFieldDouble;
import com.proit.wicket.components.EventoSearchAutoCompleteTextField;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class ConvertirAOPPage3Normal extends ConvertirAOPPage3{
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(ConvertirAOPPage3Normal.class.getName());
	
	private double importeParcial;
	private WebMarkupContainer facturasContainer;
	private CustomTextFieldDouble importeParcialInput;
	
	private Label noExistenFacturas;
	
	private List<String> nrosFacturasCompra;
	
	private OrdenPago ordenPago;
	
	private AutoCompleteTextField<String> eventoSearchAutoComplete;
	
	public ConvertirAOPPage3Normal(SolicitudPago solicitudPago){
		super(solicitudPago);
		
		ProveedorService proveedorService = new ProveedorService();
		Proveedor proveedor = (Proveedor) proveedorService.get(solicitudPago.getProveedor().getId());
		solicitudPago.setProveedor(proveedor);
		
		setearDefaultModel();
		
		ordenPago = (OrdenPago)ConvertirAOPPage3Normal.this.getDefaultModelObject();
		
		nrosFacturasCompra = new ArrayList<String>();
		
		for (FacturaSolicitudPago facturaSolicitudPago : solicitudPago.getListadoFacturas()) {
			nrosFacturasCompra.add(facturaSolicitudPago.getFacturaCompra().getNro());			
		}
		
		double totalFacturas = 0;
		FacturaCompraService facturaCompraService = new FacturaCompraService();
		List<FacturaCompra> listaFacturasCompra = facturaCompraService.getFacturas(solicitudPago.getProveedor().getRazonSocial(), null, true, null, null, nrosFacturasCompra);
		ordenPago.setListadoFacturas(listaFacturasCompra);
		
		//Selecciono todas las facturas por defecto
		for (FacturaCompra facturaCompra : listaFacturasCompra) {
			facturaCompra.setSeleccionada(true);
			if ( ! facturaCompra.isNotaCredito() ) {
				totalFacturas += facturaCompra.calculateTotal();
			} else {
				totalFacturas -= facturaCompra.calculateTotal();
			}
		}
		
		//Luego de setearDefaultModel() el importeFinal tiene la suma de los pagosSolicitudPago
		double totalPagosSolicitudPago = importeFinal;
		if (listaFacturasCompra.size()==1 
				&& Utils.round(totalFacturas,2)>Utils.round(totalPagosSolicitudPago,2)) { //Es Pago Parcial //Logica para saber si pago parcial (buscar este comentario)
			importeParcial = totalPagosSolicitudPago;
		}
		importeFinal = totalFacturas;
		
		armarCuadroObservaciones(solicitudPago, proveedor);
		
		crearForm();
		
		actualizarImportesFinalyParcial();
	}

	private void armarCuadroObservaciones(SolicitudPago solicitudPago, Proveedor proveedor) {
		String observaciones = solicitudPago.getObservaciones();
		WebMarkupContainer observacionesContainer = new WebMarkupContainer("observacionesContainer");
		Label observacionesLbl = new Label("observaciones", "Observaciones: " + observaciones);
		observacionesLbl.setVisible(observaciones!=null && !observaciones.isEmpty());
		Label importeLbl = new Label("importeSolicitud", "$ " + Utils.round2Decimals(importeParcial>0?importeParcial:importeFinal, locale) );
		Label tipoPagoLbl = new Label("tipoPago",importeParcial>0?"(Pago Parcial)":"(Pago Total)");
		String modosPagoStr = "Modos de pago disponibles: " + proveedor.getModosPagoString();
		Label modosPagoLbl = new Label("modosPago",modosPagoStr);
		observacionesContainer.add(importeLbl);
		observacionesContainer.add(tipoPagoLbl);
		observacionesContainer.add(observacionesLbl);
		observacionesContainer.add(modosPagoLbl);
		add(observacionesContainer);
	}
	
	private void crearForm() {
		FeedbackPanel feedback = new FeedbackPanel("feedback");
		
		//Facturas
		Label proveedorLbl = new Label("proveedor", solicitudPago.getProveedor().getRazonSocial());
		crearImporteFinalInput();
		importeFinalInput.setEnabled(false);
		
		importeParcialInput = new CustomTextFieldDouble("importeParcial", new PropertyModel<Double>(this, "importeParcial"));
		importeParcialInput.setOutputMarkupId(true);
		importeParcialInput.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(importeParcialInput);
            }
        });
		
		facturasContainer = crearFacturasDataView();
		
		//Formas de Pago
		WebMarkupContainer seccionEfectivo = new WebMarkupContainer("seccionEfectivo");
		WebMarkupContainer seccionCheques = new WebMarkupContainer("seccionCheques");
		WebMarkupContainer seccionTransferencias = new WebMarkupContainer("seccionTransferencias");
		WebMarkupContainer seccionTransferencia3ro = new WebMarkupContainer("seccionTransferencia3ro");
		WebMarkupContainer seccionTarjetaCredito = new WebMarkupContainer("seccionTarjetaCredito");
		
		final CustomTextFieldDouble importeEfectivoInput = crearImporteEfectivoInput();

		Label datosBcariosTransferencia = new Label("datosBcariosTransferencia",  Utils.generarDatosBancarios(solicitudPago.getProveedor().getCuitCuil(), solicitudPago.getProveedor().getCuentaBancaria()) );

		final CustomTextFieldDouble importeTransferencia3rosInput = crearImporteTransferencia3rosInput();
		
		final DropDownChoice<CobroAlternativo> cobroAlternativoDropDownChoice = crearCobroAlternativoDropDownChoice();
		
		final CustomTextFieldDouble importeTarjetaCreditoInput = crearImporteTarjetaCreditoInput();
		
		
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
			protected void onSubmit() {
				OrdenPagoService ordenPagoService = new OrdenPagoService();
				
				if ( ! validacionOK() ) {
					return;
				}
				
				ordenPago.setListadoPagos(obtenerListadoPagosPreparado(ordenPago));
				prepararListadoFacturas();
				
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
					nroOP = ordenPagoService.createOrUpdateOPNormal(ordenPago, importeParcial>0);
					
					SolicitudPagoService solicitudPagoService = new SolicitudPagoService();
					solicitudPago.setEstadoSolicitudPago(EstadoSolicitudPago.CUMPLIDA);
					solicitudPagoService.createOrUpdate(solicitudPago);
					
					String CBU = solicitudPago.getProveedor().getCuentaBancaria()!=null?solicitudPago.getProveedor().getCuentaBancaria().getCbu():null;
					String alias = solicitudPago.getProveedor().getCuentaBancaria()!=null?solicitudPago.getProveedor().getCuentaBancaria().getAlias():null;
					error = excelGenerator.generarExcel(ordenPago, solicitudPago.getProveedor().getRazonSocialConCUIT(), CBU, alias, locale);
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
				Pago pago = new Pago();
				pago.setModoPago(ModoPago.CHEQUE);
				pago.setOrdenPago(ordenPago);
				ordenPago.getListadoPagos().add(pago);
				target.add(chequesContainer);
			}
		});
		
		final WebMarkupContainer transferenciasContainer = new WebMarkupContainer("transferenciasContainer");
		transferenciasContainer.setOutputMarkupPlaceholderTag(true);

		seccionTransferencias.add(new AjaxFallbackLink<Pago>("agregarTransferencia") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				Pago pago = new Pago();
				pago.setModoPago(ModoPago.TRANSFERENCIA);
				pago.setOrdenPago(ordenPago);
				ordenPago.getListadoPagos().add(pago);
				target.add(transferenciasContainer);
			}
		});
		
		this.add(form);
		form.add(feedback.setOutputMarkupId(true));
		
		form.add(proveedorLbl);
		form.add(importeFinalInput);
		form.add(importeParcialInput);
		form.add(facturasContainer);
		
		seccionEfectivo.add(importeEfectivoInput);
		seccionTransferencias.add(datosBcariosTransferencia);
		addTransferenciasList(transferenciasContainer);
		seccionTransferencias.add(transferenciasContainer);
		seccionTransferencia3ro.add(importeTransferencia3rosInput);
		seccionTransferencia3ro.add(cobroAlternativoDropDownChoice);
		seccionTarjetaCredito.add(importeTarjetaCreditoInput);
		addChequesList(chequesContainer);
		seccionCheques.add(chequesContainer);
		
		form.add(seccionEfectivo);
		form.add(seccionTransferencias);
		form.add(seccionTransferencia3ro);
		form.add(seccionCheques);
		form.add(seccionTarjetaCredito);
		
		form.add(ajaxDatePicker);
		form.add(hoyLink);
		form.add(eventoSearchAutoComplete);
		form.add(planCuentaDropDownChoice);
		form.add(conceptoTextField);
		form.add(observacionesTextArea);
		form.add(solicitanteDropDownChoice);
		
		setearVisibilidadesModosPago(seccionEfectivo, seccionCheques, seccionTransferencias, seccionTransferencia3ro, seccionTarjetaCredito);
	}
	
	private boolean validacionOK() {
		boolean validacionOK = true;
		
		OrdenPagoService ordenPagoService = new OrdenPagoService();
		
		//Valido cantidad Facturas Seleccionadas
		int cantidadFactSeleccionadas = 0;
		double importeTotalNeto = 0;
		for (FacturaCompra factura: ordenPago.getListadoFacturas()){
			if (factura.isSeleccionada()){
				cantidadFactSeleccionadas ++;
				if ( ! factura.isNotaCredito() ) {
					importeTotalNeto += ordenPagoService.calculateTotalNetoFactura(factura);
				} else {
					importeTotalNeto -= ordenPagoService.calculateTotalNetoFactura(factura);
				}
			}
		}
		if (cantidadFactSeleccionadas==0) {
			validacionOK = informarError("Debe seleccionar al menos una factura.");
		} else if (cantidadFactSeleccionadas>Constantes.MAX_CANTIDAD_FACTURAS_POR_ORDEN_PERMITIDA) {
			validacionOK = informarError("Debe seleccionar como máximo " + Constantes.MAX_CANTIDAD_FACTURAS_POR_ORDEN_PERMITIDA + " facturas.");
		}
		
		if (importeFinal <= 0) {
			validacionOK = informarError("El Importe Final debe ser mayor a 0.");
		}				
		if (importeParcial < 0) {
			validacionOK = informarError("El Importe Parcial debe ser mayor o igual a 0.");
		}				
		if (importeParcial >= importeFinal) {
			validacionOK = informarError("El Importe Parcial debe ser menor al Importe Final.");
		}
		
		List<Pago> listadoPagos = obtenerListadoPagosPreparado(ordenPago);
		double totalPagos = ordenPagoService.calculateTotalPagos(listadoPagos);
		importeTotalNeto = importeParcial>0 ? 0 : importeTotalNeto;
		
		//Si se eligio importe parcial, se valida que el total de pagos sea dicho importe parcial, sino debe ser igual al importe total
		if ( importeParcial>0 && ! Utils.round2Decimals(totalPagos, locale).equals(Utils.round2Decimals(importeParcial, locale)) ) {
			String errorString = "El Total de Pagos debe ser igual al Importe Parcial indicado. "
								+ "Importe Parcial: $"+ Utils.round2Decimals(importeParcial, locale) + ". "
								+ "Total Pagos: $" + Utils.round2Decimals(totalPagos, locale) + ".";
			validacionOK = informarError(errorString);
		} else if ( importeParcial==0 && ! Utils.round2Decimals(totalPagos, locale).equals(Utils.round2Decimals(importeTotalNeto, locale)) ){
			String errorString = "El Total de Pagos debe ser igual al Importe Final indicado. "
					+ "Importe Total: $"+ Utils.round2Decimals(importeTotalNeto, locale) + ". "
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
		
		if ( ! ordenPagoService.cantidadTransferenciasPermitida(listadoPagos) ) {
			validacionOK = informarError("Se pueden cargar hasta " + Constantes.MAX_CANTIDAD_TRANSFERENCIAS_POR_ORDEN_PERMITIDA + " transferencias.");
		}
		
		//El importe de los cheques no hace falta validarlos porque ya vienen validados/corregidos (idem con importes transferencias)
		
		//Si se eligio importe de transferencia a terceros, se debe seleccionar un tercero, y si se elige un tercero el importe debe ser mayor a cero		
		if ( importeTransferencia3ros>0 && cobroAlternativo==null ) {
			validacionOK = informarError("El Importe de Transferencia a Terceros es mayor a Cero, pero no seleccionó ningun Tercero.");
		} else if ( importeTransferencia3ros==0 && cobroAlternativo!=null ) {
			validacionOK = informarError("Seleccionó un Tercero, por lo tanto el Importe de Transferencia a Terceros debe ser mayor a Cero.");
		}
		
		EventoService eventoService = new EventoService();
		String nombreEvento = Utils.getEventFromFullName(eventoSearchAutoComplete.getModelObject());
		String razonSocialCliente = Utils.getClientFromFullName(eventoSearchAutoComplete.getModelObject());
		if (nombreEvento == null || !eventoService.existsByClienteAndNombreEvento(razonSocialCliente, nombreEvento) ) {
			validacionOK = informarError("Debe ingresar un evento valido. Utilice la funcion autocompletado.");
		}
		
		return validacionOK;
		
	}
	
	private boolean informarError(String textoError) {
		error(textoError);
		return false;
	}

	private void setearVisibilidadesModosPago(WebMarkupContainer seccionEfectivo, WebMarkupContainer seccionCheques, WebMarkupContainer seccionTransferencias,
			WebMarkupContainer seccionTransferencia3ro, WebMarkupContainer seccionTarjetaCredito) {
		boolean efectivoVisible = false;
		boolean chequesVisible = false;
		boolean transferenciasVisible = false;
		boolean transferencia3roVisible = false;
		boolean tarjetaCreditoVisible = false;

		boolean usoEfectivo = false;
		boolean usoTarjeta = false;
		boolean usoCheque = false;
		boolean usoTransferencia = false;
		for (PagoSolicitudPago pagoSolicitudPago : solicitudPago.getListadoPagos()) {
			if (pagoSolicitudPago.isEfectivo()) {
				usoEfectivo = true;
			} else if (pagoSolicitudPago.isCheque()) {
				usoCheque = true;
			} else if (pagoSolicitudPago.isTarjetaCredito()) {
				usoTarjeta = true;
			} else if (pagoSolicitudPago.isTransferencia()) {
				usoTransferencia = true;
			}
		}			
		Proveedor proveedor = solicitudPago.getProveedor();
		efectivoVisible = proveedor.isModoEfectivo() || usoEfectivo;
		chequesVisible = proveedor.isModoCheque() || usoCheque;
		transferenciasVisible = proveedor.isModoTransferencia() || usoTransferencia;
		transferencia3roVisible = proveedor.tieneCobrosAlternativos();
		tarjetaCreditoVisible = proveedor.isModoTarjetaCredito() || usoTarjeta;
		
		seccionEfectivo.setVisible(efectivoVisible);
		seccionCheques.setVisible(chequesVisible);
		seccionTransferencias.setVisible(transferenciasVisible);
		seccionTransferencia3ro.setVisible(transferencia3roVisible);
		seccionTarjetaCredito.setVisible(tarjetaCreditoVisible);
	}
	
	private WebMarkupContainer crearFacturasDataView() {
		WebMarkupContainer facturasContainer = new WebMarkupContainer("facturasContainer");
		facturasContainer.setOutputMarkupPlaceholderTag(true);
		
		noExistenFacturas = new Label("noExistenFacturas","No existen facturas cargadas para el proveedor seleccionado.");
		
		DataView<FacturaCompra> horariosDataView = new DataView<FacturaCompra>("listaFacturas", getFacturasCompraDataProvider()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final Item<FacturaCompra> item) {
				OrdenPagoService ordenPagoService = new OrdenPagoService();
				Locale locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				item.add(new Label("nro",item.getModelObject().getNro()));
				item.add(new Label("fecha",dateFormat.format(item.getModelObject().getFecha().getTime())));
				String total = (item.getModelObject().isNotaCredito()?"-":"") + Utils.round2Decimals(item.getModelObject().calculateTotal(), locale);				
				item.add(new Label("total",total));
				final double totalNetoFactura = ordenPagoService.calculateTotalNetoFactura(item.getModelObject());
				String faltaPagar = item.getModelObject().getEstadoFactura().equals(EstadoFacturaCompra.PAGADA_PARCIAL) ? 
								" (Faltante: $" + Utils.round2Decimals(totalNetoFactura, locale) + ")"		:		"";
				item.add(new Label("estado",item.getModelObject().getEstadoFactura().getNombre() + faltaPagar));
				
				AjaxCheckBox checkbox = new AjaxCheckBox("checkbox",new PropertyModel<Boolean>(Model.of(item.getModelObject()), "seleccionada")){
					private static final long serialVersionUID = 1L;

					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						actualizarImportesFinalyParcial(target);
					}

				};
				
				item.add(checkbox);
				
				noExistenFacturas.setVisible(false);				
			}
		};
		
		facturasContainer.add(horariosDataView);
		facturasContainer.add(noExistenFacturas);
		return facturasContainer;
	}

	/**
	 * Solo uso las facturas que fueron seleccionadas desde la pagina. Las demás se descartan.
	 */
	private void prepararListadoFacturas() {
		FacturaCompraService facturaService = new FacturaCompraService();
		List<FacturaCompra> listaFacturasFinal = new ArrayList<FacturaCompra>();
		if (ordenPago.getListadoFacturas()!=null){
			for (FacturaCompra factura: ordenPago.getListadoFacturas()){
				if (factura.isSeleccionada()){
					listaFacturasFinal.add((FacturaCompra) facturaService.get(factura.getId()));
				}
			}
		}
		ordenPago.setListadoFacturas(listaFacturasFinal);
	}
	
	private IDataProvider<FacturaCompra> getFacturasCompraDataProvider() {
		IDataProvider<FacturaCompra> facturasCompraDataProvider = new IDataProvider<FacturaCompra>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void detach() {
			}

			@Override
			public Iterator<FacturaCompra> iterator(long first, long count) {
				return ordenPago.getListadoFacturas().iterator();
			}

			@Override
			public long size() {
				return ordenPago.getListadoFacturas().size();
			}

			@Override
			public IModel<FacturaCompra> model(FacturaCompra factura) {
				return new Model<FacturaCompra>(factura);
			}
        	
        };
		return facturasCompraDataProvider;
	}
	
	private void actualizarImportesFinalyParcial() {
		importeFinal = 0;
		List<FacturaCompra> listadoFacturas = ordenPago.getListadoFacturas();
		List<FacturaCompra> listadoFacturasFinal = new ArrayList<FacturaCompra>();
		for (FacturaCompra facturaCompra : listadoFacturas) {
			if (facturaCompra.isSeleccionada()){
				listadoFacturasFinal.add(facturaCompra);
			}
		}
		for (FacturaCompra facturaCompra : listadoFacturasFinal) {
			if ( facturaCompra.getTipoFactura() == null || ! facturaCompra.getTipoFactura().isNotaCredito() ) { 
				importeFinal += facturaCompra.calculateTotal();
			} else {
				importeFinal -= facturaCompra.calculateTotal();
			}
		}				
		importeParcialInput.setEnabled(listadoFacturasFinal.size()==1);
	}
	
	private void actualizarImportesFinalyParcial(AjaxRequestTarget target) {
		actualizarImportesFinalyParcial();
		target.add(importeFinalInput);
		target.add(importeParcialInput);
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