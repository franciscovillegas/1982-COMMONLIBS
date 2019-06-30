package cl.eje.bootstrap.ifacetemplatesetter;

public interface ITemplateSetter {

	void setIcon(String icon);
	
	void setHeader(boolean header);
	
	String getIcon();
	
	boolean isHeader();
	
	String getTitle();

	void setTitle(String title);

	String getBody();

	void setBody(String body);
	
	public String getFooter();

	public void setFooter(String footer);

 
	
}
