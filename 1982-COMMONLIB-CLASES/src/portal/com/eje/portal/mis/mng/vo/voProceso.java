package portal.com.eje.portal.mis.mng.vo;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.IConsultaDataRow;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.factory.Mng;
import portal.com.eje.portal.mis.mng.MisLocator;
import portal.com.eje.portal.mis.mng.enums.EnumEstado;
import java.sql.Connection;


public class voProceso implements IConsultaDataRow {

	int intProceso;
	int intPeriodo;
	voConexion conexion;
	Date dteFechaProceso;
	EnumEstado enmEstadoProceso;
	Date dteFechaReproceso;
	EnumEstado enmEstadoReproceso;
	List<voModulo> modulos;
	double dblUF;
	double dblUTM;
	double dblTopeIMP;
	
	public voProceso(int intProceso, voConexion conexion) {
		super();
		this.intProceso = intProceso;
		this.conexion = conexion;
	}
	
	public voProceso() {
		load(null, 0, null);
	}

	public voProceso(Connection conn) {
		load(conn, 0, null);
	}

	public voProceso(EnumEstado estado) {
		load(null, 0, estado);
	}

	public voProceso(Connection conn, EnumEstado estado) {
		load(conn, 0, estado);
	}
	
	public voProceso(int intProceso) {
		load(null, intProceso, null);
	}
	
	public voProceso(Connection conn, int intProceso) {
		load(conn, intProceso, null);
	}
	
