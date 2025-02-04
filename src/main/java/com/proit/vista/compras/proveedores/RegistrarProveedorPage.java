package com.proit.vista.compras.proveedores;

import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.StringValidator;

import com.proit.modelo.compras.CuentaBancaria;
import com.proit.modelo.compras.Proveedor;
import com.proit.servicios.compras.CuentaBancariaService;
import com.proit.servicios.compras.ProveedorService;
import com.proit.utils.GeneralValidator;
import com.proit.vista.base.FacturarOnLineBasePage;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class RegistrarProveedorPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(RegistrarProveedorPage.class.getName());
	
	private ProveedorService proveedorService;
	
	private boolean verCobrosAlternativos = false;
	
	public RegistrarProveedorPage() {
		this(new PageParameters(),null);
	}
	
	public RegistrarProveedorPage(final PageParameters parameters, Integer proveedorId) {
		super(parameters);
		proveedorService = new ProveedorService();
		
		setearDefaultModel(proveedorId);
		
		boolean esEditar = verificarSiEsEditar(proveedorId);
		
		crearForm(esEditar);
		add(new FeedbackPanel("feedback"));
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
		
		mostrarAlertaEnPantallaSiCorresponde(parameters);
	}

	private void setearDefaultModel(Integer proveedorId) {
		Proveedor proveedor;
		if (proveedorId != null) {
			proveedor = (Proveedor) proveedorService.get(proveedorId);
		} else {
			proveedor = new Proveedor();
			proveedor.setModoSinFactura(true);
		}
		this.setDefaultModel(Model.of(proveedor));
	}

	private void crearForm(final boolean esEditar) {
		IModel<String> razonSocialModel = new PropertyModel<String>(getDefaultModel(), "razonSocial");		
		final TextField<String> razonSocialTextField = new TextField<String>("razonSocial", razonSocialModel);
		razonSocialTextField.setRequired(true);
		razonSocialTextField.add(StringValidator.maximumLength(255));
		
		IModel<String> cuitCuilModel = new PropertyModel<String>(getDefaultModel(), "cuitCuil");
		final TextField<String> cuitCuilTextField = new TextField<String>("cuitCuil", cuitCuilModel);
		cuitCuilTextField.add(StringValidator.maximumLength(13));
		
//		WebMarkupContainer tipoCuentaContainer = new WebMarkupContainer("tipoCuentaContainer");
//		DropDownChoice<TipoCuenta> tipoCuentaDropDownChoice = crearTipoCuentaDropDownChoice(esEditar);
//		tipoCuentaContainer.add(tipoCuentaDropDownChoice);
//		
//		IModel<String> nroCuentaModel = new PropertyModel<String>(getDefaultModel(), "cuentaBancaria.nroCuenta");
//		TextField<String> nroCuentaTextField = new TextField<String>("nroCuenta", nroCuentaModel);
//		
//		WebMarkupContainer bancoContainer = new WebMarkupContainer("bancoContainer");
//		DropDownChoice<Banco> bancoDropDownChoice = crearBancoDropDownChoice(esEditar);
//		bancoContainer.add(bancoDropDownChoice);
		
		IModel<String> cbuModel = new PropertyModel<String>(getDefaultModel(), "cuentaBancaria.cbu");
		TextField<String> cbuTextField = new TextField<String>("cbu", cbuModel);
		
		IModel<String> aliasModel = new PropertyModel<String>(getDefaultModel(), "cuentaBancaria.alias");
		TextField<String> aliasTextField = new TextField<String>("alias", aliasModel);
		
		IModel<Boolean> modelModoSinFactura = new PropertyModel<Boolean>(getDefaultModel(), "modoSinFactura");
		CheckBox checkBoxModoSinFactura = new CheckBox("modoSinFactura", modelModoSinFactura);
		IModel<Boolean> modelModoEfectivo = new PropertyModel<Boolean>(getDefaultModel(), "modoEfectivo");
		CheckBox checkBoxModoEfectivo = new CheckBox("modoEfectivo", modelModoEfectivo);
		IModel<Boolean> modelModoCheque = new PropertyModel<Boolean>(getDefaultModel(), "modoCheque");
		CheckBox checkBoxModoCheque = new CheckBox("modoCheque", modelModoCheque);
		IModel<Boolean> modelModoTransferencia = new PropertyModel<Boolean>(getDefaultModel(), "modoTransferencia");
		CheckBox checkBoxModoTransferencia = new CheckBox("modoTransferencia", modelModoTransferencia);
		IModel<Boolean> modelModoTarjetaCredito = new PropertyModel<Boolean>(getDefaultModel(), "modoTarjetaCredito");
		CheckBox checkBoxModoTarjetaCredito = new CheckBox("modoTarjetaCredito", modelModoTarjetaCredito);
		//checkBoxModoSinFactura.setModelObject(true);
		checkBoxModoSinFactura.setEnabled(false);
		checkBoxModoSinFactura.setVisible(false); //Me pidio Angel que no lo quiere ver para no confundirse
		
		Link<WebPage> cancelarLink = new Link<WebPage>("cancelarLink") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage(ProveedoresPage.class);
			}
		};
				
		Form<?> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit() {
				submitForm(esEditar);
			}

		};
		Button guardarButton = new Button("guardar");
		
		Button guardarYVerCobrosAlternativosButton = new Button("guardarYVerCobrosAlternativos"){
			private static final long serialVersionUID = 1L;
			public void onSubmit() {
            	verCobrosAlternativos = true;
            }
        };        
        
        //When false (default is true), all validation and form updating is bypassed and the onSubmit method of that button is called directly, and the onSubmit method of the parent form is not called
        //guardarYVerCobrosAlternativosButton.setDefaultFormProcessing(false);
        
		add(form);
		form.add(razonSocialTextField);
		form.add(cuitCuilTextField);
