package com.proit.vista.ventas.cobranzas;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.ui.form.datepicker.AjaxDatePicker;
import com.proit.modelo.ventas.Cobranza;
import com.proit.modelo.ventas.Cobro;
import com.proit.servicios.ventas.CobranzaService;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class EditarFechasChequesPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	private Locale locale; 
	
	public EditarFechasChequesPage(Cobranza cobranza) {
		
		locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
		
		this.setDefaultModel(Model.of(cobranza));

		crearForm();

		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
	}
	
	private void crearForm() {
		FeedbackPanel feedback = new FeedbackPanel("feedback");
		
		Form<?> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onValidate() {
				super.onValidate();
				boolean validacionOK = true;

				CobranzaService cobranzaService = new CobranzaService();
				Cobranza cobranza = (Cobranza)EditarFechasChequesPage.this.getDefaultModelObject();

				if ( ! cobranzaService.todoChequeTieneFecha(cobranza.getListadoCobros()) ) {
					validacionOK = informarError("Todo cheque debe contener Fecha.");
				}

				if ( ! validacionOK ) {
					onError();
					return;
				}				

			}

			private boolean informarError(String textoError) {
				error(textoError);
				return false;
			}

			@Override
			protected void onSubmit() {
				CobranzaService cobranzaService = new CobranzaService();
				Cobranza cobranza = (Cobranza)EditarFechasChequesPage.this.getDefaultModelObject();
				cobranzaService.createOrUpdate(cobranza);
				
				String textoPorPantalla = "Cambios en fechas de cheques guardados.";
				String resultado = "OK";				
				PageParameters pageParameters = new PageParameters();
				pageParameters.add("Resultado", resultado);
				pageParameters.add("TextoPantalla", textoPorPantalla);
				setResponsePage(CobranzasPage.class, pageParameters);
			}
		};

		this.add(form);
		form.add(feedback.setOutputMarkupId(true));
		
		DataView<Cobro> dataView = addChequesList();
		form.add(dataView);
		
	}
	
	private DataView<Cobro> addChequesList() {
		IDataProvider<Cobro> cobrosDataProvider = getChequesProvider();
		
		DataView<Cobro> dataView = new DataView<Cobro>("listaCheques", cobrosDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<Cobro> item) {
				Label bancoChequeLbl = new Label("bancoCheque", item.getModelObject().getBancoCheque());
				
				Label nroChequeLbl = new Label("nroCheque", item.getModelObject().getNroCheque());
				
				AjaxDatePicker ajaxDatePicker = crearChequeDatePicker(item);
				ajaxDatePicker.setOutputMarkupId(true);
				
				Label importeChequeLbl = new Label("importeCheque", Utils.round2Decimals(item.getModelObject().getImporte(), locale));
				
				item.add(bancoChequeLbl);
				item.add(nroChequeLbl);
				item.add(ajaxDatePicker);
				item.add(importeChequeLbl);
				
				//Utilizo lo siguiente para que todos los campos ingresados en la grilla de la pagina no sean eliminados al intentar agregar o quitar una fila.
				ajaxDatePicker.add(createNewAjaxFormComponentUpdatingBehavior());
				
				if (item.getModelObject().isBorrado()) { //para el caso de editar se debe poner invisible
					item.setVisible(false);
				}
			}

			/**
			 * Utilizado para que al eliminar o agregar una fila de la tabla los campos de las demas filas no se eliminen
			 * @return
			 */
			private AjaxFormComponentUpdatingBehavior createNewAjaxFormComponentUpdatingBehavior() {
				return new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
					private static final long serialVersionUID = 1L;
					protected void onUpdate(AjaxRequestTarget target) {
					}
				};
			}
		};
		
		return dataView;
	}
	
	private IDataProvider<Cobro> getChequesProvider() {
		IDataProvider<Cobro> cobrosDataProvider = new IDataProvider<Cobro>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void detach() {
			}

			@Override
			public Iterator<Cobro> iterator(long first, long count) {
				return getListadoTodosCobros().iterator();
			}

			@Override
			public long size() {
				return getListadoTodosCobros().size();
			}

			@Override
			public IModel<Cobro> model(Cobro cobro) {
				return new Model<Cobro>(cobro);
			}
			
			private List<Cobro> getListadoTodosCobros() {
				List<Cobro> listaCobros = ((Cobranza)EditarFechasChequesPage.this.getDefaultModelObject()).getListadoCobros();
				List<Cobro> returnList = new ArrayList<Cobro>();
				for (Cobro cobro : listaCobros) {
					if (cobro.isCheque()) {
						returnList.add(cobro);
					}
				}
				return returnList;
			}
        	
        };
		return cobrosDataProvider;
	}
	
	private AjaxDatePicker crearChequeDatePicker(Item<Cobro> item) {
		IModel<Date> fechaModel = new PropertyModel<Date>(item.getModelObject(), "fechaAsDate");
		AjaxDatePicker ajaxDatePicker = new AjaxDatePicker("datepickerCheque", fechaModel, "dd/MM/yyyy", new Options());
		ajaxDatePicker.setRequired(true);
		return ajaxDatePicker;
	}

}
