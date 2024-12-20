package com.proit.vista.base;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.proit.modelo.Usuario;
import com.proit.vista.login.LoginPage;
import com.proit.vista.users.RegistrarUsuarioPage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;

public class FacturarOnLineMenuPanel extends Panel{

	private static final long serialVersionUID = 1L;
	
	//MENU
	private Link<WebPage> inicioLink;
	private WebMarkupContainer gestionDropDown;
	private WebMarkupContainer configuracionDropDown;	
	private WebMarkupContainer reportesDropDown;
	
	//CONFIGURACION
	private Link<WebPage> editarPerfilLink;
	private WebMarkupContainer cambiarClaveLink;
	private WebMarkupContainer usuariosLink;
	private WebMarkupContainer dividerConfiguracionAvanzada;
	private WebMarkupContainer headerConfiguracionAvanzada;
	private WebMarkupContainer cargaHorasLink;
	private WebMarkupContainer configLink;
	private WebMarkupContainer bancosLink;
	
	//GESTION
	private WebMarkupContainer headerGestionCompras;
	
	private WebMarkupContainer proveedoresLink;
	private WebMarkupContainer facturasComprasLink;
	private WebMarkupContainer ordenesPagoLink;
	private WebMarkupContainer solicitudesPagoLink;
	private WebMarkupContainer misSolicitudesPagoLink;
	private WebMarkupContainer planesCuentaLink;
	
	private WebMarkupContainer divider1;
	private WebMarkupContainer cajaChicaLink;
	private WebMarkupContainer presupuestoBancoLink;
	
	private WebMarkupContainer dividerEventos;
	private WebMarkupContainer eventosLink;
	
	private WebMarkupContainer dividerGestionVentas;
	private WebMarkupContainer headerGestionVentas;
	
	private WebMarkupContainer clientesLink;
	private WebMarkupContainer facturasVentasLink;
	private WebMarkupContainer cobranzasLink;
	private WebMarkupContainer solicitudesFacVtaLink;
	private WebMarkupContainer misSolicitudesFacVtaLink;


	//REPORTES
	private WebMarkupContainer headerReportesCompras;
	
	private WebMarkupContainer subdiarioComprasLink;
//	private WebMarkupContainer deudasVsPagosLink;
	private WebMarkupContainer totalesProveedorLink;
	private WebMarkupContainer tarjetasCreditoLink;
//	private WebMarkupContainer detalleCajaChicaLink;
	private WebMarkupContainer reporteOPsLink;
	private WebMarkupContainer reportePlanCuentasLink;
	
	private WebMarkupContainer dividerEventos2;
	private WebMarkupContainer totalesEventoLink;
	
	private WebMarkupContainer divider2;
	private WebMarkupContainer posicionLink;
	
	private WebMarkupContainer dividerReportesVentas;
	private WebMarkupContainer headerReportesVentas;
	
	private WebMarkupContainer subdiarioVentasLink;
	private WebMarkupContainer pendientesFactLink;
	private WebMarkupContainer deudasXClienteLink;
	private WebMarkupContainer ventasMensualXClienteLink;
	private WebMarkupContainer chequesCarteraLink;
	
