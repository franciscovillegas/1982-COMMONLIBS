package cl.eje.bootstrap.abs;

import cl.eje.bootstrap.ifacetemplatesetter.ITemplateSetter;

public class AbsTemplateSetter implements ITemplateSetter {

	private boolean header;
	
	private String title;
	private String body;
	private String icon;
	private String footer;
	
	
	@Override
	public void setIcon(String icon) {
		this.icon = icon;
		
	}
	@Override
	public void setHeader(boolean header) {
		this.header = header;
	}
	@Override
	public String getIcon() {
		return this.icon;
	}
	@Override
	public boolean isHeader() {
		return this.header;
	}
	@Override
	public String getTitle() {
		return this.title;
	}
	@Override
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public String getBody() {
		return this.body;
	}
	@Override
	public void setBody(String body) {
		this.body = body;
	}
	@Override
	public String getFooter() {
		return this.footer;
	}
	@Override
	public void setFooter(String footer) {
		this.footer = footer;
	}
	
}
