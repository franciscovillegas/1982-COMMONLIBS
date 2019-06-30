package cl.eje.model.generic.mac;

import java.util.Date;

import cl.eje.model.generic.portal.Eje_ges_parametro;
import cl.eje.model.generic.portal.Eje_ges_trabajador;
import portal.com.eje.portal.vo.annotations.ForeignKeyReference;
import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.annotations.TableReference;
import portal.com.eje.portal.vo.annotations.TableReferences;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = { @PrimaryKeyDefinition(autoIncremental = false, field = "id_postulante", numerica = true, isForeignKey = true) }, tableName = "eje_wfgen_postulante")
@TableReferences({
	@TableReference(field="voEstadoCivil"	, voClass=Eje_wfgen_estadocivil.class		, fk=@ForeignKeyReference(fk="id_estadocivil",otherTableField="id_estadocivil")),
	@TableReference(field="voCiudad"	 	, voClass=Eje_wfgen_ciudad.class	 		, fk=@ForeignKeyReference(fk="id_ciudad",otherTableField="id_ciudad")),
	@TableReference(field="voComuna"	 	, voClass=Eje_wfgen_comuna.class	 		, fk=@ForeignKeyReference(fk="id_comuna",otherTableField="id_comuna")),
	@TableReference(field="voFondoPension"	, voClass=Eje_wfgen_fondopension.class		, fk=@ForeignKeyReference(fk="id_fondopension",otherTableField="id_fondopension")),
	@TableReference(field="voFondoPrevision", voClass=Eje_wfgen_fondoprevision.class	, fk=@ForeignKeyReference(fk="id_fondoprevision",otherTableField="id_fondoprevision")),
	@TableReference(field="voFamilia"		, voClass=Eje_ges_trabajador.class	, fk=@ForeignKeyReference(fk="id_familiar",otherTableField="rut")),
	@TableReference(field="voBanco"			, voClass=Eje_wfgen_banco.class				, fk=@ForeignKeyReference(fk="id_banco",otherTableField="id_banco")),
	@TableReference(field="voGenero"		, voClass=Eje_ges_parametro.class			, fk=@ForeignKeyReference(fk="id_genero",otherTableField="cod_param")),
	@TableReference(field="voPais"			, voClass=Eje_wfgen_pais.class				, fk=@ForeignKeyReference(fk="nacionalidad",otherTableField="id_pais")),
	@TableReference(field="voTipoCuenta"	, voClass=Eje_wfgen_banco_tipocuenta.class	, fk=@ForeignKeyReference(fk="ctacorr_tipocuenta",otherTableField="id_tipocuenta")),
	@TableReference(field="voEstadoVisa"	, voClass=Eje_wfgen_estado_visa.class		, fk=@ForeignKeyReference(fk="vigvisa",otherTableField="id_estado_visa"))
})
public class Eje_wfgen_postulante extends Vo {
	private Eje_wfgen_estadocivil voEstadoCivil;
	private Eje_wfgen_ciudad voCiudad;
	private Eje_wfgen_comuna voComuna;
	private Eje_wfgen_fondopension voFondoPension;
	private Eje_wfgen_fondoprevision voFondoPrevision;
	private Eje_ges_trabajador voFamilia;
	private Eje_wfgen_banco voBanco;
	private Eje_ges_parametro voGenero;
	private Eje_wfgen_pais voPais;
	private Eje_wfgen_banco_tipocuenta voTipoCuenta;
	private Eje_wfgen_estado_visa voEstadoVisa;
	
	private Integer id_postulante;
	private String nif;
	private String dv;
	private String nombres;
	private String apellido_paterno;
	private String apellido_materno;
	private Date fecha_nacimiento;
	private Integer  id_estadocivil;
	private String fono;
	private String movil;
	private String direccion;
	private Integer  id_ciudad;
	private Integer  id_comuna;
	private Integer  id_fondopension;
	private Integer  id_fondoprevision;
	private String contacto;
	private String contacto_fono;
	private Integer  id_familiar;
	private Integer  id_banco;
	private Integer  id_cartera;
	private Integer  id_subcartera;
	private Integer  id_genero;
	private Boolean informado_preinscrito;
	private Boolean informado_inscrito;
	private String contacto_relacion;
	private String nacionalidad;
	private int vigvisa;
	private String ctacorr_numero;
	private String ctacorr_tipocuenta;
	
