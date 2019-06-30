package portal.com.eje.org.view;

import java.sql.SQLException;
import java.util.ResourceBundle;

import organica.datos.Consulta;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.output.IDataOut;
import cl.ejedigital.consultor.output.JSonDataOut;
import cl.ejedigital.consultor.output.XmlDataOut;
import cl.ejedigital.tool.properties.PropertiesTools;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.AbsClaseWebInsegura;
import portal.com.eje.frontcontroller.IOClaseWeb;


public class OrganicaViewer extends AbsClaseWebInsegura{

	public OrganicaViewer(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doPost() throws Exception {
		String accion = super.getIoClaseWeb().getParamString("accion","show");
		String thing  = super.getIoClaseWeb().getParamString("thing" ,"default");
		String pass   = super.getIoClaseWeb().getParamString("pass" ,"");
		
		ResourceBundle proper = ResourceBundle.getBundle("organica");
		PropertiesTools tools = new PropertiesTools();
		String password = tools.getString(proper,"organica.sw.password","pmp");
		
		
	
		
			if("show".equals(accion)) {
				if("default".equals(thing)) {
					showDescanso();	
				}
				else {
					
					if(password.equals(pass)) {
						
						if("getUnit".equals(thing)) {
							getUnit();
						}
						else if("getRutInUnit".equals(thing)) {
							getRutInUnit();
						}
						else if("getParent".equals(thing)) {
							getParent();
						}
						else if("getChildrens".equals(thing)) {
							getChildrens();
						}
						else if("getBossInUnit".equals(thing)) {
							getBossInUnit();
						}
						
					} else {
						returnMsg("Wrong Password");
					}
				}
			}	
		
	}

	@Override
	public void doGet() throws Exception {
		doPost();
		
	}

	
	private void showDescanso() {
		super.getIoClaseWeb().retTemplate("org/view/main.html");
	}
	
	private void returnMsg(String msg) {
		String connector = super.getIoClaseWeb().getParamString("connector","json");
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT '").append(msg).append("' as msg \n");
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal",sql.toString());
			
			
			if(data.next()) {
				IDataOut out = null;
				
				if("json".equals(connector)) {
					out = new JSonDataOut(data);
					super.getIoClaseWeb().retTexto("["+out.getListData()+"]");	
				}
				else if("xml".equals(connector)) { 
					out = new XmlDataOut(data);
					super.getIoClaseWeb().retTexto(out.getListData());	
				}
			}
			
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private void getUnit() {
		String connector = super.getIoClaseWeb().getParamString("connector","json");
		
		Object[] params = {super.getIoClaseWeb().getParamNum("rut",-1)};
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT top 1 t.unidad as unit, u.unid_desc as unit_desc \n");
		sql.append("FROM eje_ges_trabajador t left outer join (select u0.unid_id,u0.unid_desc from eje_ges_unidades u0 inner join eje_ges_jerarquia j0 on j0.nodo_id = u0.unid_id) u on u.unid_id = t.unidad \n"); 
		sql.append("WHERE t.rut = ? \n");
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal",sql.toString(), params);
			
			
			if(data.next()) {
				IDataOut out = null;
				
				if("json".equals(connector)) {
					out = new JSonDataOut(data);
					super.getIoClaseWeb().retTexto("["+out.getListData()+"]");	
				}
				else if("xml".equals(connector)) { 
					out = new XmlDataOut(data);
					super.getIoClaseWeb().retTexto(out.getListData());	
				}
			}
			else {
				returnMsg("[N/A]");
			}
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private void getRutInUnit() {
		String connector = super.getIoClaseWeb().getParamString("connector","json");
		
		Object[] params = {super.getIoClaseWeb().getParamString("unit","-1")};
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT top 1 t.unidad as unit, u.unid_desc as unit_desc , t.nombre \n");
		sql.append("FROM eje_ges_trabajador t left outer join (select u0.unid_id,u0.unid_desc from eje_ges_unidades u0 inner join eje_ges_jerarquia j0 on j0.nodo_id = u0.unid_id) u on u.unid_id = t.unidad \n"); 
		sql.append("WHERE t.unidad = ? \n");
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal",sql.toString(), params);
			
			
			if(data.next()) {
				IDataOut out = null;
				
				if("json".equals(connector)) {
					out = new JSonDataOut(data);
					super.getIoClaseWeb().retTexto("["+out.getListData()+"]");	
				}
				else if("xml".equals(connector)) { 
					out = new XmlDataOut(data);
					super.getIoClaseWeb().retTexto(out.getListData());	
				}
			}
			else {
				returnMsg("[N/A]");
			}
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void getParent() {
		String connector = super.getIoClaseWeb().getParamString("connector","json");
		
		Object[] params = {super.getIoClaseWeb().getParamString("unit","-1")};
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select j.nodo_padre as parent_unit, u.unid_desc as unit_desc \n");
		sql.append(" from eje_ges_jerarquia j inner join eje_ges_unidades u \n"); 
		sql.append(" 	on u.unid_id = j.nodo_padre \n");
		sql.append(" where j.nodo_id = ? \n"); 
		
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal",sql.toString(), params);
			
			
			if(data.next()) {
				IDataOut out = null;
				
				if("json".equals(connector)) {
					out = new JSonDataOut(data);
					super.getIoClaseWeb().retTexto("["+out.getListData()+"]");	
				}
				else if("xml".equals(connector)) { 
					out = new XmlDataOut(data);
					super.getIoClaseWeb().retTexto(out.getListData());	
				}
			}
			else {
				returnMsg("[N/A]");
			}
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void getChildrens() {
		String connector = super.getIoClaseWeb().getParamString("connector","json");
		
		Object[] params = {super.getIoClaseWeb().getParamString("unit","-1")};
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select j.nodo_id as unit, u.unid_desc as unit_desc \n");
		sql.append(" from eje_ges_jerarquia j inner join eje_ges_unidades u \n"); 
		sql.append(" 	on u.unid_id = j.nodo_id \n");
		sql.append(" where j.nodo_padre = ? \n"); 

		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal",sql.toString(), params);
			
			
			if(data.next()) {
				IDataOut out = null;
				
				if("json".equals(connector)) {
					out = new JSonDataOut(data);
					super.getIoClaseWeb().retTexto("["+out.getListData()+"]");	
				}
				else if("xml".equals(connector)) { 
					out = new XmlDataOut(data);
					super.getIoClaseWeb().retTexto(out.getListData());	
				}
			}
			else {
				returnMsg("[N/A]");
			}
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void getBossInUnit() {
		String connector = super.getIoClaseWeb().getParamString("connector","json");
		
		Object[] params = {super.getIoClaseWeb().getParamString("unit","-1")};
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select e.rut_encargado as rut , t.nombre \n");
		sql.append(" from eje_ges_unidad_encargado e inner join eje_ges_trabajador t \n");
		sql.append(" 	on t.rut = e.rut_encargado \n");
		sql.append(" where e.estado = 1 and e.unid_id = ? \n");
		
			
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal",sql.toString(), params);
			
			
			if(data.next()) {
				IDataOut out = null;
				
				if("json".equals(connector)) {
					out = new JSonDataOut(data);
					super.getIoClaseWeb().retTexto("["+out.getListData()+"]");	
				}
				else if("xml".equals(connector)) { 
					out = new XmlDataOut(data);
					super.getIoClaseWeb().retTexto(out.getListData());	
				}
			}
			else {
				returnMsg("[N/A]");
			}
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}


