package com.proit.servicios;

import java.io.Serializable;

import com.proit.modelo.TipoCuenta;

public class TipoCuentaService extends EntidadSimpleService<TipoCuenta> implements Serializable {

	private static final long serialVersionUID = 1L;

	public TipoCuentaService() {
		super(TipoCuenta.class);
	}

}