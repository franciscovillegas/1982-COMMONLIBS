package cl.ejedigital.tool.encript;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ManagerMD5 {

	public ManagerMD5() {

	}

	public String encrypt(String texto) {
		StringBuffer sb;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(texto.getBytes());
			byte result[] = md5.digest();
			sb = new StringBuffer();
			for (int i = 0; i < result.length; i++) {
				String s = Integer.toHexString(result[i]);
				int length = s.length();
				if (length >= 2) {
					sb.append(s.substring(length - 2, length));
				} else {
					sb.append("0");
					sb.append(s);
				}
			}
		} catch (NoSuchAlgorithmException e) {
			return "";
		}
		return sb.toString();
	}

}