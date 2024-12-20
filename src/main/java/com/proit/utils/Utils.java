package com.proit.utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.util.string.Strings;

import com.proit.modelo.TipoFactura;
import com.proit.modelo.compras.CuentaBancaria;
import com.proit.modelo.ventas.Cliente;

public class Utils implements Serializable{
	private static final long serialVersionUID = 1L;

	public Utils() {
	}
	
	public static Calendar firstMillisecondOfDay(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getMinimum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getMinimum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getMinimum(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND, calendar.getMinimum(Calendar.MILLISECOND));
		return calendar;
	}
	
	public static Calendar lastMillisecondOfDay(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getMaximum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getMaximum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getMaximum(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND, calendar.getMaximum(Calendar.MILLISECOND));
		return calendar;
	}
	
	public static String getNombreDia(int diaSemana){
		switch(diaSemana){
		case Constantes.LUNES: 	
			return "Lunes";
		case Constantes.MARTES: 	
			return "Martes";
		case Constantes.MIERCOLES: 	
			return "Miércoles";
		case Constantes.JUEVES: 	
			return "Jueves";
		case Constantes.VIERNES: 	
			return "Viernes";
		case Constantes.SABADO: 	
			return "Sábado";	
		}
		return "Domingo";
	}
	
	public static String getMonthString(int monthNumber) {
		switch (monthNumber) {
		case 1:
			return "Ene";
		case 2:
			return "Feb";
		case 3:
			return "Mar";
		case 4:
			return "Abr";
		case 5:
			return "May";
		case 6:
			return "Jun";
		case 7:
			return "Jul";
		case 8:
			return "Ago";
		case 9:
			return "Sep";
		case 10:
			return "Oct";
		case 11:
			return "Nov";
		case 12:
			return "Dic";
		}
		return null;
	}
	
	public static Calendar getMesActualFromListaMeses(List<Calendar> listaMeses) {
		Calendar today = Calendar.getInstance();
		for (Calendar mes : listaMeses) {
			if (mes.get(Calendar.MONTH) == today.get(Calendar.MONTH)) {
				return mes;
			}
		}
		return null;
	}
	
	public static List<Calendar> getListaMesesAnioCompleto() {
		Calendar mesDesde = Calendar.getInstance();
		Calendar mesHasta = Calendar.getInstance();
		mesDesde.set(Calendar.MONTH, mesDesde.getMinimum(Calendar.MONTH));
		mesHasta.set(Calendar.MONTH, mesHasta.getMaximum(Calendar.MONTH));
		return getListaMeses(mesDesde,mesHasta);
	}
	
	public static List<Calendar> getListaMeses(int cantidadMesesAnteriores, int cantidadMesesPosteriores) {
		Calendar mesDesde = Calendar.getInstance();
		Calendar mesHasta = Calendar.getInstance();
		mesDesde.add(Calendar.MONTH, cantidadMesesAnteriores);
		mesHasta.add(Calendar.MONTH, cantidadMesesPosteriores);
		return getListaMeses(mesDesde,mesHasta);
	}
	
	private static List<Calendar> getListaMeses(Calendar mesDesde, Calendar mesHasta) {
		List<Calendar> listaMeses = new ArrayList<Calendar>();
		Calendar mes = (Calendar)mesDesde.clone();
		mes.set(Calendar.DAY_OF_MONTH, 1);
		mes = firstMillisecondOfDay((Calendar)mes.clone());

		while(mes.compareTo(mesHasta) <= 0) {
			listaMeses.add(mes);
			mes = firstMillisecondOfDay((Calendar)mes.clone());
			mes.add(Calendar.MONTH, 1);
		}
		return listaMeses;
	}
	
	public static List<Calendar> getListaAños(int cantidadAñosAnteriores, int cantidadAñosPosteriores) {
		Calendar añoDesde = Calendar.getInstance();
		Calendar añoHasta = Calendar.getInstance();
		añoDesde.add(Calendar.YEAR, cantidadAñosAnteriores);
		añoHasta.add(Calendar.YEAR, cantidadAñosPosteriores);
		return getListaAños(añoDesde,añoHasta);
	}
	
	private static List<Calendar> getListaAños(Calendar añoDesde, Calendar añoHasta) {
		List<Calendar> listaAños = new ArrayList<Calendar>();
		Calendar año = (Calendar)añoDesde.clone();
		año.set(Calendar.MONTH, 1);
		año.set(Calendar.DAY_OF_MONTH, 1);
		año = firstMillisecondOfDay((Calendar)año.clone());

		while(año.get(Calendar.YEAR) <= añoHasta.get(Calendar.YEAR)) {
			listaAños.add(año);
			año = firstMillisecondOfDay((Calendar)año.clone());
			año.add(Calendar.YEAR, 1);
		}
		return listaAños;
	}

