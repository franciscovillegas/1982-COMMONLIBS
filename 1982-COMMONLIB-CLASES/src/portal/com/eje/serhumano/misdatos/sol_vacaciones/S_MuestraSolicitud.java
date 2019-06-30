// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 24-10-2006 18:01:38
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   S_MuestraSolicitud.java

package portal.com.eje.serhumano.misdatos.sol_vacaciones;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.misdatos.sol_vacaciones.beans.DataSolicitud;
import portal.com.eje.serhumano.misdatos.sol_vacaciones.beans.DistribucionDias;
import portal.com.eje.serhumano.misdatos.sol_vacaciones.beans.Rut;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Tools;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class S_MuestraSolicitud extends MyHttpServlet
{

    public S_MuestraSolicitud()
    {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("Entro al doPost de MuestraSolicitud");
        doGet(req, resp);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\n**** Inicio doGet: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        java.sql.Connection ConDestino = super.connMgr.getConnection("portal");
        java.sql.Connection ConOrigen = super.connMgr.getConnection("portal");
        user = SessionMgr.rescatarUsuario(req);
        String strFechaHoy = "";
        GregorianCalendar Fecha = new GregorianCalendar();
        SimpleList simplelist = new SimpleList();
        int dia = Fecha.get(5);
        int ano = Fecha.get(1);
        int mes = Fecha.get(2) + 1;
        String template = "misdatos/sol_vacaciones/solLegalMail.htm";
        SimpleHash modelRoot = new SimpleHash();
        if(ConDestino == null)
        {
            mensaje.devolverPaginaMensage(resp, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
        } else
        {
            insTracking(req, "Mustra de Solicitud".intern(), null);
            strFechaHoy = String.valueOf(String.valueOf((new StringBuilder("Santiago, ")).append(dia).append(" de ").append(Tools.RescataMes(mes)).append(" de ").append(ano)));
            modelRoot.put("fecha", strFechaHoy);
            String id_solic = "";
            String sgte = req.getParameter("sgte");
            System.err.println("-------->Sgte= ".concat(String.valueOf(String.valueOf(sgte))));
            int largolista = 0;
            String responsable = "";
            String poder = "";
            String listado[] = req.getParameterValues("listado");
            if(listado == null)
                largolista = 0;
            else
                largolista = listado.length;
            int x = 0;
            if(largolista > 0)
            {
                for(x = 0; x < largolista; x++)
                {
                    SimpleHash simplehash1 = new SimpleHash();
                    id_solic = listado[x];
                    simplehash1.put("id", id_solic);
                    simplelist.add(simplehash1);
                }

                x = 0;
                do
                {
                    if(x >= largolista)
                        break;
                    id_solic = listado[x];
                    if(id_solic.equals(sgte) || sgte.equals("0"))
                    {
                        DataSolicitud datosSolic = new DataSolicitud(ConDestino, id_solic);
                        Rut datosDestino = new Rut(ConDestino, datosSolic.getRutDestino());
                        modelRoot.put("id_solic", datosSolic.getId());
                        modelRoot.put("fec_solic", datosSolic.getFecha());
                        modelRoot.put("hora_solic", datosSolic.getHora());
                        modelRoot.put("pago_bono", datosSolic.getPagoBono());
                        responsable = datosSolic.getRutDestino();
                        poder = datosSolic.getRutPoder();
                        if(!responsable.equals(poder))
                        {
                            Rut datosPoder = new Rut(ConDestino, poder);
                            modelRoot.put("rut_poder", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(poder))))).append("-").append(datosPoder.Digito_ver))));
                            modelRoot.put("nom_poder", datosPoder.Nombre_completo);
                            modelRoot.put("poder", "1");
                        }
                        modelRoot.put("rut_dest", datosSolic.getRutDestino());
                        modelRoot.put("rut_label", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(datosSolic.getRutDestino()))))).append("-").append(datosDestino.Digito_ver))));
                        modelRoot.put("nom_jefe", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(datosDestino.Nombres)))).append(" ").append(datosDestino.Ap_paterno).append(" ").append(datosDestino.Ap_materno))));
                        modelRoot.put("cargo_jefe", datosDestino.Cargo_desc);
                        modelRoot.put("tipo_sol", datosSolic.getTipo());
                        if(datosSolic.getTipo().equals("1"))
                        {
                            template = "misdatos/sol_vacaciones/solLegalMail.htm";
                            modelRoot.put("inicio", datosSolic.getFechaInicioVaca());
                            modelRoot.put("finvaca", datosSolic.getFinVaca());
                        } else
                        {
                            template = "misdatos/sol_vacaciones/solProgMail.htm";
                        }
                        if(datosSolic.getEstado().equals("0") && "6".equals(datosSolic.getSubStatus()))
                            modelRoot.put("mantener", "1");
                        Rut datos_user = new Rut(ConOrigen, user.getRutId());
                        modelRoot.put("unidad", datos_user.Unidad_desc);
                        DistribucionDias calculadias = new DistribucionDias();
                        modelRoot.put("detalledias", calculadias.getCalculoDias(ConOrigen, datosSolic.getDiasSolic(), datosSolic.getRutDestino()));
                        modelRoot.put("diasp1", String.valueOf(datosSolic.getDiasPeriodo1()));
                        modelRoot.put("dias", String.valueOf(datosSolic.getDiasSolic()));
                        modelRoot.put("fec_curso", datosSolic.getFechaCurso());
                        modelRoot.put("hora_curso", datosSolic.getHoraCurso());
                        modelRoot.put("periodo1", datosSolic.getPeriodo1());
                        modelRoot.put("diasp2", String.valueOf(datosSolic.getDiasPeriodo2()));
                        modelRoot.put("periodo2", datosSolic.getPeriodo2());
                        modelRoot.put("restodias", String.valueOf(datosSolic.getRestoDias()));
                        modelRoot.put("restoperi1", String.valueOf(datosSolic.getRestoPeriodo1()));
                        modelRoot.put("restoperi2", String.valueOf(datosSolic.getRestoPeriodo2()));
                        Rut datosRutTrab = new Rut(ConDestino, datosSolic.getRutSolic());
                        modelRoot.put("rut_trabajador", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(datosSolic.getRutSolic()))))).append("-").append(datosRutTrab.Digito_ver))));
                        modelRoot.put("nom_trabajador", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(datosRutTrab.Nombres)))).append(" ").append(datosRutTrab.Ap_paterno).append(" ").append(datosRutTrab.Ap_materno))));
                        if(++x < largolista)
                            modelRoot.put("sgte", listado[x]);
                        else
                            modelRoot.put("fin", "0");
                        break;
                    }
                    x++;
                } while(true);
            } else
            {
                OutMessage.OutMessagePrint("Listado Vacio!!!!!!");
            }
            modelRoot.put("detalle", simplelist);
            super.retTemplate(resp,template,modelRoot);
        }
        OutMessage.OutMessagePrint("\n**** Fin doGet: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        super.connMgr.freeConnection("portal", ConOrigen);
        super.connMgr.freeConnection("portal", ConDestino);
    }

    private void devolverPaginaMensage(HttpServletResponse resp, String msg)
        throws IOException, ServletException
    {
        SimpleHash modelRoot = new SimpleHash();
        OutMessage.OutMessagePrint("**********!!!Menu Left!!!***********");
        modelRoot.put("mensaje", msg);
        super.retTemplate(resp,"WorkFlow/mensajelogin.html",modelRoot);
    }

    private Usuario user;
    private ResourceBundle proper;
    private Mensaje mensaje;
}