package portal.com.eje.genericconf.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cl.ejedigital.web.FreemakerTool;
import cl.ejedigital.web.datos.Order;
import cl.ejedigital.web.freemaker.sethash.SetHashAllFields;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import portal.com.eje.genericconf.buttongrouptools.ButtonGroupTool;
import portal.com.eje.genericconf.ifaces.IButton;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.tools.maptable.MapVoValues;
import portal.com.eje.tools.paquetefactory.PaqueteFactory;
import portal.com.eje.tools.sortcollection.VoSort;

public class FreemakerButtonTool {

	public static FreemakerButtonTool getInstance() {
		return Util.getInstance(FreemakerButtonTool.class);
	}
	/**
	 * Quedarán bajo el key del parámetro "keySimpleList" y la lista de botones bajo "subgrupos"
	 * 
	 * @author Pancho
	 * @since 24-05-2019
	 * */
 
	public void putButtonListGroupByGrupo(SimpleHash modelRoot, String keySimpleList, String paquete) throws Exception {
		if(modelRoot != null && keySimpleList != null && paquete != null) {
			
			List<IButton> beans = PaqueteFactory.getInstance().getObjects(paquete, IButton.class);
			modelRoot.put(keySimpleList, getSimpleList(beans,"subgrupos"));
		}
	}
	
	public void putButtonListGroupByGrupo(SimpleHash modelRoot, String keySimpleList, String subGroypKeyList, List<?> botones) throws Exception {
		if(modelRoot != null && keySimpleList != null && botones != null) {
			
			modelRoot.put(keySimpleList, getSimpleList((List<IButton>) botones, subGroypKeyList));
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private SimpleList getSimpleList(List<IButton> beans, String subGroypKeyList) throws Exception {
		VoSort.getInstance().sortByMethodValue(beans, "position", Integer.class, Order.Ascendente);
		
		Map<Object, List<IButton>> map = MapVoValues.groupBy(beans, "group");
		
		SimpleList lGrupos = new SimpleList();
		List<IButton> grupos = new ArrayList(map.keySet());
		if(grupos != null) {
			VoSort.getInstance().sortByMethodValue(grupos, "ordinal", Integer.class, Order.Ascendente);
			//VoSort.getInstance().sortByMethodValue(grupos, "position", Integer.class, Order.Ascendente);	
		}
		
		
		for(Object grupo : grupos) {
			SimpleHash hash = new SimpleHash();
			List<IButton> botones = map.get(grupo);
			
			SetHashAllFields.getInstance().setDataFromVo(hash, grupo);
			ButtonGroupTool.getIntance().escapeToolValues(hash, botones);
			
			SimpleList lista = FreemakerTool.getInstance().getListDataFromVo(botones, null, SetHashAllFields.getInstance());
			
			hash.put("size", String.valueOf(botones.size()));
			hash.put(subGroypKeyList, lista);
			
			lGrupos.add(hash);
		}
		
		return lGrupos;
	}
}
