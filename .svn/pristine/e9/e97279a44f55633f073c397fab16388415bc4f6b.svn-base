package com.proit.servicios;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.proit.modelo.CargaHoras;
import com.proit.wicket.FacturarOnLineAuthenticatedWebApplication;

public class CargaHorasService extends GenericService<CargaHoras> implements Serializable {

	private static final long serialVersionUID = 1L;

	public CargaHorasService() {
		super(CargaHoras.class);
	}
	
	private Criteria definirCriteria() {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(CargaHoras.class);
		criteria.add(Restrictions.eq("borrado", false));		
		return criteria;
	}
	
	@SuppressWarnings("unchecked")
	public List<CargaHoras> getCargaHoras() {
		Criteria cargaHorasCriteria = definirCriteria();
		return cargaHorasCriteria.list();
	}
	
	/**
	 * This method gets some info of {@link CargaHoras}s from database.
	 * @param primerResultado First result to obtain.
	 * @param cantidadResultados Total {@link CargaHoras}s to obtain.
	 * @return Returns List of {@link CargaHoras}s from database.
	 */
	@SuppressWarnings("unchecked")
	public Iterator<CargaHoras> getCargaHoras(long primerResultado, long cantidadResultados) {
		Criteria criteria = definirCriteria();
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("id"), "id")
				.add(Projections.property("detalle"), "detalle")
				.add(Projections.property("fecha"), "fecha")
				.add(Projections.property("monto"), "monto"));
		
		criteria.setFirstResult((int) primerResultado);
		criteria.setMaxResults((int) cantidadResultados);
		criteria.setResultTransformer(Transformers.aliasToBean(CargaHoras.class));
		criteria.addOrder(Order.desc("fecha"));
		return criteria.list().iterator();
	}
	
	public long getCargaHorasSize() {
		Criteria criteria = definirCriteria();
		criteria.setProjection(Projections.rowCount());		
		return (Long)criteria.uniqueResult();
	}

	public double getTotalHoras() {
		Criteria criteria = definirCriteria();
		criteria.setProjection(Projections.sum("monto"));
		return (Double) criteria.uniqueResult();
	}
	
	public double getTotalHorasJulio2016() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2016, 6, 0);
		Criteria criteria = definirCriteria();
		criteria.add(Restrictions.gt("fecha", calendar));
		criteria.setProjection(Projections.sum("monto"));
		Double result = (Double) criteria.uniqueResult();
		if (result==null){
			result = 0D;
		}
		return result;
	}	
	
	public double getRestoHoras() {
		return 80-getTotalHorasJulio2016();
	}

}