package com.proit.utils;

import java.io.Serializable;
import java.util.Locale;


/**
 * Esta clase provee la funcionalidad de convertir un numero representado en
 * digitos a una representacion en letras. Mejorado para leer centavos
 * 
 * @author Camilo Nova
 */
public class NumberToLetterConverter implements Serializable{
	private static final long serialVersionUID = 1L;

	private static final String[] UNIDADES = { "", "un ", "dos ", "tres ",
        "cuatro ", "cinco ", "seis ", "siete ", "ocho ", "nueve ", "diez ",
        "once ", "doce ", "trece ", "catorce ", "quince ", "dieciseis",
        "diecisiete", "dieciocho", "diecinueve", "veinte" };

    private static final String[] DECENAS = { "veinti", "treinta ", "cuarenta ",
        "cincuenta ", "sesenta ", "setenta ", "ochenta ", "noventa ",
        "cien " };

    private static final String[] CENTENAS = { "ciento ", "doscientos ",
        "trescientos ", "cuatrocientos ", "quinientos ", "seiscientos ",
        "setecientos ", "ochocientos ", "novecientos " };

    /**
     * Convierte a letras un numero de la forma $123,456.32
     * 
     * @param number
     *            Numero en representacion texto
     * @throws NumberFormatException
     *             Si valor del numero no es valido (fuera de rango o )
     * @return Numero en letras
     */
//    public static String convertNumberToLetter(String number)
//            throws NumberFormatException {
//        return convertNumberToLetter(Double.parseDouble(number));
//    }

    /**
     * Convierte un numero en representacion numerica a uno en representacion de
     * texto. El numero es valido si esta entre 0 y 999'999.999
     * 
     * @param number
     *            Numero a convertir
     * @return Numero convertido a texto
     * @throws NumberFormatException
     *             Si el numero esta fuera del rango
     */
    public static String convertNumberToLetter(double doubleNumber, Locale locale)
            throws NumberFormatException {

        StringBuilder converted = new StringBuilder();

//        String patternThreeDecimalPoints = "#.###";
//
//        DecimalFormat format = new DecimalFormat(patternThreeDecimalPoints);
//        format.setRoundingMode(RoundingMode.DOWN);
//
//         //formateamos el numero, para ajustarlo a el formato de tres puntos decimales
//        String formatedDouble = format.format(doubleNumber);
//        doubleNumber = Double.parseDouble(formatedDouble);

        doubleNumber = Utils.round(doubleNumber, 2);
        
        // Validamos que sea un numero legal
        if (doubleNumber > 999999999)
            throw new NumberFormatException(
                    "El numero es mayor de 999'999.999, "
                            + "no es posible convertirlo");

        if (doubleNumber < 0)
            throw new NumberFormatException("El numero debe ser positivo");

//        String splitNumber[] = String.valueOf(doubleNumber).replace('.', '#').split("#");
        String stringNumber = Utils.round2Decimals(doubleNumber, locale);
        stringNumber = stringNumber.replace(".", "");
        String splitNumber[] = stringNumber.split(",");

        converted.append("Pesos ");
        
        // Descompone el trio de millones
        int millon = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[0],
                8))
                + String.valueOf(getDigitAt(splitNumber[0], 7))
                + String.valueOf(getDigitAt(splitNumber[0], 6)));
        if (millon == 1)
            converted.append("un millon ");
        else if (millon > 1)
            converted.append(convertNumber(String.valueOf(millon))
                    + "millones ");

        // Descompone el trio de miles
        int miles = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[0],
                5))
                + String.valueOf(getDigitAt(splitNumber[0], 4))
                + String.valueOf(getDigitAt(splitNumber[0], 3)));
        if (miles == 1)
            converted.append("mil ");
        else if (miles > 1)
            converted.append(convertNumber(String.valueOf(miles)) + "MIL ");

        // Descompone el ultimo trio de unidades
        int cientos = Integer.parseInt(String.valueOf(getDigitAt(
                splitNumber[0], 2))
                + String.valueOf(getDigitAt(splitNumber[0], 1))
                + String.valueOf(getDigitAt(splitNumber[0], 0)));
        if (cientos == 1)
            converted.append("un");

        if (millon + miles + cientos == 0)
            converted.append("cero");
        if (cientos > 1)
            converted.append(convertNumber(String.valueOf(cientos)));

        
        // Descompone los centavos
        int centavos = Integer.parseInt(
        		  String.valueOf(getDigitAt(splitNumber[1], 1))
                + String.valueOf(getDigitAt(splitNumber[1], 0)));
        if (centavos == 1)
            converted.append("con un centavo");
        else if (centavos > 1)
            converted.append("con " + convertNumber(String.valueOf(centavos))
                    + "centavos");

        if ( ! converted.toString().contains("centavos")) {
        }
        return converted.toString();
    }

    /**
     * Convierte los trios de numeros que componen las unidades, las decenas y
     * las centenas del numero.
     * 
     * @param number
     *            Numero a convetir en digitos
     * @return Numero convertido en letras
     */
    private static String convertNumber(String number) {

        if (number.length() > 3)
            throw new NumberFormatException(
                    "La longitud maxima debe ser 3 digitos");

        // Caso especial con el 100
        if (number.equals("100")) {
            return "cien";
        }

        StringBuilder output = new StringBuilder();
        if (getDigitAt(number, 2) != 0)
            output.append(CENTENAS[getDigitAt(number, 2) - 1]);

        int k = Integer.parseInt(String.valueOf(getDigitAt(number, 1))
                + String.valueOf(getDigitAt(number, 0)));

        if (k <= 20)
            output.append(UNIDADES[k] + " ");
        else if (k > 30 && getDigitAt(number, 0) != 0)
            output.append(DECENAS[getDigitAt(number, 1) - 2] + "y "
                    + UNIDADES[getDigitAt(number, 0)]);
        else
            output.append(DECENAS[getDigitAt(number, 1) - 2]
                    + UNIDADES[getDigitAt(number, 0)]);

        return output.toString();
    }

    /**
     * Retorna el digito numerico en la posicion indicada de derecha a izquierda
     * 
     * @param origin
     *            Cadena en la cual se busca el digito
     * @param position
     *            Posicion de derecha a izquierda a retornar
     * @return Digito ubicado en la posicion indicada
     */
    private static int getDigitAt(String origin, int position) {
        if (origin.length() > position && position >= 0)
            return origin.charAt(origin.length() - position - 1) - 48;
        return 0;
    }

}