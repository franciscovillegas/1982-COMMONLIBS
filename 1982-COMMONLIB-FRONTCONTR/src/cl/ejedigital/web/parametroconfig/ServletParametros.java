package cl.ejedigital.web.parametroconfig;

import cl.ejedigital.web.MyHttpServlet;


public class ServletParametros extends MyHttpServlet {
	
	/**
	
	private static final long	serialVersionUID	= 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req,resp);
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)  {
		Validar val = new Validar();
		
		String area = val.validarDato(req.getParameter("area"),"");
		String accion = val.validarDato(req.getParameter("accion"),"load");
		
		try {
			resp.setContentType("text/html");
	        resp.setHeader("Expires", "0");
	        resp.setHeader("Pragma", "no-cache");
	        
			if("load".equals(accion)) {
				load(req,resp);
			}
			else {
				String respuesta = "";

		        
				if("select".equals(accion)) {
					respuesta = select(req,resp);
				}
				else if("save".equals(accion)) {
					
				}
				else if("update".equals(accion)) {
					
				}
				
				Writer out = resp.getWriter();
				out.write(respuesta);
				out.flush();
				out.close();
			}
		}
		catch (ServletException e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		catch (IOException e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		catch (SQLException e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
	private void load(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		String htm = "parametros/main.htm";

		Template template = super.getTemplate(htm);
		PrintWriter out = resp.getWriter();
		SimpleHash modelRoot = new SimpleHash();
		
		template.process(modelRoot,out);
		out.flush();
		out.close();

	}
	
	private String select(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
		Validar val = new Validar();
		String thing = val.validarDato(req.getParameter("thing"),"");
		
		
		if("parametros".equals(thing)) {
			return selectParametros(req.getParameter("order"));
		}
		
		return null;
	}
	
	private String selectParametros(String order) throws SQLException {
		Connection conn = super.connMgr.getConnection("portal");
		ParametroManager parametros = new ParametroManager(conn);
		
		IDataOut tool = new JavascriptArrayDataOut(parametros.getParametros());
		
		super.connMgr.freeConnection("portal",conn);
		
		if(order == null) {
			return tool.getListData();
		}
		else {
			return tool.getListData(order.split(","));
		}
	}
	
	
	private void save(HttpServletRequest req, HttpServletResponse resp) {
		
	}
	
	private void update(HttpServletRequest req, HttpServletResponse resp) {
		
	}
	 * 
	 */
}
