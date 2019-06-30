package portal.com.eje.portal.vo;

import java.util.LinkedList;
import java.util.List;

public class BatchSqlSingleton {
	private List<CharSequence> sqls;
	private List<Object[]> listaParams;

	public BatchSqlSingleton() {
		super();
		this.sqls = new LinkedList<CharSequence>();
		this.listaParams = new LinkedList<Object[]>();
	}

	public void addBatch(CharSequence sql, Object[] params) {
		sqls.add(sql);
		listaParams.add(params);
	}

	public List<CharSequence> getSqls() {
		return sqls;
	}

	public List<Object[]> getListaParams() {
		return listaParams;
	}
	
	public void addAll(BatchSqlSingleton batch) {
		if(batch == null) {
			throw new NullPointerException();
		}
		
		this.sqls.addAll(batch.getSqls());
		this.listaParams.addAll(batch.getListaParams());
	}
	
	public CharSequence getSqlConcatenate() {
		StringBuilder sql = new StringBuilder();
		
		for(CharSequence c : sqls) {
			sql.append(c).append("\n");
		}
		
		return sql;
	}

	public Object[] getParamConcatenate() {
		List<Object> listaParams = new LinkedList<Object>();
		
		for(Object[] params : this.listaParams) {
			for(Object o : params) {
				listaParams.add(o);
			}
		}
		
		return listaParams.toArray();
	}
}
