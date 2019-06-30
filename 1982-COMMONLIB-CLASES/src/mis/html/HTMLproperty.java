package mis.html;


public class HTMLproperty{
	public String Tipo;
	
	public HTMLproperty() {
		Tipo = null;
	}
	public HTMLproperty(String name) {
		Tipo = name;
	}
	
	public static HTMLproperty ConvertStrToproperty(String StrProp) {
		HTMLproperty prop=null;
		
		if ("NULL".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.NULL;
		else if ("FACE".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.FACE;
		else if ("COMMENT".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.COMMENT;
		else if ("SIZE".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.SIZE;
		else if ("COLOR".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.COLOR;
		else if ("CLEAR".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.CLEAR;
		else if ("BACKGROUND".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.BACKGROUND;
		else if ("BGCOLOR".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.BGCOLOR;
		else if ("TEXT".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.TEXT;
		else if ("LINK".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.LINK;
		else if ("VLINK".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.VLINK;
		else if ("ALINK".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.ALINK;
		else if ("WIDTH".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.WIDTH;
		else if ("HEIGHT".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.HEIGHT;
		else if ("ALIGN".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.ALIGN;
		else if ("NAME".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.NAME;
		else if ("HREF".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.HREF;
		else if ("REL".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.REL;
		else if ("REV".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.REV;
		else if ("TITLE".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.TITLE;
		else if ("TARGET".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.TARGET;
		else if ("SHAPE".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.SHAPE;
		else if ("COORDS".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.COORDS;
		else if ("ISMAP".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.ISMAP;
		else if ("NOHREF".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.NOHREF;
		else if ("ALT".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.ALT;
		else if ("ID".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.ID;
		else if ("SRC".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.SRC;
		else if ("HSPACE".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.HSPACE;
		else if ("VSPACE".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.VSPACE;
		else if ("USEMAP".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.USEMAP;
		else if ("LOWSRC".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.LOWSRC;
		else if ("CODEBASE".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.CODEBASE;
		else if ("CODE".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.CODE;
		else if ("ARCHIVE".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.ARCHIVE;
		else if ("VALUE".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.VALUE;
		else if ("VALUETYPE".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.VALUETYPE;
		else if ("TYPE".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.TYPE;
		else if ("CLASS".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.CLASS;
		else if ("STYLE".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.STYLE;
		else if ("LANG".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.LANG;
		else if ("DIR".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.DIR;
		else if ("DECLARE".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.DECLARE;
		else if ("CLASSID".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.CLASSID;
		else if ("DATA".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.DATA;
		else if ("CODETYPE".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.CODETYPE;
		else if ("STANDBY".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.STANDBY;
		else if ("BORDER".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.BORDER;
		else if ("SHAPES".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.SHAPES;
		else if ("NOSHADE".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.NOSHADE;
		else if ("COMPACT".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.COMPACT;
		else if ("START".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.START;
		else if ("ACTION".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.ACTION;
		else if ("METHOD".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.METHOD;
		else if ("ENCTYPE".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.ENCTYPE;
		else if ("CHECKED".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.CHECKED;
		else if ("MAXLENGTH".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.MAXLENGTH;
		else if ("MULTIPLE".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.MULTIPLE;
		else if ("SELECTED".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.SELECTED;
		else if ("ROWS".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.ROWS;
		else if ("COLS".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.COLS;
		else if ("DUMMY".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.DUMMY;
		else if ("CELLSPACING".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.CELLSPACING;
		else if ("CELLPADDING".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.CELLPADDING;
		else if ("VALIGN".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.VALIGN;
		else if ("HALIGN".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.HALIGN;
		else if ("NOWRAP".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.NOWRAP;
		else if ("ROWSPAN".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.ROWSPAN;
		else if ("COLSPAN".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.COLSPAN;
		else if ("PROMPT".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.PROMPT;
		else if ("HTTPEQUIV".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.HTTPEQUIV;
		else if ("LANGUAGE".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.LANGUAGE;
		else if ("VERSION".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.VERSION;
		else if ("N".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.N;
		else if ("FRAMEBORDER".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.FRAMEBORDER;
		else if ("MARGINWIDTH".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.MARGINWIDTH;
		else if ("MARGINHEIGHT".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.MARGINHEIGHT;
		else if ("SCROLLING".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.SCROLLING;
		else if ("NORESIZE".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.NORESIZE;
		else if ("MEDIA".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.MEDIA;
		else if ("ENDTAG".equalsIgnoreCase(StrProp))
			prop =  HTMLproperty.ENDTAG;
		else
			prop =  HTMLproperty.DESCONOCIDO;
		
		return prop;
	}
	
	public final static HTMLproperty DESCONOCIDO = new HTMLproperty("desconocido");
	public static final HTMLproperty NULL = new HTMLproperty("null");
	public static final HTMLproperty FACE = new HTMLproperty("face");
	public static final HTMLproperty COMMENT = new HTMLproperty("comment");
	public static final HTMLproperty SIZE  = new HTMLproperty("size");
	public static final HTMLproperty COLOR  = new HTMLproperty("color");
	public static final HTMLproperty CLEAR  = new HTMLproperty("clear");
	public static final HTMLproperty BACKGROUND  = new HTMLproperty("background");
	public static final HTMLproperty BGCOLOR  = new HTMLproperty("bgcolor");
	public static final HTMLproperty TEXT  = new HTMLproperty("text");
	public static final HTMLproperty LINK  = new HTMLproperty("link");
	public static final HTMLproperty VLINK  = new HTMLproperty("vlink");
	public static final HTMLproperty ALINK = new HTMLproperty("alink");
	public static final HTMLproperty WIDTH = new HTMLproperty("width");
	public static final HTMLproperty HEIGHT = new HTMLproperty("height");
	public static final HTMLproperty ALIGN = new HTMLproperty("align");
	public static final HTMLproperty NAME = new HTMLproperty("name");
	public static final HTMLproperty HREF = new HTMLproperty("href");
	public static final HTMLproperty REL = new HTMLproperty("rel");
	public static final HTMLproperty REV = new HTMLproperty("rev");
	public static final HTMLproperty TITLE = new HTMLproperty("title");
	public static final HTMLproperty TARGET = new HTMLproperty("target");
	public static final HTMLproperty SHAPE = new HTMLproperty("shape");
	public static final HTMLproperty COORDS = new HTMLproperty("coords");
	public static final HTMLproperty ISMAP = new HTMLproperty("ismap");
	public static final HTMLproperty NOHREF = new HTMLproperty("nohref");
	public static final HTMLproperty ALT = new HTMLproperty("alt");
	public static final HTMLproperty ID = new HTMLproperty("id");
	public static final HTMLproperty SRC = new HTMLproperty("src");
	public static final HTMLproperty HSPACE = new HTMLproperty("hspace");
	public static final HTMLproperty VSPACE = new HTMLproperty("vspace");
	public static final HTMLproperty USEMAP = new HTMLproperty("usemap");
	public static final HTMLproperty LOWSRC = new HTMLproperty("lowsrc");
	public static final HTMLproperty CODEBASE = new HTMLproperty("codebase");
	public static final HTMLproperty CODE = new HTMLproperty("code");
	public static final HTMLproperty ARCHIVE = new HTMLproperty("archive");
	public static final HTMLproperty VALUE = new HTMLproperty("value");
	public static final HTMLproperty VALUETYPE = new HTMLproperty("valuetype");
	public static final HTMLproperty TYPE = new HTMLproperty("type");
	public static final HTMLproperty CLASS = new HTMLproperty("class");
	public static final HTMLproperty STYLE = new HTMLproperty("style");
	public static final HTMLproperty LANG = new HTMLproperty("lang");
	public static final HTMLproperty DIR = new HTMLproperty("dir");
	public static final HTMLproperty DECLARE = new HTMLproperty("declare");
	public static final HTMLproperty CLASSID = new HTMLproperty("classid");
	public static final HTMLproperty DATA = new HTMLproperty("data");
	public static final HTMLproperty CODETYPE = new HTMLproperty("cadetype");
	public static final HTMLproperty STANDBY = new HTMLproperty("stanby");
	public static final HTMLproperty BORDER = new HTMLproperty("border");
	public static final HTMLproperty SHAPES = new HTMLproperty("shapes");
	public static final HTMLproperty NOSHADE = new HTMLproperty("noshape");
	public static final HTMLproperty COMPACT = new HTMLproperty("compact");
	public static final HTMLproperty START = new HTMLproperty("start");
	public static final HTMLproperty ACTION = new HTMLproperty("action");
	public static final HTMLproperty METHOD = new HTMLproperty("method");
	public static final HTMLproperty ENCTYPE = new HTMLproperty("enctype");
	public static final HTMLproperty CHECKED = new HTMLproperty("checked");
	public static final HTMLproperty MAXLENGTH = new HTMLproperty("maxlength");
	public static final HTMLproperty MULTIPLE = new HTMLproperty("multiple");
	public static final HTMLproperty SELECTED = new HTMLproperty("selected");
	public static final HTMLproperty ROWS = new HTMLproperty("rows");
	public static final HTMLproperty COLS = new HTMLproperty("cols");
	public static final HTMLproperty DUMMY = new HTMLproperty("dummy");
	public static final HTMLproperty CELLSPACING = new HTMLproperty("cellspacing");
	public static final HTMLproperty CELLPADDING = new HTMLproperty("cellpadding");
	public static final HTMLproperty VALIGN = new HTMLproperty("valign");
	public static final HTMLproperty HALIGN = new HTMLproperty("halign");
	public static final HTMLproperty NOWRAP = new HTMLproperty("nowrap");
	public static final HTMLproperty ROWSPAN = new HTMLproperty("rowspan");
	public static final HTMLproperty COLSPAN = new HTMLproperty("colspan");
	public static final HTMLproperty PROMPT = new HTMLproperty("prompt");
	public static final HTMLproperty HTTPEQUIV = new HTMLproperty("httpequiv");
	public static final HTMLproperty CONTENT = new HTMLproperty("content");
	public static final HTMLproperty LANGUAGE = new HTMLproperty("language");
	public static final HTMLproperty VERSION = new HTMLproperty("version");
	public static final HTMLproperty         N = new HTMLproperty("n");
	public static final HTMLproperty FRAMEBORDER  = new HTMLproperty("frameborder");
	public static final HTMLproperty MARGINWIDTH  = new HTMLproperty("marginwidth");
	public static final HTMLproperty MARGINHEIGHT = new HTMLproperty("marginheight");
	public static final HTMLproperty SCROLLING = new HTMLproperty("scrolling");
	public static final HTMLproperty NORESIZE = new HTMLproperty("noresize");
	public static final HTMLproperty MEDIA = new HTMLproperty("media");
	public static final HTMLproperty ENDTAG = new HTMLproperty("endtag");
}