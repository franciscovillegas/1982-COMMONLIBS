package cl.eje.model.generic.mac;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;

@TableDefinition(jndi = "mac", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_archivo", numerica = true, isForeignKey = false) }, tableName = "eje_wf_data_documentos")
public class Eje_wf_data_documentos {
	private int	id_req;
	private int	secuencia;
	private int	id_archivo;
	private int	id_persona;
	private int	id_rol;
	private int	id_tipodocumento;
	private Date fecha;
	private String nombre;
	private String 	extencion;
	private String 	roles_visibles;
	
	public int getId_req() {
		return id_req;
	}
	public void setId_req(int id_req) {
		this.id_req = id_req;
	}
	public int getSecuencia() {
		return secuencia;
	}
	public void setSecuencia(int secuencia) {
		this.secuencia = secuencia;
	}
	public int getId_archivo() {
		return id_archivo;
	}
	public void setId_archivo(int id_archivo) {
		this.id_archivo = id_archivo;
	}
	public int getId_persona() {
		return id_persona;
	}
	public void setId_persona(int id_persona) {
		this.id_persona = id_persona;
	}
	public int getId_rol() {
		return id_rol;
	}
	public void setId_rol(int id_rol) {
		this.id_rol = id_rol;
	}
	public int getId_tipodocumento() {
		return id_tipodocumento;
	}
	public void setId_tipodocumento(int id_tipodocumento) {
		this.id_tipodocumento = id_tipodocumento;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getExtencion() {
		return extencion;
	}
	public void setExtencion(String extencion) {
		this.extencion = extencion;
	}
	public String getRoles_visibles() {
		return roles_visibles;
	}
	public void setRoles_visibles(String roles_visibles) {
		this.roles_visibles = roles_visibles;
	}
	
	
	
}
