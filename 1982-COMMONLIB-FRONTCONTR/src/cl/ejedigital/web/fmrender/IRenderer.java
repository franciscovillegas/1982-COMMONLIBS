package cl.ejedigital.web.fmrender;

public interface IRenderer<T> {

	public String renderer(T t, String fieldToRender, int counter);
	
}
