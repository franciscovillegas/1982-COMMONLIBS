package portal.com.eje.usuario;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.usuario.cargo.VerCargo;
import portal.com.eje.usuario.datosrut.Rut;
import portal.com.eje.usuario.unidad.FiltroUnidad;

// Referenced classes of package portal.com.eje.usuario:
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
        String sql = "SELECT acc_id, acc_tipo, acc_puede_ver FROM eje_ges_usuario_acceso WHERE (rut_usuario = " + strRut + ")";
        consul.exec(sql);
        Permiso permiso;
        for(; consul.next(); permisos.agregarPermiso(permiso))
        {
            permiso = new Permiso(consul.getString("acc_id"), consul.getString("acc_tipo"), consul.getString("acc_puede_ver"));
            if(permiso.esTipoRestringido())
            {
                sql = "SELECT rut_trab FROM eje_ges_usuario_acceso_rut WHERE (acc_id = '" + permiso.getId() + "') AND (rut_usuario = " + strRut + ")";
                consul_ruts.exec(sql);
                for(; consul_ruts.next(); permiso.agregarRut(consul_ruts.getString("rut_trab")));
            }
        }

        consul_ruts.close();
        sql = "SELECT rol_acc.acc_id, acc.acc_tipo FROM eje_ges_rol_acceso rol_acc INNER JOIN eje_ges_usuario_rol user_rol ON rol_acc.rol_id = user_rol.rol_id INNER JOIN eje_ges_acceso acc ON rol_acc.acc_id = acc.acc_id WHERE (user_rol.rut_usuario = " + strRut + ")";
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

    public boolean tienePermiso(String accesoId, String strRut)
    {
        boolean resul = false;
        Permiso permi = permisos.getPermiso(accesoId);
        if(permi != null && permi.esTipoRestringido())
            resul = permi.puedeVerRut(strRut);
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
            String sql = "SELECT rut FROM view_busqueda WHERE (" + fu.getFiltro() + ") AND (rut = " + strRut + ")";
            Consulta consul = new Consulta(con);
            OutMessage.OutMessagePrint("tienePermisoJerarquico\n" + sql);
            consul.exec(sql);
            resul = consul.next();
            consul.close();
        }
        String id_cargo = "";
        String id_empresa = "";
        String cargo_ver = "";
        String empresa_ver = "";
        OutMessage.OutMessagePrint("TienePermisoJerarquico(solo jerarquia): " + strRut + " --> " + resul);
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
                    System.err.println("---->Cargo Ver: " + cargosver[x].toString());
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
        OutMessage.OutMessagePrint("TienePermisoJerarquico(jerarquia+cargos_ver): " + strRut + " --> " + resul);
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