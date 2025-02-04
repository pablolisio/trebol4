package com.proit.vista.presupuesto;

import java.math.BigDecimal;
import java.sql.Timestamp;
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
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.ui.form.datepicker.AjaxDatePicker;
import com.proit.modelo.auxiliar.ItemListaPresupuestoBanco;
import com.proit.servicios.PresupuestoCustomService;
import com.proit.servicios.compras.OrdenPagoService;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class PresupuestosBancoPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	private static final int RESULTADOS_POR_PAGINA = 10;
	
	private OrdenPagoService ordenPagoService;
	
	private Locale locale;
	
	private AjaxPagingNavigator navigator;
	
	private AjaxDatePicker ajaxDatePicker;
	
	private IModel<Boolean> mostrarDebitadosModel;
	private IModel<Boolean> mostrarPresupuestoModel;
	
	private Label totalLbl;
		
	public PresupuestosBancoPage(final PageParameters parameters) {
		super(parameters);
		
		ordenPagoService = new OrdenPagoService();
		locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
		
		WebMarkupContainer container = new WebMarkupContainer("dataContainer");
		container.setOutputMarkupPlaceholderTag(true);
		add(container);
				
		addFilters(container);
		
		DataView<ItemListaPresupuestoBanco> dataView = addListado(container);
		addNavigator(dataView);
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
		
		mostrarAlertaEnPantallaSiCorresponde(parameters);
	}
	
	private void addFilters(WebMarkupContainer container) {
		crearDatePicker(container);
		
		AjaxLink<String> todosLosDiasLink = crearDiaLink(container, "todasFechas", null);
		
		mostrarDebitadosModel = Model.of(false);
		CheckBox checkMostrarDebitados = crearFiltroCheckBox(container, mostrarDebitadosModel, "mostrarDebitados");
		
		mostrarPresupuestoModel = Model.of(true);
		CheckBox checkMostrarPresupuesto = crearFiltroCheckBox(container, mostrarPresupuestoModel, "mostrarPresupuesto");
		
		totalLbl = new Label("total", calculateTotal());
		totalLbl.setOutputMarkupId(true);
		
		Form<?> form = new Form<Void>("form");
		add(form);
		form.add(ajaxDatePicker);
		form.add(todosLosDiasLink);
		form.add(checkMostrarDebitados);
		form.add(checkMostrarPresupuesto);
		form.add(totalLbl);
	}	
	
	private DataView<ItemListaPresupuestoBanco> addListado(WebMarkupContainer container) {
		IDataProvider<ItemListaPresupuestoBanco> presupuestosBancoDataProvider = getPresupuestosBancoDataProvider();
		
		DataView<ItemListaPresupuestoBanco> presupuestosBancoDataView = new DataView<ItemListaPresupuestoBanco>("listaCheques", presupuestosBancoDataProvider, RESULTADOS_POR_PAGINA) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<ItemListaPresupuestoBanco> item) {
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				ItemListaPresupuestoBanco itemPresupuestoBanco = item.getModelObject();
				String razonSocialProveedor = itemPresupuestoBanco.getRazonSocialProveedor();
				String bco = itemPresupuestoBanco.getBanco();
				String nro = itemPresupuestoBanco.getNroCheque();
				String fecha = itemPresupuestoBanco.getFecha()!=null?dateFormat.format(itemPresupuestoBanco.getFecha().getTime()):"-";
				double importe = itemPresupuestoBanco.getImporte().doubleValue();
				
				Label proveedorLabel = new Label("proveedor", razonSocialProveedor);
				Label bcoLabel = new Label("banco", bco);
				Label nroLabel = new Label("nro", nro);
				Label fechaLabel = new Label("fecha", fecha);				
				Label importeLabel = new Label("importe", "$" + Utils.round2Decimals(importe, locale));
				
				Link<ItemListaPresupuestoBanco> debitarDesdebitarLink = new Link<ItemListaPresupuestoBanco>("debitarDesdebitarLink", item.getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						ItemListaPresupuestoBanco itemPresupuestoBancoSeleccionado = (ItemListaPresupuestoBanco) getModelObject();
						
						String textoPorPantalla = "Se " + (itemPresupuestoBancoSeleccionado.isDebitado()?"canceló el débito":"debitó") + " correctamente.";
						String resultado = "OK";
						
						boolean operacionOK = false;
						
						if (!itemPresupuestoBancoSeleccionado.isCustom()) {
							operacionOK = ordenPagoService.debitarDesdebitarPago(itemPresupuestoBancoSeleccionado.getId());
						} else {
							PresupuestoCustomService presupuestoCustomService = new PresupuestoCustomService();
							operacionOK = presupuestoCustomService.debitarDesdebitarPresupuestoCustom(itemPresupuestoBancoSeleccionado.getId());
						}
						
						if (!operacionOK) {
							textoPorPantalla = "Hubo un error al intentar realizar la operación.";
							resultado = "ERROR";
						}
						
						PageParameters pageParameters = new PageParameters();
						pageParameters.add("Resultado", resultado);
						pageParameters.add("TextoPantalla", textoPorPantalla);
						mostrarAlertaEnPantallaSiCorresponde(pageParameters);
						setResponsePage(PresupuestosBancoPage.class, pageParameters);
					}
				};
				
				final Boolean debitado = item.getModelObject().isDebitado();
				Label debitadoLabel = new Label("debitado", debitado?"":"NO"){
					private static final long serialVersionUID = 1L;
					@Override
				    protected void onComponentTag(final ComponentTag tag){
						super.onComponentTag(tag);
				        if (debitado) {
					        tag.put("class", "glyphicon glyphicon-ok text-success");
				        } else {
				        	tag.put("class", "text-danger");
				        }
					}
				};
				debitarDesdebitarLink.add(debitadoLabel);
				
				Link<ItemListaPresupuestoBanco> editLink = new Link<ItemListaPresupuestoBanco>("editar", item.getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						ItemListaPresupuestoBanco itemSeleccionado = (ItemListaPresupuestoBanco) getModelObject();
						setResponsePage(new RegistrarPresupuestoCustomPage(new Integer(itemSeleccionado.getId())));
					}
				};
				editLink.setVisible(itemPresupuestoBanco.isCustom());
				
				item.add(proveedorLabel);
				item.add(bcoLabel);
				item.add(nroLabel);
				item.add(fechaLabel);
				item.add(importeLabel);
				item.add(debitarDesdebitarLink);
				item.add(editLink);
			}
		};
				
		container.add(presupuestosBancoDataView);
		return presupuestosBancoDataView;
	}
	
	private IDataProvider<ItemListaPresupuestoBanco> getPresupuestosBancoDataProvider() {
		IDataProvider<ItemListaPresupuestoBanco> presupuestosBancoDataProvider = new IDataProvider<ItemListaPresupuestoBanco>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void detach() {
			}

			@Override
			public Iterator<ItemListaPresupuestoBanco> iterator(long first, long count) {
				List<Object[]> presupuestosBanco = ordenPagoService.getPresupuestosBanco(first, count, getFecha(), mostrarDebitadosModel.getObject(), mostrarPresupuestoModel.getObject());
				List<ItemListaPresupuestoBanco> listadoitemPresupuestoBanco = new ArrayList<ItemListaPresupuestoBanco>();
				for (Object[] obj : presupuestosBanco) {
					ItemListaPresupuestoBanco itemPresupuestoBanco = new ItemListaPresupuestoBanco();
					itemPresupuestoBanco.setId((Integer) obj[0]);
					itemPresupuestoBanco.setRazonSocialProveedor((String) obj[1]);
					itemPresupuestoBanco.setBanco((String) obj[2]);
					itemPresupuestoBanco.setNroCheque((String) obj[3]);
					Timestamp fechaTimestamp = ((Timestamp) obj[4]);
					Calendar fecha = null;
					if (fechaTimestamp!=null) {
						fecha = Calendar.getInstance();
						fecha.setTimeInMillis(fechaTimestamp.getTime());
					}
					itemPresupuestoBanco.setFecha(fecha);
					itemPresupuestoBanco.setImporte((BigDecimal) obj[5]);
					itemPresupuestoBanco.setDebitado((Boolean) obj[6]);
					itemPresupuestoBanco.setCustom((Boolean) obj[7]);
					listadoitemPresupuestoBanco.add(itemPresupuestoBanco);
				}
				return listadoitemPresupuestoBanco.iterator();
			}

			@Override
			public long size() {
				long size = ordenPagoService.getPresupuestosBancoSize(getFecha(), mostrarDebitadosModel.getObject(), mostrarPresupuestoModel.getObject());
				return size;
			}

			@Override
			public IModel<ItemListaPresupuestoBanco> model(ItemListaPresupuestoBanco itemPresupuestoBanco) {
				return new Model<ItemListaPresupuestoBanco>(itemPresupuestoBanco);
			}
        	
        };
		return presupuestosBancoDataProvider;
	}
	
	private Calendar getFecha() {
		Calendar fecha = null;
		if (ajaxDatePicker.getModelObject() != null){
			fecha = Calendar.getInstance();
			fecha.setTime(ajaxDatePicker.getModelObject());
		}
		return fecha;
	}
	
	private AjaxDatePicker crearDatePicker(final WebMarkupContainer container) {
		Options options = new Options();
		ajaxDatePicker = new AjaxDatePicker("datepicker", new Model<Date>(), "dd/MM/yyyy", options) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onValueChanged(IPartialPageRequestHandler handler) {
				actualizarContainer(container, handler);
			}
		};
		return ajaxDatePicker;
	}
	
	private AjaxLink<String> crearDiaLink(final WebMarkupContainer container, String nombreLink, final Date date) {
		AjaxLink<String> hoyLink = new AjaxLink<String>(nombreLink) {			
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				ajaxDatePicker.setModelObject(date);
				target.add(ajaxDatePicker);
				actualizarContainer(container, target);
			}
		};
		return hoyLink;
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
	
	private String calculateTotal() {
		return "$" + Utils.round2Decimals(ordenPagoService.getTotalPresupuestosBanco(getFecha(), mostrarDebitadosModel.getObject(), mostrarPresupuestoModel.getObject()), locale);
	}
	
	private void actualizarContainer(WebMarkupContainer container, IPartialPageRequestHandler target) {
		totalLbl.setDefaultModelObject(calculateTotal());
		target.add(container);
		target.add(navigator);
		target.add(totalLbl);
	}
	
	private void addNavigator(DataView<ItemListaPresupuestoBanco> dataView) {
		navigator = new AjaxPagingNavigator("paginator", dataView);
		add(navigator);
	}

}
