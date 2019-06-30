package cl.eje.model.generic.portal;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_cliente", isForeignKey = false, numerica = true) }, tableName = "eje_service_viewer_clientes")
public class Eje_service_viewer_clientes extends Vo {
	private int id_cliente;
	private String nombre;
	private Date fecha_update;
	private boolean vigente;
	private String table_tracking;

	public String getTable_tracking() {
		return table_tracking;
	}

	public void setTable_tracking(String table_tracking) {
		this.table_tracking = table_tracking;
	}

	public int getId_cliente() {
		return id_cliente;
	}

	public void setId_cliente(int id_cliente) {
		this.id_cliente = id_cliente;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getFecha_update() {
		return fecha_update;
	}

	public void setFecha_update(Date fecha_update) {
		this.fecha_update = fecha_update;
	}

	public boolean isVigente() {
		return vigente;
	}

	public void setVigente(boolean vigente) {
		this.vigente = vigente;
	}

}