	boolean load(Connection conn, int intProceso, EnumEstado estado) {

		StringBuilder strSQL = new StringBuilder();
		
		MisLocator mis = Mng.getInstance(MisLocator.class);
		ConsultaTool ctool = Mng.getInstance(ConsultaTool.class);
		Validar vld = Mng.getInstance(Validar.class);

		if (intProceso==0 && estado==null) {
			estado = EnumEstado.Pendiente;
		}
		
		Object[] objParam = new Object[] {};
		ArrayList<Object> params = new ArrayList<Object>(Arrays.asList(objParam));
		
		strSQL.append("select ");
		if (estado!=null) {
			strSQL.append("top 1 ");	
		}
		strSQL.append("p.id_proceso, p.periodo, p.id_persona, fecha=convert(varchar, p.fecha, 120),  p.id_empresa, \n")
		.append("fecha_proceso=convert(varchar, p.fecha_proceso, 120), p.id_estado_proceso, estado_proceso=ep.estado, estado_proceso_icono=ep.icono, \n")
		.append("fecha_reproceso=convert(varchar, p.fecha_reproceso, 120), p.id_estado_reproceso, estado_reproceso=er.estado, estado_reproceso_icono=er.icono, \n")
		.append("valor1=pv1.valor, valor2=pv2.valor, valor3=pv3.valor \n")
		.append("from eje_mis_proceso p \n")
		.append("left join eje_mis_mae_estados ep on ep.id_estado=p.id_estado_proceso \n")
		.append("left join eje_mis_mae_estados er on er.id_estado=p.id_estado_reproceso \n")
		.append("left join eje_mis_periodo_valor pv1 on pv1.periodo=p.periodo and pv1.id_tipovalor=1 \n")
		.append("left join eje_mis_periodo_valor pv2 on pv2.periodo=p.periodo and pv1.id_tipovalor=2 \n")
		.append("left join eje_mis_periodo_valor pv3 on pv3.periodo=p.periodo and pv1.id_tipovalor=3 \n")
		.append("where p.id_proceso<>0 ");
		if(intProceso!=0) {
			strSQL.append("and p.id_proceso=? ");
			params.add(intProceso);
		}else {
			strSQL.append("and vigente=1 ");
		}
		if (estado!=null) {
			if (estado==EnumEstado.Pendiente) {
				strSQL.append("and ((p.id_estado_proceso=? and p.fecha_proceso<=getdate()) or (p.id_estado_reproceso=? and p.fecha_reproceso<=getdate())) ");
			}else{
				strSQL.append("and (p.id_estado_proceso=? or p.id_estado_reproceso=?) ");
			}
			params.add(estado.getId());
			params.add(estado.getId());
		}
		strSQL.append("\n");
		if (estado!=null) {
			strSQL.append("order by p.fecha_proceso, p.fecha_reproceso, id_proceso ");
		}else{
			strSQL.append("order by p.fecha_proceso, p.fecha_reproceso ");
		}

		try {
			ConsultaData data;
			if (conn!=null) {
				data = ctool.getData(conn, strSQL.toString(), params.toArray());
			}else {
				data = ctool.getData("mis", strSQL.toString(), params.toArray());
			}
			while (data!=null && data.next()) {
				String strIdEmpresa = data.getString("id_empresa");
				this.intProceso = data.getInt("id_proceso");
				this.intPeriodo = data.getInt("periodo");
				this.conexion = new voConexion(conn, strIdEmpresa);
				this.dteFechaProceso = vld.validarDate(data.getForcedString("fecha_proceso"), "yyyy-MM-dd HH:mm:ss", null);
				this.enmEstadoProceso = EnumEstado.fromInteger(data.getInt("id_estado_proceso"));
				this.enmEstadoProceso.setNombre(data.getString("estado_proceso"));
				this.enmEstadoProceso.setIcono(data.getString("estado_proceso_icono"));
				this.dteFechaReproceso = vld.validarDate(data.getForcedString("fecha_reproceso"), "yyyy-MM-dd HH:mm:ss", null);
				this.enmEstadoReproceso = EnumEstado.fromInteger(data.getInt("id_estado_reproceso"));
				this.enmEstadoReproceso.setNombre(data.getString("estado_reproceso"));
				this.enmEstadoReproceso.setIcono(data.getString("estado_reproceso_icono"));
				this.modulos = mis.getModulos(conn, this.intProceso);
				this.dblUF = Validar.getInstance().validarDouble(data.getForcedString("valor1"));
				this.dblUTM = Validar.getInstance().validarDouble(data.getForcedString("valor2"));
				this.dblTopeIMP = Validar.getInstance().validarDouble(data.getForcedString("valor3"));
				}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
		
	}
	
	public int getId(){
		return intProceso; 
	}
	public int getPeriodo(){
		return intPeriodo; 
	}
	public String getPeriodoMesAno(){
		String strPeriodo=String.valueOf(intPeriodo);
		return strPeriodo.substring(4, 6)+strPeriodo.substring(0, 4); 
	}
	public int getAno(){
		String strPeriodo=String.valueOf(intPeriodo);
		return Integer.parseInt(strPeriodo.substring(0, 4)); 
	}
	public int getMes(){
		String strPeriodo=String.valueOf(intPeriodo);
		return Integer.parseInt(strPeriodo.substring(4, 6)); 
	}
	public voConexion getConexion(){
		return conexion; 
	}
	public Date getFechaProceso() {
		return dteFechaProceso;
	}
	public EnumEstado getEstadoProceso() {
		return enmEstadoProceso;
	}
	public Date getFechaReproceso() {
		return dteFechaReproceso;
	}
	public EnumEstado getEstadoReproceso() {
		return enmEstadoReproceso;
	}
	public double getUF() {
		return dblUF;
	}
	public double getUTM() {
		return dblUTM;
	}
	public double getTopeIMP() {
		return dblTopeIMP;
	}
	public List<voModulo> getModulos(){
		return modulos;
	}
	
	public void setId(int intProceso) {
		this.intProceso = intProceso;
	}
	
	public void setPeriodo(int intPeriodo) {
		this.intPeriodo = intPeriodo;
	}
	
	public void setFechaProceso(Date dteFechaProceso) {
		this.dteFechaProceso = dteFechaProceso;
	}
	
	public void setEstadoProceso(EnumEstado enmEstadoProceso) {
		this.enmEstadoProceso = enmEstadoProceso;
	}
	
	public void setFechaReproceso(Date dteFechaReproceso) {
		this.dteFechaReproceso = dteFechaReproceso;
	}

	public void setEstadoReproceso(EnumEstado enmEstadoReproceso) {
		this.enmEstadoReproceso = enmEstadoReproceso;
	}

	public void setModulos(List<voModulo> modulos) {
		this.modulos = modulos;
	}
	
	@Override
	public DataFields toDataField() {

		DataFields data = new DataFields();
		
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String strModulos = "";
		for (voModulo modulo: this.modulos) {
			if (!"".equals(strModulos)) {
				strModulos +=", ";
			}
			strModulos +=modulo.getNombre();
		}
		
		data.put("id_proceso", this.intProceso);
		data.put("id_periodo", this.intPeriodo);
		data.put("id_empresa", this.conexion.getEmpresa().getId());
		data.put("empresa", this.conexion.getEmpresa().getNombre());
		data.put("fecha_proceso", sdfDate.format(this.dteFechaProceso));
		data.put("id_estado_proceso", this.enmEstadoProceso.getId());
		data.put("estado_proceso", this.enmEstadoProceso.getNombre());
		data.put("estado_proceso_icono", this.enmEstadoProceso.getIcono());
		if (this.dteFechaReproceso!=null) {
			data.put("fecha_reproceso", sdfDate.format(this.dteFechaReproceso));
		}else{
			data.put("fecha_reproceso", null);
		}
		data.put("id_estado_reproceso", this.enmEstadoReproceso.getId());
		data.put("estado_reproceso", this.enmEstadoReproceso.getNombre());
		data.put("estado_reproceso_icono", this.enmEstadoReproceso.getIcono());
		data.put("modulos", strModulos);
		
		return data;
		
	}

}
