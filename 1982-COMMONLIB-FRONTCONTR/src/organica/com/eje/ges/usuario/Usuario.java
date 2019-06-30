package organica.com.eje.ges.usuario;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import organica.arbol.Nodo;
import organica.bci.traspaso.tools.Rut;
import organica.com.eje.ges.usuario.cargo.VerCargo;
import organica.com.eje.ges.usuario.unidad.VerUnidad;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import portal.com.eje.tools.md5.ManagerMD5;

// Referenced classes of package datafolder.usuario:
//            UsuariosConectados, ControlAcceso, ListaPermisos, InfoUsuario
/**
 * Se solo un usuario.
 * 
 * @author Pancho
 * @deprecated
 * @see portal.com.eje.serhumano.user.Usuario
 * **/
public class Usuario implements HttpSessionBindingListener, Serializable {

	public Usuario() {
		mensajeerror = "";
		rutUsuario = "";
		empresa = "";
		userName = "";
		rutConsultado = "";
		division = "";
		cargo = "";
		unidad = "";
		rol = false;
		passw = "";
		getErrores = 0;
		emp_rel = null;
		uni_rel = null;
		EmpUni_Rel = false;
		verMultiplesRamas = false;
		valido = false;
		rutConsultado = null;
		unidadesRel = new VerUnidad[0];
		unidadesVer = new VerUnidad[0];
		cargosVer = new VerCargo[0];
		cant_ingresos = 0;
	}

	public static Usuario rescatarUsuario(HttpServletRequest req) {
		return rescatarUsuario(req.getSession(false));
	}

