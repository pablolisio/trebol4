package com.proit.vista.ventas.facturas;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import com.googlecode.wicket.jquery.ui.form.autocomplete.AutoCompleteTextField;
import com.proit.modelo.Evento;
import com.proit.modelo.ventas.FacturaVenta;
import com.proit.servicios.EventoService;
import com.proit.servicios.ventas.FacturaVentaService;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.components.EventoSearchAutoCompleteTextField;

@AuthorizeInstantiation({"Administrador","Solo Lectura","Desarrollador"})
public class EditarEventoFactVentaPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	private AutoCompleteTextField<String> eventoSearchAutoComplete;
	
	private Boolean sinEvento;
	
	public EditarEventoFactVentaPage(FacturaVenta factura) {
		setearDefaultModel(factura);
		
		crearForm(factura);
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
	}
	
	private void setearDefaultModel(FacturaVenta factura) {
		this.setDefaultModel(Model.of(factura));
	}
	
	private void crearForm(FacturaVenta factura) {
		FeedbackPanel feedback = new FeedbackPanel("feedback");
		
		String nombreEvento = null;
		
		if (!factura.getEvento().getNombreConCliente().equals(Evento.SIN_EVENTO.getNombreConCliente())) {
			nombreEvento= factura.getEvento().getNombreConCliente();
			sinEvento = false;
		} else {
			sinEvento = true;
		}
		
		IModel<String> eventoModel = Model.of(nombreEvento);		
		eventoSearchAutoComplete = new EventoSearchAutoCompleteTextField("eventoSearchAutoComplete", eventoModel, true);
		eventoSearchAutoComplete.setRequired(true);
		
		IModel<Boolean> sinEventoModel = new PropertyModel<Boolean>(this, "sinEvento");
		final CheckBox sinEventoCheckbox = new CheckBox("sinEvento", sinEventoModel);
		sinEventoCheckbox.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;

			protected void onUpdate(AjaxRequestTarget target) {
				boolean sinEvento = (Boolean) sinEventoCheckbox.getDefaultModelObject();
				((EventoSearchAutoCompleteTextField)eventoSearchAutoComplete).setEnabled(!sinEvento);
				eventoSearchAutoComplete.setRequired(!sinEvento);
				target.add(eventoSearchAutoComplete);
            }
        });
		
		((EventoSearchAutoCompleteTextField)eventoSearchAutoComplete).setEnabled(!sinEvento);
		eventoSearchAutoComplete.setRequired(!sinEvento);
				
		Form<?> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onValidate() {
				super.onValidate();
				boolean validacionOK = true;
				
				if (sinEvento!=null && !sinEvento) {
					EventoService eventoService = new EventoService();
					String nombreEvento = Utils.getEventFromFullName(eventoSearchAutoComplete.getModelObject());
					String razonSocialCliente = Utils.getClientFromFullName(eventoSearchAutoComplete.getModelObject());
					if (nombreEvento == null || !eventoService.existsByClienteAndNombreEvento(razonSocialCliente, nombreEvento) ) {
						validacionOK = informarError("Debe ingresar un evento valido. Utilice la funcion autocompletado.");
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
				FacturaVenta factura = (FacturaVenta)EditarEventoFactVentaPage.this.getDefaultModelObject();
				Evento evento = null;
				if (sinEvento!=null && sinEvento) {
					evento = Evento.SIN_EVENTO;
				} else {
					EventoService eventoService = new EventoService();
					String nombreEvento = Utils.getEventFromFullName(eventoSearchAutoComplete.getModelObject());
					String razonSocialCliente = Utils.getClientFromFullName(eventoSearchAutoComplete.getModelObject());
					if (nombreEvento!=null) {
						evento = eventoService.getByClienteAndNombreEvento(razonSocialCliente, nombreEvento);
					}
				}
				
				if (evento!=null) {
					EventoService eventoService = new EventoService();
					eventoService.updateEventoFacturaVentaYSolFacturaVenta(factura.getId(), evento.getId(), factura.getSolicitudFacturaVenta());
					eventoService.flushSession();
				}
				
				FacturaVentaService facturaVentaService = new FacturaVentaService();
				factura = (FacturaVenta) facturaVentaService.get(factura.getId());
				setResponsePage(new RegistrarFacturaVentaPage(true, factura));
				
			}

		};
		
		this.add(form);
		form.add(feedback.setOutputMarkupId(true));
		form.add(eventoSearchAutoComplete);
		form.add(sinEventoCheckbox);
	}

}