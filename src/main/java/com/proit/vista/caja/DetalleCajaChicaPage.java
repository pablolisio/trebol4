package com.proit.vista.caja;

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

import com.proit.modelo.CajaChica;
import com.proit.modelo.Usuario;
import com.proit.servicios.CajaChicaService;
import com.proit.utils.ExcelGeneratorCajaChica;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.AJAXDownload;
import com.proit.wicket.components.CalendarChoiceRenderer;
import com.proit.wicket.dataproviders.CajaChicaDataProvider;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class DetalleCajaChicaPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	private Calendar añoSeleccionado;
	private Calendar mesSeleccionado;
	
	private CajaChicaService cajaChicaService;
	
	private Label totalAnualLabel;
	
	private Locale locale;
		
	public DetalleCajaChicaPage(final PageParameters parameters) {
		super(parameters);
		
		cajaChicaService = new CajaChicaService();
		
		locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
		
		WebMarkupContainer container = new WebMarkupContainer("dataContainer");
		container.setOutputMarkupPlaceholderTag(true);
		add(container);
		
		crearAñoDropDownChoice(container);
		
		crearMesDropDownChoice(container);
		
		agregarBotonExportarReporte();
		
		addCajaChicaPorMes(container);
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
		
		mostrarAlertaEnPantallaSiCorresponde(parameters);
	}
	
	private void agregarBotonExportarReporte() {
		final ExcelGeneratorCajaChica excelGenerator = new ExcelGeneratorCajaChica(getRuntimeConfigurationType());
		final AJAXDownload download = createAjaxDownload(excelGenerator.getFileName());
		AjaxLink<CajaChica> generarExcelLink = new AjaxLink<CajaChica>("generarExcel") {			
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				Calendar mes = null;
				if (mesSeleccionado!=null) {
					mes = Calendar.getInstance();
					mes.set(Calendar.YEAR, añoSeleccionado.get(Calendar.YEAR));
					mes.set(Calendar.MONTH, mesSeleccionado.get(Calendar.MONTH));
					mes.set(Calendar.DAY_OF_MONTH, 1);
					mes = Utils.firstMillisecondOfDay(mes);
				}
				ArrayList<CajaChica> listado = (ArrayList<CajaChica>) cajaChicaService.getListaCajaChica(mes, añoSeleccionado);
				double totalAnual = cajaChicaService.calculateMontoTotalAnual(añoSeleccionado);
				excelGenerator.generarExcel(listado, añoSeleccionado, mesSeleccionado, totalAnual);
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
				double totalAnual = cajaChicaService.calculateMontoTotalAnual(añoSeleccionado);
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
	
	private void addCajaChicaPorMes(WebMarkupContainer container) {		
		IDataProvider<Calendar> mesesDataProvider = getMesesProvider();
		
		DataView<Calendar> mesesDataView = new DataView<Calendar>("listaMeses", mesesDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<Calendar> item) {
				Calendar mes = item.getModelObject();
				DateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
				item.add(new Label("mes", dateFormat.format(mes.getTime())));
				
				IDataProvider<CajaChica> cajaChicaDataProvider = new CajaChicaDataProvider(mes);
				
				DataView<CajaChica> cajaChicaDataView = new DataView<CajaChica>("listaCajaChica", cajaChicaDataProvider) {
					private static final long serialVersionUID = 1L;
					
					@Override
					protected void populateItem(Item<CajaChica> item) {
						DateFormat dateFormatFecha = new SimpleDateFormat("dd-MM-yyyy");
						String fecha = dateFormatFecha.format(item.getModelObject().getFecha().getTime());				
						String detalle = item.getModelObject().getDetalle();
						String solicitanteStr = "---";
						if (item.getModelObject().isSolicitadoPorOtros()) {
							solicitanteStr = "Otros";
						} else if (item.getModelObject().isSolicitadoPorTodos()) {
							solicitanteStr = "Todos";
						} else {
							Usuario usuarioSolicitante = item.getModelObject().getUsuarioSolicitante();
							solicitanteStr = usuarioSolicitante!=null ? usuarioSolicitante.getNombreCompleto() : "-";
						}
						
						double monto = item.getModelObject().getMonto();
						String montoStr = "$" + Utils.round2Decimals(monto, locale);
						
						Link<CajaChica> editLink = new Link<CajaChica>("editar", item.getModel()) {
							private static final long serialVersionUID = 1L;
							@Override
							public void onClick() {
								CajaChica detalleCajaChicaSeleccionado = (CajaChica) getModelObject();
								setResponsePage(new EditarCajaChicaPage(new PageParameters(),new Integer(detalleCajaChicaSeleccionado.getId())));
							}
						};
						editLink.setEnabled(!isUsuarioLogueadoRolSoloLectura());
						item.add(editLink);
						
						item.add(new Label("fecha", fecha));
						item.add(new Label("detalle", detalle));
						item.add(new Label("solicitante", solicitanteStr));
						item.add(new Label("monto", montoStr));
						
					}
				};
				
				double totalMensual = cajaChicaService.calculateMontoTotal(mes);
				Label totalMensualLabel = new Label("totalMensual", "$" + Utils.round2Decimals(totalMensual, locale));
								
				item.add(cajaChicaDataView);
				item.add(totalMensualLabel);
				
			}
		};
		
		double totalAnual = cajaChicaService.calculateMontoTotalAnual(añoSeleccionado);
		totalAnualLabel = new Label("totalAnual", "$" + Utils.round2Decimals(totalAnual, locale));
		totalAnualLabel.setOutputMarkupPlaceholderTag(true);
		container.add(totalAnualLabel);				
		container.add(mesesDataView);
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
						//si el mes es menor a Diciembre, entonces muestro un mes mas
						if (mesLoop<11) {
							mesLoop++;
						}
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