package portal.com.eje.daemon;

import java.util.List;

import cl.ejedigital.tool.validar.Validar;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.PPMException;
import portal.com.eje.portal.parametro.ParametroLocator;
import portal.com.eje.portal.parametro.ParametroValue;
import portal.com.eje.tools.EnumTool;

public class PPMDaemonDefinition {
	private String paramNemo = this.getClass().getCanonicalName();
	
	public enum PPMDaemonDefinitionParam {
		every_seconds("60"),
		logAll("false"),
		printByConsole("false");
		
		private String defaultValue;
		
		PPMDaemonDefinitionParam(String defaultValue) {
			this.defaultValue = defaultValue;
		}
		
		public String getDefaultValue() {
			return defaultValue;
		}
	}
	
	public PPMDaemonDefinition() {
		
		try {
			
			List<PPMDaemonDefinitionParam> values = EnumTool.getArrayList(PPMDaemonDefinitionParam.class);
			for(PPMDaemonDefinitionParam v : values) {
				ParametroLocator.getInstance().ifNotExistThenBuildParametro(EModulos.getThisModulo(), paramNemo, v.toString(), v.getDefaultValue());	
			}
			
		} catch (PPMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public int getParamNum(PPMDaemonDefinitionParam key) {
		ParametroValue pv = ParametroLocator.getInstance().getValor(paramNemo, key.toString());
		
		if(pv != null && pv.getValue() != null) {
			return Validar.getInstance().validarInt( pv.getValue(), 0);
		}
		
		return Validar.getInstance().validarInt( key.getDefaultValue(), 0);
	}
	
	public boolean getParamBoolean(PPMDaemonDefinitionParam key) {
		ParametroValue pv = ParametroLocator.getInstance().getValor(paramNemo, key.toString());
		
		if(pv != null && pv.getValue() != null) {
			return Validar.getInstance().validarBoolean( pv.getValue(), false);
		}
		
		return false;
	}
}
