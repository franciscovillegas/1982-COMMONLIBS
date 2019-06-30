package organica.bci.traspaso;

import organica.arbol.Nodo;
import organica.arbol.Organica;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import java.sql.Connection;
import java.util.Vector;
import organica.tools.OutMessage;

// Referenced classes of package bci.traspaso:
//            Proceso

public class GeneraUnidadRamaEmp extends Proceso
{

    public GeneraUnidadRamaEmp(Connection conDestino)
    {
        super(null, conDestino);
    }

    private void actualizaRamas(Nodo nodo, Vector rama)
    {
        String unidad = nodo.getIdNodo();
        String empresa = nodo.getEmpresa();
        Consulta consul = new Consulta(conDestino);
        String sql = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_unidad_rama_empresa (empresa, unidad, tipo, uni_hija, emp_hija) VALUES ('")).append(empresa).append("', '").append(unidad).append("', 'U','").append(unidad).append("','").append(empresa).append("')")));
        consul.insert(sql);
        for(int x = 0; x < rama.size(); x++)
        {
            Nodo paso = (Nodo)rama.get(x);
            sql = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_unidad_rama_empresa (empresa, unidad, tipo, uni_hija, emp_hija) VALUES ('")).append(empresa).append("', '").append(unidad).append("', 'R','").append(paso.getIdNodo()).append("', '").append(paso.getEmpresa()).append("')")));
            consul.insert(sql);
        }

        consul.close();
    }

    private Vector creaRama(Nodo nodo)
    {
        Vector rama = new Vector();
        Vector subRama = new Vector();
        if(nodo.tipoNodo == "U")
            rama.add(nodo);
        for(int x = 0; x < nodo.getHijos().size(); x++)
        {
            subRama = creaRama((Nodo)nodo.getHijos().get(x));
            for(int y = 0; y < subRama.size(); y++)
                rama.add(subRama.get(y));

        }

        if(nodo.tipoNodo != "R")
            actualizaRamas(nodo, rama);
        return rama;
    }

    public boolean Run()
    {
        String sqlDest = "";
        Consulta consulDest = new Consulta(conDestino);
        OutMessage.OutMessagePrint("Generando Unidad/Rama/Empresa");
        sqlDest = "DELETE FROM eje_ges_unidad_rama_empresa";
        if(consulDest.insert(sqlDest))
            OutMessage.OutMessagePrint("Datos de destino Borrados");
        else
            OutMessage.OutMessagePrint("Error al intentar borrar datos de destino");
        if(Usuario.SuperNodo == null)
            Organica.main(new String[1]);
        Nodo nodoRaiz = Usuario.SuperNodo;
        creaRama(nodoRaiz);
        OutMessage.OutMessagePrint("Fin Genera Unidad/Rama/Empresa");
        consulDest.close();
        return true;
    }
}