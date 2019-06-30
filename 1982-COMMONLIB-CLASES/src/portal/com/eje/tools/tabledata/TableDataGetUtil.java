package portal.com.eje.tools.tabledata;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.ISenchaPage;
import cl.ejedigital.tool.strings.MyString;
import cl.ejedigital.tool.validar.Validar;
import portal.com.eje.frontcontroller.IIOClaseWebLight;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.vo.CtrGeneric;
import portal.com.eje.portal.vo.CtrTGeneric;
import portal.com.eje.portal.vo.VoTool;
import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.util.Where;
import portal.com.eje.portal.vo.vo.Vo;
import portal.com.eje.tools.security.Encrypter;

public class TableDataGetUtil {
	
	private final String PREFIJO_PARAMETRO_WHERE = "where_";
	private final String PREFIJO_PARAMETRO_WHEREENCRYPTED = "whereencrypted_";
	
	public static TableDataGetUtil getInstance() {
		return Util.getInstance(TableDataGetUtil.class);
	}

	public ConsultaData retSenchaPagged_incluQuery(IOClaseWeb io, Class<? extends Vo> classVo,List<String> camposParaFiltrar, String query) throws NullPointerException, SQLException {
		return retSenchaPagged_incluQuery(io, classVo, io.getSenchaPage(), camposParaFiltrar, query);
	}
	
	public ConsultaData retSenchaPagged_incluQuery(IOClaseWeb io, Connection conn, Class<? extends Vo> classVo, List<String> camposParaFiltrar, String query) throws NullPointerException, SQLException {
		return retSenchaPagged_incluQuery(io, conn, classVo, io.getSenchaPage(), camposParaFiltrar, query);
	}
	
	public ConsultaData retSenchaPagged_incluQuery(IOClaseWeb io, Class<? extends Vo> classVo, ISenchaPage page, List<String> camposParaFiltrar, String query) throws NullPointerException, SQLException {
		return retSenchaPagged_incluQuery(io, null, classVo, page, camposParaFiltrar, query);
	}
	
	public ConsultaData retSenchaPagged_incluQuery(IOClaseWeb io, Connection conn, Class<? extends Vo> classVo, ISenchaPage page, List<String> camposParaFiltrar, String query) throws NullPointerException, SQLException {
		if(camposParaFiltrar == null) {
			camposParaFiltrar = new ArrayList<String>();
		}
		
		if(query == null) {
			query = "";
		}
		
		query = "%"+query.trim()+"%";
		List<Where> wheres = WheresFinder.getIntance().findWhere(io.getMapParams(), io);
		
		int id = Validar.getInstance().validarInt(query, -1);
		
		
	 
		
		
		{
			Map<String, List<PrimaryKeyDefinition>> pks = VoTool.getInstance().getTablePksMap(classVo);
			Where where = null;
			for(String cpf : camposParaFiltrar) {
				if( id > 0 && pks.get(cpf) != null && pks.get(cpf).size() == 1 && pks.get(cpf).get(0).numerica()) {
					if(where == null) { 
						where = new Where(cpf,"=",id); }
					else {
						where.addOr(new Where(cpf,"=",id));	
					}
						
				}
				else {
					if(where == null) { 
						where = new Where("convert(varchar(100),"+cpf+")","like",query);
					}
					else {
						where.addOr(new Where("convert(varchar(100),"+cpf+")","like",query));	
					}
						
				}
				
			}
			wheres.add(where);
		}
	 	

 
		ConsultaData data = null;
		
		if(conn != null) {
			data = CtrTGeneric.getInstance().getDataFromClass(conn, classVo, wheres, page);
		}
		else {
			data = CtrGeneric.getInstance().getDataFromClass(classVo, wheres, page);
		}
		
		 
		
		 
		return data;
	}

	public ConsultaData retSenchaPagged_inclWhere(IOClaseWeb io, Class<? extends Vo> classVo) throws NullPointerException, SQLException {
		return retSenchaPagged_inclWhere(io, null , classVo, io.getSenchaPage());
	}
	
	public ConsultaData retSenchaPagged_inclWhere(IOClaseWeb io, Connection conn, Class<? extends Vo> classVo) throws NullPointerException, SQLException {
		return retSenchaPagged_inclWhere(io, conn , classVo, io.getSenchaPage());
	}

	public ConsultaData retSenchaPagged_inclWhere(IOClaseWeb io, Connection conn, Class<? extends Vo> classVo, ISenchaPage page) throws NullPointerException, SQLException {
		
		List<Where> lWheres = WheresFinder.getIntance().findWhere(io.getMapParams(), io);
		return retSenchaPagged_inclWhere(io, conn, classVo, lWheres, page);
		
	}
	
	public ConsultaData retSenchaPagged_inclWhere(IOClaseWeb io, Connection conn, Class<? extends Vo> classVo, List<Where> lWheres, ISenchaPage page) throws NullPointerException, SQLException {
		
		ConsultaData data = null;
		
		if(conn != null) {
			data = CtrTGeneric.getInstance().getDataFromClass(conn, classVo, lWheres, page);
		}
		else {
			data = CtrGeneric.getInstance().getDataFromClass(classVo, lWheres, page);	
		}

		return data;
		
	}
	

	
	/**
	 * Se debe usar un método con inversion de responsabilidad
	 * @deprecated
	 * */
	public List<Where> buildWheres(IOClaseWeb io) {
		Map<String, List<String>> params = io.getMapParams();
		Map<String, List<String>> wheres = new HashMap<String, List<String>>();
		for (String field : params.keySet()) {
			field = MyString.getInstance().getUniversalFromUTF8(field);
			
			if (field != null && field.startsWith(PREFIJO_PARAMETRO_WHERE)) {
				wheres.put(field.substring(PREFIJO_PARAMETRO_WHERE.length(), field.length()), params.get(field));
			}else if (field != null && field.startsWith(PREFIJO_PARAMETRO_WHEREENCRYPTED)) {
				wheres.put(field.substring(PREFIJO_PARAMETRO_WHEREENCRYPTED.length(), field.length()), Encrypter.getInstance().decrypt( params.get(field) ));
			}
		}
		
		List<Where> lWheres = new ArrayList<Where>();
		
		for(String cpf : wheres.keySet()) {
			for(String value : wheres.get(cpf)) {
				Where where = new Where(cpf,"=",value);	
				lWheres.add(where);
			}
		}
		
		return lWheres;
	}
	
}
