package cl.intercept.trackingIntranet;

import java.sql.*;

public class ManagerTracking {

    private Connection conn;

    public ManagerTracking(Connection conn) {
        this.conn = conn;
    }

    public boolean addTracking(WsgTracking wsg) {
        boolean ok = true;
        StringBuilder sql = new StringBuilder("INSERT into wsg_tracking(rut,cod_empresa,fecha,ip,browser,descripcion,hora,direc_rel, ")
        	.append("query,datos,initdate,server)  VALUES (?,?,getdate(),?,?,?,?,?,?,?,?,?) ");
        try {
            PreparedStatement pst = conn.prepareStatement(sql.toString());
            int cont = 0;
            pst.setInt(++cont, wsg.getRut());
            pst.setInt(++cont, wsg.getCod_empresa());
            //pst.setDate(++cont, wsg.getFecha());
            pst.setString(++cont, wsg.getIp());
            pst.setString(++cont, wsg.getBrowser());
            pst.setString(++cont, wsg.getDescripcion());
            pst.setDate(++cont, wsg.getHora());
            pst.setString(++cont, wsg.getDirec_rel());
            pst.setString(++cont, wsg.getQuery());
            pst.setString(++cont, wsg.getDatos());
            pst.setDate(++cont, wsg.getInitdate());
            pst.setString(++cont, wsg.getServer());
            pst.executeUpdate();
        }
        catch(SQLException e) {
            ok = false;
            e.printStackTrace();
        }
        return ok;
    }

    public ResultSet getRSforReporte() {
        PreparedStatement pst;
        try {
        	StringBuilder sql = new StringBuilder("SELECT t.datos as URL, isnull(a.tipo_desc,'') as [Nombre Pagina] , ")
        		.append("count(t.hora) as [Cantidad de Cargas] FROM wsg_tracking t left outer join wsg_cont_aviso_tipo a ")
        		.append("on a.tipo COLLATE Modern_Spanish_CI_AS = t.descripcion COLLATE Modern_Spanish_CI_AS ")
        		.append("and a.container in (SELECT cont_id FROM wsg_cont_container WHERE cont_path COLLATE ")
        		.append("Modern_Spanish_CI_AS = t.query COLLATE Modern_Spanish_CI_AS) GROUP BY t.datos, a.tipo_desc");
        	pst = conn.prepareStatement(sql.toString());
        	return pst.executeQuery();
        }
        catch(SQLException e) {
        	e.printStackTrace();
        }
        return null;
    }
}
