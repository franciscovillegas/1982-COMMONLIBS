package cl.eje.qsmcom.modulos;

import java.net.URLDecoder;
import java.net.URLEncoder;

import cl.eje.qsmcom.managers.ManagerTrabajador;
import cl.ejedigital.consultor.ConsultaData;
import freemarker.template.SimpleHash;
import portal.com.eje.frontcontroller.AbsClaseWebInsegura;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.tools.security.Encrypter;

public class ValidaCorreo extends AbsClaseWebInsegura {

	public ValidaCorreo(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doPost() throws Exception {
		doGet();
		
	}

	@Override
	public void doGet() throws Exception {
		String accion = super.getIoClaseWeb().getParamString("accion","show");
		String thing  = super.getIoClaseWeb().getParamString("thing","");

				
		if("show".equals(accion)) {

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
		else if("valida".equals(accion)) {
			if("codigo".equals(thing)) {
				validaCodigo();
			}
		}
		
		
	}

	private void validaCodigo() {
		boolean ok = false;
		
		try {
			
			String codigoEnc = super.getIoClaseWeb().getParamString("c","");
			Encrypter e = new Encrypter();
			String codigo = URLDecoder.decode(e.decrypt(codigoEnc), "ISO-8859-1");
			String[] datos = codigo.split("\\|");
			
			int rut = Integer.parseInt(datos[0]);
			ConsultaData dataConfirma 	= ManagerTrabajador.getInstance().getConfirmacionCorreo(rut,datos[1]);
			ConsultaData dataTrabajador = ManagerTrabajador.getInstance().getTrabajador(rut);
			
			
			if(dataConfirma == null || !dataConfirma.next()) {
				if(dataTrabajador != null && dataTrabajador.next()) {
					String mail = dataTrabajador.getString("mail");
					
					if(mail != null && mail.equals(datos[1])) {
						ok = ManagerTrabajador.getInstance().insertConfirmacion(rut,datos[1]);
					}
				}
			}
		

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		SimpleHash modelRoot = new SimpleHash();
		
		if(ok) {
			modelRoot.put("mensaje", "Validacion de correo realizada correctamente, ahora ingrese con su usuario clave.");
		}
		else {
			modelRoot.put("mensaje", "El código que está ingresando no es válido.");
		}
		
		String url = super.getIoClaseWeb().getReq().getRequestURL().toString();
		url = url.substring(0, url.lastIndexOf("/"));
		url = url.substring(0, url.lastIndexOf("/"));
		
		modelRoot.put("servidor", url);
		
		super.getIoClaseWeb().retTemplate("qsmcorreo/correoValidacionMensaje.html", modelRoot);
	}

}
