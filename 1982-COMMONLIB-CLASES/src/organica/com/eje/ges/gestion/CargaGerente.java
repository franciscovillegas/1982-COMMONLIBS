package organica.com.eje.ges.gestion;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import organica.com.eje.ges.usuario.Usuario;
import organica.tools.Validar;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class CargaGerente extends MyHttpServlet {
	
    public CargaGerente() { 
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {   
    	doGet(req, resp); 
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {   
    	DespResultado(req, resp); 
    }

    private void DespResultado(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
    	
    	Connection Conexion = connMgr.getConnection("portal");
        user = Usuario.rescatarUsuario(req);
        if(user.esValido()) {   

            valida = new Validar();
            SimpleHash modelRoot = new SimpleHash();
            String ruta = getServletContext().getRealPath("") + "/organica/gerentes.xls";
            
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(ruta));
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0);
            int filas = sheet.getPhysicalNumberOfRows(), estadoIU=0, exito=0, fallos=0;
            String rut="",empresa="",estado="",celda="";
            SimpleList FallosList = new SimpleList();
            SimpleHash FallosIter=new SimpleHash();
            try {    
            	PreparedStatement deleteGerente = Conexion.prepareStatement("delete from eje_ges_gerencia where rut=?");
            	PreparedStatement updateGerente = Conexion.prepareStatement("insert eje_ges_gerencia values(?,?,?,getdate())"); 
            	for(int i=0;i<filas;i++) {
            		HSSFRow row = sheet.getRow(i);
            		for(int j=0; j<3; j++) {
            			HSSFCell cell = row.getCell((short)j);
            			if (cell != null) {
            				switch(cell.getCellType()) {
            				case HSSFCell.CELL_TYPE_NUMERIC: {
            					int celdaI = (int)cell.getNumericCellValue();
            					celda = String.valueOf(celdaI); break;
            				}
            				case HSSFCell.CELL_TYPE_STRING:
            					celda= String.valueOf(cell.getStringCellValue()); break;
            				}
            				switch(j) {
            				case 0: rut=celda; break;
            				case 1: empresa=celda; break;
            				case 2: estado=celda; break;
            				}
            			}
            		}
                  
            		if(estado.equals("1")) {
            			deleteGerente.setString(1,rut);
                		estadoIU = deleteGerente.executeUpdate();
                     
                		updateGerente.setString(1,rut);
                		updateGerente.setString(2,empresa);
                		updateGerente.setString(3,estado);
                		estadoIU = updateGerente.executeUpdate();  
            		}
            		else {
            			deleteGerente.setString(1,rut);
                		estadoIU = deleteGerente.executeUpdate();
            		}
                 
            		if(estadoIU==1) {
            			exito++;
            		}
            		else {  
            			fallos++;
            			FallosIter = new SimpleHash();
            			FallosIter.put("rut",rut);
            		}
            	}
            	FallosList.add(FallosIter);
            } 
            catch (SQLException e) {  
            	e.printStackTrace();
            }
            modelRoot.put("exito",String.valueOf(exito));
            modelRoot.put("fallos",String.valueOf(fallos));
            if(fallos!=0) {
            	modelRoot.put("lfallos",FallosList);
            }
            super.retTemplate(resp,"Gestion/CargaGerentes.htm",modelRoot);
        }
        else {
            mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        }
        connMgr.freeConnection("portal", Conexion);
    }

    private Usuario user;
    private Validar valida;
    private Mensaje mensaje;
}