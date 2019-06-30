package cl.ejedigital.consultor;


public class ConsultaDataPaged extends ConsultaData {
	private int totalSize;
	private ISenchaPage page;

	public ConsultaDataPaged(ConsultaData data, ISenchaPage page, int totalSize) {
		super(data.getNombreColumnas());
		int pos = data.getPosition();
		
		data.toStart();
		while(data.next()) {
			super.data.add(data.getActualData());
		}
		
		data.setPosition(pos);
	
		this.totalSize = totalSize;
	}

	public int getTotalSize() {
		return totalSize;
	}

	public ISenchaPage getPage() {
		return page;
	}
 
}