	public static int getDiaSemana(Calendar fecha) {
		return fecha.get(Calendar.DAY_OF_WEEK)-1;
	}
	
	public static List<Integer> getListadoEnteros(int desde, int hasta, int salto){
		List<Integer> listaHorasPosibles = new ArrayList<Integer>();
		for (int i = desde; i<= hasta; i=i+salto) {
			listaHorasPosibles.add(i);
		}		
		return listaHorasPosibles;
	}
	
	/**
	 * Retorna un listado de Calendar. Por ejemplo, si horaDesde = 0, horaHasta= 24, minsIntervalo= 30,
	 * el listado a retornar tendra : 0.0, 0.30, 1.0, 1.3, ...... , 23.0, 23.30, 23.59.
	 * El ultimo calendar tendra un minuto menor de diferencia con respecto a horaHasta. 24 -> 23.59
	 * @param horaDesde	Es la hora de comienzo
	 * @param horaHasta	Es la hora de fin
	 * @param minsIntervalo	Es la cantidad de minutos que va a usar como intervalo.
	 * @return
	 */
	public static List<Calendar> getListaHorarios(int horaDesde, int horaHasta, int minsIntervalo) {
		List<Calendar> listaHorarios = new ArrayList<Calendar>();
		Calendar calendar = firstMillisecondOfDay(Calendar.getInstance());
		calendar.set(Calendar.HOUR_OF_DAY,horaDesde);
		Calendar hasta = lastMillisecondOfDay(Calendar.getInstance());
		hasta.set(Calendar.HOUR_OF_DAY,horaHasta-1);
		
		while(calendar.compareTo(hasta)<0) {
			listaHorarios.add(calendar);
			calendar = (Calendar)calendar.clone();
			calendar.add(Calendar.MINUTE, minsIntervalo);
		}
		listaHorarios.add(hasta);
		
		return listaHorarios;
	}
	
	/**
	 * Redondea a la cantidad de digitos/places pasados por parametro
	 * @param value
	 * @param places
	 * @return
	 */
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	/**
	 * Retorna un double como string agregandole siempre dos decimales (incluso si es un numero entero).
	 * Siempre utiliza dos digitos
	 * @param value
	 * @return
	 */
	public static String round2Decimals(double value, Locale locale) {
		NumberFormat numberFormatter;
		String amountOut;

		numberFormatter = NumberFormat.getNumberInstance(locale); //Usado para que pueda andar en el server Cloud tb
		//numberFormatter = NumberFormat.getNumberInstance();
		numberFormatter.setMinimumFractionDigits(2);
		amountOut = numberFormatter.format(round(value,2));
		return amountOut;
	}
	
	/**
	 * Genera texto de manera uniforme para mostrar en varias paginas los datos bancarios de la misma manera
	 * @param cuitCuil
	 * @param cuentaBancaria
	 * @return
	 */
	public static String generarDatosBancarios(String cuitCuil, CuentaBancaria cuentaBancaria) {
		if (cuentaBancaria==null) {
			return null;
		}
		String datosBancarios = ( cuitCuil!=null ? "CUIT: " + cuitCuil + ". " : "" );
		datosBancarios += (cuentaBancaria.getTipoCuenta()!=null ? cuentaBancaria.getTipoCuenta().getNombre() : ""); 
		datosBancarios += (cuentaBancaria.getNroCuenta()!=null ? " n° " + cuentaBancaria.getNroCuenta() : "");
		datosBancarios += ( cuentaBancaria.getBanco()!=null ? " (" + cuentaBancaria.getBanco().getNombre() + ") " : ""); 
		datosBancarios += cuentaBancaria.getCbu()!=null?"CBU: "+ cuentaBancaria.getCbu()+". ":"";
		datosBancarios += cuentaBancaria.getAlias()!=null?"Alias: "+ cuentaBancaria.getAlias()+". ":"";
		return datosBancarios;
	}
	
	/**
	 * Calcula el IVA de un monto especifico, sabiendo el tipo de factura pasado por parametro
	 * @param monto
	 * @param tipoFactura
	 * @return
	 */
	public static double calcularImporteIVA(double monto, TipoFactura tipoFactura) {
		return round(monto * tipoFactura.getPorcentajeIVA() / 100 , 2);
	}
	
	/**
	 * Calcula el Subtotal a partir del Total, sabiendo el tipo de factura pasado por parametro
	 * @param total
	 * @param tipoFactura
	 * @return
	 */
	public static double getSubtotalFromTotal(double total, TipoFactura tipoFactura) {
		return round(total / (1 + tipoFactura.getPorcentajeIVA() / 100), 2);
	}
	
