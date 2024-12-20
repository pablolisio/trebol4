package com.proit.modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * 
 * Esta clase representa un tipo de factura. Usado para facturas Compra y Venta
 * Los mismos pueden ser: A, B, C, Nota de Credito A, Nota de Credito B, Nota de Credito C.
 * Tambien se agregaron los tipos E (Exportacion, con su Nota de Debito y Credito) y N (N=En Negro).
 */
@Entity(name="tipo_factura")
public class TipoFactura extends EntidadGenerica implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Transient
	public static final TipoFactura TIPO_A = new TipoFactura(1, "A", "Factura Tipo A", false); //TODO falta done
	@Transient
	public static final TipoFactura TIPO_B = new TipoFactura(2, "B", "Factura Tipo B", false);//TODO falta done
	@Transient
	public static final TipoFactura TIPO_C = new TipoFactura(3, "C", "Factura Tipo C", false);//TODO falta done
	@Transient
	public static final TipoFactura NOTA_DEB_A = new TipoFactura(4, "Nota de Debito A", "Nota de Debito A", false);//TODO falta done
	@Transient
	public static final TipoFactura NOTA_DEB_B = new TipoFactura(5, "Nota de Debito B", "Nota de Debito B", false);//TODO falta done
	@Transient
	public static final TipoFactura NOTA_DEB_C = new TipoFactura(6, "Nota de Debito C", "Nota de Debito C", false);//TODO falta done
	@Transient
	public static final TipoFactura NOTA_DEB_E = new TipoFactura(15, "Nota de Debito E", "Nota de Debito E", false);//TODO falta done
	@Transient
	public static final TipoFactura NOTA_CRED_A = new TipoFactura(7, "Nota de Credito A", "Nota de Credito A", false);//TODO falta done
	@Transient
	public static final TipoFactura NOTA_CRED_B = new TipoFactura(8, "Nota de Credito B", "Nota de Credito B", false);//TODO falta done
	@Transient
	public static final TipoFactura NOTA_CRED_C = new TipoFactura(9, "Nota de Credito C", "Nota de Credito C", false);//TODO falta done
	@Transient
	public static final TipoFactura NOTA_CRED_E = new TipoFactura(16, "Nota de Credito E", "Nota de Credito E", false);//TODO falta done
	@Transient
	public static final TipoFactura NOTA_CRED_N = new TipoFactura(17, "Nota de Credito N", "Nota de Credito N", false);
	@Transient
	public static final TipoFactura TIPO_E = new TipoFactura(10, "E", "Factura Tipo E", false);//TODO falta done
	@Transient
	/**
	 * Representa las facturas en Negro.
	 */
	public static final TipoFactura TIPO_N = new TipoFactura(11, "N", "Factura Tipo N", false);//TODO falta done
	
	/**
	 * Representa Factura de Credito Electronica (FCE)
	 */
	@Transient
	public static final TipoFactura TIPO_FCE = new TipoFactura(12, "FCE", "Factura Tipo FCE", false);
	@Transient
	public static final TipoFactura NOTA_DEB_FCE = new TipoFactura(13, "Nota de Debito FCE", "Nota de Debito FCE", false);
	@Transient
	public static final TipoFactura NOTA_CRED_FCE = new TipoFactura(14, "Nota de Credito FCE", "Nota de Credito FCE", false);
	
	private String nombre;
	
	private String descripcion;
	
	public TipoFactura() {
	}
			
	public TipoFactura(int id, String nombre, String descripcion, boolean borrado) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.borrado = borrado;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public boolean isTipoA(){//TODO falta done
		return this.equals(TipoFactura.TIPO_A);
	}
	
	public boolean isTipoB(){//TODO falta done
		return this.equals(TipoFactura.TIPO_B);
	}
	
	public boolean isTipoC(){//TODO falta done
		return this.equals(TipoFactura.TIPO_C);
	}
	
	public boolean isTipoE(){//TODO falta done
		return this.equals(TipoFactura.TIPO_E);
	}
	
	public boolean isTipoN(){//TODO falta done
		return this.equals(TipoFactura.TIPO_N);
	}
	
	public boolean isTipoFCE(){
		return this.equals(TipoFactura.TIPO_FCE);
	}
	
	public boolean isNotaCreditoA(){//TODO falta done
		return this.equals(TipoFactura.NOTA_CRED_A);
	}
	
	public boolean isNotaCreditoB(){//TODO falta done
		return this.equals(TipoFactura.NOTA_CRED_B);
	}
	
	public boolean isNotaCreditoC(){//TODO falta done
		return this.equals(TipoFactura.NOTA_CRED_C);
	}
	
	public boolean isNotaCreditoE(){//TODO falta done
		return this.equals(TipoFactura.NOTA_CRED_E);
	}
	
	public boolean isNotaCreditoN(){
		return this.equals(TipoFactura.NOTA_CRED_N);
	}
	
	public boolean isNotaCreditoFCE(){
		return this.equals(TipoFactura.NOTA_CRED_FCE);
	}
	
	public boolean isNotaCredito(){//TODO falta done
		return isNotaCreditoA() || isNotaCreditoB() || isNotaCreditoC() || isNotaCreditoE() || isNotaCreditoFCE() || isNotaCreditoN();
	}
	
	public boolean isNotaDebitoA(){//TODO falta done
		return this.equals(TipoFactura.NOTA_DEB_A);
	}
	
	public boolean isNotaDebitoFCE(){
		return this.equals(TipoFactura.NOTA_DEB_FCE);
	}
	
	public boolean isNotaDebitoB(){//TODO falta done
		return this.equals(TipoFactura.NOTA_DEB_B);
	}
	
	public boolean isNotaDebitoC(){//TODO falta done
		return this.equals(TipoFactura.NOTA_DEB_C);
	}
	
	public boolean isNotaDebitoE(){//TODO falta done
		return this.equals(TipoFactura.NOTA_DEB_E);
	}
	
	public boolean isNotaDebito(){//TODO falta done
		return isNotaDebitoA() || isNotaDebitoB() || isNotaDebitoC() || isNotaDebitoE() || isNotaDebitoFCE();
	}
	
	public double getPorcentajeIVA() {//TODO falta done
		if (isTipoA() || isNotaCreditoA() || isNotaDebitoA() ||
			isTipoFCE() || isNotaCreditoFCE() || isNotaDebitoFCE()) {
			return 21;
		}
		return 0; //Los demas tipos de factura no discriminan IVA.
	}
	
	public String getNombreCorto() {
		if (isTipoA() || isTipoB() || isTipoC() || isTipoE() || isTipoN() || isTipoFCE()) {
			return getNombre();
		}
		if (isNotaCreditoA()) {
			return "NC A";
		}
		if (isNotaCreditoB()) {
			return "NC B";
		}
		if (isNotaCreditoC()) {
			return "NC C";
		}
		if (isNotaCreditoE()) {
			return "NC E";
		}
		if (isNotaCreditoFCE()) {
			return "NC FCE";
		}
		if (isNotaCreditoN()) {
			return "NC N";
		}
		if (isNotaDebitoA()) {
			return "ND A";
		}
		if (isNotaDebitoB()) {
			return "ND B";
		}
		if (isNotaDebitoC()) {
			return "ND C";
		}		
		if (isNotaDebitoE()) {
			return "ND E";
		}
		if (isNotaDebitoFCE()) {
			return "ND FCE";
		}
		return null;
	}
	
	public static TipoFactura getByNombre(String nombre) {
		switch (nombre) {
			case "A":
				return TipoFactura.TIPO_A;
			case "B":
				return TipoFactura.TIPO_B;
			case "C":
				return TipoFactura.TIPO_C;
			case "FCE":
				return TipoFactura.TIPO_FCE;
			case "Nota de Debito A":
				return TipoFactura.NOTA_DEB_A;
			case "Nota de Debito B":
				return TipoFactura.NOTA_DEB_B;
			case "Nota de Debito C":
				return TipoFactura.NOTA_DEB_C;
			case "Nota de Debito E":
				return TipoFactura.NOTA_DEB_E;
			case "Nota de Debito FCE":
				return TipoFactura.NOTA_DEB_FCE;
			case "Nota de Credito A":
				return TipoFactura.NOTA_CRED_A;
			case "Nota de Credito B":
				return TipoFactura.NOTA_CRED_B;
			case "Nota de Credito C":
				return TipoFactura.NOTA_CRED_C;
			case "Nota de Credito E":
				return TipoFactura.NOTA_CRED_E;
			case "Nota de Credito N":
				return TipoFactura.NOTA_CRED_N;
			case "Nota de Credito FCE":
				return TipoFactura.NOTA_CRED_FCE;
			case "E":
				return TipoFactura.TIPO_E;
			default:
				return TipoFactura.TIPO_N;
		}
	}
	
	@Override
	public String toString(){
		String texto = "Tipo Factura id: "+ id + ", ";
		texto += "nombre: " + nombre + ", ";
		texto += "descripcion: " + descripcion + ", ";
		return texto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TipoFactura){
			TipoFactura other = (TipoFactura) obj;
			return this.getNombre().equals(other.getNombre())
				&& ((this.getDescripcion() == null && other.getDescripcion() == null) || (this.getDescripcion() != null && this.getDescripcion().equals(other.getDescripcion())));
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode(){
	    return (int) getNombre().hashCode() *
	    		(getDescripcion()==null?0:getDescripcion().hashCode()) ;
	}
	
	
}