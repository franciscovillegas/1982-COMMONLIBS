package organica.com.eje.ges.usuario;

import java.sql.Connection;
import java.util.Vector;

import organica.com.eje.ges.usuario.cargo.VerCargo;
import organica.com.eje.ges.usuario.unidad.VerUnidad;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;

public class UserManager
{

    public UserManager(Connection conn)
    {
        conexion = conn;
        consul = new Consulta(conexion);
    }

    public boolean existeUsuario(String rutUsuario)
    {
        boolean resul = true;
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT rut_usuario FROM eje_ges_usuario  WHERE (rut_usuario =")).append(rutUsuario).append(")")));
        consul.exec(sql);
        if(!consul.next())
        {
            sql = String.valueOf(String.valueOf((new StringBuilder("SELECT rut_usuario FROM eje_ges_usuario_certif  WHERE (rut_usuario =")).append(rutUsuario).append(")")));
            consul.exec(sql);
            if(!consul.next())
            {
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT rut_usuario FROM eje_ges_usuario_anexos  WHERE (rut_usuario =")).append(rutUsuario).append(")")));
                consul.exec(sql);
                if(!consul.next())
                    resul = false;
            }
        }
        return resul;
    }

    public boolean existeUsuarioDF(String rutUsuario)
    {
        boolean resul = true;
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT rut_usuario FROM eje_ges_user_app  WHERE app_id='df' AND (rut_usuario =")).append(rutUsuario).append(")")));
        consul.exec(sql);
        if(!consul.next())
            resul = false;
        return resul;
    }

    public boolean crearUsuarioDF(String rutUsuario, String password)
    {
        boolean resul = true;
        if(password != null || password.trim().length() > 0)
        {
            String sql = String.valueOf((new StringBuilder("SELECT wp_cod_empresa,wp_cod_planta FROM eje_ges_trabajador WHERE rut=")).append(rutUsuario));
            consul.exec(sql);
            consul.next();
            String cod_empresa = consul.getString("wp_cod_empresa");
            String cod_planta = consul.getString("wp_cod_planta");
            if(cod_empresa != null && cod_planta != null)
            {
                sql = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_user_app (app_id,rut_usuario,vigente,wp_cod_empresa,wp_cod_planta) VALUES (")).append("'df',").append(rutUsuario).append(",NULL,").append(cod_empresa).append(",").append(cod_planta).append(")")));
                resul = consul.insert(sql);
            } else
            {
                resul = false;
            }
        } else
        {
            resul = false;
        }
        return resul;
    }

    public boolean crearUsuario(String rutUsuario, String password)
    {
        boolean resul = true;
        if(password != null || password.trim().length() > 0)
        {
            String sql = String.valueOf((new StringBuilder("SELECT wp_cod_empresa,wp_cod_planta FROM eje_ges_trabajador WHERE rut=")).append(rutUsuario));
            consul.exec(sql);
            consul.next();
            String cod_empresa = consul.getString("wp_cod_empresa");
            String cod_planta = consul.getString("wp_cod_planta");
            if(cod_empresa != null && cod_planta != null)
            {
                sql = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_usuario (login_usuario, password_usuario, rut_usuario, error, passw_cambiar, passw_ult_cambio,wp_cod_empresa,wp_cod_planta) VALUES ('")).append(rutUsuario).append("', '").append(password).append("', ").append(rutUsuario).append(", 0, 'S', getdate(),").append(cod_empresa).append(",").append(cod_planta).append(")")));
                resul = consul.insert(sql);
            } else
            {
                resul = false;
            }
        } else
        {
            resul = false;
        }
        return resul;
    }

    public boolean crearUsuarioCertif(String rutUsuario, String password)
    {
        boolean resul = true;
        if(password != null || password.trim().length() > 0)
        {
            String sql = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_usuario_certif (login_usuario, password_usuario, rut_usuario, error, passw_cambiar, passw_ult_cambio) VALUES ('")).append(rutUsuario).append("', '").append(password).append("', ").append(rutUsuario).append(", 0, 'S', getdate())")));
            OutMessage.OutMessagePrint("--> crearUsuario Certificados*****\n   ".concat(String.valueOf(String.valueOf(sql))));
            resul = consul.insert(sql);
        } else
        {
            resul = false;
        }
        return resul;
    }

