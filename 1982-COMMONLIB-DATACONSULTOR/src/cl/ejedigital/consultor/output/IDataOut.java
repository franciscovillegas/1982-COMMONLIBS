package cl.ejedigital.consultor.output;

import java.util.List;



public interface IDataOut {

	public String getListData();
	
	public String getListData(String[] order);
	
	public String getListDataFiltered(String[] only); 
	
	public void setFilter(List<String> l);
	
	public int getSize();
	
}
