package cl.ejedigital.web.fileupload.vo;

import java.util.ArrayList;

import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

final public class EstadisticaReplicacion {

	private int			cantOK;
	private int			cantError;
	private ArrayList	msg;

	public EstadisticaReplicacion() {
		msg = new ArrayList();
	}

	public void addOk() {
		cantOK++;
	}

	public void addError() {
		cantError++;
	}

	public void addMsg(String msg) {
		this.msg.add(msg);
	}

	public int getCantOK() {
		return cantOK;
	}

	public int getCantError() {
		return cantError;
	}

	public SimpleList getSimpleList() {
		SimpleList lista = new SimpleList();
		
		SimpleHash hash = new SimpleHash();
		hash.put("msg"," cant replicas ok: "+cantOK);
		lista.add(hash);
		hash = new SimpleHash();
		hash.put("msg"," cant replicas fallidas: "+cantError);
		lista.add(hash);
		
		for(int i = 0; i < msg.size(); i++) {
			hash = new SimpleHash();
			hash.put("msg",(String)msg.get(i));
			
			lista.add(hash);
		}
		
		return lista;
	}

}
