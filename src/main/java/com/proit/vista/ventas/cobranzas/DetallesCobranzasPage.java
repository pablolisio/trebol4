package com.proit.vista.ventas.cobranzas;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.proit.modelo.ventas.Cliente;
import com.proit.modelo.ventas.Cobranza;
import com.proit.modelo.ventas.Cobro;
import com.proit.modelo.ventas.EstadoFacturaVenta;
import com.proit.modelo.ventas.FacturaVenta;
import com.proit.modelo.ventas.FacturaVentaCobranza;
import com.proit.servicios.ventas.CobranzaService;
import com.proit.utils.Constantes;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;


@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class DetallesCobranzasPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	private CobranzaService cobranzaService;
	private Cobranza cobranza;
	private List<FacturaVenta> listadoFacturas;
	private Cliente cliente;
	
	private Locale locale;
	
	/**
	 * Si bien entra una FacturaVentaCobranza, la factura siempre viene como null, y borrado como false. 
	 * Solo se usa la Cobranza
	 * @param facturaVentaCobranza
	 */
	public DetallesCobranzasPage(FacturaVentaCobranza facturaVentaCobranza) {
		
		locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
		
		cobranzaService = new CobranzaService();
		
		cobranza = facturaVentaCobranza.getCobranza();
		cobranza = (Cobranza) cobranzaService.get(cobranza.getId());
				
		listadoFacturas = cobranza.getListadoFacturas();
		
		cliente = listadoFacturas.get(0).getCliente();
		
		WebMarkupContainer cobranzaEliminadaContainer = new WebMarkupContainer("cobranzaEliminadaContainer");
		cobranzaEliminadaContainer.setVisible(cobranza.isBorrado());
		add(cobranzaEliminadaContainer);
		
		crearForm();
		
		crearEditarFechasChequesLink();
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
	}
	
	private void crearEditarFechasChequesLink() {
		Link<Cobranza> editarFechasChequesLink = new Link<Cobranza>("editarFechasChequesLink") {			
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage(new EditarFechasChequesPage(cobranza));
			}
		};
		int cantidadCheques = contarCheques();
		editarFechasChequesLink.setVisible(cantidadCheques>0);
		add(editarFechasChequesLink);
	}

	private int contarCheques() {
		int cantidadCheques = 0;
		for (Cobro cobro : cobranza.getListadoCobros()) {
			if (cobro.isCheque()) {
				cantidadCheques ++;
			}
		}
		return cantidadCheques;
	}

	private void crearForm() {
		final String clienteStr = cliente.getRazonSocialConCUIT();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		Label nroRecibo = new Label("nroRecibo",cobranza.getNroRecibo()!=null?cobranza.getNroRecibo():"<Sin Recibo>");
		Label fechaCobranza = new Label("fechaCobranza",dateFormat.format(cobranza.getFecha().getTime()));
		Label cli = new Label("cli", clienteStr);
						
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
		detalleRetenciones += cobranza.isRetencionesValidadas() ? " (Validadas)" : " (No validadas)";
		Label retenciones = new Label("detalleRetenciones", detalleRetenciones);
		retencionesContainer.add(retenciones);
		retencionesContainer.setVisible(cobranza.getPercIva()!=0 || cobranza.getPercIibb()!=0 || cobranza.getPercGcias()!=0 || cobranza.getPercSUSS()!=0 || cobranza.getOtrasPerc()!=0);
		cobrosContainer.add(retencionesContainer);
		
		WebMarkupContainer facturasContainer = new WebMarkupContainer("facturasContainer");
		addFacturasList(facturasContainer);
		facturasContainer.setOutputMarkupPlaceholderTag(true);
		
		double totalFacturas = 0;
		for (FacturaVenta factura : cobranza.getListadoFacturas()) {
			if ( ! factura.isNotaCredito() ) {
				totalFacturas += factura.calculateTotal();
			} else {
				totalFacturas -= factura.calculateTotal();
			}
		}
		
		double totalRetenciones = cobranza.getPercIva() + cobranza.getPercIibb() + cobranza.getPercGcias() + cobranza.getPercSUSS() + cobranza.getOtrasPerc();
		double totalACobrarConRetenciones = cobranzaService.calculateTotalCobros(cobranza.getListadoCobros()) + totalRetenciones;
		boolean isCobroParcial = cobranza.getListadoFacturas().size()==1
				&& totalACobrarConRetenciones>0 && Utils.round(totalFacturas,2)>Utils.round(totalACobrarConRetenciones,2); //Logica para saber si pago parcial (buscar este comentario)
		String detalleCobros =  isCobroParcial ? " (Cobro Parcial) " : " (Cobro Total) ";
		Label totalFacturasLbl = new Label("totalFacturas", "$ " + Utils.round2Decimals(cobranzaService.calculateTotalBrutoFacturas(cobranza.getListadoFacturas()), locale));
		Label totalACobrarLbl = new Label("totalACobrarConRetenciones", "$ " + Utils.round2Decimals(totalACobrarConRetenciones, locale) + detalleCobros);
		
		//Para los casos de Facturas con Cobro Parcial
		Link<Cobranza> crearCobranzaLink = new Link<Cobranza>("crearCobranza") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				//Seteo la info necesaria para poder ir al CrearCobranza, pero directo a la pag 2 (Seleccionar Modo de Cobro)
				Cobranza cobranzaNueva = new Cobranza();
				cobranzaNueva.setListadoFacturas(cobranza.getListadoFacturas());
				((FacturarOnLineAuthenticatedWebSession) getSession()).setAttribute("cob", cobranzaNueva);
				((FacturarOnLineAuthenticatedWebSession) getSession()).setAttribute("rz", cliente.getRazonSocial());
				((FacturarOnLineAuthenticatedWebSession) getSession()).setAttribute("parcial", 0d);
				
				setResponsePage(RegistrarCobranzaPage2.class, new PageParameters());
			}
		};
		crearCobranzaLink.setVisible( ! cobranza.isBorrado() 
				&& isCobroParcial 
				&& cobranza.getListadoFacturas().get(0).getEstadoFacturaVenta().equals(EstadoFacturaVenta.COBRADO_PARCIAL)
				&&  ! isUsuarioLogueadoRolSoloLectura());
		
		this.add(nroRecibo);
		this.add(fechaCobranza);
		this.add(cli);
		this.add(cobrosContainer);
		this.add(facturasContainer);
		this.add(totalFacturasLbl);
		this.add(totalACobrarLbl);
		this.add(crearCobranzaLink);
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
				FacturaVenta factura = item.getModelObject();
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				String tipoStr = "";
				String detalleStr = "";
					tipoStr = factura.getTipoFactura().getNombre() + " - " + factura.getNro();
					double total;
					if (factura.getEstadoFacturaVenta().equals(EstadoFacturaVenta.COBRADO_PARCIAL)) {
						total = factura.calculateTotal();
					} else {
						total = cobranzaService.calculateTotalNetoFactura(factura);
					}
					detalleStr += "$" + (factura.isNotaCredito()?"-":"") + Utils.round2Decimals(total, locale) + " - "
								+ factura.getEstadoFacturaVenta().getNombre() 
								+ " (Fecha: " + dateFormat.format(factura.getFecha().getTime()) + ")" ; 
				
				final boolean isSinFactura = item.getModelObject().getNro().startsWith(Constantes.PREFIX_NRO_FACTURA_SF) ? true : false;
				tipoStr = isSinFactura ? "<Sin Factura>" : tipoStr;
				
				Label tipo = new Label("tipo",tipoStr) {
					private static final long serialVersionUID = 1L;
					@Override
				    protected void onComponentTag(final ComponentTag tag){
				        super.onComponentTag(tag);
				        if (isSinFactura) {
				        	tag.put("class", "text-warning");
				        } else {
				        	tag.put("class", "text-default");
				        }
				    }
				};
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
				List<Cobro> returnList = cobranza.getListadoCobros();
				Collections.sort(returnList, new Comparator<Cobro>() {
	                @Override
	                public int compare(Cobro cobro1, Cobro cobro2) {
	                	if (cobro1.isCheque()&&cobro2.isCheque()) {
	                		if (cobro1.getFecha()!=null && cobro2.getFecha()!=null) {
		                		return cobro1.getFecha().compareTo(cobro2.getFecha());
		                	}
		                	return 1;
	                	} else if (cobro1.isTransferencia()&&cobro2.isTransferencia()) {
	                		if (cobro1.getFecha()!=null && cobro2.getFecha()!=null) {
		                		return cobro1.getFecha().compareTo(cobro2.getFecha());
		                	}
		                	return 1;
	                	}
	                	return -1;
	                }
	            });
				
				return returnList.iterator();
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