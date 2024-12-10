package com.proit.vista.reportes;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.proit.servicios.ReportesDuplicadosService;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;

@AuthorizeInstantiation({"Administrador", "Desarrollador"})
public class DuplicadosPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	private ReportesDuplicadosService reportesDuplicadosService;
	
	private Locale locale;
		
	public DuplicadosPage(final PageParameters parameters) {
		super(parameters);
		
		reportesDuplicadosService = new ReportesDuplicadosService();
		
		locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
				
		addListado("dataContainerSolicitudFacturaVenta", "SolicitudFacturaVenta", reportesDuplicadosService.getTitulosDuplicadosSolicitudFacturaVenta(), reportesDuplicadosService.getDuplicadosSolicitudFacturaVenta(), reportesDuplicadosService.getDuplicadosSolicitudFacturaVentaSize());
		addListado("dataContainerAdicionalSolicitudFacturaVenta", "AdicionalSolicitudFacturaVenta", reportesDuplicadosService.getTitulosDuplicadosAdicionalSolicitudFacturaVenta(), reportesDuplicadosService.getDuplicadosAdicionalSolicitudFacturaVenta(), reportesDuplicadosService.getDuplicadosAdicionalSolicitudFacturaVentaSize());
		addListado("dataContainerDetalleSolicitudFacturaVenta", "DetalleSolicitudFacturaVenta", reportesDuplicadosService.getTitulosDuplicadosDetalleSolicitudFacturaVenta(), reportesDuplicadosService.getDuplicadosDetalleSolicitudFacturaVenta(), reportesDuplicadosService.getDuplicadosDetalleSolicitudFacturaVentaSize());
		addListado("dataContainerFacturaVenta", "FacturaVenta", reportesDuplicadosService.getTitulosDuplicadosFacturaVenta(), reportesDuplicadosService.getDuplicadosFacturaVenta(), reportesDuplicadosService.getDuplicadosFacturaVentaSize());
		addListado("dataContainerAdicionalFacturaVenta", "AdicionalFacturaVenta", reportesDuplicadosService.getTitulosDuplicadosAdicionalFacturaVenta(), reportesDuplicadosService.getDuplicadosAdicionalFacturaVenta(), reportesDuplicadosService.getDuplicadosAdicionalFacturaVentaSize());
		addListado("dataContainerDetalleFacturaVenta", "DetalleFacturaVenta", reportesDuplicadosService.getTitulosDuplicadosDetalleFacturaVenta(), reportesDuplicadosService.getDuplicadosDetalleFacturaVenta(), reportesDuplicadosService.getDuplicadosDetalleFacturaVentaSize());
		addListado("dataContainerCobranza", "Cobranza", reportesDuplicadosService.getTitulosDuplicadosCobranza(), reportesDuplicadosService.getDuplicadosCobranza(), reportesDuplicadosService.getDuplicadosCobranzaSize());
		addListado("dataContainerCobro", "Cobro", reportesDuplicadosService.getTitulosDuplicadosCobro(), reportesDuplicadosService.getDuplicadosCobro(), reportesDuplicadosService.getDuplicadosCobroSize());
		addListado("dataContainerSolicitudPago", "SolicitudPago", reportesDuplicadosService.getTitulosDuplicadosSolicitudPago(), reportesDuplicadosService.getDuplicadosSolicitudPago(), reportesDuplicadosService.getDuplicadosSolicitudPagoSize());
		addListado("dataContainerPagoSolicitudPago", "PagoSolicitudPago", reportesDuplicadosService.getTitulosDuplicadosPagoSolicitudPago(), reportesDuplicadosService.getDuplicadosPagoSolicitudPago(), reportesDuplicadosService.getDuplicadosPagoSolicitudPagoSize());
		addListado("dataContainerFacturaSolicitudPago", "FacturaSolicitudPago", reportesDuplicadosService.getTitulosDuplicadosFacturaSolicitudPago(), reportesDuplicadosService.getDuplicadosFacturaSolicitudPago(), reportesDuplicadosService.getDuplicadosFacturaSolicitudPagoSize());
		addListado("dataContainerOrdenPago", "OrdenPago", reportesDuplicadosService.getTitulosDuplicadosOrdenPago(), reportesDuplicadosService.getDuplicadosOrdenPago(), reportesDuplicadosService.getDuplicadosOrdenPagoSize());
		addListado("dataContainerPago", "Pago", reportesDuplicadosService.getTitulosDuplicadosPago(), reportesDuplicadosService.getDuplicadosPago(), reportesDuplicadosService.getDuplicadosPagoSize());
		addListado("dataContainerEvento", "Evento", reportesDuplicadosService.getTitulosDuplicadosEvento(), reportesDuplicadosService.getDuplicadosEvento(), reportesDuplicadosService.getDuplicadosEventoSize());
		addListado("dataContainerFacturaCompra", "FacturaCompra", reportesDuplicadosService.getTitulosDuplicadosFacturaCompra(), reportesDuplicadosService.getDuplicadosFacturaCompra(), reportesDuplicadosService.getDuplicadosFacturaCompraSize());
		addListado("dataContainerUsuario", "Usuario", reportesDuplicadosService.getTitulosDuplicadosUsuario(), reportesDuplicadosService.getDuplicadosUsuario(), reportesDuplicadosService.getDuplicadosUsuarioSize());
		addListado("dataContainerCliente", "Cliente", reportesDuplicadosService.getTitulosDuplicadosCliente(), reportesDuplicadosService.getDuplicadosCliente(), reportesDuplicadosService.getDuplicadosClienteSize());
		addListado("dataContainerProveedor", "Proveedor", reportesDuplicadosService.getTitulosDuplicadosProveedor(), reportesDuplicadosService.getDuplicadosProveedor(), reportesDuplicadosService.getDuplicadosProveedorSize());
		addListado("dataContainerPlanCuenta", "PlanCuenta", reportesDuplicadosService.getTitulosDuplicadosPlanCuenta(), reportesDuplicadosService.getDuplicadosPlanCuenta(), reportesDuplicadosService.getDuplicadosPlanCuentaSize());
		addListado("dataContainerPresupuestoCustom", "PresupuestoCustom", reportesDuplicadosService.getTitulosDuplicadosPresupuestoCustom(), reportesDuplicadosService.getDuplicadosPresupuestoCustom(), reportesDuplicadosService.getDuplicadosPresupuestoCustomSize());
		addListado("dataContainerBanco", "Banco", reportesDuplicadosService.getTitulosDuplicadosBanco(), reportesDuplicadosService.getDuplicadosBanco(), reportesDuplicadosService.getDuplicadosBancoSize());
		addListado("dataContainerCuentaBancaria", "CuentaBancaria", reportesDuplicadosService.getTitulosDuplicadosCuentaBancaria(), reportesDuplicadosService.getDuplicadosCuentaBancaria(), reportesDuplicadosService.getDuplicadosCuentaBancariaSize());
		addListado("dataContainerCajaChica", "CajaChica", reportesDuplicadosService.getTitulosDuplicadosCajaChica(), reportesDuplicadosService.getDuplicadosCajaChica(), reportesDuplicadosService.getDuplicadosCajaChicaSize());

		
		facturarOnLineMenu.setearMenuActivo(false, false, false, true);
		
		mostrarAlertaEnPantallaSiCorresponde(parameters);
	}
		
	private void addListado(String containerName, String tableName, List<String> titulos, List<Object[]> valuesList, long valuesListSize) { 
		WebMarkupContainer container = new WebMarkupContainer(containerName);
		container.setOutputMarkupPlaceholderTag(true);
		add(container);
		container.setVisible(valuesListSize>0);
		
		IDataProvider<Object[]> dataProvider = getDuplicadosDataProvider(valuesList, valuesListSize);
		
		container.add(new Label("tableName", tableName));
		
		ListView<String> titulosListView = new ListView<String>("titulos", titulos) {
			private static final long serialVersionUID = 1L;
			protected void populateItem(ListItem<String> item) {
		        item.add(new Label("titulo", item.getModel()));
		    }
		};
		container.add(titulosListView);
		
		DataView<Object[]> duplicadosDataView = new DataView<Object[]>("listaDuplicados", dataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<Object[]> item) {
				Object[] reporteDuplicados = item.getModelObject();
				
				ListView<Object> listview = new ListView<Object>("td", Arrays.asList(reporteDuplicados)) {
					private static final long serialVersionUID = 1L;
					protected void populateItem(ListItem<Object> item) {
				        item.add(new Label("value", item.getModel()));
				    }
				};
				item.add(listview);
			}
		};
		
		container.add(duplicadosDataView);
	}
	
	private IDataProvider<Object[]> getDuplicadosDataProvider(List<Object[]> valuesList, long valuesListSize) {
		IDataProvider<Object[]> duplicadosDataProvider = new IDataProvider<Object[]>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void detach() {
			}

			@Override
			public Iterator<Object[]> iterator(long first, long count) {
				return valuesList.iterator();
			}

			@Override
			public long size() {
				return valuesListSize;
			}

			@Override
			public IModel<Object[]> model(Object[] obj) {
				return new Model<Object[]>(obj);
			}
        	
        };
		return duplicadosDataProvider;
	}

}
