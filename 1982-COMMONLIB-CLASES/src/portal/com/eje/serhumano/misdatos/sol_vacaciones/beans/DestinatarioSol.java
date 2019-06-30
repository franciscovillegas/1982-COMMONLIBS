// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 24-10-2006 18:03:06
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   DestinatarioSol.java

package portal.com.eje.serhumano.misdatos.sol_vacaciones.beans;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;
import java.io.PrintStream;
import java.sql.Connection;

// Referenced classes of package portal.com.eje.serhumano.misdatos.sol_vacaciones.beans:
//            Rut, verVacaciones, verLicencias

public class DestinatarioSol
{

    public DestinatarioSol()
    {
        rut_dest = "";
        empresa_dest = "";
        unidad_dest = "";
        nivel_dest = 0;
        vaca_desde = "";
        vaca_hasta = "";
        licencia_desde = "";
        licencia_hasta = "";
        noDisponible = "";
        valido = false;
        deVaca = false;
        conLicencia = false;
    }

    public DestinatarioSol(Connection origen, Connection destino, String rut)
    {
        rut_dest = "";
        empresa_dest = "";
        unidad_dest = "";
        nivel_dest = 0;
        vaca_desde = "";
        vaca_hasta = "";
        licencia_desde = "";
        licencia_hasta = "";
        noDisponible = "";
        conOrigen = origen;
        conDestino = destino;
        Rut datosRut = new Rut(conDestino, rut);
        System.out.println("---->rut: ".concat(String.valueOf(String.valueOf(rut))));
        if(datosRut.valido)
        {
            empresa_dest = datosRut.Empresa_id;
            unidad_dest = datosRut.Unidad_id;
            nivel_dest = datosRut.Unidad_nivel;
        }
        rut_dest = RescataDest(rut);
        if(rut_dest == null)
            rut_dest = "NN";
    }

    private String RescataDest(String rut)
    {
        String rut_sup = SupDirecto(rut);
        String rutRetornar = "";
        noDisponible = rut_sup;
        int nivel_jefe = 0;
        if(!rut_sup.equals("-1"))
        {
            if(Disponible(rut_sup))
            {
                System.out.println("---->Sup Directo disponible<-------");
                rutRetornar = rut_sup;
            } else
            {
                rutRetornar = "JND";
            }
        } else
        {
            OutMessage.OutMessagePrint("---->No tiene sup Directo<-------");
            rutRetornar = "SJ";
        }
        return rutRetornar;
    }

    private String SupDirecto(String rut)
    {
        String valor = "";
        Consulta supdirecto = new Consulta(conDestino);
        if(conDestino != null)
        {
            String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT rut,rut_supdirecto,dig_supdirecto,cargo_supdirecto, nom_supdirecto FROM eje_ges_supervisor WHERE (rut = ")).append(rut).append(")")));
            supdirecto.exec(sql);
            if(supdirecto.next())
                valor = supdirecto.getString("rut_supdirecto");
            else
                valor = "-1";
        } else
        {
            OutMessage.OutMessagePrint("en Class SaldoVacaciones : Conexion es null ");
        }
        supdirecto.close();
        return valor;
    }

