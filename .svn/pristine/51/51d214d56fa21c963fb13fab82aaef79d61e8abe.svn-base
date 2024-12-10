package com.proit.vista.users;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.proit.vista.base.FacturarOnLineBasePage;
import com.proit.vista.login.FacturarOnLineSignInPanel;

public class UsuarioNoLogueadoPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	public UsuarioNoLogueadoPage(final PageParameters parameters) {
		super(parameters);
		
		FacturarOnLineSignInPanel signInPanel = new FacturarOnLineSignInPanel("signInPanel");
		add(signInPanel);
		
		this.get("facturarOnLineMenu").setVisible(false);
		
	}

}
