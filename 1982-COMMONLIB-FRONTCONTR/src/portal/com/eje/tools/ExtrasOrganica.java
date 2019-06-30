package portal.com.eje.tools;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.datos.Consulta;

public class ExtrasOrganica {
	
	public static boolean isEncargadoUnidad (int rut) {
		return isEncargadoUnidad(String.valueOf(rut), null);
	}
	
	public static boolean isEncargadoUnidad (int rut, Connection conn) {
		return isEncargadoUnidad(String.valueOf(rut), conn);
	}
	
	public static boolean isEncargadoUnidad (String rut, Connection conn) {
		StringBuilder sql = new StringBuilder("select * from eje_ges_unidad_encargado where rut_encargado=").append(rut).append(" and estado = 1");
		ConsultaData data = null;
		
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString());
			if(data.next()) {
				
					return true;

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static boolean isUserGed (String rut, Connection conn) {
		Consulta consulta = new Consulta(conn);
		StringBuilder sql = new StringBuilder("select count(app_id) as userged from eje_ges_user_app where app_id='userged' and rut_usuario=" + rut);
		consulta.exec(String.valueOf(sql)); consulta.next();
		String usuario = consulta.getString("userged");
		if(usuario.equals("0")) {
			return false;
		}
		else {
			return true;
		}
	}

	public static boolean isAdministradorCorporativo (String rut, Connection conn) {
		Consulta consulta = new Consulta(conn);
		StringBuilder sql = new StringBuilder("select count(app_id) as adm_corp from eje_ges_user_app where app_id='adm_corp' and rut_usuario=" + rut);
		consulta.exec(String.valueOf(sql)); consulta.next();
		String usuario = consulta.getString("adm_corp");
		if(usuario.equals("0")) {
			return false;
		}
		else {
			return true;
		}
	}

	public static boolean isAdministrador (String rut, Connection conn) {
		Consulta consulta = new Consulta(conn);
		StringBuilder sql = new StringBuilder("Select count(app_id) as adm from eje_ges_user_app where app_id='adm' and rut_usuario=" + rut);
		consulta.exec(String.valueOf(sql)); consulta.next();
		String usuario = consulta.getString("adm");
		if(!usuario.equals("2")) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public static boolean UnidadTieneEncargado (String unidad, String empresa, Connection conn) {
		Consulta consulta = new Consulta(conn);
		StringBuilder sql = new StringBuilder("select distinct count(rut_encargado) as rut from eje_ges_unidad_encargado where unid_id= '" + unidad + "' and unid_empresa=" + empresa);
		consulta.exec(String.valueOf(sql)); consulta.next();
		String rut = consulta.getString("rut");
		if(rut.equals("0")) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public static boolean UnidadTieneEncargadov2 (String unidad, Connection conn) {
		Consulta consulta = new Consulta(conn);
		StringBuilder sql = new StringBuilder("select distinct count(rut_encargado) as rut from eje_ges_unidad_encargado where unid_id= '" + unidad + "'");
		consulta.exec(String.valueOf(sql)); consulta.next();
		String rut = consulta.getString("rut");
		if(rut.equals("0")) {
			return false;
		}
		else {
			return true;
		}
	}	
	
	public static String UnidadEncargado (String unidad, String empresa, Connection conn) {
		Consulta consulta = new Consulta(conn);
		StringBuilder sql = new StringBuilder("select distinct rut_encargado from eje_ges_unidad_encargado where unid_id= '" + unidad + "' and unid_empresa=" + empresa + " and estado = 1") ;
		consulta.exec(String.valueOf(sql)); consulta.next();
		String rut_encargado = consulta.getString("rut_encargado");
		return rut_encargado;
	}
	
	public static String NombreTrabajador (String rut, Connection conn) {
		Consulta consulta = new Consulta(conn);
		StringBuilder sql = new StringBuilder("select distinct nombre from eje_ges_trabajador where rut=" + rut + "") ;
		consulta.exec(String.valueOf(sql)); consulta.next();
		String rut_encargado = consulta.getString("nombre");
		return rut_encargado;
	}
	
	public static String UnidadEncargadov2 (String unidad, Connection conn) {
		Consulta consulta = new Consulta(conn);
		StringBuilder sql = new StringBuilder("select distinct rut_encargado from eje_ges_unidad_encargado where unid_id='" + unidad + "' and estado = 1") ;
		consulta.exec(String.valueOf(sql)); consulta.next();
		String rut_encargado = consulta.getString("rut_encargado");
		return rut_encargado;
	}
	
	public static String UnidadPadreEncargado (String unidad, String empresa, Connection conn) {
		Consulta consulta = new Consulta(conn);
		StringBuilder sql = new StringBuilder("select nodo_padre from eje_ges_jerarquia where nodo_id= '" + unidad + "' and compania=" + empresa);
		consulta.exec(String.valueOf(sql)); consulta.next();
		String unidadPadre = consulta.getString("nodo_padre");
		return unidadPadre;
	}

	public static String DigVerEncargadoUnidad (String rut, Connection conn) {
		Consulta consulta = new Consulta(conn);
		StringBuilder sql = new StringBuilder("select distinct digito_ver from eje_ges_trabajador where rut=" + rut);
		consulta.exec(String.valueOf(sql)); consulta.next();
		String digver = consulta.getString("digito_ver");
		return digver;
	}

	public static String CargoTrabajador (String rut, Connection conn) {
		Consulta consulta = new Consulta(conn);
		StringBuilder sql = new StringBuilder("select c.descrip from eje_ges_trabajador t inner join eje_ges_cargos c on t.cargo=c.cargo and t.wp_cod_empresa=c.empresa where c.vigente='S' and t.rut=").append(rut);
		consulta.exec(String.valueOf(sql)); consulta.next();
		String nombre = consulta.getString("descrip");
		return nombre;
	}
	
	public static String NombreEncargadoUnidad (String rut, Connection conn) {
		Consulta consulta = new Consulta(conn);
		StringBuilder sql = new StringBuilder("select distinct nombre from eje_ges_trabajador where rut=" + rut);
		consulta.exec(String.valueOf(sql)); consulta.next();
		String nombre = consulta.getString("nombre");
		return nombre;
	}
	
	public static String UnidadTrabajador (String rut, Connection conn) {
		Consulta consulta = new Consulta(conn);
		StringBuilder sql = new StringBuilder("select unidad from eje_ges_trabajador where rut=" + rut);
		consulta.exec(String.valueOf(sql)); consulta.next();
		String unidad = consulta.getString("unidad");
		return unidad;
	}
	
	public static String UnidadDesc (String unidad, Connection conn) {
		Consulta consulta = new Consulta(conn);
		StringBuilder sql = new StringBuilder("select unid_desc from eje_ges_unidades where unid_id= '" + unidad +"'");
		consulta.exec(String.valueOf(sql)); consulta.next();
		String descunidad = consulta.getString("unid_desc");
		return descunidad;
	}

	public static String EmpresaTrabajador (String rut, Connection conn) {
		Consulta consulta = new Consulta(conn);
		StringBuilder sql = new StringBuilder("select empresa from eje_ges_trabajador where rut=" + rut);		
		consulta.exec(String.valueOf(sql)); consulta.next();
		String empresa = consulta.getString("empresa");
		System.out.println("RUT: " + rut + "..." + "LA EMPRESA-->  " + empresa);
		return empresa;
	}
	
	public static List NodosHijos(String unidad, String empresa, Connection conn) {
		List lista = new ArrayList();
		Consulta consulta = new Consulta(conn);
		StringBuilder sql = new StringBuilder("select nodo_id from eje_ges_jerarquia where nodo_padre='" + unidad + "'");
		consulta.exec(String.valueOf(sql));
		for(;consulta.next();) {
          lista.add(consulta.getString("nodo_id"));
        }
		return lista;
	}

	public static List NodosHijosRecursivos(String unidad, String empresa, Connection conn, List lista) {
		Consulta consulta = new Consulta(conn);
		StringBuilder sql = new StringBuilder("select nodo_id from eje_ges_jerarquia where nodo_padre='" + unidad + "' and compania=" + empresa);
		consulta.exec(String.valueOf(sql));
		for(;consulta.next();) {
          lista.add(consulta.getString("nodo_id"));
          NodosHijosRecursivos(consulta.getString("nodo_id"),empresa,conn,lista);
        }
		return lista;
	}
	
	public static List NodosHijosRecursivosJU(String unidad, String empresa, Connection conn, List lista) {
		Consulta consulta = new Consulta(conn);
		StringBuilder sql = new StringBuilder("select nodo_id from eje_ges_jerarquia where nodo_padre= '" + unidad + "' and compania=" + empresa);
		consulta.exec(String.valueOf(sql));
		for(;consulta.next();) {  
			if(!UnidadTieneEncargado(consulta.getString("nodo_id"),empresa,conn)) {  
				lista.add(consulta.getString("nodo_id"));
				NodosHijosRecursivosJU(consulta.getString("nodo_id"),empresa,conn,lista);
			}   
        }
		return lista;
	}	
	
	public static List HijosUnidad(String unidad, String empresa, Connection conn) {
		List lista = new ArrayList();
		Consulta consulta = new Consulta(conn);
		StringBuilder sql = new StringBuilder("select rut from eje_ges_trabajador where unidad= '" + unidad + "'");
		consulta.exec(String.valueOf(sql));
		for(;consulta.next();) {
          lista.add(consulta.getString("rut"));
        }
		return lista;
	}

	public static List HijosUnidadPertenencia(String unidad, String empresa, Connection conn) {
		List lista = new ArrayList();
		Consulta consulta = new Consulta(conn);
		StringBuilder sql = new StringBuilder("select rut from eje_ges_trabajador where unidad= '" + unidad + "'");
		consulta.exec(String.valueOf(sql));
		for(;consulta.next();) {
          lista.add(consulta.getString("rut"));
        }
		return lista;
	}

	public static Consulta DatosHijosUnidad(String unidad, Connection conn) {
		Consulta consulta = new Consulta(conn);
		StringBuilder sql = new StringBuilder("select cast(t.rut as varchar) +'-'+ cast(digito_ver as varchar) as rutfull,t.nombre as nombre,c.descrip as cargo,cast(t.rut as varchar) as rut from eje_ges_trabajador as t inner join eje_ges_cargos as c on t.cargo=c.cargo and t.empresa=c.empresa where t.unidad = '" + unidad + "'");
		consulta.exec(String.valueOf(sql));
		return consulta;
	}
	
	public static Consulta DatosTrabajadorEvalDes(String empresa, String unidad, String rut, Connection conn) {
		Consulta consulta = new Consulta(conn);
		StringBuilder sql = new StringBuilder("select cast(t.rut as varchar) +'-'+ cast(digito_ver as varchar) as rutfull,t.nombre as nombre,c.descrip as cargo,cast(t.rut as varchar) as rut from eje_ges_trabajador as t inner join eje_ges_cargos as c on t.cargo=c.cargo and t.empresa=c.empresa where t.empresa="+empresa+" and unidad='"+unidad+"' and rut=" + rut);
		consulta.exec(String.valueOf(sql));
		return consulta;
	}

	
	
	public static String ultimoPeriodo (Connection conn) {
		Consulta consulta = new Consulta(conn);
		StringBuilder sql = new StringBuilder("select max(peri_id) as periodo from eje_ges_periodo");
		consulta.exec(String.valueOf(sql)); consulta.next();
		return consulta.getString("periodo");
	}
	
	public static List NodosHijosRecursivosEmpresa(String unidad, Connection conn, List lista)
	{
		Consulta consulta = new Consulta(conn);
		StringBuilder sql = new StringBuilder("SELECT nodo_id FROM dbo.eje_ges_jerarquia WHERE nodo_padre=" + unidad);
		consulta.exec(String.valueOf(sql));
		for(;consulta.next();) {
          lista.add(consulta.getString("nodo_id"));
          NodosHijosRecursivosEmpresa(consulta.getString("nodo_id"),conn,lista);
        }
		consulta.close();
		return lista;
	}
	
}
