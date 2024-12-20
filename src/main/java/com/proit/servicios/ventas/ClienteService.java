package com.proit.servicios.ventas;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.util.string.Strings;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.proit.modelo.ventas.Cliente;
import com.proit.servicios.GenericService;
import com.proit.wicket.FacturarOnLineAuthenticatedWebApplication;

public class ClienteService extends GenericService<Cliente> implements Serializable {

	private static final long serialVersionUID = 1L;

	public ClienteService() {
		super(Cliente.class);
	}
	
	private Criteria definirCriteria(String razonSocial, String cuitCuil) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(Cliente.class);
		if (!Strings.isEmpty(razonSocial)) {
			criteria.add(Restrictions.ilike("razonSocial", "%" + razonSocial + "%"));
		}
		if (!Strings.isEmpty(cuitCuil)) {
			criteria.add(Restrictions.ilike("cuitCuil", "%" + cuitCuil + "%"));
		}				
		criteria.add(Restrictions.eq("borrado", false));
		return criteria;
	}
	
	@SuppressWarnings("unchecked")
	public Iterator<Cliente> getClientes(long primerResultado, long cantidadResultados, String razonSocial, String cuitCuil) {
		Criteria criteria = definirCriteria(razonSocial, cuitCuil);
		criteria.setFirstResult((int) primerResultado);
		criteria.setMaxResults((int) cantidadResultados);
		criteria.addOrder(Order.asc("razonSocial"));
		return criteria.list().iterator();
	}
	
	public long getClientesSize(String razonSocial, String cuitCuil) {
		Criteria criteria = definirCriteria(razonSocial, cuitCuil);
		criteria.setProjection(Projections.rowCount());
		return (Long)criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getClientes(String razonSocial) {
		Criteria criteria = definirCriteria(razonSocial, null);
		criteria.setProjection(Projections.distinct(Projections.projectionList()
			    	    .add(Projections.property("razonSocial"), "razonSocial") ));
		criteria.addOrder(Order.asc("razonSocial"));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getCuitCuilClientes(String cuitCuil) {
		Criteria criteria = definirCriteria(null, cuitCuil);
		criteria.setProjection(Projections.distinct(Projections.projectionList()
			    	    .add(Projections.property("cuitCuil"), "cuitCuil") ));
		criteria.addOrder(Order.asc("cuitCuil"));
		return criteria.list();
	}
	
	public Cliente getByRazonSocial(String razonSocial){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(Cliente.class);
		criteria.add(Restrictions.eq("razonSocial",razonSocial).ignoreCase());
		criteria.add(Restrictions.eq("borrado", false));
		return (Cliente) criteria.uniqueResult();
	}
	
	public Cliente getByRazonSocialAndCuitCuil(String razonSocial, String cuitCuil){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(Cliente.class);
		criteria.add(Restrictions.eq("razonSocial",razonSocial).ignoreCase());
		criteria.add(Restrictions.eq("cuitCuil",cuitCuil).ignoreCase());
		criteria.add(Restrictions.eq("borrado", false));
		return (Cliente) criteria.uniqueResult();
	}
	
	/**
	 * Verifica si la combinacion de razon social y Cuit/Cuil ya ha sido cargada previamente en el sistema
	 * @param razonSocial
	 * @param cuitCuil
	 * @return
	 */
	public boolean existsByRazonSocialAndCuitCuil(String razonSocial, String cuitCuil){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(Cliente.class);
		criteria.add(Restrictions.eq("razonSocial",razonSocial).ignoreCase());
		if (cuitCuil!=null) {
			criteria.add(Restrictions.eq("cuitCuil",cuitCuil).ignoreCase());
		}
		criteria.add(Restrictions.eq("borrado", false));
		criteria.setProjection(Projections.rowCount());
		return( (Long) criteria.uniqueResult() ) > 0;
	}
	
	public boolean existsByRazonSocial(String razonSocial){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(Cliente.class);
		criteria.add(Restrictions.eq("razonSocial",razonSocial).ignoreCase());
		criteria.add(Restrictions.eq("borrado", false));
		criteria.setProjection(Projections.rowCount());
		return( (Long) criteria.uniqueResult() ) > 0;
	}

	public boolean existsByCuitCuil(String cuitCuil) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(Cliente.class);
		criteria.add(Restrictions.eq("cuitCuil",cuitCuil).ignoreCase());
		criteria.add(Restrictions.eq("borrado", false));
		criteria.setProjection(Projections.rowCount());
		return( (Long) criteria.uniqueResult() ) > 0;
	}
	
	public String getCuitCuilByRazonSocial(String razonSocial){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(Cliente.class);
		criteria.add(Restrictions.eq("razonSocial",razonSocial));
		criteria.add(Restrictions.eq("borrado", false));
		criteria.setProjection(Projections.property("cuitCuil"));
		return (String) criteria.uniqueResult();
	}
	
	public String getRazonSocialByCuitCuil(String cuitCuil){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(Cliente.class);
		criteria.add(Restrictions.eq("cuitCuil",cuitCuil));
		criteria.add(Restrictions.eq("borrado", false));
		criteria.setProjection(Projections.property("razonSocial"));
		return (String) criteria.uniqueResult();
	}
	
	public String delete(Cliente cliente) {
		String whereIsUsed = whereIsUsed(cliente);
		if ( whereIsUsed.isEmpty() ) {
			super.delete(cliente.getId());
		}
		return whereIsUsed;
	}

	private String whereIsUsed(Cliente cliente) {
//		long cantSolicitudesPago = getCantidadUsados(SolicitudPago.class, "cliente", cliente);
//		long cantFacVta = getCantidadUsados(FacturaVenta.class, "cliente", cliente);		
//		long cantSolFacVta = getCantidadUsados(SolicitudFacturaVenta.class, "cliente", cliente);
//		return cantSolicitudesPago>0 || cantFacVta>0 || cantSolFacVta>0;
		String result = getUsedSolicitudPagoAsStr("cliente", cliente);
		result += result.isEmpty() ? "" : ". ";
		result += getUsedFacturaVentaAsStr("cliente", cliente);
		result += result.isEmpty() ? "" : ". ";
		result += getUsedSolicitudFacturaVentaAsStr("cliente", cliente);
		result += result.isEmpty() ? "" : ". ";
		return result;
	}
	
}