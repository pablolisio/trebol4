package com.proit.servicios.compras;

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

import com.proit.modelo.Usuario;
import com.proit.modelo.compras.CuentaBancaria;
import com.proit.modelo.compras.EstadoFacturaCompra;
import com.proit.modelo.compras.EstadoSolicitudPago;
import com.proit.modelo.compras.FacturaCompra;
import com.proit.modelo.compras.FacturaSolicitudPago;
import com.proit.modelo.compras.PagoSolicitudPago;
import com.proit.modelo.compras.Proveedor;
import com.proit.modelo.compras.SolicitudPago;
import com.proit.servicios.GenericService;
import com.proit.utils.Constantes;
import com.proit.utils.GeneralValidator;
import com.proit.wicket.FacturarOnLineAuthenticatedWebApplication;

public class SolicitudPagoService extends GenericService<SolicitudPago> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public SolicitudPagoService() {
		super(SolicitudPago.class);
	}
	
	private String buildQueryListaSolicitudes(boolean returnCount, long limit, long offset, String nroFactura, Usuario usuarioSolicitante, String nombreEvento, boolean soloSolicitudesPendientes, boolean incluirBorradas) {
		String sqlString = "SELECT " + ( returnCount ? "COUNT(distinct(s.*))" : "distinct(s.*)" ) + " FROM " +
				"solicitud_pago s, get_nros_factura_compra nros " +
				"where s.id = nros.solicitud_pago_id and s.borrado = false " + 
				( (nroFactura!=null) ? (" and nros.nro_factura_compra = '" + nroFactura + "'" ) : "" ) + 
				( (usuarioSolicitante!=null) ? (" and s.usuario_solicitante_id = '" + usuarioSolicitante.getId() + "'" ) : "" ) + 
				//busco el nombre del evento en el nombre y tb en la razon social del cliente
				( !Strings.isEmpty(nombreEvento) ? " and s.evento_id in ( select e.id from evento e left join cliente c ON c.id = e.cliente_id where (lower(e.nombre) like '%"+nombreEvento.toLowerCase()+"%' or lower(c.razon_social) like '%"+nombreEvento.toLowerCase()+"%')) " : "" ) ;
		
		if (soloSolicitudesPendientes) {
			sqlString += " and ( s.estado_solicitud_pago_id = " + EstadoSolicitudPago.PENDIENTE_1.getId() + " or "
					+ "s.estado_solicitud_pago_id = " + EstadoSolicitudPago.PENDIENTE_2.getId() + " or "
					+ "s.estado_solicitud_pago_id = " + EstadoSolicitudPago.PENDIENTE_3.getId() + " ) ";
		}

		sqlString += ( incluirBorradas ?  "" : " and s.borrado=false") +
				( returnCount ? "" : " ORDER BY s.fecha desc " ) +  //dejarlo asi asc para las sol, en feb/2018 me pidio angela que lo cambie a de mas nuevo a mas viejo
				( returnCount ? "" : " LIMIT " + limit ) +
		        ( returnCount ? "" : " OFFSET " + offset ) ;
		
		return sqlString;
	}

	@SuppressWarnings("unchecked")
	public Iterator<SolicitudPago> getListaSolicitudesPago(long primerResultado, long cantidadResultados, String nroFactura, Usuario usuarioSolicitante, String nombreEvento, boolean soloSolicitudesPendientes) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryListaSolicitudes(false, cantidadResultados, primerResultado, nroFactura, usuarioSolicitante, nombreEvento, soloSolicitudesPendientes, false);
		Query sqlQuery = session.createSQLQuery(sqlString).addEntity(SolicitudPago.class);
		List<SolicitudPago> queryResultList = sqlQuery.list();
		return queryResultList.iterator();
	}
	
	public long getListaSolicitudesPagoSize(String nroFactura, Usuario usuarioSolicitante, String nombreEvento, boolean soloSolicitudesPendientes) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = buildQueryListaSolicitudes(true, 0, 0, nroFactura, usuarioSolicitante, nombreEvento, soloSolicitudesPendientes, false);
		Query sqlQuery = session.createSQLQuery(sqlString);
		BigInteger size = (BigInteger) sqlQuery.uniqueResult();
		return (Long) size.longValue();
	}
	
	public double getTotalSolicitudesPagoPendientes() {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = "select * from total_solicitudes_pago_pendientes";
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
		Criteria criteria = session.createCriteria(SolicitudPago.class);
		//criteria.add(Restrictions.eq("borrado", false)); Busco todos los Nro, tb los nros de OP borradas, porque el Nro debe ser unico
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
	
	public void createOrUpdateSolicitudConProveedor(SolicitudPago solicitudPago, Proveedor proveedor) {
		solicitudPago.setProveedor(proveedor);
		solicitudPago.setRazonSocial(null);
		solicitudPago.setCuitCuil(null);
		solicitudPago.setCbu(null);
		
		EstadoSolicitudPago estadoSolicitudPago;
		if (solicitudPago.isConFactura()) {
			estadoSolicitudPago = EstadoSolicitudPago.PENDIENTE_2;
		} else {
			estadoSolicitudPago = EstadoSolicitudPago.PENDIENTE_3;
		}
		solicitudPago.setEstadoSolicitudPago(estadoSolicitudPago);
		
		super.createOrUpdate(solicitudPago);
	}
	
	@SuppressWarnings("unchecked")
	public void actualizarProveedorEnRestoSolicitudes(Proveedor proveedor, String nro) {		
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		Criteria criteria = session.createCriteria(SolicitudPago.class);
		criteria.add(Restrictions.eq("razonSocial", proveedor.getRazonSocial()));
		criteria.add(Restrictions.eq("cuitCuil", proveedor.getCuitCuil()));
		criteria.add(Restrictions.ne("nro", nro)); //todas las que son distintas al nro pasado por parametro
		criteria.add(Restrictions.eq("estadoSolicitudPago", EstadoSolicitudPago.PENDIENTE_1));
		criteria.add(Restrictions.eq("borrado", false));
		List<SolicitudPago> solicitudes = criteria.list();
		for (SolicitudPago solicitud : solicitudes) {
			createOrUpdateSolicitudConProveedor(solicitud, proveedor);
		}		
	}
	
	public String createOrUpdateSolicitudSPySF(SolicitudPago solicitudPago, CuentaBancaria cuentaBancaria) {		
		if (cuentaBancaria!=null) {
			CuentaBancariaService cuentaBancariaService = new CuentaBancariaService();
			cuentaBancariaService.createOrUpdate(cuentaBancaria);
		}
		String nro = getNextNroSolicitud();
		solicitudPago.setNro(nro); //es el ultimo paso
		super.createOrUpdate(solicitudPago);
		return nro;
	}
	
	public boolean delete(SolicitudPago solicitudPago) {
		if ( ! isBeingUsed(solicitudPago) ) {
			Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
						
			//Elimino los Pagos asociados. Para cualquier tipo de Solicitud
			for (PagoSolicitudPago pago : solicitudPago.getListadoPagos()) {
				pago.setBorrado(true);
			    session.update(pago);
			}
			
			//Elimino las Facturas asociadas, si es que tiene facturas.
			if (solicitudPago.isConFactura()) {
				for (FacturaSolicitudPago factura : solicitudPago.getListadoFacturas()) {
					factura.setBorrado(true);
					session.merge(factura);
				}
			}
			
			//Finalmente elimino la Solicitud. Para cualquier tipo de Solicitud
			super.delete(solicitudPago.getId());
				
			return true;
		}
		return false;
	}
	
	private boolean isBeingUsed(SolicitudPago solicitudPago) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(SolicitudPago.class);
		criteria.add(Restrictions.eq("id", solicitudPago.getId()));
		criteria.add(Restrictions.eq("borrado", false));
		criteria.setProjection(Projections.property("estadoSolicitudPago"));	
		EstadoSolicitudPago estadoSolicitudPago = (EstadoSolicitudPago) criteria.uniqueResult();
		if (estadoSolicitudPago.equals(EstadoSolicitudPago.CUMPLIDA) || estadoSolicitudPago.equals(EstadoSolicitudPago.RECHAZADA)) {
			return true;
		}
		return false;
	}

	public boolean cantidadChequesPermitida(List<PagoSolicitudPago> listadoPagos) {
		int cantidadCheques = 0;
		for (PagoSolicitudPago pago : listadoPagos){
			if (pago.isCheque() ){
				cantidadCheques++;
			}
		}
		return (cantidadCheques<=Constantes.MAX_CANTIDAD_CHEQUES_POR_ORDEN_PERMITIDA);
	}
	
	public boolean cantidadTransferenciasPermitida(List<PagoSolicitudPago> listadoPagos) {
		int cantidadTransferencias = 0;
		for (PagoSolicitudPago pago : listadoPagos){
			if (pago.isTransferencia() ){
				cantidadTransferencias++;
			}
		}
		return (cantidadTransferencias<=Constantes.MAX_CANTIDAD_TRANSFERENCIAS_POR_ORDEN_PERMITIDA);
	}

	public boolean todaFacturaTieneFecha(List<FacturaSolicitudPago> listadoFacturas) {
		for (FacturaSolicitudPago factura : listadoFacturas){
			if (factura.getFechaFacturaCompra()== null){
				return false;
			}
		}
		return true;
	}
	
	public boolean todaFacturaTieneTipo(List<FacturaSolicitudPago> listadoFacturas) {
		for (FacturaSolicitudPago factura : listadoFacturas){
			if (factura.getTipoFacturaCompra()== null){
				return false;
			}
		}
		return true;
	}
	
	public boolean todaFacturaTieneNro(List<FacturaSolicitudPago> listadoFacturas) {
		for (FacturaSolicitudPago factura : listadoFacturas){
			if (factura.getNroFacturaCompra()== null){
				return false;
			}
		}
		return true;
	}
	
	public boolean todaFacturaTieneNroValido(List<FacturaSolicitudPago> listadoFacturas) {
		GeneralValidator generalValidator = new GeneralValidator();
		for (FacturaSolicitudPago factura : listadoFacturas){
			if (!generalValidator.nroFacturaEsValido(factura.getNroFacturaCompra())){
				return false;
			}
		}
		return true;
	}
	
	public boolean todaFacturaYaEnSistemaEsPendiente(List<FacturaSolicitudPago> listadoFacturas, String razonSocialProveedor) {
		FacturaCompraService facturaCompraService = new FacturaCompraService();
		for (FacturaSolicitudPago factura : listadoFacturas){
			FacturaCompra facturaCompra = facturaCompraService.getFacturaByRazonSocialAndTipoAndNroFactura(razonSocialProveedor, factura.getTipoFacturaCompra(), factura.getNroFacturaCompra());
			if (facturaCompra!=null && facturaCompra.getEstadoFactura().equals(EstadoFacturaCompra.CANCELADA)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean todaFacturaYaExisteCargadaEnSistema(List<FacturaSolicitudPago> listadoFacturas, String razonSocialProveedor) {
		FacturaCompraService facturaCompraService = new FacturaCompraService();
		int cantidadFacturasEnSistema = 0;
		for (FacturaSolicitudPago factura : listadoFacturas){
			if (facturaCompraService.nroFacturaAlreadyExists(razonSocialProveedor, factura.getTipoFacturaCompra(), factura.getNroFacturaCompra())) {
				cantidadFacturasEnSistema++;
			}
		}
		return listadoFacturas.size()==cantidadFacturasEnSistema;
	}
	
	public boolean todoChequeTieneFecha(List<PagoSolicitudPago> listadoPagos) {
		for (PagoSolicitudPago pago : listadoPagos){
			if (pago.isCheque() && pago.getFecha()== null){
				return false;
			}
		}
		return true;
	}

	public double calcularSumaImportesTransferencia(List<PagoSolicitudPago> listaPagos) {
		double sumaImportesTransferencia = 0;
		for (PagoSolicitudPago pago : listaPagos){
			if (pago.isTransferencia() ){
				sumaImportesTransferencia += pago.getImporte();
			}
		}
		return sumaImportesTransferencia;
	}
	
	public double calcularSumaTotalPagos(List<PagoSolicitudPago> listaPagos) {
		double sumaTotalPagos = 0;
		for (PagoSolicitudPago pago : listaPagos){
				sumaTotalPagos += pago.getImporte();
		}
		return sumaTotalPagos;
	}
	
}
