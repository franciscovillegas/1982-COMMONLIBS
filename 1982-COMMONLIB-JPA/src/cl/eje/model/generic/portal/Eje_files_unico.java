package cl.eje.model.generic.portal;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.ForeignKey;
import portal.com.eje.portal.vo.annotations.ForeignKeys;
import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@ForeignKeys(foreignkeys = { @ForeignKey(fk = "rut_subida"), @ForeignKey(fk = "id_tipo") })
@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_file", isForeignKey = false, numerica = true) }, tableName = "eje_files_unico")
public class Eje_files_unico extends Vo {
	private Integer id_file;
	private Date fecha_subida;
	private Integer rut_subida;
	private String name_original;
	private String name_unic;
	double	bytes;
	private String machine_name;
	private String text;
	private Integer id_tipo;
	private String full_hdd_path;
	private String relative_path_to_webcontent;
	boolean isweb;
	
	public Integer getId_file() {
		return id_file;
	}
	public void setId_file(Integer id_file) {
		this.id_file = id_file;
	}
	public Date getFecha_subida() {
		return fecha_subida;
	}
	public void setFecha_subida(Date fecha_subida) {
		this.fecha_subida = fecha_subida;
	}
	public Integer getRut_subida() {
		return rut_subida;
	}
	public void setRut_subida(Integer rut_subida) {
		this.rut_subida = rut_subida;
	}
	public String getName_original() {
		return name_original;
	}
	public void setName_original(String name_original) {
		this.name_original = name_original;
	}
	public String getName_unic() {
		return name_unic;
	}
	public void setName_unic(String name_unic) {
		this.name_unic = name_unic;
	}
	public double getBytes() {
		return bytes;
	}
	public void setBytes(double bytes) {
		this.bytes = bytes;
	}
	public String getMachine_name() {
		return machine_name;
	}
	public void setMachine_name(String machine_name) {
		this.machine_name = machine_name;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Integer getId_tipo() {
		return id_tipo;
	}
	public void setId_tipo(Integer id_tipo) {
		this.id_tipo = id_tipo;
	}
	public String getFull_hdd_path() {
		return full_hdd_path;
	}
	public void setFull_hdd_path(String full_hdd_path) {
		this.full_hdd_path = full_hdd_path;
	}
	public String getRelative_path_to_webcontent() {
		return relative_path_to_webcontent;
	}
	public void setRelative_path_to_webcontent(String relative_path_to_webcontent) {
		this.relative_path_to_webcontent = relative_path_to_webcontent;
	}
	public boolean isIsweb() {
		return isweb;
	}
	public void setIsweb(boolean isweb) {
		this.isweb = isweb;
	}
	
	
	
}
