package cl.eje.helper;

import portal.com.eje.frontcontroller.IOClaseWeb;

public interface IZoneUtil extends IInterface {

	public void get(IOClaseWeb io) throws Throwable;

	public void upd(IOClaseWeb io) throws Throwable;

	public void add(IOClaseWeb io) throws Throwable;

	public void del(IOClaseWeb io) throws Throwable;
}
