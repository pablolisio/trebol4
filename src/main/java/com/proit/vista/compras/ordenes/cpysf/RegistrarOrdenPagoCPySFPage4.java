package com.proit.vista.compras.ordenes.cpysf;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.proit.modelo.TipoFactura;
import com.proit.modelo.compras.EstadoFacturaCompra;
import com.proit.modelo.compras.FacturaCompra;
import com.proit.modelo.compras.OrdenPago;
import com.proit.modelo.compras.Pago;
import com.proit.modelo.compras.Proveedor;
import com.proit.servicios.compras.OrdenPagoService;
import com.proit.servicios.compras.ProveedorService;
import com.proit.utils.Constantes;
import com.proit.utils.ExcelGeneratorOP;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.vista.compras.ordenes.OrdenesPagoPage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class RegistrarOrdenPagoCPySFPage4 extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(RegistrarOrdenPagoCPySFPage4.class.getName());
	
	private OrdenPagoService ordenPagoService;
	private OrdenPago ordenPago;
	
	private Proveedor proveedor;
	
	private Locale locale;
	
	public RegistrarOrdenPagoCPySFPage4() {

		locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
		
		ordenPagoService = new OrdenPagoService();
		ProveedorService proveedorService = new ProveedorService();
		
		String razonSocial = (String) ((FacturarOnLineAuthenticatedWebSession) getSession()).getAttribute("rz");
		proveedor = proveedorService.getByRazonSocial(razonSocial);
		
		setearOrdenPago();
		
		crearForm();
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
	}

	private void setearOrdenPago() {		//Este metodo tambien aparece en ConvertirAOPPage3CPySF
		ordenPago = (OrdenPago) ((FacturarOnLineAuthenticatedWebSession) getSession()).getAttribute("op");
		double total = (Double) ((FacturarOnLineAuthenticatedWebSession) getSession()).getAttribute("total");
		double subtotal = Utils.getSubtotalFromTotal(total, TipoFactura.TIPO_A); //Siempre usa esto como Factura A (*)
		double iva = Utils.getIvaFromTotal(total, TipoFactura.TIPO_A);
		
		//Debo crear una factura ficticia con un nro fact valido (porq tb hay que crear entrada en factura-ordenPago)
		FacturaCompra factura = new FacturaCompra();
		factura.setEstadoFactura(EstadoFacturaCompra.PENDIENTE);
		factura.setNro(Constantes.PREFIX_NRO_FACTURA_SF +ordenPago.getNro());
		factura.setFecha(Utils.firstMillisecondOfDay(Calendar.getInstance()));
		factura.setTipoFactura(TipoFactura.TIPO_A); 								//Siempre usa esto como Factura A (*)
		factura.setSubtotal(subtotal);
		factura.setIva(iva);
		factura.setPercIva(0);
		factura.setPercIibb(0);
		factura.setPercGcias(0);
		factura.setPercSUSS(0);
		factura.setOtrasPerc(0);
		//Por defecto el mesImpositivo es el mes actual
		Calendar mesImpositivo = Calendar.getInstance();
		mesImpositivo.set(Calendar.DAY_OF_MONTH, 1);
		mesImpositivo = Utils.firstMillisecondOfDay(mesImpositivo);
		factura.setMesImpositivo(mesImpositivo);
		factura.setBorrado(false);
		factura.setProveedor(proveedor);
		List<FacturaCompra> listadoFacturas = new ArrayList<FacturaCompra>();
		listadoFacturas.add(factura);
		ordenPago.setListadoFacturas(listadoFacturas);
	}
	
	private void crearForm() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		Label fechaOP = new Label("fechaOP",dateFormat.format(ordenPago.getFecha().getTime()));
		Label prov = new Label("prov", proveedor.getRazonSocialConCUIT());
		
		Label evento = new Label("evento",ordenPago.getEvento().getNombreConCliente());
		String planCuentaStr = ordenPago.getPlanCuenta()!=null? ordenPago.getPlanCuenta().getNombre() : "<Sin Plan Cuenta>";
		Label planCuenta = new Label("planCuenta", planCuentaStr);
		Label concepto = new Label("concepto",ordenPago.getConcepto());
		Label observaciones = new Label("observaciones",ordenPago.getObservaciones());
		Label solicitadoPor = new Label("solicitadoPor",ordenPago.getUsuarioSolicitante().getNombreCompleto());
		
		WebMarkupContainer pagosContainer = new WebMarkupContainer("pagosContainer");
		addPagosList(pagosContainer);
		pagosContainer.setOutputMarkupPlaceholderTag(true);
		
		WebMarkupContainer facturasContainer = new WebMarkupContainer("facturasContainer");
		addFacturasList(facturasContainer);
		facturasContainer.setOutputMarkupPlaceholderTag(true);

		String detallePago = " (Pago Total)";
		Label totalFacturasLbl = new Label("totalFacturas", "$ " + Utils.round2Decimals(ordenPagoService.calculateTotalBrutoFacturas(ordenPago.getListadoFacturas()), locale));
		Label totalAPagarLbl = new Label("totalAPagar", "$ " + Utils.round2Decimals(ordenPagoService.calculateTotalPagos(ordenPago.getListadoPagos()), locale) + detallePago);
		
		Form<?> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {				
				boolean error = false;
				
				ExcelGeneratorOP excelGenerator = new ExcelGeneratorOP(getRuntimeConfigurationType());
				String nroOP = null;
				try {
					nroOP = ordenPagoService.createOrUpdateOPCPySF(ordenPago);
					String CBU = proveedor.getCuentaBancaria()!=null?proveedor.getCuentaBancaria().getCbu():null;
					String alias = proveedor.getCuentaBancaria()!=null?proveedor.getCuentaBancaria().getAlias():null;
					error = excelGenerator.generarExcel(ordenPago, proveedor.getRazonSocialConCUIT(), CBU, alias, locale);
				} catch(Exception e) {
					log.error(e.getMessage(), e);
					error = true;
				}
				
				String textoPorPantalla = (error?"La orden de pago no ha podido ser registrada correctamente. Por favor, vuelva a intentarlo.":"Una nueva orden de pago ha sido registrada con N° "+nroOP+".");
				String resultado = (error?"ERROR":"OK");
												
				String fileName = excelGenerator.getFileName(ordenPago);
				PageParameters pageParameters = new PageParameters();
				pageParameters.add("Resultado", resultado);
				pageParameters.add("TextoPantalla", textoPorPantalla);
				pageParameters.add("FileName", fileName);	//Mando por parametro el FileName para que se pueda descargar
				setResponsePage(OrdenesPagoPage.class, pageParameters);
			}

		};
		Link<WebPage> volverLink = new Link<WebPage>("volverLink") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage(RegistrarOrdenPagoCPySFPage3.class);
			}
		};
		
		this.add(form);
		form.add(fechaOP);
		form.add(prov);
		form.add(evento);
		form.add(planCuenta);
		form.add(concepto);
		form.add(observaciones);
		form.add(solicitadoPor);
		form.add(pagosContainer);
		form.add(facturasContainer);
		form.add(totalFacturasLbl);
		form.add(totalAPagarLbl);
		form.add(volverLink);
	}

	private void addPagosList(final WebMarkupContainer pagosContainer) {
		IDataProvider<Pago> pagosDataProvider = getPagosProvider();
		
		DataView<Pago> dataView = new DataView<Pago>("listaPagos", pagosDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<Pago> item) {
				Pago pago = item.getModelObject();
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				String tipoStr = "";
				String detalleStr = "";
				if (pago.isEfectivo()){
					tipoStr = "Efectivo";
					detalleStr += "$" + Utils.round2Decimals(pago.getImporte(), locale);
				}
				if (pago.isCheque()){
					tipoStr = "Cheque";
					detalleStr += "$" + Utils.round2Decimals(pago.getImporte(), locale) + " - "
								+ pago.getBanco().getNombre() 
								+ "   Nro: " + pago.getNroCheque()
								+ "   Fecha: " + dateFormat.format(pago.getFecha().getTime()); 
								
				}
				if (pago.isTransferencia()){
					tipoStr = "Transferencia";
					detalleStr += "$" + Utils.round2Decimals(pago.getImporte(), locale);
					detalleStr += (pago.getFecha()!=null ? " - Fecha: " + dateFormat.format(pago.getFecha().getTime()) : "");
				}
				if (pago.isTransferencia3ro()){
					tipoStr = "Transferencia a 3ro";
					detalleStr += "$" + Utils.round2Decimals(pago.getImporte(), locale) + " - ";
					detalleStr += pago.getCobroAlternativo().getTitularConCUIT() +" - ";
					detalleStr += Utils.generarDatosBancarios(null, pago.getCobroAlternativo().getCuentaBancaria());					
				}
				if (pago.isTarjetaCredito()){
					tipoStr = "Tarjeta Crédito";
					detalleStr += "$" + Utils.round2Decimals(pago.getImporte(), locale);
				}
				
				Label tipo = new Label("tipo",tipoStr);
				Label detalle = new Label("detalle",detalleStr);
				
				item.add(tipo);
				item.add(detalle);
			}

		};
		
		pagosContainer.add(dataView);
	}
	
	private void addFacturasList(final WebMarkupContainer facturasContainer) {
		IDataProvider<FacturaCompra> facturasDataProvider = getFacturasProvider();
		
		DataView<FacturaCompra> dataView = new DataView<FacturaCompra>("listaFacturas", facturasDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<FacturaCompra> item) {
				FacturaCompra factura = item.getModelObject();
				String tipoStr = "";
				String detalleStr = "";
					tipoStr = "<Sin Factura>";
					detalleStr += "$" + Utils.round2Decimals(ordenPagoService.calculateTotalNetoFactura(factura), locale) + " - "
								+ factura.getEstadoFactura().getNombre() ; 
								
				
				Label tipo = new Label("tipo",tipoStr);
				Label detalle = new Label("detalle",detalleStr);
				
				item.add(tipo);
				item.add(detalle);
			}

		};
		
		facturasContainer.add(dataView);
	}
	
	private IDataProvider<Pago> getPagosProvider() {
		IDataProvider<Pago> pagosDataProvider = new IDataProvider<Pago>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void detach() {
			}

			@Override
			public Iterator<Pago> iterator(long first, long count) {
				return ordenPago.getListadoPagos().iterator();
			}

			@Override
			public long size() {
				return ordenPago.getListadoPagos().size();
			}

			@Override
			public IModel<Pago> model(Pago pago) {
				return new Model<Pago>(pago);
			}
        	
        };
		return pagosDataProvider;
	}
	
	private IDataProvider<FacturaCompra> getFacturasProvider() {
		IDataProvider<FacturaCompra> facturasDataProvider = new IDataProvider<FacturaCompra>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void detach() {
			}

			@Override
			public Iterator<FacturaCompra> iterator(long first, long count) {
				return ordenPago.getListadoFacturas().iterator();
			}

			@Override
			public long size() {
				return ordenPago.getListadoFacturas().size();
			}

			@Override
			public IModel<FacturaCompra> model(FacturaCompra factura) {
				return new Model<FacturaCompra>(factura);
			}
        	
        };
		return facturasDataProvider;
	}
	
}