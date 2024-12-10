package com.proit.vista.compras.ordenes;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;

import com.proit.vista.base.FacturarOnLineBasePage;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class RegistrarOrdenPagoPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	public RegistrarOrdenPagoPage() {
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
		
	}
	
}
