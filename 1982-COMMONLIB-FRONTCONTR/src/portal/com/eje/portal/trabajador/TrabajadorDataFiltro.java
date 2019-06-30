package portal.com.eje.portal.trabajador;

import cl.ejedigital.consultor.ISenchaPage;

/**
 * 
 * Muy lenta, ver @see
 * 
 * @deprecated
 * @see portal.com.eje.portal.trabajador.TrabajadorInfoLocator
 * */
public class TrabajadorDataFiltro {
	private boolean camelCaseActivate;
	private boolean dataContacto;
	private boolean dataContactoFono;
	private boolean dataContactoCelular;
	private boolean dataContactoMail;
	private boolean dataContactoAnexo;
	private boolean dataPersonal;
	private boolean dataDomicilio;
	private boolean dataUnidad;
	private boolean dataJefatura;
	private boolean dataEmpresa;
	private boolean dataCargo;
	private boolean dataContratacion;
	private boolean dataRemuneraciones;
	private boolean dataResumen;
	private boolean dataFormaPago;
	private String pathBacks;
	private ISenchaPage page;
	private String word;

	public TrabajadorDataFiltro() {
		dataResumen = true;
		dataContactoFono = true;
		dataContactoCelular = true;
		dataContactoMail = true;
		dataContactoAnexo = true;
	}

	public boolean isDataContactoFono() {
		return dataContactoFono;
	}

	public void setDataContactoFono(boolean dataContactoFono) {
		this.dataContactoFono = dataContactoFono;
	}

	public boolean isDataContactoCelular() {
		return dataContactoCelular;
	}

	public void setDataContactoCelular(boolean dataContactoCelular) {
		this.dataContactoCelular = dataContactoCelular;
	}

	public boolean isDataContactoMail() {
		return dataContactoMail;
	}

	public void setDataContactoMail(boolean dataContactoMail) {
		this.dataContactoMail = dataContactoMail;
	}

	public boolean isDataContactoAnexo() {
		return dataContactoAnexo;
	}

	public void setDataContactoAnexo(boolean dataContactoAnexo) {
		this.dataContactoAnexo = dataContactoAnexo;
	}

	public boolean isDataResumen() {
		return dataResumen;
	}

	public void setDataResumen(boolean dataResumen) {
		this.dataResumen = dataResumen;
	}

	public boolean isDataFormaPago() {
		return dataFormaPago;
	}

	public void setDataFormaPago(boolean dataFormaPago) {
		this.dataFormaPago = dataFormaPago;
	}
	
	public String getWord() {
		return word;
	}

	public void setWordToFiltered(String word) {
		this.word = word;
	}

	public String getPathBacks() {
		return pathBacks;
	}

	public void setPathBacks(String pathBacks) {
		this.pathBacks = pathBacks;
	}

	public boolean isDataContratacion() {
		return dataContratacion;
	}

	public void setDataContratacion(boolean dataContratacion) {
		this.dataContratacion = dataContratacion;
	}

	public boolean isDataCargo() {
		return dataCargo;
	}

	public void setDataCargo(boolean dataCargo) {
		this.dataCargo = dataCargo;
	}

	/**
	 * telefono=-, <br/>
	 * celular=, <br/>
	 * pais=CHI, <br/>
	 * mail=soledadcordero3@gmail.com,<br/>
	 */
	public boolean isDataContacto() {
		return dataContacto;
	}

	public void setDataContacto(boolean dataContacto) {
		this.dataContacto = dataContacto;
	}

	/**
	 * 
	 * domicilio=BOMBERO GARRIDO Nº1160,<br/>
	 * comuna=CURICÓ, <br/>
	 * ciudad=CURICO, <br/>
	 */
	public boolean isDataDomicilio() {
		return dataDomicilio;
	}

	public void setDataDomicilio(boolean dataDomicilio) {
		this.dataDomicilio = dataDomicilio;
	}

	public boolean isCamelCaseActivate() {
		return camelCaseActivate;
	}

	public void setCamelCaseActivate(boolean camelCaseActivate) {
		this.camelCaseActivate = camelCaseActivate;
	}

	public boolean isDataPersonal() {
		return dataPersonal;
	}

	/**
	 * Entregará la siguiente información, los datos es eun ejemplo de como
	 * vendrá el objeto: <br/>
	 * 
	 * rut=4950420,<br/>
	 * digito_ver=9, <br/>
	 * nombre=GLADYS DEL CARMEN CORTES ARENAS,<br/>
	 * nombres=GLADYS DEL CARMEN, <br/>
	 * ape_paterno=CORTES, <br/>
	 * ape_materno=ARENAS, <br/>
	 * fecha_nacim=1943-12-09 00:00:00.767,<br/>
	 * fecha_nacim_120=1943-12-09 00:00:00, <br/>
	 * now_date=20161117
	 * 
	 */
	public void setDataPersonal(boolean dataPersonal) {
		this.dataPersonal = dataPersonal;
	}

	public boolean isDataUnidad() {
		return dataUnidad;
	}

	public void setDataUnidad(boolean dataUnidad) {
		this.dataUnidad = dataUnidad;
	}

	public boolean isDataJefatura() {
		return dataJefatura;
	}

	public void setDataJefatura(boolean dataJefatura) {
		this.dataJefatura = dataJefatura;
	}

	public boolean isDataEmpresa() {
		return dataEmpresa;
	}

	public void setDataEmpresa(boolean dataEmpresa) {
		this.dataEmpresa = dataEmpresa;
	}

	public boolean isDataRemuneraciones() {
		return dataRemuneraciones;
	}

	public void setDataRemuneraciones(boolean dataRemuneraciones) {
		this.dataRemuneraciones = dataRemuneraciones;
	}

	public void setPage(ISenchaPage page) {
		this.page = page;
	}

	public ISenchaPage getPage() {
		return page;
	}

}
