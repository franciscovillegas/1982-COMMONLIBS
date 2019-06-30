package portal.com.eje.datosPlantilla;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;

public class ServletObtieneArchivoExcel extends MyHttpServlet
{

    public ServletObtieneArchivoExcel()
    {
        try
        {
            jbInit();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doGet(req, resp);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\n**** Inicio doGet: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        user = SessionMgr.rescatarUsuario(req);
        if(!user.esValido())
        {
            super.mensaje.devolverPaginaSinSesion(resp, "ERROR de Sesi&oacute;n", "Su sesi&oacute;n ha expirado o no se ha logeado.");
        } else
        {
            String nomArch = req.getParameter("archivo");
            InputStream is = getClass().getResourceAsStream("/db.properties");
            Properties fileProps = new Properties();
            try
            {
                fileProps.load(is);
            }
            catch(Exception e)
            {
                OutMessage.OutMessagePrint("No se ha podido leer el PATH para crear archivos");
            }
            String path = fileProps.getProperty("pathInforme");
            boolean exist = (new File(path + nomArch + ".xls")).exists();
            if(exist)
            {
                File f = (new File(path + nomArch + ".xls")).getAbsoluteFile();
                resp.setContentType("application/file");
                resp.setHeader("Content-Disposition", "attachment;fileName=" + nomArch + ".xls");
                InputStream in = new FileInputStream(f);
                ServletOutputStream outs = resp.getOutputStream();
                int bit = 256;
                int i = 0;
                try
                {
                    do
                    {
                        if(bit < 0)
                            break;
                        bit = in.read();
                        if(bit >= 0)
                            outs.write(bit);
                    } while(true);
                }
                catch(IOException ioe)
                {
                    ioe.printStackTrace(System.out);
                }
                outs.flush();
                outs.close();
                in.close();
            } else
            {
                OutMessage.OutMessagePrint("EL ARCHIVO NO EXISTE");
            }
            OutMessage.OutMessagePrint("\n**** FIN doGet: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        }
    }

    private void jbInit()
        throws Exception
    {
    }

    private Usuario user;
}