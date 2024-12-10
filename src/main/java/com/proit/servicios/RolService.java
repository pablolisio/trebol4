package com.proit.servicios;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.proit.modelo.Rol;

public class RolService implements Serializable {

	private static final long serialVersionUID = 1L;

	public RolService() {
	}
	
	/*
	 * Retorna un listado de Roles disponibles segun si el usuario logueado es desarrollador o no
	 */
	public List<Rol> getRoles(boolean isUsuarioLogueadoRolDesarrollodor) {
		ArrayList<Rol> listaRoles = new ArrayList<Rol>();		
		listaRoles.add(Rol.ADMINISTRADOR);
		listaRoles.add(Rol.SOLO_LECTURA);
		listaRoles.add(Rol.SOLICITANTE_PAGOS);
		listaRoles.add(Rol.SOLICITANTE_FACTURAS_VENTAS);
		listaRoles.add(Rol.EDITOR_SOLICITUDES_FACTURA);
		if (isUsuarioLogueadoRolDesarrollodor) {
			listaRoles.add(Rol.DESARROLLADOR);
		}		
		return listaRoles;
	}

}
