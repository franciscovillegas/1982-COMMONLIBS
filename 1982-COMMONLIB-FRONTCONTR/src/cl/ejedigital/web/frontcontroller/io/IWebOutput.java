package cl.ejedigital.web.frontcontroller.io;

import cl.ejedigital.consultor.output.IDataOut;
import freemarker.template.SimpleHash;

/**
 * @deprecated
 * @since 2015-06-23
 * @author Francisco
 * */
public interface IWebOutput {

    public boolean retTexto(String texto);
    
    public boolean retData(IDataOut data);
    
    public boolean retData(IDataOut data, String[] sorted);
    
    public boolean retHtml(String htmRuta, SimpleHash modelRoot);
	
	public boolean retXls(String htmRuta, SimpleHash modelRoot, String fileName);
	
}
