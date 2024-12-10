package com.proit.servicios;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.proit.modelo.TipoFactura;
import com.proit.wicket.FacturarOnLineAuthenticatedWebApplication;

public class TipoFacturaService extends EntidadSimpleService<TipoFactura> implements Serializable {

	private static final long serialVersionUID = 1L;

	public TipoFacturaService() {
		super(TipoFactura.class);
	}
	
	/**
	 * Retorna todas menos las E y N.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TipoFactura> getAllForCompras() {//TODO falta - revisar - done
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(TipoFactura.TIPO_A.getId());
		ids.add(TipoFactura.TIPO_B.getId());
		ids.add(TipoFactura.TIPO_C.getId());
		ids.add(TipoFactura.NOTA_DEB_A.getId());
		ids.add(TipoFactura.NOTA_DEB_B.getId());
		ids.add(TipoFactura.NOTA_DEB_C.getId());
		ids.add(TipoFactura.NOTA_CRED_A.getId());
		ids.add(TipoFactura.NOTA_CRED_B.getId());
		ids.add(TipoFactura.NOTA_CRED_C.getId());
		Criteria criteria = session.createCriteria(TipoFactura.class);
		criteria.add(Restrictions.in("id", ids));
		criteria.add(Restrictions.eq("borrado", false));
		criteria.addOrder(Order.asc("nombre"));
		return criteria.list();
	}
	
	/**
	 * Retorna todas menos las C. Retorna A,B,E,N,FCE y NC A, NC B, NC E, NC FCE, ND A, ND B, ND E, ND FCE
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TipoFactura> getAllForVentas() {//TODO falta - revisar - done
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(TipoFactura.TIPO_A.getId());
		ids.add(TipoFactura.TIPO_B.getId());
		ids.add(TipoFactura.TIPO_E.getId());
		ids.add(TipoFactura.TIPO_FCE.getId());
		ids.add(TipoFactura.TIPO_N.getId());
		ids.add(TipoFactura.NOTA_DEB_A.getId());
		ids.add(TipoFactura.NOTA_DEB_B.getId());
		ids.add(TipoFactura.NOTA_DEB_E.getId());
		ids.add(TipoFactura.NOTA_DEB_FCE.getId());
		ids.add(TipoFactura.NOTA_CRED_A.getId());
		ids.add(TipoFactura.NOTA_CRED_B.getId());
		ids.add(TipoFactura.NOTA_CRED_E.getId());
		ids.add(TipoFactura.NOTA_CRED_FCE.getId());
		ids.add(TipoFactura.NOTA_CRED_N.getId());
		Criteria criteria = session.createCriteria(TipoFactura.class);
		criteria.add(Restrictions.in("id", ids));
		criteria.add(Restrictions.eq("borrado", false));
		criteria.addOrder(Order.asc("nombre"));
		return criteria.list();
	}
	
	/**
	 * Retorna todas las posibles para ser asociadas a una Nota de Credito N: A,B,E,FCE,N
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TipoFactura> getAllForNotaCreditoN() {//TODO falta - revisar - done
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(TipoFactura.TIPO_A.getId());
		ids.add(TipoFactura.TIPO_B.getId());
		ids.add(TipoFactura.TIPO_E.getId());
		ids.add(TipoFactura.TIPO_FCE.getId());
		ids.add(TipoFactura.TIPO_N.getId());
		Criteria criteria = session.createCriteria(TipoFactura.class);
		criteria.add(Restrictions.in("id", ids));
		criteria.add(Restrictions.eq("borrado", false));
		criteria.addOrder(Order.asc("nombre"));
		return criteria.list();
	}
	
	/**
	 * Devuelve el tipo de factura equivalente.
	 * Ejemplo: si es NC A, retorna tipo A
	 * @param tipoFactura
	 * @return
	 */
	public TipoFactura getEquivalenteParaNCoND(TipoFactura tipoFactura) {//TODO falta - revisar - done
		if (!tipoFactura.isNotaCredito() && !tipoFactura.isNotaDebito()) {
			return null;
		}
		TipoFactura tipoFacturaSaliente=null;
		if (tipoFactura.isNotaCreditoA()) {
			tipoFacturaSaliente = TipoFactura.TIPO_A;
		} else if (tipoFactura.isNotaCreditoB()) {
			tipoFacturaSaliente = TipoFactura.TIPO_B;
		} else if (tipoFactura.isNotaCreditoC()) {
			tipoFacturaSaliente = TipoFactura.TIPO_C;
		} else if (tipoFactura.isNotaCreditoE()) {
			tipoFacturaSaliente = TipoFactura.TIPO_E;
		} else if (tipoFactura.isNotaCreditoFCE()) {
			tipoFacturaSaliente = TipoFactura.TIPO_FCE;
		} else if (tipoFactura.isNotaDebitoA()) {
			tipoFacturaSaliente = TipoFactura.TIPO_A;
		} else if (tipoFactura.isNotaDebitoB()) {
			tipoFacturaSaliente = TipoFactura.TIPO_B;
		} else if (tipoFactura.isNotaDebitoC()) {
			tipoFacturaSaliente = TipoFactura.TIPO_C;
		} else if (tipoFactura.isNotaDebitoE()) {
			tipoFacturaSaliente = TipoFactura.TIPO_E;
		} else if (tipoFactura.isNotaDebitoFCE()) {
			tipoFacturaSaliente = TipoFactura.TIPO_FCE;
		}
		return tipoFacturaSaliente;
	}

}