package cl.ejedigital.web.fileupload.vo;

public class EjeFilesUnico {
	private Integer idFile;
	private String idFileEncrypted;
	private Object idTipo;
	private String fechaSubida;
	private int rutSubida;
	private String nameOriginal;
	private String nameUnic;
	private String machineName;
	private long bytes;
	private String text;
	private String fullHddPath;
	private String relativePathToWebcontent;
	private boolean isweb;

	public EjeFilesUnico(Integer idFile, Object idTipo, String fechaSubida, int rutSubida, String nameOriginal,
						String nameUnic, long bytes, String machineName, String text, String fullHddPath,
						String relativePathToWebcontent, boolean isweb) {
		super();
		this.idFile = idFile;
		this.idTipo = idTipo;
		this.fechaSubida = fechaSubida;
		this.rutSubida = rutSubida;
		this.nameOriginal = nameOriginal;
		this.nameUnic = nameUnic;
		this.bytes = bytes;
		this.machineName = machineName;
		this.text = text;
		this.fullHddPath = fullHddPath;
		this.relativePathToWebcontent = relativePathToWebcontent;
		this.isweb = isweb;
	}

	public int getIdFile() {
		return idFile;
	}

	/**
	 * @deprecated
	 * @since 2015-dic-17
	 * @author Francisco
	 * 
	 *         Ahora los tipos pueden ser variados, se pasa una cadena, por ende
	 *         no siempre entregará un EjeFileUnicoTipo. El método que vale es
	 *         getIdTipoObject()
	 * 
	 */
	public EjeFileUnicoTipo getIdTipo() {
		if (idTipo instanceof EjeFileUnicoTipo) {
			return (EjeFileUnicoTipo) idTipo;
		} else {
			throw new RuntimeException("No pertenece al tipo, en 2015-dic-17, Francisco decidió "
					+ "que los tipos no serán estáticos o determinados por la BD, cada implementador podrá pasar cadenas de caracteres "
					+ "y el objeto los transformará en identicadores únicos, esto debido a que en la BD los archivos grabados no eran facilmente "
					+ "identificable.");
		}
	}

	public Object getIdTipoObject() {
		return idTipo;
	}

	public String getFechaSubida() {
		return fechaSubida;
	}

	public int getRutSubida() {
		return rutSubida;
	}

	public String getNameOriginal() {
		return nameOriginal;
	}

	public String getNameUnic() {
		return nameUnic;
	}

	public String getMachineName() {
		return machineName;
	}

	public long getBytes() {
		return bytes;
	}

	public String getText() {
		return text;
	}

	public String getFullHddPath() {
		return fullHddPath;
	}

	public String getRelativePathToWebcontent() {
		return relativePathToWebcontent;
	}

	public boolean isIsweb() {
		return isweb;
	}

	public String getIdFileEncrypted() {
		return idFileEncrypted;
	}

	public void setIdFileEncrypted(String idFileEncrypted) {
		this.idFileEncrypted = idFileEncrypted;
	}

 

	
}
