package com.proit.servicios;

import java.io.Serializable;

import com.proit.modelo.ModoPago;

public class ModoPagoService extends EntidadSimpleService<ModoPago> implements Serializable {

	private static final long serialVersionUID = 1L;

	public ModoPagoService() {
		super(ModoPago.class);
	}

}