package com.proit.servicios.compras;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.proit.modelo.compras.FacturaSolicitudPago;
import com.proit.servicios.GenericService;
import com.proit.wicket.FacturarOnLineAuthenticatedWebApplication;

public class FacturaSolicitudPagoService extends GenericService<FacturaSolicitudPago> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public FacturaSolicitudPagoService() {
		super(FacturaSolicitudPago.class);
	}

	@SuppressWarnings("unchecked")
	public List<String> getNrosFacturaForSearchBox(String nroFacturaParcial) {
		
		Session session = FacturarOnLineAuthenticatedWebApplication.getHibernateSession();
		String sqlString = "SELECT nro_factura_compra FROM "+  
						"get_nros_factura_compra " +
						"where nro_factura_compra like '%" + nroFacturaParcial + "%'" ;
		Query sqlQuery = session.createSQLQuery(sqlString);
		return sqlQuery.list();		
		
	}
	
}
