package organica.com.eje.ges.gestion;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

public class MaestroOrganicaTotal extends MyHttpServlet {

	private static final long	serialVersionUID	= 1L;

	public MaestroOrganicaTotal() {}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		doGet(req,resp);
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		DespResultado(req,resp);
	}

	private void DespResultado(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		java.sql.Connection Conexion = connMgr.getConnection("portal");
		Usuario user = Usuario.rescatarUsuario(req);

		if(user.esValido()) {
			insTracking(req, "Maestro de Unidades".intern(),null );
			SimpleHash modelRoot = new SimpleHash();
			String query2 = new String();
			Consulta unidades = new Consulta(Conexion);
			String query = " SELECT U.UNID_ID,LOWER(U.UNID_DESC) AS UNID_DESC, J.NODO_PADRE, ISNULL(T.RUT,'') AS RUT, ";
			query += " ISNULL(T.DIGITO_VER,'') AS DIGITO_VER, ISNULL(T.NOMBRE,'') AS NOMBRE ";
			query += " FROM EJE_GES_UNIDADES U INNER JOIN EJE_GES_JERARQUIA J ON U.UNID_ID = J.NODO_ID ";
			query += " LEFT OUTER JOIN EJE_GES_TRABAJADOR T ON T.UNIDAD = U.UNID_ID ";
			query += " WHERE u.unid_id in (select distinct unidad ";
            query += " 					   from eje_ges_trabajador ";
            query2 = "select empresa from eje_ges_uni_sup";
            unidades.exec(query2);
            
            if(unidades.next()) {
            	query += " 					   where wp_cod_empresa=" + unidades.getString("empresa");
            }
            
            query += " 					   )";
			query += " ORDER BY U.UNID_ID ";

			unidades.exec(query);
			modelRoot.put("trabajadores", unidades.getSimpleList() );
			unidades.close();

			super.retTemplate(resp,"Gestion/MaestroUnidades.htm",modelRoot);
		}
		else {
			mensaje.devolverPaginaSinSesion(resp,"","Tiempo de Sesi\363n expirado...");
		}
		connMgr.freeConnection("portal",Conexion);
	}

}