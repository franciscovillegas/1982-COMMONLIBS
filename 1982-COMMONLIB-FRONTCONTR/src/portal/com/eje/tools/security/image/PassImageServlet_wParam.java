package portal.com.eje.tools.security.image;

import java.awt.image.BufferedImage;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.tools.security.Base64Coder;
import portal.com.eje.tools.security.Config;
import portal.com.eje.tools.security.Encrypter;

public class PassImageServlet_wParam extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Encrypter _stringEncrypter = new Encrypter();

    private static final String SKEW_PROCESSOR_CLASS = Config.getProperty(Config.SKEW_PROCESSOR_CLASS);

    /**
     * Redirects the request and response to doGet
     *
     * @param request
     * @param response
     * @throws ServletException
     */
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException {
        doGet(request, response);
    }

    /**
     * Calls the ISkewImage class to process and returns the image into response
     *
     * @param request
     * @param response
     * @throws ServletException
     */
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException {
        try {
            String passedKeyStr = request.getQueryString();
            if (passedKeyStr == null) {
                ServletException se = new ServletException("passedKeyStr is invalid");
                throw se;
            }
            if (passedKeyStr.length() < Config.getPropertyInt(Config.MAX_NUMBER)) {
                ServletException se = new ServletException("passedKeyStr is invalid");
                throw se;
            }
            //passedKeyStr = passedKeyStr.substring(1, passedKeyStr.length());
            String passstring = Base64Coder.decode(passedKeyStr);

            passedKeyStr = _stringEncrypter.decrypt(passstring);
            response.setContentType("image/jpeg");
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            String [] sprocessorClasses = SKEW_PROCESSOR_CLASS.split(":");
            ISkewImage skewImage = (ISkewImage) cl.loadClass(sprocessorClasses[(int) (Math.random() * sprocessorClasses.length)]).newInstance();
            BufferedImage bufferedImage = skewImage.skewImage(passedKeyStr.substring(0, passedKeyStr.indexOf(".")));
            
    		OutputStream out = response.getOutputStream();
    		ImageIO.write(bufferedImage, "jpg", out);
    		out.close();
            
//            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(response.getOutputStream());
//            encoder.encode(bufferedImage);
        } catch (ClassNotFoundException cnf) {
            cnf.printStackTrace();
            try {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            try {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
