package cl.ejedigital.tool.votools;

import java.util.Comparator;



class VoComparator <T extends IVoBase> implements Comparator<T> {
	private boolean desc;

	VoComparator(boolean desc) {
		this.desc = desc;
	}
	
	public int compare(T o1, T o2) {
		if(o1.getVoCode() < o2.getVoCode()) {
			return putMod(-1);
		}
		else if(o1.getVoCode() > o2.getVoCode()) {
			return putMod(1);
		}
		return 0;
	}
	
	private int putMod(int i) {
		return this.desc ? i * -1 : i;
	}

}
