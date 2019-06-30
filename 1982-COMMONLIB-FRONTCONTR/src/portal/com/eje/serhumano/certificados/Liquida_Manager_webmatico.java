package portal.com.eje.serhumano.certificados;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.ConsultaDataMode;
import cl.ejedigital.tool.strings.SqlBuilder;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.datos.Consulta;
import portal.com.eje.portal.liquidacion.enums.LiquidacionOrigen;
import portal.com.eje.serhumano.datosdf.datosRut;
import portal.com.eje.tools.OutMessage;

/**
 * @author  Administrador
 */
public class Liquida_Manager_webmatico
{

    public Liquida_Manager_webmatico(Connection conex)
    {
        con = conex;
        mensajeError = "";
    }

    public Consulta GetPermisoComision() {
        consul = new Consulta(con);
        String sql = String.valueOf(new StringBuffer("SELECT 1 AS valido FROM eje_ges_aplicacion WHERE app_id='comision'")); 
        OutMessage.OutMessagePrint("Permiso Módulo Comisión: ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        return consul;
    }
    
    public Consulta GetEmpresa(String rut)
    {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("SELECT e.descrip nom_empresa,e.empresa cod FROM eje_ges_empresa e inner join eje_ges_trabajador t ")
        	.append("on e.empresa =t.wp_cod_empresa WHERE t.rut = ").append(rut);
        consul.exec(sql.toString());
        return consul;
    }
    
    /**
     * No reconoce multiples reconoce multiples registros en la cabecera
     * @deprecated
     * @since 12-04-2019
     * @see #GetEmpresa(int, int, int)
     * */
    public Consulta GetEmpresa(String rut, String periodo) {
        consul = new Consulta(con);
        StringBuffer sql = new StringBuffer("SELECT top 1 emp.descrip nom_empresa,emp.empre_dir dir_empresa ")
        	.append("FROM eje_ges_empresa emp inner join eje_ges_certif_histo_liquidacion_cabecera lc ")
        	.append("on emp.empresa = lc.empresa WHERE lc.rut = ").append(rut).append(" and lc.periodo=")
        	.append(periodo);
        OutMessage.OutMessagePrint("Cabecera Liquidacion: ".concat(sql.toString()));
        consul.exec(sql.toString());
        return consul;
    }
    
    /**
     * @author Pancho
     * @since 14-04-2019
     * */
    public ConsultaData GetEmpresa(int rut, int periodo, int empresa) throws SQLException {
        
        SqlBuilder sql = new SqlBuilder();
        sql.appendLine(" SELECT top 1 emp.descrip nom_empresa,emp.empre_dir dir_empresa ");
        sql.appendLine(" FROM eje_ges_empresa emp ");
        sql.appendLine(" 		inner join eje_ges_certif_histo_liquidacion_cabecera lc ");
        sql.appendLine(" 				on emp.empresa = lc.empresa ");
        sql.appendLine(" WHERE lc.rut = ? and lc.periodo= ? and lc.wp_cod_empresa = ? ");

        ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, new Object[] {rut, periodo , empresa});
        
        if(data != null) {
        	data.setMode(ConsultaDataMode.CONVERSION);
        }
        
        return data;
    }

    public Consulta GetSociedad(String rut)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(new StringBuffer("SELECT soc.descripcion as nom_sociedad FROM eje_ges_sociedad soc, eje_ges_trabajador trab WHERE soc.codigo = trab.sociedad AND soc.wp_cod_empresa = trab.empresa AND trab.rut = " + rut));
        OutMessage.OutMessagePrint("Cabecera Liquidacion: ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetSociedad(String rut, String periodo) {
        consul = new Consulta(con);
        String sql = "SELECT top 1 soc.descripcion as nom_sociedad FROM eje_ges_sociedad soc, eje_ges_certif_histo_liquidacion_cabecera lc WHERE soc.codigo = lc.wp_cod_planta AND soc.wp_cod_empresa = lc.wp_cod_empresa AND lc.rut = " + rut + " and lc.periodo=" + periodo;
        OutMessage.OutMessagePrint("Cabecera Liquidacion: ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        return consul;
    }
    
    /**
     * 
     * */
    public ConsultaData GetSociedad(int rut, int periodo) throws SQLException {

        SqlBuilder sql = new SqlBuilder(); 
        sql.appendLine(" SELECT top 1 soc.descripcion as nom_sociedad ");
        sql.appendLine(" FROM eje_ges_sociedad soc, eje_ges_certif_histo_liquidacion_cabecera lc ");
        sql.appendLine(" WHERE soc.codigo = lc.wp_cod_planta AND soc.wp_cod_empresa = lc.wp_cod_empresa AND lc.rut = ? and lc.periodo= ? ");

        ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, new Object[] {rut, periodo});
        
        if(data != null) {
        	data.setMode(ConsultaDataMode.CONVERSION);
        }
        
        return data;
    }

