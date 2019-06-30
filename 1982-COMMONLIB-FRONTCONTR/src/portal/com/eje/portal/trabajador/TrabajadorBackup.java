package portal.com.eje.portal.trabajador;

import java.util.ArrayList;
import java.util.List;

import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.trabajador.ifaces.ITrabajadorBackup;

public class TrabajadorBackup implements ITrabajadorBackup {

	public static ITrabajadorBackup getInstance() {
		return Util.getInstance(TrabajadorBackup.class);
	}
	
	@Override
	public boolean toHistory(List<Integer> listaRuts) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean toHistory(Integer rut) {
		List<Integer> ruts = new ArrayList<Integer>();
		ruts.add(rut);
		
		return toHistory(ruts);
	}
	
}
