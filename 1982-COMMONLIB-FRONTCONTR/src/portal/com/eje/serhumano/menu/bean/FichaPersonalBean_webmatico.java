package portal.com.eje.serhumano.menu.bean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.misc.Formatear;
import cl.ejedigital.web.FreemakerTool;
import cl.ejedigital.web.datos.ConsultaTool;
import cl.ejedigital.web.datos.DBConnectionManager;
import cl.ejedigital.web.datos.IDBConnectionManager;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import portal.com.eje.datos.Consulta;
import portal.com.eje.portal.liquidacion.enums.LiquidacionOrigen;
import portal.com.eje.portal.tool.FreemakerToolAdvance;
import portal.com.eje.serhumano.certificados.Certif_Manager;
import portal.com.eje.serhumano.certificados.Certif_Manager_webmatico;
import portal.com.eje.serhumano.certificados.Liquida_Manager;
import portal.com.eje.serhumano.certificados.Liquida_Manager_webmatico;
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

public class FichaPersonalBean_webmatico{
	private static FichaPersonalBean_webmatico instance;
	
	private FichaPersonalBean_webmatico() { 
	}
	
	public static FichaPersonalBean_webmatico getInstance() {
		if(instance == null) {
			synchronized (FichaPersonalBean_webmatico.class) {
				if(instance == null) {
					instance = new FichaPersonalBean_webmatico();
				}
			}
		}
		
		
		return instance;
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
        if(info.next()) {
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
            LiquidacionIter.put("valor_adic_2", cl.ejedigital.tool.validar.Validar.getInstance().validarDato(info.getString("valor_adic_2"), ""));
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
		}
		modelRoot.put("liquidaciones",LiquidacionList);

		return modelRoot;
	}
	
	
	public SimpleHash getDatosContratos(Connection connection,String rut) {
		SimpleHash simplehash = new SimpleHash();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd/MM/yyyy");
        GregorianCalendar gregoriancalendar = new GregorianCalendar();
        java.util.Date date = gregoriancalendar.getTime();
        String s3 = simpledateformat.format(date);
        Validar validar = new Validar();
        menuManager menumanager = new menuManager(connection);
        Consulta consulta = menumanager.getDatosContrato(rut);
        Consulta uni = menumanager.GetUnidad(rut);
        Consulta cc = menumanager.GetCentroCosto(rut);
        Consulta lt = menumanager.getDatosLugarTrabajo(rut);
 
        
        System.out.println("RUT------------>" + rut);
        if(consulta.next()) {
            String s1 = consulta.getString("digito_ver");
            simplehash.put("foto", consulta.getString("id_foto"));
            simplehash.put("rut", rut);
            simplehash.put("rut2", Tools.setFormatNumber(rut) + "-" + s1);
            
            simplehash.put("tip_jor_lab", consulta.getString("tip_jor_lab"));
            simplehash.put("mail", validar.validarDato(consulta.getString("e_mail"), "NR"));
            simplehash.put("anexo", validar.validarDato(consulta.getString("anexo"), "NR"));
            simplehash.put("nombre", consulta.getString("nombre"));
            simplehash.put("area", consulta.getString("area"));
            simplehash.put("cargo", consulta.getString("cargo"));
            simplehash.put("nom_empresa", consulta.getString("nom_empresa"));
            simplehash.put("nom_sociedad", consulta.getString("nom_sociedad"));
            simplehash.put("segdes", consulta.getString("aplica_seguro_des"));
            simplehash.put("fec_segdes", validar.validarFecha(consulta.getString("fec_ini_seguro_des")));
            simplehash.put("valor_adic_1", consulta.getString("valor_adic_1"));
            simplehash.put("valor_adic_2", consulta.getString("valor_adic_2"));
            simplehash.put("cod_tipo_trabajado", consulta.getString("cod_tipo_trabajado"));
            simplehash.put("renta", Tools.setFormatNumber(consulta.getString("sueldo_base_mensual")));
            simplehash.put("moneda_renta", consulta.getString("moneda_sueldo_base_mensual"));
            cc.next();
            uni.next();
            lt.next();
            simplehash.put("id_cco", validar.validarDato(cc.getString("id_cco"), "NR"));
            simplehash.put("cco", validar.validarDato(cc.getString("nom_centro_costo"), "NR"));
            simplehash.put("id_unidad", validar.validarDato(consulta.getString("id_unidad"), "NR"));
            simplehash.put("unidad", validar.validarDato(uni.getString("nom_unidad"), "NR"));
            simplehash.put("id_ctrab", validar.validarDato(lt.getString("representan_legal"), "NR"));
            simplehash.put("ctrab", validar.validarDato(lt.getString("ciudad"), "NR"));
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
            simplehash.put("ingre_corp", validar.validarFecha(consulta.getValor("inghold")));
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

	public SimpleHash getDatosPrev(Connection connection,String rut) {
        Validar validar = new Validar();
        SimpleHash simplehash = new SimpleHash();
        SimpleList simplelist = new SimpleList();
        menuManager menumanager = new menuManager(connection);
        Consulta consulta = menumanager.getCargas(rut);
        String numafp = menumanager.getNumeroAFP(rut);
        String numisapre = menumanager.getNumeroIsapre(rut);
        Validar val = new Validar();
        String afp_porcentaje 		= String.valueOf(val.validarDato(menumanager.getCotAfp(rut),"0"));			//mo_cot_afp modena
        String afp_monto			= String.valueOf(val.validarDato(menumanager.getMontoAfp(rut),"0"));		//cot_afp factor de la moneda
        String isapre_monto			= String.valueOf(val.validarDato(menumanager.getMontoSalud(rut),"0"));		//cot_salud
        String isapre_porcentaje	= String.valueOf(val.validarDato(menumanager.getCotSalud(rut),"0"));		//mon_salud
        String plan_salud 			=String.valueOf(val.validarDato(menumanager.getCotSalud(rut),"0"));
    
        afp_monto = NumberFormat.getNumberInstance(Locale.US).format( Double.parseDouble(afp_monto));
   /*     try {
        	isapre_monto = NumberFormat.getNumberInstance(Locale.US).format((int) Double.parseDouble(isapre_monto));
        }
        catch (NumberFormatException e) {
        	isapre_monto = String.valueOf(val.validarDato(menumanager.getMontoIsapre(rut),"0"));
        }
        
    */
 
        
        //afp_porcentaje = NumberFormat.getNumberInstance(Locale.US).format((int)d);
        if(consulta.next()) {
            String s = consulta.getString("cargas");
            Consulta consulta1 = menumanager.getDatosPrev(rut);
            Consulta consulta2 = menumanager.getDatosPrev2(rut);
            consulta1.next();
            consulta2.next(); 
            //afp_monto = consulta2.getString("concepto2");
            //afp_porcentaje = String.valueOf(val.validarDato(consulta2.getString("concepto3"),"0"));
            //afp_porcentaje = NumberFormat.getNumberInstance(Locale.US).format( Double.parseDouble(afp_porcentaje));
            
            String afp = consulta1.getString("afp");
            simplehash.put("foto", consulta1.getString("id_foto"));
            simplehash.put("nombre", validar.validarDato(consulta1.getString("nombre"), "NR"));
            simplehash.put("afp", validar.validarDato(afp, "NR"));
            simplehash.put("fec_afilia", validar.validarFecha(consulta1.getValor("fec_afi_sist")));
            simplehash.put("ingre_afp", validar.validarFecha(consulta1.getValor("fec_ing_afp"), "NR"));
            simplehash.put("cotizacion", validar.validarDato(afp_porcentaje, "NR"));
            simplehash.put("moneda_cotizacion", validar.validarDato(consulta1.getString("mo_cot_afp"), ""));
            simplehash.put("monto_adicional", validar.validarDato(consulta2.getString("concepto1"), "NR"));
            simplehash.put("moneda_adicional", validar.validarDato(consulta1.getString("mo_cot_adic"), ""));
            simplehash.put("monto_volunt",  Formatear.getInstance().numero( consulta1.getInt("ah_volunt") ) );
            simplehash.put("moneda_volunt", validar.validarDato(consulta1.getString("mo_ah_volunt"), ""));
            simplehash.put("jubilado", validar.validarDato(consulta1.getString("jubilado").trim(), ""));
            simplehash.put("monto_deposito", validar.validarDato(consulta1.getString("dep_conven"), "NR"));
            simplehash.put("moneda_deposito", validar.validarDato(consulta1.getString("mon_dep_conven"), "NR"));
            simplehash.put("afp_historica", validar.validarDato(consulta1.getString("afp_histo"), "NR"));
            simplehash.put("isapre", validar.validarDato(consulta1.getString("isapre"), "NR"));
            simplehash.put("ing_isap", validar.validarFecha(consulta1.getValor("fec_ing_isap"), "NR"));
           // simplehash.put("plan_sal", validar.validarDato(consulta1.getString("plan_salud"), "NR"));
            simplehash.put("fec_consa", validar.validarFecha(consulta1.getValor("fec_con_salud")));
            simplehash.put("fec_venc", validar.validarFecha(consulta1.getValor("venc_salud")));
            simplehash.put("mon_plan", validar.validarDato(isapre_monto, "NR"));
            simplehash.put("cotisap", isapre_porcentaje);
            simplehash.put("adicsal", validar.validarDato(consulta1.getString("adic_salud"), "NR"));
            
            if( consulta1.getString("mon_adic_salud") != null) {
            	simplehash.put("moneda_adicsal", validar.validarDato(consulta1.getString("mon_adic_salud"), "NR"));
            }
            simplehash.put("isap_histo", validar.validarDato(consulta1.getString("isap_histo"), "NR"));
            simplehash.put("camb_plan", validar.validarDato(consulta1.getString("cambio_plan"), "NR"));
            simplehash.put("mail", validar.validarDato(consulta1.getString("e_mail"), "NR"));
            simplehash.put("anexo", validar.validarDato(consulta1.getString("anexo"), "NR"));
            simplehash.put("rut2", Tools.setFormatNumber(rut));
            simplehash.put("rut", rut);
            simplehash.put("unidad", validar.validarDato(consulta1.getString("unidad"), "NR"));
            simplehash.put("numcargas", validar.validarDato(s, "0"));
            simplehash.put("total_afp", validar.validarDato(numafp, "0"));
            simplehash.put("total_isapre", validar.validarDato(numisapre, "0"));
          
            simplehash.put("monto_afp", validar.validarDato(afp_monto, "0"));
            simplehash.put("cargas", simplelist);
            consulta1.close();
            
            
            StringBuffer sql_base = new StringBuffer(" SELECT bono_antig FROM eje_ges_trabajador WHERE rut ='" + rut +"'");
         
            String monto_deafp 	="";
            		try {
                		Statement st = connection.createStatement();
                    	ResultSet data = st.executeQuery(sql_base.toString());
                    	if (data != null && data.next()) {
                    		 monto_deafp 			=String.valueOf(val.validarDato(data.getString("bono_antig"),"0"));
                    		
                    	}
                    	  
        
            		}	catch (SQLException e) {
            			monto_deafp="NR";
        				e.printStackTrace();
        			} 
            		simplehash.put("dinero_afp", validar.validarDato(monto_deafp, "0"));
        }
		
        else  {
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

	public SimpleHash getGrupoFamiliar(Connection connection,String rut, String empresa) {
        Validar validar = new Validar();
        SimpleHash simplehash = new SimpleHash();
        menuManager menumanager = new menuManager(connection);
        Consulta consulta = menumanager.getCargasFamiliares(rut);
        Consulta consulta2 = menumanager.getNumCargas(rut);
        consulta2.next();
        simplehash.put("num_cargas", consulta2.getString("cargas"));
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
            Consulta consulta1 = menumanager.getListaCargasFamiliares(rut, empresa);
            SimpleList simplelist = new SimpleList();
            SimpleHash simplehash1;
            for(; consulta1.next(); simplelist.add(simplehash1)) {
                System.out.println("-------***---EDAD :" + consulta1.getString("edad"));
                simplehash1 = new SimpleHash();
                simplehash1.put("rut", rut);
                simplehash1.put("num", validar.validarDato(consulta1.getString("secuencia"))); 
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
                String genero = (consulta1.getString("sexo") != null ? consulta1.getString("sexo") : "").toUpperCase();
                if(genero.equals("F")) {
                	genero = "Femenino";
                }
                else if(genero.equals("M")) {
                	genero = "Masculino";
                }
                simplehash1.put("sexo", genero);
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

	
	public SimpleHash getEstudios(Connection connection, String rut, String empresa) {
        Validar validar = new Validar();
        SimpleHash simplehash = new SimpleHash();
        menuManager menumanager = new menuManager(connection);
        Consulta consulta = menumanager.getFormacion(rut);
        if(consulta.next()) {
        	simplehash.put("rut", rut);
            simplehash.put("nombre", consulta.getString("nombre"));
            simplehash.put("cargo", consulta.getString("cargo"));
            simplehash.put("fecha_ingreso", validar.validarFecha(consulta.getValor("fecha_ingreso")));
            simplehash.put("unidad", consulta.getString("unidad"));
            simplehash.put("area", consulta.getString("area"));
            simplehash.put("division", consulta.getString("division"));
            Consulta consulta1 = menumanager.getEstudios(rut);
            SimpleList simplelist = new SimpleList();
            SimpleHash simplehash1;
            for(; consulta1.next(); simplelist.add(simplehash1)) {
                simplehash1 = new SimpleHash();
                simplehash1.put("rut", rut);
                simplehash1.put("nivel", validar.validarDato(consulta1.getString("nivel"), ""));
                simplehash1.put("nivel_instucion", validar.validarDato(consulta1.getString("nivel_instucion"), ""));
                simplehash1.put("tipo_programa", consulta1.getString("tipo_programa"));
                simplehash1.put("institucion", consulta1.getString("institucion"));
                simplehash1.put("desde", consulta1.getString("desde"));
                simplehash1.put("hasta", consulta1.getString("hasta"));
                simplehash1.put("finalizacion", consulta1.getString("finalizacion"));
                simplehash1.put("forma_estudio", consulta1.getString("forma_estudio"));
                simplehash1.put("titulo", consulta1.getString("titulo"));
            }
            simplehash.put("estudios", simplelist);
            consulta1.close();
        } 
        else {
            simplehash.put("error", "0");
            System.out.println("---->Rut no encontrado!!!!");
        }
        consulta.close();
		return simplehash;
	}
	
	public SimpleHash getTrayectoriaLaboral(Connection connection, String rut, String empresa) {
        Validar validar = new Validar();
        SimpleHash simplehash = new SimpleHash();
        menuManager menumanager = new menuManager(connection);
        Consulta consulta = menumanager.getFormacion(rut);
        if(consulta.next()) {
        	simplehash.put("rut", rut);
            simplehash.put("nombre", consulta.getString("nombre"));
            simplehash.put("cargo", consulta.getString("cargo"));
            simplehash.put("fecha_ingreso", validar.validarFecha(consulta.getValor("fecha_ingreso")));
            simplehash.put("unidad", consulta.getString("unidad"));
            simplehash.put("area", consulta.getString("area"));
            simplehash.put("division", consulta.getString("division"));
            Consulta consulta1 = menumanager.getTrayectoriaLaboral(rut);
            SimpleList simplelist = new SimpleList();
            SimpleHash simplehash1;
            for(; consulta1.next(); simplelist.add(simplehash1)) {
                simplehash1 = new SimpleHash();
                simplehash1.put("rut", rut);
                simplehash1.put("empresa", validar.validarDato(consulta1.getString("empresa"), ""));
                simplehash1.put("fecha_inicio", validar.validarFecha(consulta1.getString("fecha_inicio"), ""));
                simplehash1.put("fecha_termino", validar.validarFecha(consulta1.getString("fecha_termino"), ""));
                simplehash1.put("cargo", consulta1.getString("cargo"));
                simplehash1.put("antiguedad", consulta1.getString("antiguedad"));
            }
            simplehash.put("trayectoria", simplelist);
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
		 Consulta uni = menumanager.GetUnidad(rut);
         uni.next();
      	 
		if (consulta1.next()) {
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
		    simplehash.put("unidad", validar.validarDato(uni.getString("nom_unidad"), "NR"));
		       
			//simplehash.put("unidad", consulta1.getString("unidad"));
			simplehash.put("centro_costo", consulta1.getString("centro_costo"));
			simplehash.put("id_unidad", consulta1.getString("id_unidad"));
			simplehash.put("area", consulta1.getString("area"));
			simplehash.put("division", consulta1.getString("division"));
			ConsultaData consulta = menumanager.getLicenciasMedicasConsultaData(rut);
			Consulta consulta2 = menumanager.getYearLicenciaMedica(rut);
			SimpleList simplelist2 = new SimpleList();
			SimpleHash simplehash2;
			for (; consulta2.next(); simplelist2.add(simplehash2)) {
				simplehash2 = new SimpleHash();
				simplehash2.put("year_licencia", validar.validarDato(consulta2.getString("year_licencia")));
				simplehash2.put("total_dias", validar.validarDato(consulta2.getString("total_dias")));	
			}
			simplehash.put("licencias2", simplelist2);
			consulta2.close();
			SimpleList simplelist = new SimpleList();
			SimpleHash simplehash1;
			
			while(consulta != null && consulta.next()) {
				simplehash1 = new SimpleHash();
				simplehash1.put("inicio"	, Formatear.getInstance().toDate( consulta.getDateJava("fecha_inicio"), "dd-MM-yyyy" ) );
				simplehash1.put("termino"	, Formatear.getInstance().toDate( consulta.getDateJava("fecha_termino"), "dd-MM-yyyy" ));
				simplehash1.put("id_tipo_licencia", consulta.getForcedString("id_tipo_licencia"));
				String tipo_licencia_mutual="Normal";
				
				if (consulta.getString("tipo_licencia").substring(0,1).equals("5") || consulta.getString("tipo_licencia").substring(0,1).equals("6")) 
					tipo_licencia_mutual="Mutual";
				simplehash1.put("tipo_licencia", tipo_licencia_mutual);

				simplehash1.put("diagnostico", consulta.getString("diagnostico"));
				simplehash1.put("dias", consulta.getForcedString("dias"));
				simplehash1.put("grupo", consulta.getString("grupo_enfermedad"));
				simplehash1.put("rut_profesional", consulta.getString("rut_profesional"));
				simplehash1.put("profesional", consulta.getString("profesional"));
				simplehash1.put("espe_profesional", consulta.getString("espe_profesional"));
				
				simplelist.add(simplehash1);
			}
			simplehash.put("licencias", simplelist);
		}
		consulta1.close();
		uni.close();
		return simplehash;
	}

	public SimpleHash getMuestraInfoRut(Connection connection,String rut) {
        SimpleHash simplehash = new SimpleHash();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd/MM/yyyy");
        GregorianCalendar gregoriancalendar = new GregorianCalendar();
        java.util.Date date = gregoriancalendar.getTime();
        String s3 = null;
        Validar validar = new Validar();
        menuManager menumanager = new menuManager(connection);
        Consulta consulta = menumanager.getDatos(rut);
        consulta.next();
//        String cargas_normales = validar.validarDato(consulta.getString("wp_num_cargas_normale"), "0");
//        String cargas_duplo = validar.validarDato(consulta.getString("wp_num_cargas_duplo"), "0");
//        String cargas_materna = validar.validarDato(consulta.getString("wp_num_cargas_materna"), "0");
//        s3 = String.valueOf(Integer.parseInt(cargas_normales) + Integer.parseInt(cargas_duplo) + Integer.parseInt(cargas_materna));
        consulta.close();

        Consulta conCargas = menumanager.getNumCargas(rut);
        conCargas.next();
        s3 = conCargas.getString("cargas");
        conCargas.close();

        OutMessage.OutMessagePrint("\n\nCargas : " + s3);
        Consulta consulta1 = menumanager.getInfo_before(rut);
        Consulta consulta2 = menumanager.getRankingVisitados(rut);
        SimpleList simplelist = new SimpleList();
        SimpleHash simplehash1;
        for(; consulta2.next(); simplelist.add(simplehash1)) {
            simplehash1 = new SimpleHash();
            simplehash1.put("descripcion", consulta2.getString("descripcion"));
            simplehash1.put("num_vis", consulta2.getString("num_vis"));
        }
        simplehash.put("ranking", simplelist);
        consulta2.close();
        if(consulta1.next()) {
            String s1 = consulta1.getString("digito_ver");
            simplehash.put("rut", rut);
            simplehash.put("rut2", Tools.setFormatNumber(rut) + "-" + s1);
            simplehash.put("nombre", consulta1.getString("nombre"));
            simplehash.put("afp", validar.validarDato(consulta1.getString("afp"), "NR"));
            simplehash.put("isapre", validar.validarDato(consulta1.getString("isapre"), "NR"));
            simplehash.put("fec_ingreso", validar.validarFecha(consulta1.getValor("fecha_ingreso")));
            simplehash.put("fec_nac", validar.validarFecha(consulta1.getValor("fecha_nacim")));
            simplehash.put("est_civil", validar.validarDato(consulta1.getString("estado_civil"), "NR"));
            simplehash.put("fono", validar.validarDato(consulta1.getString("telefono"), "NR"));
            simplehash.put("celular", validar.validarDato(consulta1.getString("celular"), "NR"));
            simplehash.put("nacionalidad", validar.validarDato(consulta1.getString("nacionalidad"), "NR"));
            simplehash.put("profesion", validar.validarDato(consulta1.getString("profesion"), "NR"));
            simplehash.put("niveleduc", validar.validarDato(consulta1.getString("niveleduc"), "NR"));
            simplehash.put("mail", validar.validarDato(consulta1.getString("mail"), "NR"));
            simplehash.put("e_mail", validar.validarDato(consulta1.getString("e_mail"), "NR"));
            simplehash.put("mail_particular", validar.validarDato(consulta1.getString("mail_particular"), "NR"));
            
            String s2 = validar.validarDato(consulta1.getString("Xedad"), "0");
            int i = Integer.parseInt(s2) / 12;
            if(i < 0) {
            	i *= -1;
            }
            Consulta cc = menumanager.GetCentroCosto(rut);
            cc.next();
            simplehash.put("edad", String.valueOf(i));
            simplehash.put("numcargas", validar.validarDato(s3, "0"));
            simplehash.put("sangre", validar.validarDato(consulta1.getString("grupo_sangre"), "NR"));
            simplehash.put("domicilio", validar.validarDato(consulta1.getString("domicilio"), "NR"));
            simplehash.put("comuna", validar.validarDato(consulta1.getString("comuna"), "NR"));
            simplehash.put("ciudad", validar.validarDato(consulta1.getString("ciudad"), "NR"));
            simplehash.put("foto", consulta1.getString("id_foto"));
            simplehash.put("cargo", consulta1.getString("cargo"));
            simplehash.put("id_cco", validar.validarDato(cc.getString("id_cco"), "NR"));
            simplehash.put("cco", validar.validarDato(cc.getString("nom_centro_costo"), "NR"));
            cc.close();
            simplehash.put("unidad", consulta1.getString("unidad"));
            simplehash.put("id_unidad", consulta1.getString("id_unidad"));
            String genero = consulta1.getString("sexo").toUpperCase();
            if(genero.equals("F")) {
            	genero = "Femenino";
            }
            else if(genero.equals("M")) {
            	genero = "Masculino";
            }
            simplehash.put("sexo", genero);
            simplehash.put("empresa", consulta1.getString("nom_empresa"));
            simplehash.put("tipo_contrato", consulta1.getString("desc_tip_contrato"));
            simplehash.put("moneda_sueldo_pactado", consulta1.getString("moneda_sueldo_base_mensual"));
            simplehash.put("sueldo_pactado", consulta1.getString("sueldo_base_mensual"));
            simplehash.put("cta_cte", consulta1.getString("cta_cte"));
            simplehash.put("jornada", consulta1.getString("tip_jor_lab"));
            simplehash.put("fecha_contrato", validar.validarFecha(consulta1.getValor("fecha_ingreso")));
            simplehash.put("antiguedad", validar.validarFecha(consulta1.getValor("antiguedad")));
            simplehash.put("email", validar.validarDato(consulta1.getString("e_mail"), "NR"));
            simplehash.put("anexo", validar.validarDato(consulta1.getString("anexo"), "NR"));
        } 
        else {
            simplehash.put("error", "0");
            OutMessage.OutMessagePrint("---->Rut no encontrado!!!!");
        }
        
        consulta1.close();
       
        
		return simplehash;
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

		DecimalFormat fDec = new DecimalFormat("##.00");
		
		SimpleHash modelRoot = new SimpleHash();
        Validar valida = new Validar();
        Vaca_Manager cartola = new Vaca_Manager(connection);
        Consulta info = cartola.GetCabeceraCartola(rut);
		modelRoot.put("vacas2",  FreemakerToolAdvance.getInstance().getListData(cartola.getYearTotalVacaciones(rut)));

		info.next();
        modelRoot.put("rut_trab", String.valueOf((new StringBuffer(String.valueOf(String.valueOf(Tools.setFormatNumber(info.getString("rut")))))).append("-").append(info.getString("digito_ver"))));
        modelRoot.put("rut", info.getString("rut"));
        modelRoot.put("nombre", info.getString("nombre"));
        modelRoot.put("cargo", info.getString("cargo"));
        modelRoot.put("fec_ingreso", valida.validarFecha(info.getValor("fecha_ingreso")));
        modelRoot.put("fec_ing_hold", valida.validarFecha(info.getValor("inghold")));
        modelRoot.put("anos", valida.validarDato(info.getString("recono"), "0"));
        modelRoot.put("fec", valida.validarFecha(info.getValor("fecrecono")));
        modelRoot.put("anexo", valida.validarDato(info.getString("anexo"), "NR"));
        modelRoot.put("mail", valida.validarDato(info.getString("e_mail"), "NR"));
        modelRoot.put("hoy", valida.validarDato((new SimpleDateFormat("dd/MM/yyyy")).format(new Date()), "1-1-1900"));
        modelRoot.put("foto", info.getString("id_foto"));
        info.close();
        SimpleList simplelist = new SimpleList();
        modelRoot.put("saldo_dias_habiles", cartola.GetSaldoVacacionesByRut(rut,"H"));
        modelRoot.put("saldo_dias_progresivos", cartola.GetSaldoVacacionesByRut(rut,"P"));
        
        Validar val = new Validar();
        //Llama una query para obtener los días de vacaciones normales y progresivos       
        StringBuffer sql_dp = new StringBuffer(" SELECT dias_progresivos , dias_con_derecho as dias_ganados FROM eje_ges_vacaciones_wp WHERE rut ='" + rut +"'");
        double dias_progresivos 		=0;
        double dias_ganados_normales	=0;
        	try {
        		Statement st = connection.createStatement();
                ResultSet data = st.executeQuery(sql_dp.toString());
                if (data != null && data.next()) {
                	dias_progresivos 		= data.getDouble("dias_progresivos");
                	dias_ganados_normales	= data.getDouble("dias_ganados");
                	}    
        		}	catch (SQLException e) {
        			dias_progresivos=0;
    				e.printStackTrace();
    			} 
        modelRoot.put("dias_progresivos", String.valueOf(dias_progresivos));
        modelRoot.put("dias_ganados", String.valueOf(dias_ganados_normales));
        
        //Llama una query para obtener los días consumidos de vacaciones
        StringBuffer sql_dc = new StringBuffer(" SELECT  ( SUM(CAST(dias_normales AS FLOAT)) ")
        		.append("+ SUM(CAST(dias_progresivos AS FLOAT)) ) AS dias_consumidos ") 
        		.append("FROM    dbo.eje_ges_vacaciones_det ") 
        		.append("WHERE   rut = '" + rut +"'");
        double dias_consumidos 		=0;
        	try {
        		Statement st_dc = connection.createStatement();
                ResultSet data_dc = st_dc.executeQuery(sql_dc.toString());
                if (data_dc != null && data_dc.next()) {
                	dias_consumidos 		= data_dc.getDouble("dias_consumidos");
                	} 
        		}	catch (SQLException e) {
        			dias_consumidos=0;
    				e.printStackTrace();
    			} 
        modelRoot.put("dias_consumidos", String.valueOf(dias_consumidos));       		
        //Saldo = dias ganados normales + dias ganados progresivos - dias consumidos
        modelRoot.put("saldo", fDec.format(Double.parseDouble(val.validarDato(dias_ganados_normales  + dias_progresivos - dias_consumidos))));
        
        //modelRoot.put("dias_consumidos", cartola.GetVacacionesConsumidasByRut(rut));
        modelRoot.put("periodo", cartola.GetPeriodoVacacionesByRut(rut));
        Consulta vacaciones = cartola.GetDetalleCartolaNewWp(rut);
        int corr = 0;
        SimpleHash simplehash1;
        for(; vacaciones.next(); simplelist.add(simplehash1)) {
            simplehash1 = new SimpleHash();
            simplehash1.put("corr", String.valueOf(++corr));
            simplehash1.put("desde", valida.validarFecha(vacaciones.getValor("desde"), "1-1-1900"));
            simplehash1.put("hasta", valida.validarFecha(vacaciones.getValor("hasta"), "1-1-1900"));
            simplehash1.put("dias", valida.validarDato(vacaciones.getString("dias_normales"), "0"));
        }
        modelRoot.put("vacaciones", simplelist);
        vacaciones.close();
        modelRoot.put("Control", new ControlAccesoTM(new ControlAcceso(user)));
       
		return modelRoot;
	}
	
	public SimpleHash getDatosLiquidacion(Connection connection,String path, String periodo, int opcion, String rut, HttpServletRequest req) {
        String digito = "";
        String strFechaHoy = "";
        GregorianCalendar Fecha = new GregorianCalendar();
        int dia = Fecha.get(5);
        int ano = Fecha.get(1);
        int mes = Fecha.get(2) + 1;
        SimpleHash modelRoot = new SimpleHash();
        String causa_pago = "";
        Liquida_Manager_webmatico data_rut = new Liquida_Manager_webmatico(connection);
        //Consulta evolucion = data_rut.getUltimasLiquidaciones(rut, Integer.toString(ano));
        Consulta info = data_rut.GetCabecera(LiquidacionOrigen.eje_ges_trabajador, rut, periodo);
        Consulta emp = data_rut.GetEmpresa(rut,periodo);
        Consulta soc = data_rut.GetSociedad(rut, periodo);
        Consulta uni = data_rut.GetUnidad(LiquidacionOrigen.eje_ges_trabajador, rut);
        Consulta cc = data_rut.GetCentroCosto(LiquidacionOrigen.eje_ges_trabajador, rut);
        Consulta afp = data_rut.GetAfp(LiquidacionOrigen.eje_ges_trabajador, rut);
        Consulta isapre = data_rut.GetIsapre(LiquidacionOrigen.eje_ges_trabajador, rut);
        if(info.next()) {
            modelRoot.put("comisiones_ver", "false");
        	strFechaHoy = String.valueOf(String.valueOf((new StringBuffer("")).append(dia).append(" de ").append(Tools.RescataMes(mes)).append(" de ").append(ano)));
            modelRoot.put("fecha_periodo", Tools.RescataMes(Integer.parseInt(periodo.substring(4, 6))) + " " + periodo.substring(0, 4));
            modelRoot.put("fecha", strFechaHoy);
            modelRoot.put("cod_enterprise", info.getString("wp_cod_empresa"));
            modelRoot.put("empresa", info.getString("nom_empre"));
            modelRoot.put("empresa_rut",  info.getString("empre_rut")); //modelRoot.put("empresa_rut",  Tools.setFormatNumber(info.getString("empre_rut")));
            digito = info.getString("digito_ver");
            modelRoot.put("rut_trab", String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(Tools.setFormatNumber(Integer.parseInt(rut.trim())))))).append("-").append(digito))));
            modelRoot.put("nombre", info.getString("nombre"));
            modelRoot.put("cargo", info.getString("nom_cargo"));
            modelRoot.put("ecivil", info.getString("estado_civil"));
            modelRoot.put("sexo", info.getString("sexo").toUpperCase());
            
            String[]  params = { "adic_area", "adic_tipo_area","adic_zona","adic_gerente_regional",
            					 "adic_subgerente","adic_jefe","adic_modulo_gerencia_pertenece","adic_sucursal",
            					 "adic_oficina","adic_oficina_direccion","adic_cargo_funcional","adic_especialidad",
            					 "adic_cod_ejecutivo", "adic_gerencia_subgerencia", "adic_division", "adic_area2",
            					 "adic_categoria","adic_talla","adic_telefono_casa","adic_escolaridad_cod","adic_escolaridad_glosa" };

            for(String p : params) {
            	modelRoot.put(p, info.getString(p));
            }
 
            
            
            
            emp.next();
            soc.next();
            uni.next();
            cc.next();
            isapre.next();
            afp.next();
            modelRoot.put("nom_empresa", emp.getString("nom_empresa"));
            modelRoot.put("dir_empresa", emp.getString("dir_empresa"));
            modelRoot.put("dias", info.getString("ndias_trab"));
            modelRoot.put("nom_sociedad", soc.getString("nom_sociedad"));
            modelRoot.put("nom_unidad", uni.getString("nom_unidad"));
            modelRoot.put("centro_costo", cc.getString("nom_centro_costo"));
            modelRoot.put("isapre", isapre.getString("nom_isapre"));
            modelRoot.put("afp", afp.getString("nom_afp"));
            modelRoot.put("cot_afp", afp.getString("cot_afp"));
            modelRoot.put("mo_cot_afp", afp.getString("mo_cot_afp"));
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
            modelRoot.put("tot_imponible", Tools.setFormatNumber(info.getString("wp_tot_imponible")));
            modelRoot.put("tot_tributable", Tools.setFormatNumber(info.getString("base_tribut")));          
            modelRoot.put("liquido", Tools.setFormatNumber(info.getString("liquido")));
            //modelRoot.put("liquido_texto",  Formatear.getInstance().trasformaATexto(info.getInt("liquido")));
            modelRoot.put("wp_ndias_lic", Tools.setFormatNumber(info.getString("wp_ndias_lic")));
            modelRoot.put("tramo", Tools.setFormatNumber(info.getString("tramo")));
            modelRoot.put("sobregiro", Tools.setFormatNumber(info.getString("sobregiro")));
            modelRoot.put("n_cargas", Tools.setFormatNumber(info.getString("n_cargas")));
            modelRoot.put("base_tribut", Tools.setFormatNumber(info.getString("base_tribut")));
            String it = "0";
            String intr = "0";
            String reliqt = "0";
            int rta = 0;
            String rta_impon = "";	
            if(info.getString("imp_tribut") != null) {
            	it = info.getString("imp_tribut");
            }
            if(info.getString("imp_no_tribut") != null) {
            	intr = info.getString("imp_no_tribut");
            }
            if(info.getString("reliq_rentas") != null) {
            	reliqt = info.getString("reliq_rentas");
            }
            System.err.println(String.valueOf(String.valueOf((new StringBuffer("IT= ")).append(it).append(" --->INTR= ").append(intr))));
            modelRoot.put("renta_impon", Tools.setFormatNumber(info.getString("rta_impon")));
            modelRoot.put("tope_renta", Tools.setFormatNumber(info.getString("tope_imp")));
            modelRoot.put("val_uf", Tools.setFormatNumberFloat(info.getString("val_uf")));
            modelRoot.put("SubQuery", new SubQuery(connection, req, path));
            modelRoot.put("FNum", new FormatoNumero());
            String haberes = "";
            String descuentos = "";
            haberes = String.valueOf(String.valueOf((new StringBuffer("SELECT periodo, empresa, unidad,rut, tip_unidad, id_tp, glosa_haber, val_haber, orden, wp_cod_planta, -1 id_grupo, 'No definido' desc_grupo FROM view_liquidacion_haberes haberes WHERE (wp_indic_papeleta = 'S') AND (periodo = ")).append(periodo).append(") ").append("AND (rut = ").append(rut).append(") ").append(" ORDER BY rut,id_grupo,orden")));
            OutMessage.OutMessagePrint("---->haberes : " + haberes);
            modelRoot.put("haberes", haberes);
            SimpleList itemhaberes = new SimpleList();
            SimpleHash itemhaber;
            Consulta chaberes = new Consulta(connection);
            chaberes.exec(haberes);
            while(chaberes.next()) {
            	itemhaber = new SimpleHash();
            	itemhaber.put("periodo",chaberes.getString("periodo"));
            	itemhaber.put("empresa",chaberes.getString("empresa"));
            	itemhaber.put("unidad",chaberes.getString("unidad"));
            	itemhaber.put("rut",chaberes.getString("rut"));
            	itemhaber.put("tip_unidad",chaberes.getString("tip_unidad"));
            	itemhaber.put("id_tp",chaberes.getString("id_tp"));
            	itemhaber.put("glosa_haber",chaberes.getString("glosa_haber"));
            	itemhaber.put("val_haber",chaberes.getString("val_haber"));
            	itemhaber.put("orden",chaberes.getString("orden"));
            	itemhaber.put("wp_cod_planta",chaberes.getString("wp_cod_planta"));
            	itemhaber.put("id_grupo",chaberes.getString("id_grupo"));
            	itemhaber.put("desc_grupo",chaberes.getString("desc_grupo"));
            	itemhaberes.add(itemhaber);
            }
            modelRoot.put("itemhaberes", itemhaberes);
            descuentos = String.valueOf(String.valueOf((new StringBuffer("SELECT periodo, empresa, unidad,rut, tip_unidad, id_tp, glosa_descuento,val_descuento, orden, wp_cod_planta, -1 id_grupo, 'No definido' desc_grupo FROM view_liquidacion_descuentos descuentos WHERE (wp_indic_papeleta = 'S') AND (periodo = ")).append(periodo).append(") ").append("AND (rut = ").append(rut).append(") ").append(" ORDER BY rut,id_grupo,orden")));
            OutMessage.OutMessagePrint("---->descuentos : " + descuentos);
            modelRoot.put("descuentos", descuentos);
            SimpleList itemdescuentos = new SimpleList();
            SimpleHash itemdescuento;
            Consulta cdescuentos = new Consulta(connection);
            cdescuentos.exec(descuentos);
            while(cdescuentos.next()) {
            	itemdescuento = new SimpleHash();
            	itemdescuento.put("periodo",cdescuentos.getString("periodo"));
            	itemdescuento.put("empresa",cdescuentos.getString("empresa"));
            	itemdescuento.put("unidad",cdescuentos.getString("unidad"));
            	itemdescuento.put("rut",cdescuentos.getString("rut"));
            	itemdescuento.put("tip_unidad",cdescuentos.getString("tip_unidad"));
            	itemdescuento.put("id_tp",cdescuentos.getString("id_tp"));
            	itemdescuento.put("glosa_descuento",cdescuentos.getString("glosa_descuento"));
            	itemdescuento.put("val_descuento",cdescuentos.getString("val_descuento"));
            	itemdescuento.put("orden",cdescuentos.getString("orden"));
            	itemdescuento.put("wp_cod_planta",cdescuentos.getString("wp_cod_planta"));
            	itemdescuento.put("id_grupo",cdescuentos.getString("id_grupo"));
            	itemdescuento.put("desc_grupo",cdescuentos.getString("desc_grupo"));
            	itemdescuentos.add(itemdescuento);
            }
            modelRoot.put("itemdescuentos", itemdescuentos);
            
            
            StringBuilder comisiones_efectivas = new StringBuilder("SELECT rtrim(ltrim(descripcion)) descripcion,monto ")
            	.append("FROM eje_ges_certif_histo_comision_efectiva WHERE periodo = ").append(periodo).append(" AND rut = ")
            	.append(rut).append(" ORDER BY orden");
            SimpleList itemcomisionesefectivas = new SimpleList();
            SimpleHash itemcomisionesefectiva;
            Consulta ccomisionesefectivas = new Consulta(connection);
            ccomisionesefectivas.exec(comisiones_efectivas.toString());
            while(ccomisionesefectivas.next()) {
            	itemcomisionesefectiva = new SimpleHash();
            	itemcomisionesefectiva.put("descripcion",ccomisionesefectivas.getString("descripcion"));
            	itemcomisionesefectiva.put("monto",ccomisionesefectivas.getString("monto"));
            	itemcomisionesefectivas.add(itemcomisionesefectiva);
            }
            modelRoot.put("itemcomisionesefectivas", itemcomisionesefectivas);
            
            
            modelRoot.put("uri", "certificado=" + opcion + "&peri_liq=" + periodo + "");
            modelRoot.put("certificado", String.valueOf(opcion));
            modelRoot.put("cco",cc.getString("cco")); //modelRoot.put("cco",info.getString("centro_costo"));
            modelRoot.put("peri_liq", periodo);
            
            
            
            /* Horas Extras y Dias de Licencia */
            
            StringBuilder horasE = new StringBuilder("SELECT cantidad_50,cantidad_100, valor_50,valor_100 ")
                	.append("FROM eje_ges_centraliza_horas_extras WHERE periodo = ").append(periodo).append(" AND rut = ")
                	.append(rut);
               
                SimpleHash horas50=new SimpleHash();
                SimpleHash horas100=new SimpleHash();
                SimpleHash valor50=new SimpleHash();
                SimpleHash valor100=new SimpleHash();
                Consulta horasConsulta = new Consulta(connection);
                Validar valida = new Validar();
                horasConsulta.exec(horasE.toString());
                if (horasConsulta.next()) {
                	modelRoot.put("horas50",valida.validarDato(horasConsulta.getFloat("cantidad_50")));
                	modelRoot.put("horas100",valida.validarDato(horasConsulta.getFloat("cantidad_100")));
                	modelRoot.put("valor50",valida.validarDato(horasConsulta.getFloat("valor_50")));
                	modelRoot.put("valor100",valida.validarDato(horasConsulta.getFloat("valor_100")));
                }
                
                horasConsulta.close();
            
            
        } 
        else {
            modelRoot.put("error", "0");
            OutMessage.OutMessagePrint("---->Rut no encontrado!!!!");
        }
		return modelRoot;
	}

	public SimpleHash getDatosRentasSII(Connection connection,Usuario user,String rutg) {
        SimpleHash modelRoot = new SimpleHash();
        Certif_Manager_webmatico detalle_cert = new Certif_Manager_webmatico(connection);
        Validar valida = new Validar();
        Consulta periodoQ = detalle_cert.GetLastPeriodosRentasSIINew(rutg);
        periodoQ.next(); 
        String periodo=valida.validarDato(periodoQ.getString("ano"));
        String rut = user.getRut().getRut() + "-" + user.getRut().getDig();
        rutg = user.getRut().getRut();
        String nombre = user.getName();
        String opcion = "2";
        String empresa = "96609350";        
        String numcer=null,titulo=null,nomEmpresa = null,rutEmpresa = null,dirEmpresa = null,giroEmpresa = null;
        Consulta planta = detalle_cert.GetDatosPlantaRentasSIINew(empresa);        
        if (planta.next()) {  
        	nomEmpresa = valida.validarDato(planta.getString("nombre"));
        	rutEmpresa = valida.validarDato(planta.getString("rut"));
        	dirEmpresa = valida.validarDato(planta.getString("dir")); 
        	giroEmpresa = valida.validarDato(planta.getString("giro"));
        	modelRoot.put("empresa", nomEmpresa);
        	modelRoot.put("rutempresa", rutEmpresa);
        	modelRoot.put("direccion", dirEmpresa);
        	modelRoot.put("giro", giroEmpresa);
        }
        planta.close();
        SimpleList LineaList = new SimpleList();
    	SimpleHash LineaIter;
        Consulta detalle = detalle_cert.GetDetalleRentaSIINew(rutg,periodo,opcion,empresa);
        if(opcion.equals("1")) {  
        	int tot2=0,tot3=0,tot4=0,tot5=0,tot6=0,tot7=0,tot8=0,tot10=0,tot11=0,tot12=0,tot13=0,tot14=0;
        	for(;detalle.next();LineaList.add(LineaIter)) {  
        		LineaIter = new SimpleHash();
        		LineaIter.put("col1",Tools.RescataMes(Integer.parseInt(valida.validarDato(detalle.getString("mes")))) );
        		LineaIter.put("col2",valida.validarDato(detalle.getString("tothab")) );
        		LineaIter.put("col3",valida.validarDato(detalle.getString("llss"))  );
        		LineaIter.put("col4",valida.validarDato(detalle.getString("btrib")) );
        		LineaIter.put("col5",valida.validarDato(detalle.getString("impto")) );
        		LineaIter.put("col6",valida.validarDato(detalle.getString("mayor_retencion"))  );
        		LineaIter.put("col7",valida.validarDato(detalle.getString("nint"))  );
        		LineaIter.put("col8",valida.validarDato(detalle.getString("zona"))  );
        		LineaIter.put("col9",valida.validarDato(detalle.getString("factor")) );
        		LineaIter.put("col10",valida.validarDato(detalle.getString("c1")) );
        		LineaIter.put("col11",valida.validarDato(detalle.getString("c2")) );
        		LineaIter.put("col12",valida.validarDato(detalle.getString("c3")) );
        		LineaIter.put("col13",valida.validarDato(detalle.getString("c4")) );
        		LineaIter.put("col14",valida.validarDato(detalle.getString("c5")) );
        		numcer=valida.validarDato(detalle.getString("numcer"));
        		tot2= tot2+ Integer.parseInt(valida.validarDato(detalle.getString("tothab")));
        		tot3= tot3+ Integer.parseInt(valida.validarDato(detalle.getString("llss")));
        		tot4= tot4+ Integer.parseInt(valida.validarDato(detalle.getString("btrib")));
        		tot5= tot5+ Integer.parseInt(valida.validarDato(detalle.getString("impto")));
        		tot6= tot6+ Integer.parseInt(valida.validarDato(detalle.getString("mayor_retencion")));
        		tot7= tot7+ Integer.parseInt(valida.validarDato(detalle.getString("nint")));
        		tot8= tot8+ Integer.parseInt(valida.validarDato(detalle.getString("zona")));
        		tot10= tot10+ Integer.parseInt(valida.validarDato(detalle.getString("c1")));
        		tot11= tot11+ Integer.parseInt(valida.validarDato(detalle.getString("c2")));
        		tot12= tot12+ Integer.parseInt(valida.validarDato(detalle.getString("c3")));
        		tot13= tot13+ Integer.parseInt(valida.validarDato(detalle.getString("c4")));
        		tot14= tot14+ Integer.parseInt(valida.validarDato(detalle.getString("c5")));
        	}
        	modelRoot.put("opcion1",opcion);
            titulo="CERTIFICADO SOBRE SUELDOS, PENSIONES O JUBILACIONES Y OTRAS RENTAS SIMILARES";
            modelRoot.put("tot2", String.valueOf(tot2) );
            modelRoot.put("tot3", String.valueOf(tot3) );
            modelRoot.put("tot4", String.valueOf(tot4) );
            modelRoot.put("tot5", String.valueOf(tot5) );
            modelRoot.put("tot6", String.valueOf(tot6) );
            modelRoot.put("tot7", String.valueOf(tot7) );
            modelRoot.put("tot8", String.valueOf(tot8) );
            modelRoot.put("tot9","&nbsp;" );
            modelRoot.put("tot10", String.valueOf(tot10) );
            modelRoot.put("tot11", String.valueOf(tot11) );
            modelRoot.put("tot12", String.valueOf(tot12) );
            modelRoot.put("tot13", String.valueOf(tot13) );
            modelRoot.put("tot14", String.valueOf(tot14) );
        }
        else if(opcion.equals("2")) {  
        	int tot2=0,tot3=0,tot5=0,tot6=0;
        	for(;detalle.next();LineaList.add(LineaIter)) {  
        		LineaIter = new SimpleHash();
        		LineaIter.put("col1",Tools.RescataMes(Integer.parseInt(valida.validarDato(detalle.getString("mes")))) );           
        		LineaIter.put("col2",valida.validarDato(detalle.getString("btrib")) );
        		LineaIter.put("col3",valida.validarDato(detalle.getString("impto")) );
        		LineaIter.put("col4",valida.validarDato(detalle.getString("factor")) );
        		LineaIter.put("col5",valida.validarDato(detalle.getString("c1")) );
        		LineaIter.put("col6",valida.validarDato(detalle.getString("c2")) );
        		tot2= tot2+ Integer.parseInt(valida.validarDato(detalle.getString("btrib")));
        		tot3= tot3+ Integer.parseInt(valida.validarDato(detalle.getString("impto")));
        		tot5= tot5+ Integer.parseInt(valida.validarDato(detalle.getString("c1")));
        		tot6= tot6+ Integer.parseInt(valida.validarDato(detalle.getString("c2")));
        		numcer=valida.validarDato(detalle.getString("numcer"));
        	}
        	modelRoot.put("opcion2",opcion);
        	titulo="CERTIFICADO SOBRE HONORARIOS";
        	modelRoot.put("tot2", String.valueOf(tot2) );
        	modelRoot.put("tot3", String.valueOf(tot3) );
        	modelRoot.put("tot4","&nbsp;" );           
        	modelRoot.put("tot5", String.valueOf(tot5) );
        	modelRoot.put("tot6", String.valueOf(tot6) );
        }
        else if(opcion.equals("3")) {  
        	int tot2=0,tot3=0,tot4=0,tot5=0,tot7=0,tot8=0,tot9=0,tot10=0;
        	for(;detalle.next();LineaList.add(LineaIter)) {  
        		LineaIter = new SimpleHash();
        		LineaIter.put("col1",Tools.RescataMes(Integer.parseInt(valida.validarDato(detalle.getString("mes")))) );           
        		LineaIter.put("col2",valida.validarDato(detalle.getString("btrib")) );
        		LineaIter.put("col3",valida.validarDato(detalle.getString("poa1")) );
        		LineaIter.put("col4",valida.validarDato(detalle.getString("impto")) );
        		LineaIter.put("col5",valida.validarDato(detalle.getString("poa2")) );
        		LineaIter.put("col6",valida.validarDato(detalle.getString("factor")) );
        		LineaIter.put("col7",valida.validarDato(detalle.getString("c1"))  );
        		LineaIter.put("col8",valida.validarDato(detalle.getString("c2")) );
        		LineaIter.put("col9",valida.validarDato(detalle.getString("c3"))  );
        		LineaIter.put("col10",valida.validarDato(detalle.getString("c4"))  );
        		tot2= tot2+ Integer.parseInt(valida.validarDato(detalle.getString("btrib")));
        		tot3= tot3+ Integer.parseInt(valida.validarDato(detalle.getString("poa1")));
        		tot4= tot4+ Integer.parseInt(valida.validarDato(detalle.getString("impto")));              
        		tot5= tot5+ Integer.parseInt(valida.validarDato(detalle.getString("poa2")));
        		tot7= tot7+ Integer.parseInt(valida.validarDato(detalle.getString("c1")));
        		tot8= tot8+ Integer.parseInt(valida.validarDato(detalle.getString("c2")));
        		tot9= tot9+ Integer.parseInt(valida.validarDato(detalle.getString("c3")));
        		tot10= tot10+ Integer.parseInt(valida.validarDato(detalle.getString("c4")));
        		numcer=valida.validarDato(detalle.getString("numcer"));
        	}
        	modelRoot.put("opcion3",opcion);
        	titulo="CERTIFICADO SOBRE HONORARIOS Y PARTICIPACIONES O ASIGNACIONES A DIRECTORES PAGADOS POR SOCIEDADES ANONIMAS";
        	modelRoot.put("tot2", String.valueOf(tot2) );
        	modelRoot.put("tot3", String.valueOf(tot3) );
        	modelRoot.put("tot4", String.valueOf(tot4) );
        	modelRoot.put("tot5", String.valueOf(tot5) );
        	modelRoot.put("tot6","&nbsp;" );
        	modelRoot.put("tot7", String.valueOf(tot7) );
        	modelRoot.put("tot8", String.valueOf(tot8) );
        	modelRoot.put("tot9", String.valueOf(tot9) );           
        	modelRoot.put("tot10", String.valueOf(tot10) );
        }
        modelRoot.put("detalle",LineaList);
        modelRoot.put("periodo",periodo);
        modelRoot.put("nombre",nombre);
        modelRoot.put("rut",rut);
        modelRoot.put("num",numcer);
        modelRoot.put("titulo",titulo);
		return modelRoot;
	}

	public SimpleHash getDatosCotizPrev(Connection connection,String path, String periodo, int opcion,String rut) {
        SimpleHash modelRoot = new SimpleHash();		
		return modelRoot;
	}

	public SimpleHash getDatosCertAntiguedadCargo(Connection connection,String rut) {
        SimpleHash modelRoot = new SimpleHash();
        String digito = "";
        String strFechaHoy = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMMM dd,yyyy");
        GregorianCalendar Fecha = new GregorianCalendar();
        int dia = Fecha.get(5);
        int ano = Fecha.get(1);
        int mes = Fecha.get(2) + 1;
        String antiguedad = null;
        String edad = null;
        String strFechaIngre = "";
        Validar valida = new Validar();
        menuManager menumanager = new menuManager(connection);
        Consulta info2 = menumanager.getDatosContrato(rut);
        Certif_Manager_webmatico data_cert = new Certif_Manager_webmatico(connection);
        Consulta info = data_cert.GetCertifAntig(rut);
        Consulta info3 = data_cert.GetLastPeriodo(rut);
        valida.setFormatoFecha("dd-MM-yyyy");
        if(info.next() && info2.next() && info3.next()) {
            modelRoot.put("titulo", "CERTIFICADO DE ANTIGUEDAD.");
            String empresa = info.getString("empresa");
            boolean se_puede = true;
            Certif_Manager data_certificado = new Certif_Manager(connection);
            int num_certif = data_certificado.GetNumCertificado(ano);
            modelRoot.put("num", String.valueOf(num_certif));
            modelRoot.put("anio", String.valueOf(ano));
            modelRoot.put("empresa", info.getString("glosa_empresa").trim());
            modelRoot.put("empresa_rut", info.getString("empre_rut").trim());
            strFechaIngre = Tools.RescataFecha(info.getValor("fecha_ingreso"));
            OutMessage.OutMessagePrint("--Ingreso: ".concat(String.valueOf(String.valueOf(strFechaIngre))));
            modelRoot.put("ingreso", strFechaIngre);
            modelRoot.put("contrato", valida.validarDato(info.getString("tip_contrato"), "N/R"));
            modelRoot.put("rut_trab", info.getString("rut") + "-" + info.getString("digito_ver"));
            modelRoot.put("cargo", valida.validarDato(info.getString("cargo"), "N/R"));
            modelRoot.put("sueldo", Tools.setFormatNumber(valida.validarDato(info.getString("renta_reg_mensual"), "0")));
            modelRoot.put("total_haberes", Tools.setFormatNumber(valida.validarDato(info.getString("total_haberes"), "0")));
            digito = info.getString("digito_ver");
            modelRoot.put("rut", String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(Tools.setFormatNumber(Integer.parseInt(rut)))))).append("-").append(digito))));
            modelRoot.put("nombre", valida.validarDato(info.getString("nombre").trim()));
            strFechaHoy = String.valueOf(String.valueOf((new StringBuffer("Santiago, ")).append(dia).append(" de ").append(Tools.RescataMes(mes)).append(" de ").append(ano)));
            modelRoot.put("fecha", strFechaHoy);
            modelRoot.put("segdes", info2.getString("aplica_seguro_des"));
            modelRoot.put("fec_segdes", valida.validarFecha(info2.getString("fec_ini_seguro_des")));
            modelRoot.put("renta", Tools.setFormatNumber(info2.getString("sueldo_base_mensual")));
            modelRoot.put("moneda_renta", info2.getString("moneda_sueldo_base_mensual"));
            modelRoot.put("tipo_contrato", info2.getString("desc_tip_contrato").toUpperCase());
            modelRoot.put("termino", valida.validarFecha(info2.getValor("term"), "NO DEFINIDO"));
            modelRoot.put("fec_ingreso", valida.validarFecha(info2.getValor("fecha_ingreso")));
            modelRoot.put("fec_act_anho", valida.validarDato( info3.getValor("ano") ));
            modelRoot.put("fec_act_mes", Tools.RescataMes(Integer.parseInt(valida.validarDato( info3.getValor("mes") ))));
            String s2 = valida.validarDato(info2.getString("antiguedad"), "0");
            OutMessage.OutMessagePrint("------>Antiguedad= " + info2.getValor("antiguedad"));
            int i = Integer.parseInt(s2) / 12;
            if(i < 0) {
            	i *= -1;
            }
            modelRoot.put("xempre", String.valueOf(i));
            s2 = valida.validarDato(info2.getString("xcorp"), "0");
            i = Integer.parseInt(s2) / 12;
            if(i < 0) {
            	i *= -1;
            }
            modelRoot.put("xcorp", String.valueOf(i));
            modelRoot.put("ingre_corp", valida.validarFecha(info2.getValor("inghold")));
            //modelRoot.put("ingre_corp_s", Tools.RescataFecha(info2.getValor("inghold")));
            modelRoot.put("ingre_corp_s", Tools.RescataFecha(info2.getValor("fecha_ingreso")));
            modelRoot.put("fec_unidad", valida.validarFecha(info2.getValor("fec_unidad")));
            s2 = valida.validarDato(info2.getString("xunidad"), "0");
            i = Integer.parseInt(s2) / 12;
            if(i < 0) {
            	i *= -1;
            }
            modelRoot.put("xunidad", String.valueOf(i));
            modelRoot.put("ingre_cargo", valida.validarFecha(info2.getValor("ingcargo")));
            s2 = valida.validarDato(info2.getString("xcargo"), "0");
            i = Integer.parseInt(s2) / 12;
            if(i < 0) {
            	i *= -1;
            }
            modelRoot.put("xcargo", String.valueOf(i));
        } 
        else {
            modelRoot.put("error", "0");
            OutMessage.OutMessagePrint("---->Rut no encontrado!!!!");
        }
        info.close();
		return modelRoot;
	}

	public SimpleHash getDatosCertAntecedentesLaborales(Connection connection,String rut) {
		SimpleHash modelRoot = new SimpleHash();
		String id = "";
        java.util.Date fecha = new Date();
        Locale locale = new Locale("es-CL");
        Format format;
        format = new SimpleDateFormat("EEEEE d MMMMM  yyyy",locale);
        String hoy = "";
        try {
        	hoy = format.format(fecha);
        }
        catch(Exception e) {
        	hoy = "01-01-1999";
        }
    
        String digito = "";
        String strFechaHoy = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMMM dd,yyyy");
        GregorianCalendar Fecha = new GregorianCalendar();
        int dia = Fecha.get(5);
        int ano = Fecha.get(1);
        int mes = Fecha.get(2) + 1;
        String antiguedad = null;
        String edad = null;
        String strFechaIngre = "";
        Validar valida = new Validar();
        menuManager menumanager = new menuManager(connection);
        Consulta info2 = menumanager.getDatosContrato(rut);
        Certif_Manager_webmatico data_cert = new Certif_Manager_webmatico(connection);
        Consulta info = data_cert.GetCertifAntig(rut);
        valida.setFormatoFecha("dd-MM-yyyy");
        if(info.next() && info2.next()) {
            Consulta planta = data_cert.GetDatosPlanta(valida.validarDato(info.getString("empresa")),valida.validarDato(info.getString("wp_cod_planta")));
            String codMutual = null;
            String nomMutual = null;
            String nomPlanta = null;
            String codEmpresa = null;
            String dirEmpresa = null;
            String fonoEmpresa = null;
            String faxEmpresa = null;
            if (planta.next()) {
            	codMutual = valida.validarDato(planta.getString("wp_cod_mutual"));
            	nomMutual= valida.validarDato(planta.getString("descripcion_mutual"));
                nomPlanta = valida.validarDato(planta.getString("descripcion")); 
                codEmpresa = valida.validarDato(planta.getString("empresa"));
                dirEmpresa = valida.validarDato(planta.getString("empre_dir"));
                fonoEmpresa = valida.validarDato(planta.getString("fono"));
                faxEmpresa = valida.validarDato(planta.getString("fax"));
                modelRoot.put("dirEmpresa", dirEmpresa);
                modelRoot.put("fonoEmpresa", fonoEmpresa);
                modelRoot.put("faxEmpresa", faxEmpresa);
            }
            planta.close();
            connMgr = DBConnectionManager.getInstance();
            Connection conn2 = connMgr.getConnection("bdglobal");
            Consulta consul2 = new Consulta(conn2);
            String sql2 = "SELECT cod_adherente FROM MUTUAL_EMP WHERE cod_empresa =" + codEmpresa + " AND cod_mutual="+codMutual;
            //String sql2 = "SELECT cod_adherente FROM MUTUAL_EMP WHERE cod_empresa =" + codEmpresa;
            System.out.println("------------->"+sql2);
            consul2.exec(sql2);
            String codAdherente = null;
            if (consul2.next()) {
            	codAdherente = valida.validarDato(consul2.getString("cod_adherente"));
            }
            System.out.println("codAdherente------------->"+codAdherente);
            consul2.close();
            connMgr.freeConnection("bdglobal",conn2);
        	modelRoot.put("titulo", "CERTIFICADO DE ANTIGUEDAD.");
            String empresa = info.getString("empresa");
            boolean se_puede = true;
            Certif_Manager data_certificado = new Certif_Manager(connection);
            int num_certif = data_certificado.GetNumCertificado(ano);
            modelRoot.put("num", String.valueOf(num_certif));
            modelRoot.put("anio", String.valueOf(ano));
            modelRoot.put("empresa", valida.validarDato( info.getString("glosa_empresa"),"N/R" ));
            modelRoot.put("empresa_rut", valida.validarDato( info.getString("empre_rut"),"N/R" ));
            modelRoot.put("empresa_dir", valida.validarDato( info.getString("empre_dir"),"N/R" ));
            modelRoot.put("planta", nomPlanta);
            modelRoot.put("nomMutual", nomMutual);
            strFechaIngre = Tools.RescataFecha(info.getValor("fecha_ingreso"));
            OutMessage.OutMessagePrint("--Ingreso: ".concat(String.valueOf(String.valueOf(strFechaIngre))));
            modelRoot.put("ingreso", strFechaIngre);
            modelRoot.put("contrato", valida.validarDato(info.getString("tip_contrato"), "N/R"));
            modelRoot.put("rut_trab", info.getString("rut") + "-" + info.getString("digito_ver"));
            modelRoot.put("cargo", valida.validarDato(info.getString("cargo"), "N/R"));
            modelRoot.put("codAdherente", codAdherente);
            modelRoot.put("sueldo", Tools.setFormatNumber(valida.validarDato(info.getString("renta_reg_mensual"), "0")));
            modelRoot.put("total_haberes", Tools.setFormatNumber(valida.validarDato(info.getString("total_haberes"), "0")));
            digito = info.getString("digito_ver");
            modelRoot.put("rut", String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(Tools.setFormatNumber(Integer.parseInt(rut)))))).append("-").append(digito))));
            modelRoot.put("nombre", valida.validarDato(info.getString("nombre")));
            modelRoot.put("fecha_nacim", valida.validarFecha(info.getString("fecha_nacim")));
            modelRoot.put("afp", valida.validarDato(info.getString("afp")));
            modelRoot.put("isapre", valida.validarDato(info.getString("isapre")));
            strFechaHoy = String.valueOf(String.valueOf((new StringBuffer("Santiago, ")).append(dia).append(" de ").append(Tools.RescataMes(mes)).append(" de ").append(ano)));
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
            if(i < 0) {
            	i *= -1;
            }
            modelRoot.put("xempre", String.valueOf(i));
            s2 = valida.validarDato(info2.getString("xcorp"), "0");
            i = Integer.parseInt(s2) / 12;
            if(i < 0) {
            	i *= -1;
            }
            modelRoot.put("xcorp", String.valueOf(i));
            modelRoot.put("ingre_corp", valida.validarFecha(info2.getValor("inghold")));
            modelRoot.put("ingre_corp_s", Tools.RescataFecha(info2.getValor("inghold")));
            modelRoot.put("fec_unidad", valida.validarFecha(info2.getValor("fec_unidad")));
            s2 = valida.validarDato(info2.getString("xunidad"), "0");
            i = Integer.parseInt(s2) / 12;
            if(i < 0) {
            	i *= -1;
            }
            modelRoot.put("xunidad", String.valueOf(i));
            modelRoot.put("ingre_cargo", valida.validarFecha(info2.getValor("ingcargo")));
            s2 = valida.validarDato(info2.getString("xcargo"), "0");
            i = Integer.parseInt(s2) / 12;
            if(i < 0) {
            	i *= -1;
            }
            modelRoot.put("xcargo", String.valueOf(i));
        } 
        else {
            modelRoot.put("error", "0");
            OutMessage.OutMessagePrint("---->Rut no encontrado!!!!");
        }
        info.close();
		return modelRoot;
	}

	protected IDBConnectionManager connMgr;
	
}