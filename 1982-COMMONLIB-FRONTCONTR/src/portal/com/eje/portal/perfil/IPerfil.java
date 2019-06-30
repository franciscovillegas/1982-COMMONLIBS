package portal.com.eje.portal.perfil;

import cl.ejedigital.consultor.ConsultaData;

public interface IPerfil {

	/**
	 * Entregará las opciones de:<br/>
	 * <ul>
	 * 	<li>Visualizar zonas en MiEquipo</li>
	 *  <li>Vizualizar zonas en Estructura</li>
	 * </ul>
	 * */
	public ConsultaData getAtribuciones(int rut);
	
	/**
	 * Entrega las imágenes que deben ser mostradas en la zona de descanso del webmatico.
	 * */
	public ConsultaData getWebmaticoAtribuciones(int rut);
	
	public ConsultaData getPerfiles(int rut);
	
	public boolean isVerTodo(int rut);
	
}
