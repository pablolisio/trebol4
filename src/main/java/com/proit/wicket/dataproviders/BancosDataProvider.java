package com.proit.wicket.dataproviders;

import java.util.Iterator;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.proit.modelo.Banco;
import com.proit.servicios.BancoService;


public class BancosDataProvider implements IDataProvider<Banco> {

	private static final long serialVersionUID = 1L;

	private BancoService bancoService;

	public BancosDataProvider() {
		this.bancoService = new BancoService();
	}

	@Override
	public void detach() {
	}

	@Override
	public Iterator<? extends Banco> iterator(long first, long count) {
		return bancoService.getBancos(first, count);
	}

	@Override
	public long size() {
		return bancoService.getBancosSize();
	}

	@Override
	public IModel<Banco> model(Banco object) {
		return new Model<Banco>(object);
	}
}
