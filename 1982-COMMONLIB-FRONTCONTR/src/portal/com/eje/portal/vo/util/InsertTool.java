package portal.com.eje.portal.vo.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.vo.BatchSql;
import portal.com.eje.portal.vo.VoTool;
import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

public class InsertTool {

	public enum EnumInsertMethod {
		IDENTITY,
		NORMAL,
		DEFAULT
	}
	
	public static EnumInsertMethod getInsertMethod(Vo vo) throws SQLException {
		if(vo == null) {
			throw new NullPointerException();
		}
		EnumInsertMethod retorno = null;
		
		TableDefinition tableDef = VoTool.getInstance().getTableDefinition(vo.getClass());
		if(tableDef.pks().length == 1 && tableDef.pks()[0].autoIncremental()) {
			retorno = EnumInsertMethod.IDENTITY;
		}
		else if(tableDef.pks().length == 1 && !tableDef.pks()[0].autoIncremental() && !tableDef.pks()[0].isForeignKey() && tableDef.pks()[0].numerica() ) {
			retorno = EnumInsertMethod.DEFAULT;
		}
		else if(tableDef.pks().length == 1 && !tableDef.pks()[0].autoIncremental() && tableDef.pks()[0].isForeignKey() ) {
			retorno = EnumInsertMethod.NORMAL;
		}
		else if(tableDef.pks().length == 0 ) { 
			retorno = EnumInsertMethod.NORMAL;
		}
		else if(tableDef.pks().length > 0 ) {
			boolean tieneAutoincremental = false;
			
			for(PrimaryKeyDefinition pk : tableDef.pks()) {
				tieneAutoincremental &= !pk.autoIncremental();
			}
			
			if(!tieneAutoincremental && tableDef.pks().length > 0) {
				retorno = EnumInsertMethod.NORMAL;
			}
			else {
				throw new SQLException("Las pks no están bien definidas");
			}
			
		}
		else {
			throw new SQLException("Las pks no están bien definidas");
		}
		
		return retorno;
	}
	
}
