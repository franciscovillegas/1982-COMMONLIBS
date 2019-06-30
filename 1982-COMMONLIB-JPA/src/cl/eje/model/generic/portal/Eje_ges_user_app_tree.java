package cl.eje.model.generic.portal;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = false, field = "id_unidad", isForeignKey = true, numerica = true) }, tableName = "eje_ges_user_app_tree")
public class Eje_ges_user_app_tree extends Vo {
	private int id_unidad;
	private String nombre;
	private Integer id_unidad_padre;
	private String icon;

	public int getId_unidad() {
		return id_unidad;
	}

	public void setId_unidad(int id_unidad) {
		this.id_unidad = id_unidad;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getId_unidad_padre() {
		return id_unidad_padre;
	}

	public void setId_unidad_padre(Integer id_unidad_padre) {
		this.id_unidad_padre = id_unidad_padre;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

}
