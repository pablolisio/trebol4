package com.proit.vista.ventas.cobranzas;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.googlecode.wicket.jquery.ui.form.autocomplete.AutoCompleteTextField;
import com.proit.modelo.ventas.Cobranza;
import com.proit.modelo.ventas.EstadoFacturaVenta;
import com.proit.modelo.ventas.FacturaVenta;
import com.proit.servicios.ventas.CobranzaService;
import com.proit.servicios.ventas.FacturaVentaService;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.ClienteSearchAutoCompleteTextField;
import com.proit.wicket.components.CustomTextFieldDouble;
import com.proit.wicket.dataproviders.FacturasVentaDataProvider;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class RegistrarCobranzaPage1 extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;

	private AutoCompleteTextField<String> clienteSearchAutoComplete;
	private double importeFinal;
	private double importeParcial;
	
	private WebMarkupContainer facturasContainer;
	private CustomTextFieldDouble importeFinalInput;
	private CustomTextFieldDouble importeParcialInput;

	private int cantidadFacturasSeleccionadas;
	private Label noExistenFacturas;
	
	public RegistrarCobranzaPage1() {		
		setearDefaultModel();
		
		cantidadFacturasSeleccionadas=0;
		
		crearForm();
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
	}

	private void setearDefaultModel() {
		Cobranza cobranza = new Cobranza();
		this.setDefaultModel(Model.of(cobranza));
	}

	private void crearForm() {		

		FeedbackPanel feedback = new FeedbackPanel("feedback");
		
		importeFinalInput = new CustomTextFieldDouble("importeFinal", new PropertyModel<Double>(this, "importeFinal"));
		importeFinalInput.setOutputMarkupId(true);
		importeFinalInput.setEnabled(false);
		
		importeParcialInput = new CustomTextFieldDouble("importeParcial", new PropertyModel<Double>(this, "importeParcial"));
		importeParcialInput.setOutputMarkupId(true);
		importeParcialInput.setEnabled(false);
		importeParcialInput.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(importeParcialInput);
            }
        });
		
		clienteSearchAutoComplete = new ClienteSearchAutoCompleteTextField("clienteSearchAutocomplete", new Model<String>()){
			private static final long serialVersionUID = 1L;			
			@Override
			protected void onSelected(AjaxRequestTarget target){
				Cobranza cobranza = (Cobranza)RegistrarCobranzaPage1.this.getDefaultModelObject();
				cobranza.setListadoFacturas(new ArrayList<FacturaVenta>());		//Reseteo el listado de facturas, por si selecciono alguna con un cliente anterior
					
				facturasContainer.setVisible(true);
				importeFinalInput.setDefaultModelObject(0d);			//Reseteo el label de importe Final
				importeParcialInput.setDefaultModelObject(0d);			//Reseteo el label de importe Parcial
				cantidadFacturasSeleccionadas = 0;
				noExistenFacturas.setVisible(true);
				actualizarImporteParcial(target);
				target.add(facturasContainer);
				target.add(importeFinalInput);
			}
			
		};
		clienteSearchAutoComplete.setRequired(true);
		
		facturasContainer = crearFacturasDataView();
		facturasContainer.setVisible(false);
		
		Form<?> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onValidate() {
				super.onValidate();
				boolean validacionOK = true;
				
				String razonSocialCliente = clienteSearchAutoComplete.getModelObject();
				
				//Si se selecciono un cliente, verifico si se selecciono alguna factura.
				if (razonSocialCliente!=null) {
					Cobranza cobranza = (Cobranza)RegistrarCobranzaPage1.this.getDefaultModelObject();
					int cantidadFacturasSeleccionadas = 0;
					for (FacturaVenta factura: cobranza.getListadoFacturas()){
						if (factura.isSeleccionada()){
							cantidadFacturasSeleccionadas ++;
						}
					}
					if (cantidadFacturasSeleccionadas==0) {
						validacionOK = informarError("Debe seleccionar al menos una factura.");
					}
				}
				
				if (importeFinal <= 0) {
					validacionOK = informarError("El Importe Final debe ser mayor a 0.");
				}
				
				if (importeParcial < 0) {
					validacionOK = informarError("El Importe Parcial debe ser mayor o igual a 0.");
				}
				
				if (Utils.round(importeParcial,2) >= Utils.round(importeFinal,2)) {
					validacionOK = informarError("El Importe Parcial debe ser menor al Importe Final.");
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
				importeParcialInput.setDefaultModelObject(0d);			//Reseteo el label de importe Parcial
				cantidadFacturasSeleccionadas = 0;
				Cobranza cobranza = (Cobranza)RegistrarCobranzaPage1.this.getDefaultModelObject();
				cobranza.setListadoFacturas(new ArrayList<FacturaVenta>());		//Reseteo el listado de facturas, por si selecciono alguna con un cliente anterior
			}
			
			@Override
			protected void onSubmit() {
				Cobranza cobranza = getCobranzaACargar((Cobranza)RegistrarCobranzaPage1.this.getDefaultModelObject());
				
				((FacturarOnLineAuthenticatedWebSession) getSession()).setAttribute("cob", cobranza);
				((FacturarOnLineAuthenticatedWebSession) getSession()).setAttribute("rz", clienteSearchAutoComplete.getModelObject());
				((FacturarOnLineAuthenticatedWebSession) getSession()).setAttribute("parcial", importeParcial);
				
				setResponsePage(RegistrarCobranzaPage2.class, new PageParameters());
			}

			/**
			 * Obtiene la cobranza con las factuas que fueron seleccionadas desde la pagina. Las demás se descartan.
			 */
			private Cobranza getCobranzaACargar(Cobranza cobranza) {
				FacturaVentaService facturaService = new FacturaVentaService();
				Cobranza cobranzaFinal = new Cobranza();
				List<FacturaVenta> listaFacturasFinal = new ArrayList<FacturaVenta>();
				if (cobranza.getListadoFacturas()!=null){
					for (FacturaVenta factura: cobranza.getListadoFacturas()){
						if (factura.isSeleccionada()){
							//listaFacturasFinal.add(factura);
							listaFacturasFinal.add((FacturaVenta) facturaService.get(factura.getId()));
						}
					}
					if (!listaFacturasFinal.isEmpty()){
						cobranzaFinal.setListadoFacturas(listaFacturasFinal);
					}
				}
				return cobranzaFinal;
			}

		};
		
		this.add(form);
		form.add(feedback.setOutputMarkupId(true));
		form.add(clienteSearchAutoComplete);
		form.add(importeFinalInput);
		form.add(importeParcialInput);
		form.add(facturasContainer);
	}
	
	private WebMarkupContainer crearFacturasDataView() {
		WebMarkupContainer facturasContainer = new WebMarkupContainer("facturasContainer");
		facturasContainer.setOutputMarkupPlaceholderTag(true);
		
		noExistenFacturas = new Label("noExistenFacturas","No existen facturas X COBRAR para el cliente seleccionado.");
		
		DataView<FacturaVenta> horariosDataView = new DataView<FacturaVenta>("listaFacturas", getFacturasDataProvider()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final Item<FacturaVenta> item) {
				CobranzaService cobranzaService = new CobranzaService();
				Locale locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				item.add(new Label("nro",item.getModelObject().getNro()));
				item.add(new Label("fecha",dateFormat.format(item.getModelObject().getFecha().getTime())));
				String total = (item.getModelObject().isNotaCredito()?"-":"") + Utils.round2Decimals(item.getModelObject().calculateTotal(), locale);				
				item.add(new Label("total",total));
				final double totalNetoFactura = cobranzaService.calculateTotalNetoFactura(item.getModelObject());
				String faltaPagar = item.getModelObject().getEstadoFacturaVenta().equals(EstadoFacturaVenta.COBRADO_PARCIAL) ? 
								" (Faltante: $" + Utils.round2Decimals(totalNetoFactura, locale) + ")"		:		"";
				item.add(new Label("estado",item.getModelObject().getEstadoFacturaVenta().getNombre() + faltaPagar));
				
				FacturaVenta factura = new FacturaVenta();
				factura.setId(item.getModelObject().getId());
				factura.setSeleccionada(false);
				
				AjaxCheckBox checkbox = new AjaxCheckBox("checkbox",new PropertyModel<Boolean>(Model.of(factura), "seleccionada")){
					private static final long serialVersionUID = 1L;

					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						boolean isChecked = (Boolean) getDefaultModelObject();
						if (isChecked){
							if ( ! item.getModelObject().isNotaCredito() ) { 
								importeFinalInput.setDefaultModelObject((Double)importeFinalInput.getDefaultModelObject() + totalNetoFactura);
							} else {
								importeFinalInput.setDefaultModelObject((Double)importeFinalInput.getDefaultModelObject() - totalNetoFactura);
							}
							cantidadFacturasSeleccionadas++;
						}else{
							if ( ! item.getModelObject().isNotaCredito() ) { 
								importeFinalInput.setDefaultModelObject((Double)importeFinalInput.getDefaultModelObject() - totalNetoFactura);
							} else {
								importeFinalInput.setDefaultModelObject((Double)importeFinalInput.getDefaultModelObject() + totalNetoFactura);
							}
							cantidadFacturasSeleccionadas--;
						}								
						target.add(importeFinalInput);
						actualizarImporteParcial(target);
					}

				};
				
				Cobranza cobranza = (Cobranza)RegistrarCobranzaPage1.this.getDefaultModelObject();
				if (cobranza.getListadoFacturas()==null){
					cobranza.setListadoFacturas(new ArrayList<FacturaVenta>());
				}
				
				cobranza.getListadoFacturas().add(factura);
				
				item.add(checkbox);
				
				noExistenFacturas.setVisible(false);				
			}
		};
		
		facturasContainer.add(horariosDataView);
		facturasContainer.add(noExistenFacturas);
		return facturasContainer;
	}

	private IDataProvider<FacturaVenta> getFacturasDataProvider() {
		Date date = null;
		Integer idEvento = null;
		return new FacturasVentaDataProvider(clienteSearchAutoComplete.getModel(), Model.of(idEvento), Model.of(date), Model.of(true), null, Model.of(false));
	}
	
	private void actualizarImporteParcial(AjaxRequestTarget target) {
		importeParcialInput.setEnabled(cantidadFacturasSeleccionadas==1);
		if (cantidadFacturasSeleccionadas!=1) {
			importeParcialInput.setDefaultModelObject(0d);			//Reseteo el label de importe Parcial
		}
		target.add(importeParcialInput);
	}
}
