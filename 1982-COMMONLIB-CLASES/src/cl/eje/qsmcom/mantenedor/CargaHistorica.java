package cl.eje.qsmcom.mantenedor;

import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;
import cl.eje.qsmcom.managers.ManagerQSM;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.output.JSarrayTableDataOut;
import cl.ejedigital.consultor.output.jquerytable.JqueryTableTool;
import freemarker.template.SimpleHash;

public class CargaHistorica extends AbsClaseWeb {

	public CargaHistorica(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
	}

	@Override
	public void doGet() throws Exception {
		String accion = super.getIoClaseWeb().getParamString("accion","show");
		String thing  = super.getIoClaseWeb().getParamString("thing","");
		String htm	  = super.getIoClaseWeb().getParamString("htm","mantenedor/mantCargaHistorica.html");
		
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("accion",accion);
		
		if("show".equals(accion)) {
			showCargaHistoricas(htm, modelRoot);
		}
		else if("select".equals(accion)) {
			if("tablaCargasHistoricas".equals(thing)) {
				selectTablaCargasHistoricas();
			}
		}
		else if("insert".equals(accion)) {

		}
		else if("update".equals(accion)) {

		}
		else if("delete".equals(accion)) {

		}
		else if("upload".equals(accion)) {
			
		}
		
	}

	private void showCargaHistoricas(String htm, SimpleHash modelRoot) {
		super.getIoClaseWeb().retTemplate(htm,modelRoot);	
		
	}

	private void selectTablaCargasHistoricas() {
		ConsultaData dataProduccion = ManagerQSM.getInstance().getRegistroCargasHistoricas();
		JSarrayTableDataOut out = new JSarrayTableDataOut(dataProduccion);
		JqueryTableTool table = new JqueryTableTool();		
		
		super.getIoClaseWeb().retTexto(table.getServerResponde(out,1).getListData());	
	}



	@Override
	public void doPost() throws Exception {
		doGet();		
	}

}
