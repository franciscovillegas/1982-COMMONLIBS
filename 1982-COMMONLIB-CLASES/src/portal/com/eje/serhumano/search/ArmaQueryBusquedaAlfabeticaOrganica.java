package portal.com.eje.serhumano.search;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Tools;

public class ArmaQueryBusquedaAlfabeticaOrganica
{

    public ArmaQueryBusquedaAlfabeticaOrganica()
    {
        query = "";
    }

    public ArmaQueryBusquedaAlfabeticaOrganica(HttpServletRequest req, String emp)
    {
        query = "";
        String consulta = null;
        String nombre = null;
        String Bus2 = "";
        String Boton = "ape_paterno, ape_materno";
        String rut = "";
        String selUnidad = "";
        String selCargo = "";
        String empresa = "";
        String desde = "";
        String hasta = "";
        
        if(emp != null && !emp.trim().equals(""))
        {
            empresa = emp.trim();
            Bus2 = String.valueOf(String.valueOf((new StringBuilder(" AND empresa=")).append(empresa.toUpperCase()).append(" ").append(Bus2)));
        }

        if(req.getParameter("rut") != null && !req.getParameter("rut").trim().equals(""))
        {
            rut = req.getParameter("rut").trim();
            Bus2 = String.valueOf(String.valueOf((new StringBuilder(" AND rut=")).append(rut.toUpperCase()).append(" ")));
        }

        if(req.getParameter("nombre") != null && !req.getParameter("nombre").trim().equals(""))
        {
            nombre = req.getParameter("nombre").trim();
            Bus2 = String.valueOf(String.valueOf((new StringBuilder(" AND nombre like '%")).append(nombre.toUpperCase()).append("%'")));
        }

        if(req.getParameter("desde") != null && !req.getParameter("desde").trim().equals(""))
            if(req.getParameter("desde2") != null && !req.getParameter("desde2").trim().equals(""))
                if(req.getParameter("desde3") != null && !req.getParameter("desde3").trim().equals("")){
                	desde = req.getParameter("desde").toUpperCase() + "/" + req.getParameter("desde2").toUpperCase() + "/" + req.getParameter("desde3").toUpperCase();
                	Bus2 = String.valueOf(String.valueOf((new StringBuilder(" AND fecha_ingreso >='")).append(desde.toUpperCase()).append("' ").append(Bus2)));
                }
        
        if(req.getParameter("hasta") != null && !req.getParameter("hasta").trim().equals(""))
            if(req.getParameter("hasta2") != null && !req.getParameter("hasta2").trim().equals(""))
                if(req.getParameter("hasta3") != null && !req.getParameter("hasta3").trim().equals("")){
                	hasta = req.getParameter("hasta").toUpperCase() + "/" + req.getParameter("hasta2").toUpperCase() + "/" + req.getParameter("hasta3").toUpperCase();
                	Bus2 = String.valueOf(String.valueOf((new StringBuilder(" AND fecha_ingreso <='")).append(hasta.toUpperCase()).append("' ").append(Bus2)));
                }	

        if(req.getParameter("selUnidad") != null && !req.getParameter("selUnidad").trim().equals("") && !req.getParameter("selUnidad").trim().equals("-1"))
        {
            selUnidad = req.getParameter("selUnidad").trim();
            Bus2 = String.valueOf(String.valueOf((new StringBuilder(" AND unidad=")).append(selUnidad.toUpperCase()).append(" ").append(Bus2)));
        }

        if(req.getParameter("selCargo") != null && !req.getParameter("selUnidad").trim().equals("") && !req.getParameter("selCargo").trim().equals("-1"))
        {
            selCargo = req.getParameter("selCargo").trim();
            Bus2 = String.valueOf(String.valueOf((new StringBuilder(" AND cargo=")).append(selCargo.toUpperCase()).append(" ").append(Bus2)));
        }

        if(!Boton.trim().equals(""))
        {
            Boton = " ORDER BY ".concat(String.valueOf(String.valueOf(Boton)));
        }
        if(Bus2.length() > 4)
            Bus2 = Bus2.substring(4, Bus2.length());
        consulta = String.valueOf(Bus2) + String.valueOf(Boton);
        query = consulta;
    }

    public String query;
}