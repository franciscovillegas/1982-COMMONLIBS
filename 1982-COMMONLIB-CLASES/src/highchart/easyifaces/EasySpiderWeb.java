package highchart.easyifaces;

import highchart.easyifaces.component.EasySerie;
import cl.ejedigital.consultor.def.JsonObjects;


public final class EasySpiderWeb extends EasyGraph {
	
	
 
	@Override
	protected void iniciaGrafico() {
		super.chart.put("polar", true);
		super.chart.put("type" , "line");
		
		addPropTitle("text", "Titulo:" + String.valueOf(this.getClass()));
		addPropTitle("x", -80);
		
		addPropSubtitle("text", "Subtitulo:" + String.valueOf(this.getClass()));
		addPropSubtitle("x", -80);
		
		addPropPane("size", "100%");
		
		JsonObjects categorias = new JsonObjects();
		categorias.add("Sales")
				  .add("Marketing")
				  .add("Development")
				  .add("Customer Support");
				  
		addPropXAxis("categories", categorias);
		addPropXAxis("tickmarkPlacement", "on");
		addPropXAxis("lineWidth" , 0);
		
		addPropYAxis("gridLineInterpolation", "polygon");
		addPropYAxis("lineWidth", 0);
		addPropYAxis("min", 0);
		
		addPropTooltip("share", true);
		addPropTooltip("pointFormat", "<span style=\"color:{series.color}\">{series.name}: <b>{point.y:,.0f}</b><br/>");
		
      	 
		addPropLegend("align", "right");
		addPropLegend("verticalAlign", "top");
		addPropLegend("y", 70);
		addPropLegend("layout", "vertical");
		
		
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
		
	}

	@Override
	protected int getMinCategorias() {
		return 3;
	}
	 
	
 
	
}
