package portal.com.eje.portal.organica.vo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import portal.com.eje.portal.organica.vo.IUnidadGenerica;
import portal.com.eje.portal.organica.vo.UnidadGenerica;
import portal.com.eje.tools.ClaseGenerica;

public class UnidadExtendible extends UnidadGenerica {

	public UnidadExtendible(String name, boolean sumValuesToParent) {
		super(name, sumValuesToParent);
		// TODO Auto-generated constructor stub
	}

	public UnidadExtendible(String name, String pathImage, boolean sumValuesToParent) {
		super(name, pathImage, sumValuesToParent);
		// TODO Auto-generated constructor stub
	}

	public UnidadExtendible(String name, String pathImage) {
		super(name, pathImage);
		// TODO Auto-generated constructor stub
	}

	public UnidadExtendible(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public UnidadExtendible(UnidadGenerica unidad, boolean sumesValuesToParent) {
		super(unidad.getName(), null, sumesValuesToParent);
		
		importUnidad(unidad, sumesValuesToParent);
	}
	
	public void importUnidad(UnidadGenerica unidad, boolean sumesValuesToParent) {
		this.addAtributo("icon", unidad.getIcon());
		this.setSumValuesToParent(sumesValuesToParent);
		
		getAttributes().putAll(new HashMap<String, Object>(unidad.getAttributes()));
		
		importChilds(unidad, sumesValuesToParent);
	}
	
	private void importChilds(UnidadGenerica unidad, boolean sumesValuesToParent) {
		
		List<IUnidadGenerica> before = new LinkedList<IUnidadGenerica>(unidad.getChilds());
		
		Class[] defs = {UnidadGenerica.class, boolean.class};
		
		for(IUnidadGenerica child : before) {
			UnidadGenerica ugChild = (UnidadGenerica)child;
			
			Object[] params = {ugChild, sumesValuesToParent};
			
			UnidadGenerica newUnidad = (UnidadGenerica)ClaseGenerica.getInstance().getNewFromClass(this.getClass(), defs, params);
			
			addHijo(newUnidad);
		}
	}

	
	
}
