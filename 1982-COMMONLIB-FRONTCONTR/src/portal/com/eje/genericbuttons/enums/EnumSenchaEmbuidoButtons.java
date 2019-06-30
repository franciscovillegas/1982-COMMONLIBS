package portal.com.eje.genericbuttons.enums;

public enum EnumSenchaEmbuidoButtons {
	SAVE("s"),
	CLOSE("c");
	
	private String buttons;

	private EnumSenchaEmbuidoButtons(String buttons) {
		this.buttons = buttons;
	}

	public String getButtons() {
		return buttons;
	}
 
}
