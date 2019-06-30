package portal.com.eje.portal;

import cl.ejedigital.consultor.ConsultaData;

public class ManagerConsultaData {
	
	public String getFirstValueAsString(ConsultaData data, String value) {
		if(data != null) {
			int lastPosition = data.getPosition();
			
			data.toStart();
			if(data.next()) {
				return data.getForcedString(value);
			}
			
			data.setPosition(lastPosition);
		}
		
		return null;
	}
	
	public Integer getFirstValueAsInteger(ConsultaData data, String value) {
		if(data != null) {
			int lastPosition = data.getPosition();
			
			data.toStart();
			if(data.next()) {
				return data.getInt("value");
			}
			
			data.setPosition(lastPosition);
		}
		
		return null;
	}

	public Double getFirstValueAsDouble(ConsultaData data, String value) {
		if(data != null) {
			int lastPosition = data.getPosition();
			
			data.toStart();
			if(data.next()) {
				return data.getDouble(value);
			}
			
			data.setPosition(lastPosition);
		}
		
		return null;
	}
	
}
