package portal.com.eje.serhumano.formularios.tracking;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import portal.com.eje.datos.Managers;
import portal.com.eje.tools.OutMessage;

public class TrackManager extends Managers
{

    public TrackManager(Connection conexion)
    {
        super(conexion);
    }

    public Consulta getTrackingAll()
    {
        return getTracking(1);
    }

    public Consulta getTracking(int track_id)
    {
        Consulta consul = new Consulta(super.conexion);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT url, descripci\363n FROM eje_ges_tracking_func WHERE     (url = ")).append(track_id).append(")")));
        consul.exec(sql);
        OutMessage.OutMessagePrint("getTracking() --> ".concat(String.valueOf(String.valueOf(sql))));
        return consul;
    }

    public Consulta getTracking()
    {
        Consulta consul = new Consulta(super.conexion);
        String sql = String.valueOf(String.valueOf(new StringBuilder("SELECT url, descripcion FROM eje_ges_tracking_func ORDER BY orden ")));
        consul.exec(sql);
        OutMessage.OutMessagePrint("getTracking() --> ".concat(String.valueOf(String.valueOf(sql))));
        return consul;
    }

    public int addTracking(String glosa, String vigente)
    {
        boolean ok = false;
        int corr = -1;
        return corr;
    }

    public boolean updateTracking(int id, String glosa, String vigente)
    {
        boolean ok = false;
        return ok;
    }
}