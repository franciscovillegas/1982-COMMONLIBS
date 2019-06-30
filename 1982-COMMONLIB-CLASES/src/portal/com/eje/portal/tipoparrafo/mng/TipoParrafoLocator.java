package portal.com.eje.portal.tipoparrafo.mng;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.tipoparrafo.vo.voTPParametro;
import portal.com.eje.portal.tipoparrafo.vo.voTPParametroValor;
import portal.com.eje.portal.tipoparrafo.vo.voTPParrafo;
import portal.com.eje.portal.tipoparrafo.vo.voTipoParrafo;

public class TipoParrafoLocator implements ITipoParrafoMng{

	@Override
	public List<voTipoParrafo> getTipoParrafo(String strUso) {
		return getTipoParrafo(strUso, null);
	}

	@Override
	public List<voTipoParrafo> getTipoParrafo(String strUso, String strFiltro) {

		List<voTipoParrafo> lista = new ArrayList<voTipoParrafo>();

		StringBuilder strSQL = new StringBuilder();
		
		strSQL.append("select tp.id_tipoparrafo, tp.tipoparrafo, tp.vigente \n"); 
		strSQL.append("from eje_generico_tipoparrafo tp \n"); 
		strSQL.append("where uso=? \n");
    	if (strFiltro.replaceAll(" ", "")!=""){
    		strSQL.append("and tp.tipoparrafo like '%"+strFiltro.replaceAll(" ", "%")+"%' ");
    	}		
		strSQL.append("order by tp.tipoparrafo \n"); 

		Object[] params = {strUso};
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", strSQL.toString(), params);
			while (data!=null && data.next()) {
				int intId = Validar.getInstance().validarInt(data.getForcedString("id_tipoparrafo"));
				voTipoParrafo tp = new voTipoParrafo(intId, data.getForcedString("tipoparrafo"), data.getInt("vigente")==1);
				tp.setUso(strUso);
				lista.add(tp);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return lista;
		
	}

	@Override
	public boolean updTipoParrafo(voTipoParrafo tipoparrafo) {

		StringBuilder strSQL = new StringBuilder();
		
		strSQL.append("declare @id_tipoparrafo int, @tipoparrafo varchar(max), @uso varchar(max), @vigente smallint \n\n");
		
		strSQL.append("set @id_tipoparrafo=? \n");
		strSQL.append("set @tipoparrafo=? \n");
		strSQL.append("set @uso=? \n");
		strSQL.append("set @vigente=? \n");
		
		strSQL.append("if exists (select * from eje_generico_tipoparrafo where id_tipoparrafo=@id_tipoparrafo) begin \n");
		strSQL.append("	update eje_generico_tipoparrafo set tipoparrafo=@tipoparrafo, vigente=@vigente where id_tipoparrafo=@id_tipoparrafo \n");
		strSQL.append("end \n");
		strSQL.append("else begin \n");
		strSQL.append("	insert into eje_generico_tipoparrafo (tipoparrafo, uso, vigente) values (@tipoparrafo, @uso, @vigente) \n");
		strSQL.append("	set @id_tipoparrafo=@@identity \n");
		strSQL.append("end \n\n");

		strSQL.append("select id_tipoparrafo=@id_tipoparrafo ");
		
		Object[] params = {tipoparrafo.getId(), tipoparrafo.getNombre(), tipoparrafo.getUso(), tipoparrafo.getVigenteInt()};
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", strSQL.toString(), params);
			while (data!=null && data.next()) {
				tipoparrafo.setId(Validar.getInstance().validarInt(data.getForcedString("id_tipoparrafo")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	@Override
	public boolean delTipoParrafo(voTipoParrafo tipoparrafo) {
		StringBuilder strSQL = new StringBuilder();
		
		strSQL.append("declare @id_tipoparrafo int \n\n");
		
		strSQL.append("set @id_tipoparrafo=? \n\n");

		strSQL.append("delete eje_generico_tipoparrafo_parrafo where id_tipoparrafo=@id_tipoparrafo \n");
		strSQL.append("delete eje_generico_tipoparrafo where id_tipoparrafo=@id_tipoparrafo \n");
		
		Object[] params = {tipoparrafo.getId()};
		
		try {
			ConsultaTool.getInstance().execute("portal", strSQL.toString(), params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	@Override
	public List<voTPParrafo> getParrafos(int intIdTipoParrafo) {
		
		List<voTPParrafo> lista = new ArrayList<voTPParrafo>();

		StringBuilder strSQL = new StringBuilder();
		
		strSQL.append("select p.id_parrafo, p.contenido, p.vigente \n"); 
		strSQL.append("from eje_generico_tipoparrafo_parrafo p \n"); 
		strSQL.append("where p.id_tipoparrafo=? \n");
		strSQL.append("order by p.id_parrafo \n"); 

		Object[] params = {intIdTipoParrafo};
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", strSQL.toString(), params);
			while (data!=null && data.next()) {
				voTPParrafo tp = new voTPParrafo(intIdTipoParrafo, data.getInt("id_parrafo"), data.getForcedString("contenido"), data.getInt("vigente")==1);
				lista.add(tp);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return lista;
		
	}
	
	@Override
	public boolean updParrafo(voTPParrafo parrafo) {
		
		StringBuilder strSQL = new StringBuilder();
		
		boolean ok = true;
		
		try {
			int intId = parrafo.getId();
			if (intId==0) {
				strSQL.append("insert into eje_generico_tipoparrafo_parrafo (id_tipoparrafo, contenido, vigente) ");
				strSQL.append("values (?,?,?)");
				Object[] params = {parrafo.getIdTipoParrafo(), parrafo.getContenido(), parrafo.getVigente()};
				intId = (int)ConsultaTool.getInstance().insertIdentity("portal", strSQL.toString(), params);
				parrafo.setId(intId);
			}else {
				strSQL.append("update eje_generico_tipoparrafo_parrafo set contenido=?, vigente=? \n");
				strSQL.append("where id_parrafo=? \n");
				Object[] params = {parrafo.getContenido(), parrafo.getVigente(), intId};
				ConsultaTool.getInstance().update("portal", strSQL.toString(), params);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			ok = false;
			e.printStackTrace();
		}

		return ok;

	}

	@Override
	public boolean delParrafo(voTPParrafo parrafo) {
		
		StringBuilder strSQL = new StringBuilder();
		
		strSQL.append("delete eje_generico_tipoparrafo_parrafo where id_parrafo=? \n");
		
		Object[] params = {parrafo.getId()};
		
		try {
			ConsultaTool.getInstance().execute("portal", strSQL.toString(), params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
		
	}
	
	@Override
	public List<voTPParametro> getParametros(String strUso) {
		return getParametros(strUso, false);
	}

	@Override
	public List<voTPParametro> getParametros(String strUso, boolean bolConValores) {
		
		List<voTPParametro> lista = new ArrayList<voTPParametro>();

		StringBuilder strSQL = new StringBuilder();
		
		strSQL.append("select id_tpparametro, tpparametro, vigente \n"); 
		strSQL.append("from eje_generico_tipoparrafo_parametro \n"); 
		strSQL.append("where uso=? \n");
		strSQL.append("order by tpparametro \n"); 

		Object[] params = {strUso};
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", strSQL.toString(), params);
			while (data!=null && data.next()) {
				int intIdParametro = data.getInt("id_tpparametro");
				voTPParametro p = new voTPParametro(data.getInt("id_tpparametro"), data.getForcedString("tpparametro"), strUso, data.getInt("vigente")==1);
				if (bolConValores) {
					p.setValores(getParametroValores(intIdParametro));
				}
				lista.add(p);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return lista;
		
	}

	@Override
	public boolean updParametros(Connection conn, List<voTPParametro> parametros) {
		boolean ok = true;
		for (voTPParametro parametro: parametros) {
			ok &= updParametros(conn, parametro);
		}
		return ok;
	}
	
	@Override
	public boolean updParametros(Connection conn, voTPParametro parametro) {
		
		StringBuilder strSQL = new StringBuilder();
		
		boolean ok = true;
		
		try {
			int intId = parametro.getId();
			if (intId==0) {
				strSQL.append("insert into eje_generico_tipoparrafo_parametro (uso, tpparametro, vigente) ");
				strSQL.append("values (?,?,?)");
				Object[] params = {parametro.getUso(), parametro.getNombre(), parametro.getVigente()};
				intId = (int)ConsultaTool.getInstance().insertIdentity(conn, strSQL.toString(), params);
				parametro.setId(intId);
			}else {
				strSQL.append("update eje_generico_tipoparrafo_parametro set tpparametro=?, vigente=? \n");
				strSQL.append("where id_tpparametro=? \n");
				Object[] params = {parametro.getNombre(), parametro.getVigente(), intId};
				ConsultaTool.getInstance().update(conn, strSQL.toString(), params);
			}
			ok &=updValores(conn, intId, parametro.getValores());			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ok;
	}

	@Override
	public List<voTPParametroValor> getParametroValores(int intIdParametro) {

		List<voTPParametroValor> lista = new ArrayList<voTPParametroValor>();

		StringBuilder strSQL = new StringBuilder();

		strSQL.append("select id_tpparametrovalor, tpparametrovalor \n"); 
		strSQL.append("from eje_generico_tipoparrafo_parametro_valor \n"); 
		strSQL.append("where id_tpparametro=? \n");
		strSQL.append("order by tpparametrovalor \n"); 
		
		Object[] params = {intIdParametro};
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", strSQL.toString(), params);
			while (data!=null && data.next()) {
				voTPParametroValor tp = new voTPParametroValor(data.getInt("id_tpparametrovalor"), data.getForcedString("tpparametrovalor"));
				lista.add(tp);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return lista;
		
	}
	
	@Override
	public boolean updValores(Connection conn, int intIdParemtro, List<voTPParametroValor> valores) {
		boolean ok = true;
		StringBuilder strValores = new StringBuilder();
		strValores.append("-1");
		for (voTPParametroValor valor: valores) {
			ok &= updValores(conn, intIdParemtro, valor);
			strValores.append(",").append(valor.getId());
		}
		if (ok) {
			ok &= delValores(conn, intIdParemtro, strValores.toString());
		}
		return ok;
	}

	@Override
	public boolean updValores(Connection conn, int intIdParemtro, voTPParametroValor valor) {
		
		StringBuilder strSQL = new StringBuilder();
		
		boolean ok = false;
		
		try {
			int intId = 0;
			if (valor.getId()==0) {
				strSQL.append("insert into eje_generico_tipoparrafo_parametro_valor (id_tpparametro, tpparametrovalor) ");
				strSQL.append("values (?,?)");
				Object[] params = {intIdParemtro, valor.getNombre()};
				intId = (int)ConsultaTool.getInstance().insertIdentity(conn, strSQL.toString(), params);
				valor.setId(intId);
			}else {
				strSQL.append("update eje_generico_tipoparrafo_parametro_valor set tpparametrovalor=? \n");
				strSQL.append("where id_tpparametrovalor=? \n");
				Object[] params = {valor.getNombre(), valor.getId()};
				ConsultaTool.getInstance().update(conn, strSQL.toString(), params);
			}
			ok=true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ok;
	}

	@Override
	public boolean delValores(Connection conn, int intIdParemtro, String strValores) {
		
		StringBuilder strSQL = new StringBuilder();
		
		boolean ok = false;
		
		try {
			strSQL.append("delete eje_generico_tipoparrafo_parametro_valor \n");
			strSQL.append("where id_tpparametro=? and id_tpparametrovalor not in (").append(strValores).append(") \n");
			Object[] params = {intIdParemtro};
			ConsultaTool.getInstance().update(conn, strSQL.toString(), params);
			ok=true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ok;
	}
	
}
