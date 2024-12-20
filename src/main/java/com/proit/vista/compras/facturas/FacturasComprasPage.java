package com.proit.vista.compras.facturas;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.ui.form.autocomplete.AutoCompleteTextField;
import com.googlecode.wicket.jquery.ui.form.datepicker.AjaxDatePicker;
import com.proit.modelo.compras.EstadoFacturaCompra;
import com.proit.modelo.compras.FacturaCompra;
import com.proit.servicios.compras.FacturaCompraService;
import com.proit.servicios.compras.OrdenPagoService;
import com.proit.utils.ExcelGeneratorListadoFacturasCompra;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.AJAXDownload;
import com.proit.wicket.components.ProveedorSearchAutoCompleteTextField;
import com.proit.wicket.dataproviders.FacturasCompraDataProvider;

@AuthorizeInstantiation({"Administrador","Solo Lectura","Desarrollador"})
public class FacturasComprasPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(FacturasComprasPage.class.getName());
	
	private static final int RESULTADOS_POR_PAGINA = 10;
	
	private FacturaCompraService facturaService;
	private OrdenPagoService ordenPagoService;
	
	private AutoCompleteTextField<String> proveedorSearchAutoComplete;
	
	private AjaxDatePicker ajaxDatePicker;
	
	private AjaxPagingNavigator navigator;
	
	private IModel<Boolean> soloFacturasPendientesYParcialesModel;
		
	public FacturasComprasPage(final PageParameters parameters) {
		super(parameters);
				
		facturaService = new FacturaCompraService();
		ordenPagoService = new OrdenPagoService();

		WebMarkupContainer listadoContainer = new WebMarkupContainer("listadoContainer");
		listadoContainer.setOutputMarkupPlaceholderTag(true);
		
		WebMarkupContainer msjFiltrar = new WebMarkupContainer("msjFiltrar");
		msjFiltrar.setOutputMarkupPlaceholderTag(true);
		
		final WebMarkupContainer dataContainer = new WebMarkupContainer("dataContainer");
		dataContainer.setOutputMarkupPlaceholderTag(true);
		listadoContainer.add(dataContainer);
		
		crearFacturaLink();
		
		addFilters(listadoContainer, msjFiltrar);
		
		agregarBotonExportarReporte(listadoContainer);
		
		DataView<FacturaCompra> dataView = addFacturaList(dataContainer);
		addNavigator(listadoContainer, dataView);
		
		listadoContainer.setVisible(false); //Por defecto no lo muestro
		add(listadoContainer);
		add(msjFiltrar);
				
		mostrarAlertaEnPantallaSiCorresponde(parameters);
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
	}
	
	private void crearFacturaLink() {
		Link<FacturaCompra> crearFacturaLink = new Link<FacturaCompra>("crearFacturaLink", Model.of(new FacturaCompra())) {			
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage(new RegistrarFacturaPage(new PageParameters()));
			}
		};
		crearFacturaLink.setEnabled(!isUsuarioLogueadoRolSoloLectura());
		add(crearFacturaLink);
	}

	private void agregarBotonExportarReporte(WebMarkupContainer listadoContainer) {
		final ExcelGeneratorListadoFacturasCompra excelGenerator = new ExcelGeneratorListadoFacturasCompra(getRuntimeConfigurationType());
		final String excelName = excelGenerator.getFileName();
		final AJAXDownload download = createAjaxDownload(excelName);
		AjaxLink<FacturaCompra> generarExcelLink = new AjaxLink<FacturaCompra>("generarExcel") {			
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				Calendar fecha = null;
				if (ajaxDatePicker.getModel().getObject() != null){
					fecha = Calendar.getInstance();
					fecha.setTime(ajaxDatePicker.getModel().getObject());
				}
				ArrayList<FacturaCompra> listaFacturas = (ArrayList<FacturaCompra>) facturaService.getFacturas(proveedorSearchAutoComplete.getModel().getObject(), fecha ,soloFacturasPendientesYParcialesModel.getObject(), null, null, null);
				List<Object[]> totalesPorProveedor = facturaService.getTotalesPorProveedor(proveedorSearchAutoComplete.getModel().getObject(), fecha ,soloFacturasPendientesYParcialesModel.getObject());
				double totalDeuda = 0;
				double totalPagado = 0;
				for (Object[] obj : totalesPorProveedor) {
					totalDeuda +=  ((BigDecimal) obj[3]).doubleValue();
					totalPagado +=  ((BigDecimal) obj[4]).doubleValue();					
				}
				excelGenerator.generarExcel(listaFacturas, excelName, totalDeuda, totalPagado);
				download.initiate(target);
			}
		};
		listadoContainer.add(generarExcelLink);
		listadoContainer.add(download);
	}

	private void addFilters(final WebMarkupContainer container, final WebMarkupContainer msjFiltrar) {
		AutoCompleteTextField<String> proveedorSearchAutoComplete = crearProveedorSearchAutoCompleteTextField(container, msjFiltrar);
		add(proveedorSearchAutoComplete);
		
		AjaxLink<String> todosLosProveedoresLink = crearTodosProveedoresLink(container, msjFiltrar, "todosProveedores");
		add(todosLosProveedoresLink);
		
		crearDatePicker(container, msjFiltrar);
		add(ajaxDatePicker);
		
		AjaxLink<String> hoyLink = crearDiaLink(container, msjFiltrar, "hoy", Utils.firstMillisecondOfDay(Calendar.getInstance()).getTime());
		add(hoyLink);
		
		AjaxLink<String> todosLosDiasLink = crearDiaLink(container, msjFiltrar, "todos", null);
		add(todosLosDiasLink);
		
		CheckBox checkSoloFacturasPendientesYParciales = crearSoloFacturasPendientesYParcialesCheckBox(container, msjFiltrar);
		add(checkSoloFacturasPendientesYParciales);
	}

	private DataView<FacturaCompra> addFacturaList(WebMarkupContainer container) {
		IDataProvider<FacturaCompra> facturasDataProvider = getFacturasProvider();
		
		DataView<FacturaCompra> dataView = new DataView<FacturaCompra>("listaFacturas", facturasDataProvider, RESULTADOS_POR_PAGINA) {
			private static final long serialVersionUID = 1L;
			private static final String nombreModal = "eliminarModal";
			private int idIncremental = 1;
			
			@Override
			protected void populateItem(Item<FacturaCompra> item) {
				DateFormat dateFormatFecha = new SimpleDateFormat("dd-MM-yyyy");
				Locale locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
				String fecha = dateFormatFecha.format(item.getModelObject().getFecha().getTime());				
				String razonSocialConCUIT = item.getModelObject().getProveedor().getRazonSocialConCUIT();
				double totalNetoFactura = ordenPagoService.calculateTotalNetoFactura(item.getModelObject());
				String faltaPagar = item.getModelObject().getEstadoFactura().equals(EstadoFacturaCompra.PAGADA_PARCIAL) ? 
						" (Faltante: $" + Utils.round2Decimals(totalNetoFactura, locale) + ")"		:		"";
				item.add(new Label("fecha", fecha));
				item.add(new Label("proveedor", razonSocialConCUIT ));
				item.add(new Label("nro", item.getModelObject().getNro()));
				item.add(new Label("estado", item.getModelObject().getEstadoFactura().getNombre() + faltaPagar));
				String total = (item.getModelObject().isNotaCredito()?"-":"") + Utils.round2Decimals(item.getModelObject().calculateTotal(), locale);
				item.add(new Label("total", total));
				
				Link<FacturaCompra> verDetallesLink = new Link<FacturaCompra>("verDetalles", item.getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						FacturaCompra facturaSeleccionada = (FacturaCompra) getModelObject();
						setResponsePage(new RegistrarFacturaPage(new PageParameters(), true, facturaSeleccionada));
					}
				};
				item.add(verDetallesLink);
				
				Button botonIntentarEliminar = new Button("botonIntentarEliminar");
				botonIntentarEliminar.add(new AttributeModifier("data-target", "#" + nombreModal+idIncremental));
				botonIntentarEliminar.setEnabled(!isUsuarioLogueadoRolSoloLectura());
				item.add(botonIntentarEliminar);
				
				WebMarkupContainer webMarkupContainer = new WebMarkupContainer("eliminarModal");
				webMarkupContainer.add(new Label("facturaAEliminar", item.getModelObject().getNro()));
				webMarkupContainer.add(new Link<FacturaCompra>("eliminar", item.getModel()) {
					
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						FacturaCompra facturaSeleccionada = (FacturaCompra) getModelObject();
						try {
							String whereIsUsed = facturaService.delete(facturaSeleccionada);
							String textoPorPantalla = "La Factura "+facturaSeleccionada.getNro() + " ha sido eliminada.";
							if ( !whereIsUsed.isEmpty() ) {
								textoPorPantalla = "La Factura " + facturaSeleccionada.getNro() + " no fue eliminada. " + whereIsUsed;
							}							
							crearAlerta(textoPorPantalla, !whereIsUsed.isEmpty() , false);
						} catch(Exception e) {
							log.error(e.getMessage(), e);
							String textoPorPantalla = "La Factura " + facturaSeleccionada.getNro() + " no pudo ser eliminada correctamente. Por favor, vuelva a intentarlo.";
							crearAlerta(textoPorPantalla, true, false);
						}
					}
				});
				
				webMarkupContainer.setMarkupId(nombreModal+idIncremental);
				webMarkupContainer.setVisible(botonIntentarEliminar.isVisible()&&botonIntentarEliminar.isEnabled());
				idIncremental++;
				
				item.add(webMarkupContainer);
			}
		};
		
		container.add(dataView);
		return dataView;
	}
	
	private AutoCompleteTextField<String> crearProveedorSearchAutoCompleteTextField(final WebMarkupContainer container, final WebMarkupContainer msjFiltrar) {
		proveedorSearchAutoComplete = new ProveedorSearchAutoCompleteTextField("proveedorSearchAutoComplete", new Model<String>()){
			private static final long serialVersionUID = 1L;			
			@Override
			protected void onSelected(AjaxRequestTarget target){
				actualizarContainer(container, msjFiltrar, target);
			}
			
		};
		/*proveedorSearchAutoComplete.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;

			protected void onUpdate(AjaxRequestTarget target) {
				actualizarContainer(container, target);
            }
        });*/ //ESTO NO ANDABA EN CHROME
		
		return proveedorSearchAutoComplete;
	}
	
	private AjaxDatePicker crearDatePicker(final WebMarkupContainer container, final WebMarkupContainer msjFiltrar) {
		Options options = new Options();
		ajaxDatePicker = new AjaxDatePicker("datepicker", new Model<Date>(), "dd/MM/yyyy", options) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onValueChanged(IPartialPageRequestHandler handler) {
				actualizarContainer(container, msjFiltrar, handler);
			}
		};
		return ajaxDatePicker;
	}
	
	private AjaxLink<String> crearDiaLink(final WebMarkupContainer container, final WebMarkupContainer msjFiltrar, String nombreLink, final Date date) {
		AjaxLink<String> hoyLink = new AjaxLink<String>(nombreLink) {			
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				ajaxDatePicker.setModelObject(date);
				target.add(ajaxDatePicker);
				actualizarContainer(container, msjFiltrar, target);
			}
		};
		return hoyLink;
	}
	
	private AjaxLink<String> crearTodosProveedoresLink(final WebMarkupContainer container, final WebMarkupContainer msjFiltrar, String nombreLink) {
		AjaxLink<String> hoyLink = new AjaxLink<String>(nombreLink) {			
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				proveedorSearchAutoComplete.setModelObject(null);
				target.add(proveedorSearchAutoComplete);
				actualizarContainer(container, msjFiltrar, target);
			}
		};
		return hoyLink;
	}
	
	private CheckBox crearSoloFacturasPendientesYParcialesCheckBox(final WebMarkupContainer container, final WebMarkupContainer msjFiltrar) {
		soloFacturasPendientesYParcialesModel = Model.of(false);
		CheckBox checkSoloFacturasPendientesYParciales = new CheckBox("soloFacturasPendientesYParciales", soloFacturasPendientesYParcialesModel);
		checkSoloFacturasPendientesYParciales.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;

			protected void onUpdate(AjaxRequestTarget target) {
				actualizarContainer(container, msjFiltrar, target);
            }
        });
		return checkSoloFacturasPendientesYParciales;
	}
	
	private IDataProvider<FacturaCompra> getFacturasProvider() {
		return new FacturasCompraDataProvider(proveedorSearchAutoComplete.getModel(), ajaxDatePicker.getModel(),soloFacturasPendientesYParcialesModel, null, null);
	}
	
	private void addNavigator(WebMarkupContainer listadoContainer, DataView<FacturaCompra> dataView) {
		navigator = new AjaxPagingNavigator("paginator", dataView);
		listadoContainer.add(navigator);
	}
	
	private void actualizarContainer(WebMarkupContainer container, WebMarkupContainer msjFiltrar, IPartialPageRequestHandler target) {
		container.setVisible(true);//Una vez utilizado el filtro lo empiezo a mostrar siempre el listado
		msjFiltrar.setVisible(false);//Una vez utilizado el filtro no lo muestro mas al msj
		target.add(container);
		target.add(msjFiltrar);
		target.add(navigator);
	}
}
