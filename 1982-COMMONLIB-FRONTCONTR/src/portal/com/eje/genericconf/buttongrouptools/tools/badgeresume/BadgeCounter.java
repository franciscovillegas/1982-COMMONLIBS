package portal.com.eje.genericconf.buttongrouptools.tools.badgeresume;

import java.util.List;

import portal.com.eje.genericconf.ifaces.IButton;
import portal.com.eje.genericconf.ifaces.IButtonBootstrapBadgeteable;

class BadgeCounter implements IButtonBootstrapBadgeteable {
	 private Integer badgeDanger = null;
	 private Integer badgeWarning = null;
	 private Integer badgePrimary = null;
	
	 private BadgeCounter() {
		 
	 }

	public static BadgeCounter resume(List<IButton> botones) {
		BadgeCounter retorno = new BadgeCounter();
		
		if(botones != null) {
			for(IButton button : botones) {
				if(IButtonBootstrapBadgeteable.class.isAssignableFrom(button.getClass()) ) {
					IButtonBootstrapBadgeteable badget = (IButtonBootstrapBadgeteable) button;
					retorno.appendBadge(badget);	
				}
			}
		}
		
		return retorno;
	}
	
	private void appendBadge(IButtonBootstrapBadgeteable bad) {
		if(bad != null) {
			if(bad.getBadgeDanger() != null) {
				if(this.badgeDanger ==null) {
					this.badgeDanger = 0;
				}
				this.badgeDanger += bad.getBadgeDanger();	
			}
			
			if(bad.getBadgeWarning() != null) {
				if(this.badgeWarning ==null) {
					this.badgeWarning = 0;
				}
				this.badgeWarning += bad.getBadgeWarning();
			}
			
			if(bad.getBadgePrimary() != null) {
				if(this.badgePrimary ==null) {
					this.badgePrimary = 0;
				}
				
				this.badgePrimary += bad.getBadgePrimary();	
			}
			
		}
	}


	@Override
	public Integer getBadgeDanger() {
		return this.badgeDanger;
	}


	@Override
	public void setBadgeDanger(Integer badgeDanger) {
		this.badgeDanger = badgeDanger;
		
	}


	@Override
	public Integer getBadgeWarning() {
		return this.badgeWarning;
	}


	@Override
	public void setBadgeWarning(Integer badgeWarning) {
		 this.badgeWarning = badgeWarning;
		
	}


	@Override
	public Integer getBadgePrimary() {
		return this.badgePrimary;
	}


	@Override
	public void setBadgePrimary(Integer badgePrimary) {
		this.badgePrimary = badgePrimary;
	}
}
