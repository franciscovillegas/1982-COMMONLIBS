package organica.com.eje.ges.gestion.mantencion;

import organica.datos.Consulta;
import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;
import organica.tools.OutMessage;
import organica.tools.Validar;

public class InsertaHojaVida
{

    public InsertaHojaVida()
    {
    }

    public boolean InsertaHojaVida(Connection conn, String fecha, String empresa, String rut, String rut_sup, String tipo_obs, HttpServletRequest req)
    {
        boolean Grabar = false;
        String sql = "";
        if(conn != null)
        {
            Consulta consul = new Consulta(conn);
            sql = String.valueOf(String.valueOf((new StringBuilder("SELECT MAX(codigo) AS maxi FROM eje_ges_hoja_vida WHERE rut = ")).append(rut).append(" AND empresa = '").append(empresa).append("'")));
            consul.exec(sql);
            consul.next();
            Validar x = new Validar();
            x.setFormatoFecha("mm/dd/yyyy");
            fecha = x.validarFecha(fecha);
            x.setFormatoFecha("dd/mm/yyyy");
            int codigo = consul.getInt("maxi") + 1;
            sql = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_hoja_vida (tipo_obs,empresa, codigo, rut, fecha, observacion, rut_sup) VALUES (")).append(req.getParameter("tipo")).append(",'").append(empresa).append("', ").append(codigo).append(", ").append(rut).append(", getDate(), '").append(req.getParameter("obs")).append("', ").append(rut_sup).append(")")));
            if(consul.insert(sql))
            {
                OutMessage.OutMessagePrint("sql:para ".concat(String.valueOf(String.valueOf(sql))));
                Grabar = true;
            }
        }
        return Grabar;
    }
}