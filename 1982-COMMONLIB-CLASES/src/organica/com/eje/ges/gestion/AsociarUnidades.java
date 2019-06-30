package organica.com.eje.ges.gestion;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

public class AsociarUnidades extends MyHttpServlet {

	public AsociarUnidades() {
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		doGet(req, resp);
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		Connection Conexion = connMgr.getConnection("portal");
		if (Usuario.rescatarUsuario(req).esValido()) {
			String valor = req.getParameter("id_unidad");
			OutMessage.OutMessagePrint("*valor: ".concat(String.valueOf(String
					.valueOf(valor))));
			if (Conexion != null)
				CargaPagina(req, resp, Conexion);
			else
				devolverPaginaError(resp, "Error de conexi\363n a la BD");
		} else {
			devolverPaginaMensage(resp, "Time Out",
					"El tiempo de su sesi\363n ha expirado.Por favor ingrese nuevamente.");
		}
		connMgr.freeConnection("portal", Conexion);
	}

	  public void CargaPagina(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
      throws ServletException, IOException {
      user = Usuario.rescatarUsuario(req);
      SimpleHash modelRoot = new SimpleHash();
      ControlAcceso control = new ControlAcceso(user);
      String[] ccostosel= (String[])req.getParameterValues("centro_costosel");
      String[] ccostonosel= (String[])req.getParameterValues("centro_costonosel");
      String id_unidad = req.getParameter("unidad");
      String unidad = req.getParameter("desc_unidad");
      String id_empresa = req.getParameter("empresa");
      String id_listed = req.getParameter("listed");
      String id_loadf = req.getParameter("loadf");
      Consulta emp = new Consulta(Conexion);
      String Query = "";
      String actualiz = "0";
      String linkto = "0";
      if(control.tienePermiso("df_mant_unidad")) {
          linkto = "1";
          if (id_loadf != null && ccostosel == null && ccostonosel == null) {
        	  Query = String.valueOf( new StringBuilder("DELETE FROM eje_ges_unidad_rama WHERE tipo='U' AND unidad ='").append(id_unidad).append("' AND empresa='").append(id_empresa).append("'") );
              emp.insert(Query);
              Query = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_unidad_rama (empresa, unidad, tipo, uni_rama) VALUES ('")).append(id_empresa).append("', '").append(id_unidad).append("', 'U','0')")));
              emp.insert(Query);
          }

          String unidad_administrativa = "";
          ResourceBundle proper = ResourceBundle.getBundle("DataFolder");
          try {
        	  unidad_administrativa = proper.getString("portal.unidad_administrativa");
          }
          catch(MissingResourceException e) {
                  OutMessage.OutMessagePrint("Excepcion : ".concat(String.valueOf(String.valueOf(e.getMessage()))));
          }
              
          if(ccostosel == null && id_listed==null) {
        	  //Todos los centro de costos asociados a una unidad
              //String query1 = String.valueOf( new StringBuilder("SELECT distinct a.empresa,a.uni_rama,c.descrip as unid_desc FROM eje_ges_unidad_rama a,eje_ges_unidades b,eje_ges_centro_costo c WHERE c.centro_costo!= 0 AND a.uni_rama = c.centro_costo AND b.unid_empresa = a.empresa AND b.unid_id = a.unidad AND a.tipo='U' AND b.vigente = 'S' AND b.unid_empresa ='").append(id_empresa).append("' AND unid_id ='").append(id_unidad).append("'")  );
        	  String query1;
          	System.out.println("-------------------------->" + unidad_administrativa);
        	  if( unidad_administrativa.equals("0") ) {
              	System.out.println("-------------------------->" + unidad_administrativa);
            	  query1 = String.valueOf( new StringBuilder("SELECT distinct a.uni_rama,c.unid_desc as unid_desc FROM eje_ges_unidad_rama a,eje_ges_unidades b,eje_sh_unidades c WHERE c.unid_id!= 0 AND a.uni_rama = c.unid_id AND b.unid_empresa = a.empresa AND b.unid_id = a.unidad AND a.tipo='U' AND b.vigente = 'S' AND b.unid_empresa ='").append(id_empresa).append("' AND b.unid_id ='").append(id_unidad).append("'")  );
                	System.out.println("-------------------------->" + query1);
              }
        	  else
        		  query1 = String.valueOf( new StringBuilder("SELECT distinct a.uni_rama,c.descrip as unid_desc FROM eje_ges_unidad_rama a,eje_ges_unidades b,eje_ges_centro_costo c WHERE c.centro_costo!= 0 AND a.uni_rama = c.centro_costo AND b.unid_empresa = a.empresa AND b.unid_id = a.unidad AND a.tipo='U' AND b.vigente = 'S' AND b.unid_empresa ='").append(id_empresa).append("' AND unid_id ='").append(id_unidad).append("'")  );
              emp.exec(query1);
              modelRoot.put("ccostosel",emp.getSimpleList());

              //Todos los centros de costos disponibles
              //String query2 = "select distinct centro_costo as uni_rama,descrip as unid_desc,wp_cod_empresa as empresa from eje_ges_centro_costo a where centro_costo != 0 AND not exists (select 1 from eje_ges_unidad_rama b where a.centro_costo = b.uni_rama)";
              String query2 = "";
        	  if( unidad_administrativa.equals("0") )
                  query2 = "select distinct a.unid_id as uni_rama,a.unid_desc as unid_desc from eje_sh_unidades a where a.unid_id != 0 AND not exists (select 1 from eje_ges_unidad_rama b where a.unid_id = b.uni_rama)";
             else
            	 query2 = "select distinct centro_costo as uni_rama,descrip as unid_desc from eje_ges_centro_costo a where centro_costo != 0 AND not exists (select 1 from eje_ges_unidad_rama b where a.centro_costo = b.uni_rama)";
              emp.exec(query2);
              modelRoot.put("ccostonosel",emp.getSimpleList());
              modelRoot.put("unidad",id_unidad);
              modelRoot.put("desc_unidad",unidad);
              modelRoot.put("empresa",id_empresa);
          }
          else
          {
        	  if (ccostosel != null) {
              	String[] empresa = getEmpresaRama(Conexion);
              	int n = ccostosel.length;

              	String ParameterDel = "";
              	//Query = String.valueOf( new StringBuilder("SELECT distinct a.uni_rama,a.empresa FROM eje_ges_unidad_rama a,eje_ges_unidades b WHERE b.unid_empresa = a.empresa AND b.unid_id = a.unidad AND vigente = 'S' AND b.unid_empresa ='").append(id_empresa).append("' AND unid_id ='").append(id_unidad).append("'")  );
            	System.out.println("-------------------------->" + unidad_administrativa);
          	  if( unidad_administrativa.equals("0") )  {
                	System.out.println("-------------------------->" + unidad_administrativa);
                	Query = String.valueOf( new StringBuilder("SELECT distinct a.uni_rama,c.unid_desc as unid_desc FROM eje_ges_unidad_rama a,eje_ges_unidades b,eje_sh_unidades c        WHERE c.unid_id!= 0      AND a.uni_rama = c.unid_id      AND b.unid_empresa = a.empresa AND b.unid_id = a.unidad AND a.tipo='U' AND b.vigente = 'S' AND b.unid_empresa ='").append(id_empresa).append("' AND b.unid_id ='").append(id_unidad).append("'")  );
          	  }
          	  else
              	Query = String.valueOf( new StringBuilder("SELECT distinct a.uni_rama FROM eje_ges_unidad_rama a,eje_ges_unidades b,eje_ges_centro_costo c WHERE c.centro_costo!= 0 AND a.uni_rama = c.centro_costo AND b.unid_empresa = a.empresa AND b.unid_id = a.unidad AND a.tipo='U' AND b.vigente = 'S' AND b.unid_empresa ='").append(id_empresa).append("' AND unid_id ='").append(id_unidad).append("'")  );
              	emp.exec(Query);
                emp.getSimpleList();
                int m = emp.getFilasSimpleList();
                
            	  if( unidad_administrativa.equals("0") )
            		  Query = String.valueOf( new StringBuilder("SELECT distinct a.uni_rama,c.unid_desc as unid_desc FROM eje_ges_unidad_rama a,eje_ges_unidades b,eje_sh_unidades c        WHERE c.unid_id!= 0      AND a.uni_rama = c.unid_id      AND b.unid_empresa = a.empresa AND b.unid_id = a.unidad AND a.tipo='U' AND b.vigente = 'S' AND b.unid_empresa ='").append(id_empresa).append("' AND b.unid_id ='").append(id_unidad).append("'") );
            	  else
            		  Query = String.valueOf( new StringBuilder("SELECT distinct a.uni_rama FROM eje_ges_unidad_rama a,eje_ges_unidades b,eje_ges_centro_costo c WHERE c.centro_costo!= 0 AND a.uni_rama = c.centro_costo AND b.unid_empresa = a.empresa AND b.unid_id = a.unidad AND a.tipo='U' AND b.vigente = 'S' AND b.unid_empresa ='").append(id_empresa).append("' AND unid_id ='").append(id_unidad).append("'")  );
                emp.exec(Query);
                for (int i=0;i<m;i++) {
                	emp.next();
                	ParameterDel = ParameterDel + "'" + emp.getString("uni_rama");
                	ParameterDel = (i+1 < m)?ParameterDel + "',":ParameterDel + "'";
                }

           		String parameterNot = "";
           		//int h = empresa.length;
              	for(int i=0;i<n;i++) {
//              		String empre = null;
//              		for(int j=0;j<h;j++) {
//                  		if (req.getParameter("sel_" + ccostosel[i] + "_" + empresa[j]) != null) {
                  			if (i < n && parameterNot != "")    //j y h
                  				parameterNot = parameterNot + ",'";
                  			else if (i+1 <= n)    // j y h
                  				parameterNot = parameterNot + "'";
//                  			empre = req.getParameter("sel_" + ccostosel[i] + "_" + empresa[j]);
                  			parameterNot = parameterNot + ccostosel[i] + "'";
//                  		}
//              		}
              	}
          		Query = String.valueOf( new StringBuilder("DELETE FROM eje_ges_unidad_rama WHERE (unidad ='").append(id_unidad).append("' AND empresa='").append(id_empresa).append("' AND uni_rama in (").append(ParameterDel).append(")) AND NOT EXISTS (SELECT 1 FROM eje_ges_unidad_rama b WHERE eje_ges_unidad_rama.uni_rama = b.uni_rama and eje_ges_unidad_rama.empresa = b.empresa and b.uni_rama in(").append(parameterNot).append("))")  );
          		emp.insert(Query);
          		actualiz = "1"; 
            	linkto = "0";
              }
              if (ccostonosel != null) {
              	String[] empresa = getEmpresaRama(Conexion);
              	  
              	for(int i=0;i<ccostonosel.length;i++) {
//              		for(int j=0;j<empresa.length;j++) {
//                  		String empre = null;
//                  		if (req.getParameter("nosel_" + ccostonosel[i] + "_" + empresa[j]) != null) {
//                  			empre = req.getParameter("nosel_" + ccostonosel[i] + "_" + empresa[j]); 
//                      		if ( empre != null && !isEqualsCC(id_empresa,id_unidad,ccostonosel[i],Conexion) ) {
                      			
//                      			Query = String.valueOf( new StringBuilder("SELECT 1 as r FROM eje_ges_unidad_rama WHERE uni_rama='0' AND tipo='U' AND unidad='").append(id_unidad).append("' AND empresa='").append(id_empresa).append("'") );
              					Query = String.valueOf( new StringBuilder("SELECT 1 as r FROM eje_ges_unidad_rama WHERE uni_rama='0' AND tipo='U' AND unidad='").append(id_unidad).append("'") );
                      			emp.exec(Query);
                      			emp.next();
                      			boolean flag =  emp.getString("r")=="1"?false:true;
                      			if (flag) {
                          			Query = String.valueOf( new StringBuilder("INSERT INTO eje_ges_unidad_rama VALUES('").append(id_empresa).append("','").append(id_unidad).append("','").append("U','").append(ccostonosel[i]).append("')")  );
                                    emp.insert(Query);
                      			}
                      			else {
                          			Query = String.valueOf( new StringBuilder("UPDATE eje_ges_unidad_rama SET uni_rama='" ).append(ccostonosel[i]).append("' WHERE tipo='U' AND uni_rama='0' AND empresa='").append(id_empresa).append("' AND unidad='").append(id_unidad).append("'") );
                                    emp.insert(Query);
                      			}
                            	actualiz = "1";
                            	linkto = "0";
//                      		}
//                  		}
//              		}
             	}
              }
          }
      }
      emp.close();
      modelRoot.put("actualiz", actualiz);
      modelRoot.put("linkto", linkto);
      super.retTemplate(resp,"Gestion/MantUnidades/AsociarUnidad.htm",modelRoot);
  }

	private boolean isEqualsCC(String empresa, String unidad, String ccosto,
			Connection Conexion) {
		Consulta aux = new Consulta(Conexion);
		String query = String.valueOf((new StringBuilder(
				"SELECT 1 FROM eje_ges_unidad_rama WHERE empresa = '")).append(
				empresa).append("' AND unidad='").append(unidad).append(
				"' AND uni_rama='").append(ccosto).append("' AND tipo='U'"));
		aux.exec(query);
		aux.getSimpleList();
		int length = -1;
		length = aux.getFilasSimpleList();
		aux.close();
		return length > 0;
	}

	private String[] getEmpresaRama(Connection Conexion) {
		Consulta aux = new Consulta(Conexion);
		aux.exec("SELECT empresa FROM eje_ges_unidad_rama group by empresa");
		aux.getSimpleList();
		int length = aux.getFilasSimpleList();
		String result[] = (String[]) Array.newInstance(java.lang.String.class,
				length);
		aux.exec("SELECT empresa FROM eje_ges_unidad_rama group by empresa");
		for (int i = 0; i < length; i++) {
			aux.next();
			result[i] = aux.getString("empresa");
		}

		aux.close();
		return result;
	}

	private void devolverPaginaMensage(HttpServletResponse resp, String titulo,
			String msg) throws IOException, ServletException {
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("titulo", titulo);
		modelRoot.put("mensaje", msg);
		super.retTemplate(resp,"mensaje.htm",modelRoot);
	}

	private void devolverPaginaError(HttpServletResponse resp, String msg) {
		try {
			PrintWriter printwriter = resp.getWriter();
			resp.setContentType("text/html");
			printwriter.println("<html>");
			printwriter.println("<head>");
			printwriter
					.println("<title>Valores recogidos en el formulario</title>");
			printwriter.println("</head>");
			printwriter.println("<body>");
			printwriter.println(msg);
			printwriter.println("</body>");
			printwriter.println("</html>");
			printwriter.flush();
			printwriter.close();
		} catch (IOException e) {
			OutMessage.OutMessagePrint("Error al botar la pagina");
		}
	}

	private Usuario user;
	private Mensaje mensaje;
}