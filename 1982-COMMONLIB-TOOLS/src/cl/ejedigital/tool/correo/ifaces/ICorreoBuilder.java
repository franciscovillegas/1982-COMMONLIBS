package cl.ejedigital.tool.correo.ifaces;

import java.io.File;
import java.util.List;

import cl.ejedigital.tool.correo.vo.IVoDestinatario;


public interface ICorreoBuilder {
	/**
	 * Debe retornar una lista con los emails de los destinos
	 * donde cada mail es del tipo juan@miempresa.cl
	 * @return List&lt;IVoDestinatario&gt;
	 * @see IVoDestinatario
	 * */
	public abstract List<IVoDestinatario> getDestinatarios();

	/**
	 * Debe retornar el asunto del mail
	 * @return String
	 * */
	public abstract String getAsunto();
	
	/**
	 * Debe retornar una lista llena de File 
	 * @return List&lt;File&gt;
	 * @see File
	 * */
	public abstract List<File> getArchivosAdjuntos();
	
	/**
	 * Retorna el texto que irá en el html
	 * @return IVoCorreo
	 * @see IVoCorreo
	 * */
	public abstract String getBody();	
}
