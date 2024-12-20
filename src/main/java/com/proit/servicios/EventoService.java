package com.proit.servicios;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.util.string.Strings;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import com.proit.modelo.Evento;
import com.proit.modelo.Usuario;
import com.proit.modelo.compras.SolicitudPago;
import com.proit.modelo.ventas.Cliente;
import com.proit.modelo.ventas.SolicitudFacturaVenta;
import com.proit.servicios.ventas.SolicitudFacturaVentaService;
import com.proit.utils.Utils;
import com.proit.wicket.FacturarOnLineAuthenticatedWebApplication;

public class EventoService extends GenericService<Evento> implements Serializable {

	private static final long serialVersionUID = 1L;

	public EventoService() {
		super(Evento.class);
	}
	
	public Object createOrUpdateSoloEvento(Evento evento) {
		return super.createOrUpdate(evento);
	}
	
	public void createOrUpdateEventoYSolicitudFacturaVenta(Evento evento, Cliente cliente, Usuario usuarioLogueado) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		evento.setBorrado(false);
	    session.saveOrUpdate(evento);
	    
		SolicitudFacturaVentaService solicitudFacturaVentaService = new SolicitudFacturaVentaService();
		SolicitudFacturaVenta solicitud = solicitudFacturaVentaService.prepararNuevaSolicitud(evento, cliente, usuarioLogueado);
		solicitudFacturaVentaService.createOrUpdate(solicitud);
	}
	
	private String buildQuery(boolean returnCount, long limit, long offset, String razonSocialCliente, String nombreEvento, Usuario usuarioSolicitante, 
								boolean soloAbiertos, boolean permitirCualquierEvento) {
		//Para escapear caso en que un nombre contenga una comilla simple. Ejemplo: Ryan's Travel S.A.
		if (razonSocialCliente!=null) {
			razonSocialCliente = razonSocialCliente.replace("\'", "\'\'");
		}
		if (nombreEvento!=null) {
			nombreEvento = nombreEvento.replace("\'", "\'\'");
		}

		String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
				"(" +
				"SELECT * " +
				"FROM evento e LEFT JOIN cliente c " +
				"ON c.id = e.cliente_id " +
				"WHERE e.borrado = false " +
				
				( !permitirCualquierEvento && !Strings.isEmpty(razonSocialCliente) ? "and lower(c.razon_social) like '%"+razonSocialCliente.toLowerCase()+"%' ":"") +
				
				//busco el nombre del evento en el nombre y tb en la razon social del cliente
				( !Strings.isEmpty(nombreEvento) ? "and (lower(e.nombre) like '%"+nombreEvento.toLowerCase()+"%' or lower(c.razon_social) like '%"+nombreEvento.toLowerCase()+"%') ":"") +
				
				( soloAbiertos ?  "and e.cerrado=false " : "") +
				( usuarioSolicitante!=null ?  "and ( " +
													"e.id in (select evento_id from orden_pago where borrado = false and usuario_solicitante_id="+usuarioSolicitante.getId()+") or " +
													"e.id in (select evento_id from factura_venta where borrado = false and usuario_solicitante_id="+usuarioSolicitante.getId()+") or " +
													"e.id in (select evento_id from solicitud_factura_venta where borrado = false and usuario_solicitante_id="+usuarioSolicitante.getId()+") or " +
													"e.id in (select evento_id from solicitud_pago where borrado = false and usuario_solicitante_id="+usuarioSolicitante.getId()+") " +
													") "
												: "") +
				"ORDER BY c.razon_social, e.nombre asc " +
				( returnCount ? "" : " LIMIT " + limit ) +
		        ( returnCount ? "" : " OFFSET " + offset ) +
				") AS EVENTOS_RESULT" ;
		return sqlString;
	}
		
	/**
	 * This method gets some info of {@link Evento}s from database.
	 * @param primerResultado First result to obtain.
	 * @param cantidadResultados Total {@link Evento}s to obtain.
	 * @return Returns List of {@link Evento}s from database.
	 */
	@SuppressWarnings("unchecked")
	public Iterator<Evento> getEventos(long primerResultado, long cantidadResultados, String nombreEvento, Usuario usuarioSolicitante, boolean soloAbiertos) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQuery(false, cantidadResultados, primerResultado, null, nombreEvento, usuarioSolicitante, soloAbiertos, false);
		Query sqlQuery = session.createSQLQuery(sqlString).addEntity(Evento.class);
		return sqlQuery.list().iterator();
	}
	
	public long getEventosSize(String nombreEvento, Usuario usuarioSolicitante, boolean soloAbiertos) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQuery(true, 0, 0, null, nombreEvento, usuarioSolicitante, soloAbiertos, false);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
