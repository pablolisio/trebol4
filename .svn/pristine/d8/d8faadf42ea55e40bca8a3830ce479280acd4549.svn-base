package com.proit.wicket.dataproviders;

import java.util.Iterator;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.proit.modelo.Evento;
import com.proit.modelo.Usuario;
import com.proit.servicios.EventoService;


public class EventosDataProvider implements IDataProvider<Evento> {

	private static final long serialVersionUID = 1L;

	private EventoService eventoService;
	
	private IModel<String> eventoModel;
	private IModel<Usuario> usuarioSolicitanteModel;
	private IModel<Boolean> soloAbiertosModel;

	public EventosDataProvider(IModel<String> eventoModel, IModel<Usuario> usuarioSolicitanteModel, IModel<Boolean> soloAbiertosModel) {
		this.eventoService = new EventoService();
		this.eventoModel = eventoModel;
		this.usuarioSolicitanteModel = usuarioSolicitanteModel;
		this.soloAbiertosModel = soloAbiertosModel;
	}

	@Override
	public void detach() {
		this.eventoModel.detach();
		this.usuarioSolicitanteModel.detach();
		this.soloAbiertosModel.detach();
	}

	@Override
	public Iterator<? extends Evento> iterator(long first, long count) {
		return eventoService.getEventos(first, count, eventoModel.getObject(), usuarioSolicitanteModel.getObject(), soloAbiertosModel.getObject());
	}

	@Override
	public long size() {
		return eventoService.getEventosSize(eventoModel.getObject(), usuarioSolicitanteModel.getObject(), soloAbiertosModel.getObject());
	}

	@Override
	public IModel<Evento> model(Evento object) {
		return new Model<Evento>(object);
	}
}
