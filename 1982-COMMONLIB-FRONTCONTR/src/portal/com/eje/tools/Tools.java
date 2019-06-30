package portal.com.eje.tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.NotImplementedException;



// Referenced classes of package portal.com.eje.tools:
//            OutMessage

public class Tools
{

    public Tools()
    {
    }

    public static String leeArchivo(String ruta)
    {
    	String s3;
        StringBuilder buf = new StringBuilder();
        String s = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(ruta));
            while((s = reader.readLine()) != null) 
            {
                buf.append(s);
                buf.append("\n");
            }
            s3 = buf.toString();
        }
        catch(FileNotFoundException e) {
            System.out.println("leeArchivo: ".concat(String.valueOf(String.valueOf(e))));
            String s1 = "Error al recuperar los pagina";
            return s1;
        }
        catch(Exception e) {
            System.out.println("leeArchivo: ".concat(String.valueOf(String.valueOf(e))));
            String s2 = "Error al recuperar los pagina";
            return s2;
        }

        return s3;
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
        OutMessage.OutMessagePrint(listaValor);
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

    public static String setFormatNumber(String rut)
    {
        if(rut != null)
            try
            {
                rut = NumberFormat.getInstance(Locale.GERMAN).format(Integer.parseInt(rut)).toString();
            }
            catch(NumberFormatException e)
            {
                rut = "";
            }
        else
            rut = "0";
        return rut;
    }

    public static String setFormatNumberFloat(String rut)
    {
        if(rut != null)
            try
            {
                rut = NumberFormat.getInstance(Locale.GERMAN).format(Float.parseFloat(rut)).toString();
            }
            catch(NumberFormatException e)
            {
                rut = "";
            }
        else
            rut = "0";
        return rut;
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
    
    
    public static String rescataDiaSemana(int dia)
    {
        String texto = "";
        switch(dia)
        {
        case 1: // '\001'
            texto = "Lunes";
            break;

        case 2: // '\002'
            texto = "Martes";
            break;

        case 3: // '\003'
            texto = "Miercoles";
            break;

        case 4: // '\004'
            texto = "Jueves";
            break;

        case 5: // '\005'
            texto = "Viernes";
            break;

        case 6: // '\006'
            texto = "Sábado";
            break;

        case 7: // '\007'
            texto = "Domingo";
            break;        
        }
        return texto;
    }
    

    public static String setFormatNumber(int rut)
    {
        String Rut = NumberFormat.getInstance(Locale.GERMAN).format(rut);
        return Rut;
    }

    public static String Antiguedad(int nmeses, boolean conmeses)
    {
        String strAntiguedad = " ";
        int anos = nmeses / 12;
        int meses = nmeses % 12;
        OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("****ant a\361os---> ")).append(anos).append("\nmeses--->").append(meses))));
        if(anos >= 1)
            if(anos > 1)
                strAntiguedad = String.valueOf(String.valueOf(String.valueOf(anos))).concat(" a\361os ");
            else
                strAntiguedad = String.valueOf(String.valueOf(String.valueOf(anos))).concat(" a\361o ");
        if(meses == 0)
            strAntiguedad = String.valueOf(String.valueOf(String.valueOf(anos - 1))).concat(" a\361os ");
        if(conmeses && meses > 0)
            if(meses > 1)
                strAntiguedad = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(strAntiguedad)))).append(String.valueOf(meses)).append(" meses ")));
            else
                strAntiguedad = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(strAntiguedad)))).append(String.valueOf(meses)).append(" mes ")));
        return strAntiguedad;
    }

    public static String Antiguedad(int nmeses)
    {
        String strAntiguedad = " ";
        int anos = nmeses / 12;
        int meses = nmeses % 12;
        OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("****ant a\361os---> ")).append(anos).append("\nmeses--->").append(meses))));
        if(anos >= 1)
            if(anos > 1)
                strAntiguedad = String.valueOf(String.valueOf(String.valueOf(anos))).concat(" a\361os ");
            else
                strAntiguedad = String.valueOf(String.valueOf(String.valueOf(anos))).concat(" a\361o ");
        if(meses > 0)
        {
            if(meses > 1)
                strAntiguedad = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(strAntiguedad)))).append(String.valueOf(meses)).append(" meses ")));
            else
                strAntiguedad = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(strAntiguedad)))).append(String.valueOf(meses)).append(" mes ")));
        } else
        {
            strAntiguedad = String.valueOf(String.valueOf(String.valueOf(anos - 1))).concat(" a\361os ");
        }
        return strAntiguedad;
    }

    public static String RescataFecha(Object fecha)
    {
        String texto = "";
        if (fecha != null) {
            SimpleDateFormat diaFormat = new SimpleDateFormat("dd");
            SimpleDateFormat mesFormat = new SimpleDateFormat("MM");
            SimpleDateFormat anoFormat = new SimpleDateFormat("yyyy");
            texto = diaFormat.format(fecha);
            texto = String.valueOf(String.valueOf(texto)).concat(" de ");
            texto = String.valueOf(texto) + String.valueOf(RescataMes(Integer.parseInt(mesFormat.format(fecha))));
            texto = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(texto)))).append(" de ").append(anoFormat.format(fecha))));
        }
        return texto;
    }

    /**
     * @deprecated Se depreca por ser un función muy antigua, ocupa servidores de complementos
     * */
    public static boolean enviarMail(String para, String subject, String texto)
    {
    	
    	throw new NotImplementedException();
//    	
//        String mailhost;
//        OutMessage.OutMessagePrint("Entra al Enviar_mail");
//        OutMessage.OutMessagePrint("para -->".concat(String.valueOf(String.valueOf(para))));
//        if(para.length() == 0)
//            return false;
//        mailhost = "mail.complementos.cl";
//        boolean flag1;
//        try {
//            MailMessage mensaje = new MailMessage(mailhost);
//            mensaje.from("portal@complementos.cl");
//            mensaje.to("portal@complementos.cl");
//            mensaje.setSubject(subject);
//            PrintStream out = mensaje.getPrintStream();
//            out.println(texto);
//            mensaje.sendAndClose();
//            OutMessage.OutMessagePrint("Se mando el correo electr\363nico");
//            flag1 = true;
//        }
//        catch(IOException e) {
//            OutMessage.OutMessagePrint("Error en mandar el mail ".concat(String.valueOf(String.valueOf(e.getMessage()))));
//            flag1 = false;
//        }
//        return flag1;
    }

    /**
     * @deprecated Se depreca por ser un función muy antigua, ocupa servidores de complementos
     * */
    public static boolean enviarMail(String de, String para, String subject, String texto)
    {
    	throw new NotImplementedException();
    	
//    	
//        String mailhost;
//        OutMessage.OutMessagePrint("Entra al Enviar_mail");
//        OutMessage.OutMessagePrint("para -->".concat(String.valueOf(String.valueOf(para))));
//        if(para.length() == 0)
//            return false;
//        mailhost = "mail.complementos.cl";
//        boolean flag1;
//        try {
//            MailMessage mensaje = new MailMessage(mailhost);
//            mensaje.from(de);
//            mensaje.to(para);
//            mensaje.setSubject(subject);
//            PrintStream out = mensaje.getPrintStream();
//            out.println(texto);
//            mensaje.sendAndClose();
//            OutMessage.OutMessagePrint("Se mando el correo electr\363nico");
//            flag1 = true;
//        }
//        catch(IOException e) {
//            OutMessage.OutMessagePrint("Error en mandar el mail ".concat(String.valueOf(String.valueOf(e.getMessage()))));
//            flag1 = false;
//        }
//        
//        return flag1;
    }
    
    public static HashMap<String,String> resultSetToHashMap(ResultSet rs) throws SQLException {
    	HashMap<String,String> map = new HashMap<String,String>();
    	String colName = "";
    	String value = "";
    	for(int i = 1 ; i <= rs.getMetaData().getColumnCount() ; i++) {
    		colName = rs.getMetaData().getColumnName(i);
    		value = rs.getString(i);
    		map.put(colName, value);
    	}
    	return map;
    }
}