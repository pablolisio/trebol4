package com.proit.vista.login;

import org.apache.wicket.authroles.authentication.panel.SignInPanel;

import com.proit.vista.base.FacturarOnLinePage;

public class FacturarOnLineSignInPanel extends SignInPanel {

	private static final long serialVersionUID = 1L;

	public FacturarOnLineSignInPanel(String id) {
		super(id);
	}
	
	@Override
	protected void onSignInSucceeded() {
		setResponsePage(FacturarOnLinePage.class);
	}
	
	@Override
	protected void onSignInFailed()	{
		// Try the component based localizer first. If not found try the
		// application localizer. Else use the default
		error(getLocalizer().getString("signInFailed", this, "Usuario y/o clave inválido/s"));
	}

}
