package portal.com.eje.portal.trabajador;

import java.util.ArrayList;
import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.IConsultaDataStartable;
import portal.com.eje.portal.factory.Weak;
import portal.com.eje.portal.organica.OrganicaLocator;

public class TrabajadorTool implements ITrabajadorTool {
	private final String CAMPO_JEFE_RUT = "jefe_rut";
	private final String CAMPO_JEFE_NOMBRES = "jefe_nombres";
	private final String CAMPO_JEFE_NOMBRE = "jefe_nombre";
	
	private final String CAMPO_NOMBRE = "nombre";
	private final String CAMPO_NOMBRES = "nombres";
	private final String CAMPO_RUT = "rut";
	
	
	
	public static ITrabajadorTool getInstance() {
		return Weak.getInstance(TrabajadorTool.class);
	}
	
	public void addJefatura(ConsultaData data) {
		if(data != null) {
			addNombreColumnas(data);
			
			data.each(new IConsultaDataStartable() {
				
				@Override
				public void each(ConsultaData data) {
					if(data.existField(CAMPO_RUT)) {
						int rut = data.getInt(CAMPO_RUT);
						
						ConsultaData dataJefe = OrganicaLocator.getInstance().getJefeDelTrabajador(rut);
						if(dataJefe != null && dataJefe.next()) {
							data.getActualData().put(CAMPO_JEFE_NOMBRE, dataJefe.getString(CAMPO_NOMBRE));
							data.getActualData().put(CAMPO_JEFE_NOMBRES, dataJefe.getString(CAMPO_NOMBRES));
							data.getActualData().put(CAMPO_JEFE_RUT, dataJefe.getString(CAMPO_RUT));
						}	
					}
				}
			});
		}
	}
	
	private void addNombreColumnas(ConsultaData data) {
		if(data != null) {
			List<String> ncs = new ArrayList<>();
			ncs.add(CAMPO_JEFE_NOMBRE);
			ncs.add(CAMPO_JEFE_NOMBRES);
			ncs.add(CAMPO_JEFE_RUT);
			for(String nc : ncs) {
				if(!data.getNombreColumnas().contains(nc)) {
					data.getNombreColumnas().add(nc);
				}
			}	
		}
		
	}
	
}
