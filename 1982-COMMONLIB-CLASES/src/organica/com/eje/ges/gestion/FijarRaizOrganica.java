package organica.com.eje.ges.gestion;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

public class FijarRaizOrganica extends MyHttpServlet {

	public FijarRaizOrganica() {
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		doGet(req, resp);
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
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

	public void CargaPagina(HttpServletRequest req, HttpServletResponse resp,
			Connection Conexion) throws ServletException, IOException {
		user = Usuario.rescatarUsuario(req);
		String linkto = "0";
		String actualiz = req.getParameter("actualiz");
		String cod_empresa = req.getParameter("checked");
		String nueva_raiz = req.getParameter("list_empresas");
		String query = "";
		SimpleHash modelRoot = new SimpleHash();
		Consulta consul = new Consulta(Conexion);
		Consulta consulAct = new Consulta(Conexion); 
		if( actualiz == null) {
			query  = "SELECT empresa as codigo,descrip as descripcion,padre_empresa as raiz FROM eje_ges_empresa";
			consul.exec(query);
			while ( consul.next() ) {
				modelRoot.put("empresas", consul.getSimpleList());
			}
			linkto = "1";
		}
		else {
			//Lógica de actualizacion de tablas
			//eje_ges_jerarquia
			//eje_ges_empresa
			//eje_ges_uni_sup
			query = "SELECT 1 FROM eje_ges_empresa WHERE padre_empresa = " + cod_empresa;
			System.out.println("------------>" + query);
			consul.exec(query);
			if( consul.next() ) {

				//Busca el codigo de la empresa que es raiz del arbol
				String raiz_antigua = "";
				query = "SELECT empresa FROM eje_ges_empresa WHERE padre_empresa is null";
				System.out.println("NodoId empresa------------>" + query);
				consulAct.exec(query);
				if( consulAct.next() ) {
					raiz_antigua = consulAct.getString("empresa"); 
				}

				//Setea el nodo seleccionado como raiz del arbol
				query = "UPDATE eje_ges_empresa SET padre_empresa=null WHERE empresa='" + nueva_raiz + "'";
				System.out.println("NodoId empresa------------>" + query);
				consulAct.insert(query);
				
				//Setea la raiz antigua como hija del nuevo nodo raiz
				query = "UPDATE eje_ges_empresa SET padre_empresa='" + nueva_raiz + "'WHERE empresa='" + raiz_antigua + "'";
				System.out.println("NodoId empresa------------>" + query);
				consulAct.insert(query);
				
				//Setea el nodo seleccionado como nueva unidad superior de la organica
				query = "UPDATE eje_ges_uni_sup SET empresa='" + raiz_antigua + "'";
				System.out.println("NodoId empresa------------>" + query);
				consulAct.insert(query);

				consulAct.close();
				Usuario.SuperNodo = null;
				RefrescaDatos(req,resp,Conexion);
			}
		}
		consul.close();
		modelRoot.put("linkto",linkto);			
		super.retTemplate(resp,"Gestion/MantUnidades/FijarRaizOrganica.htm",modelRoot);
	}

	private void RefrescaDatos(HttpServletRequest req, HttpServletResponse resp, Connection Conexion) 
	throws ServletException, IOException {
        user = Usuario.rescatarUsuario(req);
		Consulta consul = new Consulta(Conexion);
		SimpleHash modelRoot = new SimpleHash();
        String linkto;
        OutMessage.OutMessagePrint("entro a refrescar datos**********");
		String query  = "SELECT empresa as codigo,descrip as descripcion,padre_empresa as raiz FROM eje_ges_empresa";
		consul.exec(query);
		while ( consul.next() ) {
			modelRoot.put("empresas", consul.getSimpleList());
		}
		linkto = "1";
		modelRoot.put("linkto",linkto);			
        modelRoot.put("carga", "1");
        super.retTemplate(resp,"Gestion/MantUnidades/FijarRaizOrganica.htm",modelRoot);
        
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
