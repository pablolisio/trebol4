package com.proit.wicket.dataproviders;

import java.util.Iterator;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.proit.modelo.compras.CobroAlternativo;
import com.proit.servicios.compras.CobroAlternativoService;

public class CobrosAlternativosDataProvider implements IDataProvider<CobroAlternativo> {

	private static final long serialVersionUID = 1L;

	private CobroAlternativoService cobroAlternativoService;
	
	private int idProveedor;
	
	public CobrosAlternativosDataProvider(int idProveedor) {
		this.cobroAlternativoService = new CobroAlternativoService();
		this.idProveedor = idProveedor;
	}

	@Override
	public void detach() {
		//this.idProveedorModel.detach();
	}

	@Override
	public Iterator<CobroAlternativo> iterator(long first, long count) {
		return cobroAlternativoService.getCobrosAlternativos(first, count, idProveedor);
	}

	@Override
	public long size() {
		return cobroAlternativoService.getCobrosAlternativosSize(idProveedor);
	}

	@Override
	public IModel<CobroAlternativo> model(CobroAlternativo object) {
		return new Model<CobroAlternativo>(object);
	}
}
