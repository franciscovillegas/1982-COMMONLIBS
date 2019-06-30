package portal.com.eje.serhumano.menu.bean;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.misc.Formatear;
import cl.ejedigital.web.datos.ConsultaTool;
import cl.ejedigital.web.datos.DBConnectionManager;
import cl.ejedigital.web.datos.IDBConnectionManager;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.certificados.Certif_Manager;
import portal.com.eje.serhumano.certificados.Liquida_Manager;
import portal.com.eje.serhumano.datosdf.datosRut;
import portal.com.eje.serhumano.menu.Tools;
import portal.com.eje.serhumano.menu.menuManager;
import portal.com.eje.serhumano.misdatos.Vaca_Manager;
import portal.com.eje.serhumano.user.ControlAcceso;
import portal.com.eje.serhumano.user.ControlAccesoTM;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Validar;
import portal.com.eje.tools.servlet.FormatoNumero;
import portal.com.eje.tools.servlet.SubQuery;

public class FichaPersonalBean {
	private FichaPersonalBean() { 
	}
	
	public static FichaPersonalBean getInstance() {
		return FichaPersonalHolder.fp;
	}

	private static class FichaPersonalHolder {
		private static FichaPersonalBean fp = new FichaPersonalBean();
	}
	
	public SimpleHash getDatosLiquidacionMultEmpresas(Connection connection,String path, String periodo, int opcion,String rut, HttpServletRequest req) {
        String digito = "";
        String strFechaHoy = "";
        GregorianCalendar Fecha = new GregorianCalendar();
        int dia = Fecha.get(5);
        int ano = Fecha.get(1);
        int mes = Fecha.get(2) + 1;
        Liquida_Manager data_rut = new Liquida_Manager(connection);
        SimpleHash modelRoot = new SimpleHash();
        
		SimpleList LiquidacionList = new SimpleList();
		SimpleHash LiquidacionIter = new SimpleHash();
		int totalEmpresas = data_rut.GetCountEmpresasRut(rut,periodo);
		int iteracion=1;
		Consulta empresasLista = data_rut.GetEmpresasRut(rut,periodo);
		int contEmp = 0;
		for(;empresasLista.next();LiquidacionList.add(LiquidacionIter)) {
		
		LiquidacionIter = new SimpleHash();
		String salto = iteracion == totalEmpresas ? "N" : "S";
		LiquidacionIter.put("salta", salto);	
		
        String causa_pago = "";
        String empresaL = empresasLista.getString("empresa");
        Consulta info = data_rut.GetCabeceraEmpresaRut(rut, periodo,empresaL);
        Consulta emp = data_rut.GetEmpresaEmpresaRut(rut,periodo,empresaL);
        Consulta soc = data_rut.GetSociedadEmpresaRut(rut,periodo,empresaL);
        Consulta uni = data_rut.GetUnidad(rut);
        Consulta cc = data_rut.GetCentroCosto(rut);
        Consulta isapre = data_rut.GetIsapre(rut,periodo);
        Consulta afp = data_rut.GetAfp(rut,periodo);
        if(info.next() ) {
            strFechaHoy = (new StringBuilder("").append(dia).append(" de ").append(Tools.RescataMes(mes)).append(" de ").append(ano)).toString();
            LiquidacionIter.put("fecha_periodo", Tools.RescataMes(Integer.parseInt(periodo.substring(4, 6))) + " " + periodo.substring(0, 4));
            LiquidacionIter.put("fecha", strFechaHoy);
            LiquidacionIter.put("empresa", info.getString("nom_empre"));
            String[] rutEmpresa = info.getString("empre_rut").split("-");
            
            if(rutEmpresa.length >= 1) {
            	LiquidacionIter.put("empresa_rut_int",  rutEmpresa[0] );
            }
            
            if( rutEmpresa.length >= 2) {
            	LiquidacionIter.put("empresa_rut",  Tools.setFormatNumber( rutEmpresa[0] ) + "-" + rutEmpresa[1]);
            }
            else if(rutEmpresa.length == 1) {
            	LiquidacionIter.put("empresa_rut",  Tools.setFormatNumber( rutEmpresa[0] ));
            }
            digito = info.getString("digito_ver");
            LiquidacionIter.put("rut_trab_int",rut.trim());
            LiquidacionIter.put("rut_trab", (new StringBuilder().append(Tools.setFormatNumber(Integer.parseInt(rut.trim()))).append("-").append(digito)).toString() );
            LiquidacionIter.put("nombre", info.getString("nombre"));
            LiquidacionIter.put("cargo", info.getString("nom_cargo"));
            LiquidacionIter.put("ecivil", info.getString("estado_civil"));
            
            if(info.getString("sexo") != null) {
            	if( "M".equals(info.getString("sexo").toUpperCase())) {
            		LiquidacionIter.put("sexo", "Hombre");
            	}
            	else if( "F".equals(info.getString("sexo").toUpperCase())) {
            		LiquidacionIter.put("sexo", "Mujer");
            	}
            }
            
            emp.next();
            soc.next();
            uni.next();
            cc.next();
            isapre.next();
            afp.next();
            LiquidacionIter.put("nom_empresa", emp.getString("nom_empresa"));
            LiquidacionIter.put("dir_empresa", emp.getString("empre_dir"));
            
            LiquidacionIter.put("valor_adic_1", info.getString("valor_adic_1"));
            LiquidacionIter.put("valor_adic_2", info.getString("valor_adic_2"));
            LiquidacionIter.put("fec_ter_cont", info.getString("fec_ter_cont"));          
            LiquidacionIter.put("dias", info.getString("ndias_trab"));
            LiquidacionIter.put("nom_sociedad", soc.getString("nom_sociedad"));
            LiquidacionIter.put("nom_unidad", uni.getString("nom_unidad"));
            LiquidacionIter.put("id_centro_costo", cc.getString("centro_costo"));
            LiquidacionIter.put("centro_costo", cc.getString("nom_centro_costo"));
            LiquidacionIter.put("isapre", isapre.getString("nom_isapre"));
            LiquidacionIter.put("afp", afp.getString("nom_afp"));
            afp.close();
            LiquidacionIter.put("ingreso", Tools.RescataFecha(info.getValor("fecha_ingreso")));
            causa_pago = info.getString("causa_pago");
            LiquidacionIter.put("mod_pago", info.getString("forma_pago"));
            LiquidacionIter.put("cta", info.getString("cuenta"));
            LiquidacionIter.put("banco", info.getString("banco"));
            LiquidacionIter.put("i_t", Tools.setFormatNumber(info.getString("imp_tribut")));
            LiquidacionIter.put("i_nt", Tools.setFormatNumber(info.getString("imp_no_tribut")));
            LiquidacionIter.put("ni_t", Tools.setFormatNumber(info.getString("no_imp_tribut")));
            LiquidacionIter.put("ni_nt", Tools.setFormatNumber(info.getString("no_imp_no_tribut")));
            LiquidacionIter.put("tot_haberes", Tools.setFormatNumber(info.getString("tot_haberes")));
            LiquidacionIter.put("d_varios", Tools.setFormatNumber(info.getString("dctos_varios")));
            LiquidacionIter.put("d_legales", Tools.setFormatNumber(info.getString("dctos_legales")));
            LiquidacionIter.put("tot_destos", Tools.setFormatNumber(info.getString("tot_desctos")));
            LiquidacionIter.put("tot_aportaciones", Tools.setFormatNumber(info.getString("tot_aportaciones")));
            LiquidacionIter.put("liquido", Tools.setFormatNumber(info.getString("liquido")));
            LiquidacionIter.put("liquido_texto",  Formatear.getInstance().trasformaATexto(info.getInt("liquido")));
            LiquidacionIter.put("wp_ndias_lic", Tools.setFormatNumber(info.getString("wp_ndias_lic")));
            LiquidacionIter.put("tramo", Tools.setFormatNumber(info.getString("tramo")));
            LiquidacionIter.put("sobregiro", Tools.setFormatNumber(info.getString("sobregiro")));
            LiquidacionIter.put("n_cargas", Tools.setFormatNumber(info.getString("n_cargas")));
            LiquidacionIter.put("base_tribut", Tools.setFormatNumber(info.getString("base_tribut")));
            
            String it = "0";
            String intr = "0";
            String reliqt = "0";
            int rta = 0;
            String rta_impon = "";
            if(info.getString("imp_tribut") != null)
                it = info.getString("imp_tribut");
            if(info.getString("imp_no_tribut") != null)
                intr = info.getString("imp_no_tribut");
            if(info.getString("reliq_rentas") != null)
                reliqt = info.getString("reliq_rentas");

            
            LiquidacionIter.put("imp_tribut", Tools.setFormatNumber(it));
            LiquidacionIter.put("renta_impon", Tools.setFormatNumber(info.getString("rta_impon")));
            LiquidacionIter.put("tope_renta", Tools.setFormatNumber(info.getString("tope_imp")));
            LiquidacionIter.put("val_uf", Tools.setFormatNumberFloat(info.getString("val_uf")));
            LiquidacionIter.put("SubQuery", new SubQuery(connection, req, path));
            LiquidacionIter.put("FNum", new FormatoNumero());
            StringBuilder haberes = new StringBuilder(		"SELECT periodo, empresa, unidad,rut, tip_unidad, id_tp, glosa_haber, val_haber, orden, wp_cod_planta FROM eje_ges_certif_histo_liquidacion_detalle haberes WHERE (wp_indic_papeleta = 'S') AND (periodo = ").append(periodo).append(") ").append("AND  (rut = ").append(rut).append(") ").append(" and empresa=").append(empresaL).append(" and id_tp='H' and wp_indic_papeleta='S' ORDER BY rut, orden, val_haber desc");
            LiquidacionIter.put("haberes", haberes.toString());
            StringBuilder tiempos = new StringBuilder(		"SELECT periodo, empresa, unidad,rut, tip_unidad, id_tp, glosa_haber, val_haber, orden, wp_cod_planta FROM eje_ges_certif_histo_liquidacion_detalle haberes WHERE (wp_indic_papeleta = 'S') AND (periodo = ").append(periodo).append(") ").append("AND  (rut = ").append(rut).append(") ").append(" and empresa=").append(empresaL).append(" and id_tp='T' and wp_indic_papeleta='S' ORDER BY rut, orden, val_haber desc");
            LiquidacionIter.put("tiempos", tiempos.toString());
            StringBuilder aportaciones = new StringBuilder(	"SELECT periodo, empresa, unidad,rut, tip_unidad, id_tp, glosa_haber, val_haber, orden, wp_cod_planta FROM eje_ges_certif_histo_liquidacion_detalle haberes WHERE (wp_indic_papeleta = 'S') AND (periodo = ").append(periodo).append(") ").append("AND  (rut = ").append(rut).append(") ").append(" and empresa=").append(empresaL).append(" and id_tp='A' and wp_indic_papeleta='S' ORDER BY rut, orden, val_haber desc");
            LiquidacionIter.put("aportaciones", aportaciones.toString());
            
            StringBuilder descuentos = new StringBuilder("SELECT periodo, empresa, unidad,rut, tip_unidad, id_tp, orden, glosa_descuento,val_descuento, wp_cod_planta FROM eje_ges_certif_histo_liquidacion_detalle descuentos WHERE (wp_indic_papeleta = 'S') AND (periodo = ").append(periodo).append(") ").append("AND  (rut = ").append(rut).append(") ").append(" and empresa=").append(empresaL).append(" and id_tp='D' and wp_indic_papeleta='S' ORDER BY rut, val_descuento desc");
            LiquidacionIter.put("descuentos", descuentos.toString());
            
            LiquidacionIter.put("uri", "certificado=" + opcion + "&peri_liq=" + periodo + "");
            LiquidacionIter.put("certificado", String.valueOf(opcion));
            LiquidacionIter.put("peri_liq", periodo);
        } 
        else {
        	LiquidacionIter.put("error", "0");
            OutMessage.OutMessagePrint("---->Rut no encontrado!!!!");
        }
        
        info.close();
        uni.close();
        soc.close();
        uni.close();
        cc.close();
        isapre.close();
        
        System.out.println("@@ [getDatosLiquidacionMultEmpresas] Se encontraton "+(++contEmp)+" empresa(s)");
		}
		modelRoot.put("liquidaciones",LiquidacionList);

		return modelRoot;
	}
	
