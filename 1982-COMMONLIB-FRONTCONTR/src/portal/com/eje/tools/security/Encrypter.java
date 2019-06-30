package portal.com.eje.tools.security;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import com.thoughtworks.xstream.core.util.Base64Encoder;

import cl.ejedigital.tool.misc.Cronometro;
import portal.com.eje.portal.factory.Weak;
import portal.com.eje.portal.vo.ClaseConversor;


/**
 * <b>The following code implements a class for encrypting and decrypting
 * strings using several Cipher algorithms. The class is created with a key and
 * can be used repeatedly to encrypt and decrypt strings using that key.
 * Some of the more popular algorithms are:
 * <li>Blowfish
 * <li>DES
 * <li>DESede
 * <li>PBEWithMD5AndDES
 * <li>PBEWithMD5AndTripleDES
 * <li>TripleDES
 * </b>
 */
public class Encrypter implements IEncrypter {	 
	private final String PREFIJO= "1Ag1dh";	
	 
	
	
	public final List<Class<?>> encryptables = new ArrayList<>();
	
	public static IEncrypter getInstance() {
		return Weak.getInstance(Encrypter.class);
	}
	
    private String PASS_CODE;

    private Cipher ecipher;
    private Cipher dcipher;

    public Encrypter() {
       this((String)null);
    }
    
    /**
     * Constructor used to create this object.  Responsible for setting
     * and initializing this object's encrypter and decrypter Chipher instances
     * given a Secret Key and algorithm.
     *
     * @param key       Secret Key used to initialize both the encrypter and
     *                  decrypter instances.
     * @param algorithm Which algorithm to use for creating the encrypter and
     *                  decrypter instances.
     */
    public Encrypter(SecretKey key, String algorithm) {
        try {
            ecipher = Cipher.getInstance(algorithm);
            dcipher = Cipher.getInstance(algorithm);
            ecipher.init(Cipher.ENCRYPT_MODE, key);
            dcipher.init(Cipher.DECRYPT_MODE, key);
        } catch (NoSuchPaddingException e) {
            System.err.println("EXCEPTION: NoSuchPaddingException");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("EXCEPTION: NoSuchAlgorithmException");
        } catch (InvalidKeyException e) {
            System.err.println("EXCEPTION: InvalidKeyException");
        }
        
        populateEncryptablesClass();
        
         
      
    }

    /**
     * Constructor used to create this object
     * Pass code used PASS_CODE
     */
    public Encrypter(String PASS_CODE) {
        // 8-bytes Salt
        byte[] salt = {
                (byte) 0xAA, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
                (byte) 0x56, (byte) 0x11, (byte) 0xE3, (byte) 0x03
        };

        // Iteration count
        int iterationCount = 19;

        try {
        	if(PASS_CODE == null) {
        		 PASS_CODE  = Config.getProperty(Config.PASS_CODE);
        	}
        	 
        	this.PASS_CODE = PASS_CODE;
        	 
        	
            KeySpec keySpec = new PBEKeySpec(PASS_CODE.toCharArray(), salt, iterationCount);
            SecretKey key = SecretKeyFactory.getInstance(Config.
                    getProperty(Config.ALGORITHM)).generateSecret(keySpec);

            ecipher = Cipher.getInstance(key.getAlgorithm());
            dcipher = Cipher.getInstance(key.getAlgorithm());

            // Prepare the parameters to the cipthers
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

        } catch (InvalidAlgorithmParameterException e) {
            System.err.println("EXCEPTION: InvalidAlgorithmParameterException");
        } catch (InvalidKeySpecException e) {
            System.err.println("EXCEPTION: InvalidKeySpecException");
        } catch (NoSuchPaddingException e) {
            System.err.println("EXCEPTION: NoSuchPaddingException");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("EXCEPTION: NoSuchAlgorithmException");
        } catch (InvalidKeyException e) {
            System.err.println("EXCEPTION: InvalidKeyException");
        }
        
        populateEncryptablesClass();
    }
    
    private void populateEncryptablesClass() {
    	encryptables.add(String.class);
    	encryptables.add(Integer.class);
    	encryptables.add(Byte.class);
    	encryptables.add(Short.class);
    	encryptables.add(Double.class);
    	encryptables.add(Long.class);
    	encryptables.add(Float.class);
    	
    	encryptables.add(int.class);
    	encryptables.add(byte.class);
    	encryptables.add(short.class);
    	encryptables.add(double.class);
    	encryptables.add(long.class);
    	encryptables.add(float.class);
    }

    /**
     * Takes a single String as an argument and returns an Encrypted version
     * of that String.
     *
     * @param str String to be encrypted
     * @return <code>String</code> Encrypted version of the provided String
     */
    public String encrypt(String str) {
    	String retorno = null;
        try {
            // Encode the string into bytes using utf-8
            byte[] utf8 = str.getBytes("UTF8");

            // Encrypt
            byte[] enc = ecipher.doFinal(utf8);

            // Encode bytes to base64 to get a string
            retorno = new Base64Encoder().encode(enc);
        } catch (BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (Exception e) {
        } 
        return privateApprendPrefijo(retorno);
    }
    
    public List<String> encrypt( List<String> lista) {
    	List<String> retorno = new ArrayList<String>();
    	for(String i : lista) {
    		retorno.add( encrypt(i));
    	}
    	
    	return retorno;
    }

    
    public List<String> decrypt( List<String> lista) {
    	List<String> retorno = new ArrayList<String>();
    	for(String i : lista) {
    		retorno.add( decrypt(i));
    	}
    	
    	return retorno;
    }

    /**
     * Takes a encrypted String as an argument, decrypts and returns the
     * decrypted String.
     *
     * @param str Encrypted String to be decrypted
     * @return <code>String</code> Decrypted version of the provided String#0a!%!#s3Uwi94BdpI=
     */
    public String decrypt(String str) {
    	str = privateRemovePrefijo(str);
    	String retorno = null;
    	

        try {
        	if(str != null) {
        		// Decode base64 to get bytes
 
 
                // Decode using utf-8
                retorno = new String( dcipher.doFinal(new Base64Encoder().decode(str)), "UTF8");
                 
        	}
            

        } catch (BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (IOException e) {
        }
        return retorno;
    }

	public String encrypt(Integer numero) {
		return encrypt(ClaseConversor.getInstance().getObject(numero, String.class));
	}

	public String encrypt(Object value) {
		String retorno = null;
		
		if(value != null) {
			retorno = encrypt(ClaseConversor.getInstance().getObject(value, String.class));
		}
		
		return retorno;
		
	}


	protected String privateApprendPrefijo(String value) {
		if(value != null && PREFIJO != null) {
			value = new StringBuilder().append(PREFIJO).append(value).toString();
		}
		return value;
	}
	
	protected String privateRemovePrefijo(String value) {
		if(value != null && value.startsWith(PREFIJO)) {
			value = value.substring(PREFIJO.length(), value.length());
		}
		return value;
	}
	
	@Override
	public boolean isValueEncrypted(String value) {
		
		boolean es = value != null && value.startsWith(PREFIJO);
		
		return es;
	}
	

	public static void main(String[] params) {
		Cronometro cro = new Cronometro();
		cro.start();
		
		Encrypter e = new Encrypter();
		String w = "ayayayaydasasdasddsadasdasdsadasdsadsadsadasasasasadasddsadasdasdasdasdasdasa";
		for (int i = 0; i < 1000000; i++) {
			if (!w.equals(e.decrypt(e.encrypt(w)))) {
				System.out.println(e.encrypt(w) + "         [" + i + "]");
			}
		}

		cro.printTimeHHMMSS_milli();
	}
}

