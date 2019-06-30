package cl.eje.qsmcom.modulos;

import cl.ejedigital.tool.correo.ifaces.ICorreoBuilder;
import cl.ejedigital.tool.correo.vo.IVoDestinatario;
import cl.ejedigital.tool.correo.vo.VoDestinatario;
import cl.ejedigital.web.FreemakerTool;
import freemarker.template.SimpleHash;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.resobjects.ResourceHtml;

class CorreoRecuperaClave
  implements ICorreoBuilder
{
  protected IOClaseWeb cw;
  protected VoDestinatario vo;
  protected String pass;
  protected String rutDestinatario;
  protected String nombre;
  
  public CorreoRecuperaClave(IOClaseWeb cw, String rutDestinatario, String nombre, String email, String password)
  {
    this.pass = password;
    this.cw = cw;
    this.vo = new VoDestinatario(nombre, email, Message.RecipientType.TO);
    this.rutDestinatario = rutDestinatario;
    this.nombre = nombre;
  }
  
  public List<File> getArchivosAdjuntos()
  {
    List<File> f = new ArrayList();
    return f;
  }
  
  public String getAsunto()
  {
    return "PORTAL DE PERSONAS - recupera clave [".concat(this.nombre).concat("]");
  }
  
  public String getBody()
  {
    FreemakerTool free = new FreemakerTool();
    ResourceHtml html = new ResourceHtml();
    
    SimpleHash modelRoot = new SimpleHash();
    modelRoot.put("usuario", this.rutDestinatario);
    modelRoot.put("clave", this.pass);
    
    String url = this.cw.getReq().getRequestURL().toString();
    url = url.substring(0, url.lastIndexOf("/"));
    url = url.substring(0, url.lastIndexOf("/"));
    
    modelRoot.put("servidor", url);
    try
    {
      return free.templateProcess(html.getTemplate("qsmcorreo/correoRecuperaClave.html"), modelRoot);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    catch (ServletException e)
    {
      e.printStackTrace();
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
  public List<IVoDestinatario> getDestinatarios()
  {
    List<IVoDestinatario> lista = new ArrayList();
    lista.add(this.vo);
    return lista;
  }
}
