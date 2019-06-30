package cl.ejedigital.consultor.def;

import org.apache.commons.lang.StringEscapeUtils;

import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.output.IDataOut;
import cl.ejedigital.consultor.output.JSonDataOut;
import cl.ejedigital.consultor.output.ReservedWord;

public class DataColumnDef {

	private String	sTitle;
	private String	visible;
	private String	bSearchable;
	private String	bSortable;
	private String	sWidth;
	private String	fnRender;
	
	public DataColumnDef( String	sTitle) {
		this.sTitle = sTitle;
		this.visible = "true";
	}
	
	
	public String getsWidth() {
		return sWidth;
	}

	public DataColumnDef setsWidth(String sWidth) {
		this.sWidth = sWidth;
		return this;
	}

	public String getFnRender() {
		return 	StringEscapeUtils.escapeJavaScript(fnRender);
	}

	public DataColumnDef setFnRender(String fnRender) {
		this.fnRender = fnRender;
		return this;
	}

	public String getsTitle() {
		return sTitle;
	}

	public DataColumnDef setsTitle(String sTitle) {
		this.sTitle = sTitle;
		return this;
	}

	public String getVisible() {
		return visible;
	}

	public DataColumnDef setVisible(String visible) {
		this.visible = visible;
		return this;
	}

	public String getbSearchable() {
		return bSearchable;
	}

	public DataColumnDef setbSearchable(String bSearchable) {
		this.bSearchable = bSearchable;
		return this;
	}

	public String getbSortable() {
		return bSortable;
	}

	public DataColumnDef setbSortable(String bSortable) {
		this.bSortable = bSortable;
		return this;
	}

	public DataFields toDataField(int cantColumnas) {
		DataFields data = new DataFields();

		data.put("sTitle" , this.getsTitle());
		data.put("visible", this.getVisible());
		
		if(this.getbSearchable() != null) 		data.put("bSearchable", this.getbSearchable());
		if(this.getbSortable() != null)			data.put("bSortable",   this.getbSortable());
		if(this.getsWidth() != null) 			data.put("sWidth", 	    this.getsWidth());
		if(this.getFnRender() != null) {
			
			String lFnRender = this.getFnRender() ;
			for(int i = 0 ; i<cantColumnas ; i++) {
				lFnRender = lFnRender.replaceFirst("\\@\\@pos\\[".concat(String.valueOf(i)).concat("\\]"),"\"+obj.aData["+i+"]+\"");
			}

			
			
			
			StringBuilder str = new StringBuilder();
			str.append(" function(obj) { ");
			str.append(" return  \"").append(lFnRender).append("\";");
			str.append(" }");
		
			data.put("fnRender",   new ReservedWord(str.toString()));
		}

		return data;
	}
	
	public IDataOut toJson(int cantColumnas) {
		DataList lista = new DataList();
		lista.add(toDataField(cantColumnas));
		
		JSonDataOut out = new JSonDataOut(lista);
		
		return out;
		
	}

}
