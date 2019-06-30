package organica.com.eje.ges.usuario;

import java.sql.Connection;

import organica.DatosRut.Rut;
import organica.com.eje.ges.usuario.cargo.VerCargo;
import organica.com.eje.ges.usuario.unidad.FiltroUnidad;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import portal.com.eje.permiso.PermisoPortal;

// Referenced classes of package com.eje.ges.usuario:
//            Usuario, ListaPermisos, Permiso

public class ControlAcceso
{

    public ControlAcceso(Usuario usuario)
    {
        user = usuario;
        permisos = usuario.getPermisos();
    }

    public static ListaPermisos cargarListaPermisos(Connection conexion, String strRut)
    {
        ListaPermisos permisos = new ListaPermisos();
        Consulta consul = new Consulta(conexion);
        Consulta consul_ruts = new Consulta(conexion);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT acc_id, acc_tipo, acc_puede_ver FROM eje_ges_usuario_acceso WHERE (rut_usuario = ")).append(strRut).append(")")));
        consul.exec(sql);
        Permiso permiso;
        for(; consul.next(); permisos.agregarPermiso(permiso))
        {
            permiso = new Permiso(consul.getString("acc_id"), consul.getString("acc_tipo"), consul.getString("acc_puede_ver"));
            if(permiso.esTipoRestringido())
            {
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT rut_trab FROM eje_ges_usuario_acceso_rut WHERE (acc_id = '")).append(permiso.getId()).append("') AND (rut_usuario = ").append(strRut).append(")")));
                consul_ruts.exec(sql);
                for(; consul_ruts.next(); permiso.agregarRut(consul_ruts.getString("rut_trab")));
            }
        }

        consul_ruts.close();
        sql = String.valueOf(String.valueOf((new StringBuilder("SELECT rol_acc.acc_id, acc.acc_tipo FROM eje_ges_rol_acceso rol_acc INNER JOIN eje_ges_usuario_rol user_rol ON rol_acc.rol_id = user_rol.rol_id INNER JOIN eje_ges_acceso acc ON rol_acc.acc_id = acc.acc_id WHERE (user_rol.rut_usuario = ")).append(strRut).append(")")));
        consul.exec(sql);
        do
        {
            if(!consul.next())
                break;
            permiso = new Permiso(consul.getString("acc_id"), consul.getString("acc_tipo"), "N");
            if(!permisos.existePermiso(permiso.getId()))
                permisos.agregarPermiso(permiso);
        } while(true);
        consul.close();
        Consulta consul3 = new Consulta(conexion);
        sql = "SELECT empresa, unidad FROM eje_ges_uni_sup";
        OutMessage.OutMessagePrint(sql);
        consul3.exec(sql);
        if(consul3.next())
        {
            EmpSup = consul3.getString("empresa");
            UniSup = consul3.getString("unidad");
        }
        consul3.close();
        return permisos;
    }

    public boolean tienePermiso(String accesoId)
    {
        boolean resul = false;
        Permiso permi = permisos.getPermiso(accesoId);
        if(permi != null && permi.esTipoNormal())
            resul = true;
        return resul;
    }
    
    public boolean tienePermiso(PermisoPortal p)
    {
        return tienePermiso(p.toString());
    }

    public boolean tienePermiso(String accesoId, String strRut)
    {
        boolean resul = false;
        Permiso permi = permisos.getPermiso(accesoId);
        if(permi != null && permi.esTipoRestringido())
            resul = permi.puedeVerRut(strRut);
        OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("aqui**TienePermiso: ")).append(accesoId).append(", ").append(strRut).append(" --> ").append(resul))));
        return resul;
    }

    public boolean tienePermisoJerarquico(Connection con, String strRut)
    {
        boolean resul = true;
        if(user.getRutUsuario().equals(strRut))
            resul = true;
        else
        if(!tienePermiso("df_jer_comp"))
        {
            FiltroUnidad fu = new FiltroUnidad(user);
            String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT rut FROM view_ges_busqueda2 WHERE (")).append(fu.getFiltro()).append(") AND (rut = ").append(strRut).append(")")));
            Consulta consul = new Consulta(con);
            OutMessage.OutMessagePrint("tienePermisoJerarquico\n".concat(String.valueOf(String.valueOf(sql))));
            consul.exec(sql);
            resul = consul.next();
            consul.close();
        }
        String id_cargo = "";
        String id_empresa = "";
        String cargo_ver = "";
        String empresa_ver = "";
        OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("TienePermisoJerarquico(solo jerarquia): ")).append(strRut).append(" --> ").append(resul))));
        if(!resul)
        {
            VerCargo cargosver[] = user.getCargosVer();
            if(cargosver.length > 0)
            {
                Rut dataRut = new Rut(con, strRut);
                id_empresa = dataRut.Empresa;
                id_cargo = dataRut.Id_Cargo;
                int x = 0;
                do
                {
                    if(x >= cargosver.length)
                        break;
                    System.err.println("---->Cargo Ver: ".concat(String.valueOf(String.valueOf(cargosver[x].toString()))));
                    cargo_ver = cargosver[x].getCargo();
                    empresa_ver = cargosver[x].getEmpresa();
                    if(id_empresa.equals(empresa_ver) && id_cargo.equals(cargo_ver))
                    {
                        System.err.println("Tiene Permiso.Cargo pertenece a un Cargo-VER del usuario");
                        resul = true;
                        break;
                    }
                    x++;
                } while(true);
            }
        }
        OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("TienePermisoJerarquico(jerarquia+cargos_ver): ")).append(strRut).append(" --> ").append(resul))));
        return resul;
    }

    public boolean tienePermisoJerarquico(Connection con, String accesoId, String strRut)
    {
        return tienePermisoJerarquico(con, strRut) && tienePermiso(accesoId, strRut);
    }

    private ListaPermisos permisos;
    private Usuario user;
    public static String UniSup = null;
    public static String EmpSup = null;

}