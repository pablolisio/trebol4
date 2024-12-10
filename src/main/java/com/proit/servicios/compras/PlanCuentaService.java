package com.proit.servicios.compras;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.proit.modelo.compras.PlanCuenta;
import com.proit.servicios.GenericService;
import com.proit.wicket.FacturarOnLineAuthenticatedWebApplication;

public class PlanCuentaService extends GenericService<PlanCuenta> implements Serializable {

	private static final long serialVersionUID = 1L;

	public PlanCuentaService() {
		super(PlanCuenta.class);
	}
	
	private Criteria definirCriteria() {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(PlanCuenta.class);
		criteria.add(Restrictions.eq("borrado", false));
		return criteria;
	}
	
	@SuppressWarnings("unchecked")
	public List<PlanCuenta> getPlanesCuenta() {
		Criteria criteria = definirCriteria();
		criteria.addOrder(Order.asc("nombre"));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public Iterator<PlanCuenta> getPlanesCuenta(long primerResultado, long cantidadResultados) {
		Criteria criteria = definirCriteria();
		criteria.setFirstResult((int) primerResultado);
		criteria.setMaxResults((int) cantidadResultados);
		criteria.addOrder(Order.asc("nombre"));
		return criteria.list().iterator();
	}
	
	public long getPlanesCuentaSize() {
		Criteria criteria = definirCriteria();
		criteria.setProjection(Projections.rowCount());		
		return (Long)criteria.uniqueResult();
	}

	public boolean existsByName(String nombrePlanCuenta, int idPlanCuentaActual) {
		Criteria criteria = definirCriteria();
		criteria.add(Restrictions.eq("nombre", nombrePlanCuenta).ignoreCase());
		if (idPlanCuentaActual!=0) {
			criteria.add(Restrictions.ne("id", idPlanCuentaActual));
		}
		criteria.setProjection(Projections.rowCount());
		return( (Long) criteria.uniqueResult() ) > 0;
	}
	
	public String delete(PlanCuenta planCuenta) {
		String whereIsUsed = whereIsUsed(planCuenta);
		if ( whereIsUsed.isEmpty() ) {
			super.delete(planCuenta.getId());
		}
		return whereIsUsed;
	}

	private String whereIsUsed(PlanCuenta planCuenta) {
		String result = getUsedOrdenPagoAsStr("planCuenta", planCuenta);
		result += result.isEmpty() ? "" : ". ";
//		result += getUsedSolicitudPagoAsStr("planCuenta", planCuenta);
//		result += result.isEmpty() ? "" : ". ";
		return result;
	}
	
	public void updatePlanCuentaOP(int ordenPagoId, int nuevoPlanCuentaId) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString1 = "UPDATE orden_pago set plan_cuenta_id = " + nuevoPlanCuentaId + " where id = " + ordenPagoId;		
		Query sqlQuery1 = session.createSQLQuery(sqlString1);		
		sqlQuery1.executeUpdate();
	}
	
}