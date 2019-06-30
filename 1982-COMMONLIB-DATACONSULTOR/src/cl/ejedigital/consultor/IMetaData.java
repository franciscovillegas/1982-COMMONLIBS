package cl.ejedigital.consultor;

public interface IMetaData {

	Object[] getParams();

	void setParams(Object[] params);

	boolean isPagged();

	void setPagged(boolean pagged);

	boolean isSorted();

	void setSorted(boolean sorted);

	ISenchaPage getPage();

	void setPage(ISenchaPage page);

	String getJndi();

	void setJndi(String jndi);

	String getSqlFullData();

	void setSqlFullData(String sqlFullData);

	String getSqlPaggedData();

	void setSqlPaggedData(String sqlPaggedData);

	String getSqlCountData();

	void setSqlCountData(String sqlCountData);

	String getCatalog();

	void setCatalog(String catalog);

	int getPaggedTotalCount();

	void setPaggedTotalCount(int paggedTotalCount);

}