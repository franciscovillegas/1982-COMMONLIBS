package cl.eje.qsmcom.mantenedor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.permiso.PerfilMngr;
import portal.com.eje.tools.portal.ToolOrganicaIO;
import cl.eje.qsmcom.managers.ManagerOrganica;
import cl.eje.qsmcom.managers.ManagerQSM;
import cl.eje.qsmcom.managers.ManagerTrabajador;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.output.JSarrayTableDataOut;
import cl.ejedigital.consultor.output.JSonDataOut;
import cl.ejedigital.consultor.output.jquerytable.JqueryTableTool;
import cl.ejedigital.tool.strings.MyString;
import freemarker.template.SimpleHash;

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
			}
			if("reporte".equals(thing)) {
				selectReporte();
			}
		}
		else if("insert".equals(accion)) {
			if("nodo".equals(thing)) {
				insertNodo();
			}
		}
		else if("update".equals(accion)) {
			if("nodoname".equals(thing)) {
				updateNodoName();
			}
			else if("nodoid".equals(thing)) {
				updateNodoId();
			}
			else if("trabajadorEnUnidad".equals(thing)){
				addTrabajadorAUnidad();
			}
			else if ("jefeUnidad".equals(thing)) {	
				addJefeUnidad();
			}
			else if("moveNodo".equals(thing)) {
				moveNodo();
			}
		}
		else if("delete".equals(accion)) {
			if("nodo".equals(thing)) {
				deleteNodo();
			}
		}
		else if("upload".equals(accion)) {
			if("unidades".equals(thing)) {
				uploadUnidades();
			}
			else if("asociacion".equals(thing)) {
				uploadAsociacion();
			}
		}
		
	}

	private void moveNodo() {
		String nodoId = super.getIoClaseWeb().getParamString("nodeid", "-1");
		String newParentId = super.getIoClaseWeb().getParamString("newparentid", "-1");
		
		ToolOrganicaIO org = new ToolOrganicaIO();
		boolean ok = org.setNodoParent(nodoId, newParentId);
		
		DataFields fields = new DataFields();
		fields.put("estado", new Field(String.valueOf(ok)));
		DataList lista = new DataList();
		lista.add(fields);
		
		JSonDataOut out = new JSonDataOut(lista);
		super.getIoClaseWeb().retTexto(out.getListData());
	}

	private void selectReporte() {
		List<String> filtro = new ArrayList<String>();
		filtro.add("rut");
		filtro.add("digito ver");
		filtro.add("nombres");
		filtro.add("ape_materno");
		filtro.add("ape_paterno");
		filtro.add("unidad");
		filtro.add("unid_desc");
		filtro.add("unid_id_padre");
		filtro.add("encargado_unidad");
		
		ToolOrganicaIO org = new ToolOrganicaIO();
		
		JSarrayTableDataOut out = new JSarrayTableDataOut(org.getConsulta_DetalleTrabajadoresYUnidades());
		out.setFilter(filtro);
		
		JqueryTableTool table = new JqueryTableTool();		
		super.getIoClaseWeb().retTexto(table.getServerResponde(out,1).getListData());
		
	}

	private void uploadAsociacion() {
		File organica = super.getIoClaseWeb().getFile("Filedata");
		
		ToolOrganicaIO io = new ToolOrganicaIO();
		io.procesaAsociacion(organica, super.getIoClaseWeb().getUsuario(),false); 
	}

	private void uploadUnidades() {
		File organica = super.getIoClaseWeb().getFile("Filedata");
		
		ToolOrganicaIO io = new ToolOrganicaIO();
		io.procesaJerarquia(organica, super.getIoClaseWeb().getUsuario()); 
	}

	private void addJefeUnidad() {
		String id = super.getIoClaseWeb().getParamString("id","0");
		int value = super.getIoClaseWeb().getParamNum("value", -1);
		int rut	  = super.getIoClaseWeb().getParamNum("rut",1);
		
		DataList lista = new DataList();
		DataFields fields = new DataFields();
		boolean ok = true;
		
		if(value == 0) {
			ok = ManagerQSM.getInstance().deleteJefeUnidad(id,rut);
		}
		else {
			ok &= ManagerQSM.getInstance().insertJefeUnidad(rut,Integer.parseInt(super.getIoClaseWeb().getUsuario().getRutId()), id);
			
			if(!ok) {
				fields.put("mensaje", new Field("Error al asignar Jefe de unidad."));
			} 
			else {
				fields.put("mensaje", new Field("Jefe de unidad asignado correctamente."));
			}
			
			fields.put("estado", new Field(String.valueOf(ok)));
			
			lista.add(fields);
			
		}

		JSonDataOut out = new JSonDataOut(lista);
		super.getIoClaseWeb().retTexto(out.getListData());
		
	}

	private void addTrabajadorAUnidad() {
		String id = super.getIoClaseWeb().getParamString("id","0");
		int rut	  = super.getIoClaseWeb().getParamNum("rut",1);
		
		DataFields fields = new DataFields();
		
		//SE ELIMINA COMO DEJE DE UNIDAD ANTERIOR
		ManagerQSM.getInstance().deleteJefeUnidad(rut);
		
		boolean ok = ManagerTrabajador.getInstance().updateTrabajadorUnidad(rut,id);
		
		if(!ok) {
			fields.put("mensaje", new Field("Error al asignar la unidad."));
		} 
		else {
			fields.put("mensaje", new Field("Unidad asignada correctamente."));
		}
		
		fields.put("estado", new Field(String.valueOf(ok)));
		DataList lista = new DataList();
		lista.add(fields);
		
		JSonDataOut out = new JSonDataOut(lista);
		
		super.getIoClaseWeb().retTexto(out.getListData());
	}

	private void updateNodoName() {
		String id 	  = super.getIoClaseWeb().getParamString("id","-181-1-2-1-2");
		String name	  = super.getIoClaseWeb().getParamString("name","");
		DataFields fields = new DataFields();
		
		boolean ok = ManagerOrganica.getInstance().updateNodo(id, name);
		
		if(!ok) {
			fields.put("mensaje", new Field("Error al modificar la unidad."));
		} 
		else {
			fields.put("mensaje", new Field("Unidad modificada correctamente."));
		}
		
		fields.put("estado", new Field(String.valueOf(ok)));
		fields.put("name", new Field(name));	
		DataList lista = new DataList();
		lista.add(fields);
		
		JSonDataOut out = new JSonDataOut(lista);
		
		super.getIoClaseWeb().retTexto(out.getListData());
	}
	
	private void updateNodoId() {
		String id 	  = super.getIoClaseWeb().getParamString("id","-181-1-2-1-2");
		String newid  = super.getIoClaseWeb().getParamString("newid","");
		DataFields fields = new DataFields();
		
		
		boolean ok = false;
		
		if(newid != null && newid.trim().length() >= 1) {
			ok = ManagerOrganica.getInstance().updateNodoID(id, newid);
	
			if(!ok) {
				fields.put("mensaje", new Field("No fue posible modificar el ID, inténtalo nuevamente con otro ID."));
			} 
			else {
				fields.put("mensaje", new Field("Unidad modificada correctamente."));
			}
		}
		else {
			fields.put("mensaje", new Field("El <b>NUEVO ID</b> no es válido."));
		}
		
		fields.put("estado", new Field(String.valueOf(ok)));
		fields.put("newid", new Field(newid));
		DataList lista = new DataList();
		lista.add(fields);
		
		JSonDataOut out = new JSonDataOut(lista);
		
		super.getIoClaseWeb().retTexto(out.getListData());
	}

	private void selectNodo() {
		String id 	  = super.getIoClaseWeb().getParamString("id","-181-1-2-1-2");
		String name	  = super.getIoClaseWeb().getParamString("name","");
		String htm    = super.getIoClaseWeb().getParamString("htm","mantenedor/mantOrganicaUnidad.html");
		
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("id", id);
		modelRoot.put("name", name);
		
		super.getIoClaseWeb().retTemplate(htm, modelRoot);
	}

	private void deleteNodo() {
		String unidad = super.getIoClaseWeb().getParamString("id","-181-1-2-1-2");
		
		ConsultaData desc = ManagerOrganica.getInstance().getUnidadesDescendientes(unidad);
		
		while(desc.next()) {
			//BORRA JEFE DE UNIDAD
			unidad = desc.getString("unidad");
			ManagerQSM.getInstance().deleteJefeUnidad(unidad);
			
			ManagerOrganica.getInstance().delNodo(unidad);
			ManagerOrganica.getInstance().delUnidad(unidad);	
		}

		
	}

	private void insertNodo() {
		String unidadPadre = super.getIoClaseWeb().getParamString("idparent","-181-1-2-1-2");
		
		MyString my = new MyString();
		String newId = my.rellenaCadena(my.getRandomString("1234567890", 8),'0',8);
		
		while(ManagerOrganica.getInstance().existeNodo("unid_".concat(newId))) {
			newId = my.rellenaCadena(my.getRandomString("1234567890", 8),'0',8);
		}
		
		String newGlosa = "Nueva Unidad ".concat(newId).concat("");
		
		boolean ok = ManagerOrganica.getInstance().addNodo(unidadPadre, "unid_".concat(newId));
		ok &= ManagerOrganica.getInstance().addUnidad("unid_".concat(newId), newGlosa);
		
		DataFields fields = new DataFields();
		if(!ok) {
			ManagerOrganica.getInstance().delNodo(newId);
			ManagerOrganica.getInstance().delUnidad(newId);	
			fields.put("mensaje", new Field("Error al crear la unidad."));
		} 
		else {
			fields.put("mensaje", new Field("Unidad creada correctamente."));
		}
		
		fields.put("estado", new Field(String.valueOf(ok)));
		fields.put("label", new Field(newGlosa));
		fields.put("id", new Field("unid_".concat(newId)));
		
		DataList lista = new DataList();
		lista.add(fields);
		
		JSonDataOut out = new JSonDataOut(lista);
		
		super.getIoClaseWeb().retTexto(out.getListData());
		
	}



}
