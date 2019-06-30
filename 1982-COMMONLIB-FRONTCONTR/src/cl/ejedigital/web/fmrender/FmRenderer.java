package cl.ejedigital.web.fmrender;

import java.util.HashMap;
import java.util.Map;

public class FmRenderer<T> {
	private IRenderer<T> renderer;
	private String field;

	public FmRenderer(IRenderer<T> renderer, String field) {
		super();
		this.renderer = renderer;
		this.field = field;
	}

	public IRenderer<T> getRenderer() {
		return renderer;
	}

	public String getField() {
		return field;
	}

}
