package portal.com.eje.serhumano.certificados;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.carpelect.mgr.ManagerTrabajador;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.datosdf.datosRut;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.menu.menuManager;
import portal.com.eje.serhumano.menu.bean.FichaPersonalBean;
import portal.com.eje.serhumano.menu.vo.EjeGesEmpresa;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.Formatear;
import portal.com.eje.tools.Tools;
import portal.com.eje.tools.Validar;
import portal.com.eje.tools.servlet.GetImagenEmpresa;
import cl.ejedigital.consultor.ConsultaData;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class S_certificados extends MyHttpServlet {

    public S_certificados() {

    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
        doGet(req, resp);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
    	
    	user = SessionMgr.rescatarUsuario(req);
//    	MMA 20170111
//    	Connection Conexion =  connMgr.getConnection(user.getJndi());
    	Connection Conexion =  getConnMgr().getConnection(user.getJndi());
        
        int cual = Integer.parseInt(req.getParameter("certificado"));
        String periodo = req.getParameter("peri_liq");
        String rutOrg = req.getParameter("rut_org")==null? user.getRutId() : req.getParameter("rut_org");
        
        if(Conexion != null) {
            if(user.esValido())
                switch(cual) {
                	case 13:
                		Antiguedad(req, resp, cual, Conexion);
                		super.insTracking(req, "Cert. Antiguedad".intern(), periodo);
                		break;
                	case 18:
                		Liquidacion(req, resp, Conexion, cual,rutOrg);
                		super.insTracking("/servlet/Certificado", req, "Liquidación de Sueldo".intern(), periodo);
                		break;
                	case 19:
                		Antiguedad3Meses(req, resp, cual, Conexion);;
                		super.insTracking(req, "Cert. de Renta".intern(), periodo);
                		break;
                	case 20:
                		LiquidacionAdic(req, resp, Conexion, cual,rutOrg);
                		super.insTracking(req, "Liquidación Adic".intern(), periodo);
                		break;
                	case 21:
                		SueldoBruto(req, resp, cual, Conexion);
                		super.insTracking(req, "Cert. Sueldo Bruto".intern(), periodo);
                		break;
                	 case 24: //certificado rentas sii
                         RentasSIINew(req, resp, cual, Conexion,rutOrg);
                         insTracking(req, "Cert.SII".intern(), periodo);
                         break;
                }
            else
                mensaje.devolverPaginaSinSesion(resp, "Certificados", "Tiempo de Sesi\363n expirado...");
        } 
        else {
            mensaje.devolverPaginaMensage(resp, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
        }
        
//    	MMA 20170111
//        connMgr.freeConnection(user.getJndi(), Conexion);
        getConnMgr().freeConnection(user.getJndi(), Conexion);
        
    }

    
    public void Liquidacion(HttpServletRequest req, HttpServletResponse resp, Connection Conexion, int opcion,String rutOrg)
    	throws ServletException, IOException {
    	
    	String scheme = req.getScheme();            
		String serverName = req.getServerName();     
		int serverPort = req.getServerPort();        
		String contextPath = req.getContextPath();   
		String urlBase = scheme+"://"+serverName+":"+serverPort+contextPath;
    	String rut = ( !rutOrg.equals("false") )? rutOrg : user.getRutId();
    	String periodo = req.getParameter("peri_liq");
    	FichaPersonalBean fp = FichaPersonalBean.getInstance();
    	SimpleHash modelRoot = fp.getDatosLiquidacionMultEmpresas(Conexion,getServletContext().getRealPath(getTemplatePath()),periodo,opcion,rut, req);
    	String prefijo = proper.getString("url");
        modelRoot.put("prefijo", prefijo + "Logo");
        System.out.println("Prefijo para PDF====> " + prefijo);
        modelRoot.put("urlBase", urlBase);
        modelRoot.put("periodo", periodo);
        modelRoot.put("rut", user.getRutId());
        
        IOClaseWeb io = new IOClaseWeb(this,  req, resp);
        super.retTemplate(io,"certificados/liquida_trab.html",modelRoot);       
    }
    
    public void LiquidacionAdic(HttpServletRequest req, HttpServletResponse resp,Connection Conexion, int opcion,String rutOrg) 
		throws ServletException, IOException {
	    String rut = ( !rutOrg.equals("false") )? rutOrg : user.getRutId();
	    String periodo = req.getParameter("peri_liq_adic");
    	FichaPersonalBean fp = FichaPersonalBean.getInstance();
    	SimpleHash modelRoot = fp.getDatosLiquidacionAdic(Conexion,getServletContext().getRealPath(getTemplatePath()),opcion,periodo,rut, req); 
    	String prefijo = proper.getString("url");
    	modelRoot.put("prefijo", prefijo);
    	
    	 IOClaseWeb io = new IOClaseWeb(this,  req, resp);
    	super.retTemplate(io,"certificados/liquida_trab.html",modelRoot);
    }


    public void RentasSIINew(HttpServletRequest req, HttpServletResponse resp, int certif, Connection Conexion, String rut)
    throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html");
        resp.setHeader("Expires", "0");
        resp.setHeader("Pragma", "no-cache");
        SimpleHash modelRoot = new SimpleHash();
        
        Validar valida = new Validar();
        Certif_Manager detalle_cert = new Certif_Manager(Conexion);
        
        String dv = detalle_cert.GetDVRut(rut);
        String ruts = Formatear.numero(Integer.parseInt(rut)) + "-" + valida.validarDato(dv);
        String rutg = rut;
        
        String strFechaHoy = "";
        GregorianCalendar Fecha = new GregorianCalendar();
        int dia = Fecha.get(5);
        int ano = Fecha.get(1);
        int mes = Fecha.get(2) + 1;
        strFechaHoy = String.valueOf(String.valueOf((new StringBuilder("")).append(dia).append(" de ").append(Tools.RescataMes(mes)).append(" de ").append(ano)));
        
        String nombre = valida.validarDato(detalle_cert.GetNombreRut(rut)); 
        
        String periodo = detalle_cert.GetPeriodoSIIRut(rut);
        String opcion ="1";  // S : 1: rentas, H : 2 : honorarios
        String empresa = detalle_cert.GetEmpresaRut(rut);        
        String numcer=null,titulo=null,nomEmpresa = null,rutEmpresa = null,dirEmpresa = null,giroEmpresa = null, rutEmpresa2=null, rutEmpresa3 = null;
        
        ConsultaData planta = detalle_cert.GetDatosPlantaRentasSIINew(empresa);        
        
        if (planta.next()) {  
        	nomEmpresa = valida.validarDato(planta.getString("nombre"));
            rutEmpresa = valida.validarDato(planta.getString("rut"));
            dirEmpresa = valida.validarDato(planta.getString("dir")); 
            giroEmpresa = valida.validarDato(planta.getString("giro"));
            rutEmpresa3 = valida.validarDato(planta.getForcedString("numrut"));
            rutEmpresa2 = Formatear.numero(Integer.parseInt(valida.validarDato(planta.getForcedString("numrut")))) + "-" + valida.validarDato(planta.getForcedString("digrut"));
            modelRoot.put("empresa", nomEmpresa);
            modelRoot.put("rutempresa2", rutEmpresa2);
            modelRoot.put("rutempresa", rutEmpresa);
            modelRoot.put("direccion", dirEmpresa);
            modelRoot.put("giro", giroEmpresa);
            modelRoot.put("fecha", strFechaHoy);
        }

        SimpleList LineaList = new SimpleList();
    	SimpleHash LineaIter;

        ConsultaData detalle = detalle_cert.GetDetalleRentaSIINew(rutg,periodo,opcion,rutEmpresa3);
        if(opcion.equals("1")) {  
           int tot2=0,tot3=0,tot4=0,tot5=0,tot6=0,tot7=0,tot8=0,tot10=0,tot11=0,tot12=0,tot13=0,tot14=0;
           for(;detalle.next();LineaList.add(LineaIter)) {  
        	  LineaIter = new SimpleHash();
              LineaIter.put("col1",Tools.RescataMes(Integer.parseInt(valida.validarDato(detalle.getForcedString("mes")))) );
              LineaIter.put("col2",Formatear.numero(Integer.parseInt(valida.validarDato(detalle.getForcedString("tothab")))) );
              LineaIter.put("col3",Formatear.numero(Integer.parseInt(valida.validarDato(detalle.getForcedString("llss"))))  );
              LineaIter.put("col4",Formatear.numero(Integer.parseInt(valida.validarDato(detalle.getForcedString("btrib")))) );
              LineaIter.put("col5",Formatear.numero(Integer.parseInt(valida.validarDato(detalle.getForcedString("impto")))) );
              LineaIter.put("col6",Formatear.numero(Integer.parseInt(valida.validarDato(detalle.getForcedString("mayor_retencion"))))  );
              LineaIter.put("col7",Formatear.numero(Integer.parseInt(valida.validarDato(detalle.getForcedString("nint"))))  );
              LineaIter.put("col8",Formatear.numero(Integer.parseInt(valida.validarDato(detalle.getForcedString("zona"))))  );
              LineaIter.put("col9",valida.validarDato(detalle.getForcedString("factor").replace('.',',')) );
              LineaIter.put("col10",Formatear.numero(Integer.parseInt(valida.validarDato(detalle.getForcedString("c1")))) );
              LineaIter.put("col11",Formatear.numero(Integer.parseInt(valida.validarDato(detalle.getForcedString("c2")))) );
              LineaIter.put("col12",Formatear.numero(Integer.parseInt(valida.validarDato(detalle.getForcedString("c3")))) );
              LineaIter.put("col13",Formatear.numero(Integer.parseInt(valida.validarDato(detalle.getForcedString("c4")))) );
              LineaIter.put("col14",Formatear.numero(Integer.parseInt(valida.validarDato(detalle.getForcedString("c5")))) );
              numcer=valida.validarDato(detalle.getForcedString("numcer"));
              tot2= tot2+ Integer.parseInt(valida.validarDato(detalle.getForcedString("tothab")));
              tot3= tot3+ Integer.parseInt(valida.validarDato(detalle.getForcedString("llss")));
              tot4= tot4+ Integer.parseInt(valida.validarDato(detalle.getForcedString("btrib")));
              tot5= tot5+ Integer.parseInt(valida.validarDato(detalle.getForcedString("impto")));
              tot6= tot6+ Integer.parseInt(valida.validarDato(detalle.getForcedString("mayor_retencion")));
              tot7= tot7+ Integer.parseInt(valida.validarDato(detalle.getForcedString("nint")));
              tot8= tot8+ Integer.parseInt(valida.validarDato(detalle.getForcedString("zona")));
              tot10= tot10+ Integer.parseInt(valida.validarDato(detalle.getForcedString("c1")));
              tot11= tot11+ Integer.parseInt(valida.validarDato(detalle.getForcedString("c2")));
              tot12= tot12+ Integer.parseInt(valida.validarDato(detalle.getForcedString("c3")));
              tot13= tot13+ Integer.parseInt(valida.validarDato(detalle.getForcedString("c4")));
              tot14= tot14+ Integer.parseInt(valida.validarDato(detalle.getForcedString("c5")));
            }
        	modelRoot.put("opcion1",opcion);
            titulo="CERTIFICADO SOBRE SUELDOS, PENSIONES O JUBILACIONES Y OTRAS RENTAS SIMILARES";
            modelRoot.put("tot2", Formatear.numero(Integer.parseInt(String.valueOf(tot2))) );
            modelRoot.put("tot3", Formatear.numero(Integer.parseInt(String.valueOf(tot3))) );
            modelRoot.put("tot4", Formatear.numero(Integer.parseInt(String.valueOf(tot4))) );
            modelRoot.put("tot5", Formatear.numero(Integer.parseInt(String.valueOf(tot5))) );
            modelRoot.put("tot6", Formatear.numero(Integer.parseInt(String.valueOf(tot6))) );
            modelRoot.put("tot7", Formatear.numero(Integer.parseInt(String.valueOf(tot7))) );
            modelRoot.put("tot8", Formatear.numero(Integer.parseInt(String.valueOf(tot8))) );
            modelRoot.put("tot9","&nbsp;" );
            modelRoot.put("tot10", Formatear.numero(Integer.parseInt(String.valueOf(tot10))) );
            modelRoot.put("tot11", Formatear.numero(Integer.parseInt(String.valueOf(tot11))) );
            modelRoot.put("tot12", Formatear.numero(Integer.parseInt(String.valueOf(tot12))) );
            modelRoot.put("tot13", Formatear.numero(Integer.parseInt(String.valueOf(tot13))) );
            modelRoot.put("tot14", Formatear.numero(Integer.parseInt(String.valueOf(tot14))) );
        }
        else if(opcion.equals("2")) {  
           int tot2=0,tot3=0,tot5=0,tot6=0;
           for(;detalle.next();LineaList.add(LineaIter)) {  
        	  LineaIter = new SimpleHash();
              LineaIter.put("col1",Tools.RescataMes(Integer.parseInt(valida.validarDato(detalle.getForcedString("mes")))) );           
              LineaIter.put("col2",Formatear.numero(Integer.parseInt(valida.validarDato(detalle.getForcedString("btrib")))) );
              LineaIter.put("col3",Formatear.numero(Integer.parseInt(valida.validarDato(detalle.getForcedString("impto")))) );
              LineaIter.put("col4",valida.validarDato(detalle.getForcedString("factor").replace('.',',')) );
              LineaIter.put("col5",Formatear.numero(Integer.parseInt(valida.validarDato(detalle.getForcedString("c1")))) );
              LineaIter.put("col6",Formatear.numero(Integer.parseInt(valida.validarDato(detalle.getForcedString("c2")))) );
              tot2= tot2+ Integer.parseInt(valida.validarDato(detalle.getForcedString("btrib")));
              tot3= tot3+ Integer.parseInt(valida.validarDato(detalle.getForcedString("impto")));
              tot5= tot5+ Integer.parseInt(valida.validarDato(detalle.getForcedString("c1")));
              tot6= tot6+ Integer.parseInt(valida.validarDato(detalle.getForcedString("c2")));
              numcer=valida.validarDato(detalle.getForcedString("numcer"));
           }
  	       modelRoot.put("opcion2",opcion);
           titulo="CERTIFICADO SOBRE HONORARIOS";
           modelRoot.put("tot2", Formatear.numero(Integer.parseInt(String.valueOf(tot2))) );
           modelRoot.put("tot3", Formatear.numero(Integer.parseInt(String.valueOf(tot3))) );
           modelRoot.put("tot4","&nbsp;" );           
           modelRoot.put("tot5", Formatear.numero(Integer.parseInt(String.valueOf(tot5))) );
           modelRoot.put("tot6", Formatear.numero(Integer.parseInt(String.valueOf(tot6))) );
        }
        else if(opcion.equals("3")) {  
           int tot2=0,tot3=0,tot4=0,tot5=0,tot7=0,tot8=0,tot9=0,tot10=0;
           for(;detalle.next();LineaList.add(LineaIter)) {  
        	  LineaIter = new SimpleHash();
              LineaIter.put("col1",Tools.RescataMes(Integer.parseInt(valida.validarDato(detalle.getString("mes")))) );           
              LineaIter.put("col2",valida.validarDato(detalle.getString("btrib")) );
              LineaIter.put("col3",valida.validarDato(detalle.getString("poa1")) );
              LineaIter.put("col4",valida.validarDato(detalle.getString("impto")) );
              LineaIter.put("col5",valida.validarDato(detalle.getString("poa2")) );
              LineaIter.put("col6",valida.validarDato(detalle.getString("factor")) );
              LineaIter.put("col7",valida.validarDato(detalle.getString("c1"))  );
              LineaIter.put("col8",valida.validarDato(detalle.getString("c2")) );
              LineaIter.put("col9",valida.validarDato(detalle.getString("c3"))  );
              LineaIter.put("col10",valida.validarDato(detalle.getString("c4"))  );
              tot2= tot2+ Integer.parseInt(valida.validarDato(detalle.getString("btrib")));
              tot3= tot3+ Integer.parseInt(valida.validarDato(detalle.getString("poa1")));
              tot4= tot4+ Integer.parseInt(valida.validarDato(detalle.getString("impto")));              
              tot5= tot5+ Integer.parseInt(valida.validarDato(detalle.getString("poa2")));
              tot7= tot7+ Integer.parseInt(valida.validarDato(detalle.getString("c1")));
              tot8= tot8+ Integer.parseInt(valida.validarDato(detalle.getString("c2")));
              tot9= tot9+ Integer.parseInt(valida.validarDato(detalle.getString("c3")));
              tot10= tot10+ Integer.parseInt(valida.validarDato(detalle.getString("c4")));
              numcer=valida.validarDato(detalle.getString("numcer"));
           }
           modelRoot.put("opcion3",opcion);
           titulo="CERTIFICADO SOBRE HONORARIOS Y PARTICIPACIONES O ASIGNACIONES A DIRECTORES PAGADOS POR SOCIEDADES ANONIMAS";
           modelRoot.put("tot2", String.valueOf(tot2) );
           modelRoot.put("tot3", String.valueOf(tot3) );
           modelRoot.put("tot4", String.valueOf(tot4) );
           modelRoot.put("tot5", String.valueOf(tot5) );
           modelRoot.put("tot6","&nbsp;" );
           modelRoot.put("tot7", String.valueOf(tot7) );
           modelRoot.put("tot8", String.valueOf(tot8) );
           modelRoot.put("tot9", String.valueOf(tot9) );           
           modelRoot.put("tot10", String.valueOf(tot10) );
        }
        modelRoot.put("detalle",LineaList);
        modelRoot.put("periodo",periodo);
        modelRoot.put("nombre",nombre);
        modelRoot.put("rut",ruts);
        modelRoot.put("num",numcer);
        modelRoot.put("titulo",titulo);
        
        IOClaseWeb io = new IOClaseWeb(this,  req, resp);
        super.retTemplate(io,"certificados/rentaSIINew.htm",modelRoot);
        
        out.flush();
        out.close();
    }
    
    public void Viaje(HttpServletRequest req, HttpServletResponse resp, Connection Conexion, int certif)
    	throws ServletException, IOException {
    	String html = "";
    	switch(certif) {
    		case 16:
    			html = "vacaNormal.htm";
    			break;
    		case 17:
    			html = "vacaFilial.htm";
    			break;
    	}
        FichaPersonalBean fp = FichaPersonalBean.getInstance();
        SimpleHash modelRoot = fp.getDatosViaje(Conexion,certif,"","","","","","","","","","","",user.getRutId());
        
        IOClaseWeb io = new IOClaseWeb(this,  req, resp);
        super.retTemplate(io,"certificados/".concat(String.valueOf(String.valueOf(html))),modelRoot);
    }
    
    public void Practica(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
		throws ServletException, IOException {
        FichaPersonalBean fp = FichaPersonalBean.getInstance();
        SimpleHash modelRoot = fp.getDatosPractica(Conexion,user.getRutId()); 
        
        IOClaseWeb io = new IOClaseWeb(this,  req, resp);
        super.retTemplate(io,"practica.htm",modelRoot);
    }
    
    public void Cursos(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
		throws ServletException, IOException {
        FichaPersonalBean fp = FichaPersonalBean.getInstance();
        SimpleHash modelRoot = fp.getDatosCursos(Conexion,user.getRutId()); 
        
        IOClaseWeb io = new IOClaseWeb(this,  req, resp);
        super.retTemplate(io,"Cursos.htm",modelRoot);
    }
    
    public void Estudios(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
    	throws ServletException, IOException {
        FichaPersonalBean fp = FichaPersonalBean.getInstance();
        SimpleHash modelRoot = fp.getDatosEstudios(Conexion,user.getRutId()); 
        
        IOClaseWeb io = new IOClaseWeb(this,  req, resp);
        super.retTemplate(io,"Estudios.htm",modelRoot);
    }    
    
    public void RentaBruta(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException {
        FichaPersonalBean fp = FichaPersonalBean.getInstance();
        SimpleHash modelRoot = fp.getDatosRentaBruta(Conexion,user.getRutId()); 
        
        IOClaseWeb io = new IOClaseWeb(this,  req, resp);
        super.retTemplate(io,"RentaBruta.htm",modelRoot);
    }


    public void Isapre(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException {
        FichaPersonalBean fp = FichaPersonalBean.getInstance();
        SimpleHash modelRoot = fp.getDatosIsapre(Conexion,user.getRutId()); 
        
        IOClaseWeb io = new IOClaseWeb(this,  req, resp);
        super.retTemplate(io,"certificado.htm",modelRoot);
    }

    public void Antiguedad(HttpServletRequest req, HttpServletResponse resp, int certif, Connection Conexion)
        throws ServletException, IOException {
        String html = "";
        switch(certif) {
        case 3:
            html = "Antiguedad.htm";
            break;
        case 4:
            html = "AntCargoRta.htm";
            break;
        case 5:
            html = "AntCargo.htm";
            break;
        case 12:
            html = "Certif_1.htm";
            break;
        case 13:
            html = "Certif_2.htm";
            break;
        case 14:
            html = "Certif_3.htm";
            break;
        case 15:
            html = "Certif_4.htm";
            break;
        case 16:
            html = "AntCargo.htm";
            break;
        case 19:
            html = "Certif_5.htm";
            break;
        }

        FichaPersonalBean fp = FichaPersonalBean.getInstance();
        String scheme = req.getScheme();            
		String serverName = req.getServerName();     
		int serverPort = req.getServerPort();        
		String contextPath = req.getContextPath();   
		String urlBase = scheme+"://"+serverName+":"+serverPort+contextPath;
		
		String rut_org = null;
		if(req.getParameter("rut_org") != null) {
			rut_org = req.getParameter("rut_org");
		}
		else {
			rut_org = user.getRutId();
		}
        SimpleHash modelRoot = fp.getDatosAntiguedad(certif,Conexion,rut_org);
        String prefijo = this.proper.getString("url");
        String prefijoFirma = prefijo + "Firma";
        modelRoot.put("prefijo", prefijo + "Logo");
        modelRoot.put("prefijoFirma", prefijoFirma);
        modelRoot.put("rut", user.getRutId());
        modelRoot.put("urlBase", urlBase);
        
        ConsultaData data = ManagerTrabajador.getInstance().getTrabajador(rut_org);
        if(data.next()) {
        	modelRoot.put("GetImagenEmpresaDestino", new GetImagenEmpresa(data.getString("empresa")));
        }
        
        IOClaseWeb io = new IOClaseWeb(this,  req, resp);
        super.retTemplate(io,"certificados/".concat(String.valueOf(String.valueOf(html))),modelRoot);
    }
    
    public void SueldoBruto(HttpServletRequest req, HttpServletResponse resp, int certif, Connection Conexion)
        throws ServletException, IOException {
        String html = "sueldoBruto.htm";

        FichaPersonalBean fp = FichaPersonalBean.getInstance();
        String scheme = req.getScheme();            
		String serverName = req.getServerName();     
		int serverPort = req.getServerPort();        
		String contextPath = req.getContextPath();   
		String urlBase = scheme+"://"+serverName+":"+serverPort+contextPath;
		
		String rut_org = null;
		if(req.getParameter("rut_org") != null) {
			rut_org = req.getParameter("rut_org");
		}
		else {
			rut_org = user.getRutId();
		}
		
		
		menuManager menu = new menuManager();
		EjeGesEmpresa empresa = menu.getEjeGesEmpresa(cl.ejedigital.tool.validar.Validar.getInstance().validarInt(rut_org,-1));
        String prefijo = this.proper.getString("url");
        String prefijoFirma = prefijo + "Firma";
        
        SimpleHash modelRoot = fp.getDatosAntiguedad(certif,Conexion,rut_org);
        
        modelRoot.put("prefijo", prefijo + "Logo");
        modelRoot.put("prefijoFirma", prefijoFirma);
        modelRoot.put("rut", user.getRutId());
        modelRoot.put("empresa_rut",empresa.getEmpreRut());
        modelRoot.put("empresa_representante_nom",empresa.getNomRep());
        modelRoot.put("empresa_representante_rut",empresa.getRutRepr());
        modelRoot.put("urlBase", urlBase);
        modelRoot.put("GetImagenEmpresaDestino", new GetImagenEmpresa(empresa.getEmpresa()));
        
        ConsultaData info5 = menu.getDatosCertificadoRentaCabecera(cl.ejedigital.tool.validar.Validar.getInstance().validarInt(rut_org,-1));
        if(info5 != null && info5.next()) {
        	modelRoot.put(	"tot_haberes"	,	Tools.setFormatNumber(info5.getForcedString("tot_haberes"))	);
        }
        

        

        IOClaseWeb io = new IOClaseWeb(this,  req, resp);
        super.retTemplate(io,"certificados/sueldoBruto.htm",modelRoot);
    }
    
    public void Antiguedad3Meses(HttpServletRequest req, HttpServletResponse resp, int certif, Connection Conexion)
        throws ServletException, IOException {
        String html = "";
        switch(certif) {
        	case 3:
        		html = "Antiguedad.htm";
        		break;
        	case 4:
        		html = "AntCargoRta.htm";
        		break;
        	case 5:
        		html = "AntCargo.htm";
        		break;
        	case 12:
        		html = "Certif_1.htm";
        		break;
        	case 13:
        		html = "Certif_2.htm";
        		break;
        	case 14:
        		html = "Certif_3.htm";
        		break;
        	case 15:
        		html = "Certif_4.htm";
        		break;
        	case 16:
        		html = "AntCargo.htm";
        		break;
        	case 19:
        		html = "Certif_5.htm";
        		break;
        }
        FichaPersonalBean fp = FichaPersonalBean.getInstance();
        SimpleHash modelRoot = fp.getDatosAntiguedad3Meses(certif,Conexion,user.getRutId()); 
        
        IOClaseWeb io = new IOClaseWeb(this,  req, resp);
        super.retTemplate(io,"certificados/".concat(String.valueOf(String.valueOf(html))),modelRoot);
    }
    
    ResourceBundle proper = ResourceBundle.getBundle("db");
    private Usuario user;
    private Mensaje mensaje;
    private datosRut userRut;
}