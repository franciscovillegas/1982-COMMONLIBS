package organica.tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Vector;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.NotImplementedException;


// Referenced classes of package tools:
//            OutMessage

public class Tools
{

    public Tools()
    {
    }

    public static String leeArchivo(String ruta)
    {
        try
        {
            StringBuilder buf = new StringBuilder();
            String s = null;
            BufferedReader reader = new BufferedReader(new FileReader(ruta));
            while((s = reader.readLine()) != null) 
            {
                buf.append(s);
                buf.append("\n");
            }
            String s3 = buf.toString();
            return s3;
        }
        catch(FileNotFoundException e)
        {
            OutMessage.OutMessagePrint("leeArchivo: ".concat(String.valueOf(String.valueOf(e))));
            String s1 = "Error al recuperar los pagina";
            return s1;
        }
        catch(IOException e)
        {
            OutMessage.OutMessagePrint("leeArchivo: ".concat(String.valueOf(String.valueOf(e))));
        }
        String s2 = "Error al recuperar los pagina";
        return s2;
    }

    public static String remplaza(String origen, String que, String con)
    {
        int idx = origen.indexOf(que);
        if(idx > -1)
            origen = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(origen.substring(0, idx))))).append(con).append(origen.substring(idx + que.length(), origen.length()))));
        return origen;
    }

    public static String remplazaTodos(String origen, String que, String con)
    {
        String original = origen;
        do
        {
            String strPaso = remplaza(original, que, con);
            if(!strPaso.equals(original))
                original = strPaso;
            else
                return original;
        } while(true);
    }

    public static Cookie leerCookie(HttpServletRequest req, String cual)
    {
        Cookie misCookies[] = req.getCookies();
        Cookie cookie = null;
        if(misCookies != null)
        {
            for(int x = 0; x < misCookies.length; x++)
            {
                Cookie cookie1 = misCookies[x];
                if(cookie.getName().equals(cual))
                    cookie = cookie1;
            }

        }
        return cookie;
    }

    public static Vector separaLista(String listaValor, String separador)
    {
        Vector valor = new Vector();
        int idx;
        if(listaValor != null)
            do
            {
                idx = listaValor.indexOf(separador);
                if(idx > -1)
                {
                    valor.addElement(listaValor.substring(0, idx).trim());
                    listaValor = listaValor.substring(idx + separador.length(), listaValor.length());
                } else
                {
                    valor.addElement(listaValor.trim());
                }
            } while(idx > -1);
        return valor;
    }

    public static String creaLista(Vector listaValor, String separador, String comillas)
    {
        String lista = "";
        if(listaValor.size() > 0)
        {
            for(int x = 0; x < listaValor.size(); x++)
                lista = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(lista)))).append(comillas).append(listaValor.get(x).toString()).append(comillas).append(separador)));

            int largo = lista.length() - separador.length();
            lista = lista.substring(0, largo);
        }
        return lista;
    }

    public static boolean enviarMail(Vector para, String subject, String texto)
    {
        boolean env = true;
        for(int x = 0; x < para.size(); x++)
            if(!enviarMail((String)para.get(x), subject, texto))
                env = false;

        return env;
    }

    public static String setFormatNumber(String num)
    {
        if(num != null)
            try
            {
                num = NumberFormat.getInstance(Locale.GERMAN).format(Integer.parseInt(num)).toString();
            }
            catch(NumberFormatException e)
            {
                OutMessage.OutMessagePrint("setFormatNumber: ".concat(String.valueOf(String.valueOf(e.getMessage()))));
                num = "0";
            }
        else
            num = "0";
        return num;
    }

    public static String setFormatNumber(String num, int dec)
    {
        String valor = "";
        Vector paso = separaLista(num, ".");
        valor = setFormatNumber((String)paso.get(0));
        if(paso.size() > 1)
        {
            String valor2 = (String)paso.get(1);
            if(valor2 == null)
            {
                valor2 = "";
            } else
            {
                valor2 = valor2.trim();
                if(valor2.length() > dec)
                    valor2 = valor2.substring(0, dec);
            }
            valor = String.valueOf(valor) + String.valueOf(",".concat(String.valueOf(String.valueOf(valor2))));
        }
        return valor;
    }

    public static String setFormatNumber(float num, int dec)
    {
        return setFormatNumber(String.valueOf(num), dec);
    }

    public static String setDecimalNumber(String rut)
    {
        if(rut != null)
            try
            {
                rut = NumberFormat.getPercentInstance().format(rut);
            }
            catch(NumberFormatException e)
            {
                OutMessage.OutMessagePrint("setDecimalNumber: ".concat(String.valueOf(String.valueOf(e.getMessage()))));
                rut = "";
            }
        else
            rut = "0";
        return rut;
    }

    public static String RescataMes(int mes)
    {
        String texto = "";
        switch(mes)
        {
        case 1: // '\001'
            texto = "Enero";
            break;

        case 2: // '\002'
            texto = "Febrero";
            break;

        case 3: // '\003'
            texto = "Marzo";
            break;

        case 4: // '\004'
            texto = "Abril";
            break;

        case 5: // '\005'
            texto = "Mayo";
            break;

        case 6: // '\006'
            texto = "Junio";
            break;

        case 7: // '\007'
            texto = "Julio";
            break;

        case 8: // '\b'
            texto = "Agosto";
            break;

        case 9: // '\t'
            texto = "Septiembre";
            break;

        case 10: // '\n'
            texto = "Octubre";
            break;

        case 11: // '\013'
            texto = "Noviembre";
            break;

        case 12: // '\f'
            texto = "Diciembre";
            break;
        }
        return texto;
    }

    public static String setFormatNumber(int rut)
    {
        String Rut = NumberFormat.getInstance(Locale.GERMAN).format(rut);
        return Rut;
    }

    public static String RescataFecha(Object fecha)
    {
        String texto = "";
        SimpleDateFormat diaFormat = new SimpleDateFormat("dd");
        SimpleDateFormat mesFormat = new SimpleDateFormat("MM");
        SimpleDateFormat anoFormat = new SimpleDateFormat("yyyy");
        texto = diaFormat.format(fecha);
        texto = String.valueOf(String.valueOf(texto)).concat(" de ");
        texto = String.valueOf(texto) + String.valueOf(RescataMes(Integer.parseInt(mesFormat.format(fecha))));
        texto = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(texto)))).append(" de ").append(anoFormat.format(fecha))));
        return texto;
    }
    
    /**
     * @deprecated Se depreca por ser un método muy antiguo
     * */

    public static boolean enviarMail(String para, String subject, String texto)
    {
    	throw new NotImplementedException();
//    	
//        OutMessage.OutMessagePrint("Entra al Enviar_mail");
//        OutMessage.OutMessagePrint("para -->".concat(String.valueOf(String.valueOf(para))));
//        if(para.length() == 0)
//            return false;
//        String mailhost = "mail.tie.cl";
//        try
//        {
//            MailMessage mensaje = new MailMessage(mailhost);
//            mensaje.from("pquintev@tsis.cl");
//            mensaje.to("pquintev@tsis.cl");
//            mensaje.setSubject(subject);
//            PrintStream out = mensaje.getPrintStream();
//            out.println(texto);
//            mensaje.sendAndClose();
//            OutMessage.OutMessagePrint("Se mando el correo electr\363nico");
//            boolean flag1 = true;
//            return flag1;
//        }
//        catch(IOException e)
//        {
//            OutMessage.OutMessagePrint("Error en mandar el mail ".concat(String.valueOf(String.valueOf(e.getMessage()))));
//        }
//        boolean flag = false;
//        return flag;
    }

    /**
     * @deprecated Se depreca por ser un método muy antiguo
     * */
    public static boolean enviarMail(String de, String para, String subject, String texto)
    {
    	
    	throw new NotImplementedException();
//    	
//        OutMessage.OutMessagePrint("Entra al Enviar_mail");
//        OutMessage.OutMessagePrint("para -->".concat(String.valueOf(String.valueOf(para))));
//        if(para.length() == 0)
//            return false;
//        String mailhost = "mail.tie.cl";
//        try
//        {
//            MailMessage mensaje = new MailMessage(mailhost);
//            mensaje.from(de);
//            mensaje.to(para);
//            mensaje.setSubject(subject);
//            PrintStream out = mensaje.getPrintStream();
//            out.println(texto);
//            mensaje.sendAndClose();
//            OutMessage.OutMessagePrint("Se mando el correo electr\363nico");
//            boolean flag1 = true;
//            return flag1;
//        }
//        catch(IOException e)
//        {
//            OutMessage.OutMessagePrint("Error en mandar el mail ".concat(String.valueOf(String.valueOf(e.getMessage()))));
//        }
//        boolean flag = false;
//        return flag;
    }
}