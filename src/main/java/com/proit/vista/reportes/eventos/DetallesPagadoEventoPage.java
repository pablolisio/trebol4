package com.proit.vista.reportes.eventos;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.Model;

import com.proit.modelo.auxiliar.ReporteTotalEvento;
import com.proit.modelo.compras.FacturaCompra;
import com.proit.modelo.compras.FacturaCompraOrdenPago;
import com.proit.modelo.compras.OrdenPago;
import com.proit.modelo.compras.Proveedor;
import com.proit.servicios.compras.OrdenPagoService;
import com.proit.utils.Constantes;
import com.proit.utils.ExcelGeneratorEventoDetallePagos;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.AJAXDownload;
import com.proit.wicket.dataproviders.FacturaOrdenPagoDataProvider;

@AuthorizeInstantiation({"Administrador","Desarrollador", "Solicitante Pagos", "Solicitante Facturas Ventas","Editor Solicitudes Factura"})
public class DetallesPagadoEventoPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	private OrdenPagoService ordenPagoService;
	
	private Locale locale;
	
	public DetallesPagadoEventoPage(ReporteTotalEvento reporteTotalEvento) {
		locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
		ordenPagoService = new OrdenPagoService();
		
		final WebMarkupContainer dataContainer = new WebMarkupContainer("dataContainer");
		dataContainer.setOutputMarkupPlaceholderTag(true);
		add(dataContainer);
		
		armarCuadroEncabezado(reporteTotalEvento);
		
		agregarBotonExportarReporte(reporteTotalEvento);
		
		DataView<FacturaCompraOrdenPago> dataView = addOrdenPagoList(dataContainer, reporteTotalEvento.getIdEvento(), reporteTotalEvento.getPagadoTotal().doubleValue());
		dataContainer.add(dataView);
		
		facturarOnLineMenu.setearMenuActivo(false, false, false, true);		
	}
	
	private void agregarBotonExportarReporte(final ReporteTotalEvento reporteTotalEvento) {
		String nombreEvento = Utils.concatEventAndClient(reporteTotalEvento.getEvento(), reporteTotalEvento.getCliente());
		final ExcelGeneratorEventoDetallePagos excelGenerator = new ExcelGeneratorEventoDetallePagos(getRuntimeConfigurationType(), nombreEvento);
		final AJAXDownload download = createAjaxDownload(excelGenerator.getFileName());
		AjaxLink<FacturaCompraOrdenPago> generarExcelLink = new AjaxLink<FacturaCompraOrdenPago>("generarExcel") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				ArrayList<FacturaCompraOrdenPago> listaFacturasCompraOrdenPago =  (ArrayList<FacturaCompraOrdenPago>) ordenPagoService.getListaFacturaOrdenPago(null, null, reporteTotalEvento.getIdEvento(), false, false, false, null, null, null);
				excelGenerator.generarExcel(listaFacturasCompraOrdenPago, reporteTotalEvento);
				download.initiate(target);
			}
		};
		add(generarExcelLink);
		add(download);
	}
	
	private void armarCuadroEncabezado(ReporteTotalEvento reporteTotalEvento) {
		double totalEvento = reporteTotalEvento.getTotalEvento().doubleValue();
		double costoTotal = reporteTotalEvento.getCostoTotal().doubleValue();
		double costoTotalConIVA = reporteTotalEvento.getCostoTotalConIVA().doubleValue();
		double pagadoTotal = reporteTotalEvento.getPagadoTotal().doubleValue();
		double pendienteTotal = reporteTotalEvento.getPendienteTotal().doubleValue();
		
		Label eventoLbl = new Label("evento", Utils.concatEventAndClient(reporteTotalEvento.getEvento(), reporteTotalEvento.getCliente()));
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Label fechaLbl = new Label("fecha", reporteTotalEvento.getFecha()!=null?dateFormat.format(reporteTotalEvento.getFecha().getTime()):"-");
		Label costoFinalLbl = new Label("costoFinal", reporteTotalEvento.isCostoFinal()?" final":" -");
		final Boolean cerrado = reporteTotalEvento.isCerrado();
		Label cerradoLbl = new Label("cerrado", cerrado?" Cerrado":" Abierto"){
			private static final long serialVersionUID = 1L;
			@Override
		    protected void onComponentTag(final ComponentTag tag){
				super.onComponentTag(tag);
		        if (cerrado) {
			        tag.put("class", "text-danger");
		        } else {
		        	tag.put("class", "text-success");
		        }
			}
		};
		Label totalEventoLabel = new Label("totalEvento", "$" + Utils.round2Decimals(totalEvento, locale));
		Label costoTotalLabel = new Label("costoTotal", "$" + Utils.round2Decimals(costoTotal, locale));
		Label costoTotalConIVALabel = new Label("costoTotalConIVA", "$" + Utils.round2Decimals(costoTotalConIVA, locale));
		Label pagadoTotalLabel = new Label("pagadoTotal", "$" + Utils.round2Decimals(pagadoTotal, locale));
		Label pendienteTotalLabel = new Label("pendienteTotal", "$" + Utils.round2Decimals(pendienteTotal, locale));
		
		add(eventoLbl);
		add(fechaLbl);
		add(cerradoLbl);
		add(costoFinalLbl);
		add(totalEventoLabel);
		add(costoTotalLabel);
		add(costoTotalConIVALabel);
		add(pagadoTotalLabel);
		add(pendienteTotalLabel);
	}
	
	private DataView<FacturaCompraOrdenPago> addOrdenPagoList(WebMarkupContainer container, int idEvento, double pagadoTotal) {
		IDataProvider<FacturaCompraOrdenPago> facturaOrdenPagoDataProvider = getFacturaOrdenPagoProvider(idEvento);
		
		DataView<FacturaCompraOrdenPago> dataView = new DataView<FacturaCompraOrdenPago>("listaFacturaOrdenPago", facturaOrdenPagoDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<FacturaCompraOrdenPago> item) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				OrdenPago ordenPago = item.getModelObject().getOrdenPago();
				String proveedorStr;
				//Si no tiene facturas, es porque la OP es Sin Proveedor y Sin Factura.
				if (ordenPago.getListadoFacturas().isEmpty()) {
					proveedorStr = "<Sin Proveedor>";
				} else { //Es OPNormal o OPConProveedorYSinFactura. Ambas tienen como minimo una factura
					Proveedor proveedor= ordenPago.getListadoFacturas().get(0).getProveedor(); //Agarro la primera
					proveedorStr = proveedor.getRazonSocialConCUIT();
				}
				
				item.add(new Label("proveedor", proveedorStr));
				item.add(new Label("nroOP", ordenPago.getNro()));
				item.add(new Label("fechaOP", dateFormat.format(ordenPago.getFecha().getTime())));
				Label totalAPagarLbl = new Label("totalAPagar", "$ " + Utils.round2Decimals(ordenPagoService.calculateTotalPagos(ordenPago.getListadoPagos()), locale ));
				item.add(totalAPagarLbl);
				item.add(new Label("modoPago", ordenPago.getModosPagosElegidos()));
				
				item.add(new Label("concepto", ordenPago.getConcepto()));
				
				String facturasAsociadas = "";
				for (FacturaCompra factura : ordenPago.getListadoFacturas()) {
					if (factura.getNro().startsWith(Constantes.PREFIX_NRO_FACTURA_SF)){
						facturasAsociadas = "<Sin Factura>";
					} else {
						String textoFactura = factura.getTipoFactura().getNombreCorto() + factura.getNro();
						facturasAsociadas += facturasAsociadas.isEmpty() ? textoFactura : ", " +textoFactura;
					}
				}
				facturasAsociadas = facturasAsociadas.isEmpty()?"<Sin Factura>":facturasAsociadas;
				
				item.add(new Label("facturasAsociadas", facturasAsociadas));

			}
		};
		
		Label sumaPagadoTotalLabel = new Label("sumaPagadoTotal", "$" + Utils.round2Decimals(pagadoTotal, locale));
		container.add(sumaPagadoTotalLabel);
		container.add(dataView);
		return dataView;
	}
	
	private IDataProvider<FacturaCompraOrdenPago> getFacturaOrdenPagoProvider(int idEvento) {
		String prov = null;
		String nroOP = null;
		Date fecha = null;
		boolean miFalse = false;
		Integer idPlanCuenta = null;
		return new FacturaOrdenPagoDataProvider(Model.of(prov), Model.of(nroOP), Model.of(fecha), Model.of(idEvento), Model.of(miFalse), Model.of(miFalse), Model.of(miFalse), null, Model.of(idPlanCuenta));
	}
	
}