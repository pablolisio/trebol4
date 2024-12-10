package com.proit.modelo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

/**
 * 
 * Esta clase representa el Rol que puede tener un usuario del sistema.
 */
@Entity
public class Rol extends EntidadGenerica implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Transient
	public static final Rol ADMINISTRADOR = new Rol(1, "Administrador", "Dueño del sistema", false);
	@Transient
	public static final Rol SOLO_LECTURA = new Rol(3, "Solo Lectura", "Solo puede ver cosas, pero no puede editar ni crear nada", false);
	@Transient
	public static final Rol DESARROLLADOR = new Rol(4, "Desarrollador", "Desarrollador", false);
	@Transient
	public static final Rol SOLICITANTE_PAGOS = new Rol(5, "Solicitante Pagos", "Encargado de realizar Solicitudes de Pagos", false);
	@Transient
	public static final Rol SOLICITANTE_FACTURAS_VENTAS = new Rol(6, "Solicitante Facturas Ventas", "Encargado de realizar Solicitudes de Facturas de Ventas", false);
	@Transient
	public static final Rol EDITOR_SOLICITUDES_FACTURA = new Rol(7, "Editor Solicitudes Factura", "Puede editar Solicitudes de Facturas de Ventas", false);
	@Transient
	public static final Rol[] AVAILABLE_ROLES = {ADMINISTRADOR, SOLO_LECTURA, DESARROLLADOR, SOLICITANTE_PAGOS, SOLICITANTE_FACTURAS_VENTAS, EDITOR_SOLICITUDES_FACTURA};
	
	

	@Column(name="nombre_rol", nullable = false)
	private String nombreRol;

	private String descripcion;
	
	@ManyToMany(mappedBy="listadoRoles")
	private List<Usuario> listadoUsuarios;

	public Rol() {
	}
	
	public Rol(int id, String nombreRol, String descripcion, boolean borrado) {
		this.id = id;
		this.nombreRol = nombreRol;
		this.descripcion = descripcion;
		this.borrado = borrado;
	}

	public String getNombreRol() {
		return nombreRol;
	}

	public void setNombreRol(String nombreRol) {
		this.nombreRol = nombreRol;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public List<Usuario> getListadoUsuarios() {
		return listadoUsuarios;
	}

	public void setListadoUsuarios(List<Usuario> listadoUsuarios) {
		this.listadoUsuarios = listadoUsuarios;
	}

	@Override
	public String toString(){
		String texto = "Rol id: "+ id + ", ";
		texto += "nombreRol: " + nombreRol + ", ";
		texto += "descripcion: " + descripcion;
		return texto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Rol){
			Rol other = (Rol)obj;
			return this.getNombreRol().equals(other.getNombreRol());
		} else {
			return false;
		}
	}
}
