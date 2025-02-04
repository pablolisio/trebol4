package com.proit.vista.reportes.ventas;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.proit.modelo.Usuario;
import com.proit.modelo.ventas.SolicitudFacturaVenta;
import com.proit.servicios.ventas.SolicitudFacturaVentaService;
import com.proit.utils.Utils;
import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.dataproviders.SolicitudesFacturaVentaDataProvider;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class PendientesFacturacionPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	private SolicitudFacturaVentaService solicitudFacturaService;
	
	private TextField<String> eventoTextField;
	
	private Label sumaSubTotalLabel;
	private Label sumaIvaLabel;
	private Label sumaTotalesLabel;
	
	private Locale locale;
		
	public PendientesFacturacionPage(final PageParameters parameters) {
		super(parameters);
		
		solicitudFacturaService = new SolicitudFacturaVentaService();
		
		locale = ((FacturarOnLineAuthenticatedWebSession) getSession()).getLocale();
		
		WebMarkupContainer container = new WebMarkupContainer("dataContainer");
		container.setOutputMarkupPlaceholderTag(true);
		add(container);
		
		addFilters(container);
		
		addListado(container);
		
		facturarOnLineMenu.setearMenuActivo(false, false, false, true);
		
		mostrarAlertaEnPantallaSiCorresponde(parameters);
	}
	
	private void addFilters(final WebMarkupContainer container) {
		eventoTextField = new TextField<String>("filtroEvento", Model.of(""));
		eventoTextField.setOutputMarkupId(true);
		
		final AjaxButton filterButton = new AjaxButton("buscar") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit();
				actualizarContainer(container, target);
			}
		};
		
		AjaxLink<String> todosLosEventosLink = crearTodosEventosLink(container, "todosEventos");
		
		Form<?> form = new Form<Void>("form");
		add(form);
		form.add(eventoTextField);
		form.add(todosLosEventosLink);
		form.add(filterButton);
	}
		
	private void addListado(WebMarkupContainer container) {
		IDataProvider<SolicitudFacturaVenta> solicitudesDataProvider = getSolicitudesProvider();
		
		DataView<SolicitudFacturaVenta> solicitudesDataView = new DataView<SolicitudFacturaVenta>("listaSolicitudes", solicitudesDataProvider) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<SolicitudFacturaVenta> item) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				SolicitudFacturaVenta solicitud = item.getModelObject();
				item.add(new Label("fecha", dateFormat.format(solicitud.getFecha().getTime())));
				item.add(new Label("cliente", solicitud.getCliente().getRazonSocialConCUIT()));
				item.add(new Label("tipo", solicitud.getTipoFactura().getNombreCorto()));
				item.add(new Label("estado", solicitud.getEstadoSolicitudFactura().getNombre()));
				item.add(new Label("evento", solicitud.getEvento().getNombre()));
				
				double subtotalD = solicitud.calculateSubtotal();
				double ivaD = Utils.calcularImporteIVA(subtotalD, solicitud.getTipoFactura());
				double totalD = subtotalD + ivaD;
				String subtotal = solicitud.isNotaCredito() ? "$-" : "$";
				subtotal += Utils.round2Decimals(subtotalD, locale);
				String iva = solicitud.isNotaCredito() ? "$-" : "$";
				iva += Utils.round2Decimals(ivaD, locale);
				String total = solicitud.isNotaCredito() ? "$-" : "$";
				total += Utils.round2Decimals(totalD, locale);
				item.add(new Label("subtotal", subtotal));
				item.add(new Label("iva", iva));
				item.add(new Label("total", total));
			}
		};		
		sumaSubTotalLabel = new Label("sumaSubTotal");
		sumaIvaLabel = new Label("sumaIva");
		sumaTotalesLabel = new Label("sumaTotales");
		
		recalculateTotales();
		
		sumaSubTotalLabel.setOutputMarkupId(true);
		sumaIvaLabel.setOutputMarkupId(true);
		sumaTotalesLabel.setOutputMarkupId(true);

		container.add(sumaSubTotalLabel);
		container.add(sumaIvaLabel);
		container.add(sumaTotalesLabel);
				
		container.add(solicitudesDataView);
	}
	
	private IDataProvider<SolicitudFacturaVenta> getSolicitudesProvider() {
		Usuario solicitante = null;
		Integer idEvento = null;
		return new SolicitudesFacturaVentaDataProvider(eventoTextField.getModel(), Model.of(solicitante), Model.of(true), Model.of(false), Model.of(idEvento));
	}
	
	private AjaxLink<String> crearTodosEventosLink(final WebMarkupContainer container, String nombreLink) {
		AjaxLink<String> link = new AjaxLink<String>(nombreLink) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				eventoTextField.setModelObject(null);
				target.add(eventoTextField);
				actualizarContainer(container, target);
			}
		};
		return link;
	}
	
	private void recalculateTotales() {		
		double sumaSubTotal = solicitudFacturaService.calculateSumSubtotal(null, eventoTextField.getModelObject(), true, false);
		double sumaIva = solicitudFacturaService.calculateSumIVA(null, eventoTextField.getModelObject(), true);
		double sumaTotales = sumaSubTotal + sumaIva;
		
		sumaSubTotalLabel.setDefaultModel(Model.of("$" + Utils.round2Decimals(sumaSubTotal, locale)));
		sumaIvaLabel.setDefaultModel(Model.of("$" + Utils.round2Decimals(sumaIva, locale)));
		sumaTotalesLabel.setDefaultModel(Model.of("$" + Utils.round2Decimals(sumaTotales, locale)));
	}
	
	private void actualizarContainer(final WebMarkupContainer container, AjaxRequestTarget target) {
		recalculateTotales();
		target.add(container);
	}

}
