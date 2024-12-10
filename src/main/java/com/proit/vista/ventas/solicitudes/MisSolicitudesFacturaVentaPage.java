package com.proit.vista.ventas.solicitudes;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;

@AuthorizeInstantiation({"Solicitante Facturas Ventas","Desarrollador"})
public class MisSolicitudesFacturaVentaPage extends SolicitudesFacturaVentaPage{
	private static final long serialVersionUID = 1L;

	public MisSolicitudesFacturaVentaPage(PageParameters parameters) {
		super(parameters, true);
	}
	

}
