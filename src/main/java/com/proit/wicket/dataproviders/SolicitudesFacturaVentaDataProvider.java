package com.proit.wicket.dataproviders;

import java.util.Iterator;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.proit.modelo.Usuario;
import com.proit.modelo.ventas.SolicitudFacturaVenta;
import com.proit.servicios.ventas.SolicitudFacturaVentaService;

public class SolicitudesFacturaVentaDataProvider implements IDataProvider<SolicitudFacturaVenta> {
	private static final long serialVersionUID = 1L;

	private SolicitudFacturaVentaService solicitudFacturaVentaService;
	
	private IModel<String> eventoModel;
	private IModel<Usuario> usuarioSolicitanteModel;
	private IModel<Boolean> soloSolicitudesPendientes;
	private IModel<Boolean> soloSolicitudesOK;
	private IModel<Integer> idEventoModel;

	public SolicitudesFacturaVentaDataProvider(IModel<String> eventoModel, IModel<Usuario> usuarioSolicitanteModel, IModel<Boolean> soloSolicitudesPendientes, IModel<Boolean> soloSolicitudesOK, IModel<Integer> idEventoModel) {
		this.solicitudFacturaVentaService = new SolicitudFacturaVentaService();
		this.eventoModel = eventoModel;
		this.usuarioSolicitanteModel = usuarioSolicitanteModel;
		this.soloSolicitudesPendientes = soloSolicitudesPendientes;
		this.soloSolicitudesOK = soloSolicitudesOK;
		this.idEventoModel = idEventoModel;
	}

	@Override
	public void detach() {
		this.eventoModel.detach();
		this.usuarioSolicitanteModel.detach();		
		this.soloSolicitudesPendientes.detach();
		this.soloSolicitudesOK.detach();
		this.idEventoModel.detach();
	}

	@Override
	public Iterator<SolicitudFacturaVenta> iterator(long first, long count) {
		return solicitudFacturaVentaService.getSolicitudesFactura(first, count, eventoModel.getObject(), usuarioSolicitanteModel.getObject(), soloSolicitudesPendientes.getObject(), soloSolicitudesOK.getObject(), idEventoModel.getObject());
	}

	@Override
	public long size() {
		return solicitudFacturaVentaService.getSolicitudesFacturaSize(eventoModel.getObject(), usuarioSolicitanteModel.getObject(), soloSolicitudesPendientes.getObject(), soloSolicitudesOK.getObject(), idEventoModel.getObject());
	}

	@Override
	public IModel<SolicitudFacturaVenta> model(SolicitudFacturaVenta object) {
		return new Model<SolicitudFacturaVenta>(object);
	}
}
