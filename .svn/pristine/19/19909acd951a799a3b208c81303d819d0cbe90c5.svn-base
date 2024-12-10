package com.proit.servicios;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NotificacionesService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public NotificacionesService(){
		
	}
	
	public List<String> getListaNotificaciones(boolean isUsuarioLogueadoRolAdministrador) {
		List<String> listaNotificaciones = new ArrayList<String>();
		
		if (isUsuarioLogueadoRolAdministrador) {
			verificarSiInformarRecordatorioPago(listaNotificaciones);
		}
		
		return listaNotificaciones;
	}
	
	private void verificarSiInformarRecordatorioPago(List<String> listaNotificaciones) {
		Calendar hoy = Calendar.getInstance();
		int dia = hoy.get(Calendar.DAY_OF_MONTH);
		if ( dia <= 5 ) {
			listaNotificaciones.add("Recuerde abonar el pago del Server. Si ya lo hizo, desestime este mensaje.");
		}
	}

}
