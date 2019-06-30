package organica.arbol;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import cl.ejedigital.web.datos.DBConnectionManager;
import cl.ejedigital.web.datos.IDBConnectionManager;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import freemarker.template.SimpleScalar;
import freemarker.template.Template;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import organica.com.eje.ges.usuario.Usuario;
import organica.com.eje.ges.usuario.unidad.VerUnidad;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.servlet.GetParam;

// Referenced classes of package arbol:
//            Organica, Nodo

public class MiArbolTM
    implements TemplateMethodModel
{

    public MiArbolTM(Template template)
    {
        nodoFinRama = null;
        this.template = template;
        user = null;
        req = null;
    }

    public MiArbolTM(Template template, Usuario u)
    {
        nodoFinRama = null;
        this.template = template;
        user = u;
        req = null;
    }

    public MiArbolTM(Template template, Usuario u, HttpServletRequest req)
    {
        nodoFinRama = null;
        this.template = template;
        user = u;
        this.req = req;
    }

    public MiArbolTM(Template template, Usuario u, HttpServletRequest req, String empresa)
    {
        nodoFinRama = null;
        this.template = template;
        user = u;
        this.req = req;
        this.emp = empresa;
    }
    
    public boolean isEmpty()
    {
        return false;
    }

    public TemplateModel exec(List args)
    {
        ByteArrayOutputStream uddata = new ByteArrayOutputStream(40000);
        SimpleHash modelRoot = new SimpleHash();
        PrintWriter out = new PrintWriter(uddata);
        String IdNodo = null;
        String IdEmpresa = null;
        Nodo RT = null;
        if(Usuario.SuperNodo == null)
        {
            OutMessage.OutMessagePrint("Recargar Arbol...");
            Organica.main(new String[1]);
        }
        RT = Usuario.SuperNodo;
        try
        {
            if(args.size() > 0)
                if(user != null && user.veMultiplesRamas())
                    RT = getEstructuraRestringida();
                else
                if(args.size() == 1)
                {
                    IdEmpresa = (String)args.get(0);
//                  RT = Usuario.SuperNodo.BuscarEmpresa(IdEmpresa);
                    RT = Usuario.SuperNodo.BuscarEmpresa(emp);
                } else
                if(args.size() == 2)
                {
                    IdNodo = (String)args.get(0);
                    IdEmpresa = (String)args.get(1);
//                  RT = Usuario.SuperNodo.BuscarNodo(IdNodo, IdEmpresa);
                    RT = Usuario.SuperNodo.BuscarNodo(IdNodo, emp);
                }
        }
        catch(IndexOutOfBoundsException e)
        {
            OutMessage.OutMessagePrint("Exepcion : ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        if(RT != null)
            modelRoot.put("sentencia", RT.getSimpleHasch());
        if(req != null)
            modelRoot.put("Param", new GetParam(req));
        if(nodoFinRama != null)
        {
            String paramBusEmp = nodoFinRama.getEmpresa();
            String paramBusUni = nodoFinRama.getIdNodo();
            Vector vecRama = RT.getRama(paramBusUni, paramBusEmp);
            if(vecRama != null)
            {
                SimpleList rama = new SimpleList();
                Nodo nodoPaso;
                for(int x = 0; x < vecRama.size() - 1; x++)
                {
                    SimpleHash hash = new SimpleHash();
                    nodoPaso = (Nodo)vecRama.get(x);
                    hash.put("emp", nodoPaso.getEmpresa());
                    hash.put("uni", nodoPaso.getIdNodo());
                    rama.add(hash);
                }

                modelRoot.put("rama", rama);
                nodoPaso = (Nodo)vecRama.get(vecRama.size() - 1);
                modelRoot.put("resul", true);
                System.err.println(String.valueOf(String.valueOf((new StringBuilder("----->Mi Arbol(Empresa): ")).append(nodoPaso.getEmpresa()).append("  ----->Mi Arbol(Unidad): ").append(nodoPaso.getIdNodo()))));
                modelRoot.put("resulEmp", nodoPaso.getEmpresa());
                modelRoot.put("resulUni", nodoPaso.getIdNodo());
            }
            System.err.println(String.valueOf(String.valueOf((new StringBuilder("----->Mi Arbol(Empresa): ")).append(paramBusEmp).append("  ----->Mi Arbol(Unidad): ").append(paramBusUni))));
            modelRoot.put("be", paramBusEmp);
            modelRoot.put("bu", paramBusUni);
        }
        try {
			template.process(modelRoot, out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        out.close();
        return new SimpleScalar(uddata.toString());
    }

    public void setRamaNodo(Nodo n)
    {
        nodoFinRama = n;
    }

    private Nodo getEstructuraRestringida()
    {
        OutMessage.OutMessagePrint("\n--> Estructura Restringida ");
        Nodo miNodo = Nodo.getNodoRaiz("Mi Org\341nica");
        Vector vecRamas = new Vector();
        VerUnidad vu[] = user.getUnidadesVer();

		InputStream is = getClass().getResourceAsStream("/DataFolder.properties");
		Properties dfProps = new Properties();
		try {
			dfProps.load(is);
		} catch (Exception e) {
			System.err
					.println("Can't read the properties file. Make sure jndi.properties is in the CLASSPATH");
		}
		
        String unidad_pertenencia = dfProps.getProperty("portal.unidad_pertenencia");
        
        int largo = vu == null ? 0 : vu.length;
        for(int x = 0; x < largo; x++)
        {
            VerUnidad vuPaso = vu[x];
            OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(vuPaso.toString()))));
            String unid_gestion = "";
            Nodo nodoPaso;
            if (unidad_pertenencia.equals("0"))
                unid_gestion = vuPaso.getUnidad();
            else
            	unid_gestion = getUnidadGestion(vuPaso.getUnidad(),vuPaso.getEmpresa());

            nodoPaso = Usuario.SuperNodo.BuscarNodo(unid_gestion, vuPaso.getEmpresa());
            	
            if(nodoPaso != null)
            {
               if(vuPaso.getQueVer().equals("U"))
               		vecRamas.addElement(nodoPaso.getNodoSinHijos());
               	else
               		vecRamas.addElement(nodoPaso);
            } else
            {
                OutMessage.OutMessagePrint("  --> No existe en arbol.");
            }
        }

        miNodo.Agrega_Hijo(vecRamas);
        return miNodo;
    }


    //Solucion Parche
    private String getUnidadGestion(String id,String empresa) {
    	portal.com.eje.serhumano.user.Usuario userPortal = portal.com.eje.serhumano.user.SessionMgr.rescatarUsuario(req); 
    	
        connMgr = DBConnectionManager.getInstance();
        Connection Conexion = connMgr.getConnection("portal");
        Consulta consul = new Consulta(Conexion);
    	String sql = "SELECT unidad FROM eje_ges_unidad_rama WHERE uni_rama = '" + id + "' AND empresa = '" + empresa + "'";
    	consul.exec(sql);
    	consul.next();
        connMgr.freeConnection("portal", Conexion);
        connMgr.release();
        return consul.getString("unidad");
    }
    
    private static IDBConnectionManager connMgr;
    private Template template;
    private Usuario user;
    private String emp;
    private HttpServletRequest req;
    private Nodo nodoFinRama;
}