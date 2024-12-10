package com.proit.servicios;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.proit.modelo.EntidadGenerica;
import com.proit.modelo.compras.CuentaBancaria;
import com.proit.modelo.compras.FacturaCompra;
import com.proit.modelo.compras.FacturaCompraOrdenPago;
import com.proit.modelo.compras.FacturaSolicitudPago;
import com.proit.modelo.compras.OrdenPago;
import com.proit.modelo.compras.Pago;
import com.proit.modelo.compras.SolicitudPago;
import com.proit.modelo.ventas.Cobro;
import com.proit.modelo.ventas.FacturaVenta;
import com.proit.modelo.ventas.FacturaVentaCobranza;
import com.proit.modelo.ventas.SolicitudFacturaVenta;
import com.proit.wicket.FacturarOnLineAuthenticatedWebApplication;

public class GenericService<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Class<T> t;
	
	public GenericService(final Class<T> t) {
		super();
		this.t = t;
	}

	public EntidadGenerica get(int id) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		Criteria criteria = session.createCriteria(t);
		criteria.add(Restrictions.eq("id", id));
		return (EntidadGenerica)criteria.uniqueResult();
	}
	
	public Object createOrUpdate(EntidadGenerica entidad) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
	    entidad.setBorrado(false);
	    if (entidad.getId() == 0) {
	    	return  (Integer) session.save(entidad);
	    } else {
	    	return (EntidadGenerica) session.merge(entidad);
	    }
	}
	
	@SuppressWarnings("unchecked")
	public void delete(int id) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
	    T entidad = (T) get(id); 
	    ((EntidadGenerica) entidad).setBorrado(true);
	    session.update(entidad);
	}

