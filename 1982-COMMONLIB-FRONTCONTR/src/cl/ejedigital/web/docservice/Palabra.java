package cl.ejedigital.web.docservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import cl.eje.model.generic.portal.Eje_doc_pagina;
import cl.ejedigital.tool.strings.MyString;
import cl.ejedigital.tool.strings.SqlBuilder;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.factory.Weak;
import portal.com.eje.portal.transactions.TransactionConnection;

class Palabra {
	private final String JNDI = "portal";

	static Palabra getIntance() {
		return Weak.getInstance(Palabra.class);
	}

	public void referenciaPalabras(TransactionConnection cons, List<Eje_doc_pagina> paginas) throws SQLException {
		Connection conn = cons.getConnection(JNDI);
		SqlBuilder sql = new SqlBuilder();

		sql.line("declare @tabla table (id_palabra int,  ");
		sql.line("						id_pag	   int) ");
		for (Eje_doc_pagina pag : paginas) {
			List<String> palSinRepetir = getPalabrasSinRepetir(pag);

			for (String p : palSinRepetir) {
				p = MyString.getInstance().normalizaString(p);
				
				sql.line("if(not exists(select top 1 1 from eje_doc_palabra where palabra = '").append(StringEscapeUtils.escapeSql(p)).append("')) ");
				sql.line("	begin ");
				sql.line("		insert into eje_doc_palabra (palabra) values  ('").append(StringEscapeUtils.escapeSql(p)).append("')");
				sql.line("		insert into @tabla (id_palabra, id_pag) values (@@IDENTITY,").append(pag.getId_pag()).append(")");
				sql.line("	end ");
				sql.line("	else ");
				sql.line("	begin");
				sql.line("		insert into @tabla (id_palabra, id_pag) ");
				sql.line("		select id_palabra, id_pag=").append(pag.getId_pag()).append(" ");
				sql.line("		from eje_doc_palabra ");
				sql.line("		where palabra =  '").append(StringEscapeUtils.escapeSql(p)).append("' ");
				sql.line("	end ");
			}
		}

		sql.line(" insert into eje_doc_pagina_palabra ");
		sql.line(" 	(id_palabra, id_pag ) ");
		sql.line(" select id_palabra, id_pag ");
		sql.line(" from @tabla ");
		ConsultaTool.getInstance().executeBatch(conn, sql);

	}
	
	public	List<String> getPalabrasSinRepetir(Eje_doc_pagina pag) {
		List<String> palUnicas = new ArrayList<>();
		
		if(pag != null) {
			String[] palabras = MyString.getInstance().quitaEspacios(MyString.getInstance().normalizaStringFromXml(pag.getContenido())).split(" ");
			
			for (String p : palabras) {
				if (!palUnicas.contains(p) && p != null && !p.equals("")) {
					palUnicas.add(p);
				}
			}
		}
		
		return palUnicas;
	
	}
}
