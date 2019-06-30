package cl.ejedigital.tool.misc;

public class JavaVersion {

	public static Double getVersion() {
		String version = System.getProperty("java.specification.version");
		return Double.parseDouble(version);
	}
	
}
