package portal.com.eje.portal.organica.vo;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.NotImplementedException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.output.IDataOut;
import cl.ejedigital.consultor.output.JSarrayDataOut;
import cl.ejedigital.consultor.output.JSonDataOut;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.vo.ClaseConversor;
import portal.com.eje.portal.vo.VoTool;
import portal.com.eje.portal.vo.iface.IFieldIterable;
import portal.com.eje.portal.vo.vo.Vo;
import portal.com.eje.tools.javabeans.JavaBeanTool;

public class UnidadGenericaBase implements IUnidadGenerica {
	public static final String DIOS_STR = "dios";
	protected final String PROPERTY_ICON="icon";
	protected final String PROPERTY_NOMBRE="nombre";
	protected final String PROPERTY_EXPAND="expanded";
	protected final String PROPERTY_CHILDREN = "children";
	protected final List<String> SYSTEM_NAMES=new ArrayList<>();
	protected LinkedList<IUnidadGenerica> children;
	protected IUnidadGenerica parent;
	protected String name;
	protected Map<String,Object> map;
	protected double id;
	protected boolean sumValuesToParent;
	protected Object imported;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	protected UnidadGenericaBase(String name, String pathImage) {
		this(name, pathImage, true);
	}
			
	protected UnidadGenericaBase(String name, String pathImage, boolean sumValuesToParent) {
		SYSTEM_NAMES.add(PROPERTY_ICON);
		SYSTEM_NAMES.add(PROPERTY_NOMBRE);
		SYSTEM_NAMES.add(PROPERTY_EXPAND);
		SYSTEM_NAMES.add(PROPERTY_CHILDREN);
		this.name = name;
		this.children = new LinkedList<IUnidadGenerica>();
		this.map = new HashMap<String,Object>();
		this.id = UnidadCorrelativoLocator.getInstance().getUnicNumber();
		
		this.sumValuesToParent = sumValuesToParent;
		
		if(pathImage != null)
		this.addAtributo("icon", pathImage);
	}
	
	protected UnidadGenericaBase() {
		
	}
	
	public Map<String,Object> getAttributes() {
		return this.map;
	}
	
	public String getIcon() {
		return getAtributo("icon");
	}
	
	public void setSumValuesToParent(boolean sumValuesToParent) {
		this.sumValuesToParent = sumValuesToParent;
	}

	public boolean isSumValuesToParent() {
		return this.sumValuesToParent;
	}
	
	public void setParent(IUnidadGenerica parent) {
		this.parent = parent;
	}
	
	protected UnidadGenericaBase(String name) {
		this(name, null);
	}
	
	protected UnidadGenericaBase(String name, boolean sumValuesToParent) {
		this(name, null, sumValuesToParent);
	}
	
	public List<IUnidadGenerica> getUnidadesDescendientes() {
		return children;
	}

	private boolean addUnidadDescenciente(IUnidadGenerica unidad) {
		if(unidad != null) {
			children.add(unidad);
			unidad.setParent(this);
		}
		
		return false;
	}

	public IDataOut getDataOut() {
		DataList lista = new DataList();
		
		DataFields fields = new DataFields();
		fields.put("text", name);
		fields.put("id", Math.random());
		fields.put("leaf", !(children != null && children.size() > 0));

		Set<String> set = map.keySet();
		for(String s : set) {
			fields.put(s, map.get(s));
		}
		
		if( children.size() > 0 ){
			DataList listaHijos = new DataList();
			
			for(IUnidadGenerica u : children)  {
				DataFields fieldsUnidades = new DataFields();
				fieldsUnidades.put("nodo", u.getDataOut());	
				listaHijos.add(fieldsUnidades);
			}
			
			JSarrayDataOut out = new JSarrayDataOut(listaHijos);
			fields.put(PROPERTY_CHILDREN, out);
		} 
		else {
			DataList listaHijos = new DataList();
			JSarrayDataOut out = new JSarrayDataOut(listaHijos);
			fields.put(PROPERTY_CHILDREN, out);
		}
		
		lista.add(fields);
		
		JSonDataOut out = new JSonDataOut(lista);
		
		return out;
	}

	public IUnidadGenerica addHijo(IUnidadGenerica hijo) {
	 	addUnidadDescenciente(hijo);
	 	
	 	return hijo;
	}
	
	public IUnidadGenerica addHijo(String nombre) {
		IUnidadGenerica unidad = new UnidadGenericaBase(nombre, null, this.sumValuesToParent);
		addUnidadDescenciente(unidad);
		
		return unidad;
	}
	
