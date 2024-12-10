package com.proit.modelo.auxiliar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.proit.modelo.compras.FacturaCompra;
import com.proit.utils.Constantes;
import com.proit.utils.Utils;

public class FacturaCompraExcel {
	
	private String fecha;
	
	private String proveedor;
	
	private String tipo;
	
	private String nro;
	
	private String estado;
	
	private double subtotal;
	
	private double iva;
	
	private double percIva;
	
	private double percIibb;
	
	private double percGcias;
	
	private double percSUSS;
	
	private double otrasPerc;
	
	private double total;

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getProveedor() {
		return proveedor;
	}

	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNro() {
		return nro;
	}

	public void setNro(String nro) {
		this.nro = nro;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public double getIva() {
		return iva;
	}

	public void setIva(double iva) {
		this.iva = iva;
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

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	
	public double getImporte() {
		return subtotal + iva + percIva + percIibb + percGcias + percSUSS + otrasPerc;
	}

	public void convertFromFactura(FacturaCompra factura) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		setFecha(dateFormat.format(factura.getFecha().getTime()));
		double subt = Utils.round(factura.getSubtotal(),2);
		double iv = Utils.round(factura.getIva(),2);
		double pIva = Utils.round(factura.getPercIva(),2);
		double pIibb = Utils.round(factura.getPercIibb(),2);
		double pGcias = Utils.round(factura.getPercGcias(),2);
		double pSUSS = Utils.round(factura.getPercSUSS(),2);
		double otrasP = Utils.round(factura.getOtrasPerc(),2);
		double total = Utils.round(factura.calculateTotal(),2);
		if (factura.isNotaCredito()) {
			subt = 		subt * (-1);
			iv = 		iv * (-1);
			pIva = 		pIva * (-1);
			pIibb = 	pIibb * (-1);
			pGcias = 	pGcias * (-1);
			pSUSS = 	pSUSS * (-1);
			otrasP = 	otrasP * (-1);
			total = 	total * (-1);
		}
		setSubtotal(subt);
		setIva(iv);
		setPercIva(pIva);
		setPercIibb(pIibb);
		setPercGcias(pGcias);
		setPercSUSS(pSUSS);
		setOtrasPerc(otrasP);
		setTotal(total);
		if (factura.getNro().startsWith(Constantes.PREFIX_NRO_FACTURA_SF)) {
			setNro("<Sin Factura>");
		} else {
			setNro(factura.getNro());
		}
		setProveedor(factura.getProveedor()==null?"<Sin Proveedor>":factura.getProveedor().getRazonSocialConCUIT());
		setTipo(factura.getNro().startsWith(Constantes.PREFIX_NRO_FACTURA_SF)?"-":factura.getTipoFactura().getNombreCorto());
		setNro(factura.getNro().startsWith(Constantes.PREFIX_NRO_FACTURA_SF)?"<Sin Factura>":factura.getNro());
		setEstado(factura.getEstadoFactura().getNombre());
	}

}
