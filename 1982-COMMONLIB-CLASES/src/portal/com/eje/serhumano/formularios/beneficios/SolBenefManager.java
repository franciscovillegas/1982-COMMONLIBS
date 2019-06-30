package portal.com.eje.serhumano.formularios.beneficios;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

import portal.com.eje.datos.Consulta;
import portal.com.eje.datos.Managers;
import portal.com.eje.tools.OutMessage;

public class SolBenefManager extends Managers
{

    public SolBenefManager(Connection conexion)
    {
        super(conexion);
    }

    public int addSolBeneficio(int rut, int benefId, String observ)
    {
        boolean ok = false;
        int corr = -1;
        setError();
        try
        {
            Consulta consul = new Consulta(super.conexion);
            String sql = "SELECT max(sol_num) as maximo FROM  eje_ges_sol_beneficio ";
            consul.exec(sql);
            corr = consul.next() ? consul.getInt("maximo") : 0;
            corr++;
            consul.close();
            sql = "INSERT INTO eje_ges_sol_beneficio (sol_num, rut, benef_id, fec_sol, observ) VALUES (?, ?, ?, ?, ?)";
            Date fecSol = new Date((new java.util.Date()).getTime());
            PreparedStatement pstmt = super.conexion.prepareStatement(sql);
            pstmt.setInt(1, corr);
            pstmt.setInt(2, rut);
            pstmt.setInt(3, benefId);
            pstmt.setDate(4, fecSol);
            pstmt.setString(5, observ);
            pstmt.executeUpdate();
            pstmt.close();
            ok = true;
        }
        catch(Exception e)
        {
            setError("Error al Agregar Solicitud de Beneficio", e);
            OutMessage.OutMessagePrint("Error en addSolBeneficio() --> ".concat(String.valueOf(String.valueOf(super.mensajeError))));
            corr = -1;
        }
        return corr;
    }

    public Consulta getSolicitudes(String filtro)
    {
        boolean exist = false;
        Consulta consul = new Consulta(super.conexion);
        String sql = "SELECT     fec_sol, sol_num, rut, digito_ver, nombre, empresa, unidad, benef_id, benef_glosa, observ FROM view_sol_beneficios";
        if(filtro != null && !"".equals(filtro))
            sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append(" WHERE (").append(filtro).append(")")));
        sql = String.valueOf(String.valueOf(sql)).concat(" ORDER BY fec_sol");
        OutMessage.OutMessagePrint("getSolicitudes() --> ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        return consul;
    }
}