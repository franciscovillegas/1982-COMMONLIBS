package portal.com.eje.frontcontroller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.tool.cache.CacheLocator;
import cl.ejedigital.tool.validar.Validar;
import portal.com.eje.frontcontroller.ioutils.IOUtilExcel;
import portal.com.eje.frontcontroller.ioutils.IOUtilParam;
import portal.com.eje.frontcontroller.ioutils.IOUtilSencha;
import portal.com.eje.portal.organica.OrganicaLocator;
import portal.com.eje.portal.organica.ifaces.IUnidadDependency;
import portal.com.eje.portal.organica.vo.IUnidadGenerica;
import portal.com.eje.portal.organica.vo.UnidadGenerica;
import portal.com.eje.portal.organica.vo.UnidadGenericaStyleDef;
import portal.com.eje.portal.vo.ClaseConversor;
import portal.com.eje.tools.Cache.CachePortalTypes;

public class IOSenchaJsonTree implements  IIOSenchaJsonTree {

	
	private IIOClaseWebLight  io;
	private IIOSenchaJsonTreeNodeListener nodeListener;
	private boolean useCache;
	
 
	
	public IOSenchaJsonTree(IIOClaseWebLight  io) {
		this.useCache = false;
		this.io = io;
		
		if(io.getParamBoolean("treeRefresh")) {
			this.senchaTreeClearCache();
		}
	}

	public IIOSenchaJsonTreeNodeListener getNodeListener() {
		return nodeListener;
	}

	public void setNodeListener(IIOSenchaJsonTreeNodeListener nodeListener) {
		this.nodeListener = nodeListener;
	}

	public boolean isUseCache() {
		return useCache;
	}

	public void setUseCache(boolean useCache) {
		this.useCache = useCache;
	}

	@Override
	public void retSenchaTree(IUnidadGenerica unidad, UnidadGenericaStyleDef defUnidad) {
		if(unidad != null) {
			unidad.addAtributo("usuario_is_session_valid", this.io.getUsuario().esValido());
			
			putStyle(unidad, defUnidad);
			setRecursiveStyle(unidad, defUnidad);
		}
		
		if(unidad != null) {
			String outType = this.io.getParamString("return_type", "").toLowerCase();
			if("application/excel".equals(outType)) {
				String outFileName  = this.io.getParamString("outFileName", "archivo_PeopleManager");
				ConsultaData dtaColumnas = this.io.getUtil(IOUtilParam.class).getParamConsultaData(io,"columnas");
				LinkedHashMap<String, String> columnas = new LinkedHashMap<String, String>();
				while (dtaColumnas!=null && dtaColumnas.next()) {
					columnas.put(dtaColumnas.getForcedString("columna"), dtaColumnas.getForcedString("texto"));
				}
				//ConsultaData data = getDataFromIUnidad(unidad, columnas);
				List<String> nombres = new ArrayList<String>(columnas.keySet());
				ConsultaData data = new ConsultaData(nombres);
				for(IUnidadGenerica hija: unidad.getUnidadesDescendientes()) {
					addDataFromIUnidad(data, hija, nombres);
				}
				this.io.getUtil(IOUtilExcel.class).retExcel(io, data, outFileName + ".xls", columnas);
			}else {
				this.io.retTexto( unidad.getDataOut().getListData() ); // debe ser un array [] 
			}
		}
		
	}
	
	private void addDataFromIUnidad(ConsultaData data, IUnidadGenerica unidad, List<String> nombres) {
		DataFields row = new DataFields();
		for (String strKey: nombres) {
			row.put(strKey, unidad.getAtributo(strKey));
		}
		data.add(row);
		List<IUnidadGenerica> hijas = unidad.getUnidadesDescendientes();
		for(IUnidadGenerica hija: hijas) {
			addDataFromIUnidad(data, hija, nombres);
		}
	}

	private void setRecursiveStyle(IUnidadGenerica unidad, UnidadGenericaStyleDef defUnidad) {
		
	
		if(unidad != null && defUnidad != null) {
			List<IUnidadGenerica> hijas = unidad.getUnidadesDescendientes();
			for(IUnidadGenerica h: hijas) {
				putStyle(h, defUnidad);
				setRecursiveStyle(h, defUnidad);
			}
		}
	}
	
