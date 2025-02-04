package com.proit.vista.compras.ordenes.normal;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.StringValidator;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.ui.form.datepicker.AjaxDatePicker;
import com.proit.modelo.Banco;
import com.proit.modelo.ModoPago;
import com.proit.modelo.compras.CobroAlternativo;
import com.proit.modelo.compras.OrdenPago;
import com.proit.modelo.compras.Pago;
import com.proit.modelo.compras.Proveedor;
import com.proit.servicios.BancoService;
import com.proit.servicios.compras.CobroAlternativoService;
import com.proit.servicios.compras.OrdenPagoService;
import com.proit.servicios.compras.ProveedorService;
import com.proit.utils.Constantes;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.CustomTextFieldDouble;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class RegistrarOrdenPagoNormalPage2 extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	private Proveedor proveedor;
	
	private double importeEfectivo;
	private double importeTransferencia3ros;
	private double importeTarjetaCredito;
	
	private CobroAlternativo cobroAlternativo;
	
	private Locale locale;
	
	public RegistrarOrdenPagoNormalPage2() {
		
		locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
		
		setearDefaultModel();
		
		setearProveedor();
		
		crearForm();
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
	}
	
	private void setearDefaultModel() {
		OrdenPago ordenPago = (OrdenPago) ((FacturarOnLineAuthenticatedWebSession) getSession()).getAttribute("op");
		ordenPago.setListadoPagos(new ArrayList<Pago>());
		
		//Agrego un cheque por defecto
		Pago pago = new Pago();
		pago.setModoPago(ModoPago.CHEQUE);
		pago.setOrdenPago(ordenPago);
		ordenPago.getListadoPagos().add(pago);
		
		//Agrego una transferencia por defecto
		Pago pago2 = new Pago();
		pago2.setModoPago(ModoPago.TRANSFERENCIA);
		pago2.setOrdenPago(ordenPago);
		ordenPago.getListadoPagos().add(pago2);
		
		this.setDefaultModel(Model.of(ordenPago));
	}
	
	private void setearProveedor(){
		ProveedorService proveedorService = new ProveedorService();
		String razonSocial = (String) ((FacturarOnLineAuthenticatedWebSession) getSession()).getAttribute("rz");
		proveedor = proveedorService.getByRazonSocial(razonSocial);
	}
	
	private void crearForm() {
		FeedbackPanel feedback = new FeedbackPanel("feedback");
		
		String modosPagoStr = "Modos de pago disponibles para el proveedor " + proveedor.getRazonSocial() + ": " + proveedor.getModosPagoString();
		Label modosPago = new Label("modosPago",modosPagoStr);
		
		WebMarkupContainer seccionEfectivo = new WebMarkupContainer("seccionEfectivo");
		WebMarkupContainer seccionCheques = new WebMarkupContainer("seccionCheques");
		WebMarkupContainer seccionTransferencias = new WebMarkupContainer("seccionTransferencias");
		WebMarkupContainer seccionTransferencia3ro = new WebMarkupContainer("seccionTransferencia3ro");
		WebMarkupContainer seccionTarjetaCredito = new WebMarkupContainer("seccionTarjetaCredito");
		
		final CustomTextFieldDouble importeEfectivoInput = new CustomTextFieldDouble("importeEfectivo", new PropertyModel<Double>(this, "importeEfectivo"));
		importeEfectivoInput.setRequired(true);
		importeEfectivoInput.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(importeEfectivoInput);
            }
        });
		
		Label datosBcariosTransferencia = new Label("datosBcariosTransferencia", Utils.generarDatosBancarios(proveedor.getCuitCuil(), proveedor.getCuentaBancaria()) );
		
		final CustomTextFieldDouble importeTransferencia3rosInput = new CustomTextFieldDouble("importeTransferencia3ros", new PropertyModel<Double>(this, "importeTransferencia3ros"));
		importeTransferencia3rosInput.setRequired(true);
		importeTransferencia3rosInput.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(importeTransferencia3rosInput);
            }
        });
		final DropDownChoice<CobroAlternativo> cobroAlternativoDropDownChoice = crearCobroAlternativoDropDownChoice();
		
		final CustomTextFieldDouble importeTarjetaCreditoInput = new CustomTextFieldDouble("importeTarjetaCredito", new PropertyModel<Double>(this, "importeTarjetaCredito"));
		importeTarjetaCreditoInput.setRequired(true);
		importeTarjetaCreditoInput.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(importeTarjetaCreditoInput);
            }
        });
		
		OrdenPago ordenPago = (OrdenPago)RegistrarOrdenPagoNormalPage2.this.getDefaultModelObject();
		OrdenPagoService ordenPagoService = new OrdenPagoService();
		final double importeParcial = (Double) ((FacturarOnLineAuthenticatedWebSession) getSession()).getAttribute("parcial");
		final double importeTotalNeto = importeParcial>0 ? 0 : ordenPagoService.calculateTotalNetoFacturas(ordenPago.getListadoFacturas());
		
		Label importeLbl = new Label("importe", importeParcial>0 ? "$ " + Utils.round2Decimals(importeParcial, locale) : "$ " + Utils.round2Decimals(importeTotalNeto, locale));
		
		Form<?> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onValidate() {
				super.onValidate();
				boolean validacionOK = true;
				
				OrdenPagoService ordenPagoService = new OrdenPagoService();
				OrdenPago ordenPago = (OrdenPago)RegistrarOrdenPagoNormalPage2.this.getDefaultModelObject();
				List<Pago> listadoPagos = obtenerListadoPagosPreparado(ordenPago);
				
				double totalPagos = ordenPagoService.calculateTotalPagos(listadoPagos);				
				
				//Si se eligio importe parcial, se valida que el total de pagos sea dicho importe parcial, sino debe ser igual al importe total
				if ( importeParcial>0 && ! Utils.round2Decimals(totalPagos, locale).equals(Utils.round2Decimals(importeParcial, locale)) ) {
					String errorString = "El Total de Pagos debe ser igual al Importe Parcial indicado en el paso anterior. "
										+ "Importe Parcial: $"+ Utils.round2Decimals(importeParcial, locale) + ". "
										+ "Total Pagos: $" + Utils.round2Decimals(totalPagos, locale) + ".";
					validacionOK = informarError(errorString);
				} else if ( importeParcial==0 && ! Utils.round2Decimals(totalPagos, locale).equals(Utils.round2Decimals(importeTotalNeto, locale)) ){
					String errorString = "El Total de Pagos debe ser igual al Importe Total indicado en el paso anterior. "
							+ "Importe Total: $"+ Utils.round2Decimals(importeTotalNeto, locale) + ". "
							+ "Total Pagos: $" + Utils.round2Decimals(totalPagos, locale) + ".";
					validacionOK = informarError(errorString);
				}
				
				if ( ! ordenPagoService.cantidadChequesPermitida(listadoPagos) ) {
					validacionOK = informarError("Se pueden cargar hasta " + Constantes.MAX_CANTIDAD_CHEQUES_POR_ORDEN_PERMITIDA + " cheques.");
				}
				if ( ! ordenPagoService.todoChequeTieneBanco(listadoPagos) ) {
					validacionOK = informarError("Todo cheque debe contener Banco.");
				}
				if ( ! ordenPagoService.todoChequeTieneNro(listadoPagos) ) {
					validacionOK = informarError("Todo cheque debe contener Nro.");
				}
				if ( ! ordenPagoService.todoChequeTieneFecha(listadoPagos) ) {
					validacionOK = informarError("Todo cheque debe contener Fecha.");
				}
				
				if ( ! ordenPagoService.cantidadTransferenciasPermitida(listadoPagos) ) {
					validacionOK = informarError("Se pueden cargar hasta " + Constantes.MAX_CANTIDAD_TRANSFERENCIAS_POR_ORDEN_PERMITIDA + " transferencias.");
				}
				
				//El importe de los cheques no hace falta validarlos porque ya vienen validados/corregidos (idem con importes transferencias)
				
				//Si se eligio importe de transferencia a terceros, se debe seleccionar un tercero, y si se elige un tercero el importe debe ser mayor a cero
				String tercero = cobroAlternativoDropDownChoice.getValue().toString();
				
				if ( importeTransferencia3ros>0 && tercero.isEmpty() ) {
					validacionOK = informarError("El Importe de Transferencia a Terceros es mayor a Cero, pero no seleccionó ningun Tercero.");
				} else if ( importeTransferencia3ros==0 && ! tercero.isEmpty() ) {
					validacionOK = informarError("Seleccionó un Tercero, por lo tanto el Importe de Transferencia a Terceros debe ser mayor a Cero.");
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
				OrdenPago ordenPago = (OrdenPago)RegistrarOrdenPagoNormalPage2.this.getDefaultModelObject();
				ordenPago.setListadoPagos(obtenerListadoPagosPreparado(ordenPago));
				
				((FacturarOnLineAuthenticatedWebSession) getSession()).setAttribute("op", ordenPago);
				
				setResponsePage(RegistrarOrdenPagoNormalPage3.class, new PageParameters());
			}

			/**
			 * Elimino los pagos que fueron eliminados desde el boton eliminar de la grilla.
			 * Solo tomo los que tienen importe mayor a cero
			 * Ademas Agrego los Pagos de Efectivo, Transf a 3ros, y Tarjeta Credito
			 * @param ordenPago
			 */
			private List<Pago> obtenerListadoPagosPreparado(OrdenPago ordenPago) {
				List<Pago> nuevoListadoPagos = new ArrayList<Pago>();
				for (Pago pago : ordenPago.getListadoPagos()) {
					if (!pago.isBorrado()) {
						if (pago.getImporte()!=0) {
							nuevoListadoPagos.add(pago);
						}
					}
				}
				if (importeEfectivo>0) {
					Pago pago = new Pago();
					pago.setModoPago(ModoPago.EFECTIVO);
					pago.setImporte(importeEfectivo);
					pago.setOrdenPago(ordenPago);
					nuevoListadoPagos.add(pago);
				}
				if (importeTransferencia3ros>0) {
					Pago pago = new Pago();
					pago.setModoPago(ModoPago.TRANSFERENCIA_3RO);
					pago.setImporte(importeTransferencia3ros);
					pago.setCobroAlternativo(cobroAlternativo);
					pago.setOrdenPago(ordenPago);
					nuevoListadoPagos.add(pago);
				}
				if (importeTarjetaCredito>0) {
					Pago pago = new Pago();
					pago.setModoPago(ModoPago.TARJETA_CRED);
					pago.setImporte(importeTarjetaCredito);
					pago.setOrdenPago(ordenPago);
					nuevoListadoPagos.add(pago);
				}
				return nuevoListadoPagos;
			}
		};
		
		final WebMarkupContainer chequesContainer = new WebMarkupContainer("chequesContainer");
		chequesContainer.setOutputMarkupPlaceholderTag(true);
		
		seccionCheques.add(new AjaxFallbackLink<Pago>("agregarCheque") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				OrdenPago ordenPago = (OrdenPago)RegistrarOrdenPagoNormalPage2.this.getDefaultModelObject();
				Pago pago = new Pago();
				pago.setModoPago(ModoPago.CHEQUE);
				pago.setOrdenPago(ordenPago);
				((OrdenPago)RegistrarOrdenPagoNormalPage2.this.getDefaultModelObject()).getListadoPagos().add(pago);
				target.add(chequesContainer);
			}
		});
		
		
		final WebMarkupContainer transferenciasContainer = new WebMarkupContainer("transferenciasContainer");
		transferenciasContainer.setOutputMarkupPlaceholderTag(true);		
		
		seccionTransferencias.add(new AjaxFallbackLink<Pago>("agregarTransferencia") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				OrdenPago ordenPago = (OrdenPago)RegistrarOrdenPagoNormalPage2.this.getDefaultModelObject();
				Pago pago = new Pago();
				pago.setModoPago(ModoPago.TRANSFERENCIA);
				pago.setOrdenPago(ordenPago);
				((OrdenPago)RegistrarOrdenPagoNormalPage2.this.getDefaultModelObject()).getListadoPagos().add(pago);
				target.add(transferenciasContainer);
			}
		});
		
		this.add(form);
		form.add(feedback.setOutputMarkupId(true));
		form.add(modosPago);
		form.add(importeLbl);
		
		seccionEfectivo.add(importeEfectivoInput);
		seccionTransferencias.add(datosBcariosTransferencia);
		addTransferenciasList(transferenciasContainer);
		seccionTransferencias.add(transferenciasContainer);
		seccionTransferencia3ro.add(importeTransferencia3rosInput);
		seccionTransferencia3ro.add(cobroAlternativoDropDownChoice);
		seccionTarjetaCredito.add(importeTarjetaCreditoInput);
		addChequesList(chequesContainer);
		seccionCheques.add(chequesContainer);
		
		form.add(seccionEfectivo);
		form.add(seccionTransferencias);
		form.add(seccionTransferencia3ro);
		form.add(seccionCheques);
		form.add(seccionTarjetaCredito);
		
		//Seteo visibilidades de Modos de Pago
		seccionEfectivo.setVisible(proveedor.isModoEfectivo());
		seccionCheques.setVisible(proveedor.isModoCheque());
		seccionTransferencias.setVisible(proveedor.isModoTransferencia());
		seccionTransferencia3ro.setVisible(proveedor.tieneCobrosAlternativos());
		seccionTarjetaCredito.setVisible(proveedor.isModoTarjetaCredito());
	}
	
	private DropDownChoice<CobroAlternativo> crearCobroAlternativoDropDownChoice() {
		CobroAlternativoService cobroAlternativoService = new CobroAlternativoService();
		List<CobroAlternativo> cobrosAlternativos = cobroAlternativoService.getCobrosAlternativos(proveedor.getId());
		
		IModel<CobroAlternativo> cobroAlternativoModel = new PropertyModel<CobroAlternativo>(this, "cobroAlternativo");
		
		ChoiceRenderer<CobroAlternativo> choiceRenderer = new ChoiceRenderer<CobroAlternativo>("titular"){
			private static final long serialVersionUID = 1L;
			@Override
		 	public Object getDisplayValue(CobroAlternativo object){
		        return super.getDisplayValue(object) + (object.getCuitCuil()!=null?" ("+object.getCuitCuil() + ")":"");
		    }
		};
		
		DropDownChoice<CobroAlternativo> cobroAlternativoDropDownChoice = new DropDownChoice<CobroAlternativo>("cobroAlternativo", cobroAlternativoModel, cobrosAlternativos, choiceRenderer);
		
		cobroAlternativoDropDownChoice.setNullValid(true);
		return cobroAlternativoDropDownChoice;
	}
	
	private void addChequesList(final WebMarkupContainer chequesContainer) {
		IDataProvider<Pago> pagosDataProvider = getChequesProvider();
		
		DataView<Pago> dataView = new DataView<Pago>("listaCheques", pagosDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<Pago> item) {
				DropDownChoice<Banco> bancoDropDownChoice = crearBancoDropDownChoiceCheque(item);
				
				IModel<String> nroModel = new PropertyModel<String>(item.getModelObject(), "nroCheque");
				TextField<String> nroChequeTextField = new TextField<String>("nroCheque", nroModel);
				nroChequeTextField.add(StringValidator.maximumLength(255));
				
				AjaxDatePicker ajaxDatePicker = crearChequeDatePicker(item);
				ajaxDatePicker.setOutputMarkupId(true);
				
				IModel<Double> importeModel = new PropertyModel<Double>(item.getModelObject(), "importe");
				final CustomTextFieldDouble importeChequeTextField = new CustomTextFieldDouble("importeCheque", importeModel);
				importeChequeTextField.setOutputMarkupId(true);
				importeChequeTextField.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
					private static final long serialVersionUID = 1L;
					protected void onUpdate(AjaxRequestTarget target) {
						target.add(importeChequeTextField);
		            }
		        });
				
				item.add(bancoDropDownChoice);
				item.add(nroChequeTextField);
				item.add(ajaxDatePicker);
				item.add(importeChequeTextField);
				
				//Utilizo lo siguiente para que todos los campos ingresados en la grilla de la pagina no sean eliminados al intentar agregar o quitar una fila.
				bancoDropDownChoice.add(createNewAjaxFormComponentUpdatingBehavior());
				nroChequeTextField.add(createNewAjaxFormComponentUpdatingBehavior());
				ajaxDatePicker.add(createNewAjaxFormComponentUpdatingBehavior());
				importeChequeTextField.add(createNewAjaxFormComponentUpdatingBehavior());
				
				item.add(new AjaxFallbackLink<Pago>("eliminar", item.getModel()) {			
					private static final long serialVersionUID = 1L;
					
					@Override
					public void onClick(AjaxRequestTarget target) {
						Pago pagoSeleccionado = (Pago) getModelObject();
						pagoSeleccionado.setBorrado(true);
						target.add(chequesContainer);
					}
				});
				
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
		
		chequesContainer.add(dataView);
	}
	
	private void addTransferenciasList(final WebMarkupContainer transferenciasContainer) {
		IDataProvider<Pago> pagosDataProvider = getTransferenciasProvider();
		
		DataView<Pago> dataView = new DataView<Pago>("listaTransferencias", pagosDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<Pago> item) {
				AjaxDatePicker ajaxDatePicker = crearTransferenciaDatePicker(item);
				ajaxDatePicker.setOutputMarkupId(true);
				
				IModel<Double> importeModel = new PropertyModel<Double>(item.getModelObject(), "importe");
				final CustomTextFieldDouble importeTransferenciaTextField = new CustomTextFieldDouble("importeTransferencia", importeModel);
				importeTransferenciaTextField.setOutputMarkupId(true);
				importeTransferenciaTextField.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
					private static final long serialVersionUID = 1L;
					protected void onUpdate(AjaxRequestTarget target) {
						target.add(importeTransferenciaTextField);
		            }
		        });
				
				item.add(ajaxDatePicker);
				item.add(importeTransferenciaTextField);
				
				//Utilizo lo siguiente para que todos los campos ingresados en la grilla de la pagina no sean eliminados al intentar agregar o quitar una fila.
				ajaxDatePicker.add(createNewAjaxFormComponentUpdatingBehavior());
				importeTransferenciaTextField.add(createNewAjaxFormComponentUpdatingBehavior());
				
				item.add(new AjaxFallbackLink<Pago>("eliminar", item.getModel()) {					
					private static final long serialVersionUID = 1L;
					
					@Override
					public void onClick(AjaxRequestTarget target) {
						Pago pagoSeleccionado = (Pago) getModelObject();
						pagoSeleccionado.setBorrado(true);
						target.add(transferenciasContainer);
					}
				});
				
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
		
		transferenciasContainer.add(dataView);
	}
	
	private DropDownChoice<Banco> crearBancoDropDownChoiceCheque(Item<Pago> item) {
		BancoService bancoService = new BancoService();
		List<Banco> bancos = bancoService.getBancos();
		
		IModel<Banco> bancoModel =  new PropertyModel<Banco>(item.getModelObject(), "banco");
		bancoModel.setObject(bancoService.getBancoActual(bancos));
		DropDownChoice<Banco> bancoDropDownChoice = new DropDownChoice<Banco>("bancoCheque", bancoModel, bancos, new ChoiceRenderer<Banco>("nombre"));
		//bancoDropDownChoice.setRequired(true);
		
		bancoDropDownChoice.setNullValid(true);
		return bancoDropDownChoice;
	}
	
	private AjaxDatePicker crearChequeDatePicker(Item<Pago> item) {
		IModel<Date> fechaModel = new PropertyModel<Date>(item.getModelObject(), "fechaAsDate");
		AjaxDatePicker ajaxDatePicker = new AjaxDatePicker("datepickerCheque", fechaModel, "dd/MM/yyyy", new Options());
		//ajaxDatePicker.setRequired(true);
		return ajaxDatePicker;
	}
	
	private AjaxDatePicker crearTransferenciaDatePicker(Item<Pago> item) {
		IModel<Date> fechaModel = new PropertyModel<Date>(item.getModelObject(), "fechaAsDate");
		AjaxDatePicker ajaxDatePicker = new AjaxDatePicker("datepickerTransferencia", fechaModel, "dd/MM/yyyy", new Options());
		//ajaxDatePicker.setRequired(true);
		return ajaxDatePicker;
	}
	
	private IDataProvider<Pago> getChequesProvider() {
		IDataProvider<Pago> pagosDataProvider = new IDataProvider<Pago>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void detach() {
			}

			@Override
			public Iterator<Pago> iterator(long first, long count) {
				return getListadoTodosPagos().iterator();
			}

			@Override
			public long size() {
				return getListadoTodosPagos().size();
			}

			@Override
			public IModel<Pago> model(Pago Pago) {
				return new Model<Pago>(Pago);
			}
			
			private List<Pago> getListadoTodosPagos() {
				List<Pago> listaPagos = ((OrdenPago)RegistrarOrdenPagoNormalPage2.this.getDefaultModelObject()).getListadoPagos();
				List<Pago> returnList = new ArrayList<Pago>();
				for (Pago pago : listaPagos) {
					if (pago.isCheque()) {
						returnList.add(pago);
					}
				}
				return returnList;
			}
        	
        };
		return pagosDataProvider;
	}
	
	private IDataProvider<Pago> getTransferenciasProvider() {
		IDataProvider<Pago> pagosDataProvider = new IDataProvider<Pago>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void detach() {
			}

			@Override
			public Iterator<Pago> iterator(long first, long count) {
				return getListadoTodosPagos().iterator();
			}

			@Override
			public long size() {
				return getListadoTodosPagos().size();
			}

			@Override
			public IModel<Pago> model(Pago Pago) {
				return new Model<Pago>(Pago);
			}
			
			private List<Pago> getListadoTodosPagos() {
				List<Pago> listaPagos = ((OrdenPago)RegistrarOrdenPagoNormalPage2.this.getDefaultModelObject()).getListadoPagos();
				List<Pago> returnList = new ArrayList<Pago>();
				for (Pago pago : listaPagos) {
					if (pago.isTransferencia()) {
						returnList.add(pago);
					}
				}
				return returnList;
			}
        	
        };
		return pagosDataProvider;
	}
	
}