	/**
	 * Calcula el IVA a partir del Total, sabiendo el tipo de factura pasado por parametro
	 * @param total
	 * @param tipoFactura
	 * @return
	 */
	public static double getIvaFromTotal(double total, TipoFactura tipoFactura) {
		 return round(total-getSubtotalFromTotal(total, tipoFactura), 2);
	}
	
	/**
	 * Retorna el nro de factura de venta sin el prefijo. Le quita el prefijo
	 * @param nroFacturaVentaConPrefijo
	 * @return
	 */
	public static String getNroFacturaVentaSinPrefijo(TipoFactura tipoFactura, String nroFacturaVentaConPrefijo) {
		if (tipoFactura.isTipoE() || tipoFactura.isNotaCreditoE() || tipoFactura.isNotaDebitoE()) {
			return nroFacturaVentaConPrefijo.substring(Constantes.NRO_FACTURA_VENTA_PREFIX_00004.length());
		} else if (tipoFactura.isTipoN() || tipoFactura.isNotaCreditoN()) { //Si es tipo N (formato: 00001), lo muestro entero
			return nroFacturaVentaConPrefijo;
		} 
		return nroFacturaVentaConPrefijo.substring(Constantes.NRO_FACTURA_VENTA_PREFIX_00003.length());
	}
	
	/**
	 * Retorna el nro de factura de venta con el prefijo. Le agrega el prefijo
	 * @param nroFacturaVentaConPrefijo
	 * @return
	 */
	public static String getNroFacturaVentaConPrefijo(TipoFactura tipoFactura, String nroFacturaVentaSinPrefijo) {
		if (tipoFactura.isTipoE() || tipoFactura.isNotaCreditoE() || tipoFactura.isNotaDebitoE()) {
			return Constantes.NRO_FACTURA_VENTA_PREFIX_00004 + nroFacturaVentaSinPrefijo;
		} else if (tipoFactura.isTipoN() || tipoFactura.isNotaCreditoN()) { //Si es tipo N (formato: 00001), lo muestro entero como esta
			return nroFacturaVentaSinPrefijo;
		} 
		return Constantes.NRO_FACTURA_VENTA_PREFIX_00003 + nroFacturaVentaSinPrefijo;
	}
	
	/**
	 * Retorna el nombre del evento concatenado con la razon social del cliente.
	 * @return
	 */
	public static String concatEventAndClient(String nombreEvento, Cliente cliente) {
		if (cliente!=null) {
			return concatEventAndClient(nombreEvento, cliente.getRazonSocial());
		}
		return nombreEvento;
	}
	
	/**
	 * Retorna el nombre del evento concatenado con la razon social del cliente.
	 * @return
	 */
	public static String concatEventAndClient(String nombreEvento, String razonSocialCliente) {
		if (!Strings.isEmpty(razonSocialCliente)) {
			return nombreEvento + " (" + razonSocialCliente + ")";
		}
		return nombreEvento;
	}

	/**
	 * Retorna el nombre del evento concatenado con la razon social del cliente.
	 * @return
	 */
	public static String concatEventAndClientForDB() {
		return "nombre || ' (' || razon_social || ')'";
	}
	
	/**
	 * Retorna el nombre del evento a partir del evento+cliente
	 * @param fullName
	 * @return
	 */
	public static String getEventFromFullName(String fullName) {
		if (fullName==null){
			return null;
		}
		if (fullName.contains("(")) {
			return fullName.substring(0, fullName.indexOf("(")-1);
		} 
		return fullName;
	}
	
	/**
	 * Retorna el cliente a partir del evento+cliente
	 * @param fullName
	 * @return
	 */
	public static String getClientFromFullName(String fullName) {
		if (fullName==null){
			return null;
		}
		if (fullName.contains("(")) {
			return fullName.substring(fullName.indexOf("(")+1, fullName.length()-1);
		}
		return null;
	}
	
	
	public static String getTipoYNroFacturaAsString(TipoFactura tipoFactura, String nroFactura) {
		if (tipoFactura!=null && !Strings.isEmpty(nroFactura)) {
			return nroFactura + " (" + tipoFactura.getNombre() + ")";
		}
		return null;
	}	
	public static String getNroFacturaFromTipoYNroFacturaString(String tipoYNroFacturaString) {
		return tipoYNroFacturaString.substring(0, tipoYNroFacturaString.indexOf(" ("));
	}	
	public static TipoFactura getTipoFacturaFromTipoYNroFacturaString(String tipoYNroFacturaString) {
		String nro = getNroFacturaFromTipoYNroFacturaString(tipoYNroFacturaString);
		String nombreTipo = tipoYNroFacturaString.substring(nro.length()+2,tipoYNroFacturaString.length()-1);
		return TipoFactura.getByNombre(nombreTipo);
	}
}
