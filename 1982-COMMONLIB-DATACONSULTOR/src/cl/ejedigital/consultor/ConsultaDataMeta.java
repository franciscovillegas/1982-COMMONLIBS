package cl.ejedigital.consultor;

public class ConsultaDataMeta implements IMetaData {
	private Object[]			params;
	
	private boolean 			pagged;
	private boolean				sorted;
	private ISenchaPage	page;
	private String 				jndi;
	private String				sqlFullData;
	private String				sqlPaggedData;
	private String				sqlCountData;
	private String				catalog;
	private int					paggedTotalCount;
	
	@Override
	public Object[] getParams() {
		return params;
	}
	@Override
	public void setParams(Object[] params) {
		this.params = params;
	}
	@Override
	public boolean isPagged() {
		return pagged;
	}
	@Override
	public void setPagged(boolean pagged) {
		this.pagged = pagged;
	}
	@Override
	public boolean isSorted() {
		return sorted;
	}
	@Override
	public void setSorted(boolean sorted) {
		this.sorted = sorted;
	}
	@Override
	public ISenchaPage getPage() {
		return page;
	}
	@Override
	public void setPage(ISenchaPage page) {
		this.page = page;
	}
	@Override
	public String getJndi() {
		return jndi;
	}
	@Override
	public void setJndi(String jndi) {
		this.jndi = jndi;
	}
	@Override
	public String getSqlFullData() {
		return sqlFullData;
	}
	@Override
	public void setSqlFullData(String sqlFullData) {
		this.sqlFullData = sqlFullData;
	}
	@Override
	public String getSqlPaggedData() {
		return sqlPaggedData;
	}
	@Override
	public void setSqlPaggedData(String sqlPaggedData) {
		this.sqlPaggedData = sqlPaggedData;
	}
	@Override
	public String getSqlCountData() {
		return sqlCountData;
	}
	@Override
	public void setSqlCountData(String sqlCountData) {
		this.sqlCountData = sqlCountData;
	}
	@Override
	public String getCatalog() {
		return catalog;
	}
	@Override
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	@Override
	public int getPaggedTotalCount() {
		return paggedTotalCount;
	}
	@Override
	public void setPaggedTotalCount(int paggedTotalCount) {
		this.paggedTotalCount = paggedTotalCount;
	}
	
	 
		
}
