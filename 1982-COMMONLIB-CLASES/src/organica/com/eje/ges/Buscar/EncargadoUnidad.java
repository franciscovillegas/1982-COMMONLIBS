package organica.com.eje.ges.Buscar;

import java.sql.Connection;

import organica.Indicador.Periodo;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.Validar;
import portal.com.eje.vistas.QueryPortal;
import portal.com.eje.vistas.QueryPortal.QueryPortalType;

public class EncargadoUnidad
{

    public EncargadoUnidad()
    {
        MisionUni = "";
        RutEnc = "";
        NombreEnc = "";
        Periodo = "";
        rutCambios = "";
        NombreCambios = "";
        fec_inicio = "";
        fec_actualiza = "";
        TotalUniAut = 0;
        TotalUniReal = 0;
        TotalUniDif = 0;
        TotalUniAutR = 0;
        TotalUniRealR = 0;
        TotalUniDifR = 0;
    }

    public EncargadoUnidad(Connection Conexion, String unidad, String empresa)
    {
        MisionUni = "";
        RutEnc = "";
        NombreEnc = "";
        Periodo = "";
        rutCambios = "";
        NombreCambios = "";
        fec_inicio = "";
        fec_actualiza = "";
        TotalUniAut = 0;
        TotalUniReal = 0;
        TotalUniDif = 0;
        TotalUniAutR = 0;
        TotalUniRealR = 0;
        TotalUniDifR = 0;
        Consulta info = new Consulta(Conexion);
        Periodo peri = new Periodo(Conexion);
        String rutResponsable = "";
        Validar valida = new Validar();
//        String consulta = String.valueOf(new StringBuilder("SELECT a.rut_encargado,a.nombre, a.mision,a.fec_ini,a.fec_actualiza,a.rut_cambios,a.nom_cambios FROM (").append(QueryPortal.getInstance().getQuery(QueryPortalType.viewUnidadEncargado)).append(") a,eje_ges_unidad_rama b WHERE a.periodo = " + peri.getPeriodo() + " AND b.unidad = '" + unidad + "' AND a.unid_empresa='" + empresa + "' AND a.estado = '1' AND b.tipo = 'U' AND  b.uni_rama = a.unid_id AND a.unid_empresa = b.empresa"));
//        String consulta = String.valueOf(new StringBuilder("SELECT a.rut_encargado,a.nombre, a.mision,a.fec_ini,a.fec_actualiza,a.rut_cambios,a.nom_cambios FROM viewUnidadEncargado a,eje_ges_unidad_rama b WHERE a.periodo = " + peri.getPeriodo() + " AND b.unidad = '" + unidad + "' AND a.unid_empresa='" + empresa + "' AND a.estado = '1' AND b.tipo = 'U' AND  b.uni_rama = a.unid_id AND a.unid_empresa = b.empresa"));

        
        String consulta = String.valueOf(new StringBuilder("select	a.rut_encargado, a.mision, b.nombre, a.rut_cambios, null nom_cambios, fec_ini, fec_actualiza ")
        .append("from	eje_ges_unidad_encargado		a ")
        .append("		left join eje_ges_trabajador	b on a.rut_encargado = b.rut ")
        .append("where	a.periodo = ").append(peri.getPeriodo())		
        .append(" 		and a.unid_id = '").append(unidad).append("'"));
        
        Periodo = peri.getPeriodo();
        info.exec(consulta);
        if(info.next())
        {
            rutResponsable = info.getString("rut_encargado");
            RutEnc = rutResponsable;
            MisionUni = info.getString("mision");
            NombreEnc = info.getString("nombre");
            rutCambios = info.getString("rut_cambios");
            NombreCambios = info.getString("nom_cambios");
            fec_inicio = valida.validarDato(info.getString("fec_ini"));
            fec_actualiza = valida.validarFecha(info.getValor("fec_actualiza"));
        } else
        {
            rutResponsable = "-1";
            RutEnc = rutResponsable;
            MisionUni = null;
            NombreEnc = null;
            rutCambios = null;
            NombreCambios = null;
            fec_inicio = null;
            fec_actualiza = null;
        }
        info.close();
    }

