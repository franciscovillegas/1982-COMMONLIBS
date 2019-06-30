package portal.com.eje.tools.jqtree;

import portal.com.eje.frontcontroller.AbsClaseWebInsegura;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.tools.ClaseGenerica;
import cl.ejedigital.tool.misc.Cronometro;
 

public class JqTree extends AbsClaseWebInsegura{

	public JqTree(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doPost() throws Exception {
		doGet();
		
	}

	@Override
	public void doGet() throws Exception {
		Cronometro cro = new Cronometro();
		cro.Start();
		ClaseGenerica clase = new ClaseGenerica();
		
		String path = super.getIoClaseWeb().getParamString("ctree","");
		Object[] objetos = {super.getIoClaseWeb()};
		Class[] definicion= {IOClaseWeb.class};
		
		AJqTreeMaker tree = (AJqTreeMaker) clase.getNew(path, definicion, objetos);
		
		super.getIoClaseWeb().retTexto(tree.getTree().getJqValue().getListData());
		System.out.println(new StringBuilder(getClass().toString()).append(" :").append(cro.GetMilliseconds()));
	}

}
