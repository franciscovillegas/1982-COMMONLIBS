package portal.com.eje.serhumano.formularios;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.serhumano.datosdf.datosRut;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.SortedList;
import portal.com.eje.tools.Tools;
import portal.com.eje.tools.enviaCorreo;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class S_MaestroPersonal extends MyHttpServlet
{

    public S_MaestroPersonal()
    {
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doPost(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        generaPagina(req, resp);
    }

    protected void generaPagina(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\n**** Inicio generaPagina: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        SimpleHash modelRoot = new SimpleHash();
        SimpleList simplelist = new SimpleList();
        user = SessionMgr.rescatarUsuario(req);
        if(!user.esValido())
        {
            super.mensaje.devolverPaginaSinSesion(resp, "Archivos Maestro Personal", "Tiempo de Sesi\363n expirado...");
        } else
        {
            proper = ResourceBundle.getBundle("db");
            String directory = proper.getString("portal.db.excelPath");
            if(directory.equals("") || directory.equals(null))
            {
                OutMessage.OutMessagePrint("No existen archivos disponibles");
            } else
            {
                simplelist = ListDirectoryFile(directory);
                modelRoot.put("listFiles", simplelist);
                super.retTemplate(resp,"formularios/form_maestropersonal.htm",modelRoot);
            }
        }
        OutMessage.OutMessagePrint("\n**** Fin generaPagina: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }

    private void devolverPaginaMensage(HttpServletResponse resp, String titulo, String msg)
        throws IOException, ServletException
    {
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("titulo", titulo);
        modelRoot.put("mensaje", msg);
        super.retTemplate(resp,"Gestion/Anexos/Visor/VisorResul.htm",modelRoot);
    }

    private SimpleList ListDirectoryFile(String directory)
    {
        SimpleList list = new SimpleList();
        SortedList temp = new SortedList(true);
        int n = 0;
        File dir = new File(directory);
        String children[] = dir.list();
        if(children == null)
        {
            System.out.println("Directorio no existe");
        } else
        {
            n = children.length;
            for(int i = 0; i < n; i++)
                temp.addSort(children[i]);

            for(Iterator it = temp.getList().iterator(); it.hasNext(); list.add((String)it.next()));
        }
        return list;
    }

    private datosRut userRut;
    private enviaCorreo X;
    private ResourceBundle proper;
    private Usuario user;
    private Tools tool;
}