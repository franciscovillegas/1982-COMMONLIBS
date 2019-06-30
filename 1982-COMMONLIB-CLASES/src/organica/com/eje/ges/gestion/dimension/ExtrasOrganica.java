package organica.com.eje.ges.gestion.dimension;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;

import portal.com.eje.datos.Consulta;


public class ExtrasOrganica {
	
	/**
	 * Clase fuera de mantención, se sebe usar la clase ubicada en portal.com.eje.tools.ExtrasOrganica
	 * 
	 * @deprecated
	 * @see portal.com.eje.tools.ExtrasOrganica
	 * 
	 * */
	public static boolean isEncargadoUnidad (String rut, Connection n)
	{
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

	public static boolean isGed (String rut, Connection n)
	{
		Consulta sulta = new Consulta(n);
		StringBuilder sql = new StringBuilder("select count(app_id) as userged from eje_ges_user_app where app_id='userged' and rut_usuario=" + rut);
		sulta.exec(String.valueOf(sql)); sulta.next();
		String usuario = sulta.getString("userged");
		if(usuario.equals("0")) return false;
		else return true;
	}

	public static boolean isAdministradorCorporativo (String rut, Connection n)
	{
		Consulta sulta = new Consulta(n);
		StringBuilder sql = new StringBuilder("select count(app_id) as adm_corp from eje_ges_user_app where app_id='adm_corp' and rut_usuario=" + rut);
		sulta.exec(String.valueOf(sql)); sulta.next();
		String usuario = sulta.getString("adm_corp");
		if(usuario.equals("0")) return false;
		else return true;
	}

	public static boolean isAdministrador (String rut, Connection n)
	{
		Consulta sulta = new Consulta(n);
		StringBuilder sql = new StringBuilder("Select count(app_id) as adm from eje_ges_user_app where app_id='adm' and rut_usuario=" + rut);
		sulta.exec(String.valueOf(sql)); sulta.next();
		String usuario = sulta.getString("adm");
		if(!usuario.equals("2")) return false;
		else return true;
	}
	
	public static boolean UnidadTieneEncargado (String unidad, String empresa, Connection n)
	{
		Consulta sulta = new Consulta(n);
		StringBuilder sql = new StringBuilder("select distinct count(rut_encargado) as rut from eje_ges_unidad_encargado where unid_id='" + unidad + "' and unid_empresa=" + empresa);
		sulta.exec(String.valueOf(sql)); sulta.next();
		String rut = sulta.getString("rut");
		if(rut.equals("0")) return false;
		else return true;
	}
	
	public static String UnidadEncargado (String unidad, String empresa, Connection n)
	{
		String rut_encargado = "0";
		Consulta sulta = new Consulta(n);
		StringBuilder sql = new StringBuilder("select distinct rut_encargado from eje_ges_unidad_encargado where unid_id='" + unidad + "' and unid_empresa=" + empresa + " and estado = 1") ;
		sulta.exec(String.valueOf(sql));
		if (sulta.next()){
			rut_encargado = sulta.getString("rut_encargado");
		}
		return rut_encargado;
	}
	
	public static String NombreUnidadEncargado (String rut, Connection n)
	{
		String encargado = "SIN ENCARGADO";
		Consulta sulta = new Consulta(n);
		StringBuilder sql = new StringBuilder("select distinct nombre from eje_ges_trabajador where rut=" + rut);
		sulta.exec(String.valueOf(sql)); 
		if (sulta.next()){
			 encargado = sulta.getString("nombre");
		}		
		return encargado;
	}

	public static String UnidadPadreEncargado (String unidad, String empresa, Connection n)
	{
		Consulta sulta = new Consulta(n);
		StringBuilder sql = new StringBuilder("select nodo_padre from eje_ges_jerarquia where nodo_id='" + unidad + "' and compania=" + empresa);
		sulta.exec(String.valueOf(sql)); sulta.next();
		String unidadPadre = sulta.getString("nodo_padre");
		return unidadPadre;
	}

	public static String UnidadTrabajador (String rut, Connection n)
	{
		Consulta sulta = new Consulta(n);
		StringBuilder sql = new StringBuilder("select unidad from eje_ges_trabajador where rut=" + rut);
		sulta.exec(String.valueOf(sql)); sulta.next();
		String unidad = sulta.getString("unidad");
		return unidad;
	}
	
	public static String UnidadDesc (String unidad, Connection n)
	{
		Consulta sulta = new Consulta(n);
		StringBuilder sql = new StringBuilder("select unid_desc from eje_ges_unidades where unid_id='" + unidad + "'");
		sulta.exec(String.valueOf(sql)); sulta.next();
		String descunidad = sulta.getString("unid_desc");
		return descunidad;
	}

	public static String EmpresaTrabajador (String rut, Connection n)
	{
		Consulta sulta = new Consulta(n);
		StringBuilder sql = new StringBuilder("select wp_cod_empresa from eje_ges_trabajador where rut=" + rut);
		sulta.exec(String.valueOf(sql)); sulta.next();
		String empresa = sulta.getString("wp_cod_empresa");
		return empresa;
	}

	public static String PlantaTrabajador (String rut, Connection n)
	{
		Consulta sulta = new Consulta(n);
		StringBuilder sql = new StringBuilder("select wp_cod_planta from eje_ges_trabajador where rut=" + rut);
		sulta.exec(String.valueOf(sql)); sulta.next();
		String planta = sulta.getString("wp_cod_planta");
		return planta;
	}

	public static List NodosHijos(String unidad, String empresa, Connection n)
	{
		//List lista = new List();
		List lista= new ArrayList();
		Consulta sulta = new Consulta(n);
		StringBuilder sql = new StringBuilder("select nodo_id from eje_ges_jerarquia where nodo_padre='" + unidad + "'");
		sulta.exec(String.valueOf(sql));
		for(;sulta.next();) {
          lista.add(sulta.getString("nodo_id"));
        }
		return lista;
	}

	public static List NodosHijosRecursivos(String unidad, String empresa, Connection n, List lista)
	{
		Consulta sulta = new Consulta(n);
		StringBuilder sql = new StringBuilder("select nodo_id from eje_ges_jerarquia where nodo_padre='" + unidad + "' and compania=" + empresa);
		sulta.exec(String.valueOf(sql));
		for(;sulta.next();) {
          lista.add(sulta.getString("nodo_id"));
          NodosHijosRecursivos(sulta.getString("nodo_id"),empresa,n,lista);
        }
		return lista;
	}
	
	public static List NodosHijosRecursivosJU(String unidad, String empresa, Connection n, List lista)
	{
		Consulta sulta = new Consulta(n);
		StringBuilder sql = new StringBuilder("select nodo_id from eje_ges_jerarquia where nodo_padre='" + unidad + "' and compania=" + empresa);
		sulta.exec(String.valueOf(sql));
		for(;sulta.next();) 
		{  if(!UnidadTieneEncargado(sulta.getString("nodo_id"),empresa,n))
		   {  lista.add(sulta.getString("nodo_id"));
              NodosHijosRecursivosJU(sulta.getString("nodo_id"),empresa,n,lista);
		   }   
        }
		return lista;
	}	
	
	public static List HijosUnidad(String unidad, String empresa, Connection n)
	{
		//List lista = new List();
		List lista= new ArrayList();
		Consulta sulta = new Consulta(n);
		StringBuilder sql = new StringBuilder("select rut from eje_ges_trabajador where unidad='" + unidad + "'");
		sulta.exec(String.valueOf(sql));
		for(;sulta.next();) {
          lista.add(sulta.getString("rut"));
        }
		return lista;
	}

	public static List HijosUnidadPertenencia(String unidad, String empresa, Connection n)
	{
		//List lista = new List();
		List lista= new ArrayList();
		Consulta sulta = new Consulta(n);
		StringBuilder sql = new StringBuilder("select rut from eje_ges_trabajador where unidad='" + unidad + "'");
		sulta.exec(String.valueOf(sql));
		for(;sulta.next();) {
          lista.add(sulta.getString("rut"));
        }
		return lista;
	}



	public static boolean esDivisionCorporativa (String unidad, Connection n)
	{
		boolean salida = false;
		Consulta sulta = new Consulta(n);
		StringBuilder sql = new StringBuilder("SELECT unidad_id FROM eje_ges_dimension_item INNER JOIN eje_ges_ditem_unidad ON eje_ges_dimension_item.ditem_id = eje_ges_ditem_unidad.ditem_id AND eje_ges_dimension_item.dimension_id = 2 AND eje_ges_dimension_item.ditem_desc = 'Si' WHERE eje_ges_ditem_unidad.unidad_id = '" + unidad + "'");
		sulta.exec(String.valueOf(sql));
		if (sulta.next())
			salida =  true;
		return salida;
	}

	public static String EmpresaPadre(Connection n)
	{
		String salida = "-1";
		Consulta sulta = new Consulta(n);
		StringBuilder sql = new StringBuilder("SELECT top 1 unid_empresa FROM eje_ges_unidades");
		sulta.exec(String.valueOf(sql));
		if (sulta.next())
			salida = sulta.getString("unid_empresa");
		return salida;
	}

	public static boolean isGerente (String rut, Connection n) {
		Consulta sulta = new Consulta(n);
		StringBuilder sql = new StringBuilder("select count(rut) as existe from eje_ges_gerencia where rut=" + rut + " and estado='1'" );
		sulta.exec(String.valueOf(sql)); sulta.next();
		String estado = sulta.getString("existe");
		if(estado.equals("0")) {
			return false;
		}
		else {
			return true;
		}
	}

}
