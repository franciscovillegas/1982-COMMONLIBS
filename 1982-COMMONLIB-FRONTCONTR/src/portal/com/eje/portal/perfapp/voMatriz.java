package portal.com.eje.portal.perfapp;

import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.IConsultaDataRow;

public class voMatriz implements IConsultaDataRow {
	
	private voZona zona;
	private voGrupo grupo;
	private voObjeto objeto;
	private boolean bolActivo; 

	public voMatriz(voZona zona, voGrupo grupo, voObjeto objeto) {
		super();
		this.zona = zona;
		this.grupo= grupo;
		this.objeto = objeto;
	}

	public voMatriz(voZona zona, voGrupo grupo, voObjeto objeto, boolean bolActivo) {
		super();
		this.zona = zona;
		this.grupo= grupo;
		this.objeto = objeto;
		this.bolActivo = bolActivo;
	}

	public voZona getZona() {
		return zona;
	}
	
	public voGrupo getGrupo() {
		return grupo;
	}
	
	public voObjeto getObjeto() {
		return objeto;
	}

	public boolean getActivo() {
		return bolActivo;
	}
	
	public void setActivo(boolean bolActivo) {
		this.bolActivo = bolActivo;
	}
	
	public DataFields toDataField() {
		
		DataFields data = new DataFields();

		data.put("id_zona", new Field(this.getZona().getId()));
		data.put("zona", new Field(this.getZona().getNombre()));
		data.put("zonacodigo", new Field(this.getZona().getCodigo()));
		data.put("id_grupo", new Field(this.getGrupo().getId()));
		data.put("grupo", new Field(this.getGrupo().getNombre()));
		data.put("grupocodigo", new Field(this.getGrupo().getCodigo()));
		data.put("id_objeto", new Field(this.getObjeto().getId()));
		data.put("objeto", new Field(this.getObjeto().getNombre()));
		data.put("objetocodigo", new Field(this.getObjeto().getCodigo()));
		
		return data;
		
	}
	
}
