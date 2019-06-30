package cl.eje.qsmcom.helper;

import javax.servlet.ServletContext;

import cl.eje.qsmcom.tipo.TipoCarga;
import cl.eje.qsmcom.tool.TipoRegistro;
import cl.ejedigital.consultor.ConsultaData;

public interface IDatosManipulator {

	public boolean addRegistros(ServletContext context,int idPlantilla, int periodo, int idSubida);
	
	public boolean delRegistros(int idPlantilla, int periodo);
	
	
	
	public ConsultaData getRegistosByIdReq(int idReq,TipoRegistro t);
	
	public ConsultaData getLastRegistrosByRut(TipoCarga c,int rut,TipoRegistro t);
	
	public ConsultaData getLastRegistrosByRut_PeriodoActual(int rut,TipoRegistro t);
	
	public ConsultaData getLastRegistrosByRut_Periodo(int periodo, int rut,TipoRegistro t);
	
	public ConsultaData getLastRegistrosByRut_Periodo(int periodo, int rut, TipoRegistro t, int idSubida);
	
	
}
