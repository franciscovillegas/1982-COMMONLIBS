package intranet.com.eje.qsmcom.estructuras;

import java.util.ResourceBundle;

public class ParametrosObligatorios {
	public static ParametrosObligatorios ISDEBUG		 		 = new ParametrosObligatorios("intranet.qsmcomision.debug");
	public static ParametrosObligatorios MAIL		 		     = new ParametrosObligatorios("intranet.qsmcomision.debug.mail");
	
	public static ParametrosObligatorios IDPRO_COMISION_APROBADA = new ParametrosObligatorios("intranet.qsmcomision.producto.comision.aprobada.idproducto");
	public static ParametrosObligatorios IDEVE_COMISION_APROBADA = new ParametrosObligatorios("intranet.qsmcomision.producto.comision.aprobada.idevento");
	public static ParametrosObligatorios IDSUC_COMISION_APROBADA = new ParametrosObligatorios("intranet.qsmcomision.producto.comision.aprobada.idsuceso");
	
	public static ParametrosObligatorios IDPRO_COMISION_RECHAZADA = new ParametrosObligatorios("intranet.qsmcomision.producto.comision.objetada.idproducto");
	public static ParametrosObligatorios IDEVE_COMISION_RECHAZADA = new ParametrosObligatorios("intranet.qsmcomision.producto.comision.objetada.idevento");
	public static ParametrosObligatorios IDSUC_COMISION_RECHAZADA = new ParametrosObligatorios("intranet.qsmcomision.producto.comision.objetada.idsuceso");

	public static ParametrosObligatorios IDPRO_PRODUCCION_RECHAZADA = new ParametrosObligatorios("intranet.qsmcomision.producto.produccion.objetada.idproducto");
	public static ParametrosObligatorios IDEVE_PRODUCCION_RECHAZADA = new ParametrosObligatorios("intranet.qsmcomision.producto.produccion.objetada.idevento");
	public static ParametrosObligatorios IDSUC_PRODUCCION_RECHAZADA = new ParametrosObligatorios("intranet.qsmcomision.producto.produccion.objetada.idsuceso");
		
	public static ParametrosObligatorios IDROLINGRESADOR 		 = new ParametrosObligatorios("intranet.qsmcomision.roles.idingresador");
	public static ParametrosObligatorios IDROLCOORDINADOR 		 = new ParametrosObligatorios("intranet.qsmcomision.roles.idcoordinador");
	public static ParametrosObligatorios IDROLEJECUTOR	 		 = new ParametrosObligatorios("intranet.qsmcomision.roles.idejecutor");
	public static ParametrosObligatorios IDROLCONTROLER	 		 = new ParametrosObligatorios("intranet.qsmcomision.roles.idcontroler");
	
	public static ParametrosObligatorios IDPERFILINGRESADOR 	 = new ParametrosObligatorios("intranet.qsmcomision.perfil.idingresador");
	public static ParametrosObligatorios IDPERFILATENDEDOR 		 = new ParametrosObligatorios("intranet.qsmcomision.perfil.idatendedor");
	public static ParametrosObligatorios IDPERFILGESTOR	 		 = new ParametrosObligatorios("intranet.qsmcomision.perfil.idgestor");
	public static ParametrosObligatorios IDPERFILFINALIZADOR	 = new ParametrosObligatorios("intranet.qsmcomision.perfil.idfinalizador");
	public static ParametrosObligatorios IDPERFILEJECUTIVO 		 = new ParametrosObligatorios("intranet.qsmcomision.perfil.idejecutivo");	
	
	public static ParametrosObligatorios IDORIGENERN			 = new ParametrosObligatorios("intranet.qsmcomision.origenes.ern");
	public static ParametrosObligatorios IDORIGENEXTERNO		 = new ParametrosObligatorios("intranet.qsmcomision.origenes.externo");
	public static ParametrosObligatorios IDORIGENCOMPLEMENTOS	 = new ParametrosObligatorios("intranet.qsmcomision.origenes.complementos");
	
	private String valor;
	private static ResourceBundle proper;
	
	private ParametrosObligatorios(String valor) {
		
		if(proper == null) {

			proper = ResourceBundle.getBundle("intranet");
		}
		
		try {
			this.valor = proper.getString(valor);
		} catch(Exception e) {
			e.printStackTrace();
		}
		 
	}
	 
	public String toString() {
		return this.valor;
	}
	
}
