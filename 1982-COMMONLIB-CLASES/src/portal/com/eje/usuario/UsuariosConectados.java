package portal.com.eje.usuario;

import java.util.Vector;

import portal.com.eje.tools.OutMessage;

// Referenced classes of package portal.com.eje.usuario:
//            InfoUsuario

public class UsuariosConectados
{

    public UsuariosConectados()
    {
        usuarios = new Vector();
    }

    public void addUsuario(InfoUsuario iu)
    {
        usuarios.addElement(iu);
    }

    public InfoUsuario getUsuario(int indice)
    {
        return (InfoUsuario)usuarios.get(indice);
    }

    public int cantUsuarios()
    {
        return usuarios.size();
    }

    public int buscaUsuario(String username)
    {
        OutMessage.OutMessagePrint("buscaUsuario --> " + username);
        int pos = -1;
        InfoUsuario iu = null;
        int u = 0;
        do
        {
            if(u >= cantUsuarios())
                break;
            iu = getUsuario(u);
            OutMessage.OutMessagePrint("buscaUsuario --> (" + u + ") " + iu.toString());
            if(iu.getUserName().equals(username))
            {
                pos = u;
                break;
            }
            u++;
        } while(true);
        OutMessage.OutMessagePrint("buscaUsuario --> " + username + " = " + pos);
        return pos;
    }

    public boolean existeUsuario(String username)
    {
        return buscaUsuario(username) >= 0;
    }

    public void removeUsuario(String username)
    {
        int pos = buscaUsuario(username);
        if(pos != -1)
            usuarios.removeElementAt(pos);
    }

    public void removeUsuario(InfoUsuario iu)
    {
        OutMessage.OutMessagePrint("removeUsuario --> " + iu.toString());
        int pos = buscaUsuario(iu.getUserName());
        if(pos != -1 && getUsuario(pos).equals(iu))
            usuarios.removeElementAt(pos);
    }

    public String toString()
    {
        return usuarios.toString();
    }

    private Vector usuarios;
}