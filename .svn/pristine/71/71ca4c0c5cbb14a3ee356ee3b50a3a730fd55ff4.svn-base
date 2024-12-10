package com.proit.vista.compras.solicitudes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.proit.modelo.compras.CuentaBancaria;
import com.proit.modelo.compras.EstadoSolicitudPago;
import com.proit.modelo.compras.FacturaCompra;
import com.proit.modelo.compras.FacturaSolicitudPago;
import com.proit.modelo.compras.PagoSolicitudPago;
import com.proit.modelo.compras.Proveedor;
import com.proit.modelo.compras.SolicitudPago;
import com.proit.servicios.compras.SolicitudPagoService;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;

@AuthorizeInstantiation({"Administrador","Solicitante Pagos","Desarrollador","Editor Solicitudes Factura"})
public class DetallesSolicitudesPagoPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(DetallesSolicitudesPagoPage.class.getName());
	
	private SolicitudPagoService solicitudPagoService;
	private SolicitudPago solicitudPago;
	private Proveedor proveedor;
	
	private Locale locale;
	
	public DetallesSolicitudesPagoPage(SolicitudPago solicitud) {
		
		locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
		
		solicitudPagoService = new SolicitudPagoService();
		
		solicitudPago = solicitud;
		
		boolean tieneProveedor = solicitudPago.getProveedor()!=null || solicitudPago.getRazonSocial()!=null;
		proveedor = null;
		if (tieneProveedor) {
			if (solicitudPago.getProveedor()!=null) {
				proveedor = solicitudPago.getProveedor();
			} else { //creo uno nuevo ficticio, solo para mostrar los datos en esta pantalla
				proveedor = new Proveedor();
				CuentaBancaria cuentaBancaria = new CuentaBancaria();
				cuentaBancaria.setCbu(solicitudPago.getCbu());
				proveedor.setRazonSocial(solicitudPago.getRazonSocial());
				proveedor.setCuitCuil(solicitudPago.getCuitCuil());
				proveedor.setCuentaBancaria(cuentaBancaria);
			}
		}
		
		agregarEstadoSolicitudPagoArriba();
		
		crearForm();
		
		agregarRechazarBtn();
		
		agregarEliminarBtn();
		
		Link<WebPage> volverLink = new Link<WebPage>("volverLink") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				if (isUsuarioLogueadoRolAdministrador()||isUsuarioLogueadoRolDesarrollador()) {
					setResponsePage(SolicitudesPagoPage.class);
				} else {
					setResponsePage(MisSolicitudesPagoPage.class);
				}
			}
		};
		add(volverLink);
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
	}

	private void agregarEstadoSolicitudPagoArriba() {
		EstadoSolicitudPago estadoSolicitudPago = solicitudPago.getEstadoSolicitudPago();
		WebMarkupContainer solicitudPagoRechazadaContainer = new WebMarkupContainer("solicitudPagoRechazadaContainer");
		solicitudPagoRechazadaContainer.setVisible(estadoSolicitudPago.equals(EstadoSolicitudPago.RECHAZADA));
		add(solicitudPagoRechazadaContainer);
		
		WebMarkupContainer solicitudPagoCumplidaContainer = new WebMarkupContainer("solicitudPagoCumplidaContainer");
		solicitudPagoCumplidaContainer.setVisible(estadoSolicitudPago.equals(EstadoSolicitudPago.CUMPLIDA));
		add(solicitudPagoCumplidaContainer);
		
		WebMarkupContainer solicitudPagoPendienteContainer = new WebMarkupContainer("solicitudPagoPendienteContainer");
		solicitudPagoPendienteContainer.setVisible(
					estadoSolicitudPago.equals(EstadoSolicitudPago.PENDIENTE_1)
				|| 	estadoSolicitudPago.equals(EstadoSolicitudPago.PENDIENTE_2)
				|| 	estadoSolicitudPago.equals(EstadoSolicitudPago.PENDIENTE_3));
		add(solicitudPagoPendienteContainer);
	}
	
	private void agregarRechazarBtn() {
		Button intentarRechazarBtn = new Button("botonIntentarRechazar");
		intentarRechazarBtn.setVisible( 
				(solicitudPago.isPendiente1() || solicitudPago.isPendiente2() || solicitudPago.isPendiente3() )
				&& (isUsuarioLogueadoRolAdministrador() || isUsuarioLogueadoRolDesarrollador()) );
		add(intentarRechazarBtn);
		
		Link<SolicitudPago> rechazarBtn = new Link<SolicitudPago>("rechazarBtn") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				solicitudPago.setEstadoSolicitudPago(EstadoSolicitudPago.RECHAZADA);
				solicitudPagoService.createOrUpdate(solicitudPago);
				setResponsePage(new DetallesSolicitudesPagoPage(solicitudPago));
			}
		};
		add(rechazarBtn);
		add(new Label("solicitudARechazar", solicitudPago.getNro()));
	}
	
	private void agregarEliminarBtn() {
		Button intentarEliminarBtn = new Button("botonIntentarEliminar");
		intentarEliminarBtn.setVisible( 
				(solicitudPago.isPendiente1() || solicitudPago.isPendiente2() || solicitudPago.isPendiente3() )
				&& (isUsuarioLogueadoRolSolicitantePagos() || isUsuarioLogueadoRolDesarrollador()) );
		add(intentarEliminarBtn);
		
		Link<SolicitudPago> eliminarBtn = new Link<SolicitudPago>("eliminarBtn") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				String textoPorPantalla = "La Solicitud de Pago" + solicitudPago.getNro() + " ha sido eliminada.";
				String resultado = "OK";
				try {
					boolean fueBorrada = solicitudPagoService.delete(solicitudPago);
					if (!fueBorrada) {
						textoPorPantalla = "La Solicitud de Pago" + solicitudPago.getNro() + " no puede ser eliminada, porque su estado ya no es Pendiente.";
						resultado = "ERROR";
					}
				} catch(Exception e) {
					log.error(e.getMessage(), e);
					textoPorPantalla = "La Solicitud de Pago" + solicitudPago.getNro() + " no ha sido eliminada correctamente. Por favor, vuelva a intentarlo.";
					resultado = "ERROR";
				}
				PageParameters pageParameters = new PageParameters();
				pageParameters.add("Resultado", resultado);
				pageParameters.add("TextoPantalla", textoPorPantalla);
				setResponsePage(MisSolicitudesPagoPage.class, pageParameters);
			}
		};
		add(eliminarBtn);
		add(new Label("solicitudAEliminar", solicitudPago.getNro()));
	}

	private void crearForm() {
		final String proveedorStr = solicitudPago.isSPySF()? "<Sin Proveedor>" : 
						proveedor.getRazonSocialConCUIT()
						+ (proveedor.getCuentaBancaria()!=null?
								(proveedor.getCuentaBancaria().getCbu()!=null?" - CBU:"+proveedor.getCuentaBancaria().getCbu():" - Alias:"+proveedor.getCuentaBancaria().getAlias())
								:"") ;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		Label nroSol = new Label("nroSol",solicitudPago.getNro());
		Label fechaSol = new Label("fechaSol",dateFormat.format(solicitudPago.getFecha().getTime()));
		Label prov = new Label("prov", proveedorStr)  {
			private static final long serialVersionUID = 1L;
			@Override
		    protected void onComponentTag(final ComponentTag tag){
		        super.onComponentTag(tag);
		        if (solicitudPago.isSPySF()) {
		        	tag.put("class", "text-warning");
		        } else {
		        	tag.put("class", "text-default");
		        }
		    }
		};
		
		Label evento = new Label("evento",solicitudPago.getEvento().getNombreConCliente());
		Label concepto = new Label("concepto",solicitudPago.getConcepto());
		Label observaciones = new Label("observaciones",solicitudPago.getObservaciones());
		Label solicitadoPor = new Label("solicitadoPor",solicitudPago.getUsuarioSolicitante().getNombreCompleto());
		Label cliente = new Label("cliente",solicitudPago.getCliente().getRazonSocial());
		Label nroFacturaVenta = new Label("nroFacturaVenta",solicitudPago.getNroFacturaVenta());
						
		WebMarkupContainer pagosContainer = new WebMarkupContainer("pagosContainer");
		addPagosList(pagosContainer);
		pagosContainer.setOutputMarkupPlaceholderTag(true);
		
		WebMarkupContainer facturasContainer = new WebMarkupContainer("facturasContainer");
		addFacturasList(facturasContainer);
		facturasContainer.setOutputMarkupPlaceholderTag(true);
		facturasContainer.setVisible(solicitudPago.isConFactura());
		
		Label sinFacturaLbl = new Label("sinFactura","<Sin Factura>");
		sinFacturaLbl.setVisible(!solicitudPago.isConFactura());

		double totalFacturas = 0;
		if (solicitudPago.isConFactura()) {
			for (FacturaSolicitudPago factura : solicitudPago.getListadoFacturas()) {
				if ( ! factura.isNotaCredito() ) {
					totalFacturas += factura.getTotal();
				} else {
					totalFacturas -= factura.getTotal();
				}
			}
		}
		double totalPagos = solicitudPagoService.calcularSumaTotalPagos(solicitudPago.getListadoPagos());
		boolean isPagoParcial = solicitudPago.isConFactura() && solicitudPago.getListadoFacturas().size()==1 //le tuve que agregar el "isConFactura" porq sino pinchaba para las que no tienen factura con LazyInitializationException
				&& (Utils.round(totalFacturas,2)>Utils.round(totalPagos,2)); //Logica para saber si pago parcial (buscar este comentario)
		String detallePagos =  isPagoParcial ? " (Pago Parcial) " : " (Pago Total) ";
		
		Label totalFacturasLbl = new Label("totalFacturas", "$ " + Utils.round2Decimals(totalFacturas, locale));
		Label totalAPagarLbl = new Label("totalAPagar", "$ " + Utils.round2Decimals(totalPagos, locale) + detallePagos);
		
		this.add(nroSol);
		this.add(fechaSol);
		this.add(prov);
		this.add(evento);
		this.add(concepto);
		this.add(observaciones);
		this.add(solicitadoPor);
		this.add(cliente);
		this.add(nroFacturaVenta);
		this.add(pagosContainer);
		this.add(sinFacturaLbl);
		this.add(facturasContainer);
		this.add(totalFacturasLbl);
		this.add(totalAPagarLbl);
	}

	private void addPagosList(final WebMarkupContainer pagosContainer) {
		IDataProvider<PagoSolicitudPago> pagosDataProvider = getPagosProvider();
		
		DataView<PagoSolicitudPago> dataView = new DataView<PagoSolicitudPago>("listaPagos", pagosDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<PagoSolicitudPago> item) {
				PagoSolicitudPago pago = item.getModelObject();
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				String tipoStr = "";
				String detalleStr = "";
				if (pago.isEfectivo()){
					tipoStr = "Efectivo";
					detalleStr += "$" + Utils.round2Decimals(pago.getImporte(), locale);
					detalleStr += " (Fecha: " + dateFormat.format(pago.getFecha().getTime()) + ")" ; 
				}
				if (pago.isCheque()){
					tipoStr = "Cheque";
					detalleStr += "$" + Utils.round2Decimals(pago.getImporte(), locale)
								+ " (Fecha: " + dateFormat.format(pago.getFecha().getTime()) + ")" ; 
								
				}
				if (pago.isTransferencia()){
					tipoStr = "Transferencia";
					detalleStr += "$" + Utils.round2Decimals(pago.getImporte(), locale);
					detalleStr += (pago.getFecha()!=null ? " (Fecha: " + dateFormat.format(pago.getFecha().getTime()) +")": "");
				}
				if (pago.isTransferencia3ro()){
					tipoStr = "Transferencia a 3ro";
					detalleStr += "$" + Utils.round2Decimals(pago.getImporte(), locale) + " - ";
					detalleStr += pago.getCobroAlternativo().getTitularConCUIT() + " - ";
					detalleStr += Utils.generarDatosBancarios(null, pago.getCobroAlternativo().getCuentaBancaria());					
				}
				if (pago.isTarjetaCredito()){
					tipoStr = "Tarjeta Crédito";
					detalleStr += "$" + Utils.round2Decimals(pago.getImporte(), locale);
					detalleStr += " (Fecha: " + dateFormat.format(pago.getFecha().getTime()) + ")" ; 
				}
				if (pago.isTransferenciaSinProv()){
					tipoStr = "Transferencia Bancaria";
					detalleStr += "$" + Utils.round2Decimals(pago.getImporte(), locale) + " - ";
					detalleStr += Utils.generarDatosBancarios(pago.getCuitCuil(), pago.getCuentaBancaria());
					detalleStr += (pago.getFecha()!=null ? " (Fecha: " + dateFormat.format(pago.getFecha().getTime()) +")": "");
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
		IDataProvider<FacturaSolicitudPago> facturasDataProvider = getFacturasProvider();
		
		DataView<FacturaSolicitudPago> dataView = new DataView<FacturaSolicitudPago>("listaFacturas", facturasDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<FacturaSolicitudPago> item) {
				FacturaSolicitudPago factura = item.getModelObject();
				FacturaCompra facturaCompra;
				if (factura.getFacturaCompra()!=null) {
					facturaCompra = factura.getFacturaCompra();
				} else {
					//creo una nueva ficticia, solo para mostrar los datos en esta pantalla
					facturaCompra = new FacturaCompra();
					facturaCompra.setFecha(factura.getFechaFacturaCompra());
					facturaCompra.setTipoFactura(factura.getTipoFacturaCompra());
					facturaCompra.setNro(factura.getNroFacturaCompra());
				}
				String tipoStr = "";
				String detalleStr = "";
				
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				
					tipoStr = facturaCompra.getTipoFactura().getNombre() + " - " + facturaCompra.getNro();
					detalleStr += "$" + (factura.isNotaCredito()?"-":"") + Utils.round2Decimals(factura.getTotal(), locale);
					if (facturaCompra.getFecha()!=null) {
						detalleStr += " (" + dateFormat.format(facturaCompra.getFecha().getTime())  + ")";
					}
				
				tipoStr = !solicitudPago.isConFactura() ? "<Sin Factura>" : tipoStr;
				
				Label tipo = new Label("tipo",tipoStr) {
					private static final long serialVersionUID = 1L;
					@Override
				    protected void onComponentTag(final ComponentTag tag){
				        super.onComponentTag(tag);
				        if (!solicitudPago.isConFactura()) {
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
	
	private IDataProvider<PagoSolicitudPago> getPagosProvider() {
		IDataProvider<PagoSolicitudPago> pagosDataProvider = new IDataProvider<PagoSolicitudPago>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void detach() {
			}

			@Override
			public Iterator<PagoSolicitudPago> iterator(long first, long count) {
				List<PagoSolicitudPago> returnList = solicitudPago.getListadoPagos();
				Collections.sort(returnList, new Comparator<PagoSolicitudPago>() {
	                @Override
	                public int compare(PagoSolicitudPago pago1, PagoSolicitudPago pago2) {
	                	return pago1.getFecha().compareTo(pago2.getFecha());
	                }
	            });
				
				return returnList.iterator();
			}

			@Override
			public long size() {
				return solicitudPago.getListadoPagos().size();
			}

			@Override
			public IModel<PagoSolicitudPago> model(PagoSolicitudPago pago) {
				return new Model<PagoSolicitudPago>(pago);
			}
        	
        };
		return pagosDataProvider;
	}
	
	private IDataProvider<FacturaSolicitudPago> getFacturasProvider() {
		IDataProvider<FacturaSolicitudPago> facturasDataProvider = new IDataProvider<FacturaSolicitudPago>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void detach() {
			}

			@Override
			public Iterator<FacturaSolicitudPago> iterator(long first, long count) {
				if (solicitudPago.isConFactura()) {
					return solicitudPago.getListadoFacturas().iterator();
				}
				return new ArrayList<FacturaSolicitudPago>().iterator();
			}

			@Override
			public long size() {
				if (solicitudPago.isConFactura()) {
					return solicitudPago.getListadoFacturas().size();
				}
				return 0;
			}

			@Override
			public IModel<FacturaSolicitudPago> model(FacturaSolicitudPago factura) {
				return new Model<FacturaSolicitudPago>(factura);
			}
        	
        };
		return facturasDataProvider;
	}
	
}