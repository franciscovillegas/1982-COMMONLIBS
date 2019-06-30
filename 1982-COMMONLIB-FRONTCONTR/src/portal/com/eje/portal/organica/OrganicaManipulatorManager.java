package portal.com.eje.portal.organica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import cl.eje.model.generic.portal.Eje_ges_jerarquia;
import cl.eje.model.generic.portal.Eje_ges_jerarquia_bkp;
import cl.eje.model.generic.portal.Eje_ges_unidades;
import cl.eje.model.generic.portal.Eje_ges_unidades_bkp;
import cl.eje.model.generic.portal.Eje_ges_unidades_log;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.strings.ArrayFactory;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.ConsultaTool;
import error.BadParameterException;
import portal.com.eje.portal.factory.Mng;
import portal.com.eje.portal.organica.errors.AlreadyExistUnidIdExcepcion;
import portal.com.eje.portal.organica.errors.NotExistUnidIdException;
import portal.com.eje.portal.organica.errors.UnidIdWasDeletedException;
import portal.com.eje.portal.organica.ifaces.IOrganicaManipulatorManager;
import portal.com.eje.portal.vo.CtrGeneric;
import portal.com.eje.portal.vo.CtrGenericBatch;
import portal.com.eje.portal.vo.CtrTGeneric;
import portal.com.eje.portal.vo.CtrTGenericBatch;
import portal.com.eje.portal.vo.VoTool;
import portal.com.eje.portal.vo.util.Wheres;
import portal.com.eje.portal.vo.vo.Vo;

public class OrganicaManipulatorManager implements IOrganicaManipulatorManager {

	public static IOrganicaManipulatorManager getInstance() {
		return Mng.getInstance(OrganicaManipulatorManager.class);
	}

	@Override
	public boolean existUnidId(Connection conn, String unid_id) throws SQLException {
		if(conn == null || unid_id == null) {
			
		}

		ConsultaData dataJerarquia = null;
		ConsultaData dataUnidad = null;
		
		if(conn == null) {
			dataJerarquia = CtrGeneric.getInstance().getDataFromClass(Eje_ges_jerarquia.class, Wheres.where("nodo_id", "=", unid_id).build(), null);
			dataUnidad = CtrGeneric.getInstance().getDataFromClass(Eje_ges_unidades.class, Wheres.where("unid_id", "=", unid_id).build(), null);	
		}
		else {
			dataJerarquia = CtrTGeneric.getInstance().getDataFromClass(conn, Eje_ges_jerarquia.class, Wheres.where("nodo_id", "=", unid_id).build(), null);
			dataUnidad = CtrTGeneric.getInstance().getDataFromClass(conn, Eje_ges_unidades.class, Wheres.where("unid_id", "=", unid_id).build(), null);
		}
		
		return (dataJerarquia != null && dataJerarquia.next()) || (dataUnidad != null && dataUnidad.next());
	}

	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	public boolean addUnidad(Connection conn, String unid_id, String unid_id_parent, String unid_desc) throws SQLException, AlreadyExistUnidIdExcepcion, NotExistUnidIdException {
		if (unid_id == null) {
			unid_id = String.valueOf(System.currentTimeMillis());
		}

		if (unid_id != null && existUnidId(conn, unid_id)) {
			throw new AlreadyExistUnidIdExcepcion("Ya existe unid_id " + unid_id);
		}

		if (unid_desc == null || !existUnidId(conn, unid_id_parent)) {
			throw new NotExistUnidIdException("No existe el unid_id " + unid_id_parent);
		}

		Eje_ges_jerarquia j = new Eje_ges_jerarquia();
		j.setCompania("0");
		j.setId_tipo(null);
		j.setNodo_id(unid_id);
		j.setNodo_padre(unid_id_parent);

		Eje_ges_unidades u = new Eje_ges_unidades();
		u.setUnid_empresa("0");
		u.setUnid_id(unid_id);
		u.setUnid_desc(unid_desc);
		u.setVigente("S");

		if(conn != null) {
			CtrTGeneric.getInstance().updOrAdd(conn, j);
			CtrTGeneric.getInstance().updOrAdd(conn, u);	
		}
		else {
			CtrGeneric.getInstance().updOrAdd( j);
			CtrGeneric.getInstance().updOrAdd( u);	
		}

		return true;
	}

	@Override
	public boolean setVigente(Connection conn, String unid_id, boolean vigente) throws SQLException {

		List<String> listaUnidsIds = new ArrayList<String>();
		listaUnidsIds.add(unid_id);
		
		return setVigente(conn, listaUnidsIds, vigente);
	}
	

