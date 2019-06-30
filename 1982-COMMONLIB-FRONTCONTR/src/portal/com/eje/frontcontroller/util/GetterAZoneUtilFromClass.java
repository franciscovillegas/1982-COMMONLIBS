package portal.com.eje.frontcontroller.util;

import cl.eje.helper.AZoneUtil;
import cl.eje.helper.servletmapping.TriadaThingVo;
import cl.eje.view.sencha.Version;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.iface.IServicioGetter;
import portal.com.eje.portal.factory.Weak;
import portal.com.eje.tools.EnumTool;

public class GetterAZoneUtilFromClass implements IServicioGetter {
	private Class<? extends AZoneUtil> clase;
	private final String GUION = "_";
	private final String PUNTO = ".";

	public GetterAZoneUtilFromClass(Class<? extends AZoneUtil> clase) {
		super();
		this.clase = clase;
	}
	
	@SuppressWarnings("unchecked")
	public GetterAZoneUtilFromClass(String clase) {
		super();
		try {
			this.clase = (Class<? extends AZoneUtil>) Class.forName(clase);
		} catch (ClassNotFoundException e) {
 
		}
	}
	
	public GetterAZoneUtilFromClass(TriadaThingVo triada) {
		if(triada != null) {
			String modulo = triada.getModulo();
			String thing = triada.getThing();
			if(modulo != null) {
				String version = modulo.split(GUION)[0];
				
				Version v = EnumTool.getEnumByNameIngoreCase(Version.class, version, Version.PORDEFECTO);
				
				String clase = (new StringBuilder().append(v.getPaquete()).append(PUNTO).append(modulo).append(PUNTO).append(thing).toString());
				try {
					this.clase = (Class<? extends AZoneUtil>) Class.forName(clase);
				} catch (ClassNotFoundException e) {
		 
				}
			}
		
		}
		
	}

	@Override
	public Object getServicio(IOClaseWeb io) throws Exception {
		Object o = null;
		if(clase != null) {
			o = Weak.getInstance(clase);
		}
		return o;
	}

}
