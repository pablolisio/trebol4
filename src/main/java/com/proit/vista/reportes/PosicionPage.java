package com.proit.vista.reportes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.proit.servicios.EventoService;
import com.proit.servicios.compras.FacturaCompraService;
import com.proit.servicios.compras.OrdenPagoService;
import com.proit.servicios.compras.SolicitudPagoService;
import com.proit.servicios.ventas.CobranzaService;
import com.proit.servicios.ventas.ReportesVentasService;
import com.proit.utils.ExcelGeneratorPosicion;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.AJAXDownload;
import com.proit.wicket.components.CustomTextFieldDouble;

@AuthorizeInstantiation({"Administrador", "Desarrollador"})
public class PosicionPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private String totalSaldoAcreedor;
	@SuppressWarnings("unused")
	private String totalSaldoDeudor;
	@SuppressWarnings("unused")
	private String total;
	
	private double totalVentasXCobrarSinIVA = 0;
	private double pendientesFacturacionSinIVA = 0;
	private double chequesCartera = 0;
	private double totalCostosAPagarEventosConIVA = 0;
	private double solicitudesPagoSinProcesarConIVA = 0;
	private double facturasProvPendientesPagoConIVA = 0;
	private double chequesEmitidosDiferidos = 0;
	
	private double saldoBancoCitibank = 0;
	private double saldoBancoSantander = 0;
	private double saldoBancoCiudad = 0;
	private double otrosAcreedor = 0;
	private double tarjetasCredito = 0;
	private double sueldosAportesRetirosAdic = 0;
	private double totalCreditosTomados = 0;
	private double otrosGastosMensuales = 0;
	private double ivaAPagar = 0;
	private double otrosDeudor = 0;
	
	private CustomTextFieldDouble saldoBancoCitibankInput;
	private CustomTextFieldDouble saldoBancoSantanderInput;
	private CustomTextFieldDouble saldoBancoCiudadInput;
	private CustomTextFieldDouble otrosAcreedorInput;
	private CustomTextFieldDouble tarjetasCreditoInput;
	private CustomTextFieldDouble sueldosAportesRetirosAdicInput;
	private CustomTextFieldDouble totalCreditosTomadosInput;
	private CustomTextFieldDouble otrosGastosMensualesInput;
	private CustomTextFieldDouble ivaAPagarInput;
	private CustomTextFieldDouble otrosDeudorInput;
	
	private Label totalSaldoAcreedorLabel;
	private Label totalSaldoDeudorLabel;
	private Label totalLabel;
	
	private Locale locale;
	
	public PosicionPage(PageParameters parameters) {
		locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String fecha = dateFormat.format(Calendar.getInstance().getTime());
		add(new Label("fecha", fecha));
		
		obtenerDatos();
		
		Form<?> form = crearForm();
		
		recalcularTotales();
		
		agregarBotonExportarReporte(form, fecha);
		
		add(new FeedbackPanel("feedback"));
		
		facturarOnLineMenu.setearMenuActivo(false, false, false, true);
	}
	
	private void agregarBotonExportarReporte(Form<?> form, final String fecha) {
		final ExcelGeneratorPosicion excelGenerator = new ExcelGeneratorPosicion(getRuntimeConfigurationType());
		final String excelName = excelGenerator.getFileName();
		final AJAXDownload download = createAjaxDownload(excelName);
		AjaxLink<String> generarExcelLink = new AjaxLink<String>("generarExcel") { 
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				excelGenerator.generarExcel(fecha, totalVentasXCobrarSinIVA, pendientesFacturacionSinIVA, 
												saldoBancoCitibank, saldoBancoSantander, saldoBancoCiudad, chequesCartera, otrosAcreedor, 
												totalCostosAPagarEventosConIVA, solicitudesPagoSinProcesarConIVA, facturasProvPendientesPagoConIVA, 
												chequesEmitidosDiferidos, tarjetasCredito, sueldosAportesRetirosAdic, totalCreditosTomados, 
												otrosGastosMensuales, ivaAPagar, otrosDeudor);
				download.initiate(target);
			}
		};
		form.add(generarExcelLink);
		form.add(download);
	}

	private Form<?> crearForm() {
		Label totalVentasXCobrarSinIVALabel = new Label("totalVentasXCobrarSinIVA",Utils.round2Decimals(totalVentasXCobrarSinIVA, locale));
		Label pendientesFacturacionSinIVALabel = new Label("pendientesFacturacionSinIVA",Utils.round2Decimals(pendientesFacturacionSinIVA, locale));
		Label chequesCarteraLabel = new Label("chequesCartera",Utils.round2Decimals(chequesCartera, locale));
		Label totalCostosAPagarEventosConIVALabel = new Label("totalCostosAPagarEventosConIVA",Utils.round2Decimals(totalCostosAPagarEventosConIVA, locale));
		Label solicitudesPagoSinProcesarConIVALabel = new Label("solicitudesPagoSinProcesarConIVA",Utils.round2Decimals(solicitudesPagoSinProcesarConIVA, locale));
		Label facturasProvPendientesPagoConIVALabel = new Label("facturasProvPendientesPagoConIVA",Utils.round2Decimals(facturasProvPendientesPagoConIVA, locale));
		Label chequesEmitidosDiferidosLabel = new Label("chequesEmitidosDiferidos",Utils.round2Decimals(chequesEmitidosDiferidos, locale));

		saldoBancoCitibankInput = createCustomTextFieldInput(saldoBancoCitibankInput, "saldoBancoCitibank");
		saldoBancoSantanderInput = createCustomTextFieldInput(saldoBancoSantanderInput, "saldoBancoSantander");
		saldoBancoCiudadInput = createCustomTextFieldInput(saldoBancoCiudadInput, "saldoBancoCiudad");
		otrosAcreedorInput = createCustomTextFieldInput(otrosAcreedorInput, "otrosAcreedor");
		tarjetasCreditoInput = createCustomTextFieldInput(tarjetasCreditoInput, "tarjetasCredito");
		sueldosAportesRetirosAdicInput = createCustomTextFieldInput(sueldosAportesRetirosAdicInput, "sueldosAportesRetirosAdic");
		totalCreditosTomadosInput = createCustomTextFieldInput(totalCreditosTomadosInput, "totalCreditosTomados");
		otrosGastosMensualesInput = createCustomTextFieldInput(otrosGastosMensualesInput, "otrosGastosMensuales");
		ivaAPagarInput = createCustomTextFieldInput(ivaAPagarInput, "ivaAPagar");
		otrosDeudorInput = createCustomTextFieldInput(otrosDeudorInput, "otrosDeudor");
		
		totalSaldoAcreedorLabel = new Label("totalSaldoAcreedor",new PropertyModel<String>(this, "totalSaldoAcreedor"));
		totalSaldoAcreedorLabel.setOutputMarkupId(true);
		
		totalSaldoDeudorLabel = new Label("totalSaldoDeudor",new PropertyModel<String>(this, "totalSaldoDeudor"));
		totalSaldoDeudorLabel.setOutputMarkupId(true);
		
		totalLabel = new Label("total",new PropertyModel<String>(this, "total"));
		totalLabel.setOutputMarkupId(true);
		
		Form<?> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onValidate() {
				super.onValidate();
			}
			@Override
			protected void onSubmit() {
				//do nothing
			}

		};
		
		add(form);
		form.add(saldoBancoCitibankInput);
		form.add(saldoBancoSantanderInput);
		form.add(saldoBancoCiudadInput);
		form.add(otrosAcreedorInput);
		form.add(tarjetasCreditoInput);
		form.add(sueldosAportesRetirosAdicInput);
		form.add(totalCreditosTomadosInput);
		form.add(otrosGastosMensualesInput);
		form.add(ivaAPagarInput);
		form.add(otrosDeudorInput);
		
		form.add(totalVentasXCobrarSinIVALabel);
		form.add(pendientesFacturacionSinIVALabel);
		form.add(chequesCarteraLabel);
		form.add(totalCostosAPagarEventosConIVALabel);
		form.add(solicitudesPagoSinProcesarConIVALabel);
		form.add(facturasProvPendientesPagoConIVALabel);
		form.add(chequesEmitidosDiferidosLabel);
		
		form.add(totalSaldoAcreedorLabel);
		form.add(totalSaldoDeudorLabel);
		form.add(totalLabel);
		return form;
	}

	private CustomTextFieldDouble createCustomTextFieldInput(CustomTextFieldDouble input, String nombre) {
		input = new CustomTextFieldDouble(nombre, new PropertyModel<Double>(this, nombre));
		input.setOutputMarkupId(true);
		input.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				actualizarImportes(target);
            }
        });
		return input;
	}
	
	private void obtenerDatos() {
		ReportesVentasService reportesVentasService = new ReportesVentasService();
		CobranzaService cobranzaService = new CobranzaService();
		EventoService eventoService = new EventoService();
		SolicitudPagoService solicitudPagoService = new SolicitudPagoService();
		FacturaCompraService facturaCompraService = new FacturaCompraService();
		OrdenPagoService ordenPagoService = new OrdenPagoService();
		
		totalVentasXCobrarSinIVA = reportesVentasService.calculateSumDeudasPorCliente("subtotal_anio_actual");
		pendientesFacturacionSinIVA = reportesVentasService.calculateSumDeudasPorCliente("subtotal_pendiente");
		chequesCartera = cobranzaService.getTotalChequesEnCartera();
		totalCostosAPagarEventosConIVA = eventoService.calculateSumTotalesPorEvento("pendiente_total", null, false, isUsuarioLogueadoRolAdministrador()||isUsuarioLogueadoRolDesarrollador());
		solicitudesPagoSinProcesarConIVA = solicitudPagoService.getTotalSolicitudesPagoPendientes();
		facturasProvPendientesPagoConIVA = facturaCompraService.calculateSumReporteTotalesPorProveedor("total_deuda");
		chequesEmitidosDiferidos = ordenPagoService.getTotalPresupuestosBanco(null, false, false);
	}

	private void recalcularTotales() {
		Double totalSaldoAcreedorDouble = totalVentasXCobrarSinIVA + pendientesFacturacionSinIVA + saldoBancoCitibank + saldoBancoSantander + saldoBancoCiudad + chequesCartera + otrosAcreedor;
		Double totalSaldoDeudorDouble = totalCostosAPagarEventosConIVA + solicitudesPagoSinProcesarConIVA + facturasProvPendientesPagoConIVA + chequesEmitidosDiferidos + tarjetasCredito + sueldosAportesRetirosAdic + totalCreditosTomados + otrosGastosMensuales + ivaAPagar + otrosDeudor;
		Double totalDouble = totalSaldoAcreedorDouble - totalSaldoDeudorDouble;
		totalSaldoAcreedor 	= Utils.round2Decimals(totalSaldoAcreedorDouble, locale);
		totalSaldoDeudor 	= Utils.round2Decimals(totalSaldoDeudorDouble, locale);
		total 				= Utils.round2Decimals(totalDouble, locale);
	}
	
	private void actualizarImportes(AjaxRequestTarget target) {
		recalcularTotales();
		target.add(saldoBancoCitibankInput);
		target.add(saldoBancoSantanderInput);
		target.add(saldoBancoCiudadInput);
		target.add(otrosAcreedorInput);
		target.add(tarjetasCreditoInput);
		target.add(sueldosAportesRetirosAdicInput);
		target.add(totalCreditosTomadosInput);
		target.add(otrosGastosMensualesInput);
		target.add(ivaAPagarInput);
		target.add(otrosDeudorInput);
		
		target.add(totalSaldoAcreedorLabel);
		target.add(totalSaldoDeudorLabel);
		target.add(totalLabel);
	}

}
