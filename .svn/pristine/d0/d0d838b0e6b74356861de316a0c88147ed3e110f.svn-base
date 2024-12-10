package com.proit.wicket.components;

import java.util.List;

import org.apache.wicket.model.IModel;

import com.proit.servicios.compras.FacturaSolicitudPagoService;

public class NroFacturaSolicitudPagoSearchAutoCompleteTextField extends GenericSearchAutoCompleteTextField {
	private static final long serialVersionUID = 1L;
	
	public NroFacturaSolicitudPagoSearchAutoCompleteTextField(String id, IModel<String> model) {
		super(id, model);
	}

	@Override
	protected List<String> getAllStringChoices(String textToSearch) {
		FacturaSolicitudPagoService facturaSolicitudPagoService = new FacturaSolicitudPagoService();
		return facturaSolicitudPagoService.getNrosFacturaForSearchBox(textToSearch);
	}

	@Override
	protected int getMinimumAmountOfDigits() {
		return 5;
	}
		

}
