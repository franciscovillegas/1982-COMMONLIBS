package portal.com.eje.serhumano.tracking;

import java.io.IOException;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.tools.Validar;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

/**
 * Servlet implementation class for Servlet: ViewTracking
 *
 */
 public class ViewTracking extends MyHttpServlet {

 	public ViewTracking() { super(); }   	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}  	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
//    	MMA 20170111
//    	Connection conecction = connMgr.getConnection("portal");
        Connection conecction =  getConnMgr().getConnection("portal");
        Consulta cons = new Consulta(conecction);
                
        Validar valida = new Validar();
        
        /******************************
         *  LLENADO COMBOX PORTALES  **
         ******************************/  
        String query = "SELECT idportal,nomportal FROM eje_ges_lista_portales order by nomportal";
        cons.exec(query);
        SimpleList PortalList = new SimpleList();
    	SimpleHash PortalIter;
    	PortalIter = new SimpleHash();
    	PortalIter.put("cod","-1" );
    	PortalIter.put("nombre","Seleccione un Portal..." );
    	PortalList.add(PortalIter);
        for(;cons.next();PortalList.add(PortalIter)) {
        	PortalIter = new SimpleHash();
        	PortalIter.put("cod",valida.validarDato(cons.getString("idportal")) );
        	PortalIter.put("nombre",valida.validarDato(cons.getString("nomportal")) );
        	String valor;
        	if( cons.getString("idportal").equals(request.getParameter("Portal")) ) { valor="selected"; }
        	else { valor=null; }
        	PortalIter.put("sel",valor);
        }
        SimpleHash modelRoot = new SimpleHash ();
        modelRoot.put("lportales",PortalList);
    	String selPortal;
    	if(request.getParameter("Portal")==null) { selPortal="-1"; }
    	else { selPortal=request.getParameter("Portal"); }
    	
    	String FI = valida.validarDato(request.getParameter("Finicio"),"-1");
        String FT = valida.validarDato(request.getParameter("Ftermino"),"-1");
        String FIAnsi=null ,FTAnsi = null;
        System.out.println("FI ----> " + FI);
        System.out.println("FT ----> " + FI);
        //if(!FI.equals("&nbsp;") && !FT.equals("&nbsp;"))
        if(!FI.equals("-1") && !FT.equals("-1"))
        {  
           FIAnsi = FI.substring(6,10) + FI.substring(3,5) + FI.substring(0,2);
           System.out.println("FIAnsi ----> " + FIAnsi);
           FTAnsi = FT.substring(6,10) + FT.substring(3,5) + FT.substring(0,2);
           System.out.println("FTAnsi ----> " + FTAnsi);
    	   SimpleHash FechaIter = new SimpleHash();
           FechaIter.put("inicio",FI);
           FechaIter.put("termino",FT);
           modelRoot.put("fecha",FechaIter);
        }
        
        cons.close();
