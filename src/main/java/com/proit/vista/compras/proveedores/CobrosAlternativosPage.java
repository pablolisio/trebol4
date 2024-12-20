package com.proit.vista.compras.proveedores;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.proit.modelo.compras.CobroAlternativo;
import com.proit.modelo.compras.CuentaBancaria;
import com.proit.servicios.compras.CobroAlternativoService;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.dataproviders.CobrosAlternativosDataProvider;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class CobrosAlternativosPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(CobrosAlternativosPage.class.getName());
	
	private static final int RESULTADOS_POR_PAGINA = 5;
	
	private int idProveedor;
	private CobroAlternativoService cobroAlternativoService;
	
	private AjaxPagingNavigator navigator;
	
	public CobrosAlternativosPage(final PageParameters parameters) {
		super(parameters);
		
		if (parameters.get("idProv")==null) {
			error("Error al intentar acceder a esta pagina, intentelo nuevamente...");
			return;
		} else {
			idProveedor = Integer.parseInt(parameters.get("idProv").toString());
		}
		
		String title = "Cobros Alternativos para '" + parameters.get("provName").toString() + "'"; 
		add(new Label("title", title));
		
		Link<WebPage> btnCrear = new Link<WebPage>("crearCobroAlternativo") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage(new RegistrarCobroAlternativoPage(idProveedor));
			}
		};
		
		add(btnCrear);
		
		cobroAlternativoService = new CobroAlternativoService();

		final WebMarkupContainer container = new WebMarkupContainer("dataContainer");
		container.setOutputMarkupPlaceholderTag(true);
		add(container);
		
		DataView<CobroAlternativo> dataView = addCobroAlternativoList(container);
		addNavigator(container, dataView);
				
		mostrarAlertaEnPantallaSiCorresponde(parameters);
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
	}
	
	private DataView<CobroAlternativo> addCobroAlternativoList(WebMarkupContainer container) {
		IDataProvider<CobroAlternativo> cobrosAlternativosDataProvider = getCobrosAlternativosProvider();
		
		DataView<CobroAlternativo> dataView = new DataView<CobroAlternativo>("listaCobrosAlternativos", cobrosAlternativosDataProvider, RESULTADOS_POR_PAGINA) {
			private static final long serialVersionUID = 1L;
			private static final String nombreModal = "eliminarModal";
			private int idIncremental = 1; // este atributo es utilizado para obtener un id unico para el componente modal de bootstrap.
			
			@Override
			protected void populateItem(Item<CobroAlternativo> item) {
				CuentaBancaria cuentaBancaria = item.getModelObject().getCuentaBancaria();
				item.add(new Label("titular", item.getModelObject().getTitular()));
				item.add(new Label("cuitCuil", item.getModelObject().getCuitCuil()));
				String cuenta = cuentaBancaria!=null ? cuentaBancaria.getCbu() : "---";
				item.add(new Label("cuenta", cuenta));
				
				Link<CobroAlternativo> editLink = new Link<CobroAlternativo>("editar", item.getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						CobroAlternativo cobroAlternativoSeleccionado = (CobroAlternativo) getModelObject();
						setResponsePage(new RegistrarCobroAlternativoPage(new PageParameters(),new Integer(cobroAlternativoSeleccionado.getId()),idProveedor));
					}
				};

				item.add(editLink);
				
				Button botonIntentarEliminar = new Button("botonIntentarEliminar");
				botonIntentarEliminar.add(new AttributeModifier("data-target", "#" + nombreModal+idIncremental));
				item.add(botonIntentarEliminar);
				
				WebMarkupContainer webMarkupContainer = new WebMarkupContainer("eliminarModal");
				webMarkupContainer.add(new Label("cobroAlternativoAEliminar", item.getModelObject().getTitular()));
				webMarkupContainer.add(new Link<CobroAlternativo>("eliminar", item.getModel()) {
					
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						CobroAlternativo cobroAlternativoSeleccionado = (CobroAlternativo) getModelObject();
						try {
							cobroAlternativoService.delete(cobroAlternativoSeleccionado);
							String textoPorPantalla = "El cobro alternativo "+cobroAlternativoSeleccionado.getTitular() + " ha sido eliminado.";
							crearAlerta(textoPorPantalla, false, false);
						} catch(Exception e) {
							log.error(e.getMessage(), e);
							String textoPorPantalla = "El cobro alternativo " + cobroAlternativoSeleccionado.getTitular() + " no pudo ser eliminado correctamente. Por favor, vuelva a intentarlo.";
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
	
	private IDataProvider<CobroAlternativo> getCobrosAlternativosProvider() {
		return new CobrosAlternativosDataProvider(idProveedor);
	}
	
	private void addNavigator(final WebMarkupContainer dataContainer, DataView<CobroAlternativo> dataView) {
		navigator = new AjaxPagingNavigator("paginator", dataView);
		add(navigator);
	}
	
}
