package com.proit.vista.others;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.proit.vista.base.FacturarOnLineBasePage;

public class ErrorPage404 extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	public ErrorPage404(final PageParameters parameters) {
		super(parameters);
		facturarOnLineMenu.setVisible(false);
	}

}
