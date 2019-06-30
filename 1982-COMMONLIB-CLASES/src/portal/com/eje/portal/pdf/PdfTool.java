package portal.com.eje.portal.pdf;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.NotImplementedException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

import cl.ejedigital.tool.strings.MyString;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.pdf.CWebTransformPdf;
import portal.com.eje.portal.factory.SingleFactory;
import portal.com.eje.portal.factory.SingleFactoryType;

public class PdfTool implements IPdf {

	/**
	 * Construye un pdf, con un nombre de archivo random ubicado en temporal/
	 * no se verán
	 * 
	 * @author Pancho
	 * @since 23-05-2018
	 */
	public File buildPdf(HttpFlow flow) throws IOException {

		CWebTransformPdf pdf = SingleFactory.getFactory(SingleFactoryType.UTIL).getInstance(CWebTransformPdf.class);
		
		try {
			MyString my = SingleFactory.getFactory(SingleFactoryType.UTIL).getInstance(MyString.class);
			String fileName = my.getRandomString("abcdefghijklmnopqrstuvwxyz0123456789", 30);
			return pdf.getFile(flow.getFlow(), fileName, null);
		} catch (IOException e) {
			throw e;
		}
	}
	
	/**
	 * Construye un pdf, en la dirección file que se le indique
	 * no se verán
	 * 
	 * @author Pancho
	 * @since 23-05-2018
	 */
	public File buildPdf(HttpFlow flow,File file) throws IOException {

		try {
			return CWebTransformPdf.getInstance().getFile(null, flow.getFlow(), file);
		} catch (IOException e) {
			throw e;
		}
	}

	/**
	 * Construye un pdf, SI reemplaza paths relativos asi que las imágenes si se
	 * verán
	 * 
	 * @author Pancho
	 * @since 23-05-2018
	 */

	@Override
	public File buildPdf(IOClaseWeb io, HttpFlow flow) throws IOException {
		throw new NotImplementedException();
	}

	public File setPassword(File fileFinal, String password) throws InvalidPasswordException, IOException {

		// String rutaPdfEncryp = "liquidaciones\\" + nombre + ".pdf";

		PDDocument document = PDDocument.load(fileFinal);

		// Creating access permission object
		AccessPermission ap = new AccessPermission();

		// Creating StandardProtectionPolicy object
		StandardProtectionPolicy spp = new StandardProtectionPolicy(password, password, ap);

		// Setting the length of the encryption key
		spp.setEncryptionKeyLength(128);

		// Setting the access permissions
		spp.setPermissions(ap);

		// Protecting the document
		document.protect(spp);

		// Saving the document
		document.save(fileFinal);

		// Closing the document

		document.close();

		return fileFinal;

	}

}
