package portal.com.eje.clave.formas;

import java.sql.SQLException;

import javax.mail.Message.RecipientType;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.correo.CorreoDispatcher;
import cl.ejedigital.tool.correo.CorreoProcessDefault;
import cl.ejedigital.tool.correo.ifaces.ICorreoBuilder;
import cl.ejedigital.tool.correo.ifaces.ICorreoProcess;
import cl.ejedigital.tool.correo.vo.VoDestinatario;
import cl.ejedigital.web.datos.ConsultaTool;
import freemarker.template.SimpleHash;
import portal.com.eje.clave.IRecuperarClave;
import portal.com.eje.clave.correo.CorreoClave;
import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;



public class RecuperarCajaChile extends AbsClaseWeb implements IRecuperarClave {

	public RecuperarCajaChile(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		// TODO Auto-generated constructor stub
	}

	public String getTemplate() {
		return "clave/recuperaClaveCajaChile.html";
	}
	
	public SimpleHash getSimpleHash() {
		SimpleHash hash = new SimpleHash();
		
		hash.put("claseweb",String.valueOf(this.getClass().getName()));
		return hash;
	}


	@Override
	public void doPost() throws Exception {
		doGet();
	}

	@Override
	public void doGet() throws Exception {
		
		if(super.getIoClaseWeb().getParamNum("rut",0) == 0) {
			super.getIoClaseWeb().retTemplate(this.getTemplate(),this.getSimpleHash());
		}
		else {
			ConsultaData agenciero = getAgenciero(super.getIoClaseWeb().getParamNum("rut",0));
			
			if(agenciero != null && agenciero.size() > 0) {
				VoDestinatario vo = new VoDestinatario(agenciero.getString("nombre"),agenciero.getString("correo"),RecipientType.TO);
				ICorreoBuilder correoData = new CorreoClave(agenciero.getString("password_usuario"),vo);
				ICorreoProcess correoProceso = new CorreoProcessDefault(correoData);
				CorreoDispatcher.getInstance().sendMail(correoProceso);
				
				SimpleHash hash = this.getSimpleHash();
				hash.put("msg","Correo enviado correctamente a :<b>".concat(agenciero.getString("password_usuario")).concat("</b>"));
				super.getIoClaseWeb().retTemplate(this.getTemplate(),hash);
			}
			else {
				SimpleHash hash = this.getSimpleHash();
				hash.put("msg","Lo sentimos, usted no es un agenciero o no ha inscrito correctamente su dirección de correo");
				super.getIoClaseWeb().retTemplate(this.getTemplate(),hash);
			}
		}
	}
	
	
	private ConsultaData getAgenciero(int rut) {
		String sql = "SELECT * FROM eje_cajachile_agencieros a inner join eje_ges_usuario u ON a.rut = u.rut_usuario  WHERE a.RUT = ? ";
		Object[] o = {rut};
		try {
			return ConsultaTool.getInstance().getData("portal",sql,o);
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}


}
