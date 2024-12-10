package com.proit.vista.compras.solicitudes;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;

import com.proit.vista.base.FacturarOnLineBasePage;

@AuthorizeInstantiation({"Administrador","Desarrollador","Solicitante Pagos"})
public class RegistrarSolicitudPagoPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	public RegistrarSolicitudPagoPage() {
		
		facturarOnLineMenu.setearMenuActivo(false, true, false, false);
		
	}
	
}
