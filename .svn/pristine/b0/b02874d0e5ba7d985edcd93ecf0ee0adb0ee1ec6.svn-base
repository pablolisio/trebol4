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

import com.proit.modelo.auxiliar.ReporteTotalTarjetaCredito;
import com.proit.servicios.compras.OrdenPagoService;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.CalendarChoiceRenderer;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class TarjetasCreditoPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	private Locale locale; 
	
	private Calendar añoSeleccionado;
	private Calendar mesSeleccionado;
	
	private OrdenPagoService ordenPagoService;
	
	private Label totalAnualLabel;
			
	public TarjetasCreditoPage(final PageParameters parameters) {
		super(parameters);
		
		ordenPagoService = new OrdenPagoService();
		
		locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
		
		WebMarkupContainer container = new WebMarkupContainer("dataContainer");
		container.setOutputMarkupPlaceholderTag(true);
		add(container);
		
		crearAñoDropDownChoice(container);
		
		crearMesDropDownChoice(container);
		
		addTotalesTarjetasCreditoXMes(container);
		
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
				double totalAnual = ordenPagoService.getTotalAnualTarjetasCredito(añoSeleccionado);
				totalAnualLabel.setDefaultModelObject("$" + Utils.round2Decimals(totalAnual, locale));
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
	
	private void addTotalesTarjetasCreditoXMes(WebMarkupContainer container) {		
		IDataProvider<Calendar> mesesDataProvider = getMesesProvider();	
		
		DataView<Calendar> mesesDataView = new DataView<Calendar>("listaMeses", mesesDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<Calendar> item) {
				Calendar mes = item.getModelObject();
				DateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
				item.add(new Label("mes", dateFormat.format(mes.getTime())));
				
				//IDataProvider<CajaChica> cajaChicaDataProvider = new CajaChicaDataProvider(mes);
				IDataProvider<ReporteTotalTarjetaCredito> totalesDataProvider = getTotalesTarjetaCreditoProvider(mes);
				
				DataView<ReporteTotalTarjetaCredito> totalesDataView = new DataView<ReporteTotalTarjetaCredito>("listaTotales", totalesDataProvider) {
					private static final long serialVersionUID = 1L;			
					@Override
					protected void populateItem(Item<ReporteTotalTarjetaCredito> item) {
						String total = Utils.round2Decimals(item.getModelObject().getTotal().doubleValue(), locale);
						item.add(new Label("proveedor", item.getModelObject().getProveedor()));
						item.add(new Label("total", "$" + total));
					}
				};
				
				double totalMensual = ordenPagoService.getTotalMesTarjetasCredito(mes);
				Label totalMensualLabel = new Label("totalMensual", "$" + Utils.round2Decimals(totalMensual, locale));								
				item.add(totalesDataView);
				item.add(totalMensualLabel);
				
			}
		};
		
		double totalAnual = ordenPagoService.getTotalAnualTarjetasCredito(añoSeleccionado);
		totalAnualLabel = new Label("totalAnual", "$" + Utils.round2Decimals(totalAnual, locale));
		totalAnualLabel.setOutputMarkupPlaceholderTag(true);
		container.add(totalAnualLabel);
		container.add(mesesDataView);
	}
	
	private IDataProvider<ReporteTotalTarjetaCredito> getTotalesTarjetaCreditoProvider(final Calendar mes) {
		IDataProvider<ReporteTotalTarjetaCredito> totalesProveedoresDataProvider = new IDataProvider<ReporteTotalTarjetaCredito>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void detach() {
			}

			@Override
			public Iterator<ReporteTotalTarjetaCredito> iterator(long first, long count) {
				List<Object[]> totalesTarjetaMensual = ordenPagoService.getTotalesTarjetaMensual(first, count, mes);
				List<ReporteTotalTarjetaCredito> listadoReporteTarjetaCredito = new ArrayList<ReporteTotalTarjetaCredito>();
				for (Object[] obj : totalesTarjetaMensual) {
					ReporteTotalTarjetaCredito reporteTotalTarjetaCredito = new ReporteTotalTarjetaCredito();
					reporteTotalTarjetaCredito.setProveedor((String) obj[0]);
					reporteTotalTarjetaCredito.setTotal((BigDecimal) obj[1]);
					listadoReporteTarjetaCredito.add(reporteTotalTarjetaCredito);
				}
				return listadoReporteTarjetaCredito.iterator();
			}

			@Override
			public long size() {
				return ordenPagoService.getTotalesTarjetaMensualSize(mes);
			}

			@Override
			public IModel<ReporteTotalTarjetaCredito> model(ReporteTotalTarjetaCredito reporteTotalProveedor) {
				return new Model<ReporteTotalTarjetaCredito>(reporteTotalProveedor);
			}
        	
        };
		return totalesProveedoresDataProvider;
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
