package com.proit.modelo.ventas;

import java.io.Serializable;
import java.util.BitSet;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.proit.modelo.EntidadGenerica;
import com.proit.utils.Constantes;


/**
 * Esta clase representa un cliente. 
 */
@Entity
public class Cliente extends EntidadGenerica implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name= "razon_social", nullable = false)
	private String razonSocial;
	
	@Column(name = "cuit_cuil", nullable = false)
	private String cuitCuil;
	
	@Column
	private String domicilio;
	
	/**
	 * Representa las distintas posibilidades de modos de cobro habituales para un cliente.
	 * La mascara se compone de 4 dígitos. Los mismos pueden ser 1 ó 0. 
	 * 1er dígito representa el modo de pago "Sin Factura (S/F)"
	 * 2do dígito representa el modo de pago "Efectivo"
	 * 3er dígito representa el modo de pago "Cheque"
	 * 4to dígito representa el modo de pago "Transferencia"
	 * Ej de mascara para un cliente con modo de pago S/F y Cheque: 1010
	 * Ej de mascara para un cliente con modo de pago S/F y Transferencia: 1001
	 * Ej de mascara para un cliente con modo de pago solo S/F: 1000
	 */
	@Column(name = "mascara_modo_cobros", nullable = false)
	private int mascaraModoCobros;
	
	@Column
	private String telefono1;
	
	@Column
	private String telefono2;
	
	@Column
	private String contacto;
	
	@Column
	private String mails;
	
	@Column
	private String referencias;
	
	@Column
	private String observaciones;
	
	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getCuitCuil() {
		return cuitCuil;
	}

	public void setCuitCuil(String cuitCuil) {
		this.cuitCuil = cuitCuil;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public int getMascaraModoCobros() {
		return mascaraModoCobros;
	}

	public void setMascaraModoCobros(int mascaraModoCobros) {
		this.mascaraModoCobros = mascaraModoCobros;
	}

	public String getTelefono1() {
		return telefono1;
	}

	public void setTelefono1(String telefono1) {
		this.telefono1 = telefono1;
	}

	public String getTelefono2() {
		return telefono2;
	}

	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}

	public String getContacto() {
		return contacto;
	}

	public void setContacto(String contacto) {
		this.contacto = contacto;
	}

	public String getMails() {
		return mails;
	}

	public void setMails(String mails) {
		this.mails = mails;
	}

	public String getReferencias() {
		return referencias;
	}

	public void setReferencias(String referencias) {
		this.referencias = referencias;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	public String getRazonSocialConCUIT() {
		return razonSocial + (cuitCuil!=null?" ("+cuitCuil + ")":"");
	}

	public boolean isModoSinFactura() {
		return fromInt(mascaraModoCobros).get(Constantes.SIN_FACTURA);
	}
	
	public boolean isModoEfectivo() {
		return fromInt(mascaraModoCobros).get(Constantes.EFECTIVO);
	}

	public boolean isModoCheque() {
		return fromInt(mascaraModoCobros).get(Constantes.CHEQUE);
	}

	public boolean isModoTransferencia() {
		return fromInt(mascaraModoCobros).get(Constantes.TRANSFERENCIA);
	}
	
	public void setModoSinFactura(boolean definido) {
		definirModoPago(Constantes.SIN_FACTURA, definido);
	}
	
	public void setModoEfectivo(boolean definido) {
		definirModoPago(Constantes.EFECTIVO, definido);
	}

	public void setModoCheque(boolean definido) {
		definirModoPago(Constantes.CHEQUE, definido);
	}

	public void setModoTransferencia(boolean definido) {
		definirModoPago(Constantes.TRANSFERENCIA, definido);
	}
	
	public static BitSet fromInt(int num) {
	    BitSet bs = new BitSet();
	    for (int k = 0; k < Integer.SIZE; k++) {
	        if (((num >> k) & 1) == 1) {
	            bs.set(k);
	        }
	    }
	    return bs;
	}
	
	public static int toInt(BitSet bs) {
	    int num = 0;
	    for (int k = -1; (k = bs.nextSetBit(k + 1)) != -1; ) {
	        num |= (1 << k);
	    }
	    return num;
	}

	private void definirModoPago(int index, boolean definido) {
		BitSet bitSet;
		if (definido) {
			bitSet = new BitSet(4);
			bitSet.set(index, definido);
			bitSet.or(fromInt(mascaraModoCobros));
		} else {
			bitSet = fromInt(mascaraModoCobros);
			bitSet.set(index, definido);
			bitSet.and(fromInt(mascaraModoCobros));
		}
		mascaraModoCobros = toInt(bitSet);
	}
	
	public String getModosCobroString() {
		String modos = "";
//		if (isModoSinFactura()) {
//			modos = addModo(modos, "S/F");
//		}
		if (isModoEfectivo()) {
			modos = addModo(modos, "Efectivo");
		}
		if (isModoCheque()) {
			modos = addModo(modos, "Cheque");
		}
		if (isModoTransferencia()) {
			modos = addModo(modos, "Transferencia");
		}
		modos += ".";
		return modos;
	}
	
	@Override
	public String toString(){
		String texto = "Cliente id: "+ id + ", ";
		texto += "razonSocial: " + razonSocial + ",";
		texto += "cuitCuil: " + cuitCuil + ",";
		texto += "domicilio: " + domicilio + ",";
		texto += "mascaraModoCobros: " + mascaraModoCobros + ",";
		texto += "telefono1: " + telefono1 + ", ";
		texto += "telefono2: " + telefono2 + ", ";
		texto += "contacto: " + contacto + ", ";
		texto += "mails: " + mails + ", ";
		texto += "referencias: " + referencias + ", ";
		texto += "observaciones	: " + observaciones	 + ".";
		return texto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Cliente){
			Cliente other = (Cliente) obj;
			return this.getRazonSocial().equals(other.getRazonSocial())
				&& this.getCuitCuil().equals(other.getCuitCuil())
				&& this.getMascaraModoCobros() == other.getMascaraModoCobros()
				&& this.isBorrado() == other.isBorrado();
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode(){
	    return (int) getRazonSocial().hashCode() *
	    		getCuitCuil().hashCode() *
	    		getMascaraModoCobros();
	}
	
	private String addModo(String modos, String modo){
		if (modos.isEmpty()){
			modos += modo;
		} else {
			modos += ", " + modo;
		}
		return modos;
	}
}