	private WebMarkupContainer divider3;
	private WebMarkupContainer duplicadosLink;
	
	
	public FacturarOnLineMenuPanel(String id) {
		super(id);
		
		Usuario usuario = ((FacturarOnLineAuthenticatedWebSession) getSession()).getUsuario();
		
		Link<WebPage> signOutLink = new Link<WebPage>("signOutLink") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				getSession().invalidate();
				setResponsePage(LoginPage.class);
			}
		};
		
		inicioLink = new Link<WebPage>("inicioLink") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
					setResponsePage(FacturarOnLinePage.class);
			}
		};
		
		gestionDropDown = crearGestionDropDown();
		
		configuracionDropDown = crearConfigurationDropDown();
	
		reportesDropDown = crearReportesDropDown();
		
		Label usuarioLogueado = new Label("usuarioLogueado",usuario == null ? "Cerrar Sesión" :usuario.getEmail());
		signOutLink.add(usuarioLogueado);
		
		add(inicioLink);
		add(gestionDropDown);
		add(configuracionDropDown);
		add(reportesDropDown);
		add(signOutLink);
		
		setVisibilities();
	}

	private WebMarkupContainer crearGestionDropDown() {
		WebMarkupContainer gestionDropDown = new WebMarkupContainer("gestionDropDown");
		headerGestionCompras = new WebMarkupContainer("headerGestionCompras");
		proveedoresLink = new WebMarkupContainer("proveedoresLink");
		facturasComprasLink = new WebMarkupContainer("facturasComprasLink");
		ordenesPagoLink = new WebMarkupContainer("ordenesPagoLink");
		solicitudesPagoLink = new WebMarkupContainer("solicitudesPagoLink");
		misSolicitudesPagoLink = new WebMarkupContainer("misSolicitudesPagoLink");
		planesCuentaLink = new WebMarkupContainer("planesCuentaLink");
		
		divider1 = new WebMarkupContainer("divider1");
		cajaChicaLink = new WebMarkupContainer("cajaChicaLink");
		presupuestoBancoLink  = new WebMarkupContainer("presupuestoBancoLink");
		
		dividerEventos = new WebMarkupContainer("dividerEventos");
		eventosLink = new WebMarkupContainer("eventosLink");
		
		dividerGestionVentas = new WebMarkupContainer("dividerGestionVentas");
		headerGestionVentas = new WebMarkupContainer("headerGestionVentas");
		clientesLink = new WebMarkupContainer("clientesLink");
		facturasVentasLink = new WebMarkupContainer("facturasVentasLink");
		cobranzasLink = new WebMarkupContainer("cobranzasLink");
		solicitudesFacVtaLink = new WebMarkupContainer("solicitudesFacVtaLink");
		misSolicitudesFacVtaLink = new WebMarkupContainer("misSolicitudesFacVtaLink");
		
		gestionDropDown.add(headerGestionCompras);
		gestionDropDown.add(proveedoresLink);
		gestionDropDown.add(facturasComprasLink);
		gestionDropDown.add(ordenesPagoLink);
		gestionDropDown.add(solicitudesPagoLink);
		gestionDropDown.add(misSolicitudesPagoLink);
		gestionDropDown.add(planesCuentaLink);
		gestionDropDown.add(divider1);
		gestionDropDown.add(cajaChicaLink);
		gestionDropDown.add(presupuestoBancoLink);
		gestionDropDown.add(dividerEventos);
		gestionDropDown.add(eventosLink);
		gestionDropDown.add(dividerGestionVentas);
		gestionDropDown.add(headerGestionVentas);
		gestionDropDown.add(clientesLink);
		gestionDropDown.add(facturasVentasLink);
		gestionDropDown.add(cobranzasLink);
		gestionDropDown.add(solicitudesFacVtaLink);
		gestionDropDown.add(misSolicitudesFacVtaLink);
		return gestionDropDown;
	}
	
	private WebMarkupContainer crearReportesDropDown() {
		reportesDropDown = new WebMarkupContainer("reportesDropDown");
		headerReportesCompras = new WebMarkupContainer("headerReportesCompras");
		subdiarioComprasLink = new WebMarkupContainer("subdiarioComprasLink");
//		deudasVsPagosLink = new WebMarkupContainer("deudasVsPagosLink");
		totalesProveedorLink = new WebMarkupContainer("totalesProveedorLink");
		tarjetasCreditoLink = new WebMarkupContainer("tarjetasCreditoLink");
//		detalleCajaChicaLink = new WebMarkupContainer("detalleCajaChicaLink");
		reporteOPsLink = new WebMarkupContainer("reporteOPsLink");
		reportePlanCuentasLink = new WebMarkupContainer("reportePlanCuentasLink");
		dividerEventos2 = new WebMarkupContainer("dividerEventos2");
		totalesEventoLink = new WebMarkupContainer("totalesEventoLink");
		divider2 = new WebMarkupContainer("divider2");
		posicionLink = new WebMarkupContainer("posicionLink");
		dividerReportesVentas = new WebMarkupContainer("dividerReportesVentas");
		headerReportesVentas = new WebMarkupContainer("headerReportesVentas");		
		subdiarioVentasLink = new WebMarkupContainer("subdiarioVentasLink");
		pendientesFactLink = new WebMarkupContainer("pendientesFactLink");
		deudasXClienteLink = new WebMarkupContainer("deudasXClienteLink");
		ventasMensualXClienteLink = new WebMarkupContainer("ventasMensualXClienteLink");
		chequesCarteraLink = new WebMarkupContainer("chequesCarteraLink");	
		divider3 = new WebMarkupContainer("divider3");
		duplicadosLink = new WebMarkupContainer("duplicadosLink");
		reportesDropDown.add(headerReportesCompras);
		reportesDropDown.add(subdiarioComprasLink);
//		reportesDropDown.add(deudasVsPagosLink);
		reportesDropDown.add(totalesProveedorLink);
		reportesDropDown.add(tarjetasCreditoLink);
//		reportesDropDown.add(detalleCajaChicaLink);
		reportesDropDown.add(reporteOPsLink);
		reportesDropDown.add(reportePlanCuentasLink);
		reportesDropDown.add(dividerEventos2);
		reportesDropDown.add(totalesEventoLink);
		reportesDropDown.add(divider2);
		reportesDropDown.add(posicionLink);
		reportesDropDown.add(dividerReportesVentas);
		reportesDropDown.add(headerReportesVentas);		
		reportesDropDown.add(subdiarioVentasLink);
		reportesDropDown.add(pendientesFactLink);
		reportesDropDown.add(deudasXClienteLink);
		reportesDropDown.add(ventasMensualXClienteLink);
		reportesDropDown.add(chequesCarteraLink);
		reportesDropDown.add(divider3);
		reportesDropDown.add(duplicadosLink);
		return reportesDropDown;
	}
	
	private WebMarkupContainer crearConfigurationDropDown() {
		WebMarkupContainer configuracionDropDown = new WebMarkupContainer("configuracionDropDown");
		editarPerfilLink = new Link<WebPage>("editarPerfilLink") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage(new RegistrarUsuarioPage(new PageParameters(),new Integer(((FacturarOnLineAuthenticatedWebSession) getSession()).getUsuario().getId())));
			}
		};
		cambiarClaveLink = new WebMarkupContainer("cambiarClaveLink");
		usuariosLink = new WebMarkupContainer("usuariosLink");
		dividerConfiguracionAvanzada = new WebMarkupContainer("dividerConfiguracionAvanzada");
		headerConfiguracionAvanzada = new WebMarkupContainer("headerConfiguracionAvanzada");
		cargaHorasLink = new WebMarkupContainer("cargaHorasLink");
		configLink = new WebMarkupContainer("configLink");
		bancosLink = new WebMarkupContainer("bancosLink");		
		configuracionDropDown.add(editarPerfilLink);
		configuracionDropDown.add(cambiarClaveLink);
		configuracionDropDown.add(usuariosLink);
		configuracionDropDown.add(dividerConfiguracionAvanzada);
		configuracionDropDown.add(headerConfiguracionAvanzada);
		configuracionDropDown.add(cargaHorasLink);
		configuracionDropDown.add(configLink);
		configuracionDropDown.add(bancosLink);
		return configuracionDropDown;
	}

	public void setearMenuActivo(boolean inicioActivo, boolean gestionActivo, boolean configuracionActivo,
			boolean reportesActivo){
		setComponentClass(inicioLink, inicioActivo);
		setComponentClass(gestionDropDown, gestionActivo);
		setComponentClass(configuracionDropDown, configuracionActivo);
		setComponentClass(reportesDropDown, reportesActivo);
	}
	
	private void setComponentClass(WebMarkupContainer component, final boolean hasToBeActive) {
		final boolean isLink = (component instanceof Link);
		
		AttributeModifier linkClass = new AttributeModifier("class", new AbstractReadOnlyModel<String>() {

	        private static final long serialVersionUID= 1L;

	        @Override
	        public String getObject () {
	        	String classString = "";
	        	if (hasToBeActive) {
	        		classString += "active ";
	        	}
	        	if (!isLink) {
	        		classString += "dropdown";
	        	}
	        	return classString;
	        }
	    });
		component.add(linkClass);
	}
	
	private void setVisibilities() {
		if ( ((FacturarOnLineAuthenticatedWebSession) getSession()).getUsuario() != null) {
			boolean isRolAdministrador =	((FacturarOnLineAuthenticatedWebSession) getSession()).isRolAdministrador();
			boolean isRolSoloLectura = ((FacturarOnLineAuthenticatedWebSession) getSession()).isRolSoloLectura();
			boolean isRolDesarrollador = ((FacturarOnLineAuthenticatedWebSession) getSession()).isRolDesarrollador();
			boolean isRolSolicitantePagos = ((FacturarOnLineAuthenticatedWebSession) getSession()).isRolSolicitantePagos();
			boolean isRolSolicitanteFacturasVentas = ((FacturarOnLineAuthenticatedWebSession) getSession()).isRolSolicitanteFacturasVentas();
			boolean isRolEditorSolicitudesFactura = ((FacturarOnLineAuthenticatedWebSession) getSession()).isRolEditorSolicitudesFactura();

			//Gestion Compras
			boolean proveedoresLinkVisible = 			isRolDesarrollador || isRolAdministrador || isRolSoloLectura;
			boolean facturasComprasLinkVisible = 		isRolDesarrollador || isRolAdministrador || isRolSoloLectura;
			boolean ordenesPagoLinkVisible = 			isRolDesarrollador || isRolAdministrador || isRolSoloLectura;
			boolean solicitudesPagoLinkVisible = 		isRolDesarrollador || isRolAdministrador || isRolEditorSolicitudesFactura;
			boolean misSolicitudesPagoLinkVisible = 	isRolDesarrollador || isRolSolicitantePagos;
			boolean planesCuentaLinkVisible = 			isRolDesarrollador || isRolAdministrador;
			
			boolean cajaChicaLinkVisible = 				isRolDesarrollador || isRolAdministrador;
			boolean presupuestoBancoLinkVisible = 		isRolDesarrollador || isRolAdministrador;
			
			//boolean eventosLinkVisible = 				true;	//todos pueden
			boolean eventosLinkVisible = 				isRolDesarrollador || isRolSolicitanteFacturasVentas;
			
			//Gestion Ventas
			boolean clientesLinkVisible = 				isRolDesarrollador || isRolAdministrador || isRolSoloLectura;
			boolean facturasVentasLinkVisible = 		isRolDesarrollador || isRolAdministrador;
			boolean cobranzasLinkVisible = 				isRolDesarrollador || isRolAdministrador;
			boolean solicitudesFacVtaLinkVisible = 		isRolDesarrollador || isRolAdministrador || isRolEditorSolicitudesFactura;
			boolean misSolicitudesFacVtaLinkVisible=	isRolDesarrollador || isRolSolicitanteFacturasVentas;

			//Reportes Compras
			boolean subdiarioComprasLinkVisible = 		isRolDesarrollador || isRolAdministrador;
//			boolean deudasVsPagosLinkVisible = 			isRolDesarrollador || isRolAdministrador;
			boolean totalesProveedorLinkVisible = 		isRolDesarrollador || isRolAdministrador || isRolSoloLectura;
			boolean tarjetasCreditoLinkVisible = 		isRolDesarrollador || isRolAdministrador;
//			boolean detalleCajaChicaLinkVisible = 		isRolDesarrollador || isRolAdministrador;
			boolean reporteOPsLinkVisible = 			isRolDesarrollador || isRolAdministrador;
			boolean reportePlanCuentasLinkVisible = 	isRolDesarrollador || isRolAdministrador;
			
			//boolean totalesEventoLinkVisible = 			isRolDesarrollador || isRolAdministrador;
			boolean totalesEventoLinkVisible = 			isRolDesarrollador || isRolAdministrador || isRolSolicitantePagos || isRolSolicitanteFacturasVentas || isRolEditorSolicitudesFactura;
			boolean posicionLinkVisible = 				isRolDesarrollador || isRolAdministrador;
			
			//Reportes Ventas
			boolean subdiarioVentasLinkVisible = 		isRolDesarrollador || isRolAdministrador;
			boolean pendientesFactLinkVisible = 		isRolDesarrollador || isRolAdministrador;
			boolean deudasXClienteLinkVisible = 		isRolDesarrollador || isRolAdministrador;
			boolean ventasMensualXClienteLinkVisible =	isRolDesarrollador || isRolAdministrador;
			boolean chequesCarteraLinkVisible = 		isRolDesarrollador || isRolAdministrador;
			
			boolean duplicadosLinkVisible = 			isRolDesarrollador || isRolAdministrador;
			
			//Configuracion
			boolean editarPerfilLinkVisible = 			isRolDesarrollador; //por ahora nadie puede ver el editar perfil, solo desarrollador
			boolean usuariosLinkVisible = 				isRolDesarrollador || isRolAdministrador;

			//Configuracion Avanzada
			boolean cargaHorasLinkVisible = 			isRolDesarrollador; //solo desarrollador
			boolean configLinkVisible = 				isRolDesarrollador; //solo desarrollador
			boolean bancosLinkVisible = 				isRolDesarrollador || isRolAdministrador;

			setVisibilitiesGestion(proveedoresLinkVisible, facturasComprasLinkVisible, ordenesPagoLinkVisible, solicitudesPagoLinkVisible, misSolicitudesPagoLinkVisible, planesCuentaLinkVisible, 
									cajaChicaLinkVisible, presupuestoBancoLinkVisible, eventosLinkVisible, 
									clientesLinkVisible, facturasVentasLinkVisible, cobranzasLinkVisible, solicitudesFacVtaLinkVisible, misSolicitudesFacVtaLinkVisible);
			setVisibilitiesReportes(subdiarioComprasLinkVisible,  totalesProveedorLinkVisible, tarjetasCreditoLinkVisible, reporteOPsLinkVisible, reportePlanCuentasLinkVisible, totalesEventoLinkVisible, posicionLinkVisible,
									subdiarioVentasLinkVisible, pendientesFactLinkVisible, deudasXClienteLinkVisible, ventasMensualXClienteLinkVisible, chequesCarteraLinkVisible, duplicadosLinkVisible);
			setVisibilitiesConfiguracion(editarPerfilLinkVisible, usuariosLinkVisible,
					cargaHorasLinkVisible, configLinkVisible, bancosLinkVisible);
		}
	}

	private void setVisibilitiesGestion(boolean proveedoresLinkVisible, boolean facturasComprasLinkVisible, boolean ordenesPagoLinkVisible,	boolean solicitudesPagoLinkVisible,	boolean misSolicitudesPagoLinkVisible, boolean planesCuentaLinkVisible, 
			boolean cajaChicaLinkVisible, boolean presupuestoBancoLinkVisible, boolean eventosLinkVisible, 
			boolean clientesLinkVisible, boolean facturasVentasLinkVisible, boolean cobranzasLinkVisible, boolean solicitudesFacVtaLinkVisible,	boolean misSolicitudesFacVtaLinkVisible) {
		
		//COMPRAS
		headerGestionCompras.setVisible(proveedoresLinkVisible||facturasComprasLinkVisible||ordenesPagoLinkVisible||cajaChicaLinkVisible||solicitudesPagoLinkVisible||misSolicitudesPagoLinkVisible||planesCuentaLinkVisible);
		
		proveedoresLink.setVisible(proveedoresLinkVisible);
		facturasComprasLink.setVisible(facturasComprasLinkVisible);
		ordenesPagoLink.setVisible(ordenesPagoLinkVisible);
		solicitudesPagoLink.setVisible(solicitudesPagoLinkVisible);
		misSolicitudesPagoLink.setVisible(misSolicitudesPagoLinkVisible);
		planesCuentaLink.setVisible(planesCuentaLinkVisible);
		
		divider1.setVisible(cajaChicaLinkVisible||presupuestoBancoLinkVisible);
		cajaChicaLink.setVisible(cajaChicaLinkVisible);
		presupuestoBancoLink.setVisible(presupuestoBancoLinkVisible);
		
		//EVENTOS
		dividerEventos.setVisible(eventosLinkVisible);
		eventosLink.setVisible(eventosLinkVisible);
		
		//VENTAS
		dividerGestionVentas.setVisible(clientesLinkVisible||facturasVentasLinkVisible||cobranzasLinkVisible||solicitudesFacVtaLinkVisible||misSolicitudesFacVtaLinkVisible);
		headerGestionVentas.setVisible(clientesLinkVisible||facturasVentasLinkVisible||cobranzasLinkVisible||solicitudesFacVtaLinkVisible||misSolicitudesFacVtaLinkVisible);
		
		clientesLink.setVisible(clientesLinkVisible);
		facturasVentasLink.setVisible(facturasVentasLinkVisible);
		cobranzasLink.setVisible(cobranzasLinkVisible);
		solicitudesFacVtaLink.setVisible(solicitudesFacVtaLinkVisible);
		misSolicitudesFacVtaLink.setVisible(misSolicitudesFacVtaLinkVisible);
	}
	
	private void setVisibilitiesReportes(boolean subdiarioComprasLinkVisible, boolean totalesProveedorLinkVisible, boolean tarjetasCreditoLinkVisible, boolean reporteOPsLinkVisible, boolean reportePlanCuentasLinkVisible, boolean totalesEventoLinkVisible, boolean posicionLinkVisible,
										boolean subdiarioVentasLinkVisible, boolean pendientesFactLinkVisible, boolean deudasXClienteLinkVisible, boolean ventasMensualXClienteLinkVisible,	boolean chequesCarteraLinkVisible, boolean duplicadosLinkVisible) {
				
		//COMPRAS
		headerReportesCompras.setVisible(subdiarioComprasLinkVisible||totalesProveedorLinkVisible||tarjetasCreditoLinkVisible||reporteOPsLinkVisible||reportePlanCuentasLinkVisible);
		
		subdiarioComprasLink.setVisible(subdiarioComprasLinkVisible);
//		deudasVsPagosLink.setVisible(deudasVsPagosLinkVisible);
		totalesProveedorLink.setVisible(totalesProveedorLinkVisible);
		tarjetasCreditoLink.setVisible(tarjetasCreditoLinkVisible);
//		detalleCajaChicaLink.setVisible(detalleCajaChicaLinkVisible);
		reporteOPsLink.setVisible(reporteOPsLinkVisible);
		reportePlanCuentasLink.setVisible(reportePlanCuentasLinkVisible);
		
		//EVENTOS
		dividerEventos2.setVisible(totalesEventoLinkVisible);
		totalesEventoLink.setVisible(totalesEventoLinkVisible);
		
		divider2.setVisible(posicionLinkVisible);
		posicionLink.setVisible(posicionLinkVisible);
		
		//VENTAS
		dividerReportesVentas.setVisible(subdiarioVentasLinkVisible||pendientesFactLinkVisible||deudasXClienteLinkVisible||ventasMensualXClienteLinkVisible||chequesCarteraLinkVisible);
		headerReportesVentas.setVisible(subdiarioVentasLinkVisible||pendientesFactLinkVisible||deudasXClienteLinkVisible||ventasMensualXClienteLinkVisible||chequesCarteraLinkVisible);
		
		subdiarioVentasLink.setVisible(subdiarioVentasLinkVisible);
		pendientesFactLink.setVisible(pendientesFactLinkVisible);
		deudasXClienteLink.setVisible(deudasXClienteLinkVisible);
		ventasMensualXClienteLink.setVisible(ventasMensualXClienteLinkVisible);
		chequesCarteraLink.setVisible(chequesCarteraLinkVisible);
		
		divider3.setVisible(duplicadosLinkVisible);
		duplicadosLink.setVisible(duplicadosLinkVisible);
		
		reportesDropDown.setVisible(headerReportesCompras.isVisible() || headerReportesVentas.isVisible() || dividerEventos2.isVisible());
	}
	
	private void setVisibilitiesConfiguracion(boolean editarPerfilLinkVisible,boolean usuariosLinkVisible,
			boolean cargaHorasLinkVisible, boolean configLinkVisible, boolean bancosLinkVisible) {
		
		editarPerfilLink.setVisible(editarPerfilLinkVisible);
		usuariosLink.setVisible(usuariosLinkVisible);
		
		dividerConfiguracionAvanzada.setVisible(cargaHorasLinkVisible||configLinkVisible||bancosLinkVisible);
		headerConfiguracionAvanzada.setVisible(cargaHorasLinkVisible||configLinkVisible||bancosLinkVisible);
		
		cargaHorasLink.setVisible(cargaHorasLinkVisible);
		configLink.setVisible(configLinkVisible);
		bancosLink.setVisible(bancosLinkVisible);		
	}
}
