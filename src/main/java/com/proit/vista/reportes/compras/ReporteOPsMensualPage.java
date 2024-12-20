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
import com.proit.modelo.compras.FacturaCompraOrdenPago;
import com.proit.modelo.compras.OrdenPago;
import com.proit.modelo.compras.Proveedor;
import com.proit.servicios.compras.OrdenPagoService;
import com.proit.utils.ExcelGeneratorOPsMensual;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.AJAXDownload;
import com.proit.wicket.components.CalendarChoiceRenderer;
import com.proit.wicket.dataproviders.FacturaOrdenPagoDataProvider;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class ReporteOPsMensualPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	private Calendar añoSeleccionado;
	private Calendar mesSeleccionado;
	
	private Label totalAnualLabel;
	
	private OrdenPagoService ordenPagoService;
	
	private Locale locale;
		
	public ReporteOPsMensualPage(final PageParameters parameters) {
		super(parameters);
		
		ordenPagoService = new OrdenPagoService();
		locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
		
		WebMarkupContainer container = new WebMarkupContainer("dataContainer");
		container.setOutputMarkupPlaceholderTag(true);
		add(container);
		
		crearAñoDropDownChoice(container);
		
		crearMesDropDownChoice(container);
		
		agregarBotonExportarReporte();
		
		addOPsPorMes(container);
		
		facturarOnLineMenu.setearMenuActivo(false, false, false, true);
		
		mostrarAlertaEnPantallaSiCorresponde(parameters);
	}
	
	private void agregarBotonExportarReporte() {
		final ExcelGeneratorOPsMensual excelGenerator = new ExcelGeneratorOPsMensual(getRuntimeConfigurationType());
		final AJAXDownload download = createAjaxDownload(excelGenerator.getFileName());
		AjaxLink<FacturaCompra> generarExcelLink = new AjaxLink<FacturaCompra>("generarExcel") {			
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				Calendar mesInicio = null;
				Calendar mesFin = null;
				if (mesSeleccionado!=null) {
					mesInicio = Calendar.getInstance();
					mesInicio.set(Calendar.YEAR, añoSeleccionado.get(Calendar.YEAR));
					mesInicio.set(Calendar.MONTH, mesSeleccionado.get(Calendar.MONTH));
					mesInicio.set(Calendar.DAY_OF_MONTH, 1);
					mesInicio = Utils.firstMillisecondOfDay(mesInicio);
					
					mesFin = (Calendar) mesInicio.clone();
					mesFin.add(Calendar.MONTH, 1);
				} else {
					mesInicio = Calendar.getInstance();
					mesInicio.set(Calendar.YEAR, añoSeleccionado.get(Calendar.YEAR));
					mesInicio.set(Calendar.MONTH, mesInicio.getMinimum(Calendar.MONTH));
					mesInicio.set(Calendar.DAY_OF_MONTH, 1);
					mesInicio = Utils.firstMillisecondOfDay(mesInicio);
					
					mesFin = (Calendar) mesInicio.clone();
					mesFin.add(Calendar.YEAR, 1);
				}
				ArrayList<FacturaCompraOrdenPago> listaOPs = (ArrayList<FacturaCompraOrdenPago>) ordenPagoService.getListaFacturaOrdenPago(null, null, null, false, false, false, mesInicio, mesFin, null);
				excelGenerator.generarExcel(listaOPs, añoSeleccionado, mesSeleccionado, ordenPagoService);
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
				
				double totalAnual = ordenPagoService.calculateSumTotalPagosAnual(añoSeleccionado);
				String totalAnualStr = Utils.round2Decimals(totalAnual, locale);
				totalAnualLabel.setDefaultModelObject("$"+totalAnualStr);
				
				target.add(container);
				target.add(totalAnualLabel);
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
	
	private void addOPsPorMes(WebMarkupContainer container) {
		IDataProvider<Calendar> mesesDataProvider = getMesesProvider();
		
		DataView<Calendar> mesesDataView = new DataView<Calendar>("listaMeses", mesesDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<Calendar> item) {
				Calendar mes = item.getModelObject();
				DateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
				item.add(new Label("mes", dateFormat.format(mes.getTime())));
				
				IDataProvider<FacturaCompraOrdenPago> facturaOrdenPagoDataProvider = getFacturaOrdenPagoProvider(mes);
				
				DataView<FacturaCompraOrdenPago> dataView = new DataView<FacturaCompraOrdenPago>("listaFacturaOrdenPago", facturaOrdenPagoDataProvider) {
					private static final long serialVersionUID = 1L;
					
					@Override
					protected void populateItem(Item<FacturaCompraOrdenPago> item) {
						DateFormat dateFormatFecha = new SimpleDateFormat("dd-MM-yyyy");
						OrdenPago ordenPago = item.getModelObject().getOrdenPago();
						String fecha = dateFormatFecha.format(ordenPago.getFecha().getTime());
						String proveedorStr;
						//Si no tiene facturas, es porque la OP es Sin Proveedor y Sin Factura.
						if (ordenPago.getListadoFacturas().isEmpty()) {
							proveedorStr = "<Sin Proveedor>";
						} else { //Es OPNormal o OPConProveedorYSinFactura. Ambas tienen como minimo una factura
							Proveedor proveedor= ordenPago.getListadoFacturas().get(0).getProveedor(); //Agarro la primera
							proveedorStr = proveedor.getRazonSocialConCUIT();
						}
						
						item.add(new Label("nroOP", ordenPago.getNro()));
						item.add(new Label("fecha", fecha));
						item.add(new Label("proveedor", proveedorStr));
						item.add(new Label("evento", ordenPago.getEvento().getNombreConCliente()));
						item.add(new Label("solicitante", ordenPago.getUsuarioSolicitante().getNombreCompleto()));
						item.add(new Label("modoPago", ordenPago.getModosPagosElegidos()));
						Label totalAPagarLbl = new Label("totalAPagar", "$ " + Utils.round2Decimals(ordenPagoService.calculateTotalPagos(ordenPago.getListadoPagos()), ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale()) );
						item.add(totalAPagarLbl);
						
					}
				};
				double sumaTotales = ordenPagoService.calculateSumTotalPagosMensual(mes);
				Label sumaTotalesLabel = new Label("sumaTotales", "$" + Utils.round2Decimals(sumaTotales, locale));
								
				item.add(dataView);
				item.add(sumaTotalesLabel);
				
			}
		};
		
		mesesDataView.afterRender();
		
		double totalAnual = ordenPagoService.calculateSumTotalPagosAnual(añoSeleccionado);
		String totalAnualStr = Utils.round2Decimals(totalAnual, locale);
		totalAnualLabel = new Label("totalAnual", "$"+totalAnualStr);
		totalAnualLabel.setOutputMarkupId(true);
				
		container.add(mesesDataView);
		container.add(totalAnualLabel);
	}
	
	private IDataProvider<FacturaCompraOrdenPago> getFacturaOrdenPagoProvider(Calendar mes) {
		String prov = null;
		String nroOP = null;
		Date date = null;
		Integer idEvento = null;
		Integer idPlanCuenta = null;
		return new FacturaOrdenPagoDataProvider(Model.of(prov), Model.of(nroOP), Model.of(date), Model.of(idEvento), Model.of(false), Model.of(false), Model.of(false), mes, Model.of(idPlanCuenta)); //Modificacion Nov 2021: Se quitan los borrados del Reporte OPs Mensual
	}
	
	private IDataProvider<Calendar> getMesesProvider() {
		IDataProvider<Calendar> mesesDataProvider = new IDataProvider<Calendar>() {
			private static final long serialVersionUID = 1L;
			
			private List<Calendar> listaMeses ;
			
			@Override
			public void detach() {
			}

			@Override
			public Iterator<Calendar> iterator(long first, long count) {
				return listaMeses.iterator();
			}

			@Override
			public long size() {
				listaMeses = getListadoMeses();
				return listaMeses.size();
			}

			@Override
			public IModel<Calendar> model(Calendar cal) {
				return new Model<Calendar>(cal);
			}
			
			private List<Calendar> getListadoMeses(){
				List<Calendar> listaMeses = new ArrayList<Calendar>();
				
				if (mesSeleccionado!=null) {
					Calendar mes = Calendar.getInstance();
					mes.set(Calendar.YEAR, añoSeleccionado.get(Calendar.YEAR));
					mes.set(Calendar.MONTH, mesSeleccionado.get(Calendar.MONTH));
					mes.set(Calendar.DAY_OF_MONTH, 1);
					mes = Utils.firstMillisecondOfDay(mes);

					listaMeses.add(mes);
				} else {
					int mesLoop;
					if (añoSeleccionado.get(Calendar.YEAR)==Calendar.getInstance().get(Calendar.YEAR)) {
						mesLoop = Calendar.getInstance().get(Calendar.MONTH);
					} else {
						mesLoop = 11;
					}
					
					for (int i = 0 ; i<=mesLoop; i++) {
						Calendar mes = Calendar.getInstance();
						mes.set(Calendar.YEAR, añoSeleccionado.get(Calendar.YEAR));
						mes.set(Calendar.MONTH, i);
						mes.set(Calendar.DAY_OF_MONTH, 1);
						mes = Utils.firstMillisecondOfDay(mes);
	
						listaMeses.add(mes);
					}
				}
				return listaMeses;
			}
        	
        };
		return mesesDataProvider;
	}

}
