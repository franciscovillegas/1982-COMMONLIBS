package cl.ejedigital.web.usuario.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class VoTrabajador implements Serializable,Cloneable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	/**
	 * 
	 */
	private final int			rut;
	private final String		digito_ver;
	private final String		nombre;
	private final String		cargo;
	private final int			dependencia;
	private final int			ccosto;
	private final BigDecimal	sueldo;
	private final Timestamp		fecha_nacim;
	private final Timestamp		fecha_ingreso;
	private final String		estado_civil;
	private final String		domicilio;
	private final String		comuna;
	private final String		ciudad;
	private final String		telefono;
	private final String		celular;
	private final String		anexo;
	private final String		sindicato;
	private final String		afp;
	private final Timestamp		fec_afi_sist;
	private final Timestamp		fec_ing_afp;
	private final double		cot_afp;
	private final String		mo_cot_afp;
	private final double		cot_adic;
	private final String		mo_cot_adic;
	private final int			ah_volunt;
	private final String		mo_ah_volunt;
	private final String		jubilado;
	private final int			dep_conven;
	private final String		mon_dep_conven;
	private final String		afp_histo;
	private final String		isapre;
	private final Timestamp		fec_ing_isap;
	private final String		plan_salud;
	private final Timestamp		fec_con_salud;
	private final Timestamp		venc_salud;
	private final String		mon_salud;
	private final double		cot_salud;
	private final String		mon_adic_salud;
	private final BigDecimal	adic_salud;
	private final String		isap_histo;
	private final String		cambio_plan;
	private final String		segsalcom;
	private final String		codigo;
	private final BigDecimal	sobretiempo;
	private final int			monto_sobretiempo;
	private final String		tip_reg_asistencia;
	private final int			horas_jornormal;
	private final BigDecimal	tot_imponible;
	private final BigDecimal	tot_haberes;
	private final String		ape_paterno;
	private final String		ape_materno;
	private final String		nombres;
	private final String		e_mail;
	private final int			zona;
	private final int			region;
	private final int			holding;
	private final String		empresa;
	private final String		division;
	private final int			unidad_negocio;
	private final int			sucursal;
	private final int			directorio;
	private final int			gerencia;
	private final int			subgerencia;
	private final int			departamento;
	private final int			seccion;
	private final int			edad;
	private final Timestamp		fecha_cargo;
	private final int			antig_empresa;
	private final int			antig_cargo;
	private final String		pais;
	private final String		usuario_nt;
	private final String		usuario_notes;
	private final int			renta_reg_mensual;
	private final int			bruto_promedio;
	private final double		punto_medio_escala;
	private final int			bruto_regular;
	private final int			neto_regular;
	private final int			neto_promedio;
	private final int			bruto_total;
	private final int			neto_total;
	private final Timestamp		fecha_ccosto;
	private final int			sociedad;
	private final int			corporacion;
	private final Timestamp		fecha_corporacion;
	private final BigDecimal	antig_ccosto;
	private final BigDecimal	pre;
	private final int			bruto_zona;
	private final int			neto_zona;
	private final String		fam_cargo;
	private final String		nivel;
	private final String		sexo;
	private final String		grupo_sangre;
	private final int			num_matrimonios;
	private final int			talla_vestuario;
	private final int			num_cargas;
	private final String		sit_contractual;
	private final int			tip_contrato;
	private final Timestamp		fec_ter_cont;
	private final String		tip_jor_lab;
	private final String		mot_ingreso;
	private final String		ubic_fisica;
	private final String		categ_cargo;
	private final Timestamp		fec_ing_cargo;
	private final String		area;
	private final Timestamp		fec_ing_hold;
	private final int			ant_hold;
	private final Timestamp		fec_ing_cco;
	private final int			rut_supdirecto;
	private final String		dig_supdirecto;
	private final String		nom_supdirecto;
	private final String		cargo_supdirecto;
	private final String		conv_laboral;
	private final String		poder;
	private final String		tipo_poder;
	private final Timestamp		fec_otorga_poder;
	private final String		sit_doc_cont;
	private final Timestamp		fec_ult_cont;
	private final int			anos_rec_otros;
	private final Timestamp		fec_rec_otros;
	private final int			dias_trab;
	private final BigDecimal	tot_descuento;
	private final BigDecimal	liquido;
	private final BigDecimal	rango_desde;
	private final BigDecimal	rango_hasta;
	private final String		rango_concepto;
	private final String		rango_situacion;
	private final String		forma_pago;
	private final Timestamp		fec_retiro;
	private final int			mot_retiro;
	private final BigDecimal	costo_contrato;
	private final String		unidad;
	private final Timestamp		fec_unidad;
	private final String		especialidad;
	private final int			otros_haberes;
	private final int			num_certifSII;
	private final int			costo_finiquito;
	private final String		cod_postal;
	private final int			pen_normal;
	private final int			pen_progresivo;
	private final int			pen_convenio;
	private final int			total_pendiente;
	private final String		cta_cte;
	private final String		banco;
	private final String		rel_laboral;
	private final String		mail;
	private final double		horas_trab;
	private final String		signo;
	private final int			bono_gestion;
	private final int			movilizacion;
	private final int			bono_zona;
	private final int			asig_caja;
	private final int			bono_antig;
	private final int			rta_variable;
	private final int			traslado;
	private final int			bono_anual;
	private final int			id_centro_trabajo;
	private final Timestamp		fec_centro_trabajo;
	private final Timestamp		fec_ing_sindicato;
	private final Timestamp		fec_ret_sindicato;
	private final int			condicion;
	private final String		shortname;
	private final int			periodo;
	private final int			wp_cod_empresa;
	private final int			wp_cod_planta;
	private final int			wp_cod_sucursal;
	private final int			wp_num_cargas_normale;
	private final BigDecimal	sueldo_base_mensual;
	private final String		moneda_sueldo_base_mensual;
	private final String		aplica_seguro_des;
	private final Timestamp		fec_ini_seguro_des;
	private final String		desc_tip_contrato;
	private final int			cod_lug_prestacion;
	private final int			wp_num_cargas_duplo;
	private final int			wp_num_cargas_materna;
	private final String		valor_adic_1;
	private final String		valor_adic_2;
	private final String		cod_tipo_trabajado;

	public VoTrabajador() {
		rut = -1;
		digito_ver = null;
		nombre = null;
		cargo = null;
		dependencia = 0;
		ccosto = 0;
		sueldo = null;
		fecha_nacim = null;
		fecha_ingreso = null;
		estado_civil = null;
		domicilio = null;
		comuna = null;
		ciudad = null;
		telefono = null;
		celular = null;
		anexo = null;
		sindicato = null;
		afp = null;
		fec_afi_sist = null;
		fec_ing_afp = null;
		cot_afp = 0;
		mo_cot_afp = null;
		cot_adic = 0;
		mo_cot_adic = null;
		ah_volunt = 0;
		mo_ah_volunt = null;
		jubilado = null;
		dep_conven = 0;
		mon_dep_conven = null;
		afp_histo = null;
		isapre = null;
		fec_ing_isap = null;
		plan_salud = null;
		fec_con_salud = null;
		venc_salud = null;
		mon_salud = null;
		cot_salud = 0;
		mon_adic_salud = null;
		adic_salud = null;
		isap_histo = null;
		cambio_plan = null;
		segsalcom = null;
		codigo = null;
		sobretiempo = null;
		monto_sobretiempo = 0;
		tip_reg_asistencia = null;
		horas_jornormal = 0;
		tot_imponible = null;
		tot_haberes = null;
		ape_paterno = null;
		ape_materno = null;
		nombres = null;
		e_mail = null;
		zona = 0;
		region = 0;
		holding = 0;
		empresa = null;
		division = null;
		unidad_negocio = 0;
		sucursal = 0;
		directorio = 0;
		gerencia = 0;
		subgerencia = 0;
		departamento = 0;
		seccion = 0;
		edad = 0;
		fecha_cargo = null;
		antig_empresa = 0;
		antig_cargo = 0;
		pais = null;
		usuario_nt = null;
		usuario_notes = null;
		renta_reg_mensual = 0;
		bruto_promedio = 0;
		punto_medio_escala = 0;
		bruto_regular = 0;
		neto_regular = 0;
		neto_promedio = 0;
		bruto_total = 0;
		neto_total = 0;
		fecha_ccosto = null;
		sociedad = 0;
		corporacion = 0;
		fecha_corporacion = null;
		antig_ccosto = null;
		pre = null;
		bruto_zona = 0;
		neto_zona = 0;
		fam_cargo = null;
		nivel = null;
		sexo = null;
		grupo_sangre = null;
		num_matrimonios = 0;
		talla_vestuario = 0;
		num_cargas = 0;
		sit_contractual = null;
		tip_contrato = 0;
		fec_ter_cont = null;
		tip_jor_lab = null;
		mot_ingreso = null;
		ubic_fisica = null;
		categ_cargo = null;
		fec_ing_cargo = null;
		area = null;
		fec_ing_hold = null;
		ant_hold = 0;
		fec_ing_cco = null;
		rut_supdirecto = 0;
		dig_supdirecto = null;
		nom_supdirecto = null;
		cargo_supdirecto = null;
		conv_laboral = null;
		poder = null;
		tipo_poder = null;
		fec_otorga_poder = null;
		sit_doc_cont = null;
		fec_ult_cont = null;
		anos_rec_otros = 0;
		fec_rec_otros = null;
		dias_trab = 0;
		tot_descuento = null;
		liquido = null;
		rango_desde = null;
		rango_hasta = null;
		rango_concepto = null;
		rango_situacion = null;
		forma_pago = null;
		fec_retiro = null;
		mot_retiro = 0;
		costo_contrato = null;
		unidad = null;
		fec_unidad = null;
		especialidad = null;
		otros_haberes = 0;
		num_certifSII = 0;
		costo_finiquito = 0;
		cod_postal = null;
		pen_normal = 0;
		pen_progresivo = 0;
		pen_convenio = 0;
		total_pendiente = 0;
		cta_cte = null;
		banco = null;
		rel_laboral = null;
		mail = null;
		horas_trab = 0;
		signo = null;
		bono_gestion = 0;
		movilizacion = 0;
		bono_zona = 0;
		asig_caja = 0;
		bono_antig = 0;
		rta_variable = 0;
		traslado = 0;
		bono_anual = 0;
		id_centro_trabajo = 0;
		fec_centro_trabajo = null;
		fec_ing_sindicato = null;
		fec_ret_sindicato = null;
		condicion = 0;
		shortname = null;
		periodo = 0;
		wp_cod_empresa = 0;
		wp_cod_planta = 0;
		wp_cod_sucursal = 0;
		wp_num_cargas_normale = 0;
		sueldo_base_mensual = null;
		moneda_sueldo_base_mensual = null;
		aplica_seguro_des = null;
		fec_ini_seguro_des = null;
		desc_tip_contrato = null;
		cod_lug_prestacion = 0;
		wp_num_cargas_duplo = 0;
		wp_num_cargas_materna = 0;
		valor_adic_1 = null;
		valor_adic_2 = null;
		cod_tipo_trabajado = null;
	}

	public VoTrabajador(int rut, String digito_ver, String nombre, String cargo, int dependencia, int ccosto,
						BigDecimal sueldo, Timestamp fecha_nacim, Timestamp fecha_ingreso, String estado_civil,
						String domicilio, String comuna, String ciudad, String telefono, String celular, String anexo,
						String sindicato, String afp, Timestamp fec_afi_sist, Timestamp fec_ing_afp, double cot_afp,
						String mo_cot_afp, double cot_adic, String mo_cot_adic, int ah_volunt, String mo_ah_volunt,
						String jubilado, int dep_conven, String mon_dep_conven, String afp_histo, String isapre,
						Timestamp fec_ing_isap, String plan_salud, Timestamp fec_con_salud, Timestamp venc_salud,
						String mon_salud, double cot_salud, String mon_adic_salud, BigDecimal adic_salud,
						String isap_histo, String cambio_plan, String segsalcom, String codigo, BigDecimal sobretiempo,
						int monto_sobretiempo, String tip_reg_asistencia, int horas_jornormal,
						BigDecimal tot_imponible, BigDecimal tot_haberes, String ape_paterno, String ape_materno,
						String nombres, String e_mail, int zona, int region, int holding, String empresa,
						String division, int unidad_negocio, int sucursal, int directorio, int gerencia,
						int subgerencia, int departamento, int seccion, int edad, Timestamp fecha_cargo,
						int antig_empresa, int antig_cargo, String pais, String usuario_nt, String usuario_notes,
						int renta_reg_mensual, int bruto_promedio, double punto_medio_escala, int bruto_regular,
						int neto_regular, int neto_promedio, int bruto_total, int neto_total, Timestamp fecha_ccosto,
						int sociedad, int corporacion, Timestamp fecha_corporacion, BigDecimal antig_ccosto,
						BigDecimal pre, int bruto_zona, int neto_zona, String fam_cargo, String nivel, String sexo,
						String grupo_sangre, int num_matrimonios, int talla_vestuario, int num_cargas,
						String sit_contractual, int tip_contrato, Timestamp fec_ter_cont, String tip_jor_lab,
						String mot_ingreso, String ubic_fisica, String categ_cargo, Timestamp fec_ing_cargo,
						String area, Timestamp fec_ing_hold, int ant_hold, Timestamp fec_ing_cco, int rut_supdirecto,
						String dig_supdirecto, String nom_supdirecto, String cargo_supdirecto, String conv_laboral,
						String poder, String tipo_poder, Timestamp fec_otorga_poder, String sit_doc_cont,
						Timestamp fec_ult_cont, int anos_rec_otros, Timestamp fec_rec_otros, int dias_trab,
						BigDecimal tot_descuento, BigDecimal liquido, BigDecimal rango_desde, BigDecimal rango_hasta,
						String rango_concepto, String rango_situacion, String forma_pago, Timestamp fec_retiro,
						int mot_retiro, BigDecimal costo_contrato, String unidad, Timestamp fec_unidad,
						String especialidad, int otros_haberes, int num_certifSII, int costo_finiquito,
						String cod_postal, int pen_normal, int pen_progresivo, int pen_convenio, int total_pendiente,
						String cta_cte, String banco, String rel_laboral, String mail, double horas_trab, String signo,
						int bono_gestion, int movilizacion, int bono_zona, int asig_caja, int bono_antig,
						int rta_variable, int traslado, int bono_anual, int id_centro_trabajo,
						Timestamp fec_centro_trabajo, Timestamp fec_ing_sindicato, Timestamp fec_ret_sindicato,
						int condicion, String shortname, int periodo, int wp_cod_empresa, int wp_cod_planta,
						int wp_cod_sucursal, int wp_num_cargas_normale, BigDecimal sueldo_base_mensual,
						String moneda_sueldo_base_mensual, String aplica_seguro_des, Timestamp fec_ini_seguro_des,
						String desc_tip_contrato, int cod_lug_prestacion, int wp_num_cargas_duplo,
						int wp_num_cargas_materna, String valor_adic_1, String valor_adic_2, String cod_tipo_trabajado) {
		super();
		this.rut = rut;
		this.digito_ver = digito_ver;
		this.nombre = nombre;
		this.cargo = cargo;
		this.dependencia = dependencia;
		this.ccosto = ccosto;
		this.sueldo = sueldo;
		this.fecha_nacim = fecha_nacim;
		this.fecha_ingreso = fecha_ingreso;
		this.estado_civil = estado_civil;
		this.domicilio = domicilio;
		this.comuna = comuna;
		this.ciudad = ciudad;
		this.telefono = telefono;
		this.celular = celular;
		this.anexo = anexo;
		this.sindicato = sindicato;
		this.afp = afp;
		this.fec_afi_sist = fec_afi_sist;
		this.fec_ing_afp = fec_ing_afp;
		this.cot_afp = cot_afp;
		this.mo_cot_afp = mo_cot_afp;
		this.cot_adic = cot_adic;
		this.mo_cot_adic = mo_cot_adic;
		this.ah_volunt = ah_volunt;
		this.mo_ah_volunt = mo_ah_volunt;
		this.jubilado = jubilado;
		this.dep_conven = dep_conven;
		this.mon_dep_conven = mon_dep_conven;
		this.afp_histo = afp_histo;
		this.isapre = isapre;
		this.fec_ing_isap = fec_ing_isap;
		this.plan_salud = plan_salud;
		this.fec_con_salud = fec_con_salud;
		this.venc_salud = venc_salud;
		this.mon_salud = mon_salud;
		this.cot_salud = cot_salud;
		this.mon_adic_salud = mon_adic_salud;
		this.adic_salud = adic_salud;
		this.isap_histo = isap_histo;
		this.cambio_plan = cambio_plan;
		this.segsalcom = segsalcom;
		this.codigo = codigo;
		this.sobretiempo = sobretiempo;
		this.monto_sobretiempo = monto_sobretiempo;
		this.tip_reg_asistencia = tip_reg_asistencia;
		this.horas_jornormal = horas_jornormal;
		this.tot_imponible = tot_imponible;
		this.tot_haberes = tot_haberes;
		this.ape_paterno = ape_paterno;
		this.ape_materno = ape_materno;
		this.nombres = nombres;
		this.e_mail = e_mail;
		this.zona = zona;
		this.region = region;
		this.holding = holding;
		this.empresa = empresa;
		this.division = division;
		this.unidad_negocio = unidad_negocio;
		this.sucursal = sucursal;
		this.directorio = directorio;
		this.gerencia = gerencia;
		this.subgerencia = subgerencia;
		this.departamento = departamento;
		this.seccion = seccion;
		this.edad = edad;
		this.fecha_cargo = fecha_cargo;
		this.antig_empresa = antig_empresa;
		this.antig_cargo = antig_cargo;
		this.pais = pais;
		this.usuario_nt = usuario_nt;
		this.usuario_notes = usuario_notes;
		this.renta_reg_mensual = renta_reg_mensual;
		this.bruto_promedio = bruto_promedio;
		this.punto_medio_escala = punto_medio_escala;
		this.bruto_regular = bruto_regular;
		this.neto_regular = neto_regular;
		this.neto_promedio = neto_promedio;
		this.bruto_total = bruto_total;
		this.neto_total = neto_total;
		this.fecha_ccosto = fecha_ccosto;
		this.sociedad = sociedad;
		this.corporacion = corporacion;
		this.fecha_corporacion = fecha_corporacion;
		this.antig_ccosto = antig_ccosto;
		this.pre = pre;
		this.bruto_zona = bruto_zona;
		this.neto_zona = neto_zona;
		this.fam_cargo = fam_cargo;
		this.nivel = nivel;
		this.sexo = sexo;
		this.grupo_sangre = grupo_sangre;
		this.num_matrimonios = num_matrimonios;
		this.talla_vestuario = talla_vestuario;
		this.num_cargas = num_cargas;
		this.sit_contractual = sit_contractual;
		this.tip_contrato = tip_contrato;
		this.fec_ter_cont = fec_ter_cont;
		this.tip_jor_lab = tip_jor_lab;
		this.mot_ingreso = mot_ingreso;
		this.ubic_fisica = ubic_fisica;
		this.categ_cargo = categ_cargo;
		this.fec_ing_cargo = fec_ing_cargo;
		this.area = area;
		this.fec_ing_hold = fec_ing_hold;
		this.ant_hold = ant_hold;
		this.fec_ing_cco = fec_ing_cco;
		this.rut_supdirecto = rut_supdirecto;
		this.dig_supdirecto = dig_supdirecto;
		this.nom_supdirecto = nom_supdirecto;
		this.cargo_supdirecto = cargo_supdirecto;
		this.conv_laboral = conv_laboral;
		this.poder = poder;
		this.tipo_poder = tipo_poder;
		this.fec_otorga_poder = fec_otorga_poder;
		this.sit_doc_cont = sit_doc_cont;
		this.fec_ult_cont = fec_ult_cont;
		this.anos_rec_otros = anos_rec_otros;
		this.fec_rec_otros = fec_rec_otros;
		this.dias_trab = dias_trab;
		this.tot_descuento = tot_descuento;
		this.liquido = liquido;
		this.rango_desde = rango_desde;
		this.rango_hasta = rango_hasta;
		this.rango_concepto = rango_concepto;
		this.rango_situacion = rango_situacion;
		this.forma_pago = forma_pago;
		this.fec_retiro = fec_retiro;
		this.mot_retiro = mot_retiro;
		this.costo_contrato = costo_contrato;
		this.unidad = unidad;
		this.fec_unidad = fec_unidad;
		this.especialidad = especialidad;
		this.otros_haberes = otros_haberes;
		this.num_certifSII = num_certifSII;
		this.costo_finiquito = costo_finiquito;
		this.cod_postal = cod_postal;
		this.pen_normal = pen_normal;
		this.pen_progresivo = pen_progresivo;
		this.pen_convenio = pen_convenio;
		this.total_pendiente = total_pendiente;
		this.cta_cte = cta_cte;
		this.banco = banco;
		this.rel_laboral = rel_laboral;
		this.mail = mail;
		this.horas_trab = horas_trab;
		this.signo = signo;
		this.bono_gestion = bono_gestion;
		this.movilizacion = movilizacion;
		this.bono_zona = bono_zona;
		this.asig_caja = asig_caja;
		this.bono_antig = bono_antig;
		this.rta_variable = rta_variable;
		this.traslado = traslado;
		this.bono_anual = bono_anual;
		this.id_centro_trabajo = id_centro_trabajo;
		this.fec_centro_trabajo = fec_centro_trabajo;
		this.fec_ing_sindicato = fec_ing_sindicato;
		this.fec_ret_sindicato = fec_ret_sindicato;
		this.condicion = condicion;
		this.shortname = shortname;
		this.periodo = periodo;
		this.wp_cod_empresa = wp_cod_empresa;
		this.wp_cod_planta = wp_cod_planta;
		this.wp_cod_sucursal = wp_cod_sucursal;
		this.wp_num_cargas_normale = wp_num_cargas_normale;
		this.sueldo_base_mensual = sueldo_base_mensual;
		this.moneda_sueldo_base_mensual = moneda_sueldo_base_mensual;
		this.aplica_seguro_des = aplica_seguro_des;
		this.fec_ini_seguro_des = fec_ini_seguro_des;
		this.desc_tip_contrato = desc_tip_contrato;
		this.cod_lug_prestacion = cod_lug_prestacion;
		this.wp_num_cargas_duplo = wp_num_cargas_duplo;
		this.wp_num_cargas_materna = wp_num_cargas_materna;
		this.valor_adic_1 = valor_adic_1;
		this.valor_adic_2 = valor_adic_2;
		this.cod_tipo_trabajado = cod_tipo_trabajado;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getRut() {
		return rut;
	}

	public String getDigito_ver() {
		return digito_ver;
	}

	public String getNombre() {
		return nombre;
	}

	public String getCargo() {
		return cargo;
	}

	public int getDependencia() {
		return dependencia;
	}

	public int getCcosto() {
		return ccosto;
	}

	public BigDecimal getSueldo() {
		return sueldo;
	}

	public Timestamp getFecha_nacim() {
		return fecha_nacim;
	}

	public Timestamp getFecha_ingreso() {
		return fecha_ingreso;
	}

	public String getEstado_civil() {
		return estado_civil;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public String getComuna() {
		return comuna;
	}

	public String getCiudad() {
		return ciudad;
	}

	public String getTelefono() {
		return telefono;
	}

	public String getCelular() {
		return celular;
	}

	public String getAnexo() {
		return anexo;
	}

	public String getSindicato() {
		return sindicato;
	}

	public String getAfp() {
		return afp;
	}

	public Timestamp getFec_afi_sist() {
		return fec_afi_sist;
	}

	public Timestamp getFec_ing_afp() {
		return fec_ing_afp;
	}

	public double getCot_afp() {
		return cot_afp;
	}

	public String getMo_cot_afp() {
		return mo_cot_afp;
	}

	public double getCot_adic() {
		return cot_adic;
	}

	public String getMo_cot_adic() {
		return mo_cot_adic;
	}

	public int getAh_volunt() {
		return ah_volunt;
	}

	public String getMo_ah_volunt() {
		return mo_ah_volunt;
	}

	public String getJubilado() {
		return jubilado;
	}

	public int getDep_conven() {
		return dep_conven;
	}

	public String getMon_dep_conven() {
		return mon_dep_conven;
	}

	public String getAfp_histo() {
		return afp_histo;
	}

	public String getIsapre() {
		return isapre;
	}

	public Timestamp getFec_ing_isap() {
		return fec_ing_isap;
	}

	public String getPlan_salud() {
		return plan_salud;
	}

	public Timestamp getFec_con_salud() {
		return fec_con_salud;
	}

	public Timestamp getVenc_salud() {
		return venc_salud;
	}

	public String getMon_salud() {
		return mon_salud;
	}

	public double getCot_salud() {
		return cot_salud;
	}

	public String getMon_adic_salud() {
		return mon_adic_salud;
	}

	public BigDecimal getAdic_salud() {
		return adic_salud;
	}

	public String getIsap_histo() {
		return isap_histo;
	}

	public String getCambio_plan() {
		return cambio_plan;
	}

	public String getSegsalcom() {
		return segsalcom;
	}

	public String getCodigo() {
		return codigo;
	}

	public BigDecimal getSobretiempo() {
		return sobretiempo;
	}

	public int getMonto_sobretiempo() {
		return monto_sobretiempo;
	}

	public String getTip_reg_asistencia() {
		return tip_reg_asistencia;
	}

	public int getHoras_jornormal() {
		return horas_jornormal;
	}

	public BigDecimal getTot_imponible() {
		return tot_imponible;
	}

	public BigDecimal getTot_haberes() {
		return tot_haberes;
	}

	public String getApe_paterno() {
		return ape_paterno;
	}

	public String getApe_materno() {
		return ape_materno;
	}

	public String getNombres() {
		return nombres;
	}

	public String getE_mail() {
		return e_mail;
	}

	public int getZona() {
		return zona;
	}

	public int getRegion() {
		return region;
	}

	public int getHolding() {
		return holding;
	}

	public String getEmpresa() {
		return empresa;
	}

	public String getDivision() {
		return division;
	}

	public int getUnidad_negocio() {
		return unidad_negocio;
	}

	public int getSucursal() {
		return sucursal;
	}

	public int getDirectorio() {
		return directorio;
	}

	public int getGerencia() {
		return gerencia;
	}

	public int getSubgerencia() {
		return subgerencia;
	}

	public int getDepartamento() {
		return departamento;
	}

	public int getSeccion() {
		return seccion;
	}

	public int getEdad() {
		return edad;
	}

	public Timestamp getFecha_cargo() {
		return fecha_cargo;
	}

	public int getAntig_empresa() {
		return antig_empresa;
	}

	public int getAntig_cargo() {
		return antig_cargo;
	}

	public String getPais() {
		return pais;
	}

	public String getUsuario_nt() {
		return usuario_nt;
	}

	public String getUsuario_notes() {
		return usuario_notes;
	}

	public int getRenta_reg_mensual() {
		return renta_reg_mensual;
	}

	public int getBruto_promedio() {
		return bruto_promedio;
	}

	public double getPunto_medio_escala() {
		return punto_medio_escala;
	}

	public int getBruto_regular() {
		return bruto_regular;
	}

	public int getNeto_regular() {
		return neto_regular;
	}

	public int getNeto_promedio() {
		return neto_promedio;
	}

	public int getBruto_total() {
		return bruto_total;
	}

	public int getNeto_total() {
		return neto_total;
	}

	public Timestamp getFecha_ccosto() {
		return fecha_ccosto;
	}

	public int getSociedad() {
		return sociedad;
	}

	public int getCorporacion() {
		return corporacion;
	}

	public Timestamp getFecha_corporacion() {
		return fecha_corporacion;
	}

	public BigDecimal getAntig_ccosto() {
		return antig_ccosto;
	}

	public BigDecimal getPre() {
		return pre;
	}

	public int getBruto_zona() {
		return bruto_zona;
	}

	public int getNeto_zona() {
		return neto_zona;
	}

	public String getFam_cargo() {
		return fam_cargo;
	}

	public String getNivel() {
		return nivel;
	}

	public String getSexo() {
		return sexo;
	}

	public String getGrupo_sangre() {
		return grupo_sangre;
	}

	public int getNum_matrimonios() {
		return num_matrimonios;
	}

	public int getTalla_vestuario() {
		return talla_vestuario;
	}

	public int getNum_cargas() {
		return num_cargas;
	}

	public String getSit_contractual() {
		return sit_contractual;
	}

	public int getTip_contrato() {
		return tip_contrato;
	}

	public Timestamp getFec_ter_cont() {
		return fec_ter_cont;
	}

	public String getTip_jor_lab() {
		return tip_jor_lab;
	}

	public String getMot_ingreso() {
		return mot_ingreso;
	}

	public String getUbic_fisica() {
		return ubic_fisica;
	}

	public String getCateg_cargo() {
		return categ_cargo;
	}

	public Timestamp getFec_ing_cargo() {
		return fec_ing_cargo;
	}

	public String getArea() {
		return area;
	}

	public Timestamp getFec_ing_hold() {
		return fec_ing_hold;
	}

	public int getAnt_hold() {
		return ant_hold;
	}

	public Timestamp getFec_ing_cco() {
		return fec_ing_cco;
	}

	public int getRut_supdirecto() {
		return rut_supdirecto;
	}

	public String getDig_supdirecto() {
		return dig_supdirecto;
	}

	public String getNom_supdirecto() {
		return nom_supdirecto;
	}

	public String getCargo_supdirecto() {
		return cargo_supdirecto;
	}

	public String getConv_laboral() {
		return conv_laboral;
	}

	public String getPoder() {
		return poder;
	}

	public String getTipo_poder() {
		return tipo_poder;
	}

	public Timestamp getFec_otorga_poder() {
		return fec_otorga_poder;
	}

	public String getSit_doc_cont() {
		return sit_doc_cont;
	}

	public Timestamp getFec_ult_cont() {
		return fec_ult_cont;
	}

	public int getAnos_rec_otros() {
		return anos_rec_otros;
	}

	public Timestamp getFec_rec_otros() {
		return fec_rec_otros;
	}

	public int getDias_trab() {
		return dias_trab;
	}

	public BigDecimal getTot_descuento() {
		return tot_descuento;
	}

	public BigDecimal getLiquido() {
		return liquido;
	}

	public BigDecimal getRango_desde() {
		return rango_desde;
	}

	public BigDecimal getRango_hasta() {
		return rango_hasta;
	}

	public String getRango_concepto() {
		return rango_concepto;
	}

	public String getRango_situacion() {
		return rango_situacion;
	}

	public String getForma_pago() {
		return forma_pago;
	}

	public Timestamp getFec_retiro() {
		return fec_retiro;
	}

	public int getMot_retiro() {
		return mot_retiro;
	}

	public BigDecimal getCosto_contrato() {
		return costo_contrato;
	}

	public String getUnidad() {
		return unidad;
	}

	public Timestamp getFec_unidad() {
		return fec_unidad;
	}

	public String getEspecialidad() {
		return especialidad;
	}

	public int getOtros_haberes() {
		return otros_haberes;
	}

	public int getNum_certifSII() {
		return num_certifSII;
	}

	public int getCosto_finiquito() {
		return costo_finiquito;
	}

	public String getCod_postal() {
		return cod_postal;
	}

	public int getPen_normal() {
		return pen_normal;
	}

	public int getPen_progresivo() {
		return pen_progresivo;
	}

	public int getPen_convenio() {
		return pen_convenio;
	}

	public int getTotal_pendiente() {
		return total_pendiente;
	}

	public String getCta_cte() {
		return cta_cte;
	}

	public String getBanco() {
		return banco;
	}

	public String getRel_laboral() {
		return rel_laboral;
	}

	public String getMail() {
		return mail;
	}

	public double getHoras_trab() {
		return horas_trab;
	}

	public String getSigno() {
		return signo;
	}

	public int getBono_gestion() {
		return bono_gestion;
	}

	public int getMovilizacion() {
		return movilizacion;
	}

	public int getBono_zona() {
		return bono_zona;
	}

	public int getAsig_caja() {
		return asig_caja;
	}

	public int getBono_antig() {
		return bono_antig;
	}

	public int getRta_variable() {
		return rta_variable;
	}

	public int getTraslado() {
		return traslado;
	}

	public int getBono_anual() {
		return bono_anual;
	}

	public int getId_centro_trabajo() {
		return id_centro_trabajo;
	}

	public Timestamp getFec_centro_trabajo() {
		return fec_centro_trabajo;
	}

	public Timestamp getFec_ing_sindicato() {
		return fec_ing_sindicato;
	}

	public Timestamp getFec_ret_sindicato() {
		return fec_ret_sindicato;
	}

	public int getCondicion() {
		return condicion;
	}

	public String getShortname() {
		return shortname;
	}

	public int getPeriodo() {
		return periodo;
	}

	public int getWp_cod_empresa() {
		return wp_cod_empresa;
	}

	public int getWp_cod_planta() {
		return wp_cod_planta;
	}

	public int getWp_cod_sucursal() {
		return wp_cod_sucursal;
	}

	public int getWp_num_cargas_normale() {
		return wp_num_cargas_normale;
	}

	public BigDecimal getSueldo_base_mensual() {
		return sueldo_base_mensual;
	}

	public String getMoneda_sueldo_base_mensual() {
		return moneda_sueldo_base_mensual;
	}

	public String getAplica_seguro_des() {
		return aplica_seguro_des;
	}

	public Timestamp getFec_ini_seguro_des() {
		return fec_ini_seguro_des;
	}

	public String getDesc_tip_contrato() {
		return desc_tip_contrato;
	}

	public int getCod_lug_prestacion() {
		return cod_lug_prestacion;
	}

	public int getWp_num_cargas_duplo() {
		return wp_num_cargas_duplo;
	}

	public int getWp_num_cargas_materna() {
		return wp_num_cargas_materna;
	}

	public String getValor_adic_1() {
		return valor_adic_1;
	}

	public String getValor_adic_2() {
		return valor_adic_2;
	}

	public String getCod_tipo_trabajado() {
		return cod_tipo_trabajado;
	}

}
