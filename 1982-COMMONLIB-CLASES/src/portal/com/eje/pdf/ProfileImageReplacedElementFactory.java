package portal.com.eje.pdf;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashMap;

import javax.servlet.ServletException;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.FSImage;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextImageElement;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;

import portal.com.eje.frontcontroller.resobjects.ResourceImage;
import portal.com.eje.tools.security.Base64Coder;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Image;

public class ProfileImageReplacedElementFactory implements ReplacedElementFactory {

    private final ReplacedElementFactory superFactory;
    private String rutaBase;

    public ProfileImageReplacedElementFactory(ReplacedElementFactory superFactory, String rutaBase) {
        this.superFactory = superFactory;
        this.rutaBase 	  = rutaBase;
    }

    public ReplacedElement createReplacedElement(LayoutContext layoutContext, BlockBox blockBox,
            UserAgentCallback userAgentCallback, int cssWidth, int cssHeight) {

        Element element = blockBox.getElement();
        if (element == null) {
            return null;
        }

        String nodeName = element.getNodeName();
        //String className = element.getAttribute("class");
        
        if ("img".equals(nodeName)) {
        	String src = element.getAttribute("src");
        	
        	if(src != null && src.indexOf("data:image/png;base64") != -1 ) {
        		/*SI ES BASE64*/

        		byte[] imageByte = Base64Coder.decode(src.replaceAll("data:image/png;base64,", "").toCharArray());
        		Image image;
				try {
					image = Image.getInstance(imageByte);
					FSImage fsImage = new ITextFSImage(image);
		        	 
					fsImage.scale( (int) (image.getWidth() * layoutContext.getDotsPerPixel()),
								   (int) (image.getHeight() * layoutContext.getDotsPerPixel()) );
	        		return new ITextImageElement(fsImage);
				} catch (BadElementException e) {
					e.printStackTrace();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} 
        		
        	}
        	
        	
        	HashMap<String, String> map = new HashMap<String, String>();
    		
        	if(src != null && src.indexOf("Tool?") != -1) {
        		String[] partes = new String[2];
        		partes[0] = src.substring(0, src.indexOf("Tool?")+ 4);
        		partes[1] = src.substring(src.indexOf("Tool?")+ 5 , src.length());
        		
        		map.put("omap", partes[1]);
        	}
        	else {
        		map.put("omap", src);
        	}
        	
        	if(map.get("omap") != null && map.get("omap").indexOf("omap=") != -1) {
        		map.put("omap", map.get("omap").replaceAll("omap=",""));
        	}
        	
        	
        	if(map.get("omap") != null && map.get("omap").indexOf("&") != -1) {
        		String[] params = map.get("omap").split("//&");
            	for(String p : params) {
            		String[] keysAndValues = p.split("//=");
            		
            		if(keysAndValues.length == 1) {
            			map.put("omap", keysAndValues[0]);
            		}
            		else {
            			if("omap".equals(keysAndValues[0])) {
            				map.put("omap", keysAndValues[1]);
            			}
            		}
            	}
        	}
        	
  
        	
            InputStream input = null;
            try {  
            	ByteArrayOutputStream inputStream = getImage("/".concat(map.get("omap")));    
            	
            	if(inputStream != null && inputStream.size() < 5 ) {
            		System.out.println("Imagen no válida en html, el src apunta a una imagen que no existe omap=\""+map.get("omap")+"\"");
            	}
            	else if(inputStream != null) {
	                byte[] bytes = inputStream.toByteArray();
	                Image image = Image.getInstance(bytes);
	                FSImage fsImage = new ITextFSImage(image);

	                if (fsImage != null) {
	                	
	                    if (cssWidth != -1 || cssHeight != -1) {
	                        fsImage.scale(cssWidth, cssHeight);
	                    }
	                    else {
	                    	
	                    	fsImage.scale( (int) (image.getWidth() * layoutContext.getDotsPerPixel()),
	                    				   (int) (image.getHeight() * layoutContext.getDotsPerPixel()) );
	                    }
	                    
	                    return new ITextImageElement(fsImage);
	                }
            	}
            }
            catch (IOException e) {
            	e.printStackTrace();
            }
            catch (BadElementException e) {
            	e.printStackTrace();
            } 
            finally {
            	if(input != null) {
            		IOUtils.closeQuietly(input);	
            	}
                
            }
        }
      

        return superFactory.createReplacedElement(layoutContext, blockBox, userAgentCallback, cssWidth, cssHeight);
    }

    public void reset() {
        superFactory.reset();
    }

    public void remove(Element e) {
        superFactory.remove(e);
    }

    public void setFormSubmissionListener(FormSubmissionListener listener) {
        superFactory.setFormSubmissionListener(listener);
    }
    
    private ByteArrayOutputStream getImage(String imagePath) {

    	ByteArrayOutputStream bufImage = null;
		try {
			ResourceImage imgRes = new ResourceImage();
			bufImage 			 = imgRes.getStream(imagePath);
			
		}
		catch (NullPointerException e) {
			System.out.println(e.getMessage() + " ->"+imagePath);
		}
		catch (FileNotFoundException e) {
			System.out.println(e.getMessage() + " ->"+imagePath);
		}
		catch (ServletException e ) {
			System.out.println(e.getMessage() + " ->"+imagePath);
		}
		catch (IOException e) {
			System.out.println(e.getMessage() + " ->"+imagePath);
		}

		return bufImage;		
    }
}
