package portal.com.eje.portal.roldetecterconfig;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.misc.Cronometro;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.roldetecterconfig.VoRDCParametro.TipoMiembro;
import portal.com.eje.portal.tool.AplicationsTool;
import portal.com.eje.portal.transactions.TransactionConnection;

public class RolDetecterConfigLocator implements IRolDetecterConfig {

	private static IRolDetecterConfig instance;
	private Cronometro cronometro;
	private int secondToRefresh = 60;
	
	private final String strBDPortal = AplicationsTool.getInstance().getDatabase("portal");
	
	private RolDetecterConfigLocator() {
		super();
	}
	
	public static IRolDetecterConfig getInstance() {
		return Util.getInstance(RolDetecterConfigLocator.class);
	}
	
	public void regulaDotacion(IOClaseWeb io) {
		if(debeEjecutarse()) {
			synchronized (RolDetecterConfigLocator.class) {
				if(debeEjecutarse()) {
				
					cronometro.start();

				}
			}
		}
	}
	
	private boolean debeEjecutarse() {
		boolean mustSend = false;
		
		if(cronometro == null) {
			cronometro = new Cronometro();
			mustSend = true;
		}
		else {
			mustSend = (cronometro.GetMilliseconds() >= secondToRefresh * 1000);
		}
		
		return mustSend;
	}

	@Override
	public List<VoRolDetecterConfig> getRolDetecterConfig(TransactionConnection cons) {
		return null;
	}

	@Override
	public List<VoRDCParametro> getParametros(TransactionConnection cons, int intIdParametro) {
		return getParametros(cons, intIdParametro, TipoMiembro.Todos, null);
	}

	@Override
	public List<VoRDCParametro> getParametros(TransactionConnection cons, int intIdParametro, String strOmitidos) {
		return getParametros(cons, intIdParametro, TipoMiembro.Todos, strOmitidos);
	}
	
	@Override
	public List<VoRDCParametro> getParametros(TransactionConnection cons, int intIdParametro, TipoMiembro tipo_miembro) {
		return getParametros(cons, intIdParametro, tipo_miembro, null);
	}

