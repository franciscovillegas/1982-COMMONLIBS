package portal.com.eje.serhumano.saludos.admin;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.servlet.ServletException;

import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.resobjects.ResourceHtml;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.correo.CorreoDispatcher;
import cl.ejedigital.tool.correo.CorreoProcessPropertie;
import cl.ejedigital.tool.correo.ifaces.ICorreoBuilder;
import cl.ejedigital.tool.correo.ifaces.ICorreoProcess;
import cl.ejedigital.tool.correo.vo.IVoDestinatario;
import cl.ejedigital.tool.correo.vo.VoDestinatario;
import cl.ejedigital.web.FreemakerTool;
import cl.ejedigital.web.fileupload.FileService;
import cl.ejedigital.web.fileupload.vo.EjeFileUnicoTipo;
import freemarker.template.SimpleHash;
import freemarker.template.Template;

public class Saludos extends AbsClaseWeb{

	public Saludos(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
	}

	@Override
	public void doPost() throws Exception {
		doGet();
		
	}

	@Override
	public void doGet() throws Exception {
		mainCall();
	}

	private void mainCall(){
		String accion = super.getIoClaseWeb().getParamString("accion","show");
		String thing  = super.getIoClaseWeb().getParamString("thing","");
		String htm	  = super.getIoClaseWeb().getParamString("htm","saludos/mainAdmin.html");
		
		SimpleHash modelRoot = new SimpleHash();
		FreemakerTool tool = new FreemakerTool();
		modelRoot.put("accion",accion);

		if("show".equals(accion)) {
			if("admPlantillas".equals(thing)) {
				super.getIoClaseWeb().retTemplate("saludos/admPlantillas.html");
			}
			else if("progSaludos".equals(thing)) {
				 super.getIoClaseWeb().retTemplate("saludos/progSaludos.html");
			}
			
			else if("programaciones".equals(thing)) {
				super.getIoClaseWeb().retTemplate("saludos/programados.html");
			}
			else {
				super.getIoClaseWeb().retTemplate(htm,modelRoot);		
			}
		}

		else if("select".equals(accion)) {
			if("preview".equals(thing)) {
				int idTemplate = super.getIoClaseWeb().getParamNum("idTemplate", 0);
				//retTextoTemplate(idTemplate);
			}
			else if("templates".equals(thing)) {
				MgrTemplate mgrTmpl = new MgrTemplate(super.getIoClaseWeb());
				super.getIoClaseWeb().retJson(mgrTmpl.getTemplates().getData());
			}
			else if("programaciones".equals(thing)) {
				MgrTemplate mgrTmpl = new MgrTemplate(super.getIoClaseWeb());
				super.getIoClaseWeb().retJson(mgrTmpl.getProgramacions().getData());
			}
		}
		
		else if("grabar".equals(accion)) {
			if("template".equals(thing)) {
				grabaTemplate();
			}
			else if("imagen".equals(thing)) {
				grabaTemplateImagen();
			}
			else if ("programacion".equals(thing)) {
				grabaProgramacion();
			}
		}
		
		else if("test".equals(accion)) {
			if("mail".equals(thing)) {
				testProgramacion();
			}

		}
	}
	