    public boolean crearUsuarioAnexos(String rutUsuario, String password)
    {
        boolean resul = true;
        if(password != null || password.trim().length() > 0)
        {
            String sql = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_usuario_anexos (login_usuario, password_usuario, rut_usuario, error, passw_cambiar, passw_ult_cambio) VALUES ('")).append(rutUsuario).append("', '").append(password).append("', ").append(rutUsuario).append(", 0, 'S', getdate())")));
            OutMessage.OutMessagePrint("--> crearUsuario Anexos*****\n   ".concat(String.valueOf(String.valueOf(sql))));
            resul = consul.insert(sql);
        } else
        {
            resul = false;
        }
        return resul;
    }

    public boolean cambiarClave(String rutUsuario, String password, char cambiar)
    {
        boolean resul = true;
        if(password != null || password.trim().length() > 0)
        {
            String sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_usuario SET password_usuario = '")).append(password).append("',").append(" passw_ult_cambio = getdate(),").append(" passw_cambiar = '").append(cambiar).append("', error = 0").append(" WHERE (rut_usuario = ").append(rutUsuario).append(")")));
            resul = consul.insert(sql);
        } else
        {
            resul = false;
        }
        return resul;
    }

    public boolean cambiarClaveCertif(String rutUsuario, String password, char cambiar)
    {
        boolean resul = true;
        if(password != null || password.trim().length() > 0)
        {
            String sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_usuario_certif SET password_usuario = '")).append(password).append("',").append(" passw_ult_cambio = getdate(),").append(" passw_cambiar = '").append(cambiar).append("', error = 0").append(" WHERE (rut_usuario = ").append(rutUsuario).append(")")));
            OutMessage.OutMessagePrint("--> cambiarClave Certificados\n   ".concat(String.valueOf(String.valueOf(sql))));
            resul = consul.insert(sql);
        } else
        {
            resul = false;
        }
        return resul;
    }

    public boolean cambiarClaveAnexos(String rutUsuario, String password, char cambiar)
    {
        boolean resul = true;
        if(password != null || password.trim().length() > 0)
        {
            String sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_usuario_anexos SET password_usuario = '")).append(password).append("',").append(" passw_ult_cambio = getdate(),").append(" passw_cambiar = '").append(cambiar).append("', error = 0").append(" WHERE (rut_usuario = ").append(rutUsuario).append(")")));
            OutMessage.OutMessagePrint("--> cambiarClave Anexos\n   ".concat(String.valueOf(String.valueOf(sql))));
            resul = consul.insert(sql);
        } else
        {
            resul = false;
        }
        return resul;
    }

    public boolean AsignarUniRel(String rutUsuario, VerUnidad vu)
    {
        boolean resul = true;
        String sql = "";
        if(vu != null)
            sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_usuario SET emp_rel = '")).append(vu.getEmpresa()).append("',").append(" uni_rel = '").append(vu.getUnidad()).append("',").append(" tipo_rel = '").append(vu.getQueVer()).append("'").append(" WHERE (rut_usuario = ").append(rutUsuario).append(")")));
        else
            sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_usuario SET emp_rel = null, uni_rel = null, tipo_rel = null WHERE (rut_usuario = ")).append(rutUsuario).append(")")));
        OutMessage.OutMessagePrint("--> Asigna Unidad Relativa\n   ".concat(String.valueOf(String.valueOf(sql))));
        resul = consul.insert(sql);
        return resul;
    }

