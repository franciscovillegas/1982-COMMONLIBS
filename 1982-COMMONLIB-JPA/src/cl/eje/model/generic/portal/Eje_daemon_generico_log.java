package cl.eje.model.generic.portal;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_log", numerica = true, isForeignKey = false) }, tableName = "eje_daemon_generico_log")
public class Eje_daemon_generico_log extends Vo {
	private int id_log;
	private int id_modulo;
	private Date fecha;
	private String clase;
	private String server;
	private String message;
	private int level_int;
	private int id_daemon;

	public int getId_log() {
		return id_log;
	}

	public void setId_log(int id_log) {
		this.id_log = id_log;
	}

	public int getId_modulo() {
		return id_modulo;
	}

	public void setId_modulo(int id_modulo) {
		this.id_modulo = id_modulo;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getClase() {
		return clase;
	}

	public void setClase(String clase) {
		this.clase = clase;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getLevel_int() {
		return level_int;
	}

	public void setLevel_int(int level_int) {
		this.level_int = level_int;
	}

	public int getId_daemon() {
		return id_daemon;
	}

	public void setId_daemon(int id_daemon) {
		this.id_daemon = id_daemon;
	}

}
