package com.proit.vista.reportes.compras;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.proit.vista.reportes.ReporteBasePage;

@AuthorizeInstantiation({"Administrador","Desarrollador"})
public class DeudasVsPagosPage extends ReporteBasePage{
	private static final long serialVersionUID = 1L;
	
	public DeudasVsPagosPage(PageParameters parameters) {
		super(parameters, DEUDAS_VS_PAGOS);
		
		Label listado = crearListado("getListadoDeudasVsPagosJS", getListado(mesDesdeSeleccionado, mesHastaSeleccionado));
		crearMesDropDownChoice(true,listado);
		crearMesDropDownChoice(false,listado);
	}

}
