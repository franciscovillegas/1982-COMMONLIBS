package portal.com.eje.frontcontroller.ioutils;

import java.io.Closeable;
import java.io.Flushable;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import cl.ejedigital.tool.properties.PropertiesTools;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import portal.com.eje.frontcontroller.IIOClaseWebLight;
import portal.com.eje.frontcontroller.resobjects.EnumTypeCode;
import portal.com.eje.frontcontroller.resobjects.ResourceHtml;
import portal.com.eje.frontcontroller.resobjects.ResourceTool;
import portal.com.eje.portal.factory.SingleFactory;
import portal.com.eje.portal.factory.SingleFactoryType;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.pdf.HttpFlow;
import portal.com.eje.serhumano.httpservlet.MyTemplateUbication;
import portal.com.eje.serhumano.user.ControlAcceso;
import portal.com.eje.serhumano.user.ControlAccesoTM;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.Tools;
import portal.com.eje.tools.Cache.CachePortalTypes;
import portal.com.eje.tools.freemakerfunction.ContainStr;
import portal.com.eje.tools.servlet.DecimalNumber;
import portal.com.eje.tools.servlet.GetHtml;
import portal.com.eje.tools.servlet.GetHtmlIde;
import portal.com.eje.tools.servlet.GetHtmlLikeVariable;
import portal.com.eje.tools.servlet.GetImagenEmpresa;
import portal.com.eje.tools.servlet.GetParam;
import portal.com.eje.tools.servlet.GetParametro;
import portal.com.eje.tools.servlet.GetPerfilacion;
import portal.com.eje.tools.servlet.GetProp;
import portal.com.eje.tools.servlet.ModuloDefinition;
import portal.com.eje.tools.servlet.ToCamelCase;
import portal.com.eje.tools.servlet.ToLowerCase;
import portal.com.eje.tools.servlet.ToUpperCase;

public class IOUtilFreemarker extends IOUtil {

	public static IOUtilFreemarker getInstance() {
		return Util.getInstance(IOUtilFreemarker.class);
	}
	
	public IOUtilFreemarker() {
		
	}
	
	public String getTemplateProcces(IIOClaseWebLight io, String templatePath, SimpleHash modelRoot) {
		return getTemplateProcces(io, templatePath, modelRoot, true);
	}
	
	public String getTemplateProcces(IIOClaseWebLight io, String templatePath, SimpleHash modelRoot, boolean startFromTemplatePath) {
		String stringProccesed = null;

		try {

			Closeable closeable = null;
			Flushable flusheable = null;
			Map<String, Object> mapFile = null;

			try {
				if(templatePath == null) {
					io.getResp().sendError(HttpServletResponse.SC_NOT_FOUND);
					return null;
				}
				
				modelRoot = getGenericModelRoot(io, modelRoot);
				mapFile = getObjectTemplate(io, templatePath, startFromTemplatePath);
				
				if (mapFile == null) {
					io.getResp().sendError(HttpServletResponse.SC_NOT_FOUND);
				} else {
					Template template = (Template) mapFile.get("Template");

					if(template == null) {
						io.getResp().sendError(HttpServletResponse.SC_NOT_FOUND);
						return null;
					}
					StringWriter stringWriterToReturn = new StringWriter();
					PrintWriter print = new PrintWriter(stringWriterToReturn);
					template.process(modelRoot, print);

					if (!ResourceTool.getInstance().isCacheActive(CachePortalTypes.CACHETEMPLATE)) {
						// Francisco: 01-08-2017 eliminado el cache por el lado del cliente
						// resp.setHeader("Expires", "0");
						// resp.setHeader("Pragma", "no-cache");
						// resp.setHeader("Cache-Control", "no-cache");
					} else {
						// ResourceTool.getInstance().setCacheHeader(cweb.getResp(),
						// CachePortalTypes.CACHETEMPLATE);
					}

					stringProccesed = ResourceTool.getInstance().getHeader(mapFile, EnumTypeCode.html);
					stringProccesed += "\n" + stringWriterToReturn.toString();
				}

			} catch (IllegalStateException e) {

				stringProccesed = ResourceTool.getInstance().getHeader(mapFile, EnumTypeCode.html);
			} catch (Exception e) {
				// System.err.println("@@@@ MyHttpServlet " +e.toString());
				e.printStackTrace();
				io.getServletContext().log(" Error Metodo retTemplate en : ".concat(String.valueOf(String.valueOf(getClass().getName()))), e);

			} finally {
				if (flusheable != null) {
					flusheable.flush();
				}

				if (closeable != null) {
					closeable.close();
				}
			}

		} catch (Exception e) {
			io.getServletContext().log(" Error Metodo retTemplate en : ".concat(String.valueOf(String.valueOf(getClass().getName()))), e);
		}

		return stringProccesed;
	}

