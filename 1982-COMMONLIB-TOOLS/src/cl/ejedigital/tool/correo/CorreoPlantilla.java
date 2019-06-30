package cl.ejedigital.tool.correo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Message.RecipientType;

import cl.ejedigital.tool.correo.ifaces.ICorreoBuilder;
import cl.ejedigital.tool.correo.vo.IVoDestinatario;
import cl.ejedigital.tool.correo.vo.VoDestinatario;

public class CorreoPlantilla implements ICorreoBuilder {
		private List<IVoDestinatario> listDestinatarios;
		private String asunto;
		private String msg;
		private List<File> listFile;
		
		private CorreoPlantilla() {
			listDestinatarios = new ArrayList<IVoDestinatario>();
			listFile = new ArrayList<File>();
			
			asunto = "Default correo";
			msg = "";
			
		}
		
		public CorreoPlantilla(String correoTo, String asunto, String msg) {	
			this();
			
			VoDestinatario vo = new VoDestinatario("", correoTo, RecipientType.TO);
			listDestinatarios.add(vo);
			
			this.asunto = asunto;
			this.msg = msg;
			
		}
		
		public CorreoPlantilla(List<IVoDestinatario> listDestinatarios,List<File> listFile, String asunto, String msg) {	
			this();
			
			if(this.listDestinatarios != null) {
				this.listDestinatarios = listDestinatarios;
			}

			if(this.listFile != null) {
				this.listFile = listFile;
			}
			
			this.asunto = asunto;
			this.msg = msg;
			
		}
		
		
		public List<IVoDestinatario> getDestinatarios() {
			return this.listDestinatarios;
		}

		public String getAsunto() {
			return this.asunto ;
		}

		public List<File> getArchivosAdjuntos() {
			return listFile;
		}

		public String getBody() {
			return this.msg;
		}

		
	}
