package com.proit.servicios;

import java.io.Serializable;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.proit.modelo.Config;
import com.proit.wicket.FacturarOnLineAuthenticatedWebApplication;

public class ConfigService extends GenericService<Config> implements Serializable {

	private static final long serialVersionUID = 1L;

	public ConfigService() {
		super(Config.class);
	}
	
	public Config getConfig() {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(Config.class);
		criteria.add(Restrictions.eq("borrado", false));
		return (Config) criteria.uniqueResult();
	}
	
}