package portal.com.eje.serhumano.certificados;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import freemarker.template.Template;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.Tools;

public class S_Init_Comisiones  extends MyHttpServlet {
  
	private Usuario user;
	private Mensaje mensaje;
  

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
  
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String rutOrg = req.getParameter("rut_org") == null ? "false" : req.getParameter("rut_org");
		MuestraDatos(req, resp, rutOrg);
	}
  
	public void MuestraDatos(HttpServletRequest req, HttpServletResponse resp, String rutOrg) throws IOException, ServletException {
    
		this.user = SessionMgr.rescatarUsuario(req);
		Connection connection = super.getConnMgr().getConnection("portal");
		SimpleHash modelRoot = new SimpleHash();
		PrintWriter out = resp.getWriter();
    
		if (this.user.esValido()) {
			String rut = !rutOrg.equals("false") ? rutOrg : this.user.getRutId();
			modelRoot.put("rutOrg", rutOrg);
			modelRoot.put("rutTrabajador", rut);
			ConsultaData data = getPeriodosComisiones(rut, connection);
			modelRoot.put("periodosCom", getPeriodos(data));
			Template template = getTemplate("certificados/main_comisiones.html");
			template.process(modelRoot, out);
			out.flush();
			out.close();
		} else {
			this.mensaje.devolverPaginaSinSesion(resp, "Comisiones", "Tiempo de Sesión expirado...");
		}
		
		super.getConnMgr().freeConnection("portal", connection);
	}
  
	private ConsultaData getPeriodosComisiones(String rut, Connection con) {

		StringBuffer sql = new StringBuffer("SELECT DISTINCT TOP 12 c.ID_COMISION peri_id,c.ANO_PERIODO peri_ano, ")
				.append("c.MES_PERIODO peri_mes,c.ID_VENDEDOR rut FROM (qsmfreseniuskabi..RESUMEN_COMISION c ")
				.append("INNER JOIN mac_qsmfreseniuskabi..eje_qsmcom_periodo p ON c.ANO_PERIODO=p.año_periodo AND ")
				.append("c.MES_PERIODO=p.mes_periodo) INNER JOIN eje_ges_periodo pp ON c.ANO_PERIODO=pp.peri_ano ")
				.append("AND c.MES_PERIODO=pp.peri_mes WHERE c.ID_VENDEDOR=").append(rut).append(" AND c.ID_REQ<>0 AND ")
				.append("c.ID_ESTADO_COMISION=4 AND c.ID_COMISION IN (SELECT MAX(ID_COMISION) periodo FROM ")
				.append("qsmfreseniuskabi..RESUMEN_COMISION WHERE ID_VENDEDOR=").append(rut).append(" AND ID_ESTADO_COMISION=4 ")
				.append("AND ID_REQ<>0 AND ANO_PERIODO=c.ANO_PERIODO AND MES_PERIODO=c.MES_PERIODO) order by c.ID_COMISION desc");
		
		try {
			return ConsultaTool.getInstance().getData(con, sql.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
  
	private SimpleList getPeriodos(ConsultaData data) {
		
		SimpleList periodos = new SimpleList();
		boolean primero = true;
		
		while (data.next()) {
			SimpleHash periodo = new SimpleHash();
			periodo.put("id", String.valueOf(data.getInt("peri_id")));
			periodo.put("desc", Tools.RescataMes(data.getInt("peri_mes")) + " de " + data.getInt("peri_ano"));
			
			if (primero) {
				periodo.put("primero", "selected");
				primero = false;
			}
			
			periodos.add(periodo);
		}
		
		return periodos;
		
	}
	
}
