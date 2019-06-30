package cl.ejedigital.web.docservice;

public enum EnumDocEstado {
	EN_DESARROLLO(0),
	PUBLICADA(10),
	DEPRECADO(15),
	ELIMINADA(20);
	
	private short idEstado;

	private EnumDocEstado(int estado) {
		this.idEstado = (short)estado;
	}

	public short getIdEstado() {
		return idEstado;
	}

	
}
