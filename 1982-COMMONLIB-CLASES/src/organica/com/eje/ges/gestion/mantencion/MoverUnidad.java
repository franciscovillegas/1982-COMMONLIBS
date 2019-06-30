package organica.com.eje.ges.gestion.mantencion;

import java.sql.Connection;

import organica.datos.Consulta;
import organica.tools.OutMessage;

public class MoverUnidad {

    public MoverUnidad(Connection Conexion) {
        inserta = false;
        Consulta sql = new Consulta(Conexion);
        Consulta sql2 = new Consulta(Conexion);
        Consulta sql3 = new Consulta(Conexion);
        Consulta sql4 = new Consulta(Conexion);
        Consulta sql5 = new Consulta(Conexion);
        String Query = "";
        String uni_hijo = "";
        String emp_hijo = "";
        String uni_padre = "";
        String emp_padre = "";
        Query = "SELECT unidad, empresa, unidad_padre, empresa_padre,estado FROM eje_ges_mover_jerarquia WHERE (estado = 0)";
        sql.exec(Query);
        if(sql.next()) {
            emp_padre = sql.getString("empresa_padre");
            uni_padre = sql.getString("unidad_padre");
            emp_hijo = sql.getString("empresa");
            uni_hijo = sql.getString("unidad");
            if("0".equals(uni_hijo)) {
                if("".equals(emp_padre.trim())) {
                	emp_padre = null;
                }
                if("".equals(uni_padre.trim())) {
                	uni_padre = null;
                }
                Query = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_empresa SET  padre_unidad = '")).append(uni_padre).append("', padre_empresa = '").append(emp_padre).append("' WHERE  (empresa = '").append(emp_hijo).append("') ")));
                if(sql.insert(Query)) {
                	OutMessage.OutMessagePrint("a empresa se le assigna una unidad padre, con exito!!");
                }
            } 
            else {
                if("0".equals(uni_padre)) {
                    Query = String.valueOf(String.valueOf((new StringBuilder("SELECT  COUNT(*) AS cant FROM eje_ges_jerarquia GROUP BY compania, nodo_padre HAVING (nodo_padre = '0') AND (compania = '")).append(emp_padre).append("') ")));
                    sql.exec(Query);
                    sql.next();
                    //if(sql.getInt("cant") < 1) {
                    	
                    	String nivel="1";
                    	
                        Query = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_jerarquia SET nodo_nivel=").append(nivel).append(",compania = '")).append(emp_padre).append("', nodo_padre = '0' WHERE (nodo_id = '").append(uni_hijo).append("') AND (compania = '").append(emp_hijo).append("')")));
                        if(sql3.insert(Query)) {
                            OutMessage.OutMessagePrint("Asigna unidad a una empresa, con exito!!");
                            Query = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_unidades SET unid_empresa = '")).append(emp_padre).append("' WHERE (unid_empresa = '").append(emp_hijo).append("') AND (unid_id = '").append(uni_hijo).append("')")));
                            if(sql4.insert(Query)) {
                            	OutMessage.OutMessagePrint("Actualiza tabla unidades");
                            }
                            Query = String.valueOf(String.valueOf((new StringBuilder("SELECT uni_rama FROM eje_ges_unidad_rama WHERE (tipo = 'R') AND (empresa = '")).append(emp_hijo).append("') AND (unidad = '").append(uni_hijo).append("')")));
                            sql2.exec(Query);
                            int x = 0;
                            do {
                                if(!sql2.next()) {
                                	break;
                                }
                                Query = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_unidades SET unid_empresa = '")).append(emp_padre).append("' WHERE (unid_empresa = '").append(emp_hijo).append("') AND (unid_id = '").append(sql2.getString("uni_rama")).append("')")));
                                if(sql5.insert(Query)) {
                                	x++;
                                }
                            } while(true);
                            OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("Actualiza tabla : ")).append(x).append(" unidades"))));
                            Query = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_jerarquia SET compania = '")).append(emp_padre).append("' WHERE (nodo_padre = '").append(uni_hijo).append("') AND (compania = '").append(emp_hijo).append("')")));
                            if(sql5.insert(Query)) {
                            	OutMessage.OutMessagePrint("actualizar la empresa a todas las unidades que dependan de ella");
                            }
                            //redefinir nivel para los hijos de la unidad
                            updateNivel(uni_hijo,Conexion);
                        }
                    //} 
                    //else {
                    //    OutMessage.OutMessagePrint("Ya existe una Unidad que depende directamente de la empresa ");
                    //}
                } 
                else {
                	
                	Query = String.valueOf((new StringBuilder("select (nodo_nivel+1) nivel from eje_ges_jerarquia where nodo_id = '")).append(uni_padre).append("'"));
                	sql3.exec(Query);
                	String nivel="1";
                	if(sql3.next()) {
                		nivel = sql3.getString("nivel");
                	}
                	
                    Query = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_jerarquia SET  nodo_nivel=").append(nivel).append(",nodo_padre = '")).append(uni_padre).append("' ,compania = '").append(emp_padre).append("' WHERE (nodo_id = '").append(uni_hijo).append("') AND (compania = '").append(emp_hijo).append("')")));
                    OutMessage.OutMessagePrint("Query --> ".concat(String.valueOf(String.valueOf(Query))));
                    if(sql3.insert(Query)) {
                        OutMessage.OutMessagePrint("Asigna unidad a otra unidad en la misma empresa, con exito!!");
                        Query = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_unidades SET unid_empresa = '")).append(emp_padre).append("' WHERE (unid_empresa = '").append(emp_hijo).append("') AND (unid_id = '").append(uni_hijo).append("')")));
                        if(sql4.insert(Query)) {
                            OutMessage.OutMessagePrint("Actualiza tabla unidades UP/EP <> 0");
                            Query = String.valueOf(String.valueOf((new StringBuilder("SELECT uni_rama FROM eje_ges_unidad_rama WHERE (tipo = 'R') AND (empresa = '")).append(emp_hijo).append("') AND (unidad = '").append(uni_hijo).append("')")));
                            sql2.exec(Query);
                            int x = 0;
                            do {
                                if(!sql2.next()) {
                                	break;
                                }
                                Query = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_unidades SET unid_empresa = '")).append(emp_padre).append("' WHERE (unid_empresa = '").append(emp_hijo).append("') AND (unid_id = '").append(sql2.getString("uni_rama")).append("')")));
                                if(sql5.insert(Query)) {
                                	x++;
                                }
                            } while(true);
                            OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("Actualiza tabla : ")).append(x).append(" unidades"))));
                            Query = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_jerarquia SET compania = '")).append(emp_padre).append("' WHERE (nodo_padre = '").append(uni_hijo).append("') AND (compania = '").append(emp_hijo).append("')")));
                            if(sql5.insert(Query)) {
                            	OutMessage.OutMessagePrint("actualizar la empresa a todas las unidades que dependan de ella");
                            }
                        }
                        //redefinir nivel para los hijos de la unidad
                        updateNivel(uni_hijo,Conexion);
                    }
                }
            }
            Query = "UPDATE eje_ges_mover_jerarquia SET estado=1   WHERE (estado = 0)";
            if(sql5.insert(Query)) {
            	OutMessage.OutMessagePrint("Update estado=1 !!");
            }
        }
        sql.close();
        sql2.close();
        sql3.close();
        sql4.close();
        sql5.close();
    }

    public boolean updateNivel(String unidad,Connection Conexion) {
    	
    	boolean salida=false;
    	Consulta query0 = new Consulta(Conexion);
    	Consulta query1 = new Consulta(Conexion);
    	Consulta query2 = new Consulta(Conexion);
    	Consulta update = new Consulta(Conexion);

    	String sql = "select count(*) hijos from eje_ges_jerarquia where nodo_padre='" + unidad + "'";
    	query0.exec(sql); query0.next();
    	if(!query0.getString("hijos").equals("0")) {
    		String nivel="-1";
    		sql = "select (nodo_nivel+1) nivel from eje_ges_jerarquia where nodo_id='" + unidad + "'";
    		query1.exec(sql);
    		if(query1.next()) {
    			nivel = query1.getString("nivel");
    		}
    		if(!nivel.equals("-1")) {
    			sql = "select nodo_id from eje_ges_jerarquia where nodo_padre='" + unidad + "'";
    			query2.exec(sql); 
    			for(;query2.next();) {
    				sql = "update eje_ges_jerarquia set nodo_nivel=" + nivel + " where nodo_id='" + query2.getString("nodo_id") + "'";
    				update.insert(sql);
    				updateNivel(query2.getString("nodo_id"), Conexion);
    			}
    		}
    		salida = true;
    	}
    	else {
    		salida=false;
    	}
    	return salida;
    }
    
    public boolean InsertUnidad(Connection Conexion) {
        Consulta update = new Consulta(Conexion);
        return false;
    }

    public boolean InsertEmpresa(Connection Conexion) {
        Consulta update = new Consulta(Conexion);
        return false;
    }

    public boolean inserta;
}