	private void putStyle(IUnidadGenerica h, UnidadGenericaStyleDef defUnidad) {
		Boolean activo = (Boolean) ClaseConversor.getInstance().getObject(h.getAtributo("activo"), Boolean.class);
		if(h.getUnidadesDescendientes().size() == 0) {
			if(activo) {
				String strIcon = defUnidad != null ? defUnidad.getIconHojaSelected() : null ;
				String icon = getLocalIcon(null, h, false, defUnidad, strIcon );
				h.addAtributo("icon", icon);
			}
			else {
				String strIcon = defUnidad != null ? defUnidad.getIconHoja() : null ;
				String icon = getLocalIcon(null, h, false, defUnidad, strIcon);
				h.addAtributo("icon", icon);
			}
		}
		else {
			if(activo) {
				String strIcon = defUnidad != null ? defUnidad.getIconAgrupaSelected() : null ;
				String icon = getLocalIcon(null, h, true, defUnidad, strIcon);
				h.addAtributo("icon", icon);
			}
			else {
				String strIcon = defUnidad != null ? defUnidad.getIconAgrupaSelected() : null ;
				String icon = getLocalIcon(null, h, true, defUnidad, strIcon);
				h.addAtributo("icon", icon);
			}
			
		}
	}

	@Override
	public void retSenchaTreeAllCompanies(UnidadGenericaStyleDef defUnidad) {
		List<String> roots = OrganicaLocator.getInstance().getUnidadesRaices(); 
		retSenchaTreeFromUnidad(roots.toArray(), defUnidad);
	}
 

	@Override
	public String getSenchaTreeAllCompanies(UnidadGenericaStyleDef defUnidad) {
		List<String> roots = OrganicaLocator.getInstance().getUnidadesRaices(); 
		return getSenchaTreeFromUnidad(roots.toArray(), defUnidad);
	}
	
	@Override
	public String getSenchaTreeAllCompanies(UnidadGenericaStyleDef defUnidad, boolean showUnitsHidden) {
		List<String> roots = OrganicaLocator.getInstance().getUnidadesRaices(); 
		return getSenchaTreeFromUnidad(roots.toArray(), defUnidad, (IUnidadDependency)null, showUnitsHidden);
	}	
	
	@Override
	public void retSenchaTreeFromUnidad(Object[] unidades, UnidadGenericaStyleDef defUnidad) {
		io.retTexto(getSenchaTreeFromUnidad(unidades, defUnidad));
	}
	
	
	private void doEventChangeIcon(UnidadGenericaStyleDef defUnidad) {
		
	}
	
	public String getSenchaTreeFromUnidad(Object[] unidades, UnidadGenericaStyleDef defUnida) {
		return getSenchaTreeFromUnidad(unidades, defUnida, null);
	}
	
	private String getLocalIcon(String unidId, IUnidadGenerica generica, boolean isGroup, UnidadGenericaStyleDef defUnidad, String icon) {
		String iconString = generica.getAtributo("icon");
		if(defUnidad != null && isGroup && defUnidad.getIconAgrupa() != null) {
			iconString = defUnidad.getIconAgrupa();
		}
		
		if(defUnidad != null && !isGroup && defUnidad.getIconHoja() != null) {
			iconString = defUnidad.getIconHoja();
		}
		
		String iconString2 = null;
		
		if( defUnidad != null) {
			iconString  =defUnidad.putIcon(unidId, true,iconString );
			
			iconString2 = defUnidad.putIcon2(generica, getNivel(generica), isGroup, iconString);
		}
		
		if(iconString2 != null) {
			return iconString2;
		}
		else {
			return iconString;
		}
	}
	
	private int getNivel(IUnidadGenerica unidad) {
		int nivel = 0;
		
		while(unidad != null) {
			unidad = unidad.getParent();
			nivel++;
		}
		
		return nivel;
	}
	
	
	public String getSenchaTreeFromUnidad(Object[] unidades, UnidadGenericaStyleDef defUnidad,IUnidadDependency dependencia) {
		return getSenchaTreeFromUnidad(unidades, defUnidad, dependencia, false);
	}
	
