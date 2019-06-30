package portal.com.eje.portal.vo.vo;

public class PrimaryKeyDefinition {
	private boolean autoIncremental;
	private String field;
	private boolean isForeignKey;
	private boolean numerica;

	public PrimaryKeyDefinition(boolean autoIncremental, String field, boolean isForeignKey, boolean numerica) {
		super();
		this.autoIncremental = autoIncremental;
		this.field = field;
		this.isForeignKey = isForeignKey;
		this.numerica = numerica;
	}

	public boolean isAutoIncremental() {
		return autoIncremental;
	}

	public String getField() {
		return field;
	}

	public boolean isForeignKey() {
		return isForeignKey;
	}

	public boolean isNumerica() {
		return numerica;
	}

}
