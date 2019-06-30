package cl.eje.qsmcom.modulos;

import java.util.ResourceBundle;

import javax.mail.MessagingException;

import cl.eje.qsmcom.managers.ManagerAcceso;
import cl.eje.qsmcom.managers.ManagerTrabajador;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.output.JSonDataOut;
import cl.ejedigital.tool.correo.CorreoDispatcher;
import cl.ejedigital.tool.correo.ifaces.ICorreoBuilder;
import cl.ejedigital.tool.correo.ifaces.ICorreoProcess;
import cl.ejedigital.tool.validar.Validar;
import freemarker.template.SimpleHash;
import portal.com.eje.frontcontroller.AbsClaseWebInsegura;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.permiso.PerfilMngr;

public class RecuperaClave
  extends AbsClaseWebInsegura
{
  public RecuperaClave(IOClaseWeb ioClaseWeb)
  {
    super(ioClaseWeb);
  }
  
  public void doPost()
    throws Exception
  {
    doGet();
  }
  
  public void doGet()
    throws Exception
  {
    String accion = super.getIoClaseWeb().getParamString("accion", "show");
    String thing = super.getIoClaseWeb().getParamString("thing", "");
    String htm = super.getIoClaseWeb().getParamString("htm", "mantenedor/mantPersonas.html");
    
    SimpleHash modelRoot = new SimpleHash();
    modelRoot.put("accion", accion);
    PerfilMngr perfil = new PerfilMngr();
    if (!"show".equals(accion)) {
      if (!"select".equals(accion)) {
        if (!"insert".equals(accion)) {
          if (!"update".equals(accion)) {
            if (!"delete".equals(accion)) {
              if ("help".equals(accion))
              {
                if ("pass".equals(thing)) {
                  enviarCorreoAdministrador();
                }
              }
              else if (("recovery".equals(accion)) && 
                ("pass".equals(thing))) {
                recuperaClave();
              }
            }
          }
        }
      }
    }
  }
  
  private void enviarCorreoAdministrador()
  {
    String rutWD = super.getIoClaseWeb().getParamString("rut", "11111111-1");
    
    int rut = Validar.getInstance().validarInt(rutWD.substring(0, rutWD.indexOf("-")), -1);
    String nuevoCorreo = super.getIoClaseWeb().getParamString("correo", "No definido");
    String nuevoFono = super.getIoClaseWeb().getParamString("fono", "No definido");
    
    boolean ok = false;
    
    ResourceBundle proper = ResourceBundle.getBundle("mail");
    
    String eMail = proper.getString("admin.mail");
    String Nombre = proper.getString("admin.nombre");
    
    ConsultaData data = ManagerAcceso.getInstance().getEjeGesUsuario(rut);
    if ((data != null) && (data.next()))
    {
      String clave = data.getString("password_usuario");
      

      data = ManagerTrabajador.getInstance().getTrabajador(rut);
      if (data.next())
      {
        ICorreoBuilder cb = new CorreoRecuperaClaveFallida(
          super.getIoClaseWeb(), 
          rutWD, 
          data.getString("nombres"), 
          eMail, 
          clave, 
          nuevoCorreo, 
          nuevoFono);
        
        ICorreoProcess cp = new CorreoProcessFresenius(cb);
        try
        {
          ok = CorreoDispatcher.getInstance().sendMail(cp);
        }
        catch (MessagingException e)
        {
          e.printStackTrace();
          ok = false;
        }
      }
    }
    DataFields dataFields = new DataFields();
    
    dataFields.put("estado", new Field(Boolean.valueOf(ok)));
    if (ok)
    {
      dataFields.put("msg", new Field("Se envió la clave al siguiente correo  :  ".concat(eMail)));
      dataFields.put("nextstep", new Field(""));
    }
    else
    {
      dataFields.put("msg", new Field("La clave no pudo ser enviada. A continuación le pediremos algunos datos y una de nuestras ejecutivas se comunicará con usted para solucionar el problema."));
      dataFields.put("nextstep", new Field(""));
    }
    DataList dataList = new DataList();
    dataList.add(dataFields);
    
    JSonDataOut out = new JSonDataOut(dataList);
    super.getIoClaseWeb().retTexto(out.getListData());
  }
  
  private void recuperaClave()
  {
    String rutWD = super.getIoClaseWeb().getParamString("rut", "11111111-1");
    
    int rut = Validar.getInstance().validarInt(rutWD.substring(0, rutWD.indexOf("-")), -1);
    boolean ok = false;
    String eMail = null;
    
    ConsultaData data = ManagerAcceso.getInstance().getEjeGesUsuario(rut);
    if ((data != null) && (data.next()))
    {
      String clave = data.getString("password_usuario");
      

      data = ManagerTrabajador.getInstance().getTrabajador(rut);
      if (data.next())
      {
        eMail = data.getString("e_mail");
        if ((eMail == null) || (eMail.length() <= 4))
        {
          ConsultaData data2 = ManagerTrabajador.getInstance().getConfirmacionCorreo(rut);
          if (data2.next()) {
            eMail = data2.getString("correo");
          }
        }
        if ((eMail != null) && (eMail.indexOf("@") != -1) && (eMail.length() > 4))
        {
          ICorreoBuilder cb = new CorreoRecuperaClave(
            super.getIoClaseWeb(), 
            rutWD, 
            data.getString("nombres"), 
            eMail, 
            clave);
          
          ICorreoProcess cp = new CorreoProcessFresenius(cb);
          try
          {
            ok = CorreoDispatcher.getInstance().sendMail(cp);
          }
          catch (MessagingException e)
          {
            e.printStackTrace();
            ok = false;
          }
        }
      }
    }
    DataFields dataFields = new DataFields();
    
    dataFields.put("estado", new Field(Boolean.valueOf(ok)));
    if (ok)
    {
      dataFields.put("msg", new Field("Se envió la clave al siguiente correo  :  ".concat(eMail)));
      dataFields.put("nextstep", new Field(""));
    }
    else
    {
      dataFields.put("msg", new Field("La clave no pudo ser enviada. A continuación le pediremos algunos datos y una de nuestras ejecutivas se comunicará con usted para solucionar el problema."));
      dataFields.put("nextstep", new Field(""));
    }
    DataList dataList = new DataList();
    dataList.add(dataFields);
    
    JSonDataOut out = new JSonDataOut(dataList);
    super.getIoClaseWeb().retTexto(out.getListData());
  }
}
