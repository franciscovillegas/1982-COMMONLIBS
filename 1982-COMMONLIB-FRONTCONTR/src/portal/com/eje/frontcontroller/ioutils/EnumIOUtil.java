package portal.com.eje.frontcontroller.ioutils;

public enum EnumIOUtil {
	BOLSA_DE_GATOS(IOUtilBolsaDeGatos.class),
	CONNECTION(IOUtilConnection.class),
	CSV(IOUtilCsv.class),
	DOMAIN(IOUtilDomain.class),
	EXCEL(IOUtilExcel.class),
	FILE(IOUtilFile.class),
	FREEMARKER(IOUtilFreemarker.class),
	HTML(IOUtilHtmlMessage.class),
	IMAGE(IOUtilImage.class),
	JSON(IOUtilJson.class),
	PARAM(IOUtilParam.class),
	SENCHA(IOUtilSencha.class),
	SENCHA_TREE(IOUtilSenchaTree.class),
	TRACKING(IOUtilTracking.class),
	XML(IOUtilXml.class);
	
	
	private Class clazz;
	
	EnumIOUtil(Class clazz) {
		this.clazz = clazz;
	}
	
	public Class getClazz() {
		return this.clazz;
	}
}
