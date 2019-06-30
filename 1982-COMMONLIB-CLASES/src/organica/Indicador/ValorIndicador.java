package organica.Indicador;

import organica.datos.Consulta;
import java.sql.Connection;
import organica.tools.OutMessage;

// Referenced classes of package Indicador:
//            DetIndicador

public class ValorIndicador
{

    public ValorIndicador()
    {
        valor = null;
        semaforo = null;
        imagen = null;
        destramo = null;
        tramo = null;
    }

    public ValorIndicador(DetIndicador indicador, float valor)
    {
        this.valor = null;
        semaforo = null;
        imagen = null;
        destramo = null;
        tramo = null;
        indic = indicador;
        existeValor = true;
        deterSemaforo(valor);
    }

    public ValorIndicador(Connection con, DetIndicador indicador, String periodo, String unidad, String unid_rama, String empresa)
    {
        valor = null;
        semaforo = null;
        imagen = null;
        destramo = null;
        tramo = null;
        indic = indicador;
        String consulta = "";
        Consulta dato = new Consulta(con);
        if(indic.getGrupo().equals("XXI"))
        {
            consulta = String.valueOf(String.valueOf((new StringBuilder("select ")).append(indic.getCampo()).append(" from ").append(indic.getTabla()).append(" where ").append(" empresa='").append(empresa).append("' and periodo=").append(periodo)));
            OutMessage.OutMessagePrint("xxi\n");
        } else
        {
            consulta = String.valueOf(String.valueOf((new StringBuilder("select ")).append(indic.getCampo()).append(" from ").append(indic.getTabla()).append(" where ").append(" empresa='").append(empresa).append("' and unidad='").append(unidad).append("' and uni_rama='").append(unid_rama).append("' and periodo=").append(periodo)));
        }
        dato.exec(consulta);
        if(dato.next())
        {
            float valors = dato.getFloat(indic.getCampo());
            existeValor = true;
            deterSemaforo(valors);
        } else
        {
            existeValor = false;
            OutMessage.OutMessagePrint(">>--> No encontro valor en la consulta\n".concat(String.valueOf(String.valueOf(consulta))));
        }
        dato.close();
    }

    public boolean existeValor()
    {
        return existeValor;
    }

    private void deterSemaforo(float valors)
    {
        if(valors >= Float.parseFloat(indic.getTramoSup1()))
        {
            imagen = indic.getImgagenSup();
            destramo = indic.getDescTramoSup();
            tramo = "Cr\355tico";
            semaforo = "R";
        }
        if(valors >= Float.parseFloat(indic.getTramoMedio1()) && valors <= Float.parseFloat(indic.getTramoMedio2()))
        {
            imagen = indic.getImgagenMedio();
            destramo = indic.getDescTramoMed();
            tramo = "Sensible";
            semaforo = "A";
        }
        if(valors <= Float.parseFloat(indic.getTramoInf2()))
        {
            imagen = indic.getImgagenInf();
            destramo = indic.getDescTramoInf();
            tramo = "Normal";
            semaforo = "V";
        }
        valor = Float.toString(valors);
    }

    public String getValorIndic()
    {
        return valor;
    }

    public String getDescTramo()
    {
        return destramo;
    }

    public String getImagen()
    {
        return imagen;
    }

    public String getQueTramo()
    {
        return tramo;
    }

    public String getSemaforo()
    {
        return semaforo;
    }

    private String valor;
    private String semaforo;
    private String imagen;
    private String destramo;
    private String tramo;
    private DetIndicador indic;
    private boolean existeValor;
}