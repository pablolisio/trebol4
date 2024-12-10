package com.proit.wicket.dataproviders;

import java.util.Iterator;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.proit.modelo.Usuario;
import com.proit.servicios.UsuarioService;

public class UsuariosDataProvider implements IDataProvider<Usuario> {

	private static final long serialVersionUID = 1L;

	private UsuarioService usuarioService;
	
	private IModel<String> nombreModel;
	private IModel<String> apellidoModel;
	private IModel<String> emailModel;
	private boolean isUsuarioLogueadoRolDesarrollador;
	
	public UsuariosDataProvider(IModel<String> nombreModel, IModel<String> apellidoModel, IModel<String> emailModel, boolean isUsuarioLogueadoRolDesarrollador) {
		this.usuarioService = new UsuarioService();
		this.nombreModel = nombreModel;
		this.apellidoModel = apellidoModel;
		this.emailModel = emailModel;
		this.isUsuarioLogueadoRolDesarrollador = isUsuarioLogueadoRolDesarrollador;
	}

	@Override
	public void detach() {
		this.nombreModel.detach();
		this.apellidoModel.detach();
		this.emailModel.detach();
	}

	@Override
	public Iterator<Usuario> iterator(long first, long count) {
		return usuarioService.getUsuarios(first, count, nombreModel.getObject(), apellidoModel.getObject(), emailModel.getObject(), isUsuarioLogueadoRolDesarrollador);
	}

	@Override
	public long size() {
		return usuarioService.getUsuariosSize(nombreModel.getObject(), apellidoModel.getObject(), emailModel.getObject(), isUsuarioLogueadoRolDesarrollador);
	}

	@Override
	public IModel<Usuario> model(Usuario object) {
		return new Model<Usuario>(object);
	}
}
