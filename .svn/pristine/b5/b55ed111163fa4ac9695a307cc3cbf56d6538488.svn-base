package com.proit.wicket.dataproviders;

import java.util.Calendar;
import java.util.Iterator;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.proit.modelo.CajaChica;
import com.proit.servicios.CajaChicaService;

public class CajaChicaDataProvider implements IDataProvider<CajaChica> {

	private static final long serialVersionUID = 1L;

	private CajaChicaService cajaChicaService;
	
	private Calendar mes;
	
	public CajaChicaDataProvider(Calendar mes) {
		this.cajaChicaService = new CajaChicaService();
		this.mes = mes;
	}

	@Override
	public void detach() {
		
	}

	@Override
	public Iterator<CajaChica> iterator(long first, long count) {
		return cajaChicaService.getListaCajaChica(mes, null).iterator();
	}

	@Override
	public long size() {
		return cajaChicaService.getListaCajaChicaSize(mes);
	}

	@Override
	public IModel<CajaChica> model(CajaChica object) {
		return new Model<CajaChica>(object);
	}
	
}