//	@SuppressWarnings("unchecked")
//	public List<Evento> getEventosTodos() {
//		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
//		String sqlString = buildQuery(false, 99999999, 0, null, false);
//		Query sqlQuery = session.createSQLQuery(sqlString).addEntity(Evento.class);
//		return sqlQuery.list();
//	}
//	
//	@SuppressWarnings("unchecked")
//	public List<Evento> getEventosSoloParaSolicitantes() {
//		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
//		String sqlString = buildQuery(false, 99999999, 0, null, true);
//		Query sqlQuery = session.createSQLQuery(sqlString).addEntity(Evento.class);
//		return sqlQuery.list();
//	}
	
	@SuppressWarnings("unchecked")
	public List<String> getEventos(String razonSocialCliente, String nombreEvento, boolean soloAbiertos, boolean permitirCualquierEvento) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQuery(false, 99999999, 0, razonSocialCliente, nombreEvento, null, soloAbiertos, permitirCualquierEvento);
		sqlString = "select " 
				+ "CASE WHEN razon_social is null then nombre "
				+ "ELSE " + Utils.concatEventAndClientForDB() + " "
				+ "END as nombre"
				+ " from ("
				+ sqlString
				+ ") solo_nombres";
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}
	
	public Evento getByClienteAndNombreEvento(String razonSocialCliente, String nombreEvento) {
		Criteria criteria = getCriteriaByClienteAndNombreEvento(razonSocialCliente, nombreEvento);
		return (Evento) criteria.uniqueResult();
	}

	public boolean existsByClienteAndNombreEvento(String razonSocialCliente, String nombreEvento) {
		Criteria criteria = getCriteriaByClienteAndNombreEvento(razonSocialCliente, nombreEvento);
		criteria.setProjection(Projections.rowCount());
		return( (Long) criteria.uniqueResult() ) > 0;
	}
	
	public boolean existsByClienteAndNombreEvento(String razonSocialCliente, String nombreEvento, int idEventoActual) {
		Criteria criteria = getCriteriaByClienteAndNombreEvento(razonSocialCliente, nombreEvento);
		if (idEventoActual!=0) {
			criteria.add(Restrictions.ne("id", idEventoActual));
		}
		criteria.setProjection(Projections.rowCount());
		return( (Long) criteria.uniqueResult() ) > 0;
	}

	private Criteria getCriteriaByClienteAndNombreEvento(String razonSocialCliente, String nombreEvento) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(Evento.class)
									.createAlias("cliente", "c", JoinType.LEFT_OUTER_JOIN);
		if (!Strings.isEmpty(razonSocialCliente)) {
			criteria.add(Restrictions.eq("c.razonSocial", razonSocialCliente));
		} else {
			criteria.add(Restrictions.isNull("c.razonSocial"));
		}
		criteria.add(Restrictions.eq("nombre", nombreEvento).ignoreCase());
		criteria.add(Restrictions.eq("borrado", false));
		return criteria;
	}
	
	public String delete(Evento evento) {
		String whereIsUsed = whereIsUsed(evento);
		if ( whereIsUsed.isEmpty() ) {
			super.delete(evento.getId());
		}
		return whereIsUsed;
	}

	private String whereIsUsed(Evento evento) {
//		long cantOrdenesPago = getCantidadUsados(OrdenPago.class, "evento", evento);
//		long cantFacturasVenta = getCantidadUsados(FacturaVenta.class, "evento", evento);
//		long cantSolicitudesPago = getCantidadUsados(SolicitudPago.class, "evento", evento);
//		long cantSolicitudesFacturaVenta = getCantidadUsados(SolicitudFacturaVenta.class, "evento", evento);
//		return cantOrdenesPago>0 || cantFacturasVenta>0 || cantSolicitudesPago>0 || cantSolicitudesFacturaVenta>0;
		String result = getUsedOrdenPagoAsStr("evento", evento);
		result += result.isEmpty() ? "" : ". ";
		result += getUsedFacturaVentaAsStr("evento", evento);
		result += result.isEmpty() ? "" : ". ";
		result += getUsedSolicitudPagoAsStr("evento", evento);
		result += result.isEmpty() ? "" : ". ";
		result += getUsedSolicitudFacturaVentaAsStr("evento", evento);
		result += result.isEmpty() ? "" : ". ";
		return result;
	}
	
	private boolean eventoListoParaSerCerrado(int idEvento){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryTotalesEvento(null, false, true, true);
		sqlString = "select cumplido and valida_para_cerrar from ("
				+ sqlString
				+ ") evento_ok " 
				+ "where evento_id = " + idEvento;
		Query sqlQuery = session.createSQLQuery(sqlString);
		return (Boolean) sqlQuery.uniqueResult();
	}
	
	public boolean cerrarAbrirEvento(Evento evento) {
		if (evento.isCerrado()) { 	//intenta abrir el evento
			evento.setCerrado(false);
			createOrUpdate(evento);
		} else {					//intenta cerrar el evento
			if (eventoListoParaSerCerrado(evento.getId())) {
				evento.setCerrado(true);
				createOrUpdate(evento);
			} else {
				return false;
			}
		}
		return true;
	}
	

