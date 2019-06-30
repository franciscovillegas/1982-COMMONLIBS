package portal.com.eje.tools.security;

import java.util.List;

public interface IEncrypter {
	
	public List<String> decrypt( List<String> lista);
    /**
     * Takes a encrypted String as an argument, decrypts and returns the
     * decrypted String.
     *
     * @param str Encrypted String to be decrypted
     * @return <code>String</code> Decrypted version of the provided String
     */
    public String decrypt(String str);
    
	public String encrypt(Integer numero);

	public String encrypt(Object value);
	
	public boolean isValueEncrypted(String value);
}
