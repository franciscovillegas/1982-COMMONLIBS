package portal.com.eje.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.lowagie.text.DocumentException;

import cl.ejedigital.tool.strings.MyString;
import mis.MyProxyDemo;
import mis.MyProxyDemo.TipoUrlRequest;
import portal.com.eje.applistener.ContextInfo;
import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.portal.factory.Util;


public class CWebTransformPdf extends AbsClaseWeb {
	private String XHTML_HEADER;
	private String XHTML_FOOTER;

	
	public static CWebTransformPdf getInstance() {
		return Util.getInstance(CWebTransformPdf.class);
	}
	
	
	public CWebTransformPdf(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		
		XHTML_HEADER = "<html><head>";  
		XHTML_FOOTER = "</body></html>";
	}
	
	public CWebTransformPdf() {
		super(null);
		
		XHTML_HEADER = "<html><head>";  
		XHTML_FOOTER = "</body></html>";
	}

	@Override
	public void doPost() throws Exception {
		doGet();
		
	}

	@Override
	public void doGet() throws Exception {
		//writeIntoFile(super.getIoClaseWeb().getMapParams().toString());
		
		String urlPdf 	= super.getIoClaseWeb().getParamString("urlPdf",null);   
	    String content = null;
	    
        if(urlPdf != null) {
        	MyProxyDemo demo = new MyProxyDemo();
        	ByteArrayOutputStream tmp = new ByteArrayOutputStream();
        	demo.doURLRequest(urlPdf, tmp,TipoUrlRequest.doGet);
        	content = tmp.toString();
        }
        else {
        	content =  super.getIoClaseWeb().getParamString("content",null);
        }
    	
    	String urlBase = super.getIoClaseWeb().getParamString("urlBase",null); 
    	String tipo =  super.getIoClaseWeb().getParamString("tipo","comun");
    	String rut_trab =  super.getIoClaseWeb().getParamString("rut_trab","____");
    	String xhtml = buildXHTML( content);
		
		try {
			HttpServletResponse resp = super.getIoClaseWeb().getResp();
			HttpServletRequest  req  = super.getIoClaseWeb().getReq();
	        resp.setContentType("application/file");      
	        resp.setHeader("Content-Disposition", "attachment;fileName=cert_".concat(tipo).concat("_").concat(rut_trab).concat(".pdf")); 		
			ServletOutputStream outPdf = (ServletOutputStream)generatePDF(xhtml,resp.getOutputStream(), req);
			outPdf.flush();
			outPdf.close();   
		}
		catch(Exception e) {
			System.out.println("Error->"+e);
		}
		
	}

	/**
	 * 
	 * 
	 * */
	public File getFile(String content, String fileName_onlyNameNoExtension, HttpServletRequest req ) throws IOException {
		File pathBase = new File(ContextInfo.getInstance().getRealPath("temporal"));
		if(!pathBase.exists()) {
			pathBase.mkdirs();
		}
		
		String filePath = ContextInfo.getInstance().getRealPath("temporal")+File.separator+fileName_onlyNameNoExtension+".pdf";
		File file = new File(filePath);
		while(file.exists()) {
			MyString my = new MyString();
			filePath = ContextInfo.getInstance().getRealPath("temporal")+File.separator+fileName_onlyNameNoExtension+my.getRandomString("abcdefghijklmnopqrstuvwxyz0123456789", 5)+".pdf";
			file = new File(filePath);
		}
		
		return getFile(req, content, new File(filePath));
	}
	
	public File getFile(HttpServletRequest req , String content , File file) throws IOException {
		String xhtml = buildXHTML( content);
		
    	OutputStream outPdf = null;
    	OutputStream oos = null;
		try {   
			 
			oos = new FileOutputStream(file);
			outPdf = generatePDF(xhtml,oos, req);
			
		}
		catch(Exception e) {
			System.out.println("Error->"+e);
		}
		finally {
			if ( outPdf != null ) {
				outPdf.flush();
				
			}
			
			if( outPdf != null ) {
				outPdf.close();   
				outPdf=null;
			}
			
			if(oos != null) {
				oos.close();
				oos = null;
			}
		}
		
		return file;
	}
	
	public String buildXHTML(String content) 
		throws IOException {  
		StringBuilder sb = new StringBuilder(XHTML_HEADER);  
		sb.append("").append(content).append(XHTML_FOOTER);
		String raw = sb.toString();

		InputStream rawis = new ByteArrayInputStream(raw.getBytes("iso-8859-1"));
		
		Tidy tidy = new Tidy();
		tidy.setShowWarnings(false);
		tidy.setXHTML(true);
		tidy.setDocType("omit");
		tidy.setQuiet(true);
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		tidy.parse(rawis,os);
		String out = new String(os.toByteArray());
		return out;
	}

	private OutputStream generatePDF(String xhtml, OutputStream out, HttpServletRequest req ) 
		throws ParserConfigurationException, FactoryConfigurationError, 
			   SAXException, IOException, DocumentException { 
		ITextRenderer renderer = new ITextRenderer();
		

		ProfileImageReplacedElementFactory profile = new ProfileImageReplacedElementFactory(renderer.getSharedContext().getReplacedElementFactory(), getDomainContext(req));
		
		renderer.getSharedContext().setReplacedElementFactory(profile);;
		
		
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse( new InputSource(new StringReader(xhtml)) );
		NodeList lista = doc.getElementsByTagName("link");

		int max = lista.getLength();
		for(int i = 0 ; i< max ;i++) {
			Node node = lista.item(0);
			node.getParentNode().removeChild(node);
		}

		renderer.setDocument(doc,getDomainContext(req));
		
		renderer.layout();
		renderer.createPDF(out);
        return out;
	}
	

	private String getDomainContext(HttpServletRequest req) {
		if(req == null) {
			return null;
		}
		
		String url = null;
		String path = req.getRequestURL().toString();
		
		if(path.startsWith("http://")) {
			url = path.substring(0, path.indexOf('/', "http://".length() + 1) );
		}
		else {
			url = path.substring(0, path.indexOf('/', "https://".length() + 1) );			
		}
		
		return url.concat(req.getContextPath());
	}


	public String getStringFromDocument(Document doc)
	{
	    try
	    {
	       DOMSource domSource = new DOMSource(doc);
	       StringWriter writer = new StringWriter();
	       StreamResult result = new StreamResult(writer);
	       TransformerFactory tf = TransformerFactory.newInstance();
	       Transformer transformer = tf.newTransformer();
	       transformer.transform(domSource, result);
	       return writer.toString();
	    }
	    catch(TransformerException ex)
	    {
	       ex.printStackTrace();
	       return "";
	    }
	} 
}