	@Override
	public boolean setVigente(Connection conn, List<String> listaUnidsIds, boolean vigente) throws NullPointerException, SQLException {
		if (listaUnidsIds == null) {
			throw new NullPointerException();
		}
		
		if(listaUnidsIds.size() == 0) {
			throw new IndexOutOfBoundsException("No puede tener 0 objetos");
		}
		
		Collection<Eje_ges_unidades> unidades = null;
		
		if(conn != null) {
			unidades = CtrTGeneric.getInstance().getAllFromClass(conn, Eje_ges_unidades.class, Wheres.where("unid_id", "in", listaUnidsIds).build());	
		}
		else {
			unidades = CtrGeneric.getInstance().getAllFromClass(Eje_ges_unidades.class, Wheres.where("unid_id", "in", listaUnidsIds).build());	
		}
		Date now = ConsultaTool.getInstance().getNow(conn);
		for(Eje_ges_unidades u :unidades) {
			u.setVigente(vigente ? "S" : "N");
			u.setFecha_no_vigente(now);
			
			if(!vigente) {
				u.setNombre_bloqueado(true);		
			}
		}

		if(conn != null) {
			CtrTGenericBatch.getInstance().updOrAdd(conn, (List<? extends Vo>) unidades);	
		}
		else {
			CtrGenericBatch.getInstance().updOrAdd((List<? extends Vo>) unidades);
		}
		
		return true;
	}

	@Override
	public boolean addLog(Connection conn, String glosa, Integer rutUsuario, String unid_id, Boolean vigente, String nombre, String unid_id_parent) throws NotExistUnidIdException, SQLException, BadParameterException {
		if (glosa == null) {
			throw new NullPointerException("Glosa no puede ser null");
		}

		if (rutUsuario == null) {
			throw new NullPointerException("rutUsuario no puede ser null");
		}

		if (unid_id == null) {
			throw new NullPointerException("unid_id no puede ser null");
		}

		if (!existUnidId(conn, unid_id)) {
			throw new NotExistUnidIdException("No existe el unid_id " + unid_id);
		}

		if (unid_id_parent != null && !existUnidId(conn, unid_id_parent)) {
			throw new NotExistUnidIdException("No existe el unid_id_parent " + unid_id_parent);
		}

		Eje_ges_unidades_log l = new Eje_ges_unidades_log();
		l.setGlosa(Validar.getInstance().cutStringSinComillas(glosa, 100));
		l.setRut_responsable(rutUsuario);
		l.setFecha(ConsultaTool.getInstance().getNow(conn));
		l.setUnid_id(unid_id);

		if (vigente != null) {
			l.setVigente(vigente);
		}

		l.setUnid_id_parent(unid_id_parent);
		l.setNombre(nombre);

		boolean ok = false;
		if(conn != null) {
			ok = CtrTGeneric.getInstance().add(conn, l);
		}
		else {
			ok = CtrGeneric.getInstance().add(l);
		}
		
		//backupOrganica(conn, l.getId_movimiento());
		
		return ok; 
	}

	@SuppressWarnings("unused")
	@Override
	public boolean setNombre(Connection conn, String unid_id, String unid_desc) throws NullPointerException, SQLException {
		if(conn == null || unid_id == null|| unid_desc ==null) {
			throw new NullPointerException();
		}
		
		Eje_ges_unidades u = null;
		
		if(conn != null) {
			u = CtrTGeneric.getInstance().getFromClass(conn, Eje_ges_unidades.class, Wheres.where("unid_id", "=", unid_id).build());
		}
		else {
			u = CtrGeneric.getInstance().getFromClass(Eje_ges_unidades.class, Wheres.where("unid_id", "=", unid_id).build());
		}
		
		if(u.getFecha_no_vigente() != null) {
			throw new UnidIdWasDeletedException("No se puede cambiar el nombre a una unidad que alguna vez fue eliminada");
		}
		
		u.setUnid_desc(unid_desc);

		return CtrTGeneric.getInstance().upd(conn, u);
	}

	@SuppressWarnings("unused")
	@Override
	public boolean setParent(Connection conn, String unid_id, String unid_id_parent) throws NullPointerException, SQLException {
		if(conn == null || unid_id == null || unid_id_parent == null) {
			throw new NullPointerException();
		}
		
		Eje_ges_jerarquia j = null;
		
		if(conn != null) {
			j = CtrTGeneric.getInstance().getFromClass(conn, Eje_ges_jerarquia.class, Wheres.where("nodo_id", "=", unid_id).build());	
		}
		else {
			j = CtrGeneric.getInstance().getFromClass(Eje_ges_jerarquia.class, Wheres.where("nodo_id", "=", unid_id).build());
		}
		
		j.setNodo_padre(unid_id_parent);

		return CtrTGeneric.getInstance().upd(conn, j);
	}

