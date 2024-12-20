package com.proit.vista.compras.solicitudes.conversion;

import java.util.ArrayList;
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
import com.proit.modelo.TipoFactura;
import com.proit.modelo.Usuario;
import com.proit.modelo.compras.CobroAlternativo;
import com.proit.modelo.compras.EstadoFacturaCompra;
import com.proit.modelo.compras.EstadoSolicitudPago;
import com.proit.modelo.compras.FacturaCompra;
import com.proit.modelo.compras.OrdenPago;
import com.proit.modelo.compras.Pago;
import com.proit.modelo.compras.PagoSolicitudPago;
import com.proit.modelo.compras.PlanCuenta;
import com.proit.modelo.compras.Proveedor;
import com.proit.modelo.compras.SolicitudPago;
import com.proit.servicios.EventoService;
import com.proit.servicios.compras.OrdenPagoService;
import com.proit.servicios.compras.PlanCuentaService;
import com.proit.servicios.compras.ProveedorService;
import com.proit.servicios.compras.SolicitudPagoService;
import com.proit.utils.Constantes;
import com.proit.utils.ExcelGeneratorOP;
import com.proit.utils.Utils;
import com.proit.vista.compras.solicitudes.SolicitudesPagoPage;
import com.proit.wicket.components.CustomTextFieldDouble;
import com.proit.wicket.components.EventoSearchAutoCompleteTextField;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class ConvertirAOPPage3CPySF extends ConvertirAOPPage3{
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(ConvertirAOPPage3CPySF.class.getName());
	
	private AutoCompleteTextField<String> eventoSearchAutoComplete;
	
	public ConvertirAOPPage3CPySF(SolicitudPago solicitudPago){
		super(solicitudPago);
		
		ProveedorService proveedorService = new ProveedorService();
		Proveedor proveedor = (Proveedor) proveedorService.get(solicitudPago.getProveedor().getId());
		solicitudPago.setProveedor(proveedor);
		
		setearDefaultModel();
		
		setearOrdenPago();
		
		armarCuadroObservaciones(solicitudPago, proveedor);
		
		crearForm();
	}

	private void armarCuadroObservaciones(SolicitudPago solicitudPago, Proveedor proveedor) {
		String observaciones = solicitudPago.getObservaciones();
		WebMarkupContainer observacionesContainer = new WebMarkupContainer("observacionesContainer");
		Label observacionesLbl = new Label("observaciones", "Observaciones: " + observaciones);
		observacionesLbl.setVisible(observaciones!=null && !observaciones.isEmpty());
		Label importeLbl = new Label("importeSolicitud", "$ " + Utils.round2Decimals(importeFinal, locale) );
		String modosPagoStr = "Modos de pago disponibles: " + proveedor.getModosPagoString();
		Label modosPagoLbl = new Label("modosPago",modosPagoStr);
		observacionesContainer.add(importeLbl);
		observacionesContainer.add(observacionesLbl);
		observacionesContainer.add(modosPagoLbl);
		add(observacionesContainer);
	}
	
	private void setearOrdenPago() {		//Este metodo tb esta en RegistrarOrdenPagoCPySFPage4
		OrdenPago ordenPago = (OrdenPago)ConvertirAOPPage3CPySF.this.getDefaultModelObject();
		double total = importeFinal;
		double subtotal = Utils.getSubtotalFromTotal(total, TipoFactura.TIPO_A); //Siempre usa esto como Factura A (*)
		double iva = Utils.getIvaFromTotal(total, TipoFactura.TIPO_A);
		
		//Debo crear una factura ficticia con un nro fact valido (porq tb hay que crear entrada en factura-ordenPago)
		FacturaCompra factura = new FacturaCompra();
		factura.setEstadoFactura(EstadoFacturaCompra.PENDIENTE);
		factura.setNro(Constantes.PREFIX_NRO_FACTURA_SF +ordenPago.getNro());
		factura.setFecha(Utils.firstMillisecondOfDay(Calendar.getInstance()));
		factura.setTipoFactura(TipoFactura.TIPO_A); 								//Siempre usa esto como Factura A (*)
		factura.setSubtotal(subtotal);
		factura.setIva(iva);
		factura.setPercIva(0);
		factura.setPercIibb(0);
		factura.setPercGcias(0);
		factura.setPercSUSS(0);
		factura.setOtrasPerc(0);
		//Por defecto el mesImpositivo es el mes actual
		Calendar mesImpositivo = Calendar.getInstance();
		mesImpositivo.set(Calendar.DAY_OF_MONTH, 1);
		mesImpositivo = Utils.firstMillisecondOfDay(mesImpositivo);
		factura.setMesImpositivo(mesImpositivo);
		factura.setBorrado(false);
		factura.setProveedor(solicitudPago.getProveedor());
		List<FacturaCompra> listadoFacturas = new ArrayList<FacturaCompra>();
		listadoFacturas.add(factura);
		ordenPago.setListadoFacturas(listadoFacturas);
	}
	
	private void crearForm() {
		FeedbackPanel feedback = new FeedbackPanel("feedback");
		
		//Importe Final a Pagar
		Label proveedorLbl = new Label("proveedor", solicitudPago.getProveedor().getRazonSocial());
		crearImporteFinalInput();
				
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
			protected void onValidate() {
				super.onValidate();
				boolean validacionOK = true;
				
				OrdenPagoService ordenPagoService = new OrdenPagoService();
				OrdenPago ordenPago = (OrdenPago)ConvertirAOPPage3CPySF.this.getDefaultModelObject();
				
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
				
				if ( ! ordenPagoService.cantidadTransferenciasPermitida(listadoPagos) ) {
					validacionOK = informarError("Se pueden cargar hasta " + Constantes.MAX_CANTIDAD_TRANSFERENCIAS_POR_ORDEN_PERMITIDA + " transferencias.");
				}
				
				//El importe de los cheques no hace falta validarlos porque ya vienen validados/corregidos (idem con importes transferencias)

				//Si se eligio importe de transferencia a terceros, se debe seleccionar un tercero, y si se elige un tercero el importe debe ser mayor a cero
				String tercero = cobroAlternativoDropDownChoice.getValue().toString();

				if ( importeTransferencia3ros>0 && tercero.isEmpty() ) {
					validacionOK = informarError("El Importe de Transferencia a Terceros es mayor a Cero, pero no seleccionó ningun Tercero.");
				} else if ( importeTransferencia3ros==0 && ! tercero.isEmpty() ) {
					validacionOK = informarError("Seleccionó un Tercero, por lo tanto el Importe de Transferencia a Terceros debe ser mayor a Cero.");
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
				OrdenPago ordenPago = (OrdenPago)ConvertirAOPPage3CPySF.this.getDefaultModelObject();
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
					nroOP = ordenPagoService.createOrUpdateOPCPySF(ordenPago);
					
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
				OrdenPago ordenPago = (OrdenPago)ConvertirAOPPage3CPySF.this.getDefaultModelObject();
				Pago pago = new Pago();
				pago.setModoPago(ModoPago.CHEQUE);
				pago.setOrdenPago(ordenPago);
				((OrdenPago)ConvertirAOPPage3CPySF.this.getDefaultModelObject()).getListadoPagos().add(pago);
				target.add(chequesContainer);
			}
		});
		
		final WebMarkupContainer transferenciasContainer = new WebMarkupContainer("transferenciasContainer");
		transferenciasContainer.setOutputMarkupPlaceholderTag(true);

		seccionTransferencias.add(new AjaxFallbackLink<Pago>("agregarTransferencia") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				OrdenPago ordenPago = (OrdenPago)ConvertirAOPPage3CPySF.this.getDefaultModelObject();
				Pago pago = new Pago();
				pago.setModoPago(ModoPago.TRANSFERENCIA);
				pago.setOrdenPago(ordenPago);
				((OrdenPago)ConvertirAOPPage3CPySF.this.getDefaultModelObject()).getListadoPagos().add(pago);
				target.add(transferenciasContainer);
			}
		});
		
		this.add(form);
		form.add(feedback.setOutputMarkupId(true));
		
		form.add(proveedorLbl);
		form.add(importeFinalInput);
		
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