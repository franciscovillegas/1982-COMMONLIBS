package intranet.com.eje.qsmcom.estructuras;

import java.util.ArrayList;

public class Trabajador {
	 private int rut;
	 private String dig;
	 private String nombre;
	 private String ape_mat;
	 private String ape_pat;
	 private String mail;
	 private String direccion;
	 private String telefono;
	 private ArrayList roles;
	 private String banco;
	 private String cuenta;
	 private String clave;
	 private String tipoCuenta;
	 	  
	 
	 public Trabajador(String rut, String nombre, String mail, String direccion, String telefono, String banco , String cuenta, String tipoCuenta) throws Exception {

			 this.rut = Integer.parseInt(rut.substring(0, rut.length() - 1));
			 this.dig = rut.substring(rut.length() - 1 , rut.length());
			 this.nombre = nombre;
			 
			 String apellidos = nombre.substring(nombre.lastIndexOf(",")+ 1, nombre.length()).trim() ;
			 
			 if(apellidos.lastIndexOf(" ") != -1 ) {
				 this.ape_pat = apellidos.substring(0 , apellidos.lastIndexOf(" "));
				 this.ape_mat = apellidos.substring(apellidos.lastIndexOf(" ")+1 , apellidos.length() );
			 } else {
				 this.ape_pat = apellidos;
				 this.ape_mat = "";			 
			 }
			 this.banco =banco;
			 this.cuenta = cuenta;
			 this.mail = mail;
			 this.direccion = direccion;
			 this.telefono = telefono;
			 this.tipoCuenta = tipoCuenta;
			 
	 }
	 
	 public Trabajador(String rut, String nombre, String mail, String direccion, String telefono, ArrayList roles) throws Exception {
		 this(rut, nombre , mail, direccion , telefono, "", "", "");
		 this.roles = roles;
	 }
	 
	public Trabajador(String rut, String nombre, String mail) throws Exception {
		this(rut,nombre,mail, "", "","","","");
	}
	 
	public Trabajador(String rut) throws Exception {
		this(rut,"","");
	}

	public int getRut() {
		return rut;
	}
	
	public String getDig() {
		return dig;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public String getMail() {
		return mail;
	}
	
	public String getDireccion() {
		return direccion;
	}
	
	public String getTelefono() {
		return telefono;
	}

	public String getApe_mat() {
		return ape_mat;
	}

	public String getApe_pat() {
		return ape_pat;
	}
	 
	public ArrayList getRoles() {
		return roles;
	}

	public String getBanco() {
		return banco;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getTipoCuenta() {
		return tipoCuenta;
	}

	public void setTipoCuenta(String tipoCuenta) {
		this.tipoCuenta = tipoCuenta;
	}
	
	
	
	
	
	
	
}