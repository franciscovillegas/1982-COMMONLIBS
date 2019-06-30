package portal.com.eje.tools.security;

import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import cl.ejedigital.tool.misc.Cronometro;
import portal.com.eje.portal.vo.ClaseConversor;

class EncrypterAES25_2 extends AbsDefaultEncrypter implements IEncrypter {
	private static String secretKey = "boooooooooom!!!!";
	private static String salt = "ssshhhhhhhhhhh!!!!";
	private Cipher cipher_decrypter;
	private Cipher cipher_encrypter;

	public EncrypterAES25_2() {
		init_encrypter();
		init_decrypter();
	}

	private void init_encrypter() {
		{
			 
			try {
				byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
				IvParameterSpec ivspec = new IvParameterSpec(iv);

				SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
				KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 256);
				SecretKey tmp = factory.generateSecret(spec);
				SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

				cipher_encrypter = Cipher.getInstance("AES/CBC/PKCS5Padding");
				cipher_encrypter.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);

			} catch (Exception e) {
				System.out.println("Error while encrypting: " + e.toString());
			}

		}
	}

	private void init_decrypter() {
		try {
			byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			IvParameterSpec ivspec = new IvParameterSpec(iv);

			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

			cipher_decrypter = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher_decrypter.init(Cipher.DECRYPT_MODE, secretKey, ivspec);

		} catch (Exception e) {
			System.out.println("Error while decrypting: " + e.toString());
		}

	}

	@Override
	public String encrypt(Object o) {
		String strToEncrypt = ClaseConversor.getInstance().getObject(o, String.class);
		try {
			 
			return Base64.getEncoder().encodeToString(this.cipher_encrypter.doFinal(strToEncrypt.getBytes("UTF-8")));
		} catch (Exception e) {
			System.out.println("Error while encrypting: " + e.toString());
		}
		return null;
	}

	@Override
	public String decrypt(String strToDecrypt) {
		try {

			return new String(this.cipher_decrypter.doFinal(Base64.getDecoder().decode(strToDecrypt)));
		} catch (Exception e) {
			System.out.println("Error while decrypting: " + e.toString());
		}
		return null;
	}

	public static void main(String[] params) {
		Cronometro cro = new Cronometro();
		cro.start();
		
		EncrypterAES25_2 e = new EncrypterAES25_2();
		String w = "ayayayaydasasdasddsadasdasdsadasdsadsadsadasasasasadasddsadasdasdasdasdasdasa";
		for (int i = 0; i < 1000000; i++) {
			if (!w.equals(e.decrypt(e.encrypt(w)))) {
				System.out.println(e.encrypt(w) + "         [" + i + "]");
			}
		}

		cro.printTimeHHMMSS_milli();
	}

}
