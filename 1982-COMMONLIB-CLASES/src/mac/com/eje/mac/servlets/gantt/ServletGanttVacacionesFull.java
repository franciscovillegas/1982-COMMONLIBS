package mac.com.eje.mac.servlets.gantt;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.datos.Consulta;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class ServletGanttVacacionesFull extends MyHttpServlet {
	private static final long serialVersionUID = 1L;
	
	public static String[] month = {"ENERO","FEBRERO","MARZO","ABRIL","MAYO","JUNIO",
		"JULIO","AGOSTO","SEPTIEMBRE","OCTUBRE","NOVIEMBRE","DICIEMBRE"};

	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
  		response.setContentType("text/html");
  		
  		String excel = request.getParameter("excel")==null?"0":request.getParameter("excel");
        if(excel.equals("1")){
            response.setContentType("application/vnd.ms-excel");
            System.out.println("ES EXCEL");
        }else{
        	response.setContentType("text/html");
        	System.out.println("ES HTML");
        }
        
  		Template template = getTemplate("gantt/gantt_full.htm");
  		SimpleHash modelRoot = new SimpleHash();
  		PrintWriter out = response.getWriter();
  		String p_unidad = request.getParameter("unidad");
  		String mes = request.getParameter("mes");
  		String ann = request.getParameter("ann");  		
  		
  		System.out.println("MES: " + mes + " AÑO: " + ann);
  		
  		Calendar hoy;
  		if (mes != null && ann != null) {
  			try {
  				hoy = new GregorianCalendar(Integer.parseInt(ann), Integer.parseInt(mes) - 1, 1);
  			} catch (NumberFormatException e) {
  				
  				hoy = new GregorianCalendar();
  			}
  		} else {
  			hoy = new GregorianCalendar();
  		}
  		
  		int max_day = hoy.getActualMaximum(Calendar.DAY_OF_MONTH);
		SimpleList dias = new SimpleList(); 
		for (int i = 1; i <= max_day; i++) {
			SimpleHash dia = new SimpleHash();
			dia.put("dia", String.valueOf(i));
			dias.add(dia);
		}
		modelRoot.put("dias", dias);
		Connection conn = connMgr.getConnection("portal");
		
  		// MAIN**********************************************************************************************
  		int mesActual = hoy.get(Calendar.MONTH)+1;	
  		System.out.println("dia max: " + max_day + " y mes actual: " + mesActual);
        modelRoot.put("barras", GanttJefeAll(p_unidad, dimeFechaSQL(hoy), max_day, mesActual, conn));
        modelRoot.put("varios", ListJefeAll(p_unidad, dimeFechaSQL(hoy), conn));
        // TOP **********************************************************************************************
		int mm = hoy.get(Calendar.MONTH);
		int aa = hoy.get(Calendar.YEAR);
		modelRoot.put("dias", dias);		
		modelRoot.put("mesNumerico", mes);
		modelRoot.put("unidad", p_unidad);
		modelRoot.put("mes", month[mm]);
		modelRoot.put("ann", String.valueOf(aa));
		modelRoot.put("today", String.valueOf(new StringBuilder("?mes=")
				.append(hoy.get(Calendar.MONTH) + 1).append("&ann=").append(hoy.get(Calendar.YEAR))
				.append("&unidad=").append(p_unidad)));
		hoy.add(Calendar.MONTH, 1);
		modelRoot.put("next", String.valueOf(new StringBuilder("?mes=")
				.append(hoy.get(Calendar.MONTH) + 1).append("&ann=").append(hoy.get(Calendar.YEAR))
				.append("&unidad=").append(p_unidad)));
		hoy.add(Calendar.MONTH, -2);
		modelRoot.put("prev", String.valueOf(new StringBuilder("?mes=")
				.append(hoy.get(Calendar.MONTH) + 1).append("&ann=").append(hoy.get(Calendar.YEAR))
				.append("&unidad=").append(p_unidad)));

 		
        if(excel.equals("1")){
        	modelRoot.put("xls","1");
        }
        


		try {
			template.process(modelRoot, out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

        connMgr.freeConnection("portal", conn);
  		out.flush();
  		out.close();
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}	
    
    public SimpleList GanttJefeAll(String unidad, String fecha, int max_day, int mesActual, Connection conn) {
		System.out.println("[][GanttJefeAll]Entrando...");
		System.out.println("FECHA HOY :" + fecha);
		int i;
		SimpleList matrix	= new SimpleList();
		Consulta consulta	= new Consulta(conn);
		
		StringBuilder sql	= new StringBuilder("exec eje_vac_planif_vacaciones '").append(unidad).append("','").append(fecha).append("'");
		consulta.exec(String.valueOf(sql));
		System.out.println(sql);
		
		String rutAux		= "";
		String clase		= "1";
		String estado		= "";
		int[] fila			= null;
    	String[] vac		= new String[max_day];  
		String nom			= "";
		String estadoAux 	= "";
		try {
			if (consulta.next()) {
				do {
					nom				= consulta.getString("nombre_new");
					estadoAux		= consulta.getString("estado");
                    String info		= " INICIO: "  + consulta.getString("fecha_inicio") + ", "
					            	+ " TÉRMINO: " + consulta.getString("fecha_termino");					
					String rut		= consulta.getString("rut");
					int num_dias	= consulta.getInt("dias");
					int desde		= 0;
					try {						
						// ******************************************************************************************************
						if(consulta.getString("cal").equals("AHORA")){
							desde = Integer.parseInt(consulta.getString("fecha_inicio").substring(0,2));							
							int mesTermino = Integer.parseInt(consulta.getString("fecha_termino").substring(3,5));
							if (mesTermino > mesActual){
								System.out.println("el mes de termino de las vacaciones es mayor que el mes actual....truncar calendario.");
								int factorMinus = (num_dias + desde - 1) - max_day;
								num_dias = num_dias - factorMinus;
							}else if (mesTermino == mesActual){
								System.out.println("el mes de termino de las vacaciones es igual que el mes actual");
							}else {
								if (mesTermino == 1 && mesActual == 12){
									System.out.println("el mes de termino de las vacaciones es enero del siguiente año....truncar calendario.");
									int factorMinus = (num_dias + desde - 1) - max_day;
									num_dias = num_dias - factorMinus;
								}
							}
						} else if (consulta.getString("cal").equals("ANTES")){
							System.out.println("el mes de inicio de las vacaciones es menor que el mes actual....mostrar segmento para el mes.");
							desde = 1;
							num_dias = Integer.parseInt(consulta.getString("fecha_termino").substring(0,2));
						}
						// ******************************************************************************************************
					} catch (NumberFormatException e) {
						System.out.println("[][][Exception] "+ e.toString());
					}
					if (!rut.equals(rutAux)) {
						if (!"".equals(rutAux)) {
							SimpleHash rutTrabajador = new SimpleHash();
							SimpleList dias = new SimpleList();
							for (i = 0; i < max_day; i++) {
								SimpleHash dia = new SimpleHash();
								dia.put("estado", estado);
								dia.put("ocupado", String.valueOf(fila[i]));
								dia.put("vac", vac[i]);
								dias.add(dia);
							}
							
							rutTrabajador.put("rut", dias);
							rutTrabajador.put("clase", clase);
							matrix.add(rutTrabajador);
							if ("1".equals(clase)) {
								clase = "2";
							} else {
								clase = "1";
							}
						}
						
						estado = consulta.getString("estado");
						
						fila = new int[max_day];
						for (i = 0; i < max_day; i++) {
							fila[i] = 0; 
							vac[i] = "";
						}
					}
					
					for (i = (desde - 1); i < (num_dias + desde - 1); i++) {
						
						if (rut.equals(rutAux))
						{
							fila[i] = 11;
						}else{
							fila[i] = 1;
						}
						 
						vac[i] = info; 
					}
					
					if (!rut.equals(rutAux)) {
						rutAux = rut;
					}
				} while (consulta.next());
				
				if (!"".equals(rutAux)) {
					SimpleHash rutTrabajador = new SimpleHash();
					SimpleList dias = new SimpleList();
					for (i = 0; i < max_day; i++) {
						SimpleHash dia = new SimpleHash();
						
						if (fila[i] == 11){
							dia.put("estado", estadoAux);
						}else{
							dia.put("estado", estado);
						}
						
						dia.put("ocupado", String.valueOf(fila[i]));
						dia.put("vac", vac[i]);
						dias.add(dia);
					}					
					rutTrabajador.put("rut", dias);
					rutTrabajador.put("clase", clase);
					rutTrabajador.put("nom", nom);
					matrix.add(rutTrabajador);
					if ("1".equals(clase)) {
						clase = "2";
					} else {
						clase = "1";
					}
				}
			}
		} catch (Exception e) {
			System.out.println("[][GanttJefeAll][Exception]" + e.toString());
		}
		consulta.close();
		consulta = null;
		System.out.println("[][GanttJefeAll]Saliendo...");
		return matrix;
	}
    
    public SimpleList ListJefeAll(String unidad, String fecha, Connection conn) {
		System.out.println("[][ListJefeAll]Entrando...");
		System.out.println("FECHA HOY :" + fecha);
		SimpleList matrix = new SimpleList();
		Consulta consulta = new Consulta(conn);
		StringBuilder sql = new StringBuilder("exec eje_vac_planif_vacaciones '").append(unidad).append("','").append(fecha).append("'");
		consulta.exec(String.valueOf(sql));
		System.out.println("lista: " + sql);
		String rutAux = "";
		String clase = "2";
		try {
			while (consulta.next()) {
				String rut = consulta.getString("rut");
				if (!rut.equals(rutAux)) {
					String rutT = String.valueOf(new StringBuilder(rut).append("-").append(consulta.getString("digito_ver")));
					SimpleHash trabajador = new SimpleHash();
					trabajador.put("rut", rutT);
					trabajador.put("nombre", consulta.getString("nom") + " " + consulta.getString("paterno"));
					
					if ("1".equals(clase)) {
						clase = "2";
					} else {
						clase = "1";
					}
					System.out.println("CLASEE:" + clase);
					trabajador.put("clase", clase); 
					
					matrix.add(trabajador);
					rutAux = rut;
				}
			}
		} catch (Exception e) {
			System.out.println("[][ListJefeAll][Exception]"+ e.toString());
		}
		consulta.close();
		consulta = null;
		System.out.println("[][ListJefeAll]Saliendo...");
		return matrix;
	}    
    
    public String dimeFechaSQL(Calendar fecha) {
    	Date d = fecha.getTime();
        java.text.DateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy");
        return sdf.format(d);
    }
}
