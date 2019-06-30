package portal.com.eje.tools.security;

public class EncrypterDynamic extends Encrypter {
	 
	/**
	 * @deprecated
	 * */
	public static IEncrypter getInstance(String key) {
		return Encrypter.getInstance();
	}
	
}
