package com.proit.servicios.compras;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import com.proit.modelo.compras.CuentaBancaria;
import com.proit.modelo.compras.EstadoFacturaCompra;
import com.proit.modelo.compras.FacturaCompra;
import com.proit.modelo.compras.FacturaCompraOrdenPago;
import com.proit.modelo.compras.FacturaSolicitudPago;
import com.proit.modelo.compras.OrdenPago;
import com.proit.modelo.compras.Pago;
import com.proit.modelo.compras.PagoSolicitudPago;
import com.proit.modelo.compras.SolicitudPago;
import com.proit.servicios.GenericService;
import com.proit.utils.Constantes;
import com.proit.utils.Utils;
import com.proit.wicket.FacturarOnLineAuthenticatedWebApplication;

public class OrdenPagoService extends GenericService<OrdenPago> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(OrdenPagoService.class.getName());
	
	public OrdenPagoService() {
		super(OrdenPago.class);
	}
	
	/*			//ESTO ESTABA PARA MOSTRAR EL LISTADO DE OPs USANDO UN RENGLON POR FACTURA (ES DECIR, DETALLADO : Factura - OP)
	private String buildQuery(boolean returnCount, long limit, long offset, String razonSocialProveedor, Calendar fecha, boolean soloSFYConProv, boolean soloFacturasDePagoParcial) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		//Para escapear caso en que una razon social contenga una comilla simple. Ejemplo: Ryan's Travel S.A.
		if (razonSocialProveedor!=null) {
			razonSocialProveedor = razonSocialProveedor.replace("\'", "\'\'");
		}

		String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
				"(" +
				"(SELECT " + //OPs Normales y OPs Con Proveedor y Sin Factura
				"fop.borrado as borrado, fop.orden_pago_id as orden_pago_id, fop.factura_id as factura_id" +
				" FROM factura_compra_orden_pago fop, orden_pago o, factura f, proveedor p, estado_factura_compra e" +
				" WHERE fop.orden_pago_id = o.id and fop.factura_id = f.id and f.proveedor_id = p.id and f.estado_factura_compra_id = e.id" +
				( (razonSocialProveedor!=null) ? (" and p.razon_social = '" + razonSocialProveedor + "'" ) : "" ) + 
				( (fecha!=null) ? (" and o.fecha = '" + dateFormat.format(fecha.getTime()) + "'" ) : "" ) + 
				( soloSFYConProv ? " and f.nro like '" + Constantes.PREFIX_NRO_FACTURA_SF + "%'"  : "" ) + //Nro Fact que comienza con S/F (Factura ficticia creada)
				( soloFacturasDePagoParcial ? " and e.id = " + EstadoFactura.PAGADA_PARCIAL.getId() + " " : ""  ) +
				")" +
				" UNION " + //Aca agrego las OP Sin Proveedor y Sin Factura
				"(SELECT " +
				"false as borrado, o.id as orden_pago_id, null as factura_id" +
				" FROM orden_pago o " +
				" WHERE o.id NOT IN ( SELECT distinct(orden_pago_id) FROM factura_compra_orden_pago )" +
				//Si se filtra por Prov, soloSFYConProv o soloFacturasDePagoParcial NO traigo OPs SPySF
				( (razonSocialProveedor!=null) || soloSFYConProv || soloFacturasDePagoParcial ? (" and 1=2 " ) : "" ) +
				( (fecha!=null) ? (" and o.fecha = '" + dateFormat.format(fecha.getTime()) + "'" ) : "" ) + 
				")" +
				" ORDER BY orden_pago_id desc" +
				( returnCount ? "" : " LIMIT " + limit ) +
		        ( returnCount ? "" : " OFFSET " + offset ) +
				") AS FOP_RESULT" ;
		return sqlString;
	}
	
	/**
	 * This method gets some info of {@link OrdenPago}s from database.
	 * @param primerResultado First result to obtain.
	 * @param cantidadResultados Total {@link OrdenPago}s to obtain.
	 * @return Returns {@link OrdenPago}s from database.
	 *
	@SuppressWarnings("unchecked")
	public Iterator<FacturaOrdenPago> getListaFacturaOrdenPago(long primerResultado, long cantidadResultados, String razonSocialProveedor, Calendar fecha, boolean soloSFYConProv, boolean soloFacturasDePagoParcial) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQuery(false, cantidadResultados, primerResultado, razonSocialProveedor, fecha, soloSFYConProv, soloFacturasDePagoParcial);
		Query sqlQuery = session.createSQLQuery(sqlString).addEntity(FacturaOrdenPago.class);
		List<FacturaOrdenPago> queryResultList = sqlQuery.list();
		return queryResultList.iterator();
	}
	
	public long getListaFacturaOrdenPagoSize(String razonSocialProveedor, Calendar fecha, boolean soloSFYConProv, boolean soloFacturasDePagoParcial) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQuery(true, 0, 0, razonSocialProveedor, fecha, soloSFYConProv, soloFacturasDePagoParcial);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	*/
	
	private String buildQueryListaFacturaOrdenPago(boolean returnCount, long limit, long offset, String razonSocialProveedor, Calendar fecha, Integer idEvento, 
															boolean soloSFYConProv, boolean soloFacturasDePagoParcial, boolean incluirBorradas, Calendar mesInicio, Calendar mesFin, Integer idPlanCuenta, String nroOP) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		//Para escapear caso en que una razon social contenga una comilla simple. Ejemplo: Ryan's Travel S.A.
		if (razonSocialProveedor!=null) {
			razonSocialProveedor = razonSocialProveedor.replace("\'", "\'\'");
		}
		
		String mesWhere = "";
		/*if (mes!=null) {
			Calendar inicio = mes;
			Calendar fin = (Calendar) mes.clone();
			fin.add(Calendar.MONTH, 1);
			mesWhere += " and o.fecha >= '" + dateFormat.format(inicio.getTime()) + "'" ;
			mesWhere += " and o.fecha < '" + dateFormat.format(fin.getTime()) + "'" ;
		}*/
		if (mesInicio!=null&&mesFin!=null) {
			mesWhere += " and o.fecha >= '" + dateFormat.format(mesInicio.getTime()) + "'" ;
			mesWhere += " and o.fecha < '" + dateFormat.format(mesFin.getTime()) + "'" ;
		}
		

		String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
				"(" +
				"(SELECT " + //OPs Normales y OPs Con Proveedor y Sin Factura
				"distinct ( fop.orden_pago_id ) as orden_pago_id, null as factura_compra_id, false as borrado" +
				" FROM factura_compra_orden_pago fop, orden_pago o, factura_compra f, proveedor p, estado_factura_compra e" +
				" WHERE fop.orden_pago_id = o.id and fop.factura_compra_id = f.id and f.proveedor_id = p.id and f.estado_factura_compra_id = e.id" +
				( (razonSocialProveedor!=null) ? (" and p.razon_social = '" + razonSocialProveedor + "'" ) : "" ) + 
				( (nroOP!=null) ? (" and o.nro like '%" + nroOP + "%'" ) : "" ) +
				( (fecha!=null) ? (" and o.fecha = '" + dateFormat.format(fecha.getTime()) + "'" ) : "" ) +
				( (mesInicio!=null&&mesFin!=null) ? (mesWhere) : "" ) + 
				( (idEvento!=null) ? (" and o.evento_id = '" + idEvento + "'" ) : "" ) + 
				( (idPlanCuenta!=null) ? (" and o.plan_cuenta_id = '" + idPlanCuenta + "'" ) : "" ) + 
				( soloSFYConProv ? " and f.nro like '" + Constantes.PREFIX_NRO_FACTURA_SF + "%'"  : "" ) + //Nro Fact que comienza con S/F (Factura ficticia creada)
				( soloFacturasDePagoParcial ? " and e.id = " + EstadoFacturaCompra.PAGADA_PARCIAL.getId() + " " : ""  ) +
				( incluirBorradas ?  "" : " and o.borrado=false") +
				")" +
				" UNION " + //Aca agrego las OP Sin Proveedor y Sin Factura
				"(SELECT " +
				" distinct(o.id) as orden_pago_id, null as factura_compra_id, false as borrado" +
				" FROM orden_pago o " +
				" WHERE o.id NOT IN ( SELECT distinct(orden_pago_id) FROM factura_compra_orden_pago )" +
				//Si se filtra por Prov, soloSFYConProv o soloFacturasDePagoParcial NO traigo OPs SPySF
				( (razonSocialProveedor!=null) || soloSFYConProv || soloFacturasDePagoParcial ? (" and 1=2 " ) : "" ) +
				( (nroOP!=null) ? (" and o.nro like '%" + nroOP + "%'" ) : "" ) +
				( (fecha!=null) ? (" and o.fecha = '" + dateFormat.format(fecha.getTime()) + "'" ) : "" ) + 
				( (mesInicio!=null&&mesFin!=null) ? (mesWhere) : "" ) + 
				( (idEvento!=null) ? (" and o.evento_id = '" + idEvento + "'" ) : "" ) + 
				( (idPlanCuenta!=null) ? (" and o.plan_cuenta_id = '" + idPlanCuenta + "'" ) : "" ) + 
				( incluirBorradas ?  "" : " and o.borrado=false") +
				")" +
				" ORDER BY orden_pago_id desc" +
				( returnCount ? "" : " LIMIT " + limit ) +
		        ( returnCount ? "" : " OFFSET " + offset ) +
				") AS FOP_RESULT" ;
		return sqlString;
	}
	
	/**
	 * This method gets some info of {@link OrdenPago}s from database.
	 * @param primerResultado First result to obtain.
	 * @param cantidadResultados Total {@link OrdenPago}s to obtain.
	 * @param idPlanCuenta 
	 * @return Returns {@link OrdenPago}s from database.
	 */
	@SuppressWarnings("unchecked")
	public Iterator<FacturaCompraOrdenPago> getListaFacturaOrdenPago(long primerResultado, long cantidadResultados, String razonSocialProveedor, Calendar fecha, 
											Integer idEvento, boolean soloSFYConProv, boolean soloFacturasDePagoParcial, boolean incluirBorradas, Calendar mes, Integer idPlanCuenta, String nroOP) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		Calendar mesInicio = null;
		Calendar mesFin = null;
		if (mes!=null) {
			mesInicio = mes;
			mesFin = (Calendar) mes.clone();
			mesFin.add(Calendar.MONTH, 1);
		}
		String sqlString = buildQueryListaFacturaOrdenPago(false, cantidadResultados, primerResultado, razonSocialProveedor, fecha, idEvento, soloSFYConProv, soloFacturasDePagoParcial, 
				incluirBorradas, mesInicio, mesFin, idPlanCuenta, nroOP);
		Query sqlQuery = session.createSQLQuery(sqlString).addEntity(FacturaCompraOrdenPago.class);
		List<FacturaCompraOrdenPago> queryResultList = sqlQuery.list();
		return queryResultList.iterator();
	}
	
	public long getListaFacturaOrdenPagoSize(String razonSocialProveedor, Calendar fecha, Integer idEvento, boolean soloSFYConProv, 
															boolean soloFacturasDePagoParcial, boolean incluirBorradas, Calendar mes, Integer idPlanCuenta, String nroOP) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		Calendar mesInicio = null;
		Calendar mesFin = null;
		if (mes!=null) {
			mesInicio = mes;
			mesFin = (Calendar) mes.clone();
			mesFin.add(Calendar.MONTH, 1);
		}
		String sqlString = buildQueryListaFacturaOrdenPago(true, 0, 0, razonSocialProveedor, fecha, idEvento,soloSFYConProv, soloFacturasDePagoParcial, 
				incluirBorradas, mesInicio, mesFin, idPlanCuenta, nroOP);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
	@SuppressWarnings("unchecked")
	public List<FacturaCompraOrdenPago> getListaFacturaOrdenPago(String razonSocialProveedor, Calendar fecha, Integer idEvento, boolean soloSFYConProv, boolean soloFacturasDePagoParcial, boolean incluirBorradas, Calendar mesInicio, Calendar mesFin, Integer idPlanCuenta) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryListaFacturaOrdenPago(false, 99999999, 0, razonSocialProveedor, fecha, idEvento, soloSFYConProv, soloFacturasDePagoParcial, incluirBorradas, mesInicio, mesFin, idPlanCuenta, null);
		Query sqlQuery = session.createSQLQuery(sqlString).addEntity(FacturaCompraOrdenPago.class);
		return sqlQuery.list();
	}
	
	/**
	 * Se pueden eliminar Ordenes de Pago (OP) sin restriccion. Salvo caso siguiente: 
	 * Caso: creo OP Normal parcial, luego la cancelo (son 2 OPs distintas para la misma Factura). No se puede eliminar las primeras OP. Solo la ultima
	 * Al eliminar una OP elimino los pagos asociados (listadoPagos) y se pone en estado Pendientes/Parcial a las facturas asociadas (listadoFacturas)
	 * Tambien se elimina la entrada en la tabla de mapeo: factura_compra_orden_pago
	 */
	public boolean delete(FacturaCompraOrdenPago facturaOrdenPago) {
		OrdenPago ordenPago = facturaOrdenPago.getOrdenPago();
		
		try {
			
			Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
			

			//-- Modificacion 16-mar-2021, me pidieron quitar esta restriccion. Ahora se pueden eliminar cualquier OP pagada parcial del medio. No tiene que ser si o si la ultima.
			//Caso: creo OP Normal parcial, luego la cancelo (son 2 OPs distintas para la misma Factura). No se puede eliminar las primeras OP. Solo la ultima
			//Las OP Con Prov y Sin Fact si bien tiene Factura Ficticia asociada, en la relacion FacturaOrdenPago existe una sola entrada, por lo tanto 
			//no entra en el siguiente IF. Para Las OP Sin Prov y Sin Fact, la factura viene como null, entonces tampoco entra al IF.
//			if ( !ordenPago.getListadoFacturas().isEmpty() ) {
//				Long cantidadOPsAsociadasAFactura = cantidadOPsAsociadasAFactura(ordenPago.getListadoFacturas().get(0));
//				if (cantidadOPsAsociadasAFactura>1) {
//					int IDUltimaOPAsociada = obtenerIDUltimaOPAsociada(ordenPago.getListadoFacturas().get(0));
//					if (ordenPago.getId()!= IDUltimaOPAsociada) {
//						return false;
//					}
//				}
//			}
			
			List<Pago> listadoPagos = getListadoPagos(ordenPago.getId());
			
			//Elimino los Pagos asociados. Para cualquier tipo de OP
			for (Pago pago : listadoPagos) {
				pago.setBorrado(true);
			    session.update(pago);
			}
			
			//Las OP Normales y las OP Con Prov y Sin Fact (tienen fact ficticia), entraran al siguiente IF. OP Sin Prov y Sin Fact no entran.
			if (!ordenPago.getListadoFacturas().isEmpty()) {
				//Luego modifico los estados de las facturas para que queden como estaban anteriormente
				for (FacturaCompra factura : ordenPago.getListadoFacturas()) {
					Long cantidadOPsAsociadasAFactura = cantidadOPsAsociadasAFactura(factura);
					//Para las OP Con prov y sin fact, se elimina la factura ficticia
					if (factura.getNro().startsWith(Constantes.PREFIX_NRO_FACTURA_SF)) {
						factura.setBorrado(true);
					} else {
						//Logica nueva: Una factura puede tener mas de 2 pagos parciales -> 1 fact puede tener asoc 1,2,3 o mas OPs
						//Logica anterior (antes era una factura con solo 2 pagos parciales -> 1 fact asoc a 2 ops como maximo)
						if (cantidadOPsAsociadasAFactura>=2) { //sigue siendo parcial
							factura.setEstadoFactura(EstadoFacturaCompra.PAGADA_PARCIAL);
						} else {
							factura.setEstadoFactura(EstadoFacturaCompra.PENDIENTE);
						}
					}
					
					session.merge(factura);
				}
			}
			
			//Luego elimino la/s entrada/s factura_compra_orden_pago. Las OP Sin Fact y Sin Prov no entran al siguiente IF
			if (!ordenPago.getListadoFacturas().isEmpty()) {
				if (ordenPago.getListadoFacturas().size()>1) {
					List<FacturaCompraOrdenPago> listadoCompleto = obtenerTodasFacturasOrdenPago(ordenPago);
					for (FacturaCompraOrdenPago facturaOrdenPagoABorrar : listadoCompleto) {
						facturaOrdenPagoABorrar.setBorrado(true);
						session.update(facturaOrdenPagoABorrar);
					}
				} else {
					facturaOrdenPago.setBorrado(true);
					facturaOrdenPago.setFacturaCompra(ordenPago.getListadoFacturas().get(0));//Como la factura venia en null, la vuelvo a llenar
					session.update(facturaOrdenPago);
				}
			}
			
			//Pedido por Alejandro: Agosto2017 - Si la OP tiene una sol pago asociada que la misma (en vez de quedar en cumplida) se elimine 
			if (ordenPago.getSolicitudPago()!=null) {
				Criteria criteria = session.createCriteria(SolicitudPago.class);
				criteria.add(Restrictions.eq("id", ordenPago.getSolicitudPago().getId()));
				SolicitudPago solicitudPago = (SolicitudPago)criteria.uniqueResult();
				//Elimino los Pagos asociados. Para cualquier tipo de Solicitud
				for (PagoSolicitudPago pago : solicitudPago.getListadoPagos()) {
					pago.setBorrado(true);
				}				
				//Elimino las Facturas asociadas, si es que tiene facturas.
				if (solicitudPago.isConFactura()) {
					for (FacturaSolicitudPago factura : solicitudPago.getListadoFacturas()) {
						factura.setBorrado(true);
					}
				}
				//Finalmente elimino la Solicitud. Para cualquier tipo de Solicitud
				solicitudPago.setBorrado(true);
				session.merge(solicitudPago);
			}
			//Fin Pedido por Alejandro: Agosto2017
			
			//Finalmente elimino la OP. Para cualquier tipo de OP
			super.delete(ordenPago.getId());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
		
		return true;
	}
	
	public String createOrUpdateOPNormal(OrdenPago ordenPago, boolean esPagoParcial) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		
		//Las facturas deben quedar como Canceladas o Pagadas Parcial , segun corresponda
		for (FacturaCompra factura : ordenPago.getListadoFacturas()) {
			if (!esPagoParcial) {
				factura.setEstadoFactura(EstadoFacturaCompra.CANCELADA);
			} else {//Ahora las facturas compras permiten tener mas de 2 pagos parciales
				if (factura.getEstadoFactura().equals(EstadoFacturaCompra.PAGADA_PARCIAL)){ //El pago de ahora es pago parcial, pero la factura ya estaba como pagada parcial
					double totalNetoFactura = calculateTotalNetoFactura(factura); //En otras pags lo llamo "faltante"
					double totalPagosOPNueva = calculateTotalPagos(ordenPago.getListadoPagos());
					boolean continuaSiendoPagoParcial = ordenPago.getListadoFacturas().size()==1
							&& Utils.round(totalNetoFactura,2)>Utils.round(totalPagosOPNueva,2); //Logica para saber si pago parcial (buscar este comentario)
					if (!continuaSiendoPagoParcial) {
						factura.setEstadoFactura(EstadoFacturaCompra.CANCELADA);
					}
				} else {
					factura.setEstadoFactura(EstadoFacturaCompra.PAGADA_PARCIAL);
				}
			}
			session.merge(factura);
		}
		
		//Persisto la OrdenPago
		String nroOP = getNextNroOP();
		ordenPago.setNro(nroOP); //ultimo paso
		super.createOrUpdate(ordenPago);
		
		//Luego se persiste la relacion Factura-OrdenPago
		for (FacturaCompra factura : ordenPago.getListadoFacturas()) {
			FacturaCompraOrdenPago facturaCompraOrdenPago = new FacturaCompraOrdenPago();
			facturaCompraOrdenPago.setOrdenPago(ordenPago);
			facturaCompraOrdenPago.setFacturaCompra(factura);
			facturaCompraOrdenPago.setBorrado(false);
			session.saveOrUpdate(facturaCompraOrdenPago);
		}
		
		return nroOP;
	}
	
	public String createOrUpdateOPCPySF(OrdenPago ordenPago) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		
		//Persisto la unica factura
		FacturaCompra factura = ordenPago.getListadoFacturas().get(0);
		session.save(factura);
		
		//Persisto la OrdenPago
		String nroOP = getNextNroOP();
		ordenPago.setNro(nroOP); //ultimo paso
		super.createOrUpdate(ordenPago);
		
		//Luego se persiste la relacion Factura-OrdenPago
		FacturaCompraOrdenPago facturaOrdenPago = new FacturaCompraOrdenPago();
		facturaOrdenPago.setOrdenPago(ordenPago);
		facturaOrdenPago.setFacturaCompra(factura);
		facturaOrdenPago.setBorrado(false);
		session.saveOrUpdate(facturaOrdenPago);
		
		return nroOP;
	}
	
	public String createOrUpdateOPSPySF(OrdenPago ordenPago, CuentaBancaria cuentaBancaria) {
		
		if (cuentaBancaria!=null) {
			CuentaBancariaService cuentaBancariaService = new CuentaBancariaService();
			cuentaBancariaService.createOrUpdate(cuentaBancaria);
		}
		
		//No existen facturas para persistir. Persisto solo la OrdenPago (con sus pagos asociados)
		
		//Persisto la OrdenPago
		String nroOP = getNextNroOP();
		ordenPago.setNro(nroOP); //ultimo paso			
		super.createOrUpdate(ordenPago);
		
		return nroOP;
	}
	
	public boolean cantidadChequesPermitida(List<Pago> listadoPagos) {
		int cantidadCheques = 0;
		for (Pago pago : listadoPagos){
			if (pago.isCheque() ){
				cantidadCheques++;
			}
		}
		return (cantidadCheques<=Constantes.MAX_CANTIDAD_CHEQUES_POR_ORDEN_PERMITIDA);
	}
	
	public boolean cantidadTransferenciasPermitida(List<Pago> listadoPagos) {
		int cantidadTransferencias = 0;
		for (Pago pago : listadoPagos){
			if (pago.isTransferencia() ){
				cantidadTransferencias++;
			}
		}
		return (cantidadTransferencias<=Constantes.MAX_CANTIDAD_TRANSFERENCIAS_POR_ORDEN_PERMITIDA);
	}

	public boolean todoChequeTieneBanco(List<Pago> listadoPagos) {
		for (Pago pago : listadoPagos){
			if (pago.isCheque() && pago.getBanco()== null){
				return false;
			}
		}
		return true;
	}

	public boolean todoChequeTieneNro(List<Pago> listadoPagos) {
		for (Pago pago : listadoPagos){
			if (pago.isCheque() && pago.getNroCheque()== null){
				return false;
			}
		}
		return true;
	}

	public boolean todoChequeTieneFecha(List<Pago> listadoPagos) {
		for (Pago pago : listadoPagos){
			if (pago.isCheque() && pago.getFecha()== null){
				return false;
			}
		}
		return true;
	}
	
	public double calculateTotalPagos(List<Pago> listadoPagos) {
		double totalAPagar=0;
		for (Pago pago : listadoPagos) {
			totalAPagar += pago.getImporte();
		}
		return totalAPagar;
	}

	public double calculateTotalBrutoFacturas(List<FacturaCompra> listadoFacturas) {
		double totalFacturas=0;
		for (FacturaCompra factura : listadoFacturas) {
			if ( ! factura.isNotaCredito() ) {
				totalFacturas += factura.calculateTotal();
			} else {
				totalFacturas -= factura.calculateTotal();
			}
		}
		return totalFacturas;
	}
	
	public double calculateTotalNetoFacturas(List<FacturaCompra> listadoFacturas) {
		double totalFacturas=0;
		for (FacturaCompra factura : listadoFacturas) {
			if ( ! factura.isNotaCredito() ) {
				totalFacturas += calculateTotalNetoFactura(factura);
			} else {
				totalFacturas -= calculateTotalNetoFactura(factura);
			}
		}
		return totalFacturas;
	}
		
	public double calculateTotalNetoFactura(FacturaCompra factura) {
		//si es factura pagada parcial tengo que buscar los pagos ya hechos para poder restarselo al total de la factura.
		if (factura.getEstadoFactura().equals(EstadoFacturaCompra.PAGADA_PARCIAL)) {
			List<Integer> idsOPsAsocAFact = getIdsOPsAsociadasAFactura(factura);
			double sumaPagos = 0;
			for (Integer idOP : idsOPsAsocAFact) {
				List<Pago> listadoPagos = getListadoPagos(idOP);
				for (Pago pago : listadoPagos) {
					sumaPagos += pago.getImporte();
				}
			}
			return factura.calculateTotal() - sumaPagos;
		}
		//Sino es factura pagada parcial, no hace falta saber los pagos ya hechos. 
		return factura.calculateTotal();
	}
	
	@SuppressWarnings("unchecked")
	private List<Pago> getListadoPagos(int idOP) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(Pago.class)
							.createAlias("ordenPago", "o", JoinType.RIGHT_OUTER_JOIN);
		criteria.add(Restrictions.eq("o.id", idOP)); //aca capaz usar IN en vez de eq -> Al final no hizo falta.
		criteria.add(Restrictions.eq("o.borrado", false));
		criteria.add(Restrictions.eq("borrado", false));
		return criteria.list();
	}

	private List<Integer> getIdsOPsAsociadasAFactura(FacturaCompra factura) {//Ahora una fact compra que sea PAGADA PARCIAL empieza a poder tener mas de 1 op asociada
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(FacturaCompraOrdenPago.class)
							.createAlias("ordenPago", "o", JoinType.RIGHT_OUTER_JOIN)
							.createAlias("facturaCompra", "f");
		criteria.add(Restrictions.eq("facturaCompra", factura));
		criteria.add(Restrictions.eq("o.borrado", false));
		criteria.add(Restrictions.eq("f.borrado", false));
		criteria.add(Restrictions.eq("borrado", false));
		criteria.setProjection(Projections.property("o.id"));
		return criteria.list();
	}
	
	private Long cantidadOPsAsociadasAFactura(FacturaCompra factura) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(FacturaCompraOrdenPago.class)
							.createAlias("ordenPago", "o", JoinType.RIGHT_OUTER_JOIN)
							.createAlias("facturaCompra", "f");
		criteria.add(Restrictions.eq("facturaCompra", factura));
		criteria.add(Restrictions.eq("o.borrado", false));
		criteria.add(Restrictions.eq("f.borrado", false));
		criteria.add(Restrictions.eq("borrado", false));
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}
	
