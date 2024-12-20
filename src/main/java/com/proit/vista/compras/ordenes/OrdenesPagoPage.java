package com.proit.vista.compras.ordenes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
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
import com.proit.modelo.compras.FacturaCompra;
import com.proit.modelo.compras.FacturaCompraOrdenPago;
import com.proit.modelo.compras.OrdenPago;
import com.proit.modelo.compras.Proveedor;
import com.proit.servicios.compras.OrdenPagoService;
import com.proit.utils.Constantes;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.AJAXDownload;
import com.proit.wicket.components.ProveedorSearchAutoCompleteTextField;
import com.proit.wicket.dataproviders.FacturaOrdenPagoDataProvider;

@AuthorizeInstantiation({"Administrador","Solo Lectura","Desarrollador"})
public class OrdenesPagoPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(OrdenesPagoPage.class.getName());
	
	private static final int RESULTADOS_POR_PAGINA = 10;
	
	private OrdenPagoService ordenPagoService;
	
	private AutoCompleteTextField<String> proveedorSearchAutoComplete;
	
	private TextField<String> nroOPTextField;
	
	private AjaxDatePicker ajaxDatePicker;
	
	private AjaxPagingNavigator navigator;
	
	private IModel<Boolean> soloSFYConProvModel;
	
	private IModel<Boolean> soloFacturasDePagoParcialModel;
	
	public OrdenesPagoPage(final PageParameters parameters) {
		super(parameters);
				
		ordenPagoService = new OrdenPagoService();

		WebMarkupContainer listadoContainer = new WebMarkupContainer("listadoContainer");
		listadoContainer.setOutputMarkupPlaceholderTag(true);
		
		WebMarkupContainer msjFiltrar = new WebMarkupContainer("msjFiltrar");
		msjFiltrar.setOutputMarkupPlaceholderTag(true);
		
		final WebMarkupContainer dataContainer = new WebMarkupContainer("dataContainer");
		dataContainer.setOutputMarkupPlaceholderTag(true);
		listadoContainer.add(dataContainer);
		
		crearOPLink();
		
		addFilters(listadoContainer, msjFiltrar);
		
		DataView<FacturaCompraOrdenPago> dataView = addOrdenPagoList(dataContainer);
		addNavigator(listadoContainer, dataView);
		
		listadoContainer.setVisible(false); //Por defecto no lo muestro
		add(listadoContainer);
		add(msjFiltrar);
		
		mostrarAlertaEnPantallaSiCorresponde(parameters);
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
		
		bajarArchivoSiCorresponde(parameters);
	}
	
	private void crearOPLink() {
		Link<OrdenPago> crearOPLink = new Link<OrdenPago>("crearOPLink", Model.of(new OrdenPago())) {			
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage(new RegistrarOrdenPagoPage());
			}
		};
		crearOPLink.setEnabled(!isUsuarioLogueadoRolSoloLectura());
		add(crearOPLink);
	}

	private void bajarArchivoSiCorresponde(final PageParameters parameters) {
		final String fileName = parameters.get("FileName").toString();
		
		final AJAXDownload download = createAjaxDownload(fileName);
		
		AjaxEventBehavior event = new AjaxEventBehavior("onload") {
			private static final long serialVersionUID = 1L;
			@Override
		    protected void onEvent(final AjaxRequestTarget target) {
				if ( fileName!= null && ! fileName.isEmpty() ) {
					download.initiate(target);
				}
		    }
		};
		add(event);
		add(download);
	}

	private void addFilters(final WebMarkupContainer container, final WebMarkupContainer msjFiltrar) {
		Form<?> form = new Form<Void>("form");
		add(form);
		
		AutoCompleteTextField<String> proveedorSearchAutoComplete = crearProveedorSearchAutoCompleteTextField(container, msjFiltrar);
		form.add(proveedorSearchAutoComplete);
		
		AjaxLink<String> todosLosProveedoresLink = crearTodosProveedoresLink(container, msjFiltrar, "todosProveedores");
		form.add(todosLosProveedoresLink);
		
		nroOPTextField = new TextField<String>("filtroNro", Model.of(""));
		nroOPTextField.setOutputMarkupId(true);
		form.add(nroOPTextField);
		
		AjaxButton buscarOPLink = crearFindOPLink(container, msjFiltrar, "buscar");
		form.add(buscarOPLink);
		
		AjaxLink<String> todasOPLink = crearTodasOPLink(container, msjFiltrar, "todasOP");
		form.add(todasOPLink);
		
		crearDatePicker(container, msjFiltrar);
		form.add(ajaxDatePicker);
		
		AjaxLink<String> hoyLink = crearDiaLink(container, msjFiltrar, "hoy", Utils.firstMillisecondOfDay(Calendar.getInstance()).getTime());
		form.add(hoyLink);
		
		AjaxLink<String> todosLosDiasLink = crearDiaLink(container, msjFiltrar, "todasFechas", null);
		form.add(todosLosDiasLink);

		soloSFYConProvModel = Model.of(false);
		CheckBox checkSoloSFYConProv = crearFiltroCheckBox(container, msjFiltrar, soloSFYConProvModel, "soloSFYConProv");
		form.add(checkSoloSFYConProv);

		soloFacturasDePagoParcialModel = Model.of(false);
		CheckBox checkSoloFacturasDePagoParcial = crearFiltroCheckBox(container, msjFiltrar, soloFacturasDePagoParcialModel, "soloFacturasDePagoParcial");
		form.add(checkSoloFacturasDePagoParcial);
	}

	private DataView<FacturaCompraOrdenPago> addOrdenPagoList(WebMarkupContainer container) {
		IDataProvider<FacturaCompraOrdenPago> facturaOrdenPagoDataProvider = getFacturaOrdenPagoProvider();
		
		DataView<FacturaCompraOrdenPago> dataView = new DataView<FacturaCompraOrdenPago>("listaFacturaOrdenPago", facturaOrdenPagoDataProvider, RESULTADOS_POR_PAGINA) {
			private static final long serialVersionUID = 1L;
			private static final String nombreModal = "eliminarModal";
			private int idIncremental = 1; // este atributo es utilizado para obtener un id unico para el componente modal de bootstrap.
			
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
				Label totalAPagarLbl = new Label("totalAPagar", "$ " + Utils.round2Decimals(ordenPagoService.calculateTotalPagos(ordenPago.getListadoPagos()), ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale()) );
				item.add(totalAPagarLbl);
				
				Link<FacturaCompraOrdenPago> verDetallesLink = new Link<FacturaCompraOrdenPago>("verDetalles", item.getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						FacturaCompraOrdenPago facturaOrdenPagoSeleccionado = (FacturaCompraOrdenPago) getModelObject();
						setResponsePage(new DetallesOrdenesPagoPage(facturaOrdenPagoSeleccionado));
					}
				};
				item.add(verDetallesLink);
				
				Button botonIntentarEliminar = new Button("botonIntentarEliminar");
				botonIntentarEliminar.add(new AttributeModifier("data-target", "#" + nombreModal+idIncremental));
				
				String textoEliminar = ordenPago.isBorrado() ? " ya eliminada" : "";
				Label botonIntentarEliminarLabel = new Label("textoEliminar", textoEliminar);
				botonIntentarEliminar.add(botonIntentarEliminarLabel);
				botonIntentarEliminar.setEnabled( ! ordenPago.isBorrado() && ! isUsuarioLogueadoRolSoloLectura() );
				item.add(botonIntentarEliminar);
				
				String facturasAsociadas = "";
				for (FacturaCompra factura : ordenPago.getListadoFacturas()) {
					if (factura.getNro().startsWith(Constantes.PREFIX_NRO_FACTURA_SF)){
						facturasAsociadas = "<Sin Factura>";
					} else {
						facturasAsociadas += facturasAsociadas.isEmpty() ? factura.getNro() : ", " +factura.getNro();
					}
				}
				facturasAsociadas = facturasAsociadas.isEmpty()?"<Sin Factura>":facturasAsociadas;
				WebMarkupContainer webMarkupContainer = new WebMarkupContainer("eliminarModal");
				webMarkupContainer.add(new Label("ordenPagoAEliminar", ordenPago.getNro()));
				webMarkupContainer.add(new Label("facturasAsociadas", facturasAsociadas));
				webMarkupContainer.add(new Link<FacturaCompraOrdenPago>("eliminar", item.getModel()) {
					
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						FacturaCompraOrdenPago facturaOrdenPagoSeleccionada = (FacturaCompraOrdenPago) getModelObject();
						try {
							boolean fueBorrada = ordenPagoService.delete(facturaOrdenPagoSeleccionada);
							String textoPorPantalla = "La Orden de Pago "+facturaOrdenPagoSeleccionada.getOrdenPago().getNro() + " ha sido eliminada.";
							if ( ! fueBorrada ) {
								textoPorPantalla = "La Orden de Pago " + facturaOrdenPagoSeleccionada.getOrdenPago().getNro() + " no fue eliminada. Se encontró otra OP posterior asociada a la/s factura/s correspondiente/s.";
							}
							crearAlerta(textoPorPantalla, !fueBorrada , false);
						} catch(Exception e) {
							log.error(e.getMessage(), e);
							String textoPorPantalla = "La Orden de Pago " + facturaOrdenPagoSeleccionada.getOrdenPago().getNro() + " no pudo ser eliminada correctamente. Por favor, vuelva a intentarlo.";
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
	
	private AjaxButton crearFindOPLink(final WebMarkupContainer container, final WebMarkupContainer msjFiltrar, String nombreLink) {
		final AjaxButton filterButton = new AjaxButton("buscar") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit();
				actualizarContainer(container, msjFiltrar, target);
			}
		};
		return filterButton;
	}
	
	private AjaxLink<String> crearTodasOPLink(final WebMarkupContainer container, final WebMarkupContainer msjFiltrar, String nombreLink) {
		AjaxLink<String> link = new AjaxLink<String>(nombreLink) {			
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				nroOPTextField.setModelObject(null);
				target.add(nroOPTextField);
				actualizarContainer(container, msjFiltrar, target);
			}
		};
		return link;
	}
	
	private CheckBox crearFiltroCheckBox(final WebMarkupContainer container, final WebMarkupContainer msjFiltrar, IModel<Boolean> model, String checkBoxName) {
		CheckBox checkbox = new CheckBox(checkBoxName, model);
		checkbox.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;

			protected void onUpdate(AjaxRequestTarget target) {
				actualizarContainer(container, msjFiltrar, target);
            }
        });
		return checkbox;
	}
	
	
	private IDataProvider<FacturaCompraOrdenPago> getFacturaOrdenPagoProvider() {
		Integer idEvento = null;
		boolean miTrue = true;
		Integer idPlanCuenta = null;
		return new FacturaOrdenPagoDataProvider(proveedorSearchAutoComplete.getModel(), nroOPTextField.getModel(), ajaxDatePicker.getModel(), Model.of(idEvento), 
				soloSFYConProvModel, soloFacturasDePagoParcialModel, Model.of(miTrue), null, Model.of(idPlanCuenta));
	}
	
	private void addNavigator(final WebMarkupContainer listadoContainer, DataView<FacturaCompraOrdenPago> dataView) {
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
