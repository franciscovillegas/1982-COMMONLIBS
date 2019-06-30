package portal.com.eje.datosPlantilla;

import java.io.File;
import java.sql.Connection;
import java.util.Hashtable;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Tools;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

// Referenced classes of package com.eje.datosPlantilla:
//            ConsultaNew

public class AdminEjecucionPlantillaMgr
{

    public AdminEjecucionPlantillaMgr(Connection conexion) {
        con = conexion;
    }

    public boolean ejecutaSelectAndGeneraDataForTablaTempPlantilla(int codPlantilla, int codProceso, String select) {
        boolean transaccion;
        String rut;
        transaccion = false;
        String miCampo = "";
        String codAtributo = "";
        String valor = "";
        rut = "";
        String desc = "";
        try {
        	
            Consulta consulta2 = new Consulta(con);
            String sql2 = "SELECT titulo FROM eje_ges_campoasignado ORDER BY UBICACION";
            consulta2.exec(sql2);
            Consulta consulta3 = new Consulta(con);
            String sql3 = "SELECT COUNT (titulo) AS numRegs FROM eje_ges_campoasignado";
            consulta3.exec(sql3);
            consulta3.next();
            int num = consulta3.getInt("numRegs");
            consulta3.close();
            Consulta consulta4 = new Consulta(con);
            String borraTaplaTemporal = "drop table #TEMP_DATA_PLANTILLA";
            consulta4.insert(borraTaplaTemporal);
            consulta4.close();
            int i = 1;
            String listaCamposPlantilla = "";
            String queryTablaTemporal = "create table #TEMP_DATA_PLANTILLA (";
            while(consulta2.next()) {
                if(i == num) {
                    listaCamposPlantilla = listaCamposPlantilla + "[" + consulta2.getString("titulo") + "]";
                    queryTablaTemporal = queryTablaTemporal + "[" + consulta2.getString("titulo") + "]" + " varchar(50))";
                } 
                else {
                    listaCamposPlantilla = listaCamposPlantilla + "[" + consulta2.getString("titulo") + "],";
                    queryTablaTemporal = queryTablaTemporal + "[" + consulta2.getString("titulo") + "]" + " varchar(50),";
                }
                i++;
            }
            consulta2.close();
            Consulta consulta5 = new Consulta(con);
            consulta5.insert(queryTablaTemporal);
            consulta5.close();
            Consulta consulta60 = new Consulta(con);
            String sql60 = "SELECT descripcion from eje_ges_campo";
            consulta60.exec(sql60);
            int myfilas = consulta60.getRows();
            int t = 0;
            int largo = 0;
            String mydesc[] = new String[myfilas];
            while(consulta60.next()) {
                mydesc[t] = consulta60.getString("descripcion");
                t++;
                largo++;
            }
            consulta60.close();
            Consulta consul1 = new Consulta(con);
            String borraTablaDataTemporal = "drop table #TEMP_DATA";
            consul1.insert(borraTablaDataTemporal);
            consul1.close();
            String queryTablaTemporalAux = "create table #TEMP_DATA(idreg int,codplantilla int,codproceso int,ruttrabajador int,atributo int,descripcion varchar (80),valor varchar(50))";
            Consulta consul2 = new Consulta(con);
            consul2.insert(queryTablaTemporalAux);
            consul2.close();
            Consulta consulta6 = new Consulta(con);
            consulta6.exec(select);
            ConsultaNew consultaN = new ConsultaNew(con);
            consultaN.exec(select);
            while(consulta6.next()) {
                if(!rut.equals(consulta6.getString("rut"))) {
                    for(String myfields = consultaN.obtieneSimpleListColumnDataString(); myfields.length() > 0 && myfields.indexOf(",") > 0;) {
                        miCampo = myfields.substring(0, myfields.indexOf(","));
                        int pos = myfields.length();
                        myfields = myfields.substring(myfields.indexOf(",") + 1, pos);
                        int k = 0;
                        while(k < largo) {
                            if(miCampo.equals(mydesc[k])) {
                                rut = consulta6.getString("rut");
                                codAtributo = "0";
                                desc = mydesc[k].trim();
                                String valorAntiguo = consulta6.getString(mydesc[k].trim());
                                valor = valorAntiguo.replace('\'', ' ');
                                grabaDataInTablaTempData(codPlantilla, codProceso, rut, codAtributo, desc, valor);
                            }
                            k++;
                        }
                    }

                    desc = consulta6.getString("cod").trim();
                    codAtributo = "0";
                    valor = consulta6.getString("valor").trim();
                    grabaDataInTablaTempData(codPlantilla, codProceso, rut, codAtributo, desc, valor);
                } 
                else {
                    desc = consulta6.getString("cod").trim();
                    codAtributo = "0";
                    valor = consulta6.getString("valor").trim();
                    grabaDataInTablaTempData(codPlantilla, codProceso, rut, codAtributo, desc, valor);
                }
            consulta6.close();
            consultaN.close();
            Consulta consulta7 = new Consulta(con);
            Consulta consulta8 = new Consulta(con);
            Consulta consulta9 = new Consulta(con);
            String sq = "SELECT DISTINCT (RUTTRABAJADOR) FROM #TEMP_DATA";
            consulta7.exec(sq);
            int registro = 1;
            do {
                if(!consulta7.next())
                    break;
                String valores[] = new String[num];
                String sq2 = "SELECT DESCRIPCION,VALOR FROM #TEMP_DATA WHERE (RUTTRABAJADOR =" + consulta7.getString("RUTTRABAJADOR") + ")";
                consulta8.exec(sq2);
                while(consulta8.next()) {
                    String sq3 = "SELECT TITULO,UBICACION FROM eje_ges_campoasignado ORDER BY UBICACION";
                    consulta9.exec(sq3);
                    while(consulta9.next()) 
                        if(consulta8.getString("DESCRIPCION").equals(consulta9.getString("TITULO")))
                            valores[consulta9.getInt("UBICACION") - 1] = consulta8.getString("VALOR");
                }
                registro++;
                String allValores = "";
                for(int b = 0; b < valores.length; b++) {
                    if(b == valores.length - 1) {
                        if(valores[b] == null) {
                            valores[b] = "0";
                            allValores = allValores + "'" + valores[b] + "'";
                        } 
                        else {
                            allValores = allValores + "'" + valores[b] + "'";
                        }
                        continue;
                    }
                    if(valores[b] == null) {
                        valores[b] = "0";
                        allValores = allValores + "'" + valores[b] + "',";
                    } 
                    else {
                        allValores = allValores + "'" + valores[b] + "',";
                    }
                }

                grabaDatosTablaTempPlantilla(listaCamposPlantilla, allValores);
            } 
            while(true);
            consulta7.close();
            consulta8.close();
            consulta9.close();
            transaccion = true;
            }
        }
        catch(Exception e) {
            OutMessage.OutMessagePrint("<-----No se pudo completar el metodo correctamente\n" + e);
            return transaccion;
        }
        return transaccion;
    }

