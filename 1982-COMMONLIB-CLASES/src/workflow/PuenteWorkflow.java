package workflow;

import freemarker.template.SimpleHash;
import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;


public class PuenteWorkflow extends AbsClaseWeb{

	public PuenteWorkflow(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		// TODO Auto-generated constructor stub
	}

	public void doGet() throws Exception {
		String scheme = super.getIoClaseWeb().getReq().getScheme();            
		String serverName = super.getIoClaseWeb().getReq().getServerName();     
		int serverPort = super.getIoClaseWeb().getReq().getServerPort();        
		String contextPath = "";// super.getIoClaseWeb().getReq().getContextPath();   
		String urlBase = scheme+"://"+serverName+":"+serverPort+contextPath;
		String htm = "workflow/puente_workflow.html"; 
		String rut = super.getIoClaseWeb().getUsuario().getRutId();
		String password = super.getIoClaseWeb().getUsuario().getPassWord();
		String digito = super.getIoClaseWeb().getUsuario().getRut().getDig();
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("rut", rut);
		modelRoot.put("pass", password);
		modelRoot.put("dig", digito);
		modelRoot.put("url", urlBase);
		super.getIoClaseWeb().retTemplate(htm, modelRoot);
	}

	@Override
	public void doPost() throws Exception {
		// TODO Auto-generated method stub
		doGet();
	}

}
