package portal.com.eje.frontcontroller.resobjects;

import java.io.ByteArrayOutputStream;

class FileOther {
	private ByteArrayOutputStream out;
	private String contentType;
	private String nameFile;
	
	protected ByteArrayOutputStream getOut() {
		return out;
	}
	
	protected void setOut(ByteArrayOutputStream out) {
		this.out = out;
	}
	
	protected String getContentType() {
		return contentType;
	}
	
	protected void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getNameFile() {
		return nameFile;
	}

	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}
	
	

}
