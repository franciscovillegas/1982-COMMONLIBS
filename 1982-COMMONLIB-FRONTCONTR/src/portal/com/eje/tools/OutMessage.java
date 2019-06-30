package portal.com.eje.tools;

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
        ResourceBundle proper = ResourceBundle.getBundle("db");
        try
        {
            traza = proper.getString("portal.traza.ver");
            if("".equals(traza))
                traza = "0";
        }
        catch(MissingResourceException e)
        {
            System.out.println("Exepcion : ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
    }

    private static String traza = "0";

}