package mis.html;

public class HTMLpropiedad {
	HTMLproperty VAR_prop= null;
	String VAR_valor = null;
	String VAR_STRPROP=null;
	
	public HTMLpropiedad() {
		VAR_prop = HTMLproperty.NULL;
		VAR_STRPROP = "";
		VAR_valor = "";
	}
	
	public void SetProp(String prop) {
		VAR_prop = HTMLproperty.ConvertStrToproperty(prop);
		VAR_STRPROP = prop;
	}
	
	public void SetProp(HTMLproperty prop) {
		VAR_prop = prop;
	}
	
	public void SetValor(String valor) {
		valor = valor.replace('\"',' ');
		valor = valor.trim();
		VAR_valor = valor;
	}
	
	public String GetValor() {
		return VAR_valor;
	}
	
	public HTMLproperty GetProp() {
		return VAR_prop;
	}
	
	public String GetPropStr() {
		return VAR_STRPROP;
	}
	
	public boolean vacio() {
		return (VAR_prop == HTMLproperty.NULL);
	}
}