package cl.ejedigital.tool.strings;

public class SqlBuilder  implements CharSequence {
	private StringBuilder sql;
	
	public SqlBuilder() {
		this("");
	}
	
	public SqlBuilder(String str) {
		sql = new StringBuilder(str);
	}
	
	
	public SqlBuilder line(CharSequence str) {
		 appendLine(str);
		 
		 return this;
	}
	
	public SqlBuilder appendLine(CharSequence str) {
		 sql.append(str).append("\n");
		 
		 return this;
	}
	
	public SqlBuilder append(CharSequence str) {
		 sql.append(str);
		 
		 return this;
	}
	
	public SqlBuilder append(Integer integer) {
		 sql.append(integer);
		 
		 return this;
	}

	@Override
	public int length() {
		// TODO Auto-generated method stub
		return sql.length();
	}

	@Override
	public char charAt(int index) {
		// TODO Auto-generated method stub
		return sql.charAt(index);
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		// TODO Auto-generated method stub
		return sql.subSequence(start, end);
	}
	
	public String toString() {
		return sql.toString();
	}
	
}
