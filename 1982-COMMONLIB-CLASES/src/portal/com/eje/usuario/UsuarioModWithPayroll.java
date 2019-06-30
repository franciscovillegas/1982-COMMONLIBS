package portal.com.eje.usuario;

import cl.ejedigital.web.datos.ConsultaTool;
import java.sql.SQLException;
import portal.com.eje.carpelect.mgr.ManagerTrabajador;
import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;

public class UsuarioModWithPayroll extends AbsClaseWeb {
  
	public UsuarioModWithPayroll(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
	}
  
	public void doPost() throws Exception {}
  
	public void doGet() throws Exception {
	    String accion = super.getIoClaseWeb().getParamString("accion", "show");
	    String thing = super.getIoClaseWeb().getParamString("thing", "");
	    String htm = super.getIoClaseWeb().getParamString("htm", "permiso/AsocPerfil.htm");
	    if ("upd".equals(accion)) {
	    	
	    	System.out.println("Updating correo...");
	    	
	    	if ("correoValidado".equals(thing)) {
	    		
	    		System.out.println("Validando correo...");
	        
	    		String newMail = super.getIoClaseWeb().getParamString("correo", "");
	        
	    		ManagerTrabajador.getInstance().delConfirmacion(Integer.parseInt(super.getIoClaseWeb().getUsuario().getRutId()));
	    		ManagerTrabajador.getInstance().insertConfirmacion(Integer.parseInt(super.getIoClaseWeb().getUsuario().getRutId()), newMail);
	    		
	    		if (correoValidoParaGuardar(newMail)) {
	    			System.out.println("Correo Es válido para guardar...");
	    			updateCorreoPortal(Integer.parseInt(super.getIoClaseWeb().getUsuario().getRutId()), newMail);
	    			updateCorreoPayRoll(Integer.parseInt(super.getIoClaseWeb().getUsuario().getRutId()), newMail);
	    		}

	    		super.getIoClaseWeb().retTexto("true");
	      
	    	}
	    	
	    }
	    
	}
  
	private boolean correoValidoParaGuardar(String newMail) {
		if ((newMail != null) && (
				(newMail.toLowerCase().indexOf("fresenius-kabi.cl") != -1) || 
				(newMail.toLowerCase().indexOf("fresenius-kabi.com") != -1) || 
				(newMail.toLowerCase().indexOf("fresenius_kabi.cl") != -1) || 
				(newMail.toLowerCase().indexOf("fresenius_kabi.com") != -1))) {
			return true;
		}
		return false;
	}
  
	public boolean updateCorreoPortal(int rut, String mail) {
		
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append(" update eje_ges_trabajador set  mail = ?, e_mail = ? where rut = ? ");
    
		Object[] params = { mail, mail, Integer.valueOf(rut) };
    
		boolean ok = false;
		
		try { 
			ok = ConsultaTool.getInstance().insert("portal", strConsulta.toString(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    
		return ok;
		
	}
  
	public boolean updateCorreoPayRoll(int rut, String mail) {

		StringBuffer strConsulta = new StringBuffer();
	    strConsulta.append(" UPDATE REMPLES ");
	    strConsulta.append(" \tSET pmail = ? ");
	    strConsulta.append(" WHERE ");
	    strConsulta.append(" \tconvert(integer,substring(rut,1,LEN(rut)-2)) = ? ");
	    strConsulta.append(" \tand estado in ('a','x') ");
	    strConsulta.append(" \tand fecha_ret >= convert(varchar(10),dateadd(day,1,GETDATE()),112) ");
	    strConsulta.append(" \tand tiprem = 'S'");
	    
	    Object[] params = { mail, Integer.valueOf(rut) };
	    
	    boolean ok = false;
	    try {
	    	ok = ConsultaTool.getInstance().insert("payroll", strConsulta.toString(), params);
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    }
	    
	    return ok;

	}
	
}
