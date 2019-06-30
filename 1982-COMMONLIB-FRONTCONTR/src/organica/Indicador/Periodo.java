package organica.Indicador;

import organica.datos.Consulta;
import java.sql.Connection;
import java.util.Date;
import java.util.GregorianCalendar;
import organica.tools.OutMessage;
import organica.tools.Tools;

public class Periodo
{

    private void setPeriInt()
    {
        intMes = Integer.parseInt(mes);
        intAgno = Integer.parseInt(ano);
    }

    public Periodo(Connection con)
    {
        Periodo = "";
        mes = "";
        ano = "";
        intMes = 0;
        intAgno = 0;
        Consulta consulta = new Consulta(con);
        String sql = "select  peri_id, peri_mes, peri_ano from eje_ges_periodo where peri_id = (select max(peri_id) as a from eje_ges_periodo)";
        consulta.exec(sql);
        if(existe = consulta.next())
        {
            Periodo = consulta.getString("peri_id");
            mes = consulta.getString("peri_mes");
            ano = consulta.getString("peri_ano");
            setPeriInt();
        }
        consulta.close();
        OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("Rescata ultimo periodo de la BD --> ")).append(existe).append("  ").append(Periodo))));
    }

    public Periodo(Connection con, String strPeriodo)
    {
        Periodo = "";
        mes = "";
        ano = "";
        intMes = 0;
        intAgno = 0;
        Consulta consulta = new Consulta(con);
        String sql = "select  peri_id, peri_mes, peri_ano from eje_ges_periodo where peri_id = ".concat(String.valueOf(String.valueOf(strPeriodo)));
        consulta.exec(sql);
        if(existe = consulta.next())
        {
            Periodo = consulta.getString("peri_id");
            mes = consulta.getString("peri_mes");
            ano = consulta.getString("peri_ano");
            setPeriInt();
        } else
        {
            OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("El periodo '")).append(strPeriodo).append("' no existe en la BD"))));
        }
        consulta.close();
    }

    public Periodo(String strYear, String strMes)
    {
        Periodo = "";
        mes = "";
        ano = "";
        intMes = 0;
        intAgno = 0;
        Periodo = String.valueOf(strYear) + String.valueOf(strMes);
        mes = strMes;
        ano = strYear;
        setPeriInt();
    }

    public Periodo(Date fecha)
    {
        Periodo = "";
        this.mes = "";
        ano = "";
        intMes = 0;
        intAgno = 0;
        GregorianCalendar calendario = new GregorianCalendar();
        calendario.setTime(fecha);
        ano = String.valueOf(calendario.get(1));
        int mes = calendario.get(2) + 1;
        if(mes < 10)
            this.mes = "0".concat(String.valueOf(String.valueOf(String.valueOf(mes))));
        else
            this.mes = String.valueOf(mes);
        Periodo = String.valueOf(ano) + String.valueOf(this.mes);
        setPeriInt();
    }

    public boolean existe()
    {
        return existe;
    }

    public String getPeriodo()
    {
        return Periodo;
    }

    public String getYear()
    {
        return ano;
    }

    public String getMes()
    {
        return mes;
    }

    public String getPeriodoPalabras()
    {
        String peri_pal = Tools.RescataMes(Integer.parseInt(mes));
        peri_pal = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(peri_pal)))).append(" del ").append(ano)));
        return peri_pal;
    }

    public static String fechaToPeriodo(Date fecha)
    {
        GregorianCalendar calendario = new GregorianCalendar();
        String peri = "";
        calendario.setTime(fecha);
        int year = calendario.get(1);
        int mes = calendario.get(2) + 1;
        peri = String.valueOf(year);
        if(mes < 10)
            peri = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(peri)))).append("0").append(String.valueOf(mes))));
        else
            peri = String.valueOf(peri) + String.valueOf(String.valueOf(mes));
        OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("fechaToPeriodo:  ")).append(fecha.toString()).append(" --> ").append(peri))));
        return peri;
    }

    public boolean igual(Periodo otroPeri)
    {
        return comparar(otroPeri) == 0;
    }

    public int comparar(Periodo otroPeri)
    {
        int ret;
        if(intAgno < otroPeri.intAgno)
            ret = -1;
        else
        if(intAgno == otroPeri.intAgno)
        {
            if(intMes < otroPeri.intMes)
                ret = -1;
            else
            if(intMes == otroPeri.intMes)
                ret = 0;
            else
                ret = 1;
        } else
        {
            ret = 1;
        }
        return ret;
    }

    private String Periodo;
    private String mes;
    private String ano;
    private int intMes;
    private int intAgno;
    private boolean existe;
    public static final int COMP_ANTERIOR = -1;
    public static final int COMP_IGUALES = 0;
    public static final int COMP_POSTERIOR = 1;
}