//    	MMA 20170111
//      super.connMgr.freeConnection("portal", conecction);
        getConnMgr().freeConnection("porral", conecction);

        /***************************************
         * visualizacion de datos por portal  **
         ***************************************/
        if(selPortal!="-1")
        {  
        	if(!selPortal.equals("0")) // info x portal
        	{
        		/*************************************************
                 *  datos generales del portal para cargar perfil
                 *************************************************/         	
//            	MMA 20170111
//            	Connection conecction1 = connMgr.getConnection("portal");
                Connection conecction1 =  getConnMgr().getConnection("portal");
                
                Consulta cons1 = new Consulta(conecction1);
                query = "SELECT idportal as i,rtrim(nomportal) as n,rtrim(prefijoportal) as p,rtrim(tracking) as t,rtrim(trackingfunc) as tf FROM eje_ges_lista_portales where idportal=" + selPortal;
                System.out.println("Q ----> " + query);
                cons1.exec(query); cons1.next();
                String id = valida.validarDato(cons1.getString("i"));
                String nombre = valida.validarDato(cons1.getString("n"));
                String prefijo = valida.validarDato(cons1.getString("p"));
                String tracking = valida.validarDato(cons1.getString("t"));
                String trackingfunc = valida.validarDato(cons1.getString("tf"));
                cons1.close();
//            	MMA 20170111
//              super.connMgr.freeConnection("portal", conecction1);
                getConnMgr().freeConnection("porral", conecction1);
                SimpleList PortalesList = new SimpleList();
                SimpleHash PortalesIter = new SimpleHash();
                PortalesIter.put("i",id);
                PortalesIter.put("n",nombre);
                PortalesIter.put("p",prefijo);
                PortalesIter.put("t",tracking);
                PortalesIter.put("tf",trackingfunc);
                
        		/*************************
                 *  visitas del portal  **
                 *************************/         	
                //Connection conecction2 = connMgr.getConnection(prefijo);
//            	MMA 20170111
//            	Connection conecction2 = connMgr.getConnection("portal");
                Connection conecction2 =  getConnMgr().getConnection("portal");
                Consulta cons2 = new Consulta(conecction2);
                query = "SELECT (count(*)) as visita from " + tracking + " a," + trackingfunc + " b WHERE (a.fecha Between '" + FIAnsi + "' and dateadd(day,1,'" + FTAnsi + "') ) and a.direc_rel = b.url";
                cons2.exec(query); cons2.next();
                String total = valida.validarDato(cons2.getString("visita"));
                query = "select isnull(max(x.visita),0) as total from (SELECT b.descripcion, count(*) as visita from " + tracking + " a," + trackingfunc + " b WHERE (a.fecha Between '" + FIAnsi + "' and dateadd(day,1,'" + FTAnsi + "') ) and a.direc_rel = b.url GROUP BY b.descripcion) x";
                cons2.exec(query); cons2.next();
                String total2 = valida.validarDato(cons2.getString("total"));
                if( total2.equals("&nbsp;"))
                { total2 = "0"; }
                int Ttotal = Integer.parseInt(total);
                int Ttotal2 = Integer.parseInt(total2);
                query = "SELECT (b.descripcion) as servicio,(count(*)) as visita from " + tracking + " a," + trackingfunc + " b WHERE (a.fecha Between '" + FIAnsi + "' and dateadd(day,1,'" + FTAnsi + "') ) and a.direc_rel = b.url GROUP BY b.descripcion";
                cons2.exec(query);
                SimpleList ServicioList = new SimpleList();
         	    SimpleHash ServicioIter;
         	    int temp10=0,totalvisitas=0;
         	    ResourceBundle proper = ResourceBundle.getBundle("db");
         	    int ancho= Integer.parseInt(proper.getString("ancho.barra"));
         	    String temp20;
         	    double temp31,uvisita=0, porcentaje=0;
         	    DecimalFormat df = new DecimalFormat();
         	    df.setMaximumFractionDigits(2);
         	   /********************************
                *  carga datos x item portal  **
                ********************************/
         	   for(;cons2.next();ServicioList.add(ServicioIter)) {
            	 ServicioIter = new SimpleHash();
            	 ServicioIter.put("servicio",valida.validarDato(cons2.getString("servicio")) );
            	 ServicioIter.put("visita",valida.validarDato(cons2.getString("visita")) );
            	 uvisita = Double.valueOf(valida.validarDato(cons2.getString("visita"))).doubleValue();
            	 porcentaje = (uvisita*100) / Ttotal;
            	 temp20 = String.valueOf(df.format(porcentaje));
            	 ServicioIter.put("porcentaje",valida.validarDato(temp20));
            	 temp31 =(((uvisita/Ttotal2)*100)*ancho)/100;	   
            	 temp10 = (int)temp31;        	   
            	 ServicioIter.put("ancho", String.valueOf(temp10));
             	 totalvisitas += Integer.parseInt(cons2.getString("visita"));
               }
         	   PortalesIter.put("servicios",ServicioList);
         	   String TVisitas = String.valueOf(totalvisitas);
               PortalesIter.put("total",TVisitas);
               PortalesList.add(PortalesIter);
               modelRoot.put("lportales2",PortalesList);
         	   
               cons2.close();
               //connMgr.freeConnection(prefijo,conecction2);
//           	MMA 20170111
//             super.connMgr.freeConnection("portal", conecction2);
               getConnMgr().freeConnection("porral", conecction2);
                
        	}
        	else // info de todos los portales
        	{
        		/*************************************************
                 *  datos generales del portal para cargar perfil
                 *************************************************/         	
//            	MMA 20170111
//            	Connection conecction1 = connMgr.getConnection("portal");
                Connection conecction1 =  getConnMgr().getConnection("portal");
                Consulta cons1 = new Consulta(conecction1);
                query = "SELECT idportal as i,rtrim(nomportal) as n,rtrim(prefijoportal) as p,rtrim(tracking) as t,rtrim(trackingfunc) as tf FROM eje_ges_lista_portales";
                System.out.println("Q ----> " + query);
                cons1.exec(query);
                
                SimpleList PortalesList = new SimpleList();
                SimpleHash PortalesIter = new SimpleHash();
                
                String prefijo,tracking,trackingfunc;
                
                for(;cons1.next();PortalesList.add(PortalesIter)) {
                   PortalesIter = new SimpleHash();
                   PortalesIter.put("i",valida.validarDato(cons1.getString("i")) );
                   PortalesIter.put("n",valida.validarDato(cons1.getString("n")) );
                   PortalesIter.put("p",valida.validarDato(cons1.getString("p")) );
                   PortalesIter.put("t",valida.validarDato(cons1.getString("t")) );
                   PortalesIter.put("tf",valida.validarDato(cons1.getString("tf")) );
                    
                   
                   // hasta aqui todo bien -------> inicio codigo nuevo que se va a ejecutar
                   prefijo = valida.validarDato(cons1.getString("p"));
                   tracking = valida.validarDato(cons1.getString("t"));
                   trackingfunc = valida.validarDato(cons1.getString("tf"));
                   System.out.println("P: " + prefijo + " T: " + tracking + " TF: " + trackingfunc);
           		   /*************************
                    *  visitas del portal  **
                    *************************/         	
                   Connection conecction2 = connMgr.getConnection("portal");
                   Consulta cons2 = new Consulta(conecction2);
                   query = "SELECT (count(*)) as visita from " + tracking + " a," + trackingfunc + " b WHERE (a.fecha Between '" + FIAnsi + "' and dateadd(day,1,'" + FTAnsi + "') ) and a.direc_rel = b.url";
                   cons2.exec(query); cons2.next();
                   String total = valida.validarDato(cons2.getString("visita"));
                   query = "select max(x.visita) as total from (SELECT b.descripcion, count(*) as visita from " + tracking + " a," + trackingfunc + " b WHERE (a.fecha Between '" + FIAnsi + "' and dateadd(day,1,'" + FTAnsi + "') ) and a.direc_rel = b.url GROUP BY b.descripcion) x";
                   cons2.exec(query); cons2.next();
                   String total2 = valida.validarDato(cons2.getString("total"));
                   if( total2.equals("&nbsp;"))
                   { total2 = "0"; }
                   int Ttotal = Integer.parseInt(total);
                   int Ttotal2 = Integer.parseInt(total2);
                   query = "SELECT (b.descripcion) as servicio,(count(*)) as visita from " + tracking + " a," + trackingfunc + " b WHERE (a.fecha Between '" + FIAnsi + "' and dateadd(day,1,'" + FTAnsi + "') ) and a.direc_rel = b.url GROUP BY b.descripcion";
                   cons2.exec(query);
                   SimpleList ServicioList = new SimpleList();
            	   SimpleHash ServicioIter;
            	   int temp10=0,totalvisitas=0,ancho=400;
            	   String temp20;
            	   double temp31,uvisita=0, porcentaje=0;
            	   DecimalFormat df = new DecimalFormat();
            	   df.setMaximumFractionDigits(2);
            	   /********************************
                   *  carga datos x item portal  **
                   ********************************/
            	   for(;cons2.next();ServicioList.add(ServicioIter)) {
               	     ServicioIter = new SimpleHash();
               	     ServicioIter.put("servicio",valida.validarDato(cons2.getString("servicio")) );
               	     ServicioIter.put("visita",valida.validarDato(cons2.getString("visita")) );
               	     uvisita = Double.valueOf(valida.validarDato(cons2.getString("visita"))).doubleValue();
               	     porcentaje = (uvisita*100) / Ttotal;
               	     temp20 = String.valueOf(df.format(porcentaje));
               	     ServicioIter.put("porcentaje",valida.validarDato(temp20));
               	     temp31 =(((uvisita/Ttotal2)*100)*ancho)/100;	   
               	     temp10 = (int)temp31;        	   
               	     ServicioIter.put("ancho", String.valueOf(temp10));
                	 totalvisitas += Integer.parseInt(cons2.getString("visita"));
                  }
            	  PortalesIter.put("servicios",ServicioList);
            	  String TVisitas = String.valueOf(totalvisitas);
                  PortalesIter.put("total",TVisitas);
                  //PortalesList.add(PortalesIter);
                  modelRoot.put("lportales2",PortalesList);
            	   
                  cons2.close();
                  //connMgr.freeConnection(prefijo,conecction2);
                  connMgr.freeConnection("portal",conecction2);
                   // de aqui hacia adelante todo bien -------> fin codigo nuevo que se va a ejecutar
                }
                cons1.close();
//           	MMA 20170111
//              super.connMgr.freeConnection("portal", conecction1);
                getConnMgr().freeConnection("porral", conecction1);
        	}
        }
        
        /**************************************
         * publica los datos en el template  **
         **************************************/ 
        super.retTemplate(response,"tracking/resumen.html",modelRoot);
    	
	}
	
}