    /**
     * No reconoce multiples trabajadores historia
     * @deprecated 
     * @since 12-04-2019
     * */
    public Consulta GetUnidad(LiquidacionOrigen origen, String rut)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(new StringBuffer("SELECT uni.unid_desc as nom_unidad FROM eje_sh_unidades uni, @@trabajador trab WHERE uni.unid_empresa = trab.empresa AND uni.unid_id = trab.unidad AND trab.rut = " + rut));
        OutMessage.OutMessagePrint("Cabecera Liquidacion: ".concat(String.valueOf(String.valueOf(sql))));
        
        sql = sql.replaceAll("@@trabajador", origen.toString());
        
        consul.exec(sql);
        return consul;
    }
    
    /**
     * @author Pancho
     * @since 12-04-2019
     * */
    public ConsultaData GetUnidad(LiquidacionOrigen origen, int rut, int periodo, Date fecTraspasoInc) throws SQLException
    {
    	List<Object> params = new ArrayList<Object>();
    	
        SqlBuilder sql = new SqlBuilder();
        sql.appendLine(" SELECT uni.unid_desc as nom_unidad ");
        sql.appendLine(" FROM eje_sh_unidades uni, @@trabajador trab ");
        sql.appendLine(" WHERE uni.unid_empresa = trab.empresa AND uni.unid_id = trab.unidad AND trab.rut = ? ");
        params.add(rut);
        
        if(origen == LiquidacionOrigen.eje_ges_trabajador_historia) {
        	sql.appendLine(" and trab.periodo = ? and trab.fecTraspasoInc = ? ");
        	params.add(periodo);
        	params.add(fecTraspasoInc);
        }
        
        String sqlFinal = sql.toString().replaceAll("@@trabajador", origen.toString());
        
        ConsultaData data = ConsultaTool.getInstance().getData("portal", sqlFinal, params.toArray());
        
        if(data != null) {
        	data.setMode(ConsultaDataMode.CONVERSION);
        }
        
        return data;
    }

    public Consulta GetUnidadLiquidacion(String rut, String peri)
    {
        consul = new Consulta(con);
        int periodo = Integer.parseInt(peri);
        String sql = String.valueOf(new StringBuffer("SELECT unidad FROM eje_ges_certif_histo_liquidacion_cabecera WHERE periodo=" + periodo + " AND rut=" + rut));
        OutMessage.OutMessagePrint("Cabecera Liquidacion: ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        consul.next();
        return consul;
    }

    /**
     * No reconoce multiples registros en la eje_ges_trabajador_historia
     * 
     * @deprecated
     * @author Pancho
     * @since 12-04-2018
     * 
     * */
    public Consulta GetCentroCosto(LiquidacionOrigen origen, String rut) {
        consul = new Consulta(con);
        StringBuffer sql = new StringBuffer("SELECT cc.centro_costo cco, cc.descrip as nom_centro_costo FROM eje_ges_centro_costo cc ")
        		.append("inner join @@trabajador trab on cc.wp_cod_empresa = trab.empresa AND cc.centro_costo = trab.ccosto ")
        		.append("WHERE trab.rut = ").append(rut);
        OutMessage.OutMessagePrint("Cabecera Liquidacion: ".concat(sql.toString()));
        
        String s = sql.toString().replaceAll("@@trabajador", origen.toString());
        
        consul.exec(s);
        return consul;
    }
    
    public ConsultaData GetCentroCosto(LiquidacionOrigen origen, int rut, int periodo, Date fechaTraspasoInc) throws SQLException {
    	List<Object> params = new ArrayList<Object>();
    	
    	SqlBuilder sql = new SqlBuilder();
    	sql.appendLine(" SELECT cc.centro_costo cco, cc.descrip as nom_centro_costo ");
    	sql.appendLine(" FROM eje_ges_centro_costo cc ");
    	sql.appendLine(" 	inner join @@trabajador trab on cc.wp_cod_empresa = trab.empresa AND cc.centro_costo = trab.ccosto ");
    	sql.appendLine(" WHERE trab.rut = ? ");
        
    	params.add(rut);
    	if(origen == LiquidacionOrigen.eje_ges_trabajador_historia) {
    		sql.appendLine(" and trab.periodo = ? and fecha_traspaso_inc = ? ");
    		params.add(periodo);
    		params.add(fechaTraspasoInc);
    	}
    	
        String s = sql.toString().replaceAll("@@trabajador", origen.toString());
        
        ConsultaData data = ConsultaTool.getInstance().getData("portal", s, params.toArray());
        
        if(data != null) {
        	data.setMode(ConsultaDataMode.CONVERSION);
        }
        
        return data;
    }

    public Consulta GetNombrePlanta(String rut)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(new StringBuffer("SELECT pp.descripcion as nom_planta FROM eje_ges_sociedad pp, eje_ges_trabajador trab WHERE pp.wp_cod_empresa = trab.empresa AND pp.codigo = trab.wp_cod_planta AND trab.rut = " + rut));
        OutMessage.OutMessagePrint("Cabecera Liquidacion: ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        return consul;
    }

    /**
     * No reconoce fec_traspaso_inc ni periodo cuando es historia
     * @deprecated
     * @author Pancho
     * */
    public Consulta GetAfp(LiquidacionOrigen origen, String rut)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(new StringBuffer("SELECT a.descrip as nom_afp, trab.cot_afp, trab.mo_cot_afp FROM eje_ges_afp a, @@trabajador trab WHERE a.afp = trab.afp AND trab.rut = " + rut));
        OutMessage.OutMessagePrint("Cabecera Liquidacion: ".concat(String.valueOf(String.valueOf(sql))));
        
        sql = sql.toString().replaceAll("@@trabajador", origen.toString());
        
        consul.exec(sql);
        return consul;
    }
    
    public ConsultaData GetAfp(LiquidacionOrigen origen, int rut, int periodo, Date fecTraspasoInc) throws SQLException
    {
      SqlBuilder sql = new SqlBuilder();
      sql.appendLine(" SELECT a.descrip as nom_afp, trab.cot_afp, trab.mo_cot_afp ");
      sql.appendLine(" FROM eje_ges_afp a, @@trabajador trab ");
      sql.appendLine(" WHERE a.afp = trab.afp AND trab.rut = ? ");
       
      List<Object> params = new LinkedList<Object>();
      params.add(rut);
      
      if(origen == LiquidacionOrigen.eje_ges_trabajador_historia) {
    	  sql.appendLine(" and trab.periodo = ? and trab.fec_traspaso_inc = ? ");
    	  params.add(periodo);
    	  params.add(fecTraspasoInc);
      }
      
      String sqlFinal = sql.toString().replaceAll("@@trabajador", origen.toString());
      
      ConsultaData data = ConsultaTool.getInstance().getData("portal", sqlFinal, params.toArray());
      if(data != null) {
    	  data.setMode(ConsultaDataMode.CONVERSION);
      }
      
      return data;
    }

    /**
     * No reconoce periodo ni fec_traspaso_inc cuando es historia
     * */
    public Consulta GetIsapre(LiquidacionOrigen origen, String rut)
    {
       
        String sql = String.valueOf(new StringBuffer("SELECT LTRIM(RTRIM(i.descrip)) as nom_isapre FROM eje_ges_isapres i, @@trabajador trab WHERE i.isapre = trab.isapre AND trab.rut = " + rut));
        OutMessage.OutMessagePrint("Cabecera Liquidacion: ".concat(String.valueOf(String.valueOf(sql))));
        
        sql = sql.toString().replaceAll("@@trabajador", origen.toString());
        
        consul.exec(sql);
        return consul;
    }
    
    public ConsultaData GetIsapre(LiquidacionOrigen origen, int rut, int periodo, Date fecTraspasoInc) throws SQLException {
	    SqlBuilder sql = new SqlBuilder();
	    sql.appendLine(" SELECT LTRIM(RTRIM(i.descrip)) as nom_isapre ");
	    sql.appendLine(" FROM eje_ges_isapres i, @@trabajador trab ");
	    sql.appendLine(" WHERE i.isapre = trab.isapre AND trab.rut = ? ");
	    List<Object> params = new LinkedList<Object>();
	    params.add(rut);
	    
	    if(origen == LiquidacionOrigen.eje_ges_trabajador_historia) {
	    	sql.appendLine(" and trab.periodo = ? and trab.fec_traspaso_inc = ? ");
	    	params.add(periodo);
	    	params.add(fecTraspasoInc);
	    }
	    
	    String sqlFinal = sql.toString().replaceAll("@@trabajador", origen.toString());
	    
	    ConsultaData data = ConsultaTool.getInstance().getData("portal", sqlFinal, params.toArray()); 
	    
	    if(data != null ) {
	    	data.setMode(ConsultaDataMode.CONVERSION);
	    }
	    
	    return data;
	}
    
    public ConsultaData getHaberesImponibles(int rut, String periodo) {
    	StringBuilder sql = new StringBuilder();
    	sql.append(" SELECT det.* FROM eje_ges_certif_histo_liquidacion_detalle det ");
    	sql.append(" 				LEFT JOIN dbo.eje_ges_haber def ON ");
    	sql.append("						( ");
    	sql.append("						def.wp_cod_empresa = det.wp_cod_empresa ");
    	sql.append("							AND  def.id_tp = det.id_tp ");
    	sql.append("       						AND  det.glosa_haber = def.descrip "); 
    	sql.append("       						AND  det.periodo = def.periodo "); 
    	sql.append("						) ");
    	sql.append(" WHERE   def.id_tp = 'H' ");
    	sql.append(" 		AND def.imponible = 1 "); 
    	sql.append(" 		AND det.rut = ? "); 
    	sql.append(" 		AND det.periodo = ? "); 
    	sql.append(" 		AND det.wp_indic_papeleta = 'S' ");
        sql.append(" order by def.orden ");
        
    	
    	try {
    		Object[] params = {rut, periodo};
			return ConsultaTool.getInstance().getData(this.con, sql.toString() , params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	return null;
    }
    
    
    public ConsultaData getTotalHaberesImponibles(int rut, String periodo) {
        //Fecha de corporación era fecha de ingreso 
            
            StringBuilder sqlTrabajador = new StringBuilder();
            sqlTrabajador.append(
            		"SELECT  SUM(det.val_haber) AS total \r\n" + 
            		"FROM    dbo.eje_ges_certif_histo_liquidacion_detalle det \r\n" + 
            		"	LEFT JOIN dbo.eje_ges_haber def ON \r\n" +
            		"       ( \r\n" + 
            		"			def.wp_cod_empresa = det.wp_cod_empresa \r\n" + 
            		"       	AND  def.id_tp = det.id_tp \r\n" + 
            		"       	AND  det.glosa_haber = def.descrip \r\n" + 
            		"       	AND  def.periodo= det.periodo \r\n" +
            		"       ) \r\n" + 
            		"WHERE   def.id_tp = 'H' \r\n" + 
            		"	AND def.imponible = 1 \r\n" + 
            		"	AND det.rut = ? \r\n" + 
            		"	AND det.periodo = ? \r\n" + 
            		"	AND det.wp_indic_papeleta = 'S' ");
            
          
           try {
        	   Object[] params = {rut, periodo}; 
			return ConsultaTool.getInstance().getData(this.con, sqlTrabajador.toString(), params);
			} catch (SQLException e) {
				e.printStackTrace();
			}
          
           return null;
           
    }
    
    public ConsultaData getTotalHaberesNoImponibles(int rut, String periodo) {
        //Fecha de corporación era fecha de ingreso 
        	 
            StringBuilder sqlTrabajador = new StringBuilder();
            sqlTrabajador.append(
            		"SELECT  SUM(det.val_haber) AS total \r\n" + 
            		"FROM    dbo.eje_ges_certif_histo_liquidacion_detalle det \r\n" + 
            		"	LEFT JOIN dbo.eje_ges_haber def ON \r\n" +
            		"       ( \r\n" + 
            		"			def.wp_cod_empresa = det.wp_cod_empresa \r\n" + 
            		"       	AND  def.id_tp = det.id_tp \r\n" + 
            		"       	AND  det.glosa_haber = def.descrip \r\n" + 
            		"       	AND  def.periodo= det.periodo \r\n" +
            		"       ) \r\n" + 
            		"WHERE   def.id_tp = 'H' \r\n" + 
            		"	AND def.imponible = 0 \r\n" + 
            		"	AND det.rut = ").append(rut).append(" \r\n" + 
            		"	AND det.periodo = ").append(periodo).append(" \r\n" + 
            		"	AND det.wp_indic_papeleta = 'S' ");
            
            try {
				return ConsultaTool.getInstance().getData(con, sqlTrabajador.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 
            return null;
        }
    
    public ConsultaData getHaberesNoImponibles(int rut, String periodo) {
        //Fecha de corporación era fecha de ingreso 
        	 
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT  * " );
            sql.append(" FROM    dbo.eje_ges_certif_histo_liquidacion_detalle det "); 
            sql.append(" LEFT JOIN dbo.eje_ges_haber def ON ");
            sql.append(" 		 (  ");
            sql.append(" 			def.wp_cod_empresa = det.wp_cod_empresa ");
            sql.append(" 			AND  def.id_tp = det.id_tp "); 
            sql.append(" 			AND  det.glosa_haber = def.descrip ");
        	sql.append("       		AND  det.periodo = def.periodo ");
            sql.append(" 			 ) ");
            sql.append(" WHERE   def.id_tp = 'H' "); 
            sql.append(" 				AND def.imponible = 0 "); 
            sql.append(" 				AND det.rut = ").append(rut);
            sql.append(" 				AND det.periodo = ").append(periodo); 
            sql.append(" 				AND det.wp_indic_papeleta = 'S' ");
            sql.append(" order by def.orden ");
            
            try {
				return ConsultaTool.getInstance().getData(con, sql.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 
            return null;
        }
    
    public ConsultaData getTotalDescuentosPrevisionales( int rut, String periodo) {
 
            StringBuilder sqlTrabajador = new StringBuilder();
            sqlTrabajador.append(
            		"SELECT  SUM(det.val_descuento) AS total \r\n" + 
            		"FROM    dbo.eje_ges_certif_histo_liquidacion_detalle det \r\n" + 
            		"	LEFT JOIN dbo.eje_ges_haber def ON \r\n" +
            		"       ( \r\n" + 
            		"			def.wp_cod_empresa = det.wp_cod_empresa \r\n" + 
            		"       	AND  def.id_tp = det.id_tp \r\n" + 
            		"       	AND  det.glosa_descuento = def.descrip \r\n" + 
            		"       	AND  def.periodo= det.periodo \r\n" +
            		"       ) \r\n" + 
            		"WHERE   def.id_tp = 'D' \r\n" + 
            		"	AND def.imponible = 1 \r\n" + 
            		"	AND det.rut = ").append(rut).append(" \r\n" + 
            		"	AND det.periodo = ").append(periodo).append(" \r\n" + 
            		"	AND det.wp_indic_papeleta = 'S' ");
            
            try {
				return ConsultaTool.getInstance().getData(con, sqlTrabajador.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           
            return null;
        }
    
    public ConsultaData getDescuentosPrevisionales( int rut, String periodo) {
    	 
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT  det.*  "); 
        sql.append(" FROM    dbo.eje_ges_certif_histo_liquidacion_detalle det ");
        sql.append(" 	LEFT JOIN dbo.eje_ges_haber def ON ");
        sql.append("        ( "); 
        sql.append("			def.wp_cod_empresa = det.wp_cod_empresa "); 
        sql.append("        	AND  def.id_tp = det.id_tp "); 
        sql.append("        	AND  det.glosa_descuento = def.descrip "); 
    	sql.append("       		AND  det.periodo = def.periodo ");
        sql.append("        ) "); 
        sql.append(" WHERE   def.id_tp = 'D' "); 
        sql.append(" 	AND def.imponible = 1 "); 
        sql.append(" 	AND det.rut = ").append(rut); 
        sql.append(" 	AND det.periodo = ").append(periodo); 
        sql.append(" 	AND det.wp_indic_papeleta = 'S' ");
        sql.append(" order by def.orden ");
        
        try {
 
			return ConsultaTool.getInstance().getData(con, sql.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        return null;
    }
    
    public ConsultaData getTotalDescuentosNoPrevisionales(int rut, String periodo) {
 
            StringBuilder sqlTrabajador = new StringBuilder();
            sqlTrabajador.append(
            		"SELECT  SUM(det.val_descuento) AS total \r\n" + 
            		"FROM    dbo.eje_ges_certif_histo_liquidacion_detalle det \r\n" + 
            		"	LEFT JOIN dbo.eje_ges_haber def ON \r\n" +
            		"       ( \r\n" + 
            		"			def.wp_cod_empresa = det.wp_cod_empresa \r\n" + 
            		"       	AND  def.id_tp = det.id_tp \r\n" + 
            		"       	AND  det.glosa_descuento = def.descrip \r\n" + 
            		"       	AND  def.periodo= det.periodo \r\n" +
            		"       ) \r\n" + 
            		"WHERE   def.id_tp = 'D' \r\n" + 
            		"	AND def.imponible = 0 \r\n" + 
            		"	AND det.rut = ").append(rut).append(" \r\n" + 
            		"	AND det.periodo = ").append(periodo).append(" \r\n" + 
            		"	AND det.wp_indic_papeleta = 'S' ");
     
           try {
        	  return ConsultaTool.getInstance().getData(con, sqlTrabajador.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
	            return null;
	        }
   
    public ConsultaData getDescuentosNoPrevisionales(int rut, String periodo) {
    	 
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT  det.* "); 
        sql.append(" FROM    dbo.eje_ges_certif_histo_liquidacion_detalle det "); 
        sql.append("	LEFT JOIN dbo.eje_ges_haber def ON ");
        sql.append("       ( "); 
        sql.append("			def.wp_cod_empresa = det.wp_cod_empresa "); 
        sql.append("       	AND  def.id_tp = det.id_tp "); 
        sql.append("       	AND  det.glosa_descuento = def.descrip "); 
    	sql.append("       	AND  det.periodo = def.periodo ");
        sql.append("       ) "); 
        sql.append(" WHERE   def.id_tp = 'D' "); 
        sql.append("			AND def.imponible = 0 ");
        sql.append("	AND det.rut = ").append(rut); 
        sql.append("	AND det.periodo = ").append(periodo); 
        sql.append("	AND det.wp_indic_papeleta = 'S' ");
        sql.append(" order by def.orden ");
        
       try {
    	  return ConsultaTool.getInstance().getData(con, sql.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
            return null;
        }
    
	public ConsultaData getHorasExtrasFides(int periodoInt, int rut) {
		// TODO Auto-generated method stub
		StringBuilder horasE = new StringBuilder();
		
		horasE.append(" if( exists (select * from sysobjects where name='eje_ges_centraliza_horas_extras' and xtype='U')) ");
		horasE.append("	begin ");
		horasE.append("		SELECT cantidad_50,cantidad_100, valor_50,valor_100 ")
				.append("	FROM eje_ges_centraliza_horas_extras WHERE periodo = ").append(periodoInt)
				.append(" 	AND rut = ").append(rut);
		horasE.append("	end ");
		horasE.append("	else ");
		horasE.append("	begin ");
		horasE.append("		SELECT cantidad_50=0,cantidad_100=0, valor_50=0,valor_100=0 ");
		horasE.append("	end ");
		try {
			return ConsultaTool.getInstance().getData(con, horasE.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
    
    public Consulta GetCabecera(String rut)
    {
        consul = new Consulta(con);
        userRut = new datosRut(con, rut);
        peri = getMaxPeriodo();
        empresa = userRut.Empresa;
        unidad = GetUnidadLiquidacion(rut, peri).getString("unidad");
        String sql = String.valueOf(String.valueOf((new StringBuffer("SELECT liq_cab.wp_cod_empresa, liq_cab.wp_cod_planta,liq_cab.periodo, liq_cab.empresa,empresa.descrip AS nom_empre, liq_cab.unidad,liq_cab.rut, trab.digito_ver, trab.nombre,liq_cab.causa_pago,liq_cab.fec_pago,liq_cab.forma_pago, liq_cab.cuenta,liq_cab.imp_tribut, liq_cab.imp_no_tribut,liq_cab.no_imp_tribut, liq_cab.no_imp_no_tribut,liq_cab.reliq_rentas, liq_cab.tot_haberes,liq_cab.tot_desctos, liq_cab.liquido,liq_cab.id_forma_pago, liq_cab.tope_imp,liq_cab.val_uf,liq_cab.dctos_varios,liq_cab.dctos_legales, liq_cab.dctos_impagos,liq_cab.banco, trab.fecha_ingreso FROM eje_ges_certif_histo_liquidacion_cabecera liq_cab INNER JOIN eje_ges_trabajador trab ON liq_cab.rut = trab.rut AND liq_cab.empresa = trab.empresa INNER JOIN eje_ges_empresa empresa ON liq_cab.empresa = empresa.empresa WHERE (liq_cab.periodo = ")).append(peri).append(") ").append("AND (liq_cab.empresa = '").append(empresa).append("') ").append("AND (liq_cab.unidad = '").append(unidad).append("') ").append("AND (liq_cab.rut = ").append(rut).append(")")));
        OutMessage.OutMessagePrint("Cabecera Liquidacion: ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        return consul;
    }

    /**
     * Por Pancho, no reconoce el periodo ni la fecha_traspaso_inc
     * @deprecated
     * */
    public Consulta GetCabecera( LiquidacionOrigen origen, String rut, String periodo) {
    //Fecha de corporación era fecha de ingreso 
    	consul = new Consulta(con);
        userRut = new datosRut(con, rut);
        empresa = userRut.Empresa;    
        unidad = GetUnidadLiquidacion(rut, periodo).getString("unidad");
        StringBuilder sqlTrabajador = new StringBuilder();
        sqlTrabajador.append(" SELECT isnull(liq_cab.wp_ndias_lic,0) as wp_ndias_lic, isnull(liq_cab.tramo, 0) as tramo,  isnull(liq_cab.sobregiro,0) as sobregiro, ");
        sqlTrabajador.append(" 	isnull(liq_cab.n_cargas,0) as n_cargas, isnull(liq_cab.base_tribut,0) as base_tribut,  isnull(liq_cab.wp_cod_empresa,0) as wp_cod_empresa, ");
        sqlTrabajador.append("	liq_cab.wp_cod_empresa, liq_cab.wp_afecto_imponible as rta_impon, liq_cab.wp_tot_imponible, liq_cab.wp_cod_planta, liq_cab.wp_ndias_trab as ndias_trab, ");
        sqlTrabajador.append("	liq_cab.periodo, liq_cab.empresa,empresa.descrip AS nom_empre, empresa.empre_rut, liq_cab.unidad,liq_cab.rut, trab.digito_ver, trab.nombre, ");
        sqlTrabajador.append("	liq_cab.causa_pago,liq_cab.fec_pago,liq_cab.forma_pago, liq_cab.cuenta,liq_cab.imp_tribut, liq_cab.imp_no_tribut,liq_cab.no_imp_tribut, ");
        sqlTrabajador.append("	liq_cab.no_imp_no_tribut,liq_cab.reliq_rentas, liq_cab.tot_haberes,liq_cab.tot_desctos, liq_cab.liquido,liq_cab.id_forma_pago, ");
        sqlTrabajador.append("	liq_cab.tope_imp,liq_cab.val_uf as val_uf,liq_cab.dctos_varios,liq_cab.dctos_legales, liq_cab.dctos_impagos,liq_cab.banco, ");
        sqlTrabajador.append("	fecha_ingreso=convert(varchar(10),trab.fecha_ingreso,112), trab.estado_civil, trab.sexo, cargo.descrip as nom_cargo, ");
        sqlTrabajador.append("	trab.adic_area, trab.adic_tipo_area, trab.adic_zona, trab.adic_gerente_regional, trab.adic_subgerente, trab.adic_jefe, trab.adic_modulo_gerencia_pertenece ");
        sqlTrabajador.append("	, trab.adic_sucursal , trab.adic_oficina , trab.adic_oficina_direccion , trab.adic_familia_cargo , trab.adic_cargo_funcional , trab.adic_especialidad ");
        sqlTrabajador.append("	, trab.adic_cod_ejecutivo, trab.adic_gerencia_subgerencia, trab.adic_division, trab.adic_area2, trab.adic_categoria, trab.adic_talla, trab.adic_telefono_casa");
        sqlTrabajador.append("	, trab.adic_escolaridad_cod, trab.adic_escolaridad_glosa ");
        sqlTrabajador.append(" FROM eje_ges_certif_histo_liquidacion_cabecera liq_cab ");
        sqlTrabajador.append("		INNER JOIN @@trabajador trab ON liq_cab.rut = trab.rut ");
        sqlTrabajador.append("		INNER JOIN eje_ges_cargos cargo ON trab.cargo = cargo.cargo AND trab.empresa = cargo.empresa ");
        sqlTrabajador.append("		INNER JOIN eje_ges_empresa empresa ON liq_cab.empresa = empresa.empresa ");
        sqlTrabajador.append(" WHERE (liq_cab.periodo = ").append(periodo).append(") ").append("AND (liq_cab.unidad = '").append(unidad).append("') ").append("AND (liq_cab.rut = ").append(rut).append(")");
 
        String sql = sqlTrabajador.toString().replaceAll("@@trabajador", origen.toString());
        
        consul.exec(sql);
       
        return consul;
    }
    
    public ConsultaData GetCabecera( LiquidacionOrigen origen, Date fechaTraspasoInc, String rut, String periodo) throws SQLException {
    //Fecha de corporación era fecha de ingreso 
    	 
        
        
        String unidad = GetUnidadLiquidacion(rut, periodo).getString("unidad");
        StringBuilder sqlTrabajador = new StringBuilder();
        sqlTrabajador.append(" SELECT isnull(liq_cab.wp_ndias_lic,0) as wp_ndias_lic, isnull(liq_cab.tramo, 0) as tramo,  isnull(liq_cab.sobregiro,0) as sobregiro, ");
        sqlTrabajador.append(" 	isnull(liq_cab.n_cargas,0) as n_cargas, isnull(liq_cab.base_tribut,0) as base_tribut,  isnull(liq_cab.wp_cod_empresa,0) as wp_cod_empresa, ");
        sqlTrabajador.append("	liq_cab.wp_cod_empresa, liq_cab.wp_afecto_imponible as rta_impon, liq_cab.wp_tot_imponible, liq_cab.wp_cod_planta, liq_cab.wp_ndias_trab as ndias_trab, ");
        sqlTrabajador.append("	liq_cab.periodo, liq_cab.empresa,empresa.descrip AS nom_empre, empresa.empre_rut, liq_cab.unidad,liq_cab.rut, trab.digito_ver, trab.nombre, ");
        sqlTrabajador.append("	liq_cab.causa_pago,liq_cab.fec_pago,liq_cab.forma_pago, liq_cab.cuenta,liq_cab.imp_tribut, liq_cab.imp_no_tribut,liq_cab.no_imp_tribut, ");
        sqlTrabajador.append("	liq_cab.no_imp_no_tribut,liq_cab.reliq_rentas, liq_cab.tot_haberes,liq_cab.tot_desctos, liq_cab.liquido,liq_cab.id_forma_pago, ");
        sqlTrabajador.append("	liq_cab.tope_imp,liq_cab.val_uf as val_uf,liq_cab.dctos_varios,liq_cab.dctos_legales, liq_cab.dctos_impagos,liq_cab.banco, ");
        sqlTrabajador.append("	fecha_ingreso=convert(varchar(10),trab.fecha_ingreso,112), trab.estado_civil, trab.sexo, cargo.descrip as nom_cargo, ");
        sqlTrabajador.append("	trab.adic_area, trab.adic_tipo_area, trab.adic_zona, trab.adic_gerente_regional, trab.adic_subgerente, trab.adic_jefe, trab.adic_modulo_gerencia_pertenece ");
        sqlTrabajador.append("	, trab.adic_sucursal , trab.adic_oficina , trab.adic_oficina_direccion , trab.adic_familia_cargo , trab.adic_cargo_funcional , trab.adic_especialidad ");
        sqlTrabajador.append("	, trab.adic_cod_ejecutivo, trab.adic_gerencia_subgerencia, trab.adic_division, trab.adic_area2, trab.adic_categoria, trab.adic_talla, trab.adic_telefono_casa");
        sqlTrabajador.append("	, trab.adic_escolaridad_cod, trab.adic_escolaridad_glosa ");
        sqlTrabajador.append(" FROM eje_ges_certif_histo_liquidacion_cabecera liq_cab ");
        
        List<Object> params = new ArrayList<Object>();
        if(LiquidacionOrigen.eje_ges_trabajador == origen) {
        	sqlTrabajador.append("		INNER JOIN @@trabajador trab ON liq_cab.rut = trab.rut ");
        }
        else if(LiquidacionOrigen.eje_ges_trabajador_historia == origen) {
        	sqlTrabajador.append("		INNER JOIN @@trabajador trab ON liq_cab.rut = trab.rut and liq_cab.periodo = trab.periodo and trab.fecha_traspaso_inc = ? ");
        	params.add(fechaTraspasoInc);
        }
        
        //no es neceario que esté vigente
        sqlTrabajador.append("		INNER JOIN eje_ges_cargos cargo ON trab.cargo = cargo.cargo AND trab.empresa = cargo.empresa ");
        sqlTrabajador.append("		INNER JOIN eje_ges_empresa empresa ON liq_cab.empresa = empresa.empresa ");
        sqlTrabajador.append(" WHERE (liq_cab.periodo = ?)  AND (liq_cab.unidad = ? )  AND (liq_cab.rut = ?)");
        params.add(periodo);
        params.add(unidad);
        params.add(rut);
        
        String sql = sqlTrabajador.toString().replaceAll("@@trabajador", origen.toString());
        
        ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params.toArray());
       
        if(data != null) {
        	data.setMode(ConsultaDataMode.CONVERSION);
        }
        return data;
    }
    
    

    public Consulta GetCabeceraAdicional(String rut, String periodo)
    {
        consul = new Consulta(con);
        userRut = new datosRut(con, rut);
        empresa = userRut.Empresa;
        unidad = GetUnidadLiquidacion(rut, periodo).getString("unidad");
        String sql = String.valueOf(String.valueOf((new StringBuffer("SELECT liq_cab.wp_cod_empresa, liq_cab.wp_afecto_imponible as rta_impon, liq_cab.wp_tot_imponible, liq_cab.wp_cod_planta, liq_cab.wp_ndias_trab as ndias_trab, liq_cab.periodo, liq_cab.empresa,empresa.descrip AS nom_empre, liq_cab.unidad,liq_cab.rut, trab.digito_ver, trab.nombre,liq_cab.causa_pago,liq_cab.fec_pago,liq_cab.forma_pago, liq_cab.cuenta,liq_cab.imp_tribut, liq_cab.imp_no_tribut,liq_cab.no_imp_tribut, liq_cab.no_imp_no_tribut,liq_cab.reliq_rentas, liq_cab.tot_haberes,liq_cab.tot_desctos, liq_cab.liquido,liq_cab.id_forma_pago, liq_cab.tope_imp,liq_cab.val_uf as val_uf,liq_cab.dctos_varios,liq_cab.dctos_legales, liq_cab.dctos_impagos,liq_cab.banco, trab.fecha_ingreso, trab.estado_civil, trab.sexo, cargo.descrip as nom_cargo,trab.fec_ing_hold FROM eje_ges_certif_histo_liquidacion_cabecera_adic liq_cab INNER JOIN eje_ges_trabajador trab ON liq_cab.rut = trab.rut AND liq_cab.empresa = trab.empresa INNER JOIN eje_ges_cargos cargo ON trab.cargo = cargo.cargo AND trab.empresa = cargo.empresa INNER JOIN eje_ges_empresa empresa ON liq_cab.empresa = empresa.empresa WHERE (liq_cab.periodo = ")).append(periodo).append(") ").append("AND (liq_cab.empresa = '").append(empresa).append("') ").append("AND (liq_cab.unidad = '").append(unidad).append("') ").append("AND (liq_cab.rut = ").append(rut).append(")")));
        OutMessage.OutMessagePrint("Cabecera Liquidacion: ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        return consul;
    }

    public String getMaxPeriodo()
    {
        consul = new Consulta(con);
        String periodo = "";
        String sql = "select  peri_id, peri_mes, peri_ano from eje_ges_periodo where peri_id = (select max(peri_id) as a from eje_ges_periodo)";
        consul.exec(sql);
        if(consul.next())
            periodo = consul.getString("peri_id");
        return periodo;
    }
    
    public Consulta getUltimasLiquidaciones(String rut, String year)
    {
        consul = new Consulta(con);
        String sql = "SELECT TOP 4 tot_haberes, periodo FROM eje_ges_certif_histo_liquidacion_cabecera where rut = " + rut+ " AND periodo LIKE '" + year + "%' ORDER BY periodo desc";
        consul.exec(sql);
        return consul;
    }
    

    public String getError()
    {
        return mensajeError;
    }

    public String getPeriodo()
    {
        return peri;
    }

    /**
	 * @return  Returns the empresa.
	 * @uml.property  name="empresa"
	 */
    public String getEmpresa()
    {
        return empresa;
    }

    /**
	 * @return  Returns the unidad.
	 * @uml.property  name="unidad"
	 */
    public String getUnidad()
    {
        return unidad;
    }

    /**
	 * @return  Returns the isapre.
	 * @uml.property  name="isapre"
	 */
    public String getIsapre()
    {
        return isapre;
    }

    /**
	 * @return  Returns the afp.
	 * @uml.property  name="afp"
	 */
    public String getAfp()
    {
        return afp;
    }

    public String getCentroCosto()
    {
        return centro_costo;
    }

    private Connection con;
    private Consulta consul;
    private String mensajeError;
    private datosRut userRut;
    private String peri;
    private String unidad;
    private String sociedad;
    private String empresa;
    private String centro_costo;
    private String afp;
    private String isapre;
    

}