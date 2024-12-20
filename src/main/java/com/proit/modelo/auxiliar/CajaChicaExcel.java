package com.proit.modelo.auxiliar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.proit.modelo.CajaChica;
import com.proit.modelo.Usuario;
import com.proit.utils.Utils;

public class CajaChicaExcel {
	
	private String fecha;
	
	private String detalle;
	
	private String solicitante;
		
	private double monto;
	
	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getDetalle() {
		return detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	public String getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(String solicitante) {
		this.solicitante = solicitante;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public void convertFromCajaChica(CajaChica cajaChica) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		setFecha(dateFormat.format(cajaChica.getFecha().getTime()));
		double monto = Utils.round(cajaChica.getMonto(),2);
		setMonto(monto);
		setDetalle(cajaChica.getDetalle());
		String solicitanteStr = "---";
		if (cajaChica.isSolicitadoPorOtros()) {
			solicitanteStr = "Otros";
		} else if (cajaChica.isSolicitadoPorTodos()) {
			solicitanteStr = "Todos";
		} else {
			Usuario usuarioSolicitante = cajaChica.getUsuarioSolicitante();
			solicitanteStr = usuarioSolicitante!=null?usuarioSolicitante.getNombreCompleto():"-";
		}
		setSolicitante(solicitanteStr);
	}

	
	

}
