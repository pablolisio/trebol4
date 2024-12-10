package com.proit.vista.compras.solicitudes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxEventBehavior;
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

import com.googlecode.wicket.jquery.ui.form.autocomplete.AutoCompleteTextField;
import com.proit.modelo.Rol;
import com.proit.modelo.Usuario;
import com.proit.modelo.compras.FacturaSolicitudPago;
import com.proit.modelo.compras.PagoSolicitudPago;
import com.proit.modelo.compras.SolicitudPago;
import com.proit.servicios.UsuarioService;
import com.proit.servicios.compras.SolicitudPagoService;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.vista.compras.solicitudes.conversion.ConvertirAOPPage1;
import com.proit.vista.compras.solicitudes.conversion.ConvertirAOPPage2;
import com.proit.vista.compras.solicitudes.conversion.ConvertirAOPPage3CPySF;
import com.proit.vista.compras.solicitudes.conversion.ConvertirAOPPage3Normal;
import com.proit.vista.compras.solicitudes.conversion.ConvertirAOPPage3SPySF;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.AJAXDownload;
import com.proit.wicket.components.NroFacturaSolicitudPagoSearchAutoCompleteTextField;
import com.proit.wicket.dataproviders.SolicitudesPagoDataProvider;

@AuthorizeInstantiation({"Administrador","Desarrollador","Editor Solicitudes Factura"})
public class SolicitudesPagoPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	private static final int RESULTADOS_POR_PAGINA = 10;
		
	private AjaxPagingNavigator navigator;	
	
	private AutoCompleteTextField<String> nroFacturaSearchAutoComplete;
	private TextField<String> eventoTextField;
	private IModel<Usuario> usuarioSolicitanteModel;
	@SuppressWarnings("unused")
	private Usuario usuarioSolicitante;
	private IModel<Boolean> soloSolicitudesPendientesModel;
	
	private boolean soloMostrarMisSolicitudes;
	
	private Locale locale;
	
	public SolicitudesPagoPage(final PageParameters parameters) {
		this(parameters, false);
	}
	
	public SolicitudesPagoPage(final PageParameters parameters, boolean soloMostrarMisSolicitudes) {
		super(parameters);
		
		locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
		
		this.soloMostrarMisSolicitudes = soloMostrarMisSolicitudes;
		
		add(new Label("tituloPagina", soloMostrarMisSolicitudes ? "Mis Solicitudes de Pago" : "Solicitudes de Pago" ));
		
		if (soloMostrarMisSolicitudes) {
			usuarioSolicitante = getUsuarioLogueado();
		}

		final WebMarkupContainer container = new WebMarkupContainer("dataContainer");
		container.setOutputMarkupPlaceholderTag(true);
		add(container);
				
		addFilters(container);
		
		crearSolicitudLink(soloMostrarMisSolicitudes);
		
		mostrarTotalSolicitudesPagoPendientes(soloMostrarMisSolicitudes);
		
		DataView<SolicitudPago> dataView = addSolicitudPagoList(container);
		addNavigator(container, dataView);
		
		mostrarAlertaEnPantallaSiCorresponde(parameters);
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
		
		bajarArchivoSiCorresponde(parameters);
	}
	
	private void addFilters(final WebMarkupContainer container) {
		AutoCompleteTextField<String> nroFacturaSearchAutoComplete = crearNroFacturaSearchAutoCompleteTextField(container);
		
		AjaxLink<String> todasLasFacturasLink = crearTodasFacturasLink(container, "todasFacturas");
				
		WebMarkupContainer solicitanteFilterContainer = new WebMarkupContainer("solicitanteFilterContainer");
		DropDownChoice<Usuario> usuarioSolicitanteDropDownChoice = crearUsuarioSolicitanteDropDownChoice(container);
		solicitanteFilterContainer.add(usuarioSolicitanteDropDownChoice);
		solicitanteFilterContainer.setVisible(!soloMostrarMisSolicitudes);		
		
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
		
		soloSolicitudesPendientesModel = Model.of(true);
		CheckBox checkSoloFacturasDePagoParcial = crearFiltroCheckBox(container, soloSolicitudesPendientesModel, "soloSolicitudesPendientes");		
		
		Form<?> form = new Form<Void>("form");
		add(form);
		form.add(nroFacturaSearchAutoComplete);
		form.add(todasLasFacturasLink);
		form.add(solicitanteFilterContainer);
		form.add(eventoTextField);
		form.add(todosLosEventosLink);
		form.add(filterButton);
		form.add(checkSoloFacturasDePagoParcial);
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
	
	private void crearSolicitudLink(boolean soloMostrarMisSolicitudes) {
		Link<SolicitudPago> crearSolicitudPagoLink = new Link<SolicitudPago>("crearSolicitudPagoLink", Model.of(new SolicitudPago())) {			
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage(new RegistrarSolicitudPagoPage());
			}
		};
		crearSolicitudPagoLink.setVisible(soloMostrarMisSolicitudes);
		add(crearSolicitudPagoLink);
	}
	
	private void mostrarTotalSolicitudesPagoPendientes(boolean soloMostrarMisSolicitudes) {
		SolicitudPagoService solicitudPagoService = new SolicitudPagoService();
		WebMarkupContainer totalSolicitudesPagoPendientesContainer = new WebMarkupContainer("totalSolicitudesPagoPendientesContainer");
		String totalStr = Utils.round2Decimals(solicitudPagoService.getTotalSolicitudesPagoPendientes(), locale);
		Label totalSolicitudesPagoPendientesLbl = new Label("totalSolicitudesPagoPendientes", "$" + totalStr);
		totalSolicitudesPagoPendientesContainer.setVisible(!soloMostrarMisSolicitudes);
		totalSolicitudesPagoPendientesContainer.add(totalSolicitudesPagoPendientesLbl);
		add(totalSolicitudesPagoPendientesContainer);
	}
	
	private DataView<SolicitudPago> addSolicitudPagoList(WebMarkupContainer container) {
		IDataProvider<SolicitudPago> solicitudesPagoDataProvider = getSolicitudesPagoProvider();
		
		DataView<SolicitudPago> dataView = new DataView<SolicitudPago>("listaSolicitudesPago", solicitudesPagoDataProvider, RESULTADOS_POR_PAGINA) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<SolicitudPago> item) {
				DateFormat dateFormatFecha = new SimpleDateFormat("dd-MM-yyyy");
				final SolicitudPago solicitudPago = item.getModelObject();
				String fecha = dateFormatFecha.format(solicitudPago.getFecha().getTime());
								
				item.add(new Label("nroSolicitud", solicitudPago.getNro()));
				item.add(new Label("fecha", fecha));
				
				WebMarkupContainer colSolicitanteValue = new WebMarkupContainer("colSolicitanteValue");
				Label labelSolicitante = new Label("solicitante", solicitudPago.getUsuarioSolicitante().getNombreCompleto());
				colSolicitanteValue.add(labelSolicitante);
				colSolicitanteValue.setVisible(!soloMostrarMisSolicitudes);
				item.add(colSolicitanteValue);
				
				String provStr = "";
				if (solicitudPago.getProveedor()!=null){
					provStr = solicitudPago.getProveedor().getRazonSocial() ;
				} else if (solicitudPago.getRazonSocial() != null && solicitudPago.getCuitCuil() != null){
					provStr = solicitudPago.getRazonSocial();
				} else {
					provStr = "<Sin Proveedor>";
				}
				item.add(new Label("proveedor", provStr ));
				
				List<FacturaSolicitudPago> listaFacturas = solicitudPago.getListadoFacturas();
				String facturasStr = "";
				
				if (solicitudPago.isConFactura()) {
					for (FacturaSolicitudPago facturaSolicitudPago : listaFacturas) {
						if (!facturasStr.isEmpty()) {
							facturasStr += ", ";
						}
						if (facturaSolicitudPago.getFacturaCompra()!=null) {
							facturasStr += facturaSolicitudPago.getFacturaCompra().getNro();
						} else if (facturaSolicitudPago.getNroFacturaCompra()!=null) {
							facturasStr += facturaSolicitudPago.getNroFacturaCompra();
						}
					}
				} else {
					facturasStr = "<Sin Factura>";
				}
				
				Calendar menorFechaPago=null;
				double total = 0;
				List<PagoSolicitudPago> listaPagos = solicitudPago.getListadoPagos();
				for (PagoSolicitudPago pagoSolicitudPago : listaPagos) {
					if (pagoSolicitudPago.getFecha()!=null) {
						if (menorFechaPago==null){
							menorFechaPago = pagoSolicitudPago.getFecha();
						} else if (pagoSolicitudPago.getFecha().before(menorFechaPago)){
							menorFechaPago = pagoSolicitudPago.getFecha();
						}
					}
					total += pagoSolicitudPago.getImporte();
				}
				
				String menorFechaPagoStr = menorFechaPago!=null?dateFormatFecha.format(menorFechaPago.getTime()):"---";
				item.add(new Label("menorFechaPago", menorFechaPagoStr));
				
				item.add(new Label("total", "$" + Utils.round2Decimals(total, locale)));
				item.add(new Label("facturas", facturasStr));
				
				item.add(new Label("evento", solicitudPago.getEvento().getNombreConCliente()));
				
				item.add(new Label("modoPago", solicitudPago.getModosPagosElegidos()));
				
				buildDynamicProgressBar(item, solicitudPago);
				
				Link<SolicitudPago> verDetallesLink = new Link<SolicitudPago>("verDetalles", item.getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						SolicitudPago solicitudPagoSeleccionado = (SolicitudPago) getModelObject();
						setResponsePage(new DetallesSolicitudesPagoPage(solicitudPagoSeleccionado));
					}
				};
				item.add(verDetallesLink);
				
			}

			private void buildDynamicProgressBar(Item<SolicitudPago> item, final SolicitudPago solicitudPago) {
				WebMarkupContainer colEstadoValue = new WebMarkupContainer("colEstadoValue");
				
				Link<SolicitudPago> progressContainer = new Link<SolicitudPago>("progressContainer", item.getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
				    protected void onComponentTag(final ComponentTag tag){
				        super.onComponentTag(tag);
				        if (solicitudPago.isPendiente1()) {
				        	tag.put("title", "Crear Proveedor");
				        } else if (solicitudPago.isPendiente2()) {
				        	tag.put("title", "Crear Facturas");
				        } else if (solicitudPago.isPendiente3()) {
				        	tag.put("title", "Crear OP");
				        } else if (solicitudPago.isCumplida()) {
				        	tag.put("title", "Solicitud de Pago cumplida");
				        } else if (solicitudPago.isRechazada()) {
				        	tag.put("title", "Solicitud de Pago rechazada");
				        }
				    }
					@Override
					public void onClick() {
						if (solicitudPago.isPendiente1()) {
							setResponsePage(new ConvertirAOPPage1(solicitudPago));
						} else if (solicitudPago.isPendiente2()) {
							setResponsePage(new ConvertirAOPPage2(new PageParameters(),solicitudPago));
						} else if (solicitudPago.isPendiente3()) {
							if (solicitudPago.isSPySF()) {
								setResponsePage(new ConvertirAOPPage3SPySF(solicitudPago));
							} else if (solicitudPago.isCPySF()) {
								setResponsePage(new ConvertirAOPPage3CPySF(solicitudPago));
							} else {
								setResponsePage(new ConvertirAOPPage3Normal(solicitudPago));
							}
						} else if (solicitudPago.isCumplida()) {
							setResponsePage(new DetallesSolicitudesPagoPage(solicitudPago));
						} else if (solicitudPago.isRechazada()) {
							setResponsePage(new DetallesSolicitudesPagoPage(solicitudPago));
						}
					}
				};
				colEstadoValue.add(progressContainer);
				item.add(colEstadoValue);
				
				WebMarkupContainer progressBar = new WebMarkupContainer("progressBar"){
					private static final long serialVersionUID = 1L;
					@Override
				    protected void onComponentTag(final ComponentTag tag){
				        super.onComponentTag(tag);
				        if (solicitudPago.isPendiente1()) {
					        tag.put("class", "progress-bar progress-bar-warning");
					        tag.put("aria-valuenow", "3");
					        tag.put("style", "width: 3%");
				        } else if (solicitudPago.isPendiente2()) {
					        tag.put("class", "progress-bar progress-bar-warning");
					        tag.put("aria-valuenow", "33");
					        tag.put("style", "width: 33%");
				        } else if (solicitudPago.isPendiente3()) {
					        tag.put("class", "progress-bar progress-bar-warning");
					        tag.put("aria-valuenow", "66");
					        tag.put("style", "width: 66%");
				        } else if (solicitudPago.isCumplida()) {
					        tag.put("class", "progress-bar progress-bar-success");
					        tag.put("aria-valuenow", "100");
					        tag.put("style", "width: 100%");
				        } else if (solicitudPago.isRechazada()) {
					        tag.put("class", "progress-bar progress-bar-danger");
					        tag.put("aria-valuenow", "100");
					        tag.put("style", "width: 100%");
				        }
				    }
				};
				progressContainer.add(progressBar);
				
				colEstadoValue.setVisible(!soloMostrarMisSolicitudes);
				colEstadoValue.setEnabled(isUsuarioLogueadoRolDesarrollador() || isUsuarioLogueadoRolAdministrador());
			}
		};
		
		WebMarkupContainer colSolicitante = new WebMarkupContainer("colSolicitante");
		colSolicitante.setVisible(!soloMostrarMisSolicitudes);
		container.add(colSolicitante);
		
		WebMarkupContainer colEstado = new WebMarkupContainer("colEstado");
		colEstado.setVisible(!soloMostrarMisSolicitudes);
		container.add(colEstado);
		
		container.add(dataView);
		return dataView;
	}
		
	private IDataProvider<SolicitudPago> getSolicitudesPagoProvider() {
		return new SolicitudesPagoDataProvider(nroFacturaSearchAutoComplete.getModel(), usuarioSolicitanteModel, eventoTextField.getModel(), soloSolicitudesPendientesModel);
	}
	
	private void addNavigator(final WebMarkupContainer dataContainer, DataView<SolicitudPago> dataView) {
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
	
	private AutoCompleteTextField<String> crearNroFacturaSearchAutoCompleteTextField(final WebMarkupContainer container) {
		nroFacturaSearchAutoComplete = new NroFacturaSolicitudPagoSearchAutoCompleteTextField("nroFacturaSearchAutoComplete", new Model<String>()){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSelected(AjaxRequestTarget target){
				actualizarContainer(container, target);
			}
			
		};		
		return nroFacturaSearchAutoComplete;
	}
	
	private AjaxLink<String> crearTodasFacturasLink(final WebMarkupContainer container, String nombreLink) {
		AjaxLink<String> hoyLink = new AjaxLink<String>(nombreLink) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				nroFacturaSearchAutoComplete.setModelObject(null);
				target.add(nroFacturaSearchAutoComplete);
				actualizarContainer(container, target);
			}
		};
		return hoyLink;
	}
	
	private AjaxLink<String> crearTodosEventosLink(final WebMarkupContainer container, String nombreLink) {
		AjaxLink<String> link = new AjaxLink<String>(nombreLink) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				eventoTextField.setModelObject(null);
				target.add(eventoTextField);
				actualizarContainer(container, target);
			}
		};
		return link;
	}
	
	private DropDownChoice<Usuario> crearUsuarioSolicitanteDropDownChoice(final WebMarkupContainer container) {
		UsuarioService usuarioService = new UsuarioService();
		List<Usuario> solicitantes = usuarioService.getUsuariosByRol(Rol.SOLICITANTE_PAGOS);
		
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

}