    public boolean grabaDataInTablaTempData(int codPlantilla, int codProceso, String rut, String atributo, String desc, String valor) {
        boolean transaccion;
        transaccion = false;
        String sql = "";
        try {
            Consulta consulta = new Consulta(con);
            int id = 1;
            sql = "insert into #TEMP_DATA (idreg,codplantilla,codproceso,ruttrabajador,atributo,descripcion,valor) values (" + id + "," + codPlantilla + "," + codProceso + "," + Integer.valueOf(rut).intValue() + "," + Integer.valueOf(atributo).intValue() + ",'" + desc + "','" + valor + "')";
            if(consulta.insert(sql)) {
                transaccion = true;
            } 
            else {
                OutMessage.OutMessagePrint("<-----No se pudo registrar Tx en tabla #TEMP_DATA\n".concat(String.valueOf(String.valueOf(sql))));
                transaccion = false;
            }
            consulta.close();
        }
        catch(Exception e) {
            OutMessage.OutMessagePrint("<-----No se pudo registrar Tx en tabla #TEMP_DATA\n" + e);
            return transaccion;
        }
        return transaccion;
    }

    public boolean grabaDatosTablaTempPlantilla(String campos, String valores) {
        boolean tx;
        tx = false;
        String sql = "";
        try {
            Consulta consulta = new Consulta(con);
            sql = "insert into #TEMP_DATA_PLANTILLA (" + campos + ") values (" + valores + ")";
            if(consulta.insert(sql)) {
                tx = true;
            } 
            else {
                OutMessage.OutMessagePrint("<-----No se pudo registrar Tx en tabla #TEMP_DATA_PLANTILLA\n".concat(String.valueOf(String.valueOf(sql))));
                tx = false;
            }
            consulta.close();
        }
        catch(Exception e) {
            OutMessage.OutMessagePrint("<-----No se pudo registrar Tx en tabla Plantilla\n" + e);
            return tx;
        }
        return tx;
    }

