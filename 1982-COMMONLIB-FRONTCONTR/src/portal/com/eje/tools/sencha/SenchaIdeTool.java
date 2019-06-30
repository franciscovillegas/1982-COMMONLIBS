package portal.com.eje.tools.sencha;

import java.io.File;

import cl.ejedigital.tool.strings.UrlTool;
import portal.com.eje.applistener.ContextInfo;
import portal.com.eje.portal.factory.Util;

public class SenchaIdeTool {

	public static SenchaIdeTool getInstance() {
		return Util.getInstance(SenchaIdeTool.class);
	}

	public boolean isAccesible(String ide) {
		String rp = ContextInfo.getInstance().getRealPath("/");

		StringBuilder str2 = new StringBuilder();
		str2.append(UrlTool.getInstance().siExisteQuitaUltimoChar('\\', rp));
		str2.append(File.separator).append("view");
		str2.append(File.separator).append("ide");
		str2.append(File.separator).append(ide);
		str2.append(File.separator).append("index.html");

		File file = new File(str2.toString());

		return (file != null && file.exists());

	}
}
