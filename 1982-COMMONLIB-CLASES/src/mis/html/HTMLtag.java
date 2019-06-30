package mis.html;



public class HTMLtag {
	public String Tipo=null;
	
	protected HTMLtag(String tag) {
		Tipo = tag;
	}
	
	public HTMLtag() {
		Tipo = "NULL";
	}

	public static HTMLtag ConvertStrToTag(String StrTag) {
		HTMLtag tmp = null;
		
		if ("NULL".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.HTML;
		else if("COMODIN".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.COMODIN;
		else if("!DOCTYPE".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.DOCTYPE;
		else if("COMENTARIO".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.COMENTARIO;
		else if("A".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.A;
		else if("ADDRESS".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.ADDRESS;
		else if("APPLET".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.APPLET;
		else if("AREA".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.AREA;
		else if("B".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.B;
		else if("BASE".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.BASE;
		else if("BASEFONT".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.BASEFONT;
		else if("BIG".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.BIG;
		else if("BLOCKQUOTE".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.BLOCKQUOTE;
		else if("BODY".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.BODY;
		else if("BR".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.BR;
		else if("CAPTION".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.CAPTION;
		else if("CENTER".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.CENTER;
		else if("CITE".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.CITE;
		else if("CODE".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.CODE;
		else if("DD".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.DD;
		else if("DFN".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.DFN;
		else if("DIR".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.DIR;
		else if("DIV".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.DIV;
		else if("DL".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.DL;
		else if("DT".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.DT;
		else if("EM".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.EM;
		else if("FONT".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.FONT;
		else if("FORM".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.FORM;
		else if("FRAME".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.FRAME;
		else if("FRAMESET".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.FRAMESET;
		else if("H1".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.H1;
		else if("H2".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.H2;
		else if("H3".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.H3;
		else if("H4".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.H4;
		else if("H5".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.H5;
		else if("H6".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.H6;
		else if("HEAD".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.HEAD;
		else if("HR".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.HR;
		else if("HTML".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.HTML;
		else if("I".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.HTML;
		else if("IMG".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.IMG;
		else if("INPUT".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.INPUT;
		else if("ISINDEX".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.HTML;
		else if("KBD".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.KBD;
		else if("LI".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.LI;
		else if("LINK".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.LINK;
		else if("MAP".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.MAP;
		else if("MENU".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.MENU;
		else if("META".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.META;
		else if("NOFRAMES".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.NOFRAMES;
		else if("OBJECT".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.OBJECT;
		else if("OL".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.OL;
		else if("OPTION".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.OPTION;
		else if("P".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.P;
		else if("PARAM".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.PARAM;
		else if("PRE".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.PRE;
		else if("SAMP".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.SAMP;
		else if("SCRIPT".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.SCRIPT;
		else if("SELECT".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.SELECT;
		else if("SMALL".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.SMALL;
		else if("SPAN".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.SPAN;
		else if("STRIKE".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.STRIKE;
		else if("S".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.S;
		else if("STRONG".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.STRONG;
		else if("STYLE".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.STYLE;
		else if("SUB".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.SUB;
		else if("SUP".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.SUP;
		else if("TABLE".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.TABLE;
		else if("TD".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.TD;
		else if("TEXTAREA".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.TEXTAREA;
		else if("TH".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.TH;
		else if("TITLE".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.TITLE;
		else if("TR".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.TR;
		else if("TT".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.TT;
		else if("U".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.U;
		else if("UL".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.UL;
		else if("VAR".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.VAR;
		else if("IMPLIED".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.IMPLIED;
		else if("CONTENT".equalsIgnoreCase(StrTag))
			tmp = HTMLtag.CONTENT;
		else
			tmp = HTMLtag.DESCONOCIDO;
		
		return tmp;	
	}

	public final static HTMLtag DESCONOCIDO = new HTMLtag("desconocido");
	public final static HTMLtag COMODIN = new HTMLtag("comodin");
	public final static HTMLtag NULL = new HTMLtag("null");
	public final static HTMLtag DOCTYPE = new HTMLtag("doctype");
	public final static HTMLtag COMENTARIO  = new HTMLtag("comment");
	public static final HTMLtag A = new HTMLtag("a");
	public static final HTMLtag ADDRESS = new HTMLtag("address");
	public static final HTMLtag APPLET = new HTMLtag("applet");
	public static final HTMLtag AREA = new HTMLtag("area");
	public static final HTMLtag B = new HTMLtag("b");
	public static final HTMLtag BASE = new HTMLtag("base");
	public static final HTMLtag BASEFONT = new HTMLtag("basefont");
	public static final HTMLtag BIG = new HTMLtag("big");
	public static final HTMLtag BLOCKQUOTE = new HTMLtag("blockquote");
	public static final HTMLtag BODY = new HTMLtag("body");
	public static final HTMLtag BR = new HTMLtag("br");
	public static final HTMLtag CAPTION = new HTMLtag("caption");
	public static final HTMLtag CENTER = new HTMLtag("center");
	public static final HTMLtag CITE = new HTMLtag("cite");
	public static final HTMLtag CODE = new HTMLtag("code");
	public static final HTMLtag DD = new HTMLtag("dd");
	public static final HTMLtag DFN = new HTMLtag("dfn");
	public static final HTMLtag DIR = new HTMLtag("dir");
	public static final HTMLtag DIV = new HTMLtag("div");
	public static final HTMLtag DL = new HTMLtag("dl");
	public static final HTMLtag DT = new HTMLtag("dt");
	public static final HTMLtag EM = new HTMLtag("em");
	public static final HTMLtag FONT = new HTMLtag("font");
	public static final HTMLtag FORM = new HTMLtag("form");
	public static final HTMLtag FRAME = new HTMLtag("frame");
	public static final HTMLtag FRAMESET = new HTMLtag("frameset");
	public static final HTMLtag H1 = new HTMLtag("h1");
	public static final HTMLtag H2 = new HTMLtag("h2");
	public static final HTMLtag H3 = new HTMLtag("h3");
	public static final HTMLtag H4 = new HTMLtag("h4");
	public static final HTMLtag H5 = new HTMLtag("h5");
	public static final HTMLtag H6 = new HTMLtag("h6");
	public static final HTMLtag HEAD = new HTMLtag("head");
	public static final HTMLtag HR = new HTMLtag("hr");
	public static final HTMLtag HTML = new HTMLtag("html");
	public static final HTMLtag I = new HTMLtag("i");
	public static final HTMLtag IMG = new HTMLtag("img");
	public static final HTMLtag INPUT = new HTMLtag("input");
	public static final HTMLtag ISINDEX = new HTMLtag("isindex");
	public static final HTMLtag KBD = new HTMLtag("kbd");
	public static final HTMLtag LI = new HTMLtag("li");
	public static final HTMLtag LINK = new HTMLtag("link");
	public static final HTMLtag MAP = new HTMLtag("map");
	public static final HTMLtag MENU = new HTMLtag("menu");
	public static final HTMLtag META = new HTMLtag("meta");
	public static final HTMLtag NOFRAMES = new HTMLtag("noframes");
	public static final HTMLtag OBJECT = new HTMLtag("object");
	public static final HTMLtag OL = new HTMLtag("ol");
	public static final HTMLtag OPTION = new HTMLtag("option");
	public static final HTMLtag P = new HTMLtag("p");
	public static final HTMLtag PARAM = new HTMLtag("param");
	public static final HTMLtag PRE = new HTMLtag("pre");
	public static final HTMLtag SAMP = new HTMLtag("samp");
	public static final HTMLtag SCRIPT = new HTMLtag("script");
	public static final HTMLtag SELECT = new HTMLtag("select");
	public static final HTMLtag SMALL = new HTMLtag("small");
	public static final HTMLtag SPAN = new HTMLtag("span");
	public static final HTMLtag STRIKE = new HTMLtag("strike");
	public static final HTMLtag S = new HTMLtag("s");
	public static final HTMLtag STRONG = new HTMLtag("strong");
	public static final HTMLtag STYLE = new HTMLtag("style");
	public static final HTMLtag SUB = new HTMLtag("sub");
	public static final HTMLtag SUP = new HTMLtag("sup");
	public static final HTMLtag TABLE = new HTMLtag("table");
	public static final HTMLtag TD = new HTMLtag("td");
	public static final HTMLtag TEXTAREA = new HTMLtag("textarea");
	public static final HTMLtag TH = new HTMLtag("th");
	public static final HTMLtag TITLE = new HTMLtag("title");
	public static final HTMLtag TR = new HTMLtag("tr");
	public static final HTMLtag TT = new HTMLtag("tt");
	public static final HTMLtag U = new HTMLtag("u");
	public static final HTMLtag UL = new HTMLtag("ul");
	public static final HTMLtag VAR = new HTMLtag("var");
	public static final HTMLtag IMPLIED = new HTMLtag("p-implied");
	public static final HTMLtag CONTENT = new HTMLtag("content");
}