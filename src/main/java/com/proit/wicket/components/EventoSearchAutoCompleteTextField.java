package com.proit.wicket.components;

import java.util.List;

import org.apache.wicket.model.IModel;

import com.proit.servicios.EventoService;

public class EventoSearchAutoCompleteTextField extends GenericSearchAutoCompleteTextField{
	private static final long serialVersionUID = 1L;
	
	private String razonSocialCliente;
	
	private boolean soloAbiertos;
	
	private boolean permitirCualquierEvento;
	
	public EventoSearchAutoCompleteTextField(String id, IModel<String> model, boolean soloAbiertos) {
		super(id, model);
		this.soloAbiertos = soloAbiertos;
	}
	
	public String getRazonSocialCliente() {
		return razonSocialCliente;
	}

	public void setRazonSocialCliente(String razonSocialCliente) {
		this.razonSocialCliente = razonSocialCliente;
	}

	public boolean isPermitirCualquierEvento() {
		return permitirCualquierEvento;
	}

	public void setPermitirCualquierEvento(boolean permitirCualquierEvento) {
		this.permitirCualquierEvento = permitirCualquierEvento;
	}

	@Override
	protected List<String> getAllStringChoices(String textToSearch) {
		EventoService eventoService = new EventoService();
		return eventoService.getEventos(getRazonSocialCliente(), textToSearch, soloAbiertos, permitirCualquierEvento);
	}

	@Override
	protected int getMinimumAmountOfDigits() {
		return 3;
	}

}