	public SimpleHash getDatosContratos(Connection connection,String rut) {
        SimpleHash simplehash = new SimpleHash();
        Validar validar = new Validar();
        menuManager menumanager = new menuManager(connection);
        Consulta consulta = menumanager.getDatosContrato(rut);
        Consulta uni = menumanager.GetUnidad(rut);
        Consulta cc = menumanager.GetCentroCosto(rut);
        System.out.println("RUT------------>" + rut);
        if(consulta.next()) {
            String s1 = consulta.getString("digito_ver");
            simplehash.put("foto", consulta.getString("id_foto"));
            simplehash.put("rut", rut);
            
            String rutToHash = Tools.setFormatNumber(rut);
            if(s1 != null && !"".equals(s1)) {
            	rutToHash += "-" + s1;
            }
            
            
            simplehash.put("rut2",  rutToHash);
            simplehash.put("mail", validar.validarDato(consulta.getString("e_mail"), "NR"));
            simplehash.put("anexo", validar.validarDato(consulta.getString("anexo"), "NR"));
            simplehash.put("nombre", consulta.getString("nombre"));
            simplehash.put("area", consulta.getString("area"));
            simplehash.put("cargo", consulta.getString("cargo"));
            simplehash.put("nom_empresa", consulta.getString("nom_empresa"));
            simplehash.put("nom_sociedad", consulta.getString("nom_sociedad"));
            simplehash.put("segdes", consulta.getString("aplica_seguro_des"));
            simplehash.put("fec_segdes", validar.validarFecha(consulta.getString("fec_ini_seguro_des")));
            simplehash.put("renta", Tools.setFormatNumber(consulta.getString("sueldo_base_mensual")));
            simplehash.put("moneda_renta", consulta.getString("moneda_sueldo_base_mensual"));
            cc.next();
            uni.next();
            simplehash.put("id_cco", validar.validarDato(consulta.getString("ccosto"), "No tiene"));
            simplehash.put("cco", validar.validarDato(cc.getString("nom_centro_costo"), "No tiene"));
            simplehash.put("id_unidad", validar.validarDato(consulta.getString("id_unidad"), ""));
            simplehash.put("unidad", validar.validarDato(uni.getString("nom_unidad"), "No tiene"));
            simplehash.put("id_ctrab", validar.validarDato(consulta.getString("id_centro_trabajo"), "No tiene"));
            simplehash.put("conv_laboral", consulta.getString("conv_laboral"));
            simplehash.put("sindicato", consulta.getString("sindicato"));
            simplehash.put("rut_supervisor", Tools.setFormatNumber(consulta.getString("rut_supdirecto")) + "-" + consulta.getString("dig_supdirecto"));
            simplehash.put("supervisor", consulta.getString("nom_supdirecto"));
            simplehash.put("cargo_supervisor", consulta.getString("cargo_supdirecto"));
            simplehash.put("ubicacion", consulta.getString("ubic_fisica"));
            simplehash.put("division", consulta.getString("division"));
            simplehash.put("categoria", consulta.getString("categ_cargo"));
            simplehash.put("tipo_poder", consulta.getString("tipo_poder"));
            simplehash.put("tipo_contrato", consulta.getString("desc_tip_contrato"));
            simplehash.put("termino", validar.validarFecha(consulta.getValor("term"), "NO DEFINIDO"));
            simplehash.put("fec_ingreso", validar.validarFecha(consulta.getValor("fecha_ingreso")));
            String s2 = validar.validarDato(consulta.getString("antiguedad"), "0");
            OutMessage.OutMessagePrint("------>Antiguedad= " + consulta.getValor("antiguedad"));
            int i = Integer.parseInt(s2) / 12;
            if(i < 0)
                i *= -1;
            simplehash.put("xempre", String.valueOf(i));
            s2 = validar.validarDato(consulta.getString("xcorp"), "0");
            i = Integer.parseInt(s2) / 12;
            if(i < 0)
                i *= -1;
            simplehash.put("xcorp", String.valueOf(i));
            simplehash.put("xcorp_string", getCantAAAString(i));
            
            
            if( consulta.getValor("inghold") != null && consulta.getValor("fecha_ingreso") != null ) {
            	simplehash.put("ingre_corp", validar.validarFecha(consulta.getValor("inghold")));
            }
            else {
            	simplehash.put("ingre_corp",  "n/a");
            }
            
            simplehash.put("fec_unidad", validar.validarFecha(consulta.getValor("fec_unidad")));
            s2 = validar.validarDato(consulta.getString("xunidad"), "0");
            i = Integer.parseInt(s2) / 12;
            if(i < 0)
                i *= -1;
            simplehash.put("xunidad", String.valueOf(i));
            simplehash.put("ingre_cargo", validar.validarFecha(consulta.getValor("ingcargo")));
            s2 = validar.validarDato(consulta.getString("xcargo"), "0");
            i = Integer.parseInt(s2) / 12;
            if(i < 0)
                i *= -1;
            simplehash.put("xcargo", String.valueOf(i));
            simplehash.put("segsalcom", validar.validarDato(consulta.getString("segsalcom")));
            
        } 
        else {
            simplehash.put("error", "0");
            OutMessage.OutMessagePrint("---->Rut no encontrado!!!!");
        }
        consulta.close();
        cc.close();
        uni.close();
        return simplehash;
	}
	
	private String getCantAAAString(int aaa) {
		if(aaa < 1) {
			return "Menos de un año";
		}
		else {
			return aaa + " años";
		}
	}

	public SimpleHash getDatosPrev(Connection connection,String rut) {
        Validar validar = new Validar();
        SimpleHash simplehash = new SimpleHash();
        SimpleList simplelist = new SimpleList();
        SimpleList simplelist1 = new SimpleList();
        SimpleList simplelist2 = new SimpleList();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd/MM/yyyy");
        menuManager menumanager = new menuManager(connection);
        Consulta consulta = menumanager.getCargas(rut);
        if(consulta.next()) {
            String s = consulta.getString("cargas");
            Consulta consulta1 = menumanager.getDatosPrev(rut);
            
            if(consulta1.next()) {
	            simplehash.put("foto", consulta1.getString("id_foto"));
	            simplehash.put("nombre", validar.validarDato(consulta1.getString("nombre"), "No tiene"));
	            simplehash.put("afp", validar.validarDato(consulta1.getString("afp"), "No tiene"));
	            simplehash.put("fec_afilia", validar.validarFecha(consulta1.getValor("fec_afi_sist")));
	            simplehash.put("ingre_afp", validar.validarFecha(consulta1.getValor("fec_ing_afp"), "No tiene"));
	            simplehash.put("cotizacion", validar.validarDato(Tools.setFormatNumberFloat(consulta1.getString("cot_afp")), "No tiene"));
	            simplehash.put("moneda_cotizacion", validar.validarDato(consulta1.getString("mo_cot_afp"), ""));
	            simplehash.put("monto_adicional", validar.validarDato(consulta1.getString("cot_adic") != null ? Tools.setFormatNumberFloat(consulta1.getString("cot_adic")) : consulta1.getString("cot_adic"), "No tiene"));
	            simplehash.put("moneda_adicional", validar.validarDato(consulta1.getString("mo_cot_adic"), ""));
	            simplehash.put("monto_volunt", validar.validarDato(consulta1.getString("ah_volunt") != null ? Tools.setFormatNumberFloat(consulta1.getString("ah_volunt")) : consulta1.getString("ah_volunt"), "No tiene")); 
	            simplehash.put("moneda_volunt", validar.validarDato(consulta1.getString("mo_ah_volunt"), ""));
	            simplehash.put("jubilado", validar.validarDato(consulta1.getString("jubilado"), ""));
	            simplehash.put("monto_deposito", validar.validarDato(consulta1.getString("dep_conven"), "No tiene"));
	            simplehash.put("moneda_deposito", validar.validarDato(consulta1.getString("mon_dep_conven"), "No tiene"));
	            simplehash.put("afp_historica", validar.validarDato(consulta1.getString("afp_histo"), "No tiene"));
	            simplehash.put("isapre", validar.validarDato(consulta1.getString("isapre"), "No tiene"));
	            simplehash.put("ing_isap", validar.validarFecha(consulta1.getValor("fec_ing_isap"), "No tiene"));
	            simplehash.put("plan_sal", validar.validarDato(consulta1.getString("plan_salud"), "No tiene"));
	            simplehash.put("fec_consa", validar.validarFecha(consulta1.getValor("fec_con_salud")));
	            simplehash.put("fec_venc", validar.validarFecha(consulta1.getValor("venc_salud")));
	            simplehash.put("mon_plan", validar.validarDato(consulta1.getString("mon_salud"), "No tiene"));
	            simplehash.put("cotisap", validar.validarDato(Tools.setFormatNumberFloat(String.valueOf(Tools.setRedondear(consulta1.getString("cot_salud"),2))), "No tiene"));
	            simplehash.put("adicsal", validar.validarDato(consulta1.getString("adic_salud"), "No tiene"));
	            simplehash.put("moneda_adicsal", validar.validarDato(consulta1.getString("mon_adic_salud"), "0"));
	            simplehash.put("isap_histo", validar.validarDato(consulta1.getString("isap_histo"), "No tiene"));
	            simplehash.put("camb_plan", validar.validarDato(consulta1.getString("cambio_plan"), "No tiene"));
	            simplehash.put("mail", validar.validarDato(consulta1.getString("e_mail"), "No tiene"));
	            simplehash.put("anexo", validar.validarDato(consulta1.getString("anexo"), "No tiene"));
	            simplehash.put("rut2", Tools.setFormatNumber(rut));
	            simplehash.put("rut", rut);
	            simplehash.put("unidad", validar.validarDato(consulta1.getString("unidad"), "No tiene"));
            }
            
            simplehash.put("numcargas", validar.validarDato(s, "0"));
            simplehash.put("cargas", simplelist);
            consulta1.close();
        } 
        else {
            simplehash.put("error", "0");
            System.out.println("---->Rut no encontrado!!!!");
        }
        consulta.close();
        return simplehash;
	}

	public SimpleHash getFormacion(Connection connection,String rut) {
        Validar validar = new Validar();
        SimpleHash simplehash = new SimpleHash();
        menuManager menumanager = new menuManager(connection);
        Consulta consulta = menumanager.getFormacion(rut);
        if(consulta.next()) {
            simplehash.put("rut", rut);
            simplehash.put("anexo", validar.validarDato(consulta.getString("anexo"), "NR"));
            simplehash.put("mail", validar.validarDato(consulta.getString("e_mail"), "NR"));
            simplehash.put("unidad", consulta.getString("unidad"));
            simplehash.put("nombre", consulta.getString("nombre"));
            simplehash.put("cargo", consulta.getString("cargo"));
            simplehash.put("foto", consulta.getString("id_foto"));
            Consulta consulta1 = menumanager.getDescForm(rut);
            SimpleList simplelist = new SimpleList();
            SimpleHash simplehash1;
            for(; consulta1.next(); simplelist.add(simplehash1)) {
                simplehash1 = new SimpleHash();
                simplehash1.put("tipo", validar.validarDato(consulta1.getString("tipo")));
                simplehash1.put("titulo", validar.validarDato(consulta1.getString("titulo")));
                simplehash1.put("fecha", validar.validarFecha(consulta1.getValor("fecha")));
                simplehash1.put("organismo", validar.validarDato(consulta1.getString("instituto")));
                simplehash1.put("estado", validar.validarDato(consulta1.getString("estado")));
            }

            simplehash.put("profesional", simplelist);
            consulta1.close();
            Consulta consulta2 = menumanager.getCapacitaciones(rut);
            simplelist = new SimpleList();
            SimpleHash simplehash2;
            for(; consulta2.next(); simplelist.add(simplehash2)) {
                simplehash2 = new SimpleHash();
                simplehash2.put("curso", consulta2.getString("curso"));
                simplehash2.put("fecha_inicio", validar.validarFecha(consulta2.getValor("fecha_inicio")));
                simplehash2.put("fecha_termino", validar.validarFecha(consulta2.getValor("fecha_termino")));
                simplehash2.put("organismo", validar.validarDato(consulta2.getString("organismo")));
                simplehash2.put("valor", validar.validarDato(consulta2.getString("valor")));
                simplehash2.put("asistencia", validar.validarDato(consulta2.getString("asistencia")));
                simplehash2.put("nota_aprob", validar.validarDato(consulta2.getString("nota_aprob")));
                simplehash2.put("concepto_aprob", validar.validarDato(consulta2.getString("resul_texto")));
                simplehash2.put("horas", validar.validarDato(consulta2.getString("horas")));
            }

            simplehash.put("cursos", simplelist);
            consulta2.close();
        } 
        else {
            simplehash.put("error", "0");
            System.out.println("---->Rut no encontrado!!!!");
        }
        consulta.close();
		return simplehash;
	}