	public static Usuario rescatarUsuario(HttpSession sesion) {
		Usuario user;
		if (sesion == null) {
			OutMessage.OutMessagePrint("rescatarUsuario --> Session nula");
			user = new Usuario();
		} else {
			try {
				user = (Usuario) sesion.getAttribute(sesion.getId() + "o");
			} catch (Exception e) {
				System.out.println("rescatarUsuario Exception --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
				user = new Usuario();
			}
			if (user == null) {
				OutMessage.OutMessagePrint("rescatarUsuario --> Usuario nulo");
				user = new Usuario();
			}
		}
		OutMessage.OutMessagePrint("rescatarUsuario Usuario --> ".concat(String.valueOf(String.valueOf(user.toString()))));
		return user;
	}

	public Usuario(Connection conn, String user, String pass) {
		mensajeerror = "";
		rutUsuario = "";
		empresa = "";
		userName = "";
		rutConsultado = "";
		division = "";
		cargo = "";
		unidad = "";
		rol = false;
		passw = "";
		getErrores = 0;
		emp_rel = null;
		uni_rel = null;
		EmpUni_Rel = false;
		verMultiplesRamas = false;
		cant_ingresos = 0;
		getDatos(conn, user, pass);
	}

	public boolean getDatos(Connection conn, String rut, String pass) {
		String sql = "";
		String digver = null;
		int emp = 0;
		valido = false;
		if (conn != null) {
			Consulta consul = new Consulta(conn);
			Consulta consul2 = new Consulta(conn);
			Rut xrut = new Rut(rut);
			StringBuilder  str = new StringBuilder();
			str.append(" SELECT DISTINCT "); 
			str.append(" dbo.eje_ges_trabajador.empresa, dbo.eje_ges_trabajador.division, dbo.eje_ges_trabajador.unidad, dbo.eje_ges_trabajador.area, "); 
			str.append(" dbo.eje_ges_trabajador.cargo, dbo.eje_ges_usuario.login_usuario, dbo.eje_ges_usuario.password_usuario, dbo.eje_ges_usuario.rut_usuario, "); 
			str.append(" dbo.eje_ges_trabajador.sindicato, dbo.eje_ges_trabajador.digito_ver, dbo.eje_ges_usuario.emp_rel, dbo.eje_ges_usuario.uni_rel, "); 
			str.append(" dbo.eje_ges_usuario.tipo_rel, dbo.eje_ges_usuario.cant_ingresos, 'df' AS app_id ");
			str.append(" FROM       dbo.eje_ges_usuario INNER JOIN ");
			str.append(" 			dbo.eje_ges_user_app ON ");
			str.append(" 			dbo.eje_ges_usuario.rut_usuario = dbo.eje_ges_user_app.rut_usuario ");
			str.append(" 			LEFT OUTER JOIN ");
			str.append(" dbo.eje_ges_trabajador ON dbo.eje_ges_usuario.rut_usuario = dbo.eje_ges_trabajador.rut ");
			str.append(" WHERE dbo.eje_ges_usuario.rut_usuario =").append(xrut.getRut());

			consul.exec(str.toString());
			if (consul.next()) {
				digver = consul.getString("digito_ver");
				
				if (digver == null ) {
					digver = xrut.getDigVer().toUpperCase();
				}
				if (xrut.getDigVer().toUpperCase().equals(digver.toUpperCase())) {
					
					String strClaveMD5 = ManagerMD5.encrypt(pass);
					String strClavePortal = consul.getString("password_usuario").toUpperCase().trim();
					
					if (pass.toUpperCase().equals(strClavePortal) || strClaveMD5.toUpperCase().equals(strClavePortal)) {
						rutUsuario = consul.getString("rut_usuario").trim();
						userName = consul.getString("login_usuario").trim();
						empresa = consul.getString("empresa");
						cant_ingresos = consul.getInt("cant_ingresos");
						valido = true;
						rutConsultado = rutUsuario;
						division = consul.getString("division");
						// cargo = consul.getString("cargo");
						unidad = consul.getString("unidad");
						permisos = ControlAcceso.cargarListaPermisos(conn,
								rutUsuario);
						passw = pass.toUpperCase();
						String emp_rel = consul.getString("emp_rel");
						String uni_rel = consul.getString("uni_rel");
						if (emp_rel != null && !"".equals(emp_rel)) {
							System.out.println("---------->emp_rel");
							EmpUni_Rel = true;
							if (!uni_rel.equals(unidad) && !emp_rel.equals(empresa)) {
								unidadesRel = new VerUnidad[2];
								unidadesRel[0] = new VerUnidad(emp_rel,uni_rel, consul.getString("tipo_rel"));
								unidadesRel[1] = new VerUnidad(empresa, unidad,"R");
								verMultiplesRamas = true;
							} else {
								unidadesRel = new VerUnidad[1];
								unidadesRel[0] = new VerUnidad(emp_rel,uni_rel, consul.getString("tipo_rel"));
								verMultiplesRamas = false;
							}
							Vector vecPaso = new Vector();
							for (int x = 0; x < unidadesRel.length; x++)
								vecPaso.addElement(unidadesRel[x]);
						} else {
							System.out.println("---------->no emp_rel");
							EmpUni_Rel = false;
							unidadesRel = new VerUnidad[1];
							unidadesRel[0] = new VerUnidad(empresa, unidad, "R");
							verMultiplesRamas = false;
						}
						Vector vecPaso = new Vector();
						//for (int x = 0; x < unidadesRel.length; x++)
						//	vecPaso.addElement(unidadesRel[x]);

						Consulta consul3 = new Consulta(conn);
						sql = String.valueOf(String.valueOf((new StringBuilder("SELECT empresa, unidad, tipo FROM eje_ges_usuario_unidad WHERE (rut_usuario = ")).append(rutUsuario).append(")")));
						consul3.exec(sql);
						for (; consul3.next(); vecPaso.addElement(new VerUnidad(consul3.getString("empresa"), consul3.getString("unidad"), consul3.getString("tipo"))))
							verMultiplesRamas = true;

						consul3.close();
						unidadesVer = new VerUnidad[vecPaso.size()];
						vecPaso.copyInto(unidadesVer);
						vecPaso.removeAllElements();
						Vector vecPaso2 = new Vector();
						consul3 = new Consulta(conn);
						sql = String.valueOf(String.valueOf((new StringBuilder("SELECT empresa, cargo FROM eje_ges_usuario_cargo WHERE (rut_usuario = ")).append(rutUsuario).append(")")));
						consul3.exec(sql);
						for (; consul3.next(); vecPaso2.addElement(new VerCargo(consul3.getString("empresa"), consul3.getString("cargo"))));
						consul3.close();
						cargosVer = new VerCargo[vecPaso2.size()];
						vecPaso2.copyInto(cargosVer);
						vecPaso2.removeAllElements();
						sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_usuario SET error = 0 WHERE login_usuario = '")).append(xrut.getRut()).append("'")));
						if (consul2.insert(sql))
							getErrores = 0;
					} else {
						mensajeerror = "Clave Erronea";
						sql = String.valueOf(String.valueOf((new StringBuilder("SELECT error FROM eje_ges_usuario WHERE login_usuario = '")).append(xrut.getRut()).append("'")));
						consul2.exec(sql);
						if (consul2.next()) {
							sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_usuario SET error = ")).append(consul2.getInt("error") + 1).append(" WHERE login_usuario = '").append(xrut.getRut()).append("'")));
							if (consul.insert(sql))
								getErrores = consul2.getInt("error") + 1;
						}
					}
				} else {
					mensajeerror = "Error, Digito Verificador";
				}
			} else {
				mensajeerror = "Usuario No Valido";
			}
			consul.close();
			consul2.close();
		}
		return esValido();
	}
	
	
	public boolean getDatos(Connection conn, String rut) {
		String sql = "";
		String digver = null;
		int emp = 0;
		String pass = null;
		valido = false;
		if (conn != null) {
			Consulta consul = new Consulta(conn);
			Consulta consul2 = new Consulta(conn);
			Rut xrut = new Rut(rut);
			StringBuilder  str = new StringBuilder();
			str.append(" SELECT DISTINCT "); 
			str.append(" dbo.eje_ges_trabajador.empresa, dbo.eje_ges_trabajador.division, dbo.eje_ges_trabajador.unidad, dbo.eje_ges_trabajador.area, "); 
			str.append(" dbo.eje_ges_trabajador.cargo, dbo.eje_ges_usuario.login_usuario, dbo.eje_ges_usuario.password_usuario, dbo.eje_ges_usuario.rut_usuario, "); 
			str.append(" dbo.eje_ges_trabajador.sindicato, dbo.eje_ges_trabajador.digito_ver, dbo.eje_ges_usuario.emp_rel, dbo.eje_ges_usuario.uni_rel, "); 
			str.append(" dbo.eje_ges_usuario.tipo_rel, dbo.eje_ges_usuario.cant_ingresos, 'df' AS app_id ");
			str.append(" FROM       dbo.eje_ges_usuario INNER JOIN ");
			str.append(" 			dbo.eje_ges_user_app ON ");
			str.append(" 			dbo.eje_ges_usuario.rut_usuario = dbo.eje_ges_user_app.rut_usuario ");
			str.append(" 			LEFT OUTER JOIN ");
			str.append(" dbo.eje_ges_trabajador ON dbo.eje_ges_usuario.rut_usuario = dbo.eje_ges_trabajador.rut ");
			str.append(" WHERE dbo.eje_ges_usuario.rut_usuario =").append(xrut.getRut());

			consul.exec(str.toString());
			if (consul.next()) {
				digver = consul.getString("digito_ver");
				pass=consul.getString("password_usuario");
				rutUsuario = consul.getString("rut_usuario").trim();
				userName = consul.getString("login_usuario").trim();
				empresa = consul.getString("empresa");
				cant_ingresos = consul.getInt("cant_ingresos");
				valido = true;
				rutConsultado = rutUsuario;
				division = consul.getString("division");
						// cargo = consul.getString("cargo");
				unidad = consul.getString("unidad");
				permisos = ControlAcceso.cargarListaPermisos(conn,
								rutUsuario);
				String emp_rel = consul.getString("emp_rel");
				String uni_rel = consul.getString("uni_rel");
						if (emp_rel != null && !"".equals(emp_rel)) {
							System.out.println("---------->emp_rel");
							EmpUni_Rel = true;
							if (!uni_rel.equals(unidad) && !emp_rel.equals(empresa)) {
								unidadesRel = new VerUnidad[2];
								unidadesRel[0] = new VerUnidad(emp_rel,uni_rel, consul.getString("tipo_rel"));
								unidadesRel[1] = new VerUnidad(empresa, unidad,"R");
								verMultiplesRamas = true;
							} else {
								unidadesRel = new VerUnidad[1];
								unidadesRel[0] = new VerUnidad(emp_rel,uni_rel, consul.getString("tipo_rel"));
								verMultiplesRamas = false;
							}
							Vector vecPaso = new Vector();
							for (int x = 0; x < unidadesRel.length; x++)
								vecPaso.addElement(unidadesRel[x]);
						} else {
							System.out.println("---------->no emp_rel");
							EmpUni_Rel = false;
							unidadesRel = new VerUnidad[1];
							unidadesRel[0] = new VerUnidad(empresa, unidad, "R");
							verMultiplesRamas = false;
						}
						Vector vecPaso = new Vector();
						//for (int x = 0; x < unidadesRel.length; x++)
						//	vecPaso.addElement(unidadesRel[x]);

						Consulta consul3 = new Consulta(conn);
						sql = String.valueOf(String.valueOf((new StringBuilder("SELECT empresa, unidad, tipo FROM eje_ges_usuario_unidad WHERE (rut_usuario = ")).append(rutUsuario).append(")")));
						consul3.exec(sql);
						for (; consul3.next(); vecPaso.addElement(new VerUnidad(consul3.getString("empresa"), consul3.getString("unidad"), consul3.getString("tipo"))))
							verMultiplesRamas = true;

						consul3.close();
						unidadesVer = new VerUnidad[vecPaso.size()];
						vecPaso.copyInto(unidadesVer);
						vecPaso.removeAllElements();
						Vector vecPaso2 = new Vector();
						consul3 = new Consulta(conn);
						sql = String.valueOf(String.valueOf((new StringBuilder("SELECT empresa, cargo FROM eje_ges_usuario_cargo WHERE (rut_usuario = ")).append(rutUsuario).append(")")));
						consul3.exec(sql);
						for (; consul3.next(); vecPaso2.addElement(new VerCargo(consul3.getString("empresa"), consul3.getString("cargo"))));
						consul3.close();
						cargosVer = new VerCargo[vecPaso2.size()];
						vecPaso2.copyInto(cargosVer);
						vecPaso2.removeAllElements();
						sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_usuario SET error = 0 WHERE login_usuario = '")).append(xrut.getRut()).append("'")));
						if (consul2.insert(sql))
							getErrores = 0;
					} else {
						mensajeerror = "Clave Erronea";
						sql = String.valueOf(String.valueOf((new StringBuilder("SELECT error FROM eje_ges_usuario WHERE login_usuario = '")).append(xrut.getRut()).append("'")));
						consul2.exec(sql);
						if (consul2.next()) {
							sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_usuario SET error = ")).append(consul2.getInt("error") + 1).append(" WHERE login_usuario = '").append(xrut.getRut()).append("'")));
							if (consul.insert(sql))
								getErrores = consul2.getInt("error") + 1;
						}
					}
			consul.close();
			consul2.close();
			} 
			
		return esValido();
	}

	public boolean getDatosCertif(Connection conn, String rut, String pass) {
		String sql = "";
		int emp = 0;
		valido = false;
		if (conn != null) {
			Consulta consul = new Consulta(conn);
			Consulta consul2 = new Consulta(conn);
			Rut xrut = new Rut(rut);
			sql = String
					.valueOf(String
							.valueOf((new StringBuilder(
									"SELECT rut_usuario,digito_ver,login_usuario,password_usuario,empresa,division,unidad,area,cargo,emp_rel,uni_rel,tipo_rel FROM vista_usuario_certif WHERE (rut_usuario = "))
									.append(xrut.getRut()).append(")")));
			consul.exec(sql);
			if (consul.next()) {
				if (xrut.getDigVer().toUpperCase().equals(
						consul.getString("digito_ver").toUpperCase())) {
					
					String strClaveMD5 = ManagerMD5.encrypt(pass);
					String strClavePortal = consul.getString("password_usuario").toUpperCase().trim();
					
					if (pass.toUpperCase().equals(strClavePortal) || strClaveMD5.toUpperCase().equals(strClavePortal)) {

						rutUsuario = consul.getString("rut_usuario").trim();
						userName = consul.getString("login_usuario").trim();
						empresa = consul.getString("empresa");
						valido = true;
						rutConsultado = rutUsuario;
						division = consul.getString("division");
						cargo = consul.getString("cargo");
						unidad = consul.getString("unidad");
						permisos = ControlAcceso.cargarListaPermisos(conn,
								rutUsuario);
						passw = pass.toUpperCase();
						String emp_rel = consul.getString("emp_rel");
						String uni_rel = consul.getString("uni_rel");
						if (emp_rel != null && !"".equals(emp_rel)) {
							EmpUni_Rel = true;
							System.out.println(String.valueOf(String
									.valueOf((new StringBuilder(
											"<----Su unidad= ")).append(unidad)
											.append("  --->Unidad relativa= ")
											.append(uni_rel))));
							if (!uni_rel.equals(unidad)) {
								System.out
										.println("<-----SU ORGANICA Y ORGANICA RELATIVA------>");
								unidadesRel = new VerUnidad[2];
								unidadesRel[0] = new VerUnidad(emp_rel,
										uni_rel, consul.getString("tipo_rel"));
								unidadesRel[1] = new VerUnidad(empresa, unidad,
										"R");
								verMultiplesRamas = true;
							} else if (!emp_rel.equals(empresa)) {
								System.out
										.println("<-----SU ORGANICA Y ORGANICA RELATIVA------>");
								unidadesRel = new VerUnidad[2];
								unidadesRel[0] = new VerUnidad(emp_rel,
										uni_rel, consul.getString("tipo_rel"));
								unidadesRel[1] = new VerUnidad(empresa, unidad,
										"R");
								verMultiplesRamas = true;
							} else {
								System.out
										.println("<-----SOLO SU ORGANICA,------>");
								unidadesRel = new VerUnidad[1];
								unidadesRel[0] = new VerUnidad(emp_rel,
										uni_rel, consul.getString("tipo_rel"));
								verMultiplesRamas = false;
							}
						} else {
							EmpUni_Rel = false;
							unidadesRel = new VerUnidad[1];
							unidadesRel[0] = new VerUnidad(empresa, unidad, "R");
							verMultiplesRamas = false;
						}
						Vector vecPaso = new Vector();
						for (int x = 0; x < unidadesRel.length; x++)
							vecPaso.addElement(unidadesRel[x]);

						Consulta consul3 = new Consulta(conn);
						sql = String
								.valueOf(String
										.valueOf((new StringBuilder(
												"SELECT empresa, unidad, tipo FROM eje_ges_usuario_unidad WHERE (rut_usuario = "))
												.append(rutUsuario).append(")")));
						consul3.exec(sql);
						for (; consul3.next(); vecPaso
								.addElement(new VerUnidad(consul3
										.getString("empresa"), consul3
										.getString("unidad"), consul3
										.getString("tipo"))))
							verMultiplesRamas = true;

						consul3.close();
						unidadesVer = new VerUnidad[vecPaso.size()];
						vecPaso.copyInto(unidadesVer);
						vecPaso.removeAllElements();
						Vector vecPaso2 = new Vector();
						consul3 = new Consulta(conn);
						sql = String
								.valueOf(String
										.valueOf((new StringBuilder(
												"SELECT empresa, cargo FROM eje_ges_usuario_cargo WHERE (rut_usuario = "))
												.append(rutUsuario).append(")")));
						consul3.exec(sql);
						for (; consul3.next(); vecPaso2
								.addElement(new VerCargo(consul3
										.getString("empresa"), consul3
										.getString("cargo"))))
							;
						consul3.close();
						cargosVer = new VerCargo[vecPaso2.size()];
						vecPaso2.copyInto(cargosVer);
						vecPaso2.removeAllElements();
						sql = String
								.valueOf(String
										.valueOf((new StringBuilder(
												"UPDATE eje_ges_usuario_certif SET error = 0 WHERE login_usuario = '"))
												.append(xrut.getRut()).append(
														"'")));
						if (consul2.insert(sql))
							getErrores = 0;
					} else {
						mensajeerror = "Clave Erronea";
						sql = String
								.valueOf(String
										.valueOf((new StringBuilder(
												"SELECT error FROM eje_ges_usuario_certif WHERE login_usuario = '"))
												.append(xrut.getRut()).append(
														"'")));
						consul2.exec(sql);
						if (consul2.next()) {
							sql = String
									.valueOf(String
											.valueOf((new StringBuilder(
													"UPDATE eje_ges_usuario_certif SET error = "))
													.append(
															consul2
																	.getInt("error") + 1)
													.append(
															" WHERE login_usuario = '")
													.append(xrut.getRut())
													.append("'")));
							if (consul.insert(sql))
								getErrores = consul2.getInt("error") + 1;
						}
					}
				} else {
					mensajeerror = "Error, Digito Verificador";
				}
			} else {
				mensajeerror = "Usuario No Valido";
			}
			consul.close();
			consul2.close();
		}
		return esValido();
	}

	public boolean getDatosAnexos(Connection conn, String rut, String pass) {
		String sql = "";
		int emp = 0;
		valido = false;
		if (conn != null) {
			Consulta consul = new Consulta(conn);
			Consulta consul2 = new Consulta(conn);
			Rut xrut = new Rut(rut);
			sql = String
					.valueOf(String
							.valueOf((new StringBuilder(
									"SELECT rut_usuario,digito_ver,login_usuario,password_usuario,empresa,division,unidad,area,cargo,emp_rel,uni_rel,tipo_rel FROM vista_usuario_anexos WHERE (rut_usuario = "))
									.append(xrut.getRut()).append(")")));
			consul.exec(sql);
			if (consul.next()) {
				if (xrut.getDigVer().toUpperCase().equals(
						consul.getString("digito_ver").toUpperCase())) {
					String strClaveMD5 = ManagerMD5.encrypt(pass);
					String strClavePortal = consul.getString("password_usuario").toUpperCase().trim();
					
					if (pass.toUpperCase().equals(strClavePortal) || strClaveMD5.toUpperCase().equals(strClavePortal)) {

						rutUsuario = consul.getString("rut_usuario").trim();
						userName = consul.getString("login_usuario").trim();
						empresa = consul.getString("empresa");
						valido = true;
						rutConsultado = rutUsuario;
						division = consul.getString("division");
						cargo = consul.getString("cargo");
						unidad = consul.getString("unidad");
						permisos = ControlAcceso.cargarListaPermisos(conn,
								rutUsuario);
						passw = pass.toUpperCase();
						String emp_rel = consul.getString("emp_rel");
						String uni_rel = consul.getString("uni_rel");
						if (emp_rel != null && !"".equals(emp_rel)) {
							EmpUni_Rel = true;
							System.out.println(String.valueOf(String
									.valueOf((new StringBuilder(
											"<----Su unidad= ")).append(unidad)
											.append("  --->Unidad relativa= ")
											.append(uni_rel))));
							if (!uni_rel.equals(unidad)) {
								System.out
										.println("<-----SU ORGANICA Y ORGANICA RELATIVA------>");
								unidadesRel = new VerUnidad[2];
								unidadesRel[0] = new VerUnidad(emp_rel,
										uni_rel, consul.getString("tipo_rel"));
								unidadesRel[1] = new VerUnidad(empresa, unidad,
										"R");
								verMultiplesRamas = true;
							} else if (!emp_rel.equals(empresa)) {
								System.out
										.println("<-----SU ORGANICA Y ORGANICA RELATIVA------>");
								unidadesRel = new VerUnidad[2];
								unidadesRel[0] = new VerUnidad(emp_rel,
										uni_rel, consul.getString("tipo_rel"));
								unidadesRel[1] = new VerUnidad(empresa, unidad,
										"R");
								verMultiplesRamas = true;
							} else {
								System.out
										.println("<-----SOLO SU ORGANICA,------>");
								unidadesRel = new VerUnidad[1];
								unidadesRel[0] = new VerUnidad(emp_rel,
										uni_rel, consul.getString("tipo_rel"));
								verMultiplesRamas = false;
							}
						} else {
							EmpUni_Rel = false;
							unidadesRel = new VerUnidad[1];
							unidadesRel[0] = new VerUnidad(empresa, unidad, "R");
							verMultiplesRamas = false;
						}
						Vector vecPaso = new Vector();
						for (int x = 0; x < unidadesRel.length; x++)
							vecPaso.addElement(unidadesRel[x]);

						Consulta consul3 = new Consulta(conn);
						sql = String
								.valueOf(String
										.valueOf((new StringBuilder(
												"SELECT empresa, unidad, tipo FROM eje_ges_usuario_unidad WHERE (rut_usuario = "))
												.append(rutUsuario).append(")")));
						consul3.exec(sql);
						for (; consul3.next(); vecPaso
								.addElement(new VerUnidad(consul3
										.getString("empresa"), consul3
										.getString("unidad"), consul3
										.getString("tipo"))))
							verMultiplesRamas = true;

						consul3.close();
						unidadesVer = new VerUnidad[vecPaso.size()];
						vecPaso.copyInto(unidadesVer);
						vecPaso.removeAllElements();
						Vector vecPaso2 = new Vector();
						consul3 = new Consulta(conn);
						sql = String
								.valueOf(String
										.valueOf((new StringBuilder(
												"SELECT empresa, cargo FROM eje_ges_usuario_cargo WHERE (rut_usuario = "))
												.append(rutUsuario).append(")")));
						consul3.exec(sql);
						for (; consul3.next(); vecPaso2
								.addElement(new VerCargo(consul3
										.getString("empresa"), consul3
										.getString("cargo"))))
							;
						consul3.close();
						cargosVer = new VerCargo[vecPaso2.size()];
						vecPaso2.copyInto(cargosVer);
						vecPaso2.removeAllElements();
						sql = String
								.valueOf(String
										.valueOf((new StringBuilder(
												"UPDATE eje_ges_usuario_anexos SET error = 0 WHERE login_usuario = '"))
												.append(xrut.getRut()).append(
														"'")));
						if (consul2.insert(sql))
							getErrores = 0;
					} else {
						mensajeerror = "Clave Erronea";
						sql = String
								.valueOf(String
										.valueOf((new StringBuilder(
												"SELECT error FROM eje_ges_usuario_anexos WHERE login_usuario = '"))
												.append(xrut.getRut()).append(
														"'")));
						consul2.exec(sql);
						if (consul2.next()) {
							sql = String
									.valueOf(String
											.valueOf((new StringBuilder(
													"UPDATE eje_ges_usuario_anexos SET error = "))
													.append(
															consul2
																	.getInt("error") + 1)
													.append(
															" WHERE login_usuario = '")
													.append(xrut.getRut())
													.append("'")));
							if (consul.insert(sql))
								getErrores = consul2.getInt("error") + 1;
						}
					}
				} else {
					mensajeerror = "Error, Digito Verificador";
				}
			} else {
				mensajeerror = "Usuario No Valido";
			}
			consul.close();
			consul2.close();
		}
		return esValido();
	}

	public boolean getDatosVigilantes(Connection conn, String rut, String pass) {
		String sql = "";
		int emp = 0;
		valido = false;
		if (conn != null) {
			Consulta consul = new Consulta(conn);
			Consulta consul2 = new Consulta(conn);
			Rut xrut = new Rut(rut);
			sql = String
					.valueOf(String
							.valueOf((new StringBuilder(
									"SELECT rut_usuario,digito_ver,login_usuario,password_usuario,empresa,division,unidad,area,cargo,emp_rel,uni_rel,tipo_rel FROM vista_usuario_vigilantes WHERE (rut_usuario = "))
									.append(xrut.getRut()).append(")")));
			System.err.println("-------->Vigilantes\n".concat(String
					.valueOf(String.valueOf(sql))));
			consul.exec(sql);
			if (consul.next()) {
				if (xrut.getDigVer().toUpperCase().equals(
						consul.getString("digito_ver").toUpperCase())) {
					
					String strClaveMD5 = ManagerMD5.encrypt(pass);
					String strClavePortal = consul.getString("password_usuario").toUpperCase().trim();
					
					if (pass.toUpperCase().equals(strClavePortal) || strClaveMD5.toUpperCase().equals(strClavePortal)) {

						rutUsuario = consul.getString("rut_usuario").trim();
						userName = consul.getString("login_usuario").trim();
						empresa = consul.getString("empresa");
						valido = true;
						rutConsultado = rutUsuario;
						division = consul.getString("division");
						cargo = consul.getString("cargo");
						unidad = consul.getString("unidad");
						permisos = ControlAcceso.cargarListaPermisos(conn,
								rutUsuario);
						passw = pass.toUpperCase();
						String emp_rel = consul.getString("emp_rel");
						if (emp_rel != null && !"".equals(emp_rel)) {
							EmpUni_Rel = true;
							unidadesRel = new VerUnidad[1];
							unidadesRel[0] = new VerUnidad(emp_rel, consul
									.getString("uni_rel"), consul
									.getString("tipo_rel"));
						} else {
							EmpUni_Rel = false;
							unidadesRel = new VerUnidad[1];
							unidadesRel[0] = new VerUnidad(empresa, unidad, "R");
						}
						Vector vecPaso = new Vector();
						for (int x = 0; x < unidadesRel.length; x++)
							vecPaso.addElement(unidadesRel[x]);

						Consulta consul3 = new Consulta(conn);
						sql = String
								.valueOf(String
										.valueOf((new StringBuilder(
												"SELECT empresa, unidad, tipo FROM eje_ges_usuario_unidad WHERE (rut_usuario = "))
												.append(rutUsuario).append(")")));
						consul3.exec(sql);
						verMultiplesRamas = false;
						for (; consul3.next(); vecPaso
								.addElement(new VerUnidad(consul3
										.getString("empresa"), consul3
										.getString("unidad"), consul3
										.getString("tipo"))))
							verMultiplesRamas = true;

						consul3.close();
						unidadesVer = new VerUnidad[vecPaso.size()];
						vecPaso.copyInto(unidadesVer);
						vecPaso.removeAllElements();
						sql = String
								.valueOf(String
										.valueOf((new StringBuilder(
												"UPDATE eje_ges_usuario_anexos SET error = 0 WHERE login_usuario = '"))
												.append(xrut.getRut()).append(
														"'")));
						if (consul2.insert(sql))
							getErrores = 0;
					} else {
						mensajeerror = "Clave Erronea";
						sql = String
								.valueOf(String
										.valueOf((new StringBuilder(
												"SELECT error FROM eje_ges_usuario_anexos WHERE login_usuario = '"))
												.append(xrut.getRut()).append(
														"'")));
						consul2.exec(sql);
						if (consul2.next()) {
							sql = String
									.valueOf(String
											.valueOf((new StringBuilder(
													"UPDATE eje_ges_usuario_anexos SET error = "))
													.append(
															consul2
																	.getInt("error") + 1)
													.append(
															" WHERE login_usuario = '")
													.append(xrut.getRut())
													.append("'")));
							if (consul.insert(sql))
								getErrores = consul2.getInt("error") + 1;
						}
					}
				} else {
					mensajeerror = "Error, Digito Verificador";
				}
			} else {
				mensajeerror = "Usuario No Valido";
			}
			consul.close();
			consul2.close();
		}
		return esValido();
	}

	public boolean esValido() {
		return valido;
	}

	public String getEmpresa() {
		return empresa;
	}

	public String getError() {
		return mensajeerror;
	}

	public String getDivision() {
		return division;
	}

	public String getCargo() {
		return cargo;
	}

	public String getUnidad() {
		return unidad;
	}

	public String getUserName() {
		return userName;
	}

	public String getRutUsuario() {
		return rutUsuario;
	}

	public String getRutConsultado() {
		return rutConsultado;
	}

	public String getPassWord() {
		return passw;
	}

	public String getEmp_Rel() {
		return emp_rel;
	}

	public String getUniRel() {
		return uni_rel;
	}

	public VerUnidad[] getUnidadesVer() {
		return unidadesVer;
	}

	public VerCargo[] getCargosVer() {
		return cargosVer;
	}

	public VerUnidad[] getUnidadesRel() {
		return unidadesRel;
	}

	public boolean veMultiplesRamas() {
		return verMultiplesRamas;
	}

	public ListaPermisos getPermisos() {
		return permisos;
	}

	public int getCantIngresos() {
		return cant_ingresos;
	}

	public void setRutConsultado(String rut) {
		rutConsultado = rut;
	}

	public String toString() {
		String ret = esValido() ? "RutUsuario: ".concat(String.valueOf(String
				.valueOf(getRutUsuario()))) : "Usuario no Valido!!!";
		return ret;
	}

	public void valueBound(HttpSessionBindingEvent parm1) {
		OutMessage.OutMessagePrint("Inicio de Sesion --> SesionId. "
				.concat(String.valueOf(String.valueOf(parm1.getSession()
						.getId()))));
	}

	public void valueUnbound(HttpSessionBindingEvent parm1) {
		OutMessage.OutMessagePrint(String.valueOf(String
				.valueOf((new StringBuilder("Fin de Sesion --> SesionId: "))
						.append(parm1.getSession().getId())
						.append(" usuario: ").append(getRutUsuario()))));

	}
	
	public static Nodo SuperNodo = null;
	private String mensajeerror;
	private String rutUsuario;
	private String empresa;
	private String userName;
	private boolean valido;
	private String rutConsultado;
	private String division;
	private String cargo;
	private String unidad;
	private boolean rol;
	private String passw;
	public int getErrores;
	private ListaPermisos permisos;
	public InfoUsuario infoUsuario;
	private String emp_rel;
	private String uni_rel;
	public boolean EmpUni_Rel;
	private boolean verMultiplesRamas;
	private VerUnidad unidadesRel[];
	private VerUnidad unidadesVer[];
	private VerCargo cargosVer[];
	private int cant_ingresos;

}
