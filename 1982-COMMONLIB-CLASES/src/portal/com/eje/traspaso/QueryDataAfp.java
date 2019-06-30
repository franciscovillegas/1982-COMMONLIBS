package portal.com.eje.traspaso;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;

public class QueryDataAfp
{

    public QueryDataAfp(Connection conexion, String rut, int cod_empresa, int cod_planta)
    {
        AfpCotiza = 0.0F;
        AfpCotizaAd = 0.0F;
        AfpAhorroVo = 0.0F;
        AfpDepConve = 0.0F;
        AfpCotizaM = 0.0F;
        AfpCotizaAdM = 0.0F;
        AfpAhorroVoM = 0.0F;
        AfpDepConveM = 0.0F;
        AfpCotizaMoneda = null;
        AfpCotizaAdMoneda = null;
        AfpAhorroVoMoneda = null;
        AfpDepConveMoneda = null;
        Consulta consul = new Consulta(conexion);
        Consulta consulAux = new Consulta(conexion);
        String sql = "SELECT MTO_DCTO_CTA_AHORR, UNID_COBRO_CTA_AHO,   MTO_COTIZ_VOLUNTAR, UNID_COB_MTO_VOLUN,  SUELDO_MENSUAL, MONEDA_SUELDO, COD_AFP  FROM PERSONAL WHERE RUT_TRABAJADOR = '" + rut + "'  AND " + " COD_EMPRESA = " + cod_empresa + " AND COD_PLANTA = " + cod_planta;
        consul.exec(sql);
        if(consul.next())
        {
            String sqlAux = "SELECT PJE_COTIZ_PREVISIO FROM AFP WHERE COD_VIGENTE = 'S' AND COD_AFP = " + consul.getString("COD_AFP");
            consulAux.exec(sqlAux);
            if(consulAux.next())
                AfpCotiza = consulAux.getFloat("PJE_COTIZ_PREVISIO");
            AfpCotizaM = consul.getFloat("SUELDO_MENSUAL");
            AfpCotizaMoneda = consul.getString("UNID_COBRO_CTA_AHO");
            AfpCotiza += 0.0F;
            AfpCotizaAd = 0.0F;
            AfpCotizaAdM = 0.0F;
            AfpCotizaAdMoneda = null;
            AfpAhorroVo = consul.getFloat("MTO_COTIZ_VOLUNTAR");
            AfpAhorroVoM = consul.getFloat("SUELDO_MENSUAL");
            AfpAhorroVoMoneda = consul.getString("UNID_COB_MTO_VOLUN");
            AfpDepConve = 0.0F;
            AfpDepConveM = 0.0F;
            AfpDepConveMoneda = null;
        }
        consul.close();
        consulAux.close();
    }

    public float AfpCotiza;
    public float AfpCotizaAd;
    public float AfpAhorroVo;
    public float AfpDepConve;
    public float AfpCotizaM;
    public float AfpCotizaAdM;
    public float AfpAhorroVoM;
    public float AfpDepConveM;
    public String AfpCotizaMoneda;
    public String AfpCotizaAdMoneda;
    public String AfpAhorroVoMoneda;
    public String AfpDepConveMoneda;
}