package mis.html;


import java.nio.CharBuffer;

import mis.pila.Pila;

public class HTMLnode {
	private int PosCerrado=-1;
	private int PosAbierto=-1;
	private boolean EsTagDeCierre = false;
	private boolean EstaCerrado = false;
	private HTMLtag tag=null;
	private String StrNombre = null;
	private  Pila Propiedades=null;
	public Pila Hijos = null;
	public HTMLnode NodoPareja = null;
	
	public HTMLnode(String Nombre_) {
		StrNombre = Nombre_;
		tag = HTMLtag.ConvertStrToTag(Nombre_);
		Propiedades = new Pila();
	}
	
	public HTMLnode(String Nombre_,int PosAbierto_,int PosCerrado_,boolean EsUnTagDeCierre,boolean EstaCerradoDeFormaCorta) {
		Propiedades = new Pila();
		StrNombre = Nombre_;
		PosAbierto = PosAbierto_;
		PosCerrado = PosCerrado_;
		EsTagDeCierre = EsUnTagDeCierre;
		EstaCerrado = EstaCerradoDeFormaCorta;
		tag = HTMLtag.ConvertStrToTag(Nombre_);
		Hijos = new Pila();
	}
	
	public HTMLtag GetTag() {
		return ( tag );
	}
	
	public int PosCerrado() {
		return( PosCerrado );
	}
	
	public int PosAbierto() {
		return( PosAbierto );
	}

    public String Nombre() {
    	return( StrNombre );
    }
    
    public String NombreTag() {
    	if(this.EsUnTagDeCierra()) {
    		return ("</"+this.StrNombre+">");
    	}
    	else {
    		if(this.EstaCerradoDeFormaCorta()) {
    			return ("<"+this.StrNombre+"/>");
    		}
    		else {
    			return ("<"+this.StrNombre+">");
    		}
    	}
    }
     
    public String NombreTagWithProp() {
    	if(this.EsUnTagDeCierra()) { 
    		return ("</"+this.StrNombre + this.GetStrPropiedades() +  ">");
    	}
    	else {
    		if(this.EstaCerradoDeFormaCorta()) {
    			return ("<"+this.StrNombre+ this.GetStrPropiedades() + "/>");
    		}
    		else {
    			return ("<"+this.StrNombre+ this.GetStrPropiedades() + ">");
    		}
    	}
    }
    	
    public String GetStrPropiedades() {
    	Pila tmp = new Pila();
    	HTMLpropiedad prop = null;
    	String str = " ";
    	
    	while (!Propiedades.vacia()) {
    		prop =(HTMLpropiedad) Propiedades.desApilar();
    		str = str + prop.GetPropStr() + "=" + prop.GetValor() + " ";
    	    tmp.apilar(prop);
    	}
    	tmp.volcar(Propiedades);
    	return str;
    }
    
    public void Nombre( String Nombre ) {
    	StrNombre = Nombre;
    }
    
    public boolean EsUnTagDeCierra() {
    	return ( EsTagDeCierre );
    }
    
    public boolean EstaCerradoDeFormaCorta() {
    	return ( EstaCerrado );
    }
    
    public void AddHijo(HTMLnode Hijo ) {
    	Hijos.apilar(Hijo);
    } 
    
    public int CantHijos() {
    	return ( Hijos.Count() );    	
    }
    
    public void AddPareja(HTMLnode Pareja,CharBuffer Buffer) {
    	char Contenido[] = null;
    	char Props[]=null;
    	
    	this.NodoPareja = Pareja;
    	
    	if(!this.EsTagDeCierre) {
    		int ini = this.PosCerrado+1;
    		int largo = Pareja.PosAbierto() - ini;
    		if(largo>=0) {
	    		Contenido = new char[largo];
	    		
	    		for (int i=ini;i<ini+largo;i++) { 
	    			Contenido[i-ini] = Buffer.get(i);
	    		}
	    		
	    		ini = this.PosAbierto + this.Nombre().length()+1;
	    		largo = this.PosCerrado - ini;
	    		Props = new char[largo];
	    		
	    		for (int i=ini;i<ini+largo;i++) { 
	    			Props[i-ini] = Buffer.get(i);
	    		}
	    		this.GetPropiedades(Props);
    		}
    	}
    		
    }
    
    private void GetPropiedades(char Props[]) {
    	HTMLpropiedad tmp=null;
    	String StrProp = String.valueOf(Props);
    	String Pal = null;
    	char CActual=' ';
    	int i=0;
    	int seccion=1;
    	int ComenzoPal=0;
    	int ComenzoProp=0;
    	try {
	    	if(StrProp.toString().trim().length() > 0) {
		    	StrProp= " " + StrProp + " ";
		    	for(i = 1;i<=StrProp.length();i++) {
		    		CActual =  StrProp.substring(i-1,i).toCharArray()[0];
		    		if(' ' != CActual && '=' != CActual ) {
		    			if(ComenzoProp==0) {
		    				tmp = new HTMLpropiedad();
		    				ComenzoProp=1;
		    				Pal = "";
		    			}
		    			ComenzoPal=1;
		    			Pal = Pal + String.valueOf(CActual);
		    		}
		    		
		    		if(( '=' == CActual || ' ' == CActual ) && ComenzoPal == 1 ) {
		    			if(seccion ==1) {
		    				tmp.SetProp(Pal);
		    				seccion=2;
		        			ComenzoPal=0;
		        			Pal="";
		    			}
		    			else {
		    				seccion=1;
		    				tmp.SetValor(Pal);
		    				ComenzoProp=0;
		    				Pal="";
		    				this.Propiedades.apilar(tmp);
		    			}
		    		}
		    	}
	    	}
    	}
    	catch(IndexOutOfBoundsException e) {
    		System.out.println("Error en la iteracion =" + String.valueOf(i));
    	}
    }
    
    public HTMLpropiedad GetPropiedad(HTMLproperty prop) {
    	Pila tmp = new Pila();
    	HTMLpropiedad tmp_prop = new HTMLpropiedad();
    	
    	while (!Propiedades.vacia()) {
    		tmp_prop = (HTMLpropiedad) Propiedades.desApilar();
    		if((tmp_prop).GetProp() == prop ) {
    			break;
    		}
    		else {
    			tmp.apilar(tmp_prop);
    		}
    	}
    	
    	Propiedades.apilar(tmp_prop);
    	tmp.volcar(Propiedades);
    	return (tmp_prop);
    }
}