package com.proit.wicket.dataproviders;

import java.util.Iterator;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.proit.modelo.compras.PlanCuenta;
import com.proit.servicios.compras.PlanCuentaService;

public class PlanesCuentaDataProvider implements IDataProvider<PlanCuenta> {

	private static final long serialVersionUID = 1L;

	private PlanCuentaService planCuentaService;

	public PlanesCuentaDataProvider() {
		this.planCuentaService = new PlanCuentaService();
	}

	@Override
	public void detach() {
	}

	@Override
	public Iterator<? extends PlanCuenta> iterator(long first, long count) {
		return planCuentaService.getPlanesCuenta(first, count);
	}

	@Override
	public long size() {
		return planCuentaService.getPlanesCuentaSize();
	}

	@Override
	public IModel<PlanCuenta> model(PlanCuenta object) {
		return new Model<PlanCuenta>(object);
	}
}
