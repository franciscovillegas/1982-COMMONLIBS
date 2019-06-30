package portal.com.eje.serhumano.admin.configdf;

import java.io.IOException;
import java.sql.Connection;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.serhumano.user.unidad.VerUnidad;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Tools;
import portal.com.eje.tools.servlet.GetProp;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

// Referenced classes of package portal.com.eje.serhumano.admin.configdf:
//            UserManager

public class MantenerUsuario extends MyHttpServlet
{

    public MantenerUsuario()
    {
    }

    private String queAccion(HttpServletRequest req, Connection Conexion)
    {
        UserManager usermanager = new UserManager(Conexion);
        String paramAccion = req.getParameter("accion");
        if(paramAccion == null || paramAccion.trim().equals(""))
            paramAccion = "M";
        paramAccion = paramAccion.trim().toUpperCase();
        if(usermanager.existeUsuario(req.getParameter("rut")))
        {
            if(paramAccion.equals("C"))
                paramAccion = "M";
        } else
        if(paramAccion.equals("M"))
            paramAccion = "C";
        usermanager.close();
        return paramAccion;
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection conexion = super.connMgr.getConnection("portal");
        Connection conexDf = super.connMgr.getConnection("portal");
        UserManager usermanager = new UserManager(conexDf);
        OutMessage.OutMessagePrint("\n>>---> Entro al doPost de MantenerUsuario");
        Usuario user = SessionMgr.rescatarUsuario(req);
        if(!user.esValido())
            super.mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        else
        if(!user.tieneApp("adm"))
        {
            super.mensaje.devolverPaginaMensage(resp, "", "No tiene permiso...");
        } else
        {
            boolean seguir = true;
            String strMensajeFinal = "Acci\363n Realizada con exito...";
            String paramRut = req.getParameter("rut");
            String paramAccion = queAccion(req, conexDf);
            String AccionOriginal = paramAccion;
            String paramClave = req.getParameter("clave");
            String accesosSelec[] = req.getParameterValues("acc");
            Vector vecAccesoSelec = new Vector();
            boolean es_certif = false;
            boolean solo_certif = false;
            boolean es_anexo = false;
            boolean solo_anexos = false;
            boolean jerar = false;
            boolean df = false;
            int largo_accesosSelec = 0;
            if(accesosSelec != null)
                largo_accesosSelec = accesosSelec.length;
            if(largo_accesosSelec == 1 && accesosSelec[0].equals("df_certif"))
                solo_certif = true;
            if(largo_accesosSelec == 1 && accesosSelec[0].equals("df_mant_anexo"))
                solo_anexos = true;
            int xx = 0;
            do
            {
                if(xx >= largo_accesosSelec)
                    break;
                OutMessage.OutMessagePrint("***Jerarquia Completa?******--> AccesoId: ".concat(String.valueOf(String.valueOf(accesosSelec[xx]))));
                if(accesosSelec[xx].equals("df_jer_comp"))
                {
                    jerar = true;
                    break;
                }
                xx++;
            } while(true);
            xx = 0;
            do
            {
                if(xx >= largo_accesosSelec)
                    break;
                OutMessage.OutMessagePrint("***Certif******--> AccesoId: ".concat(String.valueOf(String.valueOf(accesosSelec[xx]))));
                if(accesosSelec[xx].equals("df_certif"))
                {
                    es_certif = true;
                    break;
                }
                xx++;
            } while(true);
            xx = 0;
            do
            {
                if(xx >= largo_accesosSelec)
                    break;
                OutMessage.OutMessagePrint("***Anexos******--> AccesoId: ".concat(String.valueOf(String.valueOf(accesosSelec[xx]))));
                if(accesosSelec[xx].equals("df_mant_anexo"))
                {
                    es_anexo = true;
                    break;
                }
                xx++;
            } while(true);
            xx = 0;
            do
            {
                if(xx >= largo_accesosSelec)
                    break;
                OutMessage.OutMessagePrint("***Certif******--> AccesoId: ".concat(String.valueOf(String.valueOf(accesosSelec[xx]))));
                if(!accesosSelec[xx].equals("df_mant_anexo") && !accesosSelec[xx].equals("df_certif"))
                {
                    df = true;
                    break;
                }
                xx++;
            } while(true);
            if(es_certif && jerar && largo_accesosSelec <= 2)
                df = false;
            System.out.println(String.valueOf(String.valueOf((new StringBuilder("********Accion: ")).append(paramAccion).append("Largo Accesos: ").append(largo_accesosSelec).append("Le lleva Certif??: ").append(es_certif).append("Solo Certif?: ").append(solo_certif).append("Le lleva Anexos??: ").append(es_anexo).append("Solo Anexos?: ").append(solo_anexos).append("Clave?: ").append(paramClave))));
            if(paramAccion.equals("C"))
            {
                if(solo_certif)
                {
                    OutMessage.OutMessagePrint("%%%%%%%--> Usuario solo de certificados");
                    if(seguir = usermanager.crearUsuarioCertif(paramRut, paramClave))
                        paramAccion = "M";
                    else
                        strMensajeFinal = "Usuario no creado...";
                } else
                if(!es_certif && !es_anexo && largo_accesosSelec >= 1)
                {
                    OutMessage.OutMessagePrint("%%%%%%%--> No tiene acceso a certificados");
                    if(seguir = usermanager.crearUsuario(paramRut, paramClave))
                        paramAccion = "M";
                    else
                        strMensajeFinal = "Usuario no creado...";
                } else
                {
                    OutMessage.OutMessagePrint("%%%%%%%--> Si Le lleva ecrtificados--------->");
                    if(largo_accesosSelec > 1)
                    {
                        OutMessage.OutMessagePrint("%%%%%%%--> tiene el acceso certificados y otro mas---------->");
                        if(largo_accesosSelec == 2 && !jerar)
                        {
                            OutMessage.OutMessagePrint("%%%%%%%--> Certif--No Jerarquia Completa");
                            if(seguir = usermanager.crearUsuario(paramRut, paramClave))
                                paramAccion = "M";
                        } else
                        if(largo_accesosSelec > 2 && jerar)
                        {
                            OutMessage.OutMessagePrint("%%%%%%%--> Certif--No Jerarquia Completa");
                            if(seguir = usermanager.crearUsuario(paramRut, paramClave))
                                paramAccion = "M";
                        } else
                        if(!jerar && (seguir = usermanager.crearUsuario(paramRut, paramClave)))
                            paramAccion = "M";
                        if(es_certif)
                            if(seguir = usermanager.crearUsuarioCertif(paramRut, paramClave))
                                paramAccion = "M";
                            else
                                strMensajeFinal = "Usuario no creado...";
                    }
                }
                if(solo_anexos)
                {
                    OutMessage.OutMessagePrint("%%%%%%%--> Usuario solo de anexos");
                    if(seguir = usermanager.crearUsuarioAnexos(paramRut, paramClave))
                        paramAccion = "M";
                    else
                        strMensajeFinal = "Usuario no creado...";
                } else
                if(!es_certif && !es_anexo && largo_accesosSelec >= 1)
                {
                    OutMessage.OutMessagePrint("%%%%%%%--> No tiene acceso a anexos");
                    if(seguir = usermanager.crearUsuario(paramRut, paramClave))
                        paramAccion = "M";
                } else
                {
                    OutMessage.OutMessagePrint("%%%%%%%--> Si le lleva Anexos--------->");
                    if(largo_accesosSelec > 1)
                    {
                        OutMessage.OutMessagePrint("%%%%%%%--> Mantener---tiene el acceso anexos y otro mas---------->");
                        if(!(seguir = usermanager.crearUsuarioAnexos(paramRut, paramClave)))
                            strMensajeFinal = "Acci\363n Realizada con exito...";
                    }
                }
            } else
            if(AccionOriginal.equals("M"))
            {
                if(solo_certif)
                {
                    OutMessage.OutMessagePrint("%%%%%%%--> Mantener---Usuario solo de certificados");
                    usermanager.quitarUsuarioDF(paramRut);
                    if(!(seguir = usermanager.crearUsuarioCertif(paramRut, paramClave)))
                        strMensajeFinal = "Acci\363n Realizada con exito...";
                } else
                if(es_certif)
                {
                    OutMessage.OutMessagePrint("%%%%%%%--> Mantener---Uno de los permisos es certi--------->");
                    if(largo_accesosSelec > 1)
                    {
                        OutMessage.OutMessagePrint("%%%%%%%--> tiene el acceso certificados y otro mas---------->");
                        if(largo_accesosSelec == 2 && !jerar)
                        {
                            OutMessage.OutMessagePrint("%%%%%%%--> Mantener Certif--No Jerarquia Completa");
                            if(seguir = usermanager.crearUsuario(paramRut, paramClave))
                                paramAccion = "M";
                        } else
                        if(largo_accesosSelec > 2 && jerar)
                        {
                            OutMessage.OutMessagePrint("%%%%%%%--> Mantener Certif--No Jerarquia Completa");
                            if(seguir = usermanager.crearUsuario(paramRut, paramClave))
                                paramAccion = "M";
                        } else
                        if(!jerar && (seguir = usermanager.crearUsuario(paramRut, paramClave)))
                            paramAccion = "M";
                        if(seguir = usermanager.crearUsuarioCertif(paramRut, paramClave))
                            paramAccion = "M";
                    }
                }
                if(solo_anexos)
                {
                    OutMessage.OutMessagePrint("%%%%%%%--> Mantener--Usuario solo de anexos");
                    usermanager.quitarUsuarioDF(paramRut);
                    if(!(seguir = usermanager.crearUsuarioAnexos(paramRut, paramClave)))
                        strMensajeFinal = "Acci\363n Realizada con exito...";
                } else
                if(es_anexo)
                {
                    OutMessage.OutMessagePrint("%%%%%%%--> Mantener---Uno de los permisos es anexos--------->");
                    if(largo_accesosSelec > 1)
                    {
                        OutMessage.OutMessagePrint("%%%%%%%--> Mantener---tiene el acceso anexos y otro mas---------->");
                        if(!(seguir = usermanager.crearUsuarioAnexos(paramRut, paramClave)))
                            strMensajeFinal = "Acci\363n Realizada con exito...";
                    }
                    if(seguir = usermanager.crearUsuarioAnexos(paramRut, paramClave))
                        paramAccion = "M";
                    if(seguir = usermanager.crearUsuario(paramRut, paramClave))
                        paramAccion = "M";
                }
            }
            if(paramAccion.equals("M") && paramClave != null && !paramClave.trim().equals(""))
            {
                if(!es_certif && !es_anexo && !(seguir = usermanager.cambiarClave(paramRut, paramClave, 'S')))
                    strMensajeFinal = "La Clave no se ha cambiado...";
                if(df && !(seguir = usermanager.cambiarClave(paramRut, paramClave, 'S')))
                    strMensajeFinal = "La Clave no se ha cambiado...";
                if(es_certif && !(seguir = usermanager.cambiarClaveCertif(paramRut, paramClave, 'S')))
                    strMensajeFinal = "La Clave no se ha cambiado...";
                if(es_anexo && !(seguir = usermanager.cambiarClaveAnexos(paramRut, paramClave, 'S')))
                    strMensajeFinal = "La Clave no se ha cambiado...";
            }
            System.out.println("Seguir Antes pregunta?: ".concat(String.valueOf(String.valueOf(seguir))));
            if(!es_certif && !es_anexo && !solo_certif && !solo_anexos)
                seguir = true;
            System.out.println("Seguir Despues pregunta?: ".concat(String.valueOf(String.valueOf(seguir))));
            if(seguir)
            {
                VerUnidad unidRel = null;
                boolean tieneUnidRel = false;
                if(req.getParameter("unirel") != null && req.getParameter("rel_emp") != null && req.getParameter("rel_uni") != null && req.getParameter("rel_tipo") != null && !"".equals(req.getParameter("rel_emp")) && !"".equals(req.getParameter("rel_uni")) && !"".equals(req.getParameter("rel_tipo")))
                {
                    tieneUnidRel = true;
                    unidRel = new VerUnidad(req.getParameter("rel_emp"), req.getParameter("rel_uni"), req.getParameter("rel_tipo"));
                    OutMessage.OutMessagePrint("se asigna unidad/relativa ".concat(String.valueOf(String.valueOf(unidRel.toString()))));
                }
                usermanager.AsignarUniRel(paramRut, unidRel);
                String rolesSelec[] = req.getParameterValues("rol");
                Vector vecRolSelec = new Vector();
                Vector vecRolAgregar = new Vector();
                int largo_rolesSelec = 0;
                if(rolesSelec != null)
                    largo_rolesSelec = rolesSelec.length;
                for(int x = 0; x < largo_rolesSelec; x++)
                {
                    vecRolSelec.add(rolesSelec[x]);
                    if(req.getParameter("accion_rol_".concat(String.valueOf(String.valueOf(rolesSelec[x])))) == null)
                        vecRolAgregar.add(rolesSelec[x]);
                }

                usermanager.agregarRoles(paramRut, vecRolAgregar);
                usermanager.quitarRolesComplementarios(paramRut, vecRolSelec);
                for(int x = 0; x < largo_accesosSelec; x++)
                {
                    vecAccesoSelec.add(accesosSelec[x]);
                    String puede_ver = req.getParameter("puede_ver_".concat(String.valueOf(String.valueOf(accesosSelec[x]))));
                    String acc_tipo = req.getParameter("acc_tipo_".concat(String.valueOf(String.valueOf(accesosSelec[x]))));
                    if(req.getParameter("accion_acc_".concat(String.valueOf(String.valueOf(accesosSelec[x])))) == null)
                    {
                        if(acc_tipo != null && acc_tipo.equals("R"))
                            usermanager.agregarAccesos(paramRut, accesosSelec[x], acc_tipo, puede_ver);
                        else
                            usermanager.agregarAccesos(paramRut, accesosSelec[x], "N");
                    } else
                    {
                        if(accesosSelec[x].equals("df_certif") || accesosSelec[x].equals("df_mant_anexo"))
                            usermanager.agregarAccesos(paramRut, accesosSelec[x], acc_tipo, puede_ver);
                        if(acc_tipo != null && acc_tipo.equals("R"))
                        {
                            OutMessage.OutMessagePrint("****-->Actualizando Accesos ");
                            usermanager.actualizarAccesos(paramRut, accesosSelec[x], puede_ver);
                        }
                    }
                }

                OutMessage.OutMessagePrint("--> Accesos Seleccinados: ".concat(String.valueOf(String.valueOf(vecAccesoSelec.toString()))));
                System.out.println("Entrando a Quitar----------------\n-----AAAAAAAA");
                usermanager.quitarAccesosComplementarios(paramRut, vecAccesoSelec);
                for(int x = 0; x < largo_accesosSelec; x++)
                {
                    String rutsSelec[] = req.getParameterValues("rut_".concat(String.valueOf(String.valueOf(accesosSelec[x]))));
                    Vector vecRutsSelec = new Vector();
                    int largo_rutsSelec = 0;
                    if(rutsSelec != null)
                        largo_rutsSelec = rutsSelec.length;
                    for(int y = 0; y < largo_rutsSelec; y++)
                    {
                        vecRutsSelec.add(rutsSelec[y]);
                        if(req.getParameter(String.valueOf(String.valueOf((new StringBuilder("accion_acc_")).append(accesosSelec[x]).append("_").append(rutsSelec[y])))) == null)
                            usermanager.agregarAccesosRut(paramRut, accesosSelec[x], rutsSelec[y]);
                    }

                    usermanager.quitarAccesosRutComplementarios(paramRut, accesosSelec[x], vecRutsSelec);
                }

            }
            OutMessage.OutMessagePrint(">>---> Fin de doPost");
            generaDatos(req, resp, paramAccion, strMensajeFinal, conexion, conexDf);
        }
        usermanager.close();
        super.connMgr.freeConnection("portal", conexion);
        super.connMgr.freeConnection("portal", conexDf);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = super.connMgr.getConnection("portal");
        Connection conexDf = super.connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\n>>---> Entro al doGet de MantenerUsuario");
        Usuario user = SessionMgr.rescatarUsuario(req);
        if(!user.esValido())
            super.mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        else
        if(!user.tieneApp("adm"))
        {
            super.mensaje.devolverPaginaMensage(resp, "", "No tiene permiso...");
        } else
        {
            UserManager usermanager = new UserManager(conexDf);
            String paramAccion = queAccion(req, conexDf);
            if(paramAccion.equals("E"))
            {
                usermanager.quitarUsuario(req.getParameter("rut"));
                String url = String.valueOf(String.valueOf((new StringBuilder("ListaUsuarios?unidad=")).append(req.getParameter("unidad")).append("&empresa=").append(req.getParameter("empresa"))));
                resp.sendRedirect(url);
            } else
            {
                generaDatos(req, resp, paramAccion, null, Conexion, conexDf);
            }
            usermanager.close();
        }
        super.connMgr.freeConnection("portal", Conexion);
        super.connMgr.freeConnection("portal", conexDf);
    }

