package com.proit.utils;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class GeneralValidator implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(GeneralValidator.class.getName());

	/**
	 * Ejemplo formato de entrada para que sea valido: 23-12345678-2
	 * @param cuitCuil
	 * @return
	 */
	public boolean cuitCuilEsValido(String cuitCuil) {
		try {
			if (cuitCuil.length()!=13) {
				return false;
			}
			String[] cuitCuilSplitted = cuitCuil.split("-");
			//Intento pasarlo a int y luego me fijo la cant de digitos.
			if ( Integer.toString((Integer.parseInt(cuitCuilSplitted[0]))).length() != 2 ||
				 Integer.toString((Integer.parseInt(cuitCuilSplitted[1]))).length() != 8 ||
				 Integer.toString((Integer.parseInt(cuitCuilSplitted[2]))).length() != 1 ) {
				
				//Para el caso de CUIT: 20-07371136-4
				if (Integer.toString((Integer.parseInt(cuitCuilSplitted[1]))).length() != 8 && 
						cuitCuilSplitted[1].startsWith("0")) {
					return true;
				}
				
				return false;
			}
		} catch(Exception e){
			log.error(e.getMessage(), e);
			return false;
		}
		//return cuitCuilpasaValidacionExtra(cuitCuil);
		return true;
	}

	/*private boolean cuitCuilpasaValidacionExtra(String cuitCuil) { //Codigo robado de internet
		cuitCuil = cuitCuil.substring(0, 2) + cuitCuil.substring(3, 11) + cuitCuil.substring(12);

		//la secuencia de valores de factor es 5, 4, 3, 2, 7, 6, 5, 4, 3, 2
		int factor = 5;

		int[] c= new int[11];
		int resultado = 0;

		for (int i = 0; i <10; i++) {
			//se toma el valor de cada cifra
			c[i] = Integer.valueOf(Character.toString(cuitCuil.charAt(i))).intValue();
			//se suma al resultado el producto de la cifra por el factor que corresponde
			resultado = resultado + c[i]* factor;
			//se actualiza el valor del factor
			factor = (factor == 2)?7:factor - 1;
		}
		c[10] = Integer.valueOf(Character.toString(cuitCuil.charAt(10))).intValue();
		//se obtiene el valor calculado a comparar
		int control = (11 - (resultado % 11)) % 11;

		//Si la cifra de control es igual al valor calculado
		if (control == c[10]) {
			return true;
		}
		return false;
	}*/
	
	public boolean cbuEsValido(String cbu){
		String regex = "[0-9]+";
		try {
			if ( cbu.length() != 22) { 
				return false;
			}
			if ( ! cbu.matches(regex) ){
				return false;
			}
			
		} catch(Exception e){
			log.error(e.getMessage(), e);
			return false;
		}
		return true;
	}
	
	/**
	 * Ejemplo formato de Nro Factura: 00002-00000135  (5digs - 8digs)
	 * @param nro
	 * @return
	 */
	public boolean nroFacturaEsValido(String nro) {
		if (nro.length()!= 14) {
			return false;
		}
		String[] nroSplitted = nro.split("-");

		if (nroSplitted.length!=2) {
			return false;
		}
		
		if (nroSplitted[0].length()!=5 || nroSplitted[1].length()!=8) {
			return false;
		}		
		
		return true;
	}
}
