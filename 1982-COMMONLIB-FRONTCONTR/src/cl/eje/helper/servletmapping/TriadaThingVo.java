package cl.eje.helper.servletmapping;

import cl.eje.helper.EnumAccion;
import cl.eje.view.sencha.Version;

public class TriadaThingVo {
	private Version version;
	private String modulo;
	private String thing;
	private EnumAccion enumaccion;

	

	public Version getVersion() {
		return version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}

	public String getModulo() {
		return modulo;
	}

	public void setModulo(String modulo) {
		this.modulo = modulo;
	}

	public String getThing() {
		return thing;
	}

	public void setThing(String thing) {
		this.thing = thing;
	}

	public EnumAccion getEnumaccion() {
		return enumaccion;
	}

	public void setEnumaccion(EnumAccion enumaccion) {
		this.enumaccion = enumaccion;
	}

}
