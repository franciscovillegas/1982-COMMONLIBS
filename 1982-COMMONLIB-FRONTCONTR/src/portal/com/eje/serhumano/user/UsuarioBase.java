package portal.com.eje.serhumano.user;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.WordUtils;

import cl.ejedigital.ExceptionNotImplemented;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.tool.strings.MyString;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.ConsultaTool;
import freemarker.template.SimpleHash;
import portal.com.eje.datos.Consulta;
import portal.com.eje.permiso.PerfilPortal;
import portal.com.eje.permiso.PermisoPortal;
import portal.com.eje.portal.generico_appid.EnumAppId;
import portal.com.eje.portal.organica.OrganicaLocator;
import portal.com.eje.portal.perfapp.PerfApp;
import portal.com.eje.portal.perfapp.voGrupo;
import portal.com.eje.portal.perfapp.voObjeto;
import portal.com.eje.portal.perfapp.voUserApp;
import portal.com.eje.portal.perfapp.voZona;
import portal.com.eje.portal.perfil.PerfilLocator;
import portal.com.eje.portal.sqlserver.SqlServerTool;
import portal.com.eje.portal.trabajador.VoPersona;
import portal.com.eje.serhumano.admin.CapManager;
import portal.com.eje.serhumano.datosdf.datosRut;
import portal.com.eje.serhumano.user.jndimanager.IJndiManager;
import portal.com.eje.serhumano.user.jndimanager.JndiManagerPer;
import portal.com.eje.serhumano.user.unidad.VerUnidad;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.md5.ManagerMD5;
import portal.com.eje.tools.portal.ToolOrganicaIO;
import portal.com.eje.tools.security.Encrypter;
import portal.com.eje.usuario.UsuarioImagenManager;
import portal.com.eje.usuario.VoUsaurioImagen;

public class UsuarioBase extends UsuarioSessionable implements HttpSessionBindingListener, Serializable, IUsuario {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */

	private Map<String,UsuarioValue> map;
	/**
	 * @deprecated
	 */
	public UsuarioBase(HttpSession session) {
        this(session, null);
    }
	
	public UsuarioBase() {
        this((HttpSession)null, null);
    }
	
	private boolean jefeUnidad;
	private long sesionInit;
	private IJndiManager iJndiManager;
    private String mensajeerror;
    private Rut rut;
    private VoUsaurioImagen usuarioImagen;
    private String usuario;
    private String name;
    private String foto;
    private boolean valido;
    private ListaPermisos permisos;
    private String rol;
    private int cant_ingresos;
    private InfoUsuario infoUsuario;
    private boolean verMultiplesRamas;
    private VerUnidad unidadesVer[];
    private VerUnidad unidadesRel[];
    private Vector VerApp;
    private List<String> VerAppCarpetaElectronica;
    private List<String> VerAppReportesOrganica;
    private List<Map<String,String>> VerAppWebmatico;
    private String unidad;
    private String idUnidad;
    private String empresa;
    private String empresaDescrip;
    private String planta;
    private String PassWord;
    private String email;
    private String emailValidado;
    private Object[] unidadesDescendientes;
    public Date initdate;
    private String passwordBrowser;
    private UsuarioPerfil perfil;
    private String cargo;
    private String cargoId;

    private List<Map<String, Map<String, ArrayList<Object>>>> paAccesos;
    
   public UsuarioBase(HttpSession session, IJndiManager iJndiManager) {
		super(session );
        mensajeerror = "";
        name = "";
        cant_ingresos = 0;
        verMultiplesRamas = false;
        unidad = "";
        empresa = "";
        PassWord = "";
        valido = false;
        mensajeerror = "Datos no v\341lidos";
        rut = new Rut("1","k");
        VerApp = new Vector();
        
        if(iJndiManager == null) {
        	this.iJndiManager = new JndiManagerPer();
        }
        else {
        	this.iJndiManager = iJndiManager;
        }
        
        this.VerAppCarpetaElectronica = new LinkedList();
        this.VerAppReportesOrganica = new LinkedList();
        this.VerAppWebmatico = new ArrayList();
        
        this.paAccesos = new ArrayList<Map<String,Map<String,ArrayList<Object>>>>();
        
        map = new LinkedHashMap<String,UsuarioValue>();
    }
	
	public void updCantIngresosMasUno(Connection connPortal) {
		UserMgr usermgr = new UserMgr(connPortal);
		usermgr.setCantIngresos(rut,++cant_ingresos);
	}
	
