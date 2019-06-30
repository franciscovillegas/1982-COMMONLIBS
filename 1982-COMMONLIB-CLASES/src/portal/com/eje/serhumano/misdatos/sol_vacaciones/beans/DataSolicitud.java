// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 24-10-2006 18:03:05
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   DataSolicitud.java

package portal.com.eje.serhumano.misdatos.sol_vacaciones.beans;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Validar;
import java.sql.Connection;

public class DataSolicitud
{

    public DataSolicitud()
    {
        solic_id = "";
        solic_status = "";
        solic_rut = "";
        solic_fecha = "";
        solic_hora = "";
        solic_fec_curso = "";
        solic_hora_curso = "";
        solic_fecha_termino = "";
        solic_tipo = "";
        solic_pago_bono = "";
        solic_dias = 0;
        solic_fecha_iniciovaca = "";
        solic_fecha_finvaca = "";
        solic_rut_destino = "";
        solic_dias_p1 = 0;
        solic_periodo1 = "";
        solic_dias_p2 = 0;
        solic_periodo2 = "";
        solic_resto_dias = 0;
        solic_resto_peri1 = 0;
        solic_resto_peri2 = 0;
        solic_dias_auto = 0;
        solic_sub_status = "";
        solic_unidad = "";
        rut_gestor = "";
        rut_poder = "";
        valido = false;
    }

    public DataSolicitud(Connection conn, String id_solic)
    {
        solic_id = "";
        solic_status = "";
        solic_rut = "";
        solic_fecha = "";
        solic_hora = "";
        solic_fec_curso = "";
        solic_hora_curso = "";
        solic_fecha_termino = "";
        solic_tipo = "";
        solic_pago_bono = "";
        solic_dias = 0;
        solic_fecha_iniciovaca = "";
        solic_fecha_finvaca = "";
        solic_rut_destino = "";
        solic_dias_p1 = 0;
        solic_periodo1 = "";
        solic_dias_p2 = 0;
        solic_periodo2 = "";
        solic_resto_dias = 0;
        solic_resto_peri1 = 0;
        solic_resto_peri2 = 0;
        solic_dias_auto = 0;
        solic_sub_status = "";
        solic_unidad = "";
        rut_gestor = "";
        rut_poder = "";
        String sql = "";
        Validar valida = new Validar();
        if(conn != null)
        {
            Consulta consulta = new Consulta(conn);
            sql = String.valueOf(String.valueOf((new StringBuilder("SELECT sol_id,sol_status,sol_rut_solic, sol_fecha, sol_fec_ter,sol_tipo, sol_dias, sol_fec_inicio, sol_fec_fin,sol_rut_dest,sol_rut_ppoder,sol_fec_en_curso,sol_dias_p1, sol_periodo1,sol_dias_p2, sol_periodo2,sol_resto_dias, sol_resto_peri1,sol_resto_peri2,sol_pago_bono,sol_dias_auto, sol_sub_status, sol_unidad,sol_rut_gestor FROM eje_ges_workflow_solicitud WHERE (sol_id = ")).append(id_solic).append(")")));
            OutMessage.OutMessagePrint("---->Bean DataSolicitud:\n".concat(String.valueOf(String.valueOf(sql))));
            consulta.exec(sql);
            if(consulta.next())
            {
                valido = true;
                solic_id = consulta.getString("sol_id");
                solic_status = consulta.getString("sol_status");
                solic_rut = consulta.getString("sol_rut_solic");
                solic_fecha = valida.validarFecha(consulta.getValor("sol_fecha"));
                solic_fec_curso = valida.validarFecha(consulta.getValor("sol_fec_en_curso"));
                solic_fecha_termino = valida.validarFecha(consulta.getValor("sol_fec_ter"));
                solic_tipo = consulta.getString("sol_tipo");
                solic_dias = consulta.getInt("sol_dias");
                solic_fecha_iniciovaca = valida.validarFecha(consulta.getValor("sol_fec_inicio"));
                solic_fecha_finvaca = valida.validarFecha(consulta.getValor("sol_fec_fin"));
                solic_rut_destino = consulta.getString("sol_rut_dest");
                solic_dias_p1 = consulta.getInt("sol_dias_p1");
                solic_pago_bono = valida.validarDato(consulta.getString("sol_pago_bono"), "0");
                rut_poder = valida.validarDato(consulta.getString("sol_rut_ppoder"), "0");
                solic_periodo1 = consulta.getString("sol_periodo1");
                solic_dias_p2 = consulta.getInt("sol_dias_p2");
                solic_periodo2 = consulta.getString("sol_periodo2");
                solic_resto_dias = consulta.getInt("sol_resto_dias");
                solic_resto_peri1 = consulta.getInt("sol_resto_peri1");
                solic_resto_peri2 = consulta.getInt("sol_resto_peri2");
                solic_dias_auto = consulta.getInt("sol_dias_auto");
                solic_sub_status = consulta.getString("sol_sub_status");
                solic_unidad = consulta.getString("sol_unidad");
                rut_gestor = consulta.getString("sol_rut_gestor");
                valida.setFormatoFecha("HH:mm");
                solic_hora_curso = valida.validarFecha(consulta.getValor("sol_fec_en_curso"));
                solic_hora = valida.validarFecha(consulta.getValor("sol_fecha"));
            }
        } else
        {
            OutMessage.OutMessagePrint("en Class SaldoVacaciones : Conexion es null ");
        }
    }

