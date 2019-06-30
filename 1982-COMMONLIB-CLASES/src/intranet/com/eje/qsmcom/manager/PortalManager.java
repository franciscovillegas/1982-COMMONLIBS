package intranet.com.eje.qsmcom.manager;


import freemarker.template.SimpleList;
import intranet.com.eje.qsmcom.estructuras.DetalleComisiones;
import intranet.com.eje.qsmcom.estructuras.Trabajador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import portal.com.eje.datos.Managers;
import portal.com.eje.tools.ArrayFactory;

public class PortalManager extends Managers {
	public static Permiso PUEDE_LOGEAR = new Permiso("cexterno");
	public static Permiso PUEDE_ENTRAR_PORTAL = new Permiso("sh");
	public static Permiso PUEDE_ACCEDER_SISTEMA_SQM = new Permiso("qsmcom_eje");
	
	public PortalManager(Connection conex) {
		super(conex);
		// TODO Auto-generated constructor stub
	}
	
	public boolean addPermiso(int rut, Permiso permiso) {
		boolean ok = true;
		
		String sql = " if(not exists(select 1 from eje_ges_user_app where app_id = ? and rut_usuario = ? )) ";
		sql += " insert into eje_ges_user_app(app_id, rut_usuario, vigente, wp_cod_empresa , wp_cod_planta) values (?,?,null,-1,-1) "; 
		
		try {
			PreparedStatement pst =  super.conexion.prepareStatement(sql);
			pst.setString(1, permiso.getIdPermiso());
			pst.setInt(2, rut );
			pst.setString(3, permiso.getIdPermiso());
			pst.setInt(4, rut );
			
			pst.executeUpdate();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
			ok = false;
		}
		
		return ok;
	}
	
	public boolean delPermiso(int rut, Permiso permiso) {
		boolean ok = true;
		
		String sql = " delete from eje_ges_user_app where app_id = ? and rut_usuario = ? "; 
		
		try {
			PreparedStatement pst =  super.conexion.prepareStatement(sql);
			pst.setString(1, permiso.getIdPermiso());
			pst.setInt(2, rut );
			pst.executeUpdate();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
			ok = false;
		}
		
		return ok;
	}
	
	
	public boolean addUsuario(int rut, String password) {
		boolean ok = true;
		
		String sql = " if(not exists(select 1 from eje_ges_usuario where rut_usuario = ? )) ";
			sql += "	insert into eje_ges_usuario (login_usuario, password_usuario, rut_usuario, tipo_rel , wp_cod_empresa, wp_cod_planta , md5) ";
			sql += "	values (?,?,?,'U',-1,-1,'N') "; 
		
		try {
			PreparedStatement pst =  super.conexion.prepareStatement(sql);
			pst.setInt(1, rut );
			pst.setInt(2, rut );
			pst.setString(3, password);
			pst.setInt(4, rut );
			
			pst.executeUpdate();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
			ok = false;
		}
		
		return ok;
	}
	
