package com.proit.vista.carga;

import java.text.SimpleDateFormat;

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

import com.proit.modelo.CargaHoras;
import com.proit.servicios.CargaHorasService;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.dataproviders.CargaHorasDataProvider;


@AuthorizeInstantiation("Desarrollador")
public class CargaHorasPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(CargaHorasPage.class.getName());
	
	private CargaHorasService cargaHorasService;
	
	public CargaHorasPage(final PageParameters parameters) {
		super(parameters);
				
		cargaHorasService = new CargaHorasService();

		add(new Label("totalHoras",  cargaHorasService.getTotalHorasJulio2016()));
		add(new Label("restoHoras",  cargaHorasService.getRestoHoras()));
		
		final WebMarkupContainer container = new WebMarkupContainer("dataContainer");
		container.setOutputMarkupPlaceholderTag(true);
		add(container);
		
		addCargaHorasList(container);
				
		mostrarAlertaEnPantallaSiCorresponde(parameters);
		
		facturarOnLineMenu.setearMenuActivo(false, false, true, false);
	}

	private void addCargaHorasList(WebMarkupContainer container) {
		IDataProvider<CargaHoras> cargaHorasDataProvider = new CargaHorasDataProvider();
		
		DataView<CargaHoras> dataView = new DataView<CargaHoras>("listaCargaHoras", cargaHorasDataProvider) {
			private static final long serialVersionUID = 1L;
			private static final String nombreModal = "eliminarModal";
			private int idIncremental = 1; // este atributo es utilizado para obtener un id unico para el componente modal de bootstrap.
			
			@Override
			protected void populateItem(Item<CargaHoras> item) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
				item.add(new Label("fecha", sdf.format(item.getModelObject().getFecha().getTime())));
				item.add(new Label("detalle", item.getModelObject().getDetalle()));
				item.add(new Label("monto", item.getModelObject().getMonto()));
				
				Link<CargaHoras> editLink = new Link<CargaHoras>("editar", item.getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						CargaHoras cargaHorasSeleccionada = (CargaHoras) getModelObject();
						setResponsePage(new RegistrarCargaHorasPage(new Integer(cargaHorasSeleccionada.getId())));
					}
				};
				item.add(editLink);
				
				Button deleteButton = new Button("botonIntentarEliminar");
				deleteButton.add(new AttributeModifier("data-target", "#" + nombreModal+idIncremental));
				item.add(deleteButton);
				
				WebMarkupContainer webMarkupContainer = new WebMarkupContainer("eliminarModal");
				webMarkupContainer.add(new Label("cargaHorasAEliminar", item.getModelObject().getDetalle()));
				webMarkupContainer.add(new Link<CargaHoras>("eliminar", item.getModel()) {
					
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						CargaHoras cargaHorasSeleccionada = (CargaHoras) getModelObject();
						try {
							cargaHorasService.delete(cargaHorasSeleccionada.getId());
							String textoPorPantalla = "La carga " + cargaHorasSeleccionada.getDetalle() + " ha sido eliminada.";
							crearAlerta(textoPorPantalla, false, false);
						} catch(Exception e) {
							log.error(e.getMessage(), e);
							String textoPorPantalla = "La carga " + cargaHorasSeleccionada.getDetalle()+ " no pudo ser eliminada correctamente. Por favor, vuelva a intentarlo.";
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