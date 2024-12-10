package com.proit.vista.ventas.cobranzas;

import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.proit.modelo.ventas.Cobranza;
import com.proit.modelo.ventas.FacturaVentaCobranza;
import com.proit.servicios.ventas.CobranzaService;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.CustomTextFieldDouble;


@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class EditarRetencionesCobranzaPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(EditarRetencionesCobranzaPage.class.getName());
	
	private CobranzaService cobranzaService;
	
	private Locale locale;
	
	private CustomTextFieldDouble percIvaInput;
	private CustomTextFieldDouble percIibbInput;
	private CustomTextFieldDouble percGciasInput;
	private CustomTextFieldDouble percSUSSInput;
	private CustomTextFieldDouble otrasPercInput;
	
	/**
	 * Si bien entra una FacturaVentaCobranza, la factura siempre viene como null, y borrado como false. 
	 * Solo se usa la Cobranza
	 * @param facturaVentaCobranza
	 */
	public EditarRetencionesCobranzaPage(FacturaVentaCobranza facturaVentaCobranza) {
		
		locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
		
		cobranzaService = new CobranzaService();
		
		Cobranza cobranza = facturaVentaCobranza.getCobranza();
		cobranza = (Cobranza) cobranzaService.get(cobranza.getId());		
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
				
				if (percIvaInput.getConvertedInput() != null && percIibbInput.getConvertedInput() != null && percGciasInput.getConvertedInput() != null 
						&& percSUSSInput.getConvertedInput() != null && otrasPercInput.getConvertedInput() != null) {
					Cobranza cobranza = (Cobranza)EditarRetencionesCobranzaPage.this.getDefaultModelObject();
					cobranza = (Cobranza) cobranzaService.get(cobranza.getId());
					
					//Obtengo las retenciones ingresadas al crear la cobranza
					double totalRetencionesReal = cobranza.getPercIva() + cobranza.getPercIibb() + cobranza.getPercGcias() + cobranza.getPercSUSS() + cobranza.getOtrasPerc();
					//Obtengo las retenciones modificadas por esta pagina
					double totalRetencionesIngresado = percIvaInput.getConvertedInput() + percIibbInput.getConvertedInput() + percGciasInput.getConvertedInput() + percSUSSInput.getConvertedInput() + otrasPercInput.getConvertedInput();
					
					//Valido que totalRetencionesReal = totalRetencionesIngresado
					if ( ! Utils.round2Decimals(totalRetencionesReal, locale).equals(Utils.round2Decimals(totalRetencionesIngresado, locale)) ){
						String errorString = "El Total de las retenciones debe ser igual a $" + Utils.round2Decimals(totalRetencionesReal, locale) + ".";
						validacionOK = informarError(errorString);
					}
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
				Cobranza cobranza = (Cobranza)EditarRetencionesCobranzaPage.this.getDefaultModelObject();
				
				boolean error = false;
				
				try {
					cobranzaService.createOrUpdate(cobranza);
				} catch(Exception e) {
					log.error(e.getMessage(), e);
					error = true;
				}
				
				String textoPorPantalla = (error?"No se ha podido guardar correctamente las retenciones. Por favor, vuelva a intentarlo.":"Se han modificado las retenciones correctamente.");
				String resultado = (error?"ERROR":"OK");
				
				PageParameters pageParameters = new PageParameters();
				pageParameters.add("Resultado", resultado);
				pageParameters.add("TextoPantalla", textoPorPantalla);
				setResponsePage(CobranzasPage.class, pageParameters);
				
			}
		};
				
		this.add(form);
		
		agregarRetenciones(form);
		
		form.add(feedback.setOutputMarkupId(true));
	}
	
	private void agregarRetenciones(Form<?> form) {
		percIvaInput = new CustomTextFieldDouble("percIva", new PropertyModel<Double>(getDefaultModel(), "percIva"));
		percIvaInput.setOutputMarkupId(true);
		percIvaInput.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(percIvaInput);
            }
        });
		
		percIibbInput = new CustomTextFieldDouble("percIibb", new PropertyModel<Double>(getDefaultModel(), "percIibb"));
		percIibbInput.setOutputMarkupId(true);
		percIibbInput.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange')
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(percIibbInput);
            }
        });
		
		percGciasInput = new CustomTextFieldDouble("percGcias", new PropertyModel<Double>(getDefaultModel(), "percGcias"));
		percGciasInput.setOutputMarkupId(true);
		percGciasInput.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(percGciasInput);
            }
        });
		
		percSUSSInput = new CustomTextFieldDouble("percSUSS", new PropertyModel<Double>(getDefaultModel(), "percSUSS"));
		percSUSSInput.setOutputMarkupId(true);
		percSUSSInput.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(percSUSSInput);
            }
        });
		
		otrasPercInput = new CustomTextFieldDouble("otrasPerc", new PropertyModel<Double>(getDefaultModel(), "otrasPerc"));
		otrasPercInput.setOutputMarkupId(true);
		otrasPercInput.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(otrasPercInput);
            }
        });
		
		IModel<Boolean> validarRetencionesModel = new PropertyModel<Boolean>(getDefaultModel(), "retencionesValidadas");
		CheckBox validarRetenciones = new CheckBox("validarRetenciones", validarRetencionesModel);
		
		form.add(percIvaInput);
		form.add(percIibbInput);
		form.add(percGciasInput);
		form.add(percSUSSInput);
		form.add(otrasPercInput);
		form.add(validarRetenciones);
	}

	
}