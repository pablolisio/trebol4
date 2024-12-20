package com.proit.servicios;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.proit.utils.Constantes;
import com.proit.wicket.FacturarOnLineAuthenticatedWebApplication;

public class ReportesDuplicadosService implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String IDS_YA_REVISADOS_OPS = "6581,6582,6632,6633,6660,6661,7036,7037,7089,7090,7103,7104,7307,7308"; //Todos ya trabajados
	private static final String IDS_YA_REVISADOS_ADIC_SOL_FACT_VTA = "760,761"; //Son de una solicitud de Fact Vta del 06-06-2018
	private static final String IDS_YA_REVISADOS_DETALLE_SOL_FACT_VTA = "1231,1232"; //Uno de los dos fue borrado en el momento
	private static final String IDS_YA_REVISADOS_COBRANZAS = "211,212,269,270,488,489"; //211 y 212, una fue eliminada - 269 y 270 son de distinto cliente (lo mismo 488 y 489)
	private static final String IDS_YA_REVISADOS_PAGO_SOL_PAGO = "32,33,34,41,42,43,67,68"; //32,33,34 fueron 3 cheques del mismo importe. 41,42,43 fueron 3 cheques del mismo importe. 67,68 son 2 transfs.
	private static final String IDS_YA_REVISADOS_PAGO = "406,407,600,601"; //406,407 son 2 transfs. 600,601 son 2 transfs
	private static final String IDS_YA_REVISADOS_CAJA_CHICA = "267,268"; //267 y 268 son muy viejos (de agosto 2016)
	private static final String IDS_YA_REVISADOS_CTAS_BCARIAS = "64,65,295,296,492,493,536,537,390,391,392,978,979"; // 64,65 borradas.295,296 una fue borrada.536,537 una fue borrada.492,493 son 2 mismos cbus pero uno se uso en pago_solicitud_pago y el otro lo tiene un proveedor asignado. 390,391,392 ya revisadas por Angela.978,979 una fue borrada.
	private static final String ANIO_A_REVISAR_DUPLICADOS = "2019-01-01 00:00:00";

	// ***********************************************************************
	
	public List<String> getTitulosDuplicadosSolicitudFacturaVenta() {
		return Arrays.asList(new String [] {" - - - ID 1 - - - ","borrado 1","fecha 1","nro 1","cliente_id 1","evento_id 1","tipo_factura_id 1","nro_orden_compra 1","nro_requisicion 1","observaciones 1","factura_a_anular_id 1","usuario_solicitante_id 1","estado_solicitud_factura_venta_id 1","permitir_cualquier_evento 1"," - - - ID 2 - - - ","borrado 2","fecha 2","nro 2","cliente_id 2","evento_id 2","tipo_factura_id 2","nro_orden_compra 2","nro_requisicion 2","observaciones 2","factura_a_anular_id 2","usuario_solicitante_id 2","estado_solicitud_factura_venta_id 2","permitir_cualquier_evento 2"});
	}	
	private String buildQueryDuplicadosSolicitudFacturaVenta(boolean returnCount) {				
        String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
        		"(" +
        		"select  " + 
        		"t1.id as id1,t1.borrado as borrado1,t1.fecha as fecha1,t1.nro as nro1,t1.cliente_id as cliente_id1,t1.evento_id as evento_id1,t1.tipo_factura_id as tipo_factura_id1,t1.nro_orden_compra as nro_orden_compra1,t1.nro_requisicion as nro_requisicion1,t1.observaciones as observaciones1,t1.factura_a_anular_id as factura_a_anular_id1,t1.usuario_solicitante_id as usuario_solicitante_id1,t1.estado_solicitud_factura_venta_id as estado_solicitud_factura_venta_id1,t1.permitir_cualquier_evento as permitir_cualquier_evento1, " + 
        		"t2.id as id2,t2.borrado as borrado2,t2.fecha as fecha2,t2.nro as nro2,t2.cliente_id as cliente_id2,t2.evento_id as evento_id2,t2.tipo_factura_id as tipo_factura_id2,t2.nro_orden_compra as nro_orden_compra2,t2.nro_requisicion as nro_requisicion2,t2.observaciones as observaciones2,t2.factura_a_anular_id as factura_a_anular_id2,t2.usuario_solicitante_id as usuario_solicitante_id2,t2.estado_solicitud_factura_venta_id as estado_solicitud_factura_venta_id2,t2.permitir_cualquier_evento as permitir_cualquier_evento2 " + 
        		"from solicitud_factura_venta t1, solicitud_factura_venta t2  " + 
        		"where (t1.id+1) = t2.id  " + 
        		"and t1.fecha = t2.fecha  " + 
        		"and t1.cliente_id = t2.cliente_id  " + 
        		"and t1.evento_id = t2.evento_id  " + 
        		"and t1.tipo_factura_id = t2.tipo_factura_id  " + 
        		"and ((t1.nro_orden_compra is null and t2.nro_orden_compra is null) or (t1.nro_orden_compra = '' and t2.nro_orden_compra = '') or (t1.nro_orden_compra = t2.nro_orden_compra))  " + 
        		"and ((t1.nro_requisicion is null and t2.nro_requisicion is null) or (t1.nro_requisicion = '' and t2.nro_requisicion = '') or (t1.nro_requisicion = t2.nro_requisicion))  " + 
        		"and ((t1.observaciones is null and t2.observaciones is null) or (t1.observaciones = '' and t2.observaciones = '') or (t1.observaciones = t2.observaciones))  " + 
        		"and ((t1.factura_a_anular_id is null and t2.factura_a_anular_id is null) or (t1.factura_a_anular_id = t2.factura_a_anular_id))  " + 
        		"and t1.usuario_solicitante_id = t2.usuario_solicitante_id  " + 
        		"and t1.estado_solicitud_factura_venta_id = t2.estado_solicitud_factura_venta_id  " + 
        		"and t1.permitir_cualquier_evento = t2.permitir_cualquier_evento  " + 
        		") AS DUPLICADOS ";
		return sqlString;
	}	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDuplicadosSolicitudFacturaVenta(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosSolicitudFacturaVenta(false);
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}	
	public long getDuplicadosSolicitudFacturaVentaSize(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosSolicitudFacturaVenta(true);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
	// ***********************************************************************
	
	public List<String> getTitulosDuplicadosAdicionalSolicitudFacturaVenta() {
		return Arrays.asList(new String [] {" - - - ID 1 - - - ","borrado 1","descripcion 1","solicitud_factura_venta_id 1"," - - - ID 2 - - - ","borrado 2","descripcion 2","solicitud_factura_venta_id 2"});
	}	
	private String buildQueryDuplicadosAdicionalSolicitudFacturaVenta(boolean returnCount) {				
        String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
        		"(" +
        		"select  " + 
        		"t1.id as id1,t1.borrado as borrado1,t1.descripcion as descripcion1,t1.solicitud_factura_venta_id as solicitud_factura_venta_id1, " + 
        		"t2.id as id2,t2.borrado as borrado2,t2.descripcion as descripcion2,t2.solicitud_factura_venta_id as solicitud_factura_venta_id2 " + 
        		"from adicional_solicitud_factura_venta t1, adicional_solicitud_factura_venta t2  " + 
        		"where (t1.id+1) = t2.id  " + 
        		"and t1.descripcion = t2.descripcion  " + 
        		"and t1.solicitud_factura_venta_id = t2.solicitud_factura_venta_id  " + 
        		") AS DUPLICADOS " + 
        		
        		// *******AGREGADO PARA LOS CASOS YA REVISADOS**********
        		"WHERE id1 not in ("+IDS_YA_REVISADOS_ADIC_SOL_FACT_VTA+") and id2 not in ("+IDS_YA_REVISADOS_ADIC_SOL_FACT_VTA+") ";
        		// *******Fin AGREGADO PARA LOS CASOS YA REVISADOS*******
		return sqlString;
	}	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDuplicadosAdicionalSolicitudFacturaVenta(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosAdicionalSolicitudFacturaVenta(false);
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}	
	public long getDuplicadosAdicionalSolicitudFacturaVentaSize(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosAdicionalSolicitudFacturaVenta(true);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
	// ***********************************************************************
	
	public List<String> getTitulosDuplicadosDetalleSolicitudFacturaVenta() {
		return Arrays.asList(new String [] {" - - - ID 1 - - - ","borrado 1","cantidad 1","descripcion 1","importe 1","solicitud_factura_venta_id 1"," - - - ID 2 - - - ","borrado 2","cantidad 2","descripcion 2","importe 2","solicitud_factura_venta_id 2"});
	}	
	private String buildQueryDuplicadosDetalleSolicitudFacturaVenta(boolean returnCount) {				
        String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
        		"(" +
        		"select  " + 
        		"t1.id as id1,t1.borrado as borrado1,t1.cantidad as cantidad1,t1.descripcion as descripcion1,t1.importe as importe1,t1.solicitud_factura_venta_id as solicitud_factura_venta_id1, " + 
        		"t2.id as id2,t2.borrado as borrado2,t2.cantidad as cantidad2,t2.descripcion as descripcion2,t2.importe as importe2,t2.solicitud_factura_venta_id as solicitud_factura_venta_id2 " + 
        		"from detalle_solicitud_factura_venta t1, detalle_solicitud_factura_venta t2  " + 
        		"where (t1.id+1) = t2.id  " + 
        		"and t1.cantidad = t2.cantidad  " + 
        		"and t1.descripcion = t2.descripcion  " + 
        		"and t1.importe = t2.importe  " + 
        		"and t1.solicitud_factura_venta_id = t2.solicitud_factura_venta_id  " + 
        		") AS DUPLICADOS " + 
        		
        		// *******AGREGADO PARA LOS CASOS YA REVISADOS**********
        		"WHERE id1 not in ("+IDS_YA_REVISADOS_DETALLE_SOL_FACT_VTA+") and id2 not in ("+IDS_YA_REVISADOS_DETALLE_SOL_FACT_VTA+") ";
        		// *******Fin AGREGADO PARA LOS CASOS YA REVISADOS*******
		return sqlString;
	}	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDuplicadosDetalleSolicitudFacturaVenta(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosDetalleSolicitudFacturaVenta(false);
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}	
	public long getDuplicadosDetalleSolicitudFacturaVentaSize(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosDetalleSolicitudFacturaVenta(true);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
// ***********************************************************************	
	
	public List<String> getTitulosDuplicadosFacturaVenta() {
		return Arrays.asList(new String [] {" - - - ID 1 - - - ","borrado 1","fecha 1","nro 1","cliente_id 1","evento_id 1","tipo_factura_id 1","nro_orden_compra 1","nro_requisicion 1","observaciones 1","factura_a_anular_id 1","usuario_solicitante_id 1","estado_factura_venta_id 1","solicitud_factura_venta_id 1"," - - - ID 2 - - - ","borrado 2","fecha 2","nro 2","cliente_id 2","evento_id 2","tipo_factura_id 2","nro_orden_compra 2","nro_requisicion 2","observaciones 2","factura_a_anular_id 2","usuario_solicitante_id 2","estado_factura_venta_id 2","solicitud_factura_venta_id 2"});
	}	
	private String buildQueryDuplicadosFacturaVenta(boolean returnCount) {				
        String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
        		"(" +
        		"select  " + 
        		"t1.id as id1,t1.borrado as borrado1,t1.fecha as fecha1,t1.nro as nro1,t1.cliente_id as cliente_id1,t1.evento_id as evento_id1,t1.tipo_factura_id as tipo_factura_id1,t1.nro_orden_compra as nro_orden_compra1,t1.nro_requisicion as nro_requisicion1,t1.observaciones as observaciones1,t1.factura_a_anular_id as factura_a_anular_id1,t1.usuario_solicitante_id as usuario_solicitante_id1,t1.estado_factura_venta_id as estado_factura_venta_id1,t1.solicitud_factura_venta_id as solicitud_factura_venta_id1, " + 
        		"t2.id as id2,t2.borrado as borrado2,t2.fecha as fecha2,t2.nro as nro2,t2.cliente_id as cliente_id2,t2.evento_id as evento_id2,t2.tipo_factura_id as tipo_factura_id2,t2.nro_orden_compra as nro_orden_compra2,t2.nro_requisicion as nro_requisicion2,t2.observaciones as observaciones2,t2.factura_a_anular_id as factura_a_anular_id2,t2.usuario_solicitante_id as usuario_solicitante_id2,t2.estado_factura_venta_id as estado_factura_venta_id2,t2.solicitud_factura_venta_id as solicitud_factura_venta_id2 " + 
        		"from factura_venta t1, factura_venta t2  " + 
        		"where (t1.id+1) = t2.id  " + 
        		"and t1.fecha = t2.fecha  " + 
        		"and t1.nro = t2.nro  " + 
        		"and t1.cliente_id = t2.cliente_id  " + 
        		"and t1.evento_id = t2.evento_id  " + 
        		"and t1.tipo_factura_id = t2.tipo_factura_id  " + 
        		"and ((t1.nro_orden_compra is null and t2.nro_orden_compra is null) or (t1.nro_orden_compra = '' and t2.nro_orden_compra = '') or (t1.nro_orden_compra = t2.nro_orden_compra))  " + 
        		"and ((t1.nro_requisicion is null and t2.nro_requisicion is null) or (t1.nro_requisicion = '' and t2.nro_requisicion = '') or (t1.nro_requisicion = t2.nro_requisicion))  " + 
        		"and ((t1.observaciones is null and t2.observaciones is null) or (t1.observaciones = '' and t2.observaciones = '') or (t1.observaciones = t2.observaciones))  " + 
        		"and ((t1.factura_a_anular_id is null and t2.factura_a_anular_id is null) or (t1.factura_a_anular_id = t2.factura_a_anular_id))  " + 
        		"and t1.usuario_solicitante_id = t2.usuario_solicitante_id  " + 
        		"and t1.estado_factura_venta_id = t2.estado_factura_venta_id  " + 
        		"and ((t1.solicitud_factura_venta_id is null and t2.solicitud_factura_venta_id is null) or (t1.solicitud_factura_venta_id = t2.solicitud_factura_venta_id))  " + 
        		") AS DUPLICADOS ";
		return sqlString;
	}	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDuplicadosFacturaVenta(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosFacturaVenta(false);
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}	
	public long getDuplicadosFacturaVentaSize(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosFacturaVenta(true);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
	// ***********************************************************************
	
	public List<String> getTitulosDuplicadosAdicionalFacturaVenta() {
		return Arrays.asList(new String [] {" - - - ID 1 - - - ","borrado 1","descripcion 1","factura_venta_id 1"," - - - ID 2 - - - ","borrado 2","descripcion 2","factura_venta_id 2"});
	}	
	private String buildQueryDuplicadosAdicionalFacturaVenta(boolean returnCount) {				
        String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
        		"(" +
        		"select  " + 
        		"t1.id as id1,t1.borrado as borrado1,t1.descripcion as descripcion1,t1.factura_venta_id as factura_venta_id1, " + 
        		"t2.id as id2,t2.borrado as borrado2,t2.descripcion as descripcion2,t2.factura_venta_id as factura_venta_id2 " + 
        		"from adicional_factura_venta t1, adicional_factura_venta t2  " + 
        		"where (t1.id+1) = t2.id  " + 
        		"and t1.descripcion = t2.descripcion  " + 
        		"and t1.factura_venta_id = t2.factura_venta_id  " + 
        		") AS DUPLICADOS ";
		return sqlString;
	}	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDuplicadosAdicionalFacturaVenta(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosAdicionalFacturaVenta(false);
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}	
	public long getDuplicadosAdicionalFacturaVentaSize(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosAdicionalFacturaVenta(true);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
// ***********************************************************************	
	
	public List<String> getTitulosDuplicadosDetalleFacturaVenta() {
		return Arrays.asList(new String [] {" - - - ID 1 - - - ","borrado 1","cantidad 1","descripcion 1","importe 1","factura_venta_id 1"," - - - ID 2 - - - ","borrado 2","cantidad 2","descripcion 2","importe 2","factura_venta_id 2"});
	}	
	private String buildQueryDuplicadosDetalleFacturaVenta(boolean returnCount) {				
        String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
        		"(" +
        		"select  " + 
        		"t1.id as id1,t1.borrado as borrado1,t1.cantidad as cantidad1,t1.descripcion as descripcion1,t1.importe as importe1,t1.factura_venta_id as factura_venta_id1, " + 
        		"t2.id as id2,t2.borrado as borrado2,t2.cantidad as cantidad2,t2.descripcion as descripcion2,t2.importe as importe2,t2.factura_venta_id as factura_venta_id2 " + 
        		"from detalle_factura_venta t1, detalle_factura_venta t2  " + 
        		"where (t1.id+1) = t2.id  " + 
        		"and t1.cantidad = t2.cantidad  " + 
        		"and t1.descripcion = t2.descripcion  " + 
        		"and t1.importe = t2.importe  " + 
        		"and t1.factura_venta_id = t2.factura_venta_id  " + 
        		") AS DUPLICADOS ";
		return sqlString;
	}	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDuplicadosDetalleFacturaVenta(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosDetalleFacturaVenta(false);
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}	
	public long getDuplicadosDetalleFacturaVentaSize(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosDetalleFacturaVenta(true);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
	// ***********************************************************************
	
	public List<String> getTitulosDuplicadosCobranza() {
		return Arrays.asList(new String [] {" - - - ID 1 - - - ","borrado 1","fecha 1","nro_recibo 1","perc_iva 1","perc_iibb 1","perc_gcias 1","perc_suss 1","otras_perc 1","retenciones_validadas 1"," - - - ID 2 - - - ","borrado 2","fecha 2","nro_recibo 2","perc_iva 2","perc_iibb 2","perc_gcias 2","perc_suss 2","otras_perc 2","retenciones_validadas 2"});
	}	
	private String buildQueryDuplicadosCobranza(boolean returnCount) {				
        String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
        		"(" +
        		"select  " + 
        		"t1.id as id1,t1.borrado as borrado1,t1.fecha as fecha1,t1.nro_recibo as nro_recibo1,t1.perc_iva as perc_iva1,t1.perc_iibb as perc_iibb1,t1.perc_gcias as perc_gcias1,t1.perc_suss as perc_suss1,t1.otras_perc as otras_perc1,t1.retenciones_validadas as retenciones_validadas1, " + 
        		"t2.id as id2,t2.borrado as borrado2,t2.fecha as fecha2,t2.nro_recibo as nro_recibo2,t2.perc_iva as perc_iva2,t2.perc_iibb as perc_iibb2,t2.perc_gcias as perc_gcias2,t2.perc_suss as perc_suss2,t2.otras_perc as otras_perc2,t2.retenciones_validadas as retenciones_validadas2 " + 
        		"from cobranza t1, cobranza t2  " + 
        		"where (t1.id+1) = t2.id  " + 
        		"and t1.fecha = t2.fecha  " + 
        		"and ((t1.nro_recibo is null and t2.nro_recibo is null) or (t1.nro_recibo = '' and t2.nro_recibo = '') or (t1.nro_recibo = t2.nro_recibo))  " + 
        		"and ((t1.perc_iva is null and t2.perc_iva is null) or (t1.perc_iva = t2.perc_iva))  " + 
        		"and ((t1.perc_iibb is null and t2.perc_iibb is null) or (t1.perc_iibb = t2.perc_iibb))  " + 
        		"and ((t1.perc_gcias is null and t2.perc_gcias is null) or (t1.perc_gcias = t2.perc_gcias))  " + 
        		"and ((t1.perc_suss is null and t2.perc_suss is null) or (t1.perc_suss = t2.perc_suss))  " + 
        		"and ((t1.otras_perc is null and t2.otras_perc is null) or (t1.otras_perc = t2.otras_perc))  " + 
        		"and t1.retenciones_validadas = t2.retenciones_validadas  " + 
        		") AS DUPLICADOS " + 
        		
        		// *******AGREGADO PARA LOS CASOS YA REVISADOS**********
        		"WHERE id1 not in ("+IDS_YA_REVISADOS_COBRANZAS+") and id2 not in ("+IDS_YA_REVISADOS_COBRANZAS+") ";
        		// *******Fin AGREGADO PARA LOS CASOS YA REVISADOS*******
		return sqlString;
	}	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDuplicadosCobranza(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosCobranza(false);
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}	
	public long getDuplicadosCobranzaSize(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosCobranza(true);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
// ***********************************************************************	
	
	public List<String> getTitulosDuplicadosCobro() {
		return Arrays.asList(new String [] {" - - - ID 1 - - - ","borrado 1","modo_cobro_id 1","banco_cheque 1","nro_cheque 1","banco_transferencia_id 1","importe 1","cobranza_id 1","fecha 1"," - - - ID 2 - - - ","borrado 2","modo_cobro_id 2","banco_cheque 2","nro_cheque 2","banco_transferencia_id 2","importe 2","cobranza_id 2","fecha 2"});
	}	
	private String buildQueryDuplicadosCobro(boolean returnCount) {				
        String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
        		"(" +
        		"select  " + 
        		"t1.id as id1,t1.borrado as borrado1,t1.modo_cobro_id as modo_cobro_id1,t1.banco_cheque as banco_cheque1,t1.nro_cheque as nro_cheque1,t1.banco_transferencia_id as banco_transferencia_id1,t1.importe as importe1,t1.cobranza_id as cobranza_id1,t1.fecha as fecha1, " + 
        		"t2.id as id2,t2.borrado as borrado2,t2.modo_cobro_id as modo_cobro_id2,t2.banco_cheque as banco_cheque2,t2.nro_cheque as nro_cheque2,t2.banco_transferencia_id as banco_transferencia_id2,t2.importe as importe2,t2.cobranza_id as cobranza_id2,t2.fecha as fecha2 " + 
        		"from cobro t1, cobro t2  " + 
        		"where (t1.id+1) = t2.id  " + 
        		"and t1.modo_cobro_id = t2.modo_cobro_id  " + 
        		"and ((t1.banco_cheque is null and t2.banco_cheque is null) or (t1.banco_cheque = '' and t2.banco_cheque = '') or (t1.banco_cheque = t2.banco_cheque))  " + 
        		"and ((t1.nro_cheque is null and t2.nro_cheque is null) or (t1.nro_cheque = '' and t2.nro_cheque = '') or (t1.nro_cheque = t2.nro_cheque))  " + 
        		"and ((t1.banco_transferencia_id is null and t2.banco_transferencia_id is null) or (t1.banco_transferencia_id = t2.banco_transferencia_id))  " + 
        		"and t1.importe = t2.importe  " + 
        		"and t1.cobranza_id = t2.cobranza_id  " + 
        		"and ((t1.fecha is null and t2.fecha is null) or (t1.fecha = t2.fecha))  " + 
        		") AS DUPLICADOS ";
		return sqlString;
	}	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDuplicadosCobro(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosCobro(false);
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}	
	public long getDuplicadosCobroSize(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosCobro(true);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
	// ***********************************************************************
	
	public List<String> getTitulosDuplicadosSolicitudPago() {
		return Arrays.asList(new String [] {" - - - ID 1 - - - ","borrado 1","nro 1","fecha 1","proveedor_id 1","razon_social 1","cuit_cuil 1","cbu 1","con_factura 1","evento_id 1","concepto 1","observaciones 1","cliente_id 1","nro_factura_venta 1","usuario_solicitante_id 1","estado_solicitud_pago_id 1"," - - - ID 2 - - - ","borrado 2","nro 2","fecha 2","proveedor_id 2","razon_social 2","cuit_cuil 2","cbu 2","con_factura 2","evento_id 2","concepto 2","observaciones 2","cliente_id 2","nro_factura_venta 2","usuario_solicitante_id 2","estado_solicitud_pago_id 2"});
	}	
	private String buildQueryDuplicadosSolicitudPago(boolean returnCount) {				
        String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
        		"(" +
        		"select  " + 
        		"t1.id as id1,t1.borrado as borrado1,t1.nro as nro1,t1.fecha as fecha1,t1.proveedor_id as proveedor_id1,t1.razon_social as razon_social1,t1.cuit_cuil as cuit_cuil1,t1.cbu as cbu1,t1.con_factura as con_factura1,t1.evento_id as evento_id1,t1.concepto as concepto1,t1.observaciones as observaciones1,t1.cliente_id as cliente_id1,t1.nro_factura_venta as nro_factura_venta1,t1.usuario_solicitante_id as usuario_solicitante_id1,t1.estado_solicitud_pago_id as estado_solicitud_pago_id1, " + 
        		"t2.id as id2,t2.borrado as borrado2,t2.nro as nro2,t2.fecha as fecha2,t2.proveedor_id as proveedor_id2,t2.razon_social as razon_social2,t2.cuit_cuil as cuit_cuil2,t2.cbu as cbu2,t2.con_factura as con_factura2,t2.evento_id as evento_id2,t2.concepto as concepto2,t2.observaciones as observaciones2,t2.cliente_id as cliente_id2,t2.nro_factura_venta as nro_factura_venta2,t2.usuario_solicitante_id as usuario_solicitante_id2,t2.estado_solicitud_pago_id as estado_solicitud_pago_id2 " + 
        		"from solicitud_pago t1, solicitud_pago t2  " + 
        		"where (t1.id+1) = t2.id  " + 
        		"and t1.fecha = t2.fecha  " + 
        		"and ((t1.proveedor_id is null and t2.proveedor_id is null) or (t1.proveedor_id = t2.proveedor_id))  " + 
        		"and ((t1.razon_social is null and t2.razon_social is null) or (t1.razon_social = '' and t2.razon_social = '') or (t1.razon_social = t2.razon_social))  " + 
        		"and ((t1.cuit_cuil is null and t2.cuit_cuil is null) or (t1.cuit_cuil = '' and t2.cuit_cuil = '') or (t1.cuit_cuil = t2.cuit_cuil))  " + 
        		"and ((t1.cbu is null and t2.cbu is null) or (t1.cbu = '' and t2.cbu = '') or (t1.cbu = t2.cbu))  " + 
        		"and t1.con_factura = t2.con_factura  " + 
        		"and t1.evento_id = t2.evento_id  " + 
        		"and ((t1.concepto is null and t2.concepto is null) or (t1.concepto = '' and t2.concepto = '') or (t1.concepto = t2.concepto))  " + 
        		"and ((t1.observaciones is null and t2.observaciones is null) or (t1.observaciones = '' and t2.observaciones = '') or (t1.observaciones = t2.observaciones))  " + 
        		"and ((t1.cliente_id is null and t2.cliente_id is null) or (t1.cliente_id = t2.cliente_id))  " + 
        		"and ((t1.nro_factura_venta is null and t2.nro_factura_venta is null) or (t1.nro_factura_venta = t2.nro_factura_venta))  " + 
        		"and t1.usuario_solicitante_id = t2.usuario_solicitante_id  " + 
        		"and t1.estado_solicitud_pago_id = t2.estado_solicitud_pago_id  " + 
        		") AS DUPLICADOS ";
		return sqlString;
	}	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDuplicadosSolicitudPago(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosSolicitudPago(false);
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}	
	public long getDuplicadosSolicitudPagoSize(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosSolicitudPago(true);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
// ***********************************************************************	
	
	public List<String> getTitulosDuplicadosPagoSolicitudPago() {
		return Arrays.asList(new String [] {" - - - ID 1 - - - ","borrado 1","modo_pago_id 1","cobro_alternativo_id 1","cuenta_bancaria_id 1","cuit_cuil 1","importe 1","solicitud_pago_id 1","fecha 1"," - - - ID 2 - - - ","borrado 2","modo_pago_id 2","cobro_alternativo_id 2","cuenta_bancaria_id 2","cuit_cuil 2","importe 2","solicitud_pago_id 2","fecha 2"});
	}	
	private String buildQueryDuplicadosPagoSolicitudPago(boolean returnCount) {				
        String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
        		"(" +
        		"select  " + 
        		"t1.id as id1,t1.borrado as borrado1,t1.modo_pago_id as modo_pago_id1,t1.cobro_alternativo_id as cobro_alternativo_id1,t1.cuenta_bancaria_id as cuenta_bancaria_id1,t1.cuit_cuil as cuit_cuil1,t1.importe as importe1,t1.solicitud_pago_id as solicitud_pago_id1,t1.fecha as fecha1, " + 
        		"t2.id as id2,t2.borrado as borrado2,t2.modo_pago_id as modo_pago_id2,t2.cobro_alternativo_id as cobro_alternativo_id2,t2.cuenta_bancaria_id as cuenta_bancaria_id2,t2.cuit_cuil as cuit_cuil2,t2.importe as importe2,t2.solicitud_pago_id as solicitud_pago_id2,t2.fecha as fecha2 " + 
        		"from pago_solicitud_pago t1, pago_solicitud_pago t2  " + 
        		"where (t1.id+1) = t2.id  " + 
        		"and t1.modo_pago_id = t2.modo_pago_id  " + 
        		"and ((t1.cobro_alternativo_id is null and t2.cobro_alternativo_id is null) or (t1.cobro_alternativo_id = t2.cobro_alternativo_id))  " + 
        		"and ((t1.cuenta_bancaria_id is null and t2.cuenta_bancaria_id is null) or (t1.cuenta_bancaria_id = t2.cuenta_bancaria_id))  " + 
        		"and ((t1.cuit_cuil is null and t2.cuit_cuil is null) or (t1.cuit_cuil = '' and t2.cuit_cuil = '') or (t1.cuit_cuil = t2.cuit_cuil))  " + 
        		"and t1.importe = t2.importe  " + 
        		"and t1.solicitud_pago_id = t2.solicitud_pago_id  " + 
        		"and t1.fecha = t2.fecha  " + 
        		") AS DUPLICADOS "
        		 + 
         		
        		// *******AGREGADO PARA LOS CASOS YA REVISADOS**********
        		"WHERE id1 not in ("+IDS_YA_REVISADOS_PAGO_SOL_PAGO+") and id2 not in ("+IDS_YA_REVISADOS_PAGO_SOL_PAGO+") ";
        		// *******Fin AGREGADO PARA LOS CASOS YA REVISADOS*******
		return sqlString;
	}	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDuplicadosPagoSolicitudPago(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosPagoSolicitudPago(false);
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}	
	public long getDuplicadosPagoSolicitudPagoSize(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosPagoSolicitudPago(true);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
	// ***********************************************************************
	
	public List<String> getTitulosDuplicadosFacturaSolicitudPago() {
		return Arrays.asList(new String [] {" - - - ID 1 - - - ","borrado 1","factura_compra_id 1","tipo_factura_compra_id 1","nro_factura_compra 1","total 1","solicitud_pago_id 1","fecha_factura_compra 1"," - - - ID 2 - - - ","borrado 2","factura_compra_id 2","tipo_factura_compra_id 2","nro_factura_compra 2","total 2","solicitud_pago_id 2","fecha_factura_compra 2"});
	}	
	private String buildQueryDuplicadosFacturaSolicitudPago(boolean returnCount) {				
        String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
        		"(" +
        		"select  " + 
        		"t1.id as id1,t1.borrado as borrado1,t1.factura_compra_id as factura_compra_id1,t1.tipo_factura_compra_id as tipo_factura_compra_id1,t1.nro_factura_compra as nro_factura_compra1,t1.total as total1,t1.solicitud_pago_id as solicitud_pago_id1,t1.fecha_factura_compra as fecha_factura_compra1, " + 
        		"t2.id as id2,t2.borrado as borrado2,t2.factura_compra_id as factura_compra_id2,t2.tipo_factura_compra_id as tipo_factura_compra_id2,t2.nro_factura_compra as nro_factura_compra2,t2.total as total2,t2.solicitud_pago_id as solicitud_pago_id2,t2.fecha_factura_compra as fecha_factura_compra2 " + 
        		"from factura_solicitud_pago t1, factura_solicitud_pago t2  " + 
        		"where (t1.id+1) = t2.id  " + 
        		"and ((t1.factura_compra_id is null and t2.factura_compra_id is null) or (t1.factura_compra_id = t2.factura_compra_id))  " + 
        		"and ((t1.tipo_factura_compra_id is null and t2.tipo_factura_compra_id is null) or (t1.tipo_factura_compra_id = t2.tipo_factura_compra_id))  " + 
        		"and ((t1.nro_factura_compra is null and t2.nro_factura_compra is null) or (t1.nro_factura_compra = '' and t2.nro_factura_compra = '') or (t1.nro_factura_compra = t2.nro_factura_compra))  " + 
        		"and t1.total = t2.total  " + 
        		"and t1.solicitud_pago_id = t2.solicitud_pago_id  " + 
        		"and ((t1.fecha_factura_compra is null and t2.fecha_factura_compra is null) or (t1.fecha_factura_compra = t2.fecha_factura_compra))  " + 
        		") AS DUPLICADOS ";
		return sqlString;
	}	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDuplicadosFacturaSolicitudPago(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosFacturaSolicitudPago(false);
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}	
	public long getDuplicadosFacturaSolicitudPagoSize(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosFacturaSolicitudPago(true);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
// ***********************************************************************	
	
	public List<String> getTitulosDuplicadosOrdenPago() {
		return Arrays.asList(new String [] {" - - - ID 1 - - - ","borrado 1","fecha 1","nro 1","evento_id 1","concepto 1","usuario_solicitante_id 1","solicitud_pago_id 1","observaciones 1","plan_cuenta_id 1"," - - - ID 2 - - - ","borrado 2","fecha 2","nro 2","evento_id 2","concepto 2","usuario_solicitante_id 2","solicitud_pago_id 2","observaciones 2","plan_cuenta_id 2"});
	}	
	private String buildQueryDuplicadosOrdenPago(boolean returnCount) {				
        String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
        		"(" +
        		"select  " + 
        		"t1.id as id1,t1.borrado as borrado1,t1.fecha as fecha1,t1.nro as nro1,t1.evento_id as evento_id1,t1.concepto as concepto1,t1.usuario_solicitante_id as usuario_solicitante_id1,t1.solicitud_pago_id as solicitud_pago_id1,t1.observaciones as observaciones1,t1.plan_cuenta_id as plan_cuenta_id1, " + 
        		"t2.id as id2,t2.borrado as borrado2,t2.fecha as fecha2,t2.nro as nro2,t2.evento_id as evento_id2,t2.concepto as concepto2,t2.usuario_solicitante_id as usuario_solicitante_id2,t2.solicitud_pago_id as solicitud_pago_id2,t2.observaciones as observaciones2,t2.plan_cuenta_id as plan_cuenta_id2 " + 
        		"from orden_pago t1, orden_pago t2  " + 
        		"where (t1.id+1) = t2.id  " + 
        		"and t1.fecha = t2.fecha  " + 
        		"and t1.evento_id = t2.evento_id  " + 
        		"and ((t1.concepto is null and t2.concepto is null) or (t1.concepto = '' and t2.concepto = '') or (t1.concepto = t2.concepto))  " + 
        		"and t1.usuario_solicitante_id = t2.usuario_solicitante_id  " + 
        		"and ((t1.solicitud_pago_id is null and t2.solicitud_pago_id is null) or (t1.solicitud_pago_id = t2.solicitud_pago_id))  " + 
        		"and ((t1.observaciones is null and t2.observaciones is null) or (t1.observaciones = '' and t2.observaciones = '') or (t1.observaciones = t2.observaciones))  " + 
        		"and ((t1.plan_cuenta_id is null and t2.plan_cuenta_id is null) or (t1.plan_cuenta_id = t2.plan_cuenta_id))  " + 
        		"and (select SUM(p.importe) from pago p where p.orden_pago_id=t1.id) = (select SUM(p.importe) from pago p where p.orden_pago_id=t2.id) " + 
        		"and ( " + 
        		"( " + 
        		"(select count(fcop.factura_compra_id) from factura_compra_orden_pago fcop where fcop.orden_pago_id =t1.id) > 0  " + 
        		"and (select count(fcop.factura_compra_id) from factura_compra_orden_pago fcop where fcop.orden_pago_id =t2.id) > 0  " + 
        		"and (select SUM(fc.proveedor_id) from factura_compra_orden_pago fcop, factura_compra fc where fcop.factura_compra_id = fc.id and fcop.orden_pago_id =t1.id) = (select SUM(fc.proveedor_id) from factura_compra_orden_pago fcop, factura_compra fc where fcop.factura_compra_id = fc.id and fcop.orden_pago_id =t2.id) " + 
        		") " + 
        		"or " + 
        		"( " + 
        		"(select count(fcop.factura_compra_id) from factura_compra_orden_pago fcop where fcop.orden_pago_id =t1.id) = 0  " + 
        		"and (select count(fcop.factura_compra_id) from factura_compra_orden_pago fcop where fcop.orden_pago_id =t2.id) = 0  " + 
        		") " + 
        		") " + 
        		"order by t1.fecha " + 
        		") AS DUPLICADOS " + 
        		
        		// *******AGREGADO PARA LOS CASOS YA REVISADOS**********
        		"WHERE id1 not in ("+IDS_YA_REVISADOS_OPS+") and id2 not in ("+IDS_YA_REVISADOS_OPS+") " +
        		// *******Fin AGREGADO PARA LOS CASOS YA REVISADOS*******
        
				//*******NO SE TIENEN EN CUENTA LOS DUPLICADOS ANTERIORES A 2019 - PEDIDO POR ANGELA**********
        		"and fecha1 >= '"+ANIO_A_REVISAR_DUPLICADOS+"' " +
        		"and fecha2 >= '"+ANIO_A_REVISAR_DUPLICADOS+"' ";
        		//*******NO SE TIENEN EN CUENTA LOS DUPLICADOS ANTERIORES A 2019 - PEDIDO POR ANGELA**********
        
		return sqlString;
	}	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDuplicadosOrdenPago(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosOrdenPago(false);
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}	
	public long getDuplicadosOrdenPagoSize(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosOrdenPago(true);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
	// ***********************************************************************
	
	public List<String> getTitulosDuplicadosPago() {
		return Arrays.asList(new String [] {" - - - ID 1 - - - ","borrado 1","modo_pago_id 1","nro_cheque 1","banco_id 1","cobro_alternativo_id 1","cuenta_bancaria_id 1","importe 1","orden_pago_id 1","cuit_cuil 1","fecha 1","debitado 1"," - - - ID 2 - - - ","borrado 2","modo_pago_id 2","nro_cheque 2","banco_id 2","cobro_alternativo_id 2","cuenta_bancaria_id 2","importe 2","orden_pago_id 2","cuit_cuil 2","fecha 2","debitado 2"});
	}	
	private String buildQueryDuplicadosPago(boolean returnCount) {				
        String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
        		"(" +
        		"select  " + 
        		"t1.id as id1,t1.borrado as borrado1,t1.modo_pago_id as modo_pago_id1,t1.nro_cheque as nro_cheque1,t1.banco_id as banco_id1,t1.cobro_alternativo_id as cobro_alternativo_id1,t1.cuenta_bancaria_id as cuenta_bancaria_id1,t1.importe as importe1,t1.orden_pago_id as orden_pago_id1,t1.cuit_cuil as cuit_cuil1,t1.fecha as fecha1,t1.debitado as debitado1, " + 
        		"t2.id as id2,t2.borrado as borrado2,t2.modo_pago_id as modo_pago_id2,t2.nro_cheque as nro_cheque2,t2.banco_id as banco_id2,t2.cobro_alternativo_id as cobro_alternativo_id2,t2.cuenta_bancaria_id as cuenta_bancaria_id2,t2.importe as importe2,t2.orden_pago_id as orden_pago_id2,t2.cuit_cuil as cuit_cuil2,t2.fecha as fecha2,t2.debitado as debitado2 " + 
        		"from pago t1, pago t2  " + 
        		"where (t1.id+1) = t2.id  " + 
        		"and t1.modo_pago_id = t2.modo_pago_id  " + 
        		"and ((t1.nro_cheque is null and t2.nro_cheque is null) or (t1.nro_cheque = '' and t2.nro_cheque = '') or (t1.nro_cheque = t2.nro_cheque))  " + 
        		"and ((t1.banco_id is null and t2.banco_id is null) or (t1.banco_id = t2.banco_id))  " + 
        		"and ((t1.cobro_alternativo_id is null and t2.cobro_alternativo_id is null) or (t1.cobro_alternativo_id = t2.cobro_alternativo_id))  " + 
        		"and ((t1.cuenta_bancaria_id is null and t2.cuenta_bancaria_id is null) or (t1.cuenta_bancaria_id = t2.cuenta_bancaria_id))  " + 
        		"and t1.importe = t2.importe  " + 
        		"and t1.orden_pago_id = t2.orden_pago_id  " + 
        		"and ((t1.cuit_cuil is null and t2.cuit_cuil is null) or (t1.cuit_cuil = '' and t2.cuit_cuil = '') or (t1.cuit_cuil = t2.cuit_cuil))  " + 
        		"and ((t1.fecha is null and t2.fecha is null) or (t1.fecha = t2.fecha))  " + 
        		"and t1.debitado = t2.debitado  " + 
        		") AS DUPLICADOS " + 
        		
        		// *******AGREGADO PARA LOS CASOS YA REVISADOS**********
        		"WHERE id1 not in ("+IDS_YA_REVISADOS_PAGO+") and id2 not in ("+IDS_YA_REVISADOS_PAGO+") ";
        		// *******Fin AGREGADO PARA LOS CASOS YA REVISADOS*******
		return sqlString;
	}	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDuplicadosPago(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosPago(false);
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}	
	public long getDuplicadosPagoSize(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosPago(true);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
// ***********************************************************************	
	
	public List<String> getTitulosDuplicadosEvento() {
		return Arrays.asList(new String [] {" - - - ID 1 - - - ","borrado 1","nombre 1","total_evento 1","costo_total 1","total_evento_con_iva 1","costo_total_con_iva 1","cliente_id 1","cerrado 1","fecha 1","costo_final 1","solo_administradores 1","responsable_id 1"," - - - ID 2 - - - ","borrado 2","nombre 2","total_evento 2","costo_total 2","total_evento_con_iva 2","costo_total_con_iva 2","cliente_id 2","cerrado 2","fecha 2","costo_final 2","solo_administradores 2","responsable_id 2"});
	}	
	private String buildQueryDuplicadosEvento(boolean returnCount) {				
        String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
        		"(" +
        		"select  " + 
        		"t1.id as id1,t1.borrado as borrado1,t1.nombre as nombre1,t1.total_evento as total_evento1,t1.costo_total as costo_total1,t1.total_evento_con_iva as total_evento_con_iva1,t1.costo_total_con_iva as costo_total_con_iva1,t1.cliente_id as cliente_id1,t1.cerrado as cerrado1,t1.fecha as fecha1,t1.costo_final as costo_final1,t1.solo_administradores as solo_administradores1,t1.responsable_id as responsable_id1, " + 
        		"t2.id as id2,t2.borrado as borrado2,t2.nombre as nombre2,t2.total_evento as total_evento2,t2.costo_total as costo_total2,t2.total_evento_con_iva as total_evento_con_iva2,t2.costo_total_con_iva as costo_total_con_iva2,t2.cliente_id as cliente_id2,t2.cerrado as cerrado2,t2.fecha as fecha2,t2.costo_final as costo_final2,t2.solo_administradores as solo_administradores2,t2.responsable_id as responsable_id2 " + 
        		"from evento t1, evento t2  " + 
        		"where (t1.id+1) = t2.id  " + 
        		"and t1.nombre = t2.nombre  " + 
        		"and t1.total_evento = t2.total_evento  " + 
        		"and t1.costo_total = t2.costo_total  " + 
        		"and t1.total_evento_con_iva = t2.total_evento_con_iva  " + 
        		"and t1.costo_total_con_iva = t2.costo_total_con_iva  " + 
        		"and ((t1.cliente_id is null and t2.cliente_id is null) or (t1.cliente_id = t2.cliente_id))  " + 
        		"and t1.cerrado = t2.cerrado  " + 
        		"and ((t1.fecha is null and t2.fecha is null) or (t1.fecha = t2.fecha))  " + 
        		"and t1.costo_final = t2.costo_final  " + 
        		"and t1.solo_administradores = t2.solo_administradores  " + 
        		"and ((t1.responsable_id is null and t2.responsable_id is null) or (t1.responsable_id = t2.responsable_id))  " + 
        		") AS DUPLICADOS ";
		return sqlString;
	}	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDuplicadosEvento(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosEvento(false);
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}	
	public long getDuplicadosEventoSize(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosEvento(true);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
	// ***********************************************************************
	
	public List<String> getTitulosDuplicadosFacturaCompra() {
		return Arrays.asList(new String [] {" - - - ID 1 - - - ","borrado 1","fecha 1","mes_impositivo 1","proveedor_id 1","nro 1","subtotal 1","iva 1","perc_iva 1","perc_iibb 1","estado_factura_compra_id 1","tipo_factura_id 1","perc_gcias 1","perc_suss 1","otras_perc 1"," - - - ID 2 - - - ","borrado 2","fecha 2","mes_impositivo 2","proveedor_id 2","nro 2","subtotal 2","iva 2","perc_iva 2","perc_iibb 2","estado_factura_compra_id 2","tipo_factura_id 2","perc_gcias 2","perc_suss 2","otras_perc 2"});
	}	
	private String buildQueryDuplicadosFacturaCompra(boolean returnCount) {				
        String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
        		"(" +
        		"select  " + 
        		"t1.id as id1,t1.borrado as borrado1,t1.fecha as fecha1,t1.mes_impositivo as mes_impositivo1,t1.proveedor_id as proveedor_id1,t1.nro as nro1,t1.subtotal as subtotal1,t1.iva as iva1,t1.perc_iva as perc_iva1,t1.perc_iibb as perc_iibb1,t1.estado_factura_compra_id as estado_factura_compra_id1,t1.tipo_factura_id as tipo_factura_id1,t1.perc_gcias as perc_gcias1,t1.perc_suss as perc_suss1,t1.otras_perc as otras_perc1, " + 
        		"t2.id as id2,t2.borrado as borrado2,t2.fecha as fecha2,t2.mes_impositivo as mes_impositivo2,t2.proveedor_id as proveedor_id2,t2.nro as nro2,t2.subtotal as subtotal2,t2.iva as iva2,t2.perc_iva as perc_iva2,t2.perc_iibb as perc_iibb2,t2.estado_factura_compra_id as estado_factura_compra_id2,t2.tipo_factura_id as tipo_factura_id2,t2.perc_gcias as perc_gcias2,t2.perc_suss as perc_suss2,t2.otras_perc as otras_perc2 " + 
        		"from factura_compra t1, factura_compra t2  " + 
        		"where (t1.id+1) = t2.id  " + 
        		"and t1.fecha = t2.fecha  " + 
        		"and t1.mes_impositivo = t2.mes_impositivo  " + 
        		"and t1.proveedor_id = t2.proveedor_id  " + 
        		"and t1.nro = t2.nro  " + 
        		"and t1.subtotal = t2.subtotal  " + 
        		"and t1.iva = t2.iva  " + 
        		"and ((t1.perc_iva is null and t2.perc_iva is null) or (t1.perc_iva = t2.perc_iva))  " + 
        		"and ((t1.perc_iibb is null and t2.perc_iibb is null) or (t1.perc_iibb = t2.perc_iibb))  " + 
        		"and t1.estado_factura_compra_id = t2.estado_factura_compra_id  " + 
        		"and t1.tipo_factura_id = t2.tipo_factura_id  " + 
        		"and ((t1.perc_gcias is null and t2.perc_gcias is null) or (t1.perc_gcias = t2.perc_gcias))  " + 
        		"and ((t1.perc_suss is null and t2.perc_suss is null) or (t1.perc_suss = t2.perc_suss))  " + 
        		"and ((t1.otras_perc is null and t2.otras_perc is null) or (t1.otras_perc = t2.otras_perc))  " + 
        		") AS DUPLICADOS " +
        		
        		//Las que no son SinFactura 
        		"WHERE nro1 not like '"+Constantes.PREFIX_NRO_FACTURA_SF+"%' and nro2 not like '"+Constantes.PREFIX_NRO_FACTURA_SF+"%' ";
		return sqlString;
	}	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDuplicadosFacturaCompra(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosFacturaCompra(false);
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}	
	public long getDuplicadosFacturaCompraSize(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosFacturaCompra(true);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
// ***********************************************************************	
	
	public List<String> getTitulosDuplicadosUsuario() {
		return Arrays.asList(new String [] {" - - - ID 1 - - - ","borrado 1","apellido 1","clave 1","email 1","nombre_o_razon_social 1","telefono 1","activacion 1"," - - - ID 2 - - - ","borrado 2","apellido 2","clave 2","email 2","nombre_o_razon_social 2","telefono 2","activacion 2"});
	}	
	private String buildQueryDuplicadosUsuario(boolean returnCount) {				
        String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
        		"(" +
        		"select  " + 
        		"t1.id as id1,t1.borrado as borrado1,t1.apellido as apellido1,t1.clave as clave1,t1.email as email1,t1.nombre_o_razon_social as nombre_o_razon_social1,t1.telefono as telefono1,t1.activacion as activacion1, " + 
        		"t2.id as id2,t2.borrado as borrado2,t2.apellido as apellido2,t2.clave as clave2,t2.email as email2,t2.nombre_o_razon_social as nombre_o_razon_social2,t2.telefono as telefono2,t2.activacion as activacion2 " + 
        		"from usuario t1, usuario t2  " + 
        		"where (t1.id+1) = t2.id  " + 
        		"and ((t1.apellido is null and t2.apellido is null) or (t1.apellido = '' and t2.apellido = '') or (t1.apellido = t2.apellido))  " + 
        		"and t1.clave = t2.clave  " + 
        		"and t1.email = t2.email  " + 
        		"and ((t1.nombre_o_razon_social is null and t2.nombre_o_razon_social is null) or (t1.nombre_o_razon_social = '' and t2.nombre_o_razon_social = '') or (t1.nombre_o_razon_social = t2.nombre_o_razon_social))  " + 
        		"and ((t1.telefono is null and t2.telefono is null) or (t1.telefono = '' and t2.telefono = '') or (t1.telefono = t2.telefono))  " + 
        		"and ((t1.activacion is null and t2.activacion is null) or (t1.activacion = '' and t2.activacion = '') or (t1.activacion = t2.activacion))  " + 
        		") AS DUPLICADOS ";
		return sqlString;
	}	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDuplicadosUsuario(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosUsuario(false);
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}	
	public long getDuplicadosUsuarioSize(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosUsuario(true);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
	// ***********************************************************************
	
	public List<String> getTitulosDuplicadosCliente() {
		return Arrays.asList(new String [] {" - - - ID 1 - - - ","borrado 1","razon_social 1","cuit_cuil 1","domicilio 1","mascara_modo_cobros 1","telefono1 1","telefono2 1","contacto 1","mails 1","referencias 1","observaciones 1"," - - - ID 2 - - - ","borrado 2","razon_social 2","cuit_cuil 2","domicilio 2","mascara_modo_cobros 2","telefono1 2","telefono2 2","contacto 2","mails 2","referencias 2","observaciones 2"});
	}	
	private String buildQueryDuplicadosCliente(boolean returnCount) {				
        String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
        		"(" +
        		"select  " + 
        		"t1.id as id1,t1.borrado as borrado1,t1.razon_social as razon_social1,t1.cuit_cuil as cuit_cuil1,t1.domicilio as domicilio1,t1.mascara_modo_cobros as mascara_modo_cobros1,t1.telefono1 as telefono11,t1.telefono2 as telefono21,t1.contacto as contacto1,t1.mails as mails1,t1.referencias as referencias1,t1.observaciones as observaciones1, " + 
        		"t2.id as id2,t2.borrado as borrado2,t2.razon_social as razon_social2,t2.cuit_cuil as cuit_cuil2,t2.domicilio as domicilio2,t2.mascara_modo_cobros as mascara_modo_cobros2,t2.telefono1 as telefono12,t2.telefono2 as telefono22,t2.contacto as contacto2,t2.mails as mails2,t2.referencias as referencias2,t2.observaciones as observaciones2 " + 
        		"from cliente t1, cliente t2  " + 
        		"where (t1.id+1) = t2.id  " + 
        		"and t1.razon_social = t2.razon_social  " + 
        		"and t1.cuit_cuil = t2.cuit_cuil  " + 
        		"and ((t1.domicilio is null and t2.domicilio is null) or (t1.domicilio = '' and t2.domicilio = '') or (t1.domicilio = t2.domicilio))  " + 
        		"and t1.mascara_modo_cobros = t2.mascara_modo_cobros  " + 
        		"and ((t1.telefono1 is null and t2.telefono1 is null) or (t1.telefono1 = '' and t2.telefono1 = '') or (t1.telefono1 = t2.telefono1))  " + 
        		"and ((t1.telefono2 is null and t2.telefono2 is null) or (t1.telefono2 = '' and t2.telefono2 = '') or (t1.telefono2 = t2.telefono2))  " + 
        		"and ((t1.contacto is null and t2.contacto is null) or (t1.contacto = '' and t2.contacto = '') or (t1.contacto = t2.contacto))  " + 
        		"and ((t1.mails is null and t2.mails is null) or (t1.mails = '' and t2.mails = '') or (t1.mails = t2.mails))  " + 
        		"and ((t1.referencias is null and t2.referencias is null) or (t1.referencias = '' and t2.referencias = '') or (t1.referencias = t2.referencias))  " + 
        		"and ((t1.observaciones is null and t2.observaciones is null) or (t1.observaciones = '' and t2.observaciones = '') or (t1.observaciones = t2.observaciones))  " + 
        		") AS DUPLICADOS ";
		return sqlString;
	}	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDuplicadosCliente(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosCliente(false);
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}	
	public long getDuplicadosClienteSize(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosCliente(true);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
// ***********************************************************************	
	
	public List<String> getTitulosDuplicadosProveedor() {
		return Arrays.asList(new String [] {" - - - ID 1 - - - ","borrado 1","razon_social 1","cuit_cuil 1","cuenta_bancaria_id 1","mascara_modo_pago 1"," - - - ID 2 - - - ","borrado 2","razon_social 2","cuit_cuil 2","cuenta_bancaria_id 2","mascara_modo_pago 2"});
	}	
	private String buildQueryDuplicadosProveedor(boolean returnCount) {				
        String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
        		"(" +
        		"select  " + 
        		"t1.id as id1,t1.borrado as borrado1,t1.razon_social as razon_social1,t1.cuit_cuil as cuit_cuil1,t1.cuenta_bancaria_id as cuenta_bancaria_id1,t1.mascara_modo_pago as mascara_modo_pago1, " + 
        		"t2.id as id2,t2.borrado as borrado2,t2.razon_social as razon_social2,t2.cuit_cuil as cuit_cuil2,t2.cuenta_bancaria_id as cuenta_bancaria_id2,t2.mascara_modo_pago as mascara_modo_pago2 " + 
        		"from proveedor t1, proveedor t2  " + 
        		"where (t1.id+1) = t2.id  " + 
        		"and t1.razon_social = t2.razon_social  " + 
        		"and ((t1.cuit_cuil is null and t2.cuit_cuil is null) or (t1.cuit_cuil = '' and t2.cuit_cuil = '') or (t1.cuit_cuil = t2.cuit_cuil))  " + 
        		"and ((t1.cuenta_bancaria_id is null and t2.cuenta_bancaria_id is null) or (t1.cuenta_bancaria_id = t2.cuenta_bancaria_id))  " + 
        		"and t1.mascara_modo_pago = t2.mascara_modo_pago  " + 
        		") AS DUPLICADOS ";
		return sqlString;
	}	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDuplicadosProveedor(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosProveedor(false);
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}	
	public long getDuplicadosProveedorSize(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosProveedor(true);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
	// ***********************************************************************
	
	public List<String> getTitulosDuplicadosPlanCuenta() {
		return Arrays.asList(new String [] {" - - - ID 1 - - - ","borrado 1","nombre 1","descripcion 1"," - - - ID 2 - - - ","borrado 2","nombre 2","descripcion 2"});
	}	
	private String buildQueryDuplicadosPlanCuenta(boolean returnCount) {				
        String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
        		"(" +
        		"select  " + 
        		"t1.id as id1,t1.borrado as borrado1,t1.nombre as nombre1,t1.descripcion as descripcion1, " + 
        		"t2.id as id2,t2.borrado as borrado2,t2.nombre as nombre2,t2.descripcion as descripcion2 " + 
        		"from plan_cuenta t1, plan_cuenta t2  " + 
        		"where (t1.id+1) = t2.id  " + 
        		"and t1.nombre = t2.nombre  " + 
        		"and ((t1.descripcion is null and t2.descripcion is null) or (t1.descripcion = '' and t2.descripcion = '') or (t1.descripcion = t2.descripcion))  " + 
        		") AS DUPLICADOS ";
		return sqlString;
	}	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDuplicadosPlanCuenta(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosPlanCuenta(false);
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}	
	public long getDuplicadosPlanCuentaSize(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosPlanCuenta(true);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
// ***********************************************************************	
	
	public List<String> getTitulosDuplicadosPresupuestoCustom() {
		return Arrays.asList(new String [] {" - - - ID 1 - - - ","borrado 1","detalle 1","fecha 1","banco_id 1","importe 1","debitado 1"," - - - ID 2 - - - ","borrado 2","detalle 2","fecha 2","banco_id 2","importe 2","debitado 2"});
	}	
	private String buildQueryDuplicadosPresupuestoCustom(boolean returnCount) {				
        String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
        		"(" +
        		"select  " + 
        		"t1.id as id1,t1.borrado as borrado1,t1.detalle as detalle1,t1.fecha as fecha1,t1.banco_id as banco_id1,t1.importe as importe1,t1.debitado as debitado1, " + 
        		"t2.id as id2,t2.borrado as borrado2,t2.detalle as detalle2,t2.fecha as fecha2,t2.banco_id as banco_id2,t2.importe as importe2,t2.debitado as debitado2 " + 
        		"from presupuesto_custom t1, presupuesto_custom t2  " + 
        		"where (t1.id+1) = t2.id  " + 
        		"and t1.detalle = t2.detalle  " + 
        		"and t1.fecha = t2.fecha  " + 
        		"and t1.banco_id = t2.banco_id  " + 
        		"and t1.importe = t2.importe  " + 
        		"and t1.debitado = t2.debitado  " + 
        		") AS DUPLICADOS ";
		return sqlString;
	}	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDuplicadosPresupuestoCustom(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosPresupuestoCustom(false);
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}	
	public long getDuplicadosPresupuestoCustomSize(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosPresupuestoCustom(true);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
	// ***********************************************************************
	
	public List<String> getTitulosDuplicadosBanco() {
		return Arrays.asList(new String [] {" - - - ID 1 - - - ","borrado 1","nombre 1","actual 1"," - - - ID 2 - - - ","borrado 2","nombre 2","actual 2"});
	}	
	private String buildQueryDuplicadosBanco(boolean returnCount) {				
        String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
        		"(" +
        		"select  " + 
        		"t1.id as id1,t1.borrado as borrado1,t1.nombre as nombre1,t1.actual as actual1, " + 
        		"t2.id as id2,t2.borrado as borrado2,t2.nombre as nombre2,t2.actual as actual2 " + 
        		"from banco t1, banco t2  " + 
        		"where (t1.id+1) = t2.id  " + 
        		"and t1.nombre = t2.nombre  " + 
        		"and t1.actual = t2.actual  " + 
        		") AS DUPLICADOS ";
		return sqlString;
	}	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDuplicadosBanco(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosBanco(false);
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}	
	public long getDuplicadosBancoSize(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosBanco(true);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
// ***********************************************************************	
	
	public List<String> getTitulosDuplicadosCuentaBancaria() {
		return Arrays.asList(new String [] {" - - - ID 1 - - - ","borrado 1","tipo_cuenta_id 1","nro_cuenta 1","banco_id 1","cbu 1"," - - - ID 2 - - - ","borrado 2","tipo_cuenta_id 2","nro_cuenta 2","banco_id 2","cbu 2"});
	}	
	private String buildQueryDuplicadosCuentaBancaria(boolean returnCount) {				
        String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
        		"(" +
        		"select  " + 
        		"t1.id as id1,t1.borrado as borrado1,t1.tipo_cuenta_id as tipo_cuenta_id1,t1.nro_cuenta as nro_cuenta1,t1.banco_id as banco_id1,t1.cbu as cbu1, " + 
        		"t2.id as id2,t2.borrado as borrado2,t2.tipo_cuenta_id as tipo_cuenta_id2,t2.nro_cuenta as nro_cuenta2,t2.banco_id as banco_id2,t2.cbu as cbu2 " + 
        		"from cuenta_bancaria t1, cuenta_bancaria t2  " + 
        		"where (t1.id+1) = t2.id  " + 
        		"and ((t1.tipo_cuenta_id is null and t2.tipo_cuenta_id is null) or (t1.tipo_cuenta_id = t2.tipo_cuenta_id))  " + 
        		"and ((t1.nro_cuenta is null and t2.nro_cuenta is null) or (t1.nro_cuenta = '' and t2.nro_cuenta = '') or (t1.nro_cuenta = t2.nro_cuenta))  " + 
        		"and ((t1.banco_id is null and t2.banco_id is null) or (t1.banco_id = t2.banco_id))  " + 
        		"and t1.cbu = t2.cbu  " + 
        		") AS DUPLICADOS " + 
        		
        		// *******AGREGADO PARA LOS CASOS YA REVISADOS**********
        		"WHERE id1 not in ("+IDS_YA_REVISADOS_CTAS_BCARIAS+") and id2 not in ("+IDS_YA_REVISADOS_CTAS_BCARIAS+") ";
        		// *******Fin AGREGADO PARA LOS CASOS YA REVISADOS*******
		return sqlString;
	}	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDuplicadosCuentaBancaria(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosCuentaBancaria(false);
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}	
	public long getDuplicadosCuentaBancariaSize(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosCuentaBancaria(true);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
	// ***********************************************************************
	
	public List<String> getTitulosDuplicadosCajaChica() {
		return Arrays.asList(new String [] {" - - - ID 1 - - - ","borrado 1","fecha 1","detalle 1","mes 1","monto 1","solicitado_por_otros 1","solicitado_por_todos 1","usuario_solicitante_id 1"," - - - ID 2 - - - ","borrado 2","fecha 2","detalle 2","mes 2","monto 2","solicitado_por_otros 2","solicitado_por_todos 2","usuario_solicitante_id 2"});
	}	
	private String buildQueryDuplicadosCajaChica(boolean returnCount) {				
        String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
        		"(" +
        		"select  " + 
        		"t1.id as id1,t1.borrado as borrado1,t1.fecha as fecha1,t1.detalle as detalle1,t1.mes as mes1,t1.monto as monto1,t1.solicitado_por_otros as solicitado_por_otros1,t1.solicitado_por_todos as solicitado_por_todos1,t1.usuario_solicitante_id as usuario_solicitante_id1, " + 
        		"t2.id as id2,t2.borrado as borrado2,t2.fecha as fecha2,t2.detalle as detalle2,t2.mes as mes2,t2.monto as monto2,t2.solicitado_por_otros as solicitado_por_otros2,t2.solicitado_por_todos as solicitado_por_todos2,t2.usuario_solicitante_id as usuario_solicitante_id2 " + 
        		"from caja_chica t1, caja_chica t2  " + 
        		"where (t1.id+1) = t2.id  " + 
        		"and t1.fecha = t2.fecha  " + 
        		"and t1.detalle = t2.detalle  " + 
        		"and t1.mes = t2.mes  " + 
        		"and t1.monto = t2.monto  " + 
        		"and t1.solicitado_por_otros = t2.solicitado_por_otros  " + 
        		"and t1.solicitado_por_todos = t2.solicitado_por_todos  " + 
        		"and ((t1.usuario_solicitante_id is null and t2.usuario_solicitante_id is null) or (t1.usuario_solicitante_id = t2.usuario_solicitante_id)) " + 
        		") AS DUPLICADOS " + 
        		
        		// *******AGREGADO PARA LOS CASOS YA REVISADOS**********
        		"WHERE id1 not in ("+IDS_YA_REVISADOS_CAJA_CHICA+") and id2 not in ("+IDS_YA_REVISADOS_CAJA_CHICA+") ";
        		// *******Fin AGREGADO PARA LOS CASOS YA REVISADOS*******
        		
		return sqlString;
	}	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDuplicadosCajaChica(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosCajaChica(false);
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}	
	public long getDuplicadosCajaChicaSize(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDuplicadosCajaChica(true);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
	
	
	
}