    public void quitarRolesComplementarios(String rutUsuario, Vector rolId)
    {
        String strRoles = Tools.creaLista(rolId, ",", "'");
        String sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_usuario_rol WHERE app_id='df' AND (rut_usuario = ")).append(rutUsuario).append(")")));
        if(!strRoles.equals(""))
            sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append(" AND (rol_id NOT IN (").append(strRoles).append("))")));
        consul.insert(sql);
    }

    public void agregarRoles(String rutUsuario, String rolId)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_usuario_rol (rol_id, rut_usuario,app_id) VALUES ('")).append(rolId).append("',").append(rutUsuario).append(",'df')")));
        consul.insert(sql);
    }

    public void agregarRoles(String rutUsuario, Vector rolId)
    {
        for(int x = 0; x < rolId.size(); x++)
            agregarRoles(rutUsuario, (String)rolId.get(x));

    }

    public void quitarAccesosRutComplementarios(String rutUsuario, Vector accesosId)
    {
        String strAccesos = Tools.creaLista(accesosId, ",", "'");
        String sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_usuario_acceso_rut WHERE (rut_usuario = ")).append(rutUsuario).append(")")));
        if(!strAccesos.equals(""))
            sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append(" AND (acc_id NOT IN (").append(strAccesos).append("))")));
        consul.insert(sql);
    }

    public void quitarAccesosRutComplementarios(String rutUsuario, String accesoId, Vector trabIds)
    {
        String strTrab = Tools.creaLista(trabIds, ",", "'");
        String sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_usuario_acceso_rut WHERE (rut_usuario = ")).append(rutUsuario).append(") AND").append(" (acc_id = '").append(accesoId).append("')")));
        if(!strTrab.equals(""))
            sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append(" AND (rut_trab NOT IN (").append(strTrab).append("))")));
        consul.insert(sql);
    }

    public void quitarAccesosComplementarios(String rutUsuario, Vector accesosId)
    {
        quitarAccesosRutComplementarios(rutUsuario, accesosId);
        String strAccesos = Tools.creaLista(accesosId, ",", "'");
        boolean anex = false;
        boolean certif = false;
        String acc_id = "";
        int x = 0;
        String sql = "";
        for(x = 0; x < accesosId.size(); x++)
        {
            OutMessage.OutMessagePrint("/////--> Acceso= ".concat(String.valueOf(String.valueOf((String)accesosId.elementAt(x)))));
            acc_id = (String)accesosId.elementAt(x);
            if(acc_id.equals("df_mant_anexo"))
                anex = true;
            if(acc_id.equals("df_certif"))
                certif = true;
        }

        System.out.println("---------->Trae Anexos?: ".concat(String.valueOf(String.valueOf(anex))));
        if(!anex)
            quitarUsuarioParam(rutUsuario, "eje_ges_usuario_anexos");
        if(!certif)
            quitarUsuarioParam(rutUsuario, "eje_ges_usuario_certif");
        sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_usuario_acceso WHERE (rut_usuario = ")).append(rutUsuario).append(")")));
        if(!strAccesos.equals(""))
            sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append(" AND (acc_id NOT IN (").append(strAccesos).append("))")));
        OutMessage.OutMessagePrint("--> quitarAccesosComplementarios\n   ".concat(String.valueOf(String.valueOf(sql))));
        consul.insert(sql);
    }

    public void agregarAccesos(String rutUsuario, String accesoId, String tipo, String puedeVer)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_usuario_acceso  (rut_usuario, acc_id, acc_tipo, acc_puede_ver) VALUES (")).append(rutUsuario).append(", '").append(accesoId).append("', '").append(tipo).append("', '").append(puedeVer).append("')")));
        consul.insert(sql);
    }

    public void agregarAccesos(String rutUsuario, String accesoId, String tipo)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_usuario_acceso  (rut_usuario, acc_id, acc_tipo, acc_puede_ver) VALUES (")).append(rutUsuario).append(", '").append(accesoId).append("', '").append(tipo).append("', null)")));
        consul.insert(sql);
    }

    public void agregarAccesosRut(String rutUsuario, String accesoId, String rutTrab)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_usuario_acceso_rut  (rut_usuario, acc_id, rut_trab) VALUES (")).append(rutUsuario).append(", '").append(accesoId).append("', ").append(rutTrab).append(")")));
        consul.insert(sql);
    }

    public void actualizarAccesos(String rutUsuario, String accesoId, String puedeVer)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_usuario_acceso  SET acc_puede_ver = '")).append(puedeVer).append("'").append(" WHERE (rut_usuario = ").append(rutUsuario).append(") AND (acc_id = '").append(accesoId).append("')")));
        OutMessage.OutMessagePrint("????--> actualizarAccesos\n   ".concat(String.valueOf(String.valueOf(sql))));
        consul.insert(sql);
    }

    public void quitarUsuario(String rutUsuario)
    {
        Vector vacio = new Vector();
        quitarRolesComplementarios(rutUsuario, vacio);
        quitarAccesosRutComplementarios(rutUsuario, vacio);
        quitarAccesosComplementarios(rutUsuario, vacio);
        String sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_user_app WHERE app_id='df' AND (rut_usuario = ")).append(rutUsuario).append(")")));
        consul.insert(sql);
        quitaUsuarioTabla(rutUsuario, "eje_ges_usuario_certif");
        quitaUsuarioTabla(rutUsuario, "eje_ges_usuario_anexos");
    }

    public void quitarUsuarioDF(String rutUsuario)
    {
        Vector vacio = new Vector();
        quitarRolesComplementarios(rutUsuario, vacio);
        quitarAccesosRutComplementarios(rutUsuario, vacio);
        quitarAccesosComplementarios(rutUsuario, vacio);
        String sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_usuario_unidad WHERE (rut_usuario = ")).append(rutUsuario).append(")")));
        consul.insert(sql);
        quitaUsuarioTabla(rutUsuario, "eje_ges_usuario");
    }

    public void quitarUsuarioParam(String rutUsuario, String tabla)
    {
        if(tabla.equals("eje_ges_usuario_certif"))
            quitaUsuarioTabla(rutUsuario, tabla);
        else
            quitaUsuarioTabla(rutUsuario, tabla);
    }

    private void quitaUsuarioTabla(String rutUser, String tabla)
    {
        OutMessage.OutMessagePrint("$$$$$--> Tabla".concat(String.valueOf(String.valueOf(tabla))));
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT * FROM ")).append(tabla).append(" WHERE (rut_usuario = ").append(rutUser).append(")")));
        consul.exec(sql);
        if(consul.next())
        {
            sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM ")).append(tabla).append(" WHERE (rut_usuario = ").append(rutUser).append(")")));
            OutMessage.OutMessagePrint("$$$$$--> quitarUsuario\n   ".concat(String.valueOf(String.valueOf(sql))));
            consul.insert(sql);
        }
    }

    public void agregarRama(String rutUsuario, VerUnidad vu)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_usuario_unidad (rut_usuario, empresa, unidad, tipo) VALUES (")).append(rutUsuario).append(", '").append(vu.getEmpresa()).append("', '").append(vu.getUnidad()).append("', '").append(vu.getQueVer()).append("')")));
        OutMessage.OutMessagePrint("--> agregarRama\n   ".concat(String.valueOf(String.valueOf(sql))));
        consul.insert(sql);
    }

    public void agregarCargo(String rutUsuario, VerCargo vc)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_usuario_cargo (rut_usuario, empresa, cargo) VALUES (")).append(rutUsuario).append(", '").append(vc.getEmpresa()).append("', '").append(vc.getCargo()).append("')")));
        OutMessage.OutMessagePrint("--> agregarCargoVER\n   ".concat(String.valueOf(String.valueOf(sql))));
        consul.insert(sql);
    }

    public void quitarCargo(String rutUsuario, VerCargo vc)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_usuario_cargo WHERE (rut_usuario = ")).append(rutUsuario).append(") ").append("AND (empresa = '").append(vc.getEmpresa()).append("') ").append("AND (cargo = '").append(vc.getCargo()).append("')")));
        OutMessage.OutMessagePrint("--> Quitar CargoVER\n   ".concat(String.valueOf(String.valueOf(sql))));
        consul.insert(sql);
    }

    public void quitarRama(String rutUsuario, VerUnidad vu)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_usuario_unidad WHERE (rut_usuario = ")).append(rutUsuario).append(") AND (empresa = '").append(vu.getEmpresa()).append("') AND (unidad = '").append(vu.getUnidad()).append("')")));
        OutMessage.OutMessagePrint("--> quitarRama\n   ".concat(String.valueOf(String.valueOf(sql))));
        consul.insert(sql);
    }

    public void close()
    {
        consul.close();
    }

    private Connection conexion;
    private Consulta consul;
}