package portal.com.eje.tools.error;

import java.io.Serializable;
import java.util.Vector;

public class ResultMessage
    implements Serializable
{

    public ResultMessage()
    {
        this(false);
    }

    public ResultMessage(boolean ok)
    {
        titulo = "";
        mensaje = "";
        this.ok = false;
        detalle = new Vector();
        this.ok = ok;
    }

    public String getMensaje()
    {
        return mensaje;
    }

    public boolean isOk()
    {
        return ok;
    }

    public String getTitulo()
    {
        return titulo;
    }

    public void addDetalle(String detalle)
    {
        this.detalle.add(detalle);
    }

    public String getDetalle(int indice)
    {
        return detalle.get(indice).toString();
    }

    public int getCantDetalle()
    {
        return detalle.size();
    }

    public void setMensaje(String mensaje)
    {
        this.mensaje = mensaje;
    }

    public void setOk(boolean ok)
    {
        this.ok = ok;
    }

    public void setTitulo(String titulo)
    {
        this.titulo = titulo;
    }

    private String titulo;
    private String mensaje;
    private boolean ok;
    private Vector detalle;
}