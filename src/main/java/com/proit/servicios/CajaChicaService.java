package com.proit.servicios;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import org.apache.wicket.util.string.Strings;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.proit.modelo.CajaChica;
import com.proit.utils.Utils;
import com.proit.wicket.FacturarOnLineAuthenticatedWebApplication;

public class CajaChicaService extends GenericService<CajaChica> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public CajaChicaService(){
		super(CajaChica.class);
	}
	
	private Criteria definirCriteria(Calendar mes) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(CajaChica.class);		
		if (mes!=null){
			criteria.add(Restrictions.eq("mes", mes));
		}
		criteria.add(Restrictions.eq("borrado", false));
		return criteria;
	}
	
	@SuppressWarnings("unchecked")
	public List<CajaChica> getListaCajaChica(Calendar mes, Calendar anio) {
		Criteria criteria;
		if (mes!=null) {
			criteria = definirCriteria(mes);
		} else {
			criteria = definirCriteria(null);
			
			Calendar mesInicialAño = (Calendar) anio.clone();
			mesInicialAño.set(Calendar.MONTH, mesInicialAño.getMinimum(Calendar.MONTH));
			mesInicialAño.set(Calendar.DAY_OF_MONTH, 1);
			mesInicialAño = Utils.firstMillisecondOfDay(mesInicialAño);
			
			Calendar mesFinalAño = (Calendar) mesInicialAño.clone();
			mesFinalAño.set(Calendar.MONTH, mesInicialAño.getMaximum(Calendar.MONTH));
			
			criteria.add(Restrictions.ge("mes", mesInicialAño));
			criteria.add(Restrictions.le("mes", mesFinalAño));
		}
		criteria.addOrder(Order.asc("fecha"));
		return criteria.list();
	}
	
	public long getListaCajaChicaSize(Calendar mes) {
		Criteria criteria = definirCriteria(mes);
		criteria.setProjection(Projections.rowCount());		
		return (Long)criteria.uniqueResult();
	}
	
	public double calculateMontoTotal(Calendar mes) {
		Criteria criteria = definirCriteria(mes);
		criteria.setProjection(Projections.sum("monto"));
		Double montoTotal = (Double) criteria.uniqueResult();
		if (montoTotal==null){
			return 0d;
		}
		return montoTotal;
	}
	
	public double calculateMontoTotalAnual(Calendar mesInicialAño) {
		mesInicialAño.set(Calendar.MONTH, mesInicialAño.getMinimum(Calendar.MONTH));
		mesInicialAño.set(Calendar.DAY_OF_MONTH, 1);
		mesInicialAño = Utils.firstMillisecondOfDay(mesInicialAño);
		
		Calendar mesFinalAño = (Calendar) mesInicialAño.clone();
		mesFinalAño.set(Calendar.MONTH, mesInicialAño.getMaximum(Calendar.MONTH));
		
		Criteria criteria = definirCriteria(null);
		criteria.add(Restrictions.ge("mes", mesInicialAño));
		criteria.add(Restrictions.le("mes", mesFinalAño));
		
		criteria.setProjection(Projections.sum("monto"));
		Double montoTotal = (Double) criteria.uniqueResult();
		if (montoTotal==null){
			return 0d;
		}
		return montoTotal;
	}	
	
	//* *************************************************	
	//* *************VALIDACIONES************************
	//* *************************************************
	public boolean todoMontoMayorACero(List<CajaChica> listaCajaChica) {
		for (CajaChica cajaChica : listaCajaChica){
			if (cajaChica.getMonto()<=0)
				return false;
		}
		return true;
	}
	
	public boolean todaLineaTieneDetalle(List<CajaChica> listaCajaChica) {
		for (CajaChica cajaChica : listaCajaChica){
			if (Strings.isEmpty(cajaChica.getDetalle()))
				return false;
		}
		return true;
	}
	
	public boolean todaLineaTieneFecha(List<CajaChica> listaCajaChica) {
		for (CajaChica cajaChica : listaCajaChica){
			if (cajaChica.getFecha()==null)
				return false;
		}
		return true;
	}

}
