package cl.ejedigital.consultor;

public class Row {
	private DataFields df;

	private Row(String key, Object o) {
		df = new DataFields();
		df.put(key, o);
	}

	public static Row column(String key, Object o) {
		Row r = new Row(key, o);
		return r;
	}

	public Row add(String key, Object o) {
		df.put(key, o);

		return this;
	}

	public DataFields build() {
		return df;
	}
}
