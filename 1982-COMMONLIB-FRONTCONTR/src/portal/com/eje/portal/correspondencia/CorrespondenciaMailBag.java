package portal.com.eje.portal.correspondencia;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.ejedigital.tool.correo.vo.IVoDestinatario;
import cl.ejedigital.tool.validar.Validar;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.correspondencia.CorrespondenciaLocator;
import portal.com.eje.tools.ListUtils;

public class CorrespondenciaMailBag {
	private Logger logger = Logger.getLogger(this.getClass());
	private List<CorrespondenciaTagged> builders;
	
	public CorrespondenciaMailBag() {
		this.builders = new ArrayList<CorrespondenciaTagged>();
	}
	
	public void addCorrespondenciaTagged(CorrespondenciaTagged cT) {
		this.builders.add(cT);
	}
	
	public boolean addAll(CorrespondenciaMailBag cb) {
		return this.builders.addAll(cb.builders);
	}
	
	public int size() {
		return this.builders.size();
	}
	
	public List<CorrespondenciaTagged> getCorrepondencias() {
		return (List<CorrespondenciaTagged>) ((ArrayList<CorrespondenciaTagged>)this.builders).clone();
	}
	
	public String getCadenaDeDestinatarios() {
		StringBuilder str = new StringBuilder();
		 
		for(CorrespondenciaTagged ct : this.builders) {
			for(IVoDestinatario ivo : ct.getCb().getDestinatarios() ) {
				if(!"".equals(str.toString())) {
					str.append(",");
				}
				str.append(Validar.getInstance().cutStringSinComillas(ivo.getNombre(), 15)).append("<").append(ivo.getEmail()).append(">");
			}
		}
		
		return str.toString();
	}
	
	public List<Double> sendAll(EModulos modulo) {
		List<Double> lista = new ArrayList<Double>();
		
		for(CorrespondenciaTagged ct : builders) {
			try {
				lista.add(CorrespondenciaLocator.getInstance().sendCorreosNow(modulo, ct.getCb()));				 
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		logger.debug(lista.size()+" correos enviados ids:"+ListUtils.getInstance().concatenateValues(lista));
		
		return lista;
	}
}
