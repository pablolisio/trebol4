package com.proit.vista.compras.ordenes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.ajax.markup.html.AjaxLink;
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

import com.proit.modelo.compras.EstadoFacturaCompra;
import com.proit.modelo.compras.FacturaCompra;
import com.proit.modelo.compras.FacturaCompraOrdenPago;
import com.proit.modelo.compras.OrdenPago;
import com.proit.modelo.compras.Pago;
import com.proit.modelo.compras.Proveedor;
import com.proit.modelo.compras.SolicitudPago;
import com.proit.servicios.compras.OrdenPagoService;
import com.proit.servicios.compras.SolicitudPagoService;
import com.proit.utils.Constantes;
import com.proit.utils.ExcelGeneratorOP;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.vista.compras.facturas.RegistrarFacturaPage;
import com.proit.vista.compras.ordenes.normal.RegistrarOrdenPagoNormalPage2;
import com.proit.vista.compras.solicitudes.DetallesSolicitudesPagoPage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.AJAXDownload;

@AuthorizeInstantiation({"Administrador","Solo Lectura","Desarrollador"})
public class DetallesOrdenesPagoPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	private OrdenPagoService ordenPagoService;
	private OrdenPago ordenPago;
	private List<FacturaCompra> listadoFacturas;
	private Proveedor proveedor;
	
	private Locale locale;
	
	/**
	 * Si bien entra una FacturaOrdenPago, la factura siempre viene como null, y borrado como false. 
	 * Solo se usa la OrdenPago
	 * @param facturaOrdenPago
	 */
	public DetallesOrdenesPagoPage(FacturaCompraOrdenPago facturaOrdenPago) {
		
		locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
		
		ordenPagoService = new OrdenPagoService();
		
		ordenPago = facturaOrdenPago.getOrdenPago();
		listadoFacturas = ordenPago.getListadoFacturas();
		proveedor = null;
		if (!ordenPago.isSPySF()) {
			proveedor = listadoFacturas.get(0).getProveedor();
		}
		
		WebMarkupContainer ordenPagoEliminadaContainer = new WebMarkupContainer("ordenPagoEliminadaContainer");
		ordenPagoEliminadaContainer.setVisible(ordenPago.isBorrado());
		add(ordenPagoEliminadaContainer);
		
		crearForm();
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
	}

	private void crearForm() {
		final String proveedorStr = ordenPago.isSPySF()? "<Sin Proveedor>" : proveedor.getRazonSocialConCUIT();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		Label nroOP = new Label("nroOP",ordenPago.getNro());
		Label fechaOP = new Label("fechaOP",dateFormat.format(ordenPago.getFecha().getTime()));
		Label prov = new Label("prov", proveedorStr)  {
			private static final long serialVersionUID = 1L;
			@Override
		    protected void onComponentTag(final ComponentTag tag){
		        super.onComponentTag(tag);
		        if (ordenPago.isSPySF()) {
		        	tag.put("class", "text-warning");
		        } else {
		        	tag.put("class", "text-default");
		        }
		    }
		};
		
		Label evento = new Label("evento",ordenPago.getEvento().getNombreConCliente());
		
		Link<OrdenPago> editarEventoLink = new Link<OrdenPago>("editarEvento") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage(new EditarEventoOPPage(ordenPago));
			}
		};
		
		String planCuentaStr = ordenPago.getPlanCuenta()!=null? ordenPago.getPlanCuenta().getNombre() : "<Sin Plan Cuenta>";
		Label planCuenta = new Label("planCuenta", planCuentaStr);
		
		Link<OrdenPago> editarPlanCuentaLink = new Link<OrdenPago>("editarPlanCuenta") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage(new EditarPlanCuentaOPPage(ordenPago));
			}
		};
		
		Label concepto = new Label("concepto",ordenPago.getConcepto());
		Label observaciones = new Label("observaciones",ordenPago.getObservaciones());
		Label solicitadoPor = new Label("solicitadoPor",ordenPago.getUsuarioSolicitante().getNombreCompleto());
				
		WebMarkupContainer pagosContainer = new WebMarkupContainer("pagosContainer");
		addPagosList(pagosContainer);
		pagosContainer.setOutputMarkupPlaceholderTag(true);
		
		WebMarkupContainer facturasContainer = new WebMarkupContainer("facturasContainer");
		addFacturasList(facturasContainer);
		facturasContainer.setOutputMarkupPlaceholderTag(true);
		facturasContainer.setVisible(! ordenPago.isSPySF());
		
		Label sinFacturaLbl = new Label("sinFactura","<Sin Factura>");
		sinFacturaLbl.setVisible(ordenPago.isSPySF());

		double totalFacturas = 0;
		for (FacturaCompra factura : ordenPago.getListadoFacturas()) {
			if ( ! factura.isNotaCredito() ) {
				totalFacturas += factura.calculateTotal();
			} else {
				totalFacturas -= factura.calculateTotal();
			}
		}
		double totalPagos = ordenPagoService.calculateTotalPagos(ordenPago.getListadoPagos());
		boolean isPagoParcial = ordenPago.getListadoFacturas().size()==1
				&& totalPagos>0 && Utils.round(totalFacturas, 2)>Utils.round(totalPagos, 2); //Logica para saber si pago parcial (buscar este comentario)
		String detallePagos =  isPagoParcial ? " (Pago Parcial) " : " (Pago Total) ";
		
		//Label totalFacturasLbl = new Label("totalFacturas", "$ " + Utils.round2Decimals(ordenPagoService.calculateTotalNetoFacturas(ordenPago.getListadoFacturas())));
		Label totalFacturasLbl = new Label("totalFacturas", "$ " + Utils.round2Decimals(totalFacturas, locale));
		Label totalAPagarLbl = new Label("totalAPagar", "$ " + Utils.round2Decimals(totalPagos, locale) + detallePagos);
		
		//Detalles Solicitud de Pago asociada
		WebMarkupContainer solicitudPagoContainer = crearDetallesSolicitudPago(dateFormat);
		Label sinSolicitudPagoLbl = new Label("sinSolicitudPago", "Esta orden de pago no contiene solicitud de pago asociada...");
		sinSolicitudPagoLbl.setVisible(ordenPago.getSolicitudPago()==null);
		
		//Para los casos de OP CPySF (Checkbox "Solo S/F y con Proveedor asignado" )
		Link<FacturaCompra> crearFacturaLink = new Link<FacturaCompra>("crearFactura") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				FacturaCompra factura = ordenPago.getListadoFacturas().get(0); //Obtengo la unica factura que tiene (y es ficticia)
				factura.setEstadoFactura(EstadoFacturaCompra.CANCELADA);
				factura.setNro(null);//Lo reseteo, ya que venia con algo asi: S/F-0002/15
				setResponsePage(new RegistrarFacturaPage(new PageParameters(),false,factura));
			}
		};
		crearFacturaLink.setVisible( ! ordenPago.isBorrado() 
				&& ordenPago.isCPySF()
				&& ordenPago.getListadoFacturas().size()==1 
				&&  ! isUsuarioLogueadoRolSoloLectura());
		
		//Para los casos de Facturas con Pago Parcial (Checkbox "Solo facturas Pagadas Parcial")
		Link<OrdenPago> crearOPLink = new Link<OrdenPago>("crearOP") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				//Seteo la info necesaria para poder ir al Crear OP Normal, pero directo a la pag 2 (Seleccionar Modo de Pago)
				OrdenPago ordenPagoNueva = new OrdenPago();
				ordenPagoNueva.setConcepto(ordenPago.getConcepto());
				ordenPagoNueva.setObservaciones(ordenPago.getObservaciones());
				ordenPagoNueva.setEvento(ordenPago.getEvento());
				ordenPagoNueva.setListadoFacturas(ordenPago.getListadoFacturas());
				ordenPagoNueva.setUsuarioSolicitante(ordenPago.getUsuarioSolicitante());
				((FacturarOnLineAuthenticatedWebSession) getSession()).setAttribute("op", ordenPagoNueva);
				((FacturarOnLineAuthenticatedWebSession) getSession()).setAttribute("rz", proveedor.getRazonSocial());
				((FacturarOnLineAuthenticatedWebSession) getSession()).setAttribute("parcial", 0d);
				
				setResponsePage(RegistrarOrdenPagoNormalPage2.class, new PageParameters());
			}
		};
		crearOPLink.setVisible( ! ordenPago.isBorrado() 
				&& isPagoParcial 
				&& ordenPago.getListadoFacturas().get(0).getEstadoFactura().equals(EstadoFacturaCompra.PAGADA_PARCIAL) //Permite crear una ultima op con el faltante de pago. -> sin tocar nada de esta logica funciona la nueva idea (1 fact compra puede tener mas de 2 ops parciales)
				&&  ! isUsuarioLogueadoRolSoloLectura());
		
		final ExcelGeneratorOP excelGenerator = new ExcelGeneratorOP(getRuntimeConfigurationType());
		final String excelName = excelGenerator.getFileName(ordenPago);
		final AJAXDownload download = createAjaxDownload(excelName);
		
		AjaxLink<OrdenPago> generarExcelLink = new AjaxLink<OrdenPago>("generarExcel") {			
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				//Genero siempre el archivo. Caso OP CPySF, luego creo factura correspondiente, ahi la OP cambio (ya no es SF)
				String CBU = (proveedor!=null && proveedor.getCuentaBancaria()!=null)?proveedor.getCuentaBancaria().getCbu():null;
				String alias = (proveedor!=null && proveedor.getCuentaBancaria()!=null)?proveedor.getCuentaBancaria().getAlias():null;
				excelGenerator.generarExcel(ordenPago, proveedorStr, CBU, alias, locale);
				download.initiate(target);
			}
		};
		generarExcelLink.setVisible( ! ordenPago.isBorrado() 
					&& ! isUsuarioLogueadoRolSoloLectura());
		
		this.add(nroOP);
		this.add(fechaOP);
		this.add(prov);
		this.add(evento);
		this.add(editarEventoLink);
		this.add(planCuenta);
		this.add(editarPlanCuentaLink);
		this.add(concepto);
		this.add(observaciones);
		this.add(solicitadoPor);
		this.add(pagosContainer);
		this.add(sinFacturaLbl);
		this.add(facturasContainer);
		this.add(totalFacturasLbl);
		this.add(totalAPagarLbl);
		this.add(solicitudPagoContainer);
		this.add(sinSolicitudPagoLbl);
		this.add(crearFacturaLink);
		this.add(crearOPLink);
		this.add(generarExcelLink);
		this.add(download);
	}

	private WebMarkupContainer crearDetallesSolicitudPago(DateFormat dateFormat) {
		SolicitudPago solicitudPago = ordenPago.getSolicitudPago();
		String detalleSolicitudPago = solicitudPago==null?"":"N° " + solicitudPago.getNro();
		WebMarkupContainer solicitudPagoContainer = new WebMarkupContainer("solicitudPagoContainer");
		Label detalleSolicitudPagoLbl = new Label("detalleSolicitudPago", detalleSolicitudPago);
		solicitudPagoContainer.add(detalleSolicitudPagoLbl);
		solicitudPagoContainer.add(new AjaxFallbackLink<SolicitudPago>("linkSolicitudPago") {					
			private static final long serialVersionUID = 1L;			
			@Override
			public void onClick(AjaxRequestTarget target) {
				SolicitudPagoService solicitudPagoService = new SolicitudPagoService();
				SolicitudPago solicitudPago = (SolicitudPago) solicitudPagoService.get(ordenPago.getSolicitudPago().getId());
				setResponsePage(new DetallesSolicitudesPagoPage(solicitudPago));
			}
		});
		solicitudPagoContainer.setVisible(solicitudPago!=null);
		return solicitudPagoContainer;
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
								+ " (Nro: " + pago.getNroCheque() + ", "
								+ "Fecha: " + dateFormat.format(pago.getFecha().getTime()) + ")" ; 
								
				}
				if (pago.isTransferencia()){
					tipoStr = "Transferencia";
					detalleStr += "$" + Utils.round2Decimals(pago.getImporte(), locale);
					detalleStr += (pago.getFecha()!=null ? " - Fecha: " + dateFormat.format(pago.getFecha().getTime()) : "");
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
				}
				if (pago.isTransferenciaSinProv()){
					tipoStr = "Transferencia Bancaria";
					detalleStr += "$" + Utils.round2Decimals(pago.getImporte(), locale) + " - ";
					detalleStr += Utils.generarDatosBancarios(pago.getCuitCuil(), pago.getCuentaBancaria());
					if (pago.getFecha()!=null) {
							detalleStr += " Fecha: " + dateFormat.format(pago.getFecha().getTime());
					}
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
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				String tipoStr = "";
				String detalleStr = "";
					tipoStr = factura.getTipoFactura().getNombre() + " - " + factura.getNro();
					double total;
					if (factura.getEstadoFactura().equals(EstadoFacturaCompra.PAGADA_PARCIAL)) {
						total = factura.calculateTotal();
					} else {
						total = ordenPagoService.calculateTotalNetoFactura(factura);
					}
					detalleStr += "$" + (factura.isNotaCredito()?"-":"") + Utils.round2Decimals(total, locale) + " - "
								+ factura.getEstadoFactura().getNombre() 
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
	
	private IDataProvider<Pago> getPagosProvider() {
		IDataProvider<Pago> pagosDataProvider = new IDataProvider<Pago>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void detach() {
			}

			@Override
			public Iterator<Pago> iterator(long first, long count) {
				List<Pago> returnList = ordenPago.getListadoPagos();
				Collections.sort(returnList, new Comparator<Pago>() {
	                @Override
	                public int compare(Pago pago1, Pago pago2) {
	                	if (pago1.isCheque()&&pago2.isCheque()) {
	                		if (pago1.getFecha()!=null && pago2.getFecha()!=null) {
		                		return pago1.getFecha().compareTo(pago2.getFecha());
		                	}
		                	return 1;
	                	} else if (pago1.isTransferencia()&&pago2.isTransferencia()) {
	                		if (pago1.getFecha()!=null && pago2.getFecha()!=null) {
		                		return pago1.getFecha().compareTo(pago2.getFecha());
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