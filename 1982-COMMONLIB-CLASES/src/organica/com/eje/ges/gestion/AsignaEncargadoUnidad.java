package organica.com.eje.ges.gestion;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import organica.DatosRut.Rut;
import organica.com.eje.ges.Buscar.EncargadoUnidad;
import organica.com.eje.ges.Buscar.FotosPersonalUnidad;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.Tools;
import organica.tools.Validar;

import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.UserMgr;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class AsignaEncargadoUnidad extends MyHttpServlet
{
    public AsignaEncargadoUnidad() { }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {   doGet(req, resp); }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {   DespResultado(req, resp); }

    private void DespResultado(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {   
    	java.sql.Connection Conexion = connMgr.getConnection("portal");
        
    	user = Usuario.rescatarUsuario(req);
    	userPortal = portal.com.eje.serhumano.user.SessionMgr.rescatarUsuario(req);
    	UserMgr mgr = new UserMgr(Conexion);
        
        if(user.esValido())
        {   
            valida = new Validar();
            SimpleHash modelRoot = new SimpleHash();
            String html = "";
            insTracking(req, "Asignación de Encargado Unidad".intern(),null );
            String operacion = valida.validarDato(req.getParameter("Accion"),"0");
            String unidad = req.getParameter("unidad");
            String empresa = req.getParameter("empresa");
            String acceso = req.getParameter("acceso2");
            String query = null;
            
            if(!operacion.equals("0"))
            {   
               html = "Gestion/AsignaEncargadoUnidad/AEUresultado.htm";
               //String rut = req.getParameter("rut");
               String rut[] = req.getParameterValues("encargado");
               Consulta encargado = new Consulta(Conexion);
               Consulta encargado2 = new Consulta(Conexion);
               Calendar ahoraCal = Calendar.getInstance();
               String mes = null;
               if((ahoraCal.get(Calendar.MONTH)+1)<10)  
            	   mes = "0" + String.valueOf(ahoraCal.get(Calendar.MONTH)+1);
               else {
            	   mes = String.valueOf(ahoraCal.get(Calendar.MONTH)+1);
               }
            	               	   
               String ano = String.valueOf(ahoraCal.get(Calendar.YEAR));
               String periodo = ano + mes;
               //anulamos al encargado actual de la unidad
               SimpleList EUList = new SimpleList();
           	   SimpleHash EUIter;
           	   int contador = 0;
           	   if (rut!=null)  {
           		   contador=rut.length;
           	   }

               if(contador==0)
               { 
            	   System.out.println("A BORRAR");
            	   mgr.borrarEncargadoAnterior(unidad, 1); 
           		   mgr.borrarEncargadoAnterior(unidad, 2);
           		   mgr.actualizarEstadoEncargadoUnidad(unidad);
               }
               else
               {  
            	   for (int i=0; i< rut.length; i++)
            	   {
                       mgr.borrarEncargadoAnterior(unidad, 1);
                       mgr.borrarEncargadoAnterior(unidad, 2);
                       mgr.actualizarEstadoEncargadoUnidad(unidad);
                       EUIter = new SimpleHash();
                   	   EUIter.put("rut",rut[i] );
                       EUList.add(EUIter);
                       
                       userPortal.quitarAccesoGestion(Conexion,rut[i]);
            	   }
            	   
                   //insertamos el nuevo encargado de unidad 
            	   for (int z=0; z< rut.length; z++){
            		   mgr.insertUsuarioEncargado(rut[z], unidad, periodo, user.getRutUsuario(), "1", acceso);
           		   }
                   
                   //asignamos rut a la unidad
            	   for (int z=0; z< rut.length; z++){
	        		   mgr.insertUsuarioUnidad(rut[z], unidad);
	       		   }
                   
                   //insertamos permisos para organica
                   if(contador!=0) {
                   for (int k=0; k< rut.length; k++) {
							query = "select app_id,wp_cod_empresa,wp_cod_planta from eje_ges_user_app where rut_usuario="
									.concat(rut[k]);
							
							encargado.exec(query);
 							String ee = empresa;
							String pe = "1";
							
							int i = 0,ish = 0,idf = 0,iac = 0;
							for(; encargado.next();) {
								if((!encargado.getString("wp_cod_empresa").equals(ee)) && (i == 0)) {
									ee = encargado.getString("wp_cod_empresa");
									pe = encargado.getString("wp_cod_planta");
									i = 1;
								}
								if(encargado.getString("app_id").equals("sh"))
									ish = 1;
								else if(encargado.getString("app_id").equals("df"))
									idf = 1;
								else if(encargado.getString("app_id").equals("adm_corp"))
									iac = 1;
							}

							if(ish == 0) {
								query =
									"insert into eje_ges_user_app(app_id,rut_usuario,vigente,wp_cod_empresa,wp_cod_planta) values('sh',"
										.concat(rut[k]).concat(",NULL,").concat(ee).concat(",1)");
								encargado.insert(query);
							}
							if(idf == 0) {
								query =
									"insert into eje_ges_user_app(app_id,rut_usuario,vigente,wp_cod_empresa,wp_cod_planta) values('df',"
										.concat(rut[k]).concat(",NULL,").concat(ee).concat(",1)");
								encargado.insert(query);
							}
							if(iac == 0) {
								query =
									"insert into eje_ges_user_app(app_id,rut_usuario,vigente,wp_cod_empresa,wp_cod_planta) values('adm_corp',"
										.concat(rut[k]).concat(",NULL,").concat(ee).concat(",1)");
								encargado.insert(query);
							}
							
							
							
							boolean gestion = userPortal.tieneAccesoGestion(Conexion, rut[k]);
							System.out.println(gestion);
							if(!gestion && "1".equals(acceso)) {
																
								mgr.addAccesoGestion(Conexion,rut[k]);
							}
						}
                   }

               }

               encargado.close();
               encargado2.close();
               
               modelRoot.put("unid_id", unidad);
               modelRoot.put("empresa", empresa);
               modelRoot.put("rut", EUList);
            }
            else
            { 
               html = "Gestion/AsignaEncargadoUnidad/listaUsuarios.htm";
               fotos = new FotosPersonalUnidad();
               modelRoot.put("varios", fotos.GetFotosPersonalUnidad(Conexion, unidad, empresa));
               modelRoot.put("unid_id", unidad);
               modelRoot.put("empresa", empresa);
               
               String rutEncargado = fotos.GetEncargadoUnidad(Conexion, unidad, empresa);
               
               modelRoot.put("rutEncargado", rutEncargado );
               
               
               boolean gestion = userPortal.tieneAccesoGestion(Conexion, rutEncargado);
               
               if(gestion) {
            	   modelRoot.put("accesoEncargado", gestion? "1" : "0" );
               }
                
            }
            super.retTemplate(resp,html,modelRoot);
           
        } else
        {
            mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        }
        connMgr.freeConnection("portal", Conexion);
    }
    
    
    
   

    private Usuario user;
    private portal.com.eje.serhumano.user.Usuario userPortal;
    private Rut userRut;
    private EncargadoUnidad EU;
    private EncargadoUnidad UE;
    private FotosPersonalUnidad fotos;
    private Tools tool;
    private Validar valida;
    private Mensaje mensaje;
}