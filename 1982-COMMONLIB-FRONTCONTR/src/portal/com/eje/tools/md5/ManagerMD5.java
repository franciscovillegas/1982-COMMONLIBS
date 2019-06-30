package portal.com.eje.tools.md5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;

import portal.com.eje.datos.Consulta;

public class ManagerMD5
{

    public ManagerMD5(Connection conex)
    {
        con = conex;
    }

    public static String encrypt(String texto)
    {
        try
        {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(texto.getBytes());
            byte result[] = md5.digest();
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < result.length; i++)
            {
                String s = Integer.toHexString(result[i]);
                int length = s.length();
                if(length >= 2)
                {
                    sb.append(s.substring(length - 2, length));
                } else
                {
                    sb.append("0");
                    sb.append(s);
                }
            }

            return sb.toString();
        }
        catch(NoSuchAlgorithmException nosuchalgorithmexception)
        {
            return "";
        }
    }

    public boolean actulizaPassUsuario(String rut, String pass)
    {
        boolean retorno = false;
        consul = new Consulta(con);
        String sql = "UPDATE eje_ges_usuario SET password_usuario = '" + encrypt(pass) + "'    WHERE login_usuario = " + rut;
        retorno = consul.insert(sql);
        return retorno;
    }

    private Connection con;
    private Consulta consul;
}