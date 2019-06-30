package cl.eje.generico.fileremote.ws.injectar;

public enum EstadosDeFactura {
 //SELECT * FROM eje_siifactura_estados_facturas_en_wf;
	
	PROCESADA(1000, "Factura ingresada al WF"), //
	PROVEEDOR_NO_EXISTE(10, "No existe proveedor"), //
	NO_TIENE_FACTURA_ANTERIOR(15, "No existe factura anterior para este proveedor"), //
	ERROR_DATO(20, "Error de datos"), //
	NO_PROCESADOS(30, "No pudo ser procesado"), //
	FACTURA_CON_FECHA_ANTERIOR_ALPLAZO(35, "La ultima factura del Proveedor fue ingresada antes de la fecha de filtrado"), //
	DUPLICADO(40, "Factura ya ingresada"), //
	NO_HABLITADA_PARA_PROCESAR(50, "Tipo de factura no habilitado para procesarla"), //
	NO_SE_PUDO_ASIGNAR_FACTURA(55, "No existe persona encargada de recibir factura para esa localidad"), //
	PROCESADOS_REGLA1(70, "Factura ingresada al WF con la regla 1"), //
	PROCESADOS_REGLA2(80, "Factura ingresada al WF con la regla 2"), //
	PROCESADOS_MANUAL(90, "Factura Asignada a Receptor Manualmente"), //
	ESPERANDO_SER_PROCESADA(100, "Esperando ser procesada"); //

	int codigo;
	String texto;

	EstadosDeFactura(int codigo, String texto) {

		this.codigo = codigo;
		this.texto = texto;

	}
}