	@Override
	public List<VoRDCParametro> getParametros(TransactionConnection cons, int intIdParametro, TipoMiembro tipo_miembro, String strOmitidos) {

		StringBuffer strSQL = new StringBuffer();
		
		List<VoRDCParametro> rdcp = new ArrayList<VoRDCParametro>();
		
		if ("".equals(strOmitidos)){
			strOmitidos=null;
		}

		strSQL.append("select rdp.id_rdparam, rdp.rdparam, rdp.referencia, rdp.vigente \n")
		.append("from eje_wf_roldetecter_parametros rdp \n")
		.append("where rdp.id_rdparam<>0 \n");
		if (intIdParametro!=0) {
			strSQL.append("and rdp.id_rdparam=").append(intIdParametro).append(" \n");
		}
		if (tipo_miembro==TipoMiembro.Filtro){
			strSQL.append("and rdp.enfiltro=1 \n");	
		}else if (tipo_miembro==TipoMiembro.Responsable){
			strSQL.append("and rdp.enresponsable=1 \n");	
		}
		if (strOmitidos!=null){
			strSQL.append("and rdp.id_rdparam not in (").append(strOmitidos).append(") \n");
		}
		strSQL.append("order by rdp.rdparam ");	
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData(cons.getMac(), strSQL.toString());
			while (data!=null && data.next()){
				rdcp.add(new VoRDCParametro(data.getInt("id_rdparam"), data.getString("rdparam"), data.getString("referencia"), (data.getInt("vigente")==1)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rdcp;
		
	}

	@Override
	public boolean bolUpdateRDC(TransactionConnection cons, VoRolDetecterConfig voRDC, ConsultaData data) {

		StringBuffer strSQL = new StringBuffer();
		
		boolean ok = false;
		
		if (voRDC.getId()==0){
			ok = putIdRolDetecter(cons, voRDC);
		}else{
			ok = true;
		}
		
		if (ok) {
			
			int intVigente = 0;
			if (voRDC.getActivo()) {
				intVigente = 1;
			}
			
			strSQL.append("declare @id_roldetecter int, @roldetecter varchar(max), @descripcion varchar(max), @vigente int \n\n")
			
			.append("set @id_roldetecter=? \n")
			.append("set @roldetecter=? \n")
			.append("set @descripcion=? \n")
			.append("set @vigente=? \n\n")
			
			.append("update eje_wf_roldetecter set roldetecter=@roldetecter, descripcion=@descripcion, vigente=@vigente where id_roldetecter=@id_roldetecter \n\n")
			
			.append("update r set nombre=rd.roldetecter, descripcion=rd.descripcion \n")
			.append("from eje_wf_recursos r \n")
			.append("inner join eje_wf_roldetecter rd on rd.cod_recurso=r.cod_recurso \n")
			.append("where id_roldetecter=@id_roldetecter \n\n")
			
			.append("declare @data table (id_roldetecter int, tipo_miembro int, id_rdmiembro int, id_rdparam int, id_rdcampo int, codigo varchar(max), operador varchar(max), valor varchar(max), principal smallint, dummy varchar(max)) \n\n");
			while (data!=null && data.next()){
				
				int intIdRdParam = Integer.parseInt(getDataValor(data, "id_rdparam"));
				String strIdRDCampo = getDataValor(data, "id_rdcampo");
				String strCodigo = getDataValor(data, "codigo");
				String strOperador = getDataValor(data, "operador");
				String strValor = getDataValor(data, "valor");
				String strPrincipal = getDataValor(data, "principal");
				
				if (intIdRdParam==VoRDCParametro.Parametro.Ticket.getId()) {
					strIdRDCampo = strCodigo;
				}


				strSQL.append("insert into @data values (")
				.append("@id_roldetecter, ")
				.append(data.getForcedString("tipo_miembro")).append(", ")
				.append(data.getForcedString("id_rdmiembro")).append(", ")
				.append(data.getForcedString("id_rdparam")).append(", ");
				if (strIdRDCampo!=null) {
					strSQL.append(strIdRDCampo).append(", ");
				}else {
					strSQL.append("null, ");
				}
				strSQL.append("'").append(strCodigo).append("', ");
				if (strOperador!=null) {
					strSQL.append("'").append(strOperador).append("', ");
				}else {
					strSQL.append("null, ");
				}
				if (strValor!=null) {
					strSQL.append("'").append(strValor).append("', ");
				}else {
					strSQL.append("null, ");
				}
				if (strPrincipal!=null) {
					int intPrincipal = 0;
					if ("true".equals(strPrincipal)) {
						intPrincipal = 1;
					}
					strSQL.append("").append(intPrincipal).append(", ");
				}else {
					strSQL.append("null, ");
				}
				strSQL.append("null) \n");
			}

			strSQL.append("\n")
			
			.append("select @id_roldetecter=id_roldetecter from @data \n\n")

			.append("delete m \n")
			.append("from eje_wf_roldetecter_miembro m \n")
			.append("left join @data d on d.id_roldetecter=m.id_roldetecter and d.id_rdmiembro=m.id_rdmiembro \n")
			.append("where m.id_roldetecter=@id_roldetecter and d.id_roldetecter is null \n\n")
			
			.append("insert into eje_wf_roldetecter_miembro (id_roldetecter, id_rdmiembro, vigente) \n")
			.append("select distinct d.id_roldetecter, d.id_rdmiembro, vigente=1 \n")
			.append("from @data d \n")
			.append("left join eje_wf_roldetecter_miembro m on m.id_roldetecter=d.id_roldetecter and m.id_rdmiembro=d.id_rdmiembro \n")
			.append("where m.id_roldetecter is null \n\n")
			
			.append("delete f \n")
			.append("from eje_wf_roldetecter_miembro_filtros f \n")
			.append("left join @data d on d.id_roldetecter=f.id_roldetecter and d.id_rdmiembro=f.id_rdmiembro and d.id_rdparam=f.id_rdparam and d.id_rdcampo=f.id_rdcampo and d.codigo=f.codigo \n")
			.append("where f.id_roldetecter=@id_roldetecter and d.id_roldetecter is null \n\n")
			
			.append("delete r \n")
			.append("from eje_wf_roldetecter_miembro_responsables r \n")
			.append("left join @data d on d.id_roldetecter=r.id_roldetecter and d.id_rdmiembro=r.id_rdmiembro and d.id_rdparam=r.id_rdparam and d.codigo=r.codigo \n")
			.append("where r.id_roldetecter=@id_roldetecter and d.id_roldetecter is null \n\n")
			
			.append("update f set operador=d.operador \n")
			.append("from eje_wf_roldetecter_miembro_filtros f \n")
			.append("inner join @data d on d.id_roldetecter=f.id_roldetecter and d.id_rdmiembro=f.id_rdmiembro and d.id_rdparam=f.id_rdparam and d.valor=f.valor \n")
			.append("where d.tipo_miembro=1 \n\n")
			
			.append("insert into eje_wf_roldetecter_miembro_filtros (id_roldetecter, id_rdmiembro, id_rdparam, id_rdcampo, codigo, operador, valor) \n")
			.append("select d.id_roldetecter, d.id_rdmiembro, d.id_rdparam, d.id_rdcampo, d.codigo, d.operador, d.valor \n")
			.append("from @data d \n")
			.append("left join eje_wf_roldetecter_miembro_filtros f on f.id_roldetecter=d.id_roldetecter and f.id_rdmiembro=d.id_rdmiembro and f.id_rdparam=d.id_rdparam and f.valor=d.valor \n")
			.append("where d.tipo_miembro=1 and f.id_roldetecter is null \n\n")
			
			.append("update r set principal=d.principal \n")
			.append("from eje_wf_roldetecter_miembro_responsables r \n")
			.append("inner join @data d on r.id_roldetecter=d.id_roldetecter and r.id_rdmiembro=d.id_rdmiembro and r.id_rdparam=d.id_rdparam and r.codigo=d.codigo \n")
			.append("where d.tipo_miembro=2 \n\n") 
			
			.append("insert into eje_wf_roldetecter_miembro_responsables (id_roldetecter, id_rdmiembro, id_rdparam, codigo, principal) \n")
			.append("select d.id_roldetecter, d.id_rdmiembro, d.id_rdparam, d.codigo, d.principal \n")
			.append("from @data d \n")
			.append("left join eje_wf_roldetecter_miembro_responsables r on r.id_roldetecter=d.id_roldetecter and r.id_rdmiembro=d.id_rdmiembro and r.id_rdparam=d.id_rdparam and r.codigo=d.codigo \n")
			.append("where d.tipo_miembro=2 and r.id_roldetecter is null \n\n")
			
			.append("select resultado='ok' ");

			Object[] params = {voRDC.getId(), voRDC.getNombre(), voRDC.getDescripcion(), intVigente};
			
			try {
				ConsultaTool.getInstance().getData(cons.getMac(), strSQL.toString(), params);
				ok = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}

		return ok;
	}
	
	@Override
	public boolean bolDeleteRDC(TransactionConnection cons, VoRolDetecterConfig voRDC) {
		
		StringBuffer strSQL = new StringBuffer();
		
		boolean ok = false;
		
		strSQL.append("declare @id_roldetecter int \n\n")
		
		.append("set @id_roldetecter = ").append(voRDC.getId()).append(" \n\n")
		
		.append("delete r \n")
		.append("from eje_wf_recursos r \n")
		.append("inner join eje_wf_roldetecter rd on rd.cod_recurso=r.cod_recurso \n")
		.append("where rd.id_roldetecter=@id_roldetecter \n\n")

		.append("delete eje_wf_roldetecter where id_roldetecter=@id_roldetecter \n\n")

		.append("select resultado='ok' \n");
		
		try {
			ConsultaTool.getInstance().getData(cons.getMac(), strSQL.toString());
			ok = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ok;
		
	}
	
	private boolean putIdRolDetecter(TransactionConnection cons, VoRolDetecterConfig voRDC){
		
		StringBuffer strSQL = new StringBuffer();
		
		boolean ok = false;
		
		strSQL.append("declare @cod_recurso int, @nombre varchar(max), @descripcion varchar(max), @vigente smallint, @path varchar(max) \n\n")
		
		.append("set @nombre=? \n")
		.append("set @descripcion=? \n")
		.append("set @path=? \n")
		.append("set @vigente=? \n\n")
		
		.append("insert into eje_wf_recursos (nombre, descripcion, path, id_tiporecurso, tipo_recurso, externo) \n")
		.append("values (@nombre, @descripcion, @path, 7, 'RolDetecterConfig', 0) \n")
		.append("set @cod_recurso = @@identity \n\n")
		
		.append("insert into eje_wf_roldetecter (cod_recurso, roldetecter, descripcion, vigente) values (@cod_recurso, @nombre, @descripcion, @vigente) \n\n")
		
		.append("select id_roldetecter=@@identity, cod_recurso=@cod_recurso, path=@path \n");
		
		Object param[] = {
				voRDC.getNombre(),
				voRDC.getDescripcion(),
				"macc.controller.roldetecter.RDConfig",
				voRDC.getActivo()
		};
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData(cons.getMac(), strSQL.toString(), param);
			while (data!=null && data.next()){
				voRDC.setId(Validar.getInstance().validarInt(data.getForcedString("id_roldetecter")));
				voRDC.setCodRecurso(Validar.getInstance().validarInt(data.getForcedString("cod_recurso")));
				voRDC.setPath(data.getForcedString("path"));
				ok = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ok;
	}

	@Override
	public ConsultaData getFiltros(TransactionConnection cons, VoRolDetecterConfig voRDC) {
		return getFiltros(cons, 0, voRDC.getId());
	}
	
	private ConsultaData getFiltros(TransactionConnection cons, int intIdMiembro, int intIdRolDetecter) {

		StringBuffer strSQL = new StringBuffer();
		
		strSQL.append("select d.id_rdmiembro, d.id_rdparam, p.rdparam, c.jndi, c.tabla, campo=coalesce(c.campo, p.referencia), c.condicion, d.id_rdcampo, \n")
		.append("d.codigo, \n") 
		.append("descripcion= \n ")
		.append(" 			 case when d.id_rdparam = ").append(VoRDCParametro.Parametro.Ticket.getId()).append(" then c2.rdcampo \n")
		.append("				  when d.id_rdparam = ").append(VoRDCParametro.Parametro.Cargo.getId()).append("  then d.codigo \n")
		.append(" 			 	  when d.id_rdparam <> ").append(VoRDCParametro.Parametro.Ticket.getId()).append(" then coalesce(c.rdcampo, coalesce(coalesce(p4.unid_desc, p5.descrip), p6.nombre)) \n")
		.append(" 			 end, \n")
		.append("operador=coalesce(d.operador, '*'), d.valor \n")
 		.append("from @@mac..eje_wf_roldetecter_miembro_filtros d \n")
		.append("left join @@mac..eje_wf_roldetecter_parametros p on p.id_rdparam=d.id_rdparam \n")
		.append("left join @@mac..eje_wf_roldetecter_campos c on c.id_rdcampo=d.id_rdcampo \n")
		.append("left join (select distinct unid_id, unid_desc from eje_ges_unidades) p4 on d.id_rdparam=4 and p4.unid_id=d.codigo \n")
		.append("left join (select distinct cargo, descrip from eje_ges_cargos) p5 on d.id_rdparam=5 and p5.cargo=d.codigo \n")
		.append("left join (select distinct rut, nombre from eje_ges_trabajador) p6 on d.id_rdparam=6 and cast(p6.rut as varchar)=d.codigo \n")
		.append("left join @@mac..eje_wf_roldetecter_campos c2 on convert(varchar(100),c2.id_rdcampo)=d.codigo ")
		.append("where d.id_roldetecter=").append(intIdRolDetecter).append(" \n");
		if(intIdMiembro!=0) {
			strSQL.append("and d.id_rdmiembro=").append(intIdMiembro).append(" \n");
		}
		strSQL.append("order by d.id_rdmiembro, d.id_rdparam \n"); 

		try {
			
			String bdMac = AplicationsTool.getInstance().getDatabaseNameFromUrl(cons.getMac());
			String sql = strSQL.toString().replaceAll("@@mac", bdMac);
			
			return ConsultaTool.getInstance().getData(cons.getPortal(), sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}

	@Override
	public ConsultaData getResponsables(TransactionConnection cons, VoRolDetecterConfig voRDC) {
		return getResponsables(cons, 0, voRDC.getId());
	}
	
	private ConsultaData getResponsables(TransactionConnection cons, int intIdMiembro, int intIdRolDetecter) {
		
		StringBuffer strSQL = new StringBuffer();

		
		
		strSQL.append("select d.id_rdmiembro, d.id_rdparam, p.rdparam, d.codigo, d.principal, \n");
		strSQL.append("descripcion=( \n");
		strSQL.append("	case d.id_rdparam \n");
		strSQL.append("	when 20 then c.rdcampo \n");
		strSQL.append("	when 5 then d.codigo \n");
		strSQL.append("	else coalesce(coalesce(p4.unid_desc, p5.descrip), p6.nombre) \n");
		strSQL.append("	end \n");
		strSQL.append(") \n"); 
		strSQL.append("from @@mac..eje_wf_roldetecter_miembro_responsables d \n"); 
		strSQL.append("left join @@mac..eje_wf_roldetecter_parametros p on p.id_rdparam=d.id_rdparam \n"); 
		strSQL.append("left join @@mac..eje_wf_roldetecter_campos c on convert(varchar(100),c.id_rdcampo)=d.codigo \n"); 
		strSQL.append("left join (select distinct unid_id, unid_desc from eje_ges_unidades) p4 on d.id_rdparam=4 and p4.unid_id=d.codigo \n"); 
		strSQL.append("left join (select distinct cargo, descrip from eje_ges_cargos) p5 on d.id_rdparam=5 and p5.cargo=d.codigo \n"); 
		strSQL.append("left join (select distinct rut, nombre from  eje_ges_trabajador) p6 on d.id_rdparam=6 and cast(p6.rut as varchar)=d.codigo \n"); 
		strSQL.append("where d.id_roldetecter= ?  \n"); 
		if(intIdMiembro!=0) {
			strSQL.append(" and d.id_rdmiembro=").append(intIdMiembro).append(" \n ");
		}
		strSQL.append(" order by d.id_rdmiembro, d.id_rdparam \n"); 
		
		try {
			
			String bdMac = AplicationsTool.getInstance().getDatabaseNameFromUrl(cons.getMac());
			
			String sql = strSQL.toString().replaceAll("@@mac", bdMac);
			Object[] params = {intIdRolDetecter};
			return ConsultaTool.getInstance().getData(cons.getPortal(), sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	

	@Override
	public List<VoRDCMiembro> getMiembros(TransactionConnection cons, int intIdRolDetecter) {

		StringBuffer strSQL = new StringBuffer();

		List<VoRDCMiembro> miembros = new ArrayList<VoRDCMiembro>();
		
		strSQL.append("select id_rdmiembro \n")
		.append("from eje_wf_roldetecter_miembro \n")
		.append("where id_roldetecter=").append(intIdRolDetecter).append(" \n");
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData(cons.getMac(), strSQL.toString());
			while (data!=null && data.next()) {
				
				List<VoRDCFiltro> filtros = new ArrayList<VoRDCFiltro>();
				List<VoRDCResponsable> responsables = new ArrayList<VoRDCResponsable>();

				int intIdMiembro = data.getInt("id_rdmiembro");
				ConsultaData dtaFiltros = getFiltros(cons, intIdMiembro, intIdRolDetecter);
				while (dtaFiltros!=null && dtaFiltros.next()) {
					List<VoRDCParametro> parametro = RolDetecterConfigLocator.getInstance().getParametros(cons, dtaFiltros.getInt("id_rdparam"));
					filtros.add(new VoRDCFiltro(parametro.get(0), dtaFiltros.getForcedString("jndi"), dtaFiltros.getForcedString("tabla"), dtaFiltros.getForcedString("campo"), dtaFiltros.getForcedString("condicion"), dtaFiltros.getForcedString("codigo"), dtaFiltros.getString("descripcion"), dtaFiltros.getString("operador"), dtaFiltros.getString("valor")));
				}
				ConsultaData dtaResponsables = getResponsables(cons, intIdMiembro, intIdRolDetecter);
				while (dtaResponsables!=null && dtaResponsables.next()) {
					boolean bolPrincipal = (dtaResponsables.getInt("principal")==1);
					List<VoRDCParametro> parametro = RolDetecterConfigLocator.getInstance().getParametros(cons, dtaResponsables.getInt("id_rdparam"));
					responsables.add(new VoRDCResponsable(parametro.get(0), null, dtaResponsables.getForcedString("codigo"), dtaResponsables.getString("descripcion"), bolPrincipal));
				}
				miembros.add(new VoRDCMiembro(intIdMiembro, filtros, responsables));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return miembros;
	}

	private String getDataValor(ConsultaData data, String strCampo){
		String strRetorno = null;
		if (data.existField(strCampo)){
			strRetorno = data.getForcedString(strCampo);
		}
		return strRetorno;
	}

}
