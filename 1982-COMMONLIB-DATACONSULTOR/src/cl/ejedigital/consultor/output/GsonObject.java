package cl.ejedigital.consultor.output;

public class GsonObject {
	private String word;
	
	public GsonObject(String word) {
		this.word = word;
	}

	public String toString() {
		return this.word;
	}
}
