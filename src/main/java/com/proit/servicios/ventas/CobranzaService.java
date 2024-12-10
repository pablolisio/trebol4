package com.proit.servicios.ventas;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import com.proit.modelo.ModoPago;
import com.proit.modelo.ventas.Cobranza;
import com.proit.modelo.ventas.Cobro;
import com.proit.modelo.ventas.EstadoFacturaVenta;
import com.proit.modelo.ventas.FacturaVenta;
import com.proit.modelo.ventas.FacturaVentaCobranza;
import com.proit.servicios.GenericService;
import com.proit.utils.Utils;
import com.proit.wicket.FacturarOnLineAuthenticatedWebApplication;

public class CobranzaService extends GenericService<Cobranza> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(CobranzaService.class.getName());
	
	public CobranzaService() {
		super(Cobranza.class);
	}
	
	private String buildQueryListaFacturaVentaCobranza(boolean returnCount, long limit, long offset, String razonSocialCliente, Calendar fecha, boolean soloRetencionesSinValidar, boolean incluirBorradas) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		//Para escapear caso en que una razon social contenga una comilla simple. Ejemplo: Ryan's Travel S.A.
		if (razonSocialCliente!=null) {
			razonSocialCliente = razonSocialCliente.replace("\'", "\'\'");
		}

		String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
				"(" +
				"SELECT cobranza_id, factura_venta_id, borrado " + 
				"FROM ( " +
					"SELECT " +
					"distinct ( fvcob.cobranza_id ) as cobranza_id, null as factura_venta_id, false as borrado, co.fecha as fecha " +
					" FROM factura_venta_cobranza fvcob, cobranza co, factura_venta f, cliente c, estado_factura_venta e" +
					" WHERE fvcob.cobranza_id = co.id and fvcob.factura_venta_id = f.id and f.cliente_id = c.id and f.estado_factura_venta_id = e.id" +
					( (razonSocialCliente!=null) ? (" and c.razon_social = '" + razonSocialCliente + "'" ) : "" ) + 
					( (fecha!=null) ? (" and co.fecha = '" + dateFormat.format(fecha.getTime()) + "'" ) : "" ) + 
					( soloRetencionesSinValidar ?  " and co.retenciones_validadas=false" : "") +
					( incluirBorradas ?  "" : " and co.borrado=false") +
					" ORDER BY co.fecha desc" +
					( returnCount ? "" : " LIMIT " + limit ) +
			        ( returnCount ? "" : " OFFSET " + offset ) +
			    ") AS FVCOB_RESULT " +
				") AS FVCOB_RESULT_FINAL" ;
		return sqlString;
	}
	
	/**
	 * This method gets some info of {@link Cobranza}s from database.
	 * @param primerResultado First result to obtain.
	 * @param cantidadResultados Total {@link Cobranza}s to obtain.
	 * @return Returns {@link Cobranza}s from database.
	 */
	@SuppressWarnings("unchecked")
	public Iterator<FacturaVentaCobranza> getListaFacturaVentaCobranza(long primerResultado, long cantidadResultados, String razonSocialCliente, Calendar fecha, boolean soloRetencionesSinValidar) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryListaFacturaVentaCobranza(false, cantidadResultados, primerResultado, razonSocialCliente, fecha, soloRetencionesSinValidar, true);
		Query sqlQuery = session.createSQLQuery(sqlString).addEntity(FacturaVentaCobranza.class);
		List<FacturaVentaCobranza> queryResultList = sqlQuery.list();
		return queryResultList.iterator();
	}
	
	public long getListaFacturaVentaCobranzaSize(String razonSocialCliente, Calendar fecha, boolean soloRetencionesSinValidar) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryListaFacturaVentaCobranza(true, 0, 0, razonSocialCliente, fecha, soloRetencionesSinValidar, true);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
	/**
	 * Se pueden eliminar Cobranzas sin restriccion. Salvo caso siguiente: 
	 * Caso: creo Cobranza parcial, luego la cancelo (son 2 Cobranzas distintas para la misma Factura). No se puede eliminar las primeras Cobranzas. Solo la ultima
	 * Al eliminar una Cobranza elimino los cobros asociados (listadoCobros) y se pone en estado X Cobrar/Cobrado Parcial a las facturas asociadas (listadoFacturas)
	 * Tambien se elimina la entrada en la tabla de mapeo: factura_venta_cobranza
	 */
	public boolean delete(FacturaVentaCobranza facturaVentaCobranza) {
		Cobranza cobranza = facturaVentaCobranza.getCobranza();
		
		//Siguiendo misma logica que OPs quito esta restriccion en el delete (igual no la habia llegado a probar bien del todo la modificacion)
//		//Caso: creo Cobranza parcial, luego la cancelo (son 2 Cobranzas distintas para la misma Factura). No se puede eliminar las primeras Cobranzas. Solo la ultima
//		Long cantidadCobranzasAsociadasAFactura = cantidadCobranzasAsociadasAFactura(facturaVentaCobranza.getFacturaVenta());
//		if (!cobranza.getListadoFacturas().isEmpty() && cantidadCobranzasAsociadasAFactura>1 ) {
//			int IDUltimaCobranzaAsociada = obtenerIDUltimaCobranzaAsociada(cobranza.getListadoFacturas().get(0));
//			if (cobranza.getId()!= IDUltimaCobranzaAsociada) {
//				return false;
//			}
//		}
		
		try {
			Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
			
			List<Cobro> listadoCobros = getListadoCobros(cobranza.getId());
			
			//Elimino los Cobros asociados.
			for (Cobro cobro : listadoCobros) {
				cobro.setBorrado(true);
			    session.update(cobro);
			}
			
			//Luego modifico los estados de las facturas para que queden como estaban anteriormente
			for (FacturaVenta factura : cobranza.getListadoFacturas()) {
				
				//Logica nueva: Una factura puede tener mas de 2 cobros parciales -> 1 fact puede tener asoc 1,2,3 o mas Cobranzas
				//Logica anterior (antes era una factura con solo 2 cobros parciales -> 1 fact asoc a 2 cobranzas como maximo)
				Long cantidadCobranzasAsociadasAFactura = cantidadCobranzasAsociadasAFactura(factura);
				if (cantidadCobranzasAsociadasAFactura>=2) { //sigue siendo parcial
					factura.setEstadoFacturaVenta(EstadoFacturaVenta.COBRADO_PARCIAL);
				} else {
					factura.setEstadoFacturaVenta(EstadoFacturaVenta.X_COBRAR);
				}
				
				session.merge(factura);
			}
			
			//Luego elimino la/s entrada/s factura_venta_cobranza.
			if (cobranza.getListadoFacturas().size()>1) {
				List<FacturaVentaCobranza> listadoCompleto = obtenerTodasFacturasVentaCobranza(cobranza);
				for (FacturaVentaCobranza facturaVentaCobranzaABorrar : listadoCompleto) {
					facturaVentaCobranzaABorrar.setBorrado(true);
					session.update(facturaVentaCobranzaABorrar);
				}
			} else {
				facturaVentaCobranza.setBorrado(true);
				facturaVentaCobranza.setFacturaVenta(cobranza.getListadoFacturas().get(0));//Como la factura venia en null, la vuelvo a llenar
				session.update(facturaVentaCobranza);
			}
			
			//Finalmente elimino la Cobranza.
			super.delete(cobranza.getId());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
		
		return true;
	}
	
	public Integer createOrUpdateCobranza(Cobranza cobranza, boolean esCobroParcial) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		
		//Las facturas deben quedar como COBRADO o COBRADO PARCIAL , segun corresponda
		for (FacturaVenta factura : cobranza.getListadoFacturas()) {
			if (!esCobroParcial) {
				factura.setEstadoFacturaVenta(EstadoFacturaVenta.COBRADO);
			} else {//Ahora las facturas ventas permiten tener mas de 2 cobros parciales
				if (factura.getEstadoFacturaVenta().equals(EstadoFacturaVenta.COBRADO_PARCIAL)){ //El cobro de ahora es cobro parcial, pero la factura ya estaba como pagada parcial
					double totalNetoFactura = calculateTotalNetoFactura(factura); //En otras pags lo llamo "faltante"
					double totalCobrosCobranzaNueva = calculateTotalCobros(cobranza.getListadoCobros());
					boolean continuaSiendoCobroParcial = cobranza.getListadoFacturas().size()==1
							&& Utils.round(totalNetoFactura,2)>Utils.round(totalCobrosCobranzaNueva,2); //Logica para saber si cobro parcial (buscar este comentario)
					if (!continuaSiendoCobroParcial) {
						factura.setEstadoFacturaVenta(EstadoFacturaVenta.COBRADO);
					}
				} else {
					factura.setEstadoFacturaVenta(EstadoFacturaVenta.COBRADO_PARCIAL);
				}
			}
			session.merge(factura);
		}
		
		//Persisto la Cobranza
		int idCobranza = (Integer) super.createOrUpdate(cobranza);
		
		//Luego se persiste la relacion FacturaVenta-Cobranza
		for (FacturaVenta factura : cobranza.getListadoFacturas()) {
			FacturaVentaCobranza facturaVentaCobranza = new FacturaVentaCobranza();
			facturaVentaCobranza.setCobranza(cobranza);
			facturaVentaCobranza.setFacturaVenta(factura);
			facturaVentaCobranza.setBorrado(false);
			session.saveOrUpdate(facturaVentaCobranza);
		}
		
		return idCobranza;
	}

	public boolean todoChequeTieneBanco(List<Cobro> listadoCobros) {
		for (Cobro cobro : listadoCobros){
			if (cobro.isCheque() && cobro.getBancoCheque()== null){
				return false;
			}
		}
		return true;
	}

	public boolean todoChequeTieneNro(List<Cobro> listadoCobros) {
		for (Cobro cobro : listadoCobros){
			if (cobro.isCheque() && cobro.getNroCheque()== null){
				return false;
			}
		}
		return true;
	}

	public boolean todoChequeTieneFecha(List<Cobro> listadoCobros) {
		for (Cobro cobro : listadoCobros){
			if (cobro.isCheque() && cobro.getFecha()== null){
				return false;
			}
		}
		return true;
	}
	
	public boolean todaTransferenciaTieneBanco(List<Cobro> listadoCobros) {
		for (Cobro cobro : listadoCobros){
			if (cobro.isTransferencia() && cobro.getBancoTransferencia()== null){
				return false;
			}
		}
		return true;
	}

	public boolean todaTransferenciaTieneFecha(List<Cobro> listadoCobros) {
		for (Cobro cobro : listadoCobros){
			if (cobro.isTransferencia() && cobro.getFecha()== null){
				return false;
			}
		}
		return true;
	}
	
	public double calculateTotalCobros(List<Cobro> listadoCobros) {
		double totalAPagar=0;
		for (Cobro cobro : listadoCobros) {
			totalAPagar += cobro.getImporte();
		}
		return totalAPagar;
	}

	public double calculateTotalBrutoFacturas(List<FacturaVenta> listadoFacturas) {
		double totalFacturas=0;
		for (FacturaVenta factura : listadoFacturas) {
			FacturaVentaService facturaVentaService = new FacturaVentaService();
			factura = (FacturaVenta) facturaVentaService.get(factura.getId());
			if ( ! factura.isNotaCredito() ) {
				totalFacturas += factura.calculateTotal();
			} else {
				totalFacturas -= factura.calculateTotal();
			}
		}
		return totalFacturas;
	}
	
	public double calculateTotalNetoFacturas(List<FacturaVenta> listadoFacturas) {
		double totalFacturas=0;
		for (FacturaVenta factura : listadoFacturas) {
			FacturaVentaService facturaVentaService = new FacturaVentaService();
			factura = (FacturaVenta) facturaVentaService.get(factura.getId());
			if ( ! factura.isNotaCredito() ) {
				totalFacturas += calculateTotalNetoFactura(factura);
			} else {
				totalFacturas -= calculateTotalNetoFactura(factura);
			}
		}
		return totalFacturas;
	}
		
	public double calculateTotalNetoFactura(FacturaVenta factura) {
		//si es factura pagada parcial tengo que buscar los cobros ya hechos para poder restarselo al total de la factura.
		if (factura.getEstadoFacturaVenta().equals(EstadoFacturaVenta.COBRADO_PARCIAL)) {
			List<Integer> idsCobranzasAsocAFact = getIdsCobranzas(factura);
			double sumaCobros = 0;
			for (Integer idCobranza : idsCobranzasAsocAFact) {
				List<Cobro> listadoCobros = getListadoCobros(idCobranza);
				for (Cobro cobro : listadoCobros) {
					sumaCobros += cobro.getImporte();
				}
				double sumaRetenciones = getSumaRetenciones(idCobranza);
				sumaCobros += sumaRetenciones;
			}
			
			return factura.calculateTotal() - sumaCobros;
		}
		//Sino es factura pagada parcial, no hace falta saber los cobros ya hechos. 
		return factura.calculateTotal();
	}
	
	private double getSumaRetenciones(int idCobranza) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = "select SUM (perc_iva + perc_iibb + perc_gcias + perc_suss + otras_perc) as suma_retenciones from cobranza where id = " + idCobranza;
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigDecimal result = (BigDecimal) sqlQuery.uniqueResult();
		return result.doubleValue();
	}
	
	@SuppressWarnings("unchecked")
	private List<Cobro> getListadoCobros(int idCobranza) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(Cobro.class)
							.createAlias("cobranza", "co", JoinType.RIGHT_OUTER_JOIN);
		criteria.add(Restrictions.eq("co.id", idCobranza));
		criteria.add(Restrictions.eq("co.borrado", false));
		criteria.add(Restrictions.eq("borrado", false));
		return criteria.list();
	}

	private List<Integer> getIdsCobranzas(FacturaVenta factura) { //Ahora una facturaVenta q sea pagada parcial puede tener mas de una Cobranza asociada
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(FacturaVentaCobranza.class)
							.createAlias("cobranza", "co", JoinType.RIGHT_OUTER_JOIN)
							.createAlias("facturaVenta", "f");
		criteria.add(Restrictions.eq("facturaVenta", factura));
		criteria.add(Restrictions.eq("co.borrado", false));
		criteria.add(Restrictions.eq("f.borrado", false));
		criteria.add(Restrictions.eq("borrado", false));
		criteria.setProjection(Projections.property("co.id"));
		return criteria.list();
	}
	
	private Long cantidadCobranzasAsociadasAFactura(FacturaVenta factura) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(FacturaVentaCobranza.class)
							.createAlias("cobranza", "co", JoinType.RIGHT_OUTER_JOIN)
							.createAlias("facturaVenta", "f");
		criteria.add(Restrictions.eq("facturaVenta", factura));
		criteria.add(Restrictions.eq("co.borrado", false));
		criteria.add(Restrictions.eq("f.borrado", false));
		criteria.add(Restrictions.eq("borrado", false));
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}
	
