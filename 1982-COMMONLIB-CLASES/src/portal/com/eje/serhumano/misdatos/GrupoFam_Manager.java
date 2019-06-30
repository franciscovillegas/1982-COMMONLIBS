package portal.com.eje.serhumano.misdatos;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;

public class GrupoFam_Manager
{

    public GrupoFam_Manager(Connection conex)
    {
        con = conex;
        mensajeError = "";
    }

    public Consulta GetCabecera(String rut)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT rut, nombre, cargo, fecha_ingreso,digito_ver,id_foto,afp,isapre,estado_civil,anexo,area,e_mail,id_unidad, unidad,division FROM view_InfoRut WHERE (rut = ")).append(rut).append(")")));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetDetalleCargas(String rut)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT numero,nombre,fecha_nacim,rut_carga,dv_carga,es_carga,es_carga_salud,num_matrim,actividad,fecha_inicio,fecha_termino,sexo,rtrim(parentesco) as parentesco,(DATEDIFF(month, fecha_nacim, GETDATE()) / 12) AS edad FROM eje_ges_grupo_familiar WHERE (rut = ")).append(rut).append(")")));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetDetalleCargas(String rut, String empresa)
    {
        int codempresa = Integer.parseInt(empresa);
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT numero,nombre,fecha_nacim,rut_carga,dv_carga,es_carga,es_carga_salud,num_matrim,actividad,fecha_inicio,fecha_termino,sexo,rtrim(parentesco) as parentesco,(DATEDIFF(month, fecha_nacim, GETDATE()) / 12) AS edad FROM eje_ges_grupo_familiar WHERE (rut = ")).append(rut).append(")")) + " and wp_cod_empresa =" + codempresa);
        consul.exec(sql);
        System.out.println(sql);
        return consul;
    }

    public String getError()
    {
        return mensajeError;
    }

    private Connection con;
    private Consulta consul;
    private String mensajeError;
}