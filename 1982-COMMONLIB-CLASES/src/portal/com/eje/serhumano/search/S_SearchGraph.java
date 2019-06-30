package portal.com.eje.serhumano.search;

import java.io.IOException;
import java.sql.Connection;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Validar;
import portal.com.eje.tools.servlet.GetProp;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class S_SearchGraph extends MyHttpServlet
{

    public S_SearchGraph()
    {
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = super.connMgr.getConnection("portal");
        int gruposDe = Integer.parseInt(req.getParameter("grupo"));
        if(Conexion != null)
        {
            user = SessionMgr.rescatarUsuario(req);
            if(user.esValido())
                CargaPagina(req, resp, gruposDe, Conexion, user);
            else
                super.mensaje.devolverPaginaSinSesion(resp, "Busqueda Gr\341fica", "Tiempo de Sesi\363n expirado...");
        } else
        {
            super.mensaje.devolverPaginaMensage(resp, "Problemas T\351cnicos", "Error de conexi\363n a la BD");
        }
        super.connMgr.freeConnection("portal", Conexion);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doPost(req, resp);
    }

    public void CargaPagina(HttpServletRequest req, HttpServletResponse resp, int agrupar, Connection Conexion, Usuario user)
        throws ServletException, IOException
    {
        OutMessage.OutMessagePrint("\n****Entro a CargaPagina: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        String sentencia = "";
        SimpleHash modelRoot = new SimpleHash();
        SimpleList simplelist = new SimpleList();
        SimpleList links = new SimpleList();
        int edad1 = 0;
        int edad2 = 0;
        int regs = 0;
        int paginas = 0;
        int numS = 0;
        String sgteInicio = "";
        String sgteTermino = "";
        String sgteNum = "";
        String start = "";
        String finish = "";
        String ultNum = "";
        String ultIni = "";
        String ultTer = "";
        String firstNum = "";
        String firstIni = "";
        String firstTer = "";
        Vector inicios = new Vector();
        Vector terminos = new Vector();
        Consulta info = new Consulta(Conexion);
        Validar valida = new Validar();
        modelRoot.put("GetProp", new GetProp(ResourceBundle.getBundle("db")));
        int ini = Integer.parseInt(req.getParameter("inicio"));
        int ter = Integer.parseInt(req.getParameter("termino"));
        int actual = Integer.parseInt(req.getParameter("actual"));
        String sexo1 = req.getParameter("sexo1");
        String sexo2 = req.getParameter("sexo2");
        if(req.getParameter("rango1").length() != 0)
            edad1 = Integer.parseInt(req.getParameter("rango1"));
        if(req.getParameter("rango2").length() != 0)
            edad2 = Integer.parseInt(req.getParameter("rango2"));
        String paterno = valida.validarDato(req.getParameter("apellido1"), "");
        String materno = valida.validarDato(req.getParameter("apellido2"), "");
        String nombre = valida.validarDato(req.getParameter("nombre"), "");
        String cargo = valida.validarDato(req.getParameter("cargo"), "");
        String civil = valida.validarDato(req.getParameter("civil"), "");
        int grupIni = Integer.parseInt(req.getParameter("grupini"));
        int grupTer = Integer.parseInt(req.getParameter("grupter"));
        int veces = 1;
        int existen = 0;
        int division = 0;
        int x = 0;
        int grupos = 1;
        OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("sexo1: ")).append(sexo1).append("\n").append("sexo2: ").append(sexo2).append("\n").append("edad1: ").append(edad1).append("\n").append("edad2: ").append(edad2).append("\n").append("nombre: ").append(nombre).append("\n").append("paterno  : ").append(paterno).append("\n").append("materno: ").append(materno).append("\n").append("cargo: ").append(cargo))));
        sentencia = "SELECT distinct rut, digito_ver, nombre=ltrim(rtrim(nombre)), nombres=ltrim(rtrim(nombres)), cargo=ltrim(rtrim(cargo)), foto, e_mail, telefono, anexo, Xedad, sexo, unidad=ltrim(rtrim(unidad)), ape_paterno=ltrim(rtrim(ape_paterno)), ape_materno=ltrim(rtrim(ape_materno)), estado_civil=ltrim(rtrim(estado_civil)) FROM view_rut_all WHERE (1 = 1) and (foto is not null) ";
        if(!sexo1.equals("0") && !sexo2.equals("0"))
        {
            OutMessage.OutMessagePrint("*****Ambos sexos!!");
        } else
        {
            if(!sexo1.equals("0") && sexo1.equals("M"))
                sentencia = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sentencia)))).append(" and (sexo = '").append(sexo1).append("')")));
            if(!sexo2.equals("0") && sexo2.equals("F"))
                sentencia = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sentencia)))).append(" and (sexo = '").append(sexo2).append("')")));
        }
        modelRoot.put("sexo1", sexo1);
        modelRoot.put("sexo2", sexo2);
        if(edad1 != 0 && edad2 != 0)
        {
            sentencia = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sentencia)))).append(" and (Xedad >= ").append(edad1).append(") and (Xedad <= ").append(edad2).append(")")));
            modelRoot.put("rango1", String.valueOf(edad1));
            modelRoot.put("rango2", String.valueOf(edad2));
        } else
        {
            if(edad1 != 0)
            {
                sentencia = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sentencia)))).append(" and (Xedad = ").append(edad1).append(")")));
                modelRoot.put("rango1", String.valueOf(edad1));
            }
            if(edad2 != 0)
            {
                sentencia = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sentencia)))).append(" and (Xedad = ").append(edad2).append(")")));
                modelRoot.put("rango2", String.valueOf(edad2));
            }
        }
        if(paterno.length() != 0)
        {
            sentencia = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sentencia)))).append(" and (ape_paterno LIKE'%").append(paterno).append("%')")));
            modelRoot.put("apellido1", paterno);
        }
        if(materno.length() != 0)
        {
            sentencia = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sentencia)))).append(" and (ape_materno LIKE'%").append(materno).append("%')")));
            modelRoot.put("apellido2", materno);
        }
        if(nombre.length() != 0)
        {
            sentencia = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sentencia)))).append(" and (nombres LIKE'%").append(nombre).append("%')")));
            modelRoot.put("nombre", nombre);
        }
        if(cargo.length() != 0)
        {
            sentencia = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sentencia)))).append(" and (cargo LIKE'%").append(cargo).append("%')")));
            modelRoot.put("cargo", cargo);
        }
        if(!civil.equals("0"))
        {
            sentencia = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sentencia)))).append(" and (estado_civil LIKE'%").append(civil).append("%')")));
            modelRoot.put("civil", civil);
        }
        modelRoot.put("sexo1", sexo1);
        modelRoot.put("sexo2", sexo2);
        sentencia = String.valueOf(String.valueOf(sentencia)).concat(" order by rut");
        OutMessage.OutMessagePrint("---->consulta; ".concat(String.valueOf(String.valueOf(sentencia))));
        regs = Registros(sentencia, Conexion);
        OutMessage.OutMessagePrint("--->registros rescatados= ".concat(String.valueOf(String.valueOf(regs))));
        info.exec(sentencia);
        if(regs != 0)
        {
            int a = 1;
            int b = 12;
            int Gini = 0;
            int Gter = 0;
            division = regs % 12;
            if(division == 0)
                paginas = regs / 12;
            else
                paginas = regs / 12 + 1;
            OutMessage.OutMessagePrint("*******#paginas= ".concat(String.valueOf(String.valueOf(paginas))));
            start = String.valueOf(a);
            finish = String.valueOf(b);
            for(x = 0; x < paginas; x++)
            {
                inicios.addElement(start);
                terminos.addElement(finish);
                a += 12;
                b += 12;
                start = String.valueOf(a);
                finish = String.valueOf(b);
            }

            OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("Inicio Grupo paginas: ")).append(grupIni).append("\n").append("Termino Grupo paginas: ").append(grupTer))));
            if(paginas > agrupar)
            {
                Gini = grupIni - 1;
                Gter = grupTer;
            } else
            {
                grupos = 0;
                Gini = 0;
                Gter = inicios.size();
            }
            OutMessage.OutMessagePrint("----->Links a mostrar: ".concat(String.valueOf(String.valueOf(Gter))));
            firstIni = (String)inicios.elementAt(Gini);
            firstTer = (String)terminos.elementAt(Gini);
            firstNum = String.valueOf(Gini + 1);
            OutMessage.OutMessagePrint("**************Actual: ".concat(String.valueOf(String.valueOf(actual))));
            for(x = Gini; x < Gter; x++)
            {
                SimpleHash simplehash2 = new SimpleHash();
                simplehash2.put("num", String.valueOf(x + 1));
                if(actual != x + 1)
                {
                    simplehash2.put("sentencia", String.valueOf(String.valueOf((new StringBuilder("<a href=\"#\" onClick=\"Asigna('")).append((String)inicios.elementAt(x)).append("','").append((String)terminos.elementAt(x)).append("','").append(String.valueOf(x + 1)).append("');return false;\">").append("<font face=\"Arial, Helvetica, sans-serif\" size=\"2\">").append(String.valueOf(x + 1)).append("</font></a>&nbsp;"))));
                    ultIni = (String)inicios.elementAt(x);
                    ultTer = (String)terminos.elementAt(x);
                    ultNum = String.valueOf(x + 1);
                    sgteNum = String.valueOf(Integer.parseInt(ultNum) + 1);
                } else
                {
                    simplehash2.put("sentencia", String.valueOf(String.valueOf((new StringBuilder("<font face=\"Arial, Helvetica, sans-serif\" size=\"2\">")).append(String.valueOf(x + 1)).append("</font>&nbsp;"))));
                    sgteNum = String.valueOf(x + 2);
                    OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("-------->Sgte Num: ")).append(sgteNum).append("<--------"))));
                    numS = Integer.parseInt(sgteNum);
                    if(numS <= paginas)
                    {
                        sgteInicio = (String)inicios.elementAt(x + 1);
                        sgteTermino = (String)terminos.elementAt(x + 1);
                        modelRoot.put("num", sgteNum);
                        modelRoot.put("ster", sgteTermino);
                    }
                }
                if(actual == x + 1)
                    simplehash2.put("actual", "1");
                else
                    simplehash2.put("actual", "0");
                simplehash2.put("ini", (String)inicios.elementAt(x));
                simplehash2.put("ter", (String)terminos.elementAt(x));
                links.add(simplehash2);
            }

            if(grupIni + (agrupar - 1) < paginas)
            {
                modelRoot.put("gini", String.valueOf(grupIni));
                modelRoot.put("gter", String.valueOf(grupTer));
                if(grupIni == 1)
                {
                    modelRoot.put("atras", "0");
                    OutMessage.OutMessagePrint("******no hay BACK");
                }
                OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("***Grupo Actual: ")).append(grupIni).append(" - ").append(grupTer))));
            } else
            {
                OutMessage.OutMessagePrint("***No hay mas Grupos");
                if(grupos == 0)
                    modelRoot.put("fin", "0");
                else
                    modelRoot.put("sig", "0");
                modelRoot.put("gini", String.valueOf(grupIni));
                modelRoot.put("gter", String.valueOf(grupTer));
            }
            OutMessage.OutMessagePrint("primera pagina= ".concat(String.valueOf(String.valueOf(firstNum))));
            OutMessage.OutMessagePrint("ultima pagina= ".concat(String.valueOf(String.valueOf(ultNum))));
            modelRoot.put("ultnum", ultNum);
            modelRoot.put("ultini", ultIni);
            modelRoot.put("ultter", ultTer);
            modelRoot.put("firstnum", firstNum);
            modelRoot.put("firstini", firstIni);
            modelRoot.put("firstter", firstTer);
            ter -= 2;
            do
            {
                if(!info.next())
                    break;
                if(veces == ini)
                {
                    existen = 1;
                    SimpleHash simplehash1 = new SimpleHash();
                    simplehash1.put("foto", info.getString("foto"));
                    simplehash1.put("rut", info.getString("rut"));
                    simplehash1.put("nombre", info.getString("nombre").trim());
                    simplelist.add(simplehash1);
                    break;
                }
                veces++;
            } while(true);
            do
            {
                if(!info.next())
                    break;
                existen = 1;
                SimpleHash simplehash1 = new SimpleHash();
                simplehash1.put("foto", info.getString("foto"));
                simplehash1.put("rut", info.getString("rut"));
                simplehash1.put("nombre", info.getString("nombre").trim());
                simplelist.add(simplehash1);
                if(veces > ter)
                    break;
                veces++;
            } while(true);
            OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("inicio: ")).append(ini).append("\n").append("termino: ").append(ter))));
            ter += 3;
            modelRoot.put("detalle", simplelist);
            ter += 11;
            if(paginas > 1)
                modelRoot.put("urls", links);
            OutMessage.OutMessagePrint("Agrupa de: ".concat(String.valueOf(String.valueOf(String.valueOf(agrupar)))));
            modelRoot.put("pag", String.valueOf(actual));
            modelRoot.put("grupo", String.valueOf(agrupar));
            modelRoot.put("paginas", String.valueOf(paginas));
            modelRoot.put("regs", String.valueOf(regs));
            modelRoot.put("GetProp", new GetProp(ResourceBundle.getBundle("db")));
            OutMessage.OutMessagePrint("****FIN******");
        }
        super.retTemplate(resp,"buscar/grafica/galeria.htm",modelRoot);
        info.close();
        OutMessage.OutMessagePrint("\n****FIN de CargaPagina: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }

    private int Registros(String query, Connection Conexion)
    {
        int x = 0;
        Consulta info = new Consulta(Conexion);
        info.exec(query);
        while(info.next()) 
            x++;
        return x;
    }

    private Usuario user;
}