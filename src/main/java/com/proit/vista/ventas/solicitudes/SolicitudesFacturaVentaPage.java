package com.proit.vista.ventas.solicitudes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.Strings;

import com.proit.modelo.Rol;
import com.proit.modelo.Usuario;
import com.proit.modelo.ventas.SolicitudFacturaVenta;
import com.proit.servicios.UsuarioService;
import com.proit.servicios.ventas.SolicitudFacturaVentaService;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.dataproviders.SolicitudesFacturaVentaDataProvider;

@AuthorizeInstantiation({"Administrador","Desarrollador","Editor Solicitudes Factura"})
public class SolicitudesFacturaVentaPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	private static final int RESULTADOS_POR_PAGINA = 10;
		
	private AjaxPagingNavigator navigator;
	
	private TextField<String> eventoTextField;
	
	private IModel<Usuario> usuarioSolicitanteModel;
	@SuppressWarnings("unused")
	private Usuario usuarioSolicitante;
	private IModel<Boolean> soloSolicitudesPendientesModel;
	private IModel<Boolean> soloSolicitudesOKModel;
	
	private boolean soloMostrarMisSolicitudes;
	
	private Locale locale;
	
	public SolicitudesFacturaVentaPage(final PageParameters parameters) {
		this(parameters, false);
	}
	
	public SolicitudesFacturaVentaPage(final PageParameters parameters, boolean soloMostrarMisSolicitudes) {
		super(parameters);
		
		locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
		
		this.soloMostrarMisSolicitudes = soloMostrarMisSolicitudes;
		
		add(new Label("tituloPagina", soloMostrarMisSolicitudes ? "Mis Solicitudes de Factura Venta" : "Solicitudes de Factura Venta" ));
		
		if (soloMostrarMisSolicitudes) {
			usuarioSolicitante = getUsuarioLogueado();
		}

		final WebMarkupContainer container = new WebMarkupContainer("dataContainer");
		container.setOutputMarkupPlaceholderTag(true);
		add(container);
				
		addFilters(container);
		
		crearSolicitudLink();
		
		mostrarTotalSolicitudesFacturaPendientes(soloMostrarMisSolicitudes);
		
		DataView<SolicitudFacturaVenta> dataView = addSolicitudFacturaList(container);
		addNavigator(container, dataView);
		
		mostrarAlertaEnPantallaSiCorresponde(parameters);
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
	}
	
	private void addFilters(final WebMarkupContainer container) {
		eventoTextField = new TextField<String>("filtroEvento", Model.of(""));
		eventoTextField.setOutputMarkupId(true);
		
		final AjaxButton filterButton = new AjaxButton("buscar") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit();
				actualizarContainer(container, target);
			}
		};
		
		AjaxLink<String> todosLosEventosLink = crearTodosEventosLink(container, "todosEventos");
		
		WebMarkupContainer solicitanteFilterContainer = new WebMarkupContainer("solicitanteFilterContainer");
		DropDownChoice<Usuario> usuarioSolicitanteDropDownChoice = crearUsuarioSolicitanteDropDownChoice(container);
		solicitanteFilterContainer.add(usuarioSolicitanteDropDownChoice);
		solicitanteFilterContainer.setVisible(!soloMostrarMisSolicitudes);		
		
		soloSolicitudesPendientesModel = Model.of(true);
		soloSolicitudesOKModel = Model.of(false);
		CheckBox checkSoloSolicitudesPendientes = crearFiltroCheckBox(container, soloSolicitudesPendientesModel, "soloSolicitudesPendientes");
		CheckBox checkSoloSolicitudesOK = crearFiltroCheckBox(container, soloSolicitudesOKModel, "soloSolicitudesOK");
		WebMarkupContainer soloSolicitudesOKFiltro = new WebMarkupContainer("soloSolicitudesOKFiltro");
		soloSolicitudesOKFiltro.add(checkSoloSolicitudesOK);
		soloSolicitudesOKFiltro.setVisible(!soloMostrarMisSolicitudes);
				
		Form<?> form = new Form<Void>("form");
		add(form);
		form.add(eventoTextField);
		form.add(todosLosEventosLink);
		form.add(filterButton);
		form.add(solicitanteFilterContainer);
		form.add(checkSoloSolicitudesPendientes);
		form.add(soloSolicitudesOKFiltro);
	}
	
	private void crearSolicitudLink() {
		Link<SolicitudFacturaVenta> crearSolicitudFacturaLink = new Link<SolicitudFacturaVenta>("crearSolicitudFacturaLink", Model.of(new SolicitudFacturaVenta())) {			
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage(new RegistrarSolicitudFacturaVentaPage());
			}
		};
		crearSolicitudFacturaLink.setVisible(soloMostrarMisSolicitudes);
		add(crearSolicitudFacturaLink);
	}
	
	private void mostrarTotalSolicitudesFacturaPendientes(boolean soloMostrarMisSolicitudes) {
		SolicitudFacturaVentaService solicitudFacturaVentaService = new SolicitudFacturaVentaService();
		WebMarkupContainer totalSolicitudesFacturaPendientesContainer = new WebMarkupContainer("totalSolicitudesFacturaPendientesContainer");
		String totalStr = Utils.round2Decimals(solicitudFacturaVentaService.getTotalSolicitudesFacturaPendientes(), locale);
		Label totalSolicitudesFacturaPendientesLbl = new Label("totalSolicitudesFacturaPendientes", "$" + totalStr);
		totalSolicitudesFacturaPendientesContainer.setVisible(!soloMostrarMisSolicitudes);
		totalSolicitudesFacturaPendientesContainer.add(totalSolicitudesFacturaPendientesLbl);
		add(totalSolicitudesFacturaPendientesContainer);
	}
	
	private DataView<SolicitudFacturaVenta> addSolicitudFacturaList(WebMarkupContainer container) {
		IDataProvider<SolicitudFacturaVenta> solicitudesFacturaDataProvider = getSolicitudesFacturaProvider();
		
		DataView<SolicitudFacturaVenta> dataView = new DataView<SolicitudFacturaVenta>("listaSolicitudesFactura", solicitudesFacturaDataProvider, RESULTADOS_POR_PAGINA) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<SolicitudFacturaVenta> item) {
				DateFormat dateFormatFecha = new SimpleDateFormat("dd-MM-yyyy");
				final SolicitudFacturaVenta solicitudFactura = item.getModelObject();
				String fecha = dateFormatFecha.format(solicitudFactura.getFecha().getTime());
				
				item.add(new Label("nroSolicitud", solicitudFactura.getNro()));
				item.add(new Label("fecha", fecha));
				
				WebMarkupContainer colSolicitanteValue = new WebMarkupContainer("colSolicitanteValue");
				Label labelSolicitante = new Label("solicitante", solicitudFactura.getUsuarioSolicitante().getNombreCompleto());
				colSolicitanteValue.add(labelSolicitante);
				colSolicitanteValue.setVisible(!soloMostrarMisSolicitudes);
				item.add(colSolicitanteValue);
				
				item.add(new Label("cliente", solicitudFactura.getCliente().getRazonSocial()));				
				item.add(new Label("total", "$" + Utils.round2Decimals(solicitudFactura.calculateSubtotal(), locale)));				
				item.add(new Label("evento", solicitudFactura.getEvento().getNombre()));
				item.add(new Label("estado", solicitudFactura.getEstadoSolicitudFactura().getNombre()));
				
				final Boolean ok = !Strings.isEmpty(solicitudFactura.getNroOrdenCompra()) || !Strings.isEmpty(solicitudFactura.getNroRequisicion());
				WebMarkupContainer colOKValue = new WebMarkupContainer("colOKValue");
				Label labelOK = new Label("ok"){
					private static final long serialVersionUID = 1L;
					@Override
				    protected void onComponentTag(final ComponentTag tag){
						super.onComponentTag(tag);
				        if (ok) {
				        	tag.put("class", "glyphicon glyphicon-ok text-success");
				        } else {
				        	tag.put("class", "glyphicon glyphicon-minus text-danger");
				        }
					}
				};
				colOKValue.add(labelOK);
				colOKValue.setVisible(!soloMostrarMisSolicitudes);
				item.add(colOKValue);
								
				Link<SolicitudFacturaVenta> verDetallesLink = new Link<SolicitudFacturaVenta>("verDetalles", item.getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						SolicitudFacturaVenta solicitudSeleccionada = (SolicitudFacturaVenta) getModelObject();
						setResponsePage(new RegistrarSolicitudFacturaVentaPage(true, solicitudSeleccionada));
					}
				};
				item.add(verDetallesLink);
				
				Link<SolicitudFacturaVenta> editLink = new Link<SolicitudFacturaVenta>("editar", item.getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						SolicitudFacturaVenta solicitudSeleccionada = (SolicitudFacturaVenta) getModelObject();
						setResponsePage(new RegistrarSolicitudFacturaVentaPage(false, solicitudSeleccionada));
					}
				};
				editLink.setVisible(solicitudFactura.isPendiente());
				item.add(editLink);
				
				Link<SolicitudFacturaVenta> convertirAFacVtaLink = new Link<SolicitudFacturaVenta>("convertirAFacVta", item.getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						SolicitudFacturaVenta solicitudSeleccionada = (SolicitudFacturaVenta) getModelObject();
						setResponsePage(new ConvertirAFacturaVentaPage(solicitudSeleccionada));
					}
				};
				item.add(convertirAFacVtaLink);
				convertirAFacVtaLink.setVisible(!soloMostrarMisSolicitudes && solicitudFactura.isPendiente() && !isUsuarioLogueadoRolEditorSolicitudesFactura());
				
			}
		};
		
		WebMarkupContainer colSolicitante = new WebMarkupContainer("colSolicitante");
		colSolicitante.setVisible(!soloMostrarMisSolicitudes);
		container.add(colSolicitante);
		
		WebMarkupContainer colOK = new WebMarkupContainer("colOK");
		colOK.setVisible(!soloMostrarMisSolicitudes);
		container.add(colOK);
		
		WebMarkupContainer colConversion = new WebMarkupContainer("colConversion");
		colConversion.setVisible(!soloMostrarMisSolicitudes && !isUsuarioLogueadoRolEditorSolicitudesFactura());
		container.add(colConversion);
		
		container.add(dataView);
		return dataView;
	}
		
	private IDataProvider<SolicitudFacturaVenta> getSolicitudesFacturaProvider() {
		Integer idEvento = null;
		return new SolicitudesFacturaVentaDataProvider(eventoTextField.getModel(), usuarioSolicitanteModel, soloSolicitudesPendientesModel, soloSolicitudesOKModel, Model.of(idEvento));
	}
	
	private void addNavigator(final WebMarkupContainer dataContainer, DataView<SolicitudFacturaVenta> dataView) {
		navigator = new AjaxPagingNavigator("paginator", dataView);
		add(navigator);
	}
	
	private CheckBox crearFiltroCheckBox(final WebMarkupContainer container, IModel<Boolean> model, String checkBoxName) {
		CheckBox checkbox = new CheckBox(checkBoxName, model);
		checkbox.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;

			protected void onUpdate(AjaxRequestTarget target) {
				actualizarContainer(container, target);
            }
        });
		return checkbox;
	}
	
	private void actualizarContainer(final WebMarkupContainer container, AjaxRequestTarget target) {
		target.add(container);
		target.add(navigator);
	}
	
	private DropDownChoice<Usuario> crearUsuarioSolicitanteDropDownChoice(final WebMarkupContainer container) {
		UsuarioService usuarioService = new UsuarioService();
		List<Usuario> solicitantes = usuarioService.getUsuariosByRol(Rol.SOLICITANTE_FACTURAS_VENTAS);
		
		usuarioSolicitanteModel = new PropertyModel<Usuario>(this, "usuarioSolicitante");
		DropDownChoice<Usuario> solicitanteDropDownChoice = new DropDownChoice<Usuario>("solicitante", usuarioSolicitanteModel, solicitantes, new ChoiceRenderer<Usuario>("nombreCompleto"));
		
		solicitanteDropDownChoice.setNullValid(true);
		solicitanteDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;

			protected void onUpdate(AjaxRequestTarget target) {
				actualizarContainer(container, target);
            }
        });
		
		return solicitanteDropDownChoice;
	}
	
	private AjaxLink<String> crearTodosEventosLink(final WebMarkupContainer container, String nombreLink) {
		AjaxLink<String> hoyLink = new AjaxLink<String>(nombreLink) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				eventoTextField.setModelObject(null);
				target.add(eventoTextField);
				actualizarContainer(container, target);
			}
		};
		return hoyLink;
	}
	
}
