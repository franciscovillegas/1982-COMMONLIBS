package portal.com.eje.usuario;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.output.IDataOut;
import cl.ejedigital.consultor.output.JSarrayDataOut;
import cl.ejedigital.consultor.output.JSonDataOut;
import cl.ejedigital.web.datos.ConsultaTool;

public class UsuarioInfoCalidad extends AbsClaseWeb implements Serializable{

	private static final long serialVersionUID = -5165179122818425652L;

	public UsuarioInfoCalidad(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
	}

	@Override
	public void doPost() throws Exception {
		doGet();
	}

	@Override
	public void doGet() throws Exception {
		String methodOut = super.getIoClaseWeb().getParamString("methodOut", null);
		if( methodOut == null) {
			JSonDataOut out = new JSonDataOut(getDatosUsuario());
			super.getIoClaseWeb().retTexto(out.getListData());
		}
		else if( "sencha".equals(methodOut)) {
			super.getIoClaseWeb().retSenchaJson(getDatosUsuario(), true);
		}
	}
	
	public DataList getDatosUsuario() {
		DataList data = new DataList();
		DataFields campos = new DataFields();
		campos.put("usuarios", new Field(getApps()) );
		data.add(campos);
		return data;
	}
	
	private IDataOut getApps() {
		List<String> lista = getRuts();
		DataList data = new DataList();
		DataFields campos = null;
		for(String s : lista) {
			campos = new DataFields();
			campos.put("rut", new Field(s));
			data.add(campos);
		}
		JSonDataOut out = new JSonDataOut(data);
		DataList data2 = new DataList();
		DataFields campos2 = new DataFields();
		campos2.put("out", new Field(out));
		data2.add(campos2);
		JSarrayDataOut outJS = new JSarrayDataOut(data2);
		return outJS;
	}

	public List<String> getRuts() {
		StringBuilder strConsulta = new StringBuilder();
		strConsulta.append(" select rut from eje_ges_trabajador_calidad WHERE vigente = 1");
		List<String> lista = new ArrayList<String>();
		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal",strConsulta.toString(),null);
			for(;data.next();) {
				lista.add(data.getForcedString("rut"));
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return lista;
	}

}