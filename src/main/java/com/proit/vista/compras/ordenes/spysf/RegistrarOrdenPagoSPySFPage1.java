package com.proit.vista.compras.ordenes.spysf;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.proit.modelo.compras.OrdenPago;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.CustomTextFieldDouble;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class RegistrarOrdenPagoSPySFPage1 extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;

	private double importeFinal;
	
	private CustomTextFieldDouble importeFinalInput;
	
	public RegistrarOrdenPagoSPySFPage1() {		
		setearDefaultModel();
		
		crearForm();
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
	}

	private void setearDefaultModel() {
		OrdenPago ordenPago = new OrdenPago();
		this.setDefaultModel(Model.of(ordenPago));
	}

	private void crearForm() {		

		FeedbackPanel feedback = new FeedbackPanel("feedback");
		
		importeFinalInput = new CustomTextFieldDouble("importeFinal", new PropertyModel<Double>(this, "importeFinal"));
		importeFinalInput.setOutputMarkupId(true);
		importeFinalInput.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(importeFinalInput);
            }
        });
		
		Form<?> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;

			//Ahora se pueden cargar OPs spysf con importe en negativo (las normales y cpysf no)
			//Caso: angel le presta plata para un evento, y luego le traen vuelto
//			@Override
//			protected void onValidate() {
//				super.onValidate();
//				boolean validacionOK = true;
//				
//				if (importeFinalInput==null || importeFinalInput.getConvertedInput()==null || importeFinalInput.getConvertedInput() <= 0) {
//					validacionOK = informarError("El Importe Final debe ser mayor a 0.");
//				}
//				
//				if ( ! validacionOK ) {
//					onError();
//					return;
//				}
//			}
//
//			private boolean informarError(String textoError) {
//				error(textoError);
//				return false;
//			}
			
			@Override
			protected void onError() {
				importeFinalInput.setDefaultModelObject(0d);			//Reseteo el label de importe Final
			}
			
			@Override
			protected void onSubmit() {
				OrdenPago ordenPago = (OrdenPago)RegistrarOrdenPagoSPySFPage1.this.getDefaultModelObject();
				
				((FacturarOnLineAuthenticatedWebSession) getSession()).setAttribute("op", ordenPago);
				((FacturarOnLineAuthenticatedWebSession) getSession()).setAttribute("total", importeFinal);
				
				setResponsePage(RegistrarOrdenPagoSPySFPage2.class, new PageParameters());
			}

		};
		
		this.add(form);
		form.add(feedback.setOutputMarkupId(true));
		form.add(importeFinalInput);
	}
	
}