    public DataSolicitud(Connection conn, String id_solic, String status, String substatus)
    {
        solic_id = "";
        solic_status = "";
        solic_rut = "";
        solic_fecha = "";
        solic_hora = "";
        solic_fec_curso = "";
        solic_hora_curso = "";
        solic_fecha_termino = "";
        solic_tipo = "";
        solic_pago_bono = "";
        solic_dias = 0;
        solic_fecha_iniciovaca = "";
        solic_fecha_finvaca = "";
        solic_rut_destino = "";
        solic_dias_p1 = 0;
        solic_periodo1 = "";
        solic_dias_p2 = 0;
        solic_periodo2 = "";
        solic_resto_dias = 0;
        solic_resto_peri1 = 0;
        solic_resto_peri2 = 0;
        solic_dias_auto = 0;
        solic_sub_status = "";
        solic_unidad = "";
        rut_gestor = "";
        rut_poder = "";
        String sql = "";
        Validar valida = new Validar();
        if(conn != null)
        {
            Consulta consulta = new Consulta(conn);
            sql = String.valueOf(String.valueOf((new StringBuilder("SELECT sol_id,sol_status,sol_rut_solic, sol_fecha, sol_fec_ter,sol_tipo, sol_dias, sol_fec_inicio, sol_fec_fin,sol_rut_dest,sol_fec_en_curso,sol_dias_p1, sol_periodo1,sol_dias_p2, sol_periodo2,sol_resto_dias, sol_resto_peri1,sol_resto_peri2,sol_pago_bono,sol_dias_auto, sol_sub_status, sol_unidad,sol_rut_gestor FROM eje_ges_workflow_solicitud WHERE (sol_id = ")).append(id_solic).append(")")));
            OutMessage.OutMessagePrint("---->Bean DataSolicitud:\n".concat(String.valueOf(String.valueOf(sql))));
            consulta.exec(sql);
            if(consulta.next())
            {
                valido = true;
                solic_id = consulta.getString("sol_id");
                solic_status = consulta.getString("sol_status");
                solic_rut = consulta.getString("sol_rut_solic");
                solic_fecha = valida.validarFecha(consulta.getValor("sol_fecha"));
                solic_fec_curso = valida.validarFecha(consulta.getValor("sol_fec_en_curso"));
                solic_fecha_termino = valida.validarFecha(consulta.getValor("sol_fec_ter"));
                solic_tipo = consulta.getString("sol_tipo");
                solic_dias = consulta.getInt("sol_dias");
                solic_pago_bono = valida.validarDato(consulta.getString("sol_pago_bono"), "0");
                solic_fecha_iniciovaca = valida.validarFecha(consulta.getValor("sol_fec_inicio"));
                solic_fecha_finvaca = valida.validarFecha(consulta.getValor("sol_fec_fin"));
                solic_rut_destino = consulta.getString("sol_rut_dest");
                solic_dias_p1 = consulta.getInt("sol_dias_p1");
                solic_periodo1 = consulta.getString("sol_periodo1");
                solic_dias_p2 = consulta.getInt("sol_dias_p2");
                solic_periodo2 = consulta.getString("sol_periodo2");
                solic_resto_dias = consulta.getInt("sol_resto_dias");
                solic_resto_peri1 = consulta.getInt("sol_resto_peri1");
                solic_resto_peri2 = consulta.getInt("sol_resto_peri2");
                solic_dias_auto = consulta.getInt("sol_dias_auto");
                solic_sub_status = consulta.getString("sol_sub_status");
                solic_unidad = consulta.getString("sol_unidad");
                rut_gestor = consulta.getString("sol_rut_gestor");
                valida.setFormatoFecha("HH:mm");
                solic_hora_curso = valida.validarFecha(consulta.getValor("sol_fec_en_curso"));
                solic_hora = valida.validarFecha(consulta.getValor("sol_fecha"));
            }
        } else
        {
            OutMessage.OutMessagePrint("en Class SaldoVacaciones : Conexion es null ");
        }
    }

    public String getId()
    {
        return solic_id;
    }

    public String getEstado()
    {
        return solic_status;
    }

    public String getRutSolic()
    {
        return solic_rut;
    }

    public String getRutPoder()
    {
        return rut_poder;
    }

    public String getPagoBono()
    {
        return solic_pago_bono;
    }

    public String getFecha()
    {
        return solic_fecha;
    }

    public String getHora()
    {
        return solic_hora;
    }

    public String getFechaCurso()
    {
        return solic_fec_curso;
    }

    public String getHoraCurso()
    {
        return solic_hora_curso;
    }

    public String getFechaAprobacionRRHH()
    {
        return solic_fecha_termino;
    }

    public String getTipo()
    {
        return solic_tipo;
    }

    public int getDiasSolic()
    {
        return solic_dias;
    }

    public String getFechaInicioVaca()
    {
        return solic_fecha_iniciovaca;
    }

    public String getFinVaca()
    {
        return solic_fecha_finvaca;
    }

    public String getRutDestino()
    {
        return solic_rut_destino;
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

    public String getSubStatus()
    {
        return solic_sub_status;
    }

    public String getUnidadSolic()
    {
        return solic_unidad;
    }

    public String getRutGestor()
    {
        return rut_gestor;
    }

    private String solic_id;
    private String solic_status;
    private String solic_rut;
    private String solic_fecha;
    private String solic_hora;
    private String solic_fec_curso;
    private String solic_hora_curso;
    private String solic_fecha_termino;
    private String solic_tipo;
    private String solic_pago_bono;
    private int solic_dias;
    private String solic_fecha_iniciovaca;
    private String solic_fecha_finvaca;
    private String solic_rut_destino;
    private int solic_dias_p1;
    private String solic_periodo1;
    private int solic_dias_p2;
    private String solic_periodo2;
    private int solic_resto_dias;
    private int solic_resto_peri1;
    private int solic_resto_peri2;
    private int solic_dias_auto;
    private String solic_sub_status;
    private String solic_unidad;
    private String rut_gestor;
    private String rut_poder;
    public boolean valido;
}