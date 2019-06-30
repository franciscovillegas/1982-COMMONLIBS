package portal.com.eje.frontcontroller.resobjects;

public enum EnumTypeCode {
	html ("<!--","", "-->"),
	javascript ("/*","","*/"),
	css ("/*","","*/");
	private String ini;
	private String mid;
	private String end;
	
	EnumTypeCode(String ini, String mid, String end) {
		this.ini = ini;
		this.mid = mid;
		this.end = end;
	}
	
	public String getCommentIni() {
		return this.ini;
	}
	
	public String getCommentMid() {
		return this.mid;
	}
	
	public String getCommentEnd() {
		return this.end;
	}
	
	
	
}
