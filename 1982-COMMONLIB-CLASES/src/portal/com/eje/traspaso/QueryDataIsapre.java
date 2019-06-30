package portal.com.eje.traspaso;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;

public class QueryDataIsapre
{

    public QueryDataIsapre(Connection conexion, String rut, int cod_empresa, int cod_planta)
    {
        IsapreCotiza = "0";
        IsapreCotizaAd = "0";
        IsapreCotizaM = "0";
        IsapreCotizaAdM = "0";
        IsapreCotizaMoneda = null;
        IsapreCotizaAdMoneda = null;
        Consulta consul = new Consulta(conexion);
        String sql = "SELECT MTO_PACTADO_ISAPRE, UNID_COB_MTO_PACTA, ADICIONAL_ISAPRE, MONE_VAL_ADIC_SALU FROM PERSONAL WHERE NRO_TRABAJADOR = " + rut + " AND COD_EMPRESA = " + cod_empresa + " AND COD_PLANTA = " + cod_planta;
        consul.exec(sql);
        if(consul.next())
        {
            IsapreCotiza = consul.getString("MTO_PACTADO_ISAPRE");
            IsapreCotizaM = "0";
            IsapreCotizaMoneda = consul.getString("UNID_COB_MTO_PACTA");
            IsapreCotizaAd = consul.getString("ADICIONAL_ISAPRE");
            IsapreCotizaAdM = "0";
            IsapreCotizaAdMoneda = consul.getString("MONE_VAL_ADIC_SALU");
        }
        consul.close();
    }

    public String IsapreCotiza;
    public String IsapreCotizaAd;
    public String IsapreCotizaM;
    public String IsapreCotizaAdM;
    public String IsapreCotizaMoneda;
    public String IsapreCotizaAdMoneda;
}