	@Override
	public int countPersonas(Connection conn, List<String> listaUnidsIds) throws SQLException {
		if(listaUnidsIds == null) {
			throw new NullPointerException();
		}
		if(listaUnidsIds.size() == 0) {
			throw new IndexOutOfBoundsException("No puede ser vacío");
		}
		
		ArrayFactory af = new ArrayFactory();
		af.addAll(listaUnidsIds);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT q=COUNT(*) FROM EJE_GES_TRABAJADOR WHERE unidad in ").append(af.getArrayString());
		
		ConsultaData data = null;
		
		if(conn != null) {
			data = ConsultaTool.getInstance().getData(conn, sql);
		}
		else {
			data = ConsultaTool.getInstance().getData("jndi", sql);
		}
		
		
		int q = ConsultaTool.getInstance().getFirstValue(data, "q", Integer.class);
		
		return q;
	}

	@Override
	public void updateReferences(Connection conn, String unid_id) throws NullPointerException, SQLException, NotExistUnidIdException {
		if(unid_id == null) {
			throw new NullPointerException();
		}
		
		Eje_ges_jerarquia data = null;
		if(conn != null) {
			data = CtrTGeneric.getInstance().getFromClass(conn, Eje_ges_jerarquia.class, Wheres.where("nodo_id", "=", unid_id).build());	
		}
		else {
			data = CtrGeneric.getInstance().getFromClass(Eje_ges_jerarquia.class, Wheres.where("nodo_id", "=", unid_id).build());	
		}
		
		if(data == null) {
			throw new NotExistUnidIdException(unid_id);
		}
		
		ConsultaData dataHijos = null;
		if(conn != null) {
			dataHijos = CtrTGeneric.getInstance().getDataFromClass(conn, Eje_ges_jerarquia.class, Wheres.where("nodo_padre", "=", data.getNodo_padre()).build(), null);	
		}
		else {
			dataHijos = CtrGeneric.getInstance().getDataFromClass(Eje_ges_jerarquia.class, Wheres.where("nodo_padre", "=", data.getNodo_padre()).build(), null);	
		}	
		
		ConsultaData dataSuperior = OrganicaLocator.getInstance().getUnidadesAscendientes(conn, unid_id, true);
		
		data.setNodo_hijos(dataHijos.size());
		data.setNodo_nivel(dataSuperior.size());
	
		if(conn != null) {
			CtrTGeneric.getInstance().upd(conn, data);
		}
		else {
			CtrGeneric.getInstance().upd(data);
		}
	}

	@Override
	public void backupOrganica(Connection conn, int id_movimiento) throws NullPointerException, SQLException {
		
		Collection<Eje_ges_jerarquia> js = null;
		Collection<Eje_ges_unidades> us = null;
		if(conn != null) {
			js = CtrTGeneric.getInstance().getAllFromClass(conn, Eje_ges_jerarquia.class);
			us = CtrTGeneric.getInstance().getAllFromClass(conn, Eje_ges_unidades.class);
		}
		else {
			js = CtrGeneric.getInstance().getAllFromClass(Eje_ges_jerarquia.class);
			us = CtrGeneric.getInstance().getAllFromClass(Eje_ges_unidades.class);
		}	
		
		List<Eje_ges_jerarquia_bkp> jbs = new ArrayList<Eje_ges_jerarquia_bkp>();
		for(Eje_ges_jerarquia j : js) {
			Eje_ges_jerarquia_bkp jb = new Eje_ges_jerarquia_bkp();
			jb.setId_movimiento(id_movimiento);
			
			VoTool.getInstance().copy(j, jb);
			
			jbs.add(jb);
		}
		
		List<Eje_ges_unidades_bkp> ubs = new ArrayList<Eje_ges_unidades_bkp>();
		for(Eje_ges_unidades u : us) {
			Eje_ges_unidades_bkp ub = new Eje_ges_unidades_bkp();
			ub.setId_movimiento(id_movimiento);
			
			VoTool.getInstance().copy(u, ub);
			
			ubs.add(ub);
		}
		
		if(conn != null) {
			CtrTGenericBatch.getInstance().updOrAdd(conn, (List<? extends Vo>) js);
			CtrTGenericBatch.getInstance().updOrAdd(conn, (List<? extends Vo>) ubs);
		}
		else {
			CtrGenericBatch.getInstance().updOrAdd( (List<? extends Vo>) js);
			CtrGenericBatch.getInstance().updOrAdd( (List<? extends Vo>) ubs);
		}
	}
 
}
