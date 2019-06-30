package portal.com.eje.serhumano.admin.tools;

import java.util.Vector;

import portal.com.eje.tools.Tools;

public class managerRut
{

    public managerRut(String rutComp)
    {
        this.rutComp = null;
        rut = null;
        dig = null;
        this.rutComp = rutComp;
        Vector vecRut = Tools.separaLista(this.rutComp, "-");
        int largoVec = vecRut.size();
        rut = largoVec <= 0 ? "" : (String)vecRut.get(0);
        dig = largoVec <= 1 ? "" : (String)vecRut.get(1);
    }

    public String getRutCompleto()
    {
        return rutComp;
    }

    public String getRut()
    {
        return rut;
    }

    public String getDigVer()
    {
        return dig;
    }

    private String rutComp;
    private String rut;
    private String dig;
}