    public void generaDatos(HttpServletRequest req, HttpServletResponse resp, String paramAccion, String mensajeFinal, Connection conexion, Connection conexDF)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint(">>---> Entro a generaDatos");
        if(SessionMgr.rescatarUsuario(req).esValido())
        {
            String strUnidad = "";
            String strEmpresa = "";
            String strUnidadDescrip = "";
            String tabla = "";
            String paramRut = req.getParameter("rut");
            if(ExisteEnTabla(paramRut, "eje_ges_usuario", conexDF))
                tabla = "eje_ges_usuario";
            else
            if(ExisteEnTabla(paramRut, "eje_ges_usuario_certif", conexDF))
                tabla = "eje_ges_usuario_certif";
            else
            if(ExisteEnTabla(paramRut, "eje_ges_usuario_anexos", conexDF))
                tabla = "eje_ges_usuario_anexos";
            SimpleHash modelRoot = new SimpleHash();
            Consulta consul = new Consulta(conexDF);
            String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT nombre,cargo,digito_ver,anexo,e_mail,id_foto,id_unidad,unidad,empresa FROM view_InfoRut  WHERE (rut = ")).append(paramRut).append(")")));
            consul.exec(sql);
            if(consul.next())
            {
                strUnidad = consul.getString("id_unidad");
                strUnidadDescrip = consul.getString("unidad");
                strEmpresa = consul.getString("empresa");
                modelRoot.put("anexo", consul.getString("anexo"));
                modelRoot.put("mail", consul.getString("e_mail"));
                modelRoot.put("nombre", consul.getString("nombre"));
                modelRoot.put("cargo", consul.getString("cargo"));
                modelRoot.put("foto", consul.getString("id_foto"));
                modelRoot.put("rut_form", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(paramRut))))).append("-").append(consul.getString("digito_ver")))));
            }
            modelRoot.put("unid_desc", strUnidadDescrip);
            modelRoot.put("unid_id", strUnidad);
            modelRoot.put("rut", paramRut);
            modelRoot.put("accion", paramAccion);
            modelRoot.put("MSGRESUL", mensajeFinal);
            VerUnidad unidRel = new VerUnidad(strEmpresa, strUnidad, "R");
            boolean tieneUnidRel = false;
            if(paramAccion.equals("C"))
            {
                sql = "SELECT rol_id, rol_glosa, rol_desc FROM eje_ges_roles ORDER BY rol_desc";
            } else
            {
                sql = String.valueOf(String.valueOf((new StringBuilder("select password_usuario,error,emp_rel,uni_rel,tipo_rel from ")).append(tabla).append(" where rut_usuario = ").append(paramRut)));
                consul.exec(sql);
                System.out.println("******Rescatando Datos Usuario: ".concat(String.valueOf(String.valueOf(sql))));
                if(consul.next())
                {
                    int error = consul.getInt("error");
                    modelRoot.put("clave", consul.getString("password_usuario"));
                    String paso = consul.getString("emp_rel");
                    if(paso != null && !"".equals(paso.trim()))
                    {
                        unidRel = new VerUnidad(paso, consul.getString("uni_rel"), consul.getString("tipo_rel"));
                        tieneUnidRel = true;
                        OutMessage.OutMessagePrint("tiene unidad/relativa ".concat(String.valueOf(String.valueOf(unidRel.toString()))));
                    }
                    consul.exec("SELECT valor FROM eje_ges_parametros WHERE (id = 2)");
                    if(consul.next())
                        modelRoot.put("bloqueo", error > consul.getInt("valor"));
                }
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT rol.rol_id, rol_glosa, rol.rol_desc, usr.rut_usuario as selec FROM eje_ges_roles rol LEFT OUTER JOIN eje_ges_usuario_rol usr ON rol.rol_id = usr.rol_id AND usr.rut_usuario = ")).append(paramRut).append(" ORDER BY rol_desc")));
            }
            consul.exec(sql);
            modelRoot.put("roles", consul.getSimpleList());
            sql = String.valueOf(String.valueOf((new StringBuilder("SELECT accgr.acc_gru_glosa AS acc_grupo, acc.acc_id, acc.acc_glosa, acc.acc_tipo, uacc.rut_usuario as selec, uacc.acc_puede_ver FROM eje_ges_acceso acc INNER JOIN eje_ges_acceso_grupo accgr ON acc.acc_gru_id = accgr.acc_gru_id LEFT OUTER JOIN eje_ges_usuario_acceso uacc ON acc.acc_id = uacc.acc_id AND uacc.rut_usuario = ")).append(paramRut).append(" ORDER BY acc_grupo, acc_glosa")));
            consul.exec(sql);
            SimpleList listaAccesos = new SimpleList();
            Consulta consulRuts = new Consulta(conexDF);
            if(req.getParameter("unirel") != null && req.getParameter("rel_emp") != null && req.getParameter("rel_uni") != null && req.getParameter("rel_tipo") != null && !"".equals(req.getParameter("rel_emp")) && !"".equals(req.getParameter("rel_uni")) && !"".equals(req.getParameter("rel_tipo")))
            {
                tieneUnidRel = true;
                unidRel = new VerUnidad(req.getParameter("rel_emp"), req.getParameter("rel_uni"), req.getParameter("rel_tipo"));
                OutMessage.OutMessagePrint("se asigna unidad/relativa ".concat(String.valueOf(String.valueOf(unidRel.toString()))));
            }
            if(tieneUnidRel)
            {
                modelRoot.put("unirel", tieneUnidRel);
                modelRoot.put("rel_emp", unidRel.getEmpresa());
                modelRoot.put("rel_uni", unidRel.getUnidad());
                modelRoot.put("rel_tipo", unidRel.getQueVer());
                OutMessage.OutMessagePrint("en pag. unidad/relativa ".concat(String.valueOf(String.valueOf(unidRel.toString()))));
            }
            SimpleHash acceso;
            for(; consul.next(); listaAccesos.add(acceso))
            {
                acceso = new SimpleHash();
                String acc_id = consul.getString("acc_id");
                String accc_tipo = consul.getString("acc_tipo");
                String puede_ver = consul.getString("acc_puede_ver");
                acceso.put("acc_grupo", consul.getString("acc_grupo"));
                acceso.put("acc_id", acc_id);
                acceso.put("acc_glosa", consul.getString("acc_glosa"));
                acceso.put("acc_tipo", accc_tipo);
                acceso.put("selec", consul.getString("selec"));
                if(puede_ver == null)
                    puede_ver = "N";
                acceso.put("acc_puede_ver", puede_ver);
                if(accc_tipo.equalsIgnoreCase("R"))
                {
                    sql = String.valueOf(String.valueOf((new StringBuilder("SELECT trab.rut AS rut_trab, trab.nombre, accrut.rut_trab AS selec FROM eje_ges_trabajador trab LEFT OUTER JOIN eje_ges_usuario_acceso_rut accrut ON trab.rut = accrut.rut_trab AND accrut.rut_usuario = ")).append(paramRut).append(" AND accrut.acc_id = '").append(acc_id).append("'").append(" WHERE ((trab.unidad = '").append(unidRel.getUnidad()).append("') and  (trab.empresa='").append(unidRel.getEmpresa()).append("')) OR (trab.rut = ").append(paramRut).append(") ORDER BY nombre")));
                    consulRuts.exec(sql);
                    acceso.put("ruts", consulRuts.getSimpleList());
                }
            }

            consulRuts.close();
            modelRoot.put("accesos", listaAccesos);
            modelRoot.put("GetProp", new GetProp(ResourceBundle.getBundle("db")));
            consul.close();
            super.retTemplate(resp,"admin/configdf/MantenerUsuario.htm",modelRoot);
        } else
        {
            super.mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        }
        OutMessage.OutMessagePrint(">>---> Fin de generaDatos Mantener Usuario");
    }

    private boolean ExisteEnTabla(String rutUser, String tabla, Connection conex)
    {
        Consulta consul = new Consulta(conex);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT * FROM ")).append(tabla).append(" WHERE (rut_usuario = ").append(rutUser).append(")")));
        consul.exec(sql);
        return consul.next();
    }

    public String getServletInfo()
    {
        return "Este servlet genera el detalle del indicador";
    }
}