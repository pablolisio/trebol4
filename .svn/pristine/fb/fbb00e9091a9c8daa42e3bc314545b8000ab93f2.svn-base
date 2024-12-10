package com.proit.wicket.dataproviders;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.proit.modelo.ventas.FacturaVentaCobranza;
import com.proit.servicios.ventas.CobranzaService;

public class FacturaVentaCobranzaDataProvider implements IDataProvider<FacturaVentaCobranza> {

	private static final long serialVersionUID = 1L;

	private CobranzaService cobranzaService;
	
	private IModel<String> razonSocialModel;
	private IModel<Date> fechaModel;
	private IModel<Boolean> soloRetencionesSinValidarModel;
	
	public FacturaVentaCobranzaDataProvider(IModel<String> razonSocialModel, IModel<Date> fechaModel, IModel<Boolean> soloRetencionesSinValidarModel) {
		this.cobranzaService = new CobranzaService();
		this.razonSocialModel = razonSocialModel;
		this.fechaModel = fechaModel;
		this.soloRetencionesSinValidarModel = soloRetencionesSinValidarModel;
	}

	@Override
	public void detach() {
		this.razonSocialModel.detach();
		this.fechaModel.detach();
		this.soloRetencionesSinValidarModel.detach();
	}

	@Override
	public Iterator<FacturaVentaCobranza> iterator(long first, long count) {
		return cobranzaService.getListaFacturaVentaCobranza(first, count, razonSocialModel.getObject(), getFecha(), soloRetencionesSinValidarModel.getObject());
	}

	@Override
	public long size() {
		return cobranzaService.getListaFacturaVentaCobranzaSize(razonSocialModel.getObject(), getFecha(), soloRetencionesSinValidarModel.getObject());
	}

	@Override
	public IModel<FacturaVentaCobranza> model(FacturaVentaCobranza object) {
		return new Model<FacturaVentaCobranza>(object);
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
