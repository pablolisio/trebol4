package com.proit.vista.compras.ordenes.spysf;

import java.util.ArrayList;
import java.util.Calendar;
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
import com.proit.modelo.compras.CuentaBancaria;
import com.proit.modelo.compras.OrdenPago;
import com.proit.modelo.compras.Pago;
import com.proit.servicios.BancoService;
import com.proit.servicios.compras.OrdenPagoService;
import com.proit.utils.Constantes;
import com.proit.utils.GeneralValidator;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.CustomTextFieldDouble;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class RegistrarOrdenPagoSPySFPage2 extends FacturarOnLineBasePage {

	private static final long serialVersionUID = 1L;

	private double importeEfectivo;
	private double importeTransferenciaSinProv;
	private CuentaBancaria cuentaBancaria;
	private String cuitCuil;

	public RegistrarOrdenPagoSPySFPage2() {
		setearDefaultModel();

		cuentaBancaria = new CuentaBancaria();
		
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
		
		this.setDefaultModel(Model.of(ordenPago));
	}

	private void crearForm() {
		FeedbackPanel feedback = new FeedbackPanel("feedback");

		WebMarkupContainer seccionEfectivo = new WebMarkupContainer("seccionEfectivo");
		WebMarkupContainer seccionCheques = new WebMarkupContainer("seccionCheques");
		WebMarkupContainer seccionTransferenciaSinProv = new WebMarkupContainer("seccionTransferenciaSinProv");

		final CustomTextFieldDouble importeEfectivoInput = new CustomTextFieldDouble("importeEfectivo", new PropertyModel<Double>(this, "importeEfectivo"));
		importeEfectivoInput.setRequired(true);
		importeEfectivoInput.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(importeEfectivoInput);
			}
		});
		
		final CustomTextFieldDouble importeTransferenciaSinProvInput = new CustomTextFieldDouble("importeTransferenciaSinProv", new PropertyModel<Double>(this, "importeTransferenciaSinProv"));
		importeTransferenciaSinProvInput.setRequired(true);
		importeTransferenciaSinProvInput.add(new AjaxFormComponentUpdatingBehavior("change") { //Antes se usaba 'onchange'
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(importeTransferenciaSinProvInput);
			}
		});
		
		IModel<String> cuitCuilModel = new PropertyModel<String>(this, "cuitCuil");
		final TextField<String> cuitCuilTextField = new TextField<String>("cuitCuil", cuitCuilModel);
		cuitCuilTextField.add(StringValidator.maximumLength(13));
		
		IModel<String> cbuModel = new PropertyModel<String>(this, "cuentaBancaria.cbu");
		final TextField<String> cbuTextField = new TextField<String>("cbu", cbuModel);
		cbuTextField.add(StringValidator.maximumLength(22));
		
		final AjaxDatePicker datePickerTransferencia = crearDatePickerTransferencia();

		final Locale locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
		final double importeTotal = (Double) ((FacturarOnLineAuthenticatedWebSession) getSession()).getAttribute("total");
		Label importeLbl = new Label("importe", "$ " + Utils.round2Decimals(importeTotal, locale) );
		
		Form<?> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onValidate() {
				super.onValidate();
				boolean validacionOK = true;

				OrdenPagoService ordenPagoService = new OrdenPagoService();
				OrdenPago ordenPago = (OrdenPago)RegistrarOrdenPagoSPySFPage2.this.getDefaultModelObject();
				List<Pago> listadoPagos = obtenerListadoPagosPreparado(ordenPago);

				double totalPagos = ordenPagoService.calculateTotalPagos(listadoPagos);				

				//Se valida que el total de pagos debe ser igual al importe total
				if ( ! Utils.round2Decimals(totalPagos, locale).equals(Utils.round2Decimals(importeTotal, locale)) ){
					String errorString = "El Total de Pagos debe ser igual al Importe Total indicado en el paso anterior. "
							+ "Importe Total: $"+ Utils.round2Decimals(importeTotal, locale) + ". "
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
				//El importe de los cheques no hace falta validarlos porque ya vienen validados/corregidos
				
				GeneralValidator generalValidator = new GeneralValidator();
				if (importeTransferenciaSinProv!=0 && cuitCuilTextField.getConvertedInput()==null){
					validacionOK = informarError("Falta ingresar CUIT/CUIL.");
				} else if ( importeTransferenciaSinProv!=0 && ! generalValidator.cuitCuilEsValido(cuitCuilTextField.getConvertedInput()) ) {
					validacionOK = informarError("CUIT/CUIL ingresado no es válido.");
				}
				//if (importeTransferenciaSinProv>0 && tipoCuentaDropDownChoice.getConvertedInput()==null){
				//	validacionOK = informarError("El Importe de Transferencia es mayor a Cero, pero no seleccionó ningun Tipo Cuenta.");
				//}
				//if (importeTransferenciaSinProv>0 && nroCuentaTextField.getConvertedInput()==null){
				//	validacionOK = informarError("El Importe de Transferencia es mayor a Cero, pero no ingresó ningun Nro Cuenta.");
				//}
				//if (importeTransferenciaSinProv>0 && bancoDropDownChoice.getConvertedInput()==null){
				//	validacionOK = informarError("El Importe de Transferencia es mayor a Cero, pero no seleccionó ningun Banco.");
				//}
				if (importeTransferenciaSinProv!=0 && cbuTextField.getConvertedInput()==null){
					validacionOK = informarError("Falta ingresar CBU.");
				} else if (importeTransferenciaSinProv!=0 && ! generalValidator.cbuEsValido(cbuTextField.getConvertedInput())) {
					validacionOK = informarError("CBU ingresado no es válido.");
				}
				//TODO modificar en todos lados y en cw e impec usando "datePickerTransferencia.getConvertedInput()==null" en vez de "datePickerTransferencia.getModelObject()==null", ya que al seleccionar una fecha del calendario y desp borrarlo manualmente con backspace, "datePickerTransferencia.getModelObject()" no es null
				if (importeTransferenciaSinProv!=0 && datePickerTransferencia.getConvertedInput()==null){ 
					validacionOK = informarError("Falta ingresar Fecha de transferencia.");
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
				OrdenPago ordenPago = (OrdenPago)RegistrarOrdenPagoSPySFPage2.this.getDefaultModelObject();
				ordenPago.setListadoPagos(obtenerListadoPagosPreparado(ordenPago));

				((FacturarOnLineAuthenticatedWebSession) getSession()).setAttribute("op", ordenPago);
				//if (importeTransferenciaSinProv>0) { //Ahora uso "!=" ya que se pueden cargar OPs spysf con importes en negativo
				if (importeTransferenciaSinProv!=0) {
					((FacturarOnLineAuthenticatedWebSession) getSession()).setAttribute("cta", cuentaBancaria);
				} else {
					((FacturarOnLineAuthenticatedWebSession) getSession()).setAttribute("cta", null);
				}

				setResponsePage(RegistrarOrdenPagoSPySFPage3.class, new PageParameters());
			}

			/**
			 * Elimino los pagos que fueron eliminados desde el boton eliminar de la grilla. 
			 * Solo tomo los que tienen importe mayor a cero
			 * Ademas Agrego los Pagos de Efectivo, Transf, y Transf a 3ros
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
				//if (importeEfectivo>0) { //Ahora uso "!=" ya que se pueden cargar OPs spysf con importes en negativo
				if (importeEfectivo!=0) {
					Pago pago = new Pago();
					pago.setModoPago(ModoPago.EFECTIVO);
					pago.setImporte(importeEfectivo);
					pago.setOrdenPago(ordenPago);
					nuevoListadoPagos.add(pago);
				}
				//if (importeTransferenciaSinProv>0) { //Ahora uso "!=" ya que se pueden cargar OPs spysf con importes en negativo
				if (importeTransferenciaSinProv!=0) {
					Pago pago = new Pago();
					pago.setModoPago(ModoPago.TRANSFERENCIA_SIN_PROV);
					pago.setImporte(importeTransferenciaSinProv);
					pago.setCuentaBancaria(cuentaBancaria);
					pago.setCuitCuil(cuitCuil);
					if (datePickerTransferencia.getModelObject()!=null) {
						Calendar fecha = Calendar.getInstance();
						fecha.setTime(datePickerTransferencia.getModelObject());
						pago.setFecha(fecha);
					}
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
				OrdenPago ordenPago = (OrdenPago)RegistrarOrdenPagoSPySFPage2.this.getDefaultModelObject();
				Pago pago = new Pago();
				pago.setModoPago(ModoPago.CHEQUE);
				pago.setOrdenPago(ordenPago);
				((OrdenPago)RegistrarOrdenPagoSPySFPage2.this.getDefaultModelObject()).getListadoPagos().add(pago);
				target.add(chequesContainer);
			}
		});

		this.add(form);
		form.add(feedback.setOutputMarkupId(true));
		form.add(importeLbl);
		
		seccionEfectivo.add(importeEfectivoInput);
		addChequesList(chequesContainer);
		seccionCheques.add(chequesContainer);
		seccionTransferenciaSinProv.add(importeTransferenciaSinProvInput);
		seccionTransferenciaSinProv.add(cuitCuilTextField);
		seccionTransferenciaSinProv.add(cbuTextField);
		seccionTransferenciaSinProv.add(datePickerTransferencia);

		form.add(seccionEfectivo);
		form.add(seccionCheques);
		form.add(seccionTransferenciaSinProv);
	}

	private void addChequesList(final WebMarkupContainer chequesContainer) {
		IDataProvider<Pago> pagosDataProvider = getPagosProvider();

		DataView<Pago> dataView = new DataView<Pago>("listaCheques", pagosDataProvider) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<Pago> item) {
				DropDownChoice<Banco> bancoDropDownChoice = crearBancoDropDownChoiceCheque(item);

				IModel<String> nroModel = new PropertyModel<String>(item.getModelObject(), "nroCheque");
				TextField<String> nroChequeTextField = new TextField<String>("nroCheque", nroModel);
				nroChequeTextField.add(StringValidator.maximumLength(255));

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

	private DropDownChoice<Banco> crearBancoDropDownChoiceCheque(Item<Pago> item) {
		BancoService bancoService = new BancoService();
		List<Banco> bancos = bancoService.getBancos();

		IModel<Banco> bancoModel =  new PropertyModel<Banco>(item.getModelObject(), "banco");
		bancoModel.setObject(bancoService.getBancoActual(bancos));
		DropDownChoice<Banco> bancoDropDownChoice = new DropDownChoice<Banco>("bancoCheque", bancoModel, bancos, new ChoiceRenderer<Banco>("nombre"));
//		bancoDropDownChoice.setRequired(true);

		bancoDropDownChoice.setNullValid(true);
		return bancoDropDownChoice;
	}

	private AjaxDatePicker crearDatePickerCheque(Item<Pago> item) {
		IModel<Date> fechaModel = new PropertyModel<Date>(item.getModelObject(), "fechaAsDate");
		AjaxDatePicker ajaxDatePicker = new AjaxDatePicker("datepickerCheque", fechaModel, "dd/MM/yyyy", new Options());
		//ajaxDatePicker.setRequired(true);
		return ajaxDatePicker;
	}
	
	private AjaxDatePicker crearDatePickerTransferencia() {
		AjaxDatePicker ajaxDatePicker = new AjaxDatePicker("datepickerTransferencia", new Model<Date>(), "dd/MM/yyyy", new Options());
		//ajaxDatePicker.setRequired(true);
		return ajaxDatePicker;
	}

	private IDataProvider<Pago> getPagosProvider() {
		IDataProvider<Pago> PagosDataProvider = new IDataProvider<Pago>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void detach() {
			}

			@Override
			public Iterator<Pago> iterator(long first, long count) {
				return ((OrdenPago)RegistrarOrdenPagoSPySFPage2.this.getDefaultModelObject()).getListadoPagos().iterator();
			}

			@Override
			public long size() {
				return ((OrdenPago)RegistrarOrdenPagoSPySFPage2.this.getDefaultModelObject()).getListadoPagos().size();
			}

			@Override
			public IModel<Pago> model(Pago Pago) {
				return new Model<Pago>(Pago);
			}

		};
		return PagosDataProvider;
	}

}