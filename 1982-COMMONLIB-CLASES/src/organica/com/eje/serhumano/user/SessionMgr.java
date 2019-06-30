package organica.com.eje.serhumano.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import organica.com.eje.ges.usuario.Usuario;
import organica.tools.OutMessage;

public class SessionMgr
{

    public SessionMgr()
    {
    }

    public static Usuario rescatarUsuario(HttpSession sesion)
    {
        Usuario user;
        if(sesion == null)
        {
            OutMessage.OutMessagePrint("rescatarUsuario --> Session nula");
            user = new Usuario();
        } else
        {
            try
            {
                user = (Usuario)sesion.getAttribute(sesion.getId());
            }
            catch(Exception e)
            {
                OutMessage.OutMessagePrint("rescatarUsuario Exception --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
                user = new Usuario();
            }
            if(user == null)
            {
                OutMessage.OutMessagePrint("rescatarUsuario --> Usuario nulo");
                user = new Usuario();
            }
        }
        OutMessage.OutMessagePrint("rescatarUsuario Usuario --> ".concat(String.valueOf(String.valueOf(user.toString()))));
        return user;
    }

    public static Usuario rescatarUsuario(HttpServletRequest req)
    {
        return rescatarUsuario(req.getSession(false));
    }

    public static void guardarUsuario(HttpServletRequest req, Usuario usuario)
    {
        HttpSession miSesion = req.getSession(true);
        miSesion.setAttribute(miSesion.getId(), usuario);
        OutMessage.OutMessagePrint("guardarUsuario Usuario --> ".concat(String.valueOf(String.valueOf(usuario.toString()))));
    }
}