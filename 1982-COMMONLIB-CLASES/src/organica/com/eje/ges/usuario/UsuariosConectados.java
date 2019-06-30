package organica.com.eje.ges.usuario;

import java.io.Serializable;
import java.util.Vector;
import organica.tools.OutMessage;

// Referenced classes of package com.eje.ges.usuario:
//            InfoUsuario

public class UsuariosConectados implements Serializable
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
        OutMessage.OutMessagePrint("buscaUsuario --> ".concat(String.valueOf(String.valueOf(username))));
        int pos = -1;
        InfoUsuario iu = null;
        int u = 0;
        do
        {
            if(u >= cantUsuarios())
                break;
            iu = getUsuario(u);
            OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("buscaUsuario --> (")).append(u).append(") ").append(iu.toString()))));
            if(iu.getUserName().equals(username))
            {
                pos = u;
                break;
            }
            u++;
        } while(true);
        OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("buscaUsuario --> ")).append(username).append(" = ").append(pos))));
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
        OutMessage.OutMessagePrint("removeUsuario --> ".concat(String.valueOf(String.valueOf(iu.toString()))));
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