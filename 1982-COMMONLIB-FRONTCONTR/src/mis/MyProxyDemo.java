package mis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSession;

import org.apache.commons.io.output.ByteArrayOutputStream;

import portal.com.eje.portal.factory.Util;

public class MyProxyDemo {
	
	public static MyProxyDemo getInstance() {
		return Util.getInstance(MyProxyDemo.class);
	}
	
	public MyProxyDemo() {
	}

	/**
	 * @deprecated Se eliminó por que solo retornaba apra doGet
	 * 
	 * */
	public String doURLRequest(String strURL) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		URL url = null;
		URLConnection c = null;

		try {
			URL urlOriginal = new URL(strURL);
			url = urlOriginal;
			c = url.openConnection();

			if (c instanceof HttpURLConnection) {
				HttpURLConnection h = (HttpURLConnection) c;
				h.connect();

				for (int i = 1;; i++) {
					String strKey = h.getHeaderFieldKey(i);
					if (null == strKey) {
						break;
					}
				}

				String strContentType = h.getContentType();
				strContentType = strContentType == null ? "text/html" : strContentType;
				if ((null != strContentType) || (0 == strContentType.compareTo("application/vnd.ms-excel"))) {
					try {
						InputStream in = h.getInputStream();
						BufferedReader data = new BufferedReader(new InputStreamReader(in));
						int readBytes = 0;

						while ((readBytes = data.read()) != -1) {
							stream.write(readBytes);
						}
					} catch (Exception exc2) {
						stream = null;
					}
				}
				h.disconnect();
			} else {
				stream = null;
			}
		} catch (Exception e) {
			stream = null;
			System.out.println(e);
		} finally {
			c = null;
			url = null;
		}

