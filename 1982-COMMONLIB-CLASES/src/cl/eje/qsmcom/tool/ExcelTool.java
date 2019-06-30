package cl.eje.qsmcom.tool;

public class ExcelTool {
	private String pal1;
	
	public ExcelTool() {
		 pal1 = "abcdefghijklmnopqrstuvwxyz";
	}
	
	public String getColumnName(int columnPos) {
		int pos = 0;
		String pal = "";
		for(int i = 0; i < pal1.length() ;i++) {
			String p1 = pal1.substring(i, i+1);
			if(pos >= columnPos) {
				break;
			}
			
			pal = p1;
			pos++;
		}
		
		for(int i = 0; i < pal1.length() ;i++) {
			String p1 = pal1.substring(i, i+1);
			if(pos >= columnPos) {
				break;
			}
			
			for(int j = 0; j < pal1.length() ;j++) {
				String p2 = pal1.substring(j, j+1);
				
				if(pos >= columnPos) {
					break;
				}
				
				pal = p1 + p2;
				pos++;
			}
		}
		
		return pal;
	}
	
}
