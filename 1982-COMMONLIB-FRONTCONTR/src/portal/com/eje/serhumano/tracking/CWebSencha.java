package portal.com.eje.serhumano.tracking;

import java.sql.SQLException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.output.JSonDataOut;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.frontcontroller.AbsClaseWebInsegura;
import portal.com.eje.frontcontroller.IOClaseWeb;

public class CWebSencha extends AbsClaseWebInsegura {

	public CWebSencha(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doPost() throws Exception {
		
		String fi = super.getIoClaseWeb().getParamString("fecha_inicio","-1");
		String ft = super.getIoClaseWeb().getParamString("fecha_termino","-1");
		
		try {
		StringBuilder str = new StringBuilder();
		str.append(" select f.descripcion  as name, count(distinct rut) as data1 ");
		str.append(" from eje_ges_tracking t inner join eje_ges_tracking_func f ");
		str.append(" 	on t.direc_rel = f.url ");
		str.append(" where t.fecha Between '").append(fi).append("' and '").append(ft).append("'");
		str.append(" group by f.descripcion  ");
		
        ConsultaData data = ConsultaTool.getInstance().getData("portal", str.toString());
		
		
		JSonDataOut out = new JSonDataOut(data);
		
		super.getIoClaseWeb().retTexto("[" + out.getListData() + "]");
		
		}
		catch (SQLException e) {
			e.printStackTrace();	
		}
	}

	@Override
	public void doGet() throws Exception {
		doPost();
		
	}

}
