package portal.com.eje.usuario;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;

import org.apache.commons.lang.NotImplementedException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.FreemakerTool;
import cl.ejedigital.web.datos.ConsultaTool;
import cl.ejedigital.web.datos.DBConnectionManager;
import cl.ejedigital.web.fileupload.FileService;
import cl.ejedigital.web.fileupload.vo.EjeFilesUnico;
import freemarker.template.SimpleHash;
import portal.com.eje.frontcontroller.resobjects.ResourceHtml;
import portal.com.eje.tools.security.Base64Coder;
import portal.com.eje.tools.security.Encrypter;

public class UsuarioImagenManager {
	public enum tipoImagenTrabajador {
		temporal(1),
		cortadaYDefinitiva(5);
		int id;
		
		tipoImagenTrabajador(int id) {
			this.id = id;
		}
		
		public int getId() {
			return this.id;
		}
	}
	
	
	
	private final String IMGSINFOTO = "sinfoto.jpg";
	private final String IMGPATH = "temporal/filesUnicos/";
	private final String IMGPATH_CARGAMANUAL = "temporal/imgtrabajadores/";
	private final String IMGSQLGETLAST = " select top 1 fu.id_file, fu.name_unic, fu.name_original from eje_ges_trabajador_imagen i inner join EJE_FILES_UNICO fu on fu.ID_FILE = i.id_file where  i.rut = ? order by i.corr desc";
 
	
	public void checkExitesteTabla() {
		FreemakerTool tool = new FreemakerTool();
		ResourceHtml html = new ResourceHtml();
		
		try {
			String sql = tool.templateProcess(html.getTemplate("sql/create/[dbo].[eje_ges_trabajador_imagen]"), new SimpleHash());
			ConsultaTool.getInstance().update("portal",sql);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (ServletException e) {
			e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public VoUsaurioImagen getImagen(int rut) {
		
		VoUsaurioImagen vo = getImagenFromPrimeraCarga(rut);
		
		if(vo == null) {
			vo = getImagenFromOtrasCargas(rut);
		}
		
		if(vo == null || vo.getNameOriginal() == null) {
			vo = new VoUsaurioImagen();
			vo.setNameUnic(IMGPATH.concat(IMGSINFOTO));
			vo.setNameOriginal(IMGPATH.concat(IMGSINFOTO));
		}
		
		return vo;
	}

	/**
	 * @author PANCHO 
	 * @sinca 2016-03-30
	 * 
	 * No ha sido implementada aún
	 * */
	private VoUsaurioImagen getImagenFromOtrasCargas(int rut) {
		Object[] params = { rut };
		VoUsaurioImagen vo = null;
		
		try {
			String sql ="SELECT ft.wp_cod_empresa, ft.id_foto FROM eje_ges_foto_trab ft WHERE ft.RUT = ? and ft.wp_cod_planta is null ";
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params);
			
			if(data != null && data.next()) {
				 vo = new VoUsaurioImagen();
				vo.setNameOriginal(IMGPATH.concat(data.getForcedString("id_foto")));
				vo.setNameUnic(IMGPATH.concat(data.getForcedString("id_foto")));
				vo.setIdFile( Validar.getInstance().validarInt( data.getForcedString("wp_cod_empresa"),0) );
			}
		 
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return vo;
	}
	
	private VoUsaurioImagen getImagenFromPrimeraCarga(int rut) {
		Object[] params = { rut };
		VoUsaurioImagen vo = null;
		
		try {
			String sql ="SELECT id_foto FROM eje_ges_foto_trab WHERE RUT = ? and not wp_cod_empresa is null and not wp_cod_planta is null ";
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params);
			
			if(data != null && data.next()) {
				 vo = new VoUsaurioImagen();
				vo.setNameOriginal(IMGPATH_CARGAMANUAL.concat(data.getForcedString("id_foto")));
				vo.setNameUnic(IMGPATH_CARGAMANUAL.concat(data.getForcedString("id_foto")));
				vo.setIdFile(0);
			}
			/*
			else {
				data = ConsultaTool.getInstance().getData("portal",IMGSQLGETLAST, params);
				if(data.next()) {
					vo.setNameOriginal(IMGPATH.concat(data.getString("name_original")));
					vo.setNameUnic(IMGPATH.concat(data.getString("name_unic")));
					vo.setIdFile(data.getInt("id_file"));
				}
			}
			*/
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		/*
		if(vo.getNameOriginal() == null) {
			vo.setNameUnic(IMGPATH.concat(IMGSINFOTO));
			vo.setNameOriginal(IMGPATH.concat(IMGSINFOTO));
		}
		*/
		
		return vo;
	}
	
	/**
	 * Ya no se debe usar este método.<br/>
	 * Asimismo en jdk11 no existe javax.xml.bind
	 * 
	 * @deprecated
	 * @author Pancho
	 * */
	public byte[] getImagen(String codImagen) {
		throw new NotImplementedException();
		
//		if(codImagen != null) {
//			Encrypter enc = new Encrypter();
//			String codDecript = enc.decrypt(codImagen);
//			codDecript = codDecript.replaceAll("EJEDIGITAL_", "");
//			codDecript = codDecript.replaceAll("_EJEDIGITAL", "");
//			
//			try {
//				String sql = "SELECT n_file FROM eje_ges_trabajador_imagen WHERE corr = ? "; 
//				Object[] param = {Validar.getInstance().validarDouble(codDecript)};
//				ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, param);
//				
//				if( data != null && data.next() ) {
//					InputStream inputStream = data.getBlob("n_file").getBinaryStream();
//					String imagen = IOUtils.toString(inputStream); 
//					byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(imagen);
//					return imageBytes;
//				}
//				
//			} catch  (SQLException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} 
//		}
//		
//		return null;
	}
	
	/**
	 * Esta método entregará el código de imagen de la persona. este coódigo debe llamarse con el servlet SLoadFile con el parametro cImagen.
	 * 
	 * 
	 * */
	
	public String getCodImagen(tipoImagenTrabajador tipo, Integer rut) {
		
		try {
			String sql = "SELECT corr FROM eje_ges_trabajador_imagen WHERE rut = ? and valida = ? and tipo_imagen = ? "; 
			Object[] param = {rut, 1, tipo.getId()};
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, param);
			
			if( data != null && data.next() ) {
				Encrypter enc = new Encrypter();
				return enc.encrypt("EJEDIGITAL_"+data.getForcedString("corr")+"_EJEDIGITAL");
			}
			
		} catch  (SQLException e) {
			e.printStackTrace();
		} 
		
		return null;
	}
	
	/**
	 * Método que agregará las imagenes del trabajador, automáticamente retorna el codigo que le corresponde.
	 * Esta método entregará el código de imagen de la persona. Este código debe llamarse con el servlet SLoadFile con el parametro cImagen.
	 * 
	 * 
	 * */
	public String setImagen(tipoImagenTrabajador tipo, Integer rut, File imagen) {
 
		if(imagen != null && rut != null) {
			String base64 = Base64Coder.encode(imagen);
			return setImagen(tipo, rut, base64);		
		}
		
		return null;
 	}
	
	public String setImagen(tipoImagenTrabajador tipo, Integer rut, String base64) {
		double id = -1;
		
		try {
			String sql = "update eje_ges_trabajador_imagen set valida = ? where rut = ? and tipo_imagen = ? ";
			Object [] param = {0, rut, tipo.getId() };
			ConsultaTool.getInstance().update("portal", sql, param);
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			String sql = "INSERT INTO eje_ges_trabajador_imagen (rut, n_file,	fecha_save,	tipo_imagen, valida) values (?,?,getdate(),?,?) ";
			Object[] param = {rut, base64.getBytes(), tipo.getId(), 1};
			id = ConsultaTool.getInstance().insertIdentity("portal", sql, param);
			
			
		} catch  (SQLException e) {
			e.printStackTrace();
		}
		
		Encrypter enc = new Encrypter();
		return enc.encrypt("EJEDIGITAL_"+id+"_EJEDIGITAL");
	}
	
	
	
	
	/**
	 * la imagen se obtiene de a partir del ID, error, se subirá completamente desde ahora en BD.Para eso ver el método ; public boolean setImagenTemporal(tipoImagenTrabajador tipo, Integer rut, File imagen) {
	 * 
	 * @deprecated
	 * */
	
	public boolean setImagen(int rut, int idFile) {
		
		Object[] params = { rut, idFile };
		String sql		 = "INSERT INTO eje_ges_trabajador_imagen (rut, id_file, fecha_save) values (?,?, GETDATE())";
		String sqlCheck  = "SELECT top 1 * FROM eje_ges_foto_trab where RUT = ? ";
		String sqlUpdate = "UPDATE eje_ges_foto_trab SET id_foto = ?, wp_cod_empresa = ?, wp_cod_planta = null WHERE rut = ? ";
		String sqlInsert = "INSERT into eje_ges_foto_trab (rut, foto_trab, id_foto, wp_cod_empresa, wp_cod_planta) values (?, null, ?, ?, ?) ";
		boolean ok = true;
		Connection conn = DBConnectionManager.getInstance().getConnection("portal");
		
		FileService fs = new FileService();
		EjeFilesUnico efu = fs.getFileUnico(idFile);
		
		try {
			
			
			conn.setAutoCommit(false);
			ConsultaTool.getInstance().insert(conn, sql , params);
			
			Object[] pCheck = {rut};
			ConsultaData data = ConsultaTool.getInstance().getData(conn, sqlCheck, pCheck );
			if(data.next()) {
				Object[] pUpdate = {efu.getNameUnic(), efu.getIdFile(), rut};
				ConsultaTool.getInstance().update(conn, sqlUpdate, pUpdate );
			}
			else {
				
				
				Object[] pUpdate = { rut,  efu.getNameUnic(), efu.getIdFile() , null };
				ConsultaTool.getInstance().insert(conn, sqlInsert, pUpdate );
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
			ok = false;
		}
		finally {
			
			try {
				if(ok) {
					conn.commit();
				}
				else {
					conn.rollback();
				}
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			DBConnectionManager.getInstance().freeConnection("portal", conn);
		}
		
		return true;
	}
	
	
}
