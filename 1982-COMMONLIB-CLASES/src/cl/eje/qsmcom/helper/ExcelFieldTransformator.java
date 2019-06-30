package cl.eje.qsmcom.helper;

import portal.com.eje.tools.excel.xlsx.IReplaceField;

public class ExcelFieldTransformator implements IReplaceField {

	public String replaceNumber(String input) {
		if(input != null) {
			input = input.replaceAll("//.", "+");
			input = input.replaceAll("//,", ".");
			input = input.replaceAll("//+", ",");
		}
		return input;
	}

	public String replaceFormula(String input) {
		// TODO Auto-generated method stub
		return input;
	}

	public String replaceInLnStr(String input) {
		// TODO Auto-generated method stub
		return input;
	}

	public String replaceSSTindex(String input) {
		// TODO Auto-generated method stub
		return input;
	}

}