	/**
	 * Luego de un análisis se determinó que este método no se utiliza, y no debe utilzarse puesto que siempre obtiene la clave. Este método siempre
	 * logeará a la persona, sin importar la clave que se tenga.
	 * 
	 * @deprecated
	 * @since 19 Marzo 2014
	 * @author FVillegas
	 * */
	public boolean getDatos(String sql, Connection conn, Connection connDf, Rut rut, String app)
    {
		String getClave = "select top 1 password_usuario from eje_ges_usuario where rut_usuario = ?";
		
		Object[] params = {rut};
		
		ConsultaData data;
		try {
			data = ConsultaTool.getInstance().getData("portal",getClave, params);
			if(data != null && data.next()) {
				return getDatos(sql,null,null,rut,data.getForcedString("password_usuario"),null);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
    }
	
	
	/**
	 * El par&aocute;metro SQl debe obligatoriamente lo siguientes campos: </b>
	 * rol_id, app_id, rut_usuario, password_usuario,login_usuario,isnull(cant_ingresos,0) as cant_ingresos, md5 </br>
	 * */
	public boolean getDatos(String sql, Connection conn, Connection connDf, Rut rut, String pass, String app_VarSinUso) {
        valido = false;
        
        boolean bolPerfAppActivo = PerfApp.getInstance().getPerfAppActivo();
        
        String pass2 = pass;
            ConsultaData data;
			try {
				 data = ConsultaTool.getInstance().getData("portal",sql.toString());
				
				 if(data.next()) {
		                PassWord = data.getForcedString("password_usuario");
		                if( data.getForcedString("md5").equals("S")) {
		                	pass = ManagerMD5.encrypt(pass);
		                }
		                
		                OutMessage.OutMessagePrint("\n\n " + pass.trim() + "@" + PassWord.trim());
		                if(pass != null && pass.trim().equals(PassWord.trim())) {
		                	
		                	PassWord = pass2;
		                	
		                	String dig = data.getForcedString("digito_ver");
		                	if(dig == null) {
		                		dig = "";
		                	}
		                	
		                    this.rut = new Rut(data.getForcedString("rut_usuario"), dig);
		                    this.usuario = data.getForcedString("login_usuario");
		                    
		                    UsuarioImagenManager um = new UsuarioImagenManager();
		                    this.usuarioImagen = um.getImagen(this.getRutIdInt());
		                    
		                    rol =  data.getForcedString("rol_id");
		                    datosRut dRut = new datosRut(connDf, this.rut.getRutId());
		                    name = WordUtils.capitalizeFully(dRut.Nombres);
		                    foto = dRut.Foto;
		                    cant_ingresos =  data.getInt("cant_ingresos");
		                    valido = true;
		                    empresa = dRut.Empresa;
		                    empresaDescrip = dRut.EmpresaDescrip;
		                    unidad = dRut.Unidad;
		                    idUnidad = dRut.Id_Unidad;
		                    permisos = ControlAcceso.cargarListaPermisos(conn, getRutId());
		                    VerApp = new Vector();
		                    email  = data.getForcedString("mail");
		                    emailValidado = getEmailFromTablaConfirmacion();
		                    CapManager cm = new CapManager(conn);
		                    
		                    /*GET app_id from eje_ges_user_app*/
		                    ConsultaData x = cm.getApp(this.rut.getRutId());
		                    while(x.next()) {
		                    	 VerApp.add("app_".concat(x.getForcedString("app_id")));
		                    }           
		                    
		                    VoPersona persona = new VoPersona(Integer.parseInt(this.rut.getRutId()));
		                    if (bolPerfAppActivo) {
		                    	List<voUserApp> userapps = PerfApp.getInstance().getUserAppsDePersona(persona);
		                    	for(voUserApp userapp: userapps) {
		                    		VerApp.add("app_".concat(userapp.getCodigo()));
		                    	}
		                    }else {
		                    	/*get app_id from eje_generico_webmatico_ejegesuserapp*/
			                    List<String> xFromWebmatico = getAppIdFromWebmatico();
			                    for(String appId : xFromWebmatico) {
			                    	 VerApp.add("app_".concat(appId));
			                    }
			                }
		                
		                    ToolOrganicaIO org = new ToolOrganicaIO();
		                    cargo  = cm.getCargoRut(this.rut.getRutId(), conn, empresa);
		                    cargoId= cm.getCargoIdRut(this.rut.getRutId(), conn, empresa);
		                    
		                    if(org.getUnidadWhereIsEncargado(cl.ejedigital.tool.validar.Validar.getInstance().validarInt(rut.getRutId(), 0)) != null) {
		                    	VerApp.add("jefe_unidad");
		                    	jefeUnidad = true; 
		                    }
		                    
		                    perfil = getUsuarioPerfil();
		                    
		                    if (bolPerfAppActivo) {		/** PERFILES 2018 */
		                    	loadPaAccesos(persona);
		                    }else if(existeTablasWebmatico()){	/** PERFILES 2016 */
			                    loadAppCarpetaElectronica();
			                    loadAppReportesOrganica();
			                    loadAppWebmatico();
		                    }
		                } 
		                else {
		                    mensajeerror = "Clave Erronea";
		                }
		                
		                
		            } 
		            else {
		                mensajeerror = "Usuario No Valido";
		            }
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
           

        sesionInit = Calendar.getInstance().getTimeInMillis();
        return esValido();
	}
	
	public boolean reLoadDatos() {
		return getDatos(null,null,this.rut,this.PassWord, "sh");
	}
    
	public boolean getDatos(Connection conn, Connection connDf, Rut rut, String pass, String app) {
		  	
			/*LA QUERY ORIGINAL NO SE DEBE UTILIZAR PUESTO QUE LLAMA A UNA VISTA*/
			StringBuilder strQueryOriginal = new StringBuilder();
		  	strQueryOriginal.append(" SELECT rol_id, app_id, rut_usuario,login_usuario, digito_ver, password_usuario,login_usuario,isnull(cant_ingresos,0) as cant_ingresos, md5 ");
		  	strQueryOriginal.append(" FROM  view_eje_ges_user ");
		  	strQueryOriginal.append(" WHERE  ltrim(rtrim(rut_usuario)) = '").append(rut.getRutId()).append("' AND ");
		  	strQueryOriginal.append(" 		app_id in ('").append(app).append("','adm_cp','adm_gp')").toString();
	        
		  	/*
		  	 * 07-DIC-2016
		  	 * */
		  	
		  	boolean existe = SqlServerTool.getInstance().existColumn("portal", "eje_ges_usuario" , "mail");
		  	
		  	StringBuilder strQueryImportada = new StringBuilder();
		  	strQueryImportada.append(" select rol_id=1, us.rut_usuario, t.digito_ver, us.password_usuario, us.login_usuario, rol_puede_ver=1, us.cant_ingresos, uapp.vigente, uapp.app_id, us.md5 ");
		  	if(existe) {
		  		strQueryImportada.append(" , us.mail ");	
		  	}
		  	else {
		  		strQueryImportada.append(" , t.mail ");
		  	}
		  	
		  	strQueryImportada.append(" from eje_ges_user_app uapp "); 
		  	strQueryImportada.append(" 		INNER JOIN eje_ges_usuario us         ON uapp.rut_usuario = us.rut_usuario "); 
		  	strQueryImportada.append(" 		LEFT JOIN eje_ges_trabajador  t      ON t.rut = us.rut_usuario "); 
		  	strQueryImportada.append(" WHERE ltrim(rtrim(us.login_usuario)) =  '").append(StringEscapeUtils.escapeSql(rut.getRutId())).append("'");  	
		  	
	        if(pass != null) {
	           return getDatos(strQueryImportada.toString(), conn, connDf, rut, pass, app);
	        } else {
	           return getDatos(strQueryImportada.toString(), conn, connDf, rut, app); 
	        }
    }
	
	/**
	 * @author Pancho
	 * @since 02-05-2019
	 * */
	public boolean getDatosByEmail(Connection conn, Connection connDf, Rut rut, String email, String pass, String app) {
		StringBuilder strQueryImportada = new StringBuilder();
	  	strQueryImportada.append(" select rol_id=1, us.rut_usuario, t.digito_ver, us.password_usuario, us.login_usuario, rol_puede_ver=1, us.cant_ingresos, uapp.vigente, uapp.app_id, us.md5 ");
	  	strQueryImportada.append(" ,us.mail ");
	  	strQueryImportada.append(" from eje_ges_user_app uapp "); 
	  	strQueryImportada.append(" 		INNER JOIN eje_ges_usuario us         ON uapp.rut_usuario = us.rut_usuario "); 
	  	strQueryImportada.append(" 		LEFT JOIN eje_ges_trabajador  t      ON t.rut = us.rut_usuario "); 
	  	strQueryImportada.append(" WHERE ltrim(rtrim(us.mail)) =  '").append(StringEscapeUtils.escapeSql(email)).append("'");  	
	  	
        if(pass != null) {
           return getDatos(strQueryImportada.toString(), conn, connDf, rut, pass, app);
        }
        
        return false;
	}
	
    public boolean getDatos(Connection conn, Connection connDf, Rut rut, String app)
    {
        return getDatos(conn, connDf, rut, ((String) (null)), app);
    }

   
	
	public Object[] getUnidadesDescendientes() {
		//MMA 20180125: No se debe rescatar de la sesión ya que los cambios en otris módulos no se reflejan provocando movimientos inconsistentes.
//		if(this.unidadesDescendientes == null ) {
					List<String> lista = new ArrayList<String>();
					
					if(getIdUnidad() != null) {
						String sql = "select unidad from dbo.getdescendientes('".concat(getIdUnidad()).concat("')");
						
						try {
							ConsultaData data = ConsultaTool.getInstance().getData("portal", sql);
							while(data != null && data.next()) {
								lista.add(data.getString("unidad"));
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					unidadesDescendientes = lista.toArray();

//		}
		
		return unidadesDescendientes;
	}
	
	
	
	private UsuarioPerfil getUsuarioPerfil() {
		StringBuilder str = new StringBuilder();
		str.append(" SELECT pu.id_perfil,gp.id_perfil_creo, nombre, estado, fecha_creacion ");
		str.append(" FROM eje_generico_perfil_usuario pu inner join eje_generico_perfil gp on gp.id = pu.id_perfil ");
		str.append(" WHERE pu.rut = ? ");
		String[] params = {this.rut.getRutId()};
		UsuarioPerfil perfil = null;

		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal" , str.toString(),params);
			if(data.next()) {
				perfil =  new UsuarioPerfil(
					String.valueOf(data.getInt("id_perfil")),
					String.valueOf(data.getInt("id_perfil_creo")),
					String.valueOf(data.getString("nombre")),
					data.getBoolean("estado") ,
					String.valueOf(data.getDateJava("fecha_creacion")));
			}
		}
		catch (SQLException e) {
			perfil =  new UsuarioPerfil("-1","-1","Desconocido",false,"19001010");
		}
		
		return perfil;
	}

    public boolean usuarioVigente(Connection conn, Rut rut) {
        String sql = "";
        if(conn != null && rut != null) {
            Consulta consul = new Consulta(conn);
            sql = String.valueOf(String.valueOf((new StringBuilder("SELECT rut FROM  eje_ges_trabajador WHERE (rut = ")).append(rut.getRutId()).append(")")));
            //System.out.println("getDatos --> ".concat(String.valueOf(String.valueOf(sql))));
            consul.exec(sql);
            if(consul.next()) {
            	return true;
            }
            consul.close();
        }

        System.err.println("es valido: ".concat(String.valueOf(String.valueOf(valido))));
        return false;
    }

    /**
     * Ya no se usa
     * @deprecated
     * */
    public boolean updatePasswdDBGlobal(Connection conn, Connection connAux, String rut, String pass_a) {
        UserMgr usermgr = new UserMgr(conn);
        System.out.println("\n1");
        String sql = "";
        String sqlAux = "";
        valido = false;
        boolean ok = false;
        if(conn != null) {
            System.out.println("\n2");
            System.out.println("\n3");
            Consulta consul = new Consulta(conn);
            System.out.println("\n4");
            String ecryptedPassword = ManagerMD5.encrypt(pass_a.trim());
            sql = "UPDATE UserGlobal  SET  clave = '" + ecryptedPassword + "' WHERE (rut_trabajador = " + rut + ")";
            if(consul.insert(sql)) {
                ok = true;
                System.out.println("\nActualizada tabla UserGlobal : " + sqlAux);
            } 
            else {
                ok = false;
                System.out.println("\nError al Actualizar tabla UserGlobal : " + sqlAux);
            }
            consul.close();
        } 
        else {
            mensajeerror = "Usuario No Valido";
            System.out.println("\nError consul -->" + sql);
        }
        valido = ok;
        return ok;
    }
    
    /**
     * Pregunta por otras BD que ya no se usan.<br/> 
     * Es un lastre de implementaciones anteriores
     * @deprecated
     * */
    public boolean findUser(Connection conn, Connection connAux, String rut, String pass_a) {
        UserMgr usermgr = new UserMgr(conn);
        System.out.println("\n1");
        String sql = "";
        String sqlAux = "";
        valido = false;
        boolean ok = false;
        if(conn != null && connAux != null) {
            System.out.println("\n2");
            Consulta consulAux = new Consulta(connAux);
            System.out.println("\n3");
            Consulta consul = new Consulta(conn);
            System.out.println("\n4");
            sqlAux = "SELECT rut_trabajador, clave FROM  UserGlobal WHERE rut_trabajador = " + rut;
            System.out.println("findUser --> ".concat(String.valueOf(String.valueOf(sqlAux))));
            consulAux.exec(sqlAux);
            System.out.println("\n5");
            if(consulAux.next()) {
                System.out.println("\n6");
                String pass_b = consulAux.getString("clave");
                if(pass_b.trim().equals(pass_a.trim()) || pass_b.trim().equals(ManagerMD5.encrypt(pass_a.trim()))) {
                    sql = "UPDATE eje_ges_usuario  SET  password_usuario = '" + pass_b.trim() + "', md5 = 'S'  WHERE (rut_usuario = " + rut + ")";
                    if(consul.insert(sql)) {
                        ok = true;
                        System.out.println("\nActualizada tabla eje_ges_usuario : " + sqlAux);
                    } 
                    else {
                        ok = false;
                        System.out.println("\nError al Actualizar tabla eje_ges_usuario : " + sqlAux);
                    }
                    consul.close();
                } 
                else {
                    mensajeerror = "Clave Erronea";
                    System.out.println("\nPass distintos -->");
                }
            } 
            else {
                mensajeerror = "Usuario No Valido";
                System.out.println("\nError consul -->" + sql);
            }
            consulAux.close();
        }
        valido = ok;
        return ok;
    }

    public boolean esValido() {
        return valido;
    }

    public boolean esValidoSimple() {
    	return valido;
    }
    
    public String getError() {
        return mensajeerror;
    }

    public String getName() {
        return name;
    }

    public Rut getRut() {
        return rut;
    }

    public String getRutId() {
    	if(rut != null) {
    		return rut.getRutId();
    	}
    	
    	return null;
    }
    
    public String getRutIdEncypted() {
    	if(rut != null) {
    		String rutEnc = String.valueOf(rut.getRutId());
    		
    		Encrypter enc = new Encrypter();
        	return enc.encrypt(rutEnc);
    	}
    	else {
    		return null;
    	}
    	
    }
    
    public int getRutIdInt() {
    	if(rut != null) {
    		return Validar.getInstance().validarInt(rut.getRutId());
    	}
    	
    	return 0;
    }

    public ListaPermisos getPermisos() {
        return permisos;
    }
    
    public String getCargoDesc() {
    	if(cargo != null) {
    		return cargo;
    	}
    	else {
    		return "";
    	}
    }
    
    public String getCargoId() {
    	if(this.cargoId != null) {
    		return cargoId;
    	}
    	else {
    		return "";
    	}
    }
    
    
    public boolean isJefeUnidad() {
		return jefeUnidad;
	}

    /**
     * Este es un método rato, entrega los app_id con un "app_" de prefijo
     * @author Pancho
     * @since 17-05-2019
     * */
	public List<String> getApps() {
    	ArrayList<String> lista = new ArrayList<String>();
    	
    	if(VerApp != null) {
	    	for(int i = 0 ; i< this.VerApp.size() ; i++) {
	    		lista.add( (String) VerApp.get(i));
	    	}
    	}
    	
    	return lista;
    }
	
	public List<EnumAppId> getAppIds() {
		return new ArrayList<>( UsuarioAppIdTool.getInstance().getAppIds(getRutIdInt()).keySet() ) ;
	}

    public int getCantIngreso() {
        return cant_ingresos;
    }

    public String toString() {
        String ret = "";
        if(!valido) {
        	ret = "[valido=NO]";
        }
        else {
        	StringBuilder ret2 = new StringBuilder("[valido=SI, rut=").append(rut)
        				.append(", nombre=").append(name)
        				.append(", rol=").append(rol)
        				.append(", #ingre=").append(cant_ingresos)
        				.append(", permisos=[").append(permisos).append("]")
        				.append(", LiveTime=").append(getLiveSeconds()).append("sec ")
        				.append("]");
        	
        	ret = ret2.toString();
        }
        return ret;
    }

    public SimpleHash toHash() {
    	SimpleHash hash = new SimpleHash();
        hash.put("valido", valido);
        hash.put("nombre", name);
        if(foto != null) { 
        	foto = foto.toLowerCase();
        }
        
        String pRut = "";
		if(rut.getRut() != null &&  !"".equals(rut.getRut().trim()) ) {
			pRut += rut.getRut();
		}
		
		if(rut.getDig() != null &&  !"".equals(rut.getDig().trim()) ) {
			pRut += "-" + rut.getDig();
		}
		
		
        hash.put("foto", foto);
        hash.put("rol", rol);
        hash.put("pRut", pRut );
        hash.put("pRutId", rut.getRut());
        hash.put("pRutDig", rut.getDig());
        hash.put("pPassword", PassWord);
        hash.put("empresaPadre", this.empresa);
        hash.put("empresaDescrip", this.empresaDescrip);
        hash.put("cant_ingresos", String.valueOf(cant_ingresos));
        hash.put("cargo", cargo);
    	hash.put("valido", valido);
    	hash.put("unidad", unidad);
    	hash.put("empresa", empresa);
    	hash.put("jefeUnidad", jefeUnidad);
    	hash.put("email", email);
    	hash.put("emailValidado", emailValidado);
    	hash.put("usuarioImagen", getUsuarioImagen().getNameUnic());
    	
    	
        if(VerApp.size() > 0) {
            for(int x = 0; x < VerApp.size(); x++) {
            	hash.put(VerApp.get(x).toString(), true);
            }
        }
        
        for(String s : VerAppCarpetaElectronica) {
        	hash.put("zona_" + s, true);
        }
        
        for(String s : VerAppReportesOrganica) {
        	hash.put("zona_" + s, true);
        } 
        
        for(Map m : VerAppWebmatico ) {	
        	if("1".equals( m.get("imagen_visibilidad") )) {
        		String imagen = "webmatico_" + toFreeMakerFile((String)m.get("imagen_src"));
        		hash.put(imagen , "1");
        	}
        }
        
        return hash;
    }
    
    public String toFreeMakerFile(String filePath) {
    	if(filePath != null) {
    		if(filePath.lastIndexOf("/") != -1) {
    			filePath = filePath.substring(filePath.lastIndexOf("/")+1, filePath.length());	
    		}	
    		
    		filePath = filePath.replaceAll("\\.", "");
    	}
    	
    	return filePath;
    }
    
    public ConsultaData toConsultaData() {
    	List<String> nCols = new ArrayList<String>();
    	nCols.add("valido");
    	nCols.add("nombre");
    	nCols.add("foto");
    	nCols.add("rol");
    	nCols.add("valido");
    	nCols.add("valido");
    	
    	ConsultaData data = new ConsultaData(nCols);
    	DataFields fields = new DataFields();
    	
    	fields.put("valido", valido);
    	fields.put("nombre", name);
		if(foto != null) { 
		 	foto = foto.toLowerCase();
		}
		
		String pRut = "";
		if(rut.getRut() != null) {
			pRut += rut.getRut();
		}
		
		if(rut.getDig() != null) {
			pRut += "-" + rut.getDig();
		}
		
		fields.put("foto", foto);
		fields.put("rol", rol);
		fields.put("pRut", pRut );
		fields.put("pRutId", rut.getRut());
		fields.put("pRutDig", rut.getDig());
		fields.put("pPassword", PassWord);
		fields.put("empresaPadre", this.empresa);
		fields.put("empresaDescrip", this.empresaDescrip);
		fields.put("cant_ingresos", String.valueOf(cant_ingresos));
		fields.put("cargo", cargo);
		fields.put("valido", valido);
		fields.put("unidad", unidad);
		fields.put("empresa", empresa);
		fields.put("jefeUnidad", jefeUnidad);
		fields.put("email", email);
		fields.put("emailValidado", emailValidado);
		fields.put("usuarioImagen", getUsuarioImagen().getNameUnic());
     	
     	
	    data.add(fields);
	    
	    return data;
    }

    public SimpleHash toHash(Connection connection) {
        SimpleHash hash = new SimpleHash();
        hash.put("valido", valido);
        hash.put("nombre", name);
        if(foto != null) {
        	foto = foto.toLowerCase();
        }
        hash.put("foto", foto);
        hash.put("rol", rol);
        hash.put("pRut", String.valueOf(new StringBuilder(rut.getRut()).append("-").append(rut.getDig())));
        hash.put("pRutId", rut.getRut());
        hash.put("pRutDig", rut.getDig());
        hash.put("pPassword", PassWord);
        hash.put("cant_ingresos", String.valueOf(cant_ingresos));
        hash.put("cargo", cargo);
        
        if(VerApp.size() > 0) {
            for(int x = 0; x < VerApp.size(); x++) { 
            	hash.put(VerApp.get(x).toString(), true);
            }
        }
        return toHash2(connection,hash);
    }
    
    public SimpleHash toHash2(Connection connection, SimpleHash hash) {
    	hash.put("valido", valido);
    	hash.put("unidad", unidad);
    	hash.put("empresa", empresa);
        return hash;
    }
    
    public void valueBound(HttpSessionBindingEvent parm1) {
        OutMessage.OutMessagePrint("Inicio de Sesion --> SesionId. ".concat(String.valueOf(String.valueOf(parm1.getSession().getId()))));
        initdate = new Date();
    }

    public void valueUnbound(HttpSessionBindingEvent parm1) {
        OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("Fin de Sesion --> SesionId: ")).append(parm1.getSession().getId()).append(" usuario: ").append(toString()))));
    }

    public boolean veMultiplesRamas() {
        return verMultiplesRamas;
    }

    public VerUnidad[] getUnidadesVer() {
        return unidadesVer;
    }

    public VerUnidad[] getUnidadesRel() {
        return unidadesRel;
    }

    public String getEmpresa() {
        return empresa;
    }
    
    public String getEmpresaDescrip() {
        return empresaDescrip;
    }
    
	public String getPlanta() {
		return planta;
	}

    public String getUnidad() {
        return unidad;
    }

    public void setPassWord(String _PassWord) {
        PassWord = _PassWord;
    }

    public String getPassWord() {
        return PassWord;
    }
    
    public boolean tieneApp(String app) {
        boolean ok = false;
        try {
            if(VerApp.contains("app_".concat(String.valueOf(app)))  ||   
            		VerApp.contains( (String.valueOf(app)) )  ) {
            	ok = true;
            }

            OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("tieneApp(")).append(app).append(") --> ").append(ok))));
        }
        catch(NullPointerException e) {
        	return false;
        }
        return ok;
    }
    
    public boolean tieneApp(PermisoPortal p)
    {
        return tieneApp(p.toString());
    }

    public boolean tieneAccesoGestion(Connection conn, String rut) {
        
        boolean si = false;
        
        if(conn != null) {
            Consulta consul = new Consulta(conn);
            String sql = "select * from eje_generico_perfil_usuario where id_perfil = ".concat(String.valueOf(PerfilPortal.JefeUnidad)).concat(" and rut = ".concat(rut));
            consul.exec(sql);
            
            if(consul.next()) {
            	si = true;
            }
            consul.close();
        }
        
        return si;
    }
    
    
    /**
     * Metodo fue eliminado.  Utilizar portal.com.eje.serhumano.user.UserMgr
     * 
     * @deprecated
     * @param conn
     * @param rut
     * @return
     */

    public boolean addAccesoGestion(Connection conn, String rut) {
    	throw new ExceptionNotImplemented();
    }

    
    
    public boolean quitarAccesoGestionPorUnidad(Connection conn, String unidad, String rut) {
        
        boolean si = false;
        
        if(conn != null) {
            Consulta consul = new Consulta(conn);
            String sql = "delete from eje_generico_perfil_usuario where rut = '".concat( rut.concat("' and id_perfil = '-1'") );
            si = consul.insert(sql);
            consul.close();
        }
        
        return si;
    }
    
    public boolean quitarAccesoGestion(Connection conn, String rut) {
        
        boolean si = false;
        
        if(conn != null) {
        	ConsultaTool con = ConsultaTool.getInstance();
            String sql = "delete from eje_generico_perfil_usuario where rut = ? and id_perfil = ? ";
            String[] params = { rut, String.valueOf(PerfilPortal.JefeUnidad) };
            
            try {
				si = con.insert(conn, sql, params);
			} catch (SQLException e) {
				e.printStackTrace();
			}
        }
        
        return si;
    }

    public String getPasswordBrowser() {
        return this.passwordBrowser;
    }
    
	public UsuarioPerfil getPerfil() {
		return perfil;
	}
	
	public String getJndi() {
		return this.iJndiManager.getJndi(Integer.valueOf(this.getRutId()));
	}
	
	public String getJndi(String rut) {
		return this.iJndiManager.getJndi(Integer.valueOf(rut));
	}
	
	public String getIdUnidad() {
		return idUnidad;
	}

	public boolean grabaPass(Connection connPortal, String rut, String pass_a) {
    	boolean ok = false;
    	String queryGP = "SELECT count(*) existe FROM eje_ges_usuario_ult_acceso WHERE rut = '".concat(rut).concat("'");
        Consulta consultaGP = new Consulta(connPortal); 
        consultaGP.exec(queryGP);
        consultaGP.next();
        
        ResourceBundle proper = ResourceBundle.getBundle("db");
        String maximoPass = proper.getString("clave.maximoclavesalmacenadas");
        
        if(consultaGP.getString("existe").equals(maximoPass)) {
        	queryGP = "DELETE eje_ges_usuario_ult_acceso WHERE fecha_ult_acceso "
        	          .concat("IN (SELECT TOP 1 fecha_ult_acceso FROM eje_ges_usuario_ult_acceso where rut=").concat(rut)
        	          .concat(" order by fecha_ult_acceso asc) and rut=").concat(rut);
        	consultaGP.update(queryGP);
        }
        queryGP = "insert into eje_ges_usuario_ult_acceso(rut,fecha_ult_acceso,clave,clave_enc) "
        .concat("values('").concat(rut).concat("',getdate(),'").concat(pass_a.trim()).concat("','").concat(ManagerMD5.encrypt(pass_a.trim())).concat("')");

        if(consultaGP.insert(queryGP)) {
        	ok = true;
            //System.out.println("Registro ultima clave: OK");
        } 
        else {
        	//System.out.println("Registro ultima clave: ERROR");
        }
        
        consultaGP.close();
        return ok;
    }

	public Long getLiveSeconds() {
		return (Calendar.getInstance().getTimeInMillis()- sesionInit) / 1000L;
	}
	
	
	public String getEmail() {
		return email;
    }
	
	public String getEmailValidado() {
		return emailValidado;
    }
	
	/**
	 * @deprecated
	 * 
	 * Por Alexis, 12 Dic 2013, no deberá obtenerse el email con parametros.
	 */
	public String getEmail(Connection conn,String rut) {
		return getEmail();
    }
	


	
	
	public InfoUsuario getInfoUsuario() {
		return infoUsuario;
	}

	public void setInfoUsuario(InfoUsuario infoUsuario) {
		this.infoUsuario = infoUsuario;
	}
	
	public String getEmailFromTrabajador() {
		String sql = "SELECT e_mail FROM eje_ges_trabajador where rut=" + this.getRutId();

		ConsultaData data;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql);
			if (data != null && data.next()) {
				return data.getString("e_mail");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
    }

	public String getEmailFromTablaConfirmacion() {
		String e_mail = "";
		ConsultaData data;
		try {
			String sql = "select correo as e_mail from eje_ges_correo_confirmacion where rut=" + this.getRutId();
			data = ConsultaTool.getInstance().getData("portal", sql);
			if (data != null && data.next()) {
				e_mail = data.getString("e_mail");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "".equals(e_mail) ? null : e_mail;
	}
	
	/**
	 * @since 2016-03-29
	 * 
	 * Retorna el JNDI de un usuari en particular.
	 * 
	 * */
	
	public String getjndiConecction() {
		return "portal";
	}
	
	public String getLoginUsuario() {
		return this.usuario;
	}
	
	public VoUsaurioImagen getUsuarioImagen() {
		if(usuarioImagen !=  null) {
			return  this.usuarioImagen;	
		}
		else {
			return new VoUsaurioImagen();
		}
		
	}

	private boolean existeTablasWebmatico() {
		String sql = " SELECT top 1 1 FROM eje_generico_perfiles_rut_webmatico";
		boolean ok = false;
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql);
			ok = data != null && data.next();
		} catch (SQLException e) {
			ok = false; 
		}
		
		return ok;
	}
	
	private void loadPerfAppAccesos() {
		
	}
	
	
	private void loadAppCarpetaElectronica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" select id,op1,op2,op3,op4,op5,op6,op7 ");
		sql.append(" from eje_generico_perfiles_webmatico w  ");
		String wherePerfil		= (" where w.id = ? ");
		try {
			String[] fields = {"op1","op2","op3","op4","op5","op6","op7"};
			
			ConsultaData perfiles = PerfilLocator.getInstance().getPerfiles(getRutIdInt());
			
			if(perfiles != null && perfiles.next()) {
				Object[] params = {perfiles.getInt("id")};
				ConsultaData data = ConsultaTool.getInstance().getData("portal", sql.toString() + wherePerfil, params);
				 
				
				if(data != null && data.next()) {
					for(int i=0;i<fields.length;i++) {
						if(!"0".equals(data.getForcedString(fields[i]))) {
							VerAppCarpetaElectronica.add(fields[i]);	
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void loadAppReportesOrganica() {
		StringBuilder sql = new StringBuilder();
		sql.append("select id,costos,dotacion,cargos,tipo_contrato,costos_empresa,ingresos,egresos,resumen_in_out,saldo_vacaciones,estudios,grupo_familiar \n");
		sql.append("from  eje_generico_perfiles_webmatico w  \n");
		sql.append("where w.id = ? \n");
		
		try {
			ConsultaData dataPerfiles = PerfilLocator.getInstance().getPerfiles(getRutIdInt());
			if(dataPerfiles != null && dataPerfiles.next()) {
				String[] fields = {"costos","dotacion","cargos","tipo_contrato","costos_empresa","ingresos","egresos","resumen_in_out","saldo_vacaciones","estudios","grupo_familiar"};
				Object[] params = {dataPerfiles.getInt("id")};
				
				ConsultaData data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
			
				if(data != null && data.next()) {
					for(int i=0;i<fields.length;i++) {
						if(!"0".equals(data.getForcedString(fields[i])) && !"false".equals(data.getForcedString(fields[i]) )) {
							VerAppReportesOrganica.add(fields[i]);	
						}
					}
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println("REPORTES ORGANICA :" + VerAppReportesOrganica.size() + " atribuciones" );
	}
	
	
	/**
	 * 
	 * retorna los app_id de  eje_generico_webmatico_ejegesuserapp
	 * @since 16-05-2018
	 * @author Pancho
	 * */
	
	private List<String> getAppIdFromWebmatico() {
		List<String> lista = new ArrayList<String>();
		
		
		ConsultaData perfiles = PerfilLocator.getInstance().getPerfiles(getRutIdInt());
		try {
			if( perfiles != null && perfiles.next()) {
				/*SOLO EL PRIMER PERFIL */
				int id = perfiles.getInt("id");
				
				String sqlAppId = " SELECT * FROM eje_generico_webmatico_ejegesuserapp where id_perfil = ? ";
				Object[] params = {id};
				ConsultaData dataAppId = ConsultaTool.getInstance().getData("portal", sqlAppId, params);
				
				
				while(dataAppId != null && dataAppId.next()) {
					lista.add(dataAppId.getForcedString("app_id"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	 
		return lista;
	}
	
	private void loadAppWebmatico() {
		ConsultaData data = PerfilLocator.getInstance().getWebmaticoAtribuciones(getRutIdInt());
		
		boolean entro = false;
		
		while(data != null && data.next()) {
			entro = true;
			Map<String, String> map = new HashMap<String, String>();
			
			map.put("imagen_src"		, data.getForcedString("imagen_src"));
			map.put("imagen_visibilidad", "true".equals(data.getForcedString("imagen_visibilidad")) ? "1" : "0");
			
			VerAppWebmatico.add(map);
		}
		
		MyString my = new MyString();
		
		if(!entro) {
			for( int i = 1 ; i<= 26; i++) {
				Map<String, String> map = new HashMap<String, String>();
				
				map.put("imagen_src"		, "images/panel_control_"+my.rellenaCadena(String.valueOf(i), '0', 2)+".gif");
				map.put("imagen_visibilidad", "1");
				
				VerAppWebmatico.add(map);
			}
			 
		}
			
	 
		System.out.println("WEBMATICO :" + VerAppWebmatico.size() + " atribuciones" );
	}

	
	private void loadPaAccesos(VoPersona persona) {
		
		List<voZona> zonas = PerfApp.getInstance().getZona(persona);
		
		Map<String, Map<String, ArrayList<Object>>> mapZona = new HashMap<String, Map<String, ArrayList<Object>>>();
		for (voZona zona: zonas) {
			Map<String, ArrayList<Object>> mapGrupo = new HashMap<String, ArrayList<Object>>();
			for (voGrupo grupo: zona.getGrupos()) {
				ArrayList<Object> mapObjetos = new ArrayList<Object>();
				for (voObjeto objeto: grupo.getObjetos()) {
					mapObjetos.add(objeto.getCodigo());
				}
				mapGrupo.put(grupo.getCodigo(), mapObjetos);
			}
			mapZona.put(zona.getCodigo(), mapGrupo);
		}
		
		paAccesos.add(mapZona);
	
	}
	
	public List<String> getPrivilegio_reportesOrganica_zonaEstructura() {
		List<String> result = new ArrayList<String>();
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select id,");
		sql.append(" 	costos				=miestruc_costos,");
		sql.append(" 	dotacion			=miestruc_dotacion,");
		sql.append(" 	cargos				=miestruc_cargos,");
		sql.append(" 	tipo_contrato		=miestruc_tipo_contrato,");
		sql.append(" 	costos_empresa		=miestruc_costos_empresa,");
		sql.append(" 	ingresos			=miestruc_ingresos,");
		sql.append(" 	egresos				=miestruc_egresos,");
		sql.append(" 	resumen_in_out		=miestruc_resumen_in_out,");
		sql.append(" 	saldo_vacaciones	=miestruc_saldo_vacaciones, ");
		sql.append(" 	estudios			=miestruc_estudios, ");
		sql.append(" 	grupo_familiar		=miestruc_grupo_familiar ");
		sql.append(" from eje_generico_perfiles_webmatico w ");
		sql.append(" WHERE id = ? ");
		
		try {
			ConsultaData dataPerfiles = PerfilLocator.getInstance().getPerfiles(getRutIdInt());
			
			if(dataPerfiles != null && dataPerfiles.next()) {
				String[] fields = {"costos","dotacion","cargos","tipo_contrato","costos_empresa","ingresos","egresos","resumen_in_out","saldo_vacaciones","estudios","grupo_familiar"};
				Object[] params = {dataPerfiles.getInt("id")};
				ConsultaData data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
				
				if(data != null && data.next()) {
					for(int i=0;i<fields.length;i++) {
						if(data.getForcedString(fields[i]) != null && !"0".equals(data.getForcedString(fields[i])) && !"false".equals(data.getForcedString(fields[i]) )) {
							result.add(fields[i]);	
						}
					}
				}
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public List<String> getPrivilegio_carpetaElectronica() {
		List<String> lista = new ArrayList<String>();
		
		for(String l : VerAppCarpetaElectronica) {
			lista.add(l);
		}
		
		return lista;
	}
	
	public List<String> getPrivilegio_reportesOrganica() {
		List<String> lista = new ArrayList<String>();
		
		for(String l : VerAppReportesOrganica) {
			lista.add(l);
		}
		
		return lista;
	}
	
	public List<Map<String,String>> getPrivilegio_zonasWebmatico() {
		List<Map<String,String>> lista = new ArrayList<Map<String,String>>();
		
		for(Map<String,String> m : VerAppWebmatico) {
			lista.add(m);
		}
		
		return lista;
	}
	
	public boolean havePrivilegio_zonasWebmatico(String zona) {
		List<Map<String,String>> lista = getPrivilegio_zonasWebmatico();
		
		for(Map<String,String> m : lista) {
			if(m != null && "1".equals(m.get("imagen_visibilidad")) ) {
				String permiso = m.get("imagen_src");
				if(zona != null && permiso != null) {
					if( permiso.indexOf(zona) != -1 ) {
						return true;
					}
				}
			}
		}
		
		return false;
	}

	public boolean getExistePermisoByRut(String permiso) {
		boolean resultado = false;
		Object[] params = { rut.getRutId(),permiso };
		StringBuffer sql = new StringBuffer("select app_id from eje_ges_user_app where rut_usuario=? and app_id=? ");
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal",sql.toString(), params);
			if(data.next()) {
				resultado = true;
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return resultado;
	}
	public boolean addExtraValue(String key, Object o ) {
		if(o == null) {
			o = "";
		}
		return addExtraValue(key, o, 0);
	}
	
	public boolean addExtraValue(String key, Object o, double millisecondToLive) {
		map.put(key, new UsuarioValue(o, millisecondToLive));
		return true;
	}
	
	public Object getExtraValue(String key) {
		return getExtraValue(key, 0);
	}
	
	public Object getExtraValue(String key, double millisecondMaxOld) {
		UsuarioValue val = map.get(key);
		if(val != null) {
			 return val.getObject(millisecondMaxOld);
		}
		
		return null;
	}
	
	public String getGerencia() {
		ConsultaData data = OrganicaLocator.getInstance().getUnidadesAscendientes(this.getIdUnidad());
		StringBuilder str =  new StringBuilder();
		
		while(data != null && data.next()) {
			String u = data.getString("unid_desc");
			if( u != null && u.toLowerCase().indexOf("gerencia") != -1) {
				if(str.length() > 0) {
					str.append(" > ");
				}
				u = WordUtils.capitalizeFully(u);
				str.append(u);
			}
		}
		
		return str.toString();
	}
	
	
}