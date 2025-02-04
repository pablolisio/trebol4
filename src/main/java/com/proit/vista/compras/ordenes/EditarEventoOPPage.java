package com.proit.vista.compras.ordenes;

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
import com.proit.modelo.compras.FacturaCompraOrdenPago;
import com.proit.modelo.compras.OrdenPago;
import com.proit.servicios.EventoService;
import com.proit.servicios.compras.OrdenPagoService;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.components.EventoSearchAutoCompleteTextField;

@AuthorizeInstantiation({"Administrador","Solo Lectura","Desarrollador"})
public class EditarEventoOPPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	private AutoCompleteTextField<String> eventoSearchAutoComplete;
	
	private Boolean sinEvento;
	
	public EditarEventoOPPage(OrdenPago ordenPago) {
		setearDefaultModel(ordenPago);
		
		crearForm(ordenPago);
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
	}
	
	private void setearDefaultModel(OrdenPago ordenPago) {
		this.setDefaultModel(Model.of(ordenPago));
	}
	
	private void crearForm(OrdenPago ordenPago) {
		FeedbackPanel feedback = new FeedbackPanel("feedback");
		
		String nombreEvento = null;
		
		if (!ordenPago.getEvento().getNombreConCliente().equals(Evento.SIN_EVENTO.getNombreConCliente())) {
			nombreEvento= ordenPago.getEvento().getNombreConCliente();
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
				OrdenPago ordenPago = (OrdenPago)EditarEventoOPPage.this.getDefaultModelObject();
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
					eventoService.updateEventoOPYSolPago(ordenPago.getId(), evento.getId(), ordenPago.getSolicitudPago());
					eventoService.flushSession();
				}
				
				OrdenPagoService ordenPagoService = new OrdenPagoService();
				ordenPago = (OrdenPago) ordenPagoService.get(ordenPago.getId());
				FacturaCompraOrdenPago facturaOrdenPago = new FacturaCompraOrdenPago();
				facturaOrdenPago.setBorrado(false);
				facturaOrdenPago.setFacturaCompra(null);
				facturaOrdenPago.setOrdenPago(ordenPago);
				setResponsePage(new DetallesOrdenesPagoPage(facturaOrdenPago));
				
				
//				if (sinEvento!=null && sinEvento) {
//					ordenPago.setEvento(Evento.SIN_EVENTO);
//				} else {
//					EventoService eventoService = new EventoService();
//					String nombreEvento = Utils.getEventFromFullName(eventoSearchAutoComplete.getModelObject());
//					String razonSocialCliente = Utils.getClientFromFullName(eventoSearchAutoComplete.getModelObject());
//					if (nombreEvento!=null) {
//						ordenPago.setEvento(eventoService.getByClienteAndNombreEvento(razonSocialCliente, nombreEvento));
//					}
//				}
//				
//				setResponsePage(EditarEventoOPPage.class, new PageParameters());
			}

		};
		
		this.add(form);
		form.add(feedback.setOutputMarkupId(true));
		form.add(eventoSearchAutoComplete);
		form.add(sinEventoCheckbox);
	}

}