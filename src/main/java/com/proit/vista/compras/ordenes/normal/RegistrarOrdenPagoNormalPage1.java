package com.proit.vista.compras.ordenes.normal;

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
import com.proit.modelo.compras.EstadoFacturaCompra;
import com.proit.modelo.compras.FacturaCompra;
import com.proit.modelo.compras.OrdenPago;
import com.proit.servicios.compras.FacturaCompraService;
import com.proit.servicios.compras.OrdenPagoService;
import com.proit.utils.Constantes;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.CustomTextFieldDouble;
import com.proit.wicket.components.ProveedorSearchAutoCompleteTextField;
import com.proit.wicket.dataproviders.FacturasCompraDataProvider;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class RegistrarOrdenPagoNormalPage1 extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;

	private AutoCompleteTextField<String> proveedorSearchAutoComplete;
	private double importeFinal;
	private double importeParcial;
	
	private WebMarkupContainer facturasContainer;
	private CustomTextFieldDouble importeFinalInput;
	private CustomTextFieldDouble importeParcialInput;

	private int cantidadFacturasSeleccionadas;
	private Label noExistenFacturas;
	
	public RegistrarOrdenPagoNormalPage1() {		
		setearDefaultModel();
		
		cantidadFacturasSeleccionadas=0;
		
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
		
		proveedorSearchAutoComplete = new ProveedorSearchAutoCompleteTextField("proveedorSearchAutocomplete", new Model<String>()){
			private static final long serialVersionUID = 1L;			
			@Override
			protected void onSelected(AjaxRequestTarget target){
				OrdenPago ordenPago = (OrdenPago)RegistrarOrdenPagoNormalPage1.this.getDefaultModelObject();
				ordenPago.setListadoFacturas(new ArrayList<FacturaCompra>());		//Reseteo el listado de facturas, por si selecciono alguna con un proveedor anterior
					
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
		proveedorSearchAutoComplete.setRequired(true);
		/*proveedorSearchAutoComplete.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;

			protected void onUpdate(AjaxRequestTarget target) {
				OrdenPago ordenPago = (OrdenPago)RegistrarOrdenPagoNormalPage1.this.getDefaultModelObject();
				ordenPago.setListadoFacturas(new ArrayList<Factura>());		//Reseteo el listado de facturas, por si selecciono alguna con un proveedor anterior
					
				facturasContainer.setVisible(true);
				importeFinalInput.setDefaultModelObject(0d);			//Reseteo el label de importe Final
				importeParcialInput.setDefaultModelObject(0d);			//Reseteo el label de importe Parcial
				cantidadFacturasSeleccionadas = 0;
				actualizarImporteParcial(target);
				target.add(facturasContainer);
				target.add(importeFinalInput);
            }
        });*/    //ESTO NO ANDABA EN CHROME
		
		facturasContainer = crearFacturasDataView();
		facturasContainer.setVisible(false);
		
		Form<?> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onValidate() {
				super.onValidate();
				boolean validacionOK = true;
				
				String razonSocialProveedor = proveedorSearchAutoComplete.getModelObject();
				
				//Si se selecciono un proveedor, verifico si se selecciono alguna factura.
				if (razonSocialProveedor!=null) {
					OrdenPago ordenPago = (OrdenPago)RegistrarOrdenPagoNormalPage1.this.getDefaultModelObject();
					int cantidadFacturasSeleccionadas = 0;
					for (FacturaCompra factura: ordenPago.getListadoFacturas()){
						if (factura.isSeleccionada()){
							cantidadFacturasSeleccionadas ++;
						}
					}
					if (cantidadFacturasSeleccionadas==0) {
						validacionOK = informarError("Debe seleccionar al menos una factura.");
					} else if (cantidadFacturasSeleccionadas>Constantes.MAX_CANTIDAD_FACTURAS_POR_ORDEN_PERMITIDA) {
						validacionOK = informarError("Debe seleccionar como máximo " + Constantes.MAX_CANTIDAD_FACTURAS_POR_ORDEN_PERMITIDA + " facturas.");
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
				OrdenPago ordenPago = (OrdenPago)RegistrarOrdenPagoNormalPage1.this.getDefaultModelObject();
				ordenPago.setListadoFacturas(new ArrayList<FacturaCompra>());		//Reseteo el listado de facturas, por si selecciono alguna con un proveedor anterior
			}
			
			@Override
			protected void onSubmit() {
				OrdenPago ordenPago = getOrdenPagoACargar((OrdenPago)RegistrarOrdenPagoNormalPage1.this.getDefaultModelObject());
				
				((FacturarOnLineAuthenticatedWebSession) getSession()).setAttribute("op", ordenPago);
				((FacturarOnLineAuthenticatedWebSession) getSession()).setAttribute("rz", proveedorSearchAutoComplete.getModelObject());
				((FacturarOnLineAuthenticatedWebSession) getSession()).setAttribute("parcial", importeParcial);
				
				setResponsePage(RegistrarOrdenPagoNormalPage2.class, new PageParameters());
			}

			/**
			 * Obtiene la orden de pago con las factuas que fueron seleccionadas desde la pagina. Las demás se descartan.
			 */
			private OrdenPago getOrdenPagoACargar(OrdenPago ordenPago) {
				FacturaCompraService facturaService = new FacturaCompraService();
				OrdenPago ordenPagoFinal = new OrdenPago();
				List<FacturaCompra> listaFacturasFinal = new ArrayList<FacturaCompra>();
				if (ordenPago.getListadoFacturas()!=null){
					for (FacturaCompra factura: ordenPago.getListadoFacturas()){
						if (factura.isSeleccionada()){
							//listaFacturasFinal.add(factura);
							listaFacturasFinal.add((FacturaCompra) facturaService.get(factura.getId()));
						}
					}
					if (!listaFacturasFinal.isEmpty()){
						ordenPagoFinal.setListadoFacturas(listaFacturasFinal);
					}
				}
				return ordenPagoFinal;
			}

		};
		
		this.add(form);
		form.add(feedback.setOutputMarkupId(true));
		form.add(proveedorSearchAutoComplete);
		form.add(importeFinalInput);
		form.add(importeParcialInput);
		form.add(facturasContainer);
	}
	
	private WebMarkupContainer crearFacturasDataView() {
		WebMarkupContainer facturasContainer = new WebMarkupContainer("facturasContainer");
		facturasContainer.setOutputMarkupPlaceholderTag(true);
		
		noExistenFacturas = new Label("noExistenFacturas","No existen facturas cargadas para el proveedor seleccionado.");
		
		DataView<FacturaCompra> horariosDataView = new DataView<FacturaCompra>("listaFacturas", getFacturasDataProvider()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final Item<FacturaCompra> item) {
				OrdenPagoService ordenPagoService = new OrdenPagoService();
				Locale locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				item.add(new Label("nro",item.getModelObject().getNro()));
				item.add(new Label("fecha",dateFormat.format(item.getModelObject().getFecha().getTime())));
				String total = (item.getModelObject().isNotaCredito()?"-":"") + Utils.round2Decimals(item.getModelObject().calculateTotal(), locale);				
				item.add(new Label("total",total));
				final double totalNetoFactura = ordenPagoService.calculateTotalNetoFactura(item.getModelObject());
				String faltaPagar = item.getModelObject().getEstadoFactura().equals(EstadoFacturaCompra.PAGADA_PARCIAL) ? 
								" (Faltante: $" + Utils.round2Decimals(totalNetoFactura, locale) + ")"		:		"";
				item.add(new Label("estado",item.getModelObject().getEstadoFactura().getNombre() + faltaPagar));
				
				FacturaCompra factura = new FacturaCompra();
				factura.setId(item.getModelObject().getId());
				factura.setSeleccionada(false);
				/*factura.setEstadoFactura(item.getModelObject().getEstadoFactura());
				factura.setProveedor(item.getModelObject().getProveedor());
				factura.setFecha(item.getModelObject().getFecha());
				factura.setTipo(item.getModelObject().getTipo());
				factura.setNro(item.getModelObject().getNro());
				factura.setSubtotal(item.getModelObject().getSubtotal());
				factura.setIva(item.getModelObject().getIva());
				factura.setPercIva(item.getModelObject().getPercIva());
				factura.setPercIibb(item.getModelObject().getPercIibb());*/
				
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
				
				OrdenPago ordenPago = (OrdenPago)RegistrarOrdenPagoNormalPage1.this.getDefaultModelObject();
				if (ordenPago.getListadoFacturas()==null){
					ordenPago.setListadoFacturas(new ArrayList<FacturaCompra>());
				}
				
				ordenPago.getListadoFacturas().add(factura);
				
				item.add(checkbox);
				
				noExistenFacturas.setVisible(false);				
			}
		};
		
		facturasContainer.add(horariosDataView);
		facturasContainer.add(noExistenFacturas);
		return facturasContainer;
	}

	private IDataProvider<FacturaCompra> getFacturasDataProvider() {
		Date date = null;
		return new FacturasCompraDataProvider(proveedorSearchAutoComplete.getModel(), Model.of(date), Model.of(true), null, null);
	}
	
	private void actualizarImporteParcial(AjaxRequestTarget target) {
		importeParcialInput.setEnabled(cantidadFacturasSeleccionadas==1);
		if (cantidadFacturasSeleccionadas!=1) {
			importeParcialInput.setDefaultModelObject(0d);			//Reseteo el label de importe Parcial
		}
		target.add(importeParcialInput);
	}
}
