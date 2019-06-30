package portal.com.eje.usuario;

import java.sql.Connection;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import portal.com.eje.arbol.Nodo;
import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Rut;
import portal.com.eje.usuario.cargo.VerCargo;
import portal.com.eje.usuario.unidad.VerUnidad;

// Referenced classes of package portal.com.eje.usuario:
//            UsuariosConectados, ControlAcceso, ListaPermisos, InfoUsuario

/**
 * @deprecated 2016-03-03
 * @author PANCHO
 * Deprecado ya que nos dimos que cuenta hoy que no se usa
 * */

public class Usuario
    implements HttpSessionBindingListener
{

    public Usuario()
    {
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
    }

    public static Usuario rescatarUsuario(HttpServletRequest req)
    {
        return rescatarUsuario(req.getSession(false));
    }

    public static Usuario rescatarUsuario(HttpSession sesion)
    {
        Usuario user;
        if(sesion == null)
        {
            OutMessage.OutMessagePrint("rescatarUsuario --> Session nula");
            user = new Usuario();
        } else
        {
            try
            {
                user = (Usuario)sesion.getAttribute(sesion.getId());
            }
            catch(Exception e)
            {
                System.out.println("rescatarUsuario Exception --> " + e.getMessage());
                user = new Usuario();
            }
            if(user == null)
            {
                OutMessage.OutMessagePrint("rescatarUsuario --> Usuario nulo");
                user = new Usuario();
            }
        }
        OutMessage.OutMessagePrint("rescatarUsuario Usuario --> " + user.toString());
        return user;
    }

    public Usuario(Connection conn, String user, String pass)
    {
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
        getDatos(conn, user, pass);
    }

    public boolean getDatos(Connection conn, String rut, String pass)
    {
        String sql = "";
        int emp = 0;
        valido = false;
        if(conn != null)
        {
            Consulta consul = new Consulta(conn);
            Consulta consul2 = new Consulta(conn);
            if(conn == null)
                System.err.println("----->conexin NULA!!!!");
            Rut xrut = new Rut(rut);
            sql = "SELECT rut_usuario,digito_ver,login_usuario,password_usuario,empresa,division,unidad,area,cargo,emp_rel,uni_rel,tipo_rel FROM vista_usuario WHERE (rut_usuario = " + xrut.getRut() + ")";
            System.err.println("---->getDatos\n" + sql);
            consul.exec(sql);
            if(consul.next())
            {
                System.err.println("====>getDigVer()= " + xrut.getDigVer());
                if(xrut.getDigVer().toUpperCase().equals(consul.getString("digito_ver").toUpperCase()))
                {
                    if(pass.toUpperCase().equals(consul.getString("password_usuario").toUpperCase().trim()))
                    {
                        rutUsuario = consul.getString("rut_usuario").trim();
                        userName = consul.getString("login_usuario").trim();
                        empresa = consul.getString("empresa");
                        valido = true;
                        rutConsultado = rutUsuario;
                        division = consul.getString("division");
                        cargo = consul.getString("cargo");
                        unidad = consul.getString("unidad");
                        permisos = ControlAcceso.cargarListaPermisos(conn, rutUsuario);
                        passw = pass.toUpperCase();
                        String emp_rel = consul.getString("emp_rel");
                        String uni_rel = consul.getString("uni_rel");
                        if(emp_rel != null && !"".equals(emp_rel))
                        {
                            EmpUni_Rel = true;
                            if(!uni_rel.equals(unidad) && !emp_rel.equals(empresa))
                            {
                                unidadesRel = new VerUnidad[2];
                                unidadesRel[0] = new VerUnidad(emp_rel, uni_rel, consul.getString("tipo_rel"));
                                unidadesRel[1] = new VerUnidad(empresa, unidad, "R");
                                verMultiplesRamas = true;
                            } else
                            {
                                unidadesRel = new VerUnidad[1];
                                unidadesRel[0] = new VerUnidad(emp_rel, uni_rel, consul.getString("tipo_rel"));
                                verMultiplesRamas = false;
                            }
                        } else
                        {
                            EmpUni_Rel = false;
                            unidadesRel = new VerUnidad[1];
                            unidadesRel[0] = new VerUnidad(empresa, unidad, "R");
                            verMultiplesRamas = false;
                        }
                        Vector vecPaso = new Vector();
                        for(int x = 0; x < unidadesRel.length; x++)
                            vecPaso.addElement(unidadesRel[x]);

                        Consulta consul3 = new Consulta(conn);
                        sql = "SELECT empresa, unidad, tipo FROM eje_ges_usuario_unidad WHERE (rut_usuario = " + rutUsuario + ")";
                        consul3.exec(sql);
                        for(; consul3.next(); vecPaso.addElement(new VerUnidad(consul3.getString("empresa"), consul3.getString("unidad"), consul3.getString("tipo"))))
                            verMultiplesRamas = true;

                        consul3.close();
                        unidadesVer = new VerUnidad[vecPaso.size()];
                        vecPaso.copyInto(unidadesVer);
                        vecPaso.removeAllElements();
                        Vector vecPaso2 = new Vector();
                        consul3 = new Consulta(conn);
                        sql = "SELECT empresa, cargo FROM eje_ges_usuario_cargo WHERE (rut_usuario = " + rutUsuario + ")";
                        consul3.exec(sql);
                        for(; consul3.next(); vecPaso2.addElement(new VerCargo(consul3.getString("empresa"), consul3.getString("cargo"))));
                        consul3.close();
                        cargosVer = new VerCargo[vecPaso2.size()];
                        vecPaso2.copyInto(cargosVer);
                        vecPaso2.removeAllElements();
                        sql = "UPDATE eje_ges_usuario SET error = 0 WHERE login_usuario = '" + xrut.getRut() + "'";
                        if(consul2.insert(sql))
                            getErrores = 0;
                    } else
                    {
                        mensajeerror = "Clave Erronea";
                        sql = "SELECT error FROM eje_ges_usuario WHERE login_usuario = '" + xrut.getRut() + "'";
                        consul2.exec(sql);
                        if(consul2.next())
                        {
                            sql = "UPDATE eje_ges_usuario SET error = " + (consul2.getInt("error") + 1) + " WHERE login_usuario = '" + xrut.getRut() + "'";
                            if(consul.insert(sql))
                                getErrores = consul2.getInt("error") + 1;
                        }
                    }
                } else
                {
                    mensajeerror = "Error, Digito Verificador";
                }
            } else
            {
                mensajeerror = "Usuario No Valido";
            }
            consul.close();
            consul2.close();
        }
        return esValido();
    }

    public boolean getDatosCertif(Connection conn, String rut, String pass)
    {
        String sql = "";
        int emp = 0;
        valido = false;
        if(conn != null)
        {
            Consulta consul = new Consulta(conn);
            Consulta consul2 = new Consulta(conn);
            Rut xrut = new Rut(rut);
            sql = "SELECT rut_usuario,digito_ver,login_usuario,password_usuario,empresa,division,unidad,area,cargo,emp_rel,uni_rel,tipo_rel FROM vista_usuario_certif WHERE (rut_usuario = " + xrut.getRut() + ")";
            consul.exec(sql);
            if(consul.next())
            {
                if(xrut.getDigVer().toUpperCase().equals(consul.getString("digito_ver").toUpperCase()))
                {
                    if(pass.toUpperCase().equals(consul.getString("password_usuario").toUpperCase().trim()))
                    {
                        rutUsuario = consul.getString("rut_usuario").trim();
                        userName = consul.getString("login_usuario").trim();
                        empresa = consul.getString("empresa");
                        valido = true;
                        rutConsultado = rutUsuario;
                        division = consul.getString("division");
                        cargo = consul.getString("cargo");
                        unidad = consul.getString("unidad");
                        permisos = ControlAcceso.cargarListaPermisos(conn, rutUsuario);
                        passw = pass.toUpperCase();
                        String emp_rel = consul.getString("emp_rel");
                        String uni_rel = consul.getString("uni_rel");
                        if(emp_rel != null && !"".equals(emp_rel))
                        {
                            EmpUni_Rel = true;
                            System.out.println("<----Su unidad= " + unidad + "  --->Unidad relativa= " + uni_rel);
                            if(!uni_rel.equals(unidad))
                            {
                                System.out.println("<-----SU ORGANICA Y ORGANICA RELATIVA------>");
                                unidadesRel = new VerUnidad[2];
                                unidadesRel[0] = new VerUnidad(emp_rel, uni_rel, consul.getString("tipo_rel"));
                                unidadesRel[1] = new VerUnidad(empresa, unidad, "R");
                                verMultiplesRamas = true;
                            } else
                            if(!emp_rel.equals(empresa))
                            {
                                System.out.println("<-----SU ORGANICA Y ORGANICA RELATIVA------>");
                                unidadesRel = new VerUnidad[2];
                                unidadesRel[0] = new VerUnidad(emp_rel, uni_rel, consul.getString("tipo_rel"));
                                unidadesRel[1] = new VerUnidad(empresa, unidad, "R");
                                verMultiplesRamas = true;
                            } else
                            {
                                System.out.println("<-----SOLO SU ORGANICA,------>");
                                unidadesRel = new VerUnidad[1];
                                unidadesRel[0] = new VerUnidad(emp_rel, uni_rel, consul.getString("tipo_rel"));
                                verMultiplesRamas = false;
                            }
                        } else
                        {
                            EmpUni_Rel = false;
                            unidadesRel = new VerUnidad[1];
                            unidadesRel[0] = new VerUnidad(empresa, unidad, "R");
                            verMultiplesRamas = false;
                        }
                        Vector vecPaso = new Vector();
                        for(int x = 0; x < unidadesRel.length; x++)
                            vecPaso.addElement(unidadesRel[x]);

                        Consulta consul3 = new Consulta(conn);
                        sql = "SELECT empresa, unidad, tipo FROM eje_ges_usuario_unidad WHERE (rut_usuario = " + rutUsuario + ")";
                        consul3.exec(sql);
                        for(; consul3.next(); vecPaso.addElement(new VerUnidad(consul3.getString("empresa"), consul3.getString("unidad"), consul3.getString("tipo"))))
                            verMultiplesRamas = true;

                        consul3.close();
                        unidadesVer = new VerUnidad[vecPaso.size()];
                        vecPaso.copyInto(unidadesVer);
                        vecPaso.removeAllElements();
                        Vector vecPaso2 = new Vector();
                        consul3 = new Consulta(conn);
                        sql = "SELECT empresa, cargo FROM eje_ges_usuario_cargo WHERE (rut_usuario = " + rutUsuario + ")";
                        consul3.exec(sql);
                        for(; consul3.next(); vecPaso2.addElement(new VerCargo(consul3.getString("empresa"), consul3.getString("cargo"))));
                        consul3.close();
                        cargosVer = new VerCargo[vecPaso2.size()];
                        vecPaso2.copyInto(cargosVer);
                        vecPaso2.removeAllElements();
                        sql = "UPDATE eje_ges_usuario_certif SET error = 0 WHERE login_usuario = '" + xrut.getRut() + "'";
                        if(consul2.insert(sql))
                            getErrores = 0;
                    } else
                    {
                        mensajeerror = "Clave Erronea";
                        sql = "SELECT error FROM eje_ges_usuario_certif WHERE login_usuario = '" + xrut.getRut() + "'";
                        consul2.exec(sql);
                        if(consul2.next())
                        {
                            sql = "UPDATE eje_ges_usuario_certif SET error = " + (consul2.getInt("error") + 1) + " WHERE login_usuario = '" + xrut.getRut() + "'";
                            if(consul.insert(sql))
                                getErrores = consul2.getInt("error") + 1;
                        }
                    }
                } else
                {
                    mensajeerror = "Error, Digito Verificador";
                }
            } else
            {
                mensajeerror = "Usuario No Valido";
            }
            consul.close();
            consul2.close();
        }
        return esValido();
    }

    public boolean getDatosAnexos(Connection conn, String rut, String pass)
    {
        String sql = "";
        int emp = 0;
        valido = false;
        if(conn != null)
        {
            Consulta consul = new Consulta(conn);
            Consulta consul2 = new Consulta(conn);
            Rut xrut = new Rut(rut);
            sql = "SELECT rut_usuario,digito_ver,login_usuario,password_usuario,empresa,division,unidad,area,cargo,emp_rel,uni_rel,tipo_rel FROM vista_usuario_anexos WHERE (rut_usuario = " + xrut.getRut() + ")";
            consul.exec(sql);
            if(consul.next())
            {
                if(xrut.getDigVer().toUpperCase().equals(consul.getString("digito_ver").toUpperCase()))
                {
                    if(pass.toUpperCase().equals(consul.getString("password_usuario").toUpperCase().trim()))
                    {
                        rutUsuario = consul.getString("rut_usuario").trim();
                        userName = consul.getString("login_usuario").trim();
                        empresa = consul.getString("empresa");
                        valido = true;
                        rutConsultado = rutUsuario;
                        division = consul.getString("division");
                        cargo = consul.getString("cargo");
                        unidad = consul.getString("unidad");
                        permisos = ControlAcceso.cargarListaPermisos(conn, rutUsuario);
                        passw = pass.toUpperCase();
                        String emp_rel = consul.getString("emp_rel");
                        String uni_rel = consul.getString("uni_rel");
                        if(emp_rel != null && !"".equals(emp_rel))
                        {
                            EmpUni_Rel = true;
                            System.out.println("<----Su unidad= " + unidad + "  --->Unidad relativa= " + uni_rel);
                            if(!uni_rel.equals(unidad))
                            {
                                System.out.println("<-----SU ORGANICA Y ORGANICA RELATIVA------>");
                                unidadesRel = new VerUnidad[2];
                                unidadesRel[0] = new VerUnidad(emp_rel, uni_rel, consul.getString("tipo_rel"));
                                unidadesRel[1] = new VerUnidad(empresa, unidad, "R");
                                verMultiplesRamas = true;
                            } else
                            if(!emp_rel.equals(empresa))
                            {
                                System.out.println("<-----SU ORGANICA Y ORGANICA RELATIVA------>");
                                unidadesRel = new VerUnidad[2];
                                unidadesRel[0] = new VerUnidad(emp_rel, uni_rel, consul.getString("tipo_rel"));
                                unidadesRel[1] = new VerUnidad(empresa, unidad, "R");
                                verMultiplesRamas = true;
                            } else
                            {
                                System.out.println("<-----SOLO SU ORGANICA,------>");
                                unidadesRel = new VerUnidad[1];
                                unidadesRel[0] = new VerUnidad(emp_rel, uni_rel, consul.getString("tipo_rel"));
                                verMultiplesRamas = false;
                            }
                        } else
                        {
                            EmpUni_Rel = false;
                            unidadesRel = new VerUnidad[1];
                            unidadesRel[0] = new VerUnidad(empresa, unidad, "R");
                            verMultiplesRamas = false;
                        }
                        Vector vecPaso = new Vector();
                        for(int x = 0; x < unidadesRel.length; x++)
                            vecPaso.addElement(unidadesRel[x]);

                        Consulta consul3 = new Consulta(conn);
                        sql = "SELECT empresa, unidad, tipo FROM eje_ges_usuario_unidad WHERE (rut_usuario = " + rutUsuario + ")";
                        consul3.exec(sql);
                        for(; consul3.next(); vecPaso.addElement(new VerUnidad(consul3.getString("empresa"), consul3.getString("unidad"), consul3.getString("tipo"))))
                            verMultiplesRamas = true;

                        consul3.close();
                        unidadesVer = new VerUnidad[vecPaso.size()];
                        vecPaso.copyInto(unidadesVer);
                        vecPaso.removeAllElements();
                        Vector vecPaso2 = new Vector();
                        consul3 = new Consulta(conn);
                        sql = "SELECT empresa, cargo FROM eje_ges_usuario_cargo WHERE (rut_usuario = " + rutUsuario + ")";
                        consul3.exec(sql);
                        for(; consul3.next(); vecPaso2.addElement(new VerCargo(consul3.getString("empresa"), consul3.getString("cargo"))));
                        consul3.close();
                        cargosVer = new VerCargo[vecPaso2.size()];
                        vecPaso2.copyInto(cargosVer);
                        vecPaso2.removeAllElements();
                        sql = "UPDATE eje_ges_usuario_anexos SET error = 0 WHERE login_usuario = '" + xrut.getRut() + "'";
                        if(consul2.insert(sql))
                            getErrores = 0;
                    } else
                    {
                        mensajeerror = "Clave Erronea";
                        sql = "SELECT error FROM eje_ges_usuario_anexos WHERE login_usuario = '" + xrut.getRut() + "'";
                        consul2.exec(sql);
                        if(consul2.next())
                        {
                            sql = "UPDATE eje_ges_usuario_anexos SET error = " + (consul2.getInt("error") + 1) + " WHERE login_usuario = '" + xrut.getRut() + "'";
                            if(consul.insert(sql))
                                getErrores = consul2.getInt("error") + 1;
                        }
                    }
                } else
                {
                    mensajeerror = "Error, Digito Verificador";
                }
            } else
            {
                mensajeerror = "Usuario No Valido";
            }
            consul.close();
            consul2.close();
        }
        return esValido();
    }

    public boolean getDatosVigilantes(Connection conn, String rut, String pass)
    {
        String sql = "";
        int emp = 0;
        valido = false;
        if(conn != null)
        {
            Consulta consul = new Consulta(conn);
            Consulta consul2 = new Consulta(conn);
            Rut xrut = new Rut(rut);
            sql = "SELECT rut_usuario,digito_ver,login_usuario,password_usuario,empresa,division,unidad,area,cargo,emp_rel,uni_rel,tipo_rel FROM vista_usuario_vigilantes WHERE (rut_usuario = " + xrut.getRut() + ")";
            System.err.println("-------->Vigilantes\n" + sql);
            consul.exec(sql);
            if(consul.next())
            {
                if(xrut.getDigVer().toUpperCase().equals(consul.getString("digito_ver").toUpperCase()))
                {
                    if(pass.toUpperCase().equals(consul.getString("password_usuario").toUpperCase().trim()))
                    {
                        rutUsuario = consul.getString("rut_usuario").trim();
                        userName = consul.getString("login_usuario").trim();
                        empresa = consul.getString("empresa");
                        valido = true;
                        rutConsultado = rutUsuario;
                        division = consul.getString("division");
                        cargo = consul.getString("cargo");
                        unidad = consul.getString("unidad");
                        permisos = ControlAcceso.cargarListaPermisos(conn, rutUsuario);
                        passw = pass.toUpperCase();
                        String emp_rel = consul.getString("emp_rel");
                        if(emp_rel != null && !"".equals(emp_rel))
                        {
                            EmpUni_Rel = true;
                            unidadesRel = new VerUnidad[1];
                            unidadesRel[0] = new VerUnidad(emp_rel, consul.getString("uni_rel"), consul.getString("tipo_rel"));
                        } else
                        {
                            EmpUni_Rel = false;
                            unidadesRel = new VerUnidad[1];
                            unidadesRel[0] = new VerUnidad(empresa, unidad, "R");
                        }
                        Vector vecPaso = new Vector();
                        for(int x = 0; x < unidadesRel.length; x++)
                            vecPaso.addElement(unidadesRel[x]);

                        Consulta consul3 = new Consulta(conn);
                        sql = "SELECT empresa, unidad, tipo FROM eje_ges_usuario_unidad WHERE (rut_usuario = " + rutUsuario + ")";
                        consul3.exec(sql);
                        verMultiplesRamas = false;
                        for(; consul3.next(); vecPaso.addElement(new VerUnidad(consul3.getString("empresa"), consul3.getString("unidad"), consul3.getString("tipo"))))
                            verMultiplesRamas = true;

                        consul3.close();
                        unidadesVer = new VerUnidad[vecPaso.size()];
                        vecPaso.copyInto(unidadesVer);
                        vecPaso.removeAllElements();
                        sql = "UPDATE eje_ges_usuario_anexos SET error = 0 WHERE login_usuario = '" + xrut.getRut() + "'";
                        if(consul2.insert(sql))
                            getErrores = 0;
                    } else
                    {
                        mensajeerror = "Clave Erronea";
                        sql = "SELECT error FROM eje_ges_usuario_anexos WHERE login_usuario = '" + xrut.getRut() + "'";
                        consul2.exec(sql);
                        if(consul2.next())
                        {
                            sql = "UPDATE eje_ges_usuario_anexos SET error = " + (consul2.getInt("error") + 1) + " WHERE login_usuario = '" + xrut.getRut() + "'";
                            if(consul.insert(sql))
                                getErrores = consul2.getInt("error") + 1;
                        }
                    }
                } else
                {
                    mensajeerror = "Error, Digito Verificador";
                }
            } else
            {
                mensajeerror = "Usuario No Valido";
            }
            consul.close();
            consul2.close();
        }
        return esValido();
    }

    public boolean esValido()
    {
        return valido;
    }

    public String getEmpresa()
    {
        return empresa;
    }

    public String getError()
    {
        return mensajeerror;
    }

    public String getDivision()
    {
        return division;
    }

    public String getCargo()
    {
        return cargo;
    }

    public String getUnidad()
    {
        return unidad;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getRutUsuario()
    {
        return rutUsuario;
    }

    public String getRutConsultado()
    {
        return rutConsultado;
    }

    public String getPassWord()
    {
        return passw;
    }

    public String getEmp_Rel()
    {
        return emp_rel;
    }

    public String getUniRel()
    {
        return uni_rel;
    }

    public VerUnidad[] getUnidadesVer()
    {
        return unidadesVer;
    }

    public VerCargo[] getCargosVer()
    {
        return cargosVer;
    }

    public VerUnidad[] getUnidadesRel()
    {
        return unidadesRel;
    }

    public boolean veMultiplesRamas()
    {
        return verMultiplesRamas;
    }

    public ListaPermisos getPermisos()
    {
        return permisos;
    }

    public void setRutConsultado(String rut)
    {
        rutConsultado = rut;
    }

    public String toString()
    {
        String ret = "";
        if(esValido())
            ret = "RutUsuario: " + getRutUsuario();
        else
            ret = "Usuario no Valido!!!";
        return ret;
    }

    public void valueBound(HttpSessionBindingEvent parm1)
    {
        OutMessage.OutMessagePrint("Inicio de Sesion --> SesionId. " + parm1.getSession().getId());
    }

    public void valueUnbound(HttpSessionBindingEvent parm1)
    {
        OutMessage.OutMessagePrint("Fin de Sesion --> SesionId: " + parm1.getSession().getId() + " usuario: " + getRutUsuario());
        Usuario _tmp = this;
        userConect.removeUsuario(infoUsuario);
        Usuario _tmp1 = this;
        OutMessage.OutMessagePrint("usuarios Conectados --> " + userConect.toString());
    }

    public static Nodo SuperNodo = null;
    public static UsuariosConectados userConect = new UsuariosConectados();
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

}