package organica.bci.traspaso;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import cl.ejedigital.web.datos.DBConnectionManager;
import cl.ejedigital.web.datos.IDBConnectionManager;
import organica.com.eje.datos.ConexionODBC;
import organica.com.eje.datos.JConnect;
import organica.tools.OutMessage;

// Referenced classes of package bci.traspaso:
//            Procesable

public class RelizaTraspaso extends Thread
{

    public RelizaTraspaso()
    {
        procesos = new Vector();
        estado = 1;
        connMgr = DBConnectionManager.getInstance();
    }

    public void conectarBD()
    {
        conexionOri = (new JConnect()).ConectaSource();
        if(getConexionOri() != null)
            salidaLog("RelizaTraspaso: Conectado a la Base de Origen.");
        conexionDest = connMgr.getConnection("portal");
        if(getConexionDest() != null)
            salidaLog("RelizaTraspaso: Conectado a la Base de Destino.");
        conexionDestODBC = (new ConexionODBC()).Conecta();
        if(getConexionDestODBC() != null)
            salidaLog("RelizaTraspaso: Conectado a la Base de Destino por ODBC.");
    }

    public void desconectarBD()
    {
        salidaLog("RelizaTraspaso: DesConexion de las Bases de Datos.");
        try
        {
            if(conexionOri != null)
                conexionOri.close();
            if(conexionDestODBC != null)
                conexionDestODBC.close();
            connMgr.freeConnection("portal", conexionDest);
            connMgr.release();
        }
        catch(SQLException sqlexception)
        {
            OutMessage.OutMessagePrint(sqlexception.getMessage());
        }
    }

    public Connection getConexionOri()
    {
        return conexionOri;
    }

    public Connection getConexionDest()
    {
        return conexionDest;
    }

    public Connection getConexionDestODBC()
    {
        return conexionDestODBC;
    }

    public void addProceso(Procesable p)
    {
        procesos.addElement(p);
    }

    public Procesable getProceso(int indice)
    {
        return (Procesable)procesos.get(indice);
    }

    public int getCantidad()
    {
        return procesos.size();
    }

    private void salidaLog(String msg)
    {
        OutMessage.OutMessagePrint(msg);
    }

    public void run()
    {
        estado = 2;
        getEstado();
        for(int p = 0; p < getCantidad(); p++)
        {
            salidaLog("\nRelizaTraspaso: Hora Inicio ".concat(String.valueOf(String.valueOf((new Date()).toString()))));
            getProceso(p).toString();
            getProceso(p).Run();
            salidaLog("RelizaTraspaso: Hora Termino ".concat(String.valueOf(String.valueOf((new Date()).toString()))));
        }

        fin();
        getEstado();
    }

    private void fin()
    {
        desconectarBD();
        eliminarProcesos();
        estado = 1;
    }

    public int getEstado()
    {
        OutMessage.OutMessagePrint("RelizaTraspaso: estado --> ".concat(String.valueOf(String.valueOf(estado))));
        return estado;
    }

    public void printListaProcesos()
    {
        salidaLog("Lista de Procesos");
        for(int p = 0; p < getCantidad(); p++)
            salidaLog(String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(p + 1)))).append("\t").append(getProceso(p).toString()))));

    }

    public void eliminarProcesos()
    {
        procesos.removeAllElements();
        salidaLog("RelizaTraspaso: quedaron ".concat(String.valueOf(String.valueOf(getCantidad()))));
    }

    private Connection conexionOri;
    private Connection conexionDest;
    private Connection conexionDestODBC;
    private IDBConnectionManager connMgr;
    private Vector procesos;
    private int estado;
    public static final int NO_INICIADO = 1;
    public static final int INICIADO = 2;
}