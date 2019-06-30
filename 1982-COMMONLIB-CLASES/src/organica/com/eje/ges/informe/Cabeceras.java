package organica.com.eje.ges.informe;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.Indicador.Periodo;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class Cabeceras extends MyHttpServlet
{

    public Cabeceras()
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
        java.sql.Connection conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doPost de MainCertifLiquida(Carga Organica)");
        user = Usuario.rescatarUsuario(req);
        OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("***Rut: ")).append(user.getRutConsultado()).append("-->Es valido?: ").append(user.esValido()))));
        String sql = "";
        if(!user.esValido())
        {
            devolverPaginaMensage(resp, "Ingreso", "");
        } else
        {
            String sociedad = req.getParameter("empresa");
            String afp = req.getParameter("afp");
            String isapre = req.getParameter("isapre");
            String inicio = req.getParameter("inicio");
            String termino = req.getParameter("termino");
            int cual = Integer.parseInt(req.getParameter("cual"));
            OutMessage.OutMessagePrint("--->Cual Reporte: ".concat(String.valueOf(String.valueOf(cual))));
            SimpleHash modelRoot = new SimpleHash();
            SimpleList simplelist = new SimpleList();
            String primero = "";
            Consulta consul2 = new Consulta(conexion);
            switch(cual)
            {
            default:
                break;

            case 1: // '\001'
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT sociedad, periodo, afp as id, cabe_imagen FROM certif_cabecera_afp WHERE ((sociedad = '")).append(sociedad).append("') ").append("AND (afp = '").append(afp).append("')) ").append("AND (periodo >= ").append(inicio).append(") ").append("AND (periodo <= ").append(termino).append(") ").append("ORDER BY periodo, afp desc")));
                OutMessage.OutMessagePrint("--->Fotos: ".concat(String.valueOf(String.valueOf(sql))));
                consul2.exec(sql);
                if(consul2.next())
                {
                    SimpleHash simplehash1 = new SimpleHash();
                    primero = consul2.getString("periodo");
                    Periodo peri = new Periodo(conexion, consul2.getString("periodo"));
                    if(peri != null)
                    {
                        simplehash1.put("id", peri.getPeriodo());
                        simplehash1.put("primero", primero);
                        simplehash1.put("label", peri.getPeriodoPalabras());
                        simplehash1.put("imagen", consul2.getString("cabe_imagen"));
                        simplelist.add(simplehash1);
                    }
                    do
                    {
                        if(!consul2.next())
                            break;
                        simplehash1 = new SimpleHash();
                        peri = new Periodo(conexion, consul2.getString("periodo"));
                        if(peri != null)
                        {
                            simplehash1.put("id", peri.getPeriodo());
                            simplehash1.put("primero", primero);
                            simplehash1.put("label", peri.getPeriodoPalabras());
                            simplehash1.put("imagen", consul2.getString("cabe_imagen"));
                            simplelist.add(simplehash1);
                        }
                    } while(true);
                }
                modelRoot.put("cabeceras", simplelist);
                break;

            case 2: // '\002'
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT sociedad,periodo,salud as id,cabe_imagen FROM certif_cabecera_salud WHERE ((sociedad = '")).append(sociedad).append("') ").append("AND (salud = '").append(isapre).append("')) ").append("AND (periodo >= ").append(inicio).append(") ").append("AND (periodo <= ").append(termino).append(") ").append("ORDER BY periodo, salud desc")));
                OutMessage.OutMessagePrint("--->Fotos: ".concat(String.valueOf(String.valueOf(sql))));
                consul2.exec(sql);
                if(consul2.next())
                {
                    SimpleHash simplehash1 = new SimpleHash();
                    primero = consul2.getString("periodo");
                    Periodo peri = new Periodo(conexion, consul2.getString("periodo"));
                    if(peri != null)
                    {
                        simplehash1.put("id", peri.getPeriodo());
                        simplehash1.put("primero", primero);
                        simplehash1.put("label", peri.getPeriodoPalabras());
                        simplehash1.put("imagen", consul2.getString("cabe_imagen"));
                        simplelist.add(simplehash1);
                    }
                    do
                    {
                        if(!consul2.next())
                            break;
                        simplehash1 = new SimpleHash();
                        peri = new Periodo(conexion, consul2.getString("periodo"));
                        if(peri != null)
                        {
                            simplehash1.put("id", peri.getPeriodo());
                            simplehash1.put("primero", primero);
                            simplehash1.put("label", peri.getPeriodoPalabras());
                            simplehash1.put("imagen", consul2.getString("cabe_imagen"));
                            simplelist.add(simplehash1);
                        }
                    } while(true);
                }
                modelRoot.put("cabeceras", simplelist);
                break;
            }
            consul2.close();
            super.retTemplate(resp,"Certificados/cabecera.htm",modelRoot);
        }
        OutMessage.OutMessagePrint("Fin de doPost");
        connMgr.freeConnection("portal", conexion);
    }

    private void devolverPaginaMensage(HttpServletResponse resp, String titulo, String msg)
        throws IOException, ServletException
    {
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("titulo", titulo);
        modelRoot.put("mensaje", msg);
        super.retTemplate(resp,"mensaje.htm",modelRoot);
    }
    
    private Usuario user;
    private Mensaje mensaje;
}