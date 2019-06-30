package mis.html;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;

import mis.pila.Pila;

public class HTMLjerarquia {
    private CharBuffer Buffer = null;
    private CharBuffer BufferTmp = null;
    private CharBuffer BufferTmp2 = null;
    private HTMLnode   GlobalNodoIni= null;
    private HTMLnode   GlobalNodoFin= null;
    private final int LargoBuffer = 5000;
    private Pila pila = null;
    private final String CABEZERA_DOCUMENTO="DOCUMENTO_CLASS_FCO";
    
	public HTMLjerarquia(InputStream in) {
		pila = new Pila();
		CargarHtml(in);
	}
	
	public HTMLnode GetNodoCabezera() {
		return ( GlobalNodoIni );
	}
	
	private void CargarHtml(InputStream in) {
		try {
			BufferedReader data = new BufferedReader(new InputStreamReader(in));

			Buffer = CharBuffer.allocate(0);
			BufferTmp = CharBuffer.allocate(LargoBuffer);
			BufferTmp2 = CharBuffer.allocate(LargoBuffer);
			
			int readBytes = 0;
			
			while((readBytes = data.read()) != -1) {
					this.AgregarAlBuffer((char) readBytes);
			}
			VolcarBuffer();
 
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void AgregarAlBuffer(char Byte) {
		if (BufferTmp.remaining() == 0) {
			VolcarBuffer();
		}
		
		BufferTmp.put(Byte);
	}
	
	private void VolcarBuffer() {
		BufferTmp2 = Buffer.duplicate();
		Buffer = CharBuffer.allocate(BufferTmp2.position() + BufferTmp.position());
		Buffer.clear();
		Buffer.put(String.valueOf(BufferTmp2.array()).substring(0,BufferTmp2.position()) + String.valueOf(BufferTmp.array()).substring(0,BufferTmp.position()));
		BufferTmp = CharBuffer.allocate(LargoBuffer);
	}
		
	public void CargarTodosLosNodos() {
		Pila PilaTmp= new Pila();
		
		pila.eliminar();
		GlobalNodoIni = new HTMLnode(CABEZERA_DOCUMENTO,-1,-1,false,false);
		pila.apilar(GlobalNodoIni);
    	ReGetTodosLosNodos(0,Buffer.limit());
		GlobalNodoFin = new HTMLnode(CABEZERA_DOCUMENTO,-1,-1,true,false);
		pila.apilar(GlobalNodoFin);
		
    	pila.volcar(PilaTmp);
    	ReJerarquizaNodos(PilaTmp);
	}
	
	private void ReGetTodosLosNodos(int PosAbierto_,int PosHasta) {
    	boolean EstaCerrado = false;
    	boolean EsTagdeCerrado = false;
    	int InicioTag = 0;
    	int i=0;
    	int PosCerrado = i;
    	String StrNombre = null;
    	HTMLtag tag = null;
    	
    	try {
    		InicioTag = BuscarIndiceProximoTag(Buffer,PosAbierto_);
    		
    		if (InicioTag == -1) {
    			return;
    		}
    		
    		if (Buffer.get(InicioTag+1) == '/') {
    			EsTagdeCerrado = true;
    		}
    		
	    	StrNombre = GetNameFromIni(Buffer,InicioTag+1,PosHasta); 
	    	tag = HTMLtag.ConvertStrToTag(StrNombre);
	    	
	    	for(i=InicioTag;i<=PosHasta;i++) { 
	    		if(tag == HTMLtag.DOCTYPE ) {
			    	if(Buffer.get(i) == '>') {
						PosCerrado = i;
						EstaCerrado = true;
						break;
					}
	    		}
			    else {
			    	if(tag == HTMLtag.COMENTARIO) {
			    		if (("-->").equals(String.valueOf(Buffer.array()).substring(i, i+3))) {
			    			PosCerrado = i+3;
			    			EstaCerrado = true;
			    			break;
			    		}
			    	}
			    	else
			    		if(Buffer.get(i) == '>' && i >= 1) {
			    			PosCerrado = i;
			    			if(Buffer.get(i-1) == '/') {
			    				EstaCerrado = true;
			    			}
		    			break;
		    		}
			    }
	    	}
	    	
	    	HTMLnode nodo = new HTMLnode(StrNombre,InicioTag,PosCerrado,EsTagdeCerrado,EstaCerrado);
	    	pila.apilar(nodo);
	    	
	    	ReGetTodosLosNodos(PosCerrado,PosHasta);
    	}
    	catch(IndexOutOfBoundsException e) {
    		System.out.println(i);
    		System.out.println(e.getMessage());
    		e.printStackTrace();
    	} 
	}
    
	private void ReJerarquizaNodos(Pila PilaAlmacen) {
		HTMLnode nodo = null;
		HTMLnode nodoTmp = null;
		HTMLnode nodoTmpDos = null;
		Pila PilaAbiertos = new Pila();
		
		while (!PilaAlmacen.vacia()) {
			nodo = (HTMLnode) PilaAlmacen.desApilar();
			if (!nodo.EsUnTagDeCierra()) {
				if (!nodo.EstaCerradoDeFormaCorta()) {	//Si el tag es abierto lo Apilo
					PilaAbiertos.apilar(nodo);
				}
				else {	//El ndo abierto anterior que posee cierre posterior se le atribuye como PAPA de NODO
					nodoTmp = BuscaTagAbiertoAnteriorQueCierreDespues(PilaAbiertos,PilaAlmacen);
					nodoTmp.AddHijo(nodo);
				}
			}
			else {	//Si es un Tag Cerrado
				nodoTmp = BuscaTagAbiertoYempadronaIntermedios(nodo,PilaAbiertos);
				nodoTmpDos = BuscaTagAbiertoAnteriorQueCierreDespues(PilaAbiertos,PilaAlmacen);
				if(nodoTmpDos!=null) {
					nodoTmpDos.AddHijo(nodoTmp);
				}
				else {
					System.out.println("Terminado");
				}
			}
		}
	}
	
	private HTMLnode BuscaTagAbiertoYempadronaIntermedios(HTMLnode TagCierra,Pila PilaAbiertos) {
		HTMLnode TmpAbierto =null;
		Pila PilaAbiertosTmp= new Pila();
		boolean LoEncontre = false;
		
		while (!PilaAbiertos.vacia()) {
			TmpAbierto = (HTMLnode) PilaAbiertos.desApilar();
			if (TagCierra.Nombre().equalsIgnoreCase(TmpAbierto.Nombre()) && !TmpAbierto.EstaCerradoDeFormaCorta()  && !TmpAbierto.EsUnTagDeCierra()) {
				LoEncontre = true;
				break;
			}
			else {
				LoEncontre=true;
				PilaAbiertosTmp.apilar(TmpAbierto);
			}
		}
		
		if (LoEncontre) {
			while(!PilaAbiertosTmp.vacia()) {
				TmpAbierto.AddHijo((HTMLnode) PilaAbiertosTmp.desApilar());
			}
			TmpAbierto.AddPareja(TagCierra,Buffer);
			TagCierra.AddPareja(TmpAbierto,Buffer);
		}
		else {
			//Si no encuentra entonces deja todo como esta
			PilaAbiertos.apilar(TmpAbierto);
			while(!PilaAbiertosTmp.vacia()) {
				 PilaAbiertos.apilar((HTMLnode) PilaAbiertosTmp.desApilar());
			}
		}
		return TmpAbierto;
	}
	
	private HTMLnode BuscaTagAbiertoAnteriorQueCierreDespues(Pila PilaAbiertos,Pila PilaAlmacen) {
		HTMLnode TmpAbierto = null;
		HTMLnode TmpAlmacen = null;
		Pila PilaAbiertosTmp= new Pila();
		Pila PilaAlmacenTmp=new Pila();
		boolean Encontro = false;
		
		while (!PilaAbiertos.vacia()) {
			TmpAbierto =(HTMLnode) PilaAbiertos.desApilar();
			
			if (!TmpAbierto.EstaCerradoDeFormaCorta() && !TmpAbierto.EsUnTagDeCierra()) {
				while (!PilaAlmacen.vacia()) {
					TmpAlmacen = (HTMLnode) PilaAlmacen.desApilar();
					if(TmpAbierto.Nombre().equalsIgnoreCase(TmpAlmacen.Nombre()) && !TmpAlmacen.EstaCerradoDeFormaCorta() && TmpAlmacen.EsUnTagDeCierra()) {
						PilaAlmacenTmp.apilar(TmpAlmacen);
						Encontro = true;
						break;
					}
					else {
						PilaAlmacenTmp.apilar(TmpAlmacen);
					}
				}
				if(Encontro) {
					PilaAbiertosTmp.apilar(TmpAbierto);
					break;
				}
				else {
					PilaAlmacenTmp.volcar(PilaAlmacen);
				}
			}
			else {
				PilaAbiertosTmp.apilar(TmpAbierto);
			}
		}
		
		//Devolvemos los objetos 
		while (!PilaAbiertosTmp.vacia()) {
			PilaAbiertos.apilar(PilaAbiertosTmp.desApilar());
		}
		while (!PilaAlmacenTmp.vacia()) {
			PilaAlmacen.apilar(PilaAlmacenTmp.desApilar());
		}
		return ( TmpAbierto );
	}
		
    private int BuscarIndiceProximoTag(CharBuffer Buffer,int Comienzo) {
    	int PosHasta = Buffer.limit();
    	int i=0;
    	
    	for(i=Comienzo;i<=PosHasta;i++) {
    		if (i == Buffer.limit()) {
    			return ( -1 );
    		}
    		
    		if (i>=0) {
	    		if(Buffer.get(i) == '<') {
	    			break;
	    		}
    		}
    	}
		return ( i );
    }
    
    private String GetNameFromIni(CharBuffer Buffer,int PosAbierto_,int PosCerrado_) throws IndexOutOfBoundsException {
    	CharBuffer Nombre = CharBuffer.allocate(100);
    	String LocalNombre = null;
    	
    	for(int i=PosAbierto_;i<PosCerrado_;i++) {
    		if(Buffer.get(i) != ' ' && Buffer.get(i) != '>' && Buffer.get(i) != '\n' ) {
    			if (Buffer.get(i) != '/' ) {
    				Nombre.put(Buffer.get(i));
    			}
    		}
    		else {
    			break;
    		}
    	}
    	
    	LocalNombre = String.valueOf(Nombre.array()).substring(0,Nombre.position());
    	
    	if("!--".equalsIgnoreCase(LocalNombre)) {
    		return("Comentario");
    	}
    	else {
    		return(LocalNombre);
    	}
    }

   
    public String GetPagina() {
    	Pila tmp = new Pila();
    	String Dest = GetPagina_Re(GlobalNodoIni,HTMLtag.COMODIN,tmp,"");
    	return (Dest);
    }
    
    private String GetPagina_Re(HTMLnode nodo,HTMLtag tag,Pila Encolados,String Tab) {
		HTMLnode Tmp=null;
		Pila pila = new Pila();
		String Dest = "";
		if(nodo.GetTag().equals(tag) || tag.equals(HTMLtag.COMODIN)) {
			if(!nodo.equals(GlobalNodoIni)) {
				Dest = Dest + Tab + nodo.NombreTagWithProp() + "\n";
			}
			Encolados.apilar(nodo);
		}
		nodo.Hijos.volcar(pila);
		while(!pila.vacia()) {
			Tmp= (HTMLnode)pila.desApilar();
			Dest = Dest + Tab + GetPagina_Re(Tmp,tag,Encolados,Tab + "    ");
		}
		
		if(!nodo.equals(GlobalNodoFin)) {
			Dest = Dest + Tab + nodo.NodoPareja.NombreTagWithProp() + "\n";;
		}
			
		pila.volcar(nodo.Hijos);
		return ( Dest );
    }
}
