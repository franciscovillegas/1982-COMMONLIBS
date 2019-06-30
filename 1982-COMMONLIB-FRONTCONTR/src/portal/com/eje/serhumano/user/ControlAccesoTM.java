package portal.com.eje.serhumano.user;

import java.util.List;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;

// Referenced classes of package portal.com.eje.serhumano.user:
//            ControlAcceso

public class ControlAccesoTM
    implements TemplateMethodModel
{

    public ControlAccesoTM(ControlAcceso control)
    {
        this.control = control;
    }

    public boolean isEmpty()
    {
        return false;
    }

    public TemplateModel exec(List args)
    {
        int cant_arg = args.size();
        boolean puede = false;
        switch(cant_arg)
        {
        case 1: // '\001'
            puede = control.tienePermiso((String)args.get(0));
            break;

        case 2: // '\002'
            puede = control.tienePermiso((String)args.get(0), (String)args.get(1));
            break;
        }
        return new SimpleScalar(String.valueOf(puede));
    }

    private ControlAcceso control;
}