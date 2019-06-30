package portal.com.eje.portal.cargo;

import java.sql.SQLException;
import java.util.ArrayList;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;

public class Cargo implements ICargo {

	@Override
	public ConsultaData getCargo() {
		// TODO Auto-generated method stub
		return getCargo(0);
	}
	
	@Override
	public ConsultaData getCargo(String strFiltro) {
		return getCargo(0, null, strFiltro);
	}
	
	@Override
	public ConsultaData getCargo(Integer cargo) {
		return getCargo(cargo, null);
	}

	@Override
	public ConsultaData getCargo(Integer cargo, Integer empresa) {
		return getCargo(cargo, empresa, null);
	}

	@Override
	public ConsultaData getCargo(Integer cargo, Integer empresa, String strFiltro) {

		StringBuilder strSQL = new StringBuilder();
		
		if (strFiltro==null) {
			strFiltro="";
		}
		
		if(cargo == null) {
			throw new RuntimeException("El cargo no es válido");
		}
		
		strSQL.append("select * from eje_ges_cargos where cargo <> 0 \n");
		if (cargo!=0) {
			strSQL.append("and cargo = ").append(cargo).append(" ");
		}
		if(empresa != null) {
			strSQL.append("and empresa = ").append(empresa).append(" ");
		}
		if (!"".equals(strFiltro.replaceAll(" ", ""))){
    		strSQL.append("and descrip like '%").append(strFiltro.replaceAll(" ", "%")).append("%' \n");
    	}

		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", strSQL.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;	
		
	}



}
