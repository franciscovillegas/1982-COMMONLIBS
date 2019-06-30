package portal.com.eje.tools.fileupload.vo;


/**
 * Cambiado por su homologá en el paquete cl.ejedigital.web.
 * 
 * @deprecated 
 * 
 * */
public class EjeFilesUnico {
	private int idFile;
	private EjeFileUnicoTipo idTipo;
	private String fechaSubida;
	private int rutSubida;
	private String nameOriginal;
	private String nameUnic;
	private String machineName;
	private long bytes;

	public EjeFilesUnico(int idFile,  EjeFileUnicoTipo idTipo, String fechaSubida,
			int rutSubida, String nameOriginal, String nameUnic, long bytes, String machineName) {
		super();
		this.idFile = idFile;
		this.idTipo = idTipo;
		this.fechaSubida = fechaSubida;
		this.rutSubida = rutSubida;
		this.nameOriginal = nameOriginal;
		this.nameUnic = nameUnic;
		this.bytes = bytes;
		this.machineName = machineName;
	}
	
	public EjeFilesUnico(EjeFileUnicoTipo idTipo, String fechaSubida,
			int rutSubida, String nameOriginal, String nameUnic, long bytes) {
		this(-1, idTipo, fechaSubida, rutSubida, nameOriginal, nameUnic, bytes, null);
		
	}
	
	/**
	 * Cuando se crea una nueva
	 * 
	 * */
	public EjeFilesUnico(EjeFileUnicoTipo idTipo, 
			int rutSubida, String nameOriginal, String nameUnic, long bytes, String machineName) {
		this(-1, idTipo, "", rutSubida, nameOriginal, nameUnic, bytes, machineName );
		
	}

	public int getIdFile() {
		return idFile;
	}

	public EjeFileUnicoTipo getIdTipo() {
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

}
