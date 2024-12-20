package com.proit.vista.compras.solicitudes.conversion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.ui.form.datepicker.AjaxDatePicker;
import com.proit.modelo.Banco;
import com.proit.modelo.ModoPago;
import com.proit.modelo.Rol;
import com.proit.modelo.Usuario;
import com.proit.modelo.compras.CobroAlternativo;
import com.proit.modelo.compras.CuentaBancaria;
import com.proit.modelo.compras.OrdenPago;
import com.proit.modelo.compras.Pago;
import com.proit.modelo.compras.PagoSolicitudPago;
import com.proit.modelo.compras.SolicitudPago;
import com.proit.servicios.BancoService;
import com.proit.servicios.UsuarioService;
import com.proit.servicios.compras.CobroAlternativoService;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.CustomTextFieldDouble;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class ConvertirAOPPage3 extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected Locale locale;
	
	protected SolicitudPago solicitudPago;
	
	protected double importeEfectivo;
	protected double importeTarjetaCredito;
	
	protected double importeTransferencia3ros;
	protected CobroAlternativo cobroAlternativo;	
	
	protected double importeTransferenciaSinProv;
	protected CuentaBancaria cuentaBancaria;
	protected String cuitCuil;
	
	protected double importeFinal;
	
	protected CustomTextFieldDouble importeFinalInput;
	
	public ConvertirAOPPage3(SolicitudPago solicitudPago) {
		this.solicitudPago = solicitudPago;
		
		locale =((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
	}
	
	protected void setearDefaultModel() {
		OrdenPago ordenPago = new OrdenPago();
		
		importeFinal = 0; //lo inicializo, y luego lo sumo abajo
		
		//Formas de Pago
		List<Pago> listaPagos = new ArrayList<Pago>();
		boolean seAgregoCheque = false;
		boolean seAgregoTransferencia = false;
		cuentaBancaria = new CuentaBancaria(); //por defecto , inicializo la cuentaBancaria, despues capaz se pisa el valor
		for (PagoSolicitudPago pagoSolicitudPago : solicitudPago.getListadoPagos()) {
			if (pagoSolicitudPago.isEfectivo()) {
				importeEfectivo = pagoSolicitudPago.getImporte();
			} else if (pagoSolicitudPago.isCheque()) {
				seAgregoCheque = true;
				Pago pago = new Pago();
				pago.setModoPago(ModoPago.CHEQUE);
				pago.setFecha(pagoSolicitudPago.getFecha());
				pago.setImporte(pagoSolicitudPago.getImporte());
				pago.setOrdenPago(ordenPago);
				listaPagos.add(pago);
			} else if (pagoSolicitudPago.isTransferencia()) {
				seAgregoTransferencia = true;
				Pago pago = new Pago();
				pago.setModoPago(ModoPago.TRANSFERENCIA);
				pago.setFecha(pagoSolicitudPago.getFecha());
				pago.setImporte(pagoSolicitudPago.getImporte());
				pago.setOrdenPago(ordenPago);
				listaPagos.add(pago);
			}
			//no van a venir transferencia a 3ros desde una solicitud, porque ese caso no esta contemplado (Visto con angel)
			/* else if (pagoSolicitudPago.isTransferencia3ro()) {  
				importeTransferencia3ros = pagoSolicitudPago.getImporte();
				cobroAlternativo = pagoSolicitudPago.getCobroAlternativo();
			}*/ else if (pagoSolicitudPago.isTransferenciaSinProv()) {
				importeTransferenciaSinProv = pagoSolicitudPago.getImporte();
				cuentaBancaria = pagoSolicitudPago.getCuentaBancaria();
				cuitCuil = pagoSolicitudPago.getCuitCuil();
			} else if (pagoSolicitudPago.isTarjetaCredito()) {
				importeTarjetaCredito = pagoSolicitudPago.getImporte();
			}
			importeFinal += pagoSolicitudPago.getImporte();
		}
		if (!seAgregoCheque) {
			//Agrego un cheque por defecto
			Pago pago = new Pago();
			pago.setModoPago(ModoPago.CHEQUE);
			pago.setOrdenPago(ordenPago);
			listaPagos.add(pago);
		}
		if (!seAgregoTransferencia) {
			//Agrego una transferencia por defecto
			Pago pago = new Pago();
			pago.setModoPago(ModoPago.TRANSFERENCIA);
			pago.setOrdenPago(ordenPago);
			listaPagos.add(pago);
		}
		ordenPago.setListadoPagos(listaPagos);
		
		//Detalles
		if (ordenPago.getFecha()==null){//Seteo fecha actual por defecto
			ordenPago.setFecha(Calendar.getInstance());
		}
		ordenPago.setConcepto(solicitudPago.getConcepto());
		ordenPago.setObservaciones(solicitudPago.getObservaciones());
		ordenPago.setEvento(solicitudPago.getEvento());
		ordenPago.setUsuarioSolicitante(solicitudPago.getUsuarioSolicitante());	
		
		ordenPago.setSolicitudPago(solicitudPago);
		
		this.setDefaultModel(Model.of(ordenPago));
	}
	
	protected void crearImporteFinalInput() {
		importeFinalInput = new CustomTextFieldDouble("importeFinal", new PropertyModel<Double>(this, "importeFinal"));
		importeFinalInput.setOutputMarkupId(true);
		importeFinalInput.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(importeFinalInput);
            }
        });
	}
	
	protected DropDownChoice<CobroAlternativo> crearCobroAlternativoDropDownChoice() {
		CobroAlternativoService cobroAlternativoService = new CobroAlternativoService();
		List<CobroAlternativo> cobrosAlternativos = cobroAlternativoService.getCobrosAlternativos(solicitudPago.getProveedor().getId());

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
	
	protected void addChequesList(final WebMarkupContainer chequesContainer) {
		IDataProvider<Pago> pagosDataProvider = getChequesProvider();

		DataView<Pago> dataView = new DataView<Pago>("listaCheques", pagosDataProvider) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<Pago> item) {
				DropDownChoice<Banco> bancoDropDownChoice = crearBancoDropDownChoiceCheque(item);

				IModel<String> nroModel = new PropertyModel<String>(item.getModelObject(), "nroCheque");
				TextField<String> nroChequeTextField = new TextField<String>("nroCheque", nroModel);

				AjaxDatePicker ajaxDatePicker = crearDatePickerCheque(item);
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
	
	protected void addTransferenciasList(final WebMarkupContainer transferenciasContainer) {
		IDataProvider<Pago> pagosDataProvider = getTransferenciasProvider();
		
		DataView<Pago> dataView = new DataView<Pago>("listaTransferencias", pagosDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<Pago> item) {
				AjaxDatePicker ajaxDatePicker = crearDatePickerTransferencia(item);
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
	
	protected DropDownChoice<Banco> crearBancoDropDownChoiceCheque(Item<Pago> item) {
		BancoService bancoService = new BancoService();
		List<Banco> bancos = bancoService.getBancos();

		IModel<Banco> bancoModel =  new PropertyModel<Banco>(item.getModelObject(), "banco");
		bancoModel.setObject(bancoService.getBancoActual(bancos));
		DropDownChoice<Banco> bancoDropDownChoice = new DropDownChoice<Banco>("bancoCheque", bancoModel, bancos, new ChoiceRenderer<Banco>("nombre"));
		//bancoDropDownChoice.setRequired(true);

		bancoDropDownChoice.setNullValid(true);
		return bancoDropDownChoice;
	}

	protected AjaxDatePicker crearDatePickerCheque(Item<Pago> item) {
		IModel<Date> fechaModel = new PropertyModel<Date>(item.getModelObject(), "fechaAsDate");
		AjaxDatePicker ajaxDatePicker = new AjaxDatePicker("datepickerCheque", fechaModel, "dd/MM/yyyy", new Options());
		//ajaxDatePicker.setRequired(true);
		return ajaxDatePicker;
	}
	
	protected AjaxDatePicker crearDatePickerTransferencia(Item<Pago> item) {
		IModel<Date> fechaModel = new PropertyModel<Date>(item.getModelObject(), "fechaAsDate");
		AjaxDatePicker ajaxDatePicker = new AjaxDatePicker("datepickerTransferencia", fechaModel, "dd/MM/yyyy", new Options());
		//ajaxDatePicker.setRequired(true);
		return ajaxDatePicker;
	}
	
	protected CustomTextFieldDouble crearImporteEfectivoInput() {
		final CustomTextFieldDouble importeEfectivoInput = new CustomTextFieldDouble("importeEfectivo", new PropertyModel<Double>(this, "importeEfectivo"));
		importeEfectivoInput.setRequired(true);
		importeEfectivoInput.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(importeEfectivoInput);
			}
		});
		return importeEfectivoInput;
	}
	
	protected CustomTextFieldDouble crearImporteTransferencia3rosInput() {
		final CustomTextFieldDouble importeTransferencia3rosInput = new CustomTextFieldDouble("importeTransferencia3ros", new PropertyModel<Double>(this, "importeTransferencia3ros"));
		importeTransferencia3rosInput.setRequired(true);
		importeTransferencia3rosInput.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(importeTransferencia3rosInput);
			}
		});
		return importeTransferencia3rosInput;
	}
	
	protected CustomTextFieldDouble crearImporteTransferenciaSinProvInput() {
		final CustomTextFieldDouble importeTransferenciaSinProvInput = new CustomTextFieldDouble("importeTransferenciaSinProv", new PropertyModel<Double>(this, "importeTransferenciaSinProv"));
		importeTransferenciaSinProvInput.setRequired(true);
		importeTransferenciaSinProvInput.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(importeTransferenciaSinProvInput);
			}
		});
		return importeTransferenciaSinProvInput;
	}
	
	protected CustomTextFieldDouble crearImporteTarjetaCreditoInput() {
		final CustomTextFieldDouble importeTarjetaCreditoInput = new CustomTextFieldDouble("importeTarjetaCredito", new PropertyModel<Double>(this, "importeTarjetaCredito"));
		importeTarjetaCreditoInput.setRequired(true);
		importeTarjetaCreditoInput.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(importeTarjetaCreditoInput);
			}
		});
		return importeTarjetaCreditoInput;
	}

	protected AjaxLink<String> crearHoyLink(final AjaxDatePicker ajaxDatePicker) {
		AjaxLink<String> hoyLink = new AjaxLink<String>("hoy") {			
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				ajaxDatePicker.setModelObject(Utils.firstMillisecondOfDay(Calendar.getInstance()).getTime());
				target.add(ajaxDatePicker);
			}
		};
		return hoyLink;
	}

	protected AjaxDatePicker crearDatePicker() {
		Options options = new Options();
		Calendar calendar = ((OrdenPago)getDefaultModelObject()).getFecha();
		IModel<Date> fechaModel = Model.of(calendar.getTime());
		AjaxDatePicker ajaxDatePicker = new AjaxDatePicker("datepicker", fechaModel, "dd/MM/yyyy", options) ;
		ajaxDatePicker.setRequired(true);
		return ajaxDatePicker;
	}
	
	protected DropDownChoice<Usuario> crearSolicitanteDropDownChoice() {
		UsuarioService usuarioService = new UsuarioService();
		List<Usuario> solicitantes = usuarioService.getUsuariosByRol(Rol.SOLICITANTE_PAGOS);
		
		IModel<Usuario> solicitanteModel = new PropertyModel<Usuario>(getDefaultModel(), "usuarioSolicitante");
		Usuario usuarioSeleccionado = ((OrdenPago)ConvertirAOPPage3.this.getDefaultModelObject()).getUsuarioSolicitante();
		solicitanteModel.setObject(usuarioSeleccionado);
		DropDownChoice<Usuario> solicitanteDropDownChoice = new DropDownChoice<Usuario>("solicitante", solicitanteModel, solicitantes, new ChoiceRenderer<Usuario>("nombreCompleto"));
		solicitanteDropDownChoice.setRequired(true);
		
		solicitanteDropDownChoice.setNullValid(true);
		return solicitanteDropDownChoice;
	}
	
	protected IDataProvider<Pago> getChequesProvider() {
		IDataProvider<Pago> PagosDataProvider = new IDataProvider<Pago>() {
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
				List<Pago> listaPagos = ((OrdenPago)ConvertirAOPPage3.this.getDefaultModelObject()).getListadoPagos();
				List<Pago> returnList = new ArrayList<Pago>();
				for (Pago pago : listaPagos) {
					if (pago.isCheque()) {
						returnList.add(pago);
					}
				}
				Collections.sort(returnList, new Comparator<Pago>() {
	                @Override
	                public int compare(Pago pago1, Pago pago2) {
	                	if (pago1.getFecha()!=null && pago2.getFecha()!=null) {
	                		return pago1.getFecha().compareTo(pago2.getFecha());
	                	}
	                	return 1;
	                }
	            });
				return returnList;
			}
        	
        };
		return PagosDataProvider;
	}
	
	protected IDataProvider<Pago> getTransferenciasProvider() {
		IDataProvider<Pago> PagosDataProvider = new IDataProvider<Pago>() {
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
				List<Pago> listaPagos = ((OrdenPago)ConvertirAOPPage3.this.getDefaultModelObject()).getListadoPagos();
				List<Pago> returnList = new ArrayList<Pago>();
				for (Pago pago : listaPagos) {
					if (pago.isTransferencia()) {
						returnList.add(pago);
					}
				}
				Collections.sort(returnList, new Comparator<Pago>() {
	                @Override
	                public int compare(Pago pago1, Pago pago2) {
	                    if (pago1.getFecha()!=null && pago2.getFecha()!=null) {
	                    	return pago1.getFecha().compareTo(pago2.getFecha());
	                	}
	                	return 1;
	                }
	            });
				return returnList;
			}
        	
        };
		return PagosDataProvider;
	}
	
	/**
	 * Elimino los pagos que fueron eliminados desde el boton eliminar de la grilla. 
	 * Solo tomo los que tienen importe mayor a cero
	 * Ademas Agrego los Pagos de Efectivo, Transf, y Transf a 3ros
	 * @param ordenPago
	 */
	protected List<Pago> obtenerListadoPagosPreparado(OrdenPago ordenPago) {
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
		if (importeTransferenciaSinProv>0) {
			Pago pago = new Pago();
			pago.setModoPago(ModoPago.TRANSFERENCIA_SIN_PROV);
			pago.setImporte(importeTransferenciaSinProv);
			pago.setCuentaBancaria(cuentaBancaria);
			pago.setCuitCuil(cuitCuil);
			pago.setOrdenPago(ordenPago);
			nuevoListadoPagos.add(pago);
		}
		
		return nuevoListadoPagos;
	}
}