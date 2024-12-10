package com.proit.vista.base;

import java.io.File;

import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.string.Strings;

import com.proit.modelo.Usuario;
import com.proit.servicios.ConfigService;
import com.proit.utils.Constantes;
import com.proit.wicket.FacturarOnLineAuthenticatedWebSession;
import com.proit.wicket.components.AJAXDownload;

public class FacturarOnLineBasePage extends WebPage {
	private static final long serialVersionUID = 1L;
	
	protected FacturarOnLineMenuPanel facturarOnLineMenu;
	
	private ConfigService configService;
	
	public FacturarOnLineBasePage(final PageParameters parameters) {
		super(parameters);
		
		configService = new ConfigService();
		
		if (onlyDesarrolladorCanUseApp() && !isUsuarioLogueadoRolDesarrollador()) {
			mostrarDisculpeMolestiasPage();
		}
		
		facturarOnLineMenu = new FacturarOnLineMenuPanel("facturarOnLineMenu");
		add(facturarOnLineMenu);
		
		FacturarOnLineFooterPanel facturarOnLineFooter = new FacturarOnLineFooterPanel("facturarOnLineFooter");
		add(facturarOnLineFooter);
	}
	
	public FacturarOnLineBasePage() {
		super();
		
		configService = new ConfigService();
		
		if (onlyDesarrolladorCanUseApp() && !isUsuarioLogueadoRolDesarrollador()) {
			mostrarDisculpeMolestiasPage();
		}
		
		facturarOnLineMenu = new FacturarOnLineMenuPanel("facturarOnLineMenu");
		add(facturarOnLineMenu);
		
		FacturarOnLineFooterPanel facturarOnLineFooter = new FacturarOnLineFooterPanel("facturarOnLineFooter");
		add(facturarOnLineFooter);
	}

	protected void mostrarAlertaEnPantallaSiCorresponde(final PageParameters parameters) {
		String textoPorPantalla = parameters.get("TextoPantalla").toString();
		boolean esError = !"OK".equalsIgnoreCase(parameters.get("Resultado").toString());
		crearAlerta(textoPorPantalla, esError, false);
	}

	protected WebMarkupContainer crearAlerta(String textoPorPantalla, final boolean esError, final boolean esAlertaNotificaciones) {
		WebMarkupContainer alertaContainer = new WebMarkupContainer("alerta") {
			private static final long serialVersionUID = 1L;				
			@Override
			protected void onComponentTag(final ComponentTag tag){
			    super.onComponentTag(tag);
			    if (!esError) {
			    	tag.put("class", "alert"+ ( esAlertaNotificaciones ? "Notificaciones" : "") +" alert-success alert-dismissable");
			    } else {
			    	tag.put("class", "alert"+ ( esAlertaNotificaciones ? "Notificaciones" : "") +" alert-danger alert-dismissable");
			    }
			}
		};
		
		Label alertLabel =  new Label("textoAlerta", textoPorPantalla);
		alertaContainer.add(alertLabel);
		alertaContainer.setVisible(!Strings.isEmpty(textoPorPantalla));
		addOrReplace(alertaContainer);
		return alertaContainer;
	}
	
	protected Usuario getUsuarioLogueado(){
		return ((FacturarOnLineAuthenticatedWebSession) getSession()).getUsuario();
	}
	
	protected boolean isUsuarioLogueadoRolAdministrador(){
		return ((FacturarOnLineAuthenticatedWebSession) getSession()).isRolAdministrador();
	}
	protected boolean isUsuarioLogueadoRolSoloLectura(){
		return ((FacturarOnLineAuthenticatedWebSession) getSession()).isRolSoloLectura();
	}
	protected boolean isUsuarioLogueadoRolDesarrollador(){
		return ((FacturarOnLineAuthenticatedWebSession) getSession()).isRolDesarrollador();
	}
	protected boolean isUsuarioLogueadoRolSolicitantePagos(){
		return ((FacturarOnLineAuthenticatedWebSession) getSession()).isRolSolicitantePagos();
	}
	protected boolean isUsuarioLogueadoRolSolicitanteFacturasVentas(){
		return ((FacturarOnLineAuthenticatedWebSession) getSession()).isRolSolicitanteFacturasVentas();
	}
	protected boolean isUsuarioLogueadoRolEditorSolicitudesFactura(){
		return ((FacturarOnLineAuthenticatedWebSession) getSession()).isRolEditorSolicitudesFactura();
	}
	
	protected boolean onlyDesarrolladorCanUseApp(){
		return configService.getConfig().isOnlyDesarrolladorCanUseApp();
	}
	
	protected RuntimeConfigurationType getRuntimeConfigurationType() {
		return ((FacturarOnLineAuthenticatedWebSession) getSession()).getApplication().getConfigurationType();
	}

	/**
	 * Este método puede ser utilizado cuando se quiera mostrar la página de disculpe 
	 * las molestias intencionalmente (Error 500)
	 */
	protected void mostrarDisculpeMolestiasPage() {
		throw new AbortWithHttpErrorCodeException(500);
	}
	
	/**
	 * Used to know if we are trying to save or update an entity on a page.
	 * @param id Entity ID.
	 * @return True if we are updating an entity. False otherwise.
	 */
	protected boolean verificarSiEsEditar(Integer id) {
		boolean esEditar = (id != null);
		add(new Label("secondaryTitle",  ( esEditar ? "Editar" : "Crear" ) ));
		return esEditar;
	}

	protected AJAXDownload createAjaxDownload(final String fileName) {
		final AJAXDownload download = new AJAXDownload() {
			private static final long serialVersionUID = 1L;
	
			@Override
		    protected IResourceStream getResourceStream() {
				RuntimeConfigurationType runtimeConfigurationType = getRuntimeConfigurationType();
				String filePath;
				if (runtimeConfigurationType.equals(RuntimeConfigurationType.DEPLOYMENT)){
					filePath = Constantes.EXCEL_PATH_GENERATED_PROD +  fileName;
				} else {
					filePath = Constantes.EXCEL_PATH_GENERATED_DEV +  fileName;
				}				
				File file = new File(filePath);
				IResourceStream resStream = new FileResourceStream(file);
				return resStream;
		    }
			
			@Override
	        protected String getFileName() {
	            return fileName;
	        }
		};
		return download;
	}

}
