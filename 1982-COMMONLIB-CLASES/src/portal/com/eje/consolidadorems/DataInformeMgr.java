package portal.com.eje.consolidadorems;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ResourceBundle;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Tools;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class DataInformeMgr
{

    public DataInformeMgr(Connection conexion)
    {
        con = conexion;
    }

    public SimpleHash generaInformeConsolidado(String periodo)
    {
        SimpleHash modelRoot = new SimpleHash();
        SimpleList informe = new SimpleList();
        SimpleList totales = new SimpleList();

//		SimpleHash Dotación        
        SimpleList informeDotacion = new SimpleList();
//		SimpleHash Dotación Antiguedad        
        SimpleList informeDotacionAntiguedad = new SimpleList();
//		SimpleHash Licencias
        SimpleList informeLicencias = new SimpleList();
//		SimpleHash Vacaciones
        SimpleList informeVacaciones = new SimpleList();
//		SimpleHash Previsiones
        SimpleList empresaList = new SimpleList();
        SimpleHash empresaIter = new SimpleHash();
//		SimpleHash Isapres
        SimpleList empresaIsapreList = new SimpleList();
        SimpleHash empresaIsapreIter = new SimpleHash();

        Consulta conEmpresas = new Consulta(con);
        int totTrabs = 0;
        int totHabs = 0;
        int totLiqu = 0;
        int totDesc = 0;

        int totTrabsIndefinido = 0;
        int totTrabsFijo = 0;
        int totTrabsMasculino = 0;
        int totTrabsFemenino = 0;
        int subTotDotacion = 0;
        int totalTrabsDotacion = 0;
        int subTotDotacionAntiguedad = 0;
        int subTotLicencias = 0;        
        int totTrabsAntIndefinido = 0;
        int totTrabsAntFijo = 0;
        int totalAntTrabsDotacion = 0;
        int totLicMasc = 0;
        int totLicFem = 0;
        int totLicDias = 0;
        int totalTrabsLic = 0;
        int subTotVacaciones = 0;
        int totVacMasc = 0;
        int totVacFem = 0;
        int totVacDias = 0;
        int totalTrabsVac = 0;
        int subTotPrevisiones = 0;
        int subTotPrevisionesHist = 0;
        int totPrevMasc = 0;
        int totPrevFem = 0;
        int totalTrabsPrev = 0;
        int totPrevMascHist = 0;
        int totPrevFemHist = 0;
        int totalTrabsPrevHist = 0;
        int subTotIsapres = 0;
        int subTotIsapresHist = 0;
        int totIsapreMasc = 0;
        int totIsapreFem = 0;
        int totalTrabsIsapre = 0;
        int totIsapreMascHist = 0;
        int totIsapreFemHist = 0;
        int totalTrabsIsapreHist = 0;

        ResourceBundle proper = ResourceBundle.getBundle("db");
        String tablaEmpresa = proper.getString("tablaEmpresa");
        String tablaLiquidacion = proper.getString("tablaLiquidacion");
        String tablaTrabajador = proper.getString("tablaTrabajador");
        String tablaLicencias = proper.getString("tablaLicencias");
        String tablaVacaciones = proper.getString("tablaVacaciones");
        String sql1 = "select distinct a.empresa,b.descrip from " + tablaLiquidacion + " a," + tablaEmpresa + " b" + " where periodo=" + periodo + " and a.empresa=b.empresa order by b.descrip";
        conEmpresas.exec(sql1);
        Consulta conLiq;
        for(; conEmpresas.next(); conLiq.close())
        {
            empresaIter = new SimpleHash();
            empresaIter.put("empresa",conEmpresas.getString("descrip"));
            empresaIsapreIter = new SimpleHash();
            empresaIsapreIter.put("empresa",conEmpresas.getString("descrip"));
            SimpleHash simplehash = new SimpleHash();
            Consulta conPers = new Consulta(con);
            Consulta conHab = new Consulta(con);
            Consulta conDesc = new Consulta(con);
            conLiq = new Consulta(con);
            String sql2 = "select count(rut)as total_trab from " + tablaLiquidacion + " where empresa=" + conEmpresas.getString("empresa") + " and periodo=" + periodo;
            conPers.exec(sql2);
            conPers.next();
            String trabs = conPers.getString("total_trab");
            String sql3 = "select sum(tot_haberes)as total_haberes from " + tablaLiquidacion + " where empresa=" + conEmpresas.getString("empresa") + " and periodo=" + periodo;
            conHab.exec(sql3);
            conHab.next();
            String habs = conHab.getString("total_haberes");
            String sql4 = "select sum(liquido)as total_liquido from " + tablaLiquidacion + " where empresa=" + conEmpresas.getString("empresa") + " and periodo=" + periodo;
            conLiq.exec(sql4);
            conLiq.next();
            String liqu = conLiq.getString("total_liquido");
            String sql0 = "select sum(tot_desctos)as total_descuentos from " + tablaLiquidacion + " where empresa=" + conEmpresas.getString("empresa") + " and periodo=" + periodo;
            conDesc.exec(sql0);
            conDesc.next();
            String desc = conDesc.getString("total_descuentos");
            

//			Trabajadores con contrato plazo Indefinido
            Consulta conTipContI = new Consulta(con);
            String indefinido = "Plazo Indefinido";
            String sql5 = "select count(distinct b.rut)as total_trab_fijo from " + tablaLiquidacion + " a, " + tablaTrabajador + " b" + " where a.empresa=b.wp_cod_empresa and a.empresa=" + conEmpresas.getString("empresa") + " and a.periodo=" + periodo + " and b.desc_tip_contrato=" + "'" + indefinido + "'";
            conTipContI.exec(sql5);
            conTipContI.next();
            String trabs_indefinido = conTipContI.getString("total_trab_fijo");
            
//			Trabajadores con contrato plazo fijo
            Consulta conTipContF = new Consulta(con);
            String fijo = "Plazo Fijo";
            String sql6 = "select count(distinct b.rut)as total_trab_indefinido from " + tablaLiquidacion + " a, " + tablaTrabajador + " b" + " where a.empresa=b.wp_cod_empresa and a.empresa=" + conEmpresas.getString("empresa") + " and a.periodo=" + periodo + " and b.desc_tip_contrato=" + "'" + fijo + "'";
            conTipContF.exec(sql6);
            conTipContF.next();
            String trabs_fijo = conTipContF.getString("total_trab_indefinido");

//			Trabajadores genero M
            Consulta conGeneroM = new Consulta(con);
            String masculino = "M";
            String sql7 = "select count(distinct b.rut)as total_trab_masculino from " + tablaLiquidacion + " a, " + tablaTrabajador + " b" + " where a.empresa=b.wp_cod_empresa and a.empresa=" + conEmpresas.getString("empresa") + " and a.periodo=" + periodo + " and b.sexo=" + "'" + masculino + "'";
            conGeneroM.exec(sql7);
            conGeneroM.next();
            String trabs_masculino = conGeneroM.getString("total_trab_masculino");

//			Trabajadores genero F
            Consulta conGeneroF = new Consulta(con);
            String femenino = "F";
            String sql8 = "select count(distinct b.rut)as total_trab_femenino from " + tablaLiquidacion + " a, " + tablaTrabajador + " b" + " where a.empresa=b.wp_cod_empresa and a.empresa=" + conEmpresas.getString("empresa") + " and a.periodo=" + periodo + " and b.sexo=" + "'" + femenino + "'";
            conGeneroF.exec(sql8);
            conGeneroF.next();
            String trabs_femenino = conGeneroF.getString("total_trab_femenino");

//			Trabajadores con antiguedad indefinido
            Consulta conTipContAntI = new Consulta(con);
            String sql9 = "select count(distinct b.rut)as trabs_antiguedad_indefinido from " + tablaLiquidacion + " a, " + tablaTrabajador + " b" + " where a.empresa=b.wp_cod_empresa and a.empresa=" + conEmpresas.getString("empresa") + " and datepart(year,b.fecha_ingreso)=SUBSTRING("+"'"+periodo+"'"+", 1, 4) and datepart(month,b.fecha_ingreso)=SUBSTRING("+"'"+periodo+"'"+", 5, 2)" + " and b.desc_tip_contrato=" + "'" + indefinido + "'";
            conTipContAntI.exec(sql9);
            conTipContAntI.next();
            String trabs_antiguedad_indefinido = conTipContAntI.getString("trabs_antiguedad_indefinido");

//			Trabajadores con antiguedad fijo
            Consulta conTipContAntF = new Consulta(con);
            String sql10 = "select count(distinct b.rut)as trabs_antiguedad_fijo from " + tablaLiquidacion + " a, " + tablaTrabajador + " b" + " where a.empresa=b.wp_cod_empresa and a.empresa=" + conEmpresas.getString("empresa") + " and datepart(year,b.fecha_ingreso)=SUBSTRING("+"'"+periodo+"'"+", 1, 4) and datepart(month,b.fecha_ingreso)=SUBSTRING("+"'"+periodo+"'"+", 5, 2)" + " and b.desc_tip_contrato=" + "'" + fijo + "'";
            conTipContAntF.exec(sql10);
            conTipContAntF.next();
            String trabs_antiguedad_fijo = conTipContAntF.getString("trabs_antiguedad_fijo");

            
//			Trabajadores con licencias masculino
            Consulta conLicMasc = new Consulta(con);
            String sql11 = "select count(distinct a.rut) trabs_licencia_masculino, sum(a.dias) as dias from " + tablaLicencias + " a, " + tablaTrabajador + " b" + " where a.rut=b.rut and a.wp_cod_empresa=b.wp_cod_empresa and b.empresa=" + conEmpresas.getString("empresa") + " and datepart(year,a.fecha_inicio)=SUBSTRING("+"'"+periodo+"'"+", 1, 4) and datepart(month,a.fecha_inicio)=SUBSTRING("+"'"+periodo+"'"+", 5, 2)" + " and b.sexo=" + "'" + masculino + "'";
            conLicMasc.exec(sql11);
            conLicMasc.next();
            String trabs_licencia_masculino = conLicMasc.getString("trabs_licencia_masculino");
            
//			Trabajadores con licencias masculino
            Consulta conLicFem = new Consulta(con);
            String sql12 = "select count(distinct a.rut) trabs_licencia_Femenino, sum(a.dias) as dias from " + tablaLicencias + " a, " + tablaTrabajador + " b" + " where a.rut=b.rut and a.wp_cod_empresa=b.wp_cod_empresa and b.empresa=" + conEmpresas.getString("empresa") + " and datepart(year,a.fecha_inicio)=SUBSTRING("+"'"+periodo+"'"+", 1, 4) and datepart(month,a.fecha_inicio)=SUBSTRING("+"'"+periodo+"'"+", 5, 2)" + " and b.sexo=" + "'" + femenino + "'";
            conLicFem.exec(sql12);
            conLicFem.next();
            String trabs_licencia_femenino = conLicFem.getString("trabs_licencia_femenino");

//			Trabajadores con vacaciones masculino
            Consulta conVacMasc = new Consulta(con);
            String sql13 = "select count(distinct a.rut) trabs_vacaciones_masculino, sum(a.dias_normales) as dias from " + tablaVacaciones + " a, " + tablaTrabajador + " b" + " where a.rut=b.rut and a.empresa=b.wp_cod_empresa and b.empresa=" + conEmpresas.getString("empresa") + " and datepart(year,a.desde)=SUBSTRING("+"'"+periodo+"'"+", 1, 4) and datepart(month,a.desde)=SUBSTRING("+"'"+periodo+"'"+", 5, 2)" + " and b.sexo=" + "'" + masculino + "'";
            conVacMasc.exec(sql13);
            conVacMasc.next();
            String trabs_vacaciones_masculino = conVacMasc.getString("trabs_vacaciones_masculino");

//			Trabajadores con vacaciones femenino
            Consulta conVacFem = new Consulta(con);
            String sql14 = "select count(distinct a.rut) trabs_vacaciones_femenino, sum(a.dias_normales) as dias from " + tablaVacaciones + " a, " + tablaTrabajador + " b" + " where a.rut=b.rut and a.empresa=b.wp_cod_empresa and b.empresa=" + conEmpresas.getString("empresa") + " and datepart(year,a.desde)=SUBSTRING("+"'"+periodo+"'"+", 1, 4) and datepart(month,a.desde)=SUBSTRING("+"'"+periodo+"'"+", 5, 2)" + " and b.sexo=" + "'" + femenino + "'";
            conVacFem.exec(sql14);
            conVacFem.next();
            String trabs_vacaciones_femenino = conVacFem.getString("trabs_vacaciones_femenino");


//			Informe Previsiones            
            Consulta conPrevision = new Consulta(con);
            String sql15 = "select afp, descrip from eje_ges_afp where afp<>0";
            conPrevision.exec(sql15);
            Consulta conPrevMasc = new Consulta(con);
            Consulta conPrevFem = new Consulta(con);
            Consulta conPrevMascHist = new Consulta(con);
            Consulta conPrevFemHist = new Consulta(con);
            
            SimpleList afpList = new SimpleList();
            SimpleHash afpIter = new SimpleHash();

            for(; conPrevision.next();afpList.add(afpIter))
            {
                afpIter = new SimpleHash();
                
//    			Trabajadores con previsión masculino
                String sql16 = "select count(distinct b.rut) trabs_prevision_masculino  from " + tablaTrabajador + " b" + " where b.empresa=" + conEmpresas.getString("empresa") + " and b.afp=" + conPrevision.getString("afp") + " and datepart(year,b.fecha_ingreso)=SUBSTRING("+"'"+periodo+"'"+", 1, 4) and datepart(month,b.fecha_ingreso)=SUBSTRING("+"'"+periodo+"'"+", 5, 2)" + " and b.sexo=" + "'" + masculino + "'";
                conPrevMasc.exec(sql16);
                conPrevMasc.next();
                String trabs_prevision_masculino = conPrevMasc.getString("trabs_prevision_masculino");
                
//    			Trabajadores con previsión femenino
                String sql17 = "select count(distinct b.rut) trabs_prevision_femenino  from " + tablaTrabajador + " b" + " where b.empresa=" + conEmpresas.getString("empresa") + " and b.afp=" + conPrevision.getString("afp") + " and datepart(year,b.fecha_ingreso)=SUBSTRING("+"'"+periodo+"'"+", 1, 4) and datepart(month,b.fecha_ingreso)=SUBSTRING("+"'"+periodo+"'"+", 5, 2)" + " and b.sexo=" + "'" + femenino + "'";
                conPrevFem.exec(sql17);
                conPrevFem.next();
                String trabs_prevision_femenino = conPrevFem.getString("trabs_prevision_femenino");

//    			Trabajadores historicos con previsión masculino 
                String sql18 = "select count(distinct b.rut) trabs_prevision_masculino_hist  from " + tablaTrabajador + " b" + " where b.empresa=" + conEmpresas.getString("empresa") + " and b.afp=" + conPrevision.getString("afp") + " and b.sexo=" + "'" + masculino + "'";
                conPrevMascHist.exec(sql18);
                conPrevMascHist.next();
                String trabs_prevision_masculino_hist = conPrevMascHist.getString("trabs_prevision_masculino_hist");
                
//    			Trabajadores historicos con previsión femenino
                String sql19 = "select count(distinct b.rut) trabs_prevision_femenino_hist  from " + tablaTrabajador + " b" + " where b.empresa=" + conEmpresas.getString("empresa") + " and b.afp=" + conPrevision.getString("afp") + " and b.sexo=" + "'" + femenino + "'";
                conPrevFemHist.exec(sql19);
                conPrevFemHist.next();
                String trabs_prevision_femenino_hist = conPrevFemHist.getString("trabs_prevision_femenino_hist");
                
                
//    			registro previsiones
                afpIter.put("nombre", conPrevision.getString("descrip"));
                afpIter.put("trabs_prev_masculino", Tools.setFormatNumber(trabs_prevision_masculino));
                afpIter.put("trabs_prev_femenino", Tools.setFormatNumber(trabs_prevision_femenino));
                subTotPrevisiones = conPrevMasc.getInt("trabs_prevision_masculino") + conPrevFem.getInt("trabs_prevision_femenino");
                afpIter.put("sub_total_previsiones", Tools.setFormatNumber(subTotPrevisiones));
                
//    			registro previsiones históricas
                afpIter.put("nombreHist", conPrevision.getString("descrip"));
                afpIter.put("trabs_prev_masculino_hist", Tools.setFormatNumber(trabs_prevision_masculino_hist));
                afpIter.put("prev_femenino_hist", Tools.setFormatNumber(trabs_prevision_femenino_hist));
                subTotPrevisionesHist = conPrevMascHist.getInt("trabs_prevision_masculino_hist") + conPrevFemHist.getInt("trabs_prevision_femenino_hist");
                afpIter.put("sub_total_previsiones_hist", Tools.setFormatNumber(subTotPrevisionesHist));
                
//    			Totales Previsiones
                totPrevMasc += conPrevMasc.getInt("trabs_prevision_masculino");
                totPrevFem += conPrevFem.getInt("trabs_prevision_femenino");
                totalTrabsPrev += subTotPrevisiones;
                
//    			Totales Previsiones Históricas
                totPrevMascHist += conPrevMascHist.getInt("trabs_prevision_masculino_hist");
                totPrevFemHist += conPrevFemHist.getInt("trabs_prevision_femenino_hist");
                totalTrabsPrevHist += subTotPrevisionesHist;
                
            }
            
            empresaIter.put("afps",afpList);
            empresaList.add(empresaIter);
            conPrevision.close();            
            conPrevMasc.close();
            conPrevFem.close();
            conPrevMascHist.close();
            conPrevFemHist.close();

//			Informe Isapres
            Consulta conIsapre = new Consulta(con);
            String sql20 = "select isapre, descrip from eje_ges_isapres where isapre<>0";
            conIsapre.exec(sql20);
            Consulta conIsapreMasc = new Consulta(con);
            Consulta conIsapreFem = new Consulta(con);
            Consulta conIsapreMascHist = new Consulta(con);
            Consulta conIsapreFemHist = new Consulta(con);
            
            SimpleList isapreList = new SimpleList();
            SimpleHash isapreIter = new SimpleHash();

            for(; conIsapre.next();isapreList.add(isapreIter))
            {
                isapreIter = new SimpleHash();
                
//    			Trabajadores con Isapres masculino
                String sql21 = "select count(distinct b.rut) trabs_isapre_masculino  from " + tablaTrabajador + " b" + " where b.empresa=" + conEmpresas.getString("empresa") + " and b.isapre=" + conIsapre.getString("isapre") + " and datepart(year,b.fecha_ingreso)=SUBSTRING("+"'"+periodo+"'"+", 1, 4) and datepart(month,b.fecha_ingreso)=SUBSTRING("+"'"+periodo+"'"+", 5, 2)" + " and b.sexo=" + "'" + masculino + "'";
                conIsapreMasc.exec(sql21);
                conIsapreMasc.next();
                String trabs_isapre_masculino = conIsapreMasc.getString("trabs_isapre_masculino");
                
//    			Trabajadores con Isapres femenino
                String sql22 = "select count(distinct b.rut) trabs_isapre_femenino  from " + tablaTrabajador + " b" + " where b.empresa=" + conEmpresas.getString("empresa") + " and b.isapre=" + conIsapre.getString("isapre") + " and datepart(year,b.fecha_ingreso)=SUBSTRING("+"'"+periodo+"'"+", 1, 4) and datepart(month,b.fecha_ingreso)=SUBSTRING("+"'"+periodo+"'"+", 5, 2)" + " and b.sexo=" + "'" + femenino + "'";
                conIsapreFem.exec(sql22);
                conIsapreFem.next();
                String trabs_isapre_femenino = conIsapreFem.getString("trabs_isapre_femenino");

//    			Trabajadores historicos con Isapres masculino 
                String sql23 = "select count(distinct b.rut) trabs_isapre_masculino_hist  from " + tablaTrabajador + " b" + " where b.empresa=" + conEmpresas.getString("empresa") + " and b.isapre=" + conIsapre.getString("isapre") + " and b.sexo=" + "'" + masculino + "'";
                conIsapreMascHist.exec(sql23);
                conIsapreMascHist.next();
                String trabs_isapre_masculino_hist = conIsapreMascHist.getString("trabs_isapre_masculino_hist");
                
//    			Trabajadores historicos con Isapres femenino
                String sql24 = "select count(distinct b.rut) trabs_isapre_femenino_hist  from " + tablaTrabajador + " b" + " where b.empresa=" + conEmpresas.getString("empresa") + " and b.isapre=" + conIsapre.getString("isapre") + " and b.sexo=" + "'" + femenino + "'";
                conIsapreFemHist.exec(sql24);
                conIsapreFemHist.next();
                String trabs_isapre_femenino_hist = conIsapreFemHist.getString("trabs_isapre_femenino_hist");
                
                
//    			registro Isapres
                isapreIter.put("nombre", conIsapre.getString("descrip"));
                isapreIter.put("trabs_isapre_masculino", Tools.setFormatNumber(trabs_isapre_masculino));
                isapreIter.put("trabs_isapre_femenino", Tools.setFormatNumber(trabs_isapre_femenino));
                subTotIsapres = conIsapreMasc.getInt("trabs_isapre_masculino") + conIsapreFem.getInt("trabs_isapre_femenino");
                isapreIter.put("sub_total_isapres", Tools.setFormatNumber(subTotIsapres));
                
//    			registro Isapres históricas
                isapreIter.put("nombreHist", conIsapre.getString("descrip"));
                isapreIter.put("trabs_isapre_masculino_hist", Tools.setFormatNumber(trabs_isapre_masculino_hist));
                isapreIter.put("isapre_femenino_hist", Tools.setFormatNumber(trabs_isapre_femenino_hist));
                subTotIsapresHist = conIsapreMascHist.getInt("trabs_isapre_masculino_hist") + conIsapreFemHist.getInt("trabs_isapre_femenino_hist");
                isapreIter.put("sub_total_isapres_hist", Tools.setFormatNumber(subTotIsapresHist));
                
//    			Totales Isapres
                totIsapreMasc += conIsapreMasc.getInt("trabs_isapre_masculino");
                totIsapreFem += conIsapreFem.getInt("trabs_isapre_femenino");
                totalTrabsIsapre += subTotIsapres;
                
//    			Totales Isapres Históricas
                totIsapreMascHist += conIsapreMascHist.getInt("trabs_isapre_masculino_hist");
                totIsapreFemHist += conIsapreFemHist.getInt("trabs_isapre_femenino_hist");
                totalTrabsIsapreHist += subTotIsapresHist;
                
            }
            
            empresaIsapreIter.put("isp",isapreList);
            empresaIsapreList.add(empresaIsapreIter);
            conIsapre.close();            
            conIsapreMasc.close();
            conIsapreFem.close();
            conIsapreMascHist.close();
            conIsapreFemHist.close();
            
            
            simplehash.put("emp", conEmpresas.getString("descrip"));
            simplehash.put("trabs", Tools.setFormatNumber(trabs));
            simplehash.put("habs", Tools.setFormatNumber(habs));
            simplehash.put("liqu", Tools.setFormatNumber(liqu));
            simplehash.put("desc", Tools.setFormatNumber(desc));
            informe.add(simplehash);

//			registro por dotación
            simplehash.put("emp", conEmpresas.getString("descrip"));
            simplehash.put("trabs_indefinido", Tools.setFormatNumber(trabs_indefinido));
            simplehash.put("trabs_fijo", Tools.setFormatNumber(trabs_fijo));
            simplehash.put("trabs_masculino", Tools.setFormatNumber(trabs_masculino));
            simplehash.put("trabs_femenino", Tools.setFormatNumber(trabs_femenino));
            subTotDotacion = conTipContI.getInt("total_trab_fijo") + conTipContF.getInt("total_trab_indefinido");
            simplehash.put("sub_total_trabs", Tools.setFormatNumber(subTotDotacion));
            informeDotacion.add(simplehash);

            
//			registro por dotación y antiguedad
            simplehash.put("emp", conEmpresas.getString("descrip"));
            simplehash.put("trabs_ant_indefinido", Tools.setFormatNumber(trabs_antiguedad_indefinido));
            simplehash.put("trabs_ant_fijo", Tools.setFormatNumber(trabs_antiguedad_fijo));
            subTotDotacionAntiguedad = conTipContAntI.getInt("trabs_antiguedad_indefinido") + conTipContAntF.getInt("trabs_antiguedad_fijo");
            simplehash.put("sub_total_ant_trabs", Tools.setFormatNumber(subTotDotacionAntiguedad));
            informeDotacionAntiguedad.add(simplehash);

//			registro licencias médicas
            simplehash.put("emp", conEmpresas.getString("descrip"));
            simplehash.put("trabs_lic_masculino", Tools.setFormatNumber(trabs_licencia_masculino));
            simplehash.put("trabs_lic_femenino", Tools.setFormatNumber(trabs_licencia_femenino));
            simplehash.put("trabs_lic_dias", Tools.setFormatNumber(conLicMasc.getInt("dias") + conLicFem.getInt("dias")));
            subTotLicencias = conLicMasc.getInt("trabs_licencia_masculino") + conLicFem.getInt("trabs_licencia_femenino");
            simplehash.put("sub_total_licencias", Tools.setFormatNumber(subTotLicencias));
            informeLicencias.add(simplehash);

//			registro vacaciones
            simplehash.put("emp", conEmpresas.getString("descrip"));
            simplehash.put("trabs_vac_masculino", Tools.setFormatNumber(trabs_vacaciones_masculino));
            simplehash.put("trabs_vac_femenino", Tools.setFormatNumber(trabs_vacaciones_femenino));
            simplehash.put("trabs_vac_dias", Tools.setFormatNumber(conVacMasc.getInt("dias") + conVacFem.getInt("dias")));
            subTotVacaciones = conVacMasc.getInt("trabs_vacaciones_masculino") + conVacFem.getInt("trabs_vacaciones_femenino");
            simplehash.put("sub_total_vacaciones", Tools.setFormatNumber(subTotVacaciones));
            informeVacaciones.add(simplehash);
            
            totTrabs += conPers.getInt("total_trab");
            totHabs += conHab.getInt("total_haberes");
            totLiqu += conLiq.getInt("total_liquido");
            totDesc += conDesc.getInt("total_descuentos");
            
            conPers.close();
            conHab.close();
            conDesc.close();
            

//			Totales Dotacion
            totTrabsIndefinido += conTipContI.getInt("total_trab_fijo");
            totTrabsFijo += conTipContF.getInt("total_trab_indefinido");
            totTrabsMasculino += conGeneroM.getInt("total_trab_masculino");
            totTrabsFemenino += conGeneroF.getInt("total_trab_femenino");
            totalTrabsDotacion += subTotDotacion;

//			Totales Dotacion Antiguedad
            totTrabsAntIndefinido += conTipContAntI.getInt("trabs_antiguedad_indefinido");
            totTrabsAntFijo += conTipContAntF.getInt("trabs_antiguedad_fijo");
            totalAntTrabsDotacion += subTotDotacionAntiguedad;

//			Totales Licencias
            totLicMasc += conLicMasc.getInt("trabs_licencia_masculino");
            totLicFem += conLicFem.getInt("trabs_licencia_femenino");
            totLicDias += conLicMasc.getInt("dias") + conLicFem.getInt("dias");
            totalTrabsLic += subTotLicencias;
            
//			Totales Vacaciones
            totVacMasc += conVacMasc.getInt("trabs_vacaciones_masculino");
            totVacFem += conVacFem.getInt("trabs_vacaciones_femenino");
            totVacDias += conVacMasc.getInt("dias") + conVacFem.getInt("dias");
            totalTrabsVac += subTotVacaciones;
            
           
//			Cerrar Dotación
            conTipContI.close();
            conTipContF.close();
            conGeneroM.close();
            conGeneroF.close();

//			Cerrar Dotación Antiguedad
            conTipContAntI.close();
            conTipContAntF.close();
            
//			Cerrar Licencias
            conLicMasc.close();
            conLicFem.close();
            
//			Cerrar Vacaciones
            conVacMasc.close();
            conVacFem.close();
            
        }

        
        SimpleHash simplehash2 = new SimpleHash();
        simplehash2.put("totTrabs", Tools.setFormatNumber(totTrabs));
        simplehash2.put("totHabs", Tools.setFormatNumber(totHabs));
        simplehash2.put("totLiqu", Tools.setFormatNumber(totLiqu));
        simplehash2.put("totDesc", Tools.setFormatNumber(totDesc));

//		Totales Dotación
        simplehash2.put("totTrabsIndefinido", Tools.setFormatNumber(totTrabsIndefinido));
        simplehash2.put("totTrabsFijo", Tools.setFormatNumber(totTrabsFijo));
        simplehash2.put("totTrabsMasculino", Tools.setFormatNumber(totTrabsMasculino));
        simplehash2.put("totTrabsFemenino", Tools.setFormatNumber(totTrabsFemenino));
        simplehash2.put("totalTrabsDotacion", Tools.setFormatNumber(totalTrabsDotacion));
        
//		Totales Dotación Antiguedad
        simplehash2.put("totTrabsAntIndefinido", Tools.setFormatNumber(totTrabsIndefinido));
        simplehash2.put("totTrabsAntFijo", Tools.setFormatNumber(totTrabsFijo));
        simplehash2.put("totalAntTrabsDotacion", Tools.setFormatNumber(totalAntTrabsDotacion));
        
//		Totales Licencias
        simplehash2.put("totLicMasc", Tools.setFormatNumber(totLicMasc));
        simplehash2.put("totLicFem", Tools.setFormatNumber(totLicFem));
        simplehash2.put("totLicDias", Tools.setFormatNumber(totLicDias));
        simplehash2.put("totalTrabsLic", Tools.setFormatNumber(totalTrabsLic));
        
//		Totales Vacaciones
        simplehash2.put("totVacMasc", Tools.setFormatNumber(totVacMasc));
        simplehash2.put("totVacFem", Tools.setFormatNumber(totVacFem));
        simplehash2.put("totVacDias", Tools.setFormatNumber(totVacDias));
        simplehash2.put("totalTrabsVac", Tools.setFormatNumber(totalTrabsVac));
        
//		Totales Previsiones
        simplehash2.put("totPrevMasc", Tools.setFormatNumber(totPrevMasc));
        simplehash2.put("totPrevFem", Tools.setFormatNumber(totPrevFem));
        simplehash2.put("totalTrabsPrev", Tools.setFormatNumber(totalTrabsPrev));
        
//		Totales Previsiones Históricas
        simplehash2.put("totPrevMascHist", Tools.setFormatNumber(totPrevMascHist));
        simplehash2.put("totPrevFemHist", Tools.setFormatNumber(totPrevFemHist));
        simplehash2.put("totalTrabsPrevHist", Tools.setFormatNumber(totalTrabsPrevHist));

//		Totales Isapres
        simplehash2.put("totIsapreMasc", Tools.setFormatNumber(totIsapreMasc));
        simplehash2.put("totIsapreFem", Tools.setFormatNumber(totIsapreFem));
        simplehash2.put("totalTrabsIsapre", Tools.setFormatNumber(totalTrabsIsapre));
        
//		Totales Isapres Históricas
        simplehash2.put("totIsapreMascHist", Tools.setFormatNumber(totIsapreMascHist));
        simplehash2.put("totIsapreFemHist", Tools.setFormatNumber(totIsapreFemHist));
        simplehash2.put("totalTrabsIsapreHist", Tools.setFormatNumber(totalTrabsIsapreHist));
        
        totales.add(simplehash2);
        
//		Informe Dotación
        modelRoot.put("informeDotacion", informeDotacion);
        
//		Informe Dotación Antiguedad
        modelRoot.put("informeDotacionAntiguedad", informeDotacionAntiguedad);

//		Informe Licencias
        modelRoot.put("informeLicencias", informeLicencias);
        
//		Informe Vacaciones
        modelRoot.put("informeVacaciones", informeVacaciones);
        
//		Informe Previsiones
        modelRoot.put("informeEmpresa", empresaList);
        
//		Informe Isapres
        modelRoot.put("informeEmpresaIsapre", empresaIsapreList);

        modelRoot.put("informe", informe);
        modelRoot.put("totales", totales);
        return modelRoot;
    }

    public SimpleHash generaPeriodos()
    {
        SimpleHash modelRoot = new SimpleHash();
        SimpleList periodos = new SimpleList();
        String sql1 = "";
        Consulta consulta1 = new Consulta(con);
        ResourceBundle proper = ResourceBundle.getBundle("db");
        String tablaLiquidacion = proper.getString("tablaLiquidacion");
        sql1 = "SELECT DISTINCT periodo FROM " + tablaLiquidacion + " ORDER BY periodo DESC";
        consulta1.exec(sql1);
        SimpleHash simplehash;
        for(; consulta1.next(); periodos.add(simplehash))
        {
            simplehash = new SimpleHash();
            String periodo = Tools.RescataMes(Integer.parseInt(consulta1.getString("periodo").substring(4, 6))) + " / " + consulta1.getString("periodo").substring(0, 4);
            String valorPer = consulta1.getString("periodo");
            simplehash.put("periodo", periodo);
            simplehash.put("valor", valorPer);
        }

        consulta1.close();
        modelRoot.put("per", periodos);
        return modelRoot;
    }

    public void InsertaConsolidadoRemuneraciones(String periodo, String usuario)
    {
        Collection inf = new ArrayList();
        Consulta conEmpresas = new Consulta(con);
        ResourceBundle proper = ResourceBundle.getBundle("db");
        String tablaEmpresa = proper.getString("tablaEmpresa");
        String tablaLiquidacion = proper.getString("tablaLiquidacion");
        String sql1 = "select distinct a.empresa,b.descrip from " + tablaLiquidacion + " a," + tablaEmpresa + " b" + " where periodo=" + periodo + " and a.empresa=b.empresa order by b.descrip";
        conEmpresas.exec(sql1);
        Consulta conLiq;
        for(; conEmpresas.next(); conLiq.close())
        {
            Consulta conPers = new Consulta(con);
            Consulta conHab = new Consulta(con);
            conLiq = new Consulta(con);
            String sql2 = "select count(rut)as total_trab from " + tablaLiquidacion + " where empresa=" + conEmpresas.getString("empresa") + " and periodo=" + periodo;
            conPers.exec(sql2);
            conPers.next();
            String trabs = conPers.getString("total_trab");
            String sql3 = "select sum(tot_haberes)as total_haberes from " + tablaLiquidacion + " where empresa=" + conEmpresas.getString("empresa") + " and periodo=" + periodo;
            conHab.exec(sql3);
            conHab.next();
            String habs = conHab.getString("total_haberes");
            String sql4 = "select sum(liquido)as total_liquido from " + tablaLiquidacion + " where empresa=" + conEmpresas.getString("empresa") + " and periodo=" + periodo;
            conLiq.exec(sql4);
            conLiq.next();
            String liqu = conLiq.getString("total_liquido");
            InformeConsRemsVO informe = new InformeConsRemsVO();
            informe.setPeriodo(periodo);
            informe.setRut_usuario(usuario);
            informe.setCodempresa(conEmpresas.getString("empresa"));
            informe.setNumPers(Integer.valueOf(trabs));
            informe.setTot_haberes_consolid(Integer.valueOf(habs));
            informe.setTot_liquido_consolid(Integer.valueOf(liqu));
            inf.add(informe);
            conPers.close();
            conHab.close();
        }

        conEmpresas.close();
        grabaDatosTablaConsolidado(periodo, inf);
    }

    public void grabaDatosTablaConsolidado(String periodo, Collection informeCons)
    {
        try
        {
            ResourceBundle proper = ResourceBundle.getBundle("db");
            String tablaConsolidado = proper.getString("tablaConsolidado");
            Consulta cons = new Consulta(con);
            String sql = "select periodo from " + tablaConsolidado + " where periodo =" + periodo;
            cons.exec(sql);
            if(cons.next())
            {
                OutMessage.OutMessagePrint("BORRA E INSERTA PERIODO");
                Consulta del = new Consulta(con);
                String sqlDel = "delete from " + tablaConsolidado + " where periodo=" + periodo;
                del.insert(sqlDel);
                del.close();
                Consulta add = new Consulta(con);
                for(Iterator iter = informeCons.iterator(); iter.hasNext(); add.insert(sql))
                {
                    InformeConsRemsVO inf = (InformeConsRemsVO)iter.next();
                    sql = "insert into " + tablaConsolidado;
                    sql = sql + " (periodo,rut_usuario,codempresa,num_personas_consolid,tot_haberes_consolid,tot_liquido_consolid)";
                    sql = sql + "  values (" + inf.getPeriodo() + ",'" + inf.getRut_usuario() + "'," + inf.getCodempresa() + ",";
                    sql = sql + " " + inf.getNumPers() + "," + inf.getTot_haberes_consolid() + "," + inf.getTot_liquido_consolid() + ")";
                }

                add.close();
            } else
            {
                OutMessage.OutMessagePrint("INSERTA PERIODO");
                Consulta add = new Consulta(con);
                for(Iterator iter = informeCons.iterator(); iter.hasNext(); add.insert(sql))
                {
                    InformeConsRemsVO inf = (InformeConsRemsVO)iter.next();
                    sql = "insert into " + tablaConsolidado;
                    sql = sql + " (periodo,rut_usuario,codempresa,num_personas_consolid,tot_haberes_consolid,tot_liquido_consolid)";
                    sql = sql + "  values (" + inf.getPeriodo() + ",'" + inf.getRut_usuario() + "'," + inf.getCodempresa() + ",";
                    sql = sql + " " + inf.getNumPers() + "," + inf.getTot_haberes_consolid() + "," + inf.getTot_liquido_consolid() + ")";
                }

                add.close();
            }
            cons.close();
        }
        catch(Exception e)
        {
            OutMessage.OutMessagePrint("<-----No se pudo registrar Tx en tabla consolidado_rems\n" + e);
        }
    }

    Connection con;
}