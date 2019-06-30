package portal.com.eje.serhumano.misdatos;

import java.sql.Connection;
import java.sql.SQLException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.strings.SqlBuilder;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.datos.Consulta;

public class Vaca_Manager
{

    public Vaca_Manager(Connection conex)
    {
        con = conex;
        mensajeError = "";
    }

    public Consulta GetCabeceraCartola(String rut)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT rut, digito_ver,nombre, cargo, fecha_ingreso, id_foto,anexo,e_mail,unidad,area,division,hoy,fecha_ingreso,inghold,recono,fecrecono FROM view_InfoRut  WHERE (rut = '")).append(rut).append("')")));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetCabeceraCartolaWp(String rut) {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("SELECT (dias_pendientes+dias_del_periodo+dias_progresivos) saldo, ")
        		.append("dias_con_derecho, dias_pendientes, dias_del_periodo from eje_ges_vacaciones_wp ")
        		.append("WHERE (rut = '").append(rut).append("')");
        consul.exec(sql.toString());
        return consul;
    }

    public Consulta GetCabeceraVacacionesDev(String rut)
    {
        consul = new Consulta(con);
        //String sql = String.valueOf(String.valueOf( (new StringBuilder("SELECT cod_empresa,cod_planta,rut_cliente,dv_cliente,ano_periodo,mes_periodo,dias_devengados,fec_devengados FROM eje_ges_feriad_devengados   WHERE rut_cliente = ")).append(rut).append(")")));
    	//String sql = String.valueOf(String.valueOf( (new StringBuilder("SELECT cod_empresa,cod_planta,rut_cliente,dv_cliente,ano_periodo,mes_periodo,dias_devengados,fec_devengados FROM eje_ges_feriad_devengados a WHERE rut_cliente = ")).append(rut).append(" and convert(int,ano_periodo+mes_periodo) = (SELECT max(convert(int,ano_periodo+mes_periodo)) FROM eje_ges_feriad_devengados b where a.rut_cliente = b.rut_cliente and a.cod_empresa = b.cod_empresa) ") ));
        //String sql = String.valueOf(String.valueOf( (new StringBuilder("SELECT top 1 cod_empresa,cod_planta,rut_cliente,dv_cliente,ano_periodo,mes_periodo,dias_devengados,fec_devengados FROM eje_ges_feriad_devengados a WHERE rut_cliente = ")).append(rut).append(" and mes_periodo = (SELECT max(mes_periodo) FROM eje_ges_feriad_devengados b where a.rut_cliente = b.rut_cliente and a.cod_empresa = b.cod_empresa and a.ano_periodo=b.ano_periodo)") ));
        String sql = String.valueOf(String.valueOf( (new StringBuilder("SELECT top 1 cod_empresa,cod_planta,rut_cliente,dv_cliente,ano_periodo,mes_periodo,dias_devengados,fec_devengados FROM eje_ges_feriad_devengados WHERE rut_cliente = ")).append(rut).append(" order by ano_periodo desc,mes_periodo desc") ));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetDetalleCartola(String rut)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("select periodo,pen_normal,pen_progresivo,pen_convenio,der_normal,der_progresivo,der_convenio,uti_normal,uti_progresivo,uti_convenio,prog_vendido,periodo,bono_normal,bono_pen_normal,bono_convenio,bono_pen_convenio from eje_ges_vacaciones where rut = ")).append(rut).append(" order by periodo desc")));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetDetalleCartolaWp(String rut)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("select periodo, dias_con_derecho, dias_pendientes, dias_del_periodo, dias_prox_periodo, sdo_actual_periodo, dias_progresivos from eje_ges_vacaciones_wp where rut = ")).append(rut).append(" order by periodo desc")));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetDetalleCartolaNewWp(String rut) {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("select  distinct convert(datetime,convert(varchar(10),desde,112)) desde, ")
        		.append("convert(datetime,convert(varchar(10),hasta,112)) hasta, dias_normales from eje_ges_vacaciones_det ")
        		.append("where rut = ").append(rut).append(" order by  desde DESC");
        consul.exec(sql.toString());
        return consul;
    }

    public Consulta GetDetallePeriodo(String rut, String periodo)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("select desde,hasta,dias_normales,dias_progresivos,periodo from eje_ges_vacaciones_det where rut=")).append(rut).append(" and periodo=").append(periodo).append(" order by ano_periodo=")));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetDetallePeriodoWp(String rut, String ano_periodo)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("select ano_periodo, mes_periodo, dias_con_derecho, dias_pendientes, dias_del_periodo, dias_prox_periodo, sdo_actual_periodo, dias_progresivos from eje_ges_vacaciones_det_wp where rut=")).append(rut).append(" and ano_periodo=").append(ano_periodo).append(" order by ano_periodo, mes_periodo")));
        consul.exec(sql);
        return consul;
    }
    
    /**
     * Creado desde la integración con el WebMatico
     * @since 6-dic-2016
     * */
    public Consulta GetYearTotalVacaciones(String rut) {
    	consul = new Consulta(con);
    	StringBuilder sql = new StringBuilder("select top 3 datepart(year,desde) as year_vaca ,sum(dias_normales) as total_dias ")
    		.append("from eje_ges_vacaciones_det where rut = ").append(rut).append("  and periodo=(select max(periodo) from ")
    		.append("eje_ges_vacaciones_det) group by datepart(year,desde) order by datepart(year,desde) desc");
    	consul.exec(sql.toString());
    	return consul;
    }
    
    
    public ConsultaData getYearTotalVacaciones(String rut) {
    	try {
    		
    		Object[] params = {rut};
    		
    		SqlBuilder sql = new SqlBuilder();
        	sql.appendLine(" select top 3 datepart(year,desde) as year_vaca ,sum(dias_normales) as total_dias "); 
        	sql.appendLine(" from eje_ges_vacaciones_det "); 
        	sql.appendLine(" where rut = ? ");  
        	sql.appendLine(" group by datepart(year,desde) "); 
        	sql.appendLine(" order by datepart(year,desde) desc ");
        	
			return ConsultaTool.getInstance().getData("portal", sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	return null;
    }
    
    /**
     * Creado desde la integración con el WebMatico
     * @since 6-dic-2016
     * */
    public String GetSaldoVacacionesByRut(String rut) {
    	String saldo = "N/A";
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("select dias_pendientes + dias_del_periodo + dias_progresivos saldo ")
        		.append("from eje_ges_vacaciones_wp WHERE (rut = '").append(rut).append("') ");
        consul.exec(sql.toString());
        if(consul.next()) {
        	saldo = consul.getString("saldo");
        }
        return saldo;
    }
    
    /**
     * Creado desde la integración con el WebMatico
     * @since 19-oct-2017
     * */
    public String GetVacacionesGanadasByRut(String rut) {
    	String saldo = "N/A";
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("select saldo=dias_con_derecho ")
        		.append("from eje_ges_vacaciones_wp WHERE (rut = '").append(rut).append("') ");
        consul.exec(sql.toString());
        if(consul.next()) {
        	saldo = consul.getString("saldo");
        }
        return saldo;
    }
    
    /**
     * Creado desde la integración con el WebMatico
     * @since 19-oct-2017
     * */
    public String GetVacacionesConsumidasByRut(String rut) {
    	String saldo = "N/A";
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("select saldo=dias_con_derecho-(dias_pendientes + dias_del_periodo + dias_progresivos) ")
        		.append("from eje_ges_vacaciones_wp WHERE (rut = '").append(rut).append("') ");
        consul.exec(sql.toString());
        if(consul.next()) {
        	saldo = consul.getString("saldo");
        }
        return saldo;
    }
    	
    /**
     * Creado desde la integración con el WebMatico
     * @since 6-dic-2016
     * */
    public String GetSaldoVacacionesByRut(String rut,String tipo) {
    	String saldo = "N/A";
    	String subquery = "dias_pendientes + dias_del_periodo + dias_progresivos";
        consul = new Consulta(con);
        if(tipo.equals("H")) {
        	subquery = "dias_pendientes + dias_del_periodo";
        }
        else if(tipo.equals("P")) {
        	subquery = "dias_progresivos";
        }
        StringBuilder sql = new StringBuilder("SELECT ").append(subquery).append(" saldo ")
        		.append("from eje_ges_vacaciones_wp WHERE (rut = '").append(rut).append("') ");
        consul.exec(sql.toString());
        if(consul.next()) {
        	saldo = consul.getString("saldo");
        }
        return saldo;
    }

    /**
     * Creado desde la integración con el WebMatico
     * @since 6-dic-2016
     * */
    public String GetPeriodoVacacionesByRut(String rut) {
    	String periodo = "";
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("select distinct top 1 periodo from eje_ges_vacaciones_wp where rut = ").append(rut);
        consul.exec(sql.toString());
        if(consul.next()) {
        	periodo = consul.getString("periodo");
        }
        return periodo;
    }
    
    
    public String getError()
    {
        return mensajeError;
    }

    private Connection con;
    private Consulta consul;
    private String mensajeError;
}