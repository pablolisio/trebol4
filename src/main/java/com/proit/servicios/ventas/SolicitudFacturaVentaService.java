package com.proit.servicios.ventas;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.util.string.Strings;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.proit.modelo.Evento;
import com.proit.modelo.TipoFactura;
import com.proit.modelo.Usuario;
import com.proit.modelo.ventas.Cliente;
import com.proit.modelo.ventas.DatoAdicionalSolicitudFacturaVenta;
import com.proit.modelo.ventas.DetalleSolicitudFacturaVenta;
import com.proit.modelo.ventas.EstadoSolicitudFactura;
import com.proit.modelo.ventas.SolicitudFacturaVenta;
import com.proit.servicios.GenericService;
import com.proit.wicket.FacturarOnLineAuthenticatedWebApplication;

public class SolicitudFacturaVentaService extends GenericService<SolicitudFacturaVenta> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public SolicitudFacturaVentaService() {
		super(SolicitudFacturaVenta.class);
	}
	
	public SolicitudFacturaVenta prepararNuevaSolicitud(Evento evento, Cliente cliente, Usuario usuarioLogueado) { //TODO falta - revisar - done
		SolicitudFacturaVenta solicitud = new SolicitudFacturaVenta(Calendar.getInstance(), cliente, evento, TipoFactura.TIPO_A, usuarioLogueado, EstadoSolicitudFactura.PENDIENTE);
		solicitud.setListadoDetalles(new ArrayList<DetalleSolicitudFacturaVenta>());
		solicitud.setListadoDatosAdicionales(new ArrayList<DatoAdicionalSolicitudFacturaVenta>());
		
		//Agrego un Detalle por defecto en el listado de Detalles
		DetalleSolicitudFacturaVenta detalle = new DetalleSolicitudFacturaVenta();
		detalle.setSolicitudFacturaVenta(solicitud);
		detalle.setCantidad(1);
		detalle.setDescripcion("<Falta completar>");
		detalle.setImporte(evento.getTotalEvento());//Uso el total del evento
		solicitud.getListadoDetalles().add(detalle);
		
		solicitud.setNro(getNextNroSolicitud()); //es el ultimo paso
		
		return solicitud;
	}
	
 	private String buildQuery(boolean returnCount, long limit, long offset, String nombreEvento, Usuario usuarioSolicitante, 
 			boolean soloSolicitudesPendientes, boolean soloSolicitudesOK, Integer idEvento) {
		//Para escapear caso en que un nombre contenga una comilla simple. Ejemplo: Ryan's Travel S.A.
		if (nombreEvento!=null) {
			nombreEvento = nombreEvento.replace("\'", "\'\'");
		}

		String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
				"(" +
				"select s.* " +
				"FROM solicitud_factura_venta s " +
				"left join evento on s.evento_id = evento.id " +
				"left join cliente on evento.cliente_id = cliente.id " +
				"WHERE s.borrado = false " +
				
				//busco el nombre del evento en el nombre y tb en la razon social del cliente
				( !Strings.isEmpty(nombreEvento) ? "and (lower(evento.nombre) like '%"+nombreEvento.toLowerCase()+"%' or lower(cliente.razon_social) like '%"+nombreEvento.toLowerCase()+"%') ":"") +
				
				( usuarioSolicitante!=null ?  "and usuario_solicitante_id = " + usuarioSolicitante.getId() + " " : "") +
				
				( soloSolicitudesPendientes ?  "and estado_solicitud_factura_venta_id = " + EstadoSolicitudFactura.PENDIENTE.getId() + " " : "") +
				
				//Sol Fac Vta OK = (Si tiene Nro Orden de Compra o Nro Requisicion)
				( soloSolicitudesOK ?  "and ((s.nro_orden_compra is not null and s.nro_orden_compra <> '') OR (s.nro_requisicion is not null and s.nro_requisicion <> ''))" : "") +				
				
				( idEvento!=null ? " and s.evento_id = '" + idEvento + "'" : "" ) + 
				
				"ORDER BY s.fecha desc " + //en feb/2018, me pidio angela que lo cambie a de mas nuevo a mas viejo
				( returnCount ? "" : " LIMIT " + limit ) +
		        ( returnCount ? "" : " OFFSET " + offset ) +
				") AS SOLICITUDES_FACTURA_RESULT" ;
		return sqlString;
	}
	
	/**
	 * This method gets some info of {@link SolicitudFacturaVenta}s from database.
	 * @param primerResultado First result to obtain.
	 * @param cantidadResultados Total {@link SolicitudFacturaVenta}s to obtain.
	 * @return Returns {@link SolicitudFacturaVenta}s from database.
	 */
	@SuppressWarnings("unchecked")
	public Iterator<SolicitudFacturaVenta> getSolicitudesFactura(long primerResultado, long cantidadResultados, String nombreEvento, Usuario usuarioSolicitante, 
			boolean soloSolicitudesPendientes, boolean soloSolicitudesOK, Integer idEvento) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQuery(false, cantidadResultados, primerResultado, nombreEvento, usuarioSolicitante, soloSolicitudesPendientes, soloSolicitudesOK, idEvento);
		Query sqlQuery = session.createSQLQuery(sqlString).addEntity(SolicitudFacturaVenta.class);
		return sqlQuery.list().iterator();
	}
	
	public long getSolicitudesFacturaSize(String nombreEvento, Usuario usuarioSolicitante, boolean soloSolicitudesPendientes, boolean soloSolicitudesOK, Integer idEvento) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQuery(true, 0, 0, nombreEvento, usuarioSolicitante, soloSolicitudesPendientes, soloSolicitudesOK, idEvento);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
	@SuppressWarnings("unchecked")
	public List<SolicitudFacturaVenta> getSolicitudesFactura(String nombreEvento, Usuario usuarioSolicitante, boolean soloSolicitudesPendientes, boolean soloSolicitudesOK, Integer idEvento) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQuery(false, 99999999, 0, nombreEvento, usuarioSolicitante, soloSolicitudesPendientes, soloSolicitudesOK, idEvento);
		Query sqlQuery = session.createSQLQuery(sqlString).addEntity(SolicitudFacturaVenta.class);
		return sqlQuery.list();
	}
	
	public double getTotalSolicitudesFacturaPendientes() {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = "select * from total_solicitudes_factura_pendientes";
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigDecimal total = (BigDecimal) sqlQuery.uniqueResult();
		return total.doubleValue();
	}
	
	/**
	 * Obtengo el Proximo Nro Solicitud a utilizar, sin importar si la ultima solicitud fue borrada (Nro Solicitud debe ser unico)
	 * Formato Ejemplo: 00001  (5 digitos)
	 * @return
	 */
	public String getNextNroSolicitud() {
		long maxNro = getMaxNroSolicitudLong();
		String nextNroSolicitud;
		if (maxNro==0) { //Primer Solicitud a crear
			nextNroSolicitud = "00001";
		} else {
			maxNro ++;
			nextNroSolicitud = Long.toString(maxNro);
			nextNroSolicitud = String.format("%5s", nextNroSolicitud).replace(' ', '0');
		}
		return nextNroSolicitud;
	}
	
	private long getMaxNroSolicitudLong() {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(SolicitudFacturaVenta.class);
		//criteria.add(Restrictions.eq("borrado", false)); Busco todos los Nro, tb los nros de Sol borradas, porque el Nro debe ser unico
		criteria.setProjection(Projections.max("nro"));
		String maxNroStr = (String) criteria.uniqueResult();
		long maxNro;
		if (maxNroStr==null) { //Primer Solicitud a crear
			maxNro = 0;
		} else {
			maxNro = Long.parseLong(maxNroStr);
		}
		return maxNro;
	}
	
	public boolean delete(SolicitudFacturaVenta solicitudFacturaVenta) {
		if ( ! isBeingUsed(solicitudFacturaVenta) ) {
			Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
						
			//Elimino los Datos Adicionales asociados
			for (DatoAdicionalSolicitudFacturaVenta datoAdicional : solicitudFacturaVenta.getListadoDatosAdicionales()) {
				datoAdicional.setBorrado(true);
			    session.update(datoAdicional);
			}
			
			//Elimino los detalles asociados
			for (DetalleSolicitudFacturaVenta detalle : solicitudFacturaVenta.getListadoDetalles()) {
				detalle.setBorrado(true);
				session.merge(detalle);
			}
			
			//Finalmente elimino la Solicitud
			super.delete(solicitudFacturaVenta.getId());
				
			return true;
		}
		return false;
	}
	
	private boolean isBeingUsed(SolicitudFacturaVenta solicitudFacturaVenta) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(SolicitudFacturaVenta.class);
		criteria.add(Restrictions.eq("id", solicitudFacturaVenta.getId()));
		criteria.add(Restrictions.eq("borrado", false));
		criteria.setProjection(Projections.property("estadoSolicitudFactura"));	
		EstadoSolicitudFactura estado = (EstadoSolicitudFactura) criteria.uniqueResult();
		if (estado.equals(EstadoSolicitudFactura.CUMPLIDA) || estado.equals(EstadoSolicitudFactura.RECHAZADA)) {
			return true;
		}
		return false;
	}

	public boolean todoDetalleTieneCantidadMayorACero(List<DetalleSolicitudFacturaVenta> listaDetalles) {
		for (DetalleSolicitudFacturaVenta detalle : listaDetalles){
			if (detalle.getCantidad()<1){
				return false;
			}
		}
		return true;
	}

	public boolean todoDetalleTieneImporteDistintoACero(List<DetalleSolicitudFacturaVenta> listaDetalles) {
		for (DetalleSolicitudFacturaVenta detalle : listaDetalles){
			if (detalle.getImporte()==0){
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public double calculateSumSubtotal(Integer idEvento, String nombreEvento, boolean soloSolicitudesPendientes, boolean ocultarFacturasTipoN){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = "select s.tipo_factura_id, SUM(d.cantidad * d.importe) as subtotal ";
		sqlString += "from solicitud_factura_venta s , detalle_solicitud_factura_venta d, evento e, cliente c ";
		sqlString += "where d.solicitud_factura_venta_id = s.id and s.evento_id = e.id and s.cliente_id = c.id ";
		sqlString += "and s.borrado = false and d.borrado = false ";
		sqlString += ocultarFacturasTipoN ? "and s.tipo_factura_id NOT IN (" + TipoFactura.TIPO_N.getId() + ", " + + TipoFactura.NOTA_CRED_N.getId() + ") " : "";		
		sqlString += soloSolicitudesPendientes ? "and s.estado_solicitud_factura_venta_id = " + EstadoSolicitudFactura.PENDIENTE.getId() + " " : "";
		
		if (idEvento!=null) {
			sqlString += "and s.evento_id = " + idEvento + " ";
		} else if (!Strings.isEmpty(nombreEvento)) {
			sqlString += "and (lower(e.nombre) like '%"+nombreEvento.toLowerCase()+"%' or lower(c.razon_social) like '%"+nombreEvento.toLowerCase()+"%') ";
		}
		
		sqlString += "group by tipo_factura_id";
		Query sqlQuery = session.createSQLQuery(sqlString);
		List<Object[]> queryResult = sqlQuery.list();
		double result = 0d;
		for (Object[] obj : queryResult) {
			int idTipoFactura = ((Integer) obj[0]).intValue();
			double valor = ((BigDecimal) obj[1]).doubleValue();
			if (idTipoFactura == TipoFactura.NOTA_CRED_A.getId() || idTipoFactura == TipoFactura.NOTA_CRED_B.getId() || idTipoFactura == TipoFactura.NOTA_CRED_C.getId() || idTipoFactura == TipoFactura.NOTA_CRED_E.getId() || idTipoFactura == TipoFactura.NOTA_CRED_N.getId() || idTipoFactura == TipoFactura.NOTA_CRED_FCE.getId()) { //TODO falta done
				result -= valor;
			} else {
				result += valor;
			}
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public double calculateSumIVA(Integer idEvento, String nombreEvento, boolean soloSolicitudesPendientes){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = "select tipo_factura_id as tipo_factura_id, SUM(iva) as iva from ( ";
		sqlString += "select s.tipo_factura_id as tipo_factura_id, ";
		sqlString += "CASE WHEN (s.tipo_factura_id in ("+TipoFactura.TIPO_A.getId()+", "+TipoFactura.NOTA_DEB_A.getId()+", "+TipoFactura.NOTA_CRED_A.getId()+", "+TipoFactura.TIPO_FCE.getId()+", "+TipoFactura.NOTA_DEB_FCE.getId()+", "+TipoFactura.NOTA_CRED_FCE.getId()+")) ";
		sqlString += " THEN (d.cantidad * d.importe * 0.21) "; //TODO falta done
		sqlString += " ELSE 0 ";
		sqlString += "END as iva ";
		sqlString += "from solicitud_factura_venta s , detalle_solicitud_factura_venta d, evento e, cliente c ";
		sqlString += "where d.solicitud_factura_venta_id = s.id and s.evento_id = e.id and s.cliente_id = c.id ";
		sqlString += "and s.borrado = false and d.borrado = false ";
		sqlString += soloSolicitudesPendientes ? "and s.estado_solicitud_factura_venta_id = " + EstadoSolicitudFactura.PENDIENTE.getId() + " " : "";
		
		if (idEvento!=null) {
			sqlString += "and s.evento_id = " + idEvento + " ";
		} else if (!Strings.isEmpty(nombreEvento)) {
			sqlString += "and (lower(e.nombre) like '%"+nombreEvento.toLowerCase()+"%' or lower(c.razon_social) like '%"+nombreEvento.toLowerCase()+"%') ";
		}

		sqlString += ") as calculo_iva ";
		sqlString += "group by tipo_factura_id ";
		Query sqlQuery = session.createSQLQuery(sqlString);
		List<Object[]> queryResult = sqlQuery.list();
		double result = 0d;
		for (Object[] obj : queryResult) {
			int idTipoFactura = ((Integer) obj[0]).intValue();
			double valor = ((BigDecimal) obj[1]).doubleValue();
			if (idTipoFactura == TipoFactura.NOTA_CRED_A.getId() || idTipoFactura == TipoFactura.NOTA_CRED_B.getId() || idTipoFactura == TipoFactura.NOTA_CRED_C.getId() || idTipoFactura == TipoFactura.NOTA_CRED_E.getId() || idTipoFactura == TipoFactura.NOTA_CRED_N.getId() || idTipoFactura == TipoFactura.NOTA_CRED_FCE.getId()) { //TODO falta done
				result -= valor;
			} else {
				result += valor;
			}
		}
		return result;
	}
}