//	private int obtenerIDUltimaCobranzaAsociada(FacturaVenta factura) {
//		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
//		Criteria criteria = session.createCriteria(FacturaVentaCobranza.class)
//							.createAlias("cobranza", "co", JoinType.RIGHT_OUTER_JOIN)
//							.createAlias("facturaVenta", "f");
//		criteria.add(Restrictions.eq("facturaVenta", factura));
//		criteria.add(Restrictions.eq("co.borrado", false));
//		criteria.add(Restrictions.eq("f.borrado", false));
//		criteria.add(Restrictions.eq("borrado", false));
//		criteria.setProjection(Projections.max("co.id"));
//		//criteria.setProjection(Projections.property("co.id"));
//		int idCobranza = (Integer) criteria.uniqueResult();
//		return idCobranza;
//	}
	
	@SuppressWarnings("unchecked")
	private List<FacturaVentaCobranza> obtenerTodasFacturasVentaCobranza(Cobranza cobranza){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(FacturaVentaCobranza.class)
							.createAlias("cobranza", "co", JoinType.RIGHT_OUTER_JOIN)
							.createAlias("facturaVenta", "f");
		criteria.add(Restrictions.eq("cobranza", cobranza));
		criteria.add(Restrictions.eq("co.borrado", false));
		criteria.add(Restrictions.eq("f.borrado", false));
		criteria.add(Restrictions.eq("borrado", false));
		return criteria.list();
	}

	/**
	 * Para verificar si el Nro de Recibo de Cobranza ya existe 
	 * @param nroRecibo
	 * @return
	 */
	public boolean existsNroRecibo(String nroRecibo) {
		//El nro de recibo es opcional
		if (nroRecibo==null || nroRecibo.isEmpty()) {
			return false;
		}
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(FacturaVentaCobranza.class)
							.createAlias("cobranza", "co", JoinType.RIGHT_OUTER_JOIN)
							.createAlias("facturaVenta", "f");
		criteria.add(Restrictions.eq("co.nroRecibo", nroRecibo));		
		criteria.add(Restrictions.eq("co.borrado", false));
		criteria.add(Restrictions.eq("f.borrado", false));
		criteria.add(Restrictions.eq("borrado", false));
		criteria.setProjection(Projections.rowCount());
		return( (Long) criteria.uniqueResult() ) > 0;
	}
	
	
	
	/*
	 * USADO EN EL REPORTE DE CHEQUES EN CARTERA
	 */
	private String buildQueryChequesEnCartera(boolean returnCount) {
				
        String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
        		"(" +
        		"select  " +
	        		"cliente_cobranza.cliente as cliente, " +
	        		"c.banco_cheque as banco_cheque, " +
	        		"c.nro_cheque as nro_cheque, " +
	        		"c.fecha as fecha, " +
	        		"c.importe as importe  " +
	        	"from cobro c " +
	        	"left join ( " +
	        		"select distinct (cli_co.*)  " +
	        		"from  " +
	        		"( " +
	        		"select " +
	        			"cli.razon_social as cliente, " +
	        			"co.id as cobranza_id " +
	        		"from " +
	        			"factura_venta_cobranza fvcob, " +
	        			"cobranza co, " +
	        			"factura_venta f, " +
	        			"cliente cli  " +
	        		"where " +
	        			"fvcob.cobranza_id = co.id  " +
	        			"and fvcob.factura_venta_id = f.id  " +
	        			"and f.cliente_id = cli.id  " +
	        			"and co.borrado = false  " +
	        			"and f.borrado = false  " +
	        		") cli_co " +
	        	") cliente_cobranza on c.cobranza_id = cliente_cobranza.cobranza_id " +
	        	"where  " +
	        	"c.borrado = false  " +
	        	"and c.modo_cobro_id = " + ModoPago.CHEQUE.getId() + " " +
	        	"and c.fecha > current_date  " +
	        	"order by fecha, cliente " +
		        ") AS CHEQUES_EN_CARTERA";
		return sqlString;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getChequesEnCartera(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryChequesEnCartera(false);
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}
	
	public long getChequesEnCarteraSize(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryChequesEnCartera(true);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
	public double getTotalChequesEnCartera() {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryChequesEnCartera(false);
		sqlString = "SELECT SUM(total_result.importe) as total from ( " + sqlString + " ) as total_result";
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigDecimal total = (BigDecimal) sqlQuery.uniqueResult();
		if (total==null) {
			return 0d;
		}
		return total.doubleValue();
	}
	
}
