package organica.tools.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;

public class GetParam
    implements TemplateMethodModel
{

    public GetParam(HttpServletRequest req)
    {
        request = req;
    }

    public boolean isEmpty()
    {
        return false;
    }

    public TemplateModel exec(List args)
    {
        return new SimpleScalar(request.getParameter(args.get(0).toString()));
    }

    private HttpServletRequest request;
}