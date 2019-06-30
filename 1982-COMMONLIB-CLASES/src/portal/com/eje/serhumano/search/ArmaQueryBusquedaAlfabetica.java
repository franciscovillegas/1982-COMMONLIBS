package portal.com.eje.serhumano.search;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Tools;

public class ArmaQueryBusquedaAlfabetica
{

    public ArmaQueryBusquedaAlfabetica()
    {
        query = "";
    }

    public ArmaQueryBusquedaAlfabetica(HttpServletRequest req)
    {
        query = "";
        String consulta = null;
        String nombre = null;
        String Bus = null;
        String Bus2 = "";
        String Boton = "";
        if(req.getParameter("rut") != null && !req.getParameter("rut").trim().equals(""))
        {
            Vector rutcompleto = new Vector();
            rutcompleto = Tools.separaLista(req.getParameter("rut"), "-");
            if(rutcompleto.size() > 1)
            {
                OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("--->Rut:    ")).append((String)rutcompleto.elementAt(0)).append("\n").append("--->Digito: ").append((String)rutcompleto.elementAt(1)))));
                Bus2 = String.valueOf(String.valueOf((new StringBuilder(" AND rut ='")).append((String)rutcompleto.elementAt(0)).append("'").append(Bus2)));
                Bus2 = String.valueOf(String.valueOf((new StringBuilder(" AND digito_ver ='")).append((String)rutcompleto.elementAt(1)).append("'").append(Bus2)));
            }
        }
        if(req.getParameter("nombre") != null && !req.getParameter("nombre").trim().equals(""))
            Bus2 = String.valueOf(String.valueOf((new StringBuilder(" AND nombres LIKE'%")).append(req.getParameter("nombre").toUpperCase()).append("%'").append(Bus2)));
        if(req.getParameter("apaterno") != null && !req.getParameter("apaterno").trim().equals(""))
            Bus2 = String.valueOf(String.valueOf((new StringBuilder(" AND ape_paterno LIKE '%")).append(req.getParameter("apaterno").toUpperCase()).append("%'").append(Bus2)));
        if(req.getParameter("amaterno") != null && !req.getParameter("amaterno").trim().equals(""))
            Bus2 = String.valueOf(String.valueOf((new StringBuilder(" AND ape_materno LIKE '%")).append(req.getParameter("amaterno").toUpperCase()).append("%'").append(Bus2)));
        if(req.getParameter("unidad") != null && !req.getParameter("unidad").trim().equals("") && !req.getParameter("unidad").trim().equals("Todas"))
            Bus2 = String.valueOf(String.valueOf((new StringBuilder(" AND unidad ='")).append(req.getParameter("unidad").toUpperCase()).append("' ").append(Bus2)));
        String sel1 = "";
        String sel2 = "";
        String sel3 = "";
        String sel4 = "";
        if(req.getParameter("sel1") != null && !req.getParameter("sel1").trim().equals(""))
        {
            OutMessage.OutMessagePrint("sel1  : ".concat(String.valueOf(String.valueOf(req.getParameter("sel1").trim()))));
            sel1 = req.getParameter("sel1").trim();
            if(sel1.trim().equals("Rut"))
                Boton = String.valueOf(String.valueOf(Boton)).concat(" rut,");
            if(sel1.trim().equals("Nombre"))
                Boton = String.valueOf(String.valueOf(Boton)).concat(" nombres,");
            if(sel1.trim().equals("A. Paterno"))
                Boton = String.valueOf(String.valueOf(Boton)).concat(" ape_paterno,");
            if(sel1.trim().equals("A. Materno"))
                Boton = String.valueOf(String.valueOf(Boton)).concat(" ape_materno,");
        }
        if(req.getParameter("sel2") != null && !req.getParameter("sel2").trim().equals(""))
        {
            OutMessage.OutMessagePrint("sel2 : ".concat(String.valueOf(String.valueOf(req.getParameter("sel2").trim()))));
            if(!req.getParameter("sel2").trim().equals(sel1))
            {
                sel2 = req.getParameter("sel2").trim();
                if(sel2.trim().equals("Rut"))
                    Boton = String.valueOf(String.valueOf(Boton)).concat(" rut,");
                if(sel2.trim().equals("Nombre"))
                    Boton = String.valueOf(String.valueOf(Boton)).concat(" nombres,");
                if(sel2.trim().equals("A. Paterno"))
                    Boton = String.valueOf(String.valueOf(Boton)).concat(" ape_paterno,");
                if(sel2.trim().equals("A. Materno"))
                    Boton = String.valueOf(String.valueOf(Boton)).concat(" ape_materno,");
            }
        }
        if(req.getParameter("sel3") != null && !req.getParameter("sel3").trim().equals(""))
        {
            OutMessage.OutMessagePrint("sel3  : ".concat(String.valueOf(String.valueOf(req.getParameter("sel3").trim()))));
            if(!req.getParameter("sel3").trim().equals(sel1) && !req.getParameter("sel3").trim().equals(sel2))
            {
                sel3 = req.getParameter("sel4").trim();
                if(sel3.trim().equals("Rut"))
                    Boton = String.valueOf(String.valueOf(Boton)).concat(" rut,");
                if(sel3.trim().equals("Nombre"))
                    Boton = String.valueOf(String.valueOf(Boton)).concat(" nombres,");
                if(sel3.trim().equals("A. Paterno"))
                    Boton = String.valueOf(String.valueOf(Boton)).concat(" ape_paterno,");
                if(sel3.trim().equals("A. Materno"))
                    Boton = String.valueOf(String.valueOf(Boton)).concat(" ape_materno,");
            }
        }
        if(req.getParameter("sel4") != null && !req.getParameter("sel4").trim().equals(""))
        {
            OutMessage.OutMessagePrint("sel4  : ".concat(String.valueOf(String.valueOf(req.getParameter("sel4").trim()))));
            if(!req.getParameter("sel4").trim().equals(sel1) && !req.getParameter("sel4").trim().equals(sel2) && !req.getParameter("sel4").trim().equals(sel3))
            {
                sel4 = req.getParameter("sel4").trim();
                if(sel4.trim().equals("Rut"))
                    Boton = String.valueOf(String.valueOf(Boton)).concat(" rut,");
                if(sel4.trim().equals("Nombre"))
                    Boton = String.valueOf(String.valueOf(Boton)).concat(" nombres,");
                if(sel4.trim().equals("A. Paterno"))
                    Boton = String.valueOf(String.valueOf(Boton)).concat(" ape_paterno,");
                if(sel4.trim().equals("A. Materno"))
                    Boton = String.valueOf(String.valueOf(Boton)).concat(" ape_materno,");
            }
        }
        if(!Boton.trim().equals(""))
        {
            Boton = Boton.substring(0, Boton.length() - 1);
            Boton = " ORDER BY ".concat(String.valueOf(String.valueOf(Boton)));
        }
        if(Bus2.length() > 4)
            Bus2 = Bus2.substring(4, Bus2.length());
        consulta = String.valueOf(Bus2) + String.valueOf(Boton);
        query = consulta;
    }

    public String query;
}