    private String ParDisponible(String rut, String empresa, int nivel_jefe)
    {
        String valor = "-1";
        String sql = "";
        String sql2 = "";
        System.out.println("---->Nivel del Jefe en ParDisponible: ".concat(String.valueOf(String.valueOf(nivel_jefe))));
        if(nivel_jefe > 0)
        {
            Consulta unidades = new Consulta(conDestino);
            Consulta responsables = new Consulta(conDestino);
            if(conDestino != null)
            {
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT compania, nodo_id, nodo_nivel FROM eje_ges_workflow_jerarquia WHERE (compania = '")).append(empresa_dest).append("') ").append("AND (nodo_nivel = ").append(nivel_jefe).append(")")));
                OutMessage.OutMessagePrint("---->Buscando Unidades del nivel del Jefe:\n".concat(String.valueOf(String.valueOf(sql))));
                unidades.exec(sql);
                do
                {
                    if(!unidades.next())
                        break;
                    sql2 = String.valueOf(String.valueOf((new StringBuilder("SELECT empleados.emp_rut, empleados.emp_unidad, empleados.emp_cargo, estamentos.estamento FROM eje_ges_workflow_empleados empleados INNER JOIN eje_ges_workflow_estamentos_cargos estamentos ON empleados.emp_cargo = estamentos.cargo WHERE (empleados.emp_unidad = '")).append(unidades.getString("nodo_id")).append("') ").append("AND (estamentos.estamento = 'EP' ").append("OR estamentos.estamento = 'EN')")));
                    responsables.exec(sql2);
                    valor = RecorreResponsables(responsables);
                    if(valor.equals("-1"))
                        continue;
                    System.out.println("---->Se encontro un Par disponible: ".concat(String.valueOf(String.valueOf(valor))));
                    break;
                } while(true);
                if(valor.equals("-1"))
                {
                    nivel_jefe--;
                    System.out.println("---->Subiendo un nivel para seguir buscando nivel= ".concat(String.valueOf(String.valueOf(nivel_jefe))));
                    valor = ParDisponible(rut, empresa, nivel_jefe);
                }
            } else
            {
                OutMessage.OutMessagePrint("en Class DestinatarioSol : Conexion es null ");
            }
            unidades.close();
            responsables.close();
        } else
        {
            valor = "ultimonivel";
        }
        System.out.println("========---->Par Disponible Retornado: ".concat(String.valueOf(String.valueOf(valor))));
        return valor;
    }

    private boolean Disponible(String rut)
    {
        boolean disponible = true;
        verVacaciones vacaciones = new verVacaciones(conOrigen, rut);
        if(vacaciones.deVacaciones())
        {
            OutMessage.OutMessagePrint("---->De Vacaciones");
            deVaca = true;
            vaca_desde = vacaciones.getInicioVaca();
            vaca_hasta = vacaciones.getFinVaca();
        } else
        {
            verLicencias licencias = new verLicencias(conOrigen, rut);
            if(licencias.conLicencia())
            {
                OutMessage.OutMessagePrint("---->con Licencia");
                conLicencia = true;
                licencia_desde = licencias.getInicioLicencia();
                licencia_hasta = licencias.getFinLicencia();
            }
        }
        if(deVaca || conLicencia)
            disponible = false;
        else
            disponible = true;
        deVaca = false;
        conLicencia = false;
        return disponible;
    }

    private boolean BuscaUnidades(String unidad, int nivel)
    {
        Consulta unidades = new Consulta(conDestino);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT compania, nodo_id, nodo_nivel FROM eje_ges_workflow_jerarquia WHERE (compania = '")).append(empresa_dest).append("') ").append("AND (nodo_nivel = ").append(nivel).append(")")));
        unidades.exec(sql);
        return unidades.next();
    }

    private String RecorreResponsables(Consulta rst)
    {
        String valor = "-1";
        System.err.println("***********Recorriendo Responsables");
        do
        {
            if(!rst.next())
                break;
            if(!Disponible(rst.getString("emp_rut")))
                continue;
            valor = rst.getString("emp_rut");
            break;
        } while(true);
        return valor;
    }

    public String getRutdest()
    {
        return rut_dest;
    }

    public boolean deVacaciones()
    {
        return deVaca;
    }

    public String getInicioVacaciones()
    {
        return vaca_desde;
    }

    public String getFinVacaciones()
    {
        return vaca_hasta;
    }

    public boolean conLicenciaMedica()
    {
        return conLicencia;
    }

    public String getInicioLicencia()
    {
        return licencia_desde;
    }

    public String getFinLicencia()
    {
        return licencia_hasta;
    }

    public String getJefeNoDisponible()
    {
        return noDisponible;
    }

    private String rut_dest;
    private String empresa_dest;
    private String unidad_dest;
    private int nivel_dest;
    private boolean deVaca;
    private String vaca_desde;
    private String vaca_hasta;
    private boolean conLicencia;
    private String licencia_desde;
    private String licencia_hasta;
    private String noDisponible;
    private Connection conOrigen;
    private Connection conDestino;
    public boolean valido;
}