	public SimpleHash getGrupoFamiliar(Connection connection,String rut) {
        Validar validar = new Validar();
        SimpleHash simplehash = new SimpleHash();
        menuManager menumanager = new menuManager(connection);
        Consulta consulta = menumanager.getCargasFamiliares(rut);
        if(consulta.next()) {
            simplehash.put("rut", rut);
            simplehash.put("rut2", Tools.setFormatNumber(rut) + "-" + consulta.getString("digito_ver"));
            simplehash.put("nombre", consulta.getString("nombre"));
            simplehash.put("cargo", consulta.getString("cargo"));
            simplehash.put("fecha_ing", validar.validarFecha(consulta.getValor("fecha_ingreso")));
            simplehash.put("foto", consulta.getString("id_foto"));
            simplehash.put("afp", validar.validarDato(consulta.getString("afp"), "NR"));
            simplehash.put("isapre", validar.validarDato(consulta.getString("isapre"), "NR"));
            simplehash.put("estado", validar.validarDato(consulta.getString("estado_civil"), "NR"));
            simplehash.put("anexo", validar.validarDato(consulta.getString("anexo"), "NR"));
            simplehash.put("mail", validar.validarDato(consulta.getString("e_mail"), "NR"));
            simplehash.put("unidad", consulta.getString("unidad"));
            simplehash.put("id_unidad", consulta.getString("id_unidad"));
            simplehash.put("area", consulta.getString("area"));
            simplehash.put("division", consulta.getString("division"));
            Consulta consulta1 = menumanager.getListaCargasFamiliares(rut);
            SimpleList simplelist = new SimpleList();
            SimpleHash simplehash1;
            for(; consulta1.next(); simplelist.add(simplehash1)) {
                System.out.println("-------***---EDAD :" + consulta1.getString("edad"));
                simplehash1 = new SimpleHash();
                simplehash1.put("rut", rut);
                simplehash1.put("num", validar.validarDato(consulta1.getString("numero")));
                simplehash1.put("nombre", validar.validarDato(consulta1.getString("nombre")));
                simplehash1.put("fec_nac", validar.validarFecha(consulta1.getValor("fecha_nacim")));
                simplehash1.put("rut_carga", validar.validarDato(consulta1.getString("rut_carga")));
                simplehash1.put("dv_carga", validar.validarDato(consulta1.getString("dv_carga")));
                simplehash1.put("es_carga", validar.validarDato(consulta1.getString("es_carga")));
                simplehash1.put("es_carga_salud", validar.validarDato(consulta1.getString("es_carga_salud")));
                simplehash1.put("actividad", validar.validarDato(consulta1.getString("actividad")));
                simplehash1.put("fec_inicio", validar.validarFecha(consulta1.getValor("fecha_inicio")));
                simplehash1.put("fec_termino", validar.validarFecha(consulta1.getValor("fecha_termino")));
                simplehash1.put("edad", validar.validarDato(consulta1.getString("edad")));
                simplehash1.put("sexo", validar.validarDato(consulta1.getString("sexo")));
                simplehash1.put("parentesco", validar.validarDato(consulta1.getString("parentesco")));
            }

            simplehash.put("cargas", simplelist);
            consulta1.close();
        } 
        else {
            simplehash.put("error", "0");
            System.out.println("---->Rut no encontrado!!!!");
        }
        consulta.close();
        return simplehash;
	}

	public SimpleHash getLicenciasMedicas(Connection connection,String rut) {
        Validar validar = new Validar();
        SimpleHash simplehash = new SimpleHash();
        menuManager menumanager = new menuManager(connection);
        Consulta consulta1 = menumanager.getCargaLicencia(rut);
        if(consulta1.next()) {
            simplehash.put("rut", rut);
            simplehash.put("rut2", Tools.setFormatNumber(rut) + "-" + consulta1.getString("digito_ver"));
            simplehash.put("nombre", consulta1.getString("nombre"));
            simplehash.put("cargo", consulta1.getString("cargo"));
            simplehash.put("fecha_ing", validar.validarFecha(consulta1.getValor("fecha_ingreso")));
            simplehash.put("fecha_antig", validar.validarFecha(consulta1.getValor("fecha_antiguedad")));
            simplehash.put("foto", consulta1.getString("id_foto"));
            simplehash.put("afp", validar.validarDato(consulta1.getString("afp"), "NR"));
            simplehash.put("isapre", validar.validarDato(consulta1.getString("isapre"), "NR"));
            simplehash.put("estado", validar.validarDato(consulta1.getString("estado_civil"), "NR"));
            simplehash.put("anexo", validar.validarDato(consulta1.getString("anexo"), "NR"));
            simplehash.put("mail", validar.validarDato(consulta1.getString("e_mail"), "NR"));
            simplehash.put("unidad", consulta1.getString("unidad"));
            simplehash.put("centro_costo", consulta1.getString("centro_costo"));
            simplehash.put("id_unidad", consulta1.getString("id_unidad"));
            simplehash.put("area", consulta1.getString("area"));
            simplehash.put("division", consulta1.getString("division"));
            Consulta consulta = menumanager.getLicenciasMedicas(rut);
            SimpleList simplelist = new SimpleList();
            SimpleHash simplehash1;
            for(; consulta.next(); simplelist.add(simplehash1)) {
                simplehash1 = new SimpleHash();
                simplehash1.put("inicio", validar.validarFecha(consulta.getValor("fecha_inicio")));
                simplehash1.put("termino", validar.validarFecha(consulta.getValor("fecha_termino")));
                simplehash1.put("id_tipo_licencia", consulta.getString("id_tipo_licencia"));
                simplehash1.put("tipo_licencia", consulta.getString("tipo_licencia"));
                simplehash1.put("diagnostico", consulta.getString("diagnostico"));
                simplehash1.put("dias", consulta.getString("dias"));
                simplehash1.put("grupo", consulta.getString("grupo_enfermedad"));
                simplehash1.put("rut_profesional", consulta.getString("rut_profesional"));
                simplehash1.put("profesional", consulta.getString("profesional"));
                simplehash1.put("espe_profesional", consulta.getString("espe_profesional"));
            }

            simplehash.put("licencias", simplelist);
            consulta.close();
        }
        consulta1.close();
        return simplehash;
	}

	public SimpleHash getMuestraInfoRut(String rut) {
		Connection conn = null;

		try {
			conn = DBConnectionManager.getInstance().getConnection("portal");
			return getMuestraInfoRut(conn,rut);
		}
		catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(conn != null) {
				DBConnectionManager.getInstance().freeConnection("portal",conn);
			}
		}
		
