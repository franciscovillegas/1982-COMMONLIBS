package cl.eje.mail.modulos;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.mail.MessagingException;
import javax.mail.Message.RecipientType;
import javax.servlet.ServletException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.WordUtils;

import cl.eje.mail.managers.ManagerAcceso;
import cl.eje.mail.managers.ManagerTrabajador;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.output.JSonDataOut;
import cl.ejedigital.tool.correo.CorreoDispatcher;
import cl.ejedigital.tool.correo.ifaces.ICorreoBuilder;
import cl.ejedigital.tool.correo.ifaces.ICorreoProcess;
import cl.ejedigital.tool.correo.vo.IVoDestinatario;
import cl.ejedigital.tool.correo.vo.VoDestinatario;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.FreemakerTool;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.resobjects.ResourceHtml;
import portal.com.eje.permiso.PerfilMngr;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.correspondencia.CorrespondenciaBuilder;
import portal.com.eje.portal.correspondencia.CorrespondenciaLocator;
import portal.com.eje.portal.correspondencia.CorrespondenciaProgramacion;
import portal.com.eje.portal.correspondencia.CorrespondenciaProgramacion.ECorrespondenciaProgramacionTipo;
import portal.com.eje.portal.parametro.ParametroLocator;
import portal.com.eje.portal.parametro.ParametroValue;

public class RecuperacionPorDefault implements IRecuperaClave {

	@Override
	public void doGet(IOClaseWeb io) throws Exception {
		String accion = io.getParamString("accion","show");
		String thing  = io.getParamString("thing","");
		String htm	  = io.getParamString("htm","mantenedor/mantPersonas.html");
		
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("accion",accion);
		PerfilMngr perfil = new PerfilMngr();
		
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
		else if("help".equals(accion)) {
			if("pass".equals(thing)) {
				enviarCorreoAdministrador(io);
			}
		}
		else if("recovery".equals(accion)) {
			if("pass".equals(thing)) {
				recuperaClave(io);
			}
		}
		
	}


