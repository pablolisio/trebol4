package com.proit.vista.ventas.cobranzas;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
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

import com.proit.modelo.ventas.FacturaVenta;
import com.proit.modelo.ventas.Cobranza;
import com.proit.modelo.ventas.Cobro;
import com.proit.modelo.ventas.Cliente;
import com.proit.servicios.ventas.CobranzaService;
import com.proit.servicios.ventas.ClienteService;
import com.proit.servicios.ventas.FacturaVentaService;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class RegistrarCobranzaPage4 extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(RegistrarCobranzaPage4.class.getName());
	
	private CobranzaService cobranzaService;
	private Cobranza cobranza;
	private Locale locale;
	
	public RegistrarCobranzaPage4() {

		locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
		
		cobranzaService = new CobranzaService();
		
		cobranza = (Cobranza) ((FacturarOnLineAuthenticatedWebSession) getSession()).getAttribute("cob");
		
		crearForm();
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
	}
	
	private void crearForm() {
		final String razonSocial = (String) ((FacturarOnLineAuthenticatedWebSession) getSession()).getAttribute("rz");
		final Double importeParcial = (Double) ((FacturarOnLineAuthenticatedWebSession) getSession()).getAttribute("parcial");
		ClienteService clienteService = new ClienteService();
		Cliente cliente = clienteService.getByRazonSocial(razonSocial);
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		Label nroRecibo = new Label("nroRecibo",cobranza.getNroRecibo()!=null?cobranza.getNroRecibo():"<Sin Recibo>");
		Label fechaCobranza = new Label("fechaCobranza",dateFormat.format(cobranza.getFecha().getTime()));
		Label cli = new Label("cli", cliente.getRazonSocialConCUIT());
				
		WebMarkupContainer cobrosContainer = new WebMarkupContainer("cobrosContainer");
		addCobrosList(cobrosContainer);
		cobrosContainer.setOutputMarkupPlaceholderTag(true);
		
		WebMarkupContainer retencionesContainer = new WebMarkupContainer("retencionesContainer");
		String detalleRetenciones = "";
		detalleRetenciones += cobranza.getPercIva()!=0 ? "- PERC.IVA: $" + Utils.round2Decimals(cobranza.getPercIva(), locale) : "";
		detalleRetenciones += cobranza.getPercIibb()!=0 ? " - PERC.IIBB: $" + Utils.round2Decimals(cobranza.getPercIibb(), locale) : "";
		detalleRetenciones += cobranza.getPercGcias()!=0 ? " - PERC.Gcias: $" + Utils.round2Decimals(cobranza.getPercGcias(), locale) : "";
		detalleRetenciones += cobranza.getPercSUSS()!=0 ? " - PERC.SUSS: $" + Utils.round2Decimals(cobranza.getPercSUSS(), locale) : "";
		detalleRetenciones += cobranza.getOtrasPerc()!=0 ? " - Otras PERC.: $" + Utils.round2Decimals(cobranza.getOtrasPerc(), locale) : "";
		Label retenciones = new Label("detalleRetenciones", detalleRetenciones);
		retencionesContainer.add(retenciones);
		retencionesContainer.setVisible(cobranza.getPercIva()!=0 || cobranza.getPercIibb()!=0 || cobranza.getPercGcias()!=0 || cobranza.getPercSUSS()!=0 || cobranza.getOtrasPerc()!=0);
		cobrosContainer.add(retencionesContainer);
		
		WebMarkupContainer facturasContainer = new WebMarkupContainer("facturasContainer");
		addFacturasList(facturasContainer);
		facturasContainer.setOutputMarkupPlaceholderTag(true);

		String detalleCobro = importeParcial>0 ? " (Cobro Parcial)" : " (Cobro Total)";
		double totalRetenciones = cobranza.getPercIva() + cobranza.getPercIibb() + cobranza.getPercGcias() + cobranza.getPercSUSS() + cobranza.getOtrasPerc();
		double totalACobrarConRetenciones = cobranzaService.calculateTotalCobros(cobranza.getListadoCobros()) + totalRetenciones;
		Label totalFacturasLbl = new Label("totalFacturas", "$ " + Utils.round2Decimals(cobranzaService.calculateTotalBrutoFacturas(cobranza.getListadoFacturas()), locale));
		Label totalACobrarLbl = new Label("totalACobrarConRetenciones", "$ " + Utils.round2Decimals(totalACobrarConRetenciones, locale) + detalleCobro);
				
		Form<?> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {				
				boolean error = false;
				
				try {
					cobranzaService.createOrUpdateCobranza(cobranza, importeParcial>0);
				} catch(Exception e) {
					log.error(e.getMessage(), e);
					error = true;
				}
				
				String textoPorPantalla = (error?"La cobranza no ha podido ser registrada correctamente. Por favor, vuelva a intentarlo.":"Una nueva cobranza ha sido registrada.");
				String resultado = (error?"ERROR":"OK");
				
				PageParameters pageParameters = new PageParameters();
				pageParameters.add("Resultado", resultado);
				pageParameters.add("TextoPantalla", textoPorPantalla);
				setResponsePage(CobranzasPage.class, pageParameters);
			}

		};
		Link<WebPage> volverLink = new Link<WebPage>("volverLink") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage(RegistrarCobranzaPage3.class);
			}
		};
		
		this.add(form);
		form.add(nroRecibo);
		form.add(fechaCobranza);
		form.add(cli);
		form.add(cobrosContainer);
		form.add(facturasContainer);
		form.add(totalFacturasLbl);
		form.add(totalACobrarLbl);
		form.add(volverLink);
	}

	private void addCobrosList(final WebMarkupContainer cobrosContainer) {
		IDataProvider<Cobro> cobrosDataProvider = getCobrosProvider();
		
		DataView<Cobro> dataView = new DataView<Cobro>("listaCobros", cobrosDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<Cobro> item) {
				Cobro cobro = item.getModelObject();
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				String tipoStr = "";
				String detalleStr = "";
				if (cobro.isEfectivo()){
					tipoStr = "Efectivo";
					detalleStr += "$" + Utils.round2Decimals(cobro.getImporte(), locale);
				}
				if (cobro.isCheque()){
					tipoStr = "Cheque";
					detalleStr += "$" + Utils.round2Decimals(cobro.getImporte(), locale) + " - "
								+ cobro.getBancoCheque()
								+ "   Nro: " + cobro.getNroCheque()
								+ "   Fecha: " + dateFormat.format(cobro.getFecha().getTime()); 
								
				}
				if (cobro.isTransferencia()){
					tipoStr = "Transferencia";
					detalleStr += "$" + Utils.round2Decimals(cobro.getImporte(), locale);
					detalleStr += (cobro.getBancoTransferencia()!=null ? " - " + cobro.getBancoTransferencia().getNombre(): "");
					detalleStr += (cobro.getFecha()!=null ? " - Fecha: " + dateFormat.format(cobro.getFecha().getTime()) : "");
				}
				
				Label tipo = new Label("tipo",tipoStr);
				Label detalle = new Label("detalle",detalleStr);
				
				item.add(tipo);
				item.add(detalle);
			}

		};
		
		cobrosContainer.add(dataView);
	}
	
	private void addFacturasList(final WebMarkupContainer facturasContainer) {
		IDataProvider<FacturaVenta> facturasDataProvider = getFacturasProvider();
		
		DataView<FacturaVenta> dataView = new DataView<FacturaVenta>("listaFacturas", facturasDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<FacturaVenta> item) {
				FacturaVentaService facturaVentaService = new FacturaVentaService();
				FacturaVenta factura = item.getModelObject();
				factura = facturaVentaService.getFacturaByTipoYNroFactura(factura.getTipoFactura(), factura.getNro());
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				String tipoStr = "";
				String detalleStr = "";
					tipoStr = factura.getTipoFactura().getNombre() + " - " + factura.getNro();
					detalleStr += "$" + (factura.isNotaCredito()?"-":"") + Utils.round2Decimals(cobranzaService.calculateTotalNetoFactura(factura), locale) + " - "
								+ factura.getEstadoFacturaVenta().getNombre() 
								+ " (Fecha: " + dateFormat.format(factura.getFecha().getTime()) + ")" ; 
								
				
				Label tipo = new Label("tipo",tipoStr);
				Label detalle = new Label("detalle",detalleStr);
				
				item.add(tipo);
				item.add(detalle);
			}

		};
		
		facturasContainer.add(dataView);
	}
	
	private IDataProvider<Cobro> getCobrosProvider() {
		IDataProvider<Cobro> cobrosDataProvider = new IDataProvider<Cobro>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void detach() {
			}

			@Override
			public Iterator<Cobro> iterator(long first, long count) {
				return cobranza.getListadoCobros().iterator();
			}

			@Override
			public long size() {
				return cobranza.getListadoCobros().size();
			}

			@Override
			public IModel<Cobro> model(Cobro cobro) {
				return new Model<Cobro>(cobro);
			}
        	
        };
		return cobrosDataProvider;
	}
	
	private IDataProvider<FacturaVenta> getFacturasProvider() {
		IDataProvider<FacturaVenta> facturasDataProvider = new IDataProvider<FacturaVenta>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void detach() {
			}

			@Override
			public Iterator<FacturaVenta> iterator(long first, long count) {
				return cobranza.getListadoFacturas().iterator();
			}

			@Override
			public long size() {
				return cobranza.getListadoFacturas().size();
			}

			@Override
			public IModel<FacturaVenta> model(FacturaVenta factura) {
				return new Model<FacturaVenta>(factura);
			}
        	
        };
		return facturasDataProvider;
	}
	
}