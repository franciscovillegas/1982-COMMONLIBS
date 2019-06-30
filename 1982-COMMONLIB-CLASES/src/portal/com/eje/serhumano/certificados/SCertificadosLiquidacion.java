package portal.com.eje.serhumano.certificados;

import java.io.IOException;
import java.sql.Connection;
import java.util.Calendar;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.carpelect.mgr.ManagerTrabajador;
import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.serhumano.menu.bean.FichaPersonalBean;
import portal.com.eje.tools.servlet.GetImagenEmpresa;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.misc.Formatear;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.FreemakerTool;
import cl.ejedigital.web.datos.DBConnectionManager;
import freemarker.template.SimpleHash;

public class SCertificadosLiquidacion extends AbsClaseWeb {

	public SCertificadosLiquidacion(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doPost() throws Exception {
		String certificado = super.getIoClaseWeb().getParamString("certificado",null);
		Connection conn = DBConnectionManager.getInstance().getConnection("portal");
		String rutOrg = super.getIoClaseWeb().getParamString("rut_org",null);
		if(rutOrg == null || "false".equals(rutOrg)) {
			rutOrg = super.getIoClaseWeb().getUsuario().getRutId();
		}
		else {
			rutOrg = Validar.getInstance().validarDato(rutOrg,"-1");
		}
		
		String periodo = super.getIoClaseWeb().getParamString("periodo",null);
		 
		try {
			if("LIQREMUN_PRINCIPAL".equals(certificado)) {
				Liquidacion(super.getIoClaseWeb().getReq(), super.getIoClaseWeb().getResp(),conn,rutOrg,periodo);

				super.getIoClaseWeb().insTracking( "Liquidacion de Sueldo".intern(), periodo);
			}
			else {
				LiquidacionAdic(super.getIoClaseWeb().getReq(), super.getIoClaseWeb().getResp(), conn, rutOrg,periodo);
				super.getIoClaseWeb().insTracking( "Liquidacion Adic".intern(), periodo);
			}
		}
		catch (Exception e) {
			
		}
		finally {
			DBConnectionManager.getInstance().freeConnection("portal", conn);
		}
		
	}

	@Override
	public void doGet() throws Exception {
		


		
	}

	public void LiquidacionAdic(HttpServletRequest req, HttpServletResponse resp,Connection Conexion, String rut, String periodo) 
	throws ServletException, IOException {

		FichaPersonalBean fp = FichaPersonalBean.getInstance();
		SimpleHash modelRoot = fp.getDatosLiquidacionAdic(Conexion,super.getIoClaseWeb().getServletContext().getRealPath("/templates"),20,periodo,rut, req); 
		
		ResourceBundle proper = ResourceBundle.getBundle("db");
		String prefijo = proper.getString("url");
        modelRoot.put("prefijo", prefijo + "Logo");
        System.out.println("Prefijo para PDF====> " + prefijo);
        ConsultaData data = ManagerTrabajador.getInstance().getTrabajador(rut);
        if(data.next()) {
        	modelRoot.put("GetImagenEmpresaDestino", new GetImagenEmpresa(data.getString("empresa")));
        }
        
      
        
        
		super.getIoClaseWeb().retTemplate("certificados/liquida_trab.html",modelRoot);
	}

	private String getPeriodo_formato2(String periodo) {
		int aaa = Validar.getInstance().validarInt(periodo, 190001)/ 100;
 		int mes = Validar.getInstance().validarInt(periodo, 190001) % 100;
 		int dia = 1;
 		
 		Calendar calInicio  = Calendar.getInstance();
 		Calendar calTermino = Calendar.getInstance();
 		
 		calInicio.set(aaa, mes-1, dia);
 		calTermino.set(aaa, mes-1, dia);
 		
 		calTermino.add(Calendar.MONTH, 1);
 		calTermino.add(Calendar.DAY_OF_MONTH, -1);
 		
 		String inicio  = Formatear.getInstance().toDate(calInicio.getTime(), "dd/MM/yyyy");
 		String termino = Formatear.getInstance().toDate(calTermino.getTime(), "dd/MM/yyyy");
 		
 		return Formatear.getInstance().toMonth(mes) + " del " + aaa + " del "+inicio+ " al "+termino;
	}
	 
    public void Liquidacion(HttpServletRequest req, HttpServletResponse resp,Connection Conexion, String rut, String periodo)
    	throws ServletException, IOException {

    	FichaPersonalBean fp = FichaPersonalBean.getInstance();
    	SimpleHash modelRoot = fp.getDatosLiquidacionMultEmpresas(Conexion,super.getIoClaseWeb().getServletContext().getRealPath("/templates"),periodo,18,rut, req);
    	String scheme = req.getScheme();            
		String serverName = req.getServerName();     
		int serverPort = req.getServerPort();        
		String contextPath = req.getContextPath();   
		String urlBase = scheme+"://"+serverName+":"+serverPort+contextPath;
    	ResourceBundle proper = ResourceBundle.getBundle("db");
    	String prefijo = proper.getString("url");
        modelRoot.put("prefijo", prefijo + "Logo");
        modelRoot.put("urlBase", urlBase);
        modelRoot.put("rut_trab", rut);
        System.out.println("Prefijo para PDF====> " + prefijo);
        
        ConsultaData data = ManagerTrabajador.getInstance().getTrabajador(rut);
        if(data.next()) {
        	modelRoot.put("GetImagenEmpresaDestino", new GetImagenEmpresa(data.getString("empresa")));
        	
        	FreemakerTool tool = new FreemakerTool();
        	modelRoot.put("trabajador", tool.getData(data) );
        }
        
        modelRoot.put("periodo_alt2", getPeriodo_formato2(periodo) );
        
        
        super.getIoClaseWeb().retTemplate("certificados/liquida_trab.html",modelRoot);
    }
  
}