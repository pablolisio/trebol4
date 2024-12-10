package com.proit.wicket.dataproviders;

import java.util.Iterator;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.proit.modelo.ventas.Cliente;
import com.proit.servicios.ventas.ClienteService;

public class ClientesDataProvider implements IDataProvider<Cliente> {

	private static final long serialVersionUID = 1L;

	private ClienteService clienteService;
	
	private IModel<String> razonSocialModel;
	private IModel<String> cuitCuilModel;
	
	public ClientesDataProvider(IModel<String> razonSocialModel, IModel<String> cuitCuilModel) {
		this.clienteService = new ClienteService();
		this.razonSocialModel = razonSocialModel;
		this.cuitCuilModel = cuitCuilModel;
	}

	@Override
	public void detach() {
		this.razonSocialModel.detach();
		this.cuitCuilModel.detach();
	}

	@Override
	public Iterator<Cliente> iterator(long first, long count) {
		return clienteService.getClientes(first, count, razonSocialModel.getObject(), cuitCuilModel.getObject());
	}

	@Override
	public long size() {
		return clienteService.getClientesSize(razonSocialModel.getObject(), cuitCuilModel.getObject());
	}

	@Override
	public IModel<Cliente> model(Cliente object) {
		return new Model<Cliente>(object);
	}
}
