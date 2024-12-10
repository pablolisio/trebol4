package com.proit.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * Esta clase representa la asociacion many-to-many entre usuarios y roles. 
 */
@Entity
@Table(name="usuario_rol")
public class UsuarioRol implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column
	private boolean borrado;
	
	@Id
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="usuario_fk"), name="usuario_id", nullable = false)
	private Usuario usuario;
	
	@Id
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="rol_fk"), name="rol_id", nullable = false)
	private Rol rol;


	public boolean isBorrado() {
		return borrado;
	}
	
	public void setBorrado(boolean borrado) {
		this.borrado = borrado;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	@Override
	public String toString(){
		String texto = "Usuario-Rol: ";
		texto += "usuario: " + getUsuario()!=null ? getUsuario().getEmail() : "NULL" + ", ";
		texto += "rol: " + getRol().getNombreRol()+ ", ";
		return texto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof UsuarioRol){
			UsuarioRol other = (UsuarioRol) obj;
			return ((this.getUsuario() == null && other.getUsuario() == null) || (this.getUsuario() != null && this.getUsuario().equals(other.getUsuario())))
					&&	((this.getRol() == null && other.getRol() == null) || (this.getRol() != null && this.getRol().equals(other.getRol())))
				&& this.isBorrado()==other.isBorrado();
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode(){
	    return (int) getUsuario().hashCode() *
	    		getRol().hashCode() ;
	}
}
