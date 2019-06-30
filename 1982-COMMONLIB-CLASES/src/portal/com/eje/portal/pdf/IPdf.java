package portal.com.eje.portal.pdf;

import java.io.File;
import java.io.IOException;

import portal.com.eje.frontcontroller.IOClaseWeb;

public interface IPdf {
	
	public File buildPdf(HttpFlow flow) throws IOException;
	
	public File buildPdf(IOClaseWeb io, HttpFlow flow) throws IOException;
	
}
