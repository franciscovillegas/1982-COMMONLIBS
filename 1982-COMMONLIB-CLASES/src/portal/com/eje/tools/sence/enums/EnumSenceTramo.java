package portal.com.eje.tools.sence.enums;

public enum EnumSenceTramo {
	DESCONOCIDO((byte)0, "Hubo un error al identificar el tramo", 0),
	TRAMO1((byte)1, "Si el total de haberes de la persona es menor a 26 veces el valor de la uf", 1),
	TRAMO2((byte)2, "Si el total de haberes de la persona es mayor o igual a 26 veces el valor de la uf, pero menor a 51 veces el valor de la uf", 0.5),
	TRAMO3((byte)3, "Si el total de haberes de la persona es mayor o igual a 51 veces el valor de la uf", 0.15);

	private byte id_tramo_sence;
	private String tooltip;
	private double factorAporteSence;

	private EnumSenceTramo(byte id_tramo_sence, String tooltip, double factorAporteSence) {
		this.id_tramo_sence = id_tramo_sence;
		this.tooltip = tooltip;
		this.factorAporteSence = factorAporteSence;
	}

	public byte getId_tramo_sence() {
		return id_tramo_sence;
	}

	public String getTooltip() {
		return tooltip;
	}

	public double getFactorAporteSence() {
		return factorAporteSence;
	}

	@Override
	public String toString() {
		return name();
	}

}
