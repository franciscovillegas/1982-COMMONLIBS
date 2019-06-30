package portal.com.eje.daemon;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import cl.eje.model.generic.portal.Eje_daemon_grupos;
import cl.eje.model.generic.portal.Eje_daemon_inscritos;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.daemon.vo.TreeDaemon;
import portal.com.eje.daemon.vo.UnidadDaemon;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.organica.vo.IUnidadGenerica;
import portal.com.eje.portal.organica.vo.IUnidadGenericaStyling2;
import portal.com.eje.portal.organica.vo.UnidadGenericaStyleDef;
import portal.com.eje.portal.vo.CtrGeneric;
import portal.com.eje.portal.vo.VoTool;
import portal.com.eje.portal.vo.util.Where;

public class DaemonGrupo {
	private Logger logger = Logger.getLogger(this.getClass());

	public static DaemonGrupo getInstance() {
		return Util.getInstance(DaemonGrupo.class);
	}

	public Eje_daemon_grupos addGrupo(String nombre) throws NullPointerException, SQLException {
		Assert.notNull(nombre);

		Eje_daemon_grupos grupo = new Eje_daemon_grupos();
		grupo.setNombre(nombre);
		grupo.setFecha_creacion(ConsultaTool.getInstance().getNow());
		grupo.setIcono("../../images/btns/tag_blue.ico");
		
		CtrGeneric.getInstance().add(grupo);

		return grupo;
	}

	public boolean updateGrupo(Eje_daemon_grupos grupo) throws NullPointerException, SQLException {

		boolean ok = true;

		Assert.notNull(grupo);
		Assert.notNull(grupo.getNombre());
		Assert.isTrue(grupo.getId_grupo() > 0);
		grupo.setFecha_update(ConsultaTool.getInstance().getNow());

		ok = CtrGeneric.getInstance().upd(grupo);

		return ok;
	}

	@SuppressWarnings("unchecked")
	public boolean del(List<Integer> idGrupos) throws NullPointerException, SQLException {
		Assert.notNull(idGrupos);
		Assert.notEmpty(idGrupos);
		
		boolean ok = false;

		List<Where> wheres = new ArrayList<Where>();
		wheres.add(new Where("id_grupo", "in", idGrupos));
		Collection<Eje_daemon_grupos> cols;

		cols = CtrGeneric.getInstance().getAllFromClass(Eje_daemon_grupos.class, wheres);
		CtrGeneric.getInstance().del(cols);

		ok = true;

		return ok;
	}

	public Collection<Eje_daemon_grupos> getGrupos() throws NullPointerException, SQLException {
		Collection<Eje_daemon_grupos> cols = null;

		cols = CtrGeneric.getInstance().getAllFromClass(Eje_daemon_grupos.class);

		return cols;
	}

 
	public TreeDaemon getGruposTree()  {
		try {
			UnidadDaemon ud = privateBuildTree();
			
			return new TreeDaemon(ud, getStyle());
			
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private UnidadDaemon privateBuildTree() throws NullPointerException, SQLException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		
		UnidadDaemon dios = new UnidadDaemon("DIOS");
		Collection<Eje_daemon_grupos> grupos = DaemonManager.getCtr().getGrupos();
		Collection<Eje_daemon_inscritos> procesos = DaemonManager.getCtr().getDaemons();
		Map<Object, List<Eje_daemon_inscritos>> mapProcesos = VoTool.getInstance().getMapList((List<Eje_daemon_inscritos>) procesos, HashMap.class, ArrayList.class, "id_grupo" );
		
		if(grupos != null) {
			for(Eje_daemon_grupos grupo : grupos) {
				UnidadDaemon nodoGrupo = dios.addHijo(dios.getClass(), grupo.getNombre(), grupo.getIcono());
				nodoGrupo.importVo(grupo);
				nodoGrupo.addAtributo("tipo", "grupo");
				nodoGrupo.addAtributo("expanded", true);
				
				privateBuildProcess(nodoGrupo, mapProcesos);
			}
		}
		
		return dios;
	}
	
	private void privateBuildProcess(UnidadDaemon grupo, Map<Object, List<Eje_daemon_inscritos>> mapProcesos) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		Integer idGrupo = grupo.getAtributoInteger("id_grupo");
		
		List<Eje_daemon_inscritos> procesos = mapProcesos.get(idGrupo);
		
		if(procesos != null) {
			for(Eje_daemon_inscritos inscrito : procesos) {
				UnidadDaemon nodoGrupo = grupo.addHijo(grupo.getClass(), inscrito.getNombre(), inscrito.getIcono());
				nodoGrupo.importVo(inscrito);
				nodoGrupo.addAtributo("tipo", "proceso");
				
				if(nodoGrupo.getActivado()) {
					nodoGrupo.addAtributo("prox_fecha_ejecucion", DaemonProcess.getInstance().getNextExecution(inscrito.getPeridiosidad()));	
				}
				
				DaemonConfiguration conf = DaemonProcess.getInstance().getGetDaemonConfiguracion(inscrito.getEjecu_clase());
				if(conf != null) {
					nodoGrupo.addAtributo("url_conf", conf.pathIdeConfiguration());
				}
				 
				
				
			}
		}
	}
	
	private UnidadGenericaStyleDef getStyle() {
		UnidadGenericaStyleDef style = new UnidadGenericaStyleDef();
		
		style.addListener(new Style());
		return style;
	}
	
	class Style implements IUnidadGenericaStyling2 {

		@Override
		public String putIcon2(IUnidadGenerica unidad, int nivel, boolean isGroup, String icon) {
			if(nivel == 2) {
				String i = unidad.getAtributo("icono");
				return i != null ? i : "../../images/btns/tag_blue.ico";
			}
			
			return icon;
		}
		
	}
}
