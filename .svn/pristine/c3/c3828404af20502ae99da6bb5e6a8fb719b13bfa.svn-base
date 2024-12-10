package com.proit.wicket;

import java.util.Locale;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;

import com.proit.modelo.Rol;
import com.proit.modelo.Usuario;
import com.proit.servicios.UsuarioService;

public class FacturarOnLineAuthenticatedWebSession extends AuthenticatedWebSession {

	private static final long serialVersionUID = 1L;
	
	private Usuario usuario;
	private Roles roles;
	private boolean isRolAdministrador;
	private boolean isRolSoloLectura;
	private boolean isRolDesarrollador;
	private boolean isRolSolicitantePagos;
	private boolean isRolSolicitanteFacturasVentas;
	private boolean isRolEditorSolicitudesFactura;

	public FacturarOnLineAuthenticatedWebSession(Request request) {
		super(request);
		this.setLocale(new Locale( "es" , "AR"));
	}

	@Override
	public boolean authenticate(String email, String password) {
		usuario = new UsuarioService().getUsuario(email, password);
		roles = new Roles();
		isRolAdministrador = false;
		isRolSoloLectura = false;
		isRolDesarrollador = false;
		isRolSolicitantePagos = false;
		isRolSolicitanteFacturasVentas = false;
		isRolEditorSolicitudesFactura = false;
		if (usuario != null) {
			for (Rol rol : usuario.getListadoRoles()) {
				roles.add(rol.getNombreRol());
				if (rol.equals(Rol.ADMINISTRADOR)){
					isRolAdministrador = true;
				}
				if (rol.equals(Rol.SOLO_LECTURA)){
					isRolSoloLectura = true;
				}
				if (rol.equals(Rol.DESARROLLADOR)){
					isRolDesarrollador = true;
				}
				if (rol.equals(Rol.SOLICITANTE_PAGOS)){
					isRolSolicitantePagos = true;
				}
				if (rol.equals(Rol.SOLICITANTE_FACTURAS_VENTAS)){
					isRolSolicitanteFacturasVentas = true;
				}
				if (rol.equals(Rol.EDITOR_SOLICITUDES_FACTURA)){
					isRolEditorSolicitudesFactura = true;
				}
			}
		}
		return usuario != null;
	}

	@Override
	public Roles getRoles() {
//		if (usuario == null) {
//			return new Roles();
//		}
		
//		Roles roles = new Roles();
//		roles.add(usuario.getRol().getNombreRol());
		
		return roles;
	}
	
	public Usuario getUsuario() {
		return this.usuario;
	}
	
	public boolean isRolAdministrador() {
		return isRolAdministrador;
	}
	public boolean isRolSoloLectura() {
		return isRolSoloLectura;
	}
	public boolean isRolDesarrollador() {
		return isRolDesarrollador;
	}
	public boolean isRolSolicitantePagos() {
		return isRolSolicitantePagos;
	}
	public boolean isRolSolicitanteFacturasVentas() {
		return isRolSolicitanteFacturasVentas;
	}
	public boolean isRolEditorSolicitudesFactura(){
		return isRolEditorSolicitudesFactura;
	}
	
}
