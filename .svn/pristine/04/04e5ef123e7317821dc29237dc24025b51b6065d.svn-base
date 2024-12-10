package com.proit.wicket.dataproviders;

import java.util.Iterator;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.proit.modelo.Usuario;
import com.proit.modelo.compras.SolicitudPago;
import com.proit.servicios.compras.SolicitudPagoService;

public class SolicitudesPagoDataProvider implements IDataProvider<SolicitudPago> {
	private static final long serialVersionUID = 1L;

	private SolicitudPagoService solicitudPagoService;
	
	private IModel<String> nroFacturaModel;
	private IModel<Usuario> usuarioSolicitanteModel;
	private IModel<String> eventoModel;
	private IModel<Boolean> soloSolicitudesPendientes;

	public SolicitudesPagoDataProvider(IModel<String> nroFacturaModel, IModel<Usuario> usuarioSolicitanteModel, IModel<String> eventoModel, IModel<Boolean> soloSolicitudesPendientes) {
		this.solicitudPagoService = new SolicitudPagoService();
		this.nroFacturaModel = nroFacturaModel;
		this.usuarioSolicitanteModel = usuarioSolicitanteModel;
		this.eventoModel = eventoModel;
		this.soloSolicitudesPendientes = soloSolicitudesPendientes;
	}

	@Override
	public void detach() {
		this.nroFacturaModel.detach();
		this.usuarioSolicitanteModel.detach();
		this.eventoModel.detach();
		this.soloSolicitudesPendientes.detach();
	}

	@Override
	public Iterator<SolicitudPago> iterator(long first, long count) {
		return solicitudPagoService.getListaSolicitudesPago(first, count, nroFacturaModel.getObject(), usuarioSolicitanteModel.getObject(), eventoModel.getObject(), soloSolicitudesPendientes.getObject());
	}

	@Override
	public long size() {
		return solicitudPagoService.getListaSolicitudesPagoSize(nroFacturaModel.getObject(), usuarioSolicitanteModel.getObject(), eventoModel.getObject(), soloSolicitudesPendientes.getObject());
	}

	@Override
	public IModel<SolicitudPago> model(SolicitudPago object) {
		return new Model<SolicitudPago>(object);
	}
}
