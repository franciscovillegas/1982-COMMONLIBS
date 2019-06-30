package highchart.multipleifaces;

import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.def.JsonObject;
import cl.ejedigital.consultor.def.JsonObjects;
import highchart.multipleifaces.component.EasyChart;
import highchart.multipleifaces.component.EasyColor;
import highchart.multipleifaces.component.EasyLegend;
import highchart.multipleifaces.component.EasyPane;
import highchart.multipleifaces.component.EasyPlotOptions;
import highchart.multipleifaces.component.EasyPlotOptionsSeries;
import highchart.multipleifaces.component.EasySerie;
import highchart.multipleifaces.component.EasySeries;
import highchart.multipleifaces.component.EasyTitle;
import highchart.multipleifaces.component.EasyToolTip;
import highchart.multipleifaces.component.EasyXAxis;
import highchart.multipleifaces.component.EasyYAxi;
import highchart.multipleifaces.component.EasyYAxis;

abstract class EasyGraph extends JsonObject {
	private boolean categoriasModificadas;
	private boolean seriesYModificadas;
	private boolean seriesModificadas;
	/**
	 * 
	 */
	private static final long serialVersionUID = -3037669612231794386L;
	protected EasyChart chart;
	protected EasyTitle title;
	protected EasyTitle subtitle;
	protected EasyColor color;
	protected EasyPane pane;
	protected EasyXAxis xAxis;
	protected EasyYAxis yAxis;
	protected EasyPlotOptions plotOptions;
	protected EasyPlotOptionsSeries plotOptionsSeries;
	protected EasyToolTip tooltip;
	protected EasyLegend legend;
	protected EasySeries series;
	
	public EasyGraph() {
		inicioGeneral();
		iniciaGrafico();
	}
	
	protected void inicioGeneral() {
		chart = new EasyChart();
		title = new EasyTitle();
		subtitle = new EasyTitle();
		color = new EasyColor();
		pane = new EasyPane();
		xAxis = new EasyXAxis();
		yAxis = new EasyYAxis();
		plotOptions = new EasyPlotOptions();
		plotOptionsSeries = new EasyPlotOptionsSeries();
		tooltip = new EasyToolTip();
		legend = new EasyLegend();
		series = new EasySeries();
		
		this.put("chart", chart);
		this.put("title", title);
		this.put("subtitle", subtitle);
		this.put("color", color);
		this.put("pane", pane);
		this.put("xAxis", xAxis);
		this.put("yAxis", yAxis);
		this.put("plotOptions", plotOptions);
		this.put("plotOptionsSeries", plotOptionsSeries);
		this.put("tooltip", tooltip);
		this.put("legend", legend);
		this.put("series", series);
		
	}
	
	public void addPropChart(String key, Object value) {
		if(value != null) chart.put(key, value);
	}

	public void addPropTitle(String key, Object value) {
		if(value != null) title.put(key, value);
	}
	
	public void addPropSubtitle(String key, Object value) {
		if(value != null) subtitle.put(key, value);
	}
	
	public void addPropColor(String key, Object value) {
		if(value != null) color.put(key, value);
	}
	
	public void addPropPane(String key, Object value) {
		if(value != null) pane.put(key, value);
	}
	
	public void addPropXAxis(String key, Object value) {
		if(value != null) xAxis.put(key, value);
	}
	
	public void addPropPlotOptions(String key, Object value) {
		if(value != null) plotOptions.put(key, value);
	}
	
	public void addPropPlotOptionsSeries(String key, Object value) {
		if(value != null) plotOptionsSeries.put(key, value);
	}
	
	public void addPropTooltip(String key, Object value) {
		if(value != null) tooltip.put(key, value);
	}
	
	public void addPropLegend(String key, Object value) {
		if(value != null) legend.put(key, value);
	}
	
	public void addSerie(EasySerie serie) {
		if(serie != null)  {
			if(!seriesModificadas) {
				seriesModificadas = true;
				series.clear();
			}
			
			if( serie.sizeValores() < sizeCategorias()) {
				throw new RuntimeException("No puedes agregar una serie con menos valores que el total de categoria.");
			}
			else if( serie.sizeValores() > sizeCategorias()) {
				throw new RuntimeException("No puedes agregar una serie con más valores que el total de categorias.");
			}
			else {
				series.add(serie);	
			}
		}
	}
	
	public void addyAxis(EasyYAxi yAxi) {
		if(yAxi != null)  {
			if(!seriesYModificadas) {
				seriesYModificadas = true;
				yAxis.clear();
			}
			
			yAxis.add(yAxi);
		}
	}
	
	protected abstract int getMinCategorias();
	
	protected abstract void iniciaGrafico();
	
	public void setTitulo(String titulo) {
		addPropTitle("text", titulo);
	}
	
	public void setSubtitulo(String subtitulo) {
		addPropSubtitle("text", subtitulo);
	}
	
	/**/
	public void setYLabel() {
		
	}
	
	public void setXLabel() {
		
	}
	
	public void setXLabel(String label) {
		JsonObject oTitle = new JsonObject();
		oTitle.put("text", label);
		
		addPropXAxis("title", oTitle);
	}
	
	public void addCategoria(String categoria) {
		if(!categoriasModificadas) {
			categoriasModificadas = true;
			((JsonObjects)((Field)xAxis.get("categories")).getObject()).clear();
		}
		
		JsonObjects categorias = ((JsonObjects)((Field)xAxis.get("categories")).getObject());
		categorias.add(categoria);
	}
	
	public int sizeCategorias() {
		if( ((Field)xAxis.get("categories")) != null) {
			int cantCategorias = ((JsonObjects)((Field)xAxis.get("categories")).getObject()).size();
			return cantCategorias;
		}
		else {
			return 0;
		}
	}

	public String getDefinition() {
		
		
		if( sizeCategorias() < getMinCategorias()) {
			throw new RuntimeException("Cantidad de categorias menor a la cantidad mínima (categorias:"+ xAxis.size() + "< " + getMinCategorias()+ ")");
		}
		else {
			return this.getListData();	
		}
		
	}
	
 
}
