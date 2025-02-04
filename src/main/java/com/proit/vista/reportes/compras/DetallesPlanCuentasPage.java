package com.proit.vista.reportes.compras;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.Model;

import com.proit.modelo.auxiliar.ReportePlanCuenta;
import com.proit.modelo.compras.FacturaCompra;
import com.proit.modelo.compras.FacturaCompraOrdenPago;
import com.proit.modelo.compras.OrdenPago;
import com.proit.modelo.compras.Proveedor;
import com.proit.servicios.compras.OrdenPagoService;
import com.proit.utils.Constantes;
import com.proit.utils.ExcelGeneratorPlanCuentasDetalle;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.AJAXDownload;
import com.proit.wicket.dataproviders.FacturaOrdenPagoDataProvider;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class DetallesPlanCuentasPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	private OrdenPagoService ordenPagoService;
	
	private Locale locale;
	
	public DetallesPlanCuentasPage(ReportePlanCuenta reportePlanCuenta, Calendar mes) {
		locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
		ordenPagoService = new OrdenPagoService();
		
		final WebMarkupContainer dataContainer = new WebMarkupContainer("dataContainer");
		dataContainer.setOutputMarkupPlaceholderTag(true);
		add(dataContainer);
		
		armarCuadroEncabezado(reportePlanCuenta, mes);
		
		agregarBotonExportarReporte(reportePlanCuenta, mes);
		
		DataView<FacturaCompraOrdenPago> dataView = addList(dataContainer, reportePlanCuenta.getIdPlanCuenta(), reportePlanCuenta.getTotal().doubleValue(), mes);
		dataContainer.add(dataView);
		
		facturarOnLineMenu.setearMenuActivo(false, false, false, true);		
	}
	
	private void agregarBotonExportarReporte(final ReportePlanCuenta reportePlanCuenta, Calendar mes) {
		final ExcelGeneratorPlanCuentasDetalle excelGenerator = new ExcelGeneratorPlanCuentasDetalle(getRuntimeConfigurationType(), reportePlanCuenta.getNombrePlanCuenta());
		final AJAXDownload download = createAjaxDownload(excelGenerator.getFileName());
		AjaxLink<FacturaCompraOrdenPago> generarExcelLink = new AjaxLink<FacturaCompraOrdenPago>("generarExcel") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				Calendar mesInicio = null;
				Calendar mesFin = null;
				if (mes!=null) {
					mesInicio = mes;
					mesFin = (Calendar) mes.clone();
					mesFin.add(Calendar.MONTH, 1);
				}
				ArrayList<FacturaCompraOrdenPago> listaFacturasCompraOrdenPago =  (ArrayList<FacturaCompraOrdenPago>) ordenPagoService.getListaFacturaOrdenPago(null, null, null, false, false, false, mesInicio, mesFin, reportePlanCuenta.getIdPlanCuenta());
				excelGenerator.generarExcel(listaFacturasCompraOrdenPago, reportePlanCuenta, mes);
				download.initiate(target);
			}
		};
		add(generarExcelLink);
		add(download);
	}
	
	private void armarCuadroEncabezado(ReportePlanCuenta reportePlanCuenta, Calendar mes) {
		DateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
		Label planCuentaLbl = new Label("planCuenta", reportePlanCuenta.getNombrePlanCuenta());
		Label totalLabel = new Label("total", "$" + Utils.round2Decimals(reportePlanCuenta.getTotal().doubleValue(), locale));
		Label mesLabel = new Label("mes", dateFormat.format(mes.getTime()));
		add(planCuentaLbl);
		add(totalLabel);
		add(mesLabel);
	}
	
	private DataView<FacturaCompraOrdenPago> addList(WebMarkupContainer container, int idPlanCuenta, double total, Calendar mes) {
		IDataProvider<FacturaCompraOrdenPago> facturaOrdenPagoDataProvider = getFacturaOrdenPagoProvider(idPlanCuenta, mes);
		
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
		
		Label sumaPagadoTotalLabel = new Label("sumaPagadoTotal", "$" + Utils.round2Decimals(total, locale));
		container.add(sumaPagadoTotalLabel);
		container.add(dataView);
		return dataView;
	}
	
	private IDataProvider<FacturaCompraOrdenPago> getFacturaOrdenPagoProvider(int idPlanCuenta, Calendar mes) {
		String prov = null;
		String nroOP = null;
		Date fecha = null;
		boolean miFalse = false;
		Integer idEvento = null;
		return new FacturaOrdenPagoDataProvider(Model.of(prov), Model.of(nroOP), Model.of(fecha), Model.of(idEvento), Model.of(miFalse), Model.of(miFalse), Model.of(miFalse), mes, Model.of(idPlanCuenta));
	}
	
}