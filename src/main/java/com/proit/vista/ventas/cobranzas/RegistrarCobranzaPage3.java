package com.proit.vista.ventas.cobranzas;

import java.util.Calendar;
import java.util.Date;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.StringValidator;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.ui.form.datepicker.AjaxDatePicker;
import com.proit.modelo.ventas.Cobranza;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class RegistrarCobranzaPage3 extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	public RegistrarCobranzaPage3() {
		setearDefaultModel();
		
		crearForm();
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
	}
	
	private void setearDefaultModel() {
		Cobranza cobranza = (Cobranza) ((FacturarOnLineAuthenticatedWebSession) getSession()).getAttribute("cob");
		//Seteo fecha actual por defecto
		if (cobranza.getFecha()==null){
			cobranza.setFecha(Utils.firstMillisecondOfDay(Calendar.getInstance()));
		}
		this.setDefaultModel(Model.of(cobranza));
	}
	
	private void crearForm() {
		FeedbackPanel feedback = new FeedbackPanel("feedback");
		
		final AjaxDatePicker ajaxDatePicker = crearDatePicker();
		AjaxLink<String> hoyLink = new AjaxLink<String>("hoy") {			
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				ajaxDatePicker.setModelObject(Utils.firstMillisecondOfDay(Calendar.getInstance()).getTime());
				target.add(ajaxDatePicker);
			}
		};
		
		IModel<String> nroReciboModel = new PropertyModel<String>(getDefaultModel(), "nroRecibo");
		final TextField<String> nroReciboTextField = new TextField<String>("nroRecibo", nroReciboModel);
		nroReciboTextField.add(StringValidator.maximumLength(255));
		
		Form<?> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;
						
			@Override
			protected void onSubmit() {
				Cobranza cobranza = (Cobranza)RegistrarCobranzaPage3.this.getDefaultModelObject();
				
				Calendar fecha = Calendar.getInstance();
				fecha.setTime(ajaxDatePicker.getModelObject());
				cobranza.setFecha(fecha);
				
				setResponsePage(RegistrarCobranzaPage4.class, new PageParameters());
			}

		};
		
		this.add(form);
		form.add(feedback.setOutputMarkupId(true));
		form.add(ajaxDatePicker);
		form.add(hoyLink);
		form.add(nroReciboTextField);
	}

	private AjaxDatePicker crearDatePicker() {
		Options options = new Options();
		
		//IModel<Date> fechaModel = new Model<Date>();
		Calendar calendar = ((Cobranza)getDefaultModelObject()).getFecha();
		IModel<Date> fechaModel = Model.of(calendar.getTime());
		AjaxDatePicker ajaxDatePicker = new AjaxDatePicker("datepicker", fechaModel, "dd/MM/yyyy", options) ;
		ajaxDatePicker.setRequired(true);
		return ajaxDatePicker;
	}
	
}