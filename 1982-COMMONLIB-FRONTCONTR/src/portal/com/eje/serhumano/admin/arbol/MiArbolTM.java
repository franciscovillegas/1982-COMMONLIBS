package portal.com.eje.serhumano.admin.arbol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.serhumano.user.UsuarioSuperNodo;
import portal.com.eje.serhumano.user.unidad.VerUnidad;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.servlet.GetParam;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import freemarker.template.SimpleScalar;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;

// Referenced classes of package portal.com.eje.serhumano.admin.arbol:
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
        if(UsuarioSuperNodo.getSuperNodo() == null)
        {
            OutMessage.OutMessagePrint("Recargar Arbol...");
            Organica.main(new String[1]);
        }
        RT = UsuarioSuperNodo.getSuperNodo();
        try
        {
            if(args.size() > 0)
                if(user != null && user.veMultiplesRamas())
                    RT = getEstructuraRestringida();
                else
                if(args.size() == 1)
                {
                    IdEmpresa = (String)args.get(0);
                    RT = UsuarioSuperNodo.getSuperNodo().BuscarEmpresa(IdEmpresa);
                } else
                if(args.size() == 2)
                {
                    IdNodo = (String)args.get(0);
                    IdEmpresa = (String)args.get(1);
                    RT = UsuarioSuperNodo.getSuperNodo().BuscarNodo(IdNodo, IdEmpresa);
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
        int largo = vu != null ? vu.length : 0;
        for(int x = 0; x < largo; x++)
        {
            VerUnidad vuPaso = vu[x];
            OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(vuPaso.toString()))));
            Nodo nodoPaso = UsuarioSuperNodo.getSuperNodo().BuscarNodo(vuPaso.getUnidad(), vuPaso.getEmpresa());
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

    private Template template;
    private Usuario user;
    private HttpServletRequest req;
    private Nodo nodoFinRama;
}