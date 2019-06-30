package organica.tools;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class OutMessage
{

    public OutMessage()
    {
    }

    public static void OutMessagePrint(String message)
    {
        if("1".equals(traza))
            System.out.println(message);
    }

    public static void OutMessagePrint(Object obj)
    {
        if(obj == null)
            OutMessagePrint("null");
        else
            OutMessagePrint(obj.toString());
    }

    public static void SetTraza()
    {
        ResourceBundle proper = ResourceBundle.getBundle("DataFolder");
        try
        {
            traza = proper.getString("traza.ver");
            if("".equals(traza))
                traza = "0";
            verunidades = proper.getString("unidades.ver");
            if("".equals(verunidades))
                verunidades = "1";
        }
        catch(MissingResourceException e)
        {
            System.out.println("Exepcion : ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        System.out.println("Traza  :".concat(String.valueOf(String.valueOf(traza))));
        System.out.println("UniVer :".concat(String.valueOf(String.valueOf(verunidades))));
    }

    public static String traza = "";
    public static String verunidades = "";

}