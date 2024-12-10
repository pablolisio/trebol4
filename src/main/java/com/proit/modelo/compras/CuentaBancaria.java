package com.proit.modelo.compras;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.proit.modelo.Banco;
import com.proit.modelo.EntidadGenerica;
import com.proit.modelo.TipoCuenta;

/**
 * Esta clase representa una cuenta bancaria. 
 */
@Entity(name="cuenta_bancaria")
public class CuentaBancaria extends EntidadGenerica implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="tipo_cuenta_fk"), name="tipo_cuenta_id")
	private TipoCuenta tipoCuenta;
	
	@Column(name = "nro_cuenta")
	private String nroCuenta;
	
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="banco_fk"))
	private Banco banco;
	
	@Column
	private String cbu;
	
	@Column
	private String alias;
	
	public TipoCuenta getTipoCuenta() {
		return tipoCuenta;
	}

	public void setTipoCuenta(TipoCuenta tipoCuenta) {
		this.tipoCuenta = tipoCuenta;
	}

	public String getNroCuenta() {
		return nroCuenta;
	}

	public void setNroCuenta(String nroCuenta) {
		this.nroCuenta = nroCuenta;
	}

	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public String getCbu() {
		return cbu;
	}

	public void setCbu(String cbu) {
		this.cbu = cbu;
	}
	
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Override
	public String toString(){
		String texto = "Cuenta Bancaria id: "+ id + ", ";
		texto += "tipoCuenta: {" + tipoCuenta + "},";
		texto += "nroCuenta: " + nroCuenta + ",";
		texto += "banco: {" + banco + "},";
		texto += "cbu: " + cbu + ",";
		texto += "alias: " + alias;
		return texto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CuentaBancaria){
			CuentaBancaria other = (CuentaBancaria) obj;
			return ((this.getCbu() == null && other.getCbu() == null) || (this.getCbu() != null && this.getCbu().equals(other.getCbu())))
				&&  ((this.getAlias() == null && other.getAlias() == null) || (this.getAlias() != null && this.getAlias().equals(other.getAlias())))
				&& this.isBorrado() == other.isBorrado();
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode(){
	    return (int) (getCbu()==null?1:getCbu().hashCode()) 
	    		* (getAlias()==null?1:getAlias().hashCode());
	}
}
