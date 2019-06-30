package cl.eje.model.generic.portal;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", 
pks = { @PrimaryKeyDefinition(autoIncremental = false, field = "rut", numerica = true, isForeignKey = true) }, tableName = "eje_ges_trabajador")
public class Eje_ges_trabajador extends Vo {
	private int rut;
	private String digito_ver;
	private String nombre;
	private String cargo;
	private int dependencia;
	private int ccosto;
	private Double  sueldo;
	private Date  fecha_nacim;
	private Date  fecha_ingreso;
	private String estado_civil;
	private String domicilio;
	private String comuna;
	private String ciudad;
	private String telefono;
	private String celular;
	private String anexo;
	private String sindicato;
	private String afp;
	private Date  fec_afi_sist;
	private Date  fec_ing_afp;
	private Float 	cot_afp;
	private String mo_cot_afp;
	private Float 	cot_adic;
	private String mo_cot_adic;
	private int ah_volunt;
	private String mo_ah_volunt;
	private String jubilado;
	private int dep_conven;
	private String mon_dep_conven;
	private String afp_histo;
	private String isapre;
	private Date  fec_ing_isap;
	private String plan_salud;
	private Date  fec_con_salud;
	private Date  venc_salud;
	private String mon_salud;
	private Float 	cot_salud;
	private String mon_adic_salud;
	private Double  adic_salud;
	private String isap_histo;
	private String cambio_plan;
	private String segsalcom;
	private String codigo;
	private Double  sobretiempo;
	private int monto_sobretiempo;
	private String tip_reg_asistencia;
	private int horas_jornormal;
	private Double  tot_imponible;
	private Double  tot_haberes;
	private String ape_paterno;
	private String ape_materno;
	private String nombres;
	private String e_mail;
	private int zona;
	private int region;
	private int holding;
	private String empresa;
	private String division;
	private int unidad_negocio;
	private int sucursal;
	private int directorio;
	private int gerencia;
	private int subgerencia;
	private int departamento;
	private int seccion;
	private int edad;
	private Date  fecha_cargo;
	private int antig_empresa;
	private int antig_cargo;
	private String pais;
	private String usuario_nt;
	private String usuario_notes;
	private int renta_reg_mensual;
	private int bruto_promedio;
	private Float punto_medio_escala;
	private int bruto_regular;
	private int neto_regular;
	private int neto_promedio;
	private int bruto_total;
	private int neto_total;
	private Date  fecha_ccosto;
	private int sociedad;
	private int corporacion;
	private Date  fecha_corporacion;
	private Double  antig_ccosto;
	private Double  pre;
	private int bruto_zona;
	private int neto_zona;
	private String fam_cargo;
	private String nivel;
	private String sexo;
	private String grupo_sangre;
	private int num_matrimonios;
	private int talla_vestuario;
	private int num_cargas;
	private String sit_contractual;
	private int tip_contrato;
	private Date  fec_ter_cont;
	private String tip_jor_lab;
	private String mot_ingreso;
	private String ubic_fisica;
	private String categ_cargo;
	private Date  fec_ing_cargo;
	private String area;
	private Date  fec_ing_hold;
	private int ant_hold;
	private Date  fec_ing_cco;
	private int rut_supdirecto;
	private String dig_supdirecto;
	private String nom_supdirecto;
	private String cargo_supdirecto;
	private String conv_laboral;
	private String poder;
	private String tipo_poder;
	private Date  fec_otorga_poder;
	private String sit_doc_cont;
	private Date  fec_ult_cont;
	private int anos_rec_otros;
	private Date fec_rec_otros;
	private int dias_trab;
	private Double tot_descuento;
	private Double liquido;
	private Double rango_desde;
	private Double rango_hasta;
	private String rango_concepto;
	private String rango_situacion;
	private String forma_pago;
	private Date fec_retiro;
	private int mot_retiro;
	private Double costo_contrato;
	private String unidad;
	private Date fec_unidad;
	private String especialidad;
	private int otros_haberes;
	private int num_certifsii;
	private int costo_finiquito;
	private String cod_postal;
	private int pen_normal;
	private int pen_progresivo;
	private int pen_convenio;
	private int total_pendiente;
	private String cta_cte;
	private String banco;
	private String rel_laboral;
	private String mail;
	private Float 	horas_trab;
	private String signo;
	private int bono_gestion;
	private int movilizacion;
	private int bono_zona;
	private int asig_caja;
	private int bono_antig;
	private int rta_variable;
	private int traslado;
	private int bono_anual;
	private int id_centro_trabajo;
	private Date fec_centro_trabajo;
	private Date fec_ing_sindicato;
	private Date fec_ret_sindicato;
	private int condicion;
	private String shortname;
	private int periodo;
	private int wp_cod_empresa;
	private int wp_cod_planta;
	private int wp_cod_sucursal;
	private int wp_num_cargas_normale;
	private Double sueldo_base_mensual;
	private String moneda_sueldo_base_mensual;
	private String aplica_seguro_des;
	private Date fec_ini_seguro_des;
	private String desc_tip_contrato;
	private int cod_lug_prestacion;
	private int wp_num_cargas_duplo;
	private int wp_num_cargas_materna;
	private String valor_adic_1;
	private String valor_adic_2;
	private String cod_tipo_trabajado;
	private int cod_lugar_pago;
	private String unid_id;
	private int rol;
	private String adic_area;
	private String adic_tipo_area;
	private String adic_zona;
	private String adic_gerente_regional;
	private String adic_subgerente;
	private String adic_jefe;
	private String adic_modulo_gerencia_pertenece;
	private String adic_sucursal;
	private String adic_oficina;
	private String adic_oficina_direccion;
	private String adic_familia_cargo;
	private String adic_cargo_funcional;
	private String adic_especialidad;
	private String adic_cod_ejecutivo;
	private String adic_gerencia_subgerencia;
	private String adic_division;
	private String adic_area2;
	private String adic_categoria;
	private String adic_talla;
	private String adic_telefono_casa;
	private String adic_escolaridad_cod;
	private String adic_escolaridad_glosa;
	private String provincia;
	private String profesion;
	private String niveleduc;
	public int getRut() {
		return rut;
	}
	public void setRut(int rut) {
		this.rut = rut;
	}
	public String getDigito_ver() {
		return digito_ver;
	}
	public void setDigito_ver(String digito_ver) {
		this.digito_ver = digito_ver;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getCargo() {
		return cargo;
	}
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	public int getDependencia() {
		return dependencia;
	}
	public void setDependencia(int dependencia) {
		this.dependencia = dependencia;
	}
	public int getCcosto() {
		return ccosto;
	}
	public void setCcosto(int ccosto) {
		this.ccosto = ccosto;
	}
	public Double getSueldo() {
		return sueldo;
	}
	public void setSueldo(Double sueldo) {
		this.sueldo = sueldo;
	}
	public Date getFecha_nacim() {
		return fecha_nacim;
	}
	public void setFecha_nacim(Date fecha_nacim) {
		this.fecha_nacim = fecha_nacim;
	}
	public Date getFecha_ingreso() {
		return fecha_ingreso;
	}
	public void setFecha_ingreso(Date fecha_ingreso) {
		this.fecha_ingreso = fecha_ingreso;
	}
	public String getEstado_civil() {
		return estado_civil;
	}
	public void setEstado_civil(String estado_civil) {
		this.estado_civil = estado_civil;
	}
	public String getDomicilio() {
		return domicilio;
	}
	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}
	public String getComuna() {
		return comuna;
	}
	public void setComuna(String comuna) {
		this.comuna = comuna;
	}
	public String getCiudad() {
		return ciudad;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getCelular() {
		return celular;
	}
	public void setCelular(String celular) {
		this.celular = celular;
	}
	public String getAnexo() {
		return anexo;
	}
	public void setAnexo(String anexo) {
		this.anexo = anexo;
	}
	public String getSindicato() {
		return sindicato;
	}
	public void setSindicato(String sindicato) {
		this.sindicato = sindicato;
	}
	public String getAfp() {
		return afp;
	}
	public void setAfp(String afp) {
		this.afp = afp;
	}
	public Date getFec_afi_sist() {
		return fec_afi_sist;
	}
	public void setFec_afi_sist(Date fec_afi_sist) {
		this.fec_afi_sist = fec_afi_sist;
	}
	public Date getFec_ing_afp() {
		return fec_ing_afp;
	}
	public void setFec_ing_afp(Date fec_ing_afp) {
		this.fec_ing_afp = fec_ing_afp;
	}
	public Float getCot_afp() {
		return cot_afp;
	}
	public void setCot_afp(Float cot_afp) {
		this.cot_afp = cot_afp;
	}
	public String getMo_cot_afp() {
		return mo_cot_afp;
	}
	public void setMo_cot_afp(String mo_cot_afp) {
		this.mo_cot_afp = mo_cot_afp;
	}
	public Float getCot_adic() {
		return cot_adic;
	}
	public void setCot_adic(Float cot_adic) {
		this.cot_adic = cot_adic;
	}
	public String getMo_cot_adic() {
		return mo_cot_adic;
	}
	public void setMo_cot_adic(String mo_cot_adic) {
		this.mo_cot_adic = mo_cot_adic;
	}
	public int getAh_volunt() {
		return ah_volunt;
	}
	public void setAh_volunt(int ah_volunt) {
		this.ah_volunt = ah_volunt;
	}
	public String getMo_ah_volunt() {
		return mo_ah_volunt;
	}
	public void setMo_ah_volunt(String mo_ah_volunt) {
		this.mo_ah_volunt = mo_ah_volunt;
	}
	public String getJubilado() {
		return jubilado;
	}
	public void setJubilado(String jubilado) {
		this.jubilado = jubilado;
	}
	public int getDep_conven() {
		return dep_conven;
	}
	public void setDep_conven(int dep_conven) {
		this.dep_conven = dep_conven;
	}
	public String getMon_dep_conven() {
		return mon_dep_conven;
	}
	public void setMon_dep_conven(String mon_dep_conven) {
		this.mon_dep_conven = mon_dep_conven;
	}
	public String getAfp_histo() {
		return afp_histo;
	}
	public void setAfp_histo(String afp_histo) {
		this.afp_histo = afp_histo;
	}
	public String getIsapre() {
		return isapre;
	}
	public void setIsapre(String isapre) {
		this.isapre = isapre;
	}
	public Date getFec_ing_isap() {
		return fec_ing_isap;
	}
	public void setFec_ing_isap(Date fec_ing_isap) {
		this.fec_ing_isap = fec_ing_isap;
	}
	public String getPlan_salud() {
		return plan_salud;
	}
	public void setPlan_salud(String plan_salud) {
		this.plan_salud = plan_salud;
	}
	public Date getFec_con_salud() {
		return fec_con_salud;
	}
	public void setFec_con_salud(Date fec_con_salud) {
		this.fec_con_salud = fec_con_salud;
	}
	public Date getVenc_salud() {
		return venc_salud;
	}
	public void setVenc_salud(Date venc_salud) {
		this.venc_salud = venc_salud;
	}
	public String getMon_salud() {
		return mon_salud;
	}
	public void setMon_salud(String mon_salud) {
		this.mon_salud = mon_salud;
	}
	public Float getCot_salud() {
		return cot_salud;
	}
	public void setCot_salud(Float cot_salud) {
		this.cot_salud = cot_salud;
	}
	public String getMon_adic_salud() {
		return mon_adic_salud;
	}
	public void setMon_adic_salud(String mon_adic_salud) {
		this.mon_adic_salud = mon_adic_salud;
	}
	public Double getAdic_salud() {
		return adic_salud;
	}
	public void setAdic_salud(Double adic_salud) {
		this.adic_salud = adic_salud;
	}
	public String getIsap_histo() {
		return isap_histo;
	}
	public void setIsap_histo(String isap_histo) {
		this.isap_histo = isap_histo;
	}
	public String getCambio_plan() {
		return cambio_plan;
	}
	public void setCambio_plan(String cambio_plan) {
		this.cambio_plan = cambio_plan;
	}
	public String getSegsalcom() {
		return segsalcom;
	}
	public void setSegsalcom(String segsalcom) {
		this.segsalcom = segsalcom;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public Double getSobretiempo() {
		return sobretiempo;
	}
	public void setSobretiempo(Double sobretiempo) {
		this.sobretiempo = sobretiempo;
	}
	public int getMonto_sobretiempo() {
		return monto_sobretiempo;
	}
	public void setMonto_sobretiempo(int monto_sobretiempo) {
		this.monto_sobretiempo = monto_sobretiempo;
	}
	public String getTip_reg_asistencia() {
		return tip_reg_asistencia;
	}
	public void setTip_reg_asistencia(String tip_reg_asistencia) {
		this.tip_reg_asistencia = tip_reg_asistencia;
	}
	public int getHoras_jornormal() {
		return horas_jornormal;
	}
	public void setHoras_jornormal(int horas_jornormal) {
		this.horas_jornormal = horas_jornormal;
	}
	public Double getTot_imponible() {
		return tot_imponible;
	}
	public void setTot_imponible(Double tot_imponible) {
		this.tot_imponible = tot_imponible;
	}
	public Double getTot_haberes() {
		return tot_haberes;
	}
	public void setTot_haberes(Double tot_haberes) {
		this.tot_haberes = tot_haberes;
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
	public String getNombres() {
		return nombres;
	}
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}
	public String getE_mail() {
		return e_mail;
	}
	public void setE_mail(String e_mail) {
		this.e_mail = e_mail;
	}
	public int getZona() {
		return zona;
	}
	public void setZona(int zona) {
		this.zona = zona;
	}
	public int getRegion() {
		return region;
	}
	public void setRegion(int region) {
		this.region = region;
	}
	public int getHolding() {
		return holding;
	}
	public void setHolding(int holding) {
		this.holding = holding;
	}
	public String getEmpresa() {
		return empresa;
	}
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	public int getUnidad_negocio() {
		return unidad_negocio;
	}
	public void setUnidad_negocio(int unidad_negocio) {
		this.unidad_negocio = unidad_negocio;
	}
	public int getSucursal() {
		return sucursal;
	}
	public void setSucursal(int sucursal) {
		this.sucursal = sucursal;
	}
	public int getDirectorio() {
		return directorio;
	}
	public void setDirectorio(int directorio) {
		this.directorio = directorio;
	}
	public int getGerencia() {
		return gerencia;
	}
	public void setGerencia(int gerencia) {
		this.gerencia = gerencia;
	}
	public int getSubgerencia() {
		return subgerencia;
	}
	public void setSubgerencia(int subgerencia) {
		this.subgerencia = subgerencia;
	}
	public int getDepartamento() {
		return departamento;
	}
	public void setDepartamento(int departamento) {
		this.departamento = departamento;
	}
	public int getSeccion() {
		return seccion;
	}
	public void setSeccion(int seccion) {
		this.seccion = seccion;
	}
	public int getEdad() {
		return edad;
	}
	public void setEdad(int edad) {
		this.edad = edad;
	}
	public Date getFecha_cargo() {
		return fecha_cargo;
	}
	public void setFecha_cargo(Date fecha_cargo) {
		this.fecha_cargo = fecha_cargo;
	}
	public int getAntig_empresa() {
		return antig_empresa;
	}
	public void setAntig_empresa(int antig_empresa) {
		this.antig_empresa = antig_empresa;
	}
	public int getAntig_cargo() {
		return antig_cargo;
	}
	public void setAntig_cargo(int antig_cargo) {
		this.antig_cargo = antig_cargo;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public String getUsuario_nt() {
		return usuario_nt;
	}
	public void setUsuario_nt(String usuario_nt) {
		this.usuario_nt = usuario_nt;
	}
	public String getUsuario_notes() {
		return usuario_notes;
	}
	public void setUsuario_notes(String usuario_notes) {
		this.usuario_notes = usuario_notes;
	}
	public int getRenta_reg_mensual() {
		return renta_reg_mensual;
	}
	public void setRenta_reg_mensual(int renta_reg_mensual) {
		this.renta_reg_mensual = renta_reg_mensual;
	}
	public int getBruto_promedio() {
		return bruto_promedio;
	}
	public void setBruto_promedio(int bruto_promedio) {
		this.bruto_promedio = bruto_promedio;
	}
	public Float getPunto_medio_escala() {
		return punto_medio_escala;
	}
	public void setPunto_medio_escala(Float punto_medio_escala) {
		this.punto_medio_escala = punto_medio_escala;
	}
	public int getBruto_regular() {
		return bruto_regular;
	}
	public void setBruto_regular(int bruto_regular) {
		this.bruto_regular = bruto_regular;
	}
	public int getNeto_regular() {
		return neto_regular;
	}
	public void setNeto_regular(int neto_regular) {
		this.neto_regular = neto_regular;
	}
	public int getNeto_promedio() {
		return neto_promedio;
	}
	public void setNeto_promedio(int neto_promedio) {
		this.neto_promedio = neto_promedio;
	}
	public int getBruto_total() {
		return bruto_total;
	}
	public void setBruto_total(int bruto_total) {
		this.bruto_total = bruto_total;
	}
	public int getNeto_total() {
		return neto_total;
	}
	public void setNeto_total(int neto_total) {
		this.neto_total = neto_total;
	}
	public Date getFecha_ccosto() {
		return fecha_ccosto;
	}
	public void setFecha_ccosto(Date fecha_ccosto) {
		this.fecha_ccosto = fecha_ccosto;
	}
	public int getSociedad() {
		return sociedad;
	}
	public void setSociedad(int sociedad) {
		this.sociedad = sociedad;
	}
	public int getCorporacion() {
		return corporacion;
	}
	public void setCorporacion(int corporacion) {
		this.corporacion = corporacion;
	}
	public Date getFecha_corporacion() {
		return fecha_corporacion;
	}
	public void setFecha_corporacion(Date fecha_corporacion) {
		this.fecha_corporacion = fecha_corporacion;
	}
	public Double getAntig_ccosto() {
		return antig_ccosto;
	}
	public void setAntig_ccosto(Double antig_ccosto) {
		this.antig_ccosto = antig_ccosto;
	}
	public Double getPre() {
		return pre;
	}
	public void setPre(Double pre) {
		this.pre = pre;
	}
	public int getBruto_zona() {
		return bruto_zona;
	}
	public void setBruto_zona(int bruto_zona) {
		this.bruto_zona = bruto_zona;
	}
	public int getNeto_zona() {
		return neto_zona;
	}
	public void setNeto_zona(int neto_zona) {
		this.neto_zona = neto_zona;
	}
	public String getFam_cargo() {
		return fam_cargo;
	}
	public void setFam_cargo(String fam_cargo) {
		this.fam_cargo = fam_cargo;
	}
	public String getNivel() {
		return nivel;
	}
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public String getGrupo_sangre() {
		return grupo_sangre;
	}
	public void setGrupo_sangre(String grupo_sangre) {
		this.grupo_sangre = grupo_sangre;
	}
	public int getNum_matrimonios() {
		return num_matrimonios;
	}
	public void setNum_matrimonios(int num_matrimonios) {
		this.num_matrimonios = num_matrimonios;
	}
	public int getTalla_vestuario() {
		return talla_vestuario;
	}
	public void setTalla_vestuario(int talla_vestuario) {
		this.talla_vestuario = talla_vestuario;
	}
	public int getNum_cargas() {
		return num_cargas;
	}
	public void setNum_cargas(int num_cargas) {
		this.num_cargas = num_cargas;
	}
	public String getSit_contractual() {
		return sit_contractual;
	}
	public void setSit_contractual(String sit_contractual) {
		this.sit_contractual = sit_contractual;
	}
	public int getTip_contrato() {
		return tip_contrato;
	}
	public void setTip_contrato(int tip_contrato) {
		this.tip_contrato = tip_contrato;
	}
	public Date getFec_ter_cont() {
		return fec_ter_cont;
	}
	public void setFec_ter_cont(Date fec_ter_cont) {
		this.fec_ter_cont = fec_ter_cont;
	}
	public String getTip_jor_lab() {
		return tip_jor_lab;
	}
	public void setTip_jor_lab(String tip_jor_lab) {
		this.tip_jor_lab = tip_jor_lab;
	}
	public String getMot_ingreso() {
		return mot_ingreso;
	}
	public void setMot_ingreso(String mot_ingreso) {
		this.mot_ingreso = mot_ingreso;
	}
	public String getUbic_fisica() {
		return ubic_fisica;
	}
	public void setUbic_fisica(String ubic_fisica) {
		this.ubic_fisica = ubic_fisica;
	}
	public String getCateg_cargo() {
		return categ_cargo;
	}
	public void setCateg_cargo(String categ_cargo) {
		this.categ_cargo = categ_cargo;
	}
	public Date getFec_ing_cargo() {
		return fec_ing_cargo;
	}
	public void setFec_ing_cargo(Date fec_ing_cargo) {
		this.fec_ing_cargo = fec_ing_cargo;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public Date getFec_ing_hold() {
		return fec_ing_hold;
	}
	public void setFec_ing_hold(Date fec_ing_hold) {
		this.fec_ing_hold = fec_ing_hold;
	}
	public int getAnt_hold() {
		return ant_hold;
	}
	public void setAnt_hold(int ant_hold) {
		this.ant_hold = ant_hold;
	}
	public Date getFec_ing_cco() {
		return fec_ing_cco;
	}
	public void setFec_ing_cco(Date fec_ing_cco) {
		this.fec_ing_cco = fec_ing_cco;
	}
	public int getRut_supdirecto() {
		return rut_supdirecto;
	}
	public void setRut_supdirecto(int rut_supdirecto) {
		this.rut_supdirecto = rut_supdirecto;
	}
	public String getDig_supdirecto() {
		return dig_supdirecto;
	}
	public void setDig_supdirecto(String dig_supdirecto) {
		this.dig_supdirecto = dig_supdirecto;
	}
	public String getNom_supdirecto() {
		return nom_supdirecto;
	}
	public void setNom_supdirecto(String nom_supdirecto) {
		this.nom_supdirecto = nom_supdirecto;
	}
	public String getCargo_supdirecto() {
		return cargo_supdirecto;
	}
	public void setCargo_supdirecto(String cargo_supdirecto) {
		this.cargo_supdirecto = cargo_supdirecto;
	}
	public String getConv_laboral() {
		return conv_laboral;
	}
	public void setConv_laboral(String conv_laboral) {
		this.conv_laboral = conv_laboral;
	}
	public String getPoder() {
		return poder;
	}
	public void setPoder(String poder) {
		this.poder = poder;
	}
	public String getTipo_poder() {
		return tipo_poder;
	}
	public void setTipo_poder(String tipo_poder) {
		this.tipo_poder = tipo_poder;
	}
	public Date getFec_otorga_poder() {
		return fec_otorga_poder;
	}
	public void setFec_otorga_poder(Date fec_otorga_poder) {
		this.fec_otorga_poder = fec_otorga_poder;
	}
	public String getSit_doc_cont() {
		return sit_doc_cont;
	}
	public void setSit_doc_cont(String sit_doc_cont) {
		this.sit_doc_cont = sit_doc_cont;
	}
	public Date getFec_ult_cont() {
		return fec_ult_cont;
	}
	public void setFec_ult_cont(Date fec_ult_cont) {
		this.fec_ult_cont = fec_ult_cont;
	}
	public int getAnos_rec_otros() {
		return anos_rec_otros;
	}
	public void setAnos_rec_otros(int anos_rec_otros) {
		this.anos_rec_otros = anos_rec_otros;
	}
	public Date getFec_rec_otros() {
		return fec_rec_otros;
	}
	public void setFec_rec_otros(Date fec_rec_otros) {
		this.fec_rec_otros = fec_rec_otros;
	}
	public int getDias_trab() {
		return dias_trab;
	}
	public void setDias_trab(int dias_trab) {
		this.dias_trab = dias_trab;
	}
	public Double getTot_descuento() {
		return tot_descuento;
	}
	public void setTot_descuento(Double tot_descuento) {
		this.tot_descuento = tot_descuento;
	}
	public Double getLiquido() {
		return liquido;
	}
	public void setLiquido(Double liquido) {
		this.liquido = liquido;
	}
	public Double getRango_desde() {
		return rango_desde;
	}
	public void setRango_desde(Double rango_desde) {
		this.rango_desde = rango_desde;
	}
	public Double getRango_hasta() {
		return rango_hasta;
	}
	public void setRango_hasta(Double rango_hasta) {
		this.rango_hasta = rango_hasta;
	}
	public String getRango_concepto() {
		return rango_concepto;
	}
	public void setRango_concepto(String rango_concepto) {
		this.rango_concepto = rango_concepto;
	}
	public String getRango_situacion() {
		return rango_situacion;
	}
	public void setRango_situacion(String rango_situacion) {
		this.rango_situacion = rango_situacion;
	}
	public String getForma_pago() {
		return forma_pago;
	}
	public void setForma_pago(String forma_pago) {
		this.forma_pago = forma_pago;
	}
	public Date getFec_retiro() {
		return fec_retiro;
	}
	public void setFec_retiro(Date fec_retiro) {
		this.fec_retiro = fec_retiro;
	}
	public int getMot_retiro() {
		return mot_retiro;
	}
	public void setMot_retiro(int mot_retiro) {
		this.mot_retiro = mot_retiro;
	}
	public Double getCosto_contrato() {
		return costo_contrato;
	}
	public void setCosto_contrato(Double costo_contrato) {
		this.costo_contrato = costo_contrato;
	}
	public String getUnidad() {
		return unidad;
	}
	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}
	public Date getFec_unidad() {
		return fec_unidad;
	}
	public void setFec_unidad(Date fec_unidad) {
		this.fec_unidad = fec_unidad;
	}
	public String getEspecialidad() {
		return especialidad;
	}
	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}
	public int getOtros_haberes() {
		return otros_haberes;
	}
	public void setOtros_haberes(int otros_haberes) {
		this.otros_haberes = otros_haberes;
	}
	public int getNum_certifsii() {
		return num_certifsii;
	}
	public void setNum_certifsii(int num_certifsii) {
		this.num_certifsii = num_certifsii;
	}
	public int getCosto_finiquito() {
		return costo_finiquito;
	}
	public void setCosto_finiquito(int costo_finiquito) {
		this.costo_finiquito = costo_finiquito;
	}
	public String getCod_postal() {
		return cod_postal;
	}
	public void setCod_postal(String cod_postal) {
		this.cod_postal = cod_postal;
	}
	public int getPen_normal() {
		return pen_normal;
	}
	public void setPen_normal(int pen_normal) {
		this.pen_normal = pen_normal;
	}
	public int getPen_progresivo() {
		return pen_progresivo;
	}
	public void setPen_progresivo(int pen_progresivo) {
		this.pen_progresivo = pen_progresivo;
	}
	public int getPen_convenio() {
		return pen_convenio;
	}
	public void setPen_convenio(int pen_convenio) {
		this.pen_convenio = pen_convenio;
	}
	public int getTotal_pendiente() {
		return total_pendiente;
	}
	public void setTotal_pendiente(int total_pendiente) {
		this.total_pendiente = total_pendiente;
	}
	public String getCta_cte() {
		return cta_cte;
	}
	public void setCta_cte(String cta_cte) {
		this.cta_cte = cta_cte;
	}
	public String getBanco() {
		return banco;
	}
	public void setBanco(String banco) {
		this.banco = banco;
	}
	public String getRel_laboral() {
		return rel_laboral;
	}
	public void setRel_laboral(String rel_laboral) {
		this.rel_laboral = rel_laboral;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public Float getHoras_trab() {
		return horas_trab;
	}
	public void setHoras_trab(Float horas_trab) {
		this.horas_trab = horas_trab;
	}
	public String getSigno() {
		return signo;
	}
	public void setSigno(String signo) {
		this.signo = signo;
	}
	public int getBono_gestion() {
		return bono_gestion;
	}
	public void setBono_gestion(int bono_gestion) {
		this.bono_gestion = bono_gestion;
	}
	public int getMovilizacion() {
		return movilizacion;
	}
	public void setMovilizacion(int movilizacion) {
		this.movilizacion = movilizacion;
	}
	public int getBono_zona() {
		return bono_zona;
	}
	public void setBono_zona(int bono_zona) {
		this.bono_zona = bono_zona;
	}
	public int getAsig_caja() {
		return asig_caja;
	}
	public void setAsig_caja(int asig_caja) {
		this.asig_caja = asig_caja;
	}
	public int getBono_antig() {
		return bono_antig;
	}
	public void setBono_antig(int bono_antig) {
		this.bono_antig = bono_antig;
	}
	public int getRta_variable() {
		return rta_variable;
	}
	public void setRta_variable(int rta_variable) {
		this.rta_variable = rta_variable;
	}
	public int getTraslado() {
		return traslado;
	}
	public void setTraslado(int traslado) {
		this.traslado = traslado;
	}
	public int getBono_anual() {
		return bono_anual;
	}
	public void setBono_anual(int bono_anual) {
		this.bono_anual = bono_anual;
	}
	public int getId_centro_trabajo() {
		return id_centro_trabajo;
	}
	public void setId_centro_trabajo(int id_centro_trabajo) {
		this.id_centro_trabajo = id_centro_trabajo;
	}
	public Date getFec_centro_trabajo() {
		return fec_centro_trabajo;
	}
	public void setFec_centro_trabajo(Date fec_centro_trabajo) {
		this.fec_centro_trabajo = fec_centro_trabajo;
	}
	public Date getFec_ing_sindicato() {
		return fec_ing_sindicato;
	}
	public void setFec_ing_sindicato(Date fec_ing_sindicato) {
		this.fec_ing_sindicato = fec_ing_sindicato;
	}
	public Date getFec_ret_sindicato() {
		return fec_ret_sindicato;
	}
	public void setFec_ret_sindicato(Date fec_ret_sindicato) {
		this.fec_ret_sindicato = fec_ret_sindicato;
	}
	public int getCondicion() {
		return condicion;
	}
	public void setCondicion(int condicion) {
		this.condicion = condicion;
	}
	public String getShortname() {
		return shortname;
	}
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
	public int getPeriodo() {
		return periodo;
	}
	public void setPeriodo(int periodo) {
		this.periodo = periodo;
	}
	public int getWp_cod_empresa() {
		return wp_cod_empresa;
	}
	public void setWp_cod_empresa(int wp_cod_empresa) {
		this.wp_cod_empresa = wp_cod_empresa;
	}
	public int getWp_cod_planta() {
		return wp_cod_planta;
	}
	public void setWp_cod_planta(int wp_cod_planta) {
		this.wp_cod_planta = wp_cod_planta;
	}
	public int getWp_cod_sucursal() {
		return wp_cod_sucursal;
	}
	public void setWp_cod_sucursal(int wp_cod_sucursal) {
		this.wp_cod_sucursal = wp_cod_sucursal;
	}
	public int getWp_num_cargas_normale() {
		return wp_num_cargas_normale;
	}
	public void setWp_num_cargas_normale(int wp_num_cargas_normale) {
		this.wp_num_cargas_normale = wp_num_cargas_normale;
	}
	public Double getSueldo_base_mensual() {
		return sueldo_base_mensual;
	}
	public void setSueldo_base_mensual(Double sueldo_base_mensual) {
		this.sueldo_base_mensual = sueldo_base_mensual;
	}
	public String getMoneda_sueldo_base_mensual() {
		return moneda_sueldo_base_mensual;
	}
	public void setMoneda_sueldo_base_mensual(String moneda_sueldo_base_mensual) {
		this.moneda_sueldo_base_mensual = moneda_sueldo_base_mensual;
	}
	public String getAplica_seguro_des() {
		return aplica_seguro_des;
	}
	public void setAplica_seguro_des(String aplica_seguro_des) {
		this.aplica_seguro_des = aplica_seguro_des;
	}
	public Date getFec_ini_seguro_des() {
		return fec_ini_seguro_des;
	}
	public void setFec_ini_seguro_des(Date fec_ini_seguro_des) {
		this.fec_ini_seguro_des = fec_ini_seguro_des;
	}
	public String getDesc_tip_contrato() {
		return desc_tip_contrato;
	}
	public void setDesc_tip_contrato(String desc_tip_contrato) {
		this.desc_tip_contrato = desc_tip_contrato;
	}
	public int getCod_lug_prestacion() {
		return cod_lug_prestacion;
	}
	public void setCod_lug_prestacion(int cod_lug_prestacion) {
		this.cod_lug_prestacion = cod_lug_prestacion;
	}
	public int getWp_num_cargas_duplo() {
		return wp_num_cargas_duplo;
	}
	public void setWp_num_cargas_duplo(int wp_num_cargas_duplo) {
		this.wp_num_cargas_duplo = wp_num_cargas_duplo;
	}
	public int getWp_num_cargas_materna() {
		return wp_num_cargas_materna;
	}
	public void setWp_num_cargas_materna(int wp_num_cargas_materna) {
		this.wp_num_cargas_materna = wp_num_cargas_materna;
	}
	public String getValor_adic_1() {
		return valor_adic_1;
	}
	public void setValor_adic_1(String valor_adic_1) {
		this.valor_adic_1 = valor_adic_1;
	}
	public String getValor_adic_2() {
		return valor_adic_2;
	}
	public void setValor_adic_2(String valor_adic_2) {
		this.valor_adic_2 = valor_adic_2;
	}
	public String getCod_tipo_trabajado() {
		return cod_tipo_trabajado;
	}
	public void setCod_tipo_trabajado(String cod_tipo_trabajado) {
		this.cod_tipo_trabajado = cod_tipo_trabajado;
	}
	public int getCod_lugar_pago() {
		return cod_lugar_pago;
	}
	public void setCod_lugar_pago(int cod_lugar_pago) {
		this.cod_lugar_pago = cod_lugar_pago;
	}
	public String getUnid_id() {
		return unid_id;
	}
	public void setUnid_id(String unid_id) {
		this.unid_id = unid_id;
	}
	public int getRol() {
		return rol;
	}
	public void setRol(int rol) {
		this.rol = rol;
	}
	public String getAdic_area() {
		return adic_area;
	}
	public void setAdic_area(String adic_area) {
		this.adic_area = adic_area;
	}
	public String getAdic_tipo_area() {
		return adic_tipo_area;
	}
	public void setAdic_tipo_area(String adic_tipo_area) {
		this.adic_tipo_area = adic_tipo_area;
	}
	public String getAdic_zona() {
		return adic_zona;
	}
	public void setAdic_zona(String adic_zona) {
		this.adic_zona = adic_zona;
	}
	public String getAdic_gerente_regional() {
		return adic_gerente_regional;
	}
	public void setAdic_gerente_regional(String adic_gerente_regional) {
		this.adic_gerente_regional = adic_gerente_regional;
	}
	public String getAdic_subgerente() {
		return adic_subgerente;
	}
	public void setAdic_subgerente(String adic_subgerente) {
		this.adic_subgerente = adic_subgerente;
	}
	public String getAdic_jefe() {
		return adic_jefe;
	}
	public void setAdic_jefe(String adic_jefe) {
		this.adic_jefe = adic_jefe;
	}
	public String getAdic_modulo_gerencia_pertenece() {
		return adic_modulo_gerencia_pertenece;
	}
	public void setAdic_modulo_gerencia_pertenece(String adic_modulo_gerencia_pertenece) {
		this.adic_modulo_gerencia_pertenece = adic_modulo_gerencia_pertenece;
	}
	public String getAdic_sucursal() {
		return adic_sucursal;
	}
	public void setAdic_sucursal(String adic_sucursal) {
		this.adic_sucursal = adic_sucursal;
	}
	public String getAdic_oficina() {
		return adic_oficina;
	}
	public void setAdic_oficina(String adic_oficina) {
		this.adic_oficina = adic_oficina;
	}
	public String getAdic_oficina_direccion() {
		return adic_oficina_direccion;
	}
	public void setAdic_oficina_direccion(String adic_oficina_direccion) {
		this.adic_oficina_direccion = adic_oficina_direccion;
	}
	public String getAdic_familia_cargo() {
		return adic_familia_cargo;
	}
	public void setAdic_familia_cargo(String adic_familia_cargo) {
		this.adic_familia_cargo = adic_familia_cargo;
	}
	public String getAdic_cargo_funcional() {
		return adic_cargo_funcional;
	}
	public void setAdic_cargo_funcional(String adic_cargo_funcional) {
		this.adic_cargo_funcional = adic_cargo_funcional;
	}
	public String getAdic_especialidad() {
		return adic_especialidad;
	}
	public void setAdic_especialidad(String adic_especialidad) {
		this.adic_especialidad = adic_especialidad;
	}
	public String getAdic_cod_ejecutivo() {
		return adic_cod_ejecutivo;
	}
	public void setAdic_cod_ejecutivo(String adic_cod_ejecutivo) {
		this.adic_cod_ejecutivo = adic_cod_ejecutivo;
	}
	public String getAdic_gerencia_subgerencia() {
		return adic_gerencia_subgerencia;
	}
	public void setAdic_gerencia_subgerencia(String adic_gerencia_subgerencia) {
		this.adic_gerencia_subgerencia = adic_gerencia_subgerencia;
	}
	public String getAdic_division() {
		return adic_division;
	}
	public void setAdic_division(String adic_division) {
		this.adic_division = adic_division;
	}
	public String getAdic_area2() {
		return adic_area2;
	}
	public void setAdic_area2(String adic_area2) {
		this.adic_area2 = adic_area2;
	}
	public String getAdic_categoria() {
		return adic_categoria;
	}
	public void setAdic_categoria(String adic_categoria) {
		this.adic_categoria = adic_categoria;
	}
	public String getAdic_talla() {
		return adic_talla;
	}
	public void setAdic_talla(String adic_talla) {
		this.adic_talla = adic_talla;
	}
	public String getAdic_telefono_casa() {
		return adic_telefono_casa;
	}
	public void setAdic_telefono_casa(String adic_telefono_casa) {
		this.adic_telefono_casa = adic_telefono_casa;
	}
	public String getAdic_escolaridad_cod() {
		return adic_escolaridad_cod;
	}
	public void setAdic_escolaridad_cod(String adic_escolaridad_cod) {
		this.adic_escolaridad_cod = adic_escolaridad_cod;
	}
	public String getAdic_escolaridad_glosa() {
		return adic_escolaridad_glosa;
	}
	public void setAdic_escolaridad_glosa(String adic_escolaridad_glosa) {
		this.adic_escolaridad_glosa = adic_escolaridad_glosa;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getProfesion() {
		return profesion;
	}
	public void setProfesion(String profesion) {
		this.profesion = profesion;
	}
	public String getNiveleduc() {
		return niveleduc;
	}
	public void setNiveleduc(String niveleduc) {
		this.niveleduc = niveleduc;
	}

	
	
}
