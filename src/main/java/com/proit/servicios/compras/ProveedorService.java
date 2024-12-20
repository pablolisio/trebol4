package com.proit.servicios.compras;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.util.string.Strings;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.proit.modelo.compras.CobroAlternativo;
import com.proit.modelo.compras.Proveedor;
import com.proit.servicios.GenericService;
import com.proit.wicket.FacturarOnLineAuthenticatedWebApplication;

public class ProveedorService extends GenericService<Proveedor> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public ProveedorService() {
		super(Proveedor.class);
	}
	
	private Criteria definirCriteria(String razonSocial, String cuitCuil) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(Proveedor.class);
		if (!Strings.isEmpty(razonSocial)) {
			criteria.add(Restrictions.ilike("razonSocial", "%" + razonSocial + "%"));
		}
		if (!Strings.isEmpty(cuitCuil)) {
			criteria.add(Restrictions.ilike("cuitCuil", "%" + cuitCuil + "%"));
		}				
		criteria.add(Restrictions.eq("borrado", false));
		return criteria;
	}
	
	/**
	 * This method gets some info of {@link Proveedor}s from database.
	 * @param primerResultado First result to obtain.
	 * @param cantidadResultados Total {@link Proveedor}s to obtain.
	 * @return Returns {@link Proveedor}s from database.
	 */
	@SuppressWarnings("unchecked")
	public Iterator<Proveedor> getProveedores(long primerResultado, long cantidadResultados, String razonSocial, String cuitCuil) {
		Criteria criteria = definirCriteria(razonSocial, cuitCuil);
		criteria.setFirstResult((int) primerResultado);
		criteria.setMaxResults((int) cantidadResultados);
		criteria.addOrder(Order.asc("razonSocial"));
		return criteria.list().iterator();
	}
	
	public long getProveedoresSize(String razonSocial, String cuitCuil) {
		Criteria criteria = definirCriteria(razonSocial, cuitCuil);
		criteria.setProjection(Projections.rowCount());
		return (Long)criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getProveedores(String razonSocial) {
		Criteria criteria = definirCriteria(razonSocial, null);
		criteria.setProjection(Projections.distinct(Projections.projectionList()
			    	    .add(Projections.property("razonSocial"), "razonSocial") ));
		criteria.addOrder(Order.asc("razonSocial"));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getCuitCuilProveedores(String cuitCuil) {
		Criteria criteria = definirCriteria(null, cuitCuil);
		criteria.setProjection(Projections.distinct(Projections.projectionList()
			    	    .add(Projections.property("cuitCuil"), "cuitCuil") ));
		criteria.addOrder(Order.asc("cuitCuil"));
		return criteria.list();
	}
	
	/**
	 * Verifica si la combinacion de razon social y Cuit/Cuil ya ha sido cargada previamente en el sistema
	 * @param razonSocial
	 * @param cuitCuil
	 * @return
	 */
	public boolean existsByRazonSocialAndCuitCuil(String razonSocial, String cuitCuil){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(Proveedor.class);
		criteria.add(Restrictions.eq("razonSocial",razonSocial).ignoreCase());
		if (cuitCuil!=null) {
			criteria.add(Restrictions.eq("cuitCuil",cuitCuil).ignoreCase());
		}
		criteria.add(Restrictions.eq("borrado", false));
		criteria.setProjection(Projections.rowCount());
		return( (Long) criteria.uniqueResult() ) > 0;
	}
	
	/**
	 * Verifica si el CUIT/CUIL ya ha sido cargado previamente en el sistema
	 * @param email
	 * @return
	 */
	public boolean existsByCuitCuil(String cuitCuil){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(Proveedor.class);
		criteria.add(Restrictions.eq("cuitCuil",cuitCuil).ignoreCase());
		criteria.add(Restrictions.eq("borrado", false));
		criteria.setProjection(Projections.rowCount());
		return( (Long) criteria.uniqueResult() ) > 0;
	}
	
	/**
	 * Verifica si la razon social ya ha sido cargada previamente en el sistema
	 * @param email
	 * @return
	 */
	public boolean existsByRazonSocial(String razonSocial){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(Proveedor.class);
		criteria.add(Restrictions.eq("razonSocial",razonSocial).ignoreCase());
		criteria.add(Restrictions.eq("borrado", false));
		criteria.setProjection(Projections.rowCount());
		return( (Long) criteria.uniqueResult() ) > 0;
	}
	
	public Proveedor getByRazonSocial(String razonSocial){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(Proveedor.class);
		criteria.add(Restrictions.eq("razonSocial",razonSocial).ignoreCase());
		criteria.add(Restrictions.eq("borrado", false));
		return (Proveedor) criteria.uniqueResult();
	}
	
	public Proveedor getByRazonSocialAndCuitCuil(String razonSocial, String cuitCuil){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(Proveedor.class);
		criteria.add(Restrictions.eq("razonSocial",razonSocial).ignoreCase());
		if (cuitCuil!=null) {
			criteria.add(Restrictions.eq("cuitCuil",cuitCuil).ignoreCase());
		}
		criteria.add(Restrictions.eq("borrado", false));
		return (Proveedor) criteria.uniqueResult();
	}
	
	public String getCuitCuilByRazonSocial(String razonSocial){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(Proveedor.class);
		criteria.add(Restrictions.eq("razonSocial",razonSocial));
		criteria.add(Restrictions.eq("borrado", false));
		criteria.setProjection(Projections.property("cuitCuil"));
		return (String) criteria.uniqueResult();
	}
	
	public String getRazonSocialByCuitCuil(String cuitCuil){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(Proveedor.class);
		criteria.add(Restrictions.eq("cuitCuil",cuitCuil));
		criteria.add(Restrictions.eq("borrado", false));
		criteria.setProjection(Projections.property("razonSocial"));
		return (String) criteria.uniqueResult();
	}

	public String getCBUByRazonSocial(String razonSocial) {
		Criteria criteria = definirCriteria(razonSocial, null);
		criteria.createAlias("cuentaBancaria", "c");
		criteria.add(Restrictions.eq("c.borrado", false));
		criteria.setProjection(Projections.property("c.cbu"));
		return (String) criteria.uniqueResult();
	}

	public String getCBUByCuitCuil(String cuitCuil) {
		Criteria criteria = definirCriteria(null, cuitCuil);
		criteria.createAlias("cuentaBancaria", "c");
		criteria.add(Restrictions.eq("c.borrado", false));
		criteria.setProjection(Projections.property("c.cbu"));
		return (String) criteria.uniqueResult();
	}
	
	public String delete(Proveedor proveedor) {
		String whereIsUsed = whereIsUsed(proveedor);
		if ( !whereIsUsed.isEmpty() ) {
			return whereIsUsed;
		}
		CobroAlternativoService cobroAlternativoService = new CobroAlternativoService();
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		session.refresh(proveedor);
		List<CobroAlternativo> cobrosAlternativos= proveedor.getListadoCobrosAlternativos();
		for (CobroAlternativo cobroAlternativo : cobrosAlternativos) {
			cobroAlternativoService.delete(cobroAlternativo);
		}
		
		//Si tiene Cuenta Bancaria, la elimino
		if (proveedor.getCuentaBancaria()!=null) {
			CuentaBancariaService cuentaBancariaService = new CuentaBancariaService();
			cuentaBancariaService.delete(proveedor.getCuentaBancaria().getId());
		}
		delete(proveedor.getId());
		return whereIsUsed;
	}

	private String whereIsUsed(Proveedor proveedor) {
//		long cantFacturasCompras = getCantidadUsados(FacturaCompra.class, "proveedor", proveedor);
//		long cantSolicitudesPago = getCantidadUsados(SolicitudPago.class, "proveedor", proveedor);
//		return cantFacturasCompras>0 || cantSolicitudesPago>0;
		String result = getUsedFacturaCompraAsStr("proveedor", proveedor);
		result += result.isEmpty() ? "" : ". ";
		result += getUsedSolicitudPagoAsStr("proveedor", proveedor);
		result += result.isEmpty() ? "" : ". ";
		return result;
	}
	
}
