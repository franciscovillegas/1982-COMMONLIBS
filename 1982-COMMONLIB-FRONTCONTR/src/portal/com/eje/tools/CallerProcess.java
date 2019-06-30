package portal.com.eje.tools;

import java.lang.reflect.InvocationTargetException;

import org.quartz.InterruptableJob;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.UnableToInterruptJobException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import portal.com.eje.portal.factory.Util;


public class CallerProcess implements Job , InterruptableJob {
	private JobExecutionContext context;
	
	public CallerProcess() {
		
	}
	
	public void execute(JobExecutionContext context) throws org.quartz.JobExecutionException {
		this.context = context;
		
		try {
			XStream xstream = new XStream(new DomDriver());
			JobDataMap dataMap = context.getJobDetail().getJobDataMap();

			String pathClass 		= dataMap.getString("pathClass");
			String metodoName 		= dataMap.getString("metodoName");
			Class[] paramDef 		= (Class[])  xstream.fromXML( dataMap.getString("paramDef") );
			Object[] paramValues 	= (Object[]) xstream.fromXML( dataMap.getString("paramValues") );
			
			
			ClaseGenerica cg = Util.getInstance(ClaseGenerica.class);
			Class[] defConstructor = {};
			Class[] valConstructor = {};
 
			Object o = cg.getNew(pathClass,defConstructor,valConstructor);
			cg.ejecutaMetodo(o, metodoName,paramDef,paramValues);
		}

		catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void interrupt() throws UnableToInterruptJobException {
		// TODO Auto-generated method stub
	 
	}
 
}