package portal.com.eje.liquidadic;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.FreemakerTool;
import freemarker.template.SimpleHash;
import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;

public class SAdmLiqAdicional extends AbsClaseWeb {

	public SAdmLiqAdicional(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doPost() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doGet() throws Exception {
		
		String accion = super.getIoClaseWeb().getParamString("accion", null);
		
		if("guardar".equals( accion ) ) {
			save();
		}
		else if("creartablas".equals( accion )) {
			crearTablasNecesarias();
		}
		else {
			retTemplate();
		}
		
	}
	
	private void retTemplate() {
		SimpleHash modelRoot = new SimpleHash();
		
		
		FreemakerTool f = new FreemakerTool();
		if(ManagerLiqAdicional.getInstance().existeTablaLiquidacionAdicional()) {
			ConsultaData data = ManagerLiqAdicional.getInstance().getAllProcesos();
			modelRoot.put("allProcesos", f.getListData(data) ) ;
			
			ConsultaData dataSeleccion = ManagerLiqAdicional.getInstance().getAllProcesosSeleccionados();
			modelRoot.put("procesosSeleccionados", f.getListData(dataSeleccion) ) ;
		}
		else {
			modelRoot.put("sintablas","true");
		}
		
		super.getIoClaseWeb().retTemplate("liqadicional/conf_liqadicional.htm", modelRoot);
	}
	
	private void save() {
		String[] tipos = super.getIoClaseWeb().getParamString("tipos", "").split("\\,");
		boolean ok = true;
		
		 ManagerLiqAdicional.getInstance().setEstadoAllProcesos(false);
		
		for(String tipo : tipos) {
			if(tipo != null && tipo.length() > 0) {
				ConsultaData data = ManagerLiqAdicional.getInstance().getProceso(tipo);
				
				if(data != null && data.size() > 0) {
					ok = ok && ManagerLiqAdicional.getInstance().setEstadoUnProceso(tipo, true);
				}
				else {
					ok = ok && ManagerLiqAdicional.getInstance().insertProceso(tipo);
				}
			}
		}
		

		super.getIoClaseWeb().retTexto(String.valueOf(ok));

	}
	
	private void crearTablasNecesarias() {
		ManagerLiqAdicional.getInstance().crearTablasNecesarias();
		
		String valor = String.valueOf(ManagerLiqAdicional.getInstance().existeTablaLiquidacionAdicional());
		super.getIoClaseWeb().retTexto(valor);
	}

}
