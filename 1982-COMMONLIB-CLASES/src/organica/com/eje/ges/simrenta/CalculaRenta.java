package organica.com.eje.ges.simrenta;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.Indicador.Periodo;
import organica.com.eje.datos.ConexionODBC;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import organica.tools.Validar;
import organica.tools.servlet.FormatoFecha;
import organica.tools.servlet.FormatoNumero;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

public class CalculaRenta extends MyHttpServlet
{

    public CalculaRenta()
    {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doGet(req, resp);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doGet de SimulaRenta");
        user = Usuario.rescatarUsuario(req);
        String strRut = user.getRutConsultado();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "Simulador de Renta", "Tiempo de Sesi\363n expirado...");
        else
        if(!control.tienePermisoJerarquico(Conexion, "df_sim_rta", strRut))
            mensaje.devolverPaginaMensage(resp, "Simulador de Renta", "Usted no tiene permiso para Realizar esta Acci\363n...");
        else
            generaPagina(req, resp, "", user, Conexion);
        OutMessage.OutMessagePrint("Fin de doGet");
        connMgr.freeConnection("portal", Conexion);
    }

    public void generaPagina(HttpServletRequest req, HttpServletResponse resp, String msg, Usuario user, Connection conexion)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\nEntro a generaPagina");
        Validar valida = new Validar();
        OutMessage.OutMessagePrint("parametros\n ".concat(String.valueOf(String.valueOf(req.getQueryString()))));
        String paramRutTrab = req.getParameter("rut");
        String paramEmpresa = req.getParameter("empresa");
        String paramUnidad = req.getParameter("unidad");
        String paramCargo = req.getParameter("cargo");
        String paramCualItem = req.getParameter("que_iten");
        String paramTraslado = "0";
        String sql = null;
        int paramValorReal = 0;
        int paramValorEspe = 0;
        int paramMesEfecto = 12;
        try
        {
            paramValorReal = Integer.parseInt(valida.validarDato(req.getParameter("valor_real"), "0"));
        }
        catch(NumberFormatException e)
        {
            OutMessage.OutMessagePrint("NumberFormatException: ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        try
        {
            paramValorEspe = Integer.parseInt(valida.validarDato(req.getParameter("valor_esp"), "0"));
        }
        catch(NumberFormatException e)
        {
            OutMessage.OutMessagePrint("NumberFormatException: ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        try
        {
            if(req.getParameter("mes_efecto") != null)
            {
                paramMesEfecto = Integer.parseInt(req.getParameter("mes_efecto"));
            } else
            {
                GregorianCalendar calendario = new GregorianCalendar();
                paramMesEfecto = calendario.get(2) + 1;
            }
        }
        catch(NumberFormatException e)
        {
            OutMessage.OutMessagePrint("NumberFormatException: ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        paramTraslado = req.getParameter("tras");
        String Opc = "M";
        if(req.getParameter("radiobutton") != null)
        {
            Opc = req.getParameter("radiobutton");
            if("P".equals(Opc))
                paramValorEspe = (paramValorReal * (100 + paramValorEspe)) / 100;
            if("D".equals(Opc))
                paramValorEspe += paramValorReal;
        }
        OutMessage.OutMessagePrint("\tque_item  --> ".concat(String.valueOf(String.valueOf(paramCualItem))));
        OutMessage.OutMessagePrint("\tValorReal --> ".concat(String.valueOf(String.valueOf(paramValorReal))));
        OutMessage.OutMessagePrint("\tValorEspe --> ".concat(String.valueOf(String.valueOf(paramValorEspe))));
        String html = req.getParameter("htm");
        if(html == null)
            html = "CalculaRenta.htm";
        OutMessage.OutMessagePrint("html --> ".concat(String.valueOf(String.valueOf(html))));
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("FFecha", new FormatoFecha());
        modelRoot.put("FNum", new FormatoNumero());
        try
        {
            sql = "";
            if("4".equalsIgnoreCase(paramCualItem))
                sql = "df_sim_brutoregular";
            else
            if("7".equalsIgnoreCase(paramCualItem))
                sql = "df_sim_brutozona";
            else
            if("13".equalsIgnoreCase(paramCualItem))
                sql = "df_sim_brutopromedio";
            else
            if("16".equalsIgnoreCase(paramCualItem))
                sql = "df_sim_brutototal";
            else
            if("5".equalsIgnoreCase(paramCualItem))
                sql = "df_sim_netoregular";
            else
            if("8".equalsIgnoreCase(paramCualItem))
                sql = "df_sim_netozona";
            else
            if("14".equalsIgnoreCase(paramCualItem))
                sql = "df_sim_netopromedio";
            else
            if("17".equalsIgnoreCase(paramCualItem))
                sql = "df_sim_netototal";
            if("1".equalsIgnoreCase(paramCualItem))
                sql = "df_sim_sbasenew";
            OutMessage.OutMessagePrint("Procedure ".concat(String.valueOf(String.valueOf(sql))));
            sql = String.valueOf(String.valueOf((new StringBuilder("{call ")).append(sql).append("(?,?,?,?,?,?,?,?,?,?,").append("?,?,?,?,?,?,?,?,?,?,").append("?,?,?,?,?,").append("?,?,?,?,?,?)}")));
            ConexionODBC con = new ConexionODBC();
            Connection conex = con.Conecta();
            CallableStatement stm = conex.prepareCall(sql);
            stm.setInt(1, Integer.parseInt(paramRutTrab));
            stm.setInt(2, Integer.parseInt(req.getParameter("sueldo")));
            stm.setInt(3, Integer.parseInt(req.getParameter("gestion")));
            stm.setInt(4, paramValorReal);
            stm.setInt(5, paramValorEspe);
            stm.setInt(6, Integer.parseInt(paramCargo));
            stm.setInt(7, Integer.parseInt(paramEmpresa));
            stm.setInt(8, Integer.parseInt(paramUnidad));
            stm.setInt(9, Integer.parseInt(paramTraslado));
            stm.registerOutParameter(10, 12);
            stm.registerOutParameter(11, 12);
            stm.registerOutParameter(12, 12);
            stm.registerOutParameter(13, 12);
            stm.registerOutParameter(14, 12);
            stm.registerOutParameter(15, 12);
            stm.registerOutParameter(16, 12);
            stm.registerOutParameter(17, 12);
            stm.registerOutParameter(18, 12);
            stm.registerOutParameter(19, 12);
            stm.registerOutParameter(20, 12);
            stm.registerOutParameter(21, 12);
            stm.registerOutParameter(22, 12);
            stm.registerOutParameter(23, 12);
            stm.registerOutParameter(24, 12);
            stm.registerOutParameter(25, 12);
            stm.registerOutParameter(26, 12);
            stm.setInt(27, req.getParameter("check_b_anual") != null ? 0 : 1);
            stm.setInt(28, req.getParameter("check_caja") != null ? 0 : 1);
            stm.setInt(29, req.getParameter("check_b_antig") != null ? 0 : 1);
            stm.setInt(30, req.getParameter("check_rta_var") != null ? 0 : 1);
            stm.setInt(31, req.getParameter("check_tras") != null ? 0 : 1);
            stm.execute();
            for(int x = 10; x <= 26; x++)
                OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("param ")).append(x).append(": ").append(stm.getString(x)))));

            modelRoot.put("sueldo", stm.getString(10));
            modelRoot.put("gestion", stm.getString(11));
            modelRoot.put("movi", stm.getString(12));
            modelRoot.put("b_reg", stm.getString(13));
            modelRoot.put("n_reg", stm.getString(14));
            modelRoot.put("zona", stm.getString(15));
            modelRoot.put("b_zona", stm.getString(16));
            modelRoot.put("n_zona", stm.getString(17));
            modelRoot.put("b_anual", stm.getString(18));
            modelRoot.put("caja", stm.getString(19));
            modelRoot.put("b_antig", stm.getString(20));
            modelRoot.put("rta_var", stm.getString(21));
            modelRoot.put("b_prom", stm.getString(22));
            modelRoot.put("n_prom", stm.getString(23));
            modelRoot.put("tras", stm.getString(24));
            modelRoot.put("b_total", stm.getString(25));
            modelRoot.put("n_total", stm.getString(26));
            int netoTotal1 = 0;
            try
            {
                netoTotal1 = Integer.parseInt(req.getParameter("n_total"));
            }
            catch(NumberFormatException e)
            {
                OutMessage.OutMessagePrint("NumberFormatException: ".concat(String.valueOf(String.valueOf(e.getMessage()))));
            }
            int netoTotal2 = stm.getInt(26);
            modelRoot.put("efecto_ra", String.valueOf(((12 - paramMesEfecto) + 1) * (netoTotal2 - netoTotal1)));
            modelRoot.put("efecto_12m", String.valueOf(12 * (netoTotal2 - netoTotal1)));
            modelRoot.put("mes_efecto", String.valueOf(paramMesEfecto));
            String nivel = req.getParameter("nivel");
            if(nivel == null || "".equals(nivel))
                nivel = null;
            int neto_prom = stm.getInt(23);
            stm.close();
            conex.close();
            String codRemu = req.getParameter("cod_remu");
            String paramCargoAnt = req.getParameter("cargo_ant");
            String valor[] = calcularCodRemu(codRemu, nivel, neto_prom, paramEmpresa, paramUnidad, paramCargo, paramCargoAnt, conexion);
            modelRoot.put("cod_remu", valor[0]);
            modelRoot.put("pre", valor[1]);
        }
        catch(SQLException e)
        {
            OutMessage.OutMessagePrint("SQLException --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        super.retTemplate(resp,"Gestion/SimRenta/" + html,modelRoot);
        OutMessage.OutMessagePrint("Fin de generaPagina");
    }

    private String[] calcularCodRemu(String codRemu, String paramNivel, int renta, String empresa, String unidad, String cargo, String cargoAnt, 
            Connection conexion)
    {
        OutMessage.OutMessagePrint("\n<-- Calculo de CodRemu --> ");
        OutMessage.OutMessagePrint("\tRemu  : ".concat(String.valueOf(String.valueOf(codRemu))));
        OutMessage.OutMessagePrint("\tnivel : ".concat(String.valueOf(String.valueOf(paramNivel))));
        OutMessage.OutMessagePrint("\trenta : ".concat(String.valueOf(String.valueOf(renta))));
        OutMessage.OutMessagePrint("\tempre.: ".concat(String.valueOf(String.valueOf(empresa))));
        OutMessage.OutMessagePrint("\tunidad: ".concat(String.valueOf(String.valueOf(unidad))));
        OutMessage.OutMessagePrint("\tcargo_ant : ".concat(String.valueOf(String.valueOf(cargoAnt))));
        OutMessage.OutMessagePrint("\tcargo : ".concat(String.valueOf(String.valueOf(cargo))));
        Consulta consul = new Consulta(conexion);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT estamento FROM eje_ges_estamentos_cargos WHERE (cargo = '")).append(cargo).append("')")));
        consul.exec(sql);
        String estamento = "";
        if(consul.next())
            estamento = consul.getString("estamento");
        OutMessage.OutMessagePrint("\n\t--> Estamento : ".concat(String.valueOf(String.valueOf(estamento))));
        if(paramNivel == null || "".equals(paramNivel))
        {
            sql = String.valueOf(String.valueOf((new StringBuilder("SELECT cod_familia, BCI_1, BCI_2, BCI_3, BCI_4, NIVEL_1, NIVEL_2, NIVEL_3, NIVEL_4 FROM eje_ges_cargo_familia WHERE (empresa = '")).append(empresa).append("') AND (cod_cargo = '").append(cargo).append("')")));
            consul.exec(sql);
            String cargoFamilia = "";
            String niveles[] = new String[8];
            if(consul.next())
            {
                cargoFamilia = consul.getString("cod_familia");
                niveles[0] = consul.getString("BCI_1");
                niveles[1] = consul.getString("BCI_2");
                niveles[2] = consul.getString("BCI_3");
                niveles[3] = consul.getString("BCI_4");
                niveles[4] = consul.getString("NIVEL_1");
                niveles[5] = consul.getString("NIVEL_2");
                niveles[6] = consul.getString("NIVEL_3");
                niveles[7] = consul.getString("NIVEL_4");
            }
            boolean cargoFamEsp = "20000".equals(cargoFamilia) || "20000".equals(cargoFamilia);
            OutMessage.OutMessagePrint("\n\tFamilia Cargo : ".concat(String.valueOf(String.valueOf(cargoFamilia))));
            if(!cargoFamEsp)
            {
                OutMessage.OutMessagePrint("\tNo Pertenece a fam Especial");
                if(cargo.equals(cargoAnt))
                {
                    OutMessage.OutMessagePrint("\tNo cambio de Cargo.");
                    paramNivel = getNivelActual(codRemu);
                } else
                {
                    OutMessage.OutMessagePrint("\tCambio de Cargo.");
                    paramNivel = getNivelCalc(niveles, 0);
                }
            } else
            {
                OutMessage.OutMessagePrint("\tPertenece a fam Especial");
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT tam_comercial, tam_operacional FROM eje_ges_centro_trabajo WHERE (empresa = '")).append(empresa).append("') AND (cod_centra = '").append(unidad).append("')")));
                consul.exec(sql);
                int desde = 0;
                if(consul.next())
                {
                    String campo = "20000".equals(cargoFamilia) ? "tam_comercial" : "tam_operacional";
                    String tamano = consul.getString(campo);
                    OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("\tTama\361o(")).append(campo).append(") CT = ").append(tamano))));
                    if("1".equals(tamano) || "P".equalsIgnoreCase(tamano))
                        desde = 0;
                    else
                    if("2".equals(tamano) || "M".equalsIgnoreCase(tamano))
                        desde = 1;
                    else
                    if("3".equals(tamano) || "G".equalsIgnoreCase(tamano))
                        desde = 2;
                    else
                    if("4".equals(tamano) || "MG".equalsIgnoreCase(tamano))
                        desde = 3;
                }
                paramNivel = getNivelCalc(niveles, desde);
            }
        }
        OutMessage.OutMessagePrint("\t--> Nivel : ".concat(String.valueOf(String.valueOf(paramNivel))));
        Periodo peri = new Periodo(conexion);
        sql = String.valueOf(String.valueOf((new StringBuilder("SELECT rta_promedio FROM eje_ges_escala_salarial WHERE (periodo = ")).append(peri.getPeriodo()).append(")").append(" AND (nivel = '").append(paramNivel).append("')").append(" AND (familia_cargo = '").append(estamento).append("')")));
        consul.exec(sql);
        int pto_medio = consul.next() ? consul.getInt("rta_promedio") : 0;
        OutMessage.OutMessagePrint("\n\tPto Medio: ".concat(String.valueOf(String.valueOf(pto_medio))));
        float pre = 0.0F;
        if(pto_medio == 0)
        {
            OutMessage.OutMessagePrint("\tNo es posible calcular pre");
        } else
        {
            pre = renta * 100;
            pre /= pto_medio;
        }
        OutMessage.OutMessagePrint("\t--> Pre : ".concat(String.valueOf(String.valueOf(pre))));
        String zona = "";
        sql = String.valueOf(String.valueOf((new StringBuilder("SELECT inicio_zona_a, inicio_zona_b, inicio_zona_c, fin_zona_c FROM eje_ges_escala_niveles_porc WHERE (id_familia_puesto = '")).append(estamento).append("') AND (nivel = '").append(paramNivel).append("')")));
        consul.exec(sql);
        float valor = 0.0F;
        if(consul.next())
            if(pre < consul.getFloat("inicio_zona_a"))
                zona = "-A";
            else
            if(pre < consul.getFloat("inicio_zona_b"))
                zona = "A";
            else
            if(pre < consul.getFloat("inicio_zona_c"))
                zona = "B";
            else
            if(pre < consul.getFloat("fin_zona_c"))
                zona = "C";
            else
                zona = "+C";
        OutMessage.OutMessagePrint("\t--> Zona : ".concat(String.valueOf(String.valueOf(zona))));
        consul.close();
        String valores[] = new String[2];
        valores[0] = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(estamento)))).append(paramNivel).append(zona)));
        valores[1] = String.valueOf(pre);
        OutMessage.OutMessagePrint("<-- Fin Calculo de CodRemu -->\n");
        return valores;
    }

    private String getNivelActual(String codRemu)
    {
        OutMessage.OutMessagePrint("\tgetNivelActual");
        String nivel = "";
        for(int x = 0; x < codRemu.length(); x++)
        {
            char c = codRemu.charAt(x);
            OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("\t   char(")).append(x).append("): ").append(c))));
            if(Character.isDigit(c))
            {
                nivel = String.valueOf(nivel) + String.valueOf(c);
                continue;
            }
            if(!"".equals(nivel))
                break;
        }

        return nivel;
    }

    private String getNivelCalc(String niveles[], int desde)
    {
        OutMessage.OutMessagePrint("\tgetNivelCalc desde: ".concat(String.valueOf(String.valueOf(desde))));
        String nivel = "";
        int x = desde;
        do
        {
            OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("\t   nivel(")).append(x).append("): ").append(niveles[x]))));
            if(niveles[x] != null && !"0".equals(niveles[x]))
            {
                nivel = niveles[x];
                break;
            }
            if(++x >= niveles.length)
                x = 0;
        } while(x != desde);
        return nivel;
    }

    private Usuario user;
    private Tools tool;
    private Mensaje mensaje;
    static final int BUSQ_NIVEL_INICIO = 0;
}