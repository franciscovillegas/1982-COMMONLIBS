package portal.com.eje.serhumano.user.masterbean;

import org.springframework.beans.factory.annotation.Autowired;

import portal.com.eje.portal.appcontext.MasterBean;
import portal.com.eje.serhumano.user.ifaces.AbsLoginPeople;


public class SessionMasterBean {
		
	@Autowired(required=false)
	private AbsLoginPeople loginImpl;

	public static AbsLoginPeople getMasterBean() {
		return MasterBean.getMasterBean(SessionMasterBean.class).loginImpl;
	}
	
	public static void main(String[] args) {
		System.out.println( SessionMasterBean.getMasterBean() );
	}
}
