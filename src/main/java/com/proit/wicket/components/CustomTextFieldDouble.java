package com.proit.wicket.components;

import java.text.NumberFormat;
import java.util.Locale;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.DoubleConverter;

public class CustomTextFieldDouble extends TextField<Double>{
	private static final long serialVersionUID = 1L;

	public CustomTextFieldDouble(String id, IModel<Double> model) {
		super(id, model);
	}

	/**
	 * En caso de usar punto en vez de coma, lo reemplazo por coma. Ademas siempre lo retorna con dos digitos (incluso a los numeros enteros)
	 */	
	@SuppressWarnings("unchecked")
	@Override
	public <C> IConverter<C> getConverter(Class<C> type) {
		DoubleConverter converter = new DoubleConverter(){
			private static final long serialVersionUID = 1L;

			@Override
			 public Double convertToObject(final String value, final Locale locale) {
				String newValue = value.toString();
				if (intentoCargarDecimalesConPunto(value)) {
					String[] valueSplitted = value.split("\\.");
					String last2Digits = value.substring(value.length()-2);
					newValue = valueSplitted[0] + "," + last2Digits;
				}
//				String newValue = value.replace(",", ".");//Usado antes de cambiar el Locale en FacturarOnLineAuthenticatedWebSession
				return super.convertToObject(newValue, locale);
			}
			
			@Override
			protected NumberFormat newNumberFormat(final Locale locale) {
				NumberFormat format = NumberFormat.getInstance(locale);
				format.setMinimumFractionDigits(2);
				return format;
			}
		};
//        NumberFormat format = converter.getNumberFormat(getLocale());
//        format.setMinimumFractionDigits(2);
//        converter.setNumberFormat(getLocale(), format);
        return (IConverter<C>) converter; 
	}
	
	private boolean intentoCargarDecimalesConPunto(String value) {
		if (value.length()<3) { //no entran decimales en esa cantidad de digitos.
			return false;
		}
		String[] valueSplitted = value.split("\\.");
		String last2Digits = value.substring(value.length()-2);
		if ( valueSplitted.length>1 
				&& valueSplitted[1].length() == 2 
				&& valueSplitted[1].equals(last2Digits)) { //Ejemplo: 12.37
			return true;
		}
		return false;
	}
	
}
