package highchart.easyifaces;

import highchart.easyifaces.component.EasySerie;
import cl.ejedigital.consultor.def.JsonObject;
import cl.ejedigital.consultor.def.JsonObjects;
import cl.ejedigital.consultor.output.ReservedWord;
import cl.ejedigital.tool.misc.Formatear;
import cl.ejedigital.tool.strings.MyString;
import cl.ejedigital.tool.validar.Validar;


public final class EasyBasicPie extends EasyGraph {
	
	
 
	@Override
	protected void iniciaGrafico() {
		super.chart.put("plotBackgroundColor", null);
		super.chart.put("plotBorderWidth" , null);
		super.chart.put("plotShadow" , false);
		super.chart.put("type" , "pie");
 
		addPropTitle("text", "Titulo:" + String.valueOf(this.getClass()));
		addPropTitle("x", -80);
		
		JsonObjects categorias = new JsonObjects();
		categorias.add("Categoria 1")
				  .add("Categoria 2")
				  .add("Categoria 3")
				  .add("Categoria 4");
		
		
		addPropSubtitle("text", "Subtitulo:" + String.valueOf(this.getClass()));
		addPropSubtitle("x", -80);
		
		addPropPane("size", "150%");
		
 
		addPropTooltip("pointFormat", "{series.name}: <b>{point.percentage:.1f}%</b>");
		
	 
		 
		
		JsonObject dataLabels = new JsonObject();
		dataLabels.put("enabled", true);
		dataLabels.put("format", "<b>{point.name}</b>: {point.percentage:.1f} %");
		dataLabels.put("enabled", true);
		 
           
		
		JsonObject pie = new JsonObject();
		pie.put("allowPointSelect", true);
		pie.put("cursor", "pointer");
		pie.put("dataLabels", dataLabels);
		addPropPlotOptions("pie", pie);
		
		
		MyString my = new MyString();
		
		EasySerie s1 = new EasySerie("Serie 1");
		s1.put("colorByPoint", true);
		
		for(int i=1; i<=4;i++) {
			JsonObject slice1 = new JsonObject();
			slice1.put("y", 25);
			slice1.put("name", my.getRandomString("randomString", 20));
			
			if(i== 1) {
				slice1.put("sliced", true);
				slice1.put("selected", true);
			}
			
			s1.addValor(slice1);
		}
		
		 
		
		super.series.add(s1);
		
		
	}

	@Override
	protected int getMinCategorias() {
		return 0;
	}
	 
	
 
	
}