		try {
			if (stream != null) {
				return stream.toString("UTF-8");
			} else {
				return null;
			}

		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * @deprecated Se eliminó por que solo retornaba para doGets
	 * */

	public OutputStream doURLRequest(String strURL, OutputStream stream) {
		URL url = null;
		URLConnection c = null;

		try {
			URL urlOriginal = new URL(strURL);
			url = urlOriginal;
			c = url.openConnection();

			if (c instanceof HttpURLConnection) {
				HttpURLConnection h = (HttpURLConnection) c;
				h.connect();

				for (int i = 1;; i++) {
					String strKey = h.getHeaderFieldKey(i);
					if (null == strKey) {
						break;
					}
				}

				String strContentType = h.getContentType();
				strContentType = strContentType == null ? "text/html" : strContentType;
				if ((null != strContentType) || (0 == strContentType.compareTo("application/vnd.ms-excel"))) {
					try {
						InputStream in = h.getInputStream();
						BufferedReader data = new BufferedReader(new InputStreamReader(in));
						int readBytes = 0;

						while ((readBytes = data.read()) != -1) {
							stream.write(readBytes);
						}
					} catch (Exception exc2) {
						stream = null;
					}
				}
				h.disconnect();
			} else {
				stream = null;
			}
		} catch (Exception e) {
			stream = null;
			System.out.println(e);
		} finally {
			c = null;
			url = null;
		}
		return stream;
	}

	public String doURLRequest(String strURL, TipoUrlRequest urlType) {
		return this.doURLRequest(strURL, urlType, new HashMap());
	}

	public String doURLRequest(String strURL, TipoUrlRequest urlType, Map<String, String> params) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();

		doURLRequest(strURL, stream, urlType, params);

		try {
			return new String(stream.toByteArray(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	public HashMap<String, Object> doURLRequest(String strURL, OutputStream stream, TipoUrlRequest urlType) {
		return doURLRequest(strURL, stream, urlType, new HashMap());
	}

	public HashMap<String, Object> doURLRequest(String strURL, OutputStream stream, TipoUrlRequest urlType, Map<String, String> params) {

		HttpURLConnection h = null;
		HashMap<String, Object> dpr = new HashMap<String, Object>();
		dpr.put("Double".intern(), new Double(0));
		dpr.put("Header".intern(), new HashMap());

		try {
			/*GENERA UNA LISTA DE PARAMETROS*/
			StringBuilder paramsData = new StringBuilder();
			StringBuilder paramsDataAdic = new StringBuilder();
			
			if (params != null) {
				for (String p : params.keySet()) {
					
					String strParam = URLEncoder.encode(p, "UTF-8");
					
					if (paramsData.length() != 0) {
						paramsData.append("&");
					}
					
					if(params.get(p) != null) {
						paramsData.append(strParam);
						paramsData.append("=");
						paramsData.append(URLEncoder.encode(params.get(p), "UTF-8"));
					}
					if ("origen_ioxml".equals(strParam)){		
						String strTag = getTagValue(params.get(p), "data_adic");
						if (strTag!=null) {
							paramsDataAdic.append("data_adic");
							paramsDataAdic.append("=");
							paramsDataAdic.append(URLEncoder.encode(strTag, "UTF-8"));
						}
					}

				}
			}

			if(urlType == TipoUrlRequest.doGet) {
				if(strURL != null && strURL.indexOf("?") == -1) {
					strURL += "?" + paramsData.toString();
				}
				else {
					strURL += paramsData.toString();
				}
			}else if (paramsDataAdic.length()!=0){
				strURL += "?" + paramsDataAdic.toString();
			}
			
			URL urlOriginal = new URL(strURL);

			if (strURL != null && strURL.indexOf("https://") == 0) {
				HostnameVerifier hv = new HostnameVerifier() {
					public boolean verify(String urlHostName, SSLSession session) {
						return true;
					}
				};
				trustAllHttpsCertificates();
				HttpsURLConnection.setDefaultHostnameVerifier(hv);
				
				h = (HttpsURLConnection) urlOriginal.openConnection();

				
				
			} else if (strURL != null && strURL.indexOf("http://") == 0) {
				h = (HttpURLConnection) urlOriginal.openConnection();
			}

			if (urlType == TipoUrlRequest.doPost) {
				h.setDoOutput(true);
				h.setRequestMethod("POST");
			}

			if (h instanceof HttpURLConnection) {
				
				if (urlType == TipoUrlRequest.doPost) {
					h.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					h.setRequestProperty("Content-Length", String.valueOf(paramsData.toString().length()));
					h.getOutputStream().write(paramsData.toString().getBytes("UTF-8"));
				}
 
				h.connect();

				HashMap<String, String> header = new HashMap<String, String>();
				Set<String> keys = h.getHeaderFields().keySet();
				for (String k : keys) {
					if (h.getHeaderFields().get(k) != null && h.getHeaderFields().get(k) instanceof List) {
						String v = h.getHeaderFields().get(k).get(0);
						if (null == k) {
							dpr.put("State", new String(v.getBytes()));
	 
						}

						header.put(k, v);
					}
				}
				dpr.put("Header".intern(), header);

				for (int i = 1;; i++) {
					String strKey = h.getHeaderFieldKey(i);
					if (null == strKey) {
						break;
					}
				}

				try {
					InputStream in = h.getInputStream();
					BufferedReader data = new BufferedReader(new InputStreamReader(in));
					int readBytes = 0;
					double largo = 0;

					while ((readBytes = data.read()) != -1) {
						stream.write(readBytes);
						largo++;
					}

					dpr.put("Double", new Double(largo));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					stream = null;
				} catch (Exception e) {
					e.printStackTrace();
					stream = null;
				}

			} else {
				stream = null;
			}
		} catch (SSLHandshakeException e) {
			stream = null;
			//System.out.println("[SSLHandshakeException]["+strURL+"]"+e.getMessage());
			throw new RuntimeException(strURL,e);
		} catch (ConnectException e) {
			stream = null;
			//System.out.println("[ConnectException]["+strURL+"]"+e.getMessage());
			throw new RuntimeException(strURL,e);
		} catch (Exception e) {
			stream = null;
			//System.out.println("[Exception]["+strURL+"]"+e.getMessage());
			throw new RuntimeException(strURL,e);
		}
		
		finally {
			if (h != null) {
				h.disconnect();
			}
			h = null;

		}

		return dpr;
	}

	public enum TipoUrlRequest {
		doGet, doPost;
	}

	public static class miTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) throws java.security.cert.CertificateException {
			return;
		}

		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) throws java.security.cert.CertificateException {
			return;
		}
	}

	private static void trustAllHttpsCertificates() throws Exception {

		// Create a trust manager that does not validate certificate chains:

		javax.net.ssl.TrustManager[] trustAllCerts =

		new javax.net.ssl.TrustManager[1];

		javax.net.ssl.TrustManager tm = new miTM();

		trustAllCerts[0] = tm;

		javax.net.ssl.SSLContext sc =

		javax.net.ssl.SSLContext.getInstance("SSL");

		sc.init(null, trustAllCerts, null);

		javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(

		sc.getSocketFactory());

	}
	
	public static String getTagValue(String xml, String tagName){
		String strResultado = null;
		try {
			xml = xml.replaceAll("&quot;","\"");
			String strTag = xml.split("<string>"+tagName+"</string>")[1].split("</entry>")[0];
			strTag = strTag.split("<list>")[1].split("</list>")[0];
			strResultado = strTag.split("<string>")[1].split("</string>")[0];
		}catch(Exception e){
			System.out.println("#Error al intentar obtener parametro xml: "+tagName);
		}
		return strResultado;
	}
	
}
