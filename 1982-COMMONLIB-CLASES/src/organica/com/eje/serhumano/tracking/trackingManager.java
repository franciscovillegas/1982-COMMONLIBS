package organica.com.eje.serhumano.tracking;

import java.sql.Connection;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

import organica.datos.Consulta;
import organica.tools.OutMessage;

public class trackingManager
{

    public trackingManager(Connection connection)
    {
        con = connection;
        mensajeError = "";
    }

    public void insertTracking(int rut, int cod_empresa, String descripcion, String ip, String browser, String direc_rel, String query, 
            String datos)
    {
        Consulta consul = new Consulta(con);
        boolean ok = false;
        Date fecha_actual = new Date();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = simpledateformat.format(fecha_actual);
        Date hoy = new Date();
        Time hora = new Time(hoy.getTime());
        String sql = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_sh_tracking (rut, cod_empresa, fecha, hora, descripcion, ip, browser, direc_rel, query, datos) VALUES (")).append(rut).append(", ").append(cod_empresa).append(", '").append(fecha).append("', '").append(hora).append("', '").append(descripcion).append("', '").append(ip).append("', '").append(browser).append("', '").append(direc_rel).append("', '").append(query).append("', '").append(datos).append("')")));
        OutMessage.OutMessagePrint("\n****Insertando Tracking BD: ".concat(String.valueOf(String.valueOf(sql))));
        if(consul.insert(sql))
            ok = true;
        consul.close();
    }

    private Connection con;
    private String mensajeError;
}