	private void enviarCorreoAdministrador(IOClaseWeb io) {
		String rutWD = io.getParamString("rut","11111111-1");
		
		int rut = Validar.getInstance().validarInt(rutWD.substring(0, rutWD.indexOf("-")),-1);
		String nuevoCorreo = io.getParamString("correo","No definido");
		String nuevoFono   = io.getParamString("fono","No definido");
		
		boolean ok = false;
		
		String eMail = ParametroLocator.getInstance().getValor("conf.mail.administrator", "mail").getValue();
		String Nombre =  ParametroLocator.getInstance().getValor("conf.mail.administrator", "nombre").getValue();
	
		ConsultaData data = ManagerAcceso.getInstance().getEjeGesUsuario(rut);
		if(data != null && data.next()) {
			String clave = data.getString("password_usuario");
			
			
			data = ManagerTrabajador.getInstance().getTrabajador(rut);
			if(data.next()) {
				ResourceHtml html = new ResourceHtml();
				Template template;
				try {
					template = html.getTemplate(io.getReq(), "mail/correoRecuperaClave_adviceAdministrator.html");
					
					ICorreoBuilder cb = new CorreoRecuperaClaveFallida(
							io,
							rutWD,
							template,
							Nombre,
							WordUtils.capitalizeFully(data.getString("nombre")),
							eMail,
							clave,
							nuevoCorreo,
							nuevoFono);
				
					ICorreoProcess cp = new CorreoProcessBase(cb);
					try {
						ok = CorreoDispatcher.getInstance().sendMail(cp);
					} catch (MessagingException e) {
						e.printStackTrace();
						ok = false;
					}
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ServletException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
					
				

			}
		}
		
		DataFields dataFields = new DataFields();

		dataFields.put("estado", new Field(ok));
		
		if(ok) {
			dataFields.put("msg", new Field("Solicitud recibida"));
			dataFields.put("nextstep", new Field(""));
		}
		else {
			dataFields.put("msg", new Field("La clave no pudo ser enviada. A continuación le pediremos algunos datos y una de nuestras ejecutivas se comunicará con usted para solucionar el problema."));
			dataFields.put("nextstep", new Field(""));
		}

		DataList dataList = new DataList();
		dataList.add(dataFields);
		
		JSonDataOut out = new JSonDataOut(dataList);
		io.retTexto(out.getListData());
	
	}
	
	
	private void recuperaClave(IOClaseWeb io) {
		
		String rutWD = io.getParamString("rut","11111111-1");
		String correoInput = io.getParamString("correo", null);
		String fonoInput = io.getParamString("fono", null);
		
		int rut = Validar.getInstance().validarInt(rutWD.substring(0, rutWD.indexOf("-")),-1);
		boolean ok = false;
		String eMail = null;
		StringBuffer destinatarios = new StringBuffer(); 
		
		ConsultaData data = ManagerAcceso.getInstance().getEjeGesUsuario(rut);
		if(data != null && data.next()) {
			String clave = data.getString("password_usuario");
			
			data = ManagerTrabajador.getInstance().getTrabajador(rut);
			if(data.next()) {
				eMail = data.getString("e_mail");
				
				
				
				if(eMail == null || eMail.length() <= 4) {
					ConsultaData data2 = ManagerTrabajador.getInstance().getConfirmacionCorreo(rut);
					
					if(data2.next()) {
						eMail = data2.getString("correo");
					}
				}
				
				ICorreoBuilder cb = new CorreoRecuperaClave(
						io,
						rutWD,
						data.getString("nombres"),
						eMail,
						clave);
				
				if(eMail != null && eMail.indexOf("@") != -1 && eMail.length() > 4) {
					CorrespondenciaBuilder cb2 = new CorrespondenciaBuilder(cb);
					Double idCorrespondencia = CorrespondenciaLocator.getInstance().sendCorreosNow(EModulos.getThisModulo(), cb2);
					
//					CorrespondenciaProgramacion programacion = new CorrespondenciaProgramacion(ECorrespondenciaProgramacionTipo.ejecucion_fechas, CorrespondenciaProgramacion.getNow());
//					cb2.addProgramacion(programacion);
//					Double idCorrespondencia = CorrespondenciaLocator.getInstance().addCorreo(io, cb2);
					
					List<IVoDestinatario> listaDest =  CorrespondenciaLocator.getInstance().getDestinatarios(idCorrespondencia);
					
					for(IVoDestinatario vo : listaDest) {
						if(vo != null) {
							if (vo.getTipo()!=RecipientType.BCC) {
								if(destinatarios.toString().length() > 0) {
									destinatarios.append(",");
								}
								destinatarios.append(StringEscapeUtils.escapeHtml(vo.getEmail()));
							}
						}
					}
					
//					CorrespondenciaLocator.getInstance().sendCorreosPendientes(io, idCorrespondencia);
					ok = true;
				} 
			}
		}
		
		DataFields dataFields = new DataFields();

		dataFields.put("estado", new Field(ok));
		
		if(ok) {
			dataFields.put("msg", new Field("Se envió la clave al siguiente correo  :  ".concat(destinatarios.toString())));
			dataFields.put("email", new Field(destinatarios.toString()));
			dataFields.put("nextstep", new Field(""));
		}
		else {
			ParametroValue pvGetCorreo = ParametroLocator.getInstance().getValor(EModulos.getThisModulo(),"login.credenciales","if_correo_not_valid_then_getcorreo");
			ParametroValue pvGetCell   = ParametroLocator.getInstance().getValor(EModulos.getThisModulo(),"login.credenciales","if_correo_not_valid_then_getcellphone");
			
			dataFields.put("if_correo_not_valid_then_getcorreo"		, pvGetCorreo.getValue());
			dataFields.put("if_correo_not_valid_then_getcellphone"	, pvGetCell.getValue());
			dataFields.put("estado"									, "false");
			dataFields.put("msg", new Field("La clave no pudo ser enviada."));
			dataFields.put("nextstep", new Field(""));
		}

		DataList dataList = new DataList();
		dataList.add(dataFields);
		
		JSonDataOut out = new JSonDataOut(dataList);
		io.retTexto(out.getListData());
	
	}
}






