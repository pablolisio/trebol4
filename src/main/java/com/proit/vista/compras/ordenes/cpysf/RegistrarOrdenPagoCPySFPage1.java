package com.proit.vista.compras.ordenes.cpysf;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.googlecode.wicket.jquery.ui.form.autocomplete.AutoCompleteTextField;
import com.proit.modelo.compras.OrdenPago;
import com.proit.servicios.compras.ProveedorService;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.CustomTextFieldDouble;
import com.proit.wicket.components.ProveedorSearchAutoCompleteTextField;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class RegistrarOrdenPagoCPySFPage1 extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;

	private AutoCompleteTextField<String> proveedorSearchAutoComplete;
	private double importeFinal;
	
	private CustomTextFieldDouble importeFinalInput;
	
	public RegistrarOrdenPagoCPySFPage1() {		
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
		
		proveedorSearchAutoComplete = new ProveedorSearchAutoCompleteTextField("proveedorSearchAutocomplete", new Model<String>());
		proveedorSearchAutoComplete.setRequired(true);
		
		Form<?> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onValidate() {
				super.onValidate();
				boolean validacionOK = true;
				
				ProveedorService proveedorService = new ProveedorService();
				String razonSocialProveedor = proveedorSearchAutoComplete.getConvertedInput();
				
				if ( razonSocialProveedor!=null && ! proveedorService.existsByRazonSocial(razonSocialProveedor) ) {
					validacionOK = informarError("El proveedor ingresado no es válido.");
				}
				
				if (importeFinalInput==null || importeFinalInput.getConvertedInput()==null || importeFinalInput.getConvertedInput() <= 0) {
					validacionOK = informarError("El Importe Final debe ser mayor a 0.");
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
			protected void onError() {
				importeFinalInput.setDefaultModelObject(0d);			//Reseteo el label de importe Final
			}
			
			@Override
			protected void onSubmit() {
				OrdenPago ordenPago = (OrdenPago)RegistrarOrdenPagoCPySFPage1.this.getDefaultModelObject();
				
				((FacturarOnLineAuthenticatedWebSession) getSession()).setAttribute("op", ordenPago);
				((FacturarOnLineAuthenticatedWebSession) getSession()).setAttribute("rz", proveedorSearchAutoComplete.getModelObject());
				((FacturarOnLineAuthenticatedWebSession) getSession()).setAttribute("total", importeFinal);
				
				setResponsePage(RegistrarOrdenPagoCPySFPage2.class, new PageParameters());
			}

		};
		
		this.add(form);
		form.add(feedback.setOutputMarkupId(true));
		form.add(proveedorSearchAutoComplete);
		form.add(importeFinalInput);
	}
	
}
