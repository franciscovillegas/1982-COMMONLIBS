package portal.com.eje.genericconf.enums;

public enum EnumTooltipPlacement {
	TOP("top"),
	BOTTOM("bottom"),
	LEFT("left"),
	RIGHT("right");
	
	private String placement;

	private EnumTooltipPlacement(String placement) {
		this.placement = placement;
	}

	public String getPlacement() {
		return placement;
	}
	
	@Override
	public String toString() {
		return this.placement;
	}
	
}
