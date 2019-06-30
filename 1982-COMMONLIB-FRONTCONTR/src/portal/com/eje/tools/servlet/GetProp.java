package portal.com.eje.tools.servlet;

import java.util.List;
import java.util.ResourceBundle;

import portal.com.eje.tools.OutMessage;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;

public class GetProp
    implements TemplateMethodModel
{

    public GetProp(ResourceBundle prop)
    {
        this.prop = prop;
    }

    public boolean isEmpty()
    {
        return false;
    }

    public TemplateModel exec(List args)
    {
        SimpleScalar ret = null;
        try
        {
            ret = new SimpleScalar(prop.getString(args.get(0).toString()));
        }
        catch(Exception e)
        {
            OutMessage.OutMessagePrint("GetProp --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return ret;
    }

    private ResourceBundle prop;
}