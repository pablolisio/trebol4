package com.proit.modelo.compras;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.proit.modelo.EntidadGenerica;
import com.proit.utils.Constantes;


/**
 * Esta clase representa un proveedor. 
 */
@Entity
public class Proveedor extends EntidadGenerica implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name= "razon_social", nullable = false)
	private String razonSocial;
	
	@Column(name = "cuit_cuil")
	private String cuitCuil;
	
	@OneToOne
	@JoinColumn(foreignKey=@ForeignKey(name="cuenta_bancaria_fk"), name="cuenta_bancaria_id")
	private CuentaBancaria cuentaBancaria;
	
	/**
	 * Representa las distintas posibilidades de modos de pago habituales para un proveedor.
	 * La mascara se compone de 5 dígitos. Los mismos pueden ser 1 ó 0. 
	 * 1er dígito representa el modo de pago "Sin Factura (S/F)"
	 * 2do dígito representa el modo de pago "Efectivo"
	 * 3er dígito representa el modo de pago "Cheque"
	 * 4to dígito representa el modo de pago "Transferencia"
	 * 5to dígito representa el modo de pago "Tarjeta Credito"
	 * Ej de mascara para un proveedor con modo de pago S/F y Cheque: 10100
	 * Ej de mascara para un proveedor con modo de pago S/F y Transferencia: 10010
	 * Ej de mascara para un proveedor con modo de pago solo S/F: 10000
	 */
	@Column(name = "mascara_modo_pago", nullable = false)
	private int mascaraModoPago;
	
	/**
	 * El proveedor puede tener un, varios o ninguno cobro/s alternativo/s.
	 * Es por esto que el proveedor tiene un listado de cobros alternativos (es decir, cobros a terceros).
	 */
	@OneToMany(mappedBy="proveedor", fetch=FetchType.LAZY)
	@Cascade(value={CascadeType.ALL})
	private List<CobroAlternativo> listadoCobrosAlternativos;
	
	public Proveedor(){
		//this.mascaraModoPago = toInt(new BitSet(5)); // defino máscara por defecto
		//setModoSinFactura(true); //Siempre puede ser S/F
		listadoCobrosAlternativos = new ArrayList<CobroAlternativo>();
	}
	
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

	public CuentaBancaria getCuentaBancaria() {
		return cuentaBancaria;
	}

	public void setCuentaBancaria(CuentaBancaria cuentaBancaria) {
		this.cuentaBancaria = cuentaBancaria;
	}

	public int getMascaraModoPago() {
		return mascaraModoPago;
	}

	public void setMascaraModoPago(int mascaraModoPago) {
		this.mascaraModoPago = mascaraModoPago;
	}

	public List<CobroAlternativo> getListadoCobrosAlternativos() {
		List<CobroAlternativo> cobrosAlternativosNoBorrados = new ArrayList<CobroAlternativo>();
		for (CobroAlternativo cobroAlternativo : listadoCobrosAlternativos) {
			if (!cobroAlternativo.isBorrado()) {
				cobrosAlternativosNoBorrados.add(cobroAlternativo);
			}
		}
		return cobrosAlternativosNoBorrados;
	}
	
	public void setListadoCobrosAlternativos(List<CobroAlternativo> listadoCobrosAlternativos) {
		this.listadoCobrosAlternativos = listadoCobrosAlternativos;
	}
	
	public String getRazonSocialConCUIT() {
		return razonSocial + (cuitCuil!=null?" ("+cuitCuil + ")":"");
	}
	
	public boolean isModoSinFactura() {
		return fromInt(mascaraModoPago).get(Constantes.SIN_FACTURA);
	}
	
	public boolean isModoEfectivo() {
		return fromInt(mascaraModoPago).get(Constantes.EFECTIVO);
	}

	public boolean isModoCheque() {
		return fromInt(mascaraModoPago).get(Constantes.CHEQUE);
	}

	public boolean isModoTransferencia() {
		return fromInt(mascaraModoPago).get(Constantes.TRANSFERENCIA);
	}
	
	public boolean isModoTarjetaCredito() {
		return fromInt(mascaraModoPago).get(Constantes.TARJETA_CREDITO);
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
	
	public void setModoTarjetaCredito(boolean definido) {
		definirModoPago(Constantes.TARJETA_CREDITO, definido);
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
			bitSet = new BitSet(5);
			bitSet.set(index, definido);
			bitSet.or(fromInt(mascaraModoPago));
		} else {
			bitSet = fromInt(mascaraModoPago);
			bitSet.set(index, definido);
			bitSet.and(fromInt(mascaraModoPago));
		}
		mascaraModoPago = toInt(bitSet);
	}
	
	public String getModosPagoString() {
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
		if (tieneCobrosAlternativos()){
			modos = addModo(modos, "Transferencia a Terceros");
		}
		if (isModoTarjetaCredito()) {
			modos = addModo(modos, "Tarjeta Crédito");
		}
		modos += ".";
		return modos;
	}
	
	public boolean tieneCobrosAlternativos() {
		return ! getListadoCobrosAlternativos().isEmpty();
	}

	@Override
	public String toString(){
		String texto = "Proveedor id: "+ id + ", ";
		texto += "razonSocial: " + razonSocial + ",";
		texto += "cuitCuil: " + cuitCuil + ",";
		texto += "cuentaBancaria: {" + cuentaBancaria + "},";
		texto += "mascaraModoPago: " + mascaraModoPago + ",";
		texto += "listadoCobrosAlternativos: {";
		for (int i = 0; i < listadoCobrosAlternativos.size(); i++) {
			texto += "["+listadoCobrosAlternativos.get(i).toString()+"]";
		}
		texto += "}";
		return texto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Proveedor){
			Proveedor other = (Proveedor) obj;
			return this.getRazonSocial().equals(other.getRazonSocial())
				//&& this.getCuitCuil().equals(other.getCuitCuil())
				&& this.getMascaraModoPago() == other.getMascaraModoPago()
				&& this.isBorrado() == other.isBorrado();
		} else {
			return false;
		}
	}
	
	
	private String addModo(String modos, String modo){
		if (modos.isEmpty()){
			modos += modo;
		} else {
			modos += ", " + modo;
		}
		return modos;
	}
	
	@Override
	public int hashCode(){
	    return (int) getRazonSocial().hashCode() *
	    		//getCuitCuil().hashCode() *
	    		getMascaraModoPago();
	}
}
