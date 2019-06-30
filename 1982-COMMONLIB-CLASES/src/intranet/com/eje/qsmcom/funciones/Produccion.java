package intranet.com.eje.qsmcom.funciones;

import cl.eje.qsmcom.helper.DatosManipulator;
import cl.eje.qsmcom.helper.IDatosManipulator;
import cl.eje.qsmcom.tipo.TipoCarga;
import cl.eje.qsmcom.tool.TipoRegistro;
import cl.ejedigital.consultor.ConsultaData;

public class Produccion {
	private int rut;
	
	public Produccion (int rut) {
		this.rut = rut;
	}
	
	public boolean tieneProduccion() {
		DatosManipulator datos = new DatosManipulator();
		ConsultaData dataProduccion = datos.getLastRegistrosByRut(TipoCarga.produccion, rut,TipoRegistro.normal);

		return dataProduccion != null && dataProduccion.next();
	}
}
