package portal.com.eje.serhumano.tracking;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.output.JSarrayTableDataOut;
import cl.ejedigital.consultor.output.JSonDataOut;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;


public class TrackingListas extends AbsClaseWeb {

    public TrackingListas(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		// TODO Auto-generated constructor stub
	}
    

    public void doPost()
        throws IOException, ServletException
    {
    	String pageLoad = super.getIoClaseWeb().getParamString("pageLoad","");
    			
    	
    	if(pageLoad.equals("empresa")){
    		returnEmpresas();
    	}
    	
    	if (pageLoad.equals("users")){
    		returnFiltroUsuarios();
    	}
    }

    public void doGet()
        throws IOException, ServletException
    {
        doPost();
    }
    
    private void returnFiltroUsuarios() {
    	
    	String nombre = super.getIoClaseWeb().getParamString("nombre","");
		String cargo = super.getIoClaseWeb().getParamString("cargo","");
		String empresa = super.getIoClaseWeb().getParamString("empresa","");
		String limite = super.getIoClaseWeb().getParamString("limit","");
		String page = super.getIoClaseWeb().getParamString("page","");
		
		try{
			StringBuilder str = new StringBuilder();
			StringBuilder str2 = new StringBuilder();

			str.append(" select top ").append(limite);
			str.append(" a.rut, a.digito_ver, b.password_usuario, a.nombre, c.descrip as cargo, e.descrip as empresa ");  
			str.append(" FROM [dbo].[eje_ges_trabajador] a ");  
			str.append(" INNER JOIN [dbo].[eje_ges_usuario] b ");  
			str.append(" ON a.rut = b.login_usuario ");  
			str.append(" INNER JOIN [dbo].[eje_ges_cargos] c ");  
			str.append(" ON a.cargo = c.cargo ");  
			str.append(" INNER JOIN [dbo].[eje_ges_empresa] e ");  
			str.append(" ON a.empresa = e.empresa ");  
			str.append(" WHERE a.nombre like '%").append(nombre).append("%' ");
			str.append(" AND c.descrip LIKE '%").append(cargo).append("%' ");  
			str.append(" AND e.descrip LIKE '%").append(empresa).append("%' ");  
			str.append(" AND not rut  in (select top ( ").append(limite).append(" * (").append(Integer.parseInt(page)-1).append(") ) ");
			str.append(" rut from eje_ges_trabajador order by nombre) ");  
			str.append(" GROUP BY a.rut, a.digito_ver, b.password_usuario, a.nombre, c.descrip, e.descrip ");  
			str.append(" ORDER BY nombre ");  
	
			str2.append(" SELECT count(distinct a.rut) ");
			str2.append(" FROM [dbo].[eje_ges_trabajador] a ");
			str2.append(" INNER JOIN [dbo].[eje_ges_usuario] b ");
			str2.append(" ON a.rut = b.login_usuario ");
			str2.append(" INNER JOIN [dbo].[eje_ges_cargos] c ");
			str2.append(" ON a.cargo = c.cargo ");
			str2.append(" INNER JOIN [dbo].[eje_ges_empresa] e ");
			str2.append(" ON a.empresa = e.empresa ");
			str2.append(" WHERE a.nombre like '%").append(nombre).append("%' AND c.descrip LIKE '%").append(cargo).append("%'");
			str2.append(" AND e.descrip LIKE '%").append(empresa).append("%'");	
			ConsultaData data = ConsultaTool.getInstance().getData("portal", str.toString());
			ConsultaData data2 = ConsultaTool.getInstance().getData("portal", str2.toString());
			
			data2.next();
			JSonDataOut out = new JSonDataOut(data);
			String totalCount = "\"totalCount\":";
			super.getIoClaseWeb().retTexto("{" + totalCount + "\"" + data2 + "\",\"root\":["+ out.getListData() + "]}");			
			
		}
		
		catch(SQLException e){
			e.printStackTrace();
		}
		
    }
    private void returnEmpresas() {
    	try{
    	String str = "SELECT DISTINCT empresa AS id, UPPER(descrip) AS empresa FROM eje_ges_empresa";
        ConsultaData data = ConsultaTool.getInstance().getData("portal", str.toString());
		JSonDataOut out = new JSonDataOut(data);
		super.getIoClaseWeb().retTexto("["+ out.getListData() + "]");
    	}
		catch(SQLException e){
			e.printStackTrace();
		}
    }
    

}