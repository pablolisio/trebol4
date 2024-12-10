package com.proit.vista.bancos;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.proit.modelo.Banco;
import com.proit.servicios.BancoService;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.dataproviders.BancosDataProvider;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class BancosPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(BancosPage.class.getName());
	
	private BancoService bancoService;
	
	public BancosPage(final PageParameters parameters) {
		super(parameters);
				
		bancoService = new BancoService();
		
		final WebMarkupContainer container = new WebMarkupContainer("dataContainer");
		container.setOutputMarkupPlaceholderTag(true);
		add(container);
		
		addBancosList(container);
				
		mostrarAlertaEnPantallaSiCorresponde(parameters);
		
		facturarOnLineMenu.setearMenuActivo(false, false, true, false);
	}

	private void addBancosList(WebMarkupContainer container) {
		IDataProvider<Banco> bancosDataProvider = new BancosDataProvider();
		
		DataView<Banco> dataView = new DataView<Banco>("listaBancos", bancosDataProvider) {
			private static final long serialVersionUID = 1L;
			private static final String nombreModal = "eliminarModal";
			private int idIncremental = 1; // este atributo es utilizado para obtener un id unico para el componente modal de bootstrap.
			
			@Override
			protected void populateItem(Item<Banco> item) {
				item.add(new Label("nombre", item.getModelObject().getNombre()));
				
				Link<Banco> actualLink = new Link<Banco>("actualLink", item.getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						Banco bancoSeleccionado = (Banco) getModelObject();
						
						String textoPorPantalla = "Se seleccionó el banco '" + bancoSeleccionado.getNombre() + "' como actual.";
						String resultado = "OK";
						
						boolean operacionOK = bancoService.setBancoActual(bancoSeleccionado);
						if (!operacionOK) {
							textoPorPantalla = "El banco '" + bancoSeleccionado.getNombre() + "' no pudo ser seleccionado como actual.";
							resultado = "ERROR";
						}
						
						PageParameters pageParameters = new PageParameters();
						pageParameters.add("Resultado", resultado);
						pageParameters.add("TextoPantalla", textoPorPantalla);
						mostrarAlertaEnPantallaSiCorresponde(pageParameters);						
						
					}
				};
				actualLink.setEnabled(isUsuarioLogueadoRolDesarrollador() || isUsuarioLogueadoRolAdministrador());
				item.add(actualLink);
				
				final Boolean actual = item.getModelObject().isActual();
				Label actualLabel = new Label("actual"){
					private static final long serialVersionUID = 1L;
					@Override
				    protected void onComponentTag(final ComponentTag tag){
						super.onComponentTag(tag);
				        if (actual) {
					        tag.put("class", "glyphicon glyphicon-ok text-success");
				        } else {
				        	tag.put("class", "glyphicon glyphicon-minus text-danger");
				        }
					}
				};
				actualLink.add(actualLabel);
				
				Link<Banco> editLink = new Link<Banco>("editar", item.getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						Banco bancoSeleccionado = (Banco) getModelObject();
						setResponsePage(new RegistrarBancoPage(new Integer(bancoSeleccionado.getId())));
					}
				};
				item.add(editLink);
				
				Button deleteButton = new Button("botonIntentarEliminar");
				deleteButton.add(new AttributeModifier("data-target", "#" + nombreModal+idIncremental));
				item.add(deleteButton);
				
				WebMarkupContainer webMarkupContainer = new WebMarkupContainer("eliminarModal");
				webMarkupContainer.add(new Label("bancoAEliminar", item.getModelObject().getNombre()));
				webMarkupContainer.add(new Link<Banco>("eliminar", item.getModel()) {
					
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						Banco bancoSeleccionado = (Banco) getModelObject();
						try {
							String whereIsUsed = bancoService.delete(bancoSeleccionado);
							String textoPorPantalla = "El banco " + bancoSeleccionado.getNombre() + " ha sido eliminado.";
							if ( !whereIsUsed.isEmpty() ) {
								textoPorPantalla = "El banco " + bancoSeleccionado.getNombre() + " no fue eliminado. El mismo esta siendo usado en las siguientes entidades: " + whereIsUsed;
							}
							crearAlerta(textoPorPantalla, !whereIsUsed.isEmpty(), false);
						} catch(Exception e) {
							log.error(e.getMessage(), e);
							String textoPorPantalla = "El banco " + bancoSeleccionado.getNombre()+ " no pudo ser eliminado correctamente. Por favor, vuelva a intentarlo.";
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