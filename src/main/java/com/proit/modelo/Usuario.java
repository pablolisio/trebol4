package com.proit.modelo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.proit.utils.Constantes;

/**
 * 
 * Esta clase representa un Usuario el cual va a acceder al sistema.
 */
@Entity
public class Usuario extends EntidadGenerica implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Es el nombre de la persona o la razon social en caso de ser una empresa.
	 */
	@Column(name="nombre_o_razon_social")
	private String nombreORazonSocial;

	private String apellido;
	
	private String telefono;
	
	@Column(unique=true, nullable = false)
	private String email;

	@Column(nullable = false)
	private String clave;
	
	@ManyToMany(cascade = {CascadeType.ALL}, fetch=FetchType.LAZY)
	@JoinTable(name="usuario_rol", 
				joinColumns={@JoinColumn(name="usuario_id")}, 
		        inverseJoinColumns={@JoinColumn(name="rol_id")})
	private List<Rol> listadoRoles;
	
	/**
	 * Contiene el token para activar al usuario en caso de no estarlo,
	 * En caso de estar activado contiene la palabra "activado"
	 */
	private String activacion;

	public Usuario() {
	}

	public String getNombreORazonSocial() {
		return nombreORazonSocial;
	}

	public void setNombreORazonSocial(String nombreORazonSocial) {
		this.nombreORazonSocial = nombreORazonSocial;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}
	
	public List<Rol> getListadoRoles() {
		return listadoRoles;
	}

	public void setListadoRoles(List<Rol> listadoRoles) {
		this.listadoRoles = listadoRoles;
	}

	public String getActivacion() {
		return activacion;
	}

	public void setActivacion(String activacion) {
		this.activacion = activacion;
	}
	
	public boolean esUsuarioActivo(){
		if ( ! this.getActivacion().equals(Constantes.USUARIO_ACTIVADO) ) {
			return false;
		}
		return true;
	}
	
	public String getNombreCompleto() {
		return nombreORazonSocial + " " + apellido;
	}
	
	@Override
	public String toString() {
		String texto = "Usuario id: " + id + ", ";
		texto += "nombreORazonSocial: " + nombreORazonSocial + ", ";
		texto += "apellido: " + apellido + ", ";
		texto += "email: " + email + ", ";
		texto += "telefono: " + telefono+ ", ";
		texto += "clave: " + clave + ", ";
		return texto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Usuario){
			Usuario other = (Usuario) obj;
			return ((this.getApellido() == null && other.getApellido() == null) || (this.getApellido() != null && this.getApellido().equals(other.getApellido())))
				&& ((this.getNombreORazonSocial() == null && other.getNombreORazonSocial() == null) || (this.getNombreORazonSocial() != null && this.getNombreORazonSocial().equals(other.getNombreORazonSocial())))
				&& this.getEmail().equals(other.getEmail())
				&& ((this.getTelefono() == null && other.getTelefono() == null) || (this.getTelefono() != null && this.getTelefono().equals(other.getTelefono())))
				&& this.isBorrado() == other.isBorrado();
		} else {
			return false;
		}
	}
	
	
	public boolean isRolAdministrador() {
		for (Rol rol : listadoRoles) {
			if (rol.equals(Rol.ADMINISTRADOR)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isRolSoloLectura() {
		for (Rol rol : listadoRoles) {
			if (rol.equals(Rol.SOLO_LECTURA)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isRolDesarrollador() {
		for (Rol rol : listadoRoles) {
			if (rol.equals(Rol.DESARROLLADOR)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isRolSolicitantePagos() {
		for (Rol rol : listadoRoles) {
			if (rol.equals(Rol.SOLICITANTE_PAGOS)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isRolSolicitanteFacturasVentas() {
		for (Rol rol : listadoRoles) {
			if (rol.equals(Rol.SOLICITANTE_FACTURAS_VENTAS)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isRolEditorSolicitudesFactura() {
		for (Rol rol : listadoRoles) {
			if (rol.equals(Rol.EDITOR_SOLICITUDES_FACTURA)){
				return true;
			}
		}
		return false;
	}
	
}
