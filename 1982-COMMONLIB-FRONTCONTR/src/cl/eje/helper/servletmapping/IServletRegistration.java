package cl.eje.helper.servletmapping;

import javax.servlet.ServletRegistration;

public interface IServletRegistration {
	 public void setMapping(ServletRegistration.Dynamic myServlet);
}
