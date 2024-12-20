package com.proit.vista.reportes.compras;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.proit.modelo.auxiliar.ReportePlanCuenta;
import com.proit.servicios.compras.OrdenPagoService;
import com.proit.utils.ExcelGeneratorPlanCuentas;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.AJAXDownload;
import com.proit.wicket.components.CalendarChoiceRenderer;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class ReportePlanCuentasMensualPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	private Calendar añoSeleccionado;
	private Calendar mesSeleccionado;
	
	private Label totalAnualLabel;
	
	private OrdenPagoService ordenPagoService;
	
	private Locale locale;
		
	public ReportePlanCuentasMensualPage(final PageParameters parameters) {
		super(parameters);
		
		ordenPagoService = new OrdenPagoService();
		locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
		
		WebMarkupContainer container = new WebMarkupContainer("dataContainer");
		container.setOutputMarkupPlaceholderTag(true);
		add(container);
		
		crearAñoDropDownChoice(container);
		
		crearMesDropDownChoice(container);
		
		agregarBotonExportarReporte();
		
		addListado(container);
		
		facturarOnLineMenu.setearMenuActivo(false, false, false, true);
		
		mostrarAlertaEnPantallaSiCorresponde(parameters);
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
				
				double totalAnual = ordenPagoService.calculateSumPlanesDeCuentaAnual(añoSeleccionado);
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
	
	private void agregarBotonExportarReporte() {
		final ExcelGeneratorPlanCuentas excelGenerator = new ExcelGeneratorPlanCuentas(getRuntimeConfigurationType());
		final AJAXDownload download = createAjaxDownload(excelGenerator.getFileName());
		AjaxLink<ReportePlanCuenta> generarExcelLink = new AjaxLink<ReportePlanCuenta>("generarExcel") {			
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				List<ReportePlanCuenta> listadoReportePlanCuenta = getListadoReportePlanCuenta(mesSeleccionado, añoSeleccionado);				
				double totalAnual = ordenPagoService.calculateSumPlanesDeCuentaAnual(añoSeleccionado);
				excelGenerator.generarExcel(listadoReportePlanCuenta, añoSeleccionado, mesSeleccionado, totalAnual);
				download.initiate(target);
			}
		};
		add(generarExcelLink);
		add(download);
	}
	
	private void addListado(WebMarkupContainer container) {
		IDataProvider<Calendar> mesesDataProvider = getMesesProvider();
		
		DataView<Calendar> mesesDataView = new DataView<Calendar>("listaMeses", mesesDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<Calendar> item) {
				Calendar mes = item.getModelObject();
				DateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
				item.add(new Label("mes", dateFormat.format(mes.getTime())));
				
				IDataProvider<ReportePlanCuenta> dataProvider = getPlanesCuentaDataProvider(mes);
				
				DataView<ReportePlanCuenta> dataView = new DataView<ReportePlanCuenta>("listaPlanes", dataProvider) {
					private static final long serialVersionUID = 1L;
					
					@Override
					protected void populateItem(Item<ReportePlanCuenta> item) {
						ReportePlanCuenta reportePlanCuenta = item.getModelObject();						
						item.add(new Label("planCuenta", reportePlanCuenta.getNombrePlanCuenta()));
						
						Label totalAPagarLbl = new Label("total", "$ " + Utils.round2Decimals(reportePlanCuenta.getTotal(), ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale()) );
						item.add(totalAPagarLbl);
						
						Link<ReportePlanCuenta> verDetallesLink = new Link<ReportePlanCuenta>("verDetalles", item.getModel()) {
							private static final long serialVersionUID = 1L;
							@Override
							public void onClick() {
								ReportePlanCuenta reportePlanCuenta = (ReportePlanCuenta) getModelObject();
								setResponsePage(new DetallesPlanCuentasPage(reportePlanCuenta, mes));
							}
						};
						item.add(verDetallesLink);
					}
				};
				double sumaTotales = ordenPagoService.calculateSumPlanesDeCuentaMensual(mes);
				Label sumaTotalesLabel = new Label("sumaTotales", "$" + Utils.round2Decimals(sumaTotales, locale));
								
				item.add(dataView);
				item.add(sumaTotalesLabel);
				
			}
		};
		
		mesesDataView.afterRender();
		
		double totalAnual = ordenPagoService.calculateSumPlanesDeCuentaAnual(añoSeleccionado);
		String totalAnualStr = Utils.round2Decimals(totalAnual, locale);
		totalAnualLabel = new Label("totalAnual", "$"+totalAnualStr);
		totalAnualLabel.setOutputMarkupId(true);
				
		container.add(mesesDataView);
		container.add(totalAnualLabel);
	}
	
	private List<ReportePlanCuenta> getListadoReportePlanCuenta(Calendar mes, Calendar año) {
		List<Object[]> planesCuenta = ordenPagoService.getPlanesDeCuentaMensual(mes, año);
		List<ReportePlanCuenta> listadoReportePlanCuenta = new ArrayList<ReportePlanCuenta>();
		for (Object[] obj : planesCuenta) {
			ReportePlanCuenta reportePlanCuenta = new ReportePlanCuenta();
			reportePlanCuenta.setIdPlanCuenta((Integer) obj[0]);
			reportePlanCuenta.setNombrePlanCuenta((String) obj[1]);
			reportePlanCuenta.setTotal(((BigDecimal) obj[2]).doubleValue());
			listadoReportePlanCuenta.add(reportePlanCuenta);
		}
		return listadoReportePlanCuenta;
	}
	
	private IDataProvider<ReportePlanCuenta> getPlanesCuentaDataProvider(Calendar mes) {
		IDataProvider<ReportePlanCuenta> dataProvider = new IDataProvider<ReportePlanCuenta>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void detach() {
			}

			@Override
			public Iterator<ReportePlanCuenta> iterator(long first, long count) {
				List<ReportePlanCuenta> listadoReportePlanCuenta = getListadoReportePlanCuenta(mes, null);
				return listadoReportePlanCuenta.iterator();
			}

			@Override
			public long size() {
				return ordenPagoService.getPlanesDeCuentaMensualSize(mes);
			}

			@Override
			public IModel<ReportePlanCuenta> model(ReportePlanCuenta reportePlanCuenta) {
				return new Model<ReportePlanCuenta>(reportePlanCuenta);
			}
        	
        };
		return dataProvider;
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
