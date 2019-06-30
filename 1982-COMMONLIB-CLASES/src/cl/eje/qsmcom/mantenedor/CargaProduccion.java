package cl.eje.qsmcom.mantenedor;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.permiso.PerfilMngr;
import cl.eje.qsmcom.helper.DatosManipulator;
import cl.eje.qsmcom.helper.IDatosManipulator;
import cl.eje.qsmcom.managers.ManagerQSM;
import cl.eje.qsmcom.tipo.TipoCarga;
import cl.eje.qsmcom.upload.EjeFileUnicoTipoQSMCom;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.output.JSonDataOut;
import cl.ejedigital.tool.misc.Cronometro;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.FreemakerTool;
import cl.ejedigital.web.fileupload.FileService;
import freemarker.template.SimpleHash;

public class CargaProduccion extends AbsClaseWeb {

	public CargaProduccion(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
	}

	@Override
	public void doGet() throws Exception {
		String accion = super.getIoClaseWeb().getParamString("accion","show");
		String thing  = super.getIoClaseWeb().getParamString("thing","");
		String htm	  = super.getIoClaseWeb().getParamString("htm","mantenedor/mantCargaProduccion.html");
		
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("accion",accion);
		PerfilMngr perfil = new PerfilMngr();
		
		if("show".equals(accion)) {
			showCargaProduccion(htm, modelRoot);
		}
		else if("select".equals(accion)) {
			if("paso2".equals(thing)) {
				selectPlantillas();
			}
			else if("historia".equals(thing)) {
				selectCargaHistorica();
			}
		}
		else if("insert".equals(accion)) {

		}
		else if("update".equals(accion)) {

		}
		else if("delete".equals(accion)) {

		}
		else if("upload".equals(accion)) {
			if("produccion".equals(thing)) {
				
				int newIdSubida = uploadProduccion();
				super.getIoClaseWeb().retTexto(String.valueOf(procesaProduccion(newIdSubida)));
			}
		}
		
	}
	
	private void selectCargaHistorica() {
		List<String> filtro = new ArrayList<String>();
		filtro.add("fecha");
		filtro.add("nombre");
		filtro.add("rutsCargados");
		filtro.add("cant_regs");
		
		int idPeriodo   = super.getIoClaseWeb().getParamNum("idPeriodo", 0);
		int idPlantilla = super.getIoClaseWeb().getParamNum("idPlantilla", 0);
		
		ConsultaData dataProduccion = ManagerQSM.getInstance().getRegistroCargasHistoricas(idPeriodo, idPlantilla);
		
		JSonDataOut out2 = new JSonDataOut(dataProduccion);
		out2.setFilter(filtro);
		
		super.getIoClaseWeb().retTexto("["+out2.getListData()+"]");	
		
	}

	private boolean procesaProduccion( int newIdSubida ) {
		int idPlantilla = super.getIoClaseWeb().getParamNum("idPlantilla", 0);
		
		ConsultaData data = ManagerQSM.getInstance().getSubida(newIdSubida);
		boolean ok = false;
		
		if(data.next()){
			Cronometro cro = new Cronometro();
			cro.Start();
			
			try {
				IDatosManipulator manip = new DatosManipulator();
				manip.addRegistros(super.getIoClaseWeb().getServletContext(),idPlantilla, data.getInt("periodo"), newIdSubida);
			}
			catch (Exception e) {
				System.out.println("Posiblemente la version del excel no sea XLSX. PRODUCCION");
			}
			
			ManagerQSM.getInstance().updateTrackeoDato(newIdSubida, "tiempo_proceso", cro.GetMilliseconds());
		}
		
		
		return ok;
	}

	private int uploadProduccion() {
		FileService file = new FileService(super.getIoClaseWeb().getServletContext());
		File f = super.getIoClaseWeb().getFile("Filedata");
		int newReg  = -1;
		
		if(f != null && f.exists()) {
			int rut = Validar.getInstance().validarInt(super.getIoClaseWeb().getUsuario().getRutId(), 0);
			int idPlantilla = super.getIoClaseWeb().getParamNum("idPlantilla", 0);
			
			int addFile =  file.addFile( rut,
										f,
										EjeFileUnicoTipoQSMCom.QMSCOM_PRODUCCION);
						
			Connection mac = super.getIoClaseWeb().getConnection("mac");

			try {
				mac.setAutoCommit(false);
				int periodo = super.getIoClaseWeb().getParamNum("periodo", 0);
				newReg = ManagerQSM.getInstance().addTrackeoDato(mac, rut,periodo,addFile,idPlantilla);
				
				boolean ok = newReg > 0;
				if(ok) {
					mac.commit();
				}
				else {
					mac.rollback();
					addFile = -1;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				try {
					if(mac != null) {
						mac.setAutoCommit(true);
					}
					
					super.getIoClaseWeb().freeConnection("mac", mac);
				} catch (SQLException e) {
					e.printStackTrace();
					addFile = -1;
				}
			}

			
			super.getIoClaseWeb().retTexto(String.valueOf(addFile)); 
		}
		
		return newReg;
		
	}

	private void selectPlantillas() {
		ConsultaData data = ManagerQSM.getInstance().getPlantilla(100);
		JSonDataOut out = new JSonDataOut(data);
		
		List<String> filtro = new ArrayList<String>();
		filtro.add("id_plantilla");
		filtro.add("nombre");
		out.setFilter(filtro);

		super.getIoClaseWeb().retTexto("[" + out.getListData() + "]");
		
	}

	private void showCargaProduccion(String htm, SimpleHash modelRoot) {
		ConsultaData data = ManagerQSM.getInstance().getProcesos(TipoCarga.produccion);
		FreemakerTool tool = new FreemakerTool();
		
		modelRoot.put("procesos", tool.getListData(data));
		super.getIoClaseWeb().retTemplate(htm,modelRoot);		
	}



	@Override
	public void doPost() throws Exception {
		doGet();		
	}

}
