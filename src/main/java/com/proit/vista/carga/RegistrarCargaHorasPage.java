package com.proit.vista.carga;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.proit.modelo.CargaHoras;
import com.proit.servicios.CargaHorasService;
import com.proit.vista.base.FacturarOnLineBasePage;

@AuthorizeInstantiation("Desarrollador")
public class RegistrarCargaHorasPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(RegistrarCargaHorasPage.class.getName());
	
	private CargaHorasService cargaHorasService;
	
	public RegistrarCargaHorasPage() {
		this(null);
	}
	
	public RegistrarCargaHorasPage(Integer cargaHorasId) {
		cargaHorasService = new CargaHorasService();
		
		setearDefaultModel(cargaHorasId);
		
		boolean esEditar = verificarSiEsEditar(cargaHorasId);
		
		crearForm(esEditar);
		add(new FeedbackPanel("feedback"));
		
		facturarOnLineMenu.setearMenuActivo(false, false, true, false);
	}

	private void setearDefaultModel(Integer cargaHorasId) {
		CargaHoras cargaHoras = new CargaHoras();
		if (cargaHorasId != null) {
			cargaHoras = (CargaHoras) cargaHorasService.get(cargaHorasId);
		}
		this.setDefaultModel(Model.of(cargaHoras));
	}

	private void crearForm(final boolean esEditar) {		
		IModel<String> detalleModel = new PropertyModel<String>(getDefaultModel(), "detalle");
		TextField<String> detalleTextField = new TextField<String>("detalle", detalleModel);
		
		IModel<Double> montoModel = new PropertyModel<Double>(getDefaultModel(), "monto");
		TextField<Double> montoTextField = new TextField<Double>("monto", montoModel);
		
		Form<?> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onValidate() {
				super.onValidate();
			}
			@Override
			protected void onSubmit() {
				CargaHoras cargaHoras = (CargaHoras)RegistrarCargaHorasPage.this.getDefaultModelObject();
				if (!esEditar){
					cargaHoras.setFecha(Calendar.getInstance());
				}
				
				String textoPorPantalla = "La carga " + cargaHoras.getDetalle() + " ha sido " + (esEditar ? "editada." : "creada.");
				String resultado = "OK";
				
				try {
					cargaHorasService.createOrUpdate(cargaHoras);
				} catch(Exception e) {
					log.error(e.getMessage(), e);
					textoPorPantalla = "La carga " + cargaHoras.getDetalle() + " no pudo ser " + (esEditar ? "editada " : "creada ") + "correctamente. Por favor, vuelva a intentarlo.";
					resultado = "ERROR";
				}
				
				PageParameters pageParameters = new PageParameters();
				pageParameters.add("Resultado", resultado);
				pageParameters.add("TextoPantalla", textoPorPantalla);
				setResponsePage(CargaHorasPage.class, pageParameters);
			}

		};
		
		add(form);
		form.add(detalleTextField);
		form.add(montoTextField);
	}

}
