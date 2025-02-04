package com.proit.wicket.components;

import java.util.List;

import org.apache.wicket.model.IModel;

import com.proit.servicios.compras.FacturaCompraService;

public class TipoYNroFacturaCompraSearchAutoCompleteTextField extends GenericSearchAutoCompleteTextField {
	private static final long serialVersionUID = 1L;
	
	private String razonSocial;
	
	public TipoYNroFacturaCompraSearchAutoCompleteTextField(String id, IModel<String> model, String razonSocial) {
		super(id, model);
		if (razonSocial == null || razonSocial.isEmpty()) {
			this.razonSocial = "laoqkc,qgqqvjj81"; //razon social inexistente para que no traiga ningun nro de factura
		} else {
			this.razonSocial = razonSocial;
		}
	}

	@Override
	protected List<String> getAllStringChoices(String textToSearch) {
		FacturaCompraService facturaCompraService = new FacturaCompraService();
		return facturaCompraService.getNrosFacturaConTipoForSearchBox(razonSocial, textToSearch);
	}

	@Override
	protected int getMinimumAmountOfDigits() {
		return 5;
	}
		

}
