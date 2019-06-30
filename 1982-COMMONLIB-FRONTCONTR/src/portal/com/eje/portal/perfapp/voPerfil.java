package portal.com.eje.portal.perfapp;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.IConsultaDataRow;
import cl.ejedigital.web.datos.ConsultaTool;

public class voPerfil implements IConsultaDataRow {
	
	private int intIdPerfil;
	private String strNombre;
	private int intCtdUsuarios;
	private Date dteVigenciaDesde;
	private Date dteVigenciaHasta;
	private boolean bolPorDefecto;
	private List<voModulo> modulos;
	private List<voObjeto> objetos;
	private List<voMatriz> matriz;
	private String strJsonPersonas;
	private String strJsonUserApp;
	private String strJsonAutoservicio;
	private String strJsonObjeto;
	private String strJsonModulo;
	
	public voPerfil(int intIdPerfil, String strNombre, int intCtdUsuarios, Date dteVigenciaDesde, Date dteVigenciaHasta, boolean bolPorDefecto, List<voModulo> modulos, List<voObjeto> objetos, String strJsonPersonas, String strJsonUserApp, String strJsonAutoservicio, String strJsonObjeto, String strJsonModulo) {
		super();
		this.intIdPerfil = intIdPerfil;
		this.strNombre = strNombre;
		this.intCtdUsuarios = intCtdUsuarios;
		this.dteVigenciaDesde = dteVigenciaDesde;
		this.dteVigenciaHasta = dteVigenciaHasta;
		this.bolPorDefecto = bolPorDefecto;
		this.modulos = modulos;
		this.objetos = objetos;
		this.strJsonPersonas = strJsonPersonas;
		this.strJsonUserApp = strJsonUserApp;
		this.strJsonAutoservicio = strJsonAutoservicio;
		this.strJsonObjeto = strJsonObjeto;
		this.strJsonModulo = strJsonModulo;
	}
	
	public voPerfil(int intIdPerfil, String strNombre, int intCtdUsuarios, Date dteVigenciaDesde, Date dteVigenciaHasta, boolean bolPorDefecto, List<voModulo> modulos, List<voObjeto> objetos) {
		this(intIdPerfil, strNombre, intCtdUsuarios, dteVigenciaDesde, dteVigenciaHasta, bolPorDefecto, modulos, objetos, null, null, null, null, null);
	}
	
	public voPerfil(int intIdPerfil, String strNombre, int intCtdUsuarios, Date dteVigenciaDesde, Date dteVigenciaHasta, boolean bolPorDefecto) {
		this(intIdPerfil, strNombre, intCtdUsuarios, dteVigenciaDesde, dteVigenciaHasta, bolPorDefecto, null, null, null, null, null, null, null);
	}
	
	public voPerfil(int intIdPerfil) {
		load(intIdPerfil);
	}

	public voPerfil(Connection conn, int intIdPerfil) {
		load(conn, intIdPerfil);
	}
	
	public boolean load(int intIdPerfil) {
		return load(null, intIdPerfil);
	}

