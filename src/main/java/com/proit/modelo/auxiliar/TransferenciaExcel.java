package com.proit.modelo.auxiliar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.proit.modelo.compras.Pago;
import com.proit.utils.Utils;

public class TransferenciaExcel {
	
	private String CBU; 
	
	private String fecha;
	
	private double importe;
	
	public String getCBU() {
		return CBU;
	}

	public void setCBU(String cBU) {
		CBU = cBU;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}
	
	public void convertFromPago(Pago pago, String CBU, String alias, String cobroAlternativo) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String cbu = CBU!=null?"CBU: "+CBU+". ":"";
		cbu += alias!=null?"Alias: "+alias+". ":"";
		cbu += cobroAlternativo!=null? "("+cobroAlternativo+")":"";
		setCBU(cbu);
		setFecha(pago.getFecha()!=null?dateFormat.format(pago.getFecha().getTime()):"");
		setImporte(Utils.round(pago.getImporte(),2));		
	}

}
