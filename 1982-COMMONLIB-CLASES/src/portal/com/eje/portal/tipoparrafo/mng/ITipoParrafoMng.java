package portal.com.eje.portal.tipoparrafo.mng;

import java.sql.Connection;
import java.util.List;

import portal.com.eje.portal.tipoparrafo.vo.voTPParametro;
import portal.com.eje.portal.tipoparrafo.vo.voTPParametroValor;
import portal.com.eje.portal.tipoparrafo.vo.voTPParrafo;
import portal.com.eje.portal.tipoparrafo.vo.voTipoParrafo;


public interface ITipoParrafoMng {

	public List<voTipoParrafo> getTipoParrafo(String strUso);
	public List<voTipoParrafo> getTipoParrafo(String strUso, String strFiltro);
	public boolean updTipoParrafo(voTipoParrafo tipoparrafo);
	public boolean delTipoParrafo(voTipoParrafo tipoparrafo);
	
	public List<voTPParrafo> getParrafos(int intIdTipoParrafo);
	public boolean updParrafo(voTPParrafo parrafo);
	public boolean delParrafo(voTPParrafo parrafo);
	
	public List<voTPParametro> getParametros(String strUso);
	public List<voTPParametro> getParametros(String strUso, boolean bolConValores);
	public boolean updParametros(Connection conn, List<voTPParametro> parametros);
	public boolean updParametros(Connection conn, voTPParametro parametro);
	
	public List<voTPParametroValor> getParametroValores(int intIdParametro);
	public boolean updValores(Connection conn, int intIdParemtro, List<voTPParametroValor> valores);
	public boolean updValores(Connection conn, int intIdParemtro, voTPParametroValor valor);
	public boolean delValores(Connection conn, int intIdParemtro, String strValores);
	
	
}
