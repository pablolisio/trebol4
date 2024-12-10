package com.proit.vista.reportes.compras;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.proit.modelo.auxiliar.ReporteTotalProveedor;
import com.proit.servicios.compras.FacturaCompraService;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;

@AuthorizeInstantiation({"Administrador","Solo Lectura","Desarrollador"})
public class TotalesProveedorPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	private FacturaCompraService facturaService;
	
	private Locale locale;
		
	public TotalesProveedorPage(final PageParameters parameters) {
		super(parameters);
		
		facturaService = new FacturaCompraService();
		
		locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
		
		WebMarkupContainer container = new WebMarkupContainer("dataContainer");
		container.setOutputMarkupPlaceholderTag(true);
		add(container);
				
		addProveedoresList(container);
		
		facturarOnLineMenu.setearMenuActivo(false, false, false, true);
		
		mostrarAlertaEnPantallaSiCorresponde(parameters);
	}
	
	
	private void addProveedoresList(WebMarkupContainer container) {		
		IDataProvider<ReporteTotalProveedor> totalesProveedoresDataProvider = getTotalesProveedoresDataProvider();
		
		DataView<ReporteTotalProveedor> totalesProveedorDataView = new DataView<ReporteTotalProveedor>("listaProveedores", totalesProveedoresDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<ReporteTotalProveedor> item) {
				ReporteTotalProveedor reporteTotalProveedor = item.getModelObject();
				double totalDeuda = reporteTotalProveedor.getTotalDeuda().doubleValue();
				double totalPagado = reporteTotalProveedor.getTotalPagado().doubleValue();
				
				Label proveedorLabel = new Label("proveedor", reporteTotalProveedor.getRazonSocialConCUIT());
				Label totalDeudaLabel = new Label("totalDeuda", "$" + Utils.round2Decimals(totalDeuda, locale));
				Label totalPagadoLabel = new Label("totalPagado", "$" + Utils.round2Decimals(totalPagado, locale));
								
				item.add(proveedorLabel);
				item.add(totalDeudaLabel);
				item.add(totalPagadoLabel);
			}
		};
		
		double sumaTotalDeuda = facturaService.calculateSumReporteTotalesPorProveedor("total_deuda");
		double sumaTotalPagado = facturaService.calculateSumReporteTotalesPorProveedor("total_pagado");
		
		container.add(new Label("sumaTotalDeuda", "$" + Utils.round2Decimals(sumaTotalDeuda, locale)));
		container.add(new Label("sumaTotalPagado", "$" + Utils.round2Decimals(sumaTotalPagado, locale)));
		
		container.add(totalesProveedorDataView);
	}
	
	private IDataProvider<ReporteTotalProveedor> getTotalesProveedoresDataProvider() {
		IDataProvider<ReporteTotalProveedor> totalesProveedoresDataProvider = new IDataProvider<ReporteTotalProveedor>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void detach() {
			}

			@Override
			public Iterator<ReporteTotalProveedor> iterator(long first, long count) {
				List<Object[]> totalesPorProveedor = facturaService.getTotalesPorProveedor();
				List<ReporteTotalProveedor> listadoReporteTotalProveedor = new ArrayList<ReporteTotalProveedor>();
				for (Object[] obj : totalesPorProveedor) {
					ReporteTotalProveedor reporteTotalProveedor = new ReporteTotalProveedor();
					reporteTotalProveedor.setProveedorId((Integer) obj[0]);
					reporteTotalProveedor.setRazonSocial((String) obj[1]);
					reporteTotalProveedor.setCuitCuil((String) obj[2]);
					reporteTotalProveedor.setTotalDeuda((BigDecimal) obj[3]);
					reporteTotalProveedor.setTotalPagado((BigDecimal) obj[4]);
					reporteTotalProveedor.setTotalFacturas((BigDecimal) obj[5]);
					listadoReporteTotalProveedor.add(reporteTotalProveedor);
				}
				return listadoReporteTotalProveedor.iterator();
			}

			@Override
			public long size() {
				return facturaService.getTotalesPorProveedorSize();
			}

			@Override
			public IModel<ReporteTotalProveedor> model(ReporteTotalProveedor reporteTotalProveedor) {
				return new Model<ReporteTotalProveedor>(reporteTotalProveedor);
			}
        	
        };
		return totalesProveedoresDataProvider;
	}

}