//	private int obtenerIDUltimaOPAsociada(FacturaCompra factura) {
//		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
//		Criteria criteria = session.createCriteria(FacturaCompraOrdenPago.class)
//							.createAlias("ordenPago", "o", JoinType.RIGHT_OUTER_JOIN)
//							.createAlias("facturaCompra", "f");
//		criteria.add(Restrictions.eq("facturaCompra", factura));
//		criteria.add(Restrictions.eq("o.borrado", false));
//		criteria.add(Restrictions.eq("f.borrado", false));
//		criteria.add(Restrictions.eq("borrado", false));
//		criteria.setProjection(Projections.max("o.id"));
//		//criteria.setProjection(Projections.property("o.id"));
//		int idOP = (Integer) criteria.uniqueResult();
//		return idOP;
//	}
	
	@SuppressWarnings("unchecked")
	private List<FacturaCompraOrdenPago> obtenerTodasFacturasOrdenPago(OrdenPago ordenPago){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(FacturaCompraOrdenPago.class)
							.createAlias("ordenPago", "o", JoinType.RIGHT_OUTER_JOIN)
							.createAlias("facturaCompra", "f");
		criteria.add(Restrictions.eq("ordenPago", ordenPago));
		criteria.add(Restrictions.eq("o.borrado", false));
		criteria.add(Restrictions.eq("f.borrado", false));
		criteria.add(Restrictions.eq("borrado", false));
		return criteria.list();
	}

	/**
	 * Para verificar si el Nro de OP ya existe 
	 * @param nroOP
	 * @return
	 */
	public boolean existsOPNro(String nroOP) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(FacturaCompraOrdenPago.class)
							.createAlias("ordenPago", "o", JoinType.RIGHT_OUTER_JOIN)
							.createAlias("facturaCompra", "f");
		criteria.add(Restrictions.eq("o.nro", nroOP));		
		criteria.add(Restrictions.eq("o.borrado", false));
		criteria.add(Restrictions.eq("f.borrado", false));
		criteria.add(Restrictions.eq("borrado", false));
		criteria.setProjection(Projections.rowCount());
		return( (Long) criteria.uniqueResult() ) > 0;
	}
	
	/**
	 * Para verificar si existe ya la clave NroOP-NroFactura en la DB 
	 * @param nroOP
	 * @param nroFactura
	 * @return
	 */
	public boolean existsKey(String nroOP, String nroFactura) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(FacturaCompraOrdenPago.class)
							.createAlias("ordenPago", "o", JoinType.RIGHT_OUTER_JOIN)
							.createAlias("facturaCompra", "f");
		criteria.add(Restrictions.eq("o.nro", nroOP));
		criteria.add(Restrictions.eq("f.nro", nroFactura));		
		criteria.add(Restrictions.eq("o.borrado", false));
		criteria.add(Restrictions.eq("f.borrado", false));
		criteria.add(Restrictions.eq("borrado", false));
		criteria.setProjection(Projections.rowCount());
		return( (Long) criteria.uniqueResult() ) > 0;
	}

	/**
	 * Obtengo el Proximo Nro OP a utilizar, sin importar si la ultima OP fue borrada (Nro OP debe ser unico)
	 * Formato Ejemplo: 00001/15  (5 digitos / 2 digitos Año actual)
	 * @return
	 */
	private String getNextNroOP() {
		long maxNro = getMaxNroOPLong();
		String nextNroOP;
		DateFormat dateFormat = new SimpleDateFormat("yy");
		if (maxNro==0) { //Primer OP a crear
			nextNroOP = "00001/" + dateFormat.format(new Date());
		} else {
			maxNro ++;
			nextNroOP = Long.toString(maxNro);
			nextNroOP = String.format("%5s", nextNroOP).replace(' ', '0');
			nextNroOP += "/" + dateFormat.format(new Date());
		}
		return nextNroOP;
	}
	
	private long getMaxNroOPLong() {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(OrdenPago.class);
		//criteria.add(Restrictions.eq("borrado", false)); Busco todos los Nro, tb los nros de OP borradas, porque el Nro debe ser unico
		criteria.setProjection(Projections.max("nro"));
		String maxNroStr = (String) criteria.uniqueResult();
		long maxNro;
		if (maxNroStr==null) { //Primer OP a crear
			maxNro = 0;
		} else {
			String[] maxNroSplitted = maxNroStr.split("/");
			maxNro = Long.parseLong(maxNroSplitted[0]);
		}
		return maxNro;
	}
	
	
	
	/*
	 * USADO PARA REPORTE DE TARJETAS DE CREDITO (TOTALES POR MES)
	 */
	private String buildQueryTotalesTarjetasCredito(boolean returnCount, long limit, long offset, String mesStr, String añoStr) {
		String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
				"(" +				
				"SELECT  " +
				"proveedor, SUM(total) as total " +
				"FROM " +
				"(SELECT  " +
				"orden_pago_id as oid, SUM(importe) as total " +
				"from pago " +
				"where borrado = false " +
				"and modo_pago_id = " + ModoPago.TARJETA_CRED.getId() + " " + //Solo las Tarjeta de Cred
				"group by orden_pago_id " +
				"order by orden_pago_id " +
				") q1 LEFT OUTER JOIN ( " +
					"select  " +
					"o.id as oid, prov.razon_social as proveedor, " +
					"substring(to_char(o.fecha,'YYYY-MM-DD HH24:MI:SS') from 1 for 7) as mes, " +
					"substring(to_char(o.fecha,'YYYY-MM-DD HH24:MI:SS') from 1 for 4) as anio " +
					"from orden_pago o, factura_compra_orden_pago fop, factura_compra f, proveedor prov " +
					"where o.id = fop.orden_pago_id and f.id = fop.factura_compra_id and f.proveedor_id = prov.id " +
					"and o.borrado = false and f.borrado = false " +
					"group by oid,proveedor " +
					"order by o.id " +
				") q2 ON q1.oid = q2.oid " +
				"where 1=1" +
				(mesStr!=null?"and mes = '" + mesStr + "' " : "") +
				(añoStr!=null?"and anio = '" + añoStr + "' " : "") +
				"group by proveedor " +
				"order by proveedor " +
				( returnCount ? "" : " LIMIT " + limit ) +
		        ( returnCount ? "" : " OFFSET " + offset ) +
				") AS RESULT" ;
		return sqlString;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getTotalesTarjetaMensual(long primerResultado, long cantidadResultados, Calendar mes) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		String mesStr = dateFormat.format(mes.getTime());
		String sqlString = buildQueryTotalesTarjetasCredito(false, cantidadResultados, primerResultado, mesStr, null);
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}
	
	public long getTotalesTarjetaMensualSize(Calendar mes) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		String mesStr = dateFormat.format(mes.getTime());
		String sqlString = buildQueryTotalesTarjetasCredito(true, 99999999, 0, mesStr, null);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
	public double getTotalMesTarjetasCredito(Calendar mes) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		String mesStr = dateFormat.format(mes.getTime());
		String sqlString = buildQueryTotalesTarjetasCredito(false, 99999999, 0, mesStr, null);
		sqlString = "SELECT SUM(total_mensual_result.total) as total_anual from ( " + sqlString + " ) as total_mensual_result";
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigDecimal totalAnual = (BigDecimal) sqlQuery.uniqueResult();
		if (totalAnual==null) {
			return 0d;
		}
		return totalAnual.doubleValue();
	}
	
	public double getTotalAnualTarjetasCredito(Calendar año) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		DateFormat dateFormat = new SimpleDateFormat("yyyy");
		String añoStr = dateFormat.format(año.getTime());
		String sqlString = buildQueryTotalesTarjetasCredito(false, 99999999, 0, null, añoStr);
		sqlString = "SELECT SUM(total_anual_result.total) as total_anual from ( " + sqlString + " ) as total_anual_result";
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigDecimal totalAnual = (BigDecimal) sqlQuery.uniqueResult();
		if (totalAnual==null) {
			return 0d;
		}
		return totalAnual.doubleValue();
	}

	
	/*
	 * USADO PARA LISTADO DE PRESUPUESTO BANCO
	 */
	private String buildQueryListadoPresupuestoBanco(boolean returnCount, long limit, long offset, Calendar fecha, boolean mostrarDebitados, boolean mostrarAgregados) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
        String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
        			"(SELECT * FROM " + 
					"( " + 
						"( " + 
							"select  " + 
							"pr.id, detalle as proveedor, b.nombre as banco, '<Agregado>' as nro_cheque, fecha, importe, debitado, true as custom " + 
							"from presupuesto_custom pr " + 
							"left join banco b on pr.banco_id = b.id  " + 
							"where pr.borrado = false " + 
						") UNION ( " + 
							"select " + 
								"pago.id as id, " + 
								"CASE  " + 
									"WHEN (proveedor_orden_pago.proveedor is null) THEN '<Sin Proveedor>'  " + 
									"ELSE proveedor_orden_pago.proveedor  " + 
								"END as proveedor, " + 
								"pago.banco as banco, " + 
								"CASE  " + 
									"WHEN (pago.nro_cheque is null) THEN '<Transferencia>'  " + 
									"ELSE pago.nro_cheque  " + 
								"END as nro_cheque, " + 
								"pago.fecha as fecha, " + 
								"pago.importe as importe, " + 
								"pago.debitado as debitado, " + 
								"false as custom " + 
							"from " + 
								"( select " + 
									"p.*, " + 
									"b.nombre as banco  " + 
								"from pago p  " + 
								"left join banco b on p.banco_id = b.id  " + 
								"where p.borrado = false  " + 
								"and p.modo_pago_id IN (" + ModoPago.CHEQUE.getId() + "," + ModoPago.TRANSFERENCIA.getId() + "," + ModoPago.TRANSFERENCIA_SIN_PROV.getId() +  "," + ModoPago.TRANSFERENCIA_3RO.getId() + ") " +
								") pago left join " + 
									"( " + 
										"select " + 
											"distinct (prov_op.*)    " + 
										"from " + 
											"(  select " + 
												"prov.razon_social as proveedor, " + 
												"op.id as orden_pago_id   " + 
											"from " + 
												"factura_compra_orden_pago fcop, " + 
												"orden_pago op, " + 
												"factura_compra f, " + 
												"proveedor prov    " + 
											"where " + 
												"fcop.orden_pago_id = op.id    " + 
												"and fcop.factura_compra_id = f.id    " + 
												"and f.proveedor_id = prov.id    " + 
												"and op.borrado = false    " + 
												"and f.borrado = false ) prov_op   " + 
									") proveedor_orden_pago on pago.orden_pago_id = proveedor_orden_pago.orden_pago_id   " + 
						   ") " + 
					") AS LISTA_PRESUPUESTO_BANCO where 1=1  " + 
							( (fecha!=null) ? (" and fecha = '" + dateFormat.format(fecha.getTime()) + "'" ) : "" ) +
							(mostrarDebitados ? "" : "and debitado = false ") + //si no tilda "mostrar debitados" trae solo los que no estan debitados
							(mostrarAgregados ? "" : "and custom = false ") + //si tilda "mostrar presupuesto" trae todos
							"order by fecha, proveedor " + 
							( returnCount ? "" : " LIMIT " + limit ) +
					        ( returnCount ? "" : " OFFSET " + offset ) +
					") AS LISTA_PRESUPUESTO_BANCO_FINAL";
		return sqlString;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getPresupuestosBanco(long primerResultado, long cantidadResultados, Calendar fecha, boolean mostrarDebitados, boolean mostrarAgregados){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryListadoPresupuestoBanco(false, cantidadResultados, primerResultado, fecha, mostrarDebitados, mostrarAgregados);
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}
	
	public long getPresupuestosBancoSize(Calendar fecha, boolean mostrarDebitados, boolean mostrarAgregados){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryListadoPresupuestoBanco(true, 0, 0, fecha, mostrarDebitados, mostrarAgregados);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
	public double getTotalPresupuestosBanco(Calendar fecha, boolean mostrarDebitados, boolean mostrarAgregados) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryListadoPresupuestoBanco(false, 99999999, 0, fecha, mostrarDebitados, mostrarAgregados);
		sqlString = "SELECT SUM(total_result.importe) as total from ( " + sqlString + " ) as total_result";
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigDecimal total = (BigDecimal) sqlQuery.uniqueResult();
		if (total==null) {
			return 0d;
		}
		return total.doubleValue();
	}
	
	/*
	 * USADO PARA REPORTE OPs MENSUAL
	 */
	public double sumarTotalPagos(Calendar inicio, Calendar fin) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		//Antes se incluian las OPs borradas. Modif Nov 2021: se quitan las borradas en Reporte OPs Mensual
		String sqlString = "SELECT " + 
							"CASE WHEN (SUM(p.importe) is null) THEN 0 ELSE SUM(p.importe) END as suma " +
							"FROM orden_pago o, pago p" +
							" WHERE o.id = p.orden_pago_id and o.borrado = false and p.borrado = false" +
							((inicio!=null&&fin!=null) ? " and o.fecha >= '"+dateFormat.format(inicio.getTime())+"' and o.fecha < '"+dateFormat.format(fin.getTime())+"' " : "");
		
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		Query sqlQuery = session.createSQLQuery(sqlString);
		return ((BigDecimal) sqlQuery.uniqueResult()).doubleValue();
	}
	
	public double calculateSumTotalPagosMensual(Calendar mes){
		Calendar inicio = mes;
		Calendar fin = (Calendar) mes.clone();
		fin.add(Calendar.MONTH, 1);
		return sumarTotalPagos(inicio, fin);
	}
	
	public double calculateSumTotalPagosAnual(Calendar mesInicialAño){
		mesInicialAño.set(Calendar.MONTH, mesInicialAño.getMinimum(Calendar.MONTH));
		mesInicialAño.set(Calendar.DAY_OF_MONTH, 1);
		mesInicialAño = Utils.firstMillisecondOfDay(mesInicialAño);
		
		Calendar mesInicialAñoSiguiente = (Calendar) mesInicialAño.clone();
		mesInicialAñoSiguiente.add(Calendar.YEAR, 1);
		
		return sumarTotalPagos(mesInicialAño, mesInicialAñoSiguiente);
	}
	
	
	public boolean debitarDesdebitarPago(int idPago) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		Criteria criteria = session.createCriteria(Pago.class);
		criteria.add(Restrictions.eq("id", idPago));
		Pago pago = (Pago) criteria.uniqueResult();
		if (pago.isDebitado()) { 	//intenta desdebitar el pago
			pago.setDebitado(false);
		} else {					//intenta debitar el pago
			pago.setDebitado(true);			
		}
		createOrUpdate(pago);
		return true;
	}
	
	//Usado para reporte de Planes de Cuenta Mensual
	private String buildQueryPlanesDeCuenta(boolean returnCount, Calendar inicio, Calendar fin) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
        String sqlString = "SELECT " + ( returnCount ? "COUNT(*)" : "*" ) + " FROM " +
        			"(SELECT * FROM " + 
						"( " + 
							"select pl.id as idPlanCuenta, pl.nombre as nombrePlanCuenta, SUM(p.importe) as total " + 
							"from orden_pago o, plan_cuenta pl, pago p " + 
							"where pl.id = o.plan_cuenta_id and p.orden_pago_id = o.id " + 
							"and o.borrado = false and pl.borrado = false and p.borrado = false " + 
							((inicio!=null&&fin!=null) ? " and o.fecha >= '"+dateFormat.format(inicio.getTime())+"' and o.fecha < '"+dateFormat.format(fin.getTime())+"' " : "") +
							"group by pl.id " + 
							"order by pl.nombre asc " +
						") AS planes " + 
					") AS LISTA_PLANES_CUENTAS_MENSUAL";
		return sqlString;
	}
	
	private String buildQueryPlanesDeCuentaMensual(boolean returnCount, Calendar mes) {
		Calendar inicio = null;
		Calendar fin = null;
		if (mes!=null) { //--todos-- issue - fixed Reporte Planes de Cuenta Mensual
			inicio = mes;
			fin = (Calendar) mes.clone();
			fin.add(Calendar.MONTH, 1);
		}
		return buildQueryPlanesDeCuenta(returnCount, inicio, fin);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getPlanesDeCuentaMensual(Calendar mes, Calendar año){
		String sqlString = null;
		if (mes!=null) {
			sqlString = buildQueryPlanesDeCuentaMensual(false, mes);
		} else {
			Calendar mesInicialAño = (Calendar) año.clone();
			mesInicialAño.set(Calendar.MONTH, mesInicialAño.getMinimum(Calendar.MONTH));
			mesInicialAño.set(Calendar.DAY_OF_MONTH, 1);
			mesInicialAño = Utils.firstMillisecondOfDay(mesInicialAño);
			
			Calendar mesInicialAñoSiguiente = (Calendar) mesInicialAño.clone();
			mesInicialAñoSiguiente.add(Calendar.YEAR, 1);
			sqlString = buildQueryPlanesDeCuenta(false, mesInicialAño, mesInicialAñoSiguiente);
		}
		
		
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();
	}
	
	public long getPlanesDeCuentaMensualSize(Calendar mes){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryPlanesDeCuentaMensual(true, mes);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
	public double calculateSumPlanesDeCuentaMensual(Calendar mes) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryPlanesDeCuentaMensual(false, mes);
		sqlString = "select sum(total) from (" //'total' sale de "select pl.nombre, SUM(p.importe) as total "
				+ sqlString
				+ ") as sumaMensual";
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigDecimal sum = (BigDecimal) sqlQuery.uniqueResult();
		if (sum!=null) {
			return (Double) sum.doubleValue();
		} else {
			return 0d;
		}
	}
	
	public double calculateSumPlanesDeCuentaAnual(Calendar mesInicialAño) {
		mesInicialAño.set(Calendar.MONTH, mesInicialAño.getMinimum(Calendar.MONTH));
		mesInicialAño.set(Calendar.DAY_OF_MONTH, 1);
		mesInicialAño = Utils.firstMillisecondOfDay(mesInicialAño);
		
		Calendar mesInicialAñoSiguiente = (Calendar) mesInicialAño.clone();
		mesInicialAñoSiguiente.add(Calendar.YEAR, 1);
		
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryPlanesDeCuenta(false, mesInicialAño, mesInicialAñoSiguiente);
		sqlString = "select sum(total) from (" //'total' sale de "select pl.nombre, SUM(p.importe) as total "
				+ sqlString
				+ ") as sumaAnual";
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigDecimal sum = (BigDecimal) sqlQuery.uniqueResult();
		if (sum!=null) {
			return (Double) sum.doubleValue();
		} else {
			return 0d;
		}
	}
	
}