	private boolean tiene_familiar;
	private boolean trabajo_antes;
	
	private boolean requiere_visa;
	private boolean tiene_visa;
	
	private Date fecvencvisa;

	public Eje_wfgen_estadocivil getVoEstadoCivil() {
		return voEstadoCivil;
	}

	public void setVoEstadoCivil(Eje_wfgen_estadocivil voEstadoCivil) {
		this.voEstadoCivil = voEstadoCivil;
	}

	public Eje_wfgen_ciudad getVoCiudad() {
		return voCiudad;
	}

	public void setVoCiudad(Eje_wfgen_ciudad voCiudad) {
		this.voCiudad = voCiudad;
	}

	public Eje_wfgen_comuna getVoComuna() {
		return voComuna;
	}

	public void setVoComuna(Eje_wfgen_comuna voComuna) {
		this.voComuna = voComuna;
	}

	public Eje_wfgen_fondopension getVoFondoPension() {
		return voFondoPension;
	}

	public void setVoFondoPension(Eje_wfgen_fondopension voFondoPension) {
		this.voFondoPension = voFondoPension;
	}

	public Eje_wfgen_fondoprevision getVoFondoPrevision() {
		return voFondoPrevision;
	}

	public void setVoFondoPrevision(Eje_wfgen_fondoprevision voFondoPrevision) {
		this.voFondoPrevision = voFondoPrevision;
	}

	public Eje_ges_trabajador getVoFamilia() {
		return voFamilia;
	}

	public void setVoFamilia(Eje_ges_trabajador voFamilia) {
		this.voFamilia = voFamilia;
	}

	public Eje_wfgen_banco getVoBanco() {
		return voBanco;
	}

	public void setVoBanco(Eje_wfgen_banco voBanco) {
		this.voBanco = voBanco;
	}

	public Eje_ges_parametro getVoGenero() {
		return voGenero;
	}

	public void setVoGenero(Eje_ges_parametro voGenero) {
		this.voGenero = voGenero;
	}

	public Eje_wfgen_pais getVoPais() {
		return voPais;
	}

	public void setVoPais(Eje_wfgen_pais voPais) {
		this.voPais = voPais;
	}

	public Eje_wfgen_banco_tipocuenta getVoTipoCuenta() {
		return voTipoCuenta;
	}

	public void setVoTipoCuenta(Eje_wfgen_banco_tipocuenta voTipoCuenta) {
		this.voTipoCuenta = voTipoCuenta;
	}

	public Integer getId_postulante() {
		return id_postulante;
	}

	public void setId_postulante(Integer id_postulante) {
		this.id_postulante = id_postulante;
	}

	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getDv() {
		return dv;
	}

