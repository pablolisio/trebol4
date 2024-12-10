package com.proit.servicios.ventas;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.proit.modelo.ventas.EstadoFacturaVenta;
import com.proit.utils.Utils;
import com.proit.wicket.FacturarOnLineAuthenticatedWebApplication;

public class ReportesVentasService implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	/*
	 * USADO EN EL REPORTE DE DEUDAS POR CLIENTE
	 */
	private String buildQueryDeudasPorCliente(boolean returnCount) {
		Calendar iniAct = Calendar.getInstance();
		iniAct = Utils.firstMillisecondOfDay(iniAct);
		iniAct.set(Calendar.DAY_OF_MONTH, iniAct.getMinimum(Calendar.DAY_OF_MONTH));
		iniAct.set(Calendar.MONTH, iniAct.getMinimum(Calendar.MONTH));
		
		Calendar finAct = (Calendar) iniAct.clone();
		finAct.add(Calendar.YEAR, 1);
		
		Calendar iniAnt = (Calendar) iniAct.clone();
		iniAnt.add(Calendar.YEAR, -1);
		Calendar finAnt = (Calendar) finAct.clone();
		finAnt.add(Calendar.YEAR, -1);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				
        String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
        		"(" +
        		"select razon_social, subtotal_anio_anterior, subtotal_anio_actual, subtotal_pendiente, " +
        		"(subtotal_anio_anterior+subtotal_anio_actual+subtotal_pendiente) as total " +
        		"from " +
        		"(" +
        		"select c.razon_social, " +
        		"CASE WHEN (subtotal_anio_anterior is null) THEN 0 ELSE subtotal_anio_anterior END as subtotal_anio_anterior, " +
        		"CASE WHEN (subtotal_anio_actual is null) THEN 0 ELSE subtotal_anio_actual END as subtotal_anio_actual, " +
        		"CASE WHEN (subtotal_pendiente is null) THEN 0 ELSE subtotal_pendiente END as subtotal_pendiente " +
        		"from " +
        		"cliente c LEFT JOIN " +
        		"	(" +
        		"		select CASE WHEN (cfs.razon_social is not null) THEN cfs.razon_social ELSE cs.razon_social END as razon_social, " +
        		"				subtotal_anio_anterior, subtotal_anio_actual, cs.subtotal as subtotal_pendiente " +
        		"		from " +
        		"		(select " +
        		"		razon_social, SUM(subtotal_anio_anterior) as subtotal_anio_anterior, SUM(subtotal_anio_actual) as subtotal_anio_actual " +
        		"		from " +
        		"		( select razon_social, " +
        		"		CASE WHEN (fecha >= '"+dateFormat.format(iniAnt.getTime())+"' and fecha < '"+dateFormat.format(finAnt.getTime())+"') THEN subtotal ELSE 0 END as subtotal_anio_anterior, " +
        		"		CASE WHEN (fecha >= '"+dateFormat.format(iniAct.getTime())+"' and fecha < '"+dateFormat.format(finAct.getTime())+"') THEN subtotal ELSE 0 END as subtotal_anio_actual " +
        		"		from subtotal_por_cli_fecha_estado_factura where estado_id = "+EstadoFacturaVenta.X_COBRAR.getId()+") cf " +		//SOLO USO LAS X COBRAR
        		//"		from subtotal_por_cli_fecha_estado_factura where estado_id IN ( "+EstadoFacturaVenta.X_COBRAR.getId()+", " + EstadoFacturaVenta.COBRADO_PARCIAL.getId() + ")) cf " +		//SOLO USO LAS X COBRAR
        		"		group by razon_social " +
        		"		order by razon_social ) cfs FULL JOIN subtotal_por_cliente_solicitud cs ON cfs.razon_social = cs.razon_social " +
        		"	) lista_completa ON c.razon_social = lista_completa.razon_social " +
        		"where c.borrado = false " +
        		"ORDER BY razon_social " +
		        ") lista_con_total " +
        		") AS DEUDAS_POR_CLIENTE";
		return sqlString;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDeudasPorCliente(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDeudasPorCliente(false);
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}
	
	public long getDeudasPorClienteSize(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDeudasPorCliente(true);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
	public double calculateSumDeudasPorCliente(String nombreAtt) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryDeudasPorCliente(false);
		sqlString = "select sum("+nombreAtt+") from ("
				+ sqlString
				+ ") suma";
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigDecimal sum = (BigDecimal) sqlQuery.uniqueResult();
		return (Double) sum.doubleValue();
	}

	
	
	/*
	 * USADO EN EL REPORTE DE VENTAS POR CLIENTE
	 */
	private String buildQueryVentasPorCliente(boolean returnCount, Calendar añoSeleccionado) {
		
		Calendar añoSiguiente = (Calendar) añoSeleccionado.clone();
		añoSiguiente.add(Calendar.YEAR, 1);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy");
		String añoActual = dateFormat.format(añoSeleccionado.getTime());
		String añoProx = dateFormat.format(añoSiguiente.getTime());
		
		int numMesActual = Calendar.getInstance().get(Calendar.MONTH)+1;
				
        String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
        		"(" +
        		"select c.razon_social,  " + 
        		"CASE WHEN (ene is null) THEN 0 ELSE ene END as ene, " + 
        		"CASE WHEN (feb is null) THEN 0 ELSE feb END as feb, " + 
        		"CASE WHEN (mar is null) THEN 0 ELSE mar END as mar, " + 
        		"CASE WHEN (abr is null) THEN 0 ELSE abr END as abr, " + 
        		"CASE WHEN (may is null) THEN 0 ELSE may END as may, " + 
        		"CASE WHEN (jun is null) THEN 0 ELSE jun END as jun, " + 
        		"CASE WHEN (jul is null) THEN 0 ELSE jul END as jul, " + 
        		"CASE WHEN (ago is null) THEN 0 ELSE ago END as ago, " + 
        		"CASE WHEN (sep is null) THEN 0 ELSE sep END as sep, " + 
        		"CASE WHEN (oct is null) THEN 0 ELSE oct END as oct, " + 
        		"CASE WHEN (nov is null) THEN 0 ELSE nov END as nov, " + 
        		"CASE WHEN (dic is null) THEN 0 ELSE dic END as dic, " + 
        		"CASE WHEN (total is null) THEN 0 ELSE total END as total, " + 
        		"CASE WHEN (promedio is null) THEN 0 ELSE promedio END as promedio " + 
        		"from  " + 
        		"cliente c LEFT JOIN " + 
        		"(select razon_social,SUM(ene) as ene,SUM(feb) as feb,SUM(mar) as mar,SUM(abr) as abr,SUM(may) as may,SUM(jun) as jun,SUM(jul) as jul," +
        		"SUM(ago) as ago,SUM(sep) as sep,SUM(oct) as oct,SUM(nov) as nov,SUM(dic) as dic, " + 
        		"SUM(ene+feb+mar+abr+may+jun+jul+ago+sep+oct+nov+dic) as total, " + 
        		"SUM(ene+feb+mar+abr+may+jun+jul+ago+sep+oct+nov+dic)/"+numMesActual+" as promedio " + 
        		"from " + 
        		"(select razon_social, " + 
        		"CASE WHEN (fecha >= '"+añoActual+"-01-01' and fecha < '"+añoActual+"-02-01') THEN subtotal ELSE 0 END as ene, " +
        		"CASE WHEN (fecha >= '"+añoActual+"-02-01' and fecha < '"+añoActual+"-03-01') THEN subtotal ELSE 0 END as feb, " +
        		"CASE WHEN (fecha >= '"+añoActual+"-03-01' and fecha < '"+añoActual+"-04-01') THEN subtotal ELSE 0 END as mar, " +
        		"CASE WHEN (fecha >= '"+añoActual+"-04-01' and fecha < '"+añoActual+"-05-01') THEN subtotal ELSE 0 END as abr, " +
        		"CASE WHEN (fecha >= '"+añoActual+"-05-01' and fecha < '"+añoActual+"-06-01') THEN subtotal ELSE 0 END as may, " +
        		"CASE WHEN (fecha >= '"+añoActual+"-06-01' and fecha < '"+añoActual+"-07-01') THEN subtotal ELSE 0 END as jun, " +
        		"CASE WHEN (fecha >= '"+añoActual+"-07-01' and fecha < '"+añoActual+"-08-01') THEN subtotal ELSE 0 END as jul, " +
        		"CASE WHEN (fecha >= '"+añoActual+"-08-01' and fecha < '"+añoActual+"-09-01') THEN subtotal ELSE 0 END as ago, " +
        		"CASE WHEN (fecha >= '"+añoActual+"-09-01' and fecha < '"+añoActual+"-10-01') THEN subtotal ELSE 0 END as sep, " +
        		"CASE WHEN (fecha >= '"+añoActual+"-10-01' and fecha < '"+añoActual+"-11-01') THEN subtotal ELSE 0 END as oct, " +
        		"CASE WHEN (fecha >= '"+añoActual+"-11-01' and fecha < '"+añoActual+"-12-01') THEN subtotal ELSE 0 END as nov, " +
        		"CASE WHEN (fecha >= '"+añoActual+"-12-01' and fecha < '"+añoProx+"-01-01') THEN subtotal ELSE 0 END as dic " +
        		"from  " + 
        		"subtotal_por_cli_fecha_estado_factura) agrego_totales " + 
        		"GROUP BY razon_social) lista_completa ON c.razon_social = lista_completa.razon_social " + 
        		"where c.borrado = false " + 
        		"ORDER BY total desc, razon_social asc " + 
        		") AS VENTAS_POR_CLIENTE";
		return sqlString;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getVentasPorCliente(Calendar añoSeleccionado){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryVentasPorCliente(false, añoSeleccionado);
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}
	
	public long getVentasPorClienteSize(Calendar añoSeleccionado){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryVentasPorCliente(true, añoSeleccionado);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
	public double calculateSumVentasPorCliente(String nombreAtt, Calendar añoSeleccionado) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryVentasPorCliente(false, añoSeleccionado);
		sqlString = "select sum("+nombreAtt+") from ("
				+ sqlString
				+ ") suma";
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigDecimal sum = (BigDecimal) sqlQuery.uniqueResult();
		return (Double) sum.doubleValue();
	}
}
