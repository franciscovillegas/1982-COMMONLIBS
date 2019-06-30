package organica.com.eje.ges.carpelect;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.datos.Consulta;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.liquidadic.ManagerLiqAdicional;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Tools;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.strings.MyString;
import cl.ejedigital.web.FreemakerTool;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

// Referenced classes of package portal.com.eje.serhumano.certificados:
//            Certif_Manager

public class S_Init_Certif extends MyHttpServlet {

	public S_Init_Certif() {
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String rutOrg = req.getParameter("rut_org") == null ? "false" : req.getParameter("rut_org");
		MuestraDatos(req, resp, rutOrg);
		insTracking(req, "Certificados", null);

	}

	public void MuestraDatos(HttpServletRequest req, HttpServletResponse resp, String rutOrg)
        throws IOException, ServletException {
		
        OutMessage.OutMessagePrint("\n**** Inicio MuestraDatos: " + getClass().getName());


        portal.com.eje.serhumano.user.Usuario userPortal = portal.com.eje.serhumano.user.SessionMgr.rescatarUsuario(req); 
        user = SessionMgr.rescatarUsuario(req);
//    	MMA 20170112
//      Connection connection = connMgr.getConnection(user.getJndi());
        Connection connection = getConnMgr().getConnection(user.getJndi());
        
        SimpleHash modelRoot = new SimpleHash();

        if(user.esValido()) {
            String rut = (!rutOrg.equals("false") )? rutOrg : user.getRutId();
           
            modelRoot.put("rutOrg", rutOrg);
            
            Certif_Manager periodos = new Certif_Manager(connection);
            //ConsultaData data = periodos.getPeriodosLiquidacion(rut);
            Consulta data = periodos.getPeriodosLiquidacion(rut);
          
            modelRoot.put("periodosLiq", getPeriodos(data));

            Consulta info = new Consulta(connection);
            String consulta = "select digito_ver,nombre,id_foto,cargo from view_ges_InfoRut where rut = '" + rut + "'";
            OutMessage.OutMessagePrint("---->consulta: " + consulta);
            info.exec(consulta);
            if(info.next())
            {

	            String digito = info.getString("digito_ver");
	            modelRoot.put("rut2", Tools.setFormatNumber(rut) + "-" + digito);
	            modelRoot.put("nombre", info.getString("nombre"));
	            modelRoot.put("cargo", info.getString("cargo"));
                modelRoot.put("foto", info.getString("id_foto")); 
            }
            
            
            SimpleList lista = getLiquidacionesAdicionales(rut);
            if(lista != null) {
            	modelRoot.put("liquidacionesAdicionales", lista );
            }
         
            IOClaseWeb io = new IOClaseWeb(this, req, resp);
            io.retTemplate("CarpetaElectronica/main_certificados.html",modelRoot);
        } 
        else {
            mensaje.devolverPaginaSinSesion(resp, "Certificados", "Tiempo de Sesi\363n expirado...");
        }
//    	MMA 20170112
//        connMgr.freeConnection(user.getJndi(), connection);
        getConnMgr().freeConnection(user.getJndi(), connection);
        
        OutMessage.OutMessagePrint("\n**** Fin MuestraDatos: " + getClass().getName());
    }

	//private SimpleList getPeriodos(ConsultaData data) {
	private SimpleList getPeriodos(Consulta data) {

		SimpleList periodos = new SimpleList();
		boolean primero = true;

		while (data.next()) {
			SimpleHash periodo = new SimpleHash();
			periodo.put("id", String.valueOf(data.getInt("peri_id")));
			periodo.put("desc",
					new StringBuilder(Tools.RescataMes(data.getInt("peri_mes")))
							.append(" de ").append(data.getInt("peri_ano"))
							.toString());

			if (primero) {
				periodo.put("primero", "selected");
				primero = false;
			}

			periodos.add(periodo);
		}

		return periodos;
	}

	private SimpleList getLiquidacionesAdicionales(String rut) {
		int i = 0;
    	SimpleList lista = new SimpleList();
    	String repeticion = "";
    	Map<Integer,Integer> listaRepeticiones = new HashMap<Integer,Integer>();
    	
    	Certif_Manager periodos = new Certif_Manager(null);

    	if(ManagerLiqAdicional.getInstance().existeTablaLiquidacionAdicional()) {
	    	ConsultaData dataLiqAdicDisponible = periodos.getTipoLiquidacionAdicionalDisponibles();
	
	    	while(dataLiqAdicDisponible.next()) {
	    		
	    		String tipoProceso = dataLiqAdicDisponible.getString("tipo_proceso").trim();
	    		ConsultaData dataAdicional = periodos.getPeriodosAdic(rut,tipoProceso );
	    		
	    		while(dataAdicional.next()) {
	    			SimpleHash hash = new SimpleHash();
	    			int periId = dataAdicional.getInt("peri_id");
	    			
	    			if(listaRepeticiones.get(periId) == null) {
	    				listaRepeticiones.put(periId, 1);
	    				repeticion = "";
	    			}
	    			else {
	    				listaRepeticiones.put(periId, listaRepeticiones.get(periId) + 1 );
	    				repeticion = new StringBuilder("  (")
	    							.append(listaRepeticiones.get(periId))
	    							.append(")").toString();
	    			}
	    			
	    			hash.put("id", String.valueOf(periId).concat("|").concat(tipoProceso));
		    		hash.put("desc", new StringBuilder(Tools.RescataMes(dataAdicional.getInt("peri_mes")))
									.append(" de ")
									.append(dataAdicional.getInt("peri_ano"))
									.append(repeticion)
									.toString());
		    		
		    		lista.add(hash);
		    		i++;
	    		}
	    	
	 
	    		
	    	}
    	}
       
    	if(i > 0) {
    		return lista;
    	}
    	else {
    		return null;
    	}
    	
    }

	private Usuario user;
	private Mensaje mensaje;
}