    public String determinaQueryDatosGestion(String periodo, String ccosto, String empresa) {
        String salida;
        salida = "";
        String sql = "";
        String unidad = "";
        try {
            Consulta consultaUnidad = new Consulta(con);
            sql = "select unidad from eje_ges_unidad_rama where (uni_rama =" + ccosto + ") and (tipo = 'U') and (empresa=" + empresa + ")";
            consultaUnidad.exec(sql);
            consultaUnidad.next();
            unidad = consultaUnidad.getString("unidad");
            consultaUnidad.close();
            salida = "SELECT a.rut as RUT,a.digito_ver as DV,a.nombres as NOMBRES,a.ape_paterno as AP_PATERNO,a.ape_materno as AP_MATERNO,b.descrip as CENTRO_COSTO,c.unid_desc as UNIDAD,d.descrip as CARGO,e.glosa_haber as cod,e.val_haber as valor FROM eje_ges_trabajador a , eje_ges_centro_costo b, eje_ges_unidades c, eje_ges_cargos d, eje_ges_certif_histo_liquidacion_detalle e, eje_ges_unidad_rama f  WHERE (a.rut=e.rut) and (a.empresa=e.empresa) and (e.periodo=" + periodo + ")" + " and (a.empresa = b.wp_cod_empresa) and (a.ccosto = b.centro_costo)" + " and (a.empresa=f.empresa) and (a.ccosto=f.uni_rama) and (f.unidad=" + unidad + ")" + " and (f.unidad=c.unid_id) and (f.empresa=c.unid_empresa)" + " and (a.empresa=d.empresa) and (a.cargo=d.cargo)" + " and (e.id_tp = 'H') and (f.tipo='U')";
        }
        catch(Exception e) {
            OutMessage.OutMessagePrint("<-----error en metodo obtieneCentroCostoUsuario(String rut)\n" + e);
            return salida;
        }
        return salida;
    }

    public boolean generaInformeGestion(String query, String nombreArchivo, String path, String periodo) {
        boolean crea;
        crea = false;
        String tit = "";
        try {
            File filePath = new File(path);
            if(!filePath.exists())
                filePath.mkdirs();
            WritableWorkbook workbook = Workbook.createWorkbook(new File(path + nombreArchivo + ".xls"));
            WritableSheet sheet = workbook.createSheet("Informe Gestion", 0);
            WritableFont arial10font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false);
            arial10font.setColour(jxl.format.Colour.BLUE);
            WritableCellFormat formato = new WritableCellFormat(arial10font);
            NumberFormat formatoDigI = new NumberFormat("###,###,###,###,###");
            WritableCellFormat formatoCellDigI = new WritableCellFormat(formatoDigI);
            NumberFormat formatoDigITotCero = new NumberFormat("0");
            WritableCellFormat formatoCellDigITotCero = new WritableCellFormat(formatoDigITotCero);
            formatoCellDigITotCero.setBorder(jxl.format.Border.BOTTOM, jxl.format.BorderLineStyle.MEDIUM);
            formatoCellDigITotCero.setBorder(jxl.format.Border.TOP, jxl.format.BorderLineStyle.MEDIUM);
            NumberFormat formatoDigITot = new NumberFormat("###,###,###,###,###");
            WritableCellFormat formatoCellDigITot = new WritableCellFormat(formatoDigITot);
            formatoCellDigITot.setBorder(jxl.format.Border.BOTTOM, jxl.format.BorderLineStyle.MEDIUM);
            formatoCellDigITot.setBorder(jxl.format.Border.TOP, jxl.format.BorderLineStyle.MEDIUM);
            WritableFont arial12font = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD, true);
            WritableCellFormat formatoTitulo = new WritableCellFormat(arial12font);
            Consulta consulta = new Consulta(con);
            Consulta consulta2 = new Consulta(con);
            Consulta consulta3 = new Consulta(con);
            Consulta consulta4 = new Consulta(con);
            Consulta consultaTot = new Consulta(con);
            tit = "INFORME DE GESTI\323N  -- " + Tools.RescataMes(Integer.parseInt(periodo.substring(4, 6))) + " " + periodo.substring(0, 4) + " --";
            Label labelTitulo = new Label(0, 1, tit, formatoTitulo);
            sheet.addCell(labelTitulo);
            String sql = "SELECT ALIAS FROM eje_ges_campoasignado ORDER BY UBICACION";
            consulta.exec(sql);
            for(int i = 0; consulta.next(); i++) {
                sheet.setColumnView(i, 20);
                Label label = new Label(i, 3, consulta.getString("ALIAS"), formato);
                sheet.addCell(label);
            }