//	/*
//	 * USADO EN EL REPORTE DE TOTALES POR EVENTO (usando Solicitudes de Pago, NO Ordenes de Pago - Visto con Angel - 2015)
//	 * No se van a mostrar los eventos viejos porque los viejos solo se usaban en las OP, ahora en las Solicitudes de Pago solo se van a mostrar los nuevos
//	 */
//	private String buildQueryTotalesEvento(boolean returnCount) {
//		String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
//        		"(" +
//	        		"select " +
//	        		"cliente, evento, sum(total_pendiente) as total_pendiente, sum (total_cumplido) as total_cumplido, sum (total_solicitado) as total_solicitado " +
//	        		"from " +         		
//						"( " + 
//							"select  " + 
//							"c.razon_social as cliente,  " + 
//							"e.nombre as evento,  " + 
//							"CASE " + 
//							    "WHEN s.estado_solicitud_pago_id=" + EstadoSolicitudPago.PENDIENTE_1.getId() + " or s.estado_solicitud_pago_id=" + EstadoSolicitudPago.PENDIENTE_2.getId() + " or s.estado_solicitud_pago_id=" + EstadoSolicitudPago.PENDIENTE_3.getId() + " THEN sum(p.importe) " + 
//							    "ELSE 0 " + 
//							  "END  " + 
//							  "AS total_pendiente, " + 
//							"CASE " + 
//							    "WHEN s.estado_solicitud_pago_id=" + EstadoSolicitudPago.CUMPLIDA.getId() + " THEN sum(p.importe) " + 
//							    "ELSE 0 " + 
//							  "END  " + 
//							  "AS total_cumplido, " + 
//							"sum(p.importe) as total_solicitado " + 
//							"from solicitud_pago s, cliente c, evento e, pago_solicitud_pago p  " + 
//							"where s.cliente_id = c.id and s.evento_id = e.id and s.id = p.solicitud_pago_id " + 
//							"and s.estado_solicitud_pago_id <>  " + EstadoSolicitudPago.RECHAZADA.getId() + " " + 
//							"and p.borrado = false " + 
//							"and s.borrado = false " + 
//							"group by c.razon_social, e.nombre, s.estado_solicitud_pago_id " + 
//							"order by c.razon_social, e.nombre, s.estado_solicitud_pago_id " + 
//						") as aux " + 
//					"group by cliente, evento " + 
//					"order by cliente, evento " + 
//		        ") AS TOTALES_EVENTO";
//		return sqlString;
//	}
	
	/*
	 * USADO EN EL REPORTE DE TOTALES POR EVENTO (usando Ordenes de Pago - Visto con Angel Julio 2016)
	 */
	private String buildQueryTotalesEvento(String nombreEvento, boolean returnCount, boolean verTodos, boolean isUsuarioLogueadoRolAdministrador) {
		String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
        		"(" +
        		"select q1.evento_id, q1.cliente, q1.evento, q1.fecha, " +
        		"costo_total, costo_total_con_iva,  " +
        		"CASE WHEN (pagado_total is null) THEN 0 ELSE pagado_total END pagado_total, " +
        		"(costo_total_con_iva-(CASE WHEN (pagado_total is null) THEN 0 ELSE pagado_total END)) as pendiente_total, " +
        		//"suma_fac_vta, total_evento, total_evento_con_iva, (suma_fac_vta>0 and suma_fac_vta=total_evento) as cumplido,  " + //antes del pedido por bianca
        		"suma_fac_vta, total_evento, total_evento_con_iva, (ABS(suma_fac_vta - total_evento) <= 0.01) as cumplido,  " + //pedido por bianca
        		"q1.cerrado, q1.costo_final, " +
//        		"((suma_fac_vta=total_evento) and (costo_total_con_iva=CASE WHEN (pagado_total is null) THEN 0 ELSE pagado_total END)) as cerrado " +
				//"((costo_total_con_iva>=0) and (costo_total_con_iva=CASE WHEN (pagado_total is null) THEN 0 ELSE pagado_total END)) as valida_para_cerrar " + //antes del pedido por bianca
				"(ABS(costo_total_con_iva - (CASE WHEN (pagado_total is null) THEN 0 ELSE pagado_total END)) <= 0.01) as valida_para_cerrar, " + //pedido por bianca
				"solo_administradores, " +
				"responsable_evento " +
        		"from suma_fac_vta_por_evento q1 left join " +
        		"(select o.evento_id as evento_id, sum(p.importe) as pagado_total " +
        		"from orden_pago o, pago p  " +
        		"where o.id = p.orden_pago_id " +
        		"and p.borrado = false " +
        		"and o.borrado = false  " +
        		"group by o.evento_id " +
        		"order by o.evento_id " +
        		") q2 ON q1.evento_id = q2.evento_id " +
		        ") AS TOTALES_EVENTO " +
        		"where 1=1 " +
        		(nombreEvento!=null?"and (lower(evento) like '%"+nombreEvento.toLowerCase()+"%' or lower(cliente) like '%"+nombreEvento.toLowerCase()+"%') ":"") + 
//		        (soloNoCumplidos?"and cumplido = false ":"") +
        		(verTodos ? "" : "and (cerrado = false or cumplido = false) ") + //si no tilda "ver todos" trae los que estan abiertos y los no cumplidos
        		(isUsuarioLogueadoRolAdministrador ? "" : "and solo_administradores = false ") + //si el usuario logueado NO es administrador, solo traigo los que solo_administradores=false
		        (returnCount?"":"order by cliente asc, evento asc ");
		return sqlString;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getTotalesPorEvento(String evento, boolean verTodos, boolean isUsuarioLogueadoRolAdministrador){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryTotalesEvento(evento, false, verTodos, isUsuarioLogueadoRolAdministrador);
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}
	
	public long getTotalesPorEventoSize(String evento, boolean verTodos, boolean isUsuarioLogueadoRolAdministrador){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryTotalesEvento(evento, true, verTodos, isUsuarioLogueadoRolAdministrador);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
	public double calculateSumTotalesPorEvento(String nombreAtt, String evento, boolean verTodos, boolean isUsuarioLogueadoRolAdministrador) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryTotalesEvento(evento, false, verTodos, isUsuarioLogueadoRolAdministrador);
		sqlString = "select sum("+nombreAtt+") from ("
				+ sqlString
				+ ") suma";
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigDecimal sum = (BigDecimal) sqlQuery.uniqueResult();
		if (sum!=null) {
			return (Double) sum.doubleValue();
		} else {
			return 0d;
		}
	}
	
	public void updateEventoOPYSolPago(int ordenPagoId, int nuevoEventoId, SolicitudPago solicitudPago) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString1 = "UPDATE orden_pago set evento_id = " + nuevoEventoId + " where id = " + ordenPagoId;		
		Query sqlQuery1 = session.createSQLQuery(sqlString1);		
		sqlQuery1.executeUpdate();
		
		if (solicitudPago!=null) {
			String sqlString2 = "UPDATE solicitud_pago set evento_id = " + nuevoEventoId + " where id = " + solicitudPago.getId();
			Query sqlQuery2 = session.createSQLQuery(sqlString2);
			sqlQuery2.executeUpdate();
		}
	}
	
	public void updateEventoFacturaVentaYSolFacturaVenta(int facturaVentaId, int nuevoEventoId, SolicitudFacturaVenta solicitudFacturaVenta) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString1 = "UPDATE factura_venta set evento_id = " + nuevoEventoId + " where id = " + facturaVentaId;		
		Query sqlQuery1 = session.createSQLQuery(sqlString1);		
		sqlQuery1.executeUpdate();
		
		if (solicitudFacturaVenta!=null) {
			String sqlString2 = "UPDATE solicitud_factura_venta set evento_id = " + nuevoEventoId + " where id = " + solicitudFacturaVenta.getId();
			Query sqlQuery2 = session.createSQLQuery(sqlString2);
			sqlQuery2.executeUpdate();
		}
	}
	
}