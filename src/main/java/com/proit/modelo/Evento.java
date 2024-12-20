package com.proit.modelo;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.proit.modelo.ventas.Cliente;
import com.proit.utils.Utils;

/**
 * Esta clase representa un evento.
 * Ejemplo: Kids Tour, Otros/varios
 */
@Entity
public class Evento extends EntidadGenerica implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Transient
	public static final Evento SIN_EVENTO = new Evento(1, "<sin evento>");
	
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="cliente_fk")) //TODO agregar nullable=false
	private Cliente cliente;
	
	@Column(nullable = false)
	private String nombre;
	
	@Column(name="total_evento", columnDefinition="numeric", nullable = false)
	private double totalEvento;
	
	@Column(name="costo_total", columnDefinition="numeric", nullable = false)
	private double costoTotal;
	
	@Column(name="total_evento_con_iva", columnDefinition="numeric", nullable = false)
	private double totalEventoConIVA;
	
	@Column(name="costo_total_con_iva", columnDefinition="numeric", nullable = false)
	private double costoTotalConIVA;
	
	@Column(nullable = false)
	private boolean cerrado;
	
	@Column(columnDefinition="timestamp without time zone")
	private Calendar fecha;
	
	@Column(name="costo_final", nullable = false)
	private boolean costoFinal;
	
	/**
	 * Indica si en el reporte de Totales por Evento se va a mostrar a todos los roles o solo a administradores
	 */
	@Column(name="solo_administradores", nullable = false)
	private boolean soloAdministradores;
	
	/**
	 * Indica quien fue el usuario que creo el evento
	 */
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="responsable_fk"), name="responsable_id")
	private Usuario responsable;
	
	public Evento() {
		super();
	}
	
	private Evento(int id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public String getNombreConCliente() {
		return Utils.concatEventAndClient(nombre, cliente);
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getTotalEvento() {
		return totalEvento;
	}

	public void setTotalEvento(double totalEvento) {
		this.totalEvento = totalEvento;
	}

	public double getCostoTotal() {
		return costoTotal;
	}

	public void setCostoTotal(double costoTotal) {
		this.costoTotal = costoTotal;
	}

	public double getTotalEventoConIVA() {
		return totalEventoConIVA;
	}

	public void setTotalEventoConIVA(double totalEventoConIVA) {
		this.totalEventoConIVA = totalEventoConIVA;
	}

	public double getCostoTotalConIVA() {
		return costoTotalConIVA;
	}

	public void setCostoTotalConIVA(double costoTotalConIVA) {
		this.costoTotalConIVA = costoTotalConIVA;
	}

	public boolean isCerrado() {
		return cerrado;
	}

	public void setCerrado(boolean cerrado) {
		this.cerrado = cerrado;
	}

	public Calendar getFecha() {
		return fecha;
	}

	public void setFecha(Calendar fecha) {
		this.fecha = fecha;
	}
	
	public boolean isCostoFinal() {
		return costoFinal;
	}

	public void setCostoFinal(boolean costoFinal) {
		this.costoFinal = costoFinal;
	}

	public boolean isSoloAdministradores() {
		return soloAdministradores;
	}

	public void setSoloAdministradores(boolean soloAdministradores) {
		this.soloAdministradores = soloAdministradores;
	}

	public Usuario getResponsable() {
		return responsable;
	}

	public void setResponsable(Usuario responsable) {
		this.responsable = responsable;
	}

	//Estos dos metodos estan creados para ser usados las fechas como Date en vez de Calendar
	public Date getFechaAsDate() {
		if (fecha != null) {
			return fecha.getTime();
		}
		return null;
	}

	public void setFechaAsDate(Date fechaDate) {
		this.fecha = Calendar.getInstance();
		this.fecha.setTime(fechaDate);
	}

	@Override
	public String toString(){
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String texto = "Evento id: "+ id + ", ";
		texto += "cliente: " + (cliente!=null?cliente.getRazonSocial():"<Sin Cliente>") + ", ";
		texto += "nombre: " + nombre + ", ";
		texto += "cerrado: " + cerrado + ", ";
		texto += "costoFinal: " + costoFinal + ", ";
		if (fecha!=null) {
			texto += "fecha: " + dateFormat.format(fecha.getTime()) + ", ";
		} else {
			texto += "fecha: NULL, ";
		}
		texto += "totalEvento: " + totalEvento + ", ";
		texto += "costoTotal: " + costoTotal + ", ";
		texto += "totalEventoConIVA: " + totalEventoConIVA + ", ";
		texto += "costoTotalConIVA: " + costoTotalConIVA + ", ";
		texto += "soloAdministradores: " + soloAdministradores;
		return texto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Evento){
			Evento other = (Evento) obj;
			return ((this.getNombre() == null && other.getNombre() == null) || (this.getNombre() != null && this.getNombre().equals(other.getNombre())))
				&& ((this.getCliente() == null && other.getCliente() == null) || (this.getCliente() != null && this.getCliente().equals(other.getCliente())))
				&& this.isBorrado()==other.isBorrado();
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode(){
	    return (int) getNombre().hashCode()
	    		* getCliente().hashCode();
	}

}

