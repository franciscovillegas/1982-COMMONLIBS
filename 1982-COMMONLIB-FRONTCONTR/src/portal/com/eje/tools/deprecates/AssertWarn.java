package portal.com.eje.tools.deprecates;

public class AssertWarn {

	public static AssertWarn getInstance() {
		return AssertWarnCached.getInstance();
	}
	
	public void advice(Class<?> claseAvisadora, String msg) {
		try {
			throw new Exception("CUIDADO!!! La clase \""+claseAvisadora.getCanonicalName()+"\" dice :"+msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void isTrue(Class<?> claseAvisadora, boolean asercion , String msg) {
		if(!asercion) {
			getInstance().advice(claseAvisadora, msg);
		}
	}
}
