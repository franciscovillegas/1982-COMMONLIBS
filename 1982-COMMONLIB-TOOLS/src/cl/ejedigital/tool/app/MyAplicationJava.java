package cl.ejedigital.tool.app;

import cl.ejedigital.ExceptionNotImplemented;

public class MyAplicationJava implements IMyAplication {

	MyAplicationJava() {

	} 

	public String getPathProyecto() { 
		throw new ExceptionNotImplemented(); 
	}

	public String getPathClasses() {
		throw new ExceptionNotImplemented();
	}

	public String getPathProyecto(String relative) {
		throw new ExceptionNotImplemented();
	}

	public String getPathClasses(String relative) {
		throw new ExceptionNotImplemented();
	}

}
