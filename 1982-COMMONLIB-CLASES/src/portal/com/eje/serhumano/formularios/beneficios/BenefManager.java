package portal.com.eje.serhumano.formularios.beneficios;

import java.sql.Connection;
import java.sql.PreparedStatement;

import portal.com.eje.datos.Consulta;
import portal.com.eje.datos.Managers;
import portal.com.eje.tools.OutMessage;

public class BenefManager extends Managers
{

    public BenefManager(Connection conexion)
    {
        super(conexion);
    }

    public Consulta getBeneficios(String vigente)
    {
        boolean exist = false;
        Consulta consul = new Consulta(super.conexion);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT benef_id, benef_glosa FROM eje_ges_beneficio WHERE     (benef_vigente = '")).append(vigente).append("')").append(" ORDER BY benef_glosa")));
        OutMessage.OutMessagePrint("getBeneficios() --> ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        return consul;
    }

    public Consulta getBeneficios()
    {
        return getBeneficios("S");
    }

    public Consulta getBeneficio(int benef_id)
    {
        Consulta consul = new Consulta(super.conexion);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT benef_id, benef_glosa, benef_vigente FROM eje_ges_beneficio WHERE     (benef_id = ")).append(benef_id).append(")")));
        consul.exec(sql);
        OutMessage.OutMessagePrint("getBeneficio() --> ".concat(String.valueOf(String.valueOf(sql))));
        return consul;
    }

    public int addBeneficio(String glosa, String vigente)
    {
        boolean ok = false;
        int corr = -1;
        setError();
        try
        {
            Consulta consul = new Consulta(super.conexion);
            String sql = "SELECT max(benef_id) as maximo FROM  eje_ges_beneficio ";
            consul.exec(sql);
            corr = consul.next() ? consul.getInt("maximo") : 0;
            corr++;
            consul.close();
            sql = "INSERT INTO eje_ges_beneficio (benef_id, benef_glosa, benef_vigente) VALUES (?,?,?)";
            PreparedStatement pstmt = super.conexion.prepareStatement(sql);
            pstmt.setInt(1, corr);
            pstmt.setString(2, glosa);
            pstmt.setString(3, vigente);
            pstmt.executeUpdate();
            pstmt.close();
            ok = true;
        }
        catch(Exception e)
        {
            setError("Error al Agregar Beneficio", e);
            OutMessage.OutMessagePrint("Error en addBeneficio() --> ".concat(String.valueOf(String.valueOf(super.mensajeError))));
            corr = -1;
        }
        return corr;
    }

    public boolean updateBeneficio(int id, String glosa, String vigente)
    {
        boolean ok = false;
        setError();
        try
        {
            String sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE    eje_ges_beneficio SET benef_glosa = ?, benef_vigente = ? WHERE (benef_id = ")).append(id).append(")")));
            PreparedStatement pstmt = super.conexion.prepareStatement(sql);
            pstmt.setString(1, glosa);
            pstmt.setString(2, vigente);
            pstmt.executeUpdate();
            pstmt.close();
            ok = true;
        }
        catch(Exception e)
        {
            setError("Error al Actualizar Beneficio", e);
            OutMessage.OutMessagePrint("Error en updateBeneficio() --> ".concat(String.valueOf(String.valueOf(super.mensajeError))));
        }
        return ok;
    }
}