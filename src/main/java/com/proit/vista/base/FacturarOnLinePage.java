package com.proit.vista.base;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.proit.servicios.NotificacionesService;

@AuthorizeInstantiation({"Administrador","Solo Lectura","Desarrollador", "Solicitante Pagos", "Solicitante Facturas Ventas", "Editor Solicitudes Factura"})
public class FacturarOnLinePage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	public FacturarOnLinePage(final PageParameters parameters) {
		super(parameters);
		
		verificarNotificaciones();
		
		boolean mostrarHomeCompleto = isUsuarioLogueadoRolDesarrollador() || isUsuarioLogueadoRolAdministrador();
		
		WebMarkupContainer container = new WebMarkupContainer("container");
		container.setVisible(mostrarHomeCompleto);
		add(container);
		
		Label label = new Label("bienvenidoTxt", "¡Bienvenido a Trebol4 SRL! Acceda desde el menú a las distintas opciones para continuar...");
		label.setVisible(!mostrarHomeCompleto);
		add(label);
		
		facturarOnLineMenu.setearMenuActivo(true, false, false, false);
	}

	private void verificarNotificaciones() {
		NotificacionesService notificacionesService = new NotificacionesService();
		int cantidad = notificacionesService.getListaNotificaciones(isUsuarioLogueadoRolAdministrador()).size();
		
		String mensaje = "Usted tiene " + cantidad + (cantidad>1 ? " notificaciones" : " notificación") +  "... Por favor, para solucionarla"+ (cantidad>1 ? "s" : "") + " acceda ";
		WebMarkupContainer alertaContainer = crearAlerta(mensaje, true, true);
		
		if (cantidad==0){
			alertaContainer.setVisible(false);
		}
	}

}