	public String getSenchaTreeFromUnidad(Object[] unidades, UnidadGenericaStyleDef defUnidad,IUnidadDependency dependencia, boolean showUnitsHiddens) {
		String iconName = getIconName(defUnidad);
		String key = serialize( unidades , iconName );
		String jsonTree = null;
		
		if( this.useCache ) {
			jsonTree = (String)CacheLocator.getInstance(CachePortalTypes.CACHEORGANICA).get(key);	
		}
		
		if(jsonTree == null ) {
			IUnidadGenerica unidadDios = new  UnidadGenerica("Dios");
			unidadDios.addAtributo("context_path"				, io.getReq().getContextPath());
			unidadDios.addAtributo("usuario_is_session_valid"	, io.getUsuario().esValido());
			unidadDios.addAtributo("cls"						, "first-level");
			unidadDios.addAtributo("usuario"					, io.getUtil(IOUtilSencha.class).getJSArray(io, io.getUsuario().toConsultaData()).getListData() );
			
			unidadDios.addAtributo("expanded"  		  			, true);
			
			if(defUnidad != null ) {
				unidadDios.addAtributo("icon", getLocalIcon("dios", unidadDios, true, defUnidad, defUnidad.getIconAgrupa()));
			}
			
			
			
			if(unidades != null) {	
				 
				for(Object o: unidades ) {
					String unidad = String.valueOf(o);
					 
					ConsultaData unidadDef = OrganicaLocator.getInstance().getUnidad(unidad);
					if( unidadDef != null && unidadDef.next()) {
						String unidId = unidadDef.getString("unid_id");
						IUnidadGenerica unidadDependientes = unidadDios.addHijo(unidadDef.getString("unid_desc"));
						
						
						List<String> columnas = unidadDef.getNombreColumnas();
						for(String c : columnas) {
							unidadDependientes.addAtributo(c , unidadDef.getForcedString(c));
						}
						
						int cantHihijos = getSenchaTreeFromUnidad_Recursivo(unidadDependientes, unidId, defUnidad, dependencia, showUnitsHiddens);
						if(cantHihijos > 0) {

							unidadDependientes.addAtributo("icon" , getLocalIcon(unidId, unidadDependientes, true, defUnidad, defUnidad != null? defUnidad.getIconAgrupa() : null));
						}
						else {
							unidadDependientes.addAtributo("icon" ,  getLocalIcon(unidId, unidadDependientes, false, defUnidad, defUnidad != null? defUnidad.getIconHoja() : null));
						}
						
						this.doLoop(unidadDef, unidadDependientes);
						
						if(dependencia != null) {
							dependencia.putDependency(unidadDependientes, defUnidad, unidId, cantHihijos > 0, unidadDependientes.getAtributo("icon"));
						}
					}
					else {
						IUnidadGenerica hijo = unidadDios.addHijo("Unidad desconocida", defUnidad.putIcon(null, true, defUnidad.getIconAgrupa()) );
						if(dependencia != null) {
							dependencia.putDependency( hijo, defUnidad,  null, false,  hijo.getAtributo("icon"));
						}
					}
				}
			}
			else {
				IUnidadGenerica hijo =unidadDios.addHijo("Unidad desconocida", defUnidad.putIcon(null, true, defUnidad.getIconAgrupa()) );
				if(dependencia != null) {
					dependencia.putDependency( hijo,defUnidad,  null, false,  hijo.getAtributo("icon"));
				}
			}
	 
			unidadDios.addAtributo("time_process"    			, io.getCronometro().GetTimeHHMMSS());
			
			jsonTree = unidadDios.getDataOut().getListData();
			
			if( this.useCache ) {		
				CacheLocator.getInstance(CachePortalTypes.CACHEORGANICA).put(key, jsonTree);
			}
		}
		
		return jsonTree;
	}

 
	private int getSenchaTreeFromUnidad_Recursivo(IUnidadGenerica unidadGenerica, String unidad, UnidadGenericaStyleDef defUnidad,IUnidadDependency dependencia, boolean showUnitsHiddens) {
		ConsultaData data  = OrganicaLocator.getInstance().getUnidadesHijas(unidad, showUnitsHiddens);
		
		int hijos=0;
		while(data != null && data.next()) {
			String unidadHija = data.getString("unidad"); 					 
			IUnidadGenerica hijo = unidadGenerica.addHijo(data.getString("unid_desc"));
			
			List<String> columnas = data.getNombreColumnas();
			for(String c : columnas) {
				hijo.addAtributo(c , data.getForcedString(c));
			}
			
			int nietos = getSenchaTreeFromUnidad_Recursivo(hijo, data.getString("unid_id"), defUnidad, dependencia, showUnitsHiddens);
			if(nietos > 0) {
				hijo.addAtributo("icon" , getLocalIcon(unidadHija, hijo, true, defUnidad, defUnidad != null? defUnidad.getIconAgrupa() : null));
			}
			else {
				hijo.addAtributo("icon" , getLocalIcon(unidadHija, hijo, false, defUnidad, defUnidad != null? defUnidad.getIconHoja() : null) );
			}
			
			this.doLoop(data, hijo);
			
			if(dependencia != null) {
				dependencia.putDependency(hijo, defUnidad, data.getString("unid_id"), nietos > 0, hijo.getAtributo("icon"));
			}
			
			hijos++;
		}
		

		
		return hijos;
	}
	
