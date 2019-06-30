// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 24-10-2006 18:03:09
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   SaldoVacaciones.java

package portal.com.eje.serhumano.misdatos.sol_vacaciones.beans;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Validar;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import java.sql.Connection;

public class SaldoVacaciones
{

    public SaldoVacaciones()
    {
        saldo_total = 0;
        saldo_normal = 0;
        saldo_progresivo = 0;
        saldo_convenio = 0;
        saldo_periodo = 0;
        der_normal = 0;
        ultimo_periodo = 0;
        valido = false;
    }

    public SaldoVacaciones(Connection conn, String rut)
    {
        saldo_total = 0;
        saldo_normal = 0;
        saldo_progresivo = 0;
        saldo_convenio = 0;
        saldo_periodo = 0;
        der_normal = 0;
        ultimo_periodo = 0;
        String sql = "";
        Validar valida = new Validar();
        if(conn != null)
        {
            Consulta vacaciones = new Consulta(conn);
            sql = String.valueOf(String.valueOf((new StringBuilder("SELECT periodo,rut, pen_normal, pen_progresivo, pen_acumulado,pen_convenio FROM eje_ges_vacaciones WHERE (rut = ")).append(rut).append(") order by periodo asc")));
            OutMessage.OutMessagePrint("---->Bean Saldo vacaciones:\n".concat(String.valueOf(String.valueOf(sql))));
            vacaciones.exec(sql);
            int acumulado = 0;
            while(vacaciones.next()) 
            {
                valido = true;
                saldo_periodo = 0;
                saldo_periodo = saldo_total + vacaciones.getInt("pen_normal") + vacaciones.getInt("pen_progresivo") + vacaciones.getInt("pen_convenio");
                saldo_normal += vacaciones.getInt("pen_normal");
                saldo_progresivo += vacaciones.getInt("pen_progresivo");
                saldo_convenio += vacaciones.getInt("pen_convenio");
                acumulado += vacaciones.getInt("pen_acumulado");
                ultimo_periodo = vacaciones.getInt("periodo");
            }
            saldo_total = acumulado;
        } else
        {
            OutMessage.OutMessagePrint("en Class SaldoVacaciones : Conexion es null ");
        }
    }

    public SaldoVacaciones(Connection conn, String rut, int periodo)
    {
        saldo_total = 0;
        saldo_normal = 0;
        saldo_progresivo = 0;
        saldo_convenio = 0;
        saldo_periodo = 0;
        der_normal = 0;
        ultimo_periodo = 0;
        String sql = "";
        Validar valida = new Validar();
        if(conn != null)
        {
            Consulta vacaciones = new Consulta(conn);
            sql = String.valueOf(String.valueOf((new StringBuilder("SELECT periodo,rut,pen_normal, pen_progresivo, pen_acumulado,pen_convenio,der_normal, der_progresivo, der_convenio FROM eje_ges_vacaciones WHERE (rut = ")).append(rut).append(") ").append("AND (periodo='").append(periodo).append("')")));
            vacaciones.exec(sql);
            int acumulado = 0;
            if(vacaciones.next())
            {
                valido = true;
                der_normal = vacaciones.getInt("der_normal");
                acumulado = vacaciones.getInt("pen_acumulado");
            }
            saldo_total = acumulado;
        } else
        {
            OutMessage.OutMessagePrint("en Class SaldoVacaciones : Conexion es null ");
        }
    }

    public SimpleList getDetallePeriodo(Connection conn, String rut)
    {
        SimpleList simplelist = new SimpleList();
        if(conn != null)
        {
            Consulta saldos = new Consulta(conn);
            Validar valida = new Validar();
            String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT periodo,rut,der_normal, der_progresivo,pen_normal, pen_progresivo, pen_convenio,pen_acumulado FROM eje_ges_vacaciones WHERE (rut = ")).append(rut).append(") ").append("ORDER BY periodo desc")));
            saldos.exec(sql);
            SimpleHash simplehash1;
            for(; saldos.next(); simplelist.add(simplehash1))
            {
                simplehash1 = new SimpleHash();
                simplehash1.put("rut", saldos.getString("rut"));
                simplehash1.put("periodo", saldos.getString("periodo"));
                simplehash1.put("dnormal", valida.validarDato(saldos.getString("der_normal")));
                simplehash1.put("dprog", valida.validarDato(saldos.getString("der_progresivo")));
                simplehash1.put("pnormal", valida.validarDato(saldos.getString("pen_normal")));
                simplehash1.put("pprog", valida.validarDato(saldos.getString("pen_progresivo")));
                simplehash1.put("pconv", valida.validarDato(saldos.getString("pen_convenio")));
                simplehash1.put("pacum", valida.validarDato(saldos.getString("pen_acumulado")));
            }

            saldos.close();
        } else
        {
            OutMessage.OutMessagePrint("en SalDoVacaciones Rut getdetallePeriodo : Conexion es null ");
        }
        return simplelist;
    }

    public String getSaldoTotal()
    {
        return String.valueOf(saldo_total);
    }

    public String getDerNormal()
    {
        return String.valueOf(der_normal);
    }

    public String getSaldoNormal()
    {
        return String.valueOf(saldo_normal);
    }

    public String getSaldoProgresivo()
    {
        return String.valueOf(saldo_progresivo);
    }

    public String getSaldoConvenio()
    {
        return String.valueOf(saldo_convenio);
    }

    public String getSaldoPeriodo()
    {
        return String.valueOf(saldo_periodo);
    }

    public String getUltimoPeriodo()
    {
        return String.valueOf(ultimo_periodo);
    }

    private int saldo_total;
    private int saldo_normal;
    private int saldo_progresivo;
    private int saldo_convenio;
    private int saldo_periodo;
    private int der_normal;
    private int ultimo_periodo;
    public boolean valido;
}