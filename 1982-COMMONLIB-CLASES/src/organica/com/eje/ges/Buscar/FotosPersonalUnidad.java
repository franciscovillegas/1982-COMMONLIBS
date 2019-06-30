package organica.com.eje.ges.Buscar;

import java.sql.Connection;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import organica.datos.Consulta;
import organica.tools.OutMessage;

import freemarker.template.SimpleList;

public class FotosPersonalUnidad {

    public FotosPersonalUnidad() {
        RutSup = null;
        MisionUnidad = null;
        DescUnidad = null;
    }

    public SimpleList GetFotosPersonalUnidad(Connection Conexion, String unidad, String empresa) {
        Consulta Buscar = new Consulta(Conexion);
        String consul = "select max(peri_id) as maxi from eje_ges_periodo ";
        Buscar.exec(consul);
        Buscar.next();
        String periodo = Buscar.getString("maxi");
        consul = "SELECT a.rut_encargado, a.mision, b.unid_desc FROM eje_ges_unidad_encargado a INNER JOIN eje_ges_unidad_rama c ON a.unid_id = c.uni_rama AND a.unid_empresa = c.empresa LEFT OUTER JOIN eje_ges_unidades b ON a.unid_empresa = b.unid_empresa  AND a.unid_id = b.unid_id WHERE c.unidad = '".concat(String.valueOf(unidad)).concat("' AND a.periodo = ").concat(String.valueOf(periodo)).concat(" AND a.unid_empresa = '").concat(String.valueOf(empresa)).concat("'");
        Buscar.exec(consul);
        OutMessage.OutMessagePrint("------>SELECT Organica\n ".concat(consul));
        String unidad_administrativa = "";
        String unidad_pertenencia = "";
        ResourceBundle proper = ResourceBundle.getBundle("DataFolder");
        try {
      	  unidad_administrativa = proper.getString("portal.unidad_administrativa");
      	  unidad_pertenencia = proper.getString("portal.unidad_pertenencia");
        }
        catch(MissingResourceException e) {
                OutMessage.OutMessagePrint("Excepcion : ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }

        if(unidad_administrativa.equals("0")) {
        	if(unidad_pertenencia.equals("0")) {
        		consul = "SELECT distinct rtrim(desc_cargo) as desc_cargo,foto,costo ,rut,Rtrim(nombre) as nombre FROM view_dotacion_directa_unidad_pertenencia WHERE tipo = 'U' AND unidad = '".concat(String.valueOf(unidad)).concat("' order by nombre");
        	}
        	else {
        		consul = "SELECT distinct rtrim(desc_cargo) as desc_cargo,foto,costo ,rut,Rtrim(nombre) as nombre FROM view2_dotacion_directa_unidad_administrativa WHERE empresa = '".concat(String.valueOf(empresa)).concat("' AND tipo = 'U' AND unidad = '").concat(String.valueOf(unidad)).concat("' order by nombre");
        	}
        }
        else {
        	consul = "SELECT distinct rtrim(desc_cargo) as desc_cargo,foto,costo ,rut,Rtrim(nombre) as nombre FROM view2_dotacion_directa WHERE empresa = '".concat(empresa).concat("' AND tipo = 'U' AND unidad = '").concat(String.valueOf(unidad)).concat("' order by nombre");        
        }
        System.out.println(consul);
        String strRut = null;
        if(Buscar.next()) {
            strRut = Buscar.getString("rut_encargado");
            DescUnidad = Buscar.getString("unid_desc");
            MisionUnidad = Buscar.getString("mision");
            RutSup = strRut;
        }
        Buscar.exec(consul);
        return Buscar.getSimpleList();
    }

    public String GetEncargadoUnidad(Connection Conexion, String unidad, String empresa) {
        Consulta Buscar = new Consulta(Conexion);
        String consul = "select rut_encargado from eje_ges_unidad_encargado where unid_id='".concat(unidad).concat("' and estado=1");
        Buscar.exec(consul);
        
        String rut = null;
        
        if(Buscar.next()) {
        	rut = Buscar.getString("rut_encargado");
        }
        else {
        	rut = "0";
        }
        
        Buscar.close();
        
        return rut;
    }

    public String GetFotoEncargadoUnidad(Connection Conexion, String unidad) {
        Consulta Buscar = new Consulta(Conexion);
        String consul = "SELECT a.foto FROM view_unidad_encargado AS a WHERE unid_id='".concat(unidad).concat("' and estado=1");
        Buscar.exec(consul);
        
        String foto = null;
        
        if(Buscar.next()) {
        	foto = Buscar.getString("foto");
        }
        else {
        	foto = "0";
        }
        
        Buscar.close();
        
        return foto;
        
    }
    
    public String RutSup;
    public String MisionUnidad;
    public String DescUnidad;
}