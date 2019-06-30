package portal.com.eje.portal.organica.vo;

public class TreeGenerico<T> {
	protected IUnidadGenerica root;
	protected UnidadGenericaStyleDef style;

	public TreeGenerico(IUnidadGenerica root, UnidadGenericaStyleDef style) {
		super();
		this.root = root;
		this.style = style;
	}

	@SuppressWarnings("unchecked")
	public T getRoot() {
		return (T) root;
	}

	public UnidadGenericaStyleDef getStyle() {
		return style;
	}

	protected T grep( INodeFinder nodeFinder ) {
		T u = null;
		
		if(nodeFinder != null) {
			u = grepRecursivo(nodeFinder, root);
		}
		
		return (T) u;
	}
	
	private T grepRecursivo(INodeFinder nodeFinder, IUnidadGenerica node) {
		T buscado = null;
		
		if(nodeFinder.grep(node)) {
			buscado = (T) node;
		}
		else {
			 
			for(IUnidadGenerica child : node.getChilds()) {
				buscado = grepRecursivo(nodeFinder, child);
				if(buscado != null) {
					break;
				}
			}
			 
		}
		
		return buscado;
	}
}