		return new SimpleHash();
	}
	public SimpleHash getMuestraInfoRut(Connection connection,String rut) {
        SimpleHash simplehash = new SimpleHash();
        String s3 = null;
        Validar validar = new Validar();
        menuManager menumanager = new menuManager(connection);
        Consulta consulta = menumanager.getDatos(rut);
        consulta.next();
        String cargas_normales = validar.validarDato(consulta.getString("wp_num_cargas_normale"), "0");
        String cargas_duplo = validar.validarDato(consulta.getString("wp_num_cargas_duplo"), "0");
        String cargas_materna = validar.validarDato(consulta.getString("wp_num_cargas_materna"), "0");
        s3 = String.valueOf(Integer.parseInt(cargas_normales) + Integer.parseInt(cargas_duplo) + Integer.parseInt(cargas_materna));
        consulta.close();
        OutMessage.OutMessagePrint("\n\nCargas : " + s3);
        ConsultaData consulta1 = menumanager.getInfo(rut);
        if(consulta1.next()) {
            String s1 = consulta1.getString("digito_ver");
            simplehash.put("rut", rut);
            
            String rutToHash = Tools.setFormatNumber(rut);
            if(s1 != null && !"".equals(s1)) {
            	rutToHash += "-" + s1;
            }
            
            simplehash.put("rut2",  rutToHash);
            simplehash.put("nombre", consulta1.getString("nombre"));
            simplehash.put("afp", validar.validarDato(consulta1.getString("afp"), "No tiene"));
            simplehash.put("isapre", validar.validarDato(consulta1.getString("isapre"), "No tiene"));
            simplehash.put("fec_nac", validar.validarFecha(consulta1.getDateJava("fecha_nacim")));
            simplehash.put("est_civil", validar.validarDato(consulta1.getString("estado_civil"), "No tiene"));
            simplehash.put("fono", validar.validarDato(consulta1.getString("telefono"), "NR"));
            simplehash.put("nacionalidad", validar.validarDato(consulta1.getString("nacionalidad"), "No tiene"));
            String s2 = validar.validarDato(consulta1.getInt("Xedad"), "0");
            int i = Integer.parseInt(s2) / 12;
            if(i < 0)
                i *= -1;
            
            
            String fecNac = validar.validarFecha(consulta1.getDateJava("fecha_nacim"),null);
            if(fecNac != null ){
            	
            	try {
                	Date dateBorn  = Formatear.getInstance().toDate(fecNac, "dd/MM/yyyy");
                	simplehash.put("edad",  String.valueOf(getAge(dateBorn))  );
            	}
            	catch (Exception e) {
            		e.printStackTrace();
            	}
            	
            	
            }
            else {
                simplehash.put("edad", "-");
            }
            
            
            simplehash.put("numgrupofam", validar.validarDato(menumanager.getNumCargasInt(rut), "0"));
            simplehash.put("numcargas", validar.validarDato(s3, "0"));
            simplehash.put("sangre", validar.validarDato(consulta1.getString("grupo_sangre"), "No tiene"));
            simplehash.put("domicilio", validar.validarDato(consulta1.getString("domicilio"), "No tiene"));
            
            /*FROM 2016-04-04*/
            try {
            	
            	{
		            String sqlSelect = "select descrip from eje_ges_comunas where comuna = ? ";
		            Object[] params = {validar.validarDato(consulta1.getString("comuna"), "0")};
		            
		            ConsultaData data = ConsultaTool.getInstance().getData("portal", sqlSelect, params);
					
		            if(data != null && data.next()) {
		            	simplehash.put("comuna_str", data.getString("descrip"));
		            }
	            }
	            
	            {
		            String sqlSelect = "select descrip from eje_ges_ciudades where ciudad = ? ";
		            Object[] params = {validar.validarNum(consulta1.getString("ciudad"), 0)};
		            
		            ConsultaData data = ConsultaTool.getInstance().getData("portal", sqlSelect, params);
					
		            if(data != null && data.next()) {
		            	simplehash.put("ciudad_str", data.getString("descrip"));
		            }
	            }
	            
	            {
		            String sqlSelect = "select descrip from eje_ges_regiones where region = ? ";
		            Object[] params = {validar.validarDato(consulta1.getString("region"), "0")};
		            
		            ConsultaData data = ConsultaTool.getInstance().getData("portal", sqlSelect, params);
					
		            if(data != null && data.next()) {
		            	simplehash.put("region_str", data.getString("descrip"));
		            }
	            }
            } catch (SQLException e) {
				e.printStackTrace();
			}
            /* END FROM*/
            
            simplehash.put("comuna", validar.validarDato(consulta1.getString("comuna"), "No tiene"));
            simplehash.put("ciudad", validar.validarDato(consulta1.getString("ciudad"), "No tiene"));
            simplehash.put("region", validar.validarDato(consulta1.getString("region"), "No tiene"));
            
            simplehash.put("foto", consulta1.getString("id_foto"));
            simplehash.put("cargo", consulta1.getString("cargo"));
            simplehash.put("unidad", consulta1.getString("unidad"));
            simplehash.put("id_unidad", consulta1.getString("id_unidad"));

            String genero = consulta1.getString("sexo").toUpperCase();
            if(genero.equals("F"))
                genero = "Femenino";
            else
            	if(genero.equals("M"))
            		genero = "Masculino";
            simplehash.put("sexo", genero);
            simplehash.put("empresa", consulta1.getString("nom_empresa"));
            simplehash.put("tipo_contrato", consulta1.getString("desc_tip_contrato"));
            simplehash.put("moneda_sueldo_pactado", consulta1.getString("moneda_sueldo_base_mensual"));
            simplehash.put("sueldo_pactado", String.valueOf( consulta1.getBigDecimal("sueldo_base_mensual"))  );
            simplehash.put("cta_cte", consulta1.getString("cta_cte"));
            simplehash.put("jornada", consulta1.getString("tip_jor_lab"));
            simplehash.put("fecha_contrato", validar.validarFecha(consulta1.getDateJava("fecha_ingreso")));
            simplehash.put("antiguedad", validar.validarFecha(consulta1.getInt("antiguedad")));
            simplehash.put("email", validar.validarDato(consulta1.getString("e_mail"), "No tiene"));
            simplehash.put("anexo", validar.validarDato(consulta1.getString("anexo"), "No tiene"));
            
            /* ADD 4 Abril 2012 */
            
            simplehash.put("fecha_ingreso", validar.validarFecha(consulta1.getDateJava("fecha_ingreso")));
            simplehash.put("fec_ing_hold", validar.validarFecha(consulta1.getDateJava("inghold")));
        } 
        else {
            simplehash.put("error", "0");
            OutMessage.OutMessagePrint("---->Rut no encontrado!!!!");
        }

		return simplehash;
	}


	public int getAge(Date bornAge) {

	    Calendar today = Calendar.getInstance();
	    
	    int anios = 0 ;
	    while(today.getTimeInMillis() > bornAge.getTime()) {
	    	today.add(Calendar.YEAR,-1);
	    	
	    	if(today.getTimeInMillis() > bornAge.getTime()) {
	    		anios++;
	    	}
	    }
	    
	    
	    return anios;
	}
	
	public SimpleHash getAdmTiempo(Connection connection,String rut) {
        Validar validar = new Validar();
        SimpleHash simplehash = new SimpleHash();
        menuManager menumanager = new menuManager(connection);
        Consulta consulta = menumanager.getHorasExtras(rut);
        if(consulta.next()) {
            simplehash.put("nombre", consulta.getString("nombre"));
            simplehash.put("cargo", consulta.getString("cargo"));
            simplehash.put("sbase", Tools.setFormatNumber(validar.validarDato(consulta.getString("sueldo"), "0")));
            simplehash.put("thaber", Tools.setFormatNumber(validar.validarDato(consulta.getString("tothaber"), "0")));
            try {
                float f = (100F * consulta.getFloat("valor")) / consulta.getFloat("tothaber");
                String s1 = String.valueOf(f);
                int i = s1.indexOf(".");
                s1 = s1.substring(0, i + 2);
                simplehash.put("impacto", s1);
            }
            catch(ArithmeticException arithmeticexception) {
                System.out.println("Excepcion, enc.: " + arithmeticexception.getMessage());
            }
            simplehash.put("hrsex", Tools.setFormatNumber(validar.validarDato(consulta.getString("nhoras"), "0")));
            simplehash.put("minex", Tools.setFormatNumber(validar.validarDato(consulta.getString("nmin"), "0")));
            simplehash.put("mtoactual", Tools.setFormatNumber(validar.validarDato(consulta.getString("valor"), "0")));
            simplehash.put("foto", consulta.getString("foto"));
            simplehash.put("mail", validar.validarDato(consulta.getString("e_mail"), "NR"));
            simplehash.put("anexo", validar.validarDato(consulta.getString("anexo"), "NR"));
            simplehash.put("rut", rut);
            simplehash.put("rut2", Tools.setFormatNumber(rut) + "-" + consulta.getString("digito_ver"));
            simplehash.put("unidad", consulta.getString("unidad"));
            Consulta consulta1 = menumanager.getDeudas(rut);
            SimpleList simplelist = new SimpleList();
            SimpleHash simplehash1;
            for(; consulta1.next(); simplelist.add(simplehash1)) {
                simplehash1 = new SimpleHash();
                simplehash1.put("periodo", consulta1.getString("periodo"));
                simplehash1.put("ano", consulta1.getString("peri_ano"));
                simplehash1.put("mes", consulta1.getString("peri_mes"));
                simplehash1.put("nhrs", Tools.setFormatNumber(consulta1.getInt("nhoras")));
                simplehash1.put("nmin", Tools.setFormatNumber(consulta1.getInt("nmin")));
                simplehash1.put("mto", Tools.setFormatNumber(consulta1.getInt("valor")));
                simplehash1.put("tipo", validar.validarDato(consulta1.getString("tip"), "N/R"));
                simplehash1.put("tothab", Tools.setFormatNumber(consulta1.getInt("tothaber")));
                simplehash1.put("sbases", Tools.setFormatNumber(consulta1.getInt("sueldo")));
                try {
                    float f1 = (100F * consulta1.getFloat("valor")) / consulta1.getFloat("tothaber");
                    String s2 = String.valueOf(f1);
                    int j = s2.indexOf(".");
                    s2 = s2.substring(0, j + 3);
                    simplehash1.put("efec", s2);
                }
                catch(ArithmeticException arithmeticexception1) {
                    System.out.println("Excepcion, det.: " + arithmeticexception1.getMessage());
                }
            }

            simplehash.put("horas", simplelist);
            consulta1.close();
        } 
        else {
            simplehash.put("error", "0");
            System.out.println("---->Rut no encontrado!!!!");
        }
        consulta.close();
        return simplehash;
	}

	public SimpleHash getVacaciones(Connection connection,Usuario user,String rut) {
        SimpleHash modelRoot = new SimpleHash();
        Validar valida = new Validar();
        Vaca_Manager cartola = new Vaca_Manager(connection);
        Consulta info = cartola.GetCabeceraCartola(rut);
        String diasNormales = "";
        if(info.next()) {
        	String rutToHash = Tools.setFormatNumber(info.getString("rut"));
        	if(info.getString("digito_ver") != null && !"".equals(info.getString("digito_ver"))) {
        		rutToHash += "-" + info.getString("digito_ver");
        	}
        	modelRoot.put("rut_trab", rutToHash );
	        modelRoot.put("rut", info.getString("rut"));
	        modelRoot.put("nombre", info.getString("nombre"));
	        modelRoot.put("cargo", info.getString("cargo"));
	        modelRoot.put("fec_ingreso", valida.validarFecha(info.getValor("fecha_ingreso")));
	        modelRoot.put("anos", valida.validarDato(info.getString("recono"), "0"));
	        modelRoot.put("fec", valida.validarFecha(info.getValor("fecrecono")));
	        modelRoot.put("anexo", valida.validarDato(info.getString("anexo"), "NR"));
	        modelRoot.put("mail", valida.validarDato(info.getString("e_mail"), "NR"));
	        modelRoot.put("hoy", valida.validarDato((new SimpleDateFormat("dd/MM/yyyy")).format(new Date()), "1-1-1900"));
	        modelRoot.put("foto", info.getString("id_foto"));
        }
        info.close();
        SimpleList simplelist = new SimpleList();
        Consulta dias = cartola.GetCabeceraCartolaWp(rut);
        Consulta diasD = cartola.GetCabeceraVacacionesDev(rut);
        float saldo2 = 0.0F;
        float dias_devengados = 0.0F;
        try {
            if(diasD.next())
                dias_devengados = diasD.getFloat("dias_devengados");
        }
        catch(Exception e) {
            dias_devengados = 0.0F;
        }
        float saldo = dias_devengados;
        if(dias.next()) {
        	saldo += dias.getFloat("dias_pendientes") + dias.getFloat("dias_del_periodo");
        	saldo2 = dias.getFloat("saldo");
        }
        double d = saldo;
        d= d*100;
        Float f = new Float(d);
        d = f.intValue();
        d = d/100;
        ResourceBundle properwfv = ResourceBundle.getBundle("vacaciones");
		String origen = properwfv.getString("wfvac.source.saldos");
		if("payroll".equals(origen)) {
			d= Double.parseDouble( getSaldoTotalUsuario(rut) );
		}
        modelRoot.put("saldo", String.valueOf(d));
        modelRoot.put("saldoEntero", Tools.setRedondearStr(String.valueOf(d),0));
        OutMessage.OutMessagePrint(String.valueOf(saldo));
        Consulta vacaciones = cartola.GetDetalleCartolaNewWp(rut);
        int corr = 0;
        SimpleHash simplehash1;
        for(; vacaciones.next(); simplelist.add(simplehash1)) {
            simplehash1 = new SimpleHash();
            simplehash1.put("corr", String.valueOf(++corr));
            simplehash1.put("desde", valida.validarFecha(vacaciones.getValor("desde"), "1-1-1900"));
            simplehash1.put("hasta", valida.validarFecha(vacaciones.getValor("hasta"), "1-1-1900"));
            diasNormales = valida.validarDato(vacaciones.getString("dias_normales"), "0");
        	String subStr	= diasNormales.substring(0,diasNormales.indexOf('.'));
        	if ( subStr.length() == 1) {
        		subStr = "0"+subStr;
        	}
            simplehash1.put("dias",subStr );
        }
        modelRoot.put("vacaciones", simplelist);
        vacaciones.close();
        modelRoot.put("Control", new ControlAccesoTM(new ControlAcceso(user)));
		return modelRoot;
	}
	
	public String getSaldoTotalUsuario(String p_rut) {
		System.out.println("[AdministracionManager][getSaldoTotalUsuario]Entrando...");
		String salida = "0";
		try {
			ResourceBundle properwfv = ResourceBundle.getBundle("vacaciones");
			String origen = properwfv.getString("wfvac.source.saldos");
			IDBConnectionManager dbConnectionMgr = DBConnectionManager.getInstance();
	    	Connection conn = dbConnectionMgr.getConnection(origen);
	    	Consulta consulta = new Consulta(conn);
			StringBuilder sql = new StringBuilder("select saldo from eje_vac_trabajador_cuenta_auxiliar ")
				.append("where rut=").append(p_rut);
			if("payroll".equals(origen)) {
        		sql = new StringBuilder("select v.Totpen saldo from RVACDET v ")
        				.append("inner join remples e on v.Codigo=e.Codigo where e.Rut like '%")
        				.append(p_rut).append("%' and e.Estado in ('A','X') ");
        	}
			consulta.exec(String.valueOf(sql));
			if (consulta.next()) {
				salida = consulta.getString("saldo");
			}
			consulta.close();
			dbConnectionMgr.freeConnection(origen, conn);
			System.out.println("[AdministracionManager][getSaldoTotalUsuario]Saliendo...");
		}
		catch(Exception e) {
			System.out.println("[AdministracionManager][getSaldoTotalUsuario]Exception...");
			salida = "";
		}
		return salida;
	}
	
	public SimpleHash getDatosLiquidacion(Connection connection,String path, String periodo, int opcion,String rut, HttpServletRequest req) {
        String digito = "";
        String strFechaHoy = "";
        GregorianCalendar Fecha = new GregorianCalendar();
        int dia = Fecha.get(5);
        int ano = Fecha.get(1);
        int mes = Fecha.get(2) + 1;
        SimpleHash modelRoot = new SimpleHash();
        String causa_pago = "";
        Liquida_Manager data_rut = new Liquida_Manager(connection);
        Consulta info = data_rut.GetCabecera(rut, periodo);
        Consulta emp = data_rut.GetEmpresa(rut);
        Consulta soc = data_rut.GetSociedad(rut);
        Consulta uni = data_rut.GetUnidad(rut);
        Consulta cc = data_rut.GetCentroCosto(rut);
        Consulta afp = data_rut.GetAfp(rut);
        Consulta isapre = data_rut.GetIsapre(rut);
        if(info.next()) {
            strFechaHoy = String.valueOf(String.valueOf((new StringBuilder("")).append(dia).append(" de ").append(Tools.RescataMes(mes)).append(" de ").append(ano)));
            modelRoot.put("fecha_periodo", Tools.RescataMes(Integer.parseInt(periodo.substring(4, 6))) + " " + periodo.substring(0, 4));
            modelRoot.put("fecha", strFechaHoy);
            modelRoot.put("empresa", info.getString("nom_empre"));
            digito = info.getString("digito_ver");
            modelRoot.put("rut_trab", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(Integer.parseInt(rut.trim())))))).append("-").append(digito))));
            modelRoot.put("nombre", info.getString("nombre"));
            modelRoot.put("cargo", info.getString("nom_cargo"));
            modelRoot.put("ecivil", info.getString("estado_civil"));
            modelRoot.put("sexo", info.getString("sexo").toUpperCase());
            emp.next();
            soc.next();
            uni.next();
            cc.next();
            isapre.next();
            afp.next();
            modelRoot.put("nom_empresa", emp.getString("nom_empresa"));
            modelRoot.put("dias", info.getString("ndias_trab"));
            modelRoot.put("nom_sociedad", soc.getString("nom_sociedad"));
            modelRoot.put("nom_unidad", uni.getString("nom_unidad"));
            modelRoot.put("centro_costo", cc.getString("nom_centro_costo"));
            modelRoot.put("isapre", isapre.getString("nom_isapre"));
            modelRoot.put("afp", afp.getString("nom_afp"));
            modelRoot.put("ingreso", Tools.RescataFecha(info.getValor("fecha_ingreso")));
            causa_pago = info.getString("causa_pago");
            modelRoot.put("mod_pago", info.getString("forma_pago"));
            modelRoot.put("cta", info.getString("cuenta"));
            modelRoot.put("banco", info.getString("banco"));
            modelRoot.put("i_t", Tools.setFormatNumber(info.getString("imp_tribut")));
            modelRoot.put("i_nt", Tools.setFormatNumber(info.getString("imp_no_tribut")));
            modelRoot.put("ni_t", Tools.setFormatNumber(info.getString("no_imp_tribut")));
            modelRoot.put("ni_nt", Tools.setFormatNumber(info.getString("no_imp_no_tribut")));
            modelRoot.put("tot_haberes", Tools.setFormatNumber(info.getString("tot_haberes")));
            modelRoot.put("d_varios", Tools.setFormatNumber(info.getString("dctos_varios")));
            modelRoot.put("d_legales", Tools.setFormatNumber(info.getString("dctos_legales")));
            modelRoot.put("tot_destos", Tools.setFormatNumber(info.getString("tot_desctos")));
            modelRoot.put("liquido", Tools.setFormatNumber(info.getString("liquido")));
            String it = "0";
            String intr = "0";
            String reliqt = "0";
            int rta = 0;
            String rta_impon = "";
            if(info.getString("imp_tribut") != null)
                it = info.getString("imp_tribut");
            if(info.getString("imp_no_tribut") != null)
                intr = info.getString("imp_no_tribut");
            if(info.getString("reliq_rentas") != null)
                reliqt = info.getString("reliq_rentas");
            System.err.println(String.valueOf(String.valueOf((new StringBuilder("IT= ")).append(it).append(" --->INTR= ").append(intr))));
            modelRoot.put("renta_impon", Tools.setFormatNumber(info.getString("rta_impon")));
            modelRoot.put("tope_renta", Tools.setFormatNumber(info.getString("tope_imp")));
            modelRoot.put("val_uf", Tools.setFormatNumberFloat(info.getString("val_uf")));
            modelRoot.put("SubQuery", new SubQuery(connection, req, path));
            modelRoot.put("FNum", new FormatoNumero());
            String haberes = "";
            String descuentos = "";
            haberes = String.valueOf(String.valueOf((new StringBuilder("SELECT periodo, empresa, unidad,rut, tip_unidad, id_tp, glosa_haber, val_haber, orden, wp_cod_planta FROM view_liquidacion_haberes haberes WHERE (wp_indic_papeleta = 'S') AND (periodo = ")).append(periodo).append(") ").append("AND (empresa = '").append(data_rut.getEmpresa()).append("') ").append("AND (rut = ").append(rut).append(") ").append("ORDER BY rut, orden")));
            OutMessage.OutMessagePrint("---->haberes : " + haberes);
            modelRoot.put("haberes", haberes);
            descuentos = String.valueOf(String.valueOf((new StringBuilder("SELECT periodo, empresa, unidad,rut, tip_unidad, id_tp, orden, glosa_descuento,val_descuento, wp_cod_planta FROM view_liquidacion_descuentos descuentos WHERE (wp_indic_papeleta = 'S') AND (periodo = ")).append(periodo).append(") ").append("AND (empresa = '").append(data_rut.getEmpresa()).append("') ").append("AND (rut = ").append(rut).append(") ").append("ORDER BY rut, orden")));
            OutMessage.OutMessagePrint("---->descuentos : " + descuentos);
            modelRoot.put("descuentos", descuentos);
            modelRoot.put("uri", "certificado=" + opcion + "&peri_liq=" + periodo + "");
            modelRoot.put("certificado", String.valueOf(opcion));
            modelRoot.put("peri_liq", periodo);
        } 
        else {
            modelRoot.put("error", "0");
            OutMessage.OutMessagePrint("---->Rut no encontrado!!!!");
        }
        info.close();
        uni.close();
        soc.close();
        uni.close();
        cc.close();
        afp.close();
        isapre.close();
		return modelRoot;
	}

	public SimpleHash getDatosLiquidacionAdic(Connection Conexion, String path, int opcion,String periId, String rut, HttpServletRequest req) {
  	    String consulta = null;
  	    String digito = "";
  	    String strFechaHoy = "";
  	    String genero = "";
  	    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMMM dd,yyyy");
  	    GregorianCalendar Fecha = new GregorianCalendar();
  	    int dia = Fecha.get(5);
  	    int ano = Fecha.get(1);
  	    int mes = Fecha.get(2) + 1;
  	    
  	    SimpleList simplelist = new SimpleList();
  	    Validar valida = new Validar();
  	    String causa_pago = "";
        String aux = periId;
        String periodo = aux.substring(0,aux.indexOf("|"));
        String tipo_proceso = aux.substring(aux.indexOf("|") + 1,aux.length());

  	    Liquida_Manager liquidaManager = new Liquida_Manager(Conexion);
  	    
  	    SimpleList lista = new SimpleList();
  	    SimpleHash hash = new SimpleHash();
  	    
  	    
  	    Consulta info = liquidaManager.GetCabeceraAdicional(rut, periodo,tipo_proceso);  	    
  	    Consulta emp = liquidaManager.GetEmpresa(rut);
  	    Consulta soc = liquidaManager.GetSociedad(rut);
  	    Consulta uni = liquidaManager.GetUnidad(rut);
  	    Consulta cc = liquidaManager.GetCentroCosto(rut);
  	    Consulta afp = liquidaManager.GetAfp(rut);
  	    Consulta isapre = liquidaManager.GetIsapre(rut);
  	    if (info.next()) {
  	    	strFechaHoy = String.valueOf(String.valueOf((new StringBuilder("")).append(dia).append(" de ").append(Tools.RescataMes(mes)).append(" de ").append(ano)));
  	    	
  	    	hash.put("fecha_periodo", Tools.RescataMes(Integer.parseInt(periodo.substring(4, 6))) + " " + periodo.substring(0, 4));
  	    	hash.put("fecha", strFechaHoy);
  	    	hash.put("empresa", info.getString("nom_empre"));
  	    	digito = info.getString("digito_ver");
  	    	hash.put("rut_trab", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(Integer.parseInt(rut.trim())))))).append("-").append(digito))));
  	    	hash.put("nombre", info.getString("nombre"));
  	    	hash.put("cargo", info.getString("nom_cargo"));
  	    	hash.put("ecivil", info.getString("estado_civil"));
  	    	hash.put("sexo", info.getString("sexo").toUpperCase());
  	    	emp.next();
  	    	soc.next();
  	    	uni.next();
  	    	cc.next();
  	    	isapre.next();
  	    	afp.next();
  	    	hash.put("nom_empresa", emp.getString("nom_empresa"));
  	    	hash.put("dias", info.getString("ndias_trab"));
  	    	hash.put("nom_sociedad", soc.getString("nom_sociedad"));
  	    	hash.put("nom_unidad", uni.getString("nom_unidad"));
  	    	hash.put("centro_costo", cc.getString("nom_centro_costo"));
  	    	hash.put("isapre", isapre.getString("nom_isapre"));
  	    	hash.put("afp", afp.getString("nom_afp"));
  	    	hash.put("ingreso", Tools.RescataFecha(info.getValor("fecha_ingreso")));
  	    	causa_pago = info.getString("causa_pago");
  	    	hash.put("mod_pago", info.getString("forma_pago"));
  	    	hash.put("cta", info.getString("cuenta"));
  	    	hash.put("banco", info.getString("banco"));
  	    	hash.put("i_t", Tools.setFormatNumber(info.getString("imp_tribut")));
  	    	hash.put("i_nt", Tools.setFormatNumber(info.getString("imp_no_tribut")));
  	    	hash.put("ni_t", Tools.setFormatNumber(info.getString("no_imp_tribut")));
  	    	hash.put("ni_nt", Tools.setFormatNumber(info.getString("no_imp_no_tribut")));
  	    	hash.put("tot_haberes", Tools.setFormatNumber(info.getString("tot_haberes")));
  	    	hash.put("d_varios", Tools.setFormatNumber(info.getString("dctos_varios")));
  	    	hash.put("d_legales", Tools.setFormatNumber(info.getString("dctos_legales")));
  	    	hash.put("tot_destos", Tools.setFormatNumber(info.getString("tot_desctos")));
  	    	hash.put("liquido", Tools.setFormatNumber(info.getString("liquido")));
  	    	String it = "0";
  	    	String intr = "0";
  	    	String reliqt = "0";
  	    	int rta = 0;
  	    	String rta_impon = "";
  	    	if (info.getString("imp_tribut") != null) 
  	    		it = info.getString("imp_tribut");
  	    	if (info.getString("imp_no_tribut") != null) 
  	    		intr = info.getString("imp_no_tribut");
  	    	if (info.getString("reliq_rentas") != null) 
  	    		reliqt = info.getString("reliq_rentas");
  	    	System.err.println(String.valueOf(String.valueOf((new StringBuilder("IT= ")).append(it).append(" --->INTR= ").append(intr))));
  	    	hash.put("renta_impon", Tools.setFormatNumber(info.getString("rta_impon")));
  	    	hash.put("tope_renta", Tools.setFormatNumber(info.getString("tope_imp")));
  	    	hash.put("val_uf", Tools.setFormatNumberFloat(info.getString("val_uf")));
  	    	hash.put("SubQuery", new SubQuery(Conexion, req, path));
  	    	hash.put("FNum", new FormatoNumero());
  	    	String haberes = "";
  	    	String descuentos = "";
  	    	haberes = String.valueOf(String.valueOf((new StringBuilder("SELECT periodo, empresa, unidad,rut, tip_unidad, id_tp, glosa_haber, val_haber, orden, wp_cod_planta FROM view_liquidacion_haberes_adic haberes WHERE (wp_indic_papeleta = 'S') AND (periodo = ")).append(periodo).append(") ").append("AND (empresa = '").append(liquidaManager.getEmpresa()).append("') ").append("AND (rut = ").append(rut).append(") AND (tipo_proceso = '").append(tipo_proceso).append("') ORDER BY rut, orden")));
  	    	OutMessage.OutMessagePrint("---->haberes : " + haberes);
  	    	hash.put("haberes", haberes);
            descuentos = String.valueOf(String.valueOf((new StringBuilder("SELECT periodo, empresa, unidad,rut, tip_unidad, id_tp, orden, glosa_descuento,val_descuento, wp_cod_planta FROM view_liquidacion_descuentos_adic descuentos WHERE (wp_indic_papeleta = 'S') AND (periodo = ")).append(periodo).append(") ").append("AND (empresa = '").append(liquidaManager.getEmpresa()).append("') ").append("AND (rut = ").append(rut).append(") AND (tipo_proceso = '").append(tipo_proceso).append("') ORDER BY rut, orden")));
            OutMessage.OutMessagePrint("---->descuentos : " + descuentos);
            hash.put("descuentos", descuentos);
            hash.put("uri", "certificado=" + opcion + "&peri_liq=" + periodo + "");
            hash.put("certificado", String.valueOf(opcion));
            hash.put("peri_liq", periodo);
  	    }
  	    else {
  	    	hash.put("error", "0");
  	    	OutMessage.OutMessagePrint("---->Rut no encontrado!!!!");
  	    }
  	    info.close();
  	    uni.close();
  	    soc.close();
  	    uni.close();
  	    cc.close();
  	    afp.close();
  	    isapre.close();
  	    
  	    lista.add(hash);
  	    SimpleHash modelRoot = new SimpleHash();
  	    
  	    modelRoot.put("liquidaciones",lista);
  	    
  	    return modelRoot;
	}
	
	
	
    public SimpleHash getDatosViaje(Connection conexion, int certif, 
    				  		String desdedia, String desdemes, String desdeyear, 
    				  		String hastadia, String hastames, String hastayear, 
    				  		String dr, String mr, String yr, 
    				  		String dest, String ciudad, String rut) {
        String consulta = null;
        String digito = "";
        String id = "";
        String strFechaHoy = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMMM dd,yyyy");
        GregorianCalendar Fecha = new GregorianCalendar();
        int dia = Fecha.get(5);
        int ano = Fecha.get(1);
        int mes = Fecha.get(2) + 1;
        SimpleHash modelRoot = new SimpleHash();
        String antiguedad = null;
        String edad = null;
        String strFechaIngre = "";
        String desde = String.valueOf(new StringBuilder(desdedia).append(" de ").append(Tools.RescataMes(Integer.parseInt(desdemes))).append(" de ").append(desdeyear));
        String hasta = String.valueOf(new StringBuilder(hastadia).append(" de ").append(Tools.RescataMes(Integer.parseInt(hastames))).append(" de ").append(hastayear));
        String reintegro = String.valueOf((new StringBuilder(dr).append(" de ").append(Tools.RescataMes(Integer.parseInt(mr))).append(" de ").append(yr)));
        int destino = Integer.parseInt(dest);
        Vector paises = new Vector();
        paises.add(0, "NN");
        paises.add(1, "Argentina");
        paises.add(2, "Bolivia");
        paises.add(3, "Brasil");
        paises.add(4, "Chile");
        paises.add(5, "Colombia");
        paises.add(6, "Costa Rica");
        paises.add(7, "Cuba");
        paises.add(8, "Ecuador");
        paises.add(9, "El Salvador");
        paises.add(10, "Espa\361a");
        paises.add(11, "Estados Unidos");
        paises.add(12, "Guatemala");
        paises.add(13, "Honduras");
        paises.add(14, "M\351xico");
        paises.add(15, "Nicaragua");
        paises.add(16, "Panam\341");
        paises.add(17, "Paraguay");
        paises.add(18, "Per\372");
        paises.add(19, "Portugal");
        paises.add(20, "Puerto Rico");
        paises.add(21, "Rep. Dominicana");
        paises.add(22, "Uruguay");
        paises.add(23, "Venezuela");
        paises.add(24, "Canad\341");
        paises.add(25, "Francia");
        paises.add(26, "Finlandia");
        paises.add(27, "Grecia");
        paises.add(28, "Holanda");
        paises.add(29, "Irlanda");
        paises.add(30, "Italia");
        paises.add(31, "Jap\363n");
        paises.add(32, "Noruega");
        paises.add(33, "Otros");
        paises.add(34, "Reino Unido");
        paises.add(35, "Rumania");
        paises.add(36, "Rusia");
        paises.add(37, "Suiza");
        paises.add(38, "Taiw\341n");
        paises.add(39, "Yugoslavia");
        String pais = (String)paises.elementAt(destino);
        Validar valida = new Validar();
        String paterno = "";
        Certif_Manager data_cert = new Certif_Manager(conexion);
        Consulta info = data_cert.GetApePaterno(rut);
        if(info.next())
        	paterno = info.getString("ape_paterno");
        info = data_cert.GetDataRutViaje(rut);
        valida.setFormatoFecha("dd-MM-yyyy");
        if(info.next()) {
        	modelRoot.put("titulo", "CERTIFICADO DE VIAJE.");
        	String empresa = info.getString("empresa");
        	boolean se_puede = true;
        	if(certif == 17 && !empresa.equals("8")) {
            modelRoot.put("denegado", "1");
            se_puede = false;
        }
        if(se_puede) {
            Certif_Manager data_certificado = new Certif_Manager(conexion);
            int num_certif = data_certificado.GetNumCertificado(ano);
            modelRoot.put("num", String.valueOf(num_certif));
            modelRoot.put("anio", String.valueOf(ano));
        }
        strFechaIngre = Tools.RescataFecha(info.getValor("fecha_ingreso"));
        OutMessage.OutMessagePrint("-->Ingreso: ".concat(String.valueOf(String.valueOf(strFechaIngre))));
        modelRoot.put("ingreso", strFechaIngre);
        modelRoot.put("empresa", info.getString("glosa_empresa"));
        modelRoot.put("paterno", paterno);
        modelRoot.put("cargo", valida.validarDato(info.getString("cargo"), "N/R"));
        modelRoot.put("sueldo", Tools.setFormatNumber(valida.validarDato(info.getString("renta_reg_mensual"), "0")));
        digito = info.getString("digito_ver");
        modelRoot.put("rut", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(Integer.parseInt(rut)))))).append("-").append(digito))));
        modelRoot.put("nombre", valida.validarDato(info.getString("nombre")));
        strFechaHoy = String.valueOf(String.valueOf((new StringBuilder("Santiago, ")).append(dia).append(" de ").append(Tools.RescataMes(mes)).append(" de ").append(ano)));
        modelRoot.put("fecha", strFechaHoy);
        modelRoot.put("inicio", desde);
        modelRoot.put("termino", hasta);
        modelRoot.put("reintegro", reintegro);
        modelRoot.put("destino", pais);
        modelRoot.put("ciudad", ciudad);
        } 
        else {
        	modelRoot.put("error", "0");
        	OutMessage.OutMessagePrint("---->Rut no encontrado!!!!");
        }
        strFechaHoy = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(dia)))).append(" de ").append(Tools.RescataMes(mes)).append(" de ").append(ano)));
        info.close();
        return modelRoot;
    }

    public SimpleHash getDatosPractica(Connection Conexion, String rut) {
    	String consulta = null;
        String id = "";
        String digito = "";
        String strFechaHoy = "";
        GregorianCalendar Fecha = new GregorianCalendar();
        int dia = Fecha.get(5);
        int ano = Fecha.get(1);
        int mes = Fecha.get(2) + 1;
        boolean es = false;
        SimpleHash modelRoot = new SimpleHash();
        Date hoy = Fecha.getTime();
        Validar valida = new Validar();
        Certif_Manager data_cert = new Certif_Manager(Conexion);
        Consulta info = data_cert.GetPracticantes(rut);
        if(info.next()) {
            OutMessage.OutMessagePrint("-->Practicante");
            if(info.getValor("fec_ter_cont") != null) {
                OutMessage.OutMessagePrint("-->Fecha termino no nula");
                es = ((Date)info.getValor("fec_ter_cont")).before(hoy);
                if(es) {
                    OutMessage.OutMessagePrint("-->Fecha termino no nula--->Practicante valido");
                    modelRoot.put("nombre", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(valida.validarDato(info.getString("nombres")))))).append(valida.validarDato(info.getString("ape_paterno"))).append(valida.validarDato(info.getString("ape_materno"))))));
                    modelRoot.put("desde", valida.validarFecha(info.getString("fecha_ingreso")));
                    modelRoot.put("hasta", valida.validarFecha(info.getString("fec_ter_cont")));
                    modelRoot.put("area", info.getString("area"));
                    digito = info.getString("digito_ver");
                    modelRoot.put("rut", rut);
                    strFechaHoy = String.valueOf(String.valueOf((new StringBuilder("Santiago, ")).append(dia).append(" de ").append(Tools.RescataMes(mes)).append(" de ").append(ano)));
                    modelRoot.put("fecha", strFechaHoy);
                    strFechaHoy = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(dia)))).append(" de ").append(Tools.RescataMes(mes)).append(" de ").append(ano)));
                } 
                else {
                    modelRoot.put("valor", "1");
                    modelRoot.put("hasta", valida.validarFecha(info.getString("fec_ter_cont")));
                    OutMessage.OutMessagePrint("-->Aun no termina practica,fec termino no nula!!");
                }
            } 
            else {
                modelRoot.put("valor", "0");
                OutMessage.OutMessagePrint("-->Aun no termina practica");
            }
        } 
        else {
        	OutMessage.OutMessagePrint("-->No es Practicante");
        }
        info.close();
        return modelRoot;
    }

    public SimpleHash getDatosCursos(Connection Conexion, String rut) {
    	String consulta = null;
    	String digito = "";
    	String strFechaHoy = "";
    	GregorianCalendar Fecha = new GregorianCalendar();
    	int dia = Fecha.get(5);
    	int ano = Fecha.get(1);
    	int mes = Fecha.get(2) + 1;
    	SimpleHash modelRoot = new SimpleHash();
    	SimpleList simplelist = new SimpleList();
    	Validar valida = new Validar();
    	Certif_Manager data_cert = new Certif_Manager(Conexion);
    	Consulta info = data_cert.GetDataRutCursos(rut);
    	if(info.next()) {
    		if(info.getString("id_afp").equals("0")) {
    			modelRoot.put("mensaje", "no registra afiliaci\363n a A.F.P.");
    		} 
    		else {
    			modelRoot.put("titulo", "CERTIFICADO DE AFILIACI\323N A A.F.P.");
    			modelRoot.put("empresa", info.getString("nom_empresa"));
    			modelRoot.put("cargo", valida.validarDato(info.getString("cargo"), "N/R"));
    			modelRoot.put("afp", valida.validarDato(info.getString("afp"), "NR"));
    		}
    		digito = info.getString("digito_ver");
    		modelRoot.put("rut", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(Integer.parseInt(rut)))))).append("-").append(digito))));
    		modelRoot.put("nombre", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(valida.validarDato(info.getString("nombres")))))).append(valida.validarDato(info.getString("ape_paterno"))).append(valida.validarDato(info.getString("ape_materno"))))));
    		strFechaHoy = String.valueOf(String.valueOf((new StringBuilder("Santiago, ")).append(dia).append(" de ").append(Tools.RescataMes(mes)).append(" de ").append(ano)));
    		modelRoot.put("fecha", strFechaHoy);
    		SimpleHash simplehash1;
    		for(info = data_cert.GetCursos(rut); info.next(); simplelist.add(simplehash1)) {
    			simplehash1 = new SimpleHash();
    			simplehash1.put("cu", valida.validarDato(info.getString("curso")));
    			simplehash1.put("org", valida.validarDato(info.getString("organismo")));
    			simplehash1.put("fi", valida.validarFecha(info.getValor("fecha_inicio")));
    			simplehash1.put("ho", valida.validarDato(info.getString("horas"), "0"));
    			simplehash1.put("ft", valida.validarFecha(info.getValor("fecha_termino")));
    			simplehash1.put("res", valida.validarDato(info.getString("resultado")));
    			simplehash1.put("base", valida.validarDato(info.getString("base_tematica")));
    			simplehash1.put("asi", valida.validarDato(info.getString("asistencia")));
    			simplehash1.put("nota", valida.validarDato(info.getString("nota_aprob")));
    		}
    		modelRoot.put("cursos", simplelist);
    		} 
    	else {
    		modelRoot.put("error", "0");
    		OutMessage.OutMessagePrint("---->Rut no encontrado!!!!");
    	}
    	strFechaHoy = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(dia)))).append(" de ").append(Tools.RescataMes(mes)).append(" de ").append(ano)));
    	info.close();
    	return modelRoot;
    }

    public SimpleHash getDatosEstudios(Connection Conexion, String rut) {
        String consulta = null;
        String digito = "";
        String strFechaHoy = "";
        GregorianCalendar Fecha = new GregorianCalendar();
        int dia = Fecha.get(5);
        int ano = Fecha.get(1);
        int mes = Fecha.get(2) + 1;
        SimpleHash modelRoot = new SimpleHash();
        SimpleList simplelist = new SimpleList();
        Validar valida = new Validar();
        Certif_Manager data_cert = new Certif_Manager(Conexion);
        Consulta info = data_cert.GetDataRutEstudios(rut);
        if(info.next()) {
            if(info.getString("id_afp").equals("0")) {
                modelRoot.put("mensaje", "no registra afiliaci\363n a A.F.P.");
            } 
            else {
                modelRoot.put("titulo", "CERTIFICADO DE AFILIACI\323N A A.F.P.");
                modelRoot.put("empresa", info.getString("nom_empresa"));
                modelRoot.put("cargo", valida.validarDato(info.getString("cargo"), "N/R"));
                modelRoot.put("afp", valida.validarDato(info.getString("afp"), "NR"));
                modelRoot.put("rut_sup", valida.validarDato(info.getString("rut_supdirecto"), "N/R"));
                modelRoot.put("nom_sup", valida.validarDato(info.getString("nom_supdirecto"), "N/R"));
                modelRoot.put("cargo_sup", valida.validarDato(info.getString("cargo_supdirecto"), "N/R"));
            }
            digito = info.getString("digito_ver");
            modelRoot.put("rut", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(Integer.parseInt(rut)))))).append("-").append(digito))));
            modelRoot.put("nombre", info.getString("nombre"));
            strFechaHoy = String.valueOf(String.valueOf((new StringBuilder("Santiago, ")).append(dia).append(" de ").append(Tools.RescataMes(mes)).append(" de ").append(ano)));
            modelRoot.put("fecha", strFechaHoy);
            SimpleHash simplehash1;
            for(info = data_cert.GetEstudios(rut); info.next(); simplelist.add(simplehash1)) {
                simplehash1 = new SimpleHash();
                simplehash1.put("carrera", info.getString("beca"));
                simplehash1.put("organismo", info.getString("org_instructor"));
                simplehash1.put("periodo", info.getString("periodo"));
                simplehash1.put("resultado", valida.validarDato(info.getString("resultado")));
                OutMessage.OutMessagePrint("carrera: ".concat(String.valueOf(String.valueOf(info.getString("carrera")))));
            }

            modelRoot.put("perfeccionamiento_ext", simplelist);
        } 
        else {
        	modelRoot.put("error", "0");
        	OutMessage.OutMessagePrint("---->Rut no encontrado!!!!");
        }
        strFechaHoy = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(dia)))).append(" de ").append(Tools.RescataMes(mes)).append(" de ").append(ano)));
    	info.close();
    	return modelRoot;
    }
    
    public SimpleHash getDatosRentaBruta(Connection Conexion, String rut) {
        String consulta = null;
        String id = "";
        String digito = "";
        String strFechaHoy = "";
        String strFechaIngre = "";
        GregorianCalendar Fecha = new GregorianCalendar();
        int dia = Fecha.get(5);
        int ano = Fecha.get(1);
        int mes = Fecha.get(2) + 1;
        int total = 0;
        SimpleHash modelRoot = new SimpleHash();
        SimpleList simplelist = new SimpleList();
        Validar valida = new Validar();
        Certif_Manager data_cert = new Certif_Manager(Conexion);
        Consulta haberes = data_cert.GetHaberesFijos(rut);
        if(haberes.next()) {
            modelRoot.put("sbase", Tools.setFormatNumber(valida.validarDato(haberes.getString("renta_reg_mensual"), "0")));
            total += haberes.getInt("renta_reg_mensual");
            if(haberes.getString("gratif_fijo") != null) {
                modelRoot.put("gratif", Tools.setFormatNumber(valida.validarDato(haberes.getString("gratif_fijo"), "0")));
                total += haberes.getInt("gratif_fijo");
            }
            if(haberes.getString("asig_zona_fijo") != null) {
                modelRoot.put("zona", Tools.setFormatNumber(valida.validarDato(haberes.getString("asig_zona_fijo"), "0")));
                total += haberes.getInt("asig_zona_fijo");
            }
            if(haberes.getString("comision_fijo") != null) {
                modelRoot.put("comision", Tools.setFormatNumber(valida.validarDato(haberes.getString("comision_fijo"), "0")));
                total += haberes.getInt("comision_fijo");
            }
            if(haberes.getString("colacion_fijo") != null) {
                modelRoot.put("colacion", Tools.setFormatNumber(valida.validarDato(haberes.getString("colacion_fijo"), "0")));
                total += haberes.getInt("colacion_fijo");
            }
            if(haberes.getString("moviliza_fijo") != null) {
                modelRoot.put("movil", Tools.setFormatNumber(valida.validarDato(haberes.getString("moviliza_fijo"), "0")));
                total += haberes.getInt("moviliza_fijo");
            }
        }
        modelRoot.put("totales", Tools.setFormatNumber(valida.validarDato(String.valueOf(total), "0")));
        haberes.close();
        Consulta info = data_cert.GetDataRutRtaBruta(rut);
        if(info.next()) {
            modelRoot.put("titulo", "CERTIFICADO DE RENTA BRUTA.");
            modelRoot.put("empresa", info.getString("nom_empresa"));
            strFechaIngre = Tools.RescataFecha(info.getValor("fecha_ingreso"));
            OutMessage.OutMessagePrint("--Ingreso: ".concat(String.valueOf(String.valueOf(strFechaIngre))));
            modelRoot.put("ingreso", strFechaIngre);
            modelRoot.put("rut_sup", valida.validarDato(info.getString("rut_supdirecto"), "N/R"));
            modelRoot.put("nom_sup", valida.validarDato(info.getString("nom_supdirecto"), "N/R"));
            modelRoot.put("cargo_sup", valida.validarDato(info.getString("cargo_supdirecto"), "N/R"));
            modelRoot.put("base", Tools.setFormatNumber(valida.validarDato(info.getString("sueldo"), "0")));
            modelRoot.put("haber", Tools.setFormatNumber(valida.validarDato(info.getString("otros_haberes"), "0")));
            total = Integer.parseInt(valida.validarDato(info.getString("sueldo"), "0"));
            total += Integer.parseInt(valida.validarDato(info.getString("otros_haberes"), "0"));
            modelRoot.put("total", Tools.setFormatNumber(String.valueOf(total)));
            digito = info.getString("digito_ver");
            modelRoot.put("rut", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(Integer.parseInt(rut)))))).append("-").append(digito))));
            modelRoot.put("nombre", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(valida.validarDato(info.getString("nombres")))))).append(valida.validarDato(info.getString("ape_paterno"))).append(valida.validarDato(info.getString("ape_materno"))))));
            strFechaHoy = String.valueOf(String.valueOf((new StringBuilder("Santiago, ")).append(dia).append(" de ").append(Tools.RescataMes(mes)).append(" de ").append(ano)));
            modelRoot.put("fecha", strFechaHoy);
        } 
        else {
            modelRoot.put("error", "0");
            OutMessage.OutMessagePrint("---->Rut no encontrado!!!!");
        }
        strFechaHoy = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(dia)))).append(" de ").append(Tools.RescataMes(mes)).append(" de ").append(ano)));
        info.close();
        return modelRoot;
	}
    
    public SimpleHash getDatosRentaSII(Connection Conexion, String periId, String rut) {
        String consulta = null;
        String id = "";
        String digito = "";
        String haberes = "";
        String impuesto = "";
        String strFechaHoy = "";
        GregorianCalendar Fecha = new GregorianCalendar();
        int dia = Fecha.get(5);
        int ano = Fecha.get(1);
        int mes = Fecha.get(2) + 1;
        int total = 0;
        int mesRenta = 0;
        String mesS = "";
        SimpleHash modelRoot = new SimpleHash();
        int tot1 = 0;
        int tot2 = 0;
        int tot3 = 0;
        int tot4 = 0;
        int tot5 = 0;
        int tot6 = 0;
        int tot7 = 0;
        int tot8 = 0;
        int tot9 = 0;
        int tot10 = 0;
        int tot11 = 0;
        int tot12 = 0;
        int tot13 = 0;
        int numC = 0;
        String periodo = periId;
        if(Integer.parseInt(periodo) < ano) {
            Certif_Manager data_cert = new Certif_Manager(Conexion);
            Consulta numero = data_cert.GetNumCertifRtaSII(rut, periodo);
            if(numero.next()) {
                OutMessage.OutMessagePrint("---->Con numero!!!!");
                numC = numero.getInt("numero");
                Consulta info = new Consulta(Conexion);
                SimpleList simplelist = new SimpleList();
                Validar valida = new Validar();
                Consulta rentas = new Consulta(Conexion);
                numero.close();
                datosRut userRut = new datosRut(Conexion, rut);
                SimpleHash simplehash1;
                for(rentas = data_cert.GetDetalleRentas(rut, userRut.Empresa, periodo); rentas.next(); simplelist.add(simplehash1)) {
                    simplehash1 = new SimpleHash();
                    mesRenta = rentas.getInt("mes");
                    mesS = Tools.RescataMes(mesRenta).toUpperCase();
                    mesS = mesS.substring(0, 3);
                    simplehash1.put("mes", mesS);
                    simplehash1.put("ry", valida.validarDato(Tools.setFormatNumber(rentas.getString("renta")), "0"));
                    tot1 += rentas.getInt("renta");
                    simplehash1.put("ly", valida.validarDato(Tools.setFormatNumber(rentas.getString("leyes_soc")), "0"));
                    tot2 += rentas.getInt("leyes_soc");
                    simplehash1.put("ay", valida.validarDato(Tools.setFormatNumber(rentas.getString("renta_af_neta")), "0"));
                    tot3 += rentas.getInt("renta_af_neta");
                    simplehash1.put("iy", valida.validarDato(Tools.setFormatNumber(rentas.getString("imp_retenido")), "0"));
                    tot4 += rentas.getInt("imp_retenido");
                    simplehash1.put("ra", valida.validarDato(Tools.setFormatNumber(rentas.getString("acc_renta")), "0"));
                    tot5 += rentas.getInt("acc_renta");
                    simplehash1.put("la", valida.validarDato(Tools.setFormatNumber(rentas.getString("acc_ley")), "0"));
                    tot6 += rentas.getInt("acc_ley");
                    simplehash1.put("ia", valida.validarDato(Tools.setFormatNumber(rentas.getString("acc_imp")), "0"));
                    tot7 += rentas.getInt("acc_imp");
                    simplehash1.put("rt", valida.validarDato(Tools.setFormatNumber(rentas.getString("tot_renta")), "0"));
                    tot8 += rentas.getInt("tot_renta");
                    simplehash1.put("lt", valida.validarDato(Tools.setFormatNumber(rentas.getString("tot_ley")), "0"));
                    tot9 += rentas.getInt("tot_ley");
                    simplehash1.put("at", valida.validarDato(Tools.setFormatNumber(rentas.getString("tot_neta")), "0"));
                    tot10 += rentas.getInt("tot_neta");
                    simplehash1.put("it", valida.validarDato(Tools.setFormatNumber(rentas.getString("tot_imp")), "0"));
                    tot11 += rentas.getInt("tot_imp");
                    simplehash1.put("f", valida.validarDato(rentas.getString("factor"), "0"));
                    simplehash1.put("raf", valida.validarDato(Tools.setFormatNumber(rentas.getString("renta_actual")), "0"));
                    tot12 += rentas.getInt("renta_actual");
                    simplehash1.put("iact", valida.validarDato(Tools.setFormatNumber(rentas.getString("imp_actual")), "0"));
                    tot13 += rentas.getInt("imp_actual");
                }
                modelRoot.put("detalle", simplelist);
                modelRoot.put("t1", Tools.setFormatNumber(tot1));
                modelRoot.put("t2", Tools.setFormatNumber(tot2));
                modelRoot.put("t3", Tools.setFormatNumber(tot3));
                modelRoot.put("t4", Tools.setFormatNumber(tot4));
                modelRoot.put("t5", Tools.setFormatNumber(tot5));
                modelRoot.put("t6", Tools.setFormatNumber(tot6));
                modelRoot.put("t7", Tools.setFormatNumber(tot7));
                modelRoot.put("t8", Tools.setFormatNumber(tot8));
                modelRoot.put("t9", Tools.setFormatNumber(tot9));
                modelRoot.put("t10", Tools.setFormatNumber(tot10));
                modelRoot.put("t11", Tools.setFormatNumber(tot11));
                modelRoot.put("t12", Tools.setFormatNumber(tot12));
                modelRoot.put("t13", Tools.setFormatNumber(tot13));
                rentas.close();
                info = data_cert.GetDataRutSII(rut);
                if(info.next()) {
                    modelRoot.put("titulo", "CERTIFICADO DE SUELDOS Y OTRAS RENTAS SIMILARES.");
                    modelRoot.put("empresa", info.getString("nom_empresa").toUpperCase());
                    modelRoot.put("rutempresa", info.getString("empre_rut"));
                    modelRoot.put("direccion", info.getString("empre_dir"));
                    modelRoot.put("giro", info.getString("empre_giro"));
                    modelRoot.put("nom_rep", info.getString("nom_rep"));
                    modelRoot.put("rut_rep", info.getString("rut_repr"));
                    modelRoot.put("num", String.valueOf(numC));
                    digito = info.getString("digito_ver");
                    modelRoot.put("rut", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(Integer.parseInt(rut)))))).append("-").append(digito))));
                    modelRoot.put("nombre", valida.validarDato(info.getString("nombre")));
                    strFechaHoy = String.valueOf(String.valueOf((new StringBuilder("Santiago, ")).append(dia).append(" de ").append(Tools.RescataMes(mes)).append(" de ").append(ano)));
                    modelRoot.put("fecha", strFechaHoy);
                    modelRoot.put("periodo", periodo);
                } 
                else {
                    modelRoot.put("error", "0");
                    OutMessage.OutMessagePrint("---->Rut no encontrado!!!!");
                }
                strFechaHoy = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(dia)))).append(" de ").append(Tools.RescataMes(mes)).append(" de ").append(ano)));
                info.close();
            } 
            else {
                OutMessage.OutMessagePrint("---->Sin numero asignado!!!!");
                modelRoot.put("numero", periodo);
            }
        } 
        else {
        	modelRoot.put("contable", periodo);
        }
        return modelRoot;
    }
    
    public SimpleHash getDatosIsapre(Connection Conexion, String rut) {
        String consulta = null;
        String digito = "";
        String strFechaHoy = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMMM dd,yyyy");
        GregorianCalendar Fecha = new GregorianCalendar();
        int dia = Fecha.get(5);
        int ano = Fecha.get(1);
        int mes = Fecha.get(2) + 1;
        SimpleHash modelRoot = new SimpleHash();
        Validar valida = new Validar();
        Certif_Manager data_cert = new Certif_Manager(Conexion);
        Consulta info = data_cert.GetDataRutIsapre(rut);
        if(info.next()) {
            if(info.getString("id_isapre").equals("0")) {
                modelRoot.put("mensaje", "no registra afiliaci\363n a Isapre");
            } 
            else {
                modelRoot.put("titulo", "CERTIFICADO DE AFILIACI\323N A ISAPRE.");
                modelRoot.put("empresa", info.getString("nom_empresa"));
                modelRoot.put("cargo", valida.validarDato(info.getString("cargo"), "N/R"));
                modelRoot.put("isapre", valida.validarDato(info.getString("isapre"), "NR"));
                modelRoot.put("plan", valida.validarDato(info.getString("plan_salud"), "NR"));
                modelRoot.put("rut_sup", valida.validarDato(info.getString("rut_supdirecto"), "N/R"));
                modelRoot.put("nom_sup", valida.validarDato(info.getString("nom_supdirecto"), "N/R"));
                modelRoot.put("cargo_sup", valida.validarDato(info.getString("cargo_supdirecto"), "N/R"));
            }
            digito = info.getString("digito_ver");
            modelRoot.put("rut", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(Integer.parseInt(rut)))))).append("-").append(digito))));
            modelRoot.put("nombre", info.getString("nombre"));
            strFechaHoy = String.valueOf(String.valueOf((new StringBuilder("Santiago, ")).append(dia).append(" de ").append(Tools.RescataMes(mes)).append(" de ").append(ano)));
            modelRoot.put("fecha", strFechaHoy);
        } 
        else {
            modelRoot.put("error", "0");
            OutMessage.OutMessagePrint("---->Rut no encontrado!!!!");
        }
        strFechaHoy = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(dia)))).append(" de ").append(Tools.RescataMes(mes)).append(" de ").append(ano)));
        info.close();
        return modelRoot;
    }
    
    public SimpleHash getDatosAntiguedad(int certif, Connection Conexion, String rut) {
        String digito = "";
        String strFechaHoy = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMMM dd,yyyy");
        GregorianCalendar Fecha = new GregorianCalendar();
        int dia = Fecha.get(5);
        int ano = Fecha.get(1);
        int mes = Fecha.get(2) + 1;
        SimpleHash modelRoot = new SimpleHash();
        String antiguedad = null;
        String edad = null;
        String strFechaIngre = "";
        Validar valida = new Validar();
        menuManager menumanager = new menuManager(Conexion);
        Consulta info2 = menumanager.getDatosContrato(rut);
        Certif_Manager data_cert = new Certif_Manager(Conexion);
        Consulta info = data_cert.GetCertifAntig(rut);
        valida.setFormatoFecha("dd-MM-yyyy");
        if(info.next() && info2.next()) {
            modelRoot.put("titulo", "CERTIFICADO DE ANTIGUEDAD.");
            String empresa = info.getString("empresa");
            boolean se_puede = true;
            if(certif == 14 && !empresa.equals("4")) {
                modelRoot.put("denegado", "1");
                se_puede = false;
            }
            if(certif == 15 && !empresa.equals("12")) {
                modelRoot.put("denegado", "1");
                se_puede = false;
            }
            if(se_puede) {
                Certif_Manager data_certificado = new Certif_Manager(Conexion);
                int num_certif = data_certificado.GetNumCertificado(ano);
                modelRoot.put("num", String.valueOf(num_certif));
                modelRoot.put("anio", String.valueOf(ano));
            }
            modelRoot.put("empresa", info.getString("glosa_empresa").trim());
            strFechaIngre = Tools.RescataFecha(info.getValor("fecha_ingreso"));
            OutMessage.OutMessagePrint("--Ingreso: ".concat(String.valueOf(String.valueOf(strFechaIngre))));
            modelRoot.put("ingreso", strFechaIngre);
            modelRoot.put("contrato", valida.validarDato(info.getString("tip_contrato"), "N/R"));
            modelRoot.put("rut_trab", info.getString("rut") + "-" + info.getString("digito_ver"));
            modelRoot.put("cargo", valida.validarDato(info.getString("cargo"), "N/R"));
            modelRoot.put("sueldo", Tools.setFormatNumber(valida.validarDato(info.getString("renta_reg_mensual"), "0")));
            digito = info.getString("digito_ver");
            modelRoot.put("rut", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(Integer.parseInt(rut)))))).append("-").append(digito))));
            modelRoot.put("nombre", valida.validarDato(info.getString("nombre").trim()));
            strFechaHoy = String.valueOf(String.valueOf((new StringBuilder("Santiago, ")).append(dia).append(" de ").append(Tools.RescataMes(mes)).append(" de ").append(ano)));
            modelRoot.put("fecha", strFechaHoy);
            modelRoot.put("segdes", info2.getString("aplica_seguro_des"));
            modelRoot.put("fec_segdes", valida.validarFecha(info2.getString("fec_ini_seguro_des")));
            modelRoot.put("renta", Tools.setFormatNumber(info2.getString("sueldo_base_mensual")));
            modelRoot.put("moneda_renta", info2.getString("moneda_sueldo_base_mensual"));
            modelRoot.put("tipo_contrato",  valida.validarDato( info2.getString("desc_tip_contrato"), "").toUpperCase());
            modelRoot.put("termino", valida.validarFecha(info2.getValor("term"), "NO DEFINIDO"));
            modelRoot.put("fec_ingreso", valida.validarFecha(info2.getValor("fecha_ingreso")));
            String s2 = valida.validarDato(info2.getString("antiguedad"), "0");
            OutMessage.OutMessagePrint("------>Antiguedad= " + info2.getValor("antiguedad"));
            int i = Integer.parseInt(s2) / 12;
            if(i < 0)
                i *= -1;
            modelRoot.put("xempre", String.valueOf(i));
            s2 = valida.validarDato(info2.getString("xcorp"), "0");
            i = Integer.parseInt(s2) / 12;
            if(i < 0)
                i *= -1;
            modelRoot.put("xcorp", String.valueOf(i));
            modelRoot.put("ingre_corp", valida.validarFecha(info2.getValor("inghold")));
            modelRoot.put("ingre_corp_s", Tools.RescataFecha(info2.getValor("inghold")));
            modelRoot.put("fec_unidad", valida.validarFecha(info2.getValor("fec_unidad")));
            s2 = valida.validarDato(info2.getString("xunidad"), "0");
            i = Integer.parseInt(s2) / 12;
            if(i < 0)
                i *= -1;
            modelRoot.put("xunidad", String.valueOf(i));
            modelRoot.put("ingre_cargo", valida.validarFecha(info2.getValor("ingcargo")));
            s2 = valida.validarDato(info2.getString("xcargo"), "0");
            i = Integer.parseInt(s2) / 12;
            if(i < 0)
                i *= -1;
            modelRoot.put("xcargo", String.valueOf(i));
        } 
        else {
            modelRoot.put("error", "0");
            OutMessage.OutMessagePrint("---->Rut no encontrado!!!!");
        }
        strFechaHoy = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(dia)))).append(" de ").append(Tools.RescataMes(mes)).append(" de ").append(ano)));
        info.close();
        return modelRoot;
    }
    
    public SimpleHash getDatosAntiguedad3Meses(int certif, Connection conexion,String rut) {
		String html = "";
		String id = "";
		html = "";
		String digito = "";
		String strFechaHoy = "";
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMMMM dd,yyyy");
		GregorianCalendar Fecha = new GregorianCalendar();
		int dia = Fecha.get(5);
		int ano = Fecha.get(1);
		int mes = Fecha.get(2) + 1;
		SimpleHash modelRoot = new SimpleHash();
		String antiguedad = null;
		String edad = null;
		String strFechaIngre = "";
		String dias_trabajados = "";
        String tot_haberes = "";
        String periodo = "";
        
		Validar valida = new Validar();
		menuManager menumanager = new menuManager(conexion);
		Consulta info2 = menumanager.getDatosContrato(rut);
		Certif_Manager data_cert = new Certif_Manager(conexion);
		Consulta info = data_cert.GetCertifAntig(rut);
		Consulta info3 = data_cert.GetCertifAntig3meses(rut, 3);
		Consulta info4 = data_cert.GetCertifAntig3meses(rut, 1);
		
        Consulta info5 = menumanager.getDatosCertificadoRentaCabecera(rut);
        Consulta info6 = menumanager.getDatosCertificadoRentaDetalle(rut);
        
        modelRoot.put("rentabrutamensual", menumanager.getRentaBrutaMensual(rut));
        
        String rbm3m = menumanager.getRentaBrutaPromedio3Meses(rut);
        int rbm3m_n = Integer.parseInt(rbm3m);
        modelRoot.put("rentabrutamensual3meses", Tools.setFormatNumber(rbm3m));
        modelRoot.put("rentabrutamensual3meses_texto",  Formatear.getInstance().trasformaATexto(rbm3m_n));
        modelRoot.put("rentabrutamensual3meses_rutempresa",  menumanager.getEmpresaRentaBrutaPromedio3Meses(rut));
		
        SimpleList haberList	= new SimpleList();
    	SimpleHash haberIter	= new SimpleHash();
    	
    	if(info5.next()) {
        	modelRoot.put("dias_trabajados", info5.getString("wp_ndias_trab"));
        	modelRoot.put("tot_haberes", Tools.setFormatNumber(info5.getString("tot_haberes")));
        	periodo  = info5.getString("periodo");
        	modelRoot.put("periodo_anio", periodo.substring(0,4));
        	modelRoot.put("periodo_mes", valida.nombreMes(periodo.substring(4,6)));
        }
        
        for(;info6.next();) {
        	haberIter = new SimpleHash();
        	haberIter.put("glosa_haber",valida.validarDato(info6.getString("glosa_haber"),"-"));
        	haberIter.put("val_haber",Tools.setFormatNumber(valida.validarDato(info6.getString("val_haber"),"0")));
        	haberList.add(haberIter);
        }
        
        modelRoot.put("lhaber",haberList);
        
		valida.setFormatoFecha("dd-MM-yyyy");
		if(info.next() && info2.next()) {
			modelRoot.put("titulo", "CERTIFICADO DE REMUNERACIONES.");
			String empresa = info.getString("empresa");
			boolean se_puede = true;
			if(certif == 14 && !empresa.equals("4")) {
				modelRoot.put("denegado", "1");
				se_puede = false;
			}
			if(certif == 15 && !empresa.equals("12")) {
				modelRoot.put("denegado", "1");
				se_puede = false;
			}
			if(se_puede) {
				Certif_Manager data_certificado = new Certif_Manager(conexion);
				int num_certif = data_certificado.GetNumCertificado(ano);
				modelRoot.put("num", String.valueOf(num_certif));
				modelRoot.put("anio", String.valueOf(ano));
			}
			modelRoot.put("empresa", info.getString("glosa_empresa").trim());
			strFechaIngre = Tools.RescataFecha(info.getValor("fecha_ingreso"));
			OutMessage.OutMessagePrint("--Ingreso: ".concat(String.valueOf(String.valueOf(strFechaIngre))));
			modelRoot.put("ingreso", strFechaIngre);
			modelRoot.put("contrato", valida.validarDato(info.getString("tip_contrato"), "N/R"));
			modelRoot.put("rut_trab", info.getString("rut") + "-" + info.getString("digito_ver"));
			modelRoot.put("cargo", valida.validarDato(info.getString("cargo"), "N/R"));
			modelRoot.put("sueldo", Tools.setFormatNumber(valida.validarDato(info.getString("renta_reg_mensual"), "0")));
			digito = info.getString("digito_ver");
			modelRoot.put("rut", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(Integer.parseInt(rut)))))).append("-").append(digito))));
			modelRoot.put("nombre", valida.validarDato(info.getString("nombre").trim()));
			strFechaHoy = String.valueOf(String.valueOf((new StringBuilder("Santiago, ")).append(dia).append(" de ").append(Tools.RescataMes(mes)).append(" de ").append(ano)));
			modelRoot.put("fecha", strFechaHoy);
			modelRoot.put("segdes", info2.getString("aplica_seguro_des"));
			modelRoot.put("fec_segdes", valida.validarFecha(info2.getString("fec_ini_seguro_des")));
			modelRoot.put("renta", Tools.setFormatNumber(info2.getString("sueldo_base_mensual")));
			modelRoot.put("moneda_renta", info2.getString("moneda_sueldo_base_mensual"));
			modelRoot.put("tipo_contrato", info2.getString("desc_tip_contrato").toUpperCase());
			modelRoot.put("termino", valida.validarFecha(info2.getValor("term"), "NO DEFINIDO"));
			modelRoot.put("fec_ingreso", valida.validarFecha(info2.getValor("fecha_ingreso")));
			String s2 = valida.validarDato(info2.getString("antiguedad"), "0");
			OutMessage.OutMessagePrint("------>Antiguedad= " + info2.getValor("antiguedad"));
			int i = Integer.parseInt(s2) / 12;
			if(i < 0)
				i *= -1;
			modelRoot.put("xempre", String.valueOf(i));
			s2 = valida.validarDato(info2.getString("xcorp"), "0");
			i = Integer.parseInt(s2) / 12;
			if(i < 0)
				i *= -1;
			modelRoot.put("xcorp", String.valueOf(i));
			modelRoot.put("ingre_corp", valida.validarFecha(info2.getValor("inghold")));
			modelRoot.put("ingre_corp_s", Tools.RescataFecha(info2.getValor("inghold")));
			modelRoot.put("fec_unidad", valida.validarFecha(info2.getValor("fec_unidad")));
			s2 = valida.validarDato(info2.getString("xunidad"), "0");
			i = Integer.parseInt(s2) / 12;
			if(i < 0)
				i *= -1;
			modelRoot.put("xunidad", String.valueOf(i));
			modelRoot.put("ingre_cargo", valida.validarFecha(info2.getValor("ingcargo")));
			s2 = valida.validarDato(info2.getString("xcargo"), "0");
			i = Integer.parseInt(s2) / 12;
			if(i < 0)
				i *= -1;
			modelRoot.put("xcargo", String.valueOf(i));
			modelRoot.put("segdes", info2.getString("aplica_seguro_des"));
			int comisiones = 0;
			int bonos = 0;
			int imposiciones = 0;
			int impos_salud = 0;
			int impos_previs = 0;
			int impuestos = 0;
			int impuesto = 0;
			int imposicion = 0;
			int cargas = 0;
			int sueldo_base = 0;
			int total_haberes = 0;
			int total_descuentos = 0;
			int liquido = 0;
			float uf = 0.0F;
			double bono_vacaciones = 0.0D;
			double bono_fiestas_patrias = 0.0D;
			double bono_navidad = 0.0D;
			int cargas_normales = 0;
			int cargas_maternas = 0;
			int cargas_duplos = 0;
			if(info4.next()) {
				uf = Float.parseFloat(valida.validarDato(info4.getString("uf"), "0"));
				cargas_normales = Integer.parseInt(valida.validarDato(info4.getString("cargas_normales"), "0"));
				cargas_maternas = Integer.parseInt(valida.validarDato(info4.getString("cargas_maternas"), "0"));
				cargas_duplos = Integer.parseInt(valida.validarDato(info4.getString("cargas_duplos"), "0"));
				cargas = cargas_normales + cargas_maternas + cargas_duplos;
				sueldo_base = Integer.parseInt(valida.validarDato(info4.getString("sueldo_base"), "0"));
			}
			int k = 0;
			while(info3.next()) {
				k++;
				impos_salud = Integer.parseInt(valida.validarDato(info3.getString("impos_salud"), "0"));
				impos_previs = Integer.parseInt(valida.validarDato(info3.getString("impos_previs"), "0"));
				impuesto = Integer.parseInt(valida.validarDato(info3.getString("mto_cancel_impto"), "0"));
				int comision_1 = Integer.parseInt(valida.validarDato(info3.getString("comision_1"), "0"));
				int comision_2 = Integer.parseInt(valida.validarDato(info3.getString("comision_2"), "0"));
				int vacaciones = Integer.parseInt(valida.validarDato(info3.getString("bono_vacaciones"), "0"));
				int navidad = Integer.parseInt(valida.validarDato(info3.getString("bono_navidad"), "0"));
				int fiestas_patrias = Integer.parseInt(valida.validarDato(info3.getString("bono_fiestas_patrias"), "0"));
				comisiones += comision_1 + comision_2;
				impuestos += impuesto;
				imposiciones = imposiciones + impos_salud + impos_previs;
			}
			comisiones /= k > 0 ? k : 1;
			impuestos /= k > 0 ? k : 1;
			imposiciones /= k > 0 ? k : 1;
			if((float)sueldo_base / uf > 25F)
				bono_vacaciones = 25F * uf;
			else
				bono_vacaciones = sueldo_base;
			bono_fiestas_patrias = (double)uf * 4.5D;
			bono_navidad = (5.5D + 1.5D * (double)cargas) * (double)uf;
			bonos = (int)((bono_vacaciones + bono_fiestas_patrias + bono_navidad) / 12D);
			total_haberes = sueldo_base + bonos + comisiones;
			total_descuentos = impuestos + imposiciones;
			liquido = total_haberes - total_descuentos;
			if(k >= 1)
				modelRoot.put("no_datos", false);
			else
				modelRoot.put("no_datos", true);
			modelRoot.put("sueldo_base", Tools.setFormatNumber(String.valueOf(sueldo_base)));
			modelRoot.put("bonos", Tools.setFormatNumber(String.valueOf(bonos)));
			modelRoot.put("comisiones", Tools.setFormatNumber(String.valueOf(comisiones)));
			modelRoot.put("total_haberes", Tools.setFormatNumber(String.valueOf(total_haberes)));
			modelRoot.put("imposiciones", Tools.setFormatNumber(String.valueOf(imposiciones)));
			modelRoot.put("impuestos", Tools.setFormatNumber(String.valueOf(impuestos)));
			modelRoot.put("total_descuentos", Tools.setFormatNumber(String.valueOf(total_descuentos)));
			modelRoot.put("liquido", Tools.setFormatNumber(String.valueOf(liquido)));
			OutMessage.OutMessagePrint("\n3Meses FIN");
		} 
		else {
			modelRoot.put("error", "0");
			OutMessage.OutMessagePrint("---->Rut no encontrado!!!!");
		}
		strFechaHoy = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(dia)))).append(" de ").append(Tools.RescataMes(mes)).append(" de ").append(ano)));
		info.close();
		info2.close();
		info3.close();
		info4.close();
		return modelRoot;
	}
}