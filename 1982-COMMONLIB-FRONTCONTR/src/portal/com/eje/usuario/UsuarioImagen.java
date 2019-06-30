package portal.com.eje.usuario;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.poi.hssf.util.HSSFColor.VIOLET;

import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.resobjects.ResourceHtml;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.web.FreemakerTool;
import cl.ejedigital.web.datos.ConsultaTool;
import cl.ejedigital.web.fileupload.FileService;
import cl.ejedigital.web.fileupload.vo.EjeFileUnicoTipo;
import freemarker.template.SimpleHash;


public class UsuarioImagen extends AbsClaseWeb {
	public UsuarioImagen(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doPost() throws Exception {
		doGet();
		
	}

	@Override
	public void doGet() throws Exception {
		String accion = super.getIoClaseWeb().getParamString("accion","");
		String thing  = super.getIoClaseWeb().getParamString("thing","");
		
		if("save".equals(accion)){
			
		}
		else if("select".equals(accion)) {
			if("foto".equals(thing)) {
				int rut = super.getIoClaseWeb().getParamNum("rut",-1);
				if(rut == -1) {
					returnPathImagen( super.getIoClaseWeb().getUsuario().getRutIdInt());
				}
				else {
					returnPathImagen( rut );
				}
			}
			
		}
		else if("upload".equals(accion)) {
			if("foto".equals(thing)) {
				uploadImagen(super.getIoClaseWeb().getUsuario().getRutIdInt(),super.getIoClaseWeb().getFile("fileImgTrabajador"));
			}
			
		}
		
	}
	
	public VoUsaurioImagen getImagen(int rut) {
		UsuarioImagenManager uMng = new UsuarioImagenManager();
		VoUsaurioImagen vo =  uMng.getImagen(rut);
		
		return vo;
	}
	
	public void returnPathImagen(int rut) {
		UsuarioInfo info = new UsuarioInfo(getIoClaseWeb());
		UsuarioImagenManager uMng = new UsuarioImagenManager();
		VoUsaurioImagen vo =  uMng.getImagen(rut);
		
		DataList lista = info.getDatosUsuario();
		
		lista.get(0).put("path", new Field(vo.getNameUnic()) );
		lista.get(0).put("name", new Field(vo.getNameOriginal()));
		super.getIoClaseWeb().retJson(lista);
	}
	
	public int getIdFileImagen(int rut) {
		UsuarioImagenManager uMng = new UsuarioImagenManager();
		VoUsaurioImagen vo =  uMng.getImagen(rut);
		
		if(vo != null) {
			return vo.getIdFile();
		}
		else {
			return -1;
		}
	}
	
	public void uploadImagen(int rut, File img) {
		UsuarioImagenManager uMng = new UsuarioImagenManager();
		uMng.checkExitesteTabla();
		FileService fs = new FileService(super.getIoClaseWeb().getServletContext());
		int idFile = fs.addFile(rut,img,"UploadImagen Trabajador");
		
		uMng.setImagen(rut,idFile);
		
	}
	
	
	
}

