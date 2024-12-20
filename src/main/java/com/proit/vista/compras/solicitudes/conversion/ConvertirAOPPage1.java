package com.proit.vista.compras.solicitudes.conversion;

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

import com.proit.modelo.compras.CuentaBancaria;
import com.proit.modelo.compras.PagoSolicitudPago;
import com.proit.modelo.compras.Proveedor;
import com.proit.modelo.compras.SolicitudPago;
import com.proit.servicios.compras.CuentaBancariaService;
import com.proit.servicios.compras.ProveedorService;
import com.proit.servicios.compras.SolicitudPagoService;
import com.proit.utils.GeneralValidator;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.vista.compras.solicitudes.SolicitudesPagoPage;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class ConvertirAOPPage1 extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(ConvertirAOPPage1.class.getName());
	
	private ProveedorService proveedorService;
	
	private boolean guardarYContinuar = false;
	
	private SolicitudPago solicitudPago;
	
	public ConvertirAOPPage1(SolicitudPago solicitudPago) {
		super();
		proveedorService = new ProveedorService();
		
		this.solicitudPago = solicitudPago;
		
		setearDefaultModel();
		
		crearForm();
		add(new FeedbackPanel("feedback"));
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
	}
	
	private void setearDefaultModel() {
		Proveedor proveedor = new Proveedor();
		proveedor.setModoSinFactura(true);
		proveedor.setRazonSocial(solicitudPago.getRazonSocial());
		proveedor.setCuitCuil(solicitudPago.getCuitCuil());
		
		if (solicitudPago.getCbu()!=null) {
			CuentaBancaria cuentaBancaria = new CuentaBancaria();
			cuentaBancaria.setBorrado(false);
			cuentaBancaria.setCbu(solicitudPago.getCbu());
			proveedor.setCuentaBancaria(cuentaBancaria);
		}
		
		proveedor.setModoSinFactura(true);
		for (PagoSolicitudPago pago : solicitudPago.getListadoPagos()) {
			if (pago.isEfectivo()) {
				proveedor.setModoEfectivo(true);
			}
			if (pago.isCheque()) {
				proveedor.setModoCheque(true);
			}
			if (pago.isTransferencia()) {
				proveedor.setModoTransferencia(true);
			}
			if (pago.isTarjetaCredito()) {
				proveedor.setModoTarjetaCredito(true);
			}
		}
		
		this.setDefaultModel(Model.of(proveedor));
	}
	
	private void crearForm() {
		IModel<String> razonSocialModel = new PropertyModel<String>(getDefaultModel(), "razonSocial");
		final TextField<String> razonSocialTextField = new TextField<String>("razonSocial", razonSocialModel);
		razonSocialTextField.setRequired(true);
		
		IModel<String> cuitCuilModel = new PropertyModel<String>(getDefaultModel(), "cuitCuil");
		final TextField<String> cuitCuilTextField = new TextField<String>("cuitCuil", cuitCuilModel);
		
		IModel<String> cbuModel = new PropertyModel<String>(getDefaultModel(), "cuentaBancaria.cbu");
		TextField<String> cbuTextField = new TextField<String>("cbu", cbuModel);
		
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
				setResponsePage(SolicitudesPagoPage.class);
			}
		};
				
		Form<?> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit() {
				submitForm();
			}

		};
		Button guardarButton = new Button("guardar");
		
		Button guardarYContinuarButton = new Button("guardarYContinuar"){
			private static final long serialVersionUID = 1L;
			public void onSubmit() {
            	guardarYContinuar = true;
            }
        };        
        
        //When false (default is true), all validation and form updating is bypassed and the onSubmit method of that button is called directly, and the onSubmit method of the parent form is not called
        //guardarYVerCobrosAlternativosButton.setDefaultFormProcessing(false);
        
		add(form);
		form.add(razonSocialTextField);
		form.add(cuitCuilTextField);
		form.add(cbuTextField);
		form.add(checkBoxModoSinFactura);
		form.add(checkBoxModoEfectivo);
		form.add(checkBoxModoCheque);
		form.add(checkBoxModoTransferencia);
		form.add(checkBoxModoTarjetaCredito);
		form.add(cancelarLink);
		form.add(guardarButton);
		form.add(guardarYContinuarButton);
	}
	
	private boolean validacionOK() {
		boolean validacionOK = true;
		Proveedor proveedor = (Proveedor)ConvertirAOPPage1.this.getDefaultModelObject();
		
		boolean isModoTransferencia = proveedor.isModoTransferencia();
		String cuitCuil = proveedor.getCuitCuil();		
		String razonSocial = proveedor.getRazonSocial();
		String cbu = proveedor.getCuentaBancaria()!= null ? proveedor.getCuentaBancaria().getCbu() : null;
		
		GeneralValidator generalValidator = new GeneralValidator();
		
		if (cuitCuil!=null && proveedorService.existsByCuitCuil(cuitCuil)) {
			error("Ya existe un Proveedor con el CUIT/CUIL \"" + cuitCuil + "\".");			
			validacionOK = false;
		}
		if (proveedorService.existsByRazonSocial(razonSocial)) {
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
				
		if (isModoTransferencia && (cbu==null||cbu.isEmpty()) ){
			error("Al haber seleccionado Modo de Pago Transferencia, debe ingresar como mínimo el CBU.");
			validacionOK = false;
		}
		
		//Si no selecciona al menos un Modo de Pago
		if ( !proveedor.isModoEfectivo() && !proveedor.isModoCheque() && !proveedor.isModoTransferencia() &&  !proveedor.isModoTarjetaCredito()) {
			error("Debe seleccionar al menos un Modo de Pago.");
			validacionOK = false;
		}
		
		return validacionOK;
	}
	
	private void submitForm() {
		if ( ! validacionOK() ) {
			return;
		}
		Proveedor proveedor = (Proveedor)ConvertirAOPPage1.this.getDefaultModelObject();
		String textoPorPantalla = "El proveedor '" + proveedor.getRazonSocial() + "' ha sido creado.";
		String resultado = "OK";
		
		try {
			CuentaBancariaService cuentaBancariaService = new CuentaBancariaService();
			if (proveedor.getCuentaBancaria() != null && proveedor.getCuentaBancaria().getCbu() != null) { //unico campo requerido. Creo la cuenta bancaria si hay cbu cargado, sino no
				int cuentaBancariaId = (Integer) cuentaBancariaService.createOrUpdate(proveedor.getCuentaBancaria());
				proveedor.getCuentaBancaria().setId(cuentaBancariaId);
			} else {
				proveedor.setCuentaBancaria(null);
			}
			int proveedorNuevoId = (Integer) proveedorService.createOrUpdate(proveedor);
			proveedor.setId(proveedorNuevoId);
			
			SolicitudPagoService solicitudPagoService = new SolicitudPagoService();
			solicitudPagoService.createOrUpdateSolicitudConProveedor(solicitudPago, proveedor);
			solicitudPagoService.actualizarProveedorEnRestoSolicitudes(proveedor, solicitudPago.getNro());
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			textoPorPantalla = "El proveedor '" + proveedor.getRazonSocial() + "' no pudo ser creado correctamente. Por favor, vuelva a intentarlo.";
			resultado = "ERROR";
		}
		PageParameters pageParameters = new PageParameters();
		pageParameters.add("Resultado", resultado);
		pageParameters.add("TextoPantalla", textoPorPantalla);
		if (!guardarYContinuar) {			
			setResponsePage(SolicitudesPagoPage.class, pageParameters);
		} else {
			if (solicitudPago.isConFactura()) {
				setResponsePage(new ConvertirAOPPage2(pageParameters, solicitudPago));
			} else {
				if (solicitudPago.isCPySF()) {
					setResponsePage(new ConvertirAOPPage3CPySF(solicitudPago));
				} else {
					setResponsePage(new ConvertirAOPPage3Normal(solicitudPago));
				}
			}
		}
	}

}
