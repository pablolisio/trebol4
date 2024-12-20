package com.proit.utils;

import java.io.Serializable;

/**
 * Clase definida para creación de constantes
 *
 */
public class Constantes implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public static final int DOMINGO 	= 0;
	public static final int LUNES 		= 1;
	public static final int MARTES 		= 2;
	public static final int MIERCOLES 	= 3;
	public static final int JUEVES 		= 4;
	public static final int VIERNES 	= 5;
	public static final int SABADO 		= 6;
	
	public static final int SIN_FACTURA		= 0;
	public static final int EFECTIVO		= 1;
	public static final int CHEQUE 			= 2;
	public static final int TRANSFERENCIA	= 3;
	public static final int TARJETA_CREDITO	= 4;
	
	public static final String USUARIO_ACTIVADO = "activado";
	public static final String URL_ACTIVACION = "http://www.trebol4sistema.com.ar/activarUsuario?tk=";	

	public static final String COLOR_VERDE 		= "success";
	public static final String COLOR_CELESTE 	= "info";
	public static final String COLOR_AMARILLO	= "warning";
	public static final String COLOR_ROJO 		= "danger";
	
	public static final String PREFIX_NRO_FACTURA_SF = "S/F-";
	
	public static final String EXCEL_PATH_GENERATED_DEV = "C://Dev//Sistemas//Trebol4//Excels//generados//";
	public static final String EXCEL_PATH_OP_TEMPLATE_DEV = "C://Dev//Sistemas//Trebol4//Excels//templates//OP.xlsx";
	public static final String EXCEL_PATH_FACTURAS_COMPRA_TEMPLATE_DEV = "C://Dev//Sistemas//Trebol4//Excels//templates//ListadoFacturasCompra.xls";
	public static final String EXCEL_PATH_FACTURAS_VENTA_TEMPLATE_DEV = "C://Dev//Sistemas//Trebol4//Excels//templates//ListadoFacturasVenta.xls";
	public static final String EXCEL_PATH_SUBDIARIO_COMPRAS_TEMPLATE_DEV = "C://Dev//Sistemas//Trebol4//Excels//templates//SubdiarioCompras.xls";
	public static final String EXCEL_PATH_SUBDIARIO_VENTAS_TEMPLATE_DEV = "C://Dev//Sistemas//Trebol4//Excels//templates//SubdiarioVentas.xls";
	public static final String EXCEL_PATH_EVENTO_DETALLE_FACTURAS_TEMPLATE_DEV = "C://Dev//Sistemas//Trebol4//Excels//templates//DetalleFacturasEvento.xls";
	public static final String EXCEL_PATH_EVENTO_DETALLE_PAGOS_TEMPLATE_DEV = "C://Dev//Sistemas//Trebol4//Excels//templates//DetallePagosEvento.xls";
	public static final String EXCEL_PATH_POSICION_TEMPLATE_DEV = "C://Dev//Sistemas//Trebol4//Excels//templates//Posicion.xlsx";
	public static final String EXCEL_PATH_CAJA_CHICA_TEMPLATE_DEV = "C://Dev//Sistemas//Trebol4//Excels//templates//CajaChica.xls";
	public static final String EXCEL_PATH_TOTALES_EVENTO_TEMPLATE_DEV = "C://Dev//Sistemas//Trebol4//Excels//templates//TotalesPorEvento.xls";
	public static final String EXCEL_PATH_PLAN_CUENTAS_TEMPLATE_DEV = "C://Dev//Sistemas//Trebol4//Excels//templates//PlanDeCuentas.xls";
	public static final String EXCEL_PATH_DETALLE_PLAN_CUENTAS_TEMPLATE_DEV = "C://Dev//Sistemas//Trebol4//Excels//templates//PlanDeCuentasDetalle.xls";
	public static final String EXCEL_PATH_DEUDAS_CLIENTE_TEMPLATE_DEV = "C://Dev//Sistemas//Trebol4//Excels//templates//DeudasPorCliente.xls";
	public static final String EXCEL_PATH_OPS_MENSUAL_TEMPLATE_DEV = "C://Dev//Sistemas//Trebol4//Excels//templates//OrdenesDePago.xls";
	
	
	public static final String EXCEL_PATH_GENERATED_PROD = "/trebol4/Excels/generados/";
	public static final String EXCEL_PATH_OP_TEMPLATE_PROD = "/trebol4/Excels/templates/OP.xlsx";
	public static final String EXCEL_PATH_FACTURAS_COMPRA_TEMPLATE_PROD = "/trebol4/Excels/templates/ListadoFacturasCompra.xls";
	public static final String EXCEL_PATH_FACTURAS_VENTA_TEMPLATE_PROD = "/trebol4/Excels/templates/ListadoFacturasVenta.xls";
	public static final String EXCEL_PATH_SUBDIARIO_COMPRAS_TEMPLATE_PROD = "/trebol4/Excels/templates/SubdiarioCompras.xls";	
	public static final String EXCEL_PATH_SUBDIARIO_VENTAS_TEMPLATE_PROD = "/trebol4/Excels/templates/SubdiarioVentas.xls";
	public static final String EXCEL_PATH_EVENTO_DETALLE_FACTURAS_TEMPLATE_PROD = "/trebol4/Excels/templates/DetalleFacturasEvento.xls";
	public static final String EXCEL_PATH_EVENTO_DETALLE_PAGOS_TEMPLATE_PROD = "/trebol4/Excels/templates/DetallePagosEvento.xls";
	public static final String EXCEL_PATH_POSICION_TEMPLATE_PROD = "/trebol4/Excels/templates/Posicion.xlsx";
	public static final String EXCEL_PATH_CAJA_CHICA_TEMPLATE_PROD = "/trebol4/Excels/templates/CajaChica.xls";
	public static final String EXCEL_PATH_TOTALES_EVENTO_TEMPLATE_PROD = "/trebol4/Excels/templates/TotalesPorEvento.xls";
	public static final String EXCEL_PATH_PLAN_CUENTAS_TEMPLATE_PROD = "/trebol4/Excels/templates/PlanDeCuentas.xls";
	public static final String EXCEL_PATH_DETALLE_PLAN_CUENTAS_TEMPLATE_PROD = "/trebol4/Excels/templates/PlanDeCuentasDetalle.xls";
	public static final String EXCEL_PATH_DEUDAS_CLIENTE_TEMPLATE_PROD = "/trebol4/Excels/templates/DeudasPorCliente.xls";
	public static final String EXCEL_PATH_OPS_MENSUAL_TEMPLATE_PROD = "/trebol4/Excels/templates/OrdenesDePago.xls";
	
	public static final int MAX_CANTIDAD_CHEQUES_POR_ORDEN_PERMITIDA = 8;
	public static final int MAX_CANTIDAD_TRANSFERENCIAS_POR_ORDEN_PERMITIDA = 4;
	public static final int MAX_CANTIDAD_FACTURAS_POR_ORDEN_PERMITIDA = 10;
	
	public static final String NRO_FACTURA_VENTA_PREFIX_00003 = "00003-0000";
	public static final String NRO_FACTURA_VENTA_PREFIX_00004 = "00004-0000";
	public static final String NRO_FACTURA_VENTA_PREFIX_NONE = "-";
	
}