//		form.add(tipoCuentaContainer);
//		form.add(nroCuentaTextField);
//		form.add(bancoContainer);
		form.add(cbuTextField);
		form.add(aliasTextField);
		form.add(checkBoxModoSinFactura);
		form.add(checkBoxModoEfectivo);
		form.add(checkBoxModoCheque);
		form.add(checkBoxModoTransferencia);
		form.add(checkBoxModoTarjetaCredito);
		form.add(cancelarLink);
		form.add(guardarButton);
		form.add(guardarYVerCobrosAlternativosButton);
	}
	
	private boolean validacionOK(boolean esEditar){		
		boolean validacionOK = true;
		Proveedor proveedor = (Proveedor)RegistrarProveedorPage.this.getDefaultModelObject();
		
		boolean isModoTransferencia = proveedor.isModoTransferencia();
		String cuitCuil = proveedor.getCuitCuil();		
		String razonSocial = proveedor.getRazonSocial();
		String cbu = proveedor.getCuentaBancaria()!= null ? proveedor.getCuentaBancaria().getCbu() : null;
		String alias = proveedor.getCuentaBancaria()!= null ? proveedor.getCuentaBancaria().getAlias() : null;
		
		GeneralValidator generalValidator = new GeneralValidator();
		
		if (!esEditar && cuitCuil!=null && proveedorService.existsByCuitCuil(cuitCuil)) {
			error("Ya existe un Proveedor con el CUIT/CUIL \"" + cuitCuil + "\".");			
			validacionOK = false;
		}
		if (!esEditar && proveedorService.existsByRazonSocial(razonSocial)) {
			error("Ya existe un Proveedor con la razón social \"" + razonSocial + "\".");
			validacionOK = false;
		}
		
		if ( cuitCuil!=null &&  ! generalValidator.cuitCuilEsValido(cuitCuil) ) {
			error("CUIT/CUIL ingresado no es válido."); 
			validacionOK = false;
		}
		if ( cbu!=null && ! cbu.isEmpty() && ! generalValidator.cbuEsValido(cbu) ) {
			error("CBU ingresado no es válido."); 
			validacionOK = false;
		}
				
		if (isModoTransferencia && (cbu==null||cbu.isEmpty()) && (alias==null||alias.isEmpty()) ){
			error("Al haber seleccionado Modo de Pago Transferencia, debe ingresar el CBU y/o el Alias.");
			validacionOK = false;
		}
		
		//Si no selecciona al menos un Modo de Pago
		if ( !proveedor.isModoEfectivo() && !proveedor.isModoCheque() && !proveedor.isModoTransferencia() &&  !proveedor.isModoTarjetaCredito()) {
			error("Debe seleccionar al menos un Modo de Pago.");
			validacionOK = false;
		}
		
		return validacionOK;
	}
	
	private void submitForm(boolean esEditar) {
		if ( ! validacionOK(esEditar) ) {
			return;
		}
		Proveedor proveedor = (Proveedor)RegistrarProveedorPage.this.getDefaultModelObject();
		String textoPorPantalla = "El proveedor '" + proveedor.getRazonSocial() + "' ha sido " + (esEditar ? "editado." : "creado.");
		String resultado = "OK";
		
		try {
			CuentaBancariaService cuentaBancariaService = new CuentaBancariaService();
			if ( ! esEditar ) {
				if (proveedor.getCuentaBancaria() != null && 
						(proveedor.getCuentaBancaria().getCbu() != null || proveedor.getCuentaBancaria().getAlias() != null)) { //unicos campos requeridos. Creo la cuenta bancaria si hay cbu y/o alias cargado, sino no
					int cuentaBancariaId = (Integer) cuentaBancariaService.createOrUpdate(proveedor.getCuentaBancaria());
					proveedor.getCuentaBancaria().setId(cuentaBancariaId);
				} else {
					proveedor.setCuentaBancaria(null);
				}
			} else {
				//Si no tiene CBU, no tiene cuenta bancaria
				if (proveedor.getCuentaBancaria() != null &&
						(proveedor.getCuentaBancaria().getCbu() != null || proveedor.getCuentaBancaria().getAlias() != null)) { //unicos campos requeridos. Creo la cuenta bancaria si hay cbu y/o alias cargado, sino no
					boolean crearNuevaCuentaBancaria = proveedor.getCuentaBancaria().getId()==0;
					if (crearNuevaCuentaBancaria) {
						int cuentaBancariaId = (Integer) cuentaBancariaService.createOrUpdate(proveedor.getCuentaBancaria());
						proveedor.getCuentaBancaria().setId(cuentaBancariaId);
					} else {
						CuentaBancaria cuentaBancaria = (CuentaBancaria) cuentaBancariaService.createOrUpdate(proveedor.getCuentaBancaria());
						proveedor.setCuentaBancaria(cuentaBancaria);
					}
					
				} else { 
					proveedor.setCuentaBancaria(null);
				}
				
			}
			proveedorService.createOrUpdate(proveedor);
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			textoPorPantalla = "El proveedor '" + proveedor.getRazonSocial() + "' no pudo ser " + (esEditar ? "editado " : "creado ") + "correctamente. Por favor, vuelva a intentarlo.";
			resultado = "ERROR";
		}
		
		if (!verCobrosAlternativos) {
			PageParameters pageParameters = new PageParameters();
			pageParameters.add("Resultado", resultado);
			pageParameters.add("TextoPantalla", textoPorPantalla);
			setResponsePage(ProveedoresPage.class, pageParameters);
		} else {
			PageParameters pageParameters = new PageParameters();
			pageParameters.add("Resultado", resultado);
			pageParameters.add("TextoPantalla", textoPorPantalla);
			pageParameters.add("idProv", proveedor.getId());
			pageParameters.add("provName", proveedor.getRazonSocial());
			setResponsePage(CobrosAlternativosPage.class, pageParameters);
		}
	}

