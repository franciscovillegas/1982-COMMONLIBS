package portal.com.eje.carpelect.mantenedor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Validar;

import portal.com.eje.carpelect.mgr.ManagerTrabajador;
import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.permiso.PerfilMngr;
import portal.com.eje.tools.portal.ToolOrganicaIO;
import portal.com.eje.carpelect.mgr.ManagerOrganica;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.output.JSarrayTableDataOut;
import cl.ejedigital.consultor.output.JSonDataOut;
import cl.ejedigital.consultor.output.jquerytable.JqueryTableTool;
import cl.ejedigital.tool.strings.MyString;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class Organica extends AbsClaseWeb {

	public Organica(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
	}

	@Override
	public void doPost() throws Exception {
		doGet();
		
	}
	
	@Override
	public void doGet() throws Exception {
		String accion = super.getIoClaseWeb().getParamString("accion","show");
		String thing  = super.getIoClaseWeb().getParamString("thing","");
		String htm	  = super.getIoClaseWeb().getParamString("htm","mantenedor/mantCalendar.html");
		
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("accion",accion);
		PerfilMngr perfil = new PerfilMngr();
		
		if("show".equals(accion)) {
			
		}
		else if("select".equals(accion)) {
			if("nodo".equals(thing)) {
				selectNodo();
			}else if("reportesGestion".equals(thing)) {
				selectGestDiaria();
			}
		}
	}

	private void selectNodo() {
		String id 	  = super.getIoClaseWeb().getParamString("id","-181-1-2-1-2");
		String name	  = super.getIoClaseWeb().getParamString("name","");
		String htm    = super.getIoClaseWeb().getParamString("htm","CarpetaElectronica/mantOrganicaUnidad.html");
		
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("id", id);
		modelRoot.put("name", name);
		
		super.getIoClaseWeb().retTemplate(htm, modelRoot);
	}


	private void selectGestDiaria() {
		String id 	  = super.getIoClaseWeb().getParamString("id","-181-1-2-1-2");
		String name	  = super.getIoClaseWeb().getParamString("name","");
		String htm    = super.getIoClaseWeb().getParamString("htm","informegestion/mantGestionDiaria.html");
		
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("id", id);
		modelRoot.put("name", name);
				
		super.getIoClaseWeb().retTemplate(htm, modelRoot);

	}

	
	
}
