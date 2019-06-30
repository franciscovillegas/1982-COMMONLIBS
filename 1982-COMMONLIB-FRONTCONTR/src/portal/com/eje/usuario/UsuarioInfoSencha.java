package portal.com.eje.usuario;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.output.IDataOut;
import cl.ejedigital.consultor.output.JSarrayDataOut;
import cl.ejedigital.consultor.output.JSonDataOut;
import cl.ejedigital.web.datos.ConsultaTool;

import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.AbsClaseWebInsegura;
import portal.com.eje.frontcontroller.IOClaseWeb;

public class UsuarioInfoSencha extends AbsClaseWebInsegura implements Serializable{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5165179122818425652L;

	public UsuarioInfoSencha(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
	}

	@Override
	public void doPost() throws Exception {
		doGet();
		
	}

	@Override
	public void doGet() throws Exception {
		super.getIoClaseWeb().retSenchaJson(getDatosUsuario(), true);
	}
	
	public DataList getDatosUsuario() {
		DataList data = new DataList();
		DataFields campos = new DataFields();
		
		if( super.getIoClaseWeb().getUsuario().getName() != null) {
			campos.put("nombre" , new Field(super.getIoClaseWeb().getUsuario().getName().toLowerCase()) );	
		}
		else {
			campos.put("nombre" , new Field("No Definido") );	
		}
		
		campos.put("empresa", new Field(super.getIoClaseWeb().getUsuario().getEmpresa()) );
		campos.put("cargodesc", new Field(super.getIoClaseWeb().getUsuario().getCargoDesc().toLowerCase()) );
		campos.put("privilegios", new Field(getApps()) );
		campos.put("rut", new Field(super.getIoClaseWeb().getUsuario().getRutId()) );
		
		if( super.getIoClaseWeb().getUsuario().getRut() != null) {
			campos.put("digito_ver", new Field(super.getIoClaseWeb().getUsuario().getRut().getDig()) );
		}
		else {
			campos.put("digito_ver", new Field("0") );
		}
		campos.put("clave", new Field(super.getIoClaseWeb().getUsuario().getPassWord()) );
		campos.put("mail", new Field(super.getIoClaseWeb().getUsuario().getEmail()) );
		campos.put("mailValidado", new Field(super.getIoClaseWeb().getUsuario().getEmailValidado()) );
		campos.put("cantidadUnidadesDescendientes", new Field(super.getIoClaseWeb().getUsuario().getUnidadesDescendientes().length) );
		campos.put("cantIngresos", new Field(super.getIoClaseWeb().getUsuario().getCantIngreso()) );
		campos.put("claveUltCambio", new Field(getUltCambioClave()) );
		campos.put("valido", new Field(super.getIoClaseWeb().getUsuario().esValido()) );
		
		data.add(campos);
		
		return data;
	}
	
	private IDataOut getApps() {
		List<String> lista = super.getIoClaseWeb().getUsuario().getApps();
		DataList data = new DataList();
		DataFields campos = null;
		
		for(String s : lista) {
			campos = new DataFields();
			campos.put("app", new Field(s));
			
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
	
	private String getUltCambioClave() {
		String sql = "select passw_ult_cambio from eje_ges_usuario  where rut_usuario = ? ";
		String retorno = null;
		Object[] params = { super.getIoClaseWeb().getUsuario().getRutId() };
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params );
			
			if(data.next()) {
				Date date = data.getDateJava("passw_ult_cambio");
				if(date != null) {
					SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
					retorno = format.format(date);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return retorno;
	}

}
