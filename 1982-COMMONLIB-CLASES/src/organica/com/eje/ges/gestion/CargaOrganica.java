package organica.com.eje.ges.gestion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import organica.datos.Consulta;
import organica.tools.Validar;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.admin.CapManager;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.UserMgr;

public class CargaOrganica extends MyHttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public CargaOrganica() {}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		doGet(req,resp);
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		DespResultado(req,resp);
	}

	private void DespResultado(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		java.sql.Connection Conexion = connMgr.getConnection("portal");
		

		portal.com.eje.serhumano.user.Usuario userPortal =
			portal.com.eje.serhumano.user.SessionMgr.rescatarUsuario(req);

		try {
			IOClaseWeb io = new IOClaseWeb(this, req, resp);
			
			if(userPortal.esValido()) {				
				if(io.getMapFiles().size() > 0 ) {
					Set<String> set = io.getMapFiles().keySet();
					for(String key: set) {
						File organica = io.getMapFiles().get(key).get(0);
						procesaOrganica(organica,Conexion,userPortal,resp);
					}
				}
			}
			else {
				super.mensaje.devolverPaginaSinSesion(resp,"","Tiempo de Sesi\363n expirado...");
			}
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
		connMgr.freeConnection("portal",Conexion);
	}

	private void procesaOrganica(File f, java.sql.Connection Conexion, portal.com.eje.serhumano.user.Usuario userPortal,HttpServletResponse resp) throws FileNotFoundException, IOException {
		CapManager cm = new CapManager(Conexion);
		valida = new Validar();
		SimpleHash modelRoot = new SimpleHash();
		UserMgr mgr = new UserMgr(Conexion);
		

		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(f));
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		HSSFSheet sheet = wb.getSheetAt(0);
		int filas = sheet.getPhysicalNumberOfRows(),estadoIU = 0,exito = 0,fallos = 0;
		String rut = "",unidad = "",encargado = "",celda = "",acc_org = "0";
		SimpleList FallosList = new SimpleList();
		SimpleHash FallosIter = null;
		try {
			PreparedStatement updateUnidad =
				Conexion.prepareStatement("update eje_ges_trabajador set unidad=? where rut=?");
			for(int i = 0; i < filas; i++) {
				HSSFRow row = sheet.getRow(i);
				for(int j = 0; j < 4; j++) {
					HSSFCell cell = row.getCell((short) j);
					if(cell != null) {
						switch(cell.getCellType()) {
							case HSSFCell.CELL_TYPE_NUMERIC: {
								int celdaI = (int) cell.getNumericCellValue();
								celda = String.valueOf(celdaI);
								break;
							}
							case HSSFCell.CELL_TYPE_STRING:
								celda = String.valueOf(cell.getStringCellValue());
								break;
						}
						switch(j) {
							case 0:
								rut = celda;
								break;
							case 1:
								unidad = celda;
								break;
							case 2:
								encargado = celda;
								break;
							case 3:
								acc_org = celda;
								break;
						}
					}
				}
//				this.insertarPriv(rut, encargado, acc_org);
				// determinar si amerita registrar rotacion de unidad
				if(!unidad.equals(cm.IgualdadUnidadTrabajador(rut)))
					cm.UpdateUnidadTrabajador(rut,cm.IgualdadUnidadTrabajador(rut),unidad);

				// updatear la unidad dado el rut
				updateUnidad.setString(1,unidad);
				updateUnidad.setString(2,rut);
				estadoIU = updateUnidad.executeUpdate();
				
				mgr.borrarTrabajadorUnidad(rut);
				mgr.insertTrabajadorUnidad(rut, unidad);
				
				if(estadoIU == 1) {
					exito++;
					// determinar si es encargado de unidad
					if(encargado.equals("1")) {
						
 						Consulta encargado_unidad = new Consulta(Conexion);
						Calendar ahoraCal = Calendar.getInstance();
						String mes = null;
						if((ahoraCal.get(Calendar.MONTH) + 1) < 10)
							mes = "0" + String.valueOf(ahoraCal.get(Calendar.MONTH) + 1);
						else
							mes = String.valueOf(ahoraCal.get(Calendar.MONTH) + 1);
						String ano = String.valueOf(ahoraCal.get(Calendar.YEAR));
						String periodo = ano + mes;
						String query = "";
						
						// anulamos al encargado actual de la unidad
						mgr.actualizarEstadoEncargadoUnidad(unidad);
						
						//se borra el usuario de eje_generico_perfil_usuario
						mgr.borrarEncargadoAnterior(unidad, 2);
							
						// insertamos el nuevo encargado de unidad					
						mgr.insertUsuarioEncargado(rut, unidad, periodo, userPortal.getRutId(), "1", acc_org);
						
						mgr.insertUsuarioUnidad(rut, unidad);
						
						if("1".equals(acc_org)) {
							mgr.addAccesoGestion(Conexion,rut);
						}
						
						// insertamos permisos para organica
						query =
							String.valueOf(String.valueOf((new StringBuilder(
								"select app_id,wp_cod_empresa,wp_cod_planta from eje_ges_user_app where rut_usuario="
									+ rut + ""))));
						encargado_unidad.exec(query);
						String ee = userPortal.getEmpresa();
						String pe = "1";
						int k = 0,ish = 0,idf = 0,iac = 0;
						for(; encargado_unidad.next();) {
							if((!encargado_unidad.getString("wp_cod_empresa").equals(ee)) && (k == 0)) {
								ee = encargado_unidad.getString("wp_cod_empresa");
								pe = encargado_unidad.getString("wp_cod_planta");
								k = 1;
							}
							if(encargado_unidad.getString("app_id").equals("sh"))
								ish = 1;
							else if(encargado_unidad.getString("app_id").equals("df"))
								idf = 1;
							else if(encargado_unidad.getString("app_id").equals("adm_corp"))
								iac = 1;
						}
						if(ish == 0) {
							query =
								String.valueOf(String.valueOf((new StringBuilder(
									"insert into eje_ges_user_app(app_id,rut_usuario,vigente,wp_cod_empresa,wp_cod_planta) values('sh',"
										+ rut + ",NULL," + ee + ",1)"))));
							encargado_unidad.insert(query);
						}
						if(idf == 0) {
							query =
								String.valueOf(String.valueOf((new StringBuilder(
									"insert into eje_ges_user_app(app_id,rut_usuario,vigente,wp_cod_empresa,wp_cod_planta) values('df',"
										+ rut + ",NULL," + ee + ",1)"))));
							encargado_unidad.insert(query);
						}
						if(iac == 0) {
							query =
								String.valueOf(String.valueOf((new StringBuilder(
									"insert into eje_ges_user_app(app_id,rut_usuario,vigente,wp_cod_empresa,wp_cod_planta) values('adm_corp',"
										+ rut + ",NULL," + ee + ",1)"))));
							encargado_unidad.insert(query);
						}
					}
					else {
						mgr.borrarEncargadoAnteriorPorRut(rut);
						userPortal.quitarAccesoGestionPorUnidad(Conexion,unidad,rut);
						Consulta encargado_unidad = new Consulta(Conexion);
						// anulamos al encargado actual de la unidad
						String query =
							String.valueOf(String.valueOf((new StringBuilder(
								"update eje_ges_unidad_encargado set estado=0 where rut_encargado=")
								.append(rut))));
						//userPortal.addAccesoGestion(Conexion,rut);

						encargado_unidad.insert(query);
						// borramos permisos para organica
						query =
							String.valueOf(String.valueOf((new StringBuilder(
								"delete from eje_ges_user_app where rut_usuario=" + rut
									+ " and app_id in ('df','adm_corp')"))));
						encargado_unidad.insert(query);
					}
				}
				else {
					fallos++;
					FallosIter = new SimpleHash();
					FallosIter.put("rut",rut);
					FallosIter.put("unidad",unidad);
					FallosList.add(FallosIter);
				}
			}

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		modelRoot.put("exito",String.valueOf(exito));
		modelRoot.put("fallos",String.valueOf(fallos));
		if(fallos != 0)
			modelRoot.put("lfallos",FallosList);
		super.retTemplate(resp,"Gestion/CargaOrganica.htm",modelRoot);
	}
	
	

	private Validar	valida;
	private Mensaje	mensaje;
}