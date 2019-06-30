package portal.com.eje.genericconf;

import portal.com.eje.genericconf.ifaces.IButtonBootstrapBadgeteable;

public class AbsButtonBootstrapBadgeteable extends AbsButton implements IButtonBootstrapBadgeteable  {
	private Integer badgeDanger;
	private Integer badgeWarning;
	private Integer badgePrimary;
	
	@Override
	public Integer getBadgeDanger() {
		return badgeDanger;
	}
	@Override
	public void setBadgeDanger(Integer badgeDanger) {
		this.badgeDanger = badgeDanger;
	}
	@Override
	public Integer getBadgeWarning() {
		return badgeWarning;
	}
	@Override
	public void setBadgeWarning(Integer badgeWarning) {
		this.badgeWarning = badgeWarning;
	}
	@Override
	public Integer getBadgePrimary() {
		return badgePrimary;
	}
	@Override
	public void setBadgePrimary(Integer badgePrimary) {
		this.badgePrimary = badgePrimary;
	} 
	
	
	
}
