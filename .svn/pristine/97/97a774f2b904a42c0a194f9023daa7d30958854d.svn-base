package com.proit.vista.notifications;

import java.util.List;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.proit.servicios.NotificacionesService;
import com.proit.vista.base.FacturarOnLineBasePage;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class NotificacionesPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	private NotificacionesService notificacionesService;
	
	public NotificacionesPage(PageParameters parameters) {
		super(parameters);
		notificacionesService = new NotificacionesService();
		
		verificarNotificaciones();
		
		facturarOnLineMenu.setearMenuActivo(true, false, false, false);
	}

	private void verificarNotificaciones() {		
		List<String> listaNotificaciones = notificacionesService.getListaNotificaciones(isUsuarioLogueadoRolAdministrador());
		
		ListView<String> notificacionesListView = new ListView<String>("notificaciones", listaNotificaciones){
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<String> item) {
				item.add(new Label("mensaje",item.getModel()));
			}
			
		};
		
		final int cantidadNotificaciones = listaNotificaciones.size();
		if (cantidadNotificaciones==0)
			notificacionesListView.setVisible(false);
		
		add(notificacionesListView);
		
		add(new Label("titulo",Model.of(cantidadNotificaciones==0 ? "No hay notificaciones." : "Usted debe solucionar las siguientes situaciones:")){
			private static final long serialVersionUID = 1L;				
			@Override
			protected void onComponentTag(final ComponentTag tag){
			    super.onComponentTag(tag);
			    if (cantidadNotificaciones==0) {
			    	tag.put("class", "text-success");
			    } else {
			    	tag.put("class", "text-danger");
			    }
			}
		});
	}



}