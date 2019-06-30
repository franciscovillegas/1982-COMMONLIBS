package cl.eje.qsmcom.mantenedor;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.permiso.PerfilMngr;
import portal.com.eje.tools.excel.xlsx.Excel2010;
import portal.com.eje.tools.excel.xlsx.IExcel;
import cl.eje.qsmcom.managers.ManagerTrabajador;
import cl.eje.qsmcom.managers.ManagerUpload;
import cl.eje.qsmcom.tool.ExcelMappingTool;
import cl.eje.qsmcom.tool.IExcelMapping;
import cl.eje.qsmcom.tool.IExcelMappingAction;
import cl.eje.qsmcom.tool.PermisosQsmCom;
import cl.eje.qsmcom.upload.EjeFileUnicoTipoQSMCom;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.output.JSarrayDataOut;
import cl.ejedigital.consultor.output.JSarrayTableDataOut;
import cl.ejedigital.consultor.output.JSonDataOut;
import cl.ejedigital.consultor.output.jquerytable.JqueryTableTool;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.fileupload.FileService;
import freemarker.template.SimpleHash;

public class Trabajadores extends AbsClaseWeb {
	
	
	
	public Trabajadores(IOClaseWeb ioClaseWeb) {
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
		String htm	  = super.getIoClaseWeb().getParamString("htm","mantenedor/mantPersonas.html");
		
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("accion",accion);
		PerfilMngr perfil = new PerfilMngr();
		
		if("show".equals(accion)) {
			if("cargasmasivas".equals(thing)) {
				super.getIoClaseWeb().retTemplate("mantenedor/mantPersonas_cargasMasivas.html");
			}
			else {
				super.getIoClaseWeb().retTemplate(htm,modelRoot);		
			}
		}
		else if("select".equals(accion)) {
			if("trabajadores".equals(thing)) {
				selectTrabajadores();
			}
			else if("trabajadoresunidad".equals(thing)) {
				selectTrabajadoresUnidad();
			}
			else if ("cargasmasivas".equals(thing)) {
				selectCargasMasivas();
			}
			else if("".equals(thing)) {
				selectTrabajadores();
			}
		}
		else if("insert".equals(accion)) {
		
		}
		else if("update".equals(accion)) {
			if("contrasena".equals(thing)) {
				updateContrasena();
			}
			else if("trabajador".equals(thing)) {
				updateTrabajador();
			}
		}
		else if("delete".equals(accion)) {
			if("trabajador".equals(thing)) {
				deleteTrabajador();
			}
		}	
		else if("upload".equals(accion)) {
			if("trabajadores".equals(thing)) {
				uploadTrabajadores();
			}
		}
	}


	private void selectCargasMasivas() {		
		List<String> filtro = new ArrayList<String>();
		filtro.add("id_file");
		filtro.add("fecha_subida");
		filtro.add("rut_subida");
		filtro.add("nombre");
		filtro.add("name_original");
		filtro.add("bytes");		
		filtro.add("options");
		
		JSarrayTableDataOut out = new JSarrayTableDataOut(ManagerUpload.getInstance().getRegistroCargas(EjeFileUnicoTipoQSMCom.QMSCOM_TRABAJADOR));
		out.setFilter(filtro);
		
		JqueryTableTool table = new JqueryTableTool();		
		super.getIoClaseWeb().retTexto(table.getServerResponde(out,1).getListData());
	}

	private void selectTrabajadoresUnidad() {	
		String unidad 	  = super.getIoClaseWeb().getParamString("unidad","-1");
		
		JSarrayTableDataOut out = new JSarrayTableDataOut(ManagerTrabajador.getInstance().getTrabajadoresInUnidad(unidad));
		JqueryTableTool table = new JqueryTableTool();		
		super.getIoClaseWeb().retTexto(table.getServerResponde(out,1).getListData());
	}

	private void selectTrabajadores() {
		List<String> filtro = new ArrayList<String>();
		filtro.add("rut");
		filtro.add("digito_ver");
		filtro.add("nombres");
		filtro.add("ape_paterno");
		filtro.add("ape_materno");
		filtro.add("fecha_ingreso");
		filtro.add("options");
		
		JSarrayTableDataOut out = new JSarrayTableDataOut(ManagerTrabajador.getInstance().getTrabajadores());
		out.setFilter(filtro);
		JqueryTableTool table = new JqueryTableTool();		
		super.getIoClaseWeb().retTexto(table.getServerResponde(out,1).getListData());
	}
	
