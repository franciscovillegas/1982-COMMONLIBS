// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 24-10-2006 18:03:09
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Rut.java

package portal.com.eje.serhumano.misdatos.sol_vacaciones.beans;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Validar;
import java.sql.Connection;

public class Rut
{

    public Rut()
    {
        Rut = null;
        Digito_ver = null;
        Nombres = null;
        Ap_paterno = null;
        Ap_materno = null;
        Nombre_completo = null;
        Sindicato_id = null;
        Sindicato_desc = null;
        Bono_vaca = null;
        tiene_bono = null;
        Mail = null;
        Foto = null;
        E_Mail = null;
        Unidad_id = null;
        Unidad_desc = null;
        Cargo_id = null;
        Cargo_desc = null;
        Estamento = null;
        Empresa_id = null;
        Empresa_desc = null;
        Sueldo_Base = null;
        Bruto_Regular = null;
        Neto_Regular = null;
        Bruto_Promedio = null;
        Neto_Promedio = null;
        Tot_Haberes = null;
        Sup_nombre = null;
        Sup_Unidad = null;
        Sup_Cargo = null;
        Sup_Mail = null;
        valido = false;
    }

    public Rut(Connection conn, String rut)
    {
        Rut = null;
        Digito_ver = null;
        Nombres = null;
        Ap_paterno = null;
        Ap_materno = null;
        Nombre_completo = null;
        Sindicato_id = null;
        Sindicato_desc = null;
        Bono_vaca = null;
        tiene_bono = null;
        Mail = null;
        Foto = null;
        E_Mail = null;
        Unidad_id = null;
        Unidad_desc = null;
        Cargo_id = null;
        Cargo_desc = null;
        Estamento = null;
        Empresa_id = null;
        Empresa_desc = null;
        Sueldo_Base = null;
        Bruto_Regular = null;
        Neto_Regular = null;
        Bruto_Promedio = null;
        Neto_Promedio = null;
        Tot_Haberes = null;
        Sup_nombre = null;
        Sup_Unidad = null;
        Sup_Cargo = null;
        Sup_Mail = null;
        String sql = "";
        Validar valida = new Validar();
        if(conn != null)
        {
            Consulta consul = new Consulta(conn);
            Consulta consul2 = new Consulta(conn);
            sql = String.valueOf(String.valueOf((new StringBuilder("SELECT vista_empl.rut, vista_empl.digito,vista_empl.nombres, vista_empl.paterno, vista_empl.materno,vista_empl.id_sindicato, vista_empl.desc_sindicato,vista_empl.tiene_bono, vista_empl.bono_vaca,vista_empl.e_mail, vista_empl.mail, vista_empl.unid_id,vista_empl.unid_desc, vista_empl.cargo_id,vista_empl.cargo_desc, vista_empl.empresa_id,vista_empl.empresa_desc,vista_empl.rut_supdirecto,vista_empl.dig_supdirecto,vista_empl.cargo_supdirecto,vista_empl.nom_supdirecto,vista_empl.sueldo_base, vista_empl.bruto_regular, vista_empl.neto_regular, vista_empl.bruto_promedio,vista_empl.neto_promedio, vista_empl.tot_haberes,vista_emp_1.e_mail AS sup_mail,vista_emp_1.unid_desc AS sup_unidad, eje_ges_foto_trab.id_foto FROM view_workflow_vaca_empleados vista_empl INNER JOIN eje_ges_foto_trab ON vista_empl.rut = eje_ges_foto_trab.rut LEFT OUTER JOIN view_workflow_vaca_empleados vista_emp_1 ON vista_empl.rut_supdirecto = vista_emp_1.rut WHERE (vista_empl.rut = ")).append(rut).append(")")));
            consul.exec(sql);
            if(consul.next())
            {
                valido = true;
                Rut = consul.getString("rut").trim();
                Digito_ver = consul.getString("digito");
                Nombres = consul.getString("nombres");
                Foto = consul.getString("id_foto").toLowerCase();
                Ap_paterno = consul.getString("paterno");
                Ap_materno = consul.getString("materno");
                Nombre_completo = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Nombres)))).append(" ").append(Ap_paterno).append(" ").append(Ap_materno)));
                Sindicato_id = consul.getString("id_sindicato");
                Sindicato_desc = consul.getString("desc_sindicato");
                Bono_vaca = consul.getString("bono_vaca");
                tiene_bono = valida.validarDato(consul.getString("tiene_bono"), "0");
                Sueldo_Base = valida.validarDato(consul.getString("sueldo_base"), "0");
                Bruto_Regular = valida.validarDato(consul.getString("bruto_regular"), "0");
                Neto_Regular = valida.validarDato(consul.getString("neto_regular"), "0");
                Bruto_Promedio = valida.validarDato(consul.getString("bruto_promedio"), "0");
                Neto_Promedio = valida.validarDato(consul.getString("neto_promedio"), "0");
                Tot_Haberes = valida.validarDato(consul.getString("tot_haberes"), "0");
                Mail = consul.getString("mail");
                E_Mail = consul.getString("e_mail");
                Unidad_id = consul.getString("unid_id");
                Unidad_desc = consul.getString("unid_desc");
                Cargo_id = consul.getString("cargo_id");
                Cargo_desc = consul.getString("cargo_desc");
                Empresa_id = consul.getString("empresa_id");
                Empresa_desc = consul.getString("empresa_desc");
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT nodo_nivel, nodo_id, compania FROM eje_ges_workflow_jerarquia WHERE (compania = '")).append(Empresa_id).append("') ").append("AND (nodo_id = '").append(Unidad_id).append("')")));
                consul2.exec(sql);
                if(consul2.next())
                    Unidad_nivel = consul2.getInt("nodo_nivel");
                else
                    Unidad_nivel = -1;
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT cargo, estamento FROM eje_ges_workflow_estamentos_cargos WHERE (cargo = '")).append(Cargo_id).append("')")));
                consul2.exec(sql);
                if(consul2.next())
                    Estamento = consul2.getString("estamento");
                else
                    Estamento = "SE";
                consul2.close();
                Sup_nombre = consul.getString("nom_supdirecto");
                Sup_Cargo = consul.getString("cargo_supdirecto");
                Sup_Mail = consul.getString("sup_mail");
                Sup_Unidad = consul.getString("sup_unidad");
            } else
            {
                valido = false;
            }
        } else
        {
            OutMessage.OutMessagePrint("en Class Rut : Conexion es null ");
        }
    }

    public String Rut;
    public String Digito_ver;
    public String Nombres;
    public String Ap_paterno;
    public String Ap_materno;
    public String Nombre_completo;
    public String Sindicato_id;
    public String Sindicato_desc;
    public String Bono_vaca;
    public String tiene_bono;
    public String Mail;
    public String Foto;
    public String E_Mail;
    public String Unidad_id;
    public String Unidad_desc;
    public int Unidad_nivel;
    public String Cargo_id;
    public String Cargo_desc;
    public String Estamento;
    public String Empresa_id;
    public String Empresa_desc;
    public String Sueldo_Base;
    public String Bruto_Regular;
    public String Neto_Regular;
    public String Bruto_Promedio;
    public String Neto_Promedio;
    public String Tot_Haberes;
    public String Sup_nombre;
    public String Sup_Unidad;
    public String Sup_Cargo;
    public String Sup_Mail;
    public boolean valido;
}