package cursos.graficos;

import highchart.ifaces.ISeriesChart;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cursos.graficos.mng.GraficosMng;
import portal.com.eje.frontcontroller.IOClaseWeb;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.output.IDataOut;
import cl.ejedigital.consultor.output.JSarrayDataOut;

public class CostosCursosArea extends ISeriesChart {
	private String tipoReporte;
	private String unidad_desc;
	public CostosCursosArea(IOClaseWeb cw) {
		super(cw);
		tipoReporte = "";
		unidad_desc = "";
	}
	
	public String getTitulo() {
		return "Comparativa de Costos por " + tipoReporte;
	}

	public String getSubTitulo() {
		Date date = new Date();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		return "Costos de cursos por períodos. Información al día "+ formato.format(date);
	}

	public String getYLabel() {
		return "Costos";
	}

	public IDataOut getXLabels() {
		DataList lista	= new DataList();
		DataFields d	= null;
		GraficosMng fg	= new GraficosMng();
		String anio		= super.cw.getReq().getParameter("periodo");
		
				if (anio.equals("0")){
					ConsultaData periodo = fg.getPeriodos();
					while(periodo.next()){
						d = new DataFields();
						d.put("axis", new Field(periodo.getInt("periodo_accion")));
						lista.add(d);
					}
				}else{
					ConsultaData meses = fg.getMeses(anio);
					while(meses.next()){
						d = new DataFields();
						d.put("axis", new Field(meses.getInt("meses")));
						lista.add(d);
					}		
		}
		
		JSarrayDataOut jq = new JSarrayDataOut(lista);
		return jq;
	}

	public IDataOut getSeries() {
		String empresa		= super.cw.getReq().getParameter("empresa");
		String unidad		= super.cw.getReq().getParameter("unidad");
		String esarea		= super.cw.getReq().getParameter("esarea");
		String periodo		= super.cw.getReq().getParameter("periodo");
		GraficosMng gm		= new GraficosMng();	
		ConsultaData unid	= gm.getUnidad(unidad);
		
		if (esarea.equals("0")){
			if(unid.next()){
				unidad_desc = unid.getString("unid_desc");
			}
			tipoReporte = "Unidad: " + unidad_desc;
			
		}else{
			tipoReporte = "Área";
		}
		
		DataList lista = new DataList();
		DataFields d = null;

			d = new DataFields();
			d.put("serie", new Field(gm.getSerieCostoEmpresa(empresa,unidad,esarea,periodo)));
			lista.add(d);

			d = new DataFields();
			d.put("serie", new Field(gm.getSerieCostoSence(empresa,unidad,esarea,periodo)));
			lista.add(d);

			d = new DataFields();
			d.put("serie", new Field(gm.getSerieCostoTotal(empresa,unidad,esarea,periodo)));
			lista.add(d);
			
		JSarrayDataOut jq = new JSarrayDataOut(lista);
		return jq;
	}

}
