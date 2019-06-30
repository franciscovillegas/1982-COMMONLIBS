package organica.com.eje.ges.gestion;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.menu.Tools;
import portal.com.eje.serhumano.tracking.trackingManager;
import portal.com.eje.tools.ExtrasOrganica;
import portal.com.eje.tools.FormatearNumeros;
import portal.com.eje.tools.Validar;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class ReportesOrganica extends MyHttpServlet {

	public ReportesOrganica() {
		super();
	}   	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}  	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {
        String unidad = request.getParameter("unidad");
        String recursivo = request.getParameter("recursivo");
        String tipo = request.getParameter("tipo");
        String empresa = request.getParameter("empresa");
        if(tipo.equals("C")) {
        	doCargos(request,response,unidad,recursivo,empresa);
        }
        else if(tipo.equals("D")) {
        	doDotacion(request,response,unidad,recursivo,empresa);
        }
        else if(tipo.equals("DFI")) {
        	doDotacionTipoContrato(request,response,unidad,recursivo,empresa);
        }
        else if(tipo.equals("CE")) {
        	doDotacionCE(request,response,unidad,recursivo,empresa);
        }
        else if(tipo.equals("V")) {
        	doVacaciones(request,response,unidad,recursivo,empresa);
        }
        else if(tipo.equals("R")) {
        	doRentas(request,response,unidad,recursivo,empresa);
        }
        else if(tipo.equals("CR")) {
        	doCambioRentas(request,response,unidad,recursivo,empresa);
        }
        else if(tipo.equals("CP")) {
        	doCambioPersonal(request,response,unidad,recursivo,empresa);
        }
        else if(tipo.equals("VP")) {
        	doVinPer(request,response,unidad,recursivo,empresa);
        }
        else if(tipo.equals("DP")) {
        	doDesPer(request,response,unidad,recursivo,empresa);
        }
        else if(tipo.equals("DHD")) {
        	doDifHabDescto(request,response,unidad,recursivo,empresa);
        }
        else if(tipo.equals("HEX")) {
        	doHorasExtras(request,response,unidad,recursivo,empresa);
        }
        else if(tipo.equals("LICMED")) {
        	doLicenciasMedicas(request,response,unidad,recursivo,empresa);
        }
        else if(tipo.equals("INFTRAB")) {
        	doInformeTrabajadores(request,response,unidad,recursivo,empresa);
        }
        else if(tipo.equals("RR")) {
        	doBaseMasGratificacion(request,response,unidad,recursivo,empresa);
        }
        else if(tipo.equals("LE")) {
        	doLiquidacionEstandar(request,response,unidad,recursivo,empresa);
        }
    }  
	
	protected void doCargos(HttpServletRequest request, HttpServletResponse response, String unidad, 
		String recursivo, String empresa) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String excel = request.getParameter("excel")==null?"0":request.getParameter("excel");
        if(excel.equals("1")) {
        	response.setContentType("application/vnd.ms-excel");
        }
        else {
        	response.setContentType("text/html");
        }
        response.setHeader("Expires", "0");
        response.setHeader("Pragma", "no-cache");
        Template template = getTemplate("Gestion/Reportes/Cargos.htm");
        SimpleHash modelRoot = new SimpleHash ();
        Validar valida = new Validar();        
        Connection conecction = connMgr.getConnection("portal");
        Consulta cons = new Consulta(conecction);
        Consulta cons2 = new Consulta(conecction);
        String nomUnidad = ExtrasOrganica.UnidadDesc(unidad,conecction).toUpperCase();
        String query = "select distinct c.descrip from (eje_ges_trabajador t inner join eje_ges_cargos c on t.cargo=c.cargo and t.wp_cod_empresa=c.empresa) inner join eje_ges_unidades u on t.unidad=u.unid_id where unidad ";
        if(recursivo.equals("S")) {  
        	query+=" in (";
        	List lista2 = new ArrayList(); lista2 = ExtrasOrganica.NodosHijosRecursivos(unidad,empresa,conecction,lista2); lista2.add(unidad); 
       	   	for(int i=0;i<lista2.size();i++) {  
       	   		query+="'" + lista2.get(i).toString() + "'";
       	   		if(i<lista2.size()-1) {
       	   			query+=",";
       	   		}
       	   	}
       	   	query+=")";
        }
        else {  
        	query+= "= '" + unidad + "'"; 
        }
        query+= " order by c.descrip";
        cons.exec(query);
        SimpleList CargoList = new SimpleList();
    	SimpleHash CargoIter;
    	CargoIter = new SimpleHash();
    	int totalPP=0;
        for(;cons.next();CargoList.add(CargoIter)) {
        	CargoIter = new SimpleHash();
        	CargoIter.put("cdesc",valida.validarDato(cons.getString("descrip").toUpperCase()) );
        	SimpleList PersonaList = new SimpleList();
        	SimpleHash PersonaIter;
        	PersonaIter = new SimpleHash();
        	String query1 = "select distinct t.rut,t.digito_ver,t.nombre,Convert(datetime, t.fecha_ingreso, 13) as fi,Convert(datetime, t.fec_ter_cont, 13) as ft,t.unidad,u.unid_desc from (eje_ges_trabajador t inner join eje_ges_unidades u on t.unidad=u.unid_id) inner join eje_ges_cargos c on t.cargo=c.cargo where t.unidad ";
            if(recursivo.equals("S")) {  
            	query1+=" in (";
            	List lista2 = new ArrayList(); lista2 = ExtrasOrganica.NodosHijosRecursivos(unidad,empresa,conecction,lista2); lista2.add(unidad); 
           	   	for(int i=0;i<lista2.size();i++) {  
           	   		query1+= "'" + lista2.get(i).toString() + "'";
           	   		if(i<lista2.size()-1) {
           	   			query1+=",";
           	   		}
           	   	}
           	   	query1+=")";
            }
            else { 
            	query1+= "= '" + unidad + "'"; 
            }
            query1+= " and c.descrip='" + valida.validarDato(cons.getString("descrip")) + "' order by t.nombre";
            cons2.exec(query1);
            int totalP=0;
        	for(;cons2.next();PersonaList.add(PersonaIter)) {
        		PersonaIter = new SimpleHash();
        		PersonaIter.put("rut",valida.validarDato(cons2.getString("rut")) );
        		PersonaIter.put("digito",valida.validarDato(cons2.getString("digito_ver").toUpperCase()) );
        		PersonaIter.put("nombre",valida.validarDato(cons2.getString("nombre").toUpperCase()) );
        		PersonaIter.put("fecing",valida.validarFecha(cons2.getString("fi")) );
        		PersonaIter.put("fecter",valida.validarFecha(cons2.getString("ft"),"INDEFINIDO") );
        		PersonaIter.put("cunidad",valida.validarDato(cons2.getString("unidad")) );
        		PersonaIter.put("dunidad",valida.validarDato(cons2.getString("unid_desc").toUpperCase()) );
        		totalP++;
        	}
        	CargoIter.put("lpersona",PersonaList);
        	CargoIter.put("totalpersona",String.valueOf(totalP));
        	totalPP+=totalP; 
        }
        modelRoot.put("unidad",unidad);
        modelRoot.put("empresa",empresa);
        modelRoot.put("recursivo",recursivo);
        if(excel.equals("1")) {
        	modelRoot.put("xls","1");
        }
        modelRoot.put("lcargos",CargoList);
        modelRoot.put("total",String.valueOf(totalPP));
        if(recursivo.equals("S")) {
        	nomUnidad = "AREA " + nomUnidad; 
        }
        else {
        	nomUnidad = "UNIDAD " + nomUnidad;
        }
        modelRoot.put("nomUnidad",nomUnidad);
    	try {
			template.process(modelRoot,out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	out.flush();
    	out.close();
    	//template.process(out);	
	}
	
	protected void doDotacion(HttpServletRequest request, HttpServletResponse response, 
		String unidad, String recursivo, String empresa) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String excel = request.getParameter("excel")==null?"0":request.getParameter("excel");
        if(excel.equals("1")) {
        	response.setContentType("application/vnd.ms-excel");
        }
        else {
        	response.setContentType("text/html");
        }
        response.setHeader("Expires", "0");
        response.setHeader("Pragma", "no-cache");
        Template template = getTemplate("Gestion/Reportes/Dotacion.htm");
        SimpleHash modelRoot = new SimpleHash ();
        Validar valida = new Validar();        
        Connection conecction = connMgr.getConnection("portal");
        Consulta cons = new Consulta(conecction);
        Consulta cons2 = new Consulta(conecction);
        String nomUnidad = ExtrasOrganica.UnidadDesc(unidad,conecction).toUpperCase();
        String query = "select unid_id,unid_desc from eje_ges_unidades where unid_id ";
        if(recursivo.equals("S")) {  
        	query+="in (";
        	List lista2 = new ArrayList(); lista2 = ExtrasOrganica.NodosHijosRecursivos(unidad,empresa,conecction,lista2); lista2.add(unidad); 
       	   	for(int i=0;i<lista2.size();i++) {  
       	   		query+= "'" + lista2.get(i).toString() + "'";
       	   		if(i<lista2.size()-1) {
       	   			query+=",";
       	   		}
       	   	}
       	   	query+=")";
        }
        else {  
        	query+= "= '" + unidad + "'"; 
        }
        query+= " order by unid_desc";
        cons.exec(query);
        SimpleList UnidadList = new SimpleList();
    	SimpleHash UnidadIter;
    	UnidadIter = new SimpleHash();
    	int thaberesarea=0,tpersonas=0,tcf=0,tci=0;
        for(;cons.next();UnidadList.add(UnidadIter)) {
        	UnidadIter = new SimpleHash();
        	UnidadIter.put("uid",valida.validarDato(cons.getString("unid_id")) );
        	UnidadIter.put("udesc",valida.validarDato(cons.getString("unid_desc").toUpperCase()) );
        	SimpleList PersonaList = new SimpleList();
        	SimpleHash PersonaIter;
        	PersonaIter = new SimpleHash();
        	String query1= "select t.rut,t.digito_ver,t.nombre,t.cargo,c.descrip,Convert(datetime, t.fecha_ingreso, 13) as fi,Convert(datetime, t.fec_ter_cont, 13) as ft,lc.tot_haberes from (eje_ges_trabajador t inner join eje_ges_cargos c on t.cargo=c.cargo and t.wp_cod_empresa=c.empresa) inner join eje_ges_certif_histo_liquidacion_cabecera lc on t.rut=lc.rut where t.unidad='" + valida.validarDato(cons.getString("unid_id")) + "' and lc.periodo = (select max(peri_id) from eje_ges_periodo) order by t.nombre";
            cons2.exec(query1);
            int i=0,thaberes=0,cfijo=0,cindefinido=0;
        	for(;cons2.next();PersonaList.add(PersonaIter)) {
        		PersonaIter = new SimpleHash();
        		PersonaIter.put("rut",valida.validarDato(cons2.getString("rut")) );
        		PersonaIter.put("digito",valida.validarDato(cons2.getString("digito_ver").toUpperCase()) );
        		PersonaIter.put("nombre",valida.validarDato(cons2.getString("nombre").toUpperCase()) );
        		PersonaIter.put("ccargo",valida.validarDato(cons2.getString("cargo")) );
        		PersonaIter.put("dcargo",valida.validarDato(cons2.getString("descrip").toUpperCase()) );
        		PersonaIter.put("fecing",valida.validarFecha(cons2.getString("fi")) );
        		PersonaIter.put("fecter",valida.validarFecha(cons2.getString("ft"),"INDEFINIDO") );
        		PersonaIter.put("haberes",valida.validarDato(FormatearNumeros.numero( Integer.parseInt(cons2.getString("tot_haberes")) )));
        		i++;
        		thaberes+=Integer.parseInt(cons2.getString("tot_haberes"));
        		if(valida.validarFecha(cons2.getString("ft"),"INDEFINIDO").equals("INDEFINIDO")) {
        			cindefinido++;
        		}
        		else {
        			cfijo++;
        		}
        	}
        	if(i!=0) {  
        		UnidadIter.put("lpersona",PersonaList);
        		UnidadIter.put("tpersonasU",String.valueOf(i));
        		UnidadIter.put("tpersonasCF",String.valueOf(cfijo));
        		UnidadIter.put("tpersonasCI",String.valueOf(cindefinido));
        	}
        	else {
        		UnidadIter.put("mensaje","0");
        	}
        	UnidadIter.put("thaberes",valida.validarDato(FormatearNumeros.numero(thaberes)));
        	thaberesarea+=thaberes;
        	tpersonas+=i;
        	tcf+=cfijo;
        	tci+=cindefinido;
        }
        modelRoot.put("lunidades",UnidadList);
        modelRoot.put("thaberesarea",valida.validarDato(FormatearNumeros.numero(thaberesarea)));
        modelRoot.put("tpersonasA",String.valueOf(tpersonas));
        modelRoot.put("tpersonasCFA",String.valueOf(tcf));
        modelRoot.put("tpersonasCIA",String.valueOf(tci));
        modelRoot.put("unidad",unidad);
        modelRoot.put("empresa",empresa);
        modelRoot.put("recursivo",recursivo);
        if(excel.equals("1")) {
        	modelRoot.put("xls","1");
        }
        if(recursivo.equals("S")) {
        	nomUnidad = "AREA " + nomUnidad; 
        }
        else {
        	nomUnidad = "UNIDAD " + nomUnidad;
        }
        modelRoot.put("nomUnidad",nomUnidad);
    	try {
			template.process(modelRoot,out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	out.flush();
    	out.close();

	}
	
	protected void doDotacionTipoContrato(HttpServletRequest request, HttpServletResponse response, 
		String unidad, String recursivo, String empresa) throws ServletException, IOException {
		//plazo fijo
        PrintWriter out = response.getWriter();
        String excel = request.getParameter("excel")==null?"0":request.getParameter("excel");
        if(excel.equals("1")) {
        	response.setContentType("application/vnd.ms-excel");
        }
        else {
        	response.setContentType("text/html");
        }
        response.setHeader("Expires", "0");
        response.setHeader("Pragma", "no-cache");
        Template template = getTemplate("Gestion/Reportes/DotacionTipoContrato.htm");
        SimpleHash modelRoot = new SimpleHash ();
        Validar valida = new Validar();        
        Connection conecction = connMgr.getConnection("portal");
        Consulta cons = new Consulta(conecction);
        Consulta cons2 = new Consulta(conecction);
        String nomUnidad = ExtrasOrganica.UnidadDesc(unidad,conecction).toUpperCase();
        String query = "select unid_id,unid_desc from eje_ges_unidades where unid_id ";
        if(recursivo.equals("S")) {  
        	query+="in (";
        	List lista2 = new ArrayList(); lista2 = ExtrasOrganica.NodosHijosRecursivos(unidad,empresa,conecction,lista2); lista2.add(unidad); 
       	   	for(int i=0;i<lista2.size();i++) {  
       	   		query+= "'" + lista2.get(i).toString() + "'";
       	   		if(i<lista2.size()-1) {
       	   			query+=",";
       	   		}
       	   	}
       	   	query+=")";
        }
        else {  
        	query+= "= '" + unidad + "'"; 
        }
        query+= " order by unid_desc";
        cons.exec(query);
        SimpleList UnidadList = new SimpleList();
    	SimpleHash UnidadIter;
    	UnidadIter = new SimpleHash();
    	int thaberesarea=0,tpersonas=0,tcf=0,tci=0;
        for(;cons.next();UnidadList.add(UnidadIter)) {
        	UnidadIter = new SimpleHash();
        	UnidadIter.put("uidF",valida.validarDato(cons.getString("unid_id")) );
        	UnidadIter.put("udescF",valida.validarDato(cons.getString("unid_desc").toUpperCase()) );
        	SimpleList PersonaList = new SimpleList();
        	SimpleHash PersonaIter;
        	PersonaIter = new SimpleHash();
        	String query1= "select t.rut,t.digito_ver,t.nombre,t.cargo,c.descrip,Convert(datetime, t.fecha_ingreso, 13) as fi,Convert(datetime, t.fec_ter_cont, 13) as ft,lc.tot_haberes from (eje_ges_trabajador t inner join eje_ges_cargos c on t.cargo=c.cargo and t.wp_cod_empresa=c.empresa) inner join eje_ges_certif_histo_liquidacion_cabecera lc on t.rut=lc.rut where t.unidad='" + valida.validarDato(cons.getString("unid_id")) + "' and lc.periodo = (select max(peri_id) from eje_ges_periodo) and (t.fec_ter_cont IS NOT NULL) order by t.nombre";
            cons2.exec(query1);
            int i=0,thaberes=0,cfijo=0,cindefinido=0;
        	for(;cons2.next();PersonaList.add(PersonaIter)) {
        		PersonaIter = new SimpleHash();
        		PersonaIter.put("rutF",valida.validarDato(cons2.getString("rut")) );
        		PersonaIter.put("digitoF",valida.validarDato(cons2.getString("digito_ver").toUpperCase()) );
        		PersonaIter.put("nombreF",valida.validarDato(cons2.getString("nombre").toUpperCase()) );
        		PersonaIter.put("ccargoF",valida.validarDato(cons2.getString("cargo")) );
        		PersonaIter.put("dcargoF",valida.validarDato(cons2.getString("descrip").toUpperCase()) );
        		PersonaIter.put("fecingF",valida.validarFecha(cons2.getString("fi")) );
        		PersonaIter.put("fecterF",valida.validarFecha(cons2.getString("ft"),"INDEFINIDO") );
        		PersonaIter.put("haberesF",valida.validarDato(FormatearNumeros.numero( Integer.parseInt(cons2.getString("tot_haberes")) )));
        		i++;
        		thaberes+=Integer.parseInt(cons2.getString("tot_haberes"));
        		if(valida.validarFecha(cons2.getString("ft"),"INDEFINIDO").equals("INDEFINIDO")) {
        			cindefinido++;
        		}
        		else {
        			cfijo++;
        		}
        	}
        	if(i!=0) {  
        		UnidadIter.put("lpersonaF",PersonaList);
        		UnidadIter.put("tpersonasUF",String.valueOf(i));
        		UnidadIter.put("tpersonasCFF",String.valueOf(cfijo));
        		UnidadIter.put("tpersonasCIF",String.valueOf(cindefinido));
        	}
        	else {
        		UnidadIter.put("mensajeF","0");
        	}
        	UnidadIter.put("thaberesF",valida.validarDato(FormatearNumeros.numero(thaberes)));
        	thaberesarea+=thaberes;
        	tpersonas+=i;
        	tcf+=cfijo;
        	tci+=cindefinido;
        }
        modelRoot.put("lunidadesF",UnidadList);
        modelRoot.put("thaberesareaF",valida.validarDato(FormatearNumeros.numero(thaberesarea)));
        modelRoot.put("tpersonasAF",String.valueOf(tpersonas));
        modelRoot.put("tpersonasCFAF",String.valueOf(tcf));
        modelRoot.put("tpersonasCIAF",String.valueOf(tci));
        if(recursivo.equals("S")) {
        	nomUnidad = "AREA " + nomUnidad; 
        }
        else {
        	nomUnidad = "UNIDAD " + nomUnidad;
        }
        modelRoot.put("nomUnidadF",nomUnidad);
    	//plazo indefinido
        query = "select unid_id,unid_desc from eje_ges_unidades where unid_id ";
        if(recursivo.equals("S")) {  
        	query+="in (";
        	List lista2 = new ArrayList(); lista2 = ExtrasOrganica.NodosHijosRecursivos(unidad,empresa,conecction,lista2); lista2.add(unidad); 
       	   	for(int i=0;i<lista2.size();i++) {  
       	   		query+= "'" + lista2.get(i).toString() + "'";
       	   		if(i<lista2.size()-1) {
       	   			query+=",";
       	   		}
       	   	}
       	   	query+=")";
        }
        else {  
        	query+= "= '" + unidad + "'"; 
        }
        query+= " order by unid_desc";
        cons.exec(query);
        SimpleList UnidadList2 = new SimpleList();
    	SimpleHash UnidadIter2;
    	UnidadIter2 = new SimpleHash();
    	int thaberesarea2=0,tpersonas2=0,tcf2=0,tci2=0;
        for(;cons.next();UnidadList2.add(UnidadIter2)) {
        	UnidadIter2 = new SimpleHash();
        	UnidadIter2.put("uidI",valida.validarDato(cons.getString("unid_id")) );
        	UnidadIter2.put("udescI",valida.validarDato(cons.getString("unid_desc").toUpperCase()) );
        	SimpleList PersonaList2 = new SimpleList();
        	SimpleHash PersonaIter2;
        	PersonaIter2 = new SimpleHash();
        	String query1= "select t.rut,t.digito_ver,t.nombre,t.cargo,c.descrip,Convert(datetime, t.fecha_ingreso, 13) as fi,Convert(datetime, t.fec_ter_cont, 13) as ft,lc.tot_haberes from (eje_ges_trabajador t inner join eje_ges_cargos c on t.cargo=c.cargo and t.wp_cod_empresa=c.empresa) inner join eje_ges_certif_histo_liquidacion_cabecera lc on t.rut=lc.rut where t.unidad='" + valida.validarDato(cons.getString("unid_id")) + "' and lc.periodo = (select max(peri_id) from eje_ges_periodo) and (t.fec_ter_cont IS NULL) order by t.nombre";
            cons2.exec(query1);
            int i2=0,thaberes2=0,cfijo2=0,cindefinido2=0;
        	for(;cons2.next();PersonaList2.add(PersonaIter2)) {
        		PersonaIter2 = new SimpleHash();
        		PersonaIter2.put("rutI",valida.validarDato(cons2.getString("rut")) );
        		PersonaIter2.put("digitoI",valida.validarDato(cons2.getString("digito_ver").toUpperCase()) );
        		PersonaIter2.put("nombreI",valida.validarDato(cons2.getString("nombre").toUpperCase()) );
        		PersonaIter2.put("ccargoI",valida.validarDato(cons2.getString("cargo")) );
        		PersonaIter2.put("dcargoI",valida.validarDato(cons2.getString("descrip").toUpperCase()) );
        		PersonaIter2.put("fecingI",valida.validarFecha(cons2.getString("fi")) );
        		PersonaIter2.put("fecterI",valida.validarFecha(cons2.getString("ft"),"INDEFINIDO") );
        		PersonaIter2.put("haberesI",valida.validarDato(FormatearNumeros.numero(Integer.parseInt(cons2.getString("tot_haberes")))));
        		i2++;
        		thaberes2+=Integer.parseInt(cons2.getString("tot_haberes"));
        		if(valida.validarFecha(cons2.getString("ft"),"INDEFINIDO").equals("INDEFINIDO")) {
        			cindefinido2++;
        		}
        		else {
        			cfijo2++;
        		}
        	}
        	if(i2!=0) {  
        		UnidadIter2.put("lpersonaI",PersonaList2);
        		UnidadIter2.put("tpersonasUI",String.valueOf(i2));
        		UnidadIter2.put("tpersonasCFI",String.valueOf(cfijo2));
        		UnidadIter2.put("tpersonasCII",String.valueOf(cindefinido2));
        	}
        	else {
        		UnidadIter2.put("mensajeI","0");
        	}
        	UnidadIter2.put("thaberesI",valida.validarDato(FormatearNumeros.numero(thaberes2)));
        	thaberesarea2+=thaberes2;
        	tpersonas2+=i2;
        	tcf2+=cfijo2;
        	tci2+=cindefinido2;
        }
        modelRoot.put("lunidadesI",UnidadList2);
        modelRoot.put("thaberesareaI",valida.validarDato(FormatearNumeros.numero(thaberesarea2)));
        modelRoot.put("tpersonasAI",String.valueOf(tpersonas2));
        modelRoot.put("tpersonasCFAI",String.valueOf(tcf2));
        modelRoot.put("tpersonasCIAI",String.valueOf(tci2));
        modelRoot.put("nomUnidadI",nomUnidad);
        modelRoot.put("unidad",unidad);
        modelRoot.put("empresa",empresa);
        modelRoot.put("recursivo",recursivo);
        if(excel.equals("1")) {
        	modelRoot.put("xls","1");
        }
    	try {
			template.process(modelRoot,out);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	out.flush();
    	out.close();
	}

	protected void doDotacionCE(HttpServletRequest request, HttpServletResponse response, 
		String unidad, String recursivo, String empresa) throws ServletException, IOException { 
        PrintWriter out = response.getWriter();
        String excel = request.getParameter("excel")==null?"0":request.getParameter("excel");
        if(excel.equals("1")) {
        	response.setContentType("application/vnd.ms-excel");
        }
        else {
        	response.setContentType("text/html");
        }
        response.setHeader("Expires", "0");
        response.setHeader("Pragma", "no-cache");
        Template template = getTemplate("Gestion/Reportes/CostoEmpresa.htm");
        SimpleHash modelRoot = new SimpleHash ();
        Validar valida = new Validar();        
        Connection conecction = connMgr.getConnection("portal");
        Consulta cons = new Consulta(conecction);
        Consulta cons2 = new Consulta(conecction);
        Consulta cons3 = new Consulta(conecction);
        String nomUnidad = ExtrasOrganica.UnidadDesc(unidad,conecction).toUpperCase();
        String query = "select distinct t.wp_cod_empresa,e.descrip from eje_ges_trabajador t inner join eje_ges_empresa e on t.wp_cod_empresa=e.empresa where t.unidad ";
        if(recursivo.equals("S")) {  
        	query+="in (";
        	List lista2 = new ArrayList(); lista2 = ExtrasOrganica.NodosHijosRecursivos(unidad,empresa,conecction,lista2); lista2.add(unidad); 
       	   	for(int i=0;i<lista2.size();i++) {  
       	   		query+= "'" + lista2.get(i).toString() + "'";
       	   		if(i<lista2.size()-1) {
       	   			query+=",";
       	   		}
       	   	}
       	   	query+=")";
        }
        else {  
        	query+= "= '" + unidad + "'"; 
        }
        query+= " order by e.descrip";
        cons.exec(query);
        SimpleList EmpresaList = new SimpleList();
    	SimpleHash EmpresaIter;
    	EmpresaIter = new SimpleHash();
    	int total = 0;
        for(;cons.next();EmpresaList.add(EmpresaIter)) {
        	EmpresaIter = new SimpleHash();
        	EmpresaIter.put("idemp",valida.validarDato(cons.getString("wp_cod_empresa")) );
        	EmpresaIter.put("descemp",valida.validarDato(cons.getString("descrip").toUpperCase()) );
        	SimpleList PersonaList = new SimpleList();
        	SimpleHash PersonaIter;
        	PersonaIter = new SimpleHash();
        	query = "select distinct t.rut,t.digito_ver,t.nombre,t.cargo,c.descrip,Convert(datetime, t.fecha_ingreso, 13) as fi,Convert(datetime, t.fec_ter_cont, 13) ft,lc.tot_haberes from (eje_ges_trabajador t inner join eje_ges_cargos c on t.cargo=c.cargo and t.wp_cod_empresa=c.empresa) inner join eje_ges_certif_histo_liquidacion_cabecera lc on t.rut=lc.rut where t.unidad ";
            if(recursivo.equals("S")) {  
            	query+="in (";
            	List lista2 = new ArrayList(); lista2 = ExtrasOrganica.NodosHijosRecursivos(unidad,empresa,conecction,lista2); lista2.add(unidad); 
           	   	for(int i=0;i<lista2.size();i++) {  
           	   		query+= "'" + lista2.get(i).toString() + "'";
           	   		if(i<lista2.size()-1) {
           	   			query+=",";
           	   		}
           	   	}
           	   	query+=")";
            }
            else {  
            	query+= "= '" + unidad + "'"; 
            }
            query+= " and t.wp_cod_empresa=" + valida.validarDato(cons.getString("wp_cod_empresa")) + " and lc.periodo = (select max(peri_id) from eje_ges_periodo) order by t.rut";
            System.out.println(query);
            cons2.exec(query);
            for(;cons2.next();PersonaList.add(PersonaIter)) {
            	PersonaIter = new SimpleHash();
            	PersonaIter.put("rut",valida.validarDato(cons2.getString("rut")) );
            	PersonaIter.put("dg",valida.validarDato(cons2.getString("digito_ver").toUpperCase()) );
            	PersonaIter.put("nombre",valida.validarDato(cons2.getString("nombre").toUpperCase()) );
            	PersonaIter.put("cargo",valida.validarDato(cons2.getString("descrip").toUpperCase()) );
            	PersonaIter.put("fi",valida.validarFecha(cons2.getString("fi")) );
            	PersonaIter.put("ft",valida.validarFecha(cons2.getString("ft"),"INDEFINIDO") );
            	PersonaIter.put("haberes",valida.validarDato(FormatearNumeros.numero(Integer.parseInt(cons2.getString("tot_haberes")))));
            }
            EmpresaIter.put("lpersonas",PersonaList);
            query = "select sum(lc.tot_haberes) as totales from (eje_ges_trabajador t inner join eje_ges_certif_histo_liquidacion_cabecera lc on t.rut=lc.rut) inner join eje_ges_empresa e on t.wp_cod_empresa=e.empresa where lc.periodo=(select max(peri_id) from eje_ges_periodo) and t.unidad ";
            if(recursivo.equals("S")) {  
            	query+="in (";
            	List lista2 = new ArrayList(); lista2 = ExtrasOrganica.NodosHijosRecursivos(unidad,empresa,conecction,lista2); lista2.add(unidad); 
           	   	for(int i=0;i<lista2.size();i++) {  
           	   		query+= "'" + lista2.get(i).toString() + "'";
           	   		if(i<lista2.size()-1) {
           	   			query+=",";
           	   		}
           	   	}
           	   	query+=")";
            }
            else {  
            	query+= "= '" + unidad + "'"; 
            }
            query+= " and t.wp_cod_empresa=" + valida.validarDato(cons.getString("wp_cod_empresa"));
            cons3.exec(query); cons3.next();
            EmpresaIter.put("totalempresa",FormatearNumeros.numero( Integer.parseInt(valida.validarDato(cons3.getString("totales"),"0"))));
            total+= cons3.getInt("totales");
        }
        modelRoot.put("unidad",unidad);
        modelRoot.put("empresa",empresa);
        modelRoot.put("recursivo",recursivo);
        if(excel.equals("1")) {
        	modelRoot.put("xls","1");
        }
        modelRoot.put("lempresas",EmpresaList);
        modelRoot.put("total", FormatearNumeros.numero(total));
        if(recursivo.equals("S")) {
        	nomUnidad = "AREA " + nomUnidad; 
        }
        else {
        	nomUnidad = "UNIDAD " + nomUnidad;
        }
        modelRoot.put("nomUnidad",nomUnidad);
    	try {
			template.process(modelRoot,out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	out.flush();
    	out.close();

	}

	protected void doVacaciones(HttpServletRequest request, HttpServletResponse response, 
		String unidad, String recursivo, String empresa) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String excel = request.getParameter("excel")==null?"0":request.getParameter("excel");
        if(excel.equals("1")) {
        	response.setContentType("application/vnd.ms-excel");
        }
        else {
        	response.setContentType("text/html");
        }
        response.setHeader("Expires", "0");
        response.setHeader("Pragma", "no-cache");
        Template template = getTemplate("Gestion/Reportes/Vacaciones.htm");
        SimpleHash modelRoot = new SimpleHash ();
        Validar valida = new Validar();        
        Connection conecction = connMgr.getConnection("portal");
        Consulta cons = new Consulta(conecction);
        Consulta cons2 = new Consulta(conecction);
        Consulta cons3 = new Consulta(conecction);
        DecimalFormat dec = new DecimalFormat("###.##");        
        String nomUnidad = ExtrasOrganica.UnidadDesc(unidad,conecction).toUpperCase();
        String query = "select unid_id,unid_desc from eje_ges_unidades where unid_id ";
        if(recursivo.equals("S")) {  
        	query+="in (";
        	List lista2 = new ArrayList(); lista2 = ExtrasOrganica.NodosHijosRecursivos(unidad,empresa,conecction,lista2); lista2.add(unidad); 
       	   	for(int i=0;i<lista2.size();i++) {  
       	   		query+= "'" + lista2.get(i).toString() + "'";
       	   		if(i<lista2.size()-1) {
       	   			query+=",";
       	   		}
       	   	}
       	   	query+=")";
        }
        else {  
        	query+= "= '" + unidad + "'"; 
        }
        query+= " order by unid_desc";
        cons.exec(query);
        SimpleList UnidadList = new SimpleList();
    	SimpleHash UnidadIter;
    	UnidadIter = new SimpleHash();
    	float saldoarea = 0.0F, tomadosarea = 0.0F;
    	int j=0;
        for(;cons.next();UnidadList.add(UnidadIter)) {
        	UnidadIter = new SimpleHash();
        	UnidadIter.put("uid",valida.validarDato(cons.getString("unid_id")) );
        	UnidadIter.put("udesc",valida.validarDato(cons.getString("unid_desc").toUpperCase()) );
        	SimpleList PersonaList = new SimpleList();
        	SimpleHash PersonaIter;
        	PersonaIter = new SimpleHash();
        	query = "select t.rut,t.digito_ver,t.nombre,t.cargo,c.descrip,Convert(datetime, t.fecha_ingreso, 13) as fi,Convert(datetime, t.fec_ter_cont, 13) ft,devv=(SELECT top 1 dias_devengados FROM eje_ges_feriad_devengados WHERE rut_cliente = t.rut order by ano_periodo desc,mes_periodo desc),penv=(SELECT  dias_pendientes from eje_ges_vacaciones_wp WHERE rut = t.rut),perv=(SELECT  dias_del_periodo from eje_ges_vacaciones_wp WHERE rut = t.rut),dt = (select sum(dias_normales) from eje_ges_vacaciones_det where rut = t.rut) from eje_ges_trabajador t inner join eje_ges_cargos c on t.cargo=c.cargo and t.wp_cod_empresa=c.empresa where t.unidad = '" + valida.validarDato(cons.getString("unid_id")) + "' order by t.nombre";
            cons2.exec(query);
            int i=0;
            float saldounidad = 0.0F, tomadosunidad = 0.0F;
            for(;cons2.next();PersonaList.add(PersonaIter)) {
            	PersonaIter = new SimpleHash();
            	PersonaIter.put("rut",valida.validarDato(cons2.getString("rut")) );
            	PersonaIter.put("dg",valida.validarDato(cons2.getString("digito_ver").toUpperCase()) );
            	PersonaIter.put("nombre",valida.validarDato(cons2.getString("nombre").toUpperCase()) );
            	PersonaIter.put("cod",valida.validarDato(cons2.getString("cargo")) );
            	PersonaIter.put("cargo",valida.validarDato(cons2.getString("descrip").toUpperCase()) );
            	PersonaIter.put("fi",valida.validarFecha(cons2.getString("fi")) );
            	PersonaIter.put("ft",valida.validarFecha(cons2.getString("ft"),"INDEFINIDO") );
            	float saldo = 0.0F;
            	try {  
            		saldo = cons2.getFloat("devv"); 
            	}
                catch(Exception e) {  
                	saldo = 0.0F; 
                }
                saldo += cons2.getFloat("penv") + cons2.getFloat("perv");
                double d = saldo;
                d= d*100;
                Float f = new Float(d);
                d = f.intValue();
                d = d/100;
            	PersonaIter.put("saldo",String.valueOf(d) );
            	PersonaIter.put("totalvac",valida.validarDato(cons2.getString("dt")) );
            	i++;
            	saldounidad+=saldo;
            	tomadosunidad+=cons2.getFloat("dt");
            }
            UnidadIter.put("lpersonas",PersonaList);
            UnidadIter.put("personas", String.valueOf(i));
            UnidadIter.put("saldounidad",String.valueOf(dec.format(saldounidad)) );
            UnidadIter.put("tomadosunidad",String.valueOf(dec.format(tomadosunidad)) );
            j+=i;
            saldoarea+=saldounidad;
            tomadosarea+=tomadosunidad;
        }
        modelRoot.put("lunidades",UnidadList);
        modelRoot.put("tpersonas", String.valueOf(j));
        modelRoot.put("tsaldoarea",String.valueOf(dec.format(saldoarea)) );
        modelRoot.put("ttomadosarea",String.valueOf(dec.format(tomadosarea)) );
        modelRoot.put("unidad",unidad);
        modelRoot.put("empresa",empresa);
        modelRoot.put("recursivo",recursivo);
        if(excel.equals("1")) {
        	modelRoot.put("xls","1");
        }
        if(recursivo.equals("S")) {
        	nomUnidad = "AREA " + nomUnidad; 
        }
        else {
        	nomUnidad = "UNIDAD " + nomUnidad;
        }
        modelRoot.put("nomUnidad",nomUnidad);
    	try {
			template.process(modelRoot,out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	out.flush();
    	out.close();

	}
	
	protected void doRentas(HttpServletRequest request, HttpServletResponse response, 
		String unidad, String recursivo, String empresa) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        response.setHeader("Expires", "0");
        response.setHeader("Pragma", "no-cache");
        Template template = getTemplate("Gestion/Reportes/Rentas.htm");
        SimpleHash modelRoot = new SimpleHash ();
        Validar valida = new Validar();        
        Connection conecction = connMgr.getConnection("portal");
        Consulta cons = new Consulta(conecction);
        Consulta cons2 = new Consulta(conecction);
        String nomUnidad = ExtrasOrganica.UnidadDesc(unidad,conecction).toUpperCase();
        String query = "select unid_id,unid_desc from eje_ges_unidades where unid_id ";
        if(recursivo.equals("S")) {  
        	query+="in (";
        	List lista2 = new ArrayList(); lista2 = ExtrasOrganica.NodosHijosRecursivos(unidad,empresa,conecction,lista2); lista2.add(unidad); 
       	   	for(int i=0;i<lista2.size();i++) {  
       	   		query+= "'" + lista2.get(i).toString() + "'";
       	   		if(i<lista2.size()-1) {
       	   			query+=",";
       	   		}
       	   	}
       	   	query+=")";
        }
        else {  
        	query+= "= '" + unidad + "'"; 
        }
        query+= " order by unid_desc";
        cons.exec(query);
        SimpleList UnidadList = new SimpleList();
    	SimpleHash UnidadIter;
    	UnidadIter = new SimpleHash();
    	int thaberesarea=0,tpersonas=0,tcf=0,tci=0;
        for(;cons.next();UnidadList.add(UnidadIter)) {
        	UnidadIter = new SimpleHash();
        	UnidadIter.put("uid",valida.validarDato(cons.getString("unid_id")) );
        	UnidadIter.put("udesc",valida.validarDato(cons.getString("unid_desc").toUpperCase()) );
        	SimpleList PersonaList = new SimpleList();
        	SimpleHash PersonaIter;
        	PersonaIter = new SimpleHash();
        	String query1= "select t.rut,t.digito_ver,t.nombre,t.cargo,c.descrip,t.wp_cod_empresa as ce,e.descrip as de,t.wp_cod_planta as cp,s.descripcion as dp,t.wp_cod_sucursal as cs,su.descripcion as ds,Convert(datetime, t.fecha_ingreso, 13) as fi,Convert(datetime, t.fec_ter_cont, 13) as ft,lc.tot_haberes from ((((eje_ges_trabajador t inner join eje_ges_cargos c on t.cargo=c.cargo and t.wp_cod_empresa=c.empresa) inner join eje_ges_empresa e on t.wp_cod_empresa=e.empresa) inner join eje_ges_sociedad s on t.wp_cod_empresa=s.wp_cod_empresa and t.wp_cod_planta=s.codigo) inner join eje_ges_sucursal su on t.wp_cod_empresa=su.wp_cod_empresa and t.wp_cod_planta=su.wp_cod_planta and t.wp_cod_sucursal=su.codigo) inner join eje_ges_certif_histo_liquidacion_cabecera lc on t.rut=lc.rut where t.unidad='" + valida.validarDato(cons.getString("unid_id")) + "' and lc.periodo = (select max(peri_id) from eje_ges_periodo) order by t.nombre";
            cons2.exec(query1);
            int i=0,thaberes=0,cfijo=0,cindefinido=0;
        	for(;cons2.next();PersonaList.add(PersonaIter)) {
        		PersonaIter = new SimpleHash();
        		PersonaIter.put("rut",valida.validarDato(cons2.getString("rut")) );
        		PersonaIter.put("digito",valida.validarDato(cons2.getString("digito_ver").toUpperCase()) );
        		PersonaIter.put("nombre",valida.validarDato(cons2.getString("nombre").toUpperCase()) );
        		PersonaIter.put("ccargo",valida.validarDato(cons2.getString("cargo")) );
        		PersonaIter.put("dcargo",valida.validarDato(cons2.getString("descrip").toUpperCase()) );
        		PersonaIter.put("cempresa",valida.validarDato(cons2.getString("ce")) );
        		PersonaIter.put("dempresa",valida.validarDato(cons2.getString("de").toUpperCase()) );
        		PersonaIter.put("cplanta",valida.validarDato(cons2.getString("cp")) );
        		PersonaIter.put("dplanta",valida.validarDato(cons2.getString("dp").toUpperCase()) );
        		PersonaIter.put("csucursal",valida.validarDato(cons2.getString("cs")) );
        		PersonaIter.put("dsucursal",valida.validarDato(cons2.getString("ds").toUpperCase()) );
        		PersonaIter.put("fecing",valida.validarFecha(cons2.getString("fi")) );
        		PersonaIter.put("fecter",valida.validarFecha(cons2.getString("ft"),"INDEFINIDO") );
        		PersonaIter.put("haberes",valida.validarDato(FormatearNumeros.numero( Integer.parseInt(cons2.getString("tot_haberes")) )));
        		i++;
        		thaberes+=Integer.parseInt(cons2.getString("tot_haberes"));
        		if(valida.validarFecha(cons2.getString("ft"),"INDEFINIDO").equals("INDEFINIDO")) {
        			cindefinido++;
        		}
        		else {
        			cfijo++;
        		}
        	}
        	if(i!=0) {  
        		UnidadIter.put("lpersona",PersonaList);
        		UnidadIter.put("tpersonasU",String.valueOf(i));
        		UnidadIter.put("tpersonasCF",String.valueOf(cfijo));
        		UnidadIter.put("tpersonasCI",String.valueOf(cindefinido));
        	}
        	else {
        		UnidadIter.put("mensaje","0");
        	}
        	UnidadIter.put("thaberes",valida.validarDato(FormatearNumeros.numero(thaberes)));
        	thaberesarea+=thaberes;
        	tpersonas+=i;
        	tcf+=cfijo;
        	tci+=cindefinido;
        }
        modelRoot.put("lunidades",UnidadList);
        modelRoot.put("thaberesarea",valida.validarDato(FormatearNumeros.numero(thaberesarea)));
        modelRoot.put("tpersonasA",String.valueOf(tpersonas));
        modelRoot.put("tpersonasCFA",String.valueOf(tcf));
        modelRoot.put("tpersonasCIA",String.valueOf(tci));
        if(recursivo.equals("S")) {
        	nomUnidad = "AREA " + nomUnidad; 
        }
        else {
        	nomUnidad = "UNIDAD " + nomUnidad;
        }
        modelRoot.put("nomUnidad",nomUnidad);
    	try {
			template.process(modelRoot,out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	out.flush();
    	out.close();

	}

	protected void doCambioRentas(HttpServletRequest request, HttpServletResponse response, 
		String unidad, String recursivo, String empresa) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        //response.setContentType("application/vnd.ms-excel");
        response.setHeader("Expires", "0");
        response.setHeader("Pragma", "no-cache");
        Template template = getTemplate("Gestion/Reportes/CambioRentas.htm");
        SimpleHash modelRoot = new SimpleHash ();
        Validar valida = new Validar();        
        Connection conecction = connMgr.getConnection("portal");
        Consulta cons = new Consulta(conecction);
        Consulta cons2 = new Consulta(conecction);
        String nomUnidad = ExtrasOrganica.UnidadDesc(unidad,conecction).toUpperCase();
        String query = "select unid_id,unid_desc from eje_ges_unidades where unid_id ";
        if(recursivo.equals("S")) {  
        	query+="in (";
        	List lista2 = new ArrayList(); lista2 = ExtrasOrganica.NodosHijosRecursivos(unidad,empresa,conecction,lista2); lista2.add(unidad); 
       	   	for(int i=0;i<lista2.size();i++) {  
       	   		query+= "'" + lista2.get(i).toString() + "'";
       	   		if(i<lista2.size()-1) {
       	   			query+=",";
       	   		}
       	   	}
       	   	query+=")";
        }
        else {  
        	query+= "= '" + unidad + "'"; 
        }
        query+= " order by unid_desc";
        cons.exec(query);
        SimpleList UnidadList = new SimpleList();
    	SimpleHash UnidadIter;
    	UnidadIter = new SimpleHash();
    	int thaberesarea=0,thaberesarea2=0,tpersonas=0,tcf=0,tci=0;
        for(;cons.next();UnidadList.add(UnidadIter)) {
        	UnidadIter = new SimpleHash();
        	UnidadIter.put("uid",valida.validarDato(cons.getString("unid_id")) );
        	UnidadIter.put("udesc",valida.validarDato(cons.getString("unid_desc").toUpperCase()) );
        	SimpleList PersonaList = new SimpleList();
        	SimpleHash PersonaIter;
        	PersonaIter = new SimpleHash();
        	String query1= "select distinct t.rut,t.digito_ver,t.nombre,t.cargo,c.descrip,t.wp_cod_empresa as ce,e.descrip as de,t.wp_cod_planta as cp,s.descripcion as dp,t.wp_cod_sucursal as cs,su.descripcion as ds,Convert(datetime, t.fecha_ingreso, 13) as fi,Convert(datetime, t.fec_ter_cont, 13) as ft,lc.tot_haberes,tot_haberes2 = (select distinct tot_haberes from eje_ges_certif_histo_liquidacion_cabecera where rut= t.rut and periodo =200708) from ((((eje_ges_trabajador t inner join eje_ges_cargos c on t.cargo=c.cargo and t.wp_cod_empresa=c.empresa) inner join eje_ges_empresa e on t.wp_cod_empresa=e.empresa) inner join eje_ges_sociedad s on t.wp_cod_empresa=s.wp_cod_empresa and t.wp_cod_planta=s.codigo) inner join eje_ges_sucursal su on t.wp_cod_empresa=su.wp_cod_empresa and t.wp_cod_planta=su.wp_cod_planta and t.wp_cod_sucursal=su.codigo) inner join eje_ges_certif_histo_liquidacion_cabecera lc on t.rut=lc.rut where t.unidad='" + valida.validarDato(cons.getString("unid_id")) + "' and lc.periodo = 200709 order by t.nombre";
            cons2.exec(query1);
            int i=0,thaberes=0,thaberes2=0,cfijo=0,cindefinido=0;
        	for(;cons2.next();PersonaList.add(PersonaIter)) {
        		PersonaIter = new SimpleHash();
        		PersonaIter.put("rut",valida.validarDato(cons2.getString("rut")) );
        		PersonaIter.put("digito",valida.validarDato(cons2.getString("digito_ver").toUpperCase()) );
        		PersonaIter.put("nombre",valida.validarDato(cons2.getString("nombre").toUpperCase()) );
        		PersonaIter.put("ccargo",valida.validarDato(cons2.getString("cargo")) );
        		PersonaIter.put("dcargo",valida.validarDato(cons2.getString("descrip").toUpperCase()) );
        		PersonaIter.put("cempresa",valida.validarDato(cons2.getString("ce")) );
        		PersonaIter.put("dempresa",valida.validarDato(cons2.getString("de").toUpperCase()) );
        		PersonaIter.put("cplanta",valida.validarDato(cons2.getString("cp")) );
        		PersonaIter.put("dplanta",valida.validarDato(cons2.getString("dp").toUpperCase()) );
        		PersonaIter.put("csucursal",valida.validarDato(cons2.getString("cs")) );
        		PersonaIter.put("dsucursal",valida.validarDato(cons2.getString("ds").toUpperCase()) );
        		PersonaIter.put("fecing",valida.validarFecha(cons2.getString("fi")) );
        		PersonaIter.put("fecter",valida.validarFecha(cons2.getString("ft"),"INDEFINIDO") );
        		PersonaIter.put("haberes",valida.validarDato(FormatearNumeros.numero( Integer.parseInt(cons2.getString("tot_haberes")) )));
        		PersonaIter.put("haberes2",valida.validarDato(FormatearNumeros.numero( Integer.parseInt(cons2.getString("tot_haberes2")) )));
        		float valor = 100 - ( ( Float.parseFloat(cons2.getString("tot_haberes2")) * 100) / Float.parseFloat(cons2.getString("tot_haberes")));
        		NumberFormat nf = NumberFormat.getInstance();
        		nf.setMaximumFractionDigits(3);
        		PersonaIter.put("varhab",nf.format(valor));
        		PersonaIter.put("difhab",valida.validarDato(FormatearNumeros.numero( Integer.parseInt(cons2.getString("tot_haberes")) - Integer.parseInt(cons2.getString("tot_haberes2")) )));
        		i++;
        		thaberes+=Integer.parseInt(cons2.getString("tot_haberes"));
        		thaberes2+=Integer.parseInt(cons2.getString("tot_haberes2"));
        		if(valida.validarFecha(cons2.getString("ft"),"INDEFINIDO").equals("INDEFINIDO")) {
        			cindefinido++;
        		}
        		else {
        			cfijo++;
        		}
        	}
        	if(i!=0) {  
        		UnidadIter.put("lpersona",PersonaList);
        		UnidadIter.put("tpersonasU",String.valueOf(i));
        		UnidadIter.put("tpersonasCF",String.valueOf(cfijo));
        		UnidadIter.put("tpersonasCI",String.valueOf(cindefinido));
        	}
        	else {
        		UnidadIter.put("mensaje","0");
        	}
        	UnidadIter.put("thaberes",valida.validarDato(FormatearNumeros.numero(thaberes)));
        	UnidadIter.put("thaberes2",valida.validarDato(FormatearNumeros.numero(thaberes2)));
        	thaberesarea+=thaberes;
        	thaberesarea2+=thaberes2;
        	tpersonas+=i;
        	tcf+=cfijo;
        	tci+=cindefinido;
        }
        modelRoot.put("lunidades",UnidadList);
        modelRoot.put("thaberesarea",valida.validarDato(FormatearNumeros.numero(thaberesarea)));
        modelRoot.put("thaberesarea2",valida.validarDato(FormatearNumeros.numero(thaberesarea2)));
        modelRoot.put("tpersonasA",String.valueOf(tpersonas));
        modelRoot.put("tpersonasCFA",String.valueOf(tcf));
        modelRoot.put("tpersonasCIA",String.valueOf(tci));
        if(recursivo.equals("S")) {
        	nomUnidad = "AREA " + nomUnidad; 
        }
        else {
        	nomUnidad = "UNIDAD " + nomUnidad;
        }
        modelRoot.put("nomUnidad",nomUnidad);
    	try {
			template.process(modelRoot,out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	out.flush();
    	out.close();

	}
	
	protected void doCambioPersonal(HttpServletRequest request, HttpServletResponse response, 
		String unidad, String recursivo, String empresa) throws ServletException, IOException {
	}

	protected void doVinPer(HttpServletRequest request, HttpServletResponse response, 
		String unidad, String recursivo, String empresa) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        response.setHeader("Expires", "0");
        response.setHeader("Pragma", "no-cache");
        Template template = getTemplate("Gestion/Reportes/DesVin.htm");
        SimpleHash modelRoot = new SimpleHash ();
        Validar valida = new Validar();        
        Connection conecction = connMgr.getConnection("portal");
        Consulta cons = new Consulta(conecction);
        Consulta cons2 = new Consulta(conecction);
        Consulta cons3 = new Consulta(conecction);
        String nomUnidad = ExtrasOrganica.UnidadDesc(unidad,conecction).toUpperCase();
        String query = "select top 1 peri_ano,peri_mes from eje_ges_periodo order by peri_id desc";
        cons3.exec(query); cons3.next();
        String mes = cons3.getString("peri_mes");
        String ano =cons3.getString("peri_ano");
        query = "select unid_id,unid_desc from eje_ges_unidades where unid_id ";
        if(recursivo.equals("S")) {  
        	query+="in (";
        	List lista2 = new ArrayList(); lista2 = ExtrasOrganica.NodosHijosRecursivos(unidad,empresa,conecction,lista2); lista2.add(unidad); 
       	   	for(int i=0;i<lista2.size();i++) {  
       	   		query+= "'" + lista2.get(i).toString() + "'";
       	   		if(i<lista2.size()-1) {
       	   			query+=",";
       	   		}
       	   	}
       	   	query+=")";
        }
        else {  
        	query+= "= '" + unidad + "'"; 
        }
        query+= " order by unid_desc";
        cons.exec(query);
        SimpleList UnidadList = new SimpleList();
    	SimpleHash UnidadIter;
    	UnidadIter = new SimpleHash();
    	int tpersonas=0,tcf=0,tci=0;
        for(;cons.next();UnidadList.add(UnidadIter)) {
        	UnidadIter = new SimpleHash();
        	UnidadIter.put("uid",valida.validarDato(cons.getString("unid_id")) );
        	UnidadIter.put("udesc",valida.validarDato(cons.getString("unid_desc").toUpperCase()) );
        	SimpleList PersonaList = new SimpleList();
        	SimpleHash PersonaIter;
        	PersonaIter = new SimpleHash();
        	String query1= "select distinct t.rut,t.digito_ver,t.nombre,t.cargo,c.descrip,t.wp_cod_empresa,e.descrip as empresa,t.wp_cod_planta,s.descripcion as planta,t.wp_cod_sucursal,su.descripcion as sucursal,Convert(datetime, t.fecha_ingreso, 13) as fi,Convert(datetime, t.fec_ter_cont, 13) as ft from (((eje_ges_trabajador t inner join eje_ges_cargos c on t.cargo=c.cargo and t.wp_cod_empresa=c.empresa) inner join eje_ges_empresa e on t.wp_cod_empresa=e.empresa) inner join eje_ges_sociedad s on t.wp_cod_empresa=s.wp_cod_empresa and t.wp_cod_planta=s.codigo) inner join eje_ges_sucursal su on t.wp_cod_empresa=su.wp_cod_empresa and t.wp_cod_planta=su.wp_cod_planta and t.wp_cod_sucursal=su.codigo where t.unidad='" + valida.validarDato(cons.getString("unid_id")) + "' and datepart(month,t.fecha_ingreso)=" + mes + " and datepart(year,t.fecha_ingreso)=" + ano;
            cons2.exec(query1);
            int i=0,cfijo=0,cindefinido=0;
        	for(;cons2.next();PersonaList.add(PersonaIter)) {
        		PersonaIter = new SimpleHash();
        		PersonaIter.put("rut",valida.validarDato(cons2.getString("rut")) );
        		PersonaIter.put("digito",valida.validarDato(cons2.getString("digito_ver").toUpperCase()) );
        		PersonaIter.put("nombre",valida.validarDato(cons2.getString("nombre").toUpperCase()) );
        		PersonaIter.put("ccargo",valida.validarDato(cons2.getString("cargo")) );
        		PersonaIter.put("dcargo",valida.validarDato(cons2.getString("descrip").toUpperCase()) );
        		PersonaIter.put("cempresa",valida.validarDato(cons2.getString("wp_cod_empresa")) );
        		PersonaIter.put("dempresa",valida.validarDato(cons2.getString("empresa").toUpperCase()) );
        		PersonaIter.put("cplanta",valida.validarDato(cons2.getString("wp_cod_planta")) );
        		PersonaIter.put("dplanta",valida.validarDato(cons2.getString("planta").toUpperCase()) );
        		PersonaIter.put("csucursal",valida.validarDato(cons2.getString("wp_cod_sucursal")) );
        		PersonaIter.put("dsucursal",valida.validarDato(cons2.getString("sucursal").toUpperCase()) );
        		PersonaIter.put("fecing",valida.validarFecha(cons2.getString("fi")) );
        		PersonaIter.put("fecter",valida.validarFecha(cons2.getString("ft"),"INDEFINIDO") );
        		i++;
        		if(valida.validarFecha(cons2.getString("ft"),"INDEFINIDO").equals("INDEFINIDO")) {
        			cindefinido++;
        		}
        		else {
        			cfijo++;
        		}
        	}
        	if(i!=0) {  
        		UnidadIter.put("lpersona",PersonaList);
        		UnidadIter.put("tpersonasU",String.valueOf(i));
        		UnidadIter.put("tpersonasCF",String.valueOf(cfijo));
        		UnidadIter.put("tpersonasCI",String.valueOf(cindefinido));
        	}
        	else {
        		UnidadIter.put("mensaje","0");
        	}
        	tpersonas+=i;
        	tcf+=cfijo;
        	tci+=cindefinido;
        }
        modelRoot.put("lunidades",UnidadList);
        modelRoot.put("tpersonasA",String.valueOf(tpersonas));
        modelRoot.put("tpersonasCFA",String.valueOf(tcf));
        modelRoot.put("tpersonasCIA",String.valueOf(tci));
        if(recursivo.equals("S")) {
        	nomUnidad = "AREA " + nomUnidad; 
        }
        else {
        	nomUnidad = "UNIDAD " + nomUnidad;
        }
        modelRoot.put("nomUnidad",nomUnidad);
    	try {
			template.process(modelRoot,out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	out.flush();
    	out.close();
	}

	protected void doDesPer(HttpServletRequest request, HttpServletResponse response, 
		String unidad, String recursivo, String empresa) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        response.setHeader("Expires", "0");
        response.setHeader("Pragma", "no-cache");
        Template template = getTemplate("Gestion/Reportes/DesVin2.htm");
        SimpleHash modelRoot = new SimpleHash ();
        Validar valida = new Validar();        
        Connection conecction = connMgr.getConnection("portal");
        Consulta cons = new Consulta(conecction);
        Consulta cons2 = new Consulta(conecction);
        Consulta cons3 = new Consulta(conecction);
        String nomUnidad = ExtrasOrganica.UnidadDesc(unidad,conecction).toUpperCase();
        String query = "select top 1 peri_ano,peri_mes from eje_ges_periodo order by peri_id desc";
        cons3.exec(query); cons3.next();
        String mes = cons3.getString("peri_mes");
        String ano =cons3.getString("peri_ano");
        query = "select unid_id,unid_desc from eje_ges_unidades where unid_id ";
        if(recursivo.equals("S")) {  
        	query+="in (";
        	List lista2 = new ArrayList(); lista2 = ExtrasOrganica.NodosHijosRecursivos(unidad,empresa,conecction,lista2); lista2.add(unidad); 
       	   	for(int i=0;i<lista2.size();i++) {  
       	   		query+= "'" + lista2.get(i).toString() + "'";
       	   		if(i<lista2.size()-1) {
       	   			query+=",";
       	   		}
       	   	}
       	   	query+=")";
        }
        else {  
        	query+= "= '" + unidad + "'"; 
        }
        query+= " order by unid_desc";
        cons.exec(query);
        SimpleList UnidadList = new SimpleList();
    	SimpleHash UnidadIter;
    	UnidadIter = new SimpleHash();
    	int tpersonas=0,tcf=0,tci=0;
        for(;cons.next();UnidadList.add(UnidadIter)) {
        	UnidadIter = new SimpleHash();
        	UnidadIter.put("uid",valida.validarDato(cons.getString("unid_id")) );
        	UnidadIter.put("udesc",valida.validarDato(cons.getString("unid_desc").toUpperCase()) );
        	SimpleList PersonaList = new SimpleList();
        	SimpleHash PersonaIter;
        	PersonaIter = new SimpleHash();
        	String query1= "select  distinct t.rut,t.digito_ver,t.nombre,t.cargo,c.descrip,t.wp_cod_empresa,e.descrip as empresa,t.wp_cod_planta,s.descripcion as planta,t.wp_cod_sucursal,su.descripcion as sucursal,Convert(datetime, t.fecha_ingreso, 13) as fi,Convert(datetime, t.fec_ter_cont, 13) as ft from (((eje_ges_trabajador_desvinculado t inner join eje_ges_trabajador_historia th on t.rut=th.rut inner join eje_ges_cargos c on t.cargo=c.cargo and t.wp_cod_empresa=c.empresa) inner join eje_ges_empresa e on t.wp_cod_empresa=e.empresa) inner join eje_ges_sociedad s on t.wp_cod_empresa=s.wp_cod_empresa and t.wp_cod_planta=s.codigo) inner join eje_ges_sucursal su on t.wp_cod_empresa=su.wp_cod_empresa and t.wp_cod_planta=su.wp_cod_planta and t.wp_cod_sucursal=su.codigo where th.unidad='" + valida.validarDato(cons.getString("unid_id")) + "' order by t.nombre";
            cons2.exec(query1);
            int i=0,cfijo=0,cindefinido=0;
        	for(;cons2.next();PersonaList.add(PersonaIter)) {
        		PersonaIter = new SimpleHash();
        		PersonaIter.put("rut",valida.validarDato(cons2.getString("rut")) );
        		PersonaIter.put("digito",valida.validarDato(cons2.getString("digito_ver").toUpperCase()) );
        		PersonaIter.put("nombre",valida.validarDato(cons2.getString("nombre").toUpperCase()) );
        		PersonaIter.put("ccargo",valida.validarDato(cons2.getString("cargo")) );
        		PersonaIter.put("dcargo",valida.validarDato(cons2.getString("descrip").toUpperCase()) );
        		PersonaIter.put("cempresa",valida.validarDato(cons2.getString("wp_cod_empresa")) );
        		PersonaIter.put("dempresa",valida.validarDato(cons2.getString("empresa").toUpperCase()) );
        		PersonaIter.put("cplanta",valida.validarDato(cons2.getString("wp_cod_planta")) );
        		PersonaIter.put("dplanta",valida.validarDato(cons2.getString("planta").toUpperCase()) );
        		PersonaIter.put("csucursal",valida.validarDato(cons2.getString("wp_cod_sucursal")) );
        		PersonaIter.put("dsucursal",valida.validarDato(cons2.getString("sucursal").toUpperCase()) );
        		PersonaIter.put("fecing",valida.validarFecha(cons2.getString("fi")) );
        		PersonaIter.put("fecter",valida.validarFecha(cons2.getString("ft"),"INDEFINIDO") );
        		i++;
        		if(valida.validarFecha(cons2.getString("ft"),"INDEFINIDO").equals("INDEFINIDO")) {
        			cindefinido++;
        		}
        		else {
        			cfijo++;
        		}
        	}
        	if(i!=0) {  
        		UnidadIter.put("lpersona",PersonaList);
        		UnidadIter.put("tpersonasU",String.valueOf(i));
        		UnidadIter.put("tpersonasCF",String.valueOf(cfijo));
        		UnidadIter.put("tpersonasCI",String.valueOf(cindefinido));
        	}
        	else {
        		UnidadIter.put("mensaje","0");
        	}
        	tpersonas+=i;
        	tcf+=cfijo;
        	tci+=cindefinido;
        }
        modelRoot.put("lunidades",UnidadList);
        modelRoot.put("tpersonasA",String.valueOf(tpersonas));
        modelRoot.put("tpersonasCFA",String.valueOf(tcf));
        modelRoot.put("tpersonasCIA",String.valueOf(tci));
        if(recursivo.equals("S")) {
        	nomUnidad = "AREA " + nomUnidad; 
        }
        else {
        	nomUnidad = "UNIDAD " + nomUnidad;
        }
        modelRoot.put("nomUnidad",nomUnidad);
    	try {
			template.process(modelRoot,out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	out.flush();
    	out.close();
	}

	protected void doDifHabDescto(HttpServletRequest request, HttpServletResponse response, String unidad, String recursivo, String empresa) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String excel = request.getParameter("excel")==null?"0":request.getParameter("excel");
        if(excel.equals("1")) {
        	response.setContentType("application/vnd.ms-excel");
        }
        else {
        	response.setContentType("text/html");
        }
        response.setHeader("Expires", "0");
        response.setHeader("Pragma", "no-cache");
        Template template = getTemplate("Gestion/Reportes/DotacionDHD.htm");
        SimpleHash modelRoot = new SimpleHash ();
        Validar valida = new Validar();        
        Connection conecction = connMgr.getConnection("portal");
        Consulta cons = new Consulta(conecction);
        Consulta cons2 = new Consulta(conecction);
        String nomUnidad = ExtrasOrganica.UnidadDesc(unidad,conecction).toUpperCase();
        String query = "select unid_id,unid_desc from eje_ges_unidades where unid_id ";
        if(recursivo.equals("S")) {  
        	query+="in (";
        	List lista2 = new ArrayList(); lista2 = ExtrasOrganica.NodosHijosRecursivos(unidad,empresa,conecction,lista2); lista2.add(unidad); 
       	   	for(int i=0;i<lista2.size();i++) {  
       	   		query+= "'" + lista2.get(i).toString() + "'";
       	   		if(i<lista2.size()-1) {
       	   			query+=",";
       	   		}
       	   	}
       	   	query+=")";
        }
        else {  
        	query+= "= '" + unidad + "'"; 
        }
        query+= " order by unid_desc";
        cons.exec(query);
        SimpleList UnidadList = new SimpleList();
    	SimpleHash UnidadIter;
    	UnidadIter = new SimpleHash();
    	int tpersonas=0;
        for(;cons.next();UnidadList.add(UnidadIter)) {
        	UnidadIter = new SimpleHash();
        	UnidadIter.put("uid",valida.validarDato(cons.getString("unid_id")) );
        	UnidadIter.put("udesc",valida.validarDato(cons.getString("unid_desc").toUpperCase()) );
        	SimpleList PersonaList = new SimpleList();
        	SimpleHash PersonaIter;
        	PersonaIter = new SimpleHash();
        	String query1= "select t.rut,t.digito_ver,t.nombre from eje_ges_trabajador t where t.unidad='" + valida.validarDato(cons.getString("unid_id")) + "' order by t.nombre";
            cons2.exec(query1);
            int i=0;
        	for(;cons2.next();PersonaList.add(PersonaIter)) {
        		PersonaIter = new SimpleHash();
        		PersonaIter.put("rut",valida.validarDato(cons2.getString("rut")) );
        		PersonaIter.put("digito",valida.validarDato(cons2.getString("digito_ver").toUpperCase()) );
        		PersonaIter.put("nombre",valida.validarDato(cons2.getString("nombre").toUpperCase()) );
        		Consulta cons3 = new Consulta(conecction);
        		String hf="0",hv="0";
        		String query2= "select haber_variable,isnull(sum(val_haber),0) total from eje_ges_certif_histo_liquidacion_detalle " +
        		               "where periodo=(select max(peri_id) from eje_ges_periodo) and rut=" + cons2.getString("rut") + " and " +
        		               "id_tp='H' and wp_indic_papeleta='S' group by haber_variable";
                cons3.exec(query2);
                for(;cons3.next();) {
                	if(cons3.getString("haber_variable").equals("N")) {
                		hf=cons3.getString("total");
                	}
                	else {
                		hv=cons3.getString("total");
                	}
                }
                int th = Integer.parseInt(hf) + Integer.parseInt(hv);
                PersonaIter.put("hf",hf );
        		PersonaIter.put("hv",hv );
        		PersonaIter.put("th",String.valueOf(th) );
        		i++;
        	}
        	if(i!=0) {  
        		UnidadIter.put("lpersona",PersonaList);
        		UnidadIter.put("tpersonasU",String.valueOf(i));
        	}
        	else {
        		UnidadIter.put("mensaje","0");
        	}
        	tpersonas+=i;
        }
        modelRoot.put("unidad",unidad);
        modelRoot.put("empresa",empresa);
        modelRoot.put("recursivo",recursivo);
        if(excel.equals("1")) {
        	modelRoot.put("xls","1");
        }
        modelRoot.put("lunidades",UnidadList);
        modelRoot.put("tpersonasA",String.valueOf(tpersonas));
        if(recursivo.equals("S")) {
        	nomUnidad = "AREA " + nomUnidad; 
        }
        else {
        	nomUnidad = "UNIDAD " + nomUnidad;
        }
        modelRoot.put("nomUnidad",nomUnidad);
    	try {
			template.process(modelRoot,out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	out.flush();
    	out.close();
	}
	
	protected void doHorasExtras(HttpServletRequest request, HttpServletResponse response, 
		String unidad, String recursivo, String empresa) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String excel = request.getParameter("excel")==null?"0":request.getParameter("excel");
        if(excel.equals("1")) {
        	response.setContentType("application/vnd.ms-excel");
        }
        else {
        	response.setContentType("text/html");
        }
        response.setHeader("Expires", "0");
        response.setHeader("Pragma", "no-cache");
        Template template = getTemplate("Gestion/Reportes/HorasExtras.htm");
        SimpleHash modelRoot = new SimpleHash ();
        Validar valida = new Validar();
        DecimalFormat dec = new DecimalFormat("###.#");
        Connection conecction = connMgr.getConnection("portal");
        Consulta cons = new Consulta(conecction), cons2 = new Consulta(conecction), cons3 = new Consulta(conecction);
        String nomUnidad = ExtrasOrganica.UnidadDesc(unidad,conecction).toUpperCase();
        String query = "select top 1 peri_ano,peri_mes,peri_id from eje_ges_periodo order by peri_id desc";
        cons3.exec(query); cons3.next();
        String mes = cons3.getString("peri_mes"), ano = cons3.getString("peri_ano"), periodo = cons3.getString("peri_id");
        modelRoot.put("mes",mes);
        modelRoot.put("anho",ano);

        cons = Unidades(empresa,unidad,recursivo,conecction);
        
        SimpleList UnidadList = new SimpleList();
    	SimpleHash UnidadIter;
    	UnidadIter = new SimpleHash();
    	float he50total=0,he100total=0,he150total=0,he200total=0,hetotal=0;
    	int nrotrab_total=0,sdobase_total=0,mnttotal_total=0;
        for(;cons.next();UnidadList.add(UnidadIter)) {
        	UnidadIter = new SimpleHash();
        	UnidadIter.put("uid",valida.validarDato(cons.getString("unid_id")) );
        	UnidadIter.put("udesc",valida.validarDato(cons.getString("unid_desc").toUpperCase()) );
        	String query1= "SELECT glosa_haber,convert(decimal(10,1),sum(tip_unidad)) as hrs_extras " +
        	               "FROM eje_ges_certif_histo_liquidacion_detalle WHERE periodo = " + periodo + 
        	               " AND unidad='HORA' and rut in (select rut from eje_ges_trabajador " +
        	               "where unidad=('" + valida.validarDato(cons.getString("unid_id")) + "')) " +
        	               "GROUP BY glosa_haber";
            cons2.exec(query1);
            String HE50="0",HE100="0",HE150="0",HE200="0";
        	for(;cons2.next();) {
        		if(cons2.getString("glosa_haber").equals("Horas Extras 50%")){
        			HE50=cons2.getString("hrs_extras");
        		}
        		else if(cons2.getString("glosa_haber").equals("Horas Extras 100%")){
        			HE100=cons2.getString("hrs_extras");
        		}
        		else if(cons2.getString("glosa_haber").equals("Horas Extras 150%")){
        			HE150=cons2.getString("hrs_extras");
        		}
        		else if(cons2.getString("glosa_haber").equals("Horas Extras 120%")){
        			HE200=cons2.getString("hrs_extras");	
        		}
        	}
        	float uhetotal = Float.parseFloat(HE50)+Float.parseFloat(HE100)+Float.parseFloat(HE150)+Float.parseFloat(HE200);
        	UnidadIter.put("uhe_50",HE50 );
        	UnidadIter.put("uhe_100",HE100 );
        	UnidadIter.put("uhe_150",HE150 );
        	UnidadIter.put("uhe_200",HE200 );
        	UnidadIter.put("uhe_total",String.valueOf(uhetotal) );
        	he50total+= Float.parseFloat(HE50);
        	he100total+= Float.parseFloat(HE100);
        	he150total+= Float.parseFloat(HE150);
        	he200total+= Float.parseFloat(HE200);
        	hetotal = he50total+he100total+he150total+he200total;
        	String query2= "select count(*) as nrotrab from eje_ges_trabajador where unidad='" + 
        	               valida.validarDato(cons.getString("unid_id")) + "'";
        	cons3.exec(query2);cons3.next();
        	int unrotrab_total = Integer.parseInt(cons3.getString("nrotrab"));
        	UnidadIter.put("nrotrab_total",String.valueOf(unrotrab_total) );
        	nrotrab_total+= unrotrab_total;
        	query2= "select isnull(sum(val_haber),0) sdobase from eje_ges_certif_histo_liquidacion_detalle " +
        	        "where periodo=" + periodo + " and rut in (select rut from eje_ges_trabajador " +
        	        "where unidad='" + valida.validarDato(cons.getString("unid_id")) + "') and " +
        	        "glosa_haber='Sueldo Base'";
        	cons3.exec(query2);
        	if(cons3.next()) {
        		UnidadIter.put("sdo_base",Tools.setFormatNumber(cons3.getString("sdobase")) );	
        		sdobase_total+= Integer.parseInt(cons3.getString("sdobase"));
        	}
        	else {
        		UnidadIter.put("sdo_base","0" );
        	}
        	query2= "select isnull(sum(val_haber),0) mnttotal from eje_ges_certif_histo_liquidacion_detalle " +
        	        "where periodo=" + periodo + " and rut in (select rut from eje_ges_trabajador " +
        	        "where unidad='" + valida.validarDato(cons.getString("unid_id")) + "') and " +
        	        "unidad='HORA'";
	        cons3.exec(query2);
	        if(cons3.next()) {
	        	UnidadIter.put("umnt_total",Tools.setFormatNumber(cons3.getString("mnttotal")) );	
	        	mnttotal_total+= Integer.parseInt(cons3.getString("mnttotal"));
	        }
	        else {
	        	UnidadIter.put("sdo_base","0" );
	        }
        	float upromhe = unrotrab_total == 0 ? 0 : uhetotal/unrotrab_total;
        	UnidadIter.put("upromhe_total",String.valueOf(dec.format(upromhe)) );
        }
        modelRoot.put("lunidades",UnidadList);
        modelRoot.put("he50total",String.valueOf(he50total));
        modelRoot.put("he100total",String.valueOf(he100total));
        modelRoot.put("he150total",String.valueOf(he150total));
        modelRoot.put("he200total",String.valueOf(he200total));
        modelRoot.put("hetotal",String.valueOf(hetotal));
        modelRoot.put("nrotrab_total",String.valueOf(nrotrab_total));
        modelRoot.put("sdobase_total",Tools.setFormatNumber(String.valueOf(sdobase_total)));
        modelRoot.put("mnttotal_total",Tools.setFormatNumber(String.valueOf(mnttotal_total)));
        float promhe_total = nrotrab_total == 0 ? 0 : hetotal/nrotrab_total;
    	modelRoot.put("promhe_total",String.valueOf(dec.format(promhe_total)));
    	modelRoot.put("unidad",unidad);
        modelRoot.put("empresa",empresa);
        modelRoot.put("recursivo",recursivo);
        if(excel.equals("1")) {
        	modelRoot.put("xls","1");
        }
        if(recursivo.equals("S")) {
        	nomUnidad = "AREA " + nomUnidad; 
        }
        else {
        	nomUnidad = "UNIDAD " + nomUnidad;
        }
        modelRoot.put("nomUnidad",nomUnidad);
    	try {
			template.process(modelRoot,out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	out.flush();
    	out.close();
	}

	protected void doLicenciasMedicas(HttpServletRequest request, HttpServletResponse response, 
		String unidad, String recursivo, String empresa) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String excel = request.getParameter("excel")==null?"0":request.getParameter("excel");
        if(excel.equals("1")) {
        	response.setContentType("application/vnd.ms-excel");
        }
        else {
        	response.setContentType("text/html");
        }
        response.setHeader("Expires", "0");
        response.setHeader("Pragma", "no-cache");
        Template template = getTemplate("Gestion/Reportes/LicenciasMedicas.htm");
        SimpleHash modelRoot = new SimpleHash ();
        Validar valida = new Validar();
        DecimalFormat dec = new DecimalFormat("###.##");
        Connection conecction = connMgr.getConnection("portal");
        Consulta cons = new Consulta(conecction), cons2 = new Consulta(conecction), cons3 = new Consulta(conecction);
        String nomUnidad = ExtrasOrganica.UnidadDesc(unidad,conecction).toUpperCase();
        String query = "select top 1 peri_ano,peri_mes,peri_id from eje_ges_periodo order by peri_id desc";
        cons3.exec(query); cons3.next();
        String mes = cons3.getString("peri_mes"), ano =cons3.getString("peri_ano");
        String periodo =cons3.getString("peri_id");
        modelRoot.put("mes",mes);
        modelRoot.put("anho",ano);
        cons = Unidades(empresa,unidad,recursivo,conecction);
        SimpleList UnidadList = new SimpleList();
    	SimpleHash UnidadIter;
    	UnidadIter = new SimpleHash();
    	int nrolic_total=0,nrotrablic_total=0,nrodiaslic_total=0,nrolicAcum_total=0,nrotrablicAcum_total=0,nrodiaslicAcum_total=0;
        for(;cons.next();UnidadList.add(UnidadIter)) {
        	UnidadIter = new SimpleHash();
        	UnidadIter.put("uid",valida.validarDato(cons.getString("unid_id")) );
        	UnidadIter.put("udesc",valida.validarDato(cons.getString("unid_desc").toUpperCase()) );
        	String query1= "select count(tmp.rut) nroLic,count(distinct rut) nroTrabLic, " +
        	       "sum(cast(tmp.diasLic as int)) nroDiasLic,tmp.ultdia from (select diasLic = CASE " +
        	       "WHEN " +
        	       "cast(datepart(year,fecha_inicio) as int)*100+cast(datepart(month,fecha_inicio) as int)=" + periodo + 
        	       " THEN CASE " +
        	       "WHEN cast(datepart(year,fecha_termino) as int)*100+cast(datepart(month,fecha_termino) as int)=" + periodo + 
        	       " THEN cast(dias as varchar) ELSE " +
        	       "cast(dias-cast(datepart(day,fecha_termino) as int) as varchar) END ELSE " +
        	       "cast(datepart(day,fecha_termino) as varchar) END,rut," +
        	       "datepart(day,dateadd(day,-1,dateadd(month,1,dateadd(day,1 - datepart(day,'" + periodo + 
        	       "01'),'" + periodo + "01')))) AS ultdia from eje_ges_licencias_medicas where " +
        	       "cast(datepart(year,fecha_inicio) as int)*100+cast(datepart(month,fecha_inicio) as int)=" + periodo + 
        	       " or cast(datepart(year,fecha_termino) as int)*100+cast(datepart(month,fecha_termino) as int)=" + periodo + 
        	       ") as tmp where tmp.rut in (select rut from eje_ges_trabajador where " +
        	       "unidad='" + valida.validarDato(cons.getString("unid_id")) + "') group by tmp.ultdia";
            cons2.exec(query1);
            if(cons2.next()) {
            	UnidadIter.put("u_nrolic",cons2.getString("nroLic") );
            	UnidadIter.put("u_nrotrablic",cons2.getString("nroTrabLic") );
            	UnidadIter.put("u_nrodiaslic",cons2.getString("nroDiasLic") );
            	nrolic_total+= Integer.parseInt(cons2.getString("nroLic"));
            	nrotrablic_total+= Integer.parseInt(cons2.getString("nroTrabLic"));
            	nrodiaslic_total+= Integer.parseInt(cons2.getString("nroDiasLic"));
            	float indiclic= (Float.parseFloat(cons2.getString("nroDiasLic"))/Float.parseFloat(cons2.getString("ultdia")))*100;
            	UnidadIter.put("u_indiclic",String.valueOf(dec.format(indiclic)) );
            	String querya= "select count(rut) nroLic,count(distinct rut) nroTrabLic, " +
            	               "sum(cast(dias as int)) nroDiasLic from eje_ges_licencias_medicas " +
            	               "where (datepart(year,fecha_inicio)=" + ano + " or " +
            	               "datepart(year,fecha_termino)=" + ano + ") and rut in " +
            	               "(select rut from eje_ges_trabajador where " +
            	               "unidad='" + valida.validarDato(cons.getString("unid_id")) +"')";
            	cons3.exec(querya);
                if(cons3.next()) {
                	UnidadIter.put("u_nrolicAcum",cons3.getString("nroLic") );
                	UnidadIter.put("u_nrotrablicAcum",cons3.getString("nroTrabLic") );
                	UnidadIter.put("u_nrodiaslicAcum",cons3.getString("nroDiasLic") );
                	nrolicAcum_total+= Integer.parseInt(cons3.getString("nroLic"));
                	nrotrablicAcum_total+= Integer.parseInt(cons3.getString("nroTrabLic"));
                	nrodiaslicAcum_total+= Integer.parseInt(cons3.getString("nroDiasLic"));
                	//float indiclicAcum= (Float.parseFloat(cons3.getString("nroDiasLic"))/Float.parseFloat(cons2.getString("ultdia")))*100;
                	//UnidadIter.put("u_indiclicAcum",String.valueOf(dec.format(indiclicAcum)) );
                }
                else {
                	UnidadIter.put("u_nrolicAcum","0" );
                	UnidadIter.put("u_nrotrablicAcum","0" );
                	UnidadIter.put("u_nrodiaslicAcum","0" );
                	UnidadIter.put("u_indiclicAcum","0,00" );
                }
            }
            else {
            	UnidadIter.put("u_nrolic","0" );
            	UnidadIter.put("u_nrotrablic","0" );
            	UnidadIter.put("u_nrodiaslic","0" );
            	UnidadIter.put("u_indiclic","0,00" );	
            	UnidadIter.put("u_nrolicAcum","0" );
            	UnidadIter.put("u_nrotrablicAcum","0" );
            	UnidadIter.put("u_nrodiaslicAcum","0" );
            	UnidadIter.put("u_indiclicAcum","0,00" );	
            }
        }
        modelRoot.put("lunidades",UnidadList);
        modelRoot.put("nrolictotal",String.valueOf(nrolic_total));
        modelRoot.put("nrotrablictotal",String.valueOf(nrotrablic_total));
        modelRoot.put("nrodiaslictotal",String.valueOf(nrodiaslic_total));
        modelRoot.put("nrolicAcumtotal",String.valueOf(nrolicAcum_total));
        modelRoot.put("nrotrablicAcumtotal",String.valueOf(nrotrablicAcum_total));
        modelRoot.put("nrodiaslicAcumtotal",String.valueOf(nrodiaslicAcum_total));
        modelRoot.put("unidad",unidad);
        modelRoot.put("empresa",empresa);
        modelRoot.put("recursivo",recursivo);
        if(excel.equals("1")) {
        	modelRoot.put("xls","1");
        }
        if(recursivo.equals("S")) {
        	nomUnidad = "AREA " + nomUnidad; 
        }
        else {
        	nomUnidad = "UNIDAD " + nomUnidad;
        }
        modelRoot.put("nomUnidad",nomUnidad);
    	try {
			template.process(modelRoot,out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	out.flush();
    	out.close();
	}

	protected void doInformeTrabajadores(HttpServletRequest request, HttpServletResponse response,
			String unidad, String recursivo, String empresa) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String excel = request.getParameter("excel")==null?"0":request.getParameter("excel");
        if(excel.equals("1")) {
        	response.setContentType("application/vnd.ms-excel");
        }
        else {
        	response.setContentType("text/html");
        }
	    response.setHeader("Expires", "0");
	    response.setHeader("Pragma", "no-cache");
	    Template template = getTemplate("Gestion/Reportes/InformeTrabajadores.htm");
	    SimpleHash modelRoot = new SimpleHash ();
	    Validar valida = new Validar();
	    DecimalFormat dec = new DecimalFormat("###.##");
	    Connection conecction = connMgr.getConnection("portal");
	    Consulta cons = new Consulta(conecction), cons2 = new Consulta(conecction), cons3 = new Consulta(conecction);
        String nomUnidad = ExtrasOrganica.UnidadDesc(unidad,conecction).toUpperCase();
        String query = "select top 1 peri_ano,peri_mes,peri_id from eje_ges_periodo order by peri_id desc";
        cons3.exec(query); cons3.next();
        String mes = cons3.getString("peri_mes"), ano =cons3.getString("peri_ano"), periodo =cons3.getString("peri_id");
        modelRoot.put("mes",mes);
        modelRoot.put("anho",ano);
        cons = Unidades(empresa,unidad,recursivo,conecction);
        SimpleList UnidadList = new SimpleList();
    	SimpleHash UnidadIter;
    	UnidadIter = new SimpleHash();
    	int teA_total=0,teB_total=0,teC_total=0,teD_total=0,tgF_total=0,tgM_total=0,neA_total=0,neT_total=0,
    	    neP_total=0,ubicF_total=0,ubicB_total=0,noA_total=0,noT_total=0,noP_total=0,noJ_total=0,noE_total=0;
        for(;cons.next();UnidadList.add(UnidadIter)) {
        	UnidadIter = new SimpleHash();
        	UnidadIter.put("uid",valida.validarDato(cons.getString("unid_id")) );
        	UnidadIter.put("udesc",valida.validarDato(cons.getString("unid_desc").toUpperCase()) );
        	String query1= "select tmp.tramoEdad,count(tmp.rut) nroTrab from (select rut,tramoEdad = CASE " +
        		   "WHEN DATEDIFF(yy, eje_ges_trabajador.fecha_nacim, GETDATE())<=30 THEN '18-30' " +
        		   "WHEN DATEDIFF(yy, eje_ges_trabajador.fecha_nacim, GETDATE())>30 and DATEDIFF(yy, eje_ges_trabajador.fecha_nacim, GETDATE())<=50  THEN '30-50' " +
        		   "WHEN DATEDIFF(yy, eje_ges_trabajador.fecha_nacim, GETDATE())>50 and DATEDIFF(yy, eje_ges_trabajador.fecha_nacim, GETDATE())<=60  THEN '50-60' " +
        		   "WHEN DATEDIFF(yy, eje_ges_trabajador.fecha_nacim, GETDATE())>60  THEN '60-+' END " +
        		   "from eje_ges_trabajador where unidad='"+ valida.validarDato(cons.getString("unid_id")) + 
        		   "') as tmp group by tmp.tramoEdad";
            cons2.exec(query1);
            String teA="0",teB="0",teC="0",teD="0";
        	for(;cons2.next();) {
        		if(cons2.getString("tramoEdad").equals("18-30")){
        			teA=cons2.getString("nroTrab");
        		}
        		else if(cons2.getString("tramoEdad").equals("30-50")){
        			teB=cons2.getString("nroTrab");
        		}
        		else if(cons2.getString("tramoEdad").equals("50-60")){
        			teC=cons2.getString("nroTrab");
        		}
        		else if(cons2.getString("tramoEdad").equals("60-+")){
        			teD=cons2.getString("nroTrab");
        		}
        	}
        	UnidadIter.put("te_A",teA );
        	UnidadIter.put("te_B",teB );
        	UnidadIter.put("te_C",teC );
        	UnidadIter.put("te_D",teD );
        	teA_total+=Integer.parseInt(teA);
        	teB_total+=Integer.parseInt(teB);
        	teC_total+=Integer.parseInt(teC);
        	teD_total+=Integer.parseInt(teD);
        	query1= "select sexo,count(rut) nroTrab from eje_ges_trabajador where " +
        	        "unidad='" + valida.validarDato(cons.getString("unid_id")) + "' group by sexo"; 
        	cons2.exec(query1);
        	String tgF="0",tgM="0";
        	for(;cons2.next();) {
        		if(cons2.getString("sexo").equals("F")){
        			tgF=cons2.getString("nroTrab");
        		}
        		else if(cons2.getString("sexo").equals("M")){
        			tgM=cons2.getString("nroTrab");
        		}
        	}
        	UnidadIter.put("tg_F",tgF );
        	UnidadIter.put("tg_M",tgM );
        	tgF_total+=Integer.parseInt(tgF);
        	tgM_total+=Integer.parseInt(tgM);
        	query1= "select niveduc,count(rut) nroTrab from eje_ges_trabajador_neu where " +
        	        "rut in (select rut from eje_ges_trabajador where " +
        	        "unidad='" + valida.validarDato(cons.getString("unid_id")) + "') " +
        	        "group by niveduc";
        	cons2.exec(query1);
        	String neA="0",neT="0",neP="0";
        	for(;cons2.next();) {
        		if(cons2.getString("niveduc").equals("ADM")){
        			neA=cons2.getString("nroTrab");
        		}
        		else if(cons2.getString("niveduc").equals("TEC")){
        			neT=cons2.getString("nroTrab");
        		}
        		else if(cons2.getString("niveduc").equals("PRO")){
        			neP=cons2.getString("nroTrab");
        		}
        	}
        	UnidadIter.put("ne_A",neA );
        	UnidadIter.put("ne_T",neT );
        	UnidadIter.put("ne_P",neP );
        	neA_total+=Integer.parseInt(neA);
        	neT_total+=Integer.parseInt(neT);
        	neP_total+=Integer.parseInt(neP);
        	query1= "select cc.areanegocio ubicacion,count(distinct rut) nroTrab from " +
        	        "eje_ges_trabajador t inner join eje_cap_clasificacion_cargos cc " +
        	        "on t.cargo=cc.cargo and t.wp_cod_empresa=cc.empresa where " +
        	        "t.unidad='" + valida.validarDato(cons.getString("unid_id")) + "' " +
        	        "group by cc.areanegocio";
	        cons2.exec(query1);
	        String ubicF="0",ubicB="0";
	        for(;cons2.next();) {
	        	if(cons2.getString("ubicacion").equals("FRONT")){
	        		ubicF=cons2.getString("nroTrab");
	        	}
	        	else if(cons2.getString("ubicacion").equals("BACK")){
	        		ubicB=cons2.getString("nroTrab");
	        	}	
	        }
	        UnidadIter.put("ubic_F",ubicF );
	        UnidadIter.put("ubic_B",ubicB );
	        ubicF_total+=Integer.parseInt(ubicF);
	        ubicB_total+=Integer.parseInt(ubicB);
        	query1= "select cl.descripcion , count(t.rut) nroTrab from (eje_ges_trabajador t " +
        	        "inner join eje_cap_clasificacion_cargos cc on t.cargo=cc.cargo and " +
        	        "t.wp_cod_empresa=cc.empresa) inner join eje_cap_clasificacion cl on " +
        	        "cc.id_clasificacion=cl.id_clasificacion where " +
        	        "t.unidad='" + valida.validarDato(cons.getString("unid_id")) + 
        	        "' group by cl.descripcion"; 
        	cons2.exec(query1);
        	String noA="0",noT="0",noP="0",noJ="0",noE="0";
        	for(;cons2.next();) {
        		if(cons2.getString("descripcion").equals("Administrativo")) {
        			noA=cons2.getString("nroTrab");
        		}
        		else if(cons2.getString("descripcion").equals("Técnico")) {
        			noT=cons2.getString("nroTrab");
        		}
        		else if(cons2.getString("descripcion").equals("Profesional")) {
        			noP=cons2.getString("nroTrab");
        		}
        		else if(cons2.getString("descripcion").equals("Jefatura")) {
        			noJ=cons2.getString("nroTrab");
        		}
        		else if(cons2.getString("descripcion").equals("Ejecutivo")) {
        			noE=cons2.getString("nroTrab");
        		}
        	}
        	UnidadIter.put("no_A",noA );
        	UnidadIter.put("no_T",noT );
        	UnidadIter.put("no_P",noP );
        	UnidadIter.put("no_J",noJ );
        	UnidadIter.put("no_E",noE );
        	noA_total+=Integer.parseInt(noA);
        	noT_total+=Integer.parseInt(noT);
        	noP_total+=Integer.parseInt(noP);
        	noJ_total+=Integer.parseInt(noJ);
        	noE_total+=Integer.parseInt(noE);
        }
        modelRoot.put("lunidades",UnidadList);
        modelRoot.put("teA_total",String.valueOf(teA_total));
        modelRoot.put("teB_total",String.valueOf(teB_total));
        modelRoot.put("teC_total",String.valueOf(teC_total));
        modelRoot.put("teD_total",String.valueOf(teD_total));
        modelRoot.put("tgF_total",String.valueOf(tgF_total));
        modelRoot.put("tgM_total",String.valueOf(tgM_total));
        modelRoot.put("neA_total",String.valueOf(neA_total));
        modelRoot.put("neT_total",String.valueOf(neT_total));
        modelRoot.put("neP_total",String.valueOf(neP_total));
        modelRoot.put("ubicF_total",String.valueOf(ubicF_total));
        modelRoot.put("ubicB_total",String.valueOf(ubicB_total));
        modelRoot.put("noA_total",String.valueOf(noA_total));
        modelRoot.put("noT_total",String.valueOf(noT_total));
        modelRoot.put("noP_total",String.valueOf(noP_total));
        modelRoot.put("noJ_total",String.valueOf(noJ_total));
        modelRoot.put("noE_total",String.valueOf(noE_total));
        modelRoot.put("unidad",unidad);
        modelRoot.put("empresa",empresa);
        modelRoot.put("recursivo",recursivo);
        if(excel.equals("1")) {
        	modelRoot.put("xls","1");
        }
        if(recursivo.equals("S")) {
        	nomUnidad = "AREA " + nomUnidad; 
        }
        else {
        	nomUnidad = "UNIDAD " + nomUnidad;
        }
        modelRoot.put("nomUnidad",nomUnidad);
    	try {
			template.process(modelRoot,out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	out.flush();
    	out.close();
	}

	private Consulta Unidades(String empresa, String unidad, String recursivo, Connection con) {
		Consulta cons = new Consulta(con);
        String query = "select unid_id,unid_desc from eje_ges_unidades where unid_id ";
        if(recursivo.equals("S")) {  
        	query+="in (";
        	List lista2 = new ArrayList(); lista2 = ExtrasOrganica.NodosHijosRecursivos(unidad,empresa,con,lista2); lista2.add(unidad); 
       	   	for(int i=0;i<lista2.size();i++) {  
       	   		query+= "'" + lista2.get(i).toString() + "'";
       	   		if(i<lista2.size()-1) {
       	   			query+=",";
       	   		}
       	   	}
       	   	query+=")";
        }
        else {  
        	query+= "= '" + unidad + "'"; 
        }
        query+= " order by unid_desc";
        cons.exec(query);
        return cons;
	}
	
	protected void doBaseMasGratificacion(HttpServletRequest request, HttpServletResponse response, 
		String unidad, String recursivo, String empresa) throws ServletException, IOException {
		
        PrintWriter out	= response.getWriter();
        String excel	= request.getParameter("excel")==null?"0":request.getParameter("excel");
        
        if(excel.equals("1")) {
        	response.setContentType("application/vnd.ms-excel");
        }
        else {
        	response.setContentType("text/html");
        }
        response.setHeader("Expires", "0");
        response.setHeader("Pragma", "no-cache");
        
        Template template		= getTemplate("Gestion/Reportes/Base_Gratificacion.htm");
        SimpleHash modelRoot	= new SimpleHash ();
        Validar valida			= new Validar();
        DecimalFormat dec		= new DecimalFormat("###.#");
        Connection conecction	= connMgr.getConnection("portal");
        Consulta cons			= new Consulta(conecction), cons2 = new Consulta(conecction), cons3 = new Consulta(conecction);
        String nomUnidad		= ExtrasOrganica.UnidadDesc(unidad,conecction).toUpperCase();
        
        String query = "select top 1 peri_ano,peri_mes,peri_id from eje_ges_periodo order by peri_id desc";
        cons3.exec(query); cons3.next();
        String mes = cons3.getString("peri_mes"), ano = cons3.getString("peri_ano"), periodo = cons3.getString("peri_id");
        modelRoot.put("mes",mes);
        modelRoot.put("anho",ano);

        cons = Unidades(empresa,unidad,recursivo,conecction);
        
        SimpleList UnidadList = new SimpleList();
    	SimpleHash UnidadIter = new SimpleHash();
    	int totalRentas=0;
    	
        for(;cons.next();UnidadList.add(UnidadIter)) {
        	UnidadIter = new SimpleHash();
        	UnidadIter.put("uDesc",valida.validarDato(cons.getString("unid_desc").toUpperCase()) );
        	String unidadQry = valida.validarDato(cons.getString("unid_id"));
    		SimpleList PersonaList = new SimpleList();
    		SimpleHash PersonaIter;
    		PersonaIter = new SimpleHash();
        	String query1 = "SELECT t.rut,t.digito_ver,t.nombre,c.descrip,sum(l.val_haber) as totalHaber " +
 	               			"FROM eje_ges_certif_histo_liquidacion_detalle l " + 
 	               			"INNER JOIN eje_ges_trabajador t ON t.rut=l.rut " +
 	               			"INNER JOIN eje_ges_cargos c ON t.cargo=c.cargo AND t.wp_cod_empresa=c.empresa " +
 	               			"WHERE l.periodo = (SELECT MAX(peri_id) FROM eje_ges_periodo) " +
 	               			"AND (glosa_haber like '%gratif%' OR glosa_haber like 'Sueldo Base') " +
 	               			"AND t.unidad  = '" + unidadQry + "' " +
 	               			"GROUP BY t.rut,t.digito_ver,t.nombre,c.descrip ORDER BY t.rut";
        	cons2.exec(query1);
            
        	for(;cons2.next();PersonaList.add(PersonaIter)) {
        		PersonaIter = new SimpleHash();
        		PersonaIter.put("rut",valida.validarDato(cons2.getString("rut")));
        		PersonaIter.put("digito_ver",valida.validarDato(cons2.getString("digito_ver")));
        		PersonaIter.put("nombre",valida.validarDato(cons2.getString("nombre")) );
        		PersonaIter.put("descrip",valida.validarDato(cons2.getString("descrip")) );
        		PersonaIter.put("totalHaber",Tools.setFormatNumber(cons2.getString("totalHaber")));
    			totalRentas = totalRentas + valida.validarNum(cons2.getString("totalHaber"));
        	}
        	UnidadIter.put("lpersona",PersonaList);
        	UnidadIter.put("totalRentas",Tools.setFormatNumber(String.valueOf(totalRentas)));
        	totalRentas = 0;
        }
        
    	modelRoot.put("unidad",unidad);
        modelRoot.put("empresa",empresa);
        modelRoot.put("recursivo",recursivo);
        
        if(excel.equals("1")) {
        	modelRoot.put("xls","1");
        }
        modelRoot.put("lunidades",UnidadList);
        if(recursivo.equals("S")) {
        	nomUnidad = "AREA " + nomUnidad; 
        }
        else {
        	nomUnidad = "UNIDAD " + nomUnidad;
        }
        modelRoot.put("nomUnidad",nomUnidad);
    	try {
			template.process(modelRoot,out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	out.flush();
    	out.close();

	}
	
	 protected void doLiquidacionEstandar(HttpServletRequest request, HttpServletResponse response, 
		String unidad, String recursivo, String empresa) throws ServletException, IOException {
		 
	    PrintWriter out = response.getWriter();
	    String excel	= request.getParameter("excel")==null?"0":request.getParameter("excel");
	    
	    if(excel.equals("1")) {
	    	response.setContentType("application/vnd.ms-excel");
	    }
	    else {
	    	response.setContentType("text/html");
	    }
	    response.setHeader("Expires", "0");
	    response.setHeader("Pragma", "no-cache");
	    
	    Template template		= getTemplate("Gestion/Reportes/Liquido_Estandar.htm");
	    SimpleHash modelRoot	= new SimpleHash ();
	    Validar valida			= new Validar();
	    DecimalFormat dec		= new DecimalFormat("###.#");
	    Connection conecction	= connMgr.getConnection("portal");
	    Consulta cons			= new Consulta(conecction),cons2 = new Consulta(conecction),cons3 = new Consulta(conecction),
	    						  cons4 = new Consulta(conecction),consAux = new Consulta(conecction);;
	    String nomUnidad		= ExtrasOrganica.UnidadDesc(unidad,conecction).toUpperCase();
	    
	    String query = "SELECT top 1 peri_ano,peri_mes,peri_id,peri_utm,peri_uf FROM eje_ges_periodo ORDER BY peri_id desc";
	    cons3.exec(query); cons3.next();
	    String mes = cons3.getString("peri_mes"), ano = cons3.getString("peri_ano"), periodo = cons3.getString("peri_id");
	    String utm = cons3.getString("peri_utm"), uf = cons3.getString("peri_uf");
	    modelRoot.put("mes",mes);
	    modelRoot.put("anho",ano);
	    
	    //Tabla parametrica
	    String porsentajeAFP = "0",colacion = "0",movilizacion = "0",gratificacion = "0",topeGratificacion = "0",porsentajeAFC = "0",
	    	   topeAFC = "0";
	    
	    int totalHaberImpTrib = 0, descAFP = 0, descIsapre = 0, descAFC = 0, tributable = 0, tramo = 0, impuesto = 0;
	   
	    String queryParametro = "SELECT id, nombre, valor FROM eje_ges_parametros ORDER BY id";
	    cons3.exec(queryParametro);
		for(;cons3.next();){   
		    if (cons3.getString("id").equals("1"))
		    	porsentajeAFP		= cons3.getString("valor");
		    if (cons3.getString("id").equals("2"))
		    	gratificacion		= cons3.getString("valor");
		    if (cons3.getString("id").equals("3"))
		    	topeGratificacion	= cons3.getString("valor");
		    if (cons3.getString("id").equals("4"))
		    	porsentajeAFC		= cons3.getString("valor");
		    if (cons3.getString("id").equals("5"))
		    	topeAFC				= cons3.getString("valor");
	    }
	    
	    cons = Unidades(empresa,unidad,recursivo,conecction);
	    
	    SimpleList UnidadList	= new SimpleList();
		SimpleHash UnidadIter	= new SimpleHash();
		int totalRentaLiq = 0, contPeriodo = 0, periodoOk = 0;
		String periodoAux = "0", noImpNoTribut = "0";
		
		for(;cons.next();UnidadList.add(UnidadIter)) {
			UnidadIter				= new SimpleHash();
			SimpleList PersonaList	= new SimpleList();
			SimpleHash PersonaIter	= new SimpleHash();
			String unidadQry		= valida.validarDato(cons.getString("unid_id"));
			
			UnidadIter.put("uDesc",valida.validarDato(cons.getString("unid_desc").toUpperCase()) );
			
			String queryAux = "SELECT l.rut FROM eje_ges_certif_histo_liquidacion_cabecera l " +
							  "INNER JOIN eje_ges_trabajador t ON t.rut = l.rut " +
							  "WHERE l.periodo = " +periodo+ " AND t.unidad = " +unidadQry+ " ORDER BY l.rut";
			consAux.exec(queryAux);
			
			for(;consAux.next();) {
				
				while (periodoOk == 0){

					periodoAux = periodo;
					periodoAux = String.valueOf(Integer.valueOf(periodoAux) - contPeriodo);
					
			    	String query1 = "SELECT l.periodo, l.unidad, l.rut, t.digito_ver as dv, t.nombre, c.descrip, "+
			    					"ISNULL (l.wp_ndias_trab,0) AS ndias_trab, CONVERT(CHAR(10),t.fecha_ingreso,103) AS fecha_ingreso, " +
			    					"l.tot_haberes, l.tope_imp, l.imp_tribut AS tope_AFC, l.val_uf, l.no_imp_no_tribut " +
			    					"FROM eje_ges_certif_histo_liquidacion_cabecera l " +
			    					"INNER JOIN eje_ges_trabajador t ON t.rut = l.rut " +
			    					"INNER JOIN eje_ges_cargos c ON t.cargo=c.cargo AND t.wp_cod_empresa=c.empresa " +
			    					"WHERE l.periodo = " +periodoAux+ " AND t.unidad = "+unidadQry+ " AND l.rut = " + consAux.getString("rut");
			    	cons2.exec(query1);cons2.next();
			    	
			    	if ( cons2.getString("ndias_trab").equals("30") )
			    		periodoOk = 1;
			    	
			    	contPeriodo++;
			    	
			    	if (contPeriodo == 6){
			    		periodoOk = 1;
			    	}
				}
		    	
		    	if ( cons2.getString("ndias_trab").equals("30") ) {
		    		
//		    		PersonaList.add(PersonaIter);

		    		noImpNoTribut		= valida.validarDato(cons2.getString("no_imp_no_tribut"));
		    	    totalHaberImpTrib	= Integer.valueOf(cons2.getString("tot_haberes")) - Integer.valueOf(noImpNoTribut);
		    	    
					//Cálculo descuento AFP estándar
					if ( Integer.valueOf(cons2.getString("tope_imp")) <= totalHaberImpTrib ){
						descAFP = (int)((Integer.valueOf(cons2.getString("tope_imp")) * Double.valueOf(porsentajeAFP))/100);
					}else{
						descAFP = (int)(totalHaberImpTrib * Double.valueOf(porsentajeAFP)/100);
					}
					
					//Cálculo descuento Isapre
					if (Integer.valueOf(cons2.getString("tope_imp")) <= totalHaberImpTrib ){
						descIsapre = (Integer.valueOf(cons2.getString("tope_imp")) * 7)/100; //tope isapre
					}else{
						descIsapre = (totalHaberImpTrib * 7)/100;
					}
					
					//Cálculo descuento AFC
					if (Double.valueOf(topeAFC) * Double.valueOf(uf) <= totalHaberImpTrib){
						descAFC = (int)((Double.valueOf(topeAFC) * Double.valueOf(uf)) * Double.valueOf(porsentajeAFC))/100;
					}else{
						descAFC = (int)(totalHaberImpTrib * Double.valueOf(porsentajeAFC))/100;
					}
					
					tributable	= totalHaberImpTrib - descAFP - descIsapre - descAFC;
					
					String queryAux3 = "SELECT tramo, desde, hasta, factor, aRebajar, tasaImptoEfectiva FROM eje_ges_impuesto_unico ORDER BY tramo";
		    	    cons4.exec(queryAux3);
					
		    	    while (tramo == 0){
		    	    	cons4.next();
		    	    	
		    	    	if ( !cons4.getString("tramo").equals("8") ){
			    	    	if ( (Double.valueOf(cons4.getString("desde")) <= tributable) && (Double.valueOf(cons4.getString("hasta")) >= tributable) ){
			    	    		impuesto = (int)(tributable * Double.valueOf(cons4.getString("factor")) - Double.valueOf(cons4.getString("aRebajar")));
			    	    		tramo  = 1;
			    	    	}
		    	    	}else{
		    	    		if ( (Double.valueOf(cons4.getString("desde")) <= tributable) ){
			    	    		impuesto = (int)(tributable * Double.valueOf(cons4.getString("factor")) - Double.valueOf(cons4.getString("aRebajar")));
			    	    		tramo  = 1;
			    	    	}
		    	    	}
		    	    }
		    	    
					PersonaIter = new SimpleHash();
					PersonaIter.put("rut",valida.validarDato(cons2.getString("rut")));
					PersonaIter.put("dv",valida.validarDato(cons2.getString("dv")));
					PersonaIter.put("nombre",valida.validarDato(cons2.getString("nombre")));
					PersonaIter.put("descrip",valida.validarDato(cons2.getString("descrip")));
					PersonaIter.put("fIngreso",valida.validarDato(cons2.getString("fecha_ingreso")));
					PersonaIter.put("nDiasTrab",valida.validarDato(cons2.getString("ndias_trab")));
					PersonaIter.put("liquido",Tools.setFormatNumber(tributable - impuesto));
					
					totalRentaLiq = totalRentaLiq + (tributable - impuesto);
					
			    	tramo		= 0;
			    	periodoOk	= 0;
			    	contPeriodo	= 0;
			    	
			    	PersonaList.add(PersonaIter);
		    	}else{
			    	System.out.println("Trabajador "+cons2.getString("rut")+"-"+cons2.getString("dv")+" no tiene 30 días ("+cons2.getString("ndias_trab")+") trabajados para calcular liquidación estandar.");
			    	tramo		= 0;
			    	periodoOk	= 0;
			    	contPeriodo	= 0;
		    	}
	    	}
	    	UnidadIter.put("lpersona",PersonaList);
	    	UnidadIter.put("totalRentaLiq",Tools.setFormatNumber(String.valueOf(totalRentaLiq)));
	    	totalRentaLiq = 0;
		}
	
		modelRoot.put("unidad",unidad);
	    modelRoot.put("empresa",empresa);
	    modelRoot.put("recursivo",recursivo);
	    
	    if(excel.equals("1")) {
	    	modelRoot.put("xls","1");
	    }
	    modelRoot.put("lunidades",UnidadList);
	    if(recursivo.equals("S")) {
	    	nomUnidad = "AREA " + nomUnidad; 
	    }
	    else {
	    	nomUnidad = "UNIDAD " + nomUnidad;
	    }
	    modelRoot.put("nomUnidad",nomUnidad);
		try {
			template.process(modelRoot,out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.flush();
		out.close();

	 }
}