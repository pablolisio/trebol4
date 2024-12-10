package com.proit.wicket.components;

import java.util.List;

import org.apache.wicket.model.IModel;

import com.proit.servicios.compras.ProveedorService;

public class CuitCuilProveedorSearchAutoCompleteTextField extends GenericSearchAutoCompleteTextField{
	private static final long serialVersionUID = 1L;
		
	public CuitCuilProveedorSearchAutoCompleteTextField(String id, IModel<String> model) {
		super(id, model);	
	}
	
	@Override
	protected List<String> getAllStringChoices(String textToSearch) {
		ProveedorService proveedorService = new ProveedorService();
		return proveedorService.getCuitCuilProveedores(textToSearch);
	}

	@Override
	protected int getMinimumAmountOfDigits() {
		return 5;
	}

}
