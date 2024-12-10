package com.proit.servicios;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.proit.modelo.Banco;
import com.proit.wicket.FacturarOnLineAuthenticatedWebApplication;

public class BancoService extends GenericService<Banco> implements Serializable {

	private static final long serialVersionUID = 1L;

	public BancoService() {
		super(Banco.class);
	}
	
	private Criteria definirCriteria() {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();		
		Criteria criteria = session.createCriteria(Banco.class);
		criteria.add(Restrictions.eq("borrado", false));
		return criteria;
	}
	
	@SuppressWarnings("unchecked")
	public List<Banco> getBancos() {
		Criteria criteria = definirCriteria();
		return criteria.list();
	}
	
	/**
	 * This method gets some info of {@link Banco}s from database.
	 * @param primerResultado First result to obtain.
	 * @param cantidadResultados Total {@link Banco}s to obtain.
	 * @return Returns List of {@link Banco}s from database.
	 */
	@SuppressWarnings("unchecked")
	public Iterator<Banco> getBancos(long primerResultado, long cantidadResultados) {
		Criteria criteria = definirCriteria();
		criteria.setFirstResult((int) primerResultado);
		criteria.setMaxResults((int) cantidadResultados);
		criteria.addOrder(Order.asc("nombre"));
		return criteria.list().iterator();
	}
	
	public long getBancosSize() {
		Criteria criteria = definirCriteria();
		criteria.setProjection(Projections.rowCount());		
		return (Long)criteria.uniqueResult();
	}

	public boolean existsByName(String nombreBanco, int idBancoActual) {
		Criteria criteria = definirCriteria();
		criteria.add(Restrictions.eq("nombre", nombreBanco).ignoreCase());
		if (idBancoActual!=0) {
			criteria.add(Restrictions.ne("id", idBancoActual));
		}
		criteria.setProjection(Projections.rowCount());
		return( (Long) criteria.uniqueResult() ) > 0;
	}
	
	public Banco getBancoActual(List<Banco> bancos) {
		Banco bancoDefault = null;
		for (Banco banco : bancos) {
			if (banco.isActual()) { //banco seleccionado por defecto
				bancoDefault = banco;
				break;
			}
		}
		return bancoDefault;
	}
	
	public boolean setBancoActual(Banco bancoSeleccionado) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		for (Banco banco : getBancos()) {
			if (banco.equals(bancoSeleccionado)) {
				banco.setActual(true);
			} else {
				banco.setActual(false);
			}			
		    session.update(banco);
		}
		return true;
	}
	
	public String delete(Banco banco) {
		String whereIsUsed = whereIsUsed(banco);
		if ( whereIsUsed.isEmpty() ) {
			super.delete(banco.getId());
		}
		return whereIsUsed;
	}

	private String whereIsUsed(Banco banco) {
//		long cantidadCuentasBancarias = getCantidadUsados(CuentaBancaria.class, "banco", banco);
//		long cantidadPagos = getCantidadUsados(Pago.class, "banco", banco);
//		long cantidadCobros = getCantidadUsados(Cobro.class, "bancoTransferencia", banco);
//		return cantidadCuentasBancarias>0 || cantidadPagos>0 || cantidadCobros>0;
		String result = getUsedCuentaBancariaAsStr("banco", banco);
		result += result.isEmpty() ? "" : ". ";
		result += getUsedPagoAsStr("banco", banco);
		result += result.isEmpty() ? "" : ". ";
		result += getUsedCobroAsStr("bancoTransferencia", banco);
		result += result.isEmpty() ? "" : ". ";
		return result;
	}
	
}