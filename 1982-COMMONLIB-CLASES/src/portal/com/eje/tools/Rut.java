package portal.com.eje.tools;

import java.util.Vector;

// Referenced classes of package portal.com.eje.tools:
//            Tools

public class Rut
{

    public Rut(String rutComp)
    {
        this.rutComp = null;
        rut = null;
        dig = null;
        this.rutComp = rutComp;
        Vector vecRut = Tools.separaLista(this.rutComp, "-");
        int largoVec = vecRut.size();
        rut = largoVec > 0 ? (String)vecRut.get(0) : "";
        dig = largoVec > 1 ? (String)vecRut.get(1) : "";
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