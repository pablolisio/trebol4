package com.proit.vista.reportes.ventas;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.proit.modelo.auxiliar.ReporteChequeCartera;
import com.proit.servicios.ventas.CobranzaService;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class ChequesEnCartera extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	private CobranzaService cobranzaService;
	
	private Locale locale;
		
	public ChequesEnCartera(final PageParameters parameters) {
		super(parameters);
		
		cobranzaService = new CobranzaService();
		locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
		
		WebMarkupContainer container = new WebMarkupContainer("dataContainer");
		container.setOutputMarkupPlaceholderTag(true);
		add(container);
				
		addListado(container);
		
		container.add(new Label("total", "$" + Utils.round2Decimals(cobranzaService.getTotalChequesEnCartera(), locale)));
		
		facturarOnLineMenu.setearMenuActivo(false, false, false, true);
		
		mostrarAlertaEnPantallaSiCorresponde(parameters);
	}
	
	
	private void addListado(WebMarkupContainer container) {		
		Label noExistenCheques = new Label("noExistenCheques","Por el momento no existen cheques en cartera.");
		IDataProvider<ReporteChequeCartera> chequesCarteraDataProvider = getChequesCarteraDataProvider(noExistenCheques);
		
		DataView<ReporteChequeCartera> chequesCarteraDataView = new DataView<ReporteChequeCartera>("listaCheques", chequesCarteraDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<ReporteChequeCartera> item) {
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				ReporteChequeCartera reporteChequeCartera = item.getModelObject();
				String razonSocialCliente = reporteChequeCartera.getRazonSocialCliente();
				String bco = reporteChequeCartera.getBanco();
				String nro = reporteChequeCartera.getNroCheque();
				String fecha = dateFormat.format(reporteChequeCartera.getFecha().getTime());
				double importe = reporteChequeCartera.getImporte().doubleValue();
				
				Label clienteLabel = new Label("cliente", razonSocialCliente);
				Label bcoLabel = new Label("banco", bco);
				Label nroLabel = new Label("nro", nro);
				Label fechaLabel = new Label("fecha", fecha);				
				Label importeLabel = new Label("importe", "$" + Utils.round2Decimals(importe, locale));
								
				item.add(clienteLabel);
				item.add(bcoLabel);
				item.add(nroLabel);
				item.add(fechaLabel);
				item.add(importeLabel);
			}
		};
				
		container.add(chequesCarteraDataView);
		container.add(noExistenCheques);
	}
	
	private IDataProvider<ReporteChequeCartera> getChequesCarteraDataProvider(final Label noExistenCheques) {
		IDataProvider<ReporteChequeCartera> chequesCarteraDataProvider = new IDataProvider<ReporteChequeCartera>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void detach() {
			}

			@Override
			public Iterator<ReporteChequeCartera> iterator(long first, long count) {
				List<Object[]> chequesCartera = cobranzaService.getChequesEnCartera();
				List<ReporteChequeCartera> listadoReporteChequeCartera = new ArrayList<ReporteChequeCartera>();
				for (Object[] obj : chequesCartera) {
					Calendar fecha = Calendar.getInstance();
					fecha.setTimeInMillis(((Timestamp) obj[3]).getTime());
					ReporteChequeCartera reporteChequeCartera = new ReporteChequeCartera();
					reporteChequeCartera.setRazonSocialCliente((String) obj[0]);
					reporteChequeCartera.setBanco((String) obj[1]);
					reporteChequeCartera.setNroCheque((String) obj[2]);
					reporteChequeCartera.setFecha(fecha);
					reporteChequeCartera.setImporte((BigDecimal) obj[4]);
					listadoReporteChequeCartera.add(reporteChequeCartera);
				}
				return listadoReporteChequeCartera.iterator();
			}

			@Override
			public long size() {
				long size = cobranzaService.getChequesEnCarteraSize();
				noExistenCheques.setVisible(size==0);
				return size;
			}

			@Override
			public IModel<ReporteChequeCartera> model(ReporteChequeCartera reporteChequeCartera) {
				return new Model<ReporteChequeCartera>(reporteChequeCartera);
			}
        	
        };
		return chequesCarteraDataProvider;
	}

}
