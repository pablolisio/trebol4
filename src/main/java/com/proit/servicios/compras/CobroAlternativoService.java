package com.proit.servicios.compras;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.proit.modelo.compras.CobroAlternativo;
import com.proit.modelo.compras.Proveedor;
import com.proit.servicios.GenericService;
import com.proit.wicket.FacturarOnLineAuthenticatedWebApplication;

public class CobroAlternativoService extends GenericService<CobroAlternativo> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public CobroAlternativoService() {
		super(CobroAlternativo.class);
	}
	
	private Criteria definirCriteria(int idProveedor) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(CobroAlternativo.class)
							.createAlias("proveedor", "p")
							.add(Restrictions.eq("p.id", idProveedor));
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
	public Iterator<CobroAlternativo> getCobrosAlternativos(long primerResultado, long cantidadResultados, int idProveedor) {
		Criteria criteria = definirCriteria(idProveedor);
		criteria.setFirstResult((int) primerResultado);
		criteria.setMaxResults((int) cantidadResultados);
		return criteria.list().iterator();
	}
	
	public long getCobrosAlternativosSize(int idProveedor) {
		Criteria criteria = definirCriteria(idProveedor);
		criteria.setProjection(Projections.rowCount());
		return (Long)criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<CobroAlternativo> getCobrosAlternativos(int idProveedor) {
		Criteria criteria = definirCriteria(idProveedor);
		return criteria.list();
	}
	
	public void delete(CobroAlternativo cobroAlternativo) {
		CuentaBancariaService cuentaBancariaService = new CuentaBancariaService();
		cuentaBancariaService.delete(cobroAlternativo.getCuentaBancaria().getId());
		delete(cobroAlternativo.getId());		
	}
}
