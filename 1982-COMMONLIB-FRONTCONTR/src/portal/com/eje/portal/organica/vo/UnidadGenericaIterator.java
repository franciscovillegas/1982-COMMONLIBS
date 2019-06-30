package portal.com.eje.portal.organica.vo;

import portal.com.eje.portal.factory.Util;

public class UnidadGenericaIterator {
	private final int MAX_STACK = 250;
	public static UnidadGenericaIterator getInstance() {
		return Util.getInstance(UnidadGenericaIterator.class);
	}
  
	public void eachNode(IUnidadGenerica u, final INodeEach node) {
		if(u != null && node != null) {
			privateEachNode(u, node, 0);
		}
		
	}
	
	private void privateEachNode(IUnidadGenerica u, final INodeEach node, int pos) {
		if(u != null && node != null) {
			pos++;
			checkStackOverFlow(pos);
			node.each(u, pos);
			
			for(IUnidadGenerica child :  u.getChilds()) {
				privateEachNode(child, node, pos);
			}
		}
		
	}
	 
	
	private void checkStackOverFlow(int pos) {
		if(pos >= MAX_STACK) {
			throw new StackOverflowError("Ha sido supero el máximo de stacks");
		}
	}
}
