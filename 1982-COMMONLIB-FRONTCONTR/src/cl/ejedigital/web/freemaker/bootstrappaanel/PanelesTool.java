package cl.ejedigital.web.freemaker.bootstrappaanel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import cl.eje.bootstrap.ifaces.IContainerResource;
import cl.eje.bootstrap.ifaces.IPanelResource;
import cl.eje.bootstrap.ifacetemplatesetter.IPanel;
import cl.ejedigital.web.FreemakerTool;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.ioutils.IOUtilFreemarker;
import portal.com.eje.portal.factory.Weak;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;

public class PanelesTool {
	private final String KEY_HASH_PANEL ="panel";
	
	public static PanelesTool getInstance() {
		return Weak.getInstance(PanelesTool.class);
	}

	public SimpleList getPanelsSimpleList(IOClaseWeb io, IContainerResource res, List<IPanel> paneles, IPanelResource formatoPanel) throws IOException, ServletException, SQLException {
		SimpleList listPaneles = new SimpleList();
		for (IPanel panel : paneles) {
			SimpleHash hash = IOUtilFreemarker.getInstance().getGenericModelRoot(io, new SimpleHash());
			FreemakerTool.getInstance().setDataFromVo(hash, panel);

			String html = FreemakerTool.getInstance().templateProcessFromPath(io.getReq(), formatoPanel.getPath(), hash, formatoPanel.isFromTemplatePath());

			hash.put(KEY_HASH_PANEL, html);
			listPaneles.add(hash);
		}

		return listPaneles;
	}
 

}