//	private DropDownChoice<TipoCuenta> crearTipoCuentaDropDownChoice(boolean esEditar) {
//		TipoCuentaService tipoCuentaService = new TipoCuentaService();
//		List<TipoCuenta> tiposCuenta = tipoCuentaService.getAll();
//		IModel<TipoCuenta> tipoCuentaModel = new PropertyModel<TipoCuenta>(getDefaultModel(), "cuentaBancaria.tipoCuenta");
//		
//		if ( esEditar &&  ((Proveedor)RegistrarProveedorPage.this.getDefaultModelObject()).getCuentaBancaria() != null) {
//			TipoCuenta tipoCuentaSeleccionada = ((Proveedor)RegistrarProveedorPage.this.getDefaultModelObject()).getCuentaBancaria().getTipoCuenta();
//			tipoCuentaModel.setObject(tipoCuentaSeleccionada);
//		}
//				
//		DropDownChoice<TipoCuenta> tipoCuentaDropDownChoice = new DropDownChoice<TipoCuenta>("tipoCuenta", tipoCuentaModel, tiposCuenta, new ChoiceRenderer<TipoCuenta>("nombre"));
//		tipoCuentaDropDownChoice.setNullValid(true);
//		return tipoCuentaDropDownChoice;
//	}
//	
//	private DropDownChoice<Banco> crearBancoDropDownChoice(boolean esEditar) {
//		BancoService bancoService = new BancoService();
//		List<Banco> bancos = bancoService.getBancos();
//		IModel<Banco> bancoModel = new PropertyModel<Banco>(getDefaultModel(), "cuentaBancaria.banco");
//		
//		if ( esEditar &&  ((Proveedor)RegistrarProveedorPage.this.getDefaultModelObject()).getCuentaBancaria() != null) {
//			Banco bancoSeleccionado = ((Proveedor)RegistrarProveedorPage.this.getDefaultModelObject()).getCuentaBancaria().getBanco();
//			bancoModel.setObject(bancoSeleccionado);
//		}
//				
//		DropDownChoice<Banco> bancoDropDownChoice = new DropDownChoice<Banco>("banco", bancoModel, bancos, new ChoiceRenderer<Banco>("nombre"));
//		bancoDropDownChoice.setNullValid(true);
//		return bancoDropDownChoice;
//	}

}
