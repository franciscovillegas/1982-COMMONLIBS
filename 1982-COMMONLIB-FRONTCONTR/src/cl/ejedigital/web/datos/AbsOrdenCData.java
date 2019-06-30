package cl.ejedigital.web.datos;

import java.text.ParseException;
import java.text.RuleBasedCollator;
import java.util.Date;

import org.apache.commons.lang.NotImplementedException;

import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.tool.validar.Validar;
import portal.com.eje.portal.vo.ClaseConversor;

public class AbsOrdenCData {
	protected String colName;
	private Order order;
	private RuleBasedCollator spCollator;
	
	protected AbsOrdenCData(String colName, Order orden) {
		this.colName = colName;
		this.order = orden;
		
			String smallnTilde = new String("\u00F1");    // ñ
			String capitalNTilde = new String("\u00D1");  // Ñ

			String traditionalSpanishRules = (
			    "< a,A < b,B < c,C " +
			    "< ch, cH, Ch, CH " +
			    "< d,D < e,E < f,F " +
			    "< g,G < h,H < i,I < j,J < k,K < l,L " +
			    "< ll, lL, Ll, LL " +
			    "< m,M < n,N " +
			    "< " + smallnTilde + "," + capitalNTilde + " " +
			    "< o,O < p,P < q,Q < r,R " +
			    "< s,S < t,T < u,U < v,V < w,W < x,X " +
			    "< y,Y < z,Z");
			
				try {
					spCollator= new RuleBasedCollator(traditionalSpanishRules);
					
				} catch (ParseException e) {
					e.printStackTrace();
				}
		
	}
	
	public int absCompare(Object o1, Object o2) {
		double num = 0;
		Object objeto1 = getObjectToCompare(o1);
		Object objeto2 = getObjectToCompare(o2);
		
		if( objeto1 != null 			&& objeto2 != null ) {
			
					
			if( objeto1 instanceof Integer 
					|| objeto1 instanceof Float
					|| objeto1 instanceof Double
					|| objeto1 instanceof Long
					|| objeto1 instanceof Byte 
					|| objeto1 instanceof Short) {
				
				double uno = ClaseConversor.getInstance().getObject( objeto1, Double.class);
				double dos = ClaseConversor.getInstance().getObject( objeto2, Double.class);
				
				num =  uno - dos;
			}
			else if(objeto1 instanceof Date) {
				long time1 = 0;
				long time2 = 0;
				
				if(objeto1 != null) {
					time1 = ((Date)objeto1).getTime();
				}
				
				if(objeto2 != null) {
					time2 = ((Date)objeto2).getTime();
				}
				num =  time1 - time2 ;
			}
			else if(objeto1 instanceof String) {
				String do1 = Validar.getInstance().validarDato(objeto1,"");
				String do2 = Validar.getInstance().validarDato(objeto2,"");
				num = spCollator.compare( do1, do2 );
			}
			else {
				String do1 = Validar.getInstance().validarDato(objeto1,"");
				String do2 = Validar.getInstance().validarDato(objeto2,"");
				num = spCollator.compare( do1, do2 );
			}				
		}
	
		if(this.order == Order.Descendente) {
			num = num * -1;
		}
		
		return num < 0 ? -1 : (num > 0 ? 1 : 0);
		
	}

	protected Object getObjectToCompare(Object o1) {
		throw new NotImplementedException();
	}

	
}