	private String getIconName(UnidadGenericaStyleDef defUnidad) {
		String iconName = "normal";
		
		if(defUnidad !=null && defUnidad.getIconAgrupa() != null) {
			String icon = defUnidad.getIconAgrupa();
			
			if(icon.lastIndexOf("/") != -1) {
				iconName = icon.substring(icon.lastIndexOf("/") + 1);
			}
			else {
				iconName = icon;
			}
		} 
		
		return iconName;
	}
	private String serialize(Object[] objs, String keyIcons) {
		StringBuilder ret = new StringBuilder(keyIcons);
		if(objs != null) {
			for(Object o : objs) {
				if(ret.toString().length() > 0) {
					ret.append("_");	
				}
				ret.append(o);
			}
		}
		
		return ret.toString();
	}

	@Override
	public void senchaTreeClearCache() {
		Iterator e =  CacheLocator.getInstance(CachePortalTypes.CACHEORGANICA).keySet().iterator();
		
		List<String> toDelete  = new ArrayList<String>();
		
	 	while(e.hasNext()) {
			toDelete.add((String)e.next());
		}
	
	 	for(String s : toDelete) {
	 		CacheLocator.getInstance(CachePortalTypes.CACHEORGANICA).remove(s);
	 	}
	 	
	 	System.out.println("[Cache:CACHEORGANICA  Before:"+toDelete.size()+"  After:"+CacheLocator.getInstance(CachePortalTypes.CACHEORGANICA).size()+"]");
	 	
	}
	
	private void doLoop(ConsultaData data, IUnidadGenerica u) {
		if(this.nodeListener != null) {
			this.nodeListener.onLoop(data,u);
		}
	}

	@Override
	public void clearCache(Object[] unidades,UnidadGenericaStyleDef defUnidad) {
		String iconName = getIconName(defUnidad);
		String key = serialize( unidades , iconName );
		CacheLocator.getInstance(CachePortalTypes.CACHEORGANICA).remove(key);		
	}

	@Override
	public void retSenchaTreeAllCompanies() {
		this.retSenchaTreeAllCompanies(SenchaTreeType.normal.getStyle());
	}

	@Override
	public void retSenchaTreeFromUnidad(Object[] unidades) {
		this.retSenchaTreeFromUnidad(unidades, SenchaTreeType.normal.getStyle());
		
	}

	@Override
	public void retSenchaTreeFromUnidad(Object[] unidades, UnidadGenericaStyleDef defUnidad,IUnidadDependency dependencia) {
		String str = getSenchaTreeFromUnidad(unidades, defUnidad, dependencia);
		io.retTexto(str);
	}


}
