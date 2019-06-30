package portal.com.eje.tools;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cl.ejedigital.tool.misc.Cronometro;

public class RegistraTimes {
	private Cronometro cro;
	private Map<String, List<Double>> times;
	private double lastMilli;
	
	public RegistraTimes() {
		this.cro = new Cronometro();
		this.times = new LinkedHashMap<String, List<Double>>();
		
		this.cro.start();
	}
		
	public void registraTime(String key) {
		if(times.get(key) == null) {
			times.put(key, new ArrayList<Double>());
		}
		
		times.get(key).add(cro.GetMilliseconds() - lastMilli );
		
		lastMilli = cro.GetMilliseconds();
	}
	
	public void printStats(PrintStream out) {
		StringBuilder str = new StringBuilder();
		Set<String> sets = times.keySet();
		
		for(String s : sets) {
			str.append(s);
			str.append("\tmin\t").append(min(times.get(s)));
			str.append("\tmax\t").append(max(times.get(s)));
			str.append("\tX\t").append(promedio(times.get(s)));
			str.append("\n");
		}
		
		out.println(str.toString());
	}
	
	private int min(List<Double> lista) {
		Double min = 100D;
		for(Double d : lista) {
			if(d < min) {
				min = d;
			}
		}
		
		return min.intValue();
	}
	
	private int max(List<Double> lista) {
		Double max = 0D;
		for(Double d : lista) {
			if(d > max) {
				max = d;
			}
		}
		
		return max.intValue();
	}
	
	private int promedio(List<Double> lista) {
		Double total = 0D;
		for(Double d : lista) {
			total+=d;
		}
		
		return new Double(total/lista.size()).intValue();
	}
	
	public Double getMilliseconds() {
		return cro.GetMilliseconds();
	}
}