	public boolean delUsuario(int rut) {
		boolean ok = true;
		
		String sql = " delete from eje_ges_usuario where rut_usuario = ? "; 
		
		try {
			PreparedStatement pst =  super.conexion.prepareStatement(sql);
			pst.setInt(1, rut );
			
			pst.executeUpdate();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
			ok = false;
		}
		
		return ok;
	}
	
	
	public boolean updClaveTrabajador(int rut,String newClave) {
		boolean ok = true;
		
		String sql = " update eje_ges_usuario set password_usuario = ? where rut_usuario = ? "; 
		
		try {
			PreparedStatement pst =  super.conexion.prepareStatement(sql);
			pst.setString(1, newClave );
			pst.setInt(2, rut );
			
			pst.executeUpdate();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
			ok = false;
		}
		
		return ok;
	}
	
	
	public ResultSet getCiudades() {
		boolean ok = true;
		
		String sql = " select ciudad, descrip from eje_ges_ciudades order by descrip "; 
		ResultSet rs = null;
		
		try {
			PreparedStatement pst =  super.conexion.prepareStatement(sql);
			rs = pst.executeQuery();
			pst.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			ok = false;
		}
		
		return rs;
	}
	
	
	public float getValorUF() {
		float valor = 0.0F;
		
		String sql = " select top 1 val_uf from eje_ges_certif_histo_liquidacion_cabecera order by periodo desc "; 
		ResultSet rs = null;
		
		try {
			PreparedStatement pst =  super.conexion.prepareStatement(sql);
			rs = pst.executeQuery();
			
			
			if(rs.next()) {
				valor = rs.getFloat("val_uf");
			}
			rs.close();
			pst.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return valor;
	}
	
	public HashMap getTrabajadoresSubidos() throws Exception {
		return getTrabajadores(-1);
	}
	
	public Trabajador getTrabajadorSubido(int rut) throws Exception {
		HashMap res = getTrabajadores(rut);
		
		if(res.size() == 0) {
			return null;
		}
		else {
			Set set = res.keySet();
			Iterator ite = set.iterator();
			
			if(ite.hasNext()) {
				String rutStr = (String) ite.next();
				return (Trabajador) res.get(rutStr);
			}
			
			return null;
		}
		
	}
	
	private HashMap getTrabajadores(int rut) throws Exception {
		HashMap map = new HashMap();
		Trabajador t = null;
		String sql = "SELECT ";
		if(rut != -1) {
			sql += " top 1 ";
		}
		
		sql += " rut, digito_ver , nombre ,e_mail as email,domicilio as direccion , ";
		sql += " telefono , banco , cta_cte as cuenta, 'cta_cte' as tipo_cuenta ";
		sql += " FROM eje_ges_trabajador ";
		if(rut != -1) {
			sql += " WHERE rut = ? ";
		}
		
		try {
			PreparedStatement pst =  super.conexion.prepareStatement(sql);
			if(rut != -1) {
				pst.setInt(1, rut);
			}
			
			ResultSet rs = pst.executeQuery();
			
			while(rs.next()) {
				 String rutStr		= rs.getString("rut");
				 String rutdig    	= rutStr + rs.getString("digito_ver");
				 String nombre 		= rs.getString("nombre");
				 String email  		= rs.getString("email");
				 String direccion	= rs.getString("direccion");
				 String telefono 	= rs.getString("telefono");
				 String tipoCuenta  = rs.getString("tipo_cuenta");
				 ArrayList roles    = null;
				 t = new Trabajador(rutdig, nombre, email, direccion, telefono, roles);
				 t.setBanco(rs.getString("banco"));
				 t.setCuenta(rs.getString("cuenta"));
				 t.setTipoCuenta(tipoCuenta);
				 
				 map.put(rutStr, t);
			}
			rs.close();
			pst.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return map;
	}
	
	public ArrayList getUnidadesJefatura(int rut) {
		ArrayList array = new ArrayList();

		try {
			
			StringBuffer sql = new StringBuffer("SELECT distinct unid_id ");
			sql.append(" FROM eje_ges_unidad_encargado ");
			sql.append(" WHERE periodo in (select max(periodo) from eje_ges_unidad_encargado) ");
			sql.append(" 	   and rut_encargado = ? "); 
			
			PreparedStatement pst = conexion.prepareStatement(sql.toString());
			pst.setInt(1, rut);
			
			ResultSet rs = pst.executeQuery();
			
			while(rs.next()) {
				array.add(rs.getString("unid_id"));
			}
			rs.close();
			pst.close();
		}
		catch (Exception e) {
			System.out.println("<-----No se pudo saber si es supervisor del detalle de comisiones \n" + e);
		}
		
		return array;
	}
	
	

	
	
	public ArrayFactory getRutsDescendientes(int rut) {
		ArrayFactory array = new ArrayFactory();
		ArrayList jefa  = getUnidadesJefatura(rut);
		
		if(jefa.size() == 0) {
			array.add(rut);
		} else {
			try {
				
				StringBuffer sql = new StringBuffer();
				
				for(int i = 0; i < jefa.size() ; i++) {
					sql.append("SELECT distinct rut \n");
					sql.append(" FROM eje_ges_trabajador \n");
					sql.append(" WHERE unidad in (select unidad from dbo.getdescendientes('"+jefa.get(i)+"') )\n");
					
					if((i + 1) < jefa.size() ){
						sql.append(" union \n");
					}
				}
				
				String sqlFinal = "SELECT DISTINCT m.rut from ("+sql.toString()+") as m";
				
				
				PreparedStatement pst = conexion.prepareStatement(sqlFinal);
				
				ResultSet rs = pst.executeQuery();
				
				while(rs.next()) {
					array.add(rs.getString("rut"));
				}
				rs.close();
				pst.close();
			}
			catch (Exception e) {
				System.out.println("<-----No se pudo saber si es supervisor del detalle de comisiones \n" + e);
			}
		}
			
		return array;
	}
	
}