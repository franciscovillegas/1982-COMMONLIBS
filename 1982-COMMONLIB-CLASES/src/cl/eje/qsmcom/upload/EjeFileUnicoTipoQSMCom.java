package cl.eje.qsmcom.upload;

import cl.ejedigital.web.fileupload.vo.EjeFileUnicoTipo;

public class EjeFileUnicoTipoQSMCom extends EjeFileUnicoTipo {
	public static EjeFileUnicoTipoQSMCom QMSCOM_TRABAJADOR = new EjeFileUnicoTipoQSMCom(110, "QMSCOM_trabajador", "");
	public static EjeFileUnicoTipoQSMCom QMSCOM_COMISIONES = new EjeFileUnicoTipoQSMCom(112, "QMSCOM_comisiones", "");
	public static EjeFileUnicoTipoQSMCom QMSCOM_PRODUCCION = new EjeFileUnicoTipoQSMCom(114, "QMSCOM_produccion", "");
	
	protected EjeFileUnicoTipoQSMCom(int id, String nombre, String desc) {
		super(id, nombre, desc);
		// TODO Auto-generated constructor stub
	}
	
}
