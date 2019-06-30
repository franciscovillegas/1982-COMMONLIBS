package portal.com.eje.genericbuttons.beans.sql;

import org.springframework.stereotype.Component;

import portal.com.eje.genericbuttons.enums.EnumGenericButtonGroup;
import portal.com.eje.genericbuttons.enums.EnumGenericButtonSubGroup;
import portal.com.eje.genericconf.AbsSenchaOpener;
import portal.com.eje.portal.app.enums.EnumTypeAppIde;
import portal.com.eje.portal.generico_appid.EnumAppId;

@Component
public class EjeGenericoEjecutorSqls extends AbsSenchaOpener {

	public EjeGenericoEjecutorSqls() {
		setType(EnumTypeAppIde.SENCHA);
		setNombre("Sql Genéricos");
		setTooltip("Puedes ejecutar algunos Scripts T-Sql para llenar o modificar los valores existentes en la Base de datos ");
		setGroup(EnumGenericButtonGroup.BASEDEDATOS);
		setSubGroup(EnumGenericButtonSubGroup.GRUPO2);
		setIde("ejeb_generico_ejecutorsqls");
		setAppIdRequerido(EnumAppId.ADM);
		setIcon("sql.ico");
		
	}
 
}
