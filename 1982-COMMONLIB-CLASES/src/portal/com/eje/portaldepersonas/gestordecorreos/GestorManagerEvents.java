package portal.com.eje.portaldepersonas.gestordecorreos;

import java.sql.SQLException;
import java.util.List;

import cl.ejedigital.tool.validar.Validar;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portaldepersonas.gestordecorreos.enums.EnumGestorCorreosBandejas;
import portal.com.eje.tools.security.Encrypter;

public class GestorManagerEvents {

	public static GestorManagerEvents getInstance() {
		return Util.getInstance(GestorManagerEvents.class);
	}
	
	public boolean markUnMarkFavorito(String id_correoEncrypted) throws SQLException {
		
		Encrypter enc = new Encrypter();
		int idCorreo = Validar.getInstance().validarInt(enc.decrypt(id_correoEncrypted),-1);
		return GestorCorreoManager.getInstance().markUnMarkFavorito(idCorreo);
		
		 
	}

	public boolean markPapelera(String id_correoEncrypted) throws SQLException {
		Encrypter enc = new Encrypter();
		int idCorreo = Validar.getInstance().validarInt(enc.decrypt(id_correoEncrypted),-1);
		return GestorCorreoManager.getInstance().markPapelera(idCorreo);
	}

	public boolean moveTo(List<String> lista, EnumGestorCorreosBandejas bandeja) throws SQLException {
		
		return GestorCorreoManager.getInstance().setBandeja(lista,bandeja);
	}

	public boolean markReaded(List<String> lista, boolean readed) throws SQLException {
		return GestorCorreoManager.getInstance().markReaded(lista,readed);
	}
}
