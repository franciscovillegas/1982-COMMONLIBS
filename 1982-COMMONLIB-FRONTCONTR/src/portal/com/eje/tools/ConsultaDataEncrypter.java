package portal.com.eje.tools;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.ConsultaDataMode;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.tools.maptool.MapEncrypter;
import portal.com.eje.tools.security.Encrypter;
import portal.com.eje.tools.security.IEncrypter;

public class ConsultaDataEncrypter extends MapEncrypter {
	private final String sufijoEncrypted = "_encrypted";
	
	public static ConsultaDataEncrypter getInstance() {
		return Util.getInstance(ConsultaDataEncrypter.class);
	}
	
	
	/**
	 * Encryptra columnas de una ConsultaData
	 * 
	 * @author Pancho
	 * @since 10-10-208
	 * */
	public void encrypt(ConsultaData data, String[] columnas) {
		encrypt(data, columnas, true, Encrypter.getInstance());
	}
	/**
	 * Encryptra columnas de una ConsultaData
	 * 
	 * @author Pancho
	 * @since 10-10-208
	 * */
	public void encrypt(ConsultaData data, String[] columnas, boolean reemplazaColumna, IEncrypter encrypter) {
		
		if(data != null && columnas != null) {
			int pos = data.getPosition();
			
			IEncrypter enc = encrypter;
			
			data.toStart();
		
			try {
				while(data.next()) {
					for(String c : columnas) {
						if(data.existField(c)) {
							if(reemplazaColumna) {
								data.put(c, enc.encrypt( data.getString(c)) );
								
//								addEncryptedColumnDef(data, c);
							}
							else {
								String fieldNameEncrypted = new StringBuilder().append(c).append(sufijoEncrypted).toString();
								
								if(!data.getNombreColumnas().contains(fieldNameEncrypted)) {
									data.getNombreColumnas().add(fieldNameEncrypted);
								}
								data.put(fieldNameEncrypted, enc.encrypt( data.getString(c)) );
							}
						}
					}
				}
			}
			finally {
				data.setPosition(pos);	
			}
			
		}
		
	}
	
//	private void addEncryptedColumnDef(ConsultaData data, String nombreColumnaOriginal) {
//		
//		
//		if(data != null) {
//			addEncryptedColumnDef(data.getActualData(), nombreColumnaOriginal);
//			String nColumna = getNombreColumnaDefEncrypted(nombreColumnaOriginal);
//			if(!data.getNombreColumnas().contains(nColumna)) {
//					data.getNombreColumnas().add(nColumna);
//			}
//		}
//		
//	}

	/**
	 * Desencripta columnas de una ConsultaData
	 * 
	 * @author Pancho
	 * @since 10-10-208
	 * */
	
	/**
	 * 
	 * */
	public void decrypt(ConsultaData data, String[] columnas) {
		decrypt(data, columnas, Encrypter.getInstance());
	}
	public void decrypt(ConsultaData data, String[] columnas, IEncrypter encrypter) {
		
		if(data != null && columnas != null) {
			int pos = data.getPosition();
			
			IEncrypter enc = encrypter;
			
			data.setMode(ConsultaDataMode.CONVERSION);
			data.toStart();
			
			while(data.next()) {
				for(String c : columnas) {
					data.getActualData().put(c, enc.decrypt( data.getString(c)) );
				}
			}

			data.setPosition(pos);
		}
		
	}

	/**
	 * Reconoce automáticamente los campos encryptados
	 * */
	public void decrypt(ConsultaData data, IEncrypter encrypter) {
		if(data != null) {
			int pos = data.getPosition();
			int solo25 = 0;
			try {
				data.toStart();
				while(data.next() && solo25 <= 25) {
					for(String c : data.getActualData().keySet()) {
						String value = data.getString(c);
						if(encrypter.isValueEncrypted(value)) {
							data.put(c, encrypter.decrypt(value));
						}
					}
					
					solo25++;
				}
			}
			finally {
				data.setPosition(pos);
			}
			
			
		}
		
	}
	
	
}
