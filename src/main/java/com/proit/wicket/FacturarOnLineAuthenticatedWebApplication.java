package com.proit.wicket;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.settings.ExceptionSettings;
import org.hibernate.Session;

import com.proit.hibernate.HibernateUtil;
import com.proit.hibernate.ResetData;
import com.proit.vista.bancos.BancosPage;
import com.proit.vista.bancos.RegistrarBancoPage;
import com.proit.vista.base.FacturarOnLinePage;
import com.proit.vista.caja.CajaChicaPage;
import com.proit.vista.caja.DetalleCajaChicaPage;
import com.proit.vista.carga.CargaHorasPage;
import com.proit.vista.carga.RegistrarCargaHorasPage;
import com.proit.vista.compras.facturas.FacturasComprasPage;
import com.proit.vista.compras.facturas.RegistrarFacturaPage;
import com.proit.vista.compras.ordenes.OrdenesPagoPage;
import com.proit.vista.compras.ordenes.RegistrarOrdenPagoPage;
import com.proit.vista.compras.ordenes.cpysf.RegistrarOrdenPagoCPySFPage1;
import com.proit.vista.compras.ordenes.normal.RegistrarOrdenPagoNormalPage1;
import com.proit.vista.compras.ordenes.spysf.RegistrarOrdenPagoSPySFPage1;
import com.proit.vista.compras.plan.PlanesCuentaPage;
import com.proit.vista.compras.plan.RegistrarPlanCuentaPage;
import com.proit.vista.compras.proveedores.ProveedoresPage;
import com.proit.vista.compras.proveedores.RegistrarProveedorPage;
import com.proit.vista.compras.solicitudes.MisSolicitudesPagoPage;
import com.proit.vista.compras.solicitudes.RegistrarSolicitudPagoPage;
import com.proit.vista.compras.solicitudes.SolicitudesPagoPage;
import com.proit.vista.compras.solicitudes.cpysf.RegistrarSolicitudPagoCPySFPage;
import com.proit.vista.compras.solicitudes.normal.RegistrarSolicitudPagoNormalPage;
import com.proit.vista.compras.solicitudes.spysf.RegistrarSolicitudPagoSPySFPage;
import com.proit.vista.config.ConfigPage;
import com.proit.vista.eventos.EventosPage;
import com.proit.vista.eventos.RegistrarEventoPage;
import com.proit.vista.login.LoginPage;
import com.proit.vista.notifications.NotificacionesPage;
import com.proit.vista.others.ContactenosPage;
import com.proit.vista.others.ErrorPage404;
import com.proit.vista.others.ErrorPage500;
import com.proit.vista.others.TermsPage;
import com.proit.vista.password.CambiarClavePage;
import com.proit.vista.presupuesto.PresupuestosBancoPage;
import com.proit.vista.presupuesto.RegistrarPresupuestoCustomPage;
import com.proit.vista.reportes.DuplicadosPage;
import com.proit.vista.reportes.PosicionPage;
import com.proit.vista.reportes.compras.ReporteOPsMensualPage;
import com.proit.vista.reportes.compras.ReportePlanCuentasMensualPage;
import com.proit.vista.reportes.compras.SubdiarioComprasPage;
import com.proit.vista.reportes.compras.TarjetasCreditoPage;
import com.proit.vista.reportes.compras.TotalesProveedorPage;
import com.proit.vista.reportes.eventos.TotalesEventoPage;
import com.proit.vista.reportes.ventas.ChequesEnCartera;
import com.proit.vista.reportes.ventas.DeudasPorClientePage;
import com.proit.vista.reportes.ventas.PendientesFacturacionPage;
import com.proit.vista.reportes.ventas.SubdiarioVentasPage;
import com.proit.vista.reportes.ventas.VentasPorClientePage;
import com.proit.vista.users.ActivarUsuarioPage;
import com.proit.vista.users.RegistrarUsuarioPage;
import com.proit.vista.users.UsuarioNoLogueadoPage;
import com.proit.vista.users.UsuariosPage;
import com.proit.vista.ventas.clientes.ClientesPage;
import com.proit.vista.ventas.clientes.RegistrarClientePage;
import com.proit.vista.ventas.cobranzas.CobranzasPage;
import com.proit.vista.ventas.cobranzas.RegistrarCobranzaPage1;
import com.proit.vista.ventas.facturas.FacturasVentasPage;
import com.proit.vista.ventas.facturas.RegistrarFacturaVentaPage;
import com.proit.vista.ventas.solicitudes.MisSolicitudesFacturaVentaPage;
import com.proit.vista.ventas.solicitudes.SolicitudesFacturaVentaPage;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 * 
 * @see com.proit.Start#main(String[])
 */