	public IUnidadGenerica addHijo(String nombre, String pathImage) {
		IUnidadGenerica unidad = new UnidadGenericaBase(nombre, pathImage, this.sumValuesToParent);
		unidad.addAtributo("icon", pathImage);
		addUnidadDescenciente(unidad);
		
		return unidad;
	}
	
	public <T> T addHijo(Class<? extends UnidadExtendible> clase, String nombre, String pathImage) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		
		Class[] def = {String.class, String.class, boolean.class};
		Object[] params = {nombre, pathImage, this.sumValuesToParent};
		Object uextendible = portal.com.eje.tools.ClaseGenerica.getInstance().getNew(clase, def, params);
		
		//IUnidadGenerica unidad = new UnidadGenerica(nombre, pathImage, this.sumValuesToParent);
		((IUnidadGenerica)uextendible).addAtributo("icon", pathImage);
		addUnidadDescenciente(((IUnidadGenerica)uextendible));
		
		return (T)uextendible;
	}

	public List<IUnidadGenerica> getUnidadesByName(String name) {
		throw new NotImplementedException();
	}

	public List<IUnidadGenerica> getUnidadById(String id) {
		throw new NotImplementedException();
	}

	public IUnidadGenerica addAtributo(String atributo, String value) {
		map.put(atributo, value);
		return this;
	}

	public double getId() {
		return id;
	}

	public IUnidadGenerica addAtributo(String atributo, int value) {
		sumValuesToParent(atributo, (double) value);
		return this.addAtributo(atributo, String.valueOf(value));
	}

	public IUnidadGenerica addAtributo(String atributo, double value) {
		sumValuesToParent(atributo, value);
		return this.addAtributo(atributo, String.valueOf(value));
	}

	public String getAtributo(String atributo) {
		return (String) ClaseConversor.getInstance().getObject( map.get(atributo), String.class);
	}
	
	public Object getAtributoObject(String atributo) {
		return  map.get(atributo);
	}
	
	public Boolean getAtributoBoolean(String atributo) {
		return  (Boolean) ClaseConversor.getInstance().getObject( map.get(atributo) , Boolean.class);
	}

	public Date getAtributoDate(String atributo) {
		return  (Date) ClaseConversor.getInstance().getObject( map.get(atributo) , Date.class);
	}
	
	public Integer getAtributoInteger(String atributo) {
		return  (Integer) ClaseConversor.getInstance().getObject( map.get(atributo) , Integer.class);
	}
	
	public Double getAtributoDouble(String atributo) {
		return  (Double) ClaseConversor.getInstance().getObject( map.get(atributo) , Double.class);
	}
	
	
	public IUnidadGenerica getParent() {
		return this.parent;
	}

	public IUnidadGenerica addAtributo(String atributo, boolean value) {
		map.put(atributo, value);
		return this;
	}

	public IUnidadGenerica addAtributo(String atributo, Object value) {
		map.put(atributo, value);
		return this;
	}
	

	private void sumValuesToParent(String atributo, Double value) {
		if(sumValuesToParent) {
			UnidadGenericaBase pointer = (UnidadGenericaBase)this.parent;
			
			while( pointer != null) {	
				int valueThisPointer = Validar.getInstance().validarInt(pointer.getAtributo(atributo), 0);
				pointer.map.put(atributo, String.valueOf( valueThisPointer + value ));
				
				pointer = (UnidadGenericaBase) pointer.parent;
			}
		}

	}
	
	public List<IUnidadGenerica> getChilds() {
		return this.children;
	}

	public IUnidadGenerica findChild(String property, Object value) {
		return findChildRecursive(this, property, value);
	}
	
	public List<IUnidadGenerica> findChilds(String property, Object value) {
		return findChildsRecursive(this, property, value);
	}
	
	private IUnidadGenerica findChildRecursive(IUnidadGenerica root, String property, Object value) {
		List<IUnidadGenerica> encontrados = findChildsRecursive(root, property, value);
		IUnidadGenerica encontrado = null;
		
		if(encontrados != null && encontrados.size() > 0) {
			encontrado = encontrados.get(0);
		}
		
		return encontrado;
	}
	
	private List<IUnidadGenerica> findChildsRecursive(IUnidadGenerica root, String property, Object value) {
		List<IUnidadGenerica> childs = root.getChilds();
		List<IUnidadGenerica> encontrados = new ArrayList<IUnidadGenerica>();
		
		if (childs != null && childs.size() > 0) {
			for (IUnidadGenerica child : childs) {
				Object valueConverted = ClaseConversor.getInstance().getObject( child.getAtributo(property) , value.getClass());
				
				if (value.equals(valueConverted)) {
					encontrados.add(child);
				} else {
					encontrados.addAll(findChildsRecursive(child, property, value));
				}
			}
		}

		return encontrados;
	}
	
	public IUnidadGenerica findParent(String[] propertyes, Object value) {
		return findParentRecursive(this, propertyes, value);
	}
	
	private IUnidadGenerica findParentRecursive(IUnidadGenerica root, String[] propertyes, Object value) {
		IUnidadGenerica parent = root.getParent();
		
		while (parent != null) {
			
			boolean haveIt = containPropertieValue(root, propertyes, value);
			
			if (haveIt) {
				return parent;
			}
			else {
				parent = parent.getParent();
			}
		}

		return null;
	}
	
	private boolean containPropertieValue(IUnidadGenerica root, String[] propertyes, Object value) {
		boolean yes = false;
		
		if(propertyes != null && value != null) {
			for(String key : propertyes) {
				if( value.equals( ((UnidadGenericaBase)parent).getAttributes().get(key) )) {
					yes = true;
					break;
				}
			}	
		}
		
		return yes;
	}

	@Override
	public void importVo(Vo vo) {
		if(vo != null ) {
			this.imported = vo;
			
			VoTool.getInstance().eachField(vo, new IFieldIterable() {
				@Override
				public void iterate(String field, Object value) {
					if(!"nombre".equals(field) && !"icon".equals(field)) {
						addAtributo(field, value);
					}
				}
			});
		}
		
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}
	
	public Object getVoImported() {
		return this.imported;
	}

	@Override
	public void importJavabean(Object javaBean) {
		if(javaBean != null) {
			this.imported = javaBean;
			
			ConsultaData data = JavaBeanTool.getInstance().getDataFromObject(javaBean);
			 
			while(data != null && data.next()) {
				for(String c : data.getNombreColumnas()) {
					if(!isSystemProperty(c)) {
						addAtributo( c , data.getObject(c) );	
					}
				}
			}
		}
		
	}
 
	private boolean isSystemProperty(String name ) {
		return SYSTEM_NAMES.contains(name);
	}

	@Override
	public void expanded(boolean expand) {
		addAtributo(PROPERTY_EXPAND, expand);
		
	}

	

	@Override
	public ConsultaData getData() {
		String[] array = new String[map.keySet().size()];
		map.keySet().toArray(array);
		
		ConsultaData data = ConsultaTool.getInstance().newConsultaData(array);
		privateGetData(this, data);
		
		return data;
	}
	
	private void privateGetData(IUnidadGenerica u, ConsultaData data) {
		if( u != null && data != null) {
			Map<String,Object> iterateMap = ((UnidadGenericaBase) u).map;
			
			DataFields df = new DataFields();			
			for( Entry<String,Object> entry : iterateMap.entrySet()) {
				if(entry.getKey() == null) {
					continue;
				}
				boolean sonLosHijos = (PROPERTY_CHILDREN.equals(entry.getKey()));
				if(!sonLosHijos) {
					if(!data.getNombreColumnas().contains(entry.getKey())) {
						data.getNombreColumnas().add(entry.getKey());
					}
					df.put(entry.getKey(), entry.getValue());
				}
			}
			
			data.add(df);
			
			for(IUnidadGenerica hijo : u.getChilds()) {
				privateGetData(hijo, data);
			}
		}
		
		
	}

	
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<Map<?,?>> getListMap() {
		List<Map<?,?>> retorno = new ArrayList<>();
		
		IUnidadGenerica root = getRootNode();
		UnidadGenericaIterator.getInstance().eachNode(root, new INodeEach() {
			
			@Override
			public void each(IUnidadGenerica u, int position) {
				
				retorno.add( ((UnidadGenericaBase)u).map );
			}
		});
		return retorno;
	}

	

	@Override
	public IUnidadGenerica getRootNode() {
		int iteracion = 0 ;
		IUnidadGenerica parent = this;
		while(parent.getParent() != null) {
			iteracion++;
			parent = this;
			if(iteracion > 300) {
				throw new StackOverflowError("Se ha sobrepasado la cantidad máxima de stacks");
			}
		}
		
		return parent;
	}

	@Override
	public void eachNode(INodeEach each) {
		if(each != null) {
			IUnidadGenerica root = getRootNode();
			UnidadGenericaIterator.getInstance().eachNode(root, each);	
		}
	}
}