	public void setDv(String dv) {
		this.dv = dv;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellido_paterno() {
		return apellido_paterno;
	}

	public void setApellido_paterno(String apellido_paterno) {
		this.apellido_paterno = apellido_paterno;
	}

	public String getApellido_materno() {
		return apellido_materno;
	}

	public void setApellido_materno(String apellido_materno) {
		this.apellido_materno = apellido_materno;
	}

	public Date getFecha_nacimiento() {
		return fecha_nacimiento;
	}

	public void setFecha_nacimiento(Date fecha_nacimiento) {
		this.fecha_nacimiento = fecha_nacimiento;
	}

	public Integer getId_estadocivil() {
		return id_estadocivil;
	}

	public void setId_estadocivil(Integer id_estadocivil) {
		this.id_estadocivil = id_estadocivil;
	}

	public String getFono() {
		return fono;
	}

	public void setFono(String fono) {
		this.fono = fono;
	}

	public String getMovil() {
		return movil;
	}

	public void setMovil(String movil) {
		this.movil = movil;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Integer getId_ciudad() {
		return id_ciudad;
	}

	public void setId_ciudad(Integer id_ciudad) {
		this.id_ciudad = id_ciudad;
	}

	public Integer getId_comuna() {
		return id_comuna;
	}

	public void setId_comuna(Integer id_comuna) {
		this.id_comuna = id_comuna;
	}

	public Integer getId_fondopension() {
		return id_fondopension;
	}

	public void setId_fondopension(Integer id_fondopension) {
		this.id_fondopension = id_fondopension;
	}

	public Integer getId_fondoprevision() {
		return id_fondoprevision;
	}

	public void setId_fondoprevision(Integer id_fondoprevision) {
		this.id_fondoprevision = id_fondoprevision;
	}

	public String getContacto() {
		return contacto;
	}

	public void setContacto(String contacto) {
		this.contacto = contacto;
	}

	public String getContacto_fono() {
		return contacto_fono;
	}

	public void setContacto_fono(String contacto_fono) {
		this.contacto_fono = contacto_fono;
	}

	public Integer getId_familiar() {
		return id_familiar;
	}

	public void setId_familiar(Integer id_familiar) {
		this.id_familiar = id_familiar;
	}

	public Integer getId_banco() {
		return id_banco;
	}

	public void setId_banco(Integer id_banco) {
		this.id_banco = id_banco;
	}

	public Integer getId_cartera() {
		return id_cartera;
	}

	public void setId_cartera(Integer id_cartera) {
		this.id_cartera = id_cartera;
	}

	public Integer getId_subcartera() {
		return id_subcartera;
	}

	public void setId_subcartera(Integer id_subcartera) {
		this.id_subcartera = id_subcartera;
	}

	public Integer getId_genero() {
		return id_genero;
	}

	public void setId_genero(Integer id_genero) {
		this.id_genero = id_genero;
	}

	public Boolean getInformado_preinscrito() {
		return informado_preinscrito;
	}

	public void setInformado_preinscrito(Boolean informado_preinscrito) {
		this.informado_preinscrito = informado_preinscrito;
	}

	public Boolean getInformado_inscrito() {
		return informado_inscrito;
	}

	public void setInformado_inscrito(Boolean informado_inscrito) {
		this.informado_inscrito = informado_inscrito;
	}

	public String getContacto_relacion() {
		return contacto_relacion;
	}

	public void setContacto_relacion(String contacto_relacion) {
		this.contacto_relacion = contacto_relacion;
	}

	public String getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public int getVigvisa() {
		return vigvisa;
	}

	public void setVigvisa(int vigvisa) {
		this.vigvisa = vigvisa;
	}

	public String getCtacorr_numero() {
		return ctacorr_numero;
	}

	public void setCtacorr_numero(String ctacorr_numero) {
		this.ctacorr_numero = ctacorr_numero;
	}

	public String getCtacorr_tipocuenta() {
		return ctacorr_tipocuenta;
	}

	public void setCtacorr_tipocuenta(String ctacorr_tipocuenta) {
		this.ctacorr_tipocuenta = ctacorr_tipocuenta;
	}

	public boolean isTiene_familiar() {
		return tiene_familiar;
	}

	public void setTiene_familiar(boolean tiene_familiar) {
		this.tiene_familiar = tiene_familiar;
	}

	public boolean isTrabajo_antes() {
		return trabajo_antes;
	}

	public void setTrabajo_antes(boolean trabajo_antes) {
		this.trabajo_antes = trabajo_antes;
	}

	public boolean isRequiere_visa() {
		return requiere_visa;
	}

	public void setRequiere_visa(boolean requiere_visa) {
		this.requiere_visa = requiere_visa;
	}

	public boolean isTiene_visa() {
		return tiene_visa;
	}

	public void setTiene_visa(boolean tiene_visa) {
		this.tiene_visa = tiene_visa;
	}

	public Date getFecvencvisa() {
		return fecvencvisa;
	}

	public void setFecvencvisa(Date fecvencvisa) {
		this.fecvencvisa = fecvencvisa;
	}

	public Eje_wfgen_estado_visa getVoEstadoVisa() {
		return voEstadoVisa;
	}

	public void setVoEstadoVisa(Eje_wfgen_estado_visa voEstadoVisa) {
		this.voEstadoVisa = voEstadoVisa;
	}

	
}
