package com.proit.servicios;

import java.io.Serializable;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.proit.modelo.PresupuestoCustom;
import com.proit.wicket.FacturarOnLineAuthenticatedWebApplication;

public class PresupuestoCustomService extends GenericService<PresupuestoCustom> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public PresupuestoCustomService(){
		super(PresupuestoCustom.class);
	}

	public boolean existsByDetalle(String detalle, int idPresupuestoCustomActual) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(PresupuestoCustom.class);
		criteria.add(Restrictions.eq("detalle",detalle).ignoreCase());
		if (idPresupuestoCustomActual!=0) {
			criteria.add(Restrictions.ne("id", idPresupuestoCustomActual));
		}
		criteria.add(Restrictions.eq("borrado", false));
		criteria.setProjection(Projections.rowCount());
		return( (Long) criteria.uniqueResult() ) > 0;
	}

	public boolean debitarDesdebitarPresupuestoCustom(int idPresupuestoCustom) {
		PresupuestoCustom presupuestoCustom = (PresupuestoCustom) get(idPresupuestoCustom);
		if (presupuestoCustom.isDebitado()) { 	//intenta desdebitar el presupuestoCustom
			presupuestoCustom.setDebitado(false);
		} else {					//intenta debitar el presupuestoCustom
			presupuestoCustom.setDebitado(true);			
		}
		createOrUpdate(presupuestoCustom);
		return true;
	}

}
