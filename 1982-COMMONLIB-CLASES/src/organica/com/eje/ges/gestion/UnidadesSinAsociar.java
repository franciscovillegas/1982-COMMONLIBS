package organica.com.eje.ges.gestion;

import java.io.IOException;
import java.io.PrintWriter;
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

public class UnidadesSinAsociar extends MyHttpServlet {

	public UnidadesSinAsociar() {
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

	public void CargaPagina(HttpServletRequest req, HttpServletResponse resp,Connection Conexion) 
	throws ServletException, IOException {
		user = Usuario.rescatarUsuario(req);
		SimpleHash modelRoot = new SimpleHash();
		ControlAcceso control = new ControlAcceso(user);
		String ccostonosel[] = req.getParameterValues("centro_costonosel");
		String id_unidad = req.getParameter("unidad");
		String unidad = req.getParameter("desc_unidad");
		String id_empresa = req.getParameter("empresa");
		Consulta emp = new Consulta(Conexion);
		String Query = "";
		String actualiz = "0";
		String linkto = "0";
		if (control.tienePermiso("df_mant_unidad")) {
	          String unidad_administrativa = "";
	          ResourceBundle proper = ResourceBundle.getBundle("DataFolder");
	          try {
	        	  unidad_administrativa = proper.getString("portal.unidad_administrativa");
	          }
	          catch(MissingResourceException e) {
	                  OutMessage.OutMessagePrint("Excepcion : ".concat(String.valueOf(String.valueOf(e.getMessage()))));
	          }
			
			if (ccostonosel == null) {
				String query = "";
	        	  if( unidad_administrativa.equals("0") )
	                  query = "select distinct a.unid_id as uni_rama,a.unid_desc as unid_desc from eje_sh_unidades a where a.unid_id != 0 AND not exists (select 1 from eje_ges_unidad_rama b where a.unid_id = b.uni_rama)";
	             else
	            	 query = "select distinct centro_costo as uni_rama,descrip as unid_desc from eje_ges_centro_costo a where centro_costo != 0 AND not exists (select 1 from eje_ges_unidad_rama b where a.centro_costo = b.uni_rama)";

				emp.exec(query);
				linkto = "1";
				modelRoot.put("ccostonosel", emp.getSimpleList());
				modelRoot.put("unidad", id_unidad);
				modelRoot.put("desc_unidad", unidad);
				modelRoot.put("empresa", id_empresa);
			}
			else {
				if (ccostonosel != null) {
					for (int i = 0; i < ccostonosel.length; i++) {
						Query = String
								.valueOf((new StringBuilder(
										"SELECT 1 as r FROM eje_ges_unidad_rama WHERE uni_rama='0' AND tipo='U' AND unidad='"))
										.append(id_unidad).append("'"));
						emp.exec(Query);
						emp.next();
						boolean flag = emp.getString("r") != "1";
						if (flag) {
							Query = String
									.valueOf((new StringBuilder(
											"INSERT INTO eje_ges_unidad_rama VALUES('"))
											.append(id_empresa).append("','")
											.append(id_unidad).append("','")
											.append("U','").append(
													ccostonosel[i])
											.append("')"));
							emp.insert(Query);
						} else {
							Query = String
									.valueOf((new StringBuilder(
											"UPDATE eje_ges_unidad_rama SET uni_rama='"))
											.append(ccostonosel[i])
											.append(
													"' WHERE tipo='U' AND uni_rama='0' AND empresa='")
											.append(id_empresa).append(
													"' AND unidad='").append(
													id_unidad).append("'"));
							emp.insert(Query);
						}
						actualiz = "1";
						linkto = "0";
					}

				}
				
			}
		}
		modelRoot.put("actualiz", actualiz);
		modelRoot.put("linkto", linkto);
		emp.close();
		super.retTemplate(resp,"Gestion/MantUnidades/UnidadesSinAsociar.htm",modelRoot);
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