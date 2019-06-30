package organica.orggenerica.jerarquia;

import java.util.List;


public interface IJerarquiaNodo {

	
	public String toString();
	
	public List<IJerarquiaNodo> getHijos();
	
	public void addHijo(IJerarquiaNodo hijo);
	
	public String getNombre();
	
	public String getId();
	
	public String getClassHtml();
	
	public void setClassHtml(String classHtml);
	
	public boolean isRootNode();
	
	
}
