package com.proit.wicket.dataproviders;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.proit.modelo.ventas.FacturaVenta;
import com.proit.servicios.ventas.FacturaVentaService;

public class FacturasVentaDataProvider implements IDataProvider<FacturaVenta> {

	private static final long serialVersionUID = 1L;

	private FacturaVentaService facturaService;
	
	private IModel<String> razonSocialModel;
	private IModel<Integer> idEventoModel;
	private IModel<Date> fechaModel;
	private IModel<Boolean> soloXCobrarYCobroParcialModel;
	private Calendar mes;
	private IModel<Boolean> ocultarFacturasTipoNModel;
	
	public FacturasVentaDataProvider(IModel<String> razonSocialModel, IModel<Integer> idEventoModel, IModel<Date> fechaModel, IModel<Boolean> soloXCobrarYCobroParcialModel, 
												Calendar mes, IModel<Boolean> ocultarFacturasTipoNModel) {
		this.facturaService = new FacturaVentaService();
		this.razonSocialModel = razonSocialModel;
		this.idEventoModel = idEventoModel;
		this.fechaModel = fechaModel;
		this.soloXCobrarYCobroParcialModel = soloXCobrarYCobroParcialModel;
		this.mes = mes;
		this.ocultarFacturasTipoNModel = ocultarFacturasTipoNModel;
	}

	@Override
	public void detach() {
		this.razonSocialModel.detach();
		this.idEventoModel.detach();
		this.fechaModel.detach();
		this.soloXCobrarYCobroParcialModel.detach();
		this.ocultarFacturasTipoNModel.detach();
	}

	@Override
	public Iterator<FacturaVenta> iterator(long first, long count) {
		return facturaService.getFacturas(first, count, razonSocialModel.getObject(), idEventoModel.getObject(), getFecha(), soloXCobrarYCobroParcialModel.getObject(), mes, ocultarFacturasTipoNModel.getObject());
	}

	@Override
	public long size() {
		return facturaService.getFacturasSize(razonSocialModel.getObject(), idEventoModel.getObject(), getFecha(), soloXCobrarYCobroParcialModel.getObject(), mes, ocultarFacturasTipoNModel.getObject());
	}

	@Override
	public IModel<FacturaVenta> model(FacturaVenta object) {
		return new Model<FacturaVenta>(object);
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
