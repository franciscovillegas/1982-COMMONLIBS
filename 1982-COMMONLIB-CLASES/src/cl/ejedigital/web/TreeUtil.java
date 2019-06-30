package cl.ejedigital.web;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.organica.OrganicaLocator;
import portal.com.eje.portal.organica.vo.IUnidadGenerica;
import portal.com.eje.portal.organica.vo.UnidadGenerica;
import portal.com.eje.portal.vo.ClaseConversor;
import portal.com.eje.portal.vo.VoTool;
import portal.com.eje.portal.vo.vo.Vo;
import portal.com.eje.tools.ListUtils;

public class TreeUtil {

	public static TreeUtil getInstance() {
		return Util.getInstance(TreeUtil.class);
	}
	
	/**
	 * Retorna un Arbol a partir de un lista de Vos, es como si lo agrupara por los campos que se indican en @param String[] campos
	 * 
	 * */
	
	public IUnidadGenerica getTree(List<Vo> cols, String[] campos, boolean sumValuesToParent, boolean expandActives) {
		List<Object> fields = ListUtils.getInstance().getList(campos, LinkedList.class);
		
		int fieldPos = 0;
		IUnidadGenerica dios = new  UnidadGenerica("Dios", sumValuesToParent);
		getTree(dios, cols, fields, fieldPos, expandActives);
		
		return dios;
	}
	
	private void getTree(IUnidadGenerica dios, List<Vo> cols, List<Object> fields,int fieldPos, boolean expandActives) {
		if(cols != null && fieldPos < fields.size()) {
			String field = (String) fields.get(fieldPos++);
			Map<Object, List<Vo>> mapVos = VoTool.getInstance().getMapList(cols, HashMap.class, ArrayList.class, field);
			
			if(mapVos.size() > 0) {
				
				
				for(Object key : mapVos.keySet()) {
					
					List<Vo> subVos = mapVos.get(key);
					String retornoString = (String) ClaseConversor.getInstance().getObject(key, String.class);
					IUnidadGenerica hijo = addPropertiesFromVoOnlyWhenIsLeaf(dios, retornoString, fieldPos == fields.size(), subVos);
					
					getTree(hijo, subVos, fields, fieldPos, expandActives);
				}
				
			}
		}
		
	}
	
	/**
	 * Agrega propiedades al nodo cuando el nodo es hoja
	 * */
	private IUnidadGenerica addPropertiesFromVoOnlyWhenIsLeaf(IUnidadGenerica dios, String retornoString, boolean esHoja, List<Vo> subVos) {
		IUnidadGenerica hijo = dios.addHijo(retornoString);
		
		if(esHoja && subVos.size() > 0) {
			Vo vo = subVos.iterator().next();
			
			//hijo.addAtributo("leaf", true);
			List<Method> methods = VoTool.getInstance().getGetsMethodsWithNoParameters(vo.getClass());
			for(Method m : methods) {
				String field = VoTool.getInstance().getMetodNameWithoutSetOrGetOrIsLowerCase(m);
				Assert.isTrue(!"id".equals(field),"Ningún campo del Vo puede ser id, o si no se confunde con el id del nodo, que si o si debe llamarse id");
				
				Object value = VoTool.getInstance().getReturnFromMethod(vo, m);
				
				if("activo".equals(field) ) {
					 boolean activo = (Boolean)ClaseConversor.getInstance().getObject( value , Boolean.class);
					 if(activo) {
						 addPropertyToAllParents(dios,"activo", true);
						 addPropertyToAllParents(dios,"expanded", true);
					 }
				}
				
				if( value == null) {
					hijo.addAtributo(field, null);	
				}
				else if(Number.class.isAssignableFrom(value.getClass())) {
					hijo.addAtributo(field, (Integer)ClaseConversor.getInstance().getObject( value , Integer.class));	
				}
				else if(Boolean.class.isAssignableFrom(value.getClass())) {
					hijo.addAtributo(field, (Boolean)ClaseConversor.getInstance().getObject( value , Boolean.class));
				}
				else if(String.class.isAssignableFrom(value.getClass())) {
					hijo.addAtributo(field, (String)ClaseConversor.getInstance().getObject( value , String.class));
				}
				else {
					hijo.addAtributo(field, (String)ClaseConversor.getInstance().getObject( value , String.class));
				}
			}
		}
		return hijo;
	}
	
	/**
	 * Asigna un valor de propiedad a todos los padres hasta el nodo root
	 * */
	public void addPropertyToAllParents(IUnidadGenerica dios, String propName, boolean value) {
		if(dios != null) {
			dios.addAtributo(propName, value);
			addPropertyToAllParents(dios.getParent(), propName, value);
		}
	}
}
