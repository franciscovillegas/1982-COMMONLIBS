package portal.com.eje.portal.vo.vo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import portal.com.eje.portal.zip.ZipTool;

public class TableDefinition {
	private String jndi;
	private String table;
	private List<PrimaryKeyDefinition> pks;
	
	public TableDefinition(String jndi, String table) {
		super();
		this.jndi = jndi;
		this.table = table;
		
		pks = new ArrayList<PrimaryKeyDefinition>();
	}

	public String getJndi() {
		return jndi;
	}
	 
	public String getTable() {
		return table;
	}
	 
	public void addPk(PrimaryKeyDefinition pkd) {
		if(pkd != null) {
			pks.add(pkd);
		}
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(jndi).append("|").append(table).append("[").append("@pks").append("]");
		
		StringBuilder strPks = new StringBuilder();
		for(PrimaryKeyDefinition pk : pks) {
			if(!strPks.toString().equals("")) {
				strPks.append(",");
			}
			strPks.append("{")
			.append(pk.isAutoIncremental()?1:0).append("|")
			.append(pk.getField()).append("|")
			.append(pk.isForeignKey()?1:0).append("|")
			.append(pk.isNumerica()?1:0).append("|")
			.append("}");
		}
		
		String strFinal = str.toString().replaceAll("@pks", strPks.toString());
		return strFinal;
	}
	
	public String encodeString() {
		return ZipTool.getInstance().compressToString(toString());
	}
	
	public String encodeUrl() {
		String encoded = encodeString();
		
		try {
			return URLEncoder.encode(encoded,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static TableDefinition decode(String str) throws Exception {
		try {
			str = ZipTool.getInstance().deCompress(str);
			String primera = str.substring(0,str.indexOf("["));
			String partesPrimera[] = primera.split("\\|");
			String jndi = partesPrimera[0];
			String table = partesPrimera[1];
			
			TableDefinition td = new TableDefinition(jndi, table);
			
			String segunda = str.substring(str.indexOf("[")+1,str.indexOf("]"));
			String pks[] = segunda.split("\\,");
			for(String pk : pks) {
				String partesPk[] = pk.split("\\|");
				boolean isAutoIncremental = "1".equals(partesPk[0]);
				String field = partesPk[1];
				boolean isForeignKey = "1".equals(partesPk[2]); 
				boolean isNumerica = "1".equals(partesPk[3]); 
				
				td.addPk(new PrimaryKeyDefinition(isAutoIncremental, field, isForeignKey, isNumerica));
			}
			
			return td;
		}
		catch(Exception e) {
			throw e;
		}

	}
}
