package portal.com.eje.liquidadic;

import java.sql.SQLException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;

public class ManagerLiqAdicional {
	private static ManagerLiqAdicional instance;
	
	private ManagerLiqAdicional() {
	}
	
	public static ManagerLiqAdicional getInstance() {
		if(instance == null) {
			synchronized (ManagerLiqAdicional.class) {
				if(instance == null) {
					instance = new ManagerLiqAdicional();
				}
			}
		}
		
		return instance;
	}
	
	public ConsultaData getAllProcesos() {
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select distinct m.tipo_proceso from ");
		sql.append(" (select tipo_proceso from eje_ges_liq_adic_conf ");
		sql.append(" union ");
		sql.append(" select distinct tipo_proceso from eje_ges_certif_histo_liquidacion_cabecera_adic) as m ");
		sql.append(" WHERE not m.tipo_proceso in (select d.tipo_proceso from eje_ges_liq_adic_conf d where d.activado = 1 ) ");
		sql.append(" order by m.tipo_proceso ");
		
		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
	}
	
	public ConsultaData getAllProcesosSeleccionados() {
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select d.tipo_proceso from eje_ges_liq_adic_conf d where d.activado = 1");
		sql.append(" order by d.tipo_proceso ");
		
		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	public boolean setEstadoAllProcesos(boolean  estado) {
		StringBuilder sql = new StringBuilder();
		
		sql.append(" UPDATE eje_ges_liq_adic_conf set activado =  ? , fecha_modificacion = getdate() ");
		Object[] params = { estado };

		try {
			return ConsultaTool.getInstance().update("portal", sql.toString(), params) > 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean setEstadoUnProceso(String proceso, boolean  estado) {
		StringBuilder sql = new StringBuilder();
		
		sql.append(" UPDATE eje_ges_liq_adic_conf set activado =  ?,fecha_modificacion = getdate() WHERE tipo_proceso = ?");
		Object[] params = { estado , proceso};

		try {
			return ConsultaTool.getInstance().update("portal", sql.toString(), params) > 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public ConsultaData getProceso(String proceso) {
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT tipo_proceso, nombre, fecha_modificacion, activado FROM  eje_ges_liq_adic_conf WHERE tipo_proceso = ? ");
		Object[] params = { proceso };
		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
	}
	
	public boolean insertProceso(String proceso) {
		StringBuilder sql = new StringBuilder();
		
		sql.append(" INSERT INTO eje_ges_liq_adic_conf (tipo_proceso, nombre, fecha_modificacion, activado) ");
		sql.append(" VALUES (?,?,getdate(),?) ");
		
		Object[] params = { proceso, proceso, "1"};

		try {
			return ConsultaTool.getInstance().insert("portal", sql.toString(), params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
    public boolean existeTablaLiquidacionAdicional() {
		String sql = "select * from dbo.sysobjects where id = object_id(N'[dbo].[eje_ges_liq_adic_conf]') and OBJECTPROPERTY(id, N'IsUserTable') = 1";

		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data.size() > 0;
	}

	public boolean crearTablasNecesarias() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("   CREATE TABLE [dbo].[eje_ges_liq_adic_conf] (   ");
		sql.append("  		[tipo_proceso] [varchar] (50) COLLATE Modern_Spanish_CI_AS NOT NULL , ");
		sql.append("  		[nombre] [varchar] (100) COLLATE Modern_Spanish_CI_AS NULL , ");
		sql.append("  		[fecha_modificacion] [datetime] NULL , ");
		sql.append("  		[activado] [bit] NULL  ");
		sql.append("  ) ON [PRIMARY] ");

		try {
			return ConsultaTool.getInstance().insert("portal", sql.toString()) ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
		
	}
	
}
