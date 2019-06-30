package portal.com.eje.daemon.vo;

import portal.com.eje.portal.organica.vo.UnidadExtendible;
import portal.com.eje.portal.organica.vo.UnidadGenerica;

public class UnidadDaemon extends UnidadExtendible {

	public UnidadDaemon(String name, boolean sumValuesToParent) {
		super(name, sumValuesToParent);
		// TODO Auto-generated constructor stub
	}

	public UnidadDaemon(String name, String pathImage, boolean sumValuesToParent) {
		super(name, pathImage, sumValuesToParent);
		// TODO Auto-generated constructor stub
	}

	public UnidadDaemon(String name, String pathImage) {
		super(name, pathImage);
		// TODO Auto-generated constructor stub
	}

	public UnidadDaemon(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public UnidadDaemon(UnidadGenerica unidad, boolean sumesValuesToParent) {
		super(unidad, sumesValuesToParent);
		// TODO Auto-generated constructor stub
	}

	
	public Integer getIdDaemon() {
		return getAtributoInteger("id_daemon");
	}
	
	public Integer getIdGrupo() {
		return getAtributoInteger("id_grupo");
	}
	
	public Boolean getActivado() {
		return getAtributoBoolean("activado");
	}
	
	public String getPeridiosidad() {
		return getAtributo("peridiosidad");
	}
	
	public String getEjecuClase() {
		return getAtributo("ejecu_clase");
	}
	
	public String getEjecuLlamadaUrl() {
		return getAtributo("ejecu_llamada_url");
	}
	
	public String getEjecuDos() {
		return getAtributo("ejecu_dos");
	}
}
