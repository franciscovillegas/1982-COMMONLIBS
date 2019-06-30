package mis.highchart.graphs;

import highchart.ifaces.ISeriesChart;
import java.util.Calendar;
import portal.com.eje.frontcontroller.IOClaseWeb;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.output.IDataOut;
import cl.ejedigital.consultor.output.JSarrayDataOut;

public class CostosCursos extends ISeriesChart {
	private final int anios;
	private final int anioActual;
	private final int anioDesde;
	
	public CostosCursos(IOClaseWeb cw) {
		super(cw);
		Calendar c1 = Calendar.getInstance();
		anios = 6;
		anioActual = c1.get(Calendar.YEAR);
		anioDesde = c1.get(Calendar.YEAR)-anios;
	}
	
	public String getTitulo() {
		// TODO Auto-generated method stub
		return "Costos de Cursos por Período";
	}

	public String getSubTitulo() {
		// TODO Auto-generated method stub
		return "Costos por cursos";
	}

	public String getYLabel() {
		// TODO Auto-generated method stub
		return "Cursos";
	}

	public IDataOut getXLabels() {
		DataList lista = new DataList();
		DataFields d = null;
		JSarrayDataOut jq = null;
		
		GraficosMng fg = new GraficosMng();
		ConsultaData periodo = fg.getPeriodos();
		
		if (periodo!=null){
			while(periodo.next()){
				d = new DataFields();
				d.put("axis", new Field(periodo.getInt("periodo_accion")));
				lista.add(d);
			}
			jq = new JSarrayDataOut(lista);	
		}

		return jq;
	}

	public IDataOut getSeries() {
		GraficosMng fg = new GraficosMng();

		DataList lista = new DataList();
		DataFields d = null;
		

			d = new DataFields();
			d.put("serie", new Field(fg.getSerieCostoEmpresa()));
			lista.add(d);

		

			d = new DataFields();
			d.put("serie", new Field(fg.getSerieCostoSence()));
			lista.add(d);

		
		JSarrayDataOut jq = new JSarrayDataOut(lista);
		return jq;
	}

}
