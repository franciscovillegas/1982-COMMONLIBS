package highchart.multipleifaces;

import cl.ejedigital.consultor.def.JsonObject;
import cl.ejedigital.consultor.def.JsonObjects;
import highchart.easyifaces.component.EasySerieY;
import highchart.multipleifaces.component.EasySerie;

public final class EasyBasicMultipleAxes extends EasyGraph {

	@Override
	protected void iniciaGrafico() {
		
		addPropTitle("text", "Titulo:" + String.valueOf(this.getClass()));
		addPropTitle("x", -80);
		
		addPropSubtitle("text", "Subtitulo:" + String.valueOf(this.getClass()));
		addPropSubtitle("x", -80);
		
		addPropPane("size", "120%");
		
		JsonObjects categorias = new JsonObjects();
		categorias.add("Categoria 1")
				  .add("Categoria 2")
				  .add("Categoria 3")
				  .add("Categoria 4");
				  
		addPropXAxis("categories", categorias);
		addPropXAxis("tickmarkPlacement", "on");
		addPropXAxis("lineWidth" , 2);
		
		JsonObjects plotLines = new JsonObjects();
		JsonObject zeroPlotLine = new JsonObject();
		zeroPlotLine.put("value", 0);
		zeroPlotLine.put("width",1);
		zeroPlotLine.put("color", "#808080");
		
		plotLines.add(zeroPlotLine);
		
		addPropTooltip("valueSuffix", "");

		addPropLegend("align", "right");
		addPropLegend("verticalAlign", "top");
		addPropLegend("y", 70);
		addPropLegend("layout", "vertical");
		addPropLegend("borderWidth", 0);
		
		EasySerie s1 = new EasySerie("Serie 1");
		s1.addValor(Math.random() * 100)
			.addValor(Math.random()* 100)
			.addValor(Math.random()* 100)
			.addValor(Math.random()* 100);
		
		super.series.add(s1);
		
		EasySerie s2 = new EasySerie("Serie 2");
		s2.addValor(Math.random()* 100)
		.addValor(Math.random()* 100)
		.addValor(Math.random()* 100)
		.addValor(Math.random()* 100);
		super.series.add(s2);
		
		EasySerieY sy1 = new EasySerieY();
		sy1.addValor(Math.random() * 100)
			.addValor(Math.random()* 100)
			.addValor(Math.random()* 100)
			.addValor(Math.random()* 100);
		super.yAxis.add(sy1);
		
		EasySerieY sy2 = new EasySerieY();
		sy2.addValor(Math.random()* 100)
		.addValor(Math.random()* 100)
		.addValor(Math.random()* 100)
		.addValor(Math.random()* 100);
		super.yAxis.add(sy2);
		
	}

	@Override
	protected int getMinCategorias() {
		return 1;
	}
	 
	
 
	
}
