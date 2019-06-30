package portal.com.eje.portal.organica.ifaces;

import portal.com.eje.serhumano.user.Usuario;

public interface IOrganicaManipulator {

	public boolean add(Usuario u, String unid_id, String unid_id_parent, String unid_desc) throws Exception;

	public boolean add(Usuario u, String unid_id_parent, String unid_desc) throws Exception;

	public boolean setVigente(Usuario u, String unid_id, boolean vigente) throws Exception;

	public boolean updNombre(final Usuario u, String unid_id, String unid_desc) throws Exception;

	public boolean updPadre(Usuario u, String unid_id, String unid_id_parent) throws Exception;

}
