package organica.DatosRut;

import java.sql.Connection;

import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import organica.tools.Validar;

import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class Rut
{

    public Rut()
    {
        Rut = null;
        Nombres = null;
        Empresa = null;
        EmpresaDescrip = null;
        Id_Unidad = null;
        Unidad = null;
        Cargo = null;
        Id_Cargo = null;
        Division = null;
        Area = null;
        Anexo = null;
        fono = null;
        Sindicato = null;
        Login = null;
        Email = null;
        Mail = null;
        Foto = null;
        Digito_Ver = null;
        FecIngreso = null;
        FecNacim = null;
        Domicilio = null;
        AntigCargo = null;
        Sexo = null;
        UbicFisica = null;
        EstadoCivil = null;
        Ncargas = null;
        SueldoBase = null;
        Sup_Rut = null;
        Sup_DV = null;
        Sup_Email = null;
        Sup_Mail = null;
        Sup_Anexo = null;
        Sup_Foto = null;
        Sup_Cargo = null;
        Sup_Unidad = null;
        Sup_Nombre = null;
        TotaHijos = null;
        valido = false;
    }

    public Rut(Connection conn, String rut)
    {
        Rut = null;
        Nombres = null;
        Empresa = null;
        EmpresaDescrip = null;
        Id_Unidad = null;
        Unidad = null;
        Cargo = null;
        Id_Cargo = null;
        Division = null;
        Area = null;
        Anexo = null;
        fono = null;
        Sindicato = null;
        Login = null;
        Email = null;
        Mail = null;
        Foto = null;
        Digito_Ver = null;
        FecIngreso = null;
        FecNacim = null;
        Domicilio = null;
        AntigCargo = null;
        Sexo = null;
        UbicFisica = null;
        EstadoCivil = null;
        Ncargas = null;
        SueldoBase = null;
        Sup_Rut = null;
        Sup_DV = null;
        Sup_Email = null;
        Sup_Mail = null;
        Sup_Anexo = null;
        Sup_Foto = null;
        Sup_Cargo = null;
        Sup_Unidad = null;
        Sup_Nombre = null;
        TotaHijos = null;
        String sql = "";
        if(conn != null)
        {
            Consulta consul = new Consulta(conn);
            Consulta consul2 = new Consulta(conn);
            sql = String.valueOf(String.valueOf((new StringBuilder("SELECT distinct rut, digito_ver,id_unidad, id_cargo, empresa, nombre, cargo, sindicato, afp, isapre, division, ubic_fisica, area, rut_supd, dig_supd, nom_supd, cargo_supd, unidad, fecha_nacim, fecha_ingreso, estado_civil, domicilio, comuna, ciudad, telefono, celular, anexo, ape_paterno, ape_materno, nombres, e_mail,mail, fecha_cargo, antig_empresa, antig_cargo, pais, nacionalidad, sexo,foto, grupo_sangre,num_cargas,sueldo, empresa_descrip FROM view_ges_rut_all WHERE (rut = ")).append(rut).append(")")));
            consul.exec(sql);
            if(consul.next())
            {
                valido = true;
                Rut = consul.getString("rut").trim();
                Digito_Ver = consul.getString("digito_ver");
                Nombres = consul.getString("nombre").trim();
                Empresa = consul.getString("empresa");
                EmpresaDescrip = consul.getString("empresa_descrip");
                Unidad = consul.getString("unidad");
                Id_Unidad = consul.getString("id_unidad");
                Id_Cargo = consul.getString("id_cargo");
                Area = consul.getString("area");
                fono = consul.getString("telefono");
                Division = consul.getString("division");
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT anexo, rut, empresa, unidad FROM eje_ges_trabajadores_anexos WHERE (empresa = '")).append(Empresa).append("') ").append("AND (rut = ").append(rut).append(") ").append("AND (unidad = '").append(Id_Unidad).append("')")));
                OutMessage.OutMessagePrint("----->Rescatando Anexo(clase Rut):\n".concat(String.valueOf(String.valueOf(sql))));
                consul2.exec(sql);
                if(consul2.next())
                    Anexo = consul2.getString("anexo");
                else
                    Anexo = "";
                Sindicato = consul.getString("sindicato");
                Cargo = consul.getString("cargo");
                Email = consul.getString("e_mail");
                Foto = consul.getString("foto");
                FecIngreso = consul.getString("fecha_ingreso");
                UbicFisica = consul.getString("ubic_fisica");
                EstadoCivil = consul.getString("estado_civil");
                Ncargas = consul.getString("num_cargas");
                SueldoBase = consul.getString("sueldo");
                Sup_Nombre = consul.getString("nom_supd");
                Sup_Rut = consul.getString("rut_supd");
                Sup_DV = consul.getString("dig_supd");
                Sup_Cargo = consul.getString("cargo_supd");
                sql = String.valueOf(String.valueOf((new StringBuilder("select e_mail,mail,anexo,unidad,foto from view_ges_rut_all WHERE (rut = ")).append(consul.getString("rut_supd")).append(")")));
                consul2.exec(sql);
                if(consul2.next())
                {
                    Sup_Email = consul2.getString("e_mail");
                    Sup_Mail = consul2.getString("mail");
                    Sup_Anexo = consul2.getString("anexo");
                    Sup_Unidad = consul2.getString("unidad");
                    Sup_Foto = consul2.getString("foto");
                }
            } else
            {
                valido = false;
            }
        }
    }

    public SimpleList getPrestamos(Connection conn, String rut)
    {
        Validar valida = new Validar();
        SimpleList simplelist = new SimpleList();
        String sql = null;
        if(conn != null)
        {
            Consulta deudas = new Consulta(conn);
            sql = String.valueOf(String.valueOf((new StringBuilder("SELECT movimiento, deuda, fecha, monto_inicial, total_cuotas, cuotas_pendientes, valor_cuota, saldo FROM eje_ges_cuentas_corrientes  WHERE (rut = ")).append(rut).append(")").append(" ORDER BY fecha")));
            deudas.exec(sql);
            if(!deudas.next())
            {
                SimpleHash simplehash1 = new SimpleHash();
                simplehash1.put("nonext", "No existe Antecedentes");
                simplelist.add(simplehash1);
            } else
            {
                SimpleHash simplehash1;
                for(; deudas.next(); simplelist.add(simplehash1))
                {
                    simplehash1 = new SimpleHash();
                    simplehash1.put("movimiento", deudas.getString("movimiento"));
                    simplehash1.put("deuda", deudas.getString("deuda"));
                    simplehash1.put("fecha", valida.validarFecha(deudas.getValor("fecha")));
                    simplehash1.put("monto", Tools.setFormatNumber(deudas.getString("monto_inicial")));
                    simplehash1.put("n_ctas", valida.validarDato(deudas.getString("total_cuotas")));
                    simplehash1.put("n_ctas_pend", Tools.setFormatNumber(valida.validarDato(deudas.getString("cuotas_pendientes"), "0")));
                    simplehash1.put("valor_cta", Tools.setFormatNumber(valida.validarDato(deudas.getString("valor_cuota"), "0")));
                    simplehash1.put("saldo", Tools.setFormatNumber(valida.validarDato(deudas.getString("saldo"), "0")));
                }

                deudas.close();
            }
        }
        return simplelist;
    }

    public SimpleList getHijos(Connection conn, String rut)
    {
        Validar valida = new Validar();
        SimpleList simplelist = new SimpleList();
        String sql = null;
        if(conn != null)
        {
            Consulta hijos = new Consulta(conn);
            sql = String.valueOf(String.valueOf((new StringBuilder("SELECT rut, nombre,DATEDIFF(month, fecha_nacim, GETDATE()) / 12 AS edad,rut_carga,fecha_nacim,sexo  FROM eje_ges_grupo_familiar  WHERE (rut =")).append(rut).append(")").append(" and parentesco = 'Hijo(a)' ORDER BY fecha_nacim")));
            hijos.exec(sql);
            int x = 0;
            SimpleHash simplehash1;
            for(; hijos.next(); simplelist.add(simplehash1))
            {
                x++;
                simplehash1 = new SimpleHash();
                simplehash1.put("count", (new Integer(x)).toString());
                simplehash1.put("rut", hijos.getString("rut_carga"));
                simplehash1.put("fecha_nacim", valida.validarFecha(hijos.getString("fecha_nacim")));
                simplehash1.put("edad", hijos.getString("edad"));
                simplehash1.put("s", hijos.getString("sexo"));
                simplehash1.put("nombre", hijos.getString("nombre"));
                simplehash1.put("fecha_nacim", valida.validarFecha(hijos.getString("fecha_nacim")));
            }

            TotaHijos = (new Integer(x)).toString();
            hijos.close();
        }
        return simplelist;
    }

    public SimpleList getBeneficios(Connection conn, String empresa)
    {
        SimpleList simplelist = new SimpleList();
        String sql = null;
        if(conn != null)
        {
            Consulta beneficios = new Consulta(conn);
            sql = String.valueOf(String.valueOf((new StringBuilder("SELECT descripcion, val_ant, val_actual  FROM view_beneficios  WHERE (empresa ='")).append(empresa).append("')")));
            beneficios.exec(sql);
            int x = 0;
            SimpleHash simplehash1;
            for(; beneficios.next(); simplelist.add(simplehash1))
            {
                x++;
                simplehash1 = new SimpleHash();
                simplehash1.put("count", (new Integer(x)).toString());
                simplehash1.put("descripcion", beneficios.getString("descripcion"));
                simplehash1.put("val_ant", beneficios.getString("val_ant"));
                simplehash1.put("val_actual", beneficios.getString("val_actual"));
            }

            beneficios.close();
        }
        return simplelist;
    }

    public String Rut;
    public String Nombres;
    public String Empresa;
    public String EmpresaDescrip;
    public String Id_Unidad;
    public String Unidad;
    public String Cargo;
    public String Id_Cargo;
    public String Division;
    public String Area;
    public String Anexo;
    public String fono;
    public String Sindicato;
    public String Login;
    public String Email;
    public String Mail;
    public String Foto;
    public String Digito_Ver;
    public String FecIngreso;
    public String FecNacim;
    public String Domicilio;
    public String AntigCargo;
    public String Sexo;
    public String UbicFisica;
    public String EstadoCivil;
    public String Ncargas;
    public String SueldoBase;
    public boolean valido;
    public String Sup_Rut;
    public String Sup_DV;
    public String Sup_Email;
    public String Sup_Mail;
    public String Sup_Anexo;
    public String Sup_Foto;
    public String Sup_Cargo;
    public String Sup_Unidad;
    public String Sup_Nombre;
    public String TotaHijos;
}