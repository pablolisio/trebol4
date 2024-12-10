package com.proit.wicket.dataproviders;

import java.util.Iterator;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.proit.modelo.CargaHoras;
import com.proit.servicios.CargaHorasService;


public class CargaHorasDataProvider implements IDataProvider<CargaHoras> {

	private static final long serialVersionUID = 1L;

	private CargaHorasService cargaHorasService;

	public CargaHorasDataProvider() {
		this.cargaHorasService = new CargaHorasService();
	}

	@Override
	public void detach() {
	}

	@Override
	public Iterator<? extends CargaHoras> iterator(long first, long count) {
		return cargaHorasService.getCargaHoras(first, count);
	}

	@Override
	public long size() {
		return cargaHorasService.getCargaHorasSize();
	}

	@Override
	public IModel<CargaHoras> model(CargaHoras object) {
		return new Model<CargaHoras>(object);
	}
}
