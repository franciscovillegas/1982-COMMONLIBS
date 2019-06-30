package portal.com.eje.portal.organica.vo;

import portal.com.eje.portal.vo.vo.Vo;

public class VoDimension extends Vo {
	private int dimension_id;
	private String dimension_desc;
	private int dimension_tipo;
	private String dimension_cardinalidad;
	private String campored;

	public int getDimension_tipo() {
		return dimension_tipo;
	}

	public void setDimension_tipo(int dimension_tipo) {
		this.dimension_tipo = dimension_tipo;
	}

	public String getDimension_cardinalidad() {
		return dimension_cardinalidad;
	}

	public void setDimension_cardinalidad(String dimension_cardinalidad) {
		this.dimension_cardinalidad = dimension_cardinalidad;
	}

	public String getCampored() {
		return campored;
	}

	public void setCampored(String campored) {
		this.campored = campored;
	}

	public int getDimension_id() {
		return dimension_id;
	}

	public void setDimension_id(int dimension_id) {
		this.dimension_id = dimension_id;
	}

	public String getDimension_desc() {
		return dimension_desc;
	}

	public void setDimension_desc(String dimension_desc) {
		this.dimension_desc = dimension_desc;
	}

}
