package com.proit.vista.reportes.compras;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.proit.modelo.compras.FacturaCompra;
import com.proit.servicios.compras.FacturaCompraService;
import com.proit.utils.ExcelGeneratorSubdiarioCompras;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.AJAXDownload;
import com.proit.wicket.components.CalendarChoiceRenderer;
import com.proit.wicket.dataproviders.FacturasCompraDataProvider;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class SubdiarioComprasPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	private Label saldoProveedoresLabel;
	
	private Calendar añoSeleccionado;	
	private Calendar mesSeleccionado;
	
	private FacturaCompraService facturaService;
	
	private Locale locale;
	
//	private OrdenPagoService ordenPagoService;
		
	public SubdiarioComprasPage(final PageParameters parameters) {
		super(parameters);
		
		locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
		
		facturaService = new FacturaCompraService();
//		ordenPagoService = new OrdenPagoService();
		
		WebMarkupContainer container = new WebMarkupContainer("dataContainer");
		container.setOutputMarkupPlaceholderTag(true);
		add(container);
		
		crearAñoDropDownChoice(container);
		
		crearMesDropDownChoice(container);
		
		agregarBotonExportarReporte();
		
		addFacturasPorMesImpositivo(container);
		
		facturarOnLineMenu.setearMenuActivo(false, false, false, true);
		
		mostrarAlertaEnPantallaSiCorresponde(parameters);
	}
	
	private void agregarBotonExportarReporte() {
		final ExcelGeneratorSubdiarioCompras excelGenerator = new ExcelGeneratorSubdiarioCompras(getRuntimeConfigurationType());
		final AJAXDownload download = createAjaxDownload(excelGenerator.getFileName());
		AjaxLink<FacturaCompra> generarExcelLink = new AjaxLink<FacturaCompra>("generarExcel") {			
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				Calendar mesImpositivoInicio = null;
				Calendar mesImpositivoFin = null;
				if (mesSeleccionado!=null) {
					mesImpositivoInicio = Calendar.getInstance();
					mesImpositivoInicio.set(Calendar.YEAR, añoSeleccionado.get(Calendar.YEAR));
					mesImpositivoInicio.set(Calendar.MONTH, mesSeleccionado.get(Calendar.MONTH));
					mesImpositivoInicio.set(Calendar.DAY_OF_MONTH, 1);
					mesImpositivoInicio = Utils.firstMillisecondOfDay(mesImpositivoInicio);
					
					mesImpositivoFin = (Calendar) mesImpositivoInicio.clone(); //no le sumo nada: mesImpositivoInicio=mesImpositivoFin
				} else {
					mesImpositivoInicio = Calendar.getInstance();
					mesImpositivoInicio.set(Calendar.YEAR, añoSeleccionado.get(Calendar.YEAR));
					mesImpositivoInicio.set(Calendar.MONTH, mesImpositivoInicio.getMinimum(Calendar.MONTH));
					mesImpositivoInicio.set(Calendar.DAY_OF_MONTH, 1);
					mesImpositivoInicio = Utils.firstMillisecondOfDay(mesImpositivoInicio);
					
					mesImpositivoFin = (Calendar) mesImpositivoInicio.clone();
					mesImpositivoFin.add(Calendar.YEAR, 1);
					mesImpositivoFin.add(Calendar.DAY_OF_YEAR, -1);
				}
				ArrayList<FacturaCompra> listaFacturas = (ArrayList<FacturaCompra>) facturaService.getFacturas(null, null, false, mesImpositivoInicio, mesImpositivoFin, null);
				excelGenerator.generarExcel(listaFacturas, añoSeleccionado, mesSeleccionado);
				download.initiate(target);
			}
		};
		add(generarExcelLink);
		add(download);
	}
	
	private void crearAñoDropDownChoice(final WebMarkupContainer container) {
		List<Calendar> listaAños = Utils.getListaAños(-3, 0);
		añoSeleccionado = listaAños.get(listaAños.size()-1);
		
		final IModel<Calendar> añoModel = Model.of(añoSeleccionado);
		
		DropDownChoice<Calendar> añoDropDownChoice = new DropDownChoice<Calendar>("anio", añoModel, listaAños, new CalendarChoiceRenderer("yyyy"));
		añoDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;

			protected void onUpdate(AjaxRequestTarget target) {
				añoSeleccionado = añoModel.getObject();
				String saldoProvStr = Utils.round2Decimals(facturaService.calculateSaldoProveedoresAnual(añoSeleccionado), locale);
				saldoProveedoresLabel.setDefaultModelObject("$"+saldoProvStr);
				target.add(saldoProveedoresLabel);
				target.add(container);
            }
        });
		add(añoDropDownChoice);
	}
	
	private void crearMesDropDownChoice(final WebMarkupContainer container) {
		List<Calendar> listaMeses = Utils.getListaMesesAnioCompleto();
		mesSeleccionado = Utils.getMesActualFromListaMeses(listaMeses);
		final IModel<Calendar> mesModel = Model.of(mesSeleccionado);
		
		DropDownChoice<Calendar> mesDropDownChoice = new DropDownChoice<Calendar>("mes", mesModel, listaMeses, new CalendarChoiceRenderer("MM"));
		mesDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;

			protected void onUpdate(AjaxRequestTarget target) {
				mesSeleccionado = mesModel.getObject();
				target.add(container);
            }

        });
		mesDropDownChoice.setNullValid(true);
		add(mesDropDownChoice);
	}
	
	private void addFacturasPorMesImpositivo(WebMarkupContainer container) {
			
		IDataProvider<Calendar> mesesDataProvider = getMesesImpositivosProvider();
		
		DataView<Calendar> mesesDataView = new DataView<Calendar>("listaMesesImpositivos", mesesDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<Calendar> item) {
				Calendar mesImpositivo = item.getModelObject();
				DateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
				item.add(new Label("mesImpositivo", dateFormat.format(mesImpositivo.getTime())));
				
				IDataProvider<FacturaCompra> facturasDataProvider = getFacturasProvider(mesImpositivo, (Calendar) mesImpositivo.clone()); //mismo mes
				
				DataView<FacturaCompra> facturasDataView = new DataView<FacturaCompra>("listaFacturas", facturasDataProvider) {
					private static final long serialVersionUID = 1L;
					
					@Override
					protected void populateItem(Item<FacturaCompra> item) {
						DateFormat dateFormatFecha = new SimpleDateFormat("dd-MM-yyyy");
						String fecha = dateFormatFecha.format(item.getModelObject().getFecha().getTime());
//						double totalNetoFactura = ordenPagoService.calculateTotalNetoFactura(item.getModelObject());
//						String faltaPagar = item.getModelObject().getEstadoFactura().equals(EstadoFacturaCompra.PAGADA_PARCIAL) ? 
//								" (Faltante: $" + Utils.round2Decimals(totalNetoFactura, locale) + ")"		:		"";
						item.add(new Label("fecha", fecha));
						item.add(new Label("proveedor", item.getModelObject().getProveedor().getRazonSocialConCUIT()));
						item.add(new Label("tipo", item.getModelObject().getTipoFactura().getNombre()));
						item.add(new Label("nro", item.getModelObject().getNro()));				
//						item.add(new Label("estado", item.getModelObject().getEstadoFactura().getNombre() + faltaPagar));
						
						String subtotal = item.getModelObject().isNotaCredito() ? "$-" : "$";
						subtotal += Utils.round2Decimals(item.getModelObject().getSubtotal(), locale);
						String iva = item.getModelObject().isNotaCredito() ? "$-" : "$";
						iva += Utils.round2Decimals(item.getModelObject().getIva(), locale);
						String perciva = item.getModelObject().isNotaCredito() ? "$-" : "$";
						perciva += Utils.round2Decimals(item.getModelObject().getPercIva(), locale);
						String perciibb = item.getModelObject().isNotaCredito() ? "$-" : "$";
						perciibb += Utils.round2Decimals(item.getModelObject().getPercIibb(), locale);						
						String percgcias = item.getModelObject().isNotaCredito() ? "$-" : "$";
						percgcias += Utils.round2Decimals(item.getModelObject().getPercGcias(), locale);						
						String percsuss = item.getModelObject().isNotaCredito() ? "$-" : "$";
						percsuss += Utils.round2Decimals(item.getModelObject().getPercSUSS(), locale);						
						String otrasperc = item.getModelObject().isNotaCredito() ? "$-" : "$";
						otrasperc += Utils.round2Decimals(item.getModelObject().getOtrasPerc(), locale);						
						String total = item.getModelObject().isNotaCredito() ? "$-" : "$";
						total += Utils.round2Decimals(item.getModelObject().calculateTotal(), locale);
						item.add(new Label("subtotal", subtotal));
						item.add(new Label("iva", iva));
						item.add(new Label("perciva", perciva));
						item.add(new Label("perciibb", perciibb));
						item.add(new Label("percgcias", percgcias));
						item.add(new Label("percsuss", percsuss));
						item.add(new Label("otrasperc", otrasperc));
						item.add(new Label("total", total));
					}
				};
				double sumaSubTotal = facturaService.calculateSum(null, null, false, mesImpositivo, "subtotal");
				double sumaIva = facturaService.calculateSum(null, null, false, mesImpositivo, "iva");
				double sumaPercIva = facturaService.calculateSum(null, null, false, mesImpositivo, "percIva");
				double sumaPercIibb = facturaService.calculateSum(null, null, false, mesImpositivo, "percIibb");
				double sumaPercGcias = facturaService.calculateSum(null, null, false, mesImpositivo, "percGcias");
				double sumaPercSUSS = facturaService.calculateSum(null, null, false, mesImpositivo, "percSUSS");
				double sumaOtrasPerc = facturaService.calculateSum(null, null, false, mesImpositivo, "otrasPerc");
				double sumaTotales = sumaSubTotal + sumaIva + sumaPercIva + sumaPercIibb + sumaPercGcias + sumaPercSUSS + sumaOtrasPerc;
				double saldoDelMes = facturaService.calculateSaldoPendiente(null, null, false, mesImpositivo);
				double totalPagado = sumaTotales - saldoDelMes;
				
				Label sumaSubTotalLabel = new Label("sumaSubTotal", "$" + Utils.round2Decimals(sumaSubTotal, locale));
				Label sumaIvaLabel = new Label("sumaIva", "$" + Utils.round2Decimals(sumaIva, locale));
				Label sumaPercIvaLabel = new Label("sumaPercIva", "$" + Utils.round2Decimals(sumaPercIva, locale));
				Label sumaPercIibbLabel = new Label("sumaPercIibb", "$" + Utils.round2Decimals(sumaPercIibb, locale));
				Label sumaPercGciasLabel = new Label("sumaPercGcias", "$" + Utils.round2Decimals(sumaPercGcias, locale));
				Label sumaPercSUSSLabel = new Label("sumaPercSUSS", "$" + Utils.round2Decimals(sumaPercSUSS, locale));
				Label sumaOtrasPercLabel = new Label("sumaOtrasPerc", "$" + Utils.round2Decimals(sumaOtrasPerc, locale));
				Label sumaTotalesLabel = new Label("sumaTotales", "$" + Utils.round2Decimals(sumaTotales, locale));
				Label totalPagadoLabel = new Label("totalPagado", "$" + Utils.round2Decimals(totalPagado, locale));
				Label saldoDelMesLabel = new Label("saldoDelMes", "$" + Utils.round2Decimals(saldoDelMes, locale));
								
				item.add(facturasDataView);
				item.add(sumaSubTotalLabel);
				item.add(sumaIvaLabel);
				item.add(sumaPercIvaLabel);
				item.add(sumaPercIibbLabel);
				item.add(sumaPercGciasLabel);
				item.add(sumaPercSUSSLabel);
				item.add(sumaOtrasPercLabel);
				item.add(sumaTotalesLabel);
				item.add(totalPagadoLabel);
				item.add(saldoDelMesLabel);
				
			}
		};
		
		String saldoProvStr = Utils.round2Decimals(facturaService.calculateSaldoProveedoresAnual(añoSeleccionado), locale);
		saldoProveedoresLabel = new Label("saldoProveedores", "$"+saldoProvStr);
		saldoProveedoresLabel.setOutputMarkupId(true);
				
		container.add(mesesDataView);
		container.add(saldoProveedoresLabel);
	}
	
	private IDataProvider<FacturaCompra> getFacturasProvider(Calendar mesImpositivoInicio, Calendar mesImpositivoFin) {
		Date date = null;
		return new FacturasCompraDataProvider(new Model<String>(), Model.of(date), Model.of(false), mesImpositivoInicio, mesImpositivoFin);
	}
	
	private IDataProvider<Calendar> getMesesImpositivosProvider() {
		IDataProvider<Calendar> mesesImpositivosDataProvider = new IDataProvider<Calendar>() {
			private static final long serialVersionUID = 1L;
			
			private List<Calendar> listaMesesImpositivos ;
			
			@Override
			public void detach() {
			}

			@Override
			public Iterator<Calendar> iterator(long first, long count) {
				return listaMesesImpositivos.iterator();
			}

			@Override
			public long size() {
				listaMesesImpositivos = getListadoMesesImpositivos();
				return listaMesesImpositivos.size();
			}

			@Override
			public IModel<Calendar> model(Calendar cal) {
				return new Model<Calendar>(cal);
			}
			
			private List<Calendar> getListadoMesesImpositivos(){
				List<Calendar> listaMesesImpositivos = new ArrayList<Calendar>();
				
				if (mesSeleccionado!=null) {
					Calendar mesImpositivo = Calendar.getInstance();
					mesImpositivo.set(Calendar.YEAR, añoSeleccionado.get(Calendar.YEAR));
					mesImpositivo.set(Calendar.MONTH, mesSeleccionado.get(Calendar.MONTH));
					mesImpositivo.set(Calendar.DAY_OF_MONTH, 1);
					mesImpositivo = Utils.firstMillisecondOfDay(mesImpositivo);

					listaMesesImpositivos.add(mesImpositivo);
				} else {
					int mesLoop;
					if (añoSeleccionado.get(Calendar.YEAR)==Calendar.getInstance().get(Calendar.YEAR)) {
						mesLoop = Calendar.getInstance().get(Calendar.MONTH);
					} else {
						mesLoop = 11;
					}
					
					for (int i = 0 ; i<=mesLoop; i++) {
						Calendar mesImpositivo = Calendar.getInstance();
						mesImpositivo.set(Calendar.YEAR, añoSeleccionado.get(Calendar.YEAR));
						mesImpositivo.set(Calendar.MONTH, i);
						mesImpositivo.set(Calendar.DAY_OF_MONTH, 1);
						mesImpositivo = Utils.firstMillisecondOfDay(mesImpositivo);
	
						listaMesesImpositivos.add(mesImpositivo);
					}
				}
				return listaMesesImpositivos;
			}
        	
        };
		return mesesImpositivosDataProvider;
	}

}
