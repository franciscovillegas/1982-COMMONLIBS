// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 24-10-2006 18:03:07
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   DistribucionDias.java

package portal.com.eje.serhumano.misdatos.sol_vacaciones.beans;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Validar;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import java.sql.Connection;

// Referenced classes of package portal.com.eje.serhumano.misdatos.sol_vacaciones.beans:
//            SaldoVacaciones

public class DistribucionDias
{

    public DistribucionDias()
    {
        solic_dias_p1 = 0;
        solic_periodo1 = "";
        solic_dias_p2 = 0;
        solic_periodo2 = "";
        solic_resto_dias = 0;
        solic_resto_peri1 = 0;
        solic_resto_peri2 = 0;
        solic_dias_auto = 0;
        dias_acum = 0;
        valido = false;
    }

    public DistribucionDias(Connection conn, int periodo_actual, int dias_solicitados, String rut)
    {
        solic_dias_p1 = 0;
        solic_periodo1 = "";
        solic_dias_p2 = 0;
        solic_periodo2 = "";
        solic_resto_dias = 0;
        solic_resto_peri1 = 0;
        solic_resto_peri2 = 0;
        solic_dias_auto = 0;
        dias_acum = 0;
        int dias_periodo = 0;
        int saldo_anterior = 0;
        Validar valida = new Validar();
        if(conn != null)
        {
            Consulta consulta = new Consulta(conn);
            SaldoVacaciones diasvaca = new SaldoVacaciones(conn, rut, periodo_actual);
            dias_periodo = Integer.parseInt(diasvaca.getDerNormal());
            SaldoVacaciones diasvacaanterior = new SaldoVacaciones(conn, rut, periodo_actual - 1);
            saldo_anterior = Integer.parseInt(diasvacaanterior.getSaldoTotal());
            valido = true;
            solic_dias_p1 = saldo_anterior;
            solic_periodo1 = String.valueOf(periodo_actual - 1);
            solic_dias_p2 = dias_solicitados - saldo_anterior;
            solic_periodo2 = String.valueOf(periodo_actual);
            solic_resto_dias = dias_periodo - solic_dias_p2;
            solic_resto_peri1 = periodo_actual - 1;
            solic_resto_peri2 = periodo_actual;
            OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("-----en Class DistribucionDias\n-----periodo actual: ")).append(periodo_actual).append("\n").append("-----dias solicitados: ").append(dias_solicitados).append("\n").append("-----rut: ").append(rut).append("\n").append("-----dias p1: ").append(solic_dias_p1).append("\n").append("-----periodo1: ").append(solic_periodo1).append("\n").append("-----dias p2: ").append(solic_dias_p2).append("\n").append("-----periodo2: ").append(solic_periodo2).append("\n").append("-----resto dias: ").append(solic_resto_dias).append("\n").append("-----resto periodo1: ").append(solic_resto_peri1).append("\n").append("-----resto periodo2: ").append(solic_resto_peri2))));
        } else
        {
            OutMessage.OutMessagePrint("en Class SaldoVacaciones : Conexion es null ");
        }
    }

    public SimpleList getCalculoDias(Connection conn, int dias_solicitados, String rut)
    {
        SimpleList simplelist = new SimpleList();
        if(conn != null)
        {
            Consulta saldos = new Consulta(conn);
            String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT periodo,rut, pen_acumulado,der_normal FROM eje_ges_vacaciones WHERE (rut = ")).append(rut).append(") ").append("ORDER by periodo ASC")));
            saldos.exec(sql);
            for(int dias_anterior = 0; saldos.next(); dias_anterior = saldos.getInt("pen_acumulado"))
            {
                if(saldos.getInt("pen_acumulado") == 0)
                    continue;
                SimpleHash simplehash1 = new SimpleHash();
                dias_acum += saldos.getInt("pen_acumulado");
                if(dias_acum > dias_solicitados)
                {
                    simplehash1.put("periodo", saldos.getString("periodo"));
                    simplehash1.put("dias", String.valueOf(dias_solicitados - dias_anterior));
                    simplelist.add(simplehash1);
                    break;
                }
                simplehash1.put("periodo", saldos.getString("periodo"));
                simplehash1.put("dias", saldos.getString("pen_acumulado"));
                simplelist.add(simplehash1);
                if(dias_acum == dias_solicitados)
                    break;
            }

            saldos.close();
        } else
        {
            OutMessage.OutMessagePrint("en Class Rut getHijos : Conexion es null ");
        }
        return simplelist;
    }

    public int getDiasPeriodo1()
    {
        return solic_dias_p1;
    }

    public String getPeriodo1()
    {
        return solic_periodo1;
    }

    public int getDiasPeriodo2()
    {
        return solic_dias_p2;
    }

    public String getPeriodo2()
    {
        return solic_periodo2;
    }

    public int getRestoDias()
    {
        return solic_resto_dias;
    }

    public int getRestoPeriodo1()
    {
        return solic_resto_peri1;
    }

    public int getRestoPeriodo2()
    {
        return solic_resto_peri2;
    }

    public int getDiasAuto()
    {
        return solic_dias_auto;
    }

    private int solic_dias_p1;
    private String solic_periodo1;
    private int solic_dias_p2;
    private String solic_periodo2;
    private int solic_resto_dias;
    private int solic_resto_peri1;
    private int solic_resto_peri2;
    private int solic_dias_auto;
    private int dias_acum;
    public boolean valido;
}