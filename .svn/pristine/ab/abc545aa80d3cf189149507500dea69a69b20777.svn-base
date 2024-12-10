package com.proit.vista.reportes.ventas;

import java.math.BigDecimal;
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

import com.proit.modelo.auxiliar.ReporteVentaCliente;
import com.proit.servicios.ventas.ReportesVentasService;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.CalendarChoiceRenderer;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class VentasPorClientePage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	private Calendar añoSeleccionado;
	
	private ReportesVentasService reportesVentasService;
	
	private Locale locale;
	
	private Label sumaEneLabel;
	private Label sumaFebLabel;
	private Label sumaMarLabel;
	private Label sumaAbrLabel;
	private Label sumaMayLabel;
	private Label sumaJunLabel;
	private Label sumaJulLabel;
	private Label sumaAgoLabel;
	private Label sumaSepLabel;
	private Label sumaOctLabel;
	private Label sumaNovLabel;
	private Label sumaDicLabel;
	private Label sumaTotalesLabel;
	private Label sumaPromediosLabel;
	
	private long sumaTotales;
				
	public VentasPorClientePage(final PageParameters parameters) {
		super(parameters);
		
		reportesVentasService = new ReportesVentasService();
		
		locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
		
		WebMarkupContainer container = new WebMarkupContainer("dataContainer");
		container.setOutputMarkupPlaceholderTag(true);
		add(container);
		
		WebMarkupContainer totales = new WebMarkupContainer("totales");
		totales.setOutputMarkupPlaceholderTag(true);
		
		crearAñoDropDownChoice(container, totales);
		
		sumaEneLabel = new Label("sumaEne");
		sumaFebLabel = new Label("sumaFeb");
		sumaMarLabel = new Label("sumaMar");
		sumaAbrLabel = new Label("sumaAbr");
		sumaMayLabel = new Label("sumaMay");
		sumaJunLabel = new Label("sumaJun");
		sumaJulLabel = new Label("sumaJul");
		sumaAgoLabel = new Label("sumaAgo");
		sumaSepLabel = new Label("sumaSep");
		sumaOctLabel = new Label("sumaOct");
		sumaNovLabel = new Label("sumaNov");
		sumaDicLabel = new Label("sumaDic");
		sumaTotalesLabel = new Label("sumaTotales");
		sumaPromediosLabel = new Label("sumaPromedios");
		
		recalculateTotales();
				
		totales.add(sumaEneLabel);
		totales.add(sumaFebLabel);
		totales.add(sumaMarLabel);
		totales.add(sumaAbrLabel);
		totales.add(sumaMayLabel);
		totales.add(sumaJunLabel);
		totales.add(sumaJulLabel);
		totales.add(sumaAgoLabel);
		totales.add(sumaSepLabel);
		totales.add(sumaOctLabel);
		totales.add(sumaNovLabel);
		totales.add(sumaDicLabel);
		totales.add(sumaTotalesLabel);
		totales.add(sumaPromediosLabel);
		
		container.add(totales);
				
		addListado(container);
		
		facturarOnLineMenu.setearMenuActivo(false, false, false, true);
		
		mostrarAlertaEnPantallaSiCorresponde(parameters);
	}

	private void recalculateTotales() {
		long sumaEne = Math.round(reportesVentasService.calculateSumVentasPorCliente("ene", añoSeleccionado));
		long sumaFeb = Math.round(reportesVentasService.calculateSumVentasPorCliente("feb", añoSeleccionado));
		long sumaMar = Math.round(reportesVentasService.calculateSumVentasPorCliente("mar", añoSeleccionado));
		long sumaAbr = Math.round(reportesVentasService.calculateSumVentasPorCliente("abr", añoSeleccionado));
		long sumaMay = Math.round(reportesVentasService.calculateSumVentasPorCliente("may", añoSeleccionado));
		long sumaJun = Math.round(reportesVentasService.calculateSumVentasPorCliente("jun", añoSeleccionado));
		long sumaJul = Math.round(reportesVentasService.calculateSumVentasPorCliente("jul", añoSeleccionado));
		long sumaAgo = Math.round(reportesVentasService.calculateSumVentasPorCliente("ago", añoSeleccionado));
		long sumaSep = Math.round(reportesVentasService.calculateSumVentasPorCliente("sep", añoSeleccionado));
		long sumaOct = Math.round(reportesVentasService.calculateSumVentasPorCliente("oct", añoSeleccionado));
		long sumaNov = Math.round(reportesVentasService.calculateSumVentasPorCliente("nov", añoSeleccionado));
		long sumaDic = Math.round(reportesVentasService.calculateSumVentasPorCliente("dic", añoSeleccionado));
		sumaTotales = Math.round(reportesVentasService.calculateSumVentasPorCliente("total", añoSeleccionado));
		long sumaPromedios = Math.round(reportesVentasService.calculateSumVentasPorCliente("promedio", añoSeleccionado));
		
		sumaEneLabel.setDefaultModel(Model.of(Utils.round(sumaEne,0)));
		sumaFebLabel.setDefaultModel(Model.of(Utils.round(sumaFeb,0)));
		sumaMarLabel.setDefaultModel(Model.of(Utils.round(sumaMar,0)));
		sumaAbrLabel.setDefaultModel(Model.of(Utils.round(sumaAbr,0)));
		sumaMayLabel.setDefaultModel(Model.of(Utils.round(sumaMay,0)));
		sumaJunLabel.setDefaultModel(Model.of(Utils.round(sumaJun,0)));
		sumaJulLabel.setDefaultModel(Model.of(Utils.round(sumaJul,0)));
		sumaAgoLabel.setDefaultModel(Model.of(Utils.round(sumaAgo,0)));
		sumaSepLabel.setDefaultModel(Model.of(Utils.round(sumaSep,0)));
		sumaOctLabel.setDefaultModel(Model.of(Utils.round(sumaOct,0)));
		sumaNovLabel.setDefaultModel(Model.of(Utils.round(sumaNov,0)));
		sumaDicLabel.setDefaultModel(Model.of(Utils.round(sumaDic,0)));
		sumaTotalesLabel.setDefaultModel(Model.of(Utils.round(sumaTotales,0)));
		sumaPromediosLabel.setDefaultModel(Model.of(Utils.round(sumaPromedios,0)));
	}
	
	private void crearAñoDropDownChoice(final WebMarkupContainer container, final WebMarkupContainer totales) {
		List<Calendar> listaAños = Utils.getListaAños(-3, 0);
		añoSeleccionado = listaAños.get(listaAños.size()-1);
		
		final IModel<Calendar> añoModel = Model.of(añoSeleccionado);
		
		DropDownChoice<Calendar> añoDropDownChoice = new DropDownChoice<Calendar>("anio", añoModel, listaAños, new CalendarChoiceRenderer("yyyy"));
		añoDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;

			protected void onUpdate(AjaxRequestTarget target) { 
				añoSeleccionado = añoModel.getObject();
				recalculateTotales();
				target.add(container);
				target.add(totales);
            }
        });
		add(añoDropDownChoice);
	}
	
	private void addListado(WebMarkupContainer container) { 
		IDataProvider<ReporteVentaCliente> ventasPorClienteDataProvider = getVentasPorClienteDataProvider();
		
		DataView<ReporteVentaCliente> ventasPorClienteDataView = new DataView<ReporteVentaCliente>("listaClientes", ventasPorClienteDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<ReporteVentaCliente> item) {
				ReporteVentaCliente reporteVentaCliente = item.getModelObject();
				String cliente = reporteVentaCliente.getCliente();
				int ene = reporteVentaCliente.getEne().intValue();
				int feb = reporteVentaCliente.getFeb().intValue();
				int mar = reporteVentaCliente.getMar().intValue();
				int abr = reporteVentaCliente.getAbr().intValue();
				int may = reporteVentaCliente.getMay().intValue();
				int jun = reporteVentaCliente.getJun().intValue();
				int jul = reporteVentaCliente.getJul().intValue();
				int ago = reporteVentaCliente.getAgo().intValue();
				int sep = reporteVentaCliente.getSep().intValue();
				int oct = reporteVentaCliente.getOct().intValue();
				int nov = reporteVentaCliente.getNov().intValue();
				int dic = reporteVentaCliente.getDic().intValue();
				int total = reporteVentaCliente.getTotal().intValue();
				int promedio = reporteVentaCliente.getPromedio().intValue();
				double porcentaje = sumaTotales!=0 ? ((double)total / sumaTotales) : 0;
				
				Label clienteLabel = new Label("cliente", cliente);
				Label eneLabel = new Label("ene", Utils.round(ene,0));
				Label febLabel = new Label("feb", Utils.round(feb,0));
				Label marLabel = new Label("mar", Utils.round(mar,0));
				Label abrLabel = new Label("abr", Utils.round(abr,0));
				Label mayLabel = new Label("may", Utils.round(may,0));
				Label junLabel = new Label("jun", Utils.round(jun,0));
				Label julLabel = new Label("jul", Utils.round(jul,0));
				Label agoLabel = new Label("ago", Utils.round(ago,0));
				Label sepLabel = new Label("sep", Utils.round(sep,0));
				Label octLabel = new Label("oct", Utils.round(oct,0));
				Label novLabel = new Label("nov", Utils.round(nov,0));
				Label dicLabel = new Label("dic", Utils.round(dic,0));
				Label totalLabel = new Label("total", Utils.round(total,0));
				Label promedioLabel = new Label("promedio", Utils.round(promedio,0));
				Label porcentajeLabel = new Label("porcentaje", Utils.round2Decimals(porcentaje, locale));
				
				item.add(clienteLabel);
				item.add(eneLabel);
				item.add(febLabel);
				item.add(marLabel);
				item.add(abrLabel);
				item.add(mayLabel);
				item.add(junLabel);
				item.add(julLabel);
				item.add(agoLabel);
				item.add(sepLabel);
				item.add(octLabel);
				item.add(novLabel);
				item.add(dicLabel);
				item.add(totalLabel);
				item.add(promedioLabel);
				item.add(porcentajeLabel);
			}
		};
		
		container.add(ventasPorClienteDataView);
	}
	
	private IDataProvider<ReporteVentaCliente> getVentasPorClienteDataProvider() {
		IDataProvider<ReporteVentaCliente> ventasPorClienteDataProvider = new IDataProvider<ReporteVentaCliente>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void detach() {
			}

			@Override
			public Iterator<ReporteVentaCliente> iterator(long first, long count) {
				List<Object[]> ventasPorCliente = reportesVentasService.getVentasPorCliente(añoSeleccionado);
				List<ReporteVentaCliente> listadoReporteVentasCliente = new ArrayList<ReporteVentaCliente>();
				for (Object[] obj : ventasPorCliente) {
					ReporteVentaCliente reporteVentasCliente = new ReporteVentaCliente();
					reporteVentasCliente.setCliente((String) obj[0]);
					reporteVentasCliente.setEne((BigDecimal) obj[1]);
					reporteVentasCliente.setFeb((BigDecimal) obj[2]);
					reporteVentasCliente.setMar((BigDecimal) obj[3]);
					reporteVentasCliente.setAbr((BigDecimal) obj[4]);
					reporteVentasCliente.setMay((BigDecimal) obj[5]);
					reporteVentasCliente.setJun((BigDecimal) obj[6]);
					reporteVentasCliente.setJul((BigDecimal) obj[7]);
					reporteVentasCliente.setAgo((BigDecimal) obj[8]);
					reporteVentasCliente.setSep((BigDecimal) obj[9]);
					reporteVentasCliente.setOct((BigDecimal) obj[10]);
					reporteVentasCliente.setNov((BigDecimal) obj[11]);
					reporteVentasCliente.setDic((BigDecimal) obj[12]);
					reporteVentasCliente.setTotal((BigDecimal) obj[13]);
					reporteVentasCliente.setPromedio((BigDecimal) obj[14]);
					listadoReporteVentasCliente.add(reporteVentasCliente);
				}
				return listadoReporteVentasCliente.iterator();
			}

			@Override
			public long size() {
				return reportesVentasService.getVentasPorClienteSize(añoSeleccionado);
			}

			@Override
			public IModel<ReporteVentaCliente> model(ReporteVentaCliente reporteVentaCliente) {
				return new Model<ReporteVentaCliente>(reporteVentaCliente);
			}
        	
        };
		return ventasPorClienteDataProvider;
	}

}
