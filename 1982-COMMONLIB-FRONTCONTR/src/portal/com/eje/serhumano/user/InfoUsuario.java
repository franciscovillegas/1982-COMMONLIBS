package portal.com.eje.serhumano.user;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import portal.com.eje.tools.OutMessage;

/**
 * @deprecated
 * 
 * */
public class InfoUsuario implements Serializable {

	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -6568374163223219005L;
	public InfoUsuario(String userName, String ip)
    {
        this.userName = userName;
        this.ip = ip;
        fecha_coneccion = new Date();
    }

    public String getUserName()
    {
        return userName;
    }

    public String getIP()
    {
        return ip;
    }

    public Date getFechaConeccion()
    {
        return fecha_coneccion;
    }

    public boolean mismoOrigen(InfoUsuario iu)
    {
        boolean resul = false;
        if(ip.equals(iu.getIP()))
            resul = true;
        return resul;
    }

    public boolean equals(InfoUsuario iu)
    {
        boolean resul = getUserName().equals(iu.getUserName()) && getIP().equals(iu.getIP()) && getFechaConeccion().equals(iu.getFechaConeccion());
        OutMessage.OutMessagePrint("InfoUsuario.equals ".concat(String.valueOf(String.valueOf(resul))));
        return resul;
    }

    public String toString()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return String.valueOf(String.valueOf((new StringBuilder("(")).append(userName).append(", ").append(ip).append(", ").append(dateFormat.format(fecha_coneccion)).append(")")));
    }

    private String userName;
    private String ip;
    private Date fecha_coneccion;
}