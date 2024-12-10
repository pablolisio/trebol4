package com.proit.wicket.components;

import java.util.List;

import org.apache.wicket.model.IModel;

import com.proit.servicios.ventas.ClienteService;

public class ClienteSearchAutoCompleteTextField extends GenericSearchAutoCompleteTextField{
	private static final long serialVersionUID = 1L;
	
	public ClienteSearchAutoCompleteTextField(String id, IModel<String> model) {
		super(id, model);
	}
	
	@Override
	protected List<String> getAllStringChoices(String textToSearch) {
		ClienteService clienteService = new ClienteService();
		return clienteService.getClientes(textToSearch);
	}

	@Override
	protected int getMinimumAmountOfDigits() {
		return 3;
	}

}