	private void updateContrasena() {
		int rut = super.getIoClaseWeb().getParamNum("rut", -1);
		String cActual = super.getIoClaseWeb().getParamString("pwa", "123");
		
		boolean bol = false;
		
		if(super.getIoClaseWeb().getUsuario().getRutId().equals(String.valueOf(rut)) || 
		   super.getIoClaseWeb().getUsuario().tieneApp(PermisosQsmCom.ADM_CLAVES.toString())) {
			
			if(ManagerTrabajador.getInstance().existeContrasena(rut)) {
				bol = ManagerTrabajador.getInstance().updateContrasena(super.getIoClaseWeb().getParamString("pw", "123"),rut);
			}
			else {
				bol = ManagerTrabajador.getInstance().insertContrasena(super.getIoClaseWeb().getParamString("pw", "123"),rut);
			}
		}
			
		
		
		super.getIoClaseWeb().retTexto(String.valueOf(bol)); 
	}
	
	private void deleteTrabajador() {
		super.getIoClaseWeb().retTexto(
				String.valueOf(
						ManagerTrabajador.getInstance().delTrabajador(
								super.getIoClaseWeb().getParamNum("rut", -1))));
	}
	
	private void updateTrabajador() {
		DataList lista = new DataList();
		DataFields campos = new DataFields();
		
		boolean ok = false;
		try {
			ok = ManagerTrabajador.getInstance().updateTrabajador(super.getIoClaseWeb().getParamNum("rut", -1),
					super.getIoClaseWeb().getParamNum("column", -1), 
					super.getIoClaseWeb().getParamString("value", ""));

			if(!ok){
				campos.put("err"	 , new Field("Error desconocido, no fue posible asignar el valor."));
			}
			else {
				campos.put("err"	 , new Field("-"));
			}
			campos.put("status"	 ,   new Field(ok));
		} catch (Exception e) {
			e.printStackTrace();
			campos.put("err"	 , new Field(e.getMessage()));
			campos.put("status"	 ,   new Field(false));
		}
		
		ConsultaData data = ManagerTrabajador.getInstance().getTrabajador(super.getIoClaseWeb().getParamNum("rut", -1));
		JSarrayDataOut outOld = new JSarrayDataOut(data);
		campos.put("newvalue", new Field(super.getIoClaseWeb().getParamString("value", "")));
		campos.put("column"  , new Field(super.getIoClaseWeb().getParamNum("column", -1)));
		campos.put("oldvalue", new Field(outOld));
		lista.add(campos);
		
		JSonDataOut out = new JSonDataOut(lista);
		super.getIoClaseWeb().retTexto(out.getListData()); 
	}
	
	private void uploadTrabajadores() {
	
		FileService file = new FileService(super.getIoClaseWeb().getServletContext());
		File f = super.getIoClaseWeb().getFile("Filedata");

		if(f != null && f.exists()) {
			int rut = Validar.getInstance().validarInt(super.getIoClaseWeb().getUsuario().getRutId(), 0);
			int addFile = file.addFile( rut,
										f,
										EjeFileUnicoTipoQSMCom.QMSCOM_TRABAJADOR);
			f = file.getFile(addFile);
			IExcel excel = new Excel2010(true);
			
			try {
				excel.loadFile(f,18);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println("Posiblemente la version del excel no sea XLSX. TRABAJADORES");
			}
			
			try {
				IExcelMapping excelMapping = ExcelTrabajador.getInstance();
				IExcelMappingAction excelAction = ExcelTrabajador.getInstance();
				
				ExcelMappingTool toolExcel = new ExcelMappingTool();
				toolExcel.fullUpdateToBd(excel, excelMapping, excelAction, false);
				
			}
			catch (Exception e) {
				System.out.println("Posiblemente la version del excel no sea XLSX. TRABAJADORES");
			}
			super.getIoClaseWeb().retTexto(String.valueOf(addFile)); 
		}
	}
}



