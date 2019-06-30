package cl.ejedigital.web.docservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import cl.eje.model.generic.portal.Eje_doc_doc;
import cl.eje.model.generic.portal.Eje_doc_pagina;
import cl.eje.model.generic.portal.Eje_doc_responsable;
import cl.eje.model.generic.portal.Eje_doc_version;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.strings.SqlBuilder;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.factory.Weak;
import portal.com.eje.portal.transactions.TransactionConnection;
import portal.com.eje.portal.vo.CtrTGeneric;
import portal.com.eje.portal.vo.util.Wheres;

class Version {
	private final String JNDI = "portal";
	private final Pagina pagina = Pagina.getIntance();
	public static final String CAMPO_ID_VER = "id_ver";
	
	public static Version getIntance() {
		return Weak.getInstance(Version.class);
	}

	public Eje_doc_version addVersion(TransactionConnection cons, Eje_doc_responsable r, Eje_doc_doc doc, String contenidoDelDoc) throws NullPointerException, SQLException {
		Connection conn = cons.getConnection(JNDI);
		
		Eje_doc_version versionVo = new Eje_doc_version();
		versionVo.setId_doc(doc.getId_doc());
		versionVo.setFecha_update(ConsultaTool.getInstance().getNow(conn));
		versionVo.setRut_update(r.getRut());
		
		CtrTGeneric.getInstance().add(conn, versionVo);
		
		List<Eje_doc_pagina> paginas = pagina.addPaginas(cons, doc, versionVo, contenidoDelDoc);
		
		versionVo.setPaginas(paginas.size());
		CtrTGeneric.getInstance().upd(conn, versionVo);
		
		
		return versionVo;
	}

	public Eje_doc_version getLastVersion(TransactionConnection cons, Eje_doc_doc docToGet) throws SQLException {
		return getVersion(cons, docToGet, privateGetLastVersionId(cons, docToGet));
		
	}
	
	private Integer privateGetLastVersionId(TransactionConnection cons, Eje_doc_doc docToGet) throws SQLException {
		SqlBuilder sql = new SqlBuilder();
		sql.line("select id_ver = isnull(max(id_ver),-1) from eje_doc_version where id_doc = ? order by id_ver desc ");
		Object[] params = {docToGet.getId_doc()};
		ConsultaData data = ConsultaTool.getInstance().getData(cons.getPortal(), sql, params);
		Integer id_ver = ConsultaTool.getInstance().getFirstValue(data, "id_ver", Integer.class);
		return id_ver;
	}
	
	public Eje_doc_version getVersion(TransactionConnection cons, Eje_doc_doc docToGet, Integer id_ver) throws NullPointerException, SQLException {
		return CtrTGeneric.getInstance().getFromClass(cons.getPortal(), Eje_doc_version.class, Wheres.where(CAMPO_ID_VER, Wheres.EQUALS, id_ver).build());
	}
}
