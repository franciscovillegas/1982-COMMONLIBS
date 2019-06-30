package highchart;


import highchart.ifaces.IBarrasNegativasChart;
import highchart.ifaces.IDualAxesLineAndColumn;
import highchart.ifaces.ILineTime;
import highchart.ifaces.ISeriesChart;
import highchart.ifaces.ISpiderWeb;
import highchart.ifaces.IStackedBar;
import mis.highchart.graphs.GraphsTool;
import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.output.EscapeJavascript;
import cl.ejedigital.consultor.output.JSonDataOut;

public class SGraphsSeguro extends AbsClaseWeb {

	public SGraphsSeguro(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doGet() throws Exception {
		DataList lista = new DataList();
		
		DataFields d = null;
		String gType = super.getIoClaseWeb().getParamString("gtype", null);

		if("Serie".equals(gType)) {
			d = getSerieGraph();
		}
		else if("NegativeBars".equals(gType)) {
			d = getBarrasNegativasGraph();
		}
		else if("StackedBar".equals(gType)) {
			d = getStackedBarGraph();
		}
		else if("DualAxesLineAndColumn".equals(gType)){
			d = getDualAxesLinesAndColumn();
		}
		else if("LineTime".equals(gType)){
			d = getLineTime();
		}
		else if("SpiderWeb".equals(gType)){
			d = getSpiderWeb();
		}

		lista.add(d);
		JSonDataOut javaScript = new JSonDataOut(lista);
		javaScript.setEscape(new EscapeJavascript());
		super.getIoClaseWeb().retTexto(javaScript.getListData());	
	}

	@Override
	public void doPost() throws Exception {
		System.out.println("Post");
		doGet();
	}
	
	
	/**
	 * Muy Complejo de usar, toda la lógica se envía a la zona de construcción
	 * @since 2015-08-04
	 * @deprecated
	 * @see highchart.easyifaces EasyGraphs
	 * */
	private DataFields getLineTime() {
		GraphsTool tool = new GraphsTool();
		
		ILineTime sChart = (ILineTime)tool.getImplementacion(super.getIoClaseWeb(),ISeriesChart.class);
		DataFields d = new DataFields();
		
		if(sChart != null) {
			d.put("titulo",new Field(sChart.getTitulo()));
			d.put("yLabel",new Field(sChart.getYLabel()));
			d.put("series",new Field(sChart.getSeries()));
			d.put("titulo",new Field(sChart.getTitulo()));
		}
		
		return d;
	}
	
	/**
	 * Muy Complejo de usar, toda la lógica se envía a la zona de construcción
	 * @since 2015-08-04
	 * @deprecated
	  * @see highchart.easyifaces EasyGraphs 
	 * */
	private DataFields getSerieGraph() {
		GraphsTool tool = new GraphsTool();
		
		ISeriesChart sChart = (ISeriesChart)tool.getImplementacion(super.getIoClaseWeb(),ISeriesChart.class);
		DataFields d = new DataFields();
		
		if(sChart != null) {
			d.put("titulo",new Field(sChart.getTitulo()));
			d.put("subtitulo",new Field(sChart.getSubTitulo()));
			d.put("yLabel",new Field(sChart.getYLabel()));
			d.put("xLabel",new Field(sChart.getXLabels()));
			d.put("series",new Field(sChart.getSeries()));
			d.put("titulo",new Field(sChart.getTitulo()));
		}
		
		return d;
	}
	
	/**
	 * Muy Complejo de usar, toda la lógica se envía a la zona de construcción
	 * @since 2015-08-04
	 * @deprecated
	 * @see highchart.easyifaces EasyGraphs 
	 * */
	private DataFields getSpiderWeb() {
		GraphsTool tool = new GraphsTool();
		
		ISpiderWeb sChart = (ISpiderWeb)tool.getImplementacion(super.getIoClaseWeb(),ISpiderWeb.class);
		DataFields d = new DataFields();
		
		if(sChart != null) {
			d.put("titulo",new Field(sChart.getTitulo()));
			d.put("subtitulo",new Field(sChart.getSubTitulo()));
			d.put("series",new Field(sChart.getSeries()));
			d.put("categorias",new Field(sChart.getCategorias()));
		}
		
		return d;
	}
	
	/**
	 * Muy Complejo de usar, toda la lógica se envía a la zona de construcción
	 * @since 2015-08-04
	 * @deprecated
	 * @see highchart.easyifaces EasyGraphs 
	 * */
	private DataFields getBarrasNegativasGraph() {
		GraphsTool tool = new GraphsTool();
		
		IBarrasNegativasChart sChart = (IBarrasNegativasChart)tool.getImplementacion(super.getIoClaseWeb(),IBarrasNegativasChart.class);
		DataFields d = new DataFields();
		
		if(sChart != null) {

			d.put("titulo",new Field(sChart.getTitulo()));
			d.put("subtitulo",new Field(sChart.getSubTitulo()));
			d.put("categorias",new Field(sChart.getCategorias()));
			d.put("series",new Field(sChart.getSeries()));
			d.put("absoluteRange",new Field(sChart.getRangeAbsolute()));
		}
		
		return d;
	}
	
	/**
	 * Muy Complejo de usar, toda la lógica se envía a la zona de construcción
	 * @since 2015-08-04
	 * @deprecated
	 * @see highchart.easyifaces EasyGraphs 
	 * */
	private DataFields getStackedBarGraph() {
		GraphsTool tool = new GraphsTool();
		
		IStackedBar sChart = (IStackedBar)tool.getImplementacion(super.getIoClaseWeb(),IStackedBar.class);
		DataFields d = new DataFields();
		
		if(sChart != null) {

			d.put("titulo",new Field(sChart.getTitulo()));
			d.put("subtitulo",new Field(sChart.getSubTitulo()));
			d.put("categorias",new Field(sChart.getCategorias()));
			d.put("series",new Field(sChart.getSeries()));
			d.put("xLabel",new Field(sChart.getXLabel()));
		}
		
		return d;
	}
	
	/**
	 * Muy Complejo de usar, toda la lógica se envía a la zona de construcción
	 * @since 2015-08-04
	 * @deprecated
	 * @see highchart.easyifaces EasyGraphs
	 * */
	private DataFields getDualAxesLinesAndColumn() {
		GraphsTool tool = new GraphsTool();
		
		IDualAxesLineAndColumn sChart = (IDualAxesLineAndColumn)tool.getImplementacion(super.getIoClaseWeb(),IDualAxesLineAndColumn.class);
		DataFields d = new DataFields();
		
		if(sChart != null) {

			d.put("titulo",new Field(sChart.getTitulo()));
			d.put("subtitulo",new Field(sChart.getSubTitulo()));
			d.put("categorias",new Field(sChart.getCategorias()));
			d.put("series",new Field(sChart.getSeries()));
			d.put("xLabel",new Field(sChart.getXLabel()));
			d.put("yLabelSecundario",new Field(sChart.getYLabelSecundario()));
			d.put("yLabel",new Field(sChart.getYLabel()));
		}
		
		return d;
	}
	
	
	
	
}
