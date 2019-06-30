package portal.com.eje.tools.sence;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.springframework.util.Assert;

import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.transactions.TransactionConnection;
import portal.com.eje.tools.sence.error.UFValueIsNotValidException;
import portal.com.eje.tools.sence.util.SenceTramoCalculatorLocator;
import portal.com.eje.tools.sence.vo.SencePersonaTramoVo;

public class SenceTool {

	public static SenceTool getInstance() {
		return Util.getInstance(SenceTool.class);
	}
	
	public Collection<SencePersonaTramoVo> getPersonas(TransactionConnection cons, List<Integer> ruts) throws NullPointerException, SQLException, UFValueIsNotValidException {
		Collection<SencePersonaTramoVo> personas = null;
		if(cons != null && ruts != null && ruts.size() > 0) {
			personas = SenceTramoCalculatorLocator.getImplementacion().getPersonas(cons, ruts);
		}
		return personas;
	}
	
	/**
	 * El valor del curso, es el valor de la hora * la cantidad de horas que dura el curso
	 * @author Pancho
	 * @throws UFValueIsNotValidException 
	 * @since 07-06-2019
	 * */
	public SenceCursoPersona getPersonas(TransactionConnection cons, List<Integer> ruts, int costoCurso) throws NullPointerException, SQLException, UFValueIsNotValidException {
		Collection<SencePersonaTramoVo> personas = null;
		if(cons != null && ruts != null && ruts.size() > 0) {
			personas = SenceTramoCalculatorLocator.getImplementacion().getPersonas(cons, ruts);
		}
		
		SenceCursoPersona curso = new SenceCursoPersona(personas, costoCurso);
		return curso;
	}
}
