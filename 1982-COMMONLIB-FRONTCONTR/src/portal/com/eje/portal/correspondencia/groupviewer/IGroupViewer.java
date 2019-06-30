package portal.com.eje.portal.correspondencia.groupviewer;

import java.util.Map;

import javax.mail.Message.RecipientType;

import cl.ejedigital.tool.correo.vo.IVoDestinatario;

public interface IGroupViewer {
	
	
	/**
	 * Retornar� las personas pertenecientes a un grupo, a la fecha actual
	 * 
	 * */
	public void addDestinatariosFromGrupo(Map<String, IVoDestinatario>  finalLista, Double idGrupo, RecipientType tipo);
	
	/**
	 * Retornar� las personas pertenecientes a un grupo, a la fecha indicada por el idTimer
	 * 
	 * */
	public void addDestinatariosFromGrupo(Map<String, IVoDestinatario>  finalLista, Double idGrupo, Double idTimer, RecipientType tipo);
}