//	/**
//	 * Utilizado para ver en cuantas entidades esta siendo utilizado
//	 * @param clase
//	 * @param nombreColumna
//	 * @param entidad
//	 * @return
//	 */
//	@SuppressWarnings("rawtypes")
//	protected long getCantidadUsados(Class clase, String nombreColumna, EntidadGenerica entidad) {
//		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
//		Criteria criteria = session.createCriteria(clase);
//		criteria.add(Restrictions.eq(nombreColumna, entidad));
//		criteria.add(Restrictions.eq("borrado", false));
//		criteria.setProjection(Projections.rowCount());
//		return (Long) criteria.uniqueResult();
//	}
	
	public void flushSession(){
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		session.flush();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<EntidadGenerica> getListaUsados(Class clase, String nombreColumna, EntidadGenerica entidad) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		Criteria criteria = session.createCriteria(clase);
		criteria.add(Restrictions.eq(nombreColumna, entidad));
		criteria.add(Restrictions.eq("borrado", false));
		return criteria.list();
	}
	
	protected String getUsedSolicitudFacturaVentaAsStr(String nombreColumna, EntidadGenerica entidad) {
		String result = "";
		for (EntidadGenerica ent : getListaUsados(SolicitudFacturaVenta.class, nombreColumna, entidad)) {
			SolicitudFacturaVenta sol = (SolicitudFacturaVenta) ent;
			result += result.isEmpty()? "Solicitudes Factura Venta: " : ", ";
			result += sol.getNro();
		}
		return result;
	}

	protected String getUsedSolicitudPagoAsStr(String nombreColumna, EntidadGenerica entidad) {
		String result = "";
		for (EntidadGenerica ent : getListaUsados(SolicitudPago.class, nombreColumna, entidad)) {
			SolicitudPago sol = (SolicitudPago) ent;
			result += result.isEmpty()? "Solicitudes Pago: " : ", ";
			result += sol.getNro();
		}
		return result;
	}

	protected String getUsedFacturaVentaAsStr(String nombreColumna, EntidadGenerica entidad) {
		String result = "";
		for (EntidadGenerica ent : getListaUsados(FacturaVenta.class, nombreColumna, entidad)) {
			FacturaVenta fv = (FacturaVenta) ent;
			result += result.isEmpty()? "Facturas Venta: " : ", ";
			result += fv.getNro();
		}
		return result;
	}
	
	protected String getUsedFacturaCompraAsStr(String nombreColumna, EntidadGenerica entidad) {
		String result = "";
		for (EntidadGenerica ent : getListaUsados(FacturaCompra.class, nombreColumna, entidad)) {
			FacturaCompra fc = (FacturaCompra) ent;
			result += result.isEmpty()? "Facturas Compra: " : ", ";
			result += fc.getNro();
		}
		return result;
	}

	protected String getUsedOrdenPagoAsStr(String nombreColumna, EntidadGenerica entidad) {
		String result = "";
		for (EntidadGenerica ent : getListaUsados(OrdenPago.class, nombreColumna, entidad)) {
			OrdenPago op = (OrdenPago) ent;
			result += result.isEmpty()? "OPs: " : ", ";
			result += op.getNro();
		}
		return result;
	}
	
	protected String getUsedCuentaBancariaAsStr(String nombreColumna, EntidadGenerica entidad) {
		String result = "";
		for (EntidadGenerica ent : getListaUsados(CuentaBancaria.class, nombreColumna, entidad)) {
			CuentaBancaria cb = (CuentaBancaria) ent;
			result += result.isEmpty()? "Cuentas Bancarias: " : ", ";
			result += "CBU " + cb.getCbu();
		}
		return result;
	}
	
	protected String getUsedPagoAsStr(String nombreColumna, EntidadGenerica entidad) {
		String result = "";
		for (EntidadGenerica ent : getListaUsados(Pago.class, nombreColumna, entidad)) {
			Pago pago = (Pago) ent;
			result += result.isEmpty()? "Pagos en OPs: " : ", ";
			result += pago.getOrdenPago().getNro();
		}
		return result;
	}
	
	protected String getUsedFacturaSolicitudPagoAsStr(String nombreColumna, EntidadGenerica entidad) {
		String result = "";
		for (EntidadGenerica ent : getListaUsados(FacturaSolicitudPago.class, nombreColumna, entidad)) {
			FacturaSolicitudPago factura = (FacturaSolicitudPago) ent;
			result += result.isEmpty()? "Solicitudes Pago: " : ", ";
			result += factura.getSolicitudPago().getNro();
		}
		return result;
	}
	
	protected String getUsedCobroAsStr(String nombreColumna, EntidadGenerica entidad) {
		String result = "";
		for (EntidadGenerica ent : getListaUsados(Cobro.class, nombreColumna, entidad)) {
			Cobro cobro = (Cobro) ent;
			result += result.isEmpty()? "Cobranzas: " : ", ";
			result += cobro.getCobranza().getListadoFacturas().get(0).getCliente().getRazonSocial(); //Agarro la primera, ya que tiene como minimo una factura
			DateFormat dateFormatFecha = new SimpleDateFormat("dd-MM-yyyy");
			result += " (" + dateFormatFecha.format(cobro.getCobranza().getFecha().getTime()) + "). ";
		}
		return result;
	}
	
	protected String getUsedFacturaCompraOrdenPagoAsStr(String nombreColumna, EntidadGenerica entidad) {		
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		Criteria criteria = session.createCriteria(FacturaCompraOrdenPago.class);
		criteria.add(Restrictions.eq(nombreColumna, entidad));
		criteria.add(Restrictions.eq("borrado", false));
		@SuppressWarnings("unchecked")
		List<FacturaCompraOrdenPago> list = criteria.list();
		String result = "";		
		for (FacturaCompraOrdenPago facturaCompraOrdenPago : list) {
			result += result.isEmpty()? "OPs: " : ", ";
			result += facturaCompraOrdenPago.getOrdenPago().getNro();
		}
		return result;
	}
	
	protected String getUsedFacturaVentaCobranzaAsStr(String nombreColumna, EntidadGenerica entidad) {
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		Criteria criteria = session.createCriteria(FacturaVentaCobranza.class);
		criteria.add(Restrictions.eq(nombreColumna, entidad));
		criteria.add(Restrictions.eq("borrado", false));
		@SuppressWarnings("unchecked")
		List<FacturaVentaCobranza> list = criteria.list();
		String result = "";
		for (FacturaVentaCobranza facturaVentaCobranza : list) {
			result += result.isEmpty()? "Cobranzas : " : ", ";
			result += facturaVentaCobranza.getCobranza().getListadoFacturas().get(0).getCliente().getRazonSocial(); //Agarro la primera, ya que tiene como minimo una factura
			DateFormat dateFormatFecha = new SimpleDateFormat("dd-MM-yyyy");
			result += " (" + dateFormatFecha.format(facturaVentaCobranza.getCobranza().getFecha().getTime()) + "). ";
		}
		return result;
	}
	
}