	private void testProgramacion() {
		String idProgramacion = super.getIoClaseWeb().getParamString("idprogramacion", "");
		MgrTemplate mgrTmpl 	= new MgrTemplate(super.getIoClaseWeb()); 
		ConsultaData data = mgrTmpl.getProgramacions(idProgramacion);
		String nombre = "";
		String mensaje = "";
		String id_imagen = "";
		
		int idDestinatarios = -1;
		if(data.next()) {
			int idTemplate = data.getInt("idtemplate");
			idDestinatarios = data.getInt("destinatarios");
			
			ConsultaData dataTemplates = mgrTmpl.getTemplates(idTemplate);
			if(dataTemplates.next()) {
				nombre = dataTemplates.getForcedString("nombre");
				mensaje = dataTemplates.getForcedString("mensaje");
				id_imagen = dataTemplates.getForcedString("id_imagen");
				
			}
		}
		
		FreemakerTool tool = new FreemakerTool();
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("nombre",nombre);
		modelRoot.put("mensaje",mensaje);
		modelRoot.put("id_imagen",id_imagen);
		modelRoot.put("servlet",	String.valueOf(super.getIoClaseWeb().getReq().getRequestURL()).replaceAll("/EjeCore","/SLoadFile?idfile="+ id_imagen).trim() );

		
		Template template = null;
		ResourceHtml rsHtml = new ResourceHtml();
		try {
			
			template =rsHtml.getTemplate("saludos/plantillaCorreo.html");
			String html = tool.templateProcess(template , modelRoot);
			
			List<IVoDestinatario> lista = new ArrayList<IVoDestinatario>();
			if(idDestinatarios == 1) {
				lista.add(new VoDestinatario("Francisco", "fvillegas@ejedigital.cl" , RecipientType.BCC));
				lista.add(new VoDestinatario("Victoria Alvarez" , "victoria.alvarez@ebco.cl", RecipientType.TO));
				lista.add(new VoDestinatario("NICOLE RUSTOM CARDENAS", "nicole.rustom@ebco.cl" , RecipientType.TO));
			}
			
			CorreoGGD correo = new CorreoGGD(html, lista, nombre);
			ICorreoProcess correoProcess = new CorreoProcessPropertie(correo);
			CorreoDispatcher.getInstance().sendMail(correoProcess);
			
		} catch (IOException e1) {
			System.out.println(e1);
		} catch (MessagingException e) {
			System.out.println(e);
		} catch (ServletException e) {
			System.out.println(e);
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	private void grabaProgramacion() {
		String hora 			= super.getIoClaseWeb().getParamString("hora", "");
		String minuto 			= super.getIoClaseWeb().getParamString("minuto", "");
		String solounavez 		= super.getIoClaseWeb().getParamString("solounavez", "");
		String estado 			= super.getIoClaseWeb().getParamString("estado", "");
		String destinatarios 	= super.getIoClaseWeb().getParamString("destinatarios", "");
		String idplantilla	 	= super.getIoClaseWeb().getParamString("idplantilla"	, "");
	
		MgrTemplate mgrTmpl 	= new MgrTemplate(super.getIoClaseWeb()); 
		double num = mgrTmpl.addProgramacion(idplantilla, hora, minuto, solounavez, estado, destinatarios);

		Map<String,String> map = new HashMap<String,String>();
		map.put("success","false");
		if(num >= 0){
			map.put("success","true");
		}
		
		super.getIoClaseWeb().retJson(map);
	}
	
	private void grabaTemplateImagen() {
		int rut 				= Integer.valueOf(super.getIoClaseWeb().getUsuario().getRutId());
		File   file 			= super.getIoClaseWeb().getFile("file_upload");

		int idFile = -1;
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("success","false");
		if( file != null && file.exists()) {
			FileService f = new FileService(super.getIoClaseWeb().getServletContext());
			idFile = f.addFile(rut,file,EjeFileUnicoTipo.DESCONOCIDO);
			
			if(idFile != -1) {
				map.put("success","true");
			}
		}
		
		
		map.put("idfile",String.valueOf(idFile));
		
		super.getIoClaseWeb().retJson(map);
	}
	
	
	private void grabaTemplate() {
		int rut 				= Integer.valueOf(super.getIoClaseWeb().getUsuario().getRutId());
		int id					= super.getIoClaseWeb().getParamNum("id",-1);
		String nombreTemplate 	= super.getIoClaseWeb().getParamString("nameTemplate", "");
		String texto 			= super.getIoClaseWeb().getParamString("areaTexto", "");
		int idFile 				= super.getIoClaseWeb().getParamNum("idfile",-1);

		MgrTemplate mgrTmpl 	= new MgrTemplate(super.getIoClaseWeb()); 
		mgrTmpl.addTemplate(nombreTemplate,texto,idFile);

		
		
		super.getIoClaseWeb().retTemplate("saludos/admPlantillas.html");
	}
	
	/*
	private void retTextoTemplate(int idTemplate) {
		MgrTemplate mgrTmpl = new MgrTemplate(super.getIoClaseWeb());
		
		ConsultaData data = mgrTmpl.getTemplates(idTemplate);
		
		JSonDataOut out = new JSonDataOut(data);
		
		List<String> filtro = new ArrayList<String>();
		
		
		filtro.add("id_template");
		filtro.add("nombre_template");
		filtro.add("texto_template");
		filtro.add("id_file");
		filtro.add("id_tipo_template");
		filtro.add("vigencia");
		filtro.add("name_original");
		filtro.add("name_unic");
		out.setFilter(filtro);

		super.getIoClaseWeb().retTexto("[" + out.getListData() + "]");
	}*/
	
	
	
}



class CorreoGGD implements ICorreoBuilder {
	private String html;
	private String colaboradorName;
	private List<IVoDestinatario> lista;
	
	CorreoGGD( String html, List<IVoDestinatario> lista , String colaboradorName ) {
		this.html = html;
		this.colaboradorName = colaboradorName;
	
		IVoDestinatario vo2 = new VoDestinatario("Copia de Respaldo", "ejedigital.cl@gmail.com", RecipientType.BCC);
		lista.add(vo2);
		
		this.lista = lista;
	}
	
	public List<File> getArchivosAdjuntos() {
		// TODO Auto-generated method stub
		return new ArrayList<File>();
	}

	public String getAsunto() {
		return colaboradorName;
	}

	public String getBody() {
		// TODO Auto-generated method stub
		return html;
	}

	public List<IVoDestinatario> getDestinatarios() {
		// TODO Auto-generated method stub
		return lista;
	}
	
}
