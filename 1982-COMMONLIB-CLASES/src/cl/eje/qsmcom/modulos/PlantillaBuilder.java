package cl.eje.qsmcom.modulos;

import intranet.com.eje.qsmcom.estructuras.Periodo;
import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.tools.ExtrasOrganica;
import cl.eje.qsmcom.helper.DatosManipulator;
import cl.eje.qsmcom.helper.IDatosManipulator;
import cl.eje.qsmcom.managers.ManagerQSM;
import cl.eje.qsmcom.tipo.TipoCarga;
import cl.eje.qsmcom.tool.TipoRegistro;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.FreemakerTool;
import cl.ejedigital.web.datos.ConsultaTool;
import freemarker.template.SimpleHash;

public class PlantillaBuilder extends AbsClaseWeb{

	public PlantillaBuilder(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doGet() throws Exception {
		String accion = super.getIoClaseWeb().getParamString("accion","show");
		String thing  = super.getIoClaseWeb().getParamString("thing","");
		int localRut = Integer.parseInt(super.getIoClaseWeb().getUsuario().getRutId());
		int rut = super.getIoClaseWeb().getParamNum("rut",localRut);
		
		if(super.getIoClaseWeb().getUsuario().tieneApp("mantcom") || localRut == rut || ExtrasOrganica.isEncargadoUnidad(localRut)) {
				if("show".equals(accion)) {
					if("thisPeriodo".equals(thing)) {
						showPlantillaThisPeriodo();
					}
					else if("hPeriodo".equals(thing)) {
						showPlantillaHistPeriodo();
					}
					else if("periodo".equals(thing)) {
						showPlantilla();
					}
				}
				else if("select".equals(accion)) {
		
				}
				else if("insert".equals(accion)) {
		
				}
				else if("update".equals(accion)) {
		
				}
				else if("delete".equals(accion)) {
		
				}
				else if("upload".equals(accion)) {
					
				}
				else if("download".equals(accion)) {
					if("registrosFromTicket".equals(thing)) {
						downloadPlantillaFromTicket();
					}
					else if("thisPeriodo".equals(thing)) {
						downloadPlantillaThisPeriodo();
					}
				}
				else if("valida".equals(accion)) {
					if("codigo".equals(thing)) {
		
					}
				}
		}
		else {
			super.getIoClaseWeb().retTexto("Sin permisos para visualizar.");
		}
	}

	private void showPlantillaHistPeriodo() {
		int localRut =  super.getIoClaseWeb().getParamNum("rut", -1);
		int periodo = super.getIoClaseWeb().getParamNum("periodo", -1);
		FreemakerTool fTool = new FreemakerTool();
		
		boolean ok = false;
		SimpleHash modelRoot = new SimpleHash();
		
		modelRoot.put("no_existe", "N/A");
		
		
		if(periodo != -1 && (super.getIoClaseWeb().getUsuario().tieneApp("adm") || 
							 super.getIoClaseWeb().getUsuario().tieneApp("jefe_unidad") || 
							 localRut == Validar.getInstance().validarInt(super.getIoClaseWeb().getUsuario().getRutId(),-1)    )) {
			try {
				/* PUT SOLO "EL" REGISTRO EN EL EXCEL */
				IDatosManipulator datos = new DatosManipulator();
				
				ConsultaData dataRegistros = datos.getLastRegistrosByRut_Periodo(periodo,localRut,TipoRegistro.normal);
				int lastSubidaInPeriodo = ManagerQSM.getInstance().getLastSubidaPorRut(periodo, localRut);
				ConsultaData dataSubida = ManagerQSM.getInstance().getSubida(lastSubidaInPeriodo);
				
				if(dataSubida.next()) {
					ConsultaData dataPlantilla = ManagerQSM.getInstance().getPlantilla(dataSubida.getInt("id_plantilla"));
					String conDetalle = ConsultaTool.getInstance().getFirsStringtValue(dataPlantilla, "con_detalle");
					PlantillaRegProcess regs = new PlantillaRegProcess();
					
					if("1".equals(conDetalle) || "true".equals(conDetalle)) {
						ConsultaData dataDetalle = datos.getLastRegistrosByRut_Periodo(periodo,localRut,TipoRegistro.detalle);
						super.getIoClaseWeb().retTexto(regs.procesaRegistros(modelRoot,dataDetalle, dataRegistros, dataPlantilla));
					}
					else {
						
						super.getIoClaseWeb().retTexto(regs.procesaRegistros(modelRoot, dataRegistros, dataPlantilla));
					}
				
					ok = true;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				ok = false;
			}			
		}
		
		
		if(!ok) {
			super.getIoClaseWeb().retTexto("Cod 100 : Error al buscar información.");
		}
		
	}
	
	private void showPlantilla() {
		int localRut =  super.getIoClaseWeb().getParamNum("rut", -1);
		int periodo = super.getIoClaseWeb().getParamNum("periodo", -1);
		int idSubida = super.getIoClaseWeb().getParamNum("idSubida", -1);
		FreemakerTool fTool = new FreemakerTool();
		
		boolean ok = false;
		SimpleHash modelRoot = new SimpleHash();
		
		modelRoot.put("no_existe", "N/A");
		
		
		if(periodo != -1 && (super.getIoClaseWeb().getUsuario().tieneApp("adm") || 
							 super.getIoClaseWeb().getUsuario().tieneApp("jefe_unidad") || 
							 localRut == Validar.getInstance().validarInt(super.getIoClaseWeb().getUsuario().getRutId(),-1)    )) {
			try {
				/* PUT SOLO "EL" REGISTRO EN EL EXCEL */
				IDatosManipulator datos = new DatosManipulator();
				
				ConsultaData dataRegistros = datos.getLastRegistrosByRut_Periodo(periodo,localRut,TipoRegistro.normal,idSubida);
				ConsultaData dataSubida = ManagerQSM.getInstance().getSubida(idSubida);
				
				if(dataSubida.next()) {
					ConsultaData dataPlantilla = ManagerQSM.getInstance().getPlantilla(dataSubida.getInt("id_plantilla"));
					String conDetalle = ConsultaTool.getInstance().getFirsStringtValue(dataPlantilla, "con_detalle");
					PlantillaRegProcess regs = new PlantillaRegProcess();
					
					if("1".equals(conDetalle) || "true".equals(conDetalle)) {
						ConsultaData dataDetalle = datos.getLastRegistrosByRut_Periodo(periodo,localRut,TipoRegistro.detalle);
						super.getIoClaseWeb().retTexto(regs.procesaRegistros(modelRoot,dataDetalle, dataRegistros, dataPlantilla));
					}
					else {
						
						super.getIoClaseWeb().retTexto(regs.procesaRegistros(modelRoot, dataRegistros, dataPlantilla));
					}
				
					
					
					
					
					ok = true;
				}
				
			
			}
			catch (Exception e) {
				e.printStackTrace();
				ok = false;
			}			
		}
		
		
		if(!ok) {
			super.getIoClaseWeb().retTexto("Cod 100 : Error al buscar información.");
		}
		
	}

	private void downloadPlantillaThisPeriodo() {
		// TODO Auto-generated method stub
		
	}

	private void downloadPlantillaFromTicket() {
		int idReq = super.getIoClaseWeb().getParamNum("idreq",-1);
		int localRut = ManagerQSM.getInstance().getRutResponsable_IdReq(idReq);
		int periodo = ManagerQSM.getInstance().getPeriodoValidoParaUnTicket(idReq);
		String css = super.getIoClaseWeb().getParamString("css","");
		
		
		boolean ok = false;
		SimpleHash modelRoot = new SimpleHash();
		
		modelRoot.put("no_existe", "N/A");
		
		
		if(periodo != -1 && (super.getIoClaseWeb().getUsuario().tieneApp("app_ges_com") ||
							 super.getIoClaseWeb().getUsuario().tieneApp("adm") 		|| 
							 super.getIoClaseWeb().getUsuario().tieneApp("jefe_unidad") || 
							 localRut == Validar.getInstance().validarInt(super.getIoClaseWeb().getUsuario().getRutId(),-1)    )) {
			try {
				/* PUT SOLO "EL" REGISTRO EN EL EXCEL */
				IDatosManipulator datos = new DatosManipulator();
				
				ConsultaData dataRegistros = datos.getLastRegistrosByRut_Periodo(periodo,localRut,TipoRegistro.normal);
				int lastSubidaInPeriodo = ManagerQSM.getInstance().getLastSubidaPorRut(periodo, localRut);
				ConsultaData dataSubida = ManagerQSM.getInstance().getSubida(lastSubidaInPeriodo);
				
				if(dataSubida.next()) {
					ConsultaData dataPlantilla = ManagerQSM.getInstance().getPlantilla(dataSubida.getInt("id_plantilla"));
					String conDetalle = ConsultaTool.getInstance().getFirsStringtValue(dataPlantilla, "con_detalle");
					PlantillaRegProcess regs = new PlantillaRegProcess();
					
					if("1".equals(conDetalle) || "true".equals(conDetalle)) {
						ConsultaData dataDetalle = datos.getLastRegistrosByRut_Periodo(periodo,localRut,TipoRegistro.detalle);
						super.getIoClaseWeb().retTexto(regs.procesaRegistros(modelRoot,dataDetalle, dataRegistros, dataPlantilla));
					}
					else {
						
						super.getIoClaseWeb().retTexto(regs.procesaRegistros(modelRoot, dataRegistros, dataPlantilla));
					}

					ok = true;
				}
				
			
			}
			catch (Exception e) {
				e.printStackTrace();
				ok = false;
			}			
		}
		
		
		if(!ok) {
			super.getIoClaseWeb().retTexto("Cod 100 : Error al buscar información.");
		}
		
	}

	private void showPlantillaThisPeriodo() {
		int localRut = Integer.parseInt(super.getIoClaseWeb().getUsuario().getRutId());
		int rut = super.getIoClaseWeb().getParamNum("rut",localRut);
		boolean ok = false;
		SimpleHash modelRoot = new SimpleHash();
		
		modelRoot.put("no_existe", "N/A");
		
		try {
			/* PUT SOLO "EL" REGISTRO EN EL EXCEL */
			IDatosManipulator datos = new DatosManipulator();
			ConsultaData dataRegistros = datos.getLastRegistrosByRut_PeriodoActual(rut,TipoRegistro.normal);			
			
			/*  DETALLE   */
			Periodo periodoObj = ManagerQSM.getInstance().getPeriodoValido(TipoCarga.calculo);
			ConsultaData dataPlantilla = null;
						
			if(periodoObj != null) {
				int periodo = periodoObj.getPeriodo();
				int lastSubidaInPeriodo = ManagerQSM.getInstance().getLastSubidaPorRut(periodo , rut);
				ConsultaData dataSubida = ManagerQSM.getInstance().getSubida(lastSubidaInPeriodo);
			
				if(dataSubida.next()) {
					dataPlantilla = ManagerQSM.getInstance().getPlantilla(dataSubida.getInt("id_plantilla"));
					String conDetalle = ConsultaTool.getInstance().getFirsStringtValue(dataPlantilla, "con_detalle");
					PlantillaRegProcess regs = new PlantillaRegProcess();
					
					if("1".equals(conDetalle) || "true".equals(conDetalle)) {
						ConsultaData dataDetalle = datos.getLastRegistrosByRut_Periodo(periodo,localRut,TipoRegistro.detalle);
						super.getIoClaseWeb().retTexto(regs.procesaRegistros(modelRoot,dataDetalle, dataRegistros, dataPlantilla));
					}
					else {
						
						super.getIoClaseWeb().retTexto(regs.procesaRegistros(modelRoot, dataRegistros, dataPlantilla));
					}
				
					ok = true;
				}
			}
			
			ok = true;
		}
		catch (Exception e) {
			e.printStackTrace();
			ok = false;
		}
		
		if(!ok) {
			super.getIoClaseWeb().retTexto("Cod 100 : Error al buscar información.");
		}
		
	}

	@Override
	public void doPost() throws Exception {
		doGet();
		
	}
	
	
}
