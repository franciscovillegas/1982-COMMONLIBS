package portal.com.eje.portal.udimension;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.factory.SingleFactory;
import portal.com.eje.portal.factory.SingleFactoryType;
import portal.com.eje.portal.organica.OrganicaLocator;

public class UDimensionLocator implements IUDimension {
	
	public UDimensionLocator() {
		
	}
	
	public static IUDimension getInstance() {
		return SingleFactory.getFactory(SingleFactoryType.UTIL).getInstance(UDimensionLocator.class);
	}

	@Override
	public List<VoUTipoDimension> getTipoDimension() {
		return getTipoDimension(0, null);
	}

	@Override
	public List<VoUTipoDimension> getTipoDimension(int intIdTipoDimension) {
		return getTipoDimension(intIdTipoDimension, null);
	}

	@Override
	public List<VoUTipoDimension> getTipoDimension(int intIdTipoDimension, String strFiltro) {

		StringBuilder strSQL = new StringBuilder();
		
		List<VoUTipoDimension> lstTipoDimension = new ArrayList<VoUTipoDimension>();

		strSQL.append("select dimension_tipo, descripcion \n")
		.append("from eje_ges_dimension_tipo \n");
		if (intIdTipoDimension!=0) {
			strSQL.append("where dimension_tipo=? \n");
		}else {
			strSQL.append("where dimension_tipo<>? \n");
		}
    	if (strFiltro!=null){
    		strSQL.append("and descripcion like '%").append(strFiltro.replaceAll(" ", "%")).append("%' ");
    	}
		strSQL.append("order by descripcion \n");
		
		Object[] params = {intIdTipoDimension};
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", strSQL.toString() , params);
			while(data != null && data.next()) {
				lstTipoDimension.add(new VoUTipoDimension(data.getInt("dimension_tipo"), data.getString("descripcion")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lstTipoDimension;
	}
	
	@Override
	public VoUTipoDimension getTipoDimension(VoUDimension dimension) {
		
		StringBuilder strSQL = new StringBuilder();

		VoUTipoDimension tipo = null;
		
		strSQL.append("select dimension_tipo from eje_ges_dimension where dimension_id=?");
		
		Object[] params = {dimension.getIdDimension()};
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", strSQL.toString() , params);
			while (data!=null && data.next()) {
				tipo = new VoUTipoDimension(Validar.getInstance().validarInt(data.getForcedString("dimension_tipo")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return tipo;
		
	}
	
	@Override
	public List<VoUDimension> getDimensiones() {
		return getDimensiones(0, "");
	}

	@Override
	public List<VoUDimension> getDimensiones(int intIdDimension) {
		return getDimensiones(intIdDimension, "");
	}

	@Override
	public List<VoUDimension> getDimensiones(String strUnidad) {
		return getDimensiones(0, strUnidad);
	}

	@Override
	public List<VoUDimension> getDimensiones(int intIdDimension, String strUnidad) {
		return getDimensiones(intIdDimension, strUnidad, null);
	}
	
	@Override
	public List<VoUDimension> getDimensiones(int intIdDimension, String strUnidad, String strFiltro) {
		
		StringBuilder strSQL = new StringBuilder();
		
		List<VoUDimension> retorno = new ArrayList<VoUDimension>();
		
		strSQL.append("select distinct d.dimension_id, d.dimension_desc, d.dimension_tipo, d.dimension_cardinalidad, d.camporef \n"); 
		strSQL.append("from eje_ges_dimension d \n");
		if (!"".equals(strUnidad)){
			strSQL.append("inner join eje_ges_dimension_item di on di.dimension_id=d.dimension_id \n");	
			strSQL.append("inner join eje_ges_ditem_unidad du on du.ditem_id=di.ditem_id \n");
		}
		strSQL.append("where d.dimension_id<>0 \n");	
		if (intIdDimension!=0){
			strSQL.append("and d.dimension_id=").append(intIdDimension).append(" \n");					
		}
		if (!"".equals(strUnidad)){
			strSQL.append("and du.unidad_id='").append(strUnidad).append("' \n");						
		}
		if (strFiltro!=null) {
			strSQL.append("and dimension_desc like '%").append(strFiltro.replaceAll(" ", "%")).append("%' \n");
		}
		strSQL.append("order by d.dimension_desc \n");

		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", strSQL.toString());
			while (data!=null && data.next()){
				VoUTipoDimension tipodimension = null;
				if (data.getForcedString("dimension_tipo")!=null) {
					tipodimension = new VoUTipoDimension(Validar.getInstance().validarInt(data.getForcedString("dimension_tipo")));
				}
				VoUDimension dimension = new VoUDimension(
						data.getInt("dimension_id"),
						data.getForcedString("dimension_desc"),
						tipodimension,
						data.getForcedString("dimension_cardinalidad"),
						data.getForcedString("camporef")
						);

				retorno.add(dimension);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return retorno;
		
	}

	@Override
	public boolean updDimension(VoUDimension dimension) {

		StringBuilder strSQL = new StringBuilder();
		
		boolean ok = false;
		
		
		
		strSQL.append("declare @dimension_id int \n\n");
		strSQL.append("set @dimension_id=? \n\n");

		if (dimension.getIdDimension()==0) {
			strSQL.append("insert into eje_ges_dimension (dimension_desc, dimension_tipo, dimension_cardinalidad, camporef) values (?,?,?,?) \n\n")
			.append("select dimension_id=@@identity ");
		}else {
			strSQL.append("update eje_ges_dimension set dimension_desc=?, dimension_tipo=?, dimension_cardinalidad=?, camporef=? where dimension_id=@dimension_id \n\n")
			.append("select dimension_id=@dimension_id ");
		}
		
		Object[] params = {
				dimension.getIdDimension(),
				dimension.getNombre(),
				dimension.getTipo().getIdDimension(),
				dimension.getCardinalidad(),
				dimension.getCampoRef()
		};
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", strSQL.toString(), params);
			while (data!=null && data.next()) {
				if(data.getForcedString("dimension_id") == null) {
					throw new SQLException("No fue posible crear el id");
				}
				dimension.setId(Validar.getInstance().validarInt(data.getForcedString("dimension_id")));
			}
			
			ETipoDimension etipodimension = ETipoDimension.getEstado(getTipoDimension(dimension).getIdDimension());
			if (dimension.getValores()!=null && (etipodimension==ETipoDimension.seleccionsimple || etipodimension==ETipoDimension.seleccionmultiple)) {
				ok  = updValores(dimension);
			}else {			
				ok = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ok;
	}
	
	@Override
	public boolean delDimension(VoUDimension dimension) {

		StringBuilder strSQL = new StringBuilder();

		boolean ok = true;
		
		strSQL.append("declare @dimension_id int \n\n")
		
		.append("set @dimension_id=").append(dimension.getIdDimension()).append(" \n\n")
		
		.append("delete u \n")
		.append("from eje_ges_ditem_unidad u \n")
		.append("inner join eje_ges_dimension_item i on i.ditem_id=u.ditem_id \n")
		.append("where i.dimension_id=@dimension_id \n\n")
		
		.append("delete eje_ges_dimension_item where dimension_id=@dimension_id \n\n")
		
		.append("delete eje_ges_dimension where dimension_id=@dimension_id \n\n")
		
		.append("select resultado='ok' \n");
		
		try {
			ConsultaTool.getInstance().getData("portal", strSQL.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ok=false;
		}
		
		return ok;
	}

	@Override
	public ConsultaData getValores(int intIdDimension) {
		return getValores(intIdDimension, null);
	}

	@Override
	public ConsultaData getValores(int intIdDimension, String strUnidad) {
		return  getValores(intIdDimension, strUnidad, "");
	}
	
	@Override
	public ConsultaData getValores(int intIdDimension, String strUnidad, String strFiltro) {
		
		StringBuilder strSQL = new StringBuilder();
		
		ConsultaData data = null;
		
		strSQL.append("select di.ditem_id, di.ditem_desc, di.codref \n"); 
		strSQL.append("from eje_ges_dimension_item di \n"); 
		if (strUnidad!=null){
			strSQL.append("inner join eje_ges_ditem_unidad du on du.ditem_id=di.ditem_id \n");
		}
		strSQL.append("where di.dimension_id=").append(intIdDimension).append(" \n");
		if (strUnidad!=null){
			strSQL.append("and du.unidad_id='").append(strUnidad).append("' \n");
		}
		if (strFiltro.replaceAll(" ", "")!=""){
    		strSQL.append("and di.ditem_desc like '%").append(strFiltro.replaceAll(" ", "%")).append("%' ");
    	}
		strSQL.append("order by di.ditem_desc \n"); 
		
		try {
			data = ConsultaTool.getInstance().getData("portal", strSQL.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
				
	}

	@Override
	public boolean updValores(VoUDimension dimension) {
		// TODO Auto-generated method stub
		return updValores(dimension.getIdDimension(), dimension.getValores());
	}

	@Override
	public boolean updValores(int intIdDimension, List<VoUDValor> valores) {
		
		boolean ok = true;

		StringBuilder strSQLDel = new StringBuilder();
		
		try {
		
			strSQLDel.append("declare @data table (ditem_id int) \n");
	
			for (VoUDValor valor: valores) {
				
				StringBuilder strSQL = new StringBuilder();
				
				strSQL.append("declare @ditem_id int \n\n")
				.append("set @ditem_id=? \n\n");
				
				if (valor.getDitemId()==0) {
					strSQL.append("insert into eje_ges_dimension_item (ditem_desc, dimension_id, codref) values (?,?,?) \n")
					.append("select ditem_id=@@identity");
				}else{
					strSQL.append("update eje_ges_dimension_item set ditem_desc=?, dimension_id=?, codref=? where ditem_id=@ditem_id ")
					.append("select ditem_id=@ditem_id");
				}
				
				Object[] params = {
					valor.getDitemId(),
					valor.getDitemDesc(),
					intIdDimension,
					valor.getCodRef()
				};
	
				ConsultaData data = ConsultaTool.getInstance().getData("portal", strSQL.toString(), params);
				
				if (valor.getDitemId()==0) {
					while (data!=null && data.next()) {
						if(data.getForcedString("ditem_id") == null) {
							throw new SQLException("No fue posible crear el id: eje_ges_dimension_item");
						}
						valor.setId(Validar.getInstance().validarInt(data.getForcedString("ditem_id")));
					}
				}
				strSQLDel.append("insert into @data values(").append(valor.getDitemId()).append(") \n");
				
			}
			
			strSQLDel.append("delete d \n")
			.append("from eje_ges_dimension_item d \n")
			.append("left join @data t on t.ditem_id=d.ditem_id \n")
			.append("where d.dimension_id=").append(intIdDimension).append(" and t.ditem_id is null \n\n")
			.append("select resultado='ok' \n");
			
			ConsultaTool.getInstance().update("portal", strSQLDel.toString());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ok = false;
		}

		return ok;
	}

	@Override
	public boolean updDimensionUnidad(String strIdUnidad, List<VoUDimension> dimensiones) {
		
		boolean ok = true;
		
		try {
			for(VoUDimension dimension: dimensiones) {
				
				StringBuilder strSQL = new StringBuilder();	
				
				int intIdDimension = dimension.getIdDimension();
				int intDitemID = 0;
				String strValor = null;
				
				ETipoDimension etipodimension = ETipoDimension.getEstado(getTipoDimension(dimension).getIdDimension());
				
				if (etipodimension == ETipoDimension.seleccionsimple) {

					strSQL.append("declare @dimension_id int, @unidad_id varchar(max), @ditem_id int \n\n")
					
					.append("set @dimension_id=? \n")
					.append("set @unidad_id=? \n")
					.append("set @ditem_id=? \n\n")
	
					.append("delete u \n")
					.append("from eje_ges_ditem_unidad u \n")
					.append("inner join eje_ges_dimension_item di on di.ditem_id=u.ditem_id \n")
					.append("inner join eje_ges_dimension d on d.dimension_id=di.dimension_id \n")
					.append("where d.dimension_id=@dimension_id and u.unidad_id=@unidad_id \n\n")
					
					.append("if @ditem_id<>0 begin \n")
					.append("	insert into eje_ges_ditem_unidad (unidad_id, ditem_id) values (@unidad_id, @ditem_id) \n")
					.append("end \n\n");
					
					for (VoUDValor valor: dimension.getValores()) {
						intDitemID = valor.getDitemId();
					}
					
					Object[] params = {intIdDimension, strIdUnidad, intDitemID};
					
					ConsultaTool.getInstance().execute("portal", strSQL.toString(), params);

				}else if (etipodimension == ETipoDimension.seleccionmultiple) {
					
					Object[] objParam = new Object[] {};
					ArrayList<Object> params = new ArrayList<Object>(Arrays.asList(objParam));
					
					params.add(intIdDimension);
					params.add(strIdUnidad);
					
					strSQL.append("declare @dimension_id int, @unidad_id varchar(max) \n\n")

					.append("set @dimension_id=? \n")
					.append("set @unidad_id=? \n\n")

					.append("declare @data table (dimension_id int, ditem_id int, unidad_id varchar(max)) \n");
					for (VoUDValor valor: dimension.getValores()) {
						strSQL.append("insert into @data values (@dimension_id, ?, @unidad_id) \n");
						params.add(valor.getDitemId());
					}
					strSQL.append(" \n")
					
					.append("delete du \n")
					.append("from eje_ges_ditem_unidad du \n")
					.append("inner join eje_ges_dimension_item di on di.ditem_id=du.ditem_id \n")
					.append("left join @data d on d.dimension_id=di.dimension_id and d.unidad_id=du.unidad_id and d.ditem_id=du.ditem_id \n")
					.append("where di.dimension_id=@dimension_id \n")
					.append("and du.unidad_id=@unidad_id \n")
					.append("and d.dimension_id is null \n\n")
					
					.append("insert into eje_ges_ditem_unidad (unidad_id, ditem_id) \n")
					.append("select d.unidad_id, d.ditem_id \n")
					.append("from @data d \n")
					.append("left join ( \n")
					.append("	select distinct di.dimension_id, du.unidad_id, du.ditem_id \n")
					.append("	from eje_ges_dimension_item di \n")
					.append("	inner join eje_ges_ditem_unidad du on du.ditem_id=di.ditem_id \n")
					.append(") x on x.dimension_id=d.dimension_id and x.ditem_id=d.ditem_id and x.unidad_id=d.unidad_id \n")
					.append("where x.dimension_id is null \n");

					ConsultaTool.getInstance().execute("portal", strSQL.toString(), params.toArray());
					
				}else {
					
					for (VoUDValor valor: dimension.getValores()) {
						intDitemID = valor.getDitemId();
						strValor = valor.strDitemDesc;
					}
					
					strSQL.append("declare @dimension_id int, @unidad_id varchar(max), @ditem_id int, @ditem_desc varchar(max) \n\n")
					
					.append("set @dimension_id=? \n")
					.append("set @unidad_id=? \n")
					.append("set @ditem_desc=? \n\n");
					
					if (strValor==null) {
						strSQL.append("declare @data table (ditem_id int) \n")
						.append("insert into @data \n")
						.append("select u.ditem_id \n")
						.append("from eje_ges_ditem_unidad u \n")
						.append("inner join eje_ges_dimension_item di on di.ditem_id=u.ditem_id \n")
						.append("inner join eje_ges_dimension d on d.dimension_id=di.dimension_id \n")
						.append("where d.dimension_id=@dimension_id and u.unidad_id=@unidad_id \n\n")
						
						.append("delete u \n")
						.append("from eje_ges_ditem_unidad u \n")
						.append("inner join eje_ges_dimension_item di on di.ditem_id=u.ditem_id \n")
						.append("inner join eje_ges_dimension d on d.dimension_id=di.dimension_id \n")
						.append("inner join @data x on x.ditem_id=u.ditem_id \n")
						.append("where d.dimension_id=@dimension_id \n\n")
						
						.append("delete di \n")
						.append("from eje_ges_dimension_item di \n")
						.append("inner join eje_ges_dimension d on d.dimension_id=di.dimension_id \n")
						.append("inner join @data x on x.ditem_id=di.ditem_id \n")
						.append("where d.dimension_id=@dimension_id \n\n"); 
					}else{
						strSQL.append("declare @data table (ditem_id int, secuencia int) \n")
						.append("insert into @data \n")
						.append("select u.ditem_id, secuencia=row_number() over(order by u.ditem_id) \n")
						.append("from eje_ges_ditem_unidad u \n")
						.append("inner join eje_ges_dimension_item di on di.ditem_id=u.ditem_id \n")
						.append("where di.dimension_id=@dimension_id and u.unidad_id=@unidad_id \n\n")
						
						.append("delete di \n")
						.append("from eje_ges_dimension_item di \n")
						.append("inner join @data x on x.ditem_id=di.ditem_id \n")
						.append("where x.secuencia>1 \n\n")
						
						.append("update di set ditem_desc=@ditem_desc \n")
						.append("from eje_ges_dimension_item di \n")
						.append("inner join @data x on x.ditem_id=di.ditem_id \n")
						.append("where di.dimension_id=@dimension_id \n\n")
						
						.append("if (select count(*) from @data)=0 begin \n")
						.append("	insert into eje_ges_dimension_item (ditem_desc, dimension_id) values(@ditem_desc, @dimension_id) \n")
						.append("	insert into eje_ges_ditem_unidad (unidad_id, ditem_id) values (@unidad_id, @@identity) \n")
						.append("end \n\n");
					}
					
					Object[] params = {intIdDimension, strIdUnidad, strValor};
					
					ConsultaTool.getInstance().execute("portal", strSQL.toString(), params);
					
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ok = false;
		}
		return ok;
	}

	@Override
	public boolean updDimensionUnidad(String strIdUnidad, List<VoUDimension> dimensiones, boolean bolSubUnidades) {

		boolean ok = true;
		
		if (bolSubUnidades) {
			ConsultaData unidades = OrganicaLocator.getInstance().getUnidadesDescendientes(strIdUnidad);
			while (unidades!=null && unidades.next()){
				ok &= updDimensionUnidad(unidades.getString("unidad"), dimensiones);
			}
		}else {
			ok = updDimensionUnidad(strIdUnidad, dimensiones);
		}
		
		return ok;
	}


	@Override
	public VoUDValor newItem(int intIdDimension, String valor, boolean activo) {
		String sql = "INSERT INTO eje_ges_dimension_item (ditem_desc, dimension_id) values (?,?) ";
		Object[] params = {valor, intIdDimension};
		
		double newId = -1;
		try {
			newId = ConsultaTool.getInstance().insertIdentity("portal", sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		VoUDValor voValor = new VoUDValor((int)newId, valor);
		return voValor;
	}

}
