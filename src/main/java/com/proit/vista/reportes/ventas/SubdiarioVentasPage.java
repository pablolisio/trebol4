package com.proit.vista.reportes.ventas;

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

import com.proit.modelo.ventas.FacturaVenta;
import com.proit.servicios.ventas.FacturaVentaService;
import com.proit.servicios.ventas.SolicitudFacturaVentaService;
import com.proit.utils.ExcelGeneratorSubdiarioVentas;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.AJAXDownload;
import com.proit.wicket.components.CalendarChoiceRenderer;
import com.proit.wicket.dataproviders.FacturasVentaDataProvider;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class SubdiarioVentasPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	private Calendar añoSeleccionado;
	private Calendar mesSeleccionado;
	
	private Label totalAnualLabel;
	private Label ventaAnualEstimadaLabel;
	
	private FacturaVentaService facturaService;
	private SolicitudFacturaVentaService solicitudFacturaVentaService;
	
	private Locale locale;
		
	public SubdiarioVentasPage(final PageParameters parameters) {
		super(parameters);
		
		facturaService = new FacturaVentaService();
		solicitudFacturaVentaService = new SolicitudFacturaVentaService();
		locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
		
		WebMarkupContainer container = new WebMarkupContainer("dataContainer");
		container.setOutputMarkupPlaceholderTag(true);
		add(container);
		
		crearAñoDropDownChoice(container);
		
		crearMesDropDownChoice(container);
		
		agregarBotonExportarReporte();
		
		addFacturasPorMes(container);
		
		facturarOnLineMenu.setearMenuActivo(false, false, false, true);
		
		mostrarAlertaEnPantallaSiCorresponde(parameters);
	}
	
	private void agregarBotonExportarReporte() {
		final ExcelGeneratorSubdiarioVentas excelGenerator = new ExcelGeneratorSubdiarioVentas(getRuntimeConfigurationType());
		final AJAXDownload download = createAjaxDownload(excelGenerator.getFileName());
		AjaxLink<FacturaVenta> generarExcelLink = new AjaxLink<FacturaVenta>("generarExcel") {			
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				Calendar fechaInicio = null;
				Calendar fechaFin = null;
				if (mesSeleccionado!=null) {
					fechaInicio = Calendar.getInstance();
					fechaInicio.set(Calendar.YEAR, añoSeleccionado.get(Calendar.YEAR));
					fechaInicio.set(Calendar.MONTH, mesSeleccionado.get(Calendar.MONTH));
					fechaInicio.set(Calendar.DAY_OF_MONTH, 1);
					fechaInicio = Utils.firstMillisecondOfDay(fechaInicio);
					
					fechaFin = (Calendar) fechaInicio.clone();
					fechaFin.add(Calendar.MONTH, 1);
				} else {
					fechaInicio = Calendar.getInstance();
					fechaInicio.set(Calendar.YEAR, añoSeleccionado.get(Calendar.YEAR));
					fechaInicio.set(Calendar.MONTH, fechaInicio.getMinimum(Calendar.MONTH));
					fechaInicio.set(Calendar.DAY_OF_MONTH, 1);
					fechaInicio = Utils.firstMillisecondOfDay(fechaInicio);
					
					fechaFin = (Calendar) fechaInicio.clone();
					fechaFin.add(Calendar.YEAR, 1);
				}
				ArrayList<FacturaVenta> listaFacturas = (ArrayList<FacturaVenta>) facturaService.getFacturas(null, null, null, false, null, fechaInicio, fechaFin, true);
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
				
				double totalAnual = facturaService.calculateSumSubtotalAnual(añoSeleccionado);
				String totalAnualStr = Utils.round2Decimals(totalAnual, locale);
				totalAnualLabel.setDefaultModelObject("$"+totalAnualStr);
				
				double ventaAnualEstimada = totalAnual + solicitudFacturaVentaService.calculateSumSubtotal(null, null, true, true);
				String ventaAnualEstimadaStr = Utils.round2Decimals(ventaAnualEstimada, locale);
				ventaAnualEstimadaLabel.setDefaultModelObject("$"+ventaAnualEstimadaStr);
				
				target.add(container);
				target.add(ventaAnualEstimadaLabel);
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
	
	private void addFacturasPorMes(WebMarkupContainer container) {
		IDataProvider<Calendar> mesesDataProvider = getMesesProvider();
		
		DataView<Calendar> mesesDataView = new DataView<Calendar>("listaMeses", mesesDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<Calendar> item) {
				Calendar mes = item.getModelObject();
				DateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
				item.add(new Label("mes", dateFormat.format(mes.getTime())));
				
				IDataProvider<FacturaVenta> facturasDataProvider = getFacturasProvider(mes);
				
				DataView<FacturaVenta> facturasDataView = new DataView<FacturaVenta>("listaFacturas", facturasDataProvider) {
					private static final long serialVersionUID = 1L;
					
					@Override
					protected void populateItem(Item<FacturaVenta> item) {
						DateFormat dateFormatFecha = new SimpleDateFormat("dd-MM-yyyy");
						FacturaVenta factura = item.getModelObject();
						String fecha = dateFormatFecha.format(factura.getFecha().getTime());
//						double totalNetoFactura = ordenPagoService.calculateTotalNetoFactura(factura);
//						String faltaPagar = factura.getEstadoFactura().equals(EstadoFacturaCompra.PAGADA_PARCIAL) ? 
//								" (Faltante: $" + Utils.round2Decimals(totalNetoFactura, locale) + ")"		:		"";
						item.add(new Label("fecha", fecha));
						item.add(new Label("cliente", factura.getCliente().getRazonSocialConCUIT()));
						item.add(new Label("tipo", factura.getTipoFactura().getNombreCorto()));
						item.add(new Label("nro", ".." + Utils.getNroFacturaVentaSinPrefijo(factura.getTipoFactura(), factura.getNro())));
						//item.add(new Label("estado", factura.getEstadoFacturaVenta().getNombre() + faltaPagar)); 
						item.add(new Label("estado", factura.getEstadoFacturaVenta().getNombre()));
						item.add(new Label("evento", factura.getEvento().getNombre()));
						
						double subtotalD = factura.calculateSubtotal();
						double ivaD = Utils.calcularImporteIVA(subtotalD, factura.getTipoFactura());
						double totalD = subtotalD + ivaD;
						String subtotal = factura.isNotaCredito() ? "$-" : "$";
						subtotal += Utils.round2Decimals(subtotalD, locale);
						String iva = factura.isNotaCredito() ? "$-" : "$";
						iva += Utils.round2Decimals(ivaD, locale);
						String total = factura.isNotaCredito() ? "$-" : "$";
						total += Utils.round2Decimals(totalD, locale);
						item.add(new Label("subtotal", subtotal));
						item.add(new Label("iva", iva));
						item.add(new Label("total", total));
					}
				};
				double sumaSubTotal = facturaService.calculateSumSubtotalMensual(mes);
				double sumaIva = facturaService.calculateSumIVAMensual(mes);
				double sumaTotales = sumaSubTotal + sumaIva;
				
				Label sumaSubTotalLabel = new Label("sumaSubTotal", "$" + Utils.round2Decimals(sumaSubTotal, locale));
				Label sumaIvaLabel = new Label("sumaIva", "$" + Utils.round2Decimals(sumaIva, locale));
				Label sumaTotalesLabel = new Label("sumaTotales", "$" + Utils.round2Decimals(sumaTotales, locale));

								
				item.add(facturasDataView);
				item.add(sumaSubTotalLabel);
				item.add(sumaIvaLabel);
				item.add(sumaTotalesLabel);
				
			}
		};
		
		double totalAnual = facturaService.calculateSumSubtotalAnual(añoSeleccionado);
		String totalAnualStr = Utils.round2Decimals(totalAnual, locale);
		totalAnualLabel = new Label("totalAnual", "$"+totalAnualStr);
		totalAnualLabel.setOutputMarkupId(true);
		
		double ventaAnualEstimada = totalAnual + solicitudFacturaVentaService.calculateSumSubtotal(null, null, true, true);
		String ventaAnualEstimadaStr = Utils.round2Decimals(ventaAnualEstimada, locale);
		ventaAnualEstimadaLabel = new Label("ventaAnualEstimada", "$"+ventaAnualEstimadaStr);
		ventaAnualEstimadaLabel.setOutputMarkupId(true);
				
		container.add(mesesDataView);
		container.add(totalAnualLabel);
		container.add(ventaAnualEstimadaLabel);
	}
	
	private IDataProvider<FacturaVenta> getFacturasProvider(Calendar mes) {
		Date date = null;
		Integer idEvento = null;
		return new FacturasVentaDataProvider(new Model<String>(), Model.of(idEvento), Model.of(date), Model.of(false), mes, Model.of(true));
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
