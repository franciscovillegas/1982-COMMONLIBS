package cl.eje.qsmcom.modulos;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;

import cl.ejedigital.web.FreemakerTool;
import freemarker.template.SimpleHash;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.resobjects.ResourceHtml;

class CorreoRecuperaClaveFallida
  extends CorreoRecuperaClave
{
  protected String nuevoCorreo;
  protected String nuecoFono;
  
  public CorreoRecuperaClaveFallida(IOClaseWeb cw, String rutDestinatario, String nombre, String email, String password, String nuevoCorreo, String nuevoFono)
  {
    super(cw, rutDestinatario, nombre, email, password);
    
    this.nuecoFono = nuevoFono;
    this.nuevoCorreo = nuevoCorreo;
  }
  
  public String getBody()
  {
    FreemakerTool free = new FreemakerTool();
    ResourceHtml html = new ResourceHtml();
    
    SimpleHash modelRoot = new SimpleHash();
    modelRoot.put("usuario", this.rutDestinatario);
    modelRoot.put("clave", this.pass);
    
    modelRoot.put("adicional", "La persona adicionalmente entrego su correo :" + this.cw.getParamString("correo", "No definido") + " y teléfono :" + this.cw.getParamString("fono", "No definido"));
    
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
}
