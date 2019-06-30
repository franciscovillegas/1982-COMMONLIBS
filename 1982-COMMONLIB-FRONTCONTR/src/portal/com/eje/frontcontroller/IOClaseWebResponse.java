package portal.com.eje.frontcontroller;

import cl.ejedigital.consultor.ConsultaData;

public class IOClaseWebResponse {
	public boolean success;
	public ConsultaData data;
	public double total;
	public String message;
	public String context_path;
	public boolean usuario_is_session;
	public ConsultaData usuario;
	public String time_process;
	private String originalResponse;
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public ConsultaData getData() {
		return data;
	}
	public void setData(ConsultaData data) {
		this.data = data;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getContext_path() {
		return context_path;
	}
	public void setContext_path(String context_path) {
		this.context_path = context_path;
	}
	public boolean isUsuario_is_session() {
		return usuario_is_session;
	}
	public void setUsuario_is_session(boolean usuario_is_session) {
		this.usuario_is_session = usuario_is_session;
	}
	public ConsultaData getUsuario() {
		return usuario;
	}
	public void setUsuario(ConsultaData usuario) {
		this.usuario = usuario;
	}
	public String getTime_process() {
		return time_process;
	}
	public void setTime_process(String time_process) {
		this.time_process = time_process;
	}
	
	public String getAnalisisValue(String typeAnalisis) {
		IOClaseWebResponse cwr = this;
		if(cwr != null && cwr.getData() != null) {
			cwr.getData().toStart();
			while(cwr.getData().next()) {
				if(cwr.getData().getString("analisis").equals(typeAnalisis)) {
					return cwr.getData().getString("valor");
				}	
			}
		}
		
		return null;
	}
	public void setOriginalReponse(String xml) {
		this.originalResponse = xml;
		
	}
	public String getOriginalResponse() {
		return originalResponse;
	}
	
	
}
