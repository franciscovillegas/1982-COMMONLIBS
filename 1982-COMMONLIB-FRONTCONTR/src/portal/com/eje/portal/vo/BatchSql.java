package portal.com.eje.portal.vo;

import java.util.List;

public class BatchSql {
	private CharSequence sql;
	private List<Object[]> params;

	public BatchSql(CharSequence sql, List<Object[]> params) {
		super();
		this.sql = sql;
		this.params = params;
	}

	public CharSequence getSql() {
		return sql;
	}

	public List<Object[]> getParams() {
		return params;
	}

}
