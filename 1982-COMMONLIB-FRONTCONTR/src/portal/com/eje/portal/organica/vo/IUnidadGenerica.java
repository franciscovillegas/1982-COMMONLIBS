package portal.com.eje.portal.organica.vo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.output.IDataOut;
import portal.com.eje.portal.vo.vo.Vo;

public interface IUnidadGenerica  {

	public List<IUnidadGenerica> getUnidadesDescendientes();
	
	public List<IUnidadGenerica> getUnidadesByName(String name);
	
	public List<IUnidadGenerica> getUnidadById(String id);
	
	public IUnidadGenerica addHijo(String nombre, String pathImage);
	
	public IUnidadGenerica addHijo(String nombre);
	
	public String getAtributo(String atributo);

	public IUnidadGenerica addAtributo(String atributo, boolean value);
	
	public IUnidadGenerica addAtributo(String atributo, String value);
	
	public IUnidadGenerica addAtributo(String atributo, int value);
	
	public IUnidadGenerica addAtributo(String atributo, double value);
	
	public IDataOut getDataOut();
	
	public double getId(); 
	
	public void setParent(IUnidadGenerica parent);
	
	public IUnidadGenerica getParent();
	
	public List<IUnidadGenerica> getChilds();
	
	public String getName();

	public void setName(String name);

	public IUnidadGenerica findChild(String property, Object value);
	
	public IUnidadGenerica findParent(String[] propertyes, Object value);
	
	public Boolean getAtributoBoolean(String atributo);

	public Date getAtributoDate(String atributo);
	
	public Integer getAtributoInteger(String atributo);
	
	public Double getAtributoDouble(String atributo);
	
	public Object getAtributoObject(String atributo);
	
	/**
	 * Importa los valores de los field del Vo
	 * @since 17-10-2018
	 * @author Pancho
	 * */
	public void importVo(Vo grupo);

	public void importJavabean(Object javaBean);
	
	/**
	 * si es true, el nodo será expandido, si es false será collapsado
	 * @author Pancho
	 * @since 04-06-2019
	 * */
	public void expanded(boolean expand);

	/**
	 * Retorna una ConsultaData con referencia a los mismos objetos que contiene la IUGenerica
	 * @author Pancho
	 * */
	public ConsultaData getData();

	/**
	 * Entrega los objetos de cada unidad generica directamente, así que cuidado, se puede destruir el IUnidadGenerica si es que se modifican erroneamente los valores
	 * */
	public List<Map<?, ?>> getListMap();
	
	public IUnidadGenerica getRootNode();

	public void eachNode(INodeEach each);
}