            Consulta consultaOrderBY = new Consulta(con);
            String sqlOrderBY = "SELECT TITULO FROM eje_ges_campoasignado WHERE ORDEN != '1' ORDER BY UBICACION";
            consultaOrderBY.exec(sqlOrderBY);
            int myfilas2 = consultaOrderBY.getRows();
            int ncOrder = 0;
            int largoOrder = 0;
            String nomCampoOrder[] = new String[myfilas2];
            while(consultaOrderBY.next()) {
                nomCampoOrder[ncOrder] = consultaOrderBY.getString("TITULO");
                ncOrder++;
                largoOrder++;
            }
            consultaOrderBY.close();
            Consulta consultaSubTot = new Consulta(con);
            String sqlSubTot = "SELECT TITULO,ESNUMERICO,UBICACION FROM eje_ges_campoasignado WHERE TOTALIZADOR = '3' ORDER BY UBICACION";
            consultaSubTot.exec(sqlSubTot);
            int myfilas = consultaSubTot.getRows();
            int ncTot = 0;
            int largoTot = 0;
            String nomCampoTot[] = new String[myfilas];
            Hashtable ubicacion = new Hashtable();
            do {
                if(!consultaSubTot.next())
                    break;
                if(consultaSubTot.getString("ESNUMERICO").equals("1")) {
                    ubicacion.put(consultaSubTot.getString("TITULO"), consultaSubTot.getString("UBICACION"));
                    nomCampoTot[ncTot] = consultaSubTot.getString("TITULO");
                    ncTot++;
                    largoTot++;
                }
            } 
            while(true);
            consultaSubTot.close();
            Consulta consultaCont = new Consulta(con);
            String sqlCont = "SELECT TITULO,ESNUMERICO,UBICACION FROM eje_ges_campoasignado WHERE TOTALIZADOR = '2' ORDER BY UBICACION";
            consultaCont.exec(sqlCont);
            int myfilas3 = consultaCont.getRows();
            int ncCont = 0;
            int largoCont = 0;
            String nomCampoCont[] = new String[myfilas3];
            Hashtable ubicCont = new Hashtable();
            while(consultaCont.next()) {
                ubicCont.put(consultaCont.getString("TITULO"), consultaCont.getString("UBICACION"));
                nomCampoCont[ncCont] = consultaCont.getString("TITULO");
                ncCont++;
                largoCont++;
            }
            consultaCont.close();
            consulta3.exec(query);
            int registro = 4;
            Hashtable ordenadores = new Hashtable();
            Hashtable totales = new Hashtable();
            Hashtable contadores = new Hashtable();
            int cuenta = 0;
            int cuentaRegsConsulta = 1;
            while(consulta3.next()) {
                if(largoOrder != 0 && largoTot != 0 || largoOrder != 0 && largoCont != 0)
                    if(cuentaRegsConsulta == 1) {
                        for(int ord = 0; ord < largoOrder; ord++)
                            ordenadores.put(nomCampoOrder[ord], consulta3.getString(nomCampoOrder[ord]));

                        for(int tot = 0; tot < largoTot; tot++)
                            totales.put(nomCampoTot[tot], consulta3.getString(nomCampoTot[tot]));

                        for(int cont = 0; cont < largoCont; cont++) {
                            cuenta = 1;
                            String contador = Integer.toString(cuenta);
                            contadores.put(nomCampoCont[cont], contador);
                        }

                    } 
                    else {
                        int dif = 0;
                        for(int ord = 0; ord < largoOrder; ord++) {
                            String valorHash = (String)(String)ordenadores.get(nomCampoOrder[ord]);
                            String valorConsulta = consulta3.getString(nomCampoOrder[ord]);
                            if(!valorHash.equals(valorConsulta))
                                dif++;
                        }

                        if(dif != 0) {
                            for(int tot = 0; tot < largoTot; tot++) {
                                String ubic = (String)ubicacion.get(nomCampoTot[tot]);
                                if(Integer.parseInt((String)totales.get(nomCampoTot[tot])) == 0) {
                                	jxl.write.Number valor = new jxl.write.Number(Integer.parseInt(ubic) - 1, registro, Integer.parseInt((String)totales.get(nomCampoTot[tot])), formatoCellDigITotCero);
                                    sheet.addCell(valor);
                                } 
                                else {
                                	jxl.write.Number valor = new jxl.write.Number(Integer.parseInt(ubic) - 1, registro, Integer.parseInt((String)totales.get(nomCampoTot[tot])), formatoCellDigITot);
                                    sheet.addCell(valor);
                                }
                            }

                            for(int cont = 0; cont < largoCont; cont++) {
                                String ubica = (String)ubicCont.get(nomCampoCont[cont]);
                                WritableCellFormat integerFormat = new WritableCellFormat(NumberFormats.INTEGER);
                                jxl.write.Number number = new jxl.write.Number(Integer.parseInt(ubica) - 1, registro, Integer.parseInt((String)contadores.get(nomCampoCont[cont])), integerFormat);
                                sheet.addCell(number);
                            }

                            for(int tot = 0; tot < largoTot; tot++) {
                                int suma = consulta3.getInt(nomCampoTot[tot]);
                                totales.put(nomCampoTot[tot], consulta3.getString(nomCampoTot[tot]));
                            }

                            for(int cont = 0; cont < largoCont; cont++) {
                                cuenta = 1;
                                String contador = Integer.toString(cuenta);
                                contadores.put(nomCampoCont[cont], contador);
                            }

                            for(int ord = 0; ord < largoOrder; ord++)
                                ordenadores.put(nomCampoOrder[ord], consulta3.getString(nomCampoOrder[ord]));

                            cuentaRegsConsulta++;
                            registro++;
                        } 
                        else {
                            for(int tot = 0; tot < largoTot; tot++) {
                                int suma = consulta3.getInt(nomCampoTot[tot]) + Integer.parseInt((String)totales.get(nomCampoTot[tot]));
                                totales.put(nomCampoTot[tot], Integer.toString(suma));
                            }

                            cuenta++;
                            for(int cont = 0; cont < largoCont; cont++) {
                                String contador = Integer.toString(cuenta);
                                contadores.put(nomCampoCont[cont], contador);
                            }

                        }
                    }
                String sql4 = "SELECT TITULO,UBICACION,ESNUMERICO FROM eje_ges_campoasignado ORDER BY UBICACION";
                consulta4.exec(sql4);
                while(consulta4.next()) {
                    String titulo = consulta4.getString("TITULO");
                    int t = 1;
                    while(t < consulta3.getColumnCount() + 1) {
                        if(consulta3.getColumnName(t).equals(titulo) && consulta4.getInt("UBICACION") == t)
                            if(consulta3.getString(t).equals("null")) {
                                Label label = new Label(consulta4.getInt("UBICACION") - 1, registro, "");
                                sheet.addCell(label);
                            } 
                            else 
                            if(consulta4.getString("ESNUMERICO").equals("1")) {
                                if(consulta3.getString(t).equals("0")) {
                                	jxl.write.Number valor = new jxl.write.Number(consulta4.getInt("UBICACION") - 1, registro, 0.0D);
                                    sheet.addCell(valor);
                                } 
                                else {
                                	jxl.write.Number valor = new jxl.write.Number(consulta4.getInt("UBICACION") - 1, registro, Integer.parseInt(consulta3.getString(t)), formatoCellDigI);
                                    sheet.addCell(valor);
                                }
                            } 
                            else {
                                Label valorNoNumerico = new Label(consulta4.getInt("UBICACION") - 1, registro, consulta3.getString(t));
                                sheet.addCell(valorNoNumerico);
                            }
                        t++;
                    }
                }
                cuentaRegsConsulta++;
                registro++;
            }
            if(largoOrder != 0 && largoTot != 0 || largoOrder != 0 && largoCont != 0) {
                for(int tot = 0; tot < largoTot; tot++) {
                    String ubic = (String)ubicacion.get(nomCampoTot[tot]);
                    if(Integer.parseInt((String)totales.get(nomCampoTot[tot])) == 0) {
                    	jxl.write.Number valor = new jxl.write.Number(Integer.parseInt(ubic) - 1, registro, Integer.parseInt((String)totales.get(nomCampoTot[tot])), formatoCellDigITotCero);
                        sheet.addCell(valor);
                    } 
                    else {
                    	jxl.write.Number valor = new jxl.write.Number(Integer.parseInt(ubic) - 1, registro, Integer.parseInt((String)totales.get(nomCampoTot[tot])), formatoCellDigITot);
                        sheet.addCell(valor);
                    }
                }

                for(int cont = 0; cont < largoCont; cont++) {
                    String ubica = (String)ubicCont.get(nomCampoCont[cont]);
                    WritableCellFormat integerFormat = new WritableCellFormat(NumberFormats.INTEGER);
                    jxl.write.Number number = new jxl.write.Number(Integer.parseInt(ubica) - 1, registro, Integer.parseInt((String)contadores.get(nomCampoCont[cont])), integerFormat);
                    sheet.addCell(number);
                }

            }
            workbook.write();
            workbook.close();
            consulta.close();
            consulta2.close();
            consulta3.close();
            consulta4.close();
            consultaTot.close();
            crea = true;
        }
        catch(Exception e) {
            OutMessage.OutMessagePrint("<-----ERROR en la generacion del informe de gestion: \n" + e);
            return crea;
        }
        return crea;
    }

    public String determinaStringOrderBy()
    {
        String orderBy = " order by ";
        Consulta consulta = new Consulta(con);
        String sql = "select titulo,orden from eje_ges_campoasignado order by ubicacion";
        consulta.exec(sql);
        int contRegsNoOrderBy = 0;
        int cont;
        for(cont = 0; consulta.next(); cont++)
        {
            if(consulta.getString("orden") == "1")
                contRegsNoOrderBy++;
            if(consulta.getString("orden") == "2")
                orderBy = orderBy + "[" + consulta.getString("titulo") + "] asc,";
            if(consulta.getString("orden") == "3")
                orderBy = orderBy + "[" + consulta.getString("titulo") + "] desc,";
        }

        if(contRegsNoOrderBy == cont)
            orderBy = "";
        else
        if(orderBy.endsWith(","))
            orderBy = orderBy.substring(0, orderBy.length() - 1);
        consulta.close();
        return orderBy;
    }

    public SimpleHash generaPeriodos()
    {
        SimpleHash modelRoot = new SimpleHash();
        SimpleList periodos = new SimpleList();
        String sql1 = "";
        Consulta consulta1 = new Consulta(con);
        sql1 = "SELECT DISTINCT periodo FROM eje_ges_certif_histo_liquidacion_detalle ORDER BY periodo DESC";
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

    public boolean generaCampos()
    {
        boolean tx;
        String sql1 = "";
        String sql2 = "";
        tx = false;
        try {
            Consulta consulta1 = new Consulta(con);
            Consulta consulta2 = new Consulta(con);
            sql1 = "select descripcion from eje_ges_campo order by codcampo desc";
            consulta1.exec(sql1);
            int ubicacion = 1;
            int idCampo;
            for(idCampo = 1; consulta1.next(); idCampo++)
            {
                String titulo = consulta1.getString("descripcion");
                String alias = consulta1.getString("descripcion");
                String orden;
                if(consulta1.getString("descripcion").equals("CENTRO_COSTO"))
                    orden = "2";
                else
                    orden = "1";
                String ubic = Integer.toString(ubicacion);
                String totalizador = "1";
                String numerico = "2";
                grabaDatosCamposAsignados(idCampo, titulo, alias, orden, ubic, totalizador, numerico);
                ubicacion++;
            }

            consulta1.close();
            sql2 = "SELECT DISTINCT glosa_haber FROM eje_ges_certif_histo_liquidacion_detalle where id_tp='H' ORDER BY glosa_haber";
            consulta2.exec(sql2);
            while(consulta2.next()) 
            {
                String titulo = consulta2.getString("glosa_haber");
                String alias = consulta2.getString("glosa_haber");
                String orden = "1";
                String ubic = Integer.toString(ubicacion);
                String totalizador = "3";
                String numerico = "1";
                grabaDatosCamposAsignados(idCampo, titulo, alias, orden, ubic, totalizador, numerico);
                ubicacion++;
                idCampo++;
            }
            consulta2.close();
            tx = true;
        }
        catch(Exception e) {
            OutMessage.OutMessagePrint("<-----No se pudo completar el metodo generaCampos(): " + e);
            return tx;
        }
        return tx;
    }

    public boolean grabaDatosCamposAsignados(int idCampo, String nombreCampo, String alias, String orden, String posCampo, String totalizador, String numerico) {
        boolean transaccion;
        transaccion = false;
        String sql = "";
        try {
            Consulta consulta = new Consulta(con);
            sql = "insert into eje_ges_campoasignado (idcampoasig,codplantilla,titulo,alias,orden,ubicacion,totalizador,esnumerico) values (" + idCampo + ",1,'" + nombreCampo + "','" + alias + "'," + Integer.valueOf(orden).intValue() + "," + Integer.valueOf(posCampo).intValue() + ",'" + totalizador + "','" + numerico + "')";
            if(consulta.insert(sql)) {
                transaccion = true;
            } 
            else {
                OutMessage.OutMessagePrint("<-----No se pudo registrar Tx en tabla CampoAsignado\n".concat(String.valueOf(String.valueOf(sql))));
                transaccion = false;
            }
            consulta.close();
        }
        catch(Exception e) {
            OutMessage.OutMessagePrint("<-----No se pudo registrar Tx en tabla CampoAsignado\n" + e);
            return transaccion;
        }
        return transaccion;
    }

    public void borraDataCamposAsignados() {
        String sql = "";
        Consulta consulta = new Consulta(con);
        try {
            sql = "delete from eje_ges_campoasignado";
            consulta.insert(sql);
            consulta.close();
        }
        catch(Exception e) {
            OutMessage.OutMessagePrint("<-----No se pudo borrar data campos asignados\n" + e);
        }
    }

    public boolean datosTablaCampoAsignado() {
        boolean datos = false;
        try {
            Consulta consulta = new Consulta(con);
            String sql = "SELECT * FROM eje_ges_campoasignado";
            consulta.exec(sql);
            if(consulta.next())
                datos = true;
            consulta.close();
        }
        catch(Exception e) {
            OutMessage.OutMessagePrint("<-----No se ejecutaro el metodo datosTablaCampoAsignado()\n" + e);
            return datos;
        }
        return datos;
    }

    public String obtieneCentroCostoUsuario(String rut) {
        String ccosto = "";
        try {
            Consulta cc = obtieneCentroCosto(rut);
            cc.next();
            ccosto = cc.getString("id_cco");
            cc.close();
        }
        catch(Exception e) {
            OutMessage.OutMessagePrint("<-----error en metodo obtieneCentroCostoUsuario(String rut)\n" + e);
            return ccosto;
        }
        return ccosto;
    }

    public String obtieneEmpresaUsuario(String rut) {
        String empresa = "";
        try {
            Consulta emp = obtieneEmpresa(rut);
            emp.next();
            empresa = emp.getString("cod_empresa");
            emp.close();
        }
        catch(Exception e) {
            OutMessage.OutMessagePrint("<-----error en metodo obtieneEmpresaUsuario(String rut)\n" + e);
            return empresa;
        }
        return empresa;
    }

    public Consulta obtieneEmpresa(String rut) {
        Consulta consul = new Consulta(con);
        String sql = "SELECT emp.descrip as nom_empresa, emp.empresa as cod_empresa FROM eje_ges_empresa emp, eje_ges_trabajador trab WHERE emp.empresa = trab.empresa AND trab.rut = " + rut;
        consul.exec(sql);
        return consul;
    }

    public Consulta obtieneCentroCosto(String rut) {
        Consulta consul = new Consulta(con);
        String sql = "SELECT cc.descrip as nom_centro_costo, cc.centro_costo as id_cco FROM eje_ges_centro_costo cc, eje_ges_trabajador trab WHERE cc.wp_cod_empresa = trab.empresa AND cc.centro_costo = trab.ccosto AND trab.rut = " + rut;
        consul.exec(sql);
        return consul;
    }

    Connection con;
}