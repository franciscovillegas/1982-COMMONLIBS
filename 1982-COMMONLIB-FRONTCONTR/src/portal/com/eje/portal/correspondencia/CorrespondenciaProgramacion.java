package portal.com.eje.portal.correspondencia;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;

public class CorrespondenciaProgramacion {
	public enum ECorrespondenciaProgramacionTipo {
		ejecucion_fechas, 
		ejecucion_itera_dias_ano, 
		ejecucion_itera_dias_mes, 
		ejecucion_itera_dias_semanas
	}
	
	public enum ECorrespondenciaProgramacionEstado {
		noDefinido,
		porEnviar,
		hiloEjecutado,
		enviado,
		eliminado,
		error
	}
	
	
	private ECorrespondenciaProgramacionEstado estado;
	private ECorrespondenciaProgramacionTipo tipo;
	private Date fechaExec;
	private List<Integer> iteraciones;
	
	
	
	public CorrespondenciaProgramacion(ECorrespondenciaProgramacionTipo tipo, List<Integer> iteraciones, Date timeExec) {
		if(tipo == null) {
			throw new InvalidParameterException("El tipo no puede ser null");
		}
		else if(iteraciones == null || iteraciones.size() == 0 ) {
			throw new InvalidParameterException("El rango de iteraciones no puede ser null ni vacío.");
		}
		
		if(tipo == ECorrespondenciaProgramacionTipo.ejecucion_itera_dias_ano || 
		   tipo != ECorrespondenciaProgramacionTipo.ejecucion_itera_dias_mes || 
		   tipo != ECorrespondenciaProgramacionTipo.ejecucion_itera_dias_semanas ) {
				this.tipo = tipo;
				this.iteraciones = iteraciones;
				this.fechaExec = timeExec;
				this.estado = ECorrespondenciaProgramacionEstado.noDefinido;
		 }
		else {
			this.estado = ECorrespondenciaProgramacionEstado.error;
			throw new InvalidParameterException("El tipo \""+tipo+"\" no permite parámetros del tipo Integer");
		}
	 
	}
	
	public CorrespondenciaProgramacion(ECorrespondenciaProgramacionTipo tipo, Date fechaExec) {
		if(tipo == null) {
			throw new InvalidParameterException("El tipo no puede ser null");
		}
		else if(fechaExec == null ) {
			throw new InvalidParameterException("La fecha a ejecutar no puede ser null.");
		}
		
		if(tipo == ECorrespondenciaProgramacionTipo.ejecucion_fechas  ) {
				this.tipo = tipo;
				this.fechaExec = fechaExec;
				this.estado = ECorrespondenciaProgramacionEstado.noDefinido;
		 }
		else {
			this.estado = ECorrespondenciaProgramacionEstado.error;
			throw new InvalidParameterException("El tipo \""+tipo+"\" no permite parámetros del tipo Date ");
		}
	 
	}
	
	public static Date getNow() {
		String sql = "SELECT now=getdate()";
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql);
			if(data != null && data.next()) {
				Date date = data.getDateJava("now");
				return date;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public ECorrespondenciaProgramacionEstado getEstado() {
		return estado;
	}

	public ECorrespondenciaProgramacionTipo getTipo() {
		return tipo;
	}

	public Date getFechaExec() {
		return fechaExec;
	}

	public List<Integer> getIteraciones() {
		return iteraciones;
	}
	
	public String getIteracionesStr() {
		StringBuilder strBuilder = new StringBuilder();
		
		for(Integer i : iteraciones) {
		   if(strBuilder.toString().length() != 0) {
			   strBuilder.append(",");
		   }
		   
		   strBuilder.append(i);
		}
		
		return strBuilder.toString();
	}
	
	
	
}
