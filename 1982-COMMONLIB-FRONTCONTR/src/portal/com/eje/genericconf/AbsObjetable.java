package portal.com.eje.genericconf;

import cl.ejedigital.tool.strings.MyString;
import portal.com.eje.genericconf.ifaces.IObjetable;

public class AbsObjetable implements IObjetable {
	
	private String className;
	private String canonicalName = getClass().getCanonicalName();
	private String classId = MyString.getInstance().quitaCEspeciales(getClass().getCanonicalName());
	
	public AbsObjetable() {
		String cn = getClass().getCanonicalName();
		setClassName( cn.substring(cn.lastIndexOf(".")+1, cn.length()));
		setCanonicalName(getClass().getCanonicalName());
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getCanonicalName() {
		return canonicalName;
	}

	public void setCanonicalName(String canonicalName) {
		this.canonicalName = canonicalName;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	 

 
 
}
