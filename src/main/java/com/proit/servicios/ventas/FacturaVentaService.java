package com.proit.servicios.ventas;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import com.proit.modelo.ventas.DatoAdicionalFacturaVenta;
import com.proit.modelo.ventas.DetalleFacturaVenta;
import com.proit.modelo.ventas.EstadoFacturaVenta;
import com.proit.modelo.ventas.EstadoSolicitudFactura;
import com.proit.modelo.ventas.FacturaVenta;
import com.proit.servicios.GenericService;
import com.proit.utils.Constantes;
import com.proit.utils.Utils;
import com.proit.wicket.FacturarOnLineAuthenticatedWebApplication;

public class FacturaVentaService extends GenericService<FacturaVenta> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public FacturaVentaService() {
		super(FacturaVenta.class);
	}
	
	private Criteria definirCriteria(String razonSocialCliente, Integer idEvento, Calendar fecha, boolean soloXCobrarYParciales, List<String> nrosFacturasVenta,
												Calendar fechaInicio, Calendar fechaFin, boolean ocultarFacturasTipoN){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(FacturaVenta.class)
							.createAlias("cliente", "c")
							.createAlias("evento", "e");
		if (!Strings.isEmpty(razonSocialCliente)) {
			criteria.add(Restrictions.eq("c.razonSocial", razonSocialCliente));
		}
		if (idEvento!=null) {
			criteria.add(Restrictions.eq("e.id", idEvento));
		}
		if (fecha!=null) {
			criteria.add(Restrictions.eq("fecha", fecha));
		}
		if (soloXCobrarYParciales) {
			criteria.add(Restrictions.or(Restrictions.eq("estadoFacturaVenta", EstadoFacturaVenta.X_COBRAR),Restrictions.eq("estadoFacturaVenta", EstadoFacturaVenta.COBRADO_PARCIAL)));
		}
		if (nrosFacturasVenta!=null) {
			criteria.add(Restrictions.in("nro", nrosFacturasVenta));
		}
		if (ocultarFacturasTipoN) {
			criteria.add(Restrictions.ne("tipoFactura", TipoFactura.TIPO_N));
			criteria.add(Restrictions.ne("tipoFactura", TipoFactura.NOTA_CRED_N));
		}
		
		/*if (mes!=null) { //--todos-- issue - fixed Reporte Subdiario Ventas
			Calendar inicio = mes;
			Calendar fin = (Calendar) mes.clone();
			fin.add(Calendar.MONTH, 1);
			criteria.add(Restrictions.ge("fecha", inicio));
			criteria.add(Restrictions.lt("fecha", fin));
		}*/
		if (fechaInicio!=null && fechaFin!=null) {
			criteria.add(Restrictions.ge("fecha", fechaInicio));
			criteria.add(Restrictions.lt("fecha", fechaFin));
		}
		
		//No Traigo las Facturas Ficticias (las que comienzan con el Prefijo "S/F-")
		criteria.add(Restrictions.not(Restrictions.ilike("nro", Constantes.PREFIX_NRO_FACTURA_SF + "%")));
		criteria.add(Restrictions.eq("borrado", false));
		return criteria;
	}
	
	/**
	 * OJO: Este metodo usarlo en el FacturasVentaDataProvider nomas, porque en este metodo ademas se va a la db para obtener las facturas que fueron usadas en una NC
	 * This method gets some info of {@link FacturaVenta}s from database.
	 * @param primerResultado First result to obtain.
	 * @param cantidadResultados Total {@link FacturaVenta}s to obtain.
	 * @param ocultarFacturasTipoN 
	 * @return Returns {@link FacturaVenta}s from database.
	 */
	@SuppressWarnings("unchecked")
	public Iterator<FacturaVenta> getFacturas(long primerResultado, long cantidadResultados, String razonSocialCliente, Integer idEvento, Calendar fecha, boolean soloXCobrarYParciales, Calendar mes, boolean ocultarFacturasTipoN) {
		Calendar fechaInicio = null;
		Calendar fechaFin = null;
		if (mes!=null) {
			fechaInicio = (Calendar) mes.clone();
			fechaFin = (Calendar) mes.clone();
			fechaFin.add(Calendar.MONTH, 1);			
		}
		Criteria criteria = definirCriteria(razonSocialCliente, idEvento, fecha, soloXCobrarYParciales, null, fechaInicio, fechaFin, ocultarFacturasTipoN);
		criteria.setFirstResult((int) primerResultado);
		criteria.setMaxResults((int) cantidadResultados);
		criteria.addOrder(Order.asc("nro"));
		List<FacturaVenta> listaFacturas = criteria.list();
		
		//Verifico si la factura venta tiene asociada una Nota de Credito (para luego poder ocultar el btn de Editar y Eliminar)
		List<Integer> idsFacturasVentasQueTienenAsocUnaNC = getIdsFacturasVentasQueTienenAsocUnaNC();
		for (FacturaVenta factura : listaFacturas) {
			if (idsFacturasVentasQueTienenAsocUnaNC.contains(factura.getId())) {
				factura.setAsociadoConUnaNC(true);
			}
		}
		
		return listaFacturas.iterator();
	}
	
	/**
	 * Retorna un listado de ids de facturas de ventas que fueron usadas para crear una Nota de Credito (sea NC A o NC B, trae ambas)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Integer> getIdsFacturasVentasQueTienenAsocUnaNC() {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = "select distinct(factura_a_anular_id) " + 
							"from factura_venta " + 
							"where borrado = false and factura_a_anular_id is not null " + 
							"order by factura_a_anular_id";
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}

	public long getFacturasSize(String razonSocialCliente, Integer idEvento, Calendar fecha, boolean soloXCobrarYParciales, Calendar mes, boolean ocultarFacturasTipoN) {
		Calendar fechaInicio = null;
		Calendar fechaFin = null;
		if (mes!=null) {
			fechaInicio = (Calendar) mes.clone();
			fechaFin = (Calendar) mes.clone();
			fechaFin.add(Calendar.MONTH, 1);
		}
		Criteria criteria = definirCriteria(razonSocialCliente, idEvento, fecha, soloXCobrarYParciales, null, fechaInicio, fechaFin, ocultarFacturasTipoN);
		criteria.setProjection(Projections.rowCount());
		return (Long)criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<FacturaVenta> getFacturas(String razonSocialCliente, Integer idEvento, Calendar fecha, boolean soloXCobrarYParciales, List<String> nrosFacturasVenta, Calendar fechaInicio, Calendar fechaFin, boolean ocultarFacturasTipoN) {
		Criteria criteria = definirCriteria(razonSocialCliente, idEvento, fecha, soloXCobrarYParciales, nrosFacturasVenta, fechaInicio, fechaFin, ocultarFacturasTipoN);
//		criteria.addOrder(Order.asc("c.razonSocial"));
		criteria.addOrder(Order.asc("nro"));
		return criteria.list();
	}
		
	/**
	 * Verifica si la factura ya fue creada previamente utilizando el Tipo y Nro de Factura. 
	 * Se podria crear un nro de fact vta repetido si el TIPO es distinto 
	 * Ej : puede haber una fact a y una NC A con el mismo numero
	 * @param nroFactura
	 * @return
	 */
	public boolean nroFacturaAlreadyExists(TipoFactura tipoFactura, String nroFactura, int idFacturaActual){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		Criteria criteria = session.createCriteria(FacturaVenta.class)
									.createAlias("tipoFactura", "t");
		criteria.add(Restrictions.eq("t.id", tipoFactura.getId()));
		criteria.add(Restrictions.eq("nro", nroFactura));
		criteria.add(Restrictions.eq("borrado", false));
		if (idFacturaActual!=0) {
			criteria.add(Restrictions.ne("id", idFacturaActual));
		}
		criteria.setProjection(Projections.rowCount());
		return( (Long) criteria.uniqueResult() ) > 0;
	}
	
	/**
	 * Obtiene la FacturaVenta usando el Tipo y Nro de factura
	 * @param razonSocial
	 * @param nroFactura
	 * @return
	 */
	public FacturaVenta getFacturaByTipoYNroFactura(TipoFactura tipoFactura, String nroFactura) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		Criteria criteria = session.createCriteria(FacturaVenta.class)
									.createAlias("tipoFactura", "t");
		criteria.add(Restrictions.eq("t.id", tipoFactura.getId()));
		criteria.add(Restrictions.eq("nro", nroFactura));
		criteria.add(Restrictions.eq("borrado", false));
		return (FacturaVenta) criteria.uniqueResult();
	}

	public boolean todoDetalleTieneCantidadMayorACero(List<DetalleFacturaVenta> listaDetalles) {
		for (DetalleFacturaVenta detalle : listaDetalles){
			if (detalle.getCantidad()<1){
				return false;
			}
		}
		return true;
	}

	public boolean todoDetalleTieneImporteDistintoACero(List<DetalleFacturaVenta> listaDetalles) {
		for (DetalleFacturaVenta detalle : listaDetalles){
			if (detalle.getImporte()==0){
				return false;
			}
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private double sumarSubtotal(Calendar inicio, Calendar fin, Integer idEvento, boolean ocultarFacturasTipoN) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String sqlString = "select f.tipo_factura_id, SUM(d.cantidad * d.importe) as subtotal ";
		sqlString += "from factura_venta f , detalle_factura_venta d ";
		sqlString += "where d.factura_venta_id = f.id ";
		sqlString += "and f.borrado = false and d.borrado = false ";
		sqlString += ocultarFacturasTipoN ? "and f.tipo_factura_id NOT IN (" + TipoFactura.TIPO_N.getId() + ", " + + TipoFactura.NOTA_CRED_N.getId() + ") " : "";
		sqlString += (inicio!=null&&fin!=null) ? "and f.fecha >= '"+dateFormat.format(inicio.getTime())+"' and f.fecha < '"+dateFormat.format(fin.getTime())+"' " : "";
		//sqlString += "and f.fecha >= '2016-07-01' and f.fecha < '2016-07-29' ";
		sqlString += idEvento!=null ? "and f.evento_id = " + idEvento + " " : "";
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
	private double sumarIVA(Calendar inicio, Calendar fin, Integer idEvento) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");		
		String sqlString = "select tipo_factura_id as tipo_factura_id, SUM(iva) as iva from ( ";
		sqlString += "select f.tipo_factura_id as tipo_factura_id, ";
		sqlString += "CASE WHEN (f.tipo_factura_id in ("+TipoFactura.TIPO_A.getId()+", "+TipoFactura.NOTA_DEB_A.getId()+", "+TipoFactura.NOTA_CRED_A.getId()+", "+TipoFactura.TIPO_FCE.getId()+", "+TipoFactura.NOTA_DEB_FCE.getId()+", "+TipoFactura.NOTA_CRED_FCE.getId()+")) ";
		sqlString += " THEN (d.cantidad * d.importe * 0.21) "; //TODO falta done
		sqlString += " ELSE 0 ";
		sqlString += "END as iva ";
		sqlString += "from factura_venta f , detalle_factura_venta d ";
		sqlString += "where d.factura_venta_id = f.id ";
		sqlString += "and f.borrado = false and d.borrado = false ";
		//sqlString += ocultarFacturasTipoN ? "and f.tipo_factura_id NOT IN (" + TipoFactura.TIPO_N.getId() + ", " + + TipoFactura.NOTA_CRED_N.getId() + ") " : ""; //No agrego esta linea porque no se tienen en cuenta las Tipo N en este query
		sqlString += (inicio!=null&&fin!=null) ? "and f.fecha >= '"+dateFormat.format(inicio.getTime())+"' and f.fecha < '"+dateFormat.format(fin.getTime())+"' " : "";		
		//sqlString += "and f.fecha >= '2016-07-01' and f.fecha < '2016-07-29' ";
		sqlString += idEvento!=null ? "and f.evento_id = " + idEvento + " " : "";
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
	
	public double calculateSumSubtotalMensual(Calendar mes){
		Calendar inicio = mes;
		Calendar fin = (Calendar) mes.clone();
		fin.add(Calendar.MONTH, 1);
		return sumarSubtotal(inicio, fin, null, true);
	}
	
	public double calculateSumSubtotalAnual(Calendar mesInicialAño){
		mesInicialAño.set(Calendar.MONTH, mesInicialAño.getMinimum(Calendar.MONTH));
		mesInicialAño.set(Calendar.DAY_OF_MONTH, 1);
		mesInicialAño = Utils.firstMillisecondOfDay(mesInicialAño);
		
		Calendar mesInicialAñoSiguiente = (Calendar) mesInicialAño.clone();
		mesInicialAñoSiguiente.add(Calendar.YEAR, 1);
		
		return sumarSubtotal(mesInicialAño, mesInicialAñoSiguiente, null, true);
	}
	
	public double calculateSumSubtotalPorEvento(Integer idEvento){		
		return sumarSubtotal(null, null, idEvento, false);
	}
	
	public double calculateSumIVAMensual(Calendar mes){
		Calendar inicio = mes;
		Calendar fin = (Calendar) mes.clone();
		fin.add(Calendar.MONTH, 1);
		return sumarIVA(inicio, fin, null);
	}
	
	public double calculateSumIVAPorEvento(Integer idEvento){		
		return sumarIVA(null, null, idEvento);
	}
	
	
	public void createOrUpdateFacturaVenta(FacturaVenta facturaVenta, String nroFacturaVentaSinPrefijo) {
		//Las facturas de tipo N tienen numero automatico incremental (idem para Nota de Credito N)
		if (facturaVenta.getTipoFactura().isTipoN() || facturaVenta.getTipoFactura().isNotaCreditoN()) {
			String nro = getNextNroFacturaVenta(facturaVenta.getTipoFactura());
			facturaVenta.setNro(nro); //es el ultimo paso
		} else {			
			String nroFacturaVentaFinal = Utils.getNroFacturaVentaConPrefijo(facturaVenta.getTipoFactura(), nroFacturaVentaSinPrefijo);
			facturaVenta.setNro(nroFacturaVentaFinal);
		}		
		super.createOrUpdate(facturaVenta);
	}
	
	/**
	 * Obtengo el Proximo Nro Factura Venta N a utilizar, sin importar si la ultima Factura Venta N fue borrada (Nro Factura debe ser unico)
	 * UPDATE: No solo sirve para facturas N, para Nota de Credito N tambien sirve
	 * Formato Ejemplo: 00001  (5 digitos)
	 * @return
	 */
	private String getNextNroFacturaVenta(TipoFactura tipoFactura) {
		long maxNro = getMaxNroFacturaVentaLong(tipoFactura);
		String nextNroSolicitud;
		if (maxNro==0) { //Primer nro a crear
			nextNroSolicitud = "00001";
		} else {
			maxNro ++;
			nextNroSolicitud = Long.toString(maxNro);
			nextNroSolicitud = String.format("%5s", nextNroSolicitud).replace(' ', '0');
		}
		return nextNroSolicitud;
	}
	
	private long getMaxNroFacturaVentaLong(TipoFactura tipoFactura) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(FacturaVenta.class)
								.createAlias("tipoFactura", "t");
		criteria.add(Restrictions.eq("t.id", tipoFactura.getId())); //Busco solo las de tipo pasado por param
		//criteria.add(Restrictions.eq("borrado", false)); Busco todos los Nro, tb los nros de Fac Vta borradas, porque el Nro debe ser unico
		criteria.setProjection(Projections.max("nro"));
		String maxNroStr = (String) criteria.uniqueResult();
		long maxNro;
		if (maxNroStr==null) { //Primer Nro a crear
			maxNro = 0;
		} else {
			maxNro = Long.parseLong(maxNroStr);
		}
		return maxNro;
	}
	
	/**
	 * Solo se pueden eliminar facturas X COBRAR (no se podran eliminar las que tienen estado COBRADO, COBRADO PARCIAL o ANULADO).
	 * En caso de poder eliminarla, se retorna TRUE, caso contrario FALSE 
	 * @param factura
	 * @return
	 */
	public String delete(FacturaVenta factura) {
		String whereIsUsed = "La misma no tiene estado " + EstadoFacturaVenta.X_COBRAR.getNombre();
		//Solo se pueden eliminar facturas pendientes de pago (no canceladas, ni pago parcial)
		if (factura.getEstadoFacturaVenta().equals(EstadoFacturaVenta.X_COBRAR)) {
			whereIsUsed = whereIsUsed(factura);
			if ( whereIsUsed.isEmpty() ) {
				factura = (FacturaVenta) get(factura.getId());
				
				Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
				
				//Si tiene solicitud factura venta asociada, la solicitud pasa a ser pendiente nuevamente. 
				if (factura.getSolicitudFacturaVenta()!=null) {
					factura.getSolicitudFacturaVenta().setEstadoSolicitudFactura(EstadoSolicitudFactura.PENDIENTE);
				}
				
				//tb se deben eliminar los listados de DetalleFacturaVenta y DatoAdicionalFacturaVenta
				for (DetalleFacturaVenta detalle : factura.getListadoDetalles()) {
					detalle.setBorrado(true);
				}
				for (DatoAdicionalFacturaVenta datoAdic : factura.getListadoDatosAdicionales()) {
					datoAdic.setBorrado(true);
				}
				//Elimino ahora la factura
				factura.setBorrado(true);
				
				session.update(factura);
			} else {
				whereIsUsed = "La misma esta siendo usado en las siguientes entidades: " + whereIsUsed;
			}
		}
		return whereIsUsed;
	}
	
	private String whereIsUsed(FacturaVenta factura) {
//		long cantFacturaVentaCobranza = getCantidadUsados(FacturaVentaCobranza.class, "facturaVenta", factura); //Lo verifico por las dudas, pero solo se pueden eliminar Facts X Cobrar
//		long cantSolicitudFacturaVenta = getCantidadUsados(SolicitudFacturaVenta.class, "facturaVentaAAnular", factura);
//		long cantFacturaVentaAAnular = getCantidadUsados(FacturaVenta.class, "facturaVentaAAnular", factura);
//		return cantFacturaVentaCobranza>0 || cantSolicitudFacturaVenta>0 || cantFacturaVentaAAnular>0 ;
		
		String usedSolicitudFacturaVentaAsStr = getUsedSolicitudFacturaVentaAsStr("facturaVentaAAnular", factura);
		String usedFacturaVentaAsStr = getUsedFacturaVentaAsStr("facturaVentaAAnular", factura);
		
		String result = getUsedFacturaVentaCobranzaAsStr("facturaVenta", factura); //Lo verifico por las dudas, pero solo se pueden eliminar Facts X Cobrar
		result += result.isEmpty() ? "" : ". ";		
		result += usedSolicitudFacturaVentaAsStr.isEmpty() ? "" : ("(A anular) " + usedSolicitudFacturaVentaAsStr);
		result += result.isEmpty() ? "" : ". ";		
		result += usedFacturaVentaAsStr.isEmpty() ? "" : ("(A anular) " + usedFacturaVentaAsStr);
		result += result.isEmpty() ? "" : ". ";
		return result;
	}
		
}
