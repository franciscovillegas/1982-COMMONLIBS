package portal.com.eje.portal.mis.mng;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.factory.Mng;
import portal.com.eje.portal.mis.mng.enums.EnumEstado;
import portal.com.eje.portal.mis.mng.vo.voEmpresa;
import portal.com.eje.portal.mis.mng.vo.voModulo;
import portal.com.eje.portal.mis.mng.vo.voProceso;
import portal.com.eje.portal.mis.mng.vo.voTipoValor;

public class MisLocator implements IMisMng {

	private static IMisMng instance;
	ConsultaTool ctool = Mng.getInstance(ConsultaTool.class);
	Validar vld = Mng.getInstance(Validar.class);
	
	public static IMisMng getInstance() {
		if(instance == null) {
			synchronized (MisLocator.class) {
				if(instance == null) {
					instance = new MisLocator();
				}
			}
		}
		return instance;
	}
	
	@Override
	public List<voEmpresa> getEmpresa() {
		return getEmpresa(0);
	}
	
	@Override
	public List<voEmpresa> getEmpresa(int intIdPersona) {
		
		StringBuilder strSQL = new StringBuilder();
		
		List<voEmpresa> empresas = new ArrayList<voEmpresa>();
		
		strSQL.append("select c.id_empresa \n")
		.append("from eje_mis_conexion c \n");
		if (intIdPersona!=0) {
			strSQL.append("inner join eje_mis_grupo_empresa ge on ge.id_empresa=c.id_empresa \n")
			.append("inner join eje_mis_grupo_persona gp on gp.id_grupo=ge.id_grupo \n")
			.append("where gp.id_persona=").append(intIdPersona).append(" \n");			
		}

		try {
			ConsultaData data = ctool.getData("mis", strSQL.toString());
			while (data!=null && data.next()) {
				empresas.add(new voEmpresa(data.getString("id_empresa")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return empresas;	
		
	}


	@Override
	public List<voEmpresa> getEmpresaPortal() {
		
		StringBuilder strSQL = new StringBuilder();
		
		List<voEmpresa> empresas = new ArrayList<voEmpresa>();
		
		strSQL.append("select id_empresa=empresa from eje_ges_empresa order by descip");
		try {
			ConsultaData data = ctool.getData("portal", strSQL.toString());
			while (data!=null && data.next()) {
				empresas.add(new voEmpresa(data.getString("id_empresa")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return empresas;
	}

	@Override
	public List<voModulo> getModulos(String strIdEmpresa) {
		return getModulos(null, strIdEmpresa);
	}
	
	@Override
	public List<voModulo> getModulos(Connection conn, String strIdEmpresa) {

		StringBuilder strSQL = new StringBuilder();
		
		List<voModulo> modulos = new ArrayList<voModulo>();
		
		strSQL.append("select m.id_modulo, m.modulo \n")
		.append("from eje_mis_modulo m \n")
		.append("inner join eje_mis_grupo_modulo gm on gm.id_modulo=m.id_modulo \n")
		.append("inner join eje_mis_grupo_empresa ge on ge.id_grupo=gm.id_grupo \n")
		.append("where ge.id_empresa=? \n")
		.append("order by m.modulo ");

		Object[] params = {strIdEmpresa};

		try {
			ConsultaData data;
			if (conn!=null) {
				data = ctool.getData(conn, strSQL.toString(), params);
			}else{
				data = ctool.getData("mis", strSQL.toString(), params);
			}
			while (data!=null && data.next()) {
				modulos.add(new voModulo(data.getInt("id_modulo"), data.getString("modulo")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return modulos;
		
	}

	@Override
	public List<voModulo> getModulos(int intIdProceso) {
		return getModulos(null, intIdProceso);
	}
	
	@Override
	public List<voModulo> getModulos(Connection conn, int intIdProceso) {
		
		StringBuilder strSQL = new StringBuilder();
		
		List<voModulo> modulos = new ArrayList<voModulo>();
		
		strSQL.append("select m.id_modulo, m.modulo \n")
		.append("from eje_mis_modulo m \n")
		.append("inner join eje_mis_proceso_modulo pm on pm.id_modulo=m.id_modulo \n")
		.append("where pm.id_proceso=? \n")
		.append("order by m.modulo ");
		
		Object[] params = {intIdProceso};

		try {
			ConsultaData data;
			if (conn!=null) {
				data = ctool.getData(conn, strSQL.toString(), params);
			}else{
				data = ctool.getData("mis", strSQL.toString(), params);
			}
			while (data!=null && data.next()) {
				modulos.add(new voModulo(data.getInt("id_modulo"), data.getString("modulo")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return modulos;
	}
	
	@Override
	public List<voProceso> getProcesos(int intPeriodo) {

		StringBuffer strSQL = new StringBuffer();
		
		List<voProceso> procesos = new ArrayList<voProceso>();
		
		strSQL.append("select id_proceso \n")
		.append("from eje_mis_proceso \n")
		.append("where vigente=1 and periodo=").append(intPeriodo).append(" \n")
		.append("order by fecha_proceso, fecha_reproceso, id_proceso \n");
		try {
			ConsultaData data = ctool.getData("mis", strSQL.toString());
			while (data!=null && data.next()) {
				procesos.add(new voProceso(data.getInt("id_proceso")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return procesos;
		
	}

	@Override
	public EnumEstado getEstadoProceso(voProceso proceso) {
		return getEstadoProceso(null, proceso);
	}
	
	@Override
	public EnumEstado getEstadoProceso(Connection conn, voProceso proceso) {
		return getEstadoProceso(conn, proceso, "proceso");
	}

	@Override
	public EnumEstado getEstadoReproceso(voProceso proceso) {
		return getEstadoReproceso(null, proceso);
	}
	
	@Override
	public EnumEstado getEstadoReproceso(Connection conn, voProceso proceso) {
		return getEstadoProceso(conn, proceso, "reproceso");
	}
	
	private EnumEstado getEstadoProceso(Connection conn, voProceso proceso, String strTipo) {
		
		StringBuffer strSQL = new StringBuffer();
		
		EnumEstado estado;
		
		if ("proceso".equals(strTipo)) {
			estado = proceso.getEstadoProceso();
		}else{
			estado = proceso.getEstadoReproceso();
		}

		strSQL.append("select id_estado=p.id_estado_").append(strTipo).append(", estado=e.estado, estado_icono=e.icono \n")
		.append("from eje_mis_proceso p \n")
		.append("left join eje_mis_mae_estados e on e.id_estado=p.id_estado_").append(strTipo).append(" \n")
		.append("where p.id_proceso=").append(proceso.getId()).append(" \n");
		
		try {
			ConsultaData data = null;
			if (conn!=null) {
				data = ctool.getData(conn, strSQL.toString());
			}else{
				data = ctool.getData("mis", strSQL.toString());
			}
			while(data!=null && data.next()) {
				estado.setNombre(data.getString("estado"));
				estado.setIcono(data.getString("estado_icono"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return estado;
		
	}
	
	@Override
	public List<voTipoValor> getTipoValores(int intPeriodo) {
		return getTipoValores(null, intPeriodo);
	}

	@Override
	public List<voTipoValor> getTipoValores(Connection conn, int intPeriodo) {

		StringBuffer strSQL = new StringBuffer();
		
		List<voTipoValor> tiposvalores = new ArrayList<voTipoValor>();
		
		strSQL.append("select tv.id_tipovalor, tv.tipovalor, tv.descripcion, pv.valor \n")
		.append("from eje_mis_mae_tipovalor tv \n")
		.append("left join eje_mis_periodo_valor pv on pv.periodo=? and pv.id_tipovalor=tv.id_tipovalor \n")
		.append("order by tv.tipovalor \n");
		
		Object[] params = {intPeriodo}; 
		try {
			ConsultaData data = null;
			if (conn!=null) {
				data = ctool.getData(conn, strSQL.toString(), params);
			}else{
				data = ctool.getData("mis", strSQL.toString(), params);
			}
			while(data!=null && data.next()) {
				Double dblValor = vld.validarDouble(data.getForcedString("valor"));
				if (dblValor==0) {
					dblValor=null;
				}
				tiposvalores.add(
						new voTipoValor(
								vld.validarInt(data.getForcedString("id_tipovalor")), 
								data.getString("tipovalor"),
								data.getString("descripcion"),
								dblValor
							)
						);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tiposvalores;
	}

	
	@Override
	public boolean setEstadoProceso(Connection conn, voProceso proceso) {
		
		StringBuffer strSQL = new StringBuffer();
		
		boolean ok = false;
		
		strSQL.append("update eje_mis_proceso set id_estado_proceso=?, id_estado_reproceso=? where id_proceso=?");
		Object[] params = {proceso.getEstadoProceso().getId(), proceso.getEstadoReproceso().getId(), proceso.getId()};
		try {
			if (conn!=null) {
				ctool.update(conn, strSQL.toString(), params);
			}else{
				ctool.update("mis", strSQL.toString(), params);
			}
			ok = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ok;
	}

	
	@Override
	public boolean updProceso(Connection conn, int intIdUsuario, voProceso proceso) {

		StringBuffer strSQL = new StringBuffer();

		boolean ok = false;
		
		Object[] objParam = new Object[] {};
		ArrayList<Object> params = new ArrayList<Object>(Arrays.asList(objParam));
	
		params.add(proceso.getId());
		params.add(proceso.getPeriodo());
		params.add(intIdUsuario);
		params.add(proceso.getConexion().getEmpresa().getId());
		params.add(proceso.getFechaProceso());
		params.add(proceso.getEstadoProceso().getId());
		params.add(proceso.getFechaReproceso());
		params.add(proceso.getEstadoReproceso().getId());

		try {
			strSQL.append("declare @id_proceso int \n")
			.append("set @id_proceso=? \n");
			if (proceso.getId()==0) {
				strSQL.append("insert into eje_mis_proceso (periodo, id_persona, fecha, id_empresa, fecha_proceso, id_estado_proceso, fecha_reproceso, id_estado_reproceso) \n")
				.append("values (?, ?, getdate(), ?, ?, ?, ?, ?) \n")
				.append("select id_proceso=@@identity \n");
				ConsultaData data = ctool.getData(conn, strSQL.toString(), params.toArray());
				while (data!=null && data.next()) {
					proceso.setId(vld.validarInt(data.getForcedString("id_proceso")));
				}
			}else {
				strSQL.append("update eje_mis_proceso set periodo=?, id_persona=?, fecha=getdate(), fecha_proceso=?, id_estado_proceso=?, fecha_reproceso=?, id_estado_reproceso=? \n")
				.append("where id_proceso=@id_proceso \n");
				if (conn!=null) {
					ctool.update(conn, strSQL.toString(), params.toArray());
				}else {
					ctool.update("mis", strSQL.toString(), params.toArray());
				}
			}
			
			proceso.setEstadoProceso(getEstadoProceso(conn, proceso));
			proceso.setEstadoReproceso(getEstadoReproceso(conn, proceso));
			
			ok = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ok;
		
	}

	@Override
	public boolean updProcesoModulo(Connection conn, voProceso proceso) {

		StringBuffer strSQL = new StringBuffer();
		
		boolean ok = true;

		strSQL.append("declare @id_proceso int \n")
		.append("set @id_proceso=").append(proceso.getId()).append(" \n\n")
		
		.append("declare @data table (id_proceso int, id_modulo int) \n\n");
		for (voModulo modulo: proceso.getModulos()) {
			strSQL.append("insert into @data values (")
			.append(proceso.getId()).append(",").append(modulo.getId())
			.append(") \n");
		}
		
		strSQL.append("\n")
		.append("delete pm ")
		.append("from eje_mis_proceso_modulo pm ")
		.append("left join @data d on d.id_proceso=pm.id_proceso and d.id_modulo=pm.id_modulo \n")
		.append("where pm.id_proceso=@id_proceso and d.id_modulo is null \n\n")
		
		.append("insert into eje_mis_proceso_modulo (id_proceso, id_modulo) \n")
		.append("select d.id_proceso, d.id_modulo \n")
		.append("from @data d \n")
		.append("left join eje_mis_proceso_modulo pm on d.id_proceso=pm.id_proceso and d.id_modulo=pm.id_modulo \n")
		.append("where pm.id_modulo is null \n");
		
		try {
			if (conn!=null) {
				ctool.execute(conn, strSQL.toString());
			}else{
				ctool.execute("mis", strSQL.toString());
			}
			ok = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ok;
		
	}

	@Override                                                                       
	public boolean updTipoValores(int intPeriodo, List<voTipoValor> tipovalores) {
		return updTipoValores(null, intPeriodo, tipovalores); 
	}
	@Override
	public boolean updTipoValores(Connection conn, int intPeriodo, List<voTipoValor> tipovalores) {

		StringBuffer strSQL = new StringBuffer();
		
		boolean ok = false;

		strSQL.append("declare @periodo int \n")
		.append("set @periodo=").append(intPeriodo).append(" \n\n")
		
		.append("declare @data table (periodo int, id_tipovalor int, valor decimal(18, 9)) \n\n");
		for (voTipoValor tipovalor: tipovalores) {
			strSQL.append("insert into @data values (")
			.append("@periodo,").append(tipovalor.getId()).append(",").append(tipovalor.getValor())
			.append(") \n");
		}
		
		strSQL.append("\n")
		.append("update pv set valor=d.valor \n")
		.append("from eje_mis_periodo_valor pv \n")
		.append("inner join @data d on d.periodo=pv.periodo and d.id_tipovalor=pv.id_tipovalor \n\n")
		
		.append("insert into eje_mis_periodo_valor (periodo, id_tipovalor, valor) \n")
		.append("select d.periodo, d.id_tipovalor, d.valor \n")
		.append("from @data d \n")
		.append("left join eje_mis_periodo_valor pv on d.periodo=pv.periodo and d.id_tipovalor=pv.id_tipovalor \n")
		.append("where pv.id_tipovalor is null \n");
		
		try {
			if (conn!=null) {
				ctool.execute(conn, strSQL.toString());
			}else{
				ctool.execute("mis", strSQL.toString());
			}
			ok = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ok;
		
	}
	
	@Override
	public boolean delProcesos(List<voProceso> procesos) {
		return delProcesos(null, procesos);
	}

	@Override
	public boolean delProcesos(Connection conn, List<voProceso> procesos) {

		boolean ok = false;

		try {
			for (voProceso proceso: procesos) {
				StringBuffer strSQL = new StringBuffer();
				strSQL.append("update eje_mis_proceso set vigente=0 where id_proceso=? \n");
				Object[] params = {proceso.getId()};
				if (conn!=null) {
					ctool.update(conn, strSQL.toString(), params);
				}else{
					ctool.update("mis", strSQL.toString(), params);
				}
			}
			ok = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ok;
		
	}

}
