package organica.com.eje.datos;

import freemarker.template.SimpleHash;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import organica.tools.OutMessage;

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
        ResourceBundle proper = ResourceBundle.getBundle("DataFolder");
        verPMP = "1".equalsIgnoreCase(getValor(proper, "ver.pmp"));
        verAnexo = "1".equalsIgnoreCase(getValor(proper, "ver.anexo"));
        verContraloria = "1".equalsIgnoreCase(getValor(proper, "ver.contra"));
        OutMessage.OutMessagePrint("--> ----------------------------------- <--");
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
            OutMessage.OutMessagePrint("Exepcion : ".concat(String.valueOf(String.valueOf(e.getMessage()))));
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
        return verPMP;
    }

    public boolean puedeVerAnexos()
    {
        return verAnexo;
    }

    public boolean puedeVerContraloria()
    {
        return verContraloria;
    }

    private static boolean cargado = false;
    private static boolean verPMP;
    private static boolean verAnexo;
    private static boolean verContraloria;

}