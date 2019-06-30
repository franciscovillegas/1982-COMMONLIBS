package cl.ejedigital.web.fmrender;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

public class FmRenderers<T> {
	private Map<String,FmRenderer<T>> renderers;

	public FmRenderers() {
		super();
		this.renderers = new HashMap<String,FmRenderer<T>>();
	}
	
	public static <T>FmRenderers<T> prepare(IRenderer<T> renderer, String field) {
		FmRenderers<T> varios = new FmRenderers<T>();
		varios.add(renderer, field);
		
		return varios;
	}

	public Map<String,FmRenderer<T>> getRenderers() {
		return renderers;
	}
	
	public FmRenderer<T> getRender(String field) {
		return renderers.get(field);
	}
	
	public FmRenderers<T> add(IRenderer<T> renderer, String field) {
		Assert.notNull(renderer, "No puede ser null ");
		Assert.notNull(field, "No puede ser null ");
		
		FmRenderer<T> r = new FmRenderer<T>(renderer, field);
		
		renderers.put(field, r);
		return this;
	}
	
	
	public FmRenderers<T> add(FmRenderer<T> renderer) {
		this.renderers.put(renderer.getField(), (FmRenderer<T>) renderer);
		
		return this;
	}

}
