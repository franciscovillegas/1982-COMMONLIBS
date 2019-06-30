package cl.ejedigital.tool.log;


public interface ILog {
		
	public void debug(String msg);
	
	public void info(String msg);
	
	public void warn(String msg);
	
	public void error(String msg);
	
	public void fatal(String msg);
}
