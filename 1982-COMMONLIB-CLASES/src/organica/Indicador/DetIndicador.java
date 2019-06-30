package organica.Indicador;

import organica.datos.Consulta;
import java.sql.Connection;
import java.util.Vector;
import organica.tools.OutMessage;

public class DetIndicador
{

    public DetIndicador()
    {
    }

    public DetIndicador(Connection conn, String item, String periodo)
    {
        Consulta datos = new Consulta(conn);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT periodo_desde, periodo_hasta, grupo, grupo_desc, item,  desc_ind, formula, desc_item, indicador, tabla, tramo_inf1,  tramo_inf2, tramo_medio2, tramo_sup1, tramo_medio1,  tramo_sup2, imag_inf, imag_medio, imag_sup, glosa,nombre,  desc_tramo_inf, desc_tramo_med, desc_tramo_sup FROM view_def_indicadores WHERE (item = '")).append(item).append("') AND (periodo_desde <= ").append(periodo).append(") AND ").append(" (periodo_hasta >= ").append(periodo).append(" OR periodo_hasta IS NULL)")));
        datos.exec(sql);
        if(datos.next())
            cargaDatos(datos);
        else
            existe = false;
        datos.close();
    }

    private void cargaDatos(Consulta datos)
    {
        existe = true;
        id = datos.getString("item");
        grupo = datos.getString("grupo");
        grupo_desc = datos.getString("grupo_desc");
        desc_ind = datos.getString("desc_ind");
        formula = datos.getString("formula");
        desc_item = datos.getString("desc_item");
        campo = datos.getString("indicador");
        tabla = datos.getString("tabla");
        tramo_inf1 = datos.getString("tramo_inf1");
        tramo_inf2 = datos.getString("tramo_inf2");
        tramo_medio1 = datos.getString("tramo_medio1");
        tramo_medio2 = datos.getString("tramo_medio2");
        tramo_sup1 = datos.getString("tramo_sup1");
        tramo_sup2 = datos.getString("tramo_sup2");
        imag_inf = datos.getString("imag_inf");
        imag_medio = datos.getString("imag_medio");
        imag_sup = datos.getString("imag_sup");
        glosa = datos.getString("glosa");
        nombre = datos.getString("nombre");
        desc_tramo_inf = datos.getString("desc_tramo_inf");
        desc_tramo_med = datos.getString("desc_tramo_med");
        desc_tramo_sup = datos.getString("desc_tramo_sup");
        periodo_desde = datos.getString("periodo_desde");
        periodo_hasta = datos.getString("periodo_hasta");
    }

    public static Vector creaListaDetIndicador(Connection conn, String periodo, String criterio)
    {
        Consulta datos = new Consulta(conn);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT periodo_desde, periodo_hasta, grupo, grupo_desc, item,  desc_ind, formula, desc_item, indicador, tabla, tramo_inf1,  tramo_inf2, tramo_medio2, tramo_sup1, tramo_medio1,  tramo_sup2, imag_inf, imag_medio, imag_sup, glosa,nombre,  desc_tramo_inf, desc_tramo_med, desc_tramo_sup FROM view_def_indicadores WHERE (periodo_desde <= ")).append(periodo).append(") AND ").append(" (periodo_hasta >= ").append(periodo).append(" OR periodo_hasta IS NULL)")));
        if(criterio != null && !criterio.trim().equals(""))
            sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append(" AND (").append(criterio).append(")")));
        OutMessage.OutMessagePrint("Detalle de Indicador --> ".concat(String.valueOf(String.valueOf(sql))));
        datos.exec(sql);
        Vector listaIndic = new Vector();
        int largo = 0;
        DetIndicador detIndic;
        for(; datos.next(); listaIndic.add(detIndic))
        {
            detIndic = new DetIndicador();
            detIndic.cargaDatos(datos);
        }

        datos.close();
        return listaIndic;
    }

    public boolean existeIndicador()
    {
        return existe;
    }

    public String getId()
    {
        return id;
    }

    public String getGrupo()
    {
        return grupo;
    }

    public String getDescGrupo()
    {
        return grupo_desc;
    }

    public String getGlosa()
    {
        return glosa;
    }

    public String getNombre()
    {
        return nombre;
    }

    public String getDescIndicador()
    {
        return desc_ind;
    }

    public String getFormula()
    {
        return formula;
    }

    public String getDescItem()
    {
        return desc_item;
    }

    public String getCampo()
    {
        return campo;
    }

    public String getTabla()
    {
        return tabla;
    }

    public String getTramoInf1()
    {
        return tramo_inf1;
    }

    public String getTramoInf2()
    {
        return tramo_inf2;
    }

    public String getTramoMedio1()
    {
        return tramo_medio1;
    }

    public String getTramoMedio2()
    {
        return tramo_medio2;
    }

    public String getTramoSup1()
    {
        return tramo_sup1;
    }

    public String getTramoSup2()
    {
        return tramo_sup2;
    }

    public String getImgagenInf()
    {
        return imag_inf;
    }

    public String getImgagenMedio()
    {
        return imag_medio;
    }

    public String getImgagenSup()
    {
        return imag_sup;
    }

    public String getDescTramoInf()
    {
        return desc_tramo_inf;
    }

    public String getDescTramoMed()
    {
        return desc_tramo_med;
    }

    public String getDescTramoSup()
    {
        return desc_tramo_sup;
    }

    public String getPeriodoDesde()
    {
        return periodo_desde;
    }

    public String getPeriodoHasta()
    {
        return periodo_hasta;
    }

    public boolean perteneceAlPeriodo(String periodo)
    {
        boolean pertenece = false;
        if(periodo != null && Integer.parseInt(periodo_desde) <= Integer.parseInt(periodo))
            if(periodo_hasta == null)
                pertenece = true;
            else
            if(Integer.parseInt(periodo_hasta) >= Integer.parseInt(periodo))
                pertenece = true;
        return pertenece;
    }

    private String grupo;
    private String grupo_desc;
    private String glosa;
    private String nombre;
    private String desc_ind;
    private String formula;
    private String desc_item;
    private String campo;
    private String tabla;
    private String tramo_inf1;
    private String tramo_inf2;
    private String tramo_medio1;
    private String tramo_medio2;
    private String tramo_sup1;
    private String tramo_sup2;
    private String imag_inf;
    private String imag_medio;
    private String imag_sup;
    private String desc_tramo_inf;
    private String desc_tramo_med;
    private String desc_tramo_sup;
    private String periodo_desde;
    private String periodo_hasta;
    private String id;
    private boolean existe;
}