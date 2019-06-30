package portal.com.eje.tools;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import freemarker.template.SimpleHash;

// Referenced classes of package portal.com.eje.tools:
//            OutMessage

public class VerComponente
{

    public VerComponente()
    {
        if(!cargado)
            cargarPropiedades();
    }

    private void cargarPropiedades()
    {
        OutMessage.OutMessagePrint("\n--> Cargando propiedades de Componentes <--");
        ResourceBundle proper = ResourceBundle.getBundle("db");
        VerComponente _tmp = this;
        verPMP = "1".equalsIgnoreCase(getValor(proper, "ver.pmp"));
        VerComponente _tmp1 = this;
        verAnexo = "1".equalsIgnoreCase(getValor(proper, "ver.anexo"));
        VerComponente _tmp2 = this;
        verContraloria = "1".equalsIgnoreCase(getValor(proper, "ver.contra"));
        OutMessage.OutMessagePrint("--> ----------------------------------- <--");
        VerComponente _tmp3 = this;
        cargado = true;
    }

    private String getValor(ResourceBundle proper, String propiedad)
    {
        String propValor = null;
        try
        {
            propValor = proper.getString(propiedad);
        }
        catch(MissingResourceException e)
        {
            OutMessage.OutMessagePrint("Exepcion : " + e.getMessage());
        }
        return propValor;
    }

    public SimpleHash getSimpleHash()
    {
        SimpleHash hash = new SimpleHash();
        hash.put("pmp", verPMP);
        hash.put("anexo", verAnexo);
        hash.put("contra", verContraloria);
        return hash;
    }

    public boolean puedeVerPMP()
    {
        VerComponente _tmp = this;
        return verPMP;
    }

    public boolean puedeVerAnexos()
    {
        VerComponente _tmp = this;
        return verAnexo;
    }

    public boolean puedeVerContraloria()
    {
        VerComponente _tmp = this;
        return verContraloria;
    }

    private static boolean cargado = false;
    private static boolean verPMP;
    private static boolean verAnexo;
    private static boolean verContraloria;

}