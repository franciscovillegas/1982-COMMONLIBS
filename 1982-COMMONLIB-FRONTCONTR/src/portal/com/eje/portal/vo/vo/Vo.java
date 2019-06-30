package portal.com.eje.portal.vo.vo;

import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.vo.VoTool;
import portal.com.eje.tools.ClaseGenerica;

public class Vo implements Cloneable {

    public Object clone(){
        Object obj=null;
        try{
            obj=super.clone();
        }catch(CloneNotSupportedException ex){
            System.out.println(" no se puede duplicar");
        }
        return obj;
    }
    
    public Object copyTo(Vo vo) {
    	return VoTool.getInstance().copy(this, vo);
    }
    
    public <T> T  copyTo(Class<? extends Vo> voClass) {
    	ClaseGenerica cg = Util.getInstance(ClaseGenerica.class);
    	Vo o = (Vo)cg.getNewFromClass(voClass);
    	return VoTool.getInstance().copy(this, o);
    }
    
    public Object copyFrom(Vo vo) {
    	return VoTool.getInstance().copy(vo, this);
    }
}
