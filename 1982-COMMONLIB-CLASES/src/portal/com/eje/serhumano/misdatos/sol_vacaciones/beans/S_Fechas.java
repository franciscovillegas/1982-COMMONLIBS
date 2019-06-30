// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 24-10-2006 18:03:09
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   S_Fechas.java

package portal.com.eje.serhumano.misdatos.sol_vacaciones.beans;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Validar;
import freemarker.template.SimpleHash;

public class S_Fechas extends MyHttpServlet
{

    public S_Fechas()
    {
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        System.err.println("\nEntro al doPost de Calcular Fechas");
        java.sql.Connection conexion = super.connMgr.getConnection("portal");

        SimpleHash modelRoot = new SimpleHash();

        Validar valida = new Validar();
        Consulta consul = new Consulta(conexion);
        String fecha = null;
        int dias = 0;
        int pos = 0;
        OutMessage.OutMessagePrint("--> Fecha Inicio :".concat(String.valueOf(String.valueOf(req.getParameter("fechaTM")))));
        OutMessage.OutMessagePrint("--> Dias         :".concat(String.valueOf(String.valueOf(req.getParameter("diasTM")))));
        pos = req.getParameter("fechaTM").indexOf("/");
        OutMessage.OutMessagePrint("--->Pos= ".concat(String.valueOf(String.valueOf(pos))));
        if(pos != -1)
        {
            if(req.getParameter("fechaTM") != null)
            {
                fecha = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(req.getParameter("fechaTM").substring(0, 2))))).append("/").append(req.getParameter("fechaTM").substring(3, 5)).append("/").append(req.getParameter("fechaTM").substring(6, 10))));
                OutMessage.OutMessagePrint("--> Fecha inv :".concat(String.valueOf(String.valueOf(fecha))));
            }
            if(req.getParameter("diasTM") != null)
                dias = (new Integer(req.getParameter("diasTM"))).intValue() + 1;
            if(req.getParameter("NameReintegro") != null)
                modelRoot.put("NameReintegro", req.getParameter("NameReintegro"));
            if(req.getParameter("NameHasta") != null)
                modelRoot.put("NameHasta", req.getParameter("NameHasta"));
            if(fecha != null && dias != 0)
            {
                String sql = String.valueOf(String.valueOf((new StringBuilder(" SELECT TOP ")).append(dias).append(" cod, festivo, dia_sem, mes, dia, coddia, codano, codmes,").append(" fecha FROM view_calendario WHERE (fecha >= '").append(fecha).append("') and festivo is null and coddia <> 6 order by fecha asc")));
                OutMessage.OutMessagePrint("-->SQL Fecha ".concat(String.valueOf(String.valueOf(sql))));
                consul.exec(sql);
                int x = 0;
                do
                {
                    if(!consul.next())
                        break;
                    if(++x == dias - 1)
                    {
                        modelRoot.put("FechaHasta", valida.validarFecha(consul.getString("fecha")));
                        consul.next();
                        modelRoot.put("FechaReintegro", valida.validarFecha(consul.getString("fecha")));
                    }
                } while(true);
            }
        } else
        {
            if(req.getParameter("NameReintegro") != null)
                modelRoot.put("NameReintegro", req.getParameter("NameReintegro"));
            if(req.getParameter("NameHasta") != null)
                modelRoot.put("NameHasta", req.getParameter("NameHasta"));
            System.err.println("----->NameHasta: ".concat(String.valueOf(String.valueOf(req.getParameter("NameHasta")))));
            modelRoot.put("FechaHasta", "Fecha no v\341lida");
            modelRoot.put("FechaReintegro", "Fecha no v\341lida");
        }
        consul.close();
        super.retTemplate(resp,"misdatos/sol_vacaciones/fechasTM.htm",modelRoot);
        super.connMgr.freeConnection("portal", conexion);
        OutMessage.OutMessagePrint("Fin de doPost");
    }
}