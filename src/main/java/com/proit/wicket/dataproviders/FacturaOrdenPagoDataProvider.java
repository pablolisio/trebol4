package com.proit.wicket.dataproviders;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.proit.modelo.compras.FacturaCompraOrdenPago;
import com.proit.servicios.compras.OrdenPagoService;

public class FacturaOrdenPagoDataProvider implements IDataProvider<FacturaCompraOrdenPago> {

	private static final long serialVersionUID = 1L;

	private OrdenPagoService ordenPagoService;
	
	private IModel<String> razonSocialModel;
	private IModel<String> nroOPModel;
	private IModel<Date> fechaModel;
	private IModel<Integer> idEventoModel;
	private IModel<Integer> idPlanCuentaModel;
	private IModel<Boolean> soloSFYConProv;
	private IModel<Boolean> soloFacturasDePagoParcial;
	private IModel<Boolean> incluirBorradas;
	private Calendar mes;
	
	public FacturaOrdenPagoDataProvider(IModel<String> razonSocialModel, IModel<String> nroOPModel, IModel<Date> fechaModel, IModel<Integer> idEventoModel, IModel<Boolean> soloSFYConProv, 
										IModel<Boolean> soloFacturasDePagoParcial, IModel<Boolean> incluirBorradas, Calendar mes, IModel<Integer> idPlanCuentaModel) {
		this.ordenPagoService = new OrdenPagoService();
		this.razonSocialModel = razonSocialModel;
		this.nroOPModel = nroOPModel;
		this.fechaModel = fechaModel;
		this.idEventoModel = idEventoModel;
		this.idPlanCuentaModel = idPlanCuentaModel;
		this.soloSFYConProv = soloSFYConProv;
		this.soloFacturasDePagoParcial = soloFacturasDePagoParcial;
		this.incluirBorradas = incluirBorradas;
		this.mes = mes;
	}

	@Override
	public void detach() {
		this.razonSocialModel.detach();
		this.nroOPModel.detach();
		this.fechaModel.detach();
		this.idEventoModel.detach();
		this.idPlanCuentaModel.detach();
		this.soloSFYConProv.detach();
		this.soloFacturasDePagoParcial.detach();
		this.incluirBorradas.detach();
	}

	@Override
	public Iterator<FacturaCompraOrdenPago> iterator(long first, long count) {
		return ordenPagoService.getListaFacturaOrdenPago(first, count, razonSocialModel.getObject(), getFecha(), idEventoModel.getObject(), 
				soloSFYConProv.getObject(), soloFacturasDePagoParcial.getObject(), incluirBorradas.getObject(), mes, idPlanCuentaModel.getObject(), 
				nroOPModel.getObject());
	}

	@Override
	public long size() {
		return ordenPagoService.getListaFacturaOrdenPagoSize(razonSocialModel.getObject(), getFecha(), idEventoModel.getObject(), 
				soloSFYConProv.getObject(), soloFacturasDePagoParcial.getObject(), incluirBorradas.getObject(), mes, idPlanCuentaModel.getObject(), 
				nroOPModel.getObject());
	}

	@Override
	public IModel<FacturaCompraOrdenPago> model(FacturaCompraOrdenPago object) {
		return new Model<FacturaCompraOrdenPago>(object);
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
