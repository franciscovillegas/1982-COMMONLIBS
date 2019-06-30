package organica.bci.traspaso;

import java.sql.Connection;

// Referenced classes of package bci.traspaso:
//            Procesable

public abstract class Proceso
    implements Procesable
{

    public Proceso(Connection conOrigen, Connection conDestino)
    {
        this.conOrigen = null;
        this.conDestino = null;
        this.conOrigen = conOrigen;
        this.conDestino = conDestino;
    }

    public abstract boolean Run();

    protected Connection conOrigen;
    protected Connection conDestino;
}