package portal.com.eje.portal.mis.mng;

import java.sql.Connection;
import java.util.List;

import portal.com.eje.portal.mis.mng.enums.EnumEstado;
import portal.com.eje.portal.mis.mng.vo.voEmpresa;
import portal.com.eje.portal.mis.mng.vo.voModulo;
import portal.com.eje.portal.mis.mng.vo.voProceso;
import portal.com.eje.portal.mis.mng.vo.voTipoValor;

public interface IMisMng {
	
	public List<voEmpresa> getEmpresa();
	
	public List<voEmpresa> getEmpresa(int intIdPersona);
	
	public List<voEmpresa> getEmpresaPortal();
	
	public List<voModulo> getModulos(String strIdEmpresa);
	
	public List<voModulo> getModulos(Connection conn, String strIdEmpresa);
	
	public List<voModulo> getModulos(int intIdProceso);
	
	public List<voModulo> getModulos(Connection conn, int intIdProceso);
	
	public List<voProceso> getProcesos(int intPeriodo);
	
	public EnumEstado getEstadoProceso(voProceso proceso);
	
	public EnumEstado getEstadoProceso(Connection conn, voProceso proceso);
	
	public EnumEstado getEstadoReproceso(voProceso proceso);
	
	public EnumEstado getEstadoReproceso(Connection conn, voProceso proceso);
	
	public List<voTipoValor> getTipoValores(int intPeriodo);
	
	public List<voTipoValor> getTipoValores(Connection conn, int intPeriodo);
	
	public boolean setEstadoProceso(Connection conn, voProceso proceso);
	
	public boolean updProceso(Connection conn, int intIdUsuario, voProceso proceso); 

	public boolean updProcesoModulo(Connection conn, voProceso proceso); 
	
	public boolean updTipoValores(int intPeriodo, List<voTipoValor> tipovalores);
	
	public boolean updTipoValores(Connection conn, int intPeriodo, List<voTipoValor> tipovalores);
	
	public boolean delProcesos(List<voProceso> procesos);
	
	public boolean delProcesos(Connection conn, List<voProceso> procesos);
	
}
