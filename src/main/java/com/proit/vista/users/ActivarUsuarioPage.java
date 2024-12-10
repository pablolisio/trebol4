package com.proit.vista.users;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.proit.servicios.UsuarioService;
import com.proit.vista.base.FacturarOnLineBasePage;

public class ActivarUsuarioPage extends FacturarOnLineBasePage {
	private static final long serialVersionUID = 1L;
	
	public ActivarUsuarioPage(final PageParameters parameters) {
		super(parameters);
		facturarOnLineMenu.setVisible(false);
		
		String tokenPorParametro = parameters.get("tk").toString();		
		UsuarioService usuarioService = new UsuarioService();
		boolean usuarioActivado = usuarioService.activarUsuario(tokenPorParametro);
		
		WebMarkupContainer container = new WebMarkupContainer("container");
		WebMarkupContainer seccionMensajeOK = new WebMarkupContainer("seccionMensajeOK");
		WebMarkupContainer seccionMensajeERROR = new WebMarkupContainer("seccionMensajeERROR");
		WebMarkupContainer seccionBotonAcceso = new WebMarkupContainer("seccionBotonAcceso");
		
		if (usuarioActivado){
			seccionMensajeERROR.setVisible(false);
		} else {
			seccionMensajeOK.setVisible(false);
			seccionBotonAcceso.setVisible(false);
		}
		
		container.add(seccionMensajeOK);
		container.add(seccionMensajeERROR);
		container.add(seccionBotonAcceso);
		add(container);
		
	}

}
