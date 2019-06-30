package portal.com.eje.carpelect.data;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.permiso.PerfilMngr;

import portal.com.eje.serhumano.user.Usuario;

import portal.com.eje.carpelect.mgr.ManagerTrabajador;
import portal.com.eje.datos.Consulta;
import cl.ejedigital.consultor.output.JSarrayTableDataOut;
import cl.ejedigital.consultor.output.jquerytable.JqueryTableTool;
import freemarker.template.SimpleHash;


public class Trabajadores extends AbsClaseWeb {
	
	
	public Trabajadores(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
	}

	@Override
	public void doPost() throws Exception {
		doGet();
		
	}

	@Override
	public void doGet() throws Exception {
		String accion = super.getIoClaseWeb().getParamString("accion","show");
		String thing  = super.getIoClaseWeb().getParamString("thing","");
		String htm	  = super.getIoClaseWeb().getParamString("htm","CarpetaElectronica/mantPersonas.html");
		
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("accion",accion);
		PerfilMngr perfil = new PerfilMngr();
		
		if("show".equals(accion)) {
			if("cargasmasivas".equals(thing)) {
				super.getIoClaseWeb().retTemplate("mantenedor/mantPersonas_cargasMasivas.html");
			}
			else {
				super.getIoClaseWeb().retTemplate(htm,modelRoot);		
			}
		}
		else if("select".equals(accion)) {
			if("trabajadores".equals(thing)) {
				selectTrabajadores();
			}
			else if("trabajadoresunidad".equals(thing)) {
				selectTrabajadoresUnidad();
			}
			else if("".equals(thing)) {
				selectTrabajadores();
			}
		}
		else if("insert".equals(accion)) {
		
		}

	}


	private void selectTrabajadoresUnidad() {	
			
		String unidad 	  = super.getIoClaseWeb().getParamString("unidad","-1");
		
		Usuario user = super.getIoClaseWeb().getUsuario();
		JSarrayTableDataOut out;
		
		if ("-1".equals(user.getPerfil().getIdPerfil())) {
			super.getIoClaseWeb().getUsuario().getUnidad();
			organica.com.eje.ges.usuario.Usuario userOrg = new organica.com.eje.ges.usuario.Usuario();
			userOrg.getDatos(super.getIoClaseWeb().getConnection("portal"), user.getRut().getRutId().concat("-").concat(user.getRut().getDig()), user.getPassWord());
			
			out = new JSarrayTableDataOut(ManagerTrabajador.getInstance().getTrabajadoresInUnidad(unidad, super.getIoClaseWeb().getConnection("portal"), getEmpresaPadre(), userOrg.getUnidad() ));
		} else{
			out = new JSarrayTableDataOut(ManagerTrabajador.getInstance().getTrabajadoresInUnidad(unidad));
		}
		
		
		JqueryTableTool table = new JqueryTableTool();		
		super.getIoClaseWeb().retTexto(table.getServerResponde(out,1).getListData());
	}

	
	private String getEmpresaPadre() {
		Connection conn = super.getIoClaseWeb().getConnection("portal");
		Consulta consulta = new Consulta(conn);
		StringBuilder sql = new StringBuilder("select top 1 empresa from eje_ges_empresa where padre_empresa is null");
		consulta.exec(String.valueOf(sql)); consulta.next();
		return consulta.getString("empresa");
	}
	
	private void selectTrabajadores() {
		List<String> filtro = new ArrayList<String>();
		filtro.add("rut");
		filtro.add("digito_ver");
		filtro.add("nombres");
		filtro.add("ape_paterno");
		filtro.add("ape_materno");
		filtro.add("fecha_ingreso");
		filtro.add("options");
		
		Usuario user = super.getIoClaseWeb().getUsuario();
		JSarrayTableDataOut out;
		if ("-1".equals(user.getPerfil().getIdPerfil())) {

			organica.com.eje.ges.usuario.Usuario userOrg = new organica.com.eje.ges.usuario.Usuario();
			userOrg.getDatos(super.getIoClaseWeb().getConnection("portal"), user.getRut().getRutId().concat("-").concat(user.getRut().getDig()), user.getPassWord());
		
			out = new JSarrayTableDataOut(ManagerTrabajador.getInstance().getTrabajadores(super.getIoClaseWeb().getConnection("portal"), getEmpresaPadre(), userOrg.getUnidad()));
		} else{
			out = new JSarrayTableDataOut(ManagerTrabajador.getInstance().getTrabajadores());
		}
		
		out.setFilter(filtro);
		JqueryTableTool table = new JqueryTableTool();		
		super.getIoClaseWeb().retTexto(table.getServerResponde(out,1).getListData());
	}
	
}



