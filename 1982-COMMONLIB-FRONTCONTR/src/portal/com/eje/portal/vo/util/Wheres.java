package portal.com.eje.portal.vo.util;

import java.util.ArrayList;
import java.util.List;

public class Wheres {
	public static final String EQUALS = "=";
	public static final String IN = "in";
	
	Where master = null;
	
	public Wheres(String campo, String signus, Object value) {
		master = new Where(campo, signus, value);
	}
	
	public Wheres and(String campo, String signus, Object value) {
		master.addAnd(new Where(campo, signus, value));
		
		return this;
	}
	
	public Wheres or(String campo, String signus, Object value) {
		master.addOr(new Where(campo, signus, value));
		
		return this;
	}
	
	public static Wheres where(String campo, String signus, Object value) {
		return new Wheres(campo, signus, value);
	}
	
	public List<Where> build() {
		List<Where> wheres = new ArrayList<Where>();
		wheres.add(this.master);
		return wheres;
	}
	
	public Where getWhere() {
		return this.master;
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		
		str.append(master.toString());
		
		return str.toString();
	}
}
