package portal.com.eje.daemon;

import java.util.Date;

import cl.ejedigital.tool.misc.Cronometro;

public class PPMADaemon {
	public Class classDaemon;
	public Date initDate;
	public Date endDate;
	private Cronometro cro;
	
	public PPMADaemon() {
		cro = new Cronometro();
	}
	
	public Date getInitDate() {
		return initDate;
	}
	public void setInitDate(Date initDate) {
		this.initDate = initDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Cronometro getCro() {
		return cro;
	}

	public void setCro(Cronometro cro) {
		this.cro = cro;
	}

	public Class getClassDaemon() {
		return classDaemon;
	}

	public void setClassDaemon(Class classDaemon) {
		this.classDaemon = classDaemon;
	}
 
	
}
