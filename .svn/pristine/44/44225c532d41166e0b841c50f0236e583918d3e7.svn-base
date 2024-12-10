package com.proit.wicket.dataproviders;

import java.util.Iterator;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.proit.modelo.compras.Proveedor;
import com.proit.servicios.compras.ProveedorService;

public class ProveedoresDataProvider implements IDataProvider<Proveedor> {

	private static final long serialVersionUID = 1L;

	private ProveedorService proveedorService;
	
	private IModel<String> razonSocialModel;
	private IModel<String> cuitCuilModel;
	
	public ProveedoresDataProvider(IModel<String> razonSocialModel, IModel<String> cuitCuilModel) {
		this.proveedorService = new ProveedorService();
		this.razonSocialModel = razonSocialModel;
		this.cuitCuilModel = cuitCuilModel;
	}

	@Override
	public void detach() {
		this.razonSocialModel.detach();
		this.cuitCuilModel.detach();
	}

	@Override
	public Iterator<Proveedor> iterator(long first, long count) {
		return proveedorService.getProveedores(first, count, razonSocialModel.getObject(), cuitCuilModel.getObject());
	}

	@Override
	public long size() {
		return proveedorService.getProveedoresSize(razonSocialModel.getObject(), cuitCuilModel.getObject());
	}

	@Override
	public IModel<Proveedor> model(Proveedor object) {
		return new Model<Proveedor>(object);
	}
}
