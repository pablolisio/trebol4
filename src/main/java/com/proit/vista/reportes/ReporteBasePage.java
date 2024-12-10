package com.proit.vista.reportes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.proit.modelo.compras.EstadoFacturaCompra;
import com.proit.modelo.compras.FacturaCompra;
import com.proit.servicios.compras.FacturaCompraService;
import com.proit.servicios.compras.OrdenPagoService;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.components.CalendarChoiceRenderer;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public abstract class ReporteBasePage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static final int DEUDAS_VS_PAGOS 			= 1;
	
	private int tipoReporte;
	
	protected Calendar mesDesdeSeleccionado;
	
	protected Calendar mesHastaSeleccionado;
	
	protected WebMarkupContainer container;
	
	private List<Calendar> listaMeses;
	
	private FacturaCompraService facturaService = new FacturaCompraService();
	private OrdenPagoService ordenPagoService = new OrdenPagoService();
	
	// TODO NO Ahora: ver si se puede lograr por javaScript omitir el uso de esta variable. Problema: no se llama
	// al setOnLoadCallback de Google al intentar refrezcar el reporte con los dropdowns de meses
	// utilizado porque Google Chart no lee mas de una vez el google.setOnLoadCallback(drawVisualization)
	private Label primeraVez;
	
	public ReporteBasePage(PageParameters parameters, int tipoReporte) {
		super(parameters);
		
		this.tipoReporte = tipoReporte;
				
		listaMeses = Utils.getListaMeses(-24, 0);
		
		mesDesdeSeleccionado = listaMeses.get(listaMeses.size()-12);
		mesHastaSeleccionado = listaMeses.get(listaMeses.size()-1);
		
		container = new WebMarkupContainer("dataContainer");
		container.setOutputMarkupPlaceholderTag(true);		
		add(container);
		
		container.add(new FeedbackPanel("feedback"));
		
		definirPrimeraVez();
		
		facturarOnLineMenu.setearMenuActivo(false, false, false, true);
		
		add(new Label("secondaryTitle",  getSecondaryTitle() ));
		
		mostrarAlertaEnPantallaSiCorresponde(parameters);
	}

	/**
	 * Utilizado porque al ingresar a una página de reporte se encontró un problema:
	 * No se llama al setOnLoadCallback de Google al intentar refrezcar el reporte con los dropdowns de meses
	 * utilizado porque Google Chart no lee mas de una vez el google.setOnLoadCallback(drawVisualization)
	 */
	private void definirPrimeraVez() {
		primeraVez = new Label("getPrimeraVez", "var primeraVez=true");
		primeraVez.setEscapeModelStrings(false); // do not HTML escape JavaScript code
		primeraVez.setOutputMarkupId(true);
		container.add(primeraVez);
	}
	
	protected void crearMesDropDownChoice(final boolean esDesde, final Label listado) {
		final IModel<Calendar> mesModel = Model.of(esDesde ? mesDesdeSeleccionado : mesHastaSeleccionado);
		
		DropDownChoice<Calendar> mesDropDownChoice = new DropDownChoice<Calendar>((esDesde?"mesDesde":"mesHasta"), mesModel, Utils.getListaMeses(-24, 0), new CalendarChoiceRenderer("MM / yyyy"));
		mesDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			protected void onUpdate(AjaxRequestTarget target) {
				
				if (mesesIngresadosOK(esDesde, mesModel.getObject())){
					if (esDesde) {
						mesDesdeSeleccionado = mesModel.getObject();
					} else {
						mesHastaSeleccionado = mesModel.getObject();
					}
					
					((IModel<String>) primeraVez.getDefaultModel()).setObject("var primeraVez=false");
					((IModel<String>) listado.getDefaultModel()).setObject(getListado(mesDesdeSeleccionado, mesHastaSeleccionado));
				}
				target.add(container);
            }

			private boolean mesesIngresadosOK(boolean esDesde, Calendar mesModificado) {
				boolean mesesIngresadosOK = true;
				if ( esDesde && mesModificado.getTimeInMillis() > mesHastaSeleccionado.getTimeInMillis()){
					error("Mes Desde debe ser menor a Mes Hasta.");
					mesesIngresadosOK = false;
				}else if ( ! esDesde && mesModificado.getTimeInMillis() < mesDesdeSeleccionado.getTimeInMillis()){
					error("Mes Desde debe ser menor a Mes Hasta.");
					mesesIngresadosOK = false;
				}				
				return mesesIngresadosOK;
			}
        });
		add(mesDropDownChoice);
	}
	
	/**
	 * Crea un label en la página html con el listadoJS (como String) listo para utilizar por la API de Google  
	 * @param nombreScript
	 * @param listadoJS
	 * @return
	 */
	protected Label crearListado(String nombreScript, String listadoJS) {
		Label script = new Label(nombreScript, listadoJS);
		script.setEscapeModelStrings(false); // do not HTML escape JavaScript code
		script.setOutputMarkupId(true);
		script.setVisible(listadoJS!=null);
		container.addOrReplace(script);
		return script;
	}
	
	/**
	 * Retorna el título del reporte
	 */
	private String getSecondaryTitle() {
		switch (tipoReporte) {
			case DEUDAS_VS_PAGOS:
				return "Deudas Vs Pagos";
			default:
				return "";
		}
	}
	
	/**
	 * Devuelve el listado (como String) que se va a utilizar para la API de Google Chart
	 * según el tipo de reporte.
	 * @param desde	Fecha desde seleccionada desde el dropdown
	 * @param hasta Fecha hasta seleccionada desde el dropdown
	 * @return
	 */
	protected String getListado(Calendar desde, Calendar hasta) {
		String listadoJS = "";

		switch (tipoReporte) {
			case DEUDAS_VS_PAGOS:
				// Ejemplo: var listadoDeudasVsPagos = [['Mes Impositivo', 'Deudas', 'Pagos']['Ene 2015', 10000, 6100]]
				listadoJS += "var listadoDeudasVsPagos = [['Mes Impositivo', 'Pagos ($)', 'Deudas ($)']";
				break;
		}
		
		DateFormat dateFormat = new SimpleDateFormat("MM / yyyy");
		Calendar mes = (Calendar) desde.clone();
		while ( mes.compareTo(hasta) <= 0 ) {
//			Calendar proximoMes = (Calendar) mes.clone();
//			proximoMes.add(Calendar.MONTH,1);
			
			double saldoDelMes = calcularMontoSaldo(mes);
			double totalPagado = calcularTotalPagado(mes, saldoDelMes);
			
			switch (tipoReporte) {
				case DEUDAS_VS_PAGOS:
					listadoJS += ",\n['"+dateFormat.format(mes.getTime())+"', " + totalPagado +", " + saldoDelMes + "]";
					break;
			}		
			
			mes.add(Calendar.MONTH,1);
		}
		
		listadoJS += "];";
		
		return listadoJS;
	}

	private double calcularMontoSaldo(Calendar mesImpositivo) {
		List<FacturaCompra> facturas = facturaService.calculateSaldoMesAMes(mesImpositivo);		
		Double monto = 0d;
		for (FacturaCompra factura : facturas){
			if (factura.getEstadoFactura().equals(EstadoFacturaCompra.PENDIENTE)) {
				if ( ! factura.isNotaCredito() ) {
					monto += factura.calculateTotal();
				} else {
					monto -= factura.calculateTotal();
				}
			} else if (factura.getEstadoFactura().equals(EstadoFacturaCompra.PAGADA_PARCIAL)) {
				if ( ! factura.isNotaCredito() ) {
					monto += ordenPagoService.calculateTotalNetoFactura(factura);
				} else {
					monto -= ordenPagoService.calculateTotalNetoFactura(factura);
				}
			}
		}
		return Utils.round(monto,2);
	}
	
	private double calcularTotalPagado(Calendar mesImpositivo, double saldoDelMes) {
		double sumaSubTotal = facturaService.calculateSum(null, null, false, mesImpositivo, "subtotal");
		double sumaIva = facturaService.calculateSum(null, null, false, mesImpositivo, "iva");
		double sumaPercIva = facturaService.calculateSum(null, null, false, mesImpositivo, "percIva");
		double sumaPercIibb = facturaService.calculateSum(null, null, false, mesImpositivo, "percIibb");
		double sumaPercGcias = facturaService.calculateSum(null, null, false, mesImpositivo, "percGcias");
		double sumaPercSUSS = facturaService.calculateSum(null, null, false, mesImpositivo, "percSUSS");
		double sumaOtrasPerc = facturaService.calculateSum(null, null, false, mesImpositivo, "otrasPerc");
		double sumaTotales = sumaSubTotal + sumaIva + sumaPercIva + sumaPercIibb + sumaPercGcias + sumaPercSUSS + sumaOtrasPerc;
		return Utils.round(sumaTotales - saldoDelMes,2);
	}
	
	
}
