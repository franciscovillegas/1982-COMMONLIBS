package portal.com.eje.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ResourceBundle;

import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;

// Referenced classes of package portal.com.eje.tools:
//            OutMessage

public class MailOutlook
{

    public MailOutlook()
    {
        comando = "";
        proper = ResourceBundle.getBundle("db");
        comando = proper.getString("mail.program");
    }

    public void enviarMail(String para, String asunto, Template template, SimpleHash root, String ruta, String title, String servidor, 
            String mensajemail)
        throws IOException
    {
        try
        {
            FileWriter salida = null;
            ByteArrayOutputStream uddata = new ByteArrayOutputStream(40000);
            PrintWriter ud = new PrintWriter(uddata);
            try {
				template.process(root, ud);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            ud.close();
            String mensaje = uddata.toString();
            String nom_arch = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(ruta)))).append("/").append(title).append(".html")));
            File solicitud = new File(nom_arch);
            String ruta_fisica = solicitud.getAbsolutePath();
            salida = new FileWriter(solicitud);
            salida.write(mensaje);
            salida.close();
            Runtime run = Runtime.getRuntime();
            String param = String.valueOf(String.valueOf((new StringBuilder(" /t'")).append(para).append("'").append("/s'").append(asunto).append("'").append("/m' ").append(mensajemail).append("'").append("/r'").append(ruta_fisica).append("'")));
            String com_ejec = String.valueOf(comando) + String.valueOf(param);
            OutMessage.OutMessagePrint("--->comando Envio Correo--->\n ".concat(String.valueOf(String.valueOf(com_ejec))));
            OutMessage.OutMessagePrint("--------------->Enviando Mail*************");
            run.exec(com_ejec);
        }
        catch(IOException e)
        {
            System.out.println("IOException: ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
    }

    private String comando;
    private ResourceBundle proper;
}