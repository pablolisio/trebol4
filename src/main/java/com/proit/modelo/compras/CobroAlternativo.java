package com.proit.modelo.compras;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.proit.modelo.EntidadGenerica;


/**
 * Esta clase representa un cobro alternativo para un proveedor.
 * El caso se da cuando se tiene un proveedor al que para cobrarle, se utilizan los datos de un tercero. 
 */
@Entity(name="cobro_alternativo")
public class CobroAlternativo extends EntidadGenerica implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(nullable = false)
	private String titular;
	
	@Column(name = "cuit_cuil", nullable = false)
	private String cuitCuil;
	
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="cuenta_bancaria_fk"), name="cuenta_bancaria_id", nullable = false)
	private CuentaBancaria cuentaBancaria;
	
	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="proveedor_fk"), name="proveedor_id", nullable = false)
	private Proveedor proveedor;

	public String getTitular() {
		return titular;
	}

	public void setTitular(String titular) {
		this.titular = titular;
	}

	public String getCuitCuil() {
		return cuitCuil;
	}

	public void setCuitCuil(String cuitCuil) {
		this.cuitCuil = cuitCuil;
	}

	public CuentaBancaria getCuentaBancaria() {
		return cuentaBancaria;
	}

	public void setCuentaBancaria(CuentaBancaria cuentaBancaria) {
		this.cuentaBancaria = cuentaBancaria;
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}
	
	public String getTitularConCUIT() {
		return titular + (cuitCuil!=null?" ("+cuitCuil + ")":"");
	}

	@Override
	public String toString(){
		String texto = "Cobro Alternativo id: "+ id + ", ";
		texto += "titular: " + titular + ",";
		texto += "cuitCuil: " + cuitCuil + ",";
		texto += "cuentaBancaria: {" + cuentaBancaria + "}";		
		return texto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CobroAlternativo){
			CobroAlternativo other = (CobroAlternativo) obj;
			return this.getTitular().equals(other.getTitular())
				&& this.getCuitCuil().equals(other.getCuitCuil())
				&& this.getCuentaBancaria().equals(other.getCuentaBancaria())
				&& this.getProveedor().equals(other.getProveedor())
				&& this.isBorrado() == other.isBorrado();
		} else {
			return false;
		}
	}

}