public class FacturarOnLineAuthenticatedWebApplication extends AuthenticatedWebApplication {
	
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends WebPage> getHomePage()
	{
		return LoginPage.class;
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init()
	{
		super.init();
		
		mountPage("error404", ErrorPage404.class);
		mountPage("error500", ErrorPage500.class);
		mountPage("login", LoginPage.class);
		mountPage("inicio", FacturarOnLinePage.class);
		mountPage("usuarios", UsuariosPage.class);
		mountPage("registrar-usuario", RegistrarUsuarioPage.class);		
		mountPage("activarUsuario", ActivarUsuarioPage.class);
		mountPage("iniciar-sesion", UsuarioNoLogueadoPage.class);
		mountPage("cambiarClave", CambiarClavePage.class);
		mountPage("notificaciones", NotificacionesPage.class);
		mountPage("contacto", ContactenosPage.class);
		mountPage("terminos", TermsPage.class);
		mountPage("cargaHoras", CargaHorasPage.class);
		mountPage("registrar-cargahoras", RegistrarCargaHorasPage.class);
		mountPage("config", ConfigPage.class);
		mountPage("bancos", BancosPage.class);
		mountPage("registrar-banco", RegistrarBancoPage.class);
		
		mountPage("caja-chica", CajaChicaPage.class);
		mountPage("detalle-caja-chica", DetalleCajaChicaPage.class);
		
		mountPage("presupuestos-banco", PresupuestosBancoPage.class);
		mountPage("registrar-presupuesto", RegistrarPresupuestoCustomPage.class);
		
		mountPage("eventos", EventosPage.class);
		mountPage("registrar-evento", RegistrarEventoPage.class);
				
		//COMPRAS
		mountPage("proveedores", ProveedoresPage.class);
		mountPage("registrar-proveedor", RegistrarProveedorPage.class);		
		mountPage("facturasCompras", FacturasComprasPage.class);
		mountPage("registrar-factura-compra", RegistrarFacturaPage.class);
		mountPage("ordenesPago", OrdenesPagoPage.class);
		mountPage("registrar-op", RegistrarOrdenPagoPage.class);
		mountPage("registrar-opNormal", RegistrarOrdenPagoNormalPage1.class);
		mountPage("registrar-opCPySF", RegistrarOrdenPagoCPySFPage1.class);
		mountPage("registrar-opSPySF", RegistrarOrdenPagoSPySFPage1.class);
		mountPage("registrar-solicitud", RegistrarSolicitudPagoPage.class);
		mountPage("planesCuenta", PlanesCuentaPage.class);
		mountPage("registrar-plan-cuenta", RegistrarPlanCuentaPage.class);
		
		mountPage("solicitudesPago", SolicitudesPagoPage.class);
		mountPage("misSolicitudesPago", MisSolicitudesPagoPage.class);
		mountPage("registrar-solicitudNormal", RegistrarSolicitudPagoNormalPage.class);
		mountPage("registrar-solCPySF", RegistrarSolicitudPagoCPySFPage.class);
		mountPage("registrar-solSPySF", RegistrarSolicitudPagoSPySFPage.class);
		
		mountPage("subdiarioCompras", SubdiarioComprasPage.class);
//		mountPage("deudasVsPagos", DeudasVsPagosPage.class);
		mountPage("totalesProveedor", TotalesProveedorPage.class);
		mountPage("tarjetasCredito", TarjetasCreditoPage.class);
		mountPage("reporteOPsMensual", ReporteOPsMensualPage.class);
		mountPage("reportePlanCuentasMensual", ReportePlanCuentasMensualPage.class);
		
		mountPage("totalesEvento", TotalesEventoPage.class);
		mountPage("posicion", PosicionPage.class);
		mountPage("duplicados", DuplicadosPage.class);
		
		//VENTAS
		mountPage("clientes", ClientesPage.class);
		mountPage("registrar-cliente", RegistrarClientePage.class);
		mountPage("facturasVentas", FacturasVentasPage.class);
		mountPage("registrar-factura-venta", RegistrarFacturaVentaPage.class);
		
		mountPage("solicitudesFacVta", SolicitudesFacturaVentaPage.class);
		mountPage("misSolicitudesFacVta", MisSolicitudesFacturaVentaPage.class);
		mountPage("cobranzas", CobranzasPage.class);
		mountPage("registrar-cob", RegistrarCobranzaPage1.class);
		
		mountPage("chequesCartera", ChequesEnCartera.class);
		mountPage("subdiarioVentas", SubdiarioVentasPage.class);
		mountPage("pendientesFact", PendientesFacturacionPage.class);
		mountPage("deudasXCliente", DeudasPorClientePage.class);
		mountPage("ventasMensualXCliente", VentasPorClientePage.class);
		
		HibernateUtil.configureSessionFactory();
		
		AbstractRequestCycleListener abstractRequestCycleListener = new AbstractRequestCycleListener() {
			@Override
			public void onBeginRequest(RequestCycle cycle) {
				getHibernateSession().beginTransaction();
			}
			
			@Override
			public void onEndRequest(RequestCycle cycle) {
				Session hibernateSession = getHibernateSession();
				if (hibernateSession != null && hibernateSession.isOpen()) {
					hibernateSession.flush();
					/* Using
					 * <property name="hibernate.current_session_context_class">thread</property>
					 * on hibernate.cfg.xml, closes session after a transaction commit.
					*/
					hibernateSession.getTransaction().commit();
					HibernateUtil.closeSession();
				}
			}
		};
		
		getRequestCycleListeners().add(abstractRequestCycleListener);
		
		//para Wicket-jQuery UI
		this.getMarkupSettings().setStripWicketTags(true); //IMPORTANT!
		
		//para que se muestren bien los tildes(acentos) en las paginas HTML
		this.getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
		this.getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
		
		verificarModoDeployment();
	}

	private void verificarModoDeployment() {
		if (getConfigurationType().equals(RuntimeConfigurationType.DEPLOYMENT)){
			//para que al lanzar una excepcion en cualquier pagina se muestre la pagina de disculpe las molestias (Error 500)
			getApplicationSettings().setInternalErrorPage(ErrorPage500.class);
			getExceptionSettings().setUnexpectedExceptionDisplay(ExceptionSettings.SHOW_INTERNAL_ERROR_PAGE);
		} else {
			ResetData.initScript(false); // False -> Don't refresh database.
		}
	}
	
	public static Session getHibernateSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}

	@Override
	protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
		return FacturarOnLineAuthenticatedWebSession.class;
	}

	@Override
	protected Class<? extends WebPage> getSignInPageClass() {
		return LoginPage.class;
	}
	
	@Override
	protected void onUnauthorizedPage(final Component page) {
		throw new RestartResponseException(UsuarioNoLogueadoPage.class);
	}
	
	/**
	 * Utilizado para determinar si la aplicacion esta corriendo en modo Development o Deployment
	 */
	@Override
	public RuntimeConfigurationType getConfigurationType(){
		
		//Si el Sistema Operativo es Windows, entonces es mi laptop: Ambiente Desarrollo (DEVELOPMENT). 
		//Sino, es Linux (Server - Ambiente Productivo - DEPLOYMENT)
		String sistemaOperativo = System.getProperty("os.name").toLowerCase();
		if (sistemaOperativo.contains("win")) {
			return RuntimeConfigurationType.DEVELOPMENT;
		}
		else return RuntimeConfigurationType.DEPLOYMENT;
		
	}
}
