package portal.com.eje.portaldepersonas.gestordecorreos;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.validar.Validar;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.pdf.HttpFlow;
import portal.com.eje.portal.tool.FreemakerToolAdvance;
import portal.com.eje.tools.security.Encrypter;

public class GestorCorreoVista {
	private String pathTemplate = "view/ide/ejeb_portaldepersonas_gestorcorreos/resources/";

	public static GestorCorreoVista getInstance() {
		return Util.getInstance(GestorCorreoVista.class);
	}

	public HttpFlow getCorreo(HttpServletRequest req, int rut, String idCorreoEncrypted) throws SQLException, IOException, ServletException {
		Encrypter enc = new Encrypter();
		int idCorreo = Validar.getInstance().validarInt(enc.decrypt(idCorreoEncrypted), -1);

		ConsultaData data = GestorCorreoManager.getInstance().getCorreo(idCorreo);
		ConsultaData dataAdjuntos = GestorCorreoManager.getInstance().getArchivosArjuntos(idCorreo);

		String flow = null;
		if (data != null && data.next()) {
			GestorCorreoManager.getInstance().markReaded(rut, idCorreo);

			FreemakerToolAdvance tool = new FreemakerToolAdvance();
			SimpleHash modelRoot = tool.getData(data);
			SimpleList adjuntos = tool.getListData(dataAdjuntos);
			modelRoot.put("adjuntos", adjuntos);

			flow = tool.templateProcessFromBase(req, pathTemplate + "correo.html", modelRoot);
		}

		return new HttpFlow(flow);

	}

	public HttpFlow getEmptyBandeja(HttpServletRequest req) throws IOException, ServletException, SQLException {
		FreemakerToolAdvance tool = new FreemakerToolAdvance();
		String flow = tool.templateProcessFromBase(req, pathTemplate + "tableContainerMails.html", null);

		return new HttpFlow(flow);
	}

}
