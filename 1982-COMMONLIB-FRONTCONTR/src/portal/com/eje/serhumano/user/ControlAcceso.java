package portal.com.eje.serhumano.user;

import java.sql.Connection;
import java.sql.SQLException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;

// Referenced classes of package portal.com.eje.serhumano.user:
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
       
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT urol.rut_usuario, ra.acc_id, urol.app_id FROM eje_ges_rol_acceso ra INNER JOIN eje_ges_usuario_rol urol ON ra.rol_id = urol.rol_id WHERE (urol.rut_usuario = ")).append(strRut).append(") ").append("AND (urol.app_id = 'sh')")));
       
        ConsultaData data;
		try {
			data = ConsultaTool.getInstance().getData("portal",sql);
		    if(data != null) {
		        do
		        {
		            if(! data.next())
		                break;
		            Permiso permiso = new Permiso( data.getForcedString("acc_id"));
		            if(!permisos.existePermiso(permiso.getId()))
		                permisos.agregarPermiso(permiso);
		        } while(true);
	        }
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
    
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
        System.out.println("------>Control Acceso (restringido??): ".concat(String.valueOf(String.valueOf(permi.esTipoRestringido()))));
        if(permi != null && permi.esTipoRestringido())
            resul = permi.puedeVerRut(strRut);
        OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("aqui**TienePermiso: ")).append(accesoId).append(", ").append(strRut).append(" --> ").append(resul))));
        return resul;
    }

    public boolean tienePermisoJerarquico(Connection con, String strRut)
    {
        boolean resul = true;
        if(user.getRut().equals(strRut))
            resul = true;
        else
            resul = false;
        OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("TienePermisoJerarquico: ")).append(strRut).append(" --> ").append(resul))));
        return resul;
    }

    public boolean tienePermisoJerarquico(Connection con, String accesoId, String strRut)
    {
        return tienePermisoJerarquico(con, strRut) && tienePermiso(accesoId, strRut);
    }

    private ListaPermisos permisos;
    private Usuario user;
}