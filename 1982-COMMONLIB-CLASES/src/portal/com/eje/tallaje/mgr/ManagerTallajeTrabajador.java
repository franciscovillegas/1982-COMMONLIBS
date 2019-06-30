package portal.com.eje.tallaje.mgr;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import organica.datos.Consulta;
import portal.com.eje.tallaje.data.Tallajes;
import portal.com.eje.tools.ExtrasOrganica;
import portal.com.eje.tools.Tools;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.ConsultaTool;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class ManagerTallajeTrabajador {
	private static ManagerTallajeTrabajador instance;
	private List<String> columnName;
	
	public ManagerTallajeTrabajador() {
		columnName = new ArrayList<String>();
		columnName.add("rut");
		columnName.add("digito_ver");
		columnName.add("nombres");
		columnName.add("ape_paterno");
		columnName.add("ape_materno");
		columnName.add("fecha_ingreso");

	}
	
	public static ManagerTallajeTrabajador getInstance() {
		if(instance == null) {
			synchronized (ManagerTallajeTrabajador.class) {
				if(instance == null) {
					instance = new ManagerTallajeTrabajador();
				}
			}
		}
		return instance;
	}
	
    public SimpleList getTallajesTrabajadores(Connection conn, String rut) {
    	String cargo = getCargoTrabajador(conn,rut);
    	String genero = getGeneroTrabajador(conn,rut);
    	String conceptos = getTallajesCargoGenero(conn,cargo,genero);
    	SimpleList tallajes = new SimpleList();
    	SimpleHash talla;
		Consulta consulta = new Consulta(conn);
        StringBuilder sql = new StringBuilder("select c.id,c.concepto,c.etiqueta,talla_valor=(select isnull(talla_valor,'') ")
        	.append("from eje_tallaje_trabajador where rut=").append(rut).append(" and concepto=c.id) from ")
        	.append("eje_tallaje_concepto c where c.id in ").append(conceptos).append(" order by c.etiqueta");
        consulta.exec(String.valueOf(sql));
        for(; consulta.next(); tallajes.add(talla)) {
        	talla = new SimpleHash();
        	talla.put("id", consulta.getString("id"));
        	talla.put("concepto", consulta.getString("concepto"));
        	talla.put("etiqueta", consulta.getString("etiqueta"));
        	talla.put("talla_valor", consulta.getString("talla_valor"));
        }
		return tallajes;
    }

    public String getTallajesCargoGenero(Connection conn, String cargo, String genero) {
    	String Sconceptos = "(";
    	List<String> conceptos = getConceptos(conn);
    	for(int i=0;i<conceptos.size();i++) {
    		if(getTieneConcepto(conn,cargo,genero,conceptos.get(i))) {
    			Sconceptos += getIdConcepto(conn,conceptos.get(i)) + ",";
    		}
    	}
    	Sconceptos += "0)";
    	return Sconceptos;
    }
    
    public String getIdConcepto(Connection conn, String concepto) {
    	String id = "-1";
		Consulta consulta = new Consulta(conn);
        StringBuilder sql = new StringBuilder("select id from eje_tallaje_concepto where concepto='")
        	.append(concepto).append("'");
        consulta.exec(String.valueOf(sql));
        if(consulta.next()) {
        	id = consulta.getString("id");
        }
		return id;
    }
    
    public boolean getTieneConcepto(Connection conn, String cargo, String genero, String concepto) {
    	boolean tiene = false;
		Consulta consulta = new Consulta(conn);
        StringBuilder sql = new StringBuilder("select ").append(concepto).append(" as concepto from eje_tallaje_cargo where cod_cargo=")
        	.append(cargo).append(" and genero='").append(genero).append("'");
        consulta.exec(String.valueOf(sql));
        if(consulta.next()) {
        	tiene = consulta.getString("concepto").equals("1") ? true : false;
        }
		return tiene;
    }

    public List<String> getConceptos(Connection conn) {
    	ArrayList conceptos = new ArrayList();
    	int i=0;
    	Consulta consulta = new Consulta(conn);
        StringBuilder sql = new StringBuilder("SELECT COLUMN_NAME FROM information_schema.columns WHERE table_name = 'eje_tallaje_cargo' and COLUMN_NAME like 'concepto%'");
        consulta.exec(String.valueOf(sql));
        for(; consulta.next();) {
        	conceptos.add(i, consulta.getString("COLUMN_NAME"));
        	i++;
        }
        return conceptos;
    }
    
    public String getCargoTrabajador(Connection conn, String rut) {
    	String cargo = "-1";
		Consulta consulta = new Consulta(conn);
        StringBuilder sql = new StringBuilder("select top 1 cargo from eje_ges_trabajador where rut=").append(rut);
        consulta.exec(String.valueOf(sql));
        if(consulta.next()) {
        	cargo = consulta.getString("cargo");
        }
		return cargo;
    }
    
    public String getGeneroTrabajador(Connection conn, String rut) {
    	String genero = "M";
		Consulta consulta = new Consulta(conn);
        StringBuilder sql = new StringBuilder("select top 1 sexo from eje_ges_trabajador where rut=").append(rut);
        consulta.exec(String.valueOf(sql));
        if(consulta.next()) {
        	genero = consulta.getString("sexo");
        }
		return genero;
    }
    
    public boolean getTieneTalla(Connection conn, String rut, String cargo, String id) {
    	boolean tiene = false;
		Consulta consulta = new Consulta(conn);
        StringBuilder sql = new StringBuilder("select COUNT(*) existe from eje_tallaje_trabajador where rut=")
        	.append(rut).append(" and cargo=").append(cargo).append(" and concepto=").append(id);
        consulta.exec(String.valueOf(sql));
        if(consulta.next()) {
        	tiene = consulta.getString("existe").equals("1") ? true : false;
        }
		return tiene;
    }
    
    public boolean insertTallaTrabajador(Connection conn, String rut, String cargo, String id, String valor) {
    	boolean inserta = false;
		Consulta consulta = new Consulta(conn);
        StringBuilder sql = new StringBuilder("insert eje_tallaje_trabajador(rut,cargo,concepto,talla_valor,fecha_update) ")
        	.append("values (").append(rut).append(",").append(cargo).append(",").append(id).append(",'")
        	.append(valor).append("',GETDATE())");
        if(consulta.insert(String.valueOf(sql))) {
        	inserta = true;
        }
		return inserta;
    }
	
    public boolean updateTallaTrabajador(Connection conn, String rut, String cargo, String id, String valor) {
    	boolean updatea = false;
		Consulta consulta = new Consulta(conn);
        StringBuilder sql = new StringBuilder("update eje_tallaje_trabajador set talla_valor='").append(valor)
        		.append("',fecha_update=GETDATE() where rut=").append(rut).append(" and cargo=").append(cargo)
        		.append(" and concepto=").append(id);
        if(consulta.insert(String.valueOf(sql))) {
        	updatea = true;
        }
		return updatea;
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
	public boolean updateTrabajador(int rut, int campoPos, String value)  {
		StringBuilder strConsulta = new StringBuilder();
		strConsulta.append(" update eje_ges_trabajador SET ".concat(getTrabajadorColumnName(campoPos)).concat(" = ? where rut = ?"));
		
		Object[] params = {value,rut};

		boolean ok = false;
		try {
			ok = ConsultaTool.getInstance().update("portal",strConsulta.toString(),params) > 0 ;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ok;
	}
	
	public String getTrabajadorColumnName(int id) {
		return columnName.get(id);
	}
	
	public boolean insertTrabajador(int rut, String digitoVer, String nombre,String nombres, String apePaterno, String apeMaterno,
								String cargo, String unidad, String dependencia,String fecIngreso, String centroCosto,String division,
								String mail) {
		
		StringBuilder strConsulta = new StringBuilder();
		strConsulta.append(" INSERT INTO eje_ges_trabajador (rut,digito_ver,nombre,nombres,ape_Paterno,ape_Materno, ");
		strConsulta.append(" cargo,unidad,dependencia,fecha_ingreso,ccosto,division, mail, periodo, ");
		strConsulta.append(" wp_cod_empresa, wp_cod_planta,wp_cod_sucursal)");
		strConsulta.append(" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
		
		Object[] params = {rut,digitoVer,
					Validar.getInstance().cutString(nombre, 50),
					Validar.getInstance().cutString(nombres, 20),
					Validar.getInstance().cutString(apePaterno, 20),
					Validar.getInstance().cutString(apeMaterno, 20),cargo,unidad,dependencia, fecIngreso, centroCosto, division, mail,0,0,0,0};

		boolean ok = false;
		try {
			ok = ConsultaTool.getInstance().insert("portal",strConsulta.toString(),params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ok;
	}
	
	public boolean existeTrabajador(int rut) {
		StringBuilder strConsulta = new StringBuilder();
		strConsulta.append(" SELECT 1 FROM eje_ges_trabajador ");
		strConsulta.append(" WHERE rut = ? ");
		
		Object[] params = {rut};

		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal",strConsulta.toString(),params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data.next();
	}
	

	public boolean updateTrabajador(int rut, String digitoVer, String nombre,String nombres, String apePaterno, String apeMaterno,
									String cargo, String unidad, String dependencia,String fecIngreso, String centroCosto,String division,
									String mail)  {
		StringBuilder strConsulta = new StringBuilder();
		strConsulta.append(" UPDATE eje_ges_trabajador ");
		strConsulta.append(" SET digito_ver = ?,nombre=?,nombres=?,ape_Paterno=?,ape_Materno=?, ");
		strConsulta.append(" 	cargo=?,unidad=?,dependencia=?,fecha_ingreso=?,ccosto=?,division=?, mail=? ");
		strConsulta.append(" WHERE  rut = ? ");
		
		Object[] params = {digitoVer, nombre, nombres, apePaterno, apeMaterno, cargo,
							unidad, dependencia, fecIngreso, centroCosto, division, mail, rut};

		boolean ok = false;
		try {
			ok = ConsultaTool.getInstance().update("portal",strConsulta.toString(),params) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ok;
	}
	
	public ConsultaData getTrabajadoresInUnidad(String unidad) {
		return getTrabajadores(null, unidad);
	}
	

	public ConsultaData getTrabajadores() {
		return getTrabajadores(null,null);
	}
	
	
	public ConsultaData getTrabajador(String rut) {
		return getTrabajadores(Validar.getInstance().validarInt(rut, -1),null);
	}
	
	public ConsultaData getTrabajador(int rut) {
		return getTrabajadores(rut,null);
	}
	
	public ConsultaData getTrabajadores(Integer rut, String unidad) {
		StringBuilder strConsulta = new StringBuilder();
		strConsulta.append(" select distinct t.rut,t.digito_ver,t.nombres,t.ape_paterno,t.ape_materno, \n");
		strConsulta.append(" 	convert(varchar,t.fecha_ingreso,106) as fecha_ingreso, \n");
		strConsulta.append(" 	isnull(u.estado,0) as es_encargado, \n");
		strConsulta.append(" 	'' as options, mail, nombre, t.unidad, t.empresa \n");
		strConsulta.append(" from eje_ges_trabajador t \n");
		strConsulta.append(" 	left join eje_ges_unidad_encargado u on u.unid_id = t.unidad and t.rut = u.rut_encargado and u.estado = 1 \n");
		strConsulta.append(" 	left join eje_ges_trabajador_unidad c on t.rut = c.rut \n");
		strConsulta.append(" WHERE t.rut <> 99999999  \n");
		
		ArrayList<Object> lista = new ArrayList<Object>();
		if(rut != null) {
			strConsulta.append(" and t.rut = ?");
			lista.add(rut);
		}
		
		if(unidad != null) {
			strConsulta.append(" and c.unidad = ?");
			lista.add(unidad);
		}
		
		
	
		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal",strConsulta.toString(),lista.toArray());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}
	

    public List getTrabajadoresUnidades(Connection conn, List orgUnidades) {
		Consulta consulta = new Consulta(conn);
        List lstTrabajadores = new ArrayList();

		for (int i=0; i < orgUnidades.size(); i++) {
			if(orgUnidades.get(i) != null) {
				String strUnidad = orgUnidades.get(i).toString();
				StringBuilder sql = new StringBuilder("select distinct rut from eje_ges_trabajador where unidad = '"+ strUnidad +"' ");
	
				consulta.exec(String.valueOf(sql));
				for(;consulta.next();) {
					lstTrabajadores.add(consulta.getString("rut"));
			    }
			}
		}

		return lstTrabajadores;
	} 
    
}
