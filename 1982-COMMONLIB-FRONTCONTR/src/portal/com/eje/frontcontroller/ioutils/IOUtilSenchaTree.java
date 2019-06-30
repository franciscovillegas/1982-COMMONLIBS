package portal.com.eje.frontcontroller.ioutils;

import org.springframework.util.Assert;

import cl.ejedigital.consultor.ConsultaData;
import portal.com.eje.frontcontroller.IIOClaseWebLight;
import portal.com.eje.frontcontroller.IIOSenchaJsonTree;
import portal.com.eje.frontcontroller.IIOSenchaJsonTreeNodeListener;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.IOSenchaJsonTree;
import portal.com.eje.frontcontroller.SenchaTreeType;
import portal.com.eje.frontcontroller.ioutils.sencha.util.RetornoSenchaManipulator;
import portal.com.eje.portal.factory.Weak;
import portal.com.eje.portal.organica.OrganicaLocator;
import portal.com.eje.portal.organica.ifaces.IUnidadDependency;
import portal.com.eje.portal.organica.vo.IUnidadGenerica;
import portal.com.eje.portal.organica.vo.UnidadGenericaStyleDef;

public class IOUtilSenchaTree extends IOUtil {
 
	public static IOUtilSenchaTree getIntance() {
		return Weak.getInstance(IOUtilSenchaTree.class);
	}
	/**
	 *	Se debe construir a través del getInstance()
	 * @deprecated
	 * */
	public IOUtilSenchaTree() {
		
	}
	
	public IIOSenchaJsonTree getSenchaJsonTree(IIOClaseWebLight io) {
		return new IOSenchaJsonTree(io);
	}
	
	private void manipulaEscapeTree(IIOClaseWebLight io, IUnidadGenerica unidad) {
		if(io != null && unidad != null) {
			RetornoSenchaManipulator.getInstance().setUtilidad(unidad, (IOClaseWeb) io);		
		}
	}
	
	public void retSenchaTree(IIOClaseWebLight io, IUnidadGenerica unidad) {
		manipulaEscapeTree(  io, unidad);
		
		IIOSenchaJsonTree tree = getSenchaJsonTree(io);
		tree.retSenchaTree(unidad, null);	
	}
	
	public void retSenchaTree(IIOClaseWebLight io, Object unidadObject, UnidadGenericaStyleDef style) {		
		IUnidadGenerica unidad = null;
		if(unidadObject != null ) {
			Assert.isAssignable(IUnidadGenerica.class, unidadObject.getClass());
			
			unidad = (IUnidadGenerica) unidadObject;
		}
		
		manipulaEscapeTree(  io, unidad);
		IIOSenchaJsonTree tree = getSenchaJsonTree(io);
		tree.retSenchaTree(unidad, style);
	}
	
	/**
	 * Genera una orgánica según las caracteristicas del usuario logeado<br/>
	 * 	<ul>
	 * 		<li>Si es jefatura; se mostrarán todas las unidades descendientes</li>
	 * 		<li>Si no lo es; solo se mostrará la unidad</li>
	 *  </ul>
	 * */
	
	public void retSenchaTree_miEstructura(IIOClaseWebLight io, UnidadGenericaStyleDef u) {
		
		String unidad = null;
		ConsultaData data = null;
		
		if(io.getUsuario().isJefeUnidad()) {
			data = OrganicaLocator.getInstance().getUnidad(io.getUsuario().getIdUnidad());
		}
		else {
			data = OrganicaLocator.getInstance().getUnidad(io.getUsuario().getIdUnidad());
		}
		
		if(data != null && data.next()) {
			unidad= data.getForcedString("unid_id");
		}
		
		retSenchaTreeFromUnidad(io, unidad,u);
	}
	
	
	/**
	 * Genera una orgánica según los la unidad que se entregue y el estilos de los nodos
	 * 
	 * */
	
	public void retSenchaTreeFromUnidad(IIOClaseWebLight io,String unidad) {
		IIOSenchaJsonTree tree = getSenchaJsonTree(io);
		Object[] oUnidad = {unidad};
		tree.retSenchaTreeFromUnidad(oUnidad, SenchaTreeType.normal.getStyle());
	}
	

	/**
	 * Genera una orgánica según los la unidad que se entregue y el estilos de los nodos
	 * 
	 * */
	public void retSenchaTreeFromUnidad(IIOClaseWebLight io, String unidad, UnidadGenericaStyleDef style) {
		IIOSenchaJsonTree tree = getSenchaJsonTree(io);
		Object[] oUnidad = {unidad};
		tree.retSenchaTreeFromUnidad(oUnidad, style);
	}
	
 
	
	/**
	 * Genera una orgánica de la compañía completa
	 * 
	 * */
	public void retSenchaTreeAllCompanies(IIOClaseWebLight io) {
		retSenchaTreeAllCompanies(io, (IIOSenchaJsonTreeNodeListener)null);
	}
	
	/**
	 * Genera una orgánica de la compañía completa
	 * 
	 * */
	public void retSenchaTreeAllCompanies(IIOClaseWebLight io, IIOSenchaJsonTreeNodeListener nd) {
		IIOSenchaJsonTree tree = getSenchaJsonTree(io);
		tree.setNodeListener(nd);
		tree.retSenchaTreeAllCompanies(SenchaTreeType.normal.getStyle());
	}
	
	/**
	 * Genera una orgánica de la compañía completa
	 * 
	 * */
	public void retSenchaTreeAllCompanies(IIOClaseWebLight io, UnidadGenericaStyleDef style) {
		IIOSenchaJsonTree tree = getSenchaJsonTree(io);
		tree.retSenchaTreeAllCompanies(style);
	}
	
	
	/**
	 * Genera una orgánica según los la unidad que se entregue y el estilos de los nodos
	 * 
	 * */
	public void retSenchaTreeFromUnidad(IIOClaseWebLight io, Object[] unidades) {
		IIOSenchaJsonTree tree = getSenchaJsonTree(io);
		tree.retSenchaTreeFromUnidad(unidades, SenchaTreeType.normal.getStyle());
	}
	
	/**
	 * Genera una orgánica según los la unidad que se entregue y el estilos de los nodos
	 * 
	 * */
	public void retSenchaTreeFromUnidad(IIOClaseWebLight io, Object[] unidades, UnidadGenericaStyleDef defUnidad) {
		IIOSenchaJsonTree tree = getSenchaJsonTree(io);
		tree.retSenchaTreeFromUnidad(unidades, defUnidad);
	}

	public void retSenchaTreeFromUnidad(IIOClaseWebLight io, Object[] unidades, UnidadGenericaStyleDef defUnidad, IUnidadDependency dependencia) {
		IIOSenchaJsonTree tree = getSenchaJsonTree(io);
		tree.retSenchaTreeFromUnidad(unidades, defUnidad, dependencia);
	}
	
}
