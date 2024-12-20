package com.proit.wicket.dataproviders;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.proit.modelo.compras.FacturaCompra;
import com.proit.servicios.compras.FacturaCompraService;

public class FacturasCompraDataProvider implements IDataProvider<FacturaCompra> {

	private static final long serialVersionUID = 1L;

	private FacturaCompraService facturaService;
	
	private IModel<String> razonSocialModel;
	private IModel<Date> fechaModel;
	private IModel<Boolean> soloPendientesYParcialesModel;
	private Calendar mesImpositivoInicio;
	private Calendar mesImpositivoFin;
	
	public FacturasCompraDataProvider(IModel<String> razonSocialModel, IModel<Date> fechaModel, IModel<Boolean> soloPendientesYParcialesModel, Calendar mesImpositivoInicio, Calendar mesImpositivoFin) {
		this.facturaService = new FacturaCompraService();
		this.razonSocialModel = razonSocialModel;
		this.fechaModel = fechaModel;
		this.soloPendientesYParcialesModel = soloPendientesYParcialesModel;
		this.mesImpositivoInicio = mesImpositivoInicio;
		this.mesImpositivoFin = mesImpositivoFin;
	}

	@Override
	public void detach() {
		this.razonSocialModel.detach();
		this.fechaModel.detach();
		this.soloPendientesYParcialesModel.detach();
	}

	@Override
	public Iterator<FacturaCompra> iterator(long first, long count) {
		return facturaService.getFacturas(first, count, razonSocialModel.getObject(), getFecha(), soloPendientesYParcialesModel.getObject(), mesImpositivoInicio, mesImpositivoFin);
	}

	@Override
	public long size() {
		return facturaService.getFacturasSize(razonSocialModel.getObject(), getFecha(), soloPendientesYParcialesModel.getObject(), mesImpositivoInicio, mesImpositivoFin);
	}

	@Override
	public IModel<FacturaCompra> model(FacturaCompra object) {
		return new Model<FacturaCompra>(object);
	}
	
	private Calendar getFecha() {
		Calendar fecha = null;
		if (fechaModel.getObject() != null){
			fecha = Calendar.getInstance();
			fecha.setTime(fechaModel.getObject());
		}
		return fecha;
	}
}
