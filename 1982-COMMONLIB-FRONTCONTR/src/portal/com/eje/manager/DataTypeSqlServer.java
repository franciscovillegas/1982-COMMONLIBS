package portal.com.eje.manager;


public enum DataTypeSqlServer {
	Bigint("Bigint"),
	Int("Int"),
	Smallint("Smallint"),
	Tinyint("Tinyint"),
	Bit("Bit"),
	Decimal("Decimal"),
	Numeric("Numeric"),
	Money("Money"),
	Smallmoney("Smallmoney"),
	Float("Float"),
	Real("Real"),
	Datetime("Datetime"),
	Smalldatetime("Smalldatetime"),
	Date("Date"),
	Time("Time"),
	Datetime2("Datetime2"),
	Datetimeoffset("Datetimeoffset"),
	Char("Char"),
	Varchar("Varchar"),
	Text("Text"),
	Nchar("Nchar"),
	Nvarchar("Nvarchar"),
	Ntext("Ntext"),
	Binary("Binary"),
	Varbinary("Varbinary"),
	Image("Image"),
	Sql_variant("Sql_variant"),
	Timestamp("Timestamp"),
	Uniqueidentifier("Uniqueidentifier"),
	Xml("Xml"),
	Cursor("Cursor"),
	Table("Table");

	private String tipo;
	
	DataTypeSqlServer(String tipo) {
		this.tipo = tipo;
	}
	
	public String toString() {
		return tipo;
	}
}
