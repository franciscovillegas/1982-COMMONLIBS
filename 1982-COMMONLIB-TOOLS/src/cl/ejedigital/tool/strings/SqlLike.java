package cl.ejedigital.tool.strings;

import cl.ejedigital.tool.misc.Formatear;

public class SqlLike {
	private String query;
	
	private SqlLike(String query) {
		if(query == null) {
			throw new NullPointerException("No puede ser null ");
		}
		this.query = query;
	}
	
	public static String buildLikeParam(String query) {
		if(query != null) {
			SqlLike like = new SqlLike(query);
			return like.build();
		}
		
		return query;
	}
	
	public String build() {
		String q = Formatear.getInstance().tTrim(query);
		
		String retorno = "%" + q + "%";
		return retorno;
	}
}