	public HttpFlow getTemplateFlow(IIOClaseWebLight  io,String templatePath, SimpleHash modelRoot) {
		return new HttpFlow(getTemplateProcces(io, templatePath, modelRoot));
	}
	
    public Map<String,Object> getObjectTemplate(IIOClaseWebLight  io, String templateName, boolean startFromTemplatePath)  throws Exception {
    	Map<String,Object> map = null;
    	
    	 
    		ResourceHtml hml = SingleFactory.getFactory(SingleFactoryType.UTIL).getInstance(ResourceHtml.class);
    		map=hml.getObjectTemplate(MyTemplateUbication.SrcAndWebContent, io.getReq(), templateName, startFromTemplatePath);
		 
    	
    	return map;
    }
    
	public SimpleHash getGenericModelRoot(IIOClaseWebLight io, SimpleHash modelRoot) {
		if (io != null && io.getReq() != null) {

			Usuario user = SessionMgr.rescatarUsuario(io.getReq());

			if (user != null && user.esValido()) {
				modelRoot.put("Control", new ControlAccesoTM(new ControlAcceso(user)));
				modelRoot.put("usuario", user.toHash());
				//Pancho: 30-04-2019
				//No puede ser rut, el valor hash que se ingresa, Hay muchos rut ingresados en los replates, es mejor usar usuario.pRut
				//modelRoot.put("rut", user.getRutId());
				modelRoot.put("GetImagenEmpresa", new GetImagenEmpresa(user));
				modelRoot.put("correo", user.getEmailFromTablaConfirmacion());
				
			}

			modelRoot.put("fullcontext", io.getUtil(IOUtilDomain.class).getUrlContext(io));
			modelRoot.put("contexto_cliente", io.getUtil(IOUtilDomain.class).getContextCliente());
			modelRoot.put("contexto_modulo", io.getUtil(IOUtilDomain.class).getContextModulo());
			modelRoot.put("pmontaje", io.getUtil(IOUtilDomain.class).getContext());

			modelRoot.put("context", io.getReq().getContextPath());
			modelRoot.put("GetParam", new GetParam(io));

			modelRoot.put("GetHtml", new GetHtml(modelRoot, io.getReq())); // from path /templates/
			modelRoot.put("GetHtmlIde", new GetHtmlIde(modelRoot, io.getReq())); //from path folder /view/ide/*
			modelRoot.put("GetHtmlLikeVariable", new GetHtmlLikeVariable(modelRoot, io.getReq())); //from path folder /view/ide/*
			modelRoot.put("GetPerfilacion", new GetPerfilacion(io));

			
			
		}

		modelRoot.put("GetProp", new GetProp(ResourceBundle.getBundle("db")));
		modelRoot.put("GetParametro", GetParametro.getInstance());
		modelRoot.put("ToUpperCase", ToUpperCase.getInstance());
		modelRoot.put("ToCamelCase", ToCamelCase.getInstance());
		modelRoot.put("ToLowerCase", ToLowerCase.getInstance());
		modelRoot.put("DecimalNumber", DecimalNumber.getInstance());
		modelRoot.put("ModuloDefinition", ModuloDefinition.getInstance());
		modelRoot.put("ContainStr", ContainStr.getInstance());
		
 
		modelRoot.put("random", String.valueOf((int) (Math.random() * 1000000000)));

		Calendar cal = Calendar.getInstance();
		int yy = cal.get(Calendar.YEAR);
		int mm = cal.get(Calendar.MONTH) + 1;
		int dd = cal.get(Calendar.DAY_OF_MONTH);
		modelRoot.put("fechaActual", new StringBuilder().append(dd).append(" de ").append(Tools.RescataMes(mm)).append(" del ").append(yy).toString());

		if (PropertiesTools.getInstance().existsBundle("analytics")) {
			modelRoot.put("googleAnalytics", PropertiesTools.getInstance().getString(ResourceBundle.getBundle("analytics"), "google.key", ""));
		}

		return modelRoot;
	}
}
