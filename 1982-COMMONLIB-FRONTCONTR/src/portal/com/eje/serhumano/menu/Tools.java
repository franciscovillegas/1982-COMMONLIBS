package portal.com.eje.serhumano.menu;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Vector;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import cl.ejedigital.tool.validar.Validar;


public class Tools
{

    public Tools()
    {
    }

    public static String leeArchivo(String s)
    {
        StringBuilder StringBuilder = new StringBuilder();
        String s2 = null;
        try
        {
            BufferedReader bufferedreader = new BufferedReader(new FileReader(s));
            while((s2 = bufferedreader.readLine()) != null) 
            {
                StringBuilder.append(s2);
                StringBuilder.append("\n");
            }
            String s4 = StringBuilder.toString();
            return s4;
        }
        catch(FileNotFoundException e)
        {
            System.err.println(e);
            return "";
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
        return "";
    }

    public static String remplaza(String s, String s1, String s2)
    {
        int i = s.indexOf(s1);
        if(i > -1)
            s = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(s.substring(0, i))))).append(s2).append(s.substring(i + s1.length(), s.length()))));
        return s;
    }

    public static String remplazaTodos(String s, String s1, String s2)
    {
        String s3 = s;
        do
        {
            String s4 = remplaza(s3, s1, s2);
            if(!s4.equals(s3))
                s3 = s4;
            else
                return s3;
        } while(true);
    }

    public static Cookie leerCookie(HttpServletRequest httpservletrequest, String s)
    {
        Cookie acookie[] = httpservletrequest.getCookies();
        Cookie cookie = null;
        if(acookie != null)
        {
            for(int i = 0; i < acookie.length; i++)
            {
                Cookie cookie1 = acookie[i];
                if(cookie.getName().equals(s))
                    cookie = cookie1;
            }

        }
        return cookie;
    }

    public static Vector separaLista(String s, String s1)
    {
        Vector vector = new Vector();
        int i;
        if(s != null)
            do
            {
                i = s.indexOf(s1);
                if(i > -1)
                {
                    vector.addElement(s.substring(0, i).trim());
                    s = s.substring(i + s1.length(), s.length());
                } else
                {
                    vector.addElement(s.trim());
                }
            } while(i > -1);
        return vector;
    }

    public static String creaLista(Vector vector, String s, String s1)
    {
        String s2 = "";
        if(vector.size() > 0)
        {
            for(int i = 0; i < vector.size(); i++)
                s2 = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(s2)))).append(s1).append(vector.get(i).toString()).append(s1).append(s)));

            int j = s2.length() - s.length();
            s2 = s2.substring(0, j);
        }
        return s2;
    }

    public static boolean enviarMail(Vector vector, String s, String s1)
    {
        boolean flag = true;
        return flag;
    }

    public static String setFormatNumber(String s)
    {
        if(s != null)
            try
            {
                s = NumberFormat.getInstance(Locale.GERMAN).format(new BigDecimal(s)).toString();
            }
            catch(NumberFormatException numberformatexception)
            {
                System.out.println("setFormatNumber: ".concat(String.valueOf(String.valueOf(numberformatexception.getMessage()))));
                s = "0";
            }
        else
            s = "0";
        return s;
    }

    public static String setFormatNumberFloat(String s) {
        if(s != null)
            try {
                s = NumberFormat.getInstance(Locale.GERMAN).format(Float.parseFloat(s)).toString();
            }
            catch(NumberFormatException numberformatexception) {
                System.out.println("setFormatNumber: ".concat(String.valueOf(String.valueOf(numberformatexception.getMessage()))));
                s = "0";
            }
        else {
        	s = "0";
        }
            
        return s;
    }
    
    public static double setRedondear( double numero, int decimales ) {
        return Math.round(numero*Math.pow(10,decimales))/Math.pow(10,decimales);
    }
    
    public static double setRedondear( String numero, int decimales ) {
    	Double d = Double.valueOf(Validar.getInstance().validarDouble(numero,0)).doubleValue();
        return Math.round(d*Math.pow(10,decimales))/Math.pow(10,decimales);
    }

    public static String setRedondearStr( String numero, int decimales ) {
    	String resultado = "0";
    	Double d = Double.valueOf(numero).doubleValue();
    	resultado = Integer.toString((int) Math.floor(Math.round(d*Math.pow(10,decimales))/Math.pow(10,decimales)));
    	return resultado;
    }
    
    public static String setFormatNumber(String s, int i)
    {
        String s1 = "";
        Vector vector = separaLista(s, ".");
        s1 = setFormatNumber((String)vector.get(0));
        if(vector.size() > 1)
        {
            String s2 = (String)vector.get(1);
            if(s2 == null)
            {
                s2 = "";
            } else
            {
                s2 = s2.trim();
                if(s2.length() > i)
                    s2 = s2.substring(0, i);
            }
            s1 = String.valueOf(s1) + String.valueOf(",".concat(String.valueOf(String.valueOf(s2))));
        }
        return s1;
    }

    public static String setFormatNumber(float f, int i)
    {
        return setFormatNumber(String.valueOf(f), i);
    }

    public static String setDecimalNumber(String s)
    {
        if(s != null)
            try
            {
                s = NumberFormat.getPercentInstance().format(s);
            }
            catch(NumberFormatException numberformatexception)
            {
                System.out.println("setDecimalNumber: ".concat(String.valueOf(String.valueOf(numberformatexception.getMessage()))));
                s = "";
            }
        else
            s = "0";
        return s;
    }

    public static String RescataMes(int i)
    {
        String s = "";
        switch(i)
        {
        case 1: // '\001'
            s = "Enero";
            break;

        case 2: // '\002'
            s = "Febrero";
            break;

        case 3: // '\003'
            s = "Marzo";
            break;

        case 4: // '\004'
            s = "Abril";
            break;

        case 5: // '\005'
            s = "Mayo";
            break;

        case 6: // '\006'
            s = "Junio";
            break;

        case 7: // '\007'
            s = "Julio";
            break;

        case 8: // '\b'
            s = "Agosto";
            break;

        case 9: // '\t'
            s = "Septiembre";
            break;

        case 10: // '\n'
            s = "Octubre";
            break;

        case 11: // '\013'
            s = "Noviembre";
            break;

        case 12: // '\f'
            s = "Diciembre";
            break;
        }
        return s;
    }

    public static String setFormatNumber(int i)
    {
        String s = NumberFormat.getInstance(Locale.GERMAN).format(i);
        return s;
    }

    public static String RescataFecha(Object obj)
    {
        String s = "";
        
    	if(obj != null) {
    		SimpleDateFormat simpledateformat = new SimpleDateFormat("dd");
            SimpleDateFormat simpledateformat1 = new SimpleDateFormat("MM");
            SimpleDateFormat simpledateformat2 = new SimpleDateFormat("yyyy");
            s = simpledateformat.format(obj);
            s = String.valueOf(String.valueOf(s)).concat(" de ");
            s = String.valueOf(s) + String.valueOf(RescataMes(Integer.parseInt(simpledateformat1.format(obj))));
            s = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(s)))).append(" de ").append(simpledateformat2.format(obj))));
    	}
 
        return s;
    }
}