package com.proit.vista.compras.plan;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.proit.modelo.compras.PlanCuenta;
import com.proit.servicios.compras.PlanCuentaService;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.dataproviders.PlanesCuentaDataProvider;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class PlanesCuentaPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(PlanesCuentaPage.class.getName());
	
	private PlanCuentaService planCuentaService;
	
	public PlanesCuentaPage(final PageParameters parameters) {
		super(parameters);
				
		planCuentaService = new PlanCuentaService();
		
		final WebMarkupContainer container = new WebMarkupContainer("dataContainer");
		container.setOutputMarkupPlaceholderTag(true);
		add(container);
		
		addPlanesCuentaList(container);
				
		mostrarAlertaEnPantallaSiCorresponde(parameters);
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
	}

	private void addPlanesCuentaList(WebMarkupContainer container) {
		IDataProvider<PlanCuenta> planesCuentaDataProvider = new PlanesCuentaDataProvider();
		
		DataView<PlanCuenta> dataView = new DataView<PlanCuenta>("listaPlanesCuenta", planesCuentaDataProvider) {
			private static final long serialVersionUID = 1L;
			private static final String nombreModal = "eliminarModal";
			private int idIncremental = 1; // este atributo es utilizado para obtener un id unico para el componente modal de bootstrap.
			
			@Override
			protected void populateItem(Item<PlanCuenta> item) {
				item.add(new Label("nombre", item.getModelObject().getNombre()));
				item.add(new Label("descripcion", item.getModelObject().getDescripcion()));
				
				Link<PlanCuenta> editLink = new Link<PlanCuenta>("editar", item.getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						PlanCuenta planCuentaSeleccionado = (PlanCuenta) getModelObject();
						setResponsePage(new RegistrarPlanCuentaPage(new Integer(planCuentaSeleccionado.getId())));
					}
				};
				item.add(editLink);
				
				Button deleteButton = new Button("botonIntentarEliminar");
				deleteButton.add(new AttributeModifier("data-target", "#" + nombreModal+idIncremental));
				item.add(deleteButton);
				
				WebMarkupContainer webMarkupContainer = new WebMarkupContainer("eliminarModal");
				webMarkupContainer.add(new Label("planCuentaAEliminar", item.getModelObject().getNombre()));
				webMarkupContainer.add(new Link<PlanCuenta>("eliminar", item.getModel()) {
					
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						PlanCuenta planCuentaSeleccionado = (PlanCuenta) getModelObject();
						try {
							String whereIsUsed = planCuentaService.delete(planCuentaSeleccionado);
							String textoPorPantalla = "El Plan de Cuenta" + planCuentaSeleccionado.getNombre() + " ha sido eliminado.";
							if ( !whereIsUsed.isEmpty() ) {
								textoPorPantalla = "El Plan de Cuenta" + planCuentaSeleccionado.getNombre() + " no fue eliminado. El mismo esta siendo usado en las siguientes entidades: " + whereIsUsed;
							}
							crearAlerta(textoPorPantalla, !whereIsUsed.isEmpty(), false);
						} catch(Exception e) {
							log.error(e.getMessage(), e);
							String textoPorPantalla = "El Plan de Cuenta" + planCuentaSeleccionado.getNombre()+ " no pudo ser eliminado correctamente. Por favor, vuelva a intentarlo.";
							crearAlerta(textoPorPantalla, true, false);
						}
					}
				});
				
				webMarkupContainer.setMarkupId(nombreModal+idIncremental);
				webMarkupContainer.setVisible(deleteButton.isVisible()&&deleteButton.isEnabled());
				idIncremental++;
				
				item.add(webMarkupContainer);
			}
		};
		container.add(dataView);
	}
	
}