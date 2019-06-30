package tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletOutputStream;

import mis.MyProxyDemo;
import mis.MyProxyDemo.TipoUrlRequest;

import org.apache.commons.io.output.ByteArrayOutputStream;

import portal.com.eje.frontcontroller.AbsClaseWebInsegura;
import portal.com.eje.frontcontroller.IOClaseWeb;

public class ProxyWeb extends AbsClaseWebInsegura  {

	public ProxyWeb(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doPost() throws Exception {
		doThings( TipoUrlRequest.doPost);
	}

	@Override
	public void doGet() throws Exception {
		doThings( TipoUrlRequest.doGet);

	}
 
	
	@SuppressWarnings("unchecked")
	private void doThings(TipoUrlRequest tipo) {
		MyProxyDemo demo = new MyProxyDemo();
		ByteArrayOutputStream tmp = new ByteArrayOutputStream();
		HashMap<String,Object> map = demo.doURLRequest(super.getIoClaseWeb().getParamString("url", "null"), tmp, tipo );
		super.getIoClaseWeb().getResp().setContentType(((HashMap<String,String>)map.get("Header")).get("Content-Type").toString());
		
		System.out.println(map);
		
		PrintWriter outFinal=null;
		try {
			outFinal = super.getIoClaseWeb().getResp().getWriter();
			
			outFinal.write(processRelativePaths(tmp.toString()));
			outFinal.flush(); 
			outFinal.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private String processRelativePaths(String str) {
		
		String url = super.getIoClaseWeb().getParamString("url", "/");
		String servidor = super.getIoClaseWeb().getReq().getRequestURL().toString();
		String query = super.getIoClaseWeb().getReq().getQueryString();
		String folderUrl = getLastFolder(url);
		System.out.print("Antes:"+str.length());
		str = str.replaceAll("href=\"", "href=\"".concat(servidor).concat("?").concat("claseweb=tools.ProxyWeb&url=").concat(folderUrl) );
		System.out.print("Despues:"+str.length());
		return str.toString();
	}

	private String getLastFolder(String fullUrl) {
		String ret = "";
		String tmpUrl = "";
		
		if(fullUrl != null && ( fullUrl.indexOf("http://") != -1 || fullUrl.indexOf("https://") != -1) ) {
			tmpUrl = fullUrl.substring(fullUrl.indexOf("://")+3, fullUrl.length());
			
			if(tmpUrl.indexOf("/") != -1) {
				ret = tmpUrl.substring(0, tmpUrl.indexOf("/")+1);
			}
			else {
				ret = "";
			}
		} else {
			
		}
		
		return fullUrl.substring(0, fullUrl.indexOf("://")+3) + ret;
	}
	
}
