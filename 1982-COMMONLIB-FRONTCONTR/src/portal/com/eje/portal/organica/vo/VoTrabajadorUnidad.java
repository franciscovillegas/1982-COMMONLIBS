package portal.com.eje.portal.organica.vo;

import java.util.Date;

import portal.com.eje.portal.vo.vo.Vo;

public class VoTrabajadorUnidad extends Vo {
	private int id_persona;
	private int rut;
	private int id_encargado;
	private String nif;
	private String nombre;
	private String nombres;
	private String persona;
	private String ape_paterno;
	private String ape_materno;

	private String email;
	private int id_empresa;
	private String empresa;
	private String id_unidad;
	private String unid_id;
	private String unidad;
	private String unid_desc;
	private String id_cargo;
	private String cargo;
	private String fecha_ingreso;
	private Date fecha_ingreso_date;
	private int wp_cod_planta = 1;
	private int compania;
	private String mail;
	private String e_mail;
	private String telefono;
	private String fec_ter_cont;
	private String encargado;
	private boolean es_encargado;
	private boolean pertenece_a_esta_unidad;

	public int getId_persona() {
		return id_persona;
	}

	public void setId_persona(int id_persona) {
		this.id_persona = id_persona;
	}

	public int getRut() {
		return rut;
	}

	public void setRut(int rut) {
		this.rut = rut;
	}

	public int getId_encargado() {
		return id_encargado;
	}

	public void setId_encargado(int id_encargado) {
		this.id_encargado = id_encargado;
	}

	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getPersona() {
		return persona;
	}

	public void setPersona(String persona) {
		this.persona = persona;
	}

	public String getApe_paterno() {
		return ape_paterno;
	}

	public void setApe_paterno(String ape_paterno) {
		this.ape_paterno = ape_paterno;
	}

	public String getApe_materno() {
		return ape_materno;
	}

	public void setApe_materno(String ape_materno) {
		this.ape_materno = ape_materno;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getId_empresa() {
		return id_empresa;
	}

	public void setId_empresa(int id_empresa) {
		this.id_empresa = id_empresa;
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public String getId_unidad() {
		return id_unidad;
	}

	public void setId_unidad(String id_unidad) {
		this.id_unidad = id_unidad;
	}

	public String getUnid_id() {
		return unid_id;
	}

	public void setUnid_id(String unid_id) {
		this.unid_id = unid_id;
	}

	public String getUnidad() {
		return unidad;
	}

	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}

	public String getUnid_desc() {
		return unid_desc;
	}

	public void setUnid_desc(String unid_desc) {
		this.unid_desc = unid_desc;
	}

	public String getId_cargo() {
		return id_cargo;
	}

	public void setId_cargo(String id_cargo) {
		this.id_cargo = id_cargo;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getFecha_ingreso() {
		return fecha_ingreso;
	}

	public void setFecha_ingreso(String fecha_ingreso) {
		this.fecha_ingreso = fecha_ingreso;
	}

	public Date getFecha_ingreso_date() {
		return fecha_ingreso_date;
	}

	public void setFecha_ingreso_date(Date fecha_ingreso_date) {
		this.fecha_ingreso_date = fecha_ingreso_date;
	}

	public int getWp_cod_planta() {
		return wp_cod_planta;
	}

	public void setWp_cod_planta(int wp_cod_planta) {
		this.wp_cod_planta = wp_cod_planta;
	}

	public int getCompania() {
		return compania;
	}

	public void setCompania(int compania) {
		this.compania = compania;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getE_mail() {
		return e_mail;
	}

	public void setE_mail(String e_mail) {
		this.e_mail = e_mail;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getFec_ter_cont() {
		return fec_ter_cont;
	}

	public void setFec_ter_cont(String fec_ter_cont) {
		this.fec_ter_cont = fec_ter_cont;
	}

	public String getEncargado() {
		return encargado;
	}

	public void setEncargado(String encargado) {
		this.encargado = encargado;
	}

	public boolean isEs_encargado() {
		return es_encargado;
	}

	public void setEs_encargado(boolean es_encargado) {
		this.es_encargado = es_encargado;
	}

	public boolean isPertenece_a_esta_unidad() {
		return pertenece_a_esta_unidad;
	}

	public void setPertenece_a_esta_unidad(boolean pertenece_a_esta_unidad) {
		this.pertenece_a_esta_unidad = pertenece_a_esta_unidad;
	}

}
