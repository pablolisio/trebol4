package com.proit.servicios.compras;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.util.string.Strings;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.proit.modelo.TipoFactura;
import com.proit.modelo.compras.EstadoFacturaCompra;
import com.proit.modelo.compras.FacturaCompra;
import com.proit.servicios.GenericService;
import com.proit.utils.Constantes;
import com.proit.utils.Utils;
import com.proit.wicket.FacturarOnLineAuthenticatedWebApplication;

public class FacturaCompraService extends GenericService<FacturaCompra> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public FacturaCompraService() {
		super(FacturaCompra.class);
	}
	
	private Criteria definirCriteria(String razonSocialProveedor, Calendar fecha, boolean soloPendientesYParciales, Calendar mesImpositivoInicio, Calendar mesImpositivoFin, List<String> nrosFacturasCompra){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(FacturaCompra.class)
							.createAlias("proveedor", "p");
		if (!Strings.isEmpty(razonSocialProveedor)) {
			criteria.add(Restrictions.eq("p.razonSocial", razonSocialProveedor));
		}
		if (fecha!=null) {
			criteria.add(Restrictions.eq("fecha", fecha));
		}
		if (soloPendientesYParciales) {
			criteria.add(Restrictions.or(Restrictions.eq("estadoFactura", EstadoFacturaCompra.PENDIENTE),Restrictions.eq("estadoFactura", EstadoFacturaCompra.PAGADA_PARCIAL)));
		}
		if (mesImpositivoInicio!=null&&mesImpositivoFin!=null) { //--todos-- issue - fixed Reporte Subdiario Compras
			criteria.add(Restrictions.ge("mesImpositivo", mesImpositivoInicio));
			criteria.add(Restrictions.le("mesImpositivo", mesImpositivoFin));
		}
		if (nrosFacturasCompra!=null) {
			criteria.add(Restrictions.in("nro", nrosFacturasCompra));
		}
		
		//No Traigo las Facturas Ficticias (las que comienzan con el Prefijo "S/F-")
		criteria.add(Restrictions.not(Restrictions.ilike("nro", Constantes.PREFIX_NRO_FACTURA_SF + "%")));
		criteria.add(Restrictions.eq("borrado", false));
		return criteria;
	}
	
	/**
	 * This method gets some info of {@link FacturaCompra}s from database.
	 * @param primerResultado First result to obtain.
	 * @param cantidadResultados Total {@link FacturaCompra}s to obtain.
	 * @return Returns {@link FacturaCompra}s from database.
	 */
	@SuppressWarnings("unchecked")
	public Iterator<FacturaCompra> getFacturas(long primerResultado, long cantidadResultados, String razonSocialProveedor, Calendar fecha, boolean soloPendientesYParciales, Calendar mesImpositivoInicio, Calendar mesImpositivoFin) {
		Criteria criteria = definirCriteria(razonSocialProveedor, fecha, soloPendientesYParciales, mesImpositivoInicio, mesImpositivoFin, null);
		criteria.setFirstResult((int) primerResultado);
		criteria.setMaxResults((int) cantidadResultados);
		criteria.addOrder(Order.desc("fecha"));
		return criteria.list().iterator();
	}
	
	public long getFacturasSize(String razonSocialProveedor, Calendar fecha, boolean soloPendientesYParciales, Calendar mesImpositivoInicio, Calendar mesImpositivoFin) {
		Criteria criteria = definirCriteria(razonSocialProveedor, fecha, soloPendientesYParciales, mesImpositivoInicio, mesImpositivoFin, null);
		criteria.setProjection(Projections.rowCount());
		return (Long)criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<FacturaCompra> getFacturas(String razonSocialProveedor, Calendar fecha, boolean soloPendientesYParciales, Calendar mesImpositivoInicio, Calendar mesImpositivoFin, List<String> nrosFacturasCompra) {
		Criteria criteria = definirCriteria(razonSocialProveedor, fecha, soloPendientesYParciales, mesImpositivoInicio, mesImpositivoFin, nrosFacturasCompra);
		criteria.addOrder(Order.asc("p.razonSocial"));
		criteria.addOrder(Order.desc("fecha"));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getNrosFacturaConTipoForSearchBox(String razonSocialProveedor, String nroFacturaParcial) {
		Criteria criteria = definirCriteria(razonSocialProveedor, null, false, null, null, null);
		criteria.add(Restrictions.ilike("nro", "%" + nroFacturaParcial  + "%"));
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("tipoFactura"), "tipoFactura")
				.add(Projections.property("nro"), "nro"));
		criteria.addOrder(Order.asc("nro"));
		List<Object[]> objs = criteria.list();
		List<String> result = new ArrayList<String>();
		for(Object[] obj : objs) {
			result.add(Utils.getTipoYNroFacturaAsString((TipoFactura) obj[0], (String) obj[1]));
		}
		return result;
	}
	
	public double calculateSum(String razonSocialProveedor, Calendar fecha, boolean soloPendientesYParciales, Calendar mesImpositivo, String nombreAtt) {
		//Sumo las facturas tipo A, B y C, y las notas de debito A, B y C (tb suman)
		Criteria criteria1 = definirCriteria(razonSocialProveedor, fecha, soloPendientesYParciales, mesImpositivo, (Calendar)mesImpositivo.clone(), null);
		criteria1.add(Restrictions.or(
					Restrictions.eq("tipoFactura", TipoFactura.TIPO_A),
					Restrictions.eq("tipoFactura", TipoFactura.TIPO_B),
					Restrictions.eq("tipoFactura", TipoFactura.TIPO_C),
					Restrictions.eq("tipoFactura", TipoFactura.NOTA_DEB_A),
					Restrictions.eq("tipoFactura", TipoFactura.NOTA_DEB_B),
					Restrictions.eq("tipoFactura", TipoFactura.NOTA_DEB_C)));
		criteria1.setProjection(Projections.sum(nombreAtt));
		Double resultABCYNDs = (Double) criteria1.uniqueResult();
		
		//Sumo las notas de credito A, B y C
		Criteria criteria2 = definirCriteria(razonSocialProveedor, fecha, soloPendientesYParciales, mesImpositivo, (Calendar)mesImpositivo.clone(), null);
		criteria2.add(Restrictions.or(
				Restrictions.eq("tipoFactura", TipoFactura.NOTA_CRED_A),
				Restrictions.eq("tipoFactura", TipoFactura.NOTA_CRED_B),
				Restrictions.eq("tipoFactura", TipoFactura.NOTA_CRED_C)));
		criteria2.setProjection(Projections.sum(nombreAtt));
		Double resultNotasCred = (Double) criteria2.uniqueResult();
		
		resultABCYNDs 	= resultABCYNDs==null	? 0 : resultABCYNDs;
		resultNotasCred = resultNotasCred==null	? 0 : resultNotasCred;
		
		return (resultABCYNDs - resultNotasCred);
	}
	
	private double sumarFacturas(List<FacturaCompra> facturas) {
		OrdenPagoService ordenPagoService = new OrdenPagoService();
		double result = 0;
		for (FacturaCompra factura : facturas){
			if (factura.getEstadoFactura().equals(EstadoFacturaCompra.PENDIENTE)) {
				if ( ! factura.isNotaCredito() ) {
					result += factura.calculateTotal();
				} else {
					result -= factura.calculateTotal();
				}
			} else if (factura.getEstadoFactura().equals(EstadoFacturaCompra.PAGADA_PARCIAL)) {
				if ( ! factura.isNotaCredito() ) {
					result += ordenPagoService.calculateTotalNetoFactura(factura);
				} else {
					result -= ordenPagoService.calculateTotalNetoFactura(factura);
				}
			}
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public double calculateSaldoProveedoresAnual(Calendar mesInicialAño) {
		mesInicialAño.set(Calendar.MONTH, mesInicialAño.getMinimum(Calendar.MONTH));
		mesInicialAño.set(Calendar.DAY_OF_MONTH, 1);
		mesInicialAño = Utils.firstMillisecondOfDay(mesInicialAño);
		
		Calendar mesFinalAño = (Calendar) mesInicialAño.clone();
		mesFinalAño.set(Calendar.MONTH, mesInicialAño.getMaximum(Calendar.MONTH));
		
		Criteria criteria = definirCriteria(null, null, false, null, null, null);
		criteria.add(Restrictions.ge("mesImpositivo", mesInicialAño));
		criteria.add(Restrictions.le("mesImpositivo", mesFinalAño));
		
		return sumarFacturas(criteria.list());
	}	
	
	public double calculateSaldoPendiente(String razonSocialProveedor, Calendar fecha, boolean soloPendientesYParciales, Calendar mesImpositivo) {
		List<FacturaCompra> facturas = getFacturas(razonSocialProveedor, fecha, soloPendientesYParciales, mesImpositivo, (Calendar)mesImpositivo.clone(), null);
		return sumarFacturas(facturas);
	}
	
	@SuppressWarnings("unchecked")
	public List<FacturaCompra> calculateSaldoMesAMes(Calendar mesImpositivo) {
		Criteria criteria = definirCriteria(null, null, false, mesImpositivo, (Calendar)mesImpositivo.clone(), null);
//		criteria.add(Restrictions.between("mesImpositivo", mesImpositivoDesde, mesImpositivoHasta));
		criteria.addOrder(Order.asc("mesImpositivo"));		
		return criteria.list();
	}
	
	/**
	 * Verifica si la factura ya fue creada previamente para el proveedor utilizando el Tipo y Nro de Factura. 
	 * Se podria crear un nro de fact vta repetido si el TIPO es distinto 
	 * Ej : puede haber una fact a y una NC A con el mismo numero
	 * @return
	 */
	public boolean nroFacturaAlreadyExists(String razonSocial, TipoFactura tipoFactura, String nroFactura){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		Criteria criteria = session.createCriteria(FacturaCompra.class)
							.createAlias("proveedor", "p")
							.createAlias("tipoFactura", "t");
		criteria.add(Restrictions.eq("p.razonSocial", razonSocial).ignoreCase());
		criteria.add(Restrictions.eq("t.id", tipoFactura.getId()));
		criteria.add(Restrictions.eq("nro", nroFactura));
		criteria.add(Restrictions.eq("borrado", false));
		criteria.setProjection(Projections.rowCount());
		return( (Long) criteria.uniqueResult() ) > 0;
	}
	
	/**
	 * Obtiene la FacturaCompra usando la razon social del proveedor y el tipo y nro de factura
	 * @param razonSocial
	 * @param nroFactura
	 * @return
	 */
	public FacturaCompra getFacturaByRazonSocialAndTipoAndNroFactura(String razonSocial, TipoFactura tipoFactura, String nroFactura) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		Criteria criteria = session.createCriteria(FacturaCompra.class)
							.createAlias("proveedor", "p")
							.createAlias("tipoFactura", "t");
		criteria.add(Restrictions.eq("p.razonSocial", razonSocial).ignoreCase());
		criteria.add(Restrictions.eq("t.id", tipoFactura.getId()));
		criteria.add(Restrictions.eq("nro", nroFactura));
		criteria.add(Restrictions.eq("borrado", false));
		return (FacturaCompra) criteria.uniqueResult();
	}
	
	/**
	 * Solo se pueden eliminar facturas pendientes de pago (no canceladas, ni pago parcial).
	 * En caso de poder eliminarla, se retorna TRUE, caso contrario FALSE 
	 * @param factura
	 * @return
	 */
	public String delete(FacturaCompra factura) {
		String whereIsUsed = "La misma no tiene estado " + EstadoFacturaCompra.PENDIENTE.getNombre();
		//Solo se pueden eliminar facturas pendientes de pago (no canceladas, ni pago parcial)
		if (factura.getEstadoFactura().equals(EstadoFacturaCompra.PENDIENTE)) {		
			whereIsUsed = whereIsUsed(factura);
			if ( whereIsUsed.isEmpty() ) {
				super.delete(factura.getId());
			} else {
				whereIsUsed = "La misma esta siendo usado en las siguientes entidades: " + whereIsUsed;
			}
		}
		return whereIsUsed;
	}
	
	private String whereIsUsed(FacturaCompra factura) {
//		long cantFacturasSolicitudPago = getCantidadUsados(FacturaSolicitudPago.class, "facturaCompra", factura);
//		long cantFacturaCompraOrdenPago = getCantidadUsados(FacturaCompraOrdenPago.class, "facturaCompra", factura);		
//		return cantFacturasSolicitudPago>0 || cantFacturaCompraOrdenPago>0 ;
		String result = getUsedFacturaSolicitudPagoAsStr("facturaCompra", factura);
		result += result.isEmpty() ? "" : ". ";
		result += getUsedFacturaCompraOrdenPagoAsStr("facturaCompra", factura);
		result += result.isEmpty() ? "" : ". ";
		return result;
	}

	/*
	 * USADO EN EL REPORTE DE TOTALES DEUDAS POR PROVEEDOR
	 */
	private String buildQueryTotalesFactura(boolean returnCount, String razonSocialProveedor, Calendar fechaCal, boolean soloPendientesYParciales) {
		//Para escapear caso en que una razon social contenga una comilla simple. Ejemplo: Ryan's Travel S.A.
		if (razonSocialProveedor!=null) {
			razonSocialProveedor = razonSocialProveedor.replace("\'", "\'\'");
		}
		
		String fechaStr = null;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (fechaCal!=null) {
			fechaStr = dateFormat.format(fechaCal.getTime());
		}
		
		String querySoloPendientesYParciales = null;
		if (soloPendientesYParciales) {
			querySoloPendientesYParciales = " and (";
			querySoloPendientesYParciales += " f.estado_factura_compra_id = " + EstadoFacturaCompra.PENDIENTE.getId();
			querySoloPendientesYParciales += " OR ";
			querySoloPendientesYParciales += " f.estado_factura_compra_id = " + EstadoFacturaCompra.PAGADA_PARCIAL.getId();
			querySoloPendientesYParciales += ")";
		}
				
        String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
        		"(" +
        		"select "
        		+ "final_result.proveedor_id, final_result.razon_social, final_result.cuit_cuil, "
        		+ "sum(final_result.total_deuda) as total_deuda, "
        		+ "(sum(final_result.total_facturas)-sum(final_result.total_deuda)) as total_pagado, "
        		+ "sum(final_result.total_facturas) as total_facturas\n" + 
        		"from\n" + 
		        "(\n" + 
        		
			        "(\n" + //total_facturas = 0, calculo total_deuda , solo usando las facturas pendientes y parciales (las canceladas no tienen deuda)
				        "select result1.proveedor_id,result1.razon_social, result1.cuit_cuil, sum(result1.total) as total_deuda , 0 as total_facturas\n" + 
				        " FROM\n" + 
				        "(\n" + 
					        "(\n" + 	//calculo total por estado_factura_compra y tipo_factura (los 3 estados: pendiente, parcial y canceladas)
						        "select f.proveedor_id, prov.razon_social, prov.cuit_cuil, f.estado_factura_compra_id,f.tipo_factura_id, \n" + 
						        " CASE \n" +
						        "	WHEN (f.tipo_factura_id<" + TipoFactura.NOTA_CRED_A.getId() + ") THEN ( sum(subtotal) + sum(iva) + sum(perc_iva) + sum(perc_iibb) )\n" + 
						        "	ELSE ( sum(subtotal) + sum(iva) + sum(perc_iva) + sum(perc_iibb) ) * (-1)\n" + 
						        " END as total\n" + 
						        "from  factura_compra f, proveedor prov\n" + 
						        "where f.proveedor_id = prov.id\n" + 
						        "and f.borrado = false and prov.borrado = false\n" + 
						        "and f.nro not like 'S/F%'\n" + 
						        ( razonSocialProveedor!=null ? " and prov.razon_social = '" + razonSocialProveedor + "'" : "" ) +
						        ( fechaStr!=null ? " and f.fecha = '" + fechaStr + "'" : "" ) +
						        ( querySoloPendientesYParciales!=null ? querySoloPendientesYParciales : "" ) +
						        "group by f.proveedor_id, prov.razon_social, prov.cuit_cuil, f.estado_factura_compra_id, f.tipo_factura_id\n" + 
						        "order by f.proveedor_id, f.estado_factura_compra_id, f.tipo_factura_id\n" + 
					        ")\n" + 
					        "UNION\n" + 
					        "(\n" + 	//calculo total pago solo de las de pago parcial
						        "select f.proveedor_id, prov.razon_social, prov.cuit_cuil, f.estado_factura_compra_id,f.tipo_factura_id, sum(p.importe) * (-1) as total\n" + 
						        "from factura_compra_orden_pago fop, orden_pago o, factura_compra f, pago p, proveedor prov\n" + 
						        "where fop.factura_compra_id = f.id and fop.orden_pago_id = o.id and p.orden_pago_id = o.id and f.proveedor_id = prov.id\n" + 
						        "and fop.borrado=false and o.borrado=false and f.borrado=false and p.borrado=false and prov.borrado=false\n" + 
						        "and f.estado_factura_compra_id=" + EstadoFacturaCompra.PAGADA_PARCIAL.getId() +" --solo las de pago parcial\n" + //solo las de pago parcial
						        "and f.nro not like 'S/F%'\n" + 
						        ( razonSocialProveedor!=null ? " and prov.razon_social = '" + razonSocialProveedor + "'" : "" ) +
						        ( fechaStr!=null ? " and f.fecha = '" + fechaStr + "'" : "" ) +
						        ( querySoloPendientesYParciales!=null ? querySoloPendientesYParciales : "" ) +
						        "group by f.proveedor_id, prov.razon_social, prov.cuit_cuil, f.estado_factura_compra_id, f.tipo_factura_id\n" + 
						        "order by f.proveedor_id, f.estado_factura_compra_id, f.tipo_factura_id\n" + 
						    ")\n" + 
						") as result1\n" + 
				        "WHERE (result1.estado_factura_compra_id=" + EstadoFacturaCompra.PENDIENTE.getId() +") or (result1.estado_factura_compra_id=" + EstadoFacturaCompra.PAGADA_PARCIAL.getId() +")\n" + 
				        "group by result1.proveedor_id, result1.razon_social, result1.cuit_cuil\n" + 
				        "order by result1.proveedor_id\n" + 
			        ")\n" + 
			        "UNION\n" + 
			        "(\n" + //total_deuda = 0, calculo total_facturas
				        "select result8.proveedor_id, result8.razon_social , result8.cuit_cuil , 0 as total_deuda, sum(result8.total) as total_facturas\n" + 
				        "from\n" + 
				        "(\n" + //calculo total_facturas por tipo_factura, restando las NCs
					        "select f.proveedor_id, prov.razon_social, prov.cuit_cuil,\n" + 
					        " CASE \n" +
					        "	WHEN (f.tipo_factura_id<" + TipoFactura.NOTA_CRED_A.getId() + ") THEN ( sum(subtotal) + sum(iva) + sum(perc_iva) + sum(perc_iibb) )\n" + 
					        "	ELSE ( sum(subtotal) + sum(iva) + sum(perc_iva) + sum(perc_iibb) ) * (-1)\n" + 
					        " END as total\n" + 
					        "from  factura_compra f, proveedor prov\n" + 
					        "where f.proveedor_id = prov.id\n" + 
					        "and f.borrado = false and prov.borrado=false\n" + 
					        "and f.nro not like 'S/F%'\n" + 
					        ( razonSocialProveedor!=null ? " and prov.razon_social = '" + razonSocialProveedor + "'" : "" ) +
					        ( fechaStr!=null ? " and f.fecha = '" + fechaStr + "'" : "" ) +
					        ( querySoloPendientesYParciales!=null ? querySoloPendientesYParciales : "" ) +
					        "group by f.proveedor_id, prov.razon_social, prov.cuit_cuil, f.tipo_factura_id\n" + 
					        "order by f.proveedor_id, f.tipo_factura_id \n" + 
				        ") as result8\n" + 
				        "group by result8.proveedor_id, result8.razon_social , result8.cuit_cuil\n" + 
				        "order by result8.razon_social\n" + 
			        ")\n" + 
		       
		        ") as final_result\n" + 
		        "group by final_result.proveedor_id, final_result.razon_social , final_result.cuit_cuil\n" + 
		        "order by final_result.razon_social" +
		        ") AS TOTALES_PROVEEDOR";
		return sqlString;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getTotalesPorProveedor(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryTotalesFactura(false, null, null, false);
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}
	
	public long getTotalesPorProveedorSize(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryTotalesFactura(true, null, null, false);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getTotalesPorProveedor(String razonSocialProveedor, Calendar fechaCal, boolean soloPendientesYParciales){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryTotalesFactura(false, razonSocialProveedor, fechaCal, soloPendientesYParciales);
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}
	
	public double calculateSumReporteTotalesPorProveedor(String nombreAtt) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryTotalesFactura(false, null, null, false);
		sqlString = "select sum("+nombreAtt+") from ("
				+ sqlString
				+ ") suma";
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigDecimal sum = (BigDecimal) sqlQuery.uniqueResult();
		return (Double) sum.doubleValue();
	}
	
}
