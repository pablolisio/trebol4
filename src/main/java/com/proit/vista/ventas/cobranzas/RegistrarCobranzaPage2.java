package com.proit.vista.ventas.cobranzas;

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
import org.apache.wicket.markup.html.form.CheckBox;
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
import com.proit.modelo.ventas.Cliente;
import com.proit.modelo.ventas.Cobranza;
import com.proit.modelo.ventas.Cobro;
import com.proit.servicios.BancoService;
import com.proit.servicios.ventas.ClienteService;
import com.proit.servicios.ventas.CobranzaService;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.CustomTextFieldDouble;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class RegistrarCobranzaPage2 extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	private Cliente cliente;
	
	private double importeEfectivo;
	
	private CustomTextFieldDouble percIvaInput;
	private CustomTextFieldDouble percIibbInput;
	private CustomTextFieldDouble percGciasInput;
	private CustomTextFieldDouble percSUSSInput;
	private CustomTextFieldDouble otrasPercInput;
	
	private Locale locale;
	
	public RegistrarCobranzaPage2() {
		
		locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
		
		setearDefaultModel();
		
		setearCliente();
		
		crearForm();
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
	}
	
	private void setearDefaultModel() {
		Cobranza cobranza = (Cobranza) ((FacturarOnLineAuthenticatedWebSession) getSession()).getAttribute("cob");
		cobranza.setListadoCobros(new ArrayList<Cobro>());
		
		//Agrego un cheque por defecto
		Cobro cobro = new Cobro();
		cobro.setModoCobro(ModoPago.CHEQUE);
		cobro.setCobranza(cobranza);
		cobranza.getListadoCobros().add(cobro);
		
		//Agrego una transferencia por defecto
		Cobro cobro2 = new Cobro();
		cobro2.setModoCobro(ModoPago.TRANSFERENCIA);
		cobro2.setCobranza(cobranza);
		cobranza.getListadoCobros().add(cobro2);
		
		this.setDefaultModel(Model.of(cobranza));
	}
	
	private void setearCliente(){
		ClienteService clienteService = new ClienteService();
		String razonSocial = (String) ((FacturarOnLineAuthenticatedWebSession) getSession()).getAttribute("rz");
		cliente = clienteService.getByRazonSocial(razonSocial);
	}
	
	private void crearForm() {
		FeedbackPanel feedback = new FeedbackPanel("feedback");
		
		String modosCobroStr = "Modos de cobros disponibles para el cliente " + cliente.getRazonSocial() + ": " + cliente.getModosCobroString();
		Label modosCobro = new Label("modosCobro",modosCobroStr);
		
		WebMarkupContainer seccionEfectivo = new WebMarkupContainer("seccionEfectivo");
		WebMarkupContainer seccionCheques = new WebMarkupContainer("seccionCheques");
		WebMarkupContainer seccionTransferencias = new WebMarkupContainer("seccionTransferencias");
		
		final CustomTextFieldDouble importeEfectivoInput = new CustomTextFieldDouble("importeEfectivo", new PropertyModel<Double>(this, "importeEfectivo"));
		importeEfectivoInput.setRequired(true);
		importeEfectivoInput.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(importeEfectivoInput);
            }
        });
		
		Label datosBcariosTransferencia = new Label("datosBcariosTransferencia", Utils.generarDatosBancarios(cliente.getCuitCuil(), null) );
		
		Cobranza cobranza = (Cobranza)RegistrarCobranzaPage2.this.getDefaultModelObject();
		CobranzaService cobranzaService = new CobranzaService();
		final double importeParcial = (Double) ((FacturarOnLineAuthenticatedWebSession) getSession()).getAttribute("parcial");
		final double importeTotalNeto = importeParcial>0 ? 0 : cobranzaService.calculateTotalNetoFacturas(cobranza.getListadoFacturas());
		
		Label importeLbl = new Label("importe", importeParcial>0 ? "$ " + Utils.round2Decimals(importeParcial, locale) : "$ " + Utils.round2Decimals(importeTotalNeto, locale));
		
		Form<?> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onValidate() {
				super.onValidate();
				boolean validacionOK = true;
				
				CobranzaService cobranzaService = new CobranzaService();
				Cobranza cobranza = (Cobranza)RegistrarCobranzaPage2.this.getDefaultModelObject();
				List<Cobro> listadoCobros = obtenerListadoCobrosPreparado(cobranza);
				
				double totalCobros = cobranzaService.calculateTotalCobros(listadoCobros);
				double totalRetenciones = cobranza.getPercIva() + cobranza.getPercIibb() + cobranza.getPercGcias() + cobranza.getPercSUSS() + cobranza.getOtrasPerc();
				double sumaCobrosYRetenciones = totalCobros + totalRetenciones;
				
				//Valido que: suma de facturas vta seleccionadas (con iva incluido) = suma de pagos + suma retenciones
				//Si se eligio importe parcial, se valida que el total de (cobros+retenciones) sea dicho importe parcial, sino debe ser igual al importe total
				if ( importeParcial>0 && ! Utils.round2Decimals(sumaCobrosYRetenciones, locale).equals(Utils.round2Decimals(importeParcial, locale)) ) {
					String errorString = "El Total de Cobros+Retenciones debe ser igual al Importe Parcial indicado en el paso anterior. "
										+ "Importe Parcial: $"+ Utils.round2Decimals(importeParcial, locale) + ". "
										+ "Total Cobros+Retenciones: $" + Utils.round2Decimals(sumaCobrosYRetenciones, locale) + ".";
					validacionOK = informarError(errorString);
				} else if ( importeParcial==0 && ! Utils.round2Decimals(sumaCobrosYRetenciones, locale).equals(Utils.round2Decimals(importeTotalNeto, locale)) ){
					String errorString = "El Total de Cobros+Retenciones debe ser igual al Importe Total indicado en el paso anterior. "
							+ "Importe Total: $"+ Utils.round2Decimals(importeTotalNeto, locale) + ". "
							+ "Total Cobros+Retenciones: $" + Utils.round2Decimals(sumaCobrosYRetenciones, locale) + ".";
					validacionOK = informarError(errorString);
				}
				
				if ( ! cobranzaService.todoChequeTieneBanco(listadoCobros) ) {
					validacionOK = informarError("Todo cheque debe contener Banco.");
				}
				if ( ! cobranzaService.todoChequeTieneNro(listadoCobros) ) {
					validacionOK = informarError("Todo cheque debe contener Nro.");
				}
				if ( ! cobranzaService.todoChequeTieneFecha(listadoCobros) ) {
					validacionOK = informarError("Todo cheque debe contener Fecha.");
				}
				
				if ( ! cobranzaService.todaTransferenciaTieneBanco(listadoCobros) ) {
					validacionOK = informarError("Toda transferencia debe contener Banco.");
				}
				if ( ! cobranzaService.todaTransferenciaTieneFecha(listadoCobros) ) {
					validacionOK = informarError("Toda transferencia debe contener Fecha.");
				}
				
				//El importe de los cheques no hace falta validarlos porque ya vienen validados/corregidos (idem con importes transferencias)
				
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
				Cobranza cobranza = (Cobranza)RegistrarCobranzaPage2.this.getDefaultModelObject();
				cobranza.setListadoCobros(obtenerListadoCobrosPreparado(cobranza));
				
				((FacturarOnLineAuthenticatedWebSession) getSession()).setAttribute("cob", cobranza);
				
				setResponsePage(RegistrarCobranzaPage3.class, new PageParameters());
			}

			/**
			 * Elimino los cobros que fueron eliminados desde el boton eliminar de la grilla.
			 * Solo tomo los que tienen importe mayor a cero
			 * Ademas Agrego los cobros de Efectivo
			 * @param cobranza
			 */
			private List<Cobro> obtenerListadoCobrosPreparado(Cobranza cobranza) {
				List<Cobro> nuevoListadoCobros = new ArrayList<Cobro>();
				for (Cobro cobro : cobranza.getListadoCobros()) {
					if (!cobro.isBorrado()) {
						if (cobro.getImporte()!=0) {
							nuevoListadoCobros.add(cobro);
						}
					}
				}
				if (importeEfectivo>0) {
					Cobro cobro = new Cobro();
					cobro.setModoCobro(ModoPago.EFECTIVO);
					cobro.setImporte(importeEfectivo);
					cobro.setCobranza(cobranza);
					nuevoListadoCobros.add(cobro);
				}
				return nuevoListadoCobros;
			}
		};
		
		final WebMarkupContainer chequesContainer = new WebMarkupContainer("chequesContainer");
		chequesContainer.setOutputMarkupPlaceholderTag(true);
		
		seccionCheques.add(new AjaxFallbackLink<Cobro>("agregarCheque") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				Cobranza cobranza = (Cobranza)RegistrarCobranzaPage2.this.getDefaultModelObject();
				Cobro cobro = new Cobro();
				cobro.setModoCobro(ModoPago.CHEQUE);
				cobro.setCobranza(cobranza);
				((Cobranza)RegistrarCobranzaPage2.this.getDefaultModelObject()).getListadoCobros().add(cobro);
				target.add(chequesContainer);
			}
		});
		
		
		final WebMarkupContainer transferenciasContainer = new WebMarkupContainer("transferenciasContainer");
		transferenciasContainer.setOutputMarkupPlaceholderTag(true);		
		
		seccionTransferencias.add(new AjaxFallbackLink<Cobro>("agregarTransferencia") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				Cobranza cobranza = (Cobranza)RegistrarCobranzaPage2.this.getDefaultModelObject();
				Cobro cobro = new Cobro();
				cobro.setModoCobro(ModoPago.TRANSFERENCIA);
				cobro.setCobranza(cobranza);
				((Cobranza)RegistrarCobranzaPage2.this.getDefaultModelObject()).getListadoCobros().add(cobro);
				target.add(transferenciasContainer);
			}
		});
		
		this.add(form);
		
		agregarRetenciones(form);		
		
		form.add(feedback.setOutputMarkupId(true));
		form.add(modosCobro);
		form.add(importeLbl);
		
		seccionEfectivo.add(importeEfectivoInput);
		seccionTransferencias.add(datosBcariosTransferencia);
		addTransferenciasList(transferenciasContainer);
		seccionTransferencias.add(transferenciasContainer);
		addChequesList(chequesContainer);
		seccionCheques.add(chequesContainer);
		
		form.add(seccionEfectivo);
		form.add(seccionTransferencias);
		form.add(seccionCheques);
		
		//Seteo visibilidades de Modos de Cobro
		seccionEfectivo.setVisible(cliente.isModoEfectivo());
		seccionCheques.setVisible(cliente.isModoCheque());
		seccionTransferencias.setVisible(cliente.isModoTransferencia());
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
		percIibbInput.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
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

	private void addChequesList(final WebMarkupContainer chequesContainer) {
		IDataProvider<Cobro> cobrosDataProvider = getChequesProvider();
		
		DataView<Cobro> dataView = new DataView<Cobro>("listaCheques", cobrosDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<Cobro> item) {
				IModel<String> bancoChequeModel = new PropertyModel<String>(item.getModelObject(), "bancoCheque");
				TextField<String> bancoChequeTextField = new TextField<String>("bancoCheque", bancoChequeModel);
				bancoChequeTextField.add(StringValidator.maximumLength(255));
				
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
				
				item.add(bancoChequeTextField);
				item.add(nroChequeTextField);
				item.add(ajaxDatePicker);
				item.add(importeChequeTextField);
				
				//Utilizo lo siguiente para que todos los campos ingresados en la grilla de la pagina no sean eliminados al intentar agregar o quitar una fila.
				bancoChequeTextField.add(createNewAjaxFormComponentUpdatingBehavior());
				nroChequeTextField.add(createNewAjaxFormComponentUpdatingBehavior());
				ajaxDatePicker.add(createNewAjaxFormComponentUpdatingBehavior());
				importeChequeTextField.add(createNewAjaxFormComponentUpdatingBehavior());
				
				item.add(new AjaxFallbackLink<Cobro>("eliminar", item.getModel()) {			
					private static final long serialVersionUID = 1L;
					
					@Override
					public void onClick(AjaxRequestTarget target) {
						Cobro cobroSeleccionado = (Cobro) getModelObject();
						cobroSeleccionado.setBorrado(true);
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
		IDataProvider<Cobro> cobrosDataProvider = getTransferenciasProvider();
		
		DataView<Cobro> dataView = new DataView<Cobro>("listaTransferencias", cobrosDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<Cobro> item) {
				DropDownChoice<Banco> bancoDropDownChoice = crearBancoTransferenciaDropDownChoiceCheque(item);
				
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
				
				item.add(bancoDropDownChoice);
				item.add(ajaxDatePicker);
				item.add(importeTransferenciaTextField);
				
				//Utilizo lo siguiente para que todos los campos ingresados en la grilla de la pagina no sean eliminados al intentar agregar o quitar una fila.
				bancoDropDownChoice.add(createNewAjaxFormComponentUpdatingBehavior());
				ajaxDatePicker.add(createNewAjaxFormComponentUpdatingBehavior());
				importeTransferenciaTextField.add(createNewAjaxFormComponentUpdatingBehavior());
				
				item.add(new AjaxFallbackLink<Cobro>("eliminar", item.getModel()) {					
					private static final long serialVersionUID = 1L;
					
					@Override
					public void onClick(AjaxRequestTarget target) {
						Cobro cobroSeleccionado = (Cobro) getModelObject();
						cobroSeleccionado.setBorrado(true);
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
	
	private DropDownChoice<Banco> crearBancoTransferenciaDropDownChoiceCheque(Item<Cobro> item) {
		BancoService bancoService = new BancoService();
		List<Banco> bancos = bancoService.getBancos();
		
		IModel<Banco> bancoModel =  new PropertyModel<Banco>(item.getModelObject(), "bancoTransferencia");
		bancoModel.setObject(bancoService.getBancoActual(bancos));
		DropDownChoice<Banco> bancoDropDownChoice = new DropDownChoice<Banco>("bancoTransferencia", bancoModel, bancos, new ChoiceRenderer<Banco>("nombre"));
		//bancoDropDownChoice.setRequired(true);
		
		bancoDropDownChoice.setNullValid(true);
		return bancoDropDownChoice;
	}
	
	private AjaxDatePicker crearChequeDatePicker(Item<Cobro> item) {
		IModel<Date> fechaModel = new PropertyModel<Date>(item.getModelObject(), "fechaAsDate");
		AjaxDatePicker ajaxDatePicker = new AjaxDatePicker("datepickerCheque", fechaModel, "dd/MM/yyyy", new Options());
		//ajaxDatePicker.setRequired(true);
		return ajaxDatePicker;
	}
	
	private AjaxDatePicker crearTransferenciaDatePicker(Item<Cobro> item) {
		IModel<Date> fechaModel = new PropertyModel<Date>(item.getModelObject(), "fechaAsDate");
		AjaxDatePicker ajaxDatePicker = new AjaxDatePicker("datepickerTransferencia", fechaModel, "dd/MM/yyyy", new Options());
		//ajaxDatePicker.setRequired(true);
		return ajaxDatePicker;
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
				List<Cobro> listaCobros = ((Cobranza)RegistrarCobranzaPage2.this.getDefaultModelObject()).getListadoCobros();
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
	
	private IDataProvider<Cobro> getTransferenciasProvider() {
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
				List<Cobro> listaCobros = ((Cobranza)RegistrarCobranzaPage2.this.getDefaultModelObject()).getListadoCobros();
				List<Cobro> returnList = new ArrayList<Cobro>();
				for (Cobro cobro : listaCobros) {
					if (cobro.isTransferencia()) {
						returnList.add(cobro);
					}
				}
				return returnList;
			}
        	
        };
		return cobrosDataProvider;
	}
	
}