    public String getFecInicio(Connection Conexion, String unidad, String empresa)
    {
        Consulta info = new Consulta(Conexion);
        Periodo peri = new Periodo(Conexion);
        String rutResponsable = "";
        String inicio = null;
        Validar valida = new Validar();
        String consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT rut_encargado,nombre, mision,fec_ini,fec_actualiza,rut_cambios,nom_cambios FROM view_unidad_encargado WHERE (periodo = ")).append(peri.getPeriodo()).append(") ").append("AND (unid_id = '").append(unidad).append("') ").append("AND (unid_empresa='").append(empresa).append("') ").append("AND (estado='1')")));
        info.exec(consulta);
        if(info.next())
            inicio = valida.validarDato(info.getString("fec_ini"), null);
        info.close();
        return inicio;
    }

    public void DotacionTotalUnidad(Connection Conexion, String unidad, String empresa)
    {
        Consulta info = new Consulta(Conexion);
        Periodo peri = new Periodo(Conexion);
        String consul = String.valueOf(String.valueOf((new StringBuilder("SELECT planta_indef, dot_aut FROM eje_ges_indic_bae_dotacion WHERE (uni_rama='U') and (empresa = '")).append(empresa).append("') AND (unidad = '").append(unidad).append("') AND ").append(" (periodo = ").append(peri.getPeriodo()).append(")")));
        info.exec(consul);
        if(info.next())
        {
            TotalUniAut = info.getInt("dot_aut");
            TotalUniReal = info.getInt("planta_indef");
            TotalUniDif = info.getInt("dot_aut") - info.getInt("planta_indef");
        } else
        {
            TotalUniAut = 0;
            TotalUniReal = 0;
            TotalUniDif = 0;
        }
        info.close();
    }

    public void DotacionTotalUnidadRama(Connection Conexion, String unidad, String empresa)
    {
        Consulta info = new Consulta(Conexion);
        Periodo peri = new Periodo(Conexion);
        String consul = String.valueOf(String.valueOf((new StringBuilder("SELECT planta_indef, dot_aut FROM eje_ges_indic_bae_dotacion WHERE (uni_rama='R') and (empresa = '")).append(empresa).append("') AND (unidad = '").append(unidad).append("') AND ").append(" (periodo = ").append(peri.getPeriodo()).append(")")));
        info.exec(consul);
        if(info.next())
        {
            TotalUniAutR = info.getInt("dot_aut");
            TotalUniRealR = info.getInt("planta_indef");
            TotalUniDifR = info.getInt("dot_aut") - info.getInt("planta_indef");
        } else
        {
            TotalUniAutR = 0;
            TotalUniRealR = 0;
            TotalUniDifR = 0;
        }
        info.close();
    }

    public void setEncargadoInactivo(Connection Conexion, Usuario user, String empresa, String unidad, String periodo, String antiguo)
    {
        Consulta info = new Consulta(Conexion);
        String sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_unidad_encargado SET estado = '0',rut_cambios= ")).append(user.getRutUsuario()).append("WHERE (unid_empresa='").append(empresa).append("') ").append("and (unid_id='").append(unidad).append("') ").append("and (periodo=").append(periodo).append(") ").append("and (rut_encargado=").append(antiguo).append(")")));
        info.insert(sql);
        info.close();
    }

    public void setEncargadosInactivos(Connection Conexion, Usuario user, String empresa, String unidad)
    {
        Consulta info = new Consulta(Conexion);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT * FROM eje_ges_unidad_encargado WHERE (unid_empresa = '")).append(empresa).append("') ").append("AND (unid_id = '").append(unidad).append("')")));
        info.exec(sql);
        for(; info.next(); info.insert(sql))
            sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_unidad_encargado SET estado = '0',rut_cambios= ")).append(user.getRutUsuario()).append(" WHERE (unid_empresa='").append(empresa).append("') ").append("and (unid_id='").append(unidad).append("')")));

        info.close();
    }

    public String MisionUni;
    public String RutEnc;
    public String NombreEnc;
    public String Periodo;
    public String rutCambios;
    public String NombreCambios;
    public String fec_inicio;
    public String fec_actualiza;
    public int TotalUniAut;
    public int TotalUniReal;
    public int TotalUniDif;
    public int TotalUniAutR;
    public int TotalUniRealR;
    public int TotalUniDifR;
}