	public boolean load(Connection conn, int intIdPerfil) {
		
		this.intIdPerfil = intIdPerfil;
		
		StringBuilder strSQL = new StringBuilder();
		
		strSQL.append("select p.id_paperfil, p.nombre, pp.ctd_usuarios, p.vigencia_desde, p.vigencia_hasta, p.por_defecto \n")
		.append("from eje_perfapp_perfil p \n")
		.append("left join (select id_paperfil, ctd_usuarios=count(id_persona) from eje_perfapp_perfil_persona group by id_paperfil) pp on pp.id_paperfil=p.id_paperfil ")
		.append("where p.id_paperfil=? \n");
		
		Object[] params = {intIdPerfil};
		try {
			ConsultaData data;
			if (conn!=null){
				data = ConsultaTool.getInstance().getData(conn, strSQL.toString() , params);
			}else{
				data = ConsultaTool.getInstance().getData("portal", strSQL.toString() , params);
			}
			while (data!=null && data.next()) {
				this.strNombre = data.getString("nombre");
				this.intCtdUsuarios = data.getInt("ctd_usuarios");
				this.dteVigenciaDesde = data.getDateJava("vigencia_desde");
				this.dteVigenciaHasta = data.getDateJava("vigencia_hasta");
				this.bolPorDefecto = (data.getInt("por_defecto"))==1;
				this.modulos = null;
				this.objetos = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	
	
	public int getId() {
		return intIdPerfil;
	}
	
	public void setId(int intIdPerfil) {
		this.intIdPerfil = intIdPerfil;
	}
	
	public String strGetNombre() {
		return strNombre;
	}

	public int getCtdUsuarios() {
		return intCtdUsuarios;
	}
	
	public Date getVigenciaDesde() {
		return dteVigenciaDesde;
	}

	public Date getVigenciaHasta() {
		return dteVigenciaHasta;
	}

	public boolean getPorDefecto() {
		return bolPorDefecto;
	}
	
	public List<voModulo> getModulos(){
		return modulos;
	}
	
	public List<voObjeto> getObjetos(){
		return objetos;
	}

	public void setMatriz(List<voMatriz> matriz) {
		this.matriz =matriz;
	}
	
	public String getJsonPersonas() {
		return strJsonPersonas;
	}
	
	public void setJsonPersonas(String strJsonPersonas) {
		this.strJsonPersonas = strJsonPersonas;
	}
	
	public String getJsonUserApp() {
		return strJsonUserApp;
	}
	
	public void setJsonUserApp(String strJsonUserApp) {
		this.strJsonUserApp = strJsonUserApp;
	}
	
	public String getJsonAutoservicio() {
		return strJsonAutoservicio;
	}
	
	public void setJsonAutoservicio(String strJsonAutoservicio) {
		this.strJsonAutoservicio = strJsonAutoservicio;
	}

	public String getJsonObjeto() {
		return strJsonObjeto;
	}

	public void setJsonObjeto(String strJsonObjeto) {
		this.strJsonObjeto = strJsonObjeto;
	}
	
	public String getJsonModulo() {
		return strJsonModulo;
	}

	public void setJsonModulo(String strJsonModulo) {
		this.strJsonModulo = strJsonModulo;
	}

	public DataFields toDataField() {
		
		DataFields data = new DataFields();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		data.put("id_perfil", new Field(this.intIdPerfil));
		data.put("nombre", new Field(this.strNombre));
		if (this.intCtdUsuarios==0) {
			data.put("ctd_usuarios", null);
		}else{
			data.put("ctd_usuarios", new Field(this.intCtdUsuarios));
		}
		if (this.dteVigenciaDesde==null) {
			data.put("vigencia_desde", null);
		}else {
			data.put("vigencia_desde", new Field(sdf.format(this.dteVigenciaDesde)));
		}
		if (this.dteVigenciaHasta==null) {
			data.put("vigencia_hasta", null);
		}else {
			data.put("vigencia_hasta", new Field(sdf.format(this.dteVigenciaHasta)));
		}
		data.put("por_defecto", new Field(this.bolPorDefecto));
		
		if (matriz!=null) {
			for (voMatriz record: matriz) {
				StringBuilder strCampo = new StringBuilder("z").append(record.getZona().getId()).append("g").append(record.getGrupo().getId()).append("o").append(record.getObjeto().getId());
				data.put(strCampo.toString(), record.getActivo());
			}
			if (matriz.size()==1) {
				data.put("id_zona", matriz.get(0).getZona().getId());
				data.put("id_grupo", matriz.get(0).getGrupo().getId());
				data.put("id_objeto", matriz.get(0).getObjeto().getId());
			}
		}
		data.put("json_personas", new Field(this.strJsonPersonas));
		data.put("json_userapp", new Field(this.strJsonUserApp));
		data.put("json_autoservicio", new Field(this.strJsonAutoservicio));
		data.put("json_objeto", new Field(this.strJsonObjeto));
		data.put("json_modulo", new Field(this.strJsonModulo));
		
		return data;
		
	}
	
}
