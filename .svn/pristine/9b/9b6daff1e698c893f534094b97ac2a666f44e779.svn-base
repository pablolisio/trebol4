package com.proit.modelo.ventas;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.proit.modelo.EntidadGenerica;

/**
 * 
 * Esta clase representa una Cobranza.
 */
@Entity
@Table(name="cobranza")
public class Cobranza extends EntidadGenerica implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(columnDefinition="timestamp without time zone", nullable = false)
	private Calendar fecha;
	
	@Column(name = "nro_recibo")
	private String nroRecibo;
	

	/**
	 * La cobranza puede tener uno o varios cobros.
	 */
	@OneToMany(mappedBy="cobranza", fetch=FetchType.LAZY)
	@Cascade(value={CascadeType.ALL})
	private List<Cobro> listadoCobros;
	
	@ManyToMany(mappedBy="listadoCobranzas")
	private List<FacturaVenta> listadoFacturas;
	
	@Column(name="perc_iva", columnDefinition="numeric")
	private double percIva;
	
	@Column(name="perc_iibb", columnDefinition="numeric")
	private double percIibb;
	
	@Column(name="perc_gcias", columnDefinition="numeric")
	private double percGcias;
	
	@Column(name="perc_suss", columnDefinition="numeric")
	private double percSUSS;
	
	@Column(name="otras_perc", columnDefinition="numeric")
	private double otrasPerc;
	
	@Column(name="retenciones_validadas", nullable = false)
	protected boolean retencionesValidadas;
	
	public Calendar getFecha() {
		return fecha;
	}

	public void setFecha(Calendar fecha) {
		this.fecha = fecha;
	}

	public String getNroRecibo() {
		return nroRecibo;
	}

	public void setNroRecibo(String nroRecibo) {
		this.nroRecibo = nroRecibo;
	}

	public List<Cobro> getListadoCobros() {
		return listadoCobros;
	}

	public void setListadoCobros(List<Cobro> listadoCobros) {
		this.listadoCobros = listadoCobros;
	}

	public List<FacturaVenta> getListadoFacturas() {
		return listadoFacturas;
	}

	public void setListadoFacturas(List<FacturaVenta> listadoFacturas) {
		this.listadoFacturas = listadoFacturas;
	}

	public double getPercIva() {
		return percIva;
	}

	public void setPercIva(double percIva) {
		this.percIva = percIva;
	}

	public double getPercIibb() {
		return percIibb;
	}

	public void setPercIibb(double percIibb) {
		this.percIibb = percIibb;
	}

	public double getPercGcias() {
		return percGcias;
	}

	public void setPercGcias(double percGcias) {
		this.percGcias = percGcias;
	}

	public double getPercSUSS() {
		return percSUSS;
	}

	public void setPercSUSS(double percSUSS) {
		this.percSUSS = percSUSS;
	}

	public double getOtrasPerc() {
		return otrasPerc;
	}

	public void setOtrasPerc(double otrasPerc) {
		this.otrasPerc = otrasPerc;
	}

	public boolean isRetencionesValidadas() {
		return retencionesValidadas;
	}

	public void setRetencionesValidadas(boolean retencionesValidadas) {
		this.retencionesValidadas = retencionesValidadas;
	}

	@Override
	public String toString(){
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String texto = "Cobranza id: "+ id + ", ";
		texto += "fecha: " + dateFormat.format(fecha.getTime())+ ", ";
		texto += "nroRecibo: " + nroRecibo+ ", ";
		texto += "percIva: " + percIva + ", ";
		texto += "percIibb: " + percIibb + ", ";
		texto += "percGcias: " + percGcias + ", ";
		texto += "percSUSS: " + percSUSS + ", ";
		texto += "otrasPerc: " + otrasPerc + ", ";
		texto += "retencionesValidadas: " + retencionesValidadas + ", ";
		texto += "listadoFacturas: {";
		if (listadoFacturas!=null){
			for (int i = 0; i < listadoFacturas.size(); i++) {
				texto += "["+listadoFacturas.get(i).toString()+"]";
			}
		} else {
			texto+="empty";
		}
		texto += "}";
		texto += "listadoPagos: {";
		if (listadoCobros!=null){
			for (int i = 0; i < listadoCobros.size(); i++) {
				texto += "["+listadoCobros.get(i).toString()+"]";
			}
		} else {
			texto+="empty";
		}
		texto += "}";
		return texto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Cobranza){
			Cobranza other = (Cobranza) obj;
			return ((this.getFecha() == null && other.getFecha() == null) || (this.getFecha() != null && this.getFecha().equals(other.getFecha())))
				&&	((this.getNroRecibo() == null && other.getNroRecibo() == null) || (this.getNroRecibo() != null && this.getNroRecibo().equals(other.getNroRecibo())))
				&& this.getId() == other.getId() //TODO esta bien esto?
				&& this.isBorrado()==other.isBorrado();
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode(){
	    return (int) getFecha().hashCode() *
	    		getNroRecibo().hashCode() *
	    		getId();
	}
}
