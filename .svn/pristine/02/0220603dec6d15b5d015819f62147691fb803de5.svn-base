package com.proit.servicios;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.proit.wicket.FacturarOnLineAuthenticatedWebApplication;

public class EntidadSimpleService<U> extends GenericService<U> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Class<U> u;

	public EntidadSimpleService(final Class<U> u) {
		super(u);
		this.u = u;
	}
	
	@SuppressWarnings("unchecked")
	public List<U> getAll() {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(u);
		criteria.add(Restrictions.eq("borrado", false));
		return criteria.list();
	}

}