package com.proit.vista.compras.proveedores;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.StringValidator;

import com.proit.modelo.Banco;
import com.proit.modelo.TipoCuenta;
import com.proit.modelo.compras.CobroAlternativo;
import com.proit.modelo.compras.CuentaBancaria;
import com.proit.modelo.compras.Proveedor;
import com.proit.servicios.BancoService;
import com.proit.servicios.TipoCuentaService;
import com.proit.servicios.compras.CobroAlternativoService;
import com.proit.servicios.compras.CuentaBancariaService;
import com.proit.servicios.compras.ProveedorService;
import com.proit.utils.GeneralValidator;
import com.proit.vista.base.FacturarOnLineBasePage;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class RegistrarCobroAlternativoPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(RegistrarCobroAlternativoPage.class.getName());
	
	private Proveedor proveedor;
	private CobroAlternativoService cobroAlternativoService;
	private ProveedorService proveedorService;
	
	public RegistrarCobroAlternativoPage(Integer idProveedor) {		
		this(new PageParameters(),null,idProveedor);
	}
	
	public RegistrarCobroAlternativoPage(final PageParameters parameters, Integer cobroAlternativoId, Integer idProveedor) {
		super(parameters);
		proveedorService = new ProveedorService();
		cobroAlternativoService = new CobroAlternativoService();
		
		proveedor = (Proveedor) proveedorService.get(idProveedor);
				
		setearDefaultModel(cobroAlternativoId);
		
		boolean esEditar = verificarSiEsEditar(cobroAlternativoId);
		
		crearForm(esEditar);
		add(new FeedbackPanel("feedback"));
		
		String title = "Cobros Alternativos para '" + proveedor.getRazonSocial() + "'"; 
		add(new Label("title", title));
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
		
		mostrarAlertaEnPantallaSiCorresponde(parameters);
	}

	private void setearDefaultModel(Integer cobroAlternativoId) {
		CobroAlternativo cobroAlternativo = new CobroAlternativo();
		if (cobroAlternativoId != null) {
			cobroAlternativo = (CobroAlternativo) cobroAlternativoService.get(cobroAlternativoId);
		} 
		this.setDefaultModel(Model.of(cobroAlternativo));
	}

	private void crearForm(final boolean esEditar) {
		IModel<String> titularModel = new PropertyModel<String>(getDefaultModel(), "titular");		
		final TextField<String> titularTextField = new TextField<String>("titular", titularModel);
		titularTextField.setRequired(true);
		titularTextField.add(StringValidator.maximumLength(255));
		
		IModel<String> cuitCuilModel = new PropertyModel<String>(getDefaultModel(), "cuitCuil");
		final TextField<String> cuitCuilTextField = new TextField<String>("cuitCuil", cuitCuilModel);
		cuitCuilTextField.setRequired(true);
		cuitCuilTextField.add(StringValidator.maximumLength(13));
		
		WebMarkupContainer tipoCuentaContainer = new WebMarkupContainer("tipoCuentaContainer");
		DropDownChoice<TipoCuenta> tipoCuentaDropDownChoice = crearTipoCuentaDropDownChoice(esEditar);
		tipoCuentaContainer.add(tipoCuentaDropDownChoice);
		
		IModel<String> nroCuentaModel = new PropertyModel<String>(getDefaultModel(), "cuentaBancaria.nroCuenta");
		TextField<String> nroCuentaTextField = new TextField<String>("nroCuenta", nroCuentaModel);
		
		WebMarkupContainer bancoContainer = new WebMarkupContainer("bancoContainer");
		DropDownChoice<Banco> bancoDropDownChoice = crearBancoDropDownChoice(esEditar);
		bancoContainer.add(bancoDropDownChoice);
		
		IModel<String> cbuModel = new PropertyModel<String>(getDefaultModel(), "cuentaBancaria.cbu");
		final TextField<String> cbuTextField = new TextField<String>("cbu", cbuModel);
		cbuTextField.setRequired(true);
		
		Link<WebPage> cancelarLink = new Link<WebPage>("cancelarLink") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				PageParameters pageParameters = new PageParameters();
				pageParameters.add("idProv", proveedor.getId());
				pageParameters.add("provName", proveedor.getRazonSocial());
				setResponsePage(CobrosAlternativosPage.class, pageParameters);
			}
		};
				
		Form<?> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onValidate(){
				boolean validacionOK = true;
				String cuitCuil = cuitCuilTextField.getConvertedInput();
				String cbu = cbuTextField.getConvertedInput();
				
				GeneralValidator generalValidator = new GeneralValidator();
				
				if ( ! generalValidator.cuitCuilEsValido(cuitCuil) ) {
					error("CUIT/CUIL ingresado no es válido."); 
					validacionOK = false;
				}
				if ( ! generalValidator.cbuEsValido(cbu) ) {
					error("CBU ingresado no es válido."); 
					validacionOK = false;
				}
				
				if ( ! validacionOK ) {
					onError();
					return;
				}
				
			}
			
			@Override
			protected void onSubmit() {
				CobroAlternativo cobroAlternativo = (CobroAlternativo)RegistrarCobroAlternativoPage.this.getDefaultModelObject();
				String textoPorPantalla = "El cobro alternativo '" + cobroAlternativo.getTitular() + "' ha sido " + (esEditar ? "editado" : "creado");
				textoPorPantalla += " para el proveedor '"+proveedor.getRazonSocial()+"'";
				String resultado = "OK";
				
				try {
					CuentaBancariaService cuentaBancariaService = new CuentaBancariaService();
					if ( ! esEditar ) {
						int cuentaBancariaId = (Integer) cuentaBancariaService.createOrUpdate(cobroAlternativo.getCuentaBancaria());
						cobroAlternativo.getCuentaBancaria().setId(cuentaBancariaId);
					} else {
						CuentaBancaria cuentaBancaria = (CuentaBancaria) cuentaBancariaService.createOrUpdate(cobroAlternativo.getCuentaBancaria());
						cobroAlternativo.setCuentaBancaria(cuentaBancaria);
					}
					cobroAlternativo.setProveedor(proveedor);
					cobroAlternativoService.createOrUpdate(cobroAlternativo);
				} catch(Exception e) {
					log.error(e.getMessage(), e);
					textoPorPantalla = "El cobro alternativo '" + cobroAlternativo.getTitular() + "' no pudo ser " + (esEditar ? "editado " : "creado ") + "correctamente. Por favor, vuelva a intentarlo.";
					resultado = "ERROR";
				}
				
				PageParameters pageParameters = new PageParameters();
				pageParameters.add("Resultado", resultado);
				pageParameters.add("TextoPantalla", textoPorPantalla);
				pageParameters.add("idProv", proveedor.getId());
				pageParameters.add("provName", proveedor.getRazonSocial());
				setResponsePage(CobrosAlternativosPage.class, pageParameters);				
			}

		};
		
		add(form);
		form.add(titularTextField);
		form.add(cuitCuilTextField);
		form.add(tipoCuentaContainer);
		form.add(nroCuentaTextField);
		form.add(bancoContainer);
		form.add(cbuTextField);
		form.add(cancelarLink);
	}
	
	private DropDownChoice<TipoCuenta> crearTipoCuentaDropDownChoice(boolean esEditar) {
		TipoCuentaService tipoCuentaService = new TipoCuentaService();
		List<TipoCuenta> tiposCuenta = tipoCuentaService.getAll();
		
		TipoCuenta tipoCuentaSeleccionada = null;
		if ( esEditar ) {
			tipoCuentaSeleccionada = ((CobroAlternativo)RegistrarCobroAlternativoPage.this.getDefaultModelObject()).getCuentaBancaria().getTipoCuenta();
		}
		
		IModel<TipoCuenta> tipoCuentaModel = new PropertyModel<TipoCuenta>(getDefaultModel(), "cuentaBancaria.tipoCuenta");
		tipoCuentaModel.setObject(tipoCuentaSeleccionada);
		DropDownChoice<TipoCuenta> tipoCuentaDropDownChoice = new DropDownChoice<TipoCuenta>("tipoCuenta", tipoCuentaModel, tiposCuenta, new ChoiceRenderer<TipoCuenta>("nombre"));
		
		tipoCuentaDropDownChoice.setNullValid(true);
		return tipoCuentaDropDownChoice;
	}
	
	private DropDownChoice<Banco> crearBancoDropDownChoice(boolean esEditar) {
		BancoService bancoService = new BancoService();
		List<Banco> bancos = bancoService.getBancos();
		
		Banco bancoSeleccionado = null;
		if ( esEditar ) {
			bancoSeleccionado = ((CobroAlternativo)RegistrarCobroAlternativoPage.this.getDefaultModelObject()).getCuentaBancaria().getBanco();
		}
		
		IModel<Banco> bancoModel = new PropertyModel<Banco>(getDefaultModel(), "cuentaBancaria.banco");
		bancoModel.setObject(bancoSeleccionado);
		DropDownChoice<Banco> bancoDropDownChoice = new DropDownChoice<Banco>("banco", bancoModel, bancos, new ChoiceRenderer<Banco>("nombre"));
		
		bancoDropDownChoice.setNullValid(true);
		return bancoDropDownChoice;
	}

}
