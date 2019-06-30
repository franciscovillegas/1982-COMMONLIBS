package cl.ejedigital.web.docservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cl.eje.model.generic.portal.Eje_doc_doc;
import cl.eje.model.generic.portal.Eje_doc_doc_tag;
import cl.eje.model.generic.portal.Eje_doc_tag;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.ISenchaPage;
import cl.ejedigital.tool.strings.SqlBuilder;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.factory.Weak;
import portal.com.eje.portal.transactions.TransactionConnection;
import portal.com.eje.portal.vo.CtrTGeneric;
import portal.com.eje.portal.vo.VoTool;
import portal.com.eje.portal.vo.util.Wheres;

class Tag {
	private final String CAMPO_ID_TAG ="id_tag";
	private final String CAMPO_ID_DOC ="id_doc";
	private final String CAMPO_NOMBRE ="nombre";
	
	public static Tag getIntance() {
		return Weak.getInstance(Tag.class);
	}
	
	public boolean addTag(TransactionConnection cons, Eje_doc_doc doc, String tag) throws NullPointerException, SQLException {
		Connection connPortal = cons.getPortal();
		
		if(cons != null && doc != null) {
			tag = DocListenersTool.getIntance().onNormalizeTag(tag);
			
			Eje_doc_tag docTag = getTag(cons, tag);
			if(docTag == null) {
				docTag = new Eje_doc_tag();
				docTag.setNombre(tag);
				
				CtrTGeneric.getInstance().add(connPortal, docTag);
			}
			
			
			Eje_doc_doc_tag tagDelDoc = getTag(cons, doc, docTag);
			if(tagDelDoc == null) {
				tagDelDoc = new Eje_doc_doc_tag();
				tagDelDoc.setId_doc(doc.getId_doc());
				tagDelDoc.setId_tag(docTag.getId_tag());
				
				CtrTGeneric.getInstance().add(connPortal, tagDelDoc);
			}
		}
		
		return true;
	}
	
	public Collection<TagSelected> getTags(TransactionConnection cons, Eje_doc_doc doc, String query, ISenchaPage page) throws SQLException {
		Connection connPortal = cons.getPortal();
		List<Object> params = new ArrayList<>();
		
		SqlBuilder sql = new SqlBuilder();
		sql.line(" select distinct t.*,selected=case when COUNT(d.id_doc)>0 then 1 else 0 end");
		sql.line(" from eje_doc_tag t");
		sql.line(" 	left join eje_doc_doc_tag dt on t.id_tag = dt.id_tag ");
		sql.line(" 	left join eje_doc_doc d on d.id_doc = dt.id_doc and d.id_doc = ? ");
		
		params.add(doc.getId_doc());
		
		if(query != null) {
			query = "%"+query.replaceAll("  ", " ").replaceAll(" ", "%")+"%";
			sql.line(" where t.nombre like ? ");
			
			params.add(query);
		}
		sql.line(" 	 group by t.id_tag, t.nombre ");
		String sqlFinal = "select * from ("+sql.toString()+") as m order by m.selected desc ";
	
		
		
		ConsultaData data = ConsultaTool.getInstance().getData(connPortal, sqlFinal, params.toArray());
		return VoTool.getInstance().buildVo(data, TagSelected.class);
	}
	
	public Eje_doc_doc_tag getTag(TransactionConnection cons, Eje_doc_doc doc, Eje_doc_tag tag) throws NullPointerException, SQLException {
		Connection connPortal = cons.getPortal();
		Eje_doc_doc_tag docTag = CtrTGeneric.getInstance().getFromClass(connPortal, Eje_doc_doc_tag.class, 
				Wheres.where(CAMPO_ID_TAG, Wheres.EQUALS, tag.getId_tag())
				.and(CAMPO_ID_DOC, Wheres.EQUALS, doc.getId_doc())
				.build());
		return docTag;
	}
	public Eje_doc_tag getTag(TransactionConnection cons, String tag) throws NullPointerException, SQLException {
		Connection connPortal = cons.getPortal();
		Eje_doc_tag docTag = CtrTGeneric.getInstance().getFromClass(connPortal, Eje_doc_tag.class, Wheres.where(CAMPO_NOMBRE, Wheres.EQUALS, tag).build());
		return docTag;
	}

}
