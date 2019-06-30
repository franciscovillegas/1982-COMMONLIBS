package mis;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletOutputStream;

public class ProxyDemo {
    public ProxyDemo() {
    }
    
    public ServletOutputStream doURLRequest(String strURL, ServletOutputStream stream) {
      URL url = null;
      URLConnection c = null;

      try {
          URL urlOriginal = new URL(strURL);
          url = urlOriginal;
          c = url.openConnection();

          if (c instanceof HttpURLConnection) {
              HttpURLConnection h = (HttpURLConnection) c;
              h.connect();

              for (int i = 1; ; i++) {
                  String strKey = h.getHeaderFieldKey(i);
                  if (null == strKey) {
                    break;
                  }
                }

              String strContentType = h.getContentType();
              strContentType = strContentType==null?"text/html":strContentType;
              if ((null != strContentType) && (  (0 == strContentType.compareTo("text/html"))) || (0 == strContentType.compareTo("application/vnd.ms-excel"))  ) {
                  try {
                      InputStream in = h.getInputStream();
                      BufferedReader data = new BufferedReader(new InputStreamReader(in));
                      int readBytes = 0;
                      while((readBytes = data.read()) != -1) {
                        stream.write(readBytes);
                      }
                  }
                  catch(Exception exc2) {
                	  stream = null;
                  }
              }
              h.disconnect();
           } 
          else {
        	  stream = null;
          }
      }
      catch(Exception e) {
    	  stream = null;
    	  System.out.println(e);
      }
      finally {
    	  c = null;
    	  url = null;
